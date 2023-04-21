package com.dcivision.dms.web;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import com.dcivision.dms.DmsErrorConstant;
import com.dcivision.dms.DmsOperationConstant;
import com.dcivision.dms.bean.DmsDocument;
import com.dcivision.dms.bean.DmsLocMaster;
import com.dcivision.dms.bean.DmsRoot;
import com.dcivision.dms.core.DocumentOperationManager;
import com.dcivision.dms.core.DocumentRetrievalManager;
import com.dcivision.dms.core.RootOperationManager;
import com.dcivision.dms.core.RootRetrievalManager;
import com.dcivision.dms.dao.DmsDocumentDAObject;
import com.dcivision.dms.dao.DmsLocMasterDAObject;
import com.dcivision.dms.dao.DmsRootDAObject;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.DataSourceFactory;
import com.dcivision.framework.ErrorConstant;
import com.dcivision.framework.GlobalConstant;
import com.dcivision.framework.MessageResourcesFactory;
import com.dcivision.framework.SessionContainer;
import com.dcivision.framework.SystemFunctionConstant;
import com.dcivision.framework.SystemParameterConstant;
import com.dcivision.framework.SystemParameterFactory;
import com.dcivision.framework.TextUtility;
import com.dcivision.framework.Utility;
import com.dcivision.framework.web.AbstractMaintAction;

/**
  MaintFolderStructureLoaderAction.java

  This class is for folder structure import.

    @author          Tony Lee
    @company         DCIVision Limited
    @creation date   23/10/2003
    @version         $Revision: 1.34.2.4 $
 */
public class MaintFolderStructureLoaderAction extends AbstractMaintAction {

    public static final String REVISION = "$Revision: 1.34.2.4 $";

    protected String parentID = "";

    protected String rootID = "";

    protected String expParentID = "";

    protected String expRootID = "";

    protected String zipParentID = "";

    protected String zipRootID = "";

    protected String zipExpParentID = "";

    protected String zipExpRootID = "";

    protected String documentName = "";

    protected FormFile uploadFile = null;

    protected FormFile zipUploadFile = null;

    protected InputStream is = null;

    protected InputStream zipIs = null;

    protected List nameList = new ArrayList();

    protected DocumentRetrievalManager docRetrievalManager = null;

    protected DocumentOperationManager docOperationManager = null;

    protected Integer startDocID = null;

    protected Integer intRootID = null;

    protected Integer countSuccess = new Integer(0);

    protected Integer countFail = new Integer(0);

    protected String failRows = "";

    protected String resultString = null;

    protected ArrayList resultList = new ArrayList();

    protected ArrayList urlStringList = null;

    public MaintFolderStructureLoaderAction() {
        super();
    }

    /**
   * getMajorDAOClassName
   *
   * @return  The class name of the major DAObject will be used in this action.
   */
    public String getMajorDAOClassName() {
        return null;
    }

    /**
   * getFunctionCode
   *
   * @return  The corresponding system function code of action.
   */
    public String getFunctionCode() {
        return (SystemFunctionConstant.DMS_ADMIN);
    }

    /**
   * setPageTitle
   * set the extend page title and page path.
   * default page path/title will be created by navmode and functionCode
   */
    public void setPageTitle(HttpServletRequest request, HttpServletResponse response, ActionForm form, ActionMapping mapping, ActionForward actionForward) {
        String extendTitle = MessageResourcesFactory.getMessage(this.getSessionContainer(request).getSessionLocale(), "dms.label.folder_shortcut");
        request.setAttribute(GlobalConstant.EXTEND_PAGE_TITLE, extendTitle);
        request.setAttribute(GlobalConstant.EXTEND_PAGE_TITLE_SHOW_ONLY, new Boolean(true));
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        this.retrieveFunctionCode(request, response, mapping);
        SessionContainer sessionContainer = this.getSessionContainer(request);
        Connection conn = this.getConnection(request);
        ActionForward retValue = null;
        urlStringList = new ArrayList();
        DocumentRetrievalManager docRetrievalManager = new DocumentRetrievalManager(sessionContainer, conn);
        RootRetrievalManager rootRetrievalManager = new RootRetrievalManager(sessionContainer, conn);
        MaintFolderStructureLoaderForm actionForm = (MaintFolderStructureLoaderForm) form;
        countSuccess = new Integer(0);
        if (!Utility.isEmpty(actionForm.getCurFunctionCode())) {
            this.setFunctionCode(actionForm.getCurFunctionCode());
        }
        String opMode = actionForm.getOpMode();
        documentName = actionForm.getDocumentName();
        parentID = actionForm.getParentID();
        rootID = actionForm.getRootID();
        uploadFile = actionForm.getUploadFile();
        zipUploadFile = actionForm.getZipUploadFile();
        expParentID = actionForm.getExpParentID();
        expRootID = actionForm.getExpRootID();
        zipParentID = actionForm.getZipParentID();
        zipRootID = actionForm.getZipRootID();
        zipExpParentID = actionForm.getZipExpParentID();
        zipExpRootID = actionForm.getZipExpRootID();
        if (Utility.isEmpty(rootID)) {
            List rootList = rootRetrievalManager.getAccessiblePublicRootsList();
            if (!Utility.isEmpty(rootList)) {
                DmsDocument documentRoot = docRetrievalManager.getRootFolderByRootID(((DmsRoot) rootList.get(0)).getID());
                actionForm.setRootID(documentRoot.getRootID().toString());
                actionForm.setParentID(documentRoot.getParentID().toString());
            }
        }
        try {
            if (Utility.isEmpty(opMode) || "I".equals(opMode)) {
                if (uploadFile != null) {
                    is = uploadFile.getInputStream();
                    if (GlobalConstant.PRIORITY_LOW.equals(actionForm.getChooseLocation())) {
                        resultList = loadFolderStructure(is, new Integer(rootID), new Integer(parentID), sessionContainer);
                    } else if (GlobalConstant.PRIORITY_MEDIUM.equals(actionForm.getChooseLocation())) {
                        resultList = loadRootFolderStructure(is, sessionContainer, request);
                    }
                    if (((resultList.get(0)).toString()).equals("Y")) {
                        this.addMessage(request, DmsErrorConstant.SUCCESS_ROW, resultList.get(1));
                        this.addMessage(request, DmsErrorConstant.FAIL_ROW, resultList.get(2));
                        if (!Utility.isEmpty((resultList.get(3)).toString())) {
                            this.addError(request, DmsErrorConstant.ROW_ERROR, resultList.get(3));
                        }
                        resultList.clear();
                    } else {
                        this.addError(request, DmsErrorConstant.FILE_ERROR, resultList.get(1));
                        resultList.clear();
                    }
                    retValue = mapping.findForward(GlobalConstant.NAV_MODE_VIEW);
                } else {
                    log.debug("null uploadFile");
                    retValue = mapping.findForward(GlobalConstant.NAV_MODE_NEW);
                }
            } else if ("E".equals(opMode)) {
                if (GlobalConstant.PRIORITY_LOW.equals(actionForm.getExportFolderType())) {
                    resultList = exportFolderStructure(mapping, form, request, new Integer(expRootID), new Integer(expParentID));
                } else if (GlobalConstant.PRIORITY_MEDIUM.equals(actionForm.getExportFolderType())) {
                    resultList = exportRootFolderStructure(mapping, form, request, response);
                }
                retValue = mapping.findForward(GlobalConstant.NAV_MODE_VIEW);
            } else if ("ZIF".equals(opMode)) {
                if (zipUploadFile != null) {
                    sessionContainer.setByPassPermission(true);
                    zipIs = zipUploadFile.getInputStream();
                    File file = this.gainFile(mapping, form, request, zipIs);
                    if (GlobalConstant.PRIORITY_LOW.equals(actionForm.getZipChooseLocation())) {
                        resultList = loadZipIncFileFolderStructure(mapping, form, request, new Integer(zipParentID), new Integer(zipRootID), file);
                    } else if (GlobalConstant.PRIORITY_MEDIUM.equals(actionForm.getZipChooseLocation())) {
                        resultList = loadZipIncFileRootFolderStructure(mapping, form, request, file);
                    }
                    if (((resultList.get(0)).toString()).equals("Y")) {
                        this.addMessage(request, DmsErrorConstant.SUCCESS_ROW, resultList.get(1));
                        this.addMessage(request, DmsErrorConstant.FAIL_ROW, resultList.get(2));
                        if (!Utility.isEmpty((resultList.get(3)).toString())) {
                            this.addError(request, DmsErrorConstant.ROW_ERROR, resultList.get(3));
                        }
                    } else {
                        this.addError(request, DmsErrorConstant.ZIPFILE_ERROR, resultList.get(1));
                    }
                    file.delete();
                    retValue = mapping.findForward(GlobalConstant.NAV_MODE_VIEW);
                    sessionContainer.setByPassPermission(false);
                } else {
                    log.debug("null uploadZipFile");
                    retValue = mapping.findForward(GlobalConstant.NAV_MODE_NEW);
                }
            } else if ("ZI".equals(opMode)) {
                if (zipUploadFile != null) {
                    zipIs = zipUploadFile.getInputStream();
                    File file = this.gainFile(mapping, form, request, zipIs);
                    if (GlobalConstant.PRIORITY_LOW.equals(actionForm.getZipChooseLocation())) {
                        resultList = loadZipFolderStructure(mapping, form, request, new Integer(zipParentID), new Integer(zipRootID), file);
                    } else if (GlobalConstant.PRIORITY_MEDIUM.equals(actionForm.getZipChooseLocation())) {
                        resultList = loadZipRootFolderStructure(mapping, form, request, file);
                    }
                    if (((resultList.get(0)).toString()).equals("Y")) {
                        this.addMessage(request, DmsErrorConstant.SUCCESS_ROW, resultList.get(1));
                        this.addMessage(request, DmsErrorConstant.FAIL_ROW, resultList.get(2));
                        if (!Utility.isEmpty((resultList.get(3)).toString())) {
                            this.addError(request, DmsErrorConstant.ROW_ERROR, resultList.get(3));
                        }
                    } else {
                        this.addError(request, DmsErrorConstant.ZIPFILE_ERROR, resultList.get(1));
                    }
                    file.delete();
                    retValue = mapping.findForward(GlobalConstant.NAV_MODE_VIEW);
                } else {
                    log.debug("null uploadZipFile");
                    retValue = mapping.findForward(GlobalConstant.NAV_MODE_NEW);
                }
            } else if ("ZE".equals(opMode)) {
                this.exportZipFolderStructure(mapping, form, request, response);
                retValue = mapping.findForward(GlobalConstant.NAV_MODE_VIEW);
            } else if ("S".equals(opMode)) {
                actionForm.setZipDestinationPath(null);
                retValue = mapping.findForward(GlobalConstant.NAV_MODE_VIEW);
            }
        } catch (Exception appEx) {
            this.rollback(request);
            log.error(appEx, appEx);
            throw new ApplicationException(appEx);
        } finally {
            conn = null;
        }
        return retValue;
    }

    public ArrayList loadFolderStructure(InputStream is, Integer rootID, Integer parentID, SessionContainer sessionContainer) throws ApplicationException {
        Connection conn = null;
        ArrayList resultList = new ArrayList();
        try {
            conn = DataSourceFactory.getConnection();
            this.docRetrievalManager = new DocumentRetrievalManager(sessionContainer, conn);
            this.docOperationManager = new DocumentOperationManager(sessionContainer, conn);
            this.startDocID = parentID;
            this.intRootID = rootID;
            Workbook workbook = Workbook.getWorkbook(is);
            try {
                this.validatorXSL(workbook);
            } catch (ApplicationException ex) {
                resultList.add(0, "N");
                resultList.add(1, ex.getMessage());
                return resultList;
            }
            try {
                this.validatorXSL(workbook);
            } catch (ApplicationException ex) {
                resultList.add(0, "N");
                resultList.add(1, ex.getMessage());
            }
            int numOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numOfSheets; i++) {
                String refNo = "";
                String desc = "";
                Sheet sheet = workbook.getSheet(i);
                for (int j = 1; j < sheet.getRows(); j++) {
                    Cell[] cellAry = sheet.getRow(j);
                    try {
                        if (cellAry.length > 0 && TextUtility.noNull(cellAry[0].getContents()).trim().length() > 0) {
                            List rowList = new ArrayList();
                            rowList.add(TextUtility.noNull(cellAry[0].getContents()).trim());
                            if (cellAry.length > 1) {
                                if (!Utility.isEmpty(cellAry[1].getContents().trim())) {
                                    rowList.add(TextUtility.noNull(cellAry[1].getContents()).trim());
                                }
                                if (cellAry.length > 2) {
                                    if (!Utility.isEmpty(cellAry[2].getContents().trim())) {
                                        String[] splitAry = TextUtility.splitString(TextUtility.noNull(cellAry[2].getContents()).trim(), "//");
                                        if (splitAry != null) {
                                            for (int k = 0; k < splitAry.length; k++) {
                                                rowList.add(splitAry[k]);
                                            }
                                        }
                                    }
                                    if (cellAry.length > 3) {
                                        if (!Utility.isEmpty(cellAry[3].getContents().trim())) {
                                            refNo = TextUtility.noNull(cellAry[3].getContents()).trim();
                                        }
                                        if (cellAry.length > 4) {
                                            if (!Utility.isEmpty(cellAry[4].getContents().trim())) {
                                                desc = TextUtility.noNull(cellAry[4].getContents()).trim();
                                            }
                                        }
                                    }
                                }
                            }
                            processRow(rowList, refNo, desc);
                        }
                    } catch (ApplicationException e) {
                        countFail = new Integer(countFail.intValue() + 1);
                        failRows = failRows + MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "errors.dms.row_no") + String.valueOf(j + 1) + MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "errors.dms.error") + MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), e.getMsgCode()) + "<br>";
                    }
                }
            }
            resultList.add(0, "Y");
            resultList.add(1, countSuccess);
            resultList.add(2, countFail);
            resultList.add(3, failRows);
            conn.commit();
        } catch (Exception e) {
            log.error(e, e);
            resultList.add(0, "N");
            resultList.add(1, e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (Exception ignore) {
            } finally {
                conn = null;
            }
            try {
                is.close();
            } catch (Exception ignore) {
            } finally {
                is = null;
            }
            docRetrievalManager.release();
            docOperationManager.release();
        }
        return resultList;
    }

    public ArrayList loadRootFolderStructure(InputStream is, SessionContainer sessionContainer, HttpServletRequest request) throws ApplicationException {
        Connection conn = null;
        ArrayList resultList = new ArrayList();
        try {
            conn = DataSourceFactory.getConnection();
            this.docRetrievalManager = new DocumentRetrievalManager(sessionContainer, conn);
            this.docOperationManager = new DocumentOperationManager(sessionContainer, conn);
            DmsRoot dmsRoot = null;
            Integer rootID = null;
            Integer parentID = new Integer(0);
            DmsRootDAObject dmsRootDAObject = new DmsRootDAObject(sessionContainer, conn);
            RootOperationManager rootOperationManager = new RootOperationManager(this.getSessionContainer(request), conn);
            Workbook workbook = Workbook.getWorkbook(is);
            try {
                this.validatorXSL(workbook);
            } catch (ApplicationException ex) {
                resultList.add(0, "N");
                resultList.add(1, ex.getMessage());
                return resultList;
            }
            int numOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numOfSheets; i++) {
                String refNo = "";
                String desc = "";
                Sheet sheet = workbook.getSheet(i);
                for (int j = 1; j < sheet.getRows(); j++) {
                    Cell[] cellAry = sheet.getRow(j);
                    try {
                        if (cellAry.length > 0 && TextUtility.noNull(cellAry[0].getContents()).trim().length() > 0) {
                            List rowList = new ArrayList();
                            rowList.add(TextUtility.noNull(cellAry[0].getContents()).trim());
                            if (cellAry.length > 1) {
                                if (!Utility.isEmpty(cellAry[1].getContents().trim())) {
                                    rowList.add(TextUtility.noNull(cellAry[1].getContents()).trim());
                                }
                                if (cellAry.length > 2) {
                                    String[] splitAry = TextUtility.splitString(TextUtility.noNull(cellAry[2].getContents()).trim(), "//");
                                    if (splitAry != null) {
                                        for (int k = 0; k < splitAry.length; k++) {
                                            rowList.add(splitAry[k]);
                                        }
                                    }
                                    if (cellAry.length > 3) {
                                        refNo = TextUtility.noNull(cellAry[3].getContents()).trim();
                                        if (cellAry.length > 4) {
                                            desc = TextUtility.noNull(cellAry[4].getContents()).trim();
                                        }
                                    }
                                }
                            }
                            dmsRoot = this.getDmsRootObject(sessionContainer, conn, TextUtility.noNull(cellAry[0].getContents()).trim());
                            if (!Utility.isEmpty(dmsRoot)) {
                                rootID = dmsRoot.getID();
                            } else {
                                dmsRoot = new DmsRoot();
                                dmsRoot.setRootName(TextUtility.noNull(cellAry[0].getContents()).trim());
                                dmsRoot.setRootType(DmsRoot.PUBLIC_ROOT);
                                dmsRoot.setLocID(new Integer(1));
                                dmsRoot.setStorageLimit(new Double(0));
                                dmsRoot.setOwnerID(sessionContainer.getUserRecordID());
                                dmsRoot = rootOperationManager.createPublicRoot(dmsRoot);
                                rootID = dmsRoot.getID();
                                countSuccess = new Integer(countSuccess.intValue() + 1);
                                log.debug("Root folder created............");
                                dmsRootDAObject = null;
                            }
                            this.startDocID = parentID;
                            this.intRootID = rootID;
                            processRow(rowList, refNo, desc);
                        }
                    } catch (ApplicationException e) {
                        countFail = new Integer(countFail.intValue() + 1);
                        failRows = failRows + MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "errors.dms.row_no") + String.valueOf(j + 1) + MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "errors.dms.error") + MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), e.getMsgCode()) + "<br>";
                    }
                }
            }
            resultList.add(0, "Y");
            resultList.add(1, countSuccess);
            resultList.add(2, countFail);
            resultList.add(3, failRows);
            conn.commit();
        } catch (Exception e) {
            log.error(e, e);
            resultList.add(0, "N");
            resultList.add(1, e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (Exception ignore) {
            } finally {
                conn = null;
            }
            try {
                is.close();
            } catch (Exception ignore) {
            } finally {
                is = null;
            }
            docRetrievalManager.release();
            docOperationManager.release();
        }
        return resultList;
    }

    public ArrayList loadZipFolderStructure(ActionMapping mapping, ActionForm form, HttpServletRequest request, Integer parentID, Integer rootID, File file) throws ApplicationException {
        SessionContainer sessionContainer = null;
        Connection conn = null;
        ArrayList resultList = new ArrayList();
        ArrayList tempList = new ArrayList();
        ZipFile zipFile = null;
        try {
            sessionContainer = this.getSessionContainer(request);
            conn = DataSourceFactory.getConnection();
            this.docRetrievalManager = new DocumentRetrievalManager(sessionContainer, conn);
            this.docOperationManager = new DocumentOperationManager(sessionContainer, conn);
            this.startDocID = parentID;
            this.intRootID = rootID;
            zipFile = new ZipFile(file);
            Enumeration zipEntries = zipFile.getEntries();
            String srtTemp = "";
            while (zipEntries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) zipEntries.nextElement();
                String zipName = entry.getName();
                if (!zipName.equals("/")) {
                    srtTemp = zipName.substring(zipName.length() - 1, zipName.length());
                    if ("/".equals(srtTemp)) {
                        tempList.add(zipName);
                    }
                }
            }
            if (!Utility.isEmpty(tempList) && tempList.size() > 0) {
                for (int i = 0; i < tempList.size(); i++) {
                    List rowList = new ArrayList();
                    String[] splitAry = TextUtility.splitString(TextUtility.noNull((String) tempList.get(i)).trim(), "/");
                    for (int j = 0; j < splitAry.length; j++) {
                        String fileName = splitAry[j];
                        if (!Utility.isEmpty(fileName)) {
                            rowList.add(splitAry[j]);
                        }
                    }
                    processRow(rowList, " ", " ");
                }
            }
            resultList.add(0, "Y");
            resultList.add(1, countSuccess);
            resultList.add(2, countFail);
            resultList.add(3, failRows);
            conn.commit();
        } catch (Exception e) {
            log.error(e, e);
            resultList.add(0, "N");
            resultList.add(1, e.getMessage());
        } finally {
            docRetrievalManager.release();
            docOperationManager.release();
            try {
                zipFile.close();
            } catch (Exception ex) {
            }
            try {
                conn.close();
            } catch (Exception ignore) {
            } finally {
                conn = null;
            }
        }
        return resultList;
    }

    public ArrayList loadZipRootFolderStructure(ActionMapping mapping, ActionForm form, HttpServletRequest request, File file) throws ApplicationException {
        Connection conn = null;
        SessionContainer sessionContainer = null;
        ArrayList tempList = new ArrayList();
        ArrayList resultList = new ArrayList();
        ZipFile zipFile = null;
        try {
            conn = this.getConnection(request);
            sessionContainer = this.getSessionContainer(request);
            this.docRetrievalManager = new DocumentRetrievalManager(sessionContainer, conn);
            this.docOperationManager = new DocumentOperationManager(sessionContainer, conn);
            DmsRoot dmsRoot = null;
            Integer rootID = null;
            Integer parentID = new Integer(0);
            DmsRootDAObject dmsRootDAObject = new DmsRootDAObject(sessionContainer, conn);
            RootOperationManager rootOperationManager = new RootOperationManager(this.getSessionContainer(request), conn);
            zipFile = new ZipFile(file);
            Enumeration zipEntries = zipFile.getEntries();
            String srtTemp = "";
            while (zipEntries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) zipEntries.nextElement();
                String zipName = entry.getName();
                if (!zipName.equals("/")) {
                    srtTemp = zipName.substring(zipName.length() - 1, zipName.length());
                    if ("/".equals(srtTemp)) {
                        tempList.add(zipName);
                    }
                }
            }
            if (!Utility.isEmpty(tempList)) {
                String rootName = TextUtility.formatFileName(this.zipUploadFile.getFileName());
                rootName = new String(rootName.getBytes(), "utf-8");
                String[] splitAry = TextUtility.splitString(rootName, ".");
                dmsRoot = this.getDmsRootObject(sessionContainer, conn, TextUtility.noNull(splitAry[0]).trim());
                if (!Utility.isEmpty(dmsRoot)) {
                    rootID = dmsRoot.getID();
                } else {
                    dmsRoot = new DmsRoot();
                    dmsRoot.setRootName(TextUtility.noNull(splitAry[0]).trim());
                    dmsRoot.setRootType(DmsRoot.PUBLIC_ROOT);
                    dmsRoot.setLocID(new Integer(1));
                    dmsRoot.setStorageLimit(new Double(0));
                    dmsRoot.setOwnerID(sessionContainer.getUserRecordID());
                    dmsRoot = rootOperationManager.createPublicRoot(dmsRoot);
                    rootID = dmsRoot.getID();
                    log.debug("Root zip folder created............");
                }
            }
            countSuccess = new Integer(0);
            this.startDocID = dmsRoot.getRootFolderID();
            this.intRootID = rootID;
            if (!Utility.isEmpty(tempList) && tempList.size() > 0) {
                for (int i = 0; i < tempList.size(); i++) {
                    List rowList = new ArrayList();
                    String[] splitAry = TextUtility.splitString(TextUtility.noNull((String) tempList.get(i)).trim(), "/");
                    for (int j = 0; j < splitAry.length; j++) {
                        rowList.add(splitAry[j]);
                    }
                    processRow(rowList, " ", " ");
                }
            }
            resultList.add(0, "Y");
            resultList.add(1, countSuccess);
            resultList.add(2, countFail);
            resultList.add(3, failRows);
            conn.commit();
        } catch (Exception e) {
            log.error(e, e);
            resultList.add(0, "N");
            resultList.add(1, e.getMessage());
        } finally {
            try {
                zipFile.close();
            } catch (Exception ex) {
            }
        }
        return resultList;
    }

    public ArrayList loadZipIncFileRootFolderStructure(ActionMapping mapping, ActionForm form, HttpServletRequest request, File file) throws ApplicationException {
        Connection conn = null;
        SessionContainer sessionContainer = null;
        ArrayList tempList = new ArrayList();
        ArrayList resultList = new ArrayList();
        ZipFile zipFile = null;
        try {
            conn = this.getConnection(request);
            sessionContainer = this.getSessionContainer(request);
            this.docRetrievalManager = new DocumentRetrievalManager(sessionContainer, conn);
            this.docOperationManager = new DocumentOperationManager(sessionContainer, conn);
            DmsRoot dmsRoot = null;
            Integer rootID = null;
            RootOperationManager rootOperationManager = new RootOperationManager(this.getSessionContainer(request), conn);
            String sExt = "";
            zipFile = new ZipFile(file);
            Enumeration e = zipFile.getEntries();
            while (e.hasMoreElements()) {
                org.apache.tools.zip.ZipEntry z = (org.apache.tools.zip.ZipEntry) e.nextElement();
                String zipName = "";
                String name = "";
                if (z.isDirectory()) {
                    name = z.getName();
                    name = name.substring(0, name.length() - 1);
                    zipName = name + "|" + DmsDocument.FOLDER_TYPE + "|" + "Null";
                } else {
                    name = z.getName();
                    sExt = TextUtility.getExtension(name);
                    long currentTime = new java.util.Date().getTime();
                    String folderName = SystemParameterFactory.getSystemParameter(SystemParameterConstant.DMS_EMAILARCHIVE_CACHEFOLDER_PATH) + currentTime + "." + sExt;
                    File f = new File(folderName);
                    f.createNewFile();
                    InputStream ins = zipFile.getInputStream(z);
                    FileOutputStream out = new FileOutputStream(f);
                    try {
                        byte[] by = new byte[1024];
                        int c;
                        while ((c = ins.read(by)) != -1) {
                            out.write(by, 0, c);
                        }
                    } catch (Exception ex) {
                    } finally {
                        out.close();
                        ins.close();
                    }
                    zipName = name + "|" + DmsDocument.DOCUMENT_TYPE + "|" + folderName;
                }
                tempList.add(zipName);
            }
            if (!Utility.isEmpty(tempList)) {
                String rootName = TextUtility.formatFileName(this.zipUploadFile.getFileName());
                rootName = new String(rootName.getBytes(), "utf-8");
                String[] splitAry = TextUtility.splitString(rootName, ".");
                dmsRoot = this.getDmsRootObject(sessionContainer, conn, TextUtility.noNull(splitAry[0]).trim());
                if (!Utility.isEmpty(dmsRoot)) {
                    rootID = dmsRoot.getID();
                } else {
                    dmsRoot = new DmsRoot();
                    dmsRoot.setRootName(TextUtility.noNull(splitAry[0]).trim());
                    dmsRoot.setRootType(DmsRoot.PUBLIC_ROOT);
                    dmsRoot.setLocID(new Integer(1));
                    dmsRoot.setStorageLimit(new Double(0));
                    dmsRoot.setOwnerID(sessionContainer.getUserRecordID());
                    dmsRoot = rootOperationManager.createPublicRoot(dmsRoot);
                    rootID = dmsRoot.getID();
                    log.debug("Root zip folder created............");
                }
            }
            countSuccess = new Integer(0);
            this.startDocID = dmsRoot.getRootFolderID();
            this.intRootID = rootID;
            if (!Utility.isEmpty(tempList) && tempList.size() > 0) {
                for (int i = 0; i < tempList.size(); i++) {
                    List rowList = new ArrayList();
                    String[] splitTempList = TextUtility.splitString(TextUtility.noNull((String) tempList.get(i)).trim(), "|");
                    String name = splitTempList[0];
                    String type = splitTempList[1];
                    String fileName = splitTempList[2];
                    String[] splitAry = TextUtility.splitString(name, "/");
                    for (int j = 0; j < splitAry.length; j++) {
                        rowList.add(splitAry[j]);
                    }
                    processRow(rowList, " ", " ", type, fileName, request);
                }
            }
            resultList.add(0, "Y");
            resultList.add(1, countSuccess);
            resultList.add(2, countFail);
            resultList.add(3, failRows);
            conn.commit();
        } catch (Exception e) {
            log.error(e, e);
            resultList.add(0, "N");
            resultList.add(1, e.getMessage());
        } finally {
            try {
                zipFile.close();
            } catch (Exception ignroe) {
            }
        }
        return resultList;
    }

    public ArrayList loadZipIncFileFolderStructure(ActionMapping mapping, ActionForm form, HttpServletRequest request, Integer parentID, Integer rootID, File file) throws ApplicationException {
        SessionContainer sessionContainer = null;
        Connection conn = null;
        ArrayList resultList = new ArrayList();
        ArrayList tempList = new ArrayList();
        countSuccess = new Integer(0);
        ZipFile zipFile = null;
        try {
            sessionContainer = this.getSessionContainer(request);
            conn = DataSourceFactory.getConnection();
            this.docRetrievalManager = new DocumentRetrievalManager(sessionContainer, conn);
            this.docOperationManager = new DocumentOperationManager(sessionContainer, conn);
            this.startDocID = parentID;
            this.intRootID = rootID;
            String sExt = "";
            zipFile = new ZipFile(file);
            Enumeration e = zipFile.getEntries();
            while (e.hasMoreElements()) {
                org.apache.tools.zip.ZipEntry z = (org.apache.tools.zip.ZipEntry) e.nextElement();
                String zipName = "";
                String name = "";
                if (z.isDirectory()) {
                    name = z.getName();
                    name = name.substring(0, name.length() - 1);
                    zipName = name + "|" + DmsDocument.FOLDER_TYPE + "|" + "Null";
                } else {
                    long currentTime = new java.util.Date().getTime();
                    name = z.getName();
                    sExt = TextUtility.getExtension(name);
                    String folderName = SystemParameterFactory.getSystemParameter(SystemParameterConstant.DMS_EMAILARCHIVE_CACHEFOLDER_PATH) + currentTime + "." + sExt;
                    File f = new File(folderName);
                    f.createNewFile();
                    InputStream ins = zipFile.getInputStream(z);
                    FileOutputStream out = new FileOutputStream(f);
                    try {
                        byte[] by = new byte[1024];
                        int c;
                        while ((c = ins.read(by)) != -1) {
                            out.write(by, 0, c);
                        }
                    } catch (Exception ex) {
                    } finally {
                        out.close();
                        ins.close();
                    }
                    zipName = name + "|" + DmsDocument.DOCUMENT_TYPE + "|" + folderName;
                }
                tempList.add(zipName);
            }
            if (!Utility.isEmpty(tempList) && tempList.size() > 0) {
                for (int i = 0; i < tempList.size(); i++) {
                    List rowList = new ArrayList();
                    String[] splitTempList = TextUtility.splitString(TextUtility.noNull((String) tempList.get(i)).trim(), "|");
                    String name = splitTempList[0];
                    String type = splitTempList[1];
                    String fileName = splitTempList[2];
                    String[] splitAry = TextUtility.splitString(name, "/");
                    for (int j = 0; j < splitAry.length; j++) {
                        rowList.add(splitAry[j]);
                    }
                    processRow(rowList, " ", " ", type, fileName, request);
                }
            }
            resultList.add(0, "Y");
            resultList.add(1, countSuccess);
            resultList.add(2, countFail);
            resultList.add(3, failRows);
            conn.commit();
        } catch (Exception e) {
            log.error(e, e);
            resultList.add(0, "N");
            resultList.add(1, e.getMessage());
        } finally {
            docRetrievalManager.release();
            docOperationManager.release();
            try {
                zipFile.close();
            } catch (Exception ignroe) {
            }
            try {
                conn.close();
            } catch (Exception ignroe) {
            } finally {
                conn = null;
            }
        }
        return resultList;
    }

    public void processRow(List rowList, String refNo, String desc) throws ApplicationException {
        Integer parentID = this.startDocID;
        for (int i = 0; i < rowList.size(); i++) {
            DmsDocument doc = this.docRetrievalManager.getDocumentByNameParentID((String) rowList.get(i), parentID);
            if (doc == null) {
                DmsDocument newFolder = new DmsDocument();
                newFolder.setRootID(intRootID);
                newFolder.setParentID(parentID);
                String documentName = (String) rowList.get(i);
                documentName = documentName.replaceAll("\n", "");
                documentName = documentName.replaceAll("\r", "");
                newFolder.setDocumentName(documentName);
                newFolder.setDocumentType(DmsDocument.FOLDER_TYPE);
                newFolder.setCreateType(DmsOperationConstant.DMS_CREATE_BY_SYSTEM);
                newFolder.setRecordStatus(GlobalConstant.RECORD_STATUS_ACTIVE);
                newFolder.setEffectiveStartDate(Utility.getCurrentTimestamp());
                newFolder.setItemStatus(GlobalConstant.RECORD_STATUS_ACTIVE);
                newFolder.setPriority(GlobalConstant.PRIORITY_MEDIUM);
                if (i == rowList.size() - 1) {
                    newFolder.setReferenceNo(refNo);
                    newFolder.setDescription(desc);
                }
                doc = this.docOperationManager.createFolder(newFolder);
                countSuccess = new Integer(countSuccess.intValue() + 1);
            }
            parentID = doc.getID();
        }
    }

    public void processRow(List rowList, String refNo, String desc, String type, String fileName, HttpServletRequest request) throws ApplicationException {
        Integer parentID = this.startDocID;
        for (int i = 0; i < rowList.size(); i++) {
            DmsDocument doc = this.docRetrievalManager.getDocumentByNameParentID((String) rowList.get(i), parentID);
            if (DmsDocument.DOCUMENT_TYPE.equals(type) && i == rowList.size() - 1) {
                File f = new File(fileName);
                if (doc == null) {
                    DmsDocument newFolder = new DmsDocument();
                    newFolder.setRootID(intRootID);
                    newFolder.setParentID(parentID);
                    String documentName = (String) rowList.get(i);
                    documentName = documentName.replaceAll("\n", "");
                    documentName = documentName.replaceAll("\r", "");
                    newFolder.setDocumentName(documentName);
                    newFolder.setDocumentType(DmsDocument.DOCUMENT_TYPE);
                    newFolder.setCreateType(DmsOperationConstant.DMS_CREATE_BY_SYSTEM);
                    newFolder.setRecordStatus(GlobalConstant.RECORD_STATUS_ACTIVE);
                    newFolder.setEffectiveStartDate(Utility.getCurrentTimestamp());
                    newFolder.setItemStatus(GlobalConstant.RECORD_STATUS_ACTIVE);
                    newFolder.setPriority(GlobalConstant.PRIORITY_MEDIUM);
                    newFolder.setItemSize(TextUtility.parseIntegerObj((String.valueOf(f.length()))));
                    DataInputStream inputStream;
                    try {
                        inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(fileName)));
                        doc = this.docOperationManager.createDocument(newFolder, inputStream);
                        countSuccess = new Integer(countSuccess.intValue() + 1);
                        this.commit(request);
                    } catch (FileNotFoundException e) {
                        log.error(e, e);
                    } catch (Exception ee) {
                        log.error(ee, ee);
                    }
                }
            } else {
                if (doc == null) {
                    DmsDocument newFolder = new DmsDocument();
                    newFolder.setRootID(intRootID);
                    newFolder.setParentID(parentID);
                    String documentName = (String) rowList.get(i);
                    documentName = documentName.replaceAll("\n", "");
                    documentName = documentName.replaceAll("\r", "");
                    newFolder.setDocumentName(documentName);
                    newFolder.setDocumentType(DmsDocument.FOLDER_TYPE);
                    newFolder.setCreateType(DmsOperationConstant.DMS_CREATE_BY_SYSTEM);
                    newFolder.setRecordStatus(GlobalConstant.RECORD_STATUS_ACTIVE);
                    newFolder.setEffectiveStartDate(Utility.getCurrentTimestamp());
                    newFolder.setItemStatus(GlobalConstant.RECORD_STATUS_ACTIVE);
                    newFolder.setPriority(GlobalConstant.PRIORITY_MEDIUM);
                    doc = this.docOperationManager.createFolder(newFolder);
                    countSuccess = new Integer(countSuccess.intValue() + 1);
                    this.commit(request);
                }
                parentID = doc.getID();
            }
        }
    }

    public DmsRoot getDmsRootObject(SessionContainer sessionContainer, Connection conn, String folderName) throws ApplicationException {
        DmsRoot dmsRoot = new DmsRoot();
        DmsRootDAObject dmsRootDAObject = new DmsRootDAObject(sessionContainer, conn);
        dmsRoot = (DmsRoot) dmsRootDAObject.getByFolderName(folderName);
        dmsRootDAObject = null;
        if (!Utility.isEmpty(dmsRoot)) {
            return dmsRoot;
        } else {
            return null;
        }
    }

    public ArrayList exportRootFolderStructure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        Connection conn = null;
        SessionContainer sessionContainer = null;
        ArrayList resultList = new ArrayList();
        try {
            conn = this.getConnection(request);
            sessionContainer = this.getSessionContainer(request);
            this.docRetrievalManager = new DocumentRetrievalManager(sessionContainer, conn);
            this.docOperationManager = new DocumentOperationManager(sessionContainer, conn);
            Integer rootID = null;
            Integer parentID = new Integer(0);
            DmsRootDAObject dmsRootDAObject = new DmsRootDAObject(sessionContainer, conn);
            DmsRoot dmsRoot = null;
            ArrayList publicRootList = (ArrayList) dmsRootDAObject.getAllPublicRootList();
            String rootFolderName = "";
            if (Utility.isEmpty(publicRootList)) {
            } else {
                for (int i = 0; i < publicRootList.size(); i++) {
                    dmsRoot = (DmsRoot) (publicRootList.get(i));
                    rootID = dmsRoot.getID();
                    rootFolderName = dmsRoot.getRootName();
                    DmsDocument doc = docRetrievalManager.getRootFolderByRootID(dmsRoot.getID());
                    if (!Utility.isEmpty(doc)) {
                        parentID = doc.getID();
                        ArrayList publicEndSubFolderList = (ArrayList) this.getFolderTreeByParentIDRootID(parentID, rootID, rootFolderName, sessionContainer, request);
                    }
                }
                this.exportExclFolder(mapping, request);
            }
            dmsRootDAObject = null;
        } catch (Exception e) {
            log.error(e, e);
        }
        return resultList;
    }

    public ArrayList exportFolderStructure(ActionMapping mapping, ActionForm form, HttpServletRequest request, Integer rootID, Integer parentID) throws ApplicationException {
        Connection conn = null;
        SessionContainer sessionContainer = null;
        ArrayList resultList = new ArrayList();
        try {
            conn = this.getConnection(request);
            sessionContainer = this.getSessionContainer(request);
            this.docRetrievalManager = new DocumentRetrievalManager(sessionContainer, conn);
            this.docOperationManager = new DocumentOperationManager(sessionContainer, conn);
            DmsDocument document = docRetrievalManager.getRootFolderByRootID(rootID);
            if (Utility.isEmpty(document)) {
            } else {
                String rootFolderName = document.getDocumentName();
                ArrayList publicEndSubFolderList = (ArrayList) this.getFolderTreeByParentIDRootID(parentID, rootID, rootFolderName, sessionContainer, request);
                this.exportExclFolder(mapping, request);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
        return resultList;
    }

    public void exportZipFolderStructure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        Connection conn = null;
        SessionContainer sessionContainer = null;
        ArrayList resultList = new ArrayList();
        try {
            conn = this.getConnection(request);
            sessionContainer = this.getSessionContainer(request);
            MaintFolderStructureLoaderForm actionForm = (MaintFolderStructureLoaderForm) form;
            this.docRetrievalManager = new DocumentRetrievalManager(sessionContainer, conn);
            this.docOperationManager = new DocumentOperationManager(sessionContainer, conn);
            RootRetrievalManager rootRetrievalManager = new RootRetrievalManager(sessionContainer, conn);
            String tempConvertedName = String.valueOf(System.currentTimeMillis());
            StringBuffer tempZipFile = new StringBuffer();
            DmsLocMaster locMaster = rootRetrievalManager.getTargetLocMasterByLocID(new Integer(1));
            String locMasterPath = locMaster.getLocPath();
            log.debug("locMasterPath = " + locMasterPath);
            tempZipFile = tempZipFile.append(locMasterPath).append("/").append(tempConvertedName).append(".zip");
            org.apache.tools.zip.ZipOutputStream out = new ZipOutputStream(new FileOutputStream(tempZipFile.toString()));
            Integer rootID = null;
            Integer parentID = null;
            if (GlobalConstant.PRIORITY_LOW.equals(actionForm.getZipExportFolderType())) {
                parentID = new Integer(actionForm.getZipExpParentID());
                rootID = new Integer(actionForm.getZipExpRootID());
            } else if (GlobalConstant.PRIORITY_MEDIUM.equals(actionForm.getZipExportFolderType())) {
                parentID = new Integer(0);
            }
            if (!Utility.isEmpty(rootID)) {
                String[] zipSourcePath = TextUtility.splitString(actionForm.getZipSourcePath(), "\\");
                out.putNextEntry(new ZipEntry("/"));
                this.Zip(parentID, rootID, request, out, zipSourcePath[zipSourcePath.length - 1]);
            } else {
                this.Zip(parentID, rootID, request, out, "");
            }
            request.getSession().setAttribute("zipExportFileName", tempZipFile.toString());
            out.close();
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    /**
   *  Method getFolderTree() - Get the dmsDocument list under the directory.
   *
   *  @param parentID                       The parentID of parent directory.
   *  @param rootID                         The rootID of root directory.
   *  @return                               The List of folders under that directory.
   *  @throws ApplicationException          Throws when list operation fault.
   */
    public synchronized List getFolderTreeByParentIDRootID(Integer parentID, Integer rootID, String urlString, SessionContainer sessionContainer, HttpServletRequest request) throws ApplicationException {
        Connection conn = null;
        List finalList = new ArrayList();
        try {
            conn = this.getConnection(request);
            List tmpList = null;
            DmsDocumentDAObject dmsDocumentDAO = new DmsDocumentDAObject(sessionContainer, conn);
            List documentList = dmsDocumentDAO.getFolderListByParentID(parentID, rootID, DmsDocument.FOLDER_TYPE, false);
            finalList.addAll(documentList);
            String subUrlString = "";
            if (parentID.intValue() == 0) {
                urlString = "";
            }
            if (!Utility.isEmpty(documentList)) {
                for (int i = 0; i < documentList.size(); i++) {
                    DmsDocument doc = (DmsDocument) documentList.get(i);
                    subUrlString = urlString;
                    if (!Utility.isEmpty(subUrlString)) {
                        subUrlString += "\\\\";
                    }
                    subUrlString += doc.getDocumentName();
                    tmpList = getFolderTreeByParentIDRootID(doc.getID(), doc.getRootID(), subUrlString, sessionContainer, request);
                    if (!Utility.isEmpty(tmpList)) {
                        finalList.addAll(tmpList);
                    }
                }
            } else {
                DmsDocument docTemp = (DmsDocument) dmsDocumentDAO.getObjectByID(parentID);
                urlString += "||";
                if (!Utility.isEmpty(docTemp.getReferenceNo())) {
                    urlString += docTemp.getReferenceNo();
                } else {
                    urlString += " ";
                }
                urlString += "||";
                if (!Utility.isEmpty(docTemp.getDescription())) {
                    urlString += docTemp.getDescription();
                } else {
                    urlString += " ";
                }
                urlStringList.add(urlString);
            }
            dmsDocumentDAO = null;
        } catch (Exception e) {
            log.error(e, e);
        }
        return finalList;
    }

    /**
   * Creates a cell and aligns it a certain way.
   *
   * @param wb        the workbook
   * @param row       the row to create the cell in
   * @param column    the column number to create the cell in
   * @param align     the alignment for the cell.
   */
    private static void createCell(HSSFWorkbook wb, HSSFRow row, short column, String value) {
        HSSFCell cell = row.createCell(column);
        cell.setEncoding(cell.ENCODING_UTF_16);
        cell.setCellValue(value);
    }

    public void exportExclFolder(ActionMapping mapping, HttpServletRequest request) throws ApplicationException {
        SessionContainer sessionContainer = this.getSessionContainer(request);
        Connection conn = this.getConnection(request);
        RootRetrievalManager rootRetrievalManager = new RootRetrievalManager(sessionContainer, conn);
        List folderTreeList = urlStringList;
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("Export Folder");
            sheet.setDefaultColumnWidth((short) 24);
            short rowNum = 0;
            HSSFRow row = sheet.createRow(rowNum++);
            createCell(wb, row, (short) 0, MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "dms.label.main_folder"));
            createCell(wb, row, (short) 1, MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "dms.label.sub_folder"));
            createCell(wb, row, (short) 2, MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "dms.label.resting_folders"));
            createCell(wb, row, (short) 3, MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "dms.label.reference_no"));
            createCell(wb, row, (short) 4, MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "dms.label.description"));
            row = sheet.createRow(rowNum++);
            for (int j = 0; j < folderTreeList.size(); j++) {
                String[] ExlFieldArray = TextUtility.splitString(folderTreeList.get(j).toString(), "||");
                String[] folderFieldArray = TextUtility.splitString(ExlFieldArray[0].toString(), "\\\\");
                String restintFloder = "";
                if (folderFieldArray != null) {
                    if (folderFieldArray.length > 2) {
                        restintFloder = "";
                        for (int k = 2; k < folderFieldArray.length; k++) {
                            if (k != 2) {
                                restintFloder += "//";
                            }
                            restintFloder += folderFieldArray[k];
                        }
                    }
                    createCell(wb, row, (short) 0, folderFieldArray[0].toString());
                    if (folderFieldArray.length > 1) {
                        createCell(wb, row, (short) 1, folderFieldArray[1].toString());
                    } else {
                        createCell(wb, row, (short) 1, "");
                    }
                    createCell(wb, row, (short) 2, restintFloder);
                    createCell(wb, row, (short) 3, ExlFieldArray[1].toString());
                    createCell(wb, row, (short) 4, ExlFieldArray[2].toString());
                    row = sheet.createRow(rowNum++);
                }
            }
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            wb.write(byteOut);
            DmsLocMasterDAObject locMasterDAO = new DmsLocMasterDAObject(sessionContainer, conn);
            DmsLocMaster locMaster = (DmsLocMaster) locMasterDAO.getObjectByID(new Integer(1));
            String locMasterPath = locMaster.getLocPath();
            byte[] excelData = byteOut.toByteArray();
            String path = locMasterPath + "/excel" + Math.random() + ".xls";
            FileOutputStream f = new FileOutputStream(path);
            InputStream in = null;
            try {
                in = new ByteArrayInputStream(excelData);
                int c;
                while ((c = in.read()) != -1) {
                    f.write(c);
                }
            } catch (Exception e) {
                log.error(e);
            } finally {
                try {
                    in.close();
                } catch (Exception ignore) {
                } finally {
                    in = null;
                }
                try {
                    f.close();
                } catch (Exception ignore) {
                } finally {
                    f = null;
                }
            }
            request.getSession().setAttribute("exlExportFileName", path);
            byteOut.close();
            sessionContainer = null;
            locMasterDAO = null;
            docRetrievalManager.release();
        } catch (Exception e) {
            log.error("export folder error.", e);
        }
    }

    public File gainFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, InputStream is) throws ApplicationException {
        SessionContainer sessionContainer = this.getSessionContainer(request);
        File file = null;
        try {
            long currentTime = new java.util.Date().getTime();
            Random rand = new Random();
            Double randomNum = new Double((Math.random() * 1000));
            String folderName = SystemParameterFactory.getSystemParameter(SystemParameterConstant.DMS_EMAILARCHIVE_CACHEFOLDER_PATH);
            File catcheFolder = new File(folderName);
            if (!catcheFolder.exists()) {
                catcheFolder.mkdir();
            }
            String fileName = sessionContainer.getUserRecordID() + "_" + currentTime + "_" + randomNum.intValue() + ".tmp";
            FileOutputStream objfile = new FileOutputStream(folderName + fileName);
            InputStream inputStream = is;
            int bufferSize = 8192;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            int allLength = 0;
            while ((length = inputStream.read(buffer, 0, bufferSize)) != -1) {
                objfile.write(buffer, 0, length);
            }
            String name = folderName + fileName;
            file = new File(name);
            objfile.flush();
            objfile.close();
        } catch (Exception e) {
            log.error(e, e);
        }
        return file;
    }

    public void Zip(Integer parentID, Integer rootID, HttpServletRequest request, ZipOutputStream out, String base) throws ApplicationException {
        Connection conn = null;
        SessionContainer sessionContainer = null;
        try {
            conn = this.getConnection(request);
            sessionContainer = this.getSessionContainer(request);
            List documentList = null;
            List docRootListTemp = null;
            List listTemp = null;
            DmsDocument docTemp = null;
            DmsRoot dmsRoot = null;
            DmsDocument doc = null;
            DmsDocumentDAObject dmsDocumentDAO = new DmsDocumentDAObject(sessionContainer, conn);
            DmsRootDAObject dmsRootDAObject = new DmsRootDAObject(sessionContainer, conn);
            if (!Utility.isEmpty(rootID)) {
                documentList = dmsDocumentDAO.getListByParentID(parentID, rootID, DmsDocument.FOLDER_TYPE);
                out.putNextEntry(new ZipEntry(base + "/"));
                base = base.length() == 0 ? "" : base + "/";
                for (int i = 0; i < documentList.size(); i++) {
                    docTemp = (DmsDocument) documentList.get(i);
                    parentID = docTemp.getID();
                    rootID = docTemp.getRootID();
                    Zip(parentID, rootID, request, out, base + docTemp.getDocumentName());
                }
            } else {
                out.putNextEntry(new ZipEntry(base + "/"));
                base = base.length() == 0 ? "" : base + "/";
                docRootListTemp = dmsRootDAObject.getAllPublicRootList();
                for (int j = 0; j < docRootListTemp.size(); j++) {
                    dmsRoot = (DmsRoot) docRootListTemp.get(j);
                    doc = docRetrievalManager.getRootFolderByRootID(dmsRoot.getID());
                    parentID = doc.getID();
                    rootID = doc.getRootID();
                    Zip(parentID, rootID, request, out, base + doc.getDocumentName());
                }
            }
            dmsDocumentDAO = null;
            dmsRootDAObject = null;
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    public void validatorXSL(Workbook workbook) throws ApplicationException {
        int numOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numOfSheets; i++) {
            Sheet sheet = workbook.getSheet(i);
            for (int j = 1; j < sheet.getRows(); j++) {
                Cell[] cellAry = sheet.getRow(j);
                try {
                    for (int k = 0; k < cellAry.length; k++) {
                        if (k == 1 && !Utility.isEmpty(cellAry[1].getContents())) {
                            if (Utility.isEmpty(cellAry[k - 1].getContents()) || cellAry[k - 1].getContents() == null) {
                                throw new ApplicationException(ErrorConstant.SYS_CHARACTER_ERROR);
                            }
                        } else if (k == 2 && !Utility.isEmpty(cellAry[2].getContents())) {
                            if (Utility.isEmpty(cellAry[k - 1].getContents()) || cellAry[k - 1].getContents() == null) {
                                throw new ApplicationException(ErrorConstant.SYS_CHARACTER_ERROR);
                            }
                        }
                        if (k == 2) {
                            String[] splitAry = TextUtility.splitString(TextUtility.noNull(cellAry[2].getContents()).trim(), "//");
                            for (int loop = 0; loop < splitAry.length; loop++) {
                                TextUtility.stringValidation(splitAry[loop]);
                            }
                        } else {
                            TextUtility.stringValidation(cellAry[k].getContents());
                        }
                    }
                } catch (ApplicationException ex) {
                    throw new ApplicationException(ErrorConstant.SYS_CHARACTER_ERROR);
                }
            }
        }
    }
}
