package org.phenoscape.tto;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Collection;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.obo.util.TermUtil;
import org.obo.datamodel.Dbxref;
import org.obo.datamodel.OBOClass;
import org.obo.datamodel.OBOSession;
import org.obo.datamodel.ObjectFactory;
import org.obo.datamodel.Synonym;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GNIMerger {

    private static final String GNIURLPREFIX = "http://gni.globalnames.org/name_strings.json?search_term=";

    private static final String GNIDETAILSPREFIX = "http://gni.globalnames.org/name_strings/";

    private static final String NAMEKEY = "name";

    private static final String NAMESTRINGSKEY = "name_strings";

    private static final String LSIDKEY = "lsid";

    private static final String DATAKEY = "data";

    private static final String NAMESCOUNTKEY = "name_strings_total";

    private static final String DATASOURCEKEY = "data_source";

    private static final String WEBKEY = "web_site_url";

    private static final String CASURL = "http://research.calacademy.org/ichthyology/catalog";

    private static final String SPACEEXP = " ";

    private static final int LIMIT = 5000;

    static Logger logger = Logger.getLogger(GNIMerger.class.getName());

    public void merge(OBOSession theSession) {
        logger.setLevel(Level.INFO);
        final Collection<OBOClass> terms = TermUtil.getTerms(theSession);
        logger.info("Term count is " + terms.size());
        final ObjectFactory oboFactory = theSession.getObjectFactory();
        final GNINames names = new GNINames();
        int count = 0;
        for (OBOClass term : terms) {
            String[] components = term.getName().split(SPACEEXP);
            if (components.length >= 2 && !hasGNIref(term)) {
                names.binomialName = term.getName();
                names.binomialLSID = null;
                names.cofUsage = null;
                names.cofUsageLSID = null;
                String GNIQuery = GNIURLPREFIX + components[0] + "+" + components[1];
                try {
                    URL gnirequest = new URL(GNIQuery);
                    InputStream gniStream = gnirequest.openStream();
                    Reader gniReader = new InputStreamReader(gniStream);
                    count++;
                    logger.info("Query is " + GNIQuery);
                    processJSON(gniReader, names);
                    if (names.binomialLSID != null) {
                        final int binomialcolonpos = names.binomialLSID.indexOf(':');
                        if (binomialcolonpos != -1) term.addDbxref(oboFactory.createDbxref(names.binomialLSID.substring(0, binomialcolonpos), names.binomialLSID.substring(binomialcolonpos + 1), "", Dbxref.RELATED_SYNONYM, null));
                        if (names.cofUsageLSID != null) {
                            final int cofColonpos = names.cofUsageLSID.indexOf(':');
                            if (cofColonpos != -1) {
                                Synonym s = oboFactory.createSynonym(names.cofUsage, Synonym.NARROW_SYNONYM);
                                Dbxref d = (Dbxref) oboFactory.createDbxref(names.cofUsageLSID.substring(0, cofColonpos), names.cofUsageLSID.substring(cofColonpos + 1), "", Dbxref.RELATED_SYNONYM, null);
                                s.addXref(d);
                                term.addSynonym(s);
                            }
                        }
                    }
                } catch (IOException e) {
                    logger.error("Could not check " + GNIURLPREFIX + " because " + e.getMessage());
                }
                logger.info("Waiting 1.6 seconds");
                try {
                    Thread.sleep(1600);
                } catch (InterruptedException e) {
                    logger.info("Got an interrupted exception");
                }
                logger.info("Done");
            }
            if (count > LIMIT) return;
        }
    }

    /**
	 * This check if the term has any xrefs to GNI.  If there are, this term can be skipped.
	 * @param term OBO term to check for GNI xrefs
	 * @return true if term has GNI xrefs
	 */
    private boolean hasGNIref(OBOClass term) {
        if (!term.getDbxrefs().isEmpty()) {
            for (Dbxref ref : term.getDbxrefs()) {
                if ("urn".equals(ref.getDatabase()) && ref.getDatabaseID().contains("globalnames.org")) {
                    logger.warn("Term " + term.getID() + " already has GNI xref in TTO: urn:" + ref.getDatabaseID());
                    return true;
                }
            }
            return false;
        } else return false;
    }

    void processJSON(Reader gniReader, GNINames results) {
        final JsonParser parser = new JsonParser();
        final JsonElement jroot = parser.parse(gniReader);
        if (jroot.getAsJsonObject().get(NAMESCOUNTKEY).getAsInt() > 0) {
            final JsonElement nameStringsElement = jroot.getAsJsonObject().get(NAMESTRINGSKEY);
            if (nameStringsElement.isJsonArray()) {
                for (JsonElement nameElement : nameStringsElement.getAsJsonArray()) {
                    String childName = nameElement.getAsJsonObject().get(NAMEKEY).getAsString();
                    String id = nameElement.getAsJsonObject().get("id").getAsString();
                    logger.info("Query name is " + results.binomialName + ";Name is " + childName + " id is " + id);
                    if (results.binomialName.equals(childName)) {
                        JsonElement lsidElement = nameElement.getAsJsonObject().get(LSIDKEY);
                        results.binomialLSID = lsidElement.getAsString();
                        logger.info("Hit raw name: " + childName + "; lsid = " + results.binomialLSID);
                    }
                    JsonElement details = getDetails(id);
                    if (details != null) {
                        JsonElement detailsElement = nameElement.getAsJsonObject().get(LSIDKEY);
                        results.cofUsage = childName;
                        results.cofUsageLSID = detailsElement.getAsString();
                        logger.info("Hit CoF name: " + results.cofUsage + "; CoF lsid = " + results.cofUsageLSID);
                        return;
                    }
                }
            }
        } else {
            logger.info("No matches found");
            return;
        }
    }

    JsonElement getDetails(String id) {
        final String requestURL = GNIDETAILSPREFIX + id + ".json";
        final JsonParser parser = new JsonParser();
        try {
            final InputStream gniStream = new URL(requestURL).openStream();
            final Reader gniReader = new InputStreamReader(gniStream);
            final JsonElement detailsRoot = parser.parse(gniReader);
            final JsonElement result = processDetails(detailsRoot);
            return result;
        } catch (IOException e) {
            logger.error("Could not check " + requestURL + " because " + e.getMessage());
            return null;
        }
    }

    private JsonElement processDetails(JsonElement e) {
        final JsonArray dataArray = e.getAsJsonObject().get(DATAKEY).getAsJsonArray();
        for (JsonElement ele : dataArray) {
            JsonObject datum = ele.getAsJsonObject();
            if (processDatum(datum)) {
                return e;
            }
        }
        return null;
    }

    private boolean processDatum(JsonElement datum) {
        if (datum != null) {
            final JsonElement dataSourceElement = datum.getAsJsonObject().get(DATASOURCEKEY);
            if (dataSourceElement != null) {
                final String webSite = dataSourceElement.getAsJsonObject().get(WEBKEY).getAsString();
                return CASURL.equals(webSite);
            } else {
                logger.error("Null datum in " + datum);
                return false;
            }
        } else {
            logger.error("Null datum in json data array");
            return false;
        }
    }

    static class GNINames {

        String binomialName;

        String binomialLSID = null;

        String cofUsage = null;

        String cofUsageLSID = null;
    }
}
