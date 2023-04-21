package org.verus.ngl.sl.bprocess.administration;

import java.util.Hashtable;
import org.hibernate.Query;
import org.hibernate.Session;
import org.verus.ngl.sl.bprocess.technicalprocessing.ComplexVolume;
import org.verus.ngl.sl.bprocess.technicalprocessing.Document;
import org.verus.ngl.sl.objectmodel.technicalprocessing.DOCUMENT;
import org.verus.ngl.sl.bprocess.technicalprocessing.Catvolume;
import org.verus.ngl.utilities.logging.NGLLogging;
import org.verus.ngl.sl.bprocess.technicalprocessing.SearchableCatalogueRecord;
import org.verus.ngl.sl.utilities.Connections;
import org.verus.ngl.sl.utilities.NGLBeanFactory;
import org.verus.ngl.sl.objectmodel.technicalprocessing.CAPTIONS_PATTERNS;
import org.verus.ngl.sl.bprocess.technicalprocessing.Utility;
import org.verus.ngl.utilities.marc.NGLConverter;
import org.verus.ngl.utilities.marc.NGLMARCRecord;
import org.verus.ngl.sl.bprocess.technicalprocessing.ViewMARCRecords;

/**
 *
 * @author verus
 */
public class ItemDetailsImpl implements ItemDetails {

    @Override
    public Hashtable getItemDetails(String itemId, Integer libraryId, String dataBaseId) {
        Hashtable itemDeatilsTable = new Hashtable();
        Hashtable hashtable = new Hashtable();
        Integer[] integers = new Integer[3];
        String xml = new String();
        String volumeNo = "";
        String materialCategoryName = "";
        String materialTypeName = "";
        String accNo = "";
        int volId = 0;
        int volumeId = 0;
        String status = "";
        try {
            Document document = (Document) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("document");
            Catvolume cat_volume = (Catvolume) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("cat_volume");
            SearchableCatalogueRecord scr = (SearchableCatalogueRecord) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("searchable_cataloguerecord");
            Object obj = document.fetchDocument(itemId, libraryId, dataBaseId);
            if (obj != null) {
                DOCUMENT doc = (DOCUMENT) obj;
                volumeId = doc.getVolumeId();
                status = doc.getStatus();
                ComplexVolume complexVolume = (ComplexVolume) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("complex_volume");
                long count = complexVolume.getCountByVolumeId(volumeId, libraryId, dataBaseId);
                if (count == 0) {
                    integers = cat_volume.getCountByVolumeId(volumeId, libraryId, dataBaseId);
                    xml = scr.getXmlWholeRecord(integers[0], integers[1], dataBaseId);
                } else {
                    volId = complexVolume.getVolumeId(volumeId, libraryId, dataBaseId);
                    integers = cat_volume.getCountByVolumeId(volId, libraryId, dataBaseId);
                    xml = scr.getXmlWholeRecord(integers[0], integers[1], dataBaseId);
                }
                NGLMARCRecord[] nGLMARCRecords = NGLConverter.getInstance().getMarcModel(xml);
                if (nGLMARCRecords != null && nGLMARCRecords.length > 0) {
                    ViewMARCRecords viewMARCRecords = new ViewMARCRecords();
                    hashtable = viewMARCRecords.viewCatalogRecordDescription(nGLMARCRecords[0]);
                    NGLLogging.getFineLogger().fine("===hashtable===: " + hashtable);
                }
                String enumChronoXml = cat_volume.getEnumChrono(volumeId, dataBaseId);
                String capPatXml = "";
                if (integers[0] != -1 && integers[1] != -1 && integers[2] != -1) {
                    try {
                        Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
                        Session session = connections.getSession(dataBaseId);
                        Query query = session.getNamedQuery("CAPTIONS_PATTERNS.findByCataloguerecordidOwnerLibraryIdCapPatId");
                        query.setParameter("cataloguerecordid", integers[0]);
                        query.setParameter("ownerLibraryId", integers[1]);
                        query.setParameter("capPatId", integers[2]);
                        Object obj1 = query.uniqueResult();
                        CAPTIONS_PATTERNS captions_patterns = (CAPTIONS_PATTERNS) obj1;
                        if (captions_patterns.getCapPat() != null && captions_patterns.getCapPat().length() > 0) {
                            capPatXml = captions_patterns.getCapPat();
                        } else {
                            capPatXml = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (capPatXml != null && capPatXml.length() > 0 && enumChronoXml != null && enumChronoXml.length() > 0) {
                    volumeNo = Utility.getInstance().getEnumChronoDisplayString(capPatXml, enumChronoXml);
                }
                int materialTypeId = doc.getMaterialTypeId();
                int materialCategoryId = doc.getMaterialCategoryId();
                MaterialCategory mc = (MaterialCategory) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("materialCategory");
                materialCategoryName = mc.getMaterialCategoryName(materialCategoryId, libraryId, dataBaseId);
                AdmCoMaterialType admCoMaterialType = (AdmCoMaterialType) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("adm_co_material_type");
                materialTypeName = admCoMaterialType.getMaterialTypeName(materialTypeId, dataBaseId);
            } else {
                NGLLogging.getFineLogger().fine(" The item not found, check item number again!!!");
            }
            if (status != null && status.length() > 0 && !status.equalsIgnoreCase("")) {
                itemDeatilsTable.put("status", status);
            } else {
                itemDeatilsTable.put("status", "");
            }
            if (itemId != null && itemId.length() > 0 && !itemId.equalsIgnoreCase("")) {
                itemDeatilsTable.put("ItemId", itemId);
            } else {
                itemDeatilsTable.put("ItemId", "");
            }
            if (hashtable.get("Title") != null && hashtable.get("Title").toString().length() > 0 && !hashtable.get("Title").toString().equalsIgnoreCase("")) {
                itemDeatilsTable.put("Title", hashtable.get("Title"));
            } else {
                itemDeatilsTable.put("Title", "");
            }
            if (volumeNo != null && volumeNo.length() > 0) {
                itemDeatilsTable.put("VolumeNo", volumeNo);
            } else {
                itemDeatilsTable.put("VolumeNo", "");
            }
            if (hashtable.get("Author") != null && hashtable.get("Author").toString().length() > 0 && !hashtable.get("Author").toString().equalsIgnoreCase("")) {
                itemDeatilsTable.put("Author", hashtable.get("Author"));
            } else {
                itemDeatilsTable.put("Author", "");
            }
            if (hashtable.get("Edition") != null && hashtable.get("Edition").toString().length() > 0 && !hashtable.get("Edition").toString().equalsIgnoreCase("")) {
                itemDeatilsTable.put("Edition", hashtable.get("Edition"));
            } else {
                itemDeatilsTable.put("Edition", "");
            }
            if (hashtable.get("Imprint") != null && hashtable.get("Imprint").toString().length() > 0 && !hashtable.get("Imprint").toString().equalsIgnoreCase("")) {
                itemDeatilsTable.put("InPrint", hashtable.get("Imprint"));
            } else {
                itemDeatilsTable.put("InPrint", "");
            }
            if (hashtable.get("ISBN") != null && hashtable.get("ISBN").toString().length() > 0 && !hashtable.get("ISBN").toString().equalsIgnoreCase("")) {
                itemDeatilsTable.put("ISBN", hashtable.get("ISBN"));
            } else {
                itemDeatilsTable.put("ISBN", "");
            }
            if (hashtable.get("ISSN") != null && hashtable.get("ISSN").toString().length() > 0 && !hashtable.get("ISSN").toString().equalsIgnoreCase("")) {
                itemDeatilsTable.put("ISSN", hashtable.get("ISSN"));
            } else {
                itemDeatilsTable.put("ISSN", "");
            }
            if (materialTypeName != null && materialTypeName.length() > 0 && !materialTypeName.equalsIgnoreCase("")) {
                itemDeatilsTable.put("Type", materialTypeName);
            } else {
                itemDeatilsTable.put("Type", "");
            }
            if (materialCategoryName != null && materialCategoryName.length() > 0 && !materialCategoryName.equalsIgnoreCase("")) {
                itemDeatilsTable.put("Category", materialCategoryName);
            } else {
                itemDeatilsTable.put("Category", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemDeatilsTable;
    }

    @Override
    public Hashtable getItemDetailsByVolumeId(Integer volumeId, Integer libraryId, String dataBaseId) {
        Hashtable itemDeatilsTable = new Hashtable();
        Hashtable hashtable = new Hashtable();
        Integer[] integers = new Integer[3];
        String xml = new String();
        String volumeNo = "";
        String materialCategoryName = "";
        String materialTypeName = "";
        String accNo = "";
        int volId = 0;
        String status = "";
        try {
            Document document = (Document) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("document");
            Catvolume cat_volume = (Catvolume) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("cat_volume");
            SearchableCatalogueRecord scr = (SearchableCatalogueRecord) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("searchable_cataloguerecord");
            ComplexVolume complexVolume = (ComplexVolume) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("complex_volume");
            long count = complexVolume.getCountByVolumeId(volumeId, libraryId, dataBaseId);
            if (count == 0) {
                integers = cat_volume.getCountByVolumeId(volumeId, libraryId, dataBaseId);
                xml = scr.getXmlWholeRecord(integers[0], integers[1], dataBaseId);
            } else {
                volId = complexVolume.getVolumeId(volumeId, libraryId, dataBaseId);
                integers = cat_volume.getCountByVolumeId(volId, libraryId, dataBaseId);
                xml = scr.getXmlWholeRecord(integers[0], integers[1], dataBaseId);
            }
            NGLMARCRecord[] nGLMARCRecords = NGLConverter.getInstance().getMarcModel(xml);
            if (nGLMARCRecords != null && nGLMARCRecords.length > 0) {
                ViewMARCRecords viewMARCRecords = new ViewMARCRecords();
                hashtable = viewMARCRecords.viewCatalogRecordDescription(nGLMARCRecords[0]);
                NGLLogging.getFineLogger().fine("===hashtable===: " + hashtable);
            }
            String enumChronoXml = cat_volume.getEnumChrono(volumeId, dataBaseId);
            String capPatXml = "";
            if (integers[0] != -1 && integers[1] != -1 && integers[2] != -1) {
                try {
                    Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
                    Session session = connections.getSession(dataBaseId);
                    Query query = session.getNamedQuery("CAPTIONS_PATTERNS.findByCataloguerecordidOwnerLibraryIdCapPatId");
                    query.setParameter("cataloguerecordid", integers[0]);
                    query.setParameter("ownerLibraryId", integers[1]);
                    query.setParameter("capPatId", integers[2]);
                    Object obj1 = query.uniqueResult();
                    CAPTIONS_PATTERNS captions_patterns = (CAPTIONS_PATTERNS) obj1;
                    if (captions_patterns.getCapPat() != null && captions_patterns.getCapPat().length() > 0) {
                        capPatXml = captions_patterns.getCapPat();
                    } else {
                        capPatXml = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (capPatXml != null && capPatXml.length() > 0 && enumChronoXml != null && enumChronoXml.length() > 0) {
                volumeNo = Utility.getInstance().getEnumChronoDisplayString(capPatXml, enumChronoXml);
            }
            if (hashtable.get("Title") != null && hashtable.get("Title").toString().length() > 0 && !hashtable.get("Title").toString().equalsIgnoreCase("")) {
                itemDeatilsTable.put("Title", hashtable.get("Title"));
            } else {
                itemDeatilsTable.put("Title", "");
            }
            if (volumeNo != null && volumeNo.length() > 0) {
                itemDeatilsTable.put("VolumeNo", volumeNo);
            } else {
                itemDeatilsTable.put("VolumeNo", "");
            }
            if (hashtable.get("Author") != null && hashtable.get("Author").toString().length() > 0 && !hashtable.get("Author").toString().equalsIgnoreCase("")) {
                itemDeatilsTable.put("Author", hashtable.get("Author"));
            } else {
                itemDeatilsTable.put("Author", "");
            }
            if (hashtable.get("Edition") != null && hashtable.get("Edition").toString().length() > 0 && !hashtable.get("Edition").toString().equalsIgnoreCase("")) {
                itemDeatilsTable.put("Edition", hashtable.get("Edition"));
            } else {
                itemDeatilsTable.put("Edition", "");
            }
            if (hashtable.get("Imprint") != null && hashtable.get("Imprint").toString().length() > 0 && !hashtable.get("Imprint").toString().equalsIgnoreCase("")) {
                itemDeatilsTable.put("InPrint", hashtable.get("Imprint"));
            } else {
                itemDeatilsTable.put("InPrint", "");
            }
            if (hashtable.get("ISBN") != null && hashtable.get("ISBN").toString().length() > 0 && !hashtable.get("ISBN").toString().equalsIgnoreCase("")) {
                itemDeatilsTable.put("ISBN", hashtable.get("ISBN"));
            } else {
                itemDeatilsTable.put("ISBN", "");
            }
            if (hashtable.get("ISSN") != null && hashtable.get("ISSN").toString().length() > 0 && !hashtable.get("ISSN").toString().equalsIgnoreCase("")) {
                itemDeatilsTable.put("ISSN", hashtable.get("ISSN"));
            } else {
                itemDeatilsTable.put("ISSN", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemDeatilsTable;
    }

    @Override
    public Hashtable getItemDetailsByCatalogRecordIdNOwnerLibId(Integer catalogueRecordId, Integer ownerLibraryId, Integer libraryId, String dataBaseId) {
        Hashtable itemDeatilsTable = new Hashtable();
        Hashtable hashtable = new Hashtable();
        Integer[] integers = new Integer[3];
        String xml = new String();
        try {
            SearchableCatalogueRecord scr = (SearchableCatalogueRecord) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("searchable_cataloguerecord");
            xml = scr.getXmlWholeRecord(integers[0], integers[1], dataBaseId);
            NGLMARCRecord[] nGLMARCRecords = NGLConverter.getInstance().getMarcModel(xml);
            if (nGLMARCRecords != null && nGLMARCRecords.length > 0) {
                ViewMARCRecords viewMARCRecords = new ViewMARCRecords();
                hashtable = viewMARCRecords.viewCatalogRecordDescription(nGLMARCRecords[0]);
                NGLLogging.getFineLogger().fine("===hashtable===: " + hashtable);
            }
            if (hashtable.get("Title") != null && hashtable.get("Title").toString().length() > 0 && !hashtable.get("Title").toString().equalsIgnoreCase("")) {
                itemDeatilsTable.put("Title", hashtable.get("Title"));
            } else {
                itemDeatilsTable.put("Title", "");
            }
            if (hashtable.get("Author") != null && hashtable.get("Author").toString().length() > 0 && !hashtable.get("Author").toString().equalsIgnoreCase("")) {
                itemDeatilsTable.put("Author", hashtable.get("Author"));
            } else {
                itemDeatilsTable.put("Author", "");
            }
            if (hashtable.get("Edition") != null && hashtable.get("Edition").toString().length() > 0 && !hashtable.get("Edition").toString().equalsIgnoreCase("")) {
                itemDeatilsTable.put("Edition", hashtable.get("Edition"));
            } else {
                itemDeatilsTable.put("Edition", "");
            }
            if (hashtable.get("Imprint") != null && hashtable.get("Imprint").toString().length() > 0 && !hashtable.get("Imprint").toString().equalsIgnoreCase("")) {
                itemDeatilsTable.put("InPrint", hashtable.get("Imprint"));
            } else {
                itemDeatilsTable.put("InPrint", "");
            }
            if (hashtable.get("ISBN") != null && hashtable.get("ISBN").toString().length() > 0 && !hashtable.get("ISBN").toString().equalsIgnoreCase("")) {
                itemDeatilsTable.put("ISBN", hashtable.get("ISBN"));
            } else {
                itemDeatilsTable.put("ISBN", "");
            }
            if (hashtable.get("ISSN") != null && hashtable.get("ISSN").toString().length() > 0 && !hashtable.get("ISSN").toString().equalsIgnoreCase("")) {
                itemDeatilsTable.put("ISSN", hashtable.get("ISSN"));
            } else {
                itemDeatilsTable.put("ISSN", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemDeatilsTable;
    }
}
