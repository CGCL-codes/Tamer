package org.eun.oai.server.catalog;

import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.document.Document;
import org.eun.ilox.oai.indexer.IloxException;
import org.eun.ilox.oai.indexer.LuceneConfiguration;
import org.eun.oai.server.util.IloxCatalogUtil;
import ORG.oclc.oai.server.catalog.RecordFactory;
import ORG.oclc.oai.server.verb.CannotDisseminateFormatException;

public class IloxRecordFactory extends RecordFactory {

    private IloxCatalogUtil crosswalkUtil;

    private String repositoryIdentifier = null;

    public IloxRecordFactory(Properties properties) {
        super(properties);
        try {
            crosswalkUtil = IloxCatalogUtil.getInstance(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String classname = "IloxRecordFactory";
        repositoryIdentifier = properties.getProperty(classname + ".repositoryIdentifier");
        if (repositoryIdentifier == null) {
            throw new IllegalArgumentException(classname + ".repositoryIdentifier is missing from the properties file");
        }
        Logger.getAnonymousLogger().log(Level.INFO, "An IloxRecordFactory object is created!");
        Logger.getAnonymousLogger().log(Level.INFO, "repositoryIdentifier: " + repositoryIdentifier);
    }

    /**
     * Extract the local identifier from the native item
     *
     * @param nativeItem native Item object
     * @return local identifier
     */
    public String getLocalIdentifier(Object nativeItem) {
        Document doc = crosswalkUtil.getDoc(nativeItem);
        return doc.getField(LuceneConfiguration.LUCENE_IDENTIFIER).stringValue();
    }

    @Override
    public String fromOAIIdentifier(String identifier) {
        try {
            StringTokenizer tokenizer = new StringTokenizer(identifier, ":");
            tokenizer.nextToken();
            tokenizer.nextToken();
            String result = tokenizer.nextToken();
            while (tokenizer.hasMoreElements()) {
                result = result.concat(":" + tokenizer.nextToken());
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Iterator getAbouts(Object arg0) {
        return null;
    }

    /**
     * get the datestamp from the item
     *
     * @param nativeItem a native item presumably containing a datestamp somewhere
     * @return a String containing the datestamp for the item
     * @exception IllegalArgumentException Something is wrong with the argument.
     */
    @Override
    public String getDatestamp(Object nativeItem) {
        Document doc = crosswalkUtil.getDoc(nativeItem);
        String date = doc.getField(LuceneConfiguration.LUCENE_DATE).stringValue();
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6, 8);
        String hour = date.substring(8, 10);
        String minute = date.substring(10, 12);
        String second = date.substring(12, 14);
        return year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":" + second + "Z";
    }

    @Override
    public String getOAIIdentifier(Object nativeItem) {
        StringBuffer sb = new StringBuffer();
        sb.append("oai:");
        sb.append(repositoryIdentifier);
        sb.append(":");
        sb.append(getLocalIdentifier(nativeItem));
        return sb.toString();
    }

    @Override
    public Iterator getSetSpecs(Object arg0) throws IllegalArgumentException {
        return null;
    }

    @Override
    public boolean isDeleted(Object arg0) {
        return false;
    }

    @Override
    public String quickCreate(Object nativeItem, String schemaLocation, String metadataPrefix) throws IllegalArgumentException, CannotDisseminateFormatException {
        try {
            Document doc = crosswalkUtil.getDoc(nativeItem);
            String result = doc.get(LuceneConfiguration.LUCENE_CONTENT);
            if (result.startsWith("<?")) {
                int offset = result.indexOf("?>");
                result = result.substring(offset + 2);
            }
            return putInRecord(result, nativeItem);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
	 * Support for quickCreate() function
	 * Wrap the metadata in a record
	 * @param metadata
	 * @param document
	 * @return
	 */
    private String putInRecord(String metadata, Object nativeItem) {
        String record = "<record>";
        record = record + "\n" + "<header>";
        record = record + "\n" + "<identifier>";
        record = record + getOAIIdentifier(nativeItem);
        record = record + "</identifier>";
        record = record + "\n" + "<datestamp>";
        record = record + getDatestamp(nativeItem);
        record = record + "</datestamp>";
        record = record + "\n" + "</header>";
        record = record + "\n" + "<metadata>";
        record = record + "\n" + metadata;
        record = record + "\n" + "</metadata>";
        record = record + "\n" + "</record>";
        return record;
    }
}
