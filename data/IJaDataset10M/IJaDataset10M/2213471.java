package org.wijiscommons.ssaf.process.solr.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.w3c.dom.Document;
import org.wijiscommons.ssaf.process.solr.SSAFIndex;
import org.wijiscommons.ssaf.process.solr.SSAFSolrErrorCodes;
import org.wijiscommons.ssaf.process.solr.SSAFSolrException;
import org.wijiscommons.ssaf.process.solr.SSAFSolrFieldTypeEnum;
import org.wijiscommons.ssaf.process.solr.jaxb.JaxbFactory;
import org.wijiscommons.ssaf.schema.search.DataType;
import org.wijiscommons.ssaf.schema.search.Pointer;
import org.wijiscommons.ssaf.schema.search.PointerItem;
import org.wijiscommons.ssaf.schema.search.PointerItems;
import org.wijiscommons.ssaf.util.VarUtil;
import org.wijiscommons.ssaf.util.dom.SSAFErrorTypes;

public class SSAFIndexImpl implements SSAFIndex {

    public static final String DATE_SUFFIX = "T00:00:00Z";

    private final Log log = LogFactory.getLog(SSAFIndexImpl.class);

    private int commitSize = 100;

    private final JaxbFactory jaxbFactory;

    private Map<String, SSAFSolrFieldTypeEnum> fieldMapping;

    private SSAFSearchImpl ssafSearchImpl;

    public SSAFIndexImpl(String schemaRoot) {
        jaxbFactory = new JaxbFactory("org.wijiscommons.ssaf.schema.search", new File(schemaRoot + "schema/search/schema.xsd"));
    }

    public boolean reIndex(String solrLocation, String pointerFolderLocation) throws SSAFSolrException {
        List<String> filePaths = getFilePaths(pointerFolderLocation);
        List<Pointer> pointerList = new ArrayList<Pointer>();
        for (String filePath : filePaths) {
            Pointer pointer = validateXML(filePath);
            pointerList.add(pointer);
            if (pointerList.size() == getCommitSize()) {
                Pointer[] pointers = new Pointer[pointerList.size()];
                addStashDocumentToSOLRIndex(solrLocation, pointerList.toArray(pointers));
                pointerList.clear();
            }
        }
        if (!pointerList.isEmpty()) {
            Pointer[] pointers = new Pointer[pointerList.size()];
            addStashDocumentToSOLRIndex(solrLocation, pointerList.toArray(pointers));
        }
        return true;
    }

    /**
     * 
     * @param pointerFolderLocation
     * @return list of file paths of all the xml files in the folder
     *         <code>pointerFolderLocation</code>
     */
    private List<String> getFilePaths(String pointerFolderLocation) {
        List<String> returnList = new ArrayList<String>();
        File folder = new File(pointerFolderLocation);
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                if (fileName.endsWith(".xml")) {
                    returnList.add(pointerFolderLocation + "/" + fileName);
                }
            }
        }
        return returnList;
    }

    /**
	 * 
	 */
    public boolean index(String location, Document document) throws SSAFSolrException {
        Pointer pointer = validateXML(document);
        addStashDocumentToSOLRIndex(location, pointer);
        return true;
    }

    /**
     * This method adds Pointers to Solr Index
     * 
     * @param solrLocation
     * @param pointers
     * @throws SSAFSolrException
     * @throws IOException
     * @throws SolrServerException
     */
    private void addStashDocumentToSOLRIndex(String solrLocation, Pointer... pointers) throws SSAFSolrException {
        SolrServer server = new SolrConnection().getSolrServer(solrLocation);
        List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
        for (Pointer pointer : pointers) {
            SolrInputDocument doc = new SolrInputDocument();
            String recordUri = pointer.getRecordUri();
            String stashFilePath = pointer.getStashFilepath();
            String stashType = pointer.getStashType();
            XMLGregorianCalendar stashDate = pointer.getStashDatetime();
            String pointerFilePath = pointer.getPointerFilepath();
            PointerItems pointerItems = pointer.getPointerItems();
            VarUtil.checkNonblankString("recordUri", recordUri);
            VarUtil.checkNonblankString("stashFilePath", stashFilePath);
            VarUtil.checkNonblankString("stashType", stashType);
            VarUtil.checkNonNullObject("stashDate", stashDate);
            VarUtil.checkNonblankString("pointerFilePath", pointerFilePath);
            VarUtil.checkNonNullObject("ponterItems", pointerItems);
            doc.addField("record_uri", recordUri);
            doc.addField("stash_filepath", stashFilePath);
            doc.addField("stash_type", stashType);
            doc.addField("stash_date", stashDate);
            doc.addField("pointer_filepath", pointerFilePath);
            List<PointerItem> pointerItemList = pointerItems.getPointerItem();
            for (PointerItem pointerItem : pointerItemList) {
                SSAFSolrFieldTypeEnum fieldType = fieldMapping.get(pointerItem.getItemName());
                Boolean isIndexed = null;
                if (fieldType != null) {
                    isIndexed = fieldType.isIndexed();
                }
                if (isIndexed == null) {
                    throw new SSAFSolrException("Pointer item, " + pointerItem.getItemName() + ", is not defined in semantics");
                }
                if (Boolean.valueOf(isIndexed)) {
                    DataType dataType = DataType.fromValue(fieldType.getDataType());
                    String fieldName = pointerItem.getItemName() + "_" + dataType.value();
                    String pointerValue = "";
                    switch(dataType) {
                        case STRING:
                            pointerValue = pointerItem.getItemValue().toLowerCase();
                            break;
                        case DATE:
                            pointerValue = pointerItem.getItemValue().concat(DATE_SUFFIX);
                            break;
                        default:
                            pointerValue = pointerItem.getItemValue();
                            break;
                    }
                    doc.addField(fieldName, pointerValue);
                }
            }
            docs.add(doc);
        }
        try {
            for (Pointer pointer : pointers) {
                SolrDocument solrDocument = ssafSearchImpl.searchWithRecordUri(solrLocation, pointer.getRecordUri());
                if (solrDocument != null) {
                    String pointerFilePath = (String) solrDocument.get("pointer_filepath");
                    deleteFile(pointerFilePath);
                    String stashFilePath = (String) solrDocument.get("stash_filepath");
                    deleteFile(stashFilePath);
                }
            }
            server.add(docs);
            server.commit();
        } catch (SolrServerException e) {
            log.error("Unable to Stash pointers", e);
            e.printStackTrace();
            throw new SSAFSolrException(SSAFSolrErrorCodes.UNABLE_TO_STASH);
        } catch (IOException e) {
            log.error("Unable to Stash pointers", e);
            e.printStackTrace();
            throw new SSAFSolrException(SSAFSolrErrorCodes.UNABLE_TO_STASH);
        }
    }

    private void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
	 * Creates Pointer Object from Document which contains Pointer XML
	 * @param document
	 * @return
	 * @throws SSAFSolrException
	 */
    private Pointer validateXML(Document document) throws SSAFSolrException {
        Pointer pointer = null;
        try {
            Unmarshaller u = jaxbFactory.getUnmarshaller();
            pointer = (Pointer) u.unmarshal(document);
        } catch (JAXBException je) {
            SSAFSolrException ssafException = new SSAFSolrException(SSAFSolrErrorCodes.XML_UNMARSHALL_ERROR, SSAFErrorTypes.REQUEST_RECORD_ERROR);
            throw ssafException;
        }
        return pointer;
    }

    /**
	 * Creates Pointer from filePath
	 * @param filePath
	 * @return
	 * @throws SSAFSolrException
	 */
    private Pointer validateXML(String filePath) throws SSAFSolrException {
        Pointer pointer = null;
        try {
            Unmarshaller u = jaxbFactory.getUnmarshaller();
            pointer = (Pointer) u.unmarshal(new File(filePath));
        } catch (JAXBException je) {
            SSAFSolrException ssafException = new SSAFSolrException(SSAFSolrErrorCodes.XML_UNMARSHALL_ERROR, SSAFErrorTypes.NONLOGIC);
            throw ssafException;
        }
        return pointer;
    }

    public int getCommitSize() {
        return commitSize;
    }

    public void setCommitSize(int commitSize) {
        this.commitSize = commitSize;
    }

    public Map<String, SSAFSolrFieldTypeEnum> getFieldMapping() {
        return fieldMapping;
    }

    public void setFieldMapping(Map<String, SSAFSolrFieldTypeEnum> fieldMapping) {
        this.fieldMapping = fieldMapping;
    }

    public void setSsafSearchImpl(SSAFSearchImpl ssafSearchImpl) {
        this.ssafSearchImpl = ssafSearchImpl;
    }

    public SSAFSearchImpl getSsafSearchImpl() {
        return ssafSearchImpl;
    }
}
