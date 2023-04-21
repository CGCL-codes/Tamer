package au.edu.educationau.opensource.dsm.adapters.contenthandler;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import au.edu.educationau.opensource.dsm.adapters.SearchAdapterProperties;
import au.edu.educationau.opensource.dsm.obj.ResultCategory;
import au.edu.educationau.opensource.dsm.obj.ResultMetadata;
import au.edu.educationau.opensource.dsm.obj.SearchCriteria;
import au.edu.educationau.opensource.dsm.obj.SearchResult;
import au.edu.educationau.opensource.dsm.worker.ResultSetUnifier;

public class SRWContentHandler extends DefaultHandler {

    private SearchResult result = new SearchResult();

    private ResultCategory cat = new ResultCategory();

    private ArrayList results = new ArrayList();

    private ArrayList categories = new ArrayList(5);

    private ArrayList metadata = new ArrayList(5);

    private StringBuffer titleBuffer = new StringBuffer();

    private StringBuffer descriptionBuffer = new StringBuffer();

    private StringBuffer identifierBuffer = new StringBuffer();

    private StringBuffer categoryNameBuffer = new StringBuffer();

    private short count = 0;

    private short found = 0;

    private short nextRecordPosition = 0;

    private String currentElement = "";

    private String previousElement = "";

    private SearchAdapterProperties props = new SearchAdapterProperties();

    private SearchCriteria criteria = new SearchCriteria();

    private String resultSetID = "";

    private String diagnosticCode = "";

    /***/
    public short getFound() {
        return found;
    }

    /***/
    public short getNextRecordPosition() {
        return nextRecordPosition;
    }

    /***/
    public String getResultSetID() {
        return resultSetID;
    }

    /***/
    public short getCount() {
        return (short) results.size();
    }

    /***/
    public ArrayList getResults() {
        return results;
    }

    /***/
    public String getDiagnosticCode() {
        return diagnosticCode;
    }

    /**
	 * @param props
	 */
    public void setSearchAdapterProperties(SearchAdapterProperties props) {
        this.props = props;
    }

    /**
	 * @param criteria
	 */
    public void setSearchCriteria(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    boolean done = false;

    /**
	 * @param ch
	 * @param ch_start
	 * @param ch_length
	 * @exception SAXException
	 */
    public void characters(char[] ch, int ch_start, int ch_length) throws SAXException {
        String val = String.copyValueOf(ch, ch_start, ch_length);
        if (currentElement.equals("nextRecordPosition")) {
            try {
                this.nextRecordPosition = Short.parseShort(val);
            } catch (NumberFormatException oe) {
            }
        } else if (currentElement.equals("numberOfRecords")) {
            try {
                this.found = Short.parseShort(val);
            } catch (NumberFormatException oe) {
            }
        } else if (currentElement.equals("resultSetId")) {
            this.resultSetID = val;
        } else if (currentElement.equals("identifier")) {
            identifierBuffer.append(val);
        } else if (currentElement.equals("relevance")) {
            float relevance = 0f;
            try {
                relevance = Float.parseFloat(val);
            } catch (NumberFormatException oe) {
            }
            this.result.setRelevance(ResultSetUnifier.normaliseRelevance(relevance, props.getRelevanceMultiplier(), props.getRelevanceOffset()));
        } else if (currentElement.equals("title")) {
            titleBuffer.append(val);
        } else if (currentElement.equals("description")) {
            descriptionBuffer.append(val);
        } else if (currentElement.equals("date")) {
            result.setDate(val);
        } else if (currentElement.equals("uri")) {
            this.diagnosticCode = val;
        } else if (currentElement.equals("")) {
            if (previousElement.equals("description")) {
                descriptionBuffer.append(val);
            } else if (previousElement.equals("title")) {
                titleBuffer.append(val);
            } else if (previousElement.equals("identifier")) {
                identifierBuffer.append(val);
            }
        }
        if (!currentElement.equals("")) {
            previousElement = currentElement;
        }
        currentElement = "";
    }

    /**
	 * @param ch
	 * @param ch_start
	 * @param ch_length
	 * @exception SAXException
	 */
    public void ignorableWhitespace(char[] ch, int ch_start, int ch_length) throws SAXException {
    }

    /**
	 * @param uri
	 * @param localName
	 * @param qName
	 * @exception SAXException
	 */
    public void endElement(java.lang.String uri, java.lang.String localName, java.lang.String qName) throws SAXException {
        if (localName.equals("record")) {
            result.setDescription(descriptionBuffer.toString().trim());
            result.setLink(identifierBuffer.toString().trim());
            result.setTitle(titleBuffer.toString().trim());
            result.setSource(props.getAdapterCode());
            result.setCollectionName(props.getDisplayTitle());
            metadata.trimToSize();
            ResultMetadata[] metadataArray = new ResultMetadata[metadata.size()];
            result.setResultMetadata((ResultMetadata[]) metadata.toArray(metadataArray));
            float combinedHitFrequency = ResultSetUnifier.getHitFrequency(criteria.getQueryArray(), result.getTitle(), result.getDescription(), criteria.getKeywordConstraint(), criteria.isCaseSensitive());
            result.setHitFrequency(combinedHitFrequency);
            results.add(result);
        }
    }

    /**
	 * @param target
	 * @param data
	 * @exception SAXException
	 */
    public void processingInstruction(String target, String data) throws SAXException {
    }

    /**
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param attr
	 * @exception SAXException
	 */
    public void startElement(java.lang.String uri, java.lang.String localName, java.lang.String qName, Attributes attr) throws SAXException {
        this.currentElement = localName;
        if (localName.equals("record")) {
            result = new SearchResult();
            titleBuffer = new StringBuffer();
            descriptionBuffer = new StringBuffer();
            identifierBuffer = new StringBuffer();
            metadata = new ArrayList(5);
        }
    }
}
