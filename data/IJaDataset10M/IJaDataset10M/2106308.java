package au.edu.diasb.annotation.danno.oai;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import ORG.oclc.oai.util.OAIUtil;
import au.edu.diasb.annotation.danno.query.InvalidQueryParametersException;
import au.edu.diasb.annotation.danno.query.QueryDescriptor;

/**
 * An OAISetMapper object maps from OAI set names to the corresponding Danno query.
 * 
 * @author scrawley
 */
public class OAISetMapper {

    /**
     * This class wraps the Iterator<String> for the OAI set xml map, keeping
     * track of how many Strings have been pulled from the iterator.  When
     * we reach 'batchSize' we stop.  Calling 'resetCounter' allows us to resume.
     */
    public static class OAISetIterator implements Iterator<String> {

        private final Iterator<String> it;

        private int count, batchCount;

        private final int size;

        private final int batchSize;

        public OAISetIterator(Iterator<String> it, int size, int batchSize) {
            super();
            this.it = it;
            this.size = size;
            this.batchSize = batchSize;
        }

        @Override
        public boolean hasNext() {
            return (batchCount < batchSize) && it.hasNext();
        }

        @Override
        public String next() {
            String res = it.next();
            batchCount++;
            return res;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove not supported");
        }

        /**
         * Start a new batch
         */
        public void reset() {
            batchCount = 0;
        }

        /**
         * Test if we can return the remaining contents of the iterator in the current batch
         * 
         * @return {@code true} if another batch will be needed.
         */
        public boolean continuationNeeded() {
            return count + batchSize - batchCount < size;
        }
    }

    private static final Pattern OAI_SET_NAME_PAT = Pattern.compile("[\\w.\\-_!~*'()]++(:[\\w.\\-_!~*'()]++)*");

    private Map<String, QueryDescriptor> queryMap = new HashMap<String, QueryDescriptor>();

    private Map<String, String> xmlMap = new TreeMap<String, String>();

    private int batchSize = 1000;

    public OAISetMapper() {
        super();
    }

    public void setBatchSize(int batchSize) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("batchSize must be > 0");
        }
        this.batchSize = batchSize;
    }

    /**
     * Inject the mapper's mappings.  The mappings are supplied in the form of a list of
     * setDescriptor strings.  These consisting of 3 or 4 fields separated by a '|' character
     * as follows:
     * <ol>
     * <li>An OAI setSpec conforming to the syntax in the OAI-PMH specification.
     * <li>A short human readable name for the set.
     * <li>The Danno query that defines the OAI set's membership.
     * <li>A longer description for the set (optional).
     * </ol>
     * Any whitespace around a field will be trimmed.
     * 
     * @param setDescriptors the mappings as a list of setDescriptor strings.
     * @throws InvalidQueryParametersException 
     */
    public void setMappings(List<String> setDescriptors) throws InvalidOAISetException {
        queryMap.clear();
        xmlMap.clear();
        for (String sd : setDescriptors) {
            String[] parts = sd.split("\\|", 4);
            if (parts.length < 3) {
                throw new InvalidOAISetException("OAI set descriptor has too few parts");
            }
            String setSpec = parts[0].trim();
            if (!OAI_SET_NAME_PAT.matcher(setSpec).matches()) {
                throw new InvalidOAISetException("OAI set spec '" + setSpec + "' is invalid");
            }
            String setName = parts[1].trim();
            if (setName.length() == 0) {
                throw new InvalidOAISetException("OAI set name is empty");
            }
            String query = parts[2].trim();
            if (query.length() == 0) {
                throw new InvalidOAISetException("OAI set query is empty");
            }
            String description = parts.length == 4 ? parts[3].trim() : "";
            if (queryMap.get(setSpec) != null) {
                throw new InvalidOAISetException("OAI set spec '" + setSpec + "' is multiply defined");
            }
            try {
                queryMap.put(setSpec, createOAISetQuery(query));
            } catch (InvalidQueryParametersException ex) {
                throw new InvalidOAISetException("OAI set query '" + query + "' is invalid: " + ex.getMessage(), ex);
            }
            xmlMap.put(setSpec, buildXmlSet(setSpec, setName, description));
        }
    }

    /**
     * This method uses {@link QueryDescriptor#parseQuery(String)} to create a 
     * QueryDescriptor, then rewrites the primary query as an 'unspecified' query
     * with an additional restriction.
     * 
     * @param query the query String
     * @return the base query to use in ListIdentifiers or ListRecords.
     * @throws InvalidQueryParametersException
     */
    private QueryDescriptor createOAISetQuery(String query) throws InvalidQueryParametersException {
        QueryDescriptor qd = QueryDescriptor.parseQuery(query);
        switch(qd.getType()) {
            case QueryDescriptor.QT_ANNOTATES:
                qd.getRestrictions().add(QueryDescriptor.RT_ANNOTATES, qd.getValue());
                break;
            case QueryDescriptor.QT_REPLY_TREE:
                qd.getRestrictions().add(QueryDescriptor.RT_REPLY_TREE, qd.getValue());
                break;
            default:
                throw new InvalidQueryParametersException("This primary query type is not allowed here");
        }
        qd.setType(QueryDescriptor.QT_UNSPECIFIED);
        qd.setValue("?");
        return qd;
    }

    private String buildXmlSet(String setSpec, String setName, String description) {
        StringBuilder sb = new StringBuilder(100);
        sb.append("<set><setSpec>").append(OAIUtil.xmlEncode(setSpec)).append("</setSpec>");
        sb.append("<setName>").append(OAIUtil.xmlEncode(setName)).append("</setName>");
        if (description.length() > 0) {
            sb.append("<setDescription>");
            sb.append("<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\"");
            sb.append(" xmlns:dc=\"http://purl.org/dc/elements/1.1/\"");
            sb.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
            sb.append(" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/");
            sb.append(" http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">");
            sb.append("<dc:description>").append(OAIUtil.xmlEncode(description)).append("</dc:description>");
            sb.append("</oai_dc:dc></setDescription>");
        }
        sb.append("</set>\n");
        return sb.toString();
    }

    /**
     * Get the query corresponding to a set name.  The result will be a copy that the
     * caller is free to change.
     * 
     * @param setName an OAI set name
     * @return the corresponding query, or {@code null} is the set name is unknown.
     */
    public QueryDescriptor getQuery(String setName) {
        QueryDescriptor qd = queryMap.get(setName);
        return qd == null ? null : new QueryDescriptor(qd);
    }

    public OAISetIterator getXmlIterator() {
        return new OAISetIterator(xmlMap.values().iterator(), xmlMap.size(), batchSize);
    }
}
