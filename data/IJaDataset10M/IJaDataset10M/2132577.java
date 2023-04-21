package com.patientis.client.reports;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JRViewer;
import com.patientis.business.common.ICustomController;
import com.patientis.business.output.DefaultInventoryItemOutputController;
import com.patientis.client.action.BaseAction;
import com.patientis.client.action.TableAction;
import com.patientis.client.common.BaseController;
import com.patientis.client.common.DynamicForm;
import com.patientis.client.service.reports.ReportService;
import com.patientis.framework.api.services.ReferenceServer;
import com.patientis.framework.controls.IControlPanel;
import com.patientis.framework.controls.IFormDisplay;
import com.patientis.framework.controls.ISControlPanel;
import com.patientis.framework.controls.ISPanel;
import com.patientis.framework.controls.forms.ISDialog;
import com.patientis.framework.controls.forms.ISFrame;
import com.patientis.framework.controls.listeners.ISActionListener;
import com.patientis.framework.controls.menus.ISMenuItem;
import com.patientis.framework.controls.menus.ISPopupMenu;
import com.patientis.client.common.PromptsController;
import com.patientis.framework.itext.PrintPDF;
import com.patientis.framework.locale.SystemUtil;
import com.patientis.framework.scripting.IReceiveMessage;
import com.patientis.framework.scripting.ISEvent;
import com.patientis.framework.scripting.ServiceUtility;
import com.patientis.framework.utility.FileSystemUtil;
import com.patientis.framework.utility.PrintUtility;
import com.patientis.framework.utility.ProcessUtil;
import com.patientis.model.common.ByteWrapper;
import com.patientis.model.common.Converter;
import com.patientis.model.common.DefaultBaseModel;
import com.patientis.model.common.IBaseModel;
import com.patientis.model.reference.ApplicationDialogReference;
import com.patientis.model.reference.ContentTypeReference;
import com.patientis.model.reference.ContextReference;
import com.patientis.model.reference.AccessReference;
import com.patientis.model.reference.ViewReference;
import com.patientis.model.clinical.FormModel;
import com.patientis.model.reports.ReportModel;
import com.patientis.model.security.ApplicationControlModel;
import com.patientis.model.system.FileModel;
import com.patientis.client.service.clinical.ClinicalService;
import com.patientis.client.service.common.BaseService;
import com.patientis.client.service.security.SecurityService;

/**
 * Report Builder primary function is as a mediator of actionable messages.
 * 
 */
public class ReportBuilderController extends BaseController {

    /**
	 * Last selected
	 */
    private ReportModel selectedReport = new ReportModel();

    /**
	 * Dynamic
	 */
    private ISPanel formPanel = new ISPanel(new BorderLayout());

    /**
	 * Use getInstance()
	 */
    private ReportBuilderController() {
    }

    /**
	 * Get a new instance of the controller
	 * 
	 * @return
	 */
    public static ReportBuilderController getInstance() {
        ReportBuilderController controller = new ReportBuilderController();
        ReportModel ReportModelVariable = new ReportModel();
        controller.setDefaultBaseModel(ReportModelVariable);
        return controller;
    }

    /**
	 * Start the application  
	 * 
	 * @throws Exception
	 */
    public void start() throws Exception {
        startFrame(ApplicationDialogReference.REPORTBUILDERDIALOG);
        loadAccessListResultsByRef(AccessReference.REPORTMODELLIST.getRefId());
        enableOkApplyCancelOnChange();
        setDisableOkUntilChanges(true);
        registerGlobalRefresh(ContextReference.REPORTBUILDERREPORTMODELLISTTABLE);
    }

    /**
	 * @see com.patientis.client.common.BaseController#getControlPanel(java.lang.Long)
	 */
    @Override
    public IControlPanel getControlPanel(Long viewId) throws Exception {
        IFormDisplay display = SecurityService.getApplicationViewFromCache(viewId);
        if (display.getViewRefId() == ViewReference.REPORTBUILDERLISTPANEL.getRefId()) {
            return new ISControlPanel(getFormMediator(), new DefaultBaseModel(), display, getFrame());
        } else if (display.getViewRefId() == ViewReference.EMPTYFORMTEMPLATE.getRefId()) {
            return new ISControlPanel(formPanel);
        } else {
            return super.getControlPanel(viewId);
        }
    }

    /**
	 * Form specific message mediation
	 */
    @Override
    protected void mediateMessages() {
        getFormMediator().register(new IReceiveMessage() {

            public boolean receive(ISEvent event, Object value) throws Exception {
                switch(event) {
                    case EXECUTEACTION:
                        BaseAction action = (BaseAction) value;
                        switch(action.getActionReference()) {
                            case REFRESHVIEW:
                                if (action.hasContext(ContextReference.REPORTBUILDERREPORTMODELLISTTABLE)) {
                                    loadAccessListResultsByRef(AccessReference.REPORTMODELLIST.getRefId());
                                }
                                return false;
                            case SYSTEMRUN:
                                runReport(action, false);
                                return true;
                            case SYSTEMEDITSETTINGS:
                                editReportSettings();
                                return true;
                            case SYSTEMPDF:
                                runReport(action, true);
                                return true;
                            case CANCELSUBMITFORM:
                                cancelCloseForm();
                                return true;
                            case APPLYFORMCHANGES:
                                save();
                                return true;
                            case CANCELFORMCHANGES:
                                clearReport(new ReportModel());
                                refreshAllTables();
                                return true;
                            case SYSTEMADD:
                                if (action.hasContext(ContextReference.REPORTBUILDERREPORTMODELLISTTABLE)) {
                                    return addReport();
                                }
                                return false;
                            case REPORTUPLOADJASPERFILE:
                                return uploadJasper();
                            case REPORTUPLOADJRXMLFILE:
                                return uploadJRXML();
                            case REPORTUPLOADPDFFILE:
                                return uploadPDF();
                            case REPORTUPLOADHTMLXSLT:
                                return uploadHtmlXslt();
                            case REPORTUPLOADPDFXSLT:
                                return uploadPdfXslt();
                            case SYSTEMEDITIMAGE:
                                editImage(action, getReport());
                                return true;
                            case SYSTEMCHANGEIMAGE:
                                changeImage(action, getReport());
                                return true;
                            case SYSTEMREMOVE:
                                if (action.hasContext(ContextReference.REPORTBUILDERREPORTMODELLISTTABLE)) {
                                    return removeReport();
                                } else {
                                    removeSelectedTableRows(action.getContextRefId());
                                    enableApplyOk();
                                    return true;
                                }
                        }
                        break;
                    case EXECUTETABLEACTION:
                        TableAction tableAction = (TableAction) value;
                        switch(tableAction.getActionReference()) {
                            case SYSTEMRIGHTCLICK:
                                if (tableAction.hasContext(ContextReference.REPORTBUILDERREPORTMODELLISTTABLE)) {
                                    selectedReport = tableAction.hasSelectedRows() ? (ReportModel) tableAction.getSelectedModel() : new ReportModel();
                                    popupReportSettings(tableAction);
                                    return true;
                                }
                                return false;
                            case TABLEROWSELECTED:
                                if (tableAction.hasContext(ContextReference.REPORTBUILDERREPORTMODELLISTTABLE)) {
                                    selectedReport = tableAction.hasSelectedRows() ? (ReportModel) tableAction.getSelectedModel() : new ReportModel();
                                    clearReport(selectedReport);
                                    disableOkApplyCancelOnChange();
                                    return false;
                                }
                        }
                }
                return false;
            }

            public String toString() {
                return "ReportBuilderController.mediateMessages";
            }
        }, this);
    }

    /**
	 * Get the selected order
	 * 
	 * @return
	 */
    private ReportModel getReport() {
        return (ReportModel) getDefaultBaseModel();
    }

    /**
	 * Clear the order and replaced with selected order
	 */
    private void clearReport(ReportModel selectedReport) throws Exception {
        getReport().copyAllFrom(selectedReport);
        disableOkApplyCancelOnChange();
    }

    /**
	 * Save the ReportModelVariable
	 * 
	 * @throws Exception
	 */
    private void save() throws Exception {
        validateControls();
        ReportService.store(getReport());
        loadAccessListResultsByRef(AccessReference.REPORTMODELLIST.getRefId());
        clearReport(new ReportModel());
        disableOkApplyCancelOnChange();
    }

    /**
	 * Add a slot
	 * 
	 * @param action
	 * @return
	 * @throws Exception
	 */
    private boolean addReport() throws Exception {
        ReportModel newReport = new ReportModel();
        clearReport(newReport);
        enableApplyOk();
        return true;
    }

    /**
	 * Remove the selected appointment type
	 * 
	 * @return
	 * @throws Exception
	 */
    private boolean removeReport() throws Exception {
        if (selectedReport.isNotNew()) {
            if (PromptsController.questionIsOKCancelOK(getFrame(), "Remove " + selectedReport.getDisplayListText(), "Remove")) {
                selectedReport.setDeleted();
                ReportService.store(selectedReport);
                loadAccessListResultsByRef(AccessReference.REPORTMODELLIST.getRefId());
            }
        }
        return false;
    }

    /**
	 * Upload jasper file
	 * 
	 * @return
	 * @throws Exception
	 */
    private boolean uploadJasper() throws Exception {
        FileModel file = ServiceUtility.browseStoreFile(getFrameOrDialog(), "Upload report compiled .jasper file", "jasper", ContentTypeReference.APPLICATIONJASPER.getRefId());
        getReport().setJasperFileId(file.getId());
        return true;
    }

    /**
	 * Upload jasper file
	 * 
	 * @return
	 * @throws Exception
	 */
    private boolean uploadPDF() throws Exception {
        FileModel file = ServiceUtility.browseStoreFile(getFrameOrDialog(), "Upload report .PDF file", "pdf", ContentTypeReference.APPLICATIONPDF.getRefId());
        getReport().setPdfFileId(file.getId());
        return true;
    }

    /**
	 * Upload jasper file
	 * 
	 * @return
	 * @throws Exception
	 */
    private boolean uploadJRXML() throws Exception {
        FileModel file = ServiceUtility.browseStoreFile(getFrameOrDialog(), "Upload report source .jrxml file", "jrxml", ContentTypeReference.APPLICATIONJRXML.getRefId());
        getReport().setJrxmlFileId(file.getId());
        return true;
    }

    /**
	 * Upload jasper file
	 * 
	 * @return
	 * @throws Exception
	 */
    private boolean uploadHtmlXslt() throws Exception {
        FileModel file = ServiceUtility.browseStoreFile(getFrameOrDialog(), "Upload report source .xsl file", "xsl", ContentTypeReference.TEXTPLAIN.getRefId());
        getReport().setXsltHtmlFileId(file.getId());
        return true;
    }

    /**
	 * Upload jasper file
	 * 
	 * @return
	 * @throws Exception
	 */
    private boolean uploadPdfXslt() throws Exception {
        FileModel file = ServiceUtility.browseStoreFile(getFrameOrDialog(), "Upload report source .xsl file", "xsl", ContentTypeReference.TEXTPLAIN.getRefId());
        getReport().setXsltPdfFileId(file.getId());
        return true;
    }

    /**
	 * 
	 * @param report
	 * @throws Exception
	 */
    public void popupReportSettings(TableAction action) throws Exception {
        if (selectedReport != null && selectedReport.getCustomControllerRef().isNotNew()) {
            ISPopupMenu popup = new ISPopupMenu(0L);
            popup.add(new ISMenuItem("Edit Settings", new ISActionListener() {

                @Override
                public void actionExecuted(ActionEvent e) throws Exception {
                    editReportSettings();
                }
            }) {

                private static final long serialVersionUID = 1L;
            });
            popup.show(action.getSource(), action.getMouseEvent().getX(), action.getMouseEvent().getY());
        }
    }

    /**
	 * 
	 * @throws Exception
	 */
    public void editReportSettings() throws Exception {
        if (selectedReport.isNotNew()) {
            ReportModel report = ReportService.getReportForReportRefId(selectedReport.getReportRef().getId());
            long settingsFormId = ServiceUtility.editCustomControllerSettings(getFrameOrDialog(), report.getDefaultSettingsFormId(), selectedReport.getCustomControllerRef().getId());
            if (settingsFormId > 0L && report.getDefaultSettingsFormId() == 0L) {
                report.setDefaultSettingsFormId(settingsFormId);
                ReportService.store(report);
                ReportModel savedReport = ReportService.getReportForReportRefId(report.getReportRef().getId());
                selectedReport.copyAllFrom(savedReport);
            }
        }
    }

    /**
	 * 
	 * @throws Exception
	 */
    public void runReport(BaseAction action, boolean pdf) throws Exception {
        if (selectedReport != null) {
            if (selectedReport.getCustomControllerRef().isNotNew()) {
                FormModel form = new FormModel();
                form.giveItem();
                form.givePatientQueryModel();
                if (selectedReport.getDefaultSettingsFormId() > 0) {
                    FormModel settings = ClinicalService.getForm(selectedReport.getDefaultSettingsFormId());
                    form.merge(form.getRecords(), settings.getRecords(), false);
                }
                ByteWrapper bytes = ReportService.getPdfReportForSettings(selectedReport.getReportRef().getId(), form);
                if (pdf) {
                    FileSystemUtil.createBinaryFile(PromptsController.promptSelectFile(action.getParentFrameOrDialog(getFrameOrDialog()), "Save pdf", "pdf"), bytes.getBytes());
                } else {
                    ServiceUtility.previewPDFFile(action.getParentFrameOrDialog(getFrameOrDialog()), bytes.getBytes());
                }
            } else {
                FormModel settingsForm = new FormModel();
                String xml = ReportService.getReportXmlForSettings(selectedReport.getReportRef().getId(), settingsForm);
                if (Converter.isNotEmpty(xml)) {
                    PromptsController.displayValue(getFrameOrDialog(), xml, "Report XML");
                }
            }
        }
    }

    /**
	 * 
	 * @throws Exception
	 */
    public void previewPDF() throws Exception {
        if (selectedReport != null) {
            ApplicationControlModel acm = new ApplicationControlModel();
            acm.setControlReportRef(selectedReport.getReportRef());
            ServiceUtility.generateReport(0, 0, acm, getFrameOrDialog(), new ArrayList<IBaseModel>());
        }
    }
}
