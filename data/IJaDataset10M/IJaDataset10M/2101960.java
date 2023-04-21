package org.bungeni.editor.metadata;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bungeni.editor.config.DocumentMetadataReader;
import org.bungeni.ooo.OOComponentHelper;
import org.jdom.Element;

/**
 *
 * @author Administrator
 */
public class DocumentMetadataSupplier {

    private HashMap<String, DocumentMetadata> metadataMap = new HashMap<String, DocumentMetadata>();

    private OOComponentHelper ooDocument;

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DocumentMetadataSupplier.class.getName());

    private static int METADATA_NAME_COLUMN = 0;

    private static int METADATA_DATATYPE_COLUMN = 1;

    private static int METADATA_TYPE_COLUMN = 2;

    private static int METADATA_DISPLAYNAME_COLUMN = 3;

    private static int METADATA_VISIBLE_COLUMN = 4;

    private static int METADATA_TABLE_CONFIG = 5;

    /** Creates a new instance of DocumentMetadataFactory */
    public DocumentMetadataSupplier(OOComponentHelper ooDoc) {
        ooDocument = ooDoc;
        log.debug("in Constructor()");
        initDocumentMetadataVariables();
    }

    public int getVisibleCount() {
        return metadataMap.size();
    }

    public DocumentMetadata[] getDocumentMetadata() {
        return metadataMap.values().toArray(new DocumentMetadata[metadataMap.size()]);
    }

    private void debug() {
        Iterator<String> iter = metadataMap.keySet().iterator();
        while (iter.hasNext()) {
            log.debug("DocumentMetadataSupplier : keyset value = " + iter.next());
        }
    }

    public void setOOComponentHelper(OOComponentHelper ooDoc) {
        this.ooDocument = ooDoc;
    }

    public void loadMetadataFromDocument() {
        try {
            log.debug("loadMetadataFromDocument: begin");
            if (!metadataMap.isEmpty()) {
                Iterator metaIterator = metadataMap.keySet().iterator();
                while (metaIterator.hasNext()) {
                    String metaName = (String) metaIterator.next();
                    DocumentMetadata metadata = metadataMap.get(metaName);
                    log.debug("loadMetadataFromDocument: metaName " + metaName);
                    if (ooDocument.propertyExists(metadata.getName())) {
                        metadata.setValue(ooDocument.getPropertyValue(metaName));
                        log.debug("loadMetadataFromDocument : metaName:" + metaName + ", value = " + metadata.getValue());
                        metadataMap.put(metadata.getName(), metadata);
                    } else {
                        metadata.setValue("ERROR_PROP_DOES_NOT_EXIST");
                        metadataMap.put(metadata.getName(), metadata);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("error in refreshmetadata() " + ex.getMessage());
        }
    }

    public void updateMetadataToDocument() {
        if (!metadataMap.isEmpty()) {
            Iterator metaIterator = metadataMap.keySet().iterator();
            while (metaIterator.hasNext()) {
                String metaName = (String) metaIterator.next();
                updateMetadataToDocument(metaName);
            }
        }
    }

    public void updateMetadataToDocument(String name) {
        if (metadataMap.containsKey(name)) {
            DocumentMetadata metadata = metadataMap.get(name);
            if (ooDocument.propertyExists(metadata.getName())) {
                ooDocument.setPropertyValue(metadata.getName(), metadata.getValue());
            } else {
                ooDocument.addProperty(metadata.getName(), metadata.getValue());
            }
        }
    }

    private void initDocumentMetadataVariables() {
        try {
            List<Element> listMetadata = DocumentMetadataReader.getInstance().getVisibleMetadatas();
            if (null != listMetadata) {
                metadataMap.clear();
                for (Element metadataElem : listMetadata) {
                    DocumentMetadata meta = convertElementToDocumentMetadata(metadataElem);
                    this.metadataMap.put(meta.getName(), meta);
                }
            }
        } catch (Exception ex) {
            log.error("exception in DocumentMetadataSupplier :" + ex.getMessage());
        }
    }

    public static DocumentMetadata convertElementToDocumentMetadata(Element metaElem) {
        String metaName = metaElem.getAttributeValue("name");
        String metaDataType = metaElem.getAttributeValue("datatype");
        String metaType = metaElem.getAttributeValue("type");
        String metaDisplay = org.bungeni.extutils.CommonResourceBundleHelperFunctions.getDocMetaString(metaElem.getChildTextNormalize("title"));
        String visible = metaElem.getAttributeValue("visible");
        String tableConfig = metaElem.getAttributeValue("tabular-config");
        DocumentMetadata meta = new DocumentMetadata(metaName, metaType, metaDataType, metaDisplay, Integer.parseInt(visible), tableConfig);
        return meta;
    }
}
