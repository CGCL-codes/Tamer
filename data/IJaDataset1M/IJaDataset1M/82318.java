package com.dcivision.dms.core;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import com.dcivision.dms.bean.DmsArchive;
import com.dcivision.dms.bean.DmsContent;
import com.dcivision.dms.bean.DmsDefaultProfileSetting;
import com.dcivision.dms.bean.DmsDocument;
import com.dcivision.dms.bean.DmsDocumentDetail;
import com.dcivision.dms.bean.DmsVersion;
import com.dcivision.framework.SessionContainer;
import com.dcivision.framework.Utility;
import com.dcivision.framework.bean.SysUserDefinedIndex;
import com.dcivision.framework.bean.SysUserDefinedIndexDetail;
import com.dcivision.framework.xml.XMLUtility;

public class ArchiveDataFileParser extends XMLUtility {

    public static final String REVISION = "$Revision: 1.19.2.1 $";

    String[] docList = null;

    String[][] versionsList = null;

    DmsArchive dmsArchive = null;

    SessionContainer ctx = null;

    Connection conn = null;

    String dataFilePath = null;

    /** Creates a new instance of ArchiveDataFileTransformer */
    public ArchiveDataFileParser() {
    }

    public ArchiveDataFileParser(String file) {
        super(file);
    }

    public void setDocList(String[] docList) {
        this.docList = docList;
    }

    public String[] getDocList() {
        return (this.docList);
    }

    public void setVersionsList(String[][] versionsList) {
        this.versionsList = versionsList;
    }

    public String[][] getVersionsList() {
        return (this.versionsList);
    }

    public void setDmsArchive(DmsArchive dmsArchive) {
        this.dmsArchive = dmsArchive;
    }

    public DmsArchive getDmsArchive() {
        return (this.dmsArchive);
    }

    public void setSessionContainer(SessionContainer ctx) {
        this.ctx = ctx;
    }

    public SessionContainer getSessionContainer() {
        return (this.ctx);
    }

    public void setConnection(Connection conn) {
        this.conn = conn;
    }

    public Connection getConnection() {
        return (this.conn);
    }

    public String getDataFilePath() {
        return this.dataFilePath;
    }

    public void setDataFilePath(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    public void parse(InputSource inputSource) throws IOException, SAXException {
        DocumentRetrievalManager retrievalManager = null;
        try {
            ContentHandler handler = this.getContentHandler();
            handler.startDocument();
            AttributesImpl rootElementAtt = new AttributesImpl();
            rootElementAtt.addAttribute("", "", "xmlns", "", "http://tempuri.org/data_file.xsd");
            rootElementAtt.addAttribute("", "", "archive_id", "", this.dmsArchive.getID().toString());
            rootElementAtt.addAttribute("", "", "archive_date", "", this.dmsArchive.getCreateDate() == null ? "" : this.dmsArchive.getCreateDate().toString());
            rootElementAtt.addAttribute("", "", "archive_by", "", this.dmsArchive.getCreatorName());
            rootElementAtt.addAttribute("", "", "type", "", this.dmsArchive.getArchiveType());
            String segmentAtt = this.dataFilePath.substring(dataFilePath.indexOf("" + dmsArchive.getArchiveName() + "/") + dmsArchive.getArchiveName().length() + 1, dataFilePath.indexOf("/image"));
            rootElementAtt.addAttribute("", "", "segment", "", segmentAtt);
            rootElementAtt.addAttribute("", "", "no_of_files", "", new Integer(docList.length).toString());
            this.writeStartTag("file_list", "", rootElementAtt);
            this.setIndent("\n\t");
            retrievalManager = new DocumentRetrievalManager(ctx, conn);
            String value = "";
            Integer reParentID = null;
            for (int i = 0; i < this.docList.length; i++) {
                DmsDocument dmsDocument = null;
                SysUserDefinedIndex udfIndex = null;
                String[] versions = this.versionsList[i];
                dmsDocument = retrievalManager.getDocument(new Integer(docList[i]));
                if (!this.checkCompoundSub(dmsDocument)) {
                    String effectiveStartDate = dmsDocument.getEffectiveStartDate() == null ? "" : dmsDocument.getEffectiveStartDate().toString();
                    String effectiveEndDate = dmsDocument.getEffectiveEndDate() == null ? "" : dmsDocument.getEffectiveEndDate().toString();
                    value = "default";
                    udfIndex = retrievalManager.getUserDefinedFieldByDocument(dmsDocument);
                    if (udfIndex != null) {
                        value = udfIndex.getUserDefinedType();
                    }
                    for (int j = 0; j < versions.length; j++) {
                        Integer versionID = new Integer(versions[j]);
                        DmsVersion dmsVersion = retrievalManager.getVersionByVersionID(versionID);
                        DmsContent tmpContent = retrievalManager.getContentByContentID(dmsVersion.getContentID());
                        AttributesImpl fileElementAtt = new AttributesImpl();
                        fileElementAtt.addAttribute("", "", "name", "", tmpContent.getConvertedName());
                        fileElementAtt.addAttribute("", "", "doctype", "", value);
                        fileElementAtt.addAttribute("", "", "id", "", dmsDocument.getID().toString());
                        fileElementAtt.addAttribute("", "", "version_id", "", versionID.toString());
                        this.writeStartTag("file", "", fileElementAtt);
                        this.setIndent("\n\t\t");
                        this.writeTag("file_name", dmsDocument.getDocumentName(), new AttributesImpl());
                        String path = retrievalManager.getLocationPath(dmsDocument.getParentID());
                        this.writeTag("location", path, new AttributesImpl());
                        this.writeTag("document_type", dmsDocument.getDocumentType(), new AttributesImpl());
                        this.writeTag("version_label", dmsVersion.getVersionLabel() == null ? "" : dmsVersion.getVersionLabel(), new AttributesImpl());
                        this.writeTag("version_number", dmsVersion.getVersionNumber() == null ? "" : dmsVersion.getVersionNumber().toString(), new AttributesImpl());
                        this.writeTag("effective_start_date", effectiveStartDate, new AttributesImpl());
                        this.writeTag("effective_end_date", effectiveEndDate, new AttributesImpl());
                        this.writeTag("description", dmsVersion.getDescription() == null ? "" : dmsVersion.getDescription(), new AttributesImpl());
                        this.writeTag("file_reference_number", dmsVersion.getReferenceNo() == null ? "" : dmsVersion.getReferenceNo(), new AttributesImpl());
                        Method getFileMth = null;
                        this.writeStartTag("default_profile", "", new AttributesImpl());
                        this.setIndent("\n\t\t\t");
                        int totalFieldCount = Integer.parseInt(com.dcivision.framework.SystemParameterFactory.getSystemParameter(com.dcivision.framework.SystemParameterConstant.DMS_DEFAULT_PROFILE_FIELD_COUNT));
                        com.dcivision.dms.dao.DmsDefaultProfileSettingDAObject dmsProfileDAO = new com.dcivision.dms.dao.DmsDefaultProfileSettingDAObject(null, conn);
                        List dmsDefaultProfile = dmsProfileDAO.getFullList();
                        for (int k = 0; k < totalFieldCount; k++) {
                            DmsDefaultProfileSetting setting = ((DmsDefaultProfileSetting) dmsDefaultProfile.get(k));
                            if (setting != null) {
                                AttributesImpl defaultProfileElementAtt = new AttributesImpl();
                                defaultProfileElementAtt.addAttribute("", "", "id", "", setting.getID().toString());
                                defaultProfileElementAtt.addAttribute("", "", "type", "", setting.getFieldType());
                                defaultProfileElementAtt.addAttribute("", "", "name", "", setting.getFieldName());
                                getFileMth = dmsDocument.getClass().getMethod("getUserDef" + (k + 1), null);
                                String udfValue = (String) getFileMth.invoke(dmsDocument, null);
                                if (Utility.isEmpty(udfValue)) {
                                    udfValue = "";
                                }
                                this.writeTag("user_define_field", udfValue, defaultProfileElementAtt);
                            }
                        }
                        this.setIndent("\n\t\t");
                        this.writeEndTag("default_profile");
                        AttributesImpl docProfileElementAtt = new AttributesImpl();
                        if (udfIndex != null) {
                            docProfileElementAtt.addAttribute("", "", "id", "", udfIndex.getID().toString());
                            docProfileElementAtt.addAttribute("", "", "name", "", udfIndex.getUserDefinedType());
                            docProfileElementAtt.addAttribute("", "", "description", "", udfIndex.getDescription() == null ? "" : udfIndex.getDescription());
                        }
                        this.writeStartTag("document_profile", "", docProfileElementAtt);
                        this.setIndent("\n\t\t\t");
                        if (udfIndex != null) {
                            List udfDetailList = retrievalManager.getUDFDetailList(udfIndex.getID());
                            for (int k = 0; k < udfDetailList.size(); k++) {
                                SysUserDefinedIndexDetail udfDetail = (SysUserDefinedIndexDetail) udfDetailList.get(k);
                                if (Utility.isEmpty(udfDetail.getFieldName())) continue;
                                AttributesImpl udfDetailElementAtt = new AttributesImpl();
                                udfDetailElementAtt.addAttribute("", "", "id", "", udfDetail.getID().toString());
                                udfDetailElementAtt.addAttribute("", "", "type", "", udfDetail.getFieldType());
                                udfDetailElementAtt.addAttribute("", "", "name", "", udfDetail.getFieldName());
                                DmsDocumentDetail dmsDocumentDetail = retrievalManager.getDetailObjectByDocIDUDFDetailID(dmsDocument.getID(), udfDetail.getID());
                                String fieldValue = "";
                                if (dmsDocumentDetail != null) {
                                    if (udfDetail.getFieldType().equals(SysUserDefinedIndexDetail.DATE_FIELD)) {
                                        fieldValue = dmsDocumentDetail.getDateValue() == null ? "" : dmsDocumentDetail.getDateValue().toString();
                                    } else if (udfDetail.getFieldType().equals(SysUserDefinedIndexDetail.STRING_FIELD) || udfDetail.getFieldType().equals(SysUserDefinedIndexDetail.FIELD_TYPE_SELECT_DATABASE)) {
                                        if (!Utility.isEmpty(dmsDocumentDetail.getFieldValue())) {
                                            fieldValue = dmsDocumentDetail.getFieldValue();
                                        } else {
                                            fieldValue = "";
                                        }
                                    } else if (udfDetail.getFieldType().equals(SysUserDefinedIndexDetail.NUMBER_FIELD)) {
                                        fieldValue = dmsDocumentDetail.getNumericValue() == null ? "" : dmsDocumentDetail.getNumericValue().toString();
                                    }
                                }
                                this.writeTag("field", fieldValue, udfDetailElementAtt);
                            }
                        }
                        this.setIndent("\n\t\t");
                        this.writeEndTag("document_profile");
                        String segment = this.getSegment(dmsDocument.getID(), dmsVersion.getID());
                        this.writeTag("segment", segment, new AttributesImpl());
                        if (hasSubDoucment(dmsDocument, dmsVersion, versions)) {
                            this.getCompoundDocumentSub(dmsDocument.getID(), inputSource);
                        }
                        this.setIndent("\n\t");
                        this.writeEndTag("file");
                    }
                } else {
                    if (!dmsDocument.getParentID().equals(reParentID)) {
                        if (!checkParentID(dmsDocument)) {
                            reParentID = dmsDocument.getParentID();
                            this.getParentDocument(dmsDocument.getParentID(), inputSource);
                        }
                    }
                }
            }
            this.setIndent("\n");
            this.writeEndTag("file_list");
            handler.endDocument();
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    public boolean checkCompoundSub(DmsDocument dmsDocument) {
        boolean isCompoundSub = false;
        Integer parentID = dmsDocument.getParentID();
        DocumentRetrievalManager retrievalManager = new DocumentRetrievalManager(ctx, conn);
        try {
            DmsDocument document = retrievalManager.getDocument(parentID);
            if (DmsDocument.COMPOUND_DOC_TYPE.equals(document.getDocumentType()) || DmsDocument.PAPER_DOC_TYPE.equals(document.getDocumentType()) || DmsDocument.EMAIL_DOC_TYPE.equals(document.getDocumentType()) || DmsDocument.FORM_DOC_TYPE.equals(document.getDocumentType()) || DmsDocument.FLOW_DOC_TYPE.equals(document.getDocumentType())) {
                isCompoundSub = true;
            }
        } catch (Exception e) {
        }
        return isCompoundSub;
    }

    protected boolean hasSubDoucment(DmsDocument dmsDocument, DmsVersion dmsVersion, String[] versions) {
        boolean hasSubDoucment = false;
        if (DmsDocument.COMPOUND_DOC_TYPE.equals(dmsDocument.getDocumentType()) || DmsDocument.EMAIL_DOC_TYPE.equals(dmsDocument.getDocumentType()) || DmsDocument.FORM_DOC_TYPE.equals(dmsDocument.getDocumentType()) || DmsDocument.FLOW_DOC_TYPE.equals(dmsDocument.getDocumentType())) {
            if (versions.length > 1) {
                if ("CURRENT".equals(dmsVersion.getVersionLabel())) {
                    hasSubDoucment = true;
                }
            } else {
                hasSubDoucment = true;
            }
        } else if (DmsDocument.PAPER_DOC_TYPE.equals(dmsDocument.getDocumentType())) {
            hasSubDoucment = true;
        }
        return hasSubDoucment;
    }

    protected boolean checkParentID(DmsDocument dmsDocument) {
        Integer parentID = dmsDocument.getParentID();
        for (int i = 0; i < this.docList.length; i++) {
            if (parentID.equals(new Integer(docList[i]))) {
                return true;
            }
        }
        return false;
    }

    protected void getParentDocument(Integer documentID, InputSource inputSource) throws IOException, SAXException {
        DocumentRetrievalManager retrievalManager = new DocumentRetrievalManager(ctx, conn);
        try {
            DmsDocument dmsDocument = retrievalManager.getDocument(documentID);
            DmsVersion dmsVersion = retrievalManager.getTopVersionByDocumentID(documentID);
            DmsContent tmpContent = retrievalManager.getContentByContentID(dmsVersion.getContentID());
            String value = "default";
            String effectiveStartDate = dmsDocument.getEffectiveStartDate() == null ? "" : dmsDocument.getEffectiveStartDate().toString();
            String effectiveEndDate = dmsDocument.getEffectiveEndDate() == null ? "" : dmsDocument.getEffectiveEndDate().toString();
            AttributesImpl fileElementAtt = new AttributesImpl();
            fileElementAtt.addAttribute("", "", "name", "", tmpContent.getConvertedName());
            fileElementAtt.addAttribute("", "", "doctype", "", value);
            fileElementAtt.addAttribute("", "", "id", "", dmsDocument.getID().toString());
            fileElementAtt.addAttribute("", "", "version_id", "", dmsVersion.getID().toString());
            this.writeStartTag("file", "", fileElementAtt);
            this.setIndent("\n\t\t");
            this.writeTag("file_name", dmsDocument.getDocumentName(), new AttributesImpl());
            String path = retrievalManager.getLocationPath(dmsDocument.getParentID());
            this.writeTag("location", path, new AttributesImpl());
            this.writeTag("document_type", dmsDocument.getDocumentType(), new AttributesImpl());
            this.writeTag("version_label", dmsVersion.getVersionLabel() == null ? "" : dmsVersion.getVersionLabel(), new AttributesImpl());
            this.writeTag("version_number", dmsVersion.getVersionNumber() == null ? "" : dmsVersion.getVersionNumber().toString(), new AttributesImpl());
            this.writeTag("effective_start_date", effectiveStartDate, new AttributesImpl());
            this.writeTag("effective_end_date", effectiveEndDate, new AttributesImpl());
            this.writeTag("description", dmsVersion.getDescription() == null ? "" : dmsVersion.getDescription(), new AttributesImpl());
            this.writeTag("file_reference_number", dmsVersion.getReferenceNo() == null ? "" : dmsVersion.getReferenceNo(), new AttributesImpl());
            Method getFileMth = null;
            this.writeStartTag("default_profile", "", new AttributesImpl());
            this.setIndent("\n\t\t\t");
            int totalFieldCount = Integer.parseInt(com.dcivision.framework.SystemParameterFactory.getSystemParameter(com.dcivision.framework.SystemParameterConstant.DMS_DEFAULT_PROFILE_FIELD_COUNT));
            com.dcivision.dms.dao.DmsDefaultProfileSettingDAObject dmsProfileDAO = new com.dcivision.dms.dao.DmsDefaultProfileSettingDAObject(null, conn);
            List dmsDefaultProfile = dmsProfileDAO.getFullList();
            for (int k = 0; k < totalFieldCount; k++) {
                DmsDefaultProfileSetting setting = ((DmsDefaultProfileSetting) dmsDefaultProfile.get(k));
                if (setting != null) {
                    AttributesImpl defaultProfileElementAtt = new AttributesImpl();
                    defaultProfileElementAtt.addAttribute("", "", "id", "", setting.getID().toString());
                    defaultProfileElementAtt.addAttribute("", "", "type", "", setting.getFieldType());
                    defaultProfileElementAtt.addAttribute("", "", "name", "", setting.getFieldName());
                    getFileMth = dmsDocument.getClass().getMethod("getUserDef" + (k + 1), null);
                    String udfValue = (String) getFileMth.invoke(dmsDocument, null);
                    if (Utility.isEmpty(udfValue)) {
                        udfValue = "";
                    }
                    this.writeTag("user_define_field", udfValue, defaultProfileElementAtt);
                }
            }
            this.setIndent("\n\t\t");
            this.writeEndTag("default_profile");
            AttributesImpl docProfileElementAtt = new AttributesImpl();
            SysUserDefinedIndex udfIndex = retrievalManager.getUserDefinedFieldByDocument(dmsDocument);
            if (udfIndex != null) {
                docProfileElementAtt.addAttribute("", "", "id", "", udfIndex.getID().toString());
                docProfileElementAtt.addAttribute("", "", "name", "", udfIndex.getUserDefinedType());
                docProfileElementAtt.addAttribute("", "", "description", "", udfIndex.getDescription());
            }
            this.writeStartTag("document_profile", "", docProfileElementAtt);
            this.setIndent("\n\t\t\t");
            if (udfIndex != null) {
                List udfDetailList = retrievalManager.getUDFDetailList(udfIndex.getID());
                for (int k = 0; k < udfDetailList.size(); k++) {
                    SysUserDefinedIndexDetail udfDetail = (SysUserDefinedIndexDetail) udfDetailList.get(k);
                    AttributesImpl udfDetailElementAtt = new AttributesImpl();
                    udfDetailElementAtt.addAttribute("", "", "id", "", udfDetail.getID().toString());
                    udfDetailElementAtt.addAttribute("", "", "type", "", udfDetail.getFieldType());
                    udfDetailElementAtt.addAttribute("", "", "name", "", udfDetail.getFieldName());
                    DmsDocumentDetail dmsDocumentDetail = retrievalManager.getDetailObjectByDocIDUDFDetailID(dmsDocument.getID(), udfDetail.getID());
                    String fieldValue = "";
                    if (dmsDocumentDetail != null) {
                        if (udfDetail.getFieldType().equals(SysUserDefinedIndexDetail.DATE_FIELD)) {
                            fieldValue = dmsDocumentDetail.getDateValue() == null ? "" : dmsDocumentDetail.getDateValue().toString();
                        } else if (udfDetail.getFieldType().equals(SysUserDefinedIndexDetail.STRING_FIELD) || udfDetail.getFieldType().equals(SysUserDefinedIndexDetail.FIELD_TYPE_SELECT_DATABASE)) {
                            if (!Utility.isEmpty(dmsDocumentDetail.getFieldValue())) {
                                fieldValue = dmsDocumentDetail.getFieldValue();
                            } else {
                                fieldValue = "";
                            }
                        } else if (udfDetail.getFieldType().equals(SysUserDefinedIndexDetail.NUMBER_FIELD)) {
                            fieldValue = dmsDocumentDetail.getNumericValue() == null ? "" : dmsDocumentDetail.getNumericValue().toString();
                        }
                    }
                    this.writeTag("field", fieldValue, udfDetailElementAtt);
                }
            }
            this.setIndent("\n\t\t");
            this.writeEndTag("document_profile");
            String segment = this.getSegment(dmsDocument.getID(), dmsVersion.getID());
            this.writeTag("segment", segment, new AttributesImpl());
            this.getCompoundDocumentSub(documentID, inputSource);
            this.setIndent("\n\t");
            this.writeEndTag("file");
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    protected void getCompoundDocumentSub(Integer documentID, InputSource inputSource) throws IOException, SAXException {
        DocumentRetrievalManager retrievalManager = new DocumentRetrievalManager(ctx, conn);
        DocumentOperationManager docOperationManager = new DocumentOperationManager(ctx, conn);
        try {
            List subList = docOperationManager.getSubDocumentByParentID(documentID);
            this.writeStartTag("sub_document", "", new AttributesImpl());
            String value = "";
            for (int m = 0; m < subList.size(); m++) {
                DmsDocument document = null;
                SysUserDefinedIndex defineIndex = null;
                List versionList = new ArrayList();
                document = (DmsDocument) subList.get(m);
                if (DmsArchive.ARCHIVE_TYPE.equals(dmsArchive.getArchiveType())) {
                    versionList = retrievalManager.getVersionListByDocumentID(document.getID());
                } else {
                    DmsVersion topVersion = retrievalManager.getTopVersionByDocumentID(document.getID());
                    versionList.add(topVersion);
                }
                String effStartDate = document.getEffectiveStartDate() == null ? "" : document.getEffectiveStartDate().toString();
                String effEndDate = document.getEffectiveEndDate() == null ? "" : document.getEffectiveEndDate().toString();
                value = "default";
                defineIndex = retrievalManager.getUserDefinedFieldByDocument(document);
                if (defineIndex != null) {
                    value = defineIndex.getUserDefinedType();
                }
                for (int u = 0; u < versionList.size(); u++) {
                    DmsVersion version = (DmsVersion) versionList.get(u);
                    DmsContent content = retrievalManager.getContentByContentID(version.getContentID());
                    AttributesImpl fElementAtt = new AttributesImpl();
                    fElementAtt.addAttribute("", "", "name", "", content.getConvertedName());
                    fElementAtt.addAttribute("", "", "doctype", "", value);
                    fElementAtt.addAttribute("", "", "id", "", document.getID().toString());
                    fElementAtt.addAttribute("", "", "version_id", "", version.getID().toString());
                    this.setIndent("\n\t\t\t");
                    this.writeStartTag("file", "", fElementAtt);
                    this.setIndent("\n\t\t\t\t");
                    this.writeTag("file_name", document.getDocumentName(), new AttributesImpl());
                    String path = retrievalManager.getLocationPath(document.getParentID());
                    this.writeTag("location", path, new AttributesImpl());
                    this.writeTag("document_type", document.getDocumentType(), new AttributesImpl());
                    this.writeTag("version_label", version.getVersionLabel(), new AttributesImpl());
                    this.writeTag("version_number", version.getVersionNumber() == null ? "" : version.getVersionNumber().toString(), new AttributesImpl());
                    this.writeTag("effective_start_date", effStartDate, new AttributesImpl());
                    this.writeTag("effective_end_date", effEndDate, new AttributesImpl());
                    this.writeTag("description", (version.getDescription() != null ? version.getDescription() : ""), new AttributesImpl());
                    this.writeTag("file_reference_number", (version.getReferenceNo() != null ? version.getReferenceNo() : ""), new AttributesImpl());
                    Method getFileMth = null;
                    this.writeStartTag("default_profile", "", new AttributesImpl());
                    this.setIndent("\n\t\t\t\t\t");
                    int totalFieldCount = Integer.parseInt(com.dcivision.framework.SystemParameterFactory.getSystemParameter(com.dcivision.framework.SystemParameterConstant.DMS_DEFAULT_PROFILE_FIELD_COUNT));
                    com.dcivision.dms.dao.DmsDefaultProfileSettingDAObject dmsProfileDAO = new com.dcivision.dms.dao.DmsDefaultProfileSettingDAObject(null, conn);
                    List dmsDefaultProfile = dmsProfileDAO.getFullList();
                    for (int k = 0; k < totalFieldCount; k++) {
                        DmsDefaultProfileSetting setting = ((DmsDefaultProfileSetting) dmsDefaultProfile.get(k));
                        if (setting != null) {
                            AttributesImpl defaultProfileElementAtt = new AttributesImpl();
                            defaultProfileElementAtt.addAttribute("", "", "id", "", setting.getID().toString());
                            defaultProfileElementAtt.addAttribute("", "", "type", "", setting.getFieldType());
                            defaultProfileElementAtt.addAttribute("", "", "name", "", setting.getFieldName());
                            getFileMth = document.getClass().getMethod("getUserDef" + (k + 1), null);
                            String udfValue = (String) getFileMth.invoke(document, null);
                            if (Utility.isEmpty(udfValue)) {
                                udfValue = "";
                            }
                            this.writeTag("user_define_field", udfValue, defaultProfileElementAtt);
                        }
                    }
                    this.setIndent("\n\t\t\t\t");
                    this.writeEndTag("default_profile");
                    AttributesImpl profileElementAtt = new AttributesImpl();
                    if (defineIndex != null) {
                        profileElementAtt.addAttribute("", "", "id", "", defineIndex.getID().toString());
                        profileElementAtt.addAttribute("", "", "name", "", defineIndex.getUserDefinedType());
                        profileElementAtt.addAttribute("", "", "description", "", defineIndex.getDescription());
                    }
                    this.writeStartTag("document_profile", "", profileElementAtt);
                    this.setIndent("\n\t\t\t\t\t");
                    if (defineIndex != null) {
                        List udfDetailList = retrievalManager.getUDFDetailList(defineIndex.getID());
                        for (int k = 0; k < udfDetailList.size(); k++) {
                            SysUserDefinedIndexDetail udfDetail = (SysUserDefinedIndexDetail) udfDetailList.get(k);
                            AttributesImpl udfDetailElementAtt = new AttributesImpl();
                            udfDetailElementAtt.addAttribute("", "", "id", "", udfDetail.getID().toString());
                            udfDetailElementAtt.addAttribute("", "", "type", "", udfDetail.getFieldType());
                            udfDetailElementAtt.addAttribute("", "", "name", "", udfDetail.getFieldName());
                            DmsDocumentDetail dmsDocumentDetail = retrievalManager.getDetailObjectByDocIDUDFDetailID(document.getID(), udfDetail.getID());
                            String fieldValue = "";
                            if (dmsDocumentDetail != null) {
                                if (udfDetail.getFieldType().equals(SysUserDefinedIndexDetail.DATE_FIELD)) {
                                    fieldValue = dmsDocumentDetail.getDateValue() == null ? "" : dmsDocumentDetail.getDateValue().toString();
                                } else if (udfDetail.getFieldType().equals(SysUserDefinedIndexDetail.STRING_FIELD) || udfDetail.getFieldType().equals(SysUserDefinedIndexDetail.FIELD_TYPE_SELECT_DATABASE)) {
                                    if (!Utility.isEmpty(dmsDocumentDetail.getFieldValue())) {
                                        fieldValue = dmsDocumentDetail.getFieldValue();
                                    } else {
                                        fieldValue = "";
                                    }
                                } else if (udfDetail.getFieldType().equals(SysUserDefinedIndexDetail.NUMBER_FIELD)) {
                                    fieldValue = dmsDocumentDetail.getNumericValue() == null ? "" : dmsDocumentDetail.getNumericValue().toString();
                                }
                            }
                            this.writeTag("field", fieldValue, udfDetailElementAtt);
                        }
                    }
                    this.setIndent("\n\t\t\t\t");
                    this.writeEndTag("document_profile");
                    String segment = this.getSegment(document.getID(), version.getID());
                    this.writeTag("segment", segment, new AttributesImpl());
                    this.setIndent("\n\t\t\t");
                    this.writeEndTag("file");
                }
            }
            this.setIndent("\n\t\t");
            this.writeEndTag("sub_document");
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    protected String getSegment(Integer documentID, Integer versionID) throws IOException {
        DocumentRetrievalManager retrievalManager = new DocumentRetrievalManager(ctx, conn);
        String segment = "";
        try {
            segment = retrievalManager.getSegment(dmsArchive, documentID, versionID);
        } catch (Exception e) {
        }
        return segment;
    }
}
