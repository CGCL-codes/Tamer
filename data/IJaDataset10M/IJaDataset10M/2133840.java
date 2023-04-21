package org.patientos.portal.business;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.chain.web.WebContext;
import org.patientos.portal.actions.BaseAction;
import org.patientos.portal.actions.WelcomeAction;
import org.patientos.portal.common.WebUser;
import com.patientis.business.clinical.FormTypeRules;
import com.patientis.business.common.ICustomController;
import com.patientis.business.reports.ContentUtil;
import com.patientis.business.reports.ProcessUtil;
import com.patientis.client.service.common.BaseService;
import com.patientis.framework.web.controls.Services;
import org.patientos.portal.forms.ClinicalForm;
import org.patientos.portal.wwwext.controls.ExtUtil;
import com.patientis.framework.api.services.ClinicalServer;
import com.patientis.framework.api.services.PatientServer;
import com.patientis.framework.api.services.ReferenceServer;
import com.patientis.framework.api.services.SecurityServer;
import com.patientis.framework.api.services.SystemServer;
import com.patientis.framework.api.standard.StandardFormTypeReference;
import com.patientis.framework.api.standard.StandardRecordItemReference;
import com.patientis.framework.controls.exceptions.ISConversionException;
import com.patientis.framework.logging.Log;
import com.patientis.model.clinical.FormModel;
import com.patientis.model.clinical.FormRecordDetailModel;
import com.patientis.model.clinical.FormRecordModel;
import com.patientis.model.clinical.FormTypeModel;
import com.patientis.model.clinical.RecordItemModel;
import com.patientis.model.common.BaseModel;
import com.patientis.model.common.Converter;
import com.patientis.model.common.DateTimeModel;
import com.patientis.model.common.DisplayModel;
import com.patientis.model.common.IBaseModel;
import com.patientis.model.common.IdentifierModel;
import com.patientis.model.common.ServiceCall;
import com.patientis.model.order.OrderModel;
import com.patientis.model.patient.PatientIdentifierModel;
import com.patientis.model.patient.PatientModel;
import com.patientis.model.patient.VisitModel;
import com.patientis.model.reference.ActionReference;
import com.patientis.model.reference.ApplicationSettingReference;
import com.patientis.model.reference.FormDisplayTypeReference;
import com.patientis.model.reference.FormGroupReference;
import com.patientis.model.reference.RecordItemReference;
import com.patientis.model.reference.RefModel;
import com.patientis.model.reference.VisitStatusReference;
import com.patientis.model.scheduling.AppointmentModel;
import com.patientis.model.security.ApplicationControlModel;
import com.patientis.model.security.ApplicationViewModel;

/**
 * @author gcaulton
 *
 */
public class FormsProcessing {

    public static FormModel createForm(List<ApplicationControlModel> controls, HttpServletRequest request, ServiceCall call) {
        FormModel form = new FormModel();
        Map<String, ApplicationControlModel> map = new Hashtable<String, ApplicationControlModel>();
        for (ApplicationControlModel acm : controls) {
            if (acm.getRecordItemRef().isNotNew()) {
                String id = "value(" + acm.getHtmlLabel() + ")";
                map.put(id, acm);
                Log.debug("map has " + id);
            }
        }
        Enumeration e1 = request.getParameterNames();
        while (e1.hasMoreElements()) {
            String pname = e1.nextElement().toString();
            Log.debug("check " + pname);
            try {
                if (map.containsKey(pname)) {
                    String pvalue = request.getParameter(pname);
                    if (Converter.isNotEmpty(pvalue)) {
                        ApplicationControlModel control = map.get(pname);
                        BaseModel base = control.getBaseModel(form, call);
                        if (base instanceof FormRecordModel) {
                            FormRecordModel record = (FormRecordModel) base;
                            if (record.getDataTypeRef().isNew()) {
                                long dataTypeRefId = ClinicalServer.getDataTypeForRecordItemRefId(control.getRecordItemRefId(), call);
                                record.setDataTypeRef(new DisplayModel(dataTypeRefId));
                            }
                            record.setValue(pvalue);
                            form.getRecords().add(record);
                        }
                    } else {
                        Log.debug("empty " + pname);
                    }
                } else {
                    Log.debug("map no match " + pname);
                }
            } catch (Exception ex) {
                Log.error(ex.getMessage());
            }
        }
        Log.debug(form.getRecordDisplay());
        return form;
    }

    /**
     * 
     * @param form
     * @throws Exception
     */
    public static FormModel createForm(ClinicalForm clinicalForm, HttpServletRequest request, FormTypeModel formType, long patientId, long visitId, BaseAction action, FormModel errors, ServiceCall call) throws Exception {
        FormModel form = null;
        if (formType.isNotNew()) {
            List<ApplicationControlModel> controls = Services.getOrderBean().getExpandedControlsForFormType(formType.getFormTypeRefId(), call);
            Map<String, Boolean> passedParams = new Hashtable<String, Boolean>();
            Enumeration e1 = request.getParameterNames();
            while (e1.hasMoreElements()) {
                String aname = Converter.convertDisplayString(e1.nextElement());
                passedParams.put(aname, Boolean.TRUE);
            }
            form = Services.getClinicalBean().prepareNewForm(patientId, visitId, formType.getFormTypeRefId(), call);
            if (visitId > 0L) {
                PatientModel patient = Services.getPatientBean().getPatientForVisitId(visitId, call);
                if (passedParams.containsKey("formdisplaytype")) {
                    long formDisplayTypeRefId = Converter.convertLong(request.getParameter("formdisplaytype"));
                    if (formDisplayTypeRefId == FormDisplayTypeReference.SYSTEMREGISTRATIONVISITNEW.getRefId()) {
                        VisitModel visit = new VisitModel();
                        visit.setVisitStatusRef(new DisplayModel(VisitStatusReference.ACTIVE.getRefId()));
                        patient.setVisit(visit);
                        Log.debug("reset visit");
                    }
                }
                form.addQueryModel(patient);
            }
            for (ApplicationControlModel acm : controls) {
                Object formValue = null;
                try {
                    if (acm.getCustomControllerRef().isNotNew()) {
                        ICustomController controller = Converter.convertCustomController(acm.getCustomControllerRef().getId());
                        controller.portalPreSave(form, action, errors, request, call);
                    }
                    formValue = PropertyUtils.getProperty(clinicalForm, "value(" + acm.getHtmlLabel() + ")");
                    String format = acm.getApplicationControlFormat();
                    IBaseModel baseModel = acm.getBaseModel(form, call);
                    if (Converter.isNotEmpty(format) && (format.contains("M") || format.contains("H") || format.contains("h"))) {
                        formValue = getDateTimeValue(acm, baseModel, request, formValue);
                    }
                    if (baseModel != null) {
                        if (formValue != null && Converter.isNotEmpty(Converter.convertDisplayString(formValue)) && !(formValue.toString().equals("null"))) {
                            Log.debug(baseModel.getClass() + " formValue " + formValue.getClass().getName());
                            Log.debug(">>>>>>>>>>" + acm.getApplicationControlName() + " = " + Converter.convertDisplayString(formValue));
                            baseModel.setValue(acm.getModel(), formValue);
                            Log.debug(">>> saved " + acm.getModel() + " " + baseModel.getValue(acm.getModel()));
                            FormRecordModel record = new FormRecordModel();
                            record.setValue(acm.getModel(), formValue);
                            Log.debug("??? " + acm.getModel() + " " + record.getValue(acm.getModel()));
                        } else {
                            if (passedParams.containsKey("value(" + acm.getHtmlLabel() + ")")) {
                                Log.debug("XXXXXXXXXXXx wiping " + acm.getApplicationControlName());
                                baseModel.setValue(acm.getModel(), null);
                            }
                        }
                    }
                    Log.debug(formValue);
                } catch (ISConversionException cex) {
                    Log.error(cex.getMessage() + " " + acm.getApplicationControlName() + " to " + formValue);
                } catch (Exception ex) {
                    Log.error(ex.getMessage() + " " + acm.getApplicationControlName() + " to " + formValue);
                }
            }
            updateDataTypeRecordDt(form, formType.getFormTypeRefId(), patientId, visitId, call);
            Log.debug(form.getRecordDisplay());
        } else {
            Log.warn("formTypeRefId not found " + formType.getFormTypeRefId());
        }
        for (AppointmentModel appt : form.getAppointments()) {
            Log.debug("...appt.." + appt.getAppointmentTypeRef() + " " + appt.getAppointmentStartDt());
        }
        for (OrderModel o : form.getOrders()) {
            Log.debug("...order.." + o.getOrderTemplateRef() + " ");
        }
        return form;
    }

    /**
     * 
     * @param acm
     * @param baseModel
     * @return
     * @throws Exception
     */
    private static Object getDateTimeValue(ApplicationControlModel acm, IBaseModel baseModel, HttpServletRequest request, Object originalValue) throws Exception {
        String format = acm.getApplicationControlFormat();
        Object formValue = originalValue;
        if (!format.contains("M") && !format.contains("d") && !format.contains("y")) {
            String textTime = request.getParameter("value(" + acm.getHtmlLabel() + ")");
            DateTimeModel date = Converter.convertDateTimeModel(baseModel.getValue(acm.getModel()));
            if (date == null || date.isNull()) {
                date = DateTimeModel.get1900();
            }
            Log.debug("adding time (" + textTime + ") to " + date);
            DateTimeModel time = Converter.convertTime(date, textTime);
            Log.debug("time is " + time);
            formValue = time;
        } else if (format.contains("M")) {
            String time = Converter.convertDateTimeModel(baseModel.getValue(acm.getModel())).toString(DateTimeModel.getDefaultTimeFormat());
            if (Converter.isEmpty(time)) {
                time = DateTimeModel.getNow().getStartOfDay().toString(DateTimeModel.getDefaultTimeFormat());
            }
            DateTimeModel date = Converter.convertDateTimeModel(request.getParameter("value(" + acm.getHtmlLabel() + ")"));
            Log.debug("adding date (" + date + ") to " + time);
            DateTimeModel datetime = Converter.convertTime(date, time);
            Log.debug("time is " + datetime);
            formValue = datetime;
        }
        return formValue;
    }

    /**
	 * 
	 * @param form
	 * @throws Exception
	 */
    public static void updateDataTypeRecordDt(FormModel form, long formTypeRefId, long patientId, long visitId, ServiceCall call) throws Exception {
        if (form.getPatientId() == 0L) {
            form.setPatientId(patientId);
        }
        if (form.getVisitId() == 0L) {
            form.setVisitId(visitId);
        }
        if (form.getFormTypeRef().isNew()) {
            form.setFormTypeRef(new DisplayModel(formTypeRefId));
        }
        for (FormRecordModel record : form.getRecords()) {
            record.setSuspendBaseFirePropertyChange(true);
            if (record.getDataTypeRef().isNew() && record.getRecordItemRef().isNotNew()) {
                long dataTypeRefId = ClinicalServer.getDataTypeForRecordItemRefId(record.getRecordItemRef().getId(), call);
                record.setDataTypeRef(ReferenceServer.getDisplayModel(dataTypeRefId));
            }
            if (record.getRecordDt().isNull()) {
                record.setRecordDt(form.getFormDt());
            }
            if (record.getPatientId() == 0L) {
                record.setPatientId(patientId);
            }
            if (record.getVisitId() == 0L) {
                record.setVisitId(visitId);
            }
            for (FormRecordDetailModel detail : record.getDetails()) {
                if (detail.getDataTypeRef().isNew() && detail.getRecordItemRef().isNotNew()) {
                    long dataTypeRefId = ClinicalServer.getDataTypeForRecordItemRefId(detail.getRecordItemRef().getId(), call);
                    detail.setDataTypeRef(ReferenceServer.getDisplayModel(dataTypeRefId));
                }
            }
        }
    }

    /**
	  * 
	  * @param action
	  * @param formType
	  * @param form
	  * @param messages
	  * @param call
	  * @return
	  * @throws Exception
	  */
    public static String getHtmlForForm(long passedApplicationControlId, HttpServletRequest request, BaseAction action, FormTypeModel passedFormType, FormModel form, long toolbarFormTypeRefId, boolean useTemplate, long visitId, boolean embedded, ServiceCall call, boolean tabDisplay) throws Exception {
        StringBuffer js = new StringBuffer(1024);
        FormTypeModel formType = FormTypeRules.getFormType(passedFormType.getFormDisplayTypeRef().getId(), passedFormType.getFormTypeRefId());
        if (formType.getFormTypeRef().isId(StandardFormTypeReference.StandardWebApplicationMainApplicationPage) || formType.getFormTypeRefId() == 0L) {
            WebUser webuser = action.getWebUser(request.getSession());
            formType = ClinicalServer.getFormTypeForFormTypeRefId(webuser.getLastTabFormTypeRefId(call));
            Log.debug("REDIRECTING........." + formType.getFormTypeRef().getDisplay() + " user " + webuser.getUser().getUserLogin() + " patient " + webuser.getPortalUser().getPortalUserLogin() + " patientId " + form.getPatientId() + " visit " + visitId);
            if (visitId == 0L && form.getPatientId() == 0L) {
                if (webuser.getPortalUser().isNotNew()) {
                    form.setPatientId(webuser.getCurrentPatient().getId());
                    form.setVisitId(webuser.getCurrentPatient().getViewVisitQueryModel().getId());
                }
            }
            Log.debug(formType.getFormTypeRefId());
        }
        ApplicationControlModel originalControl = new ApplicationControlModel();
        if (passedApplicationControlId > 0L) {
            originalControl = SecurityServer.getApplicationControl(passedApplicationControlId);
        }
        HttpSession session = request.getSession();
        long viewRefId = Services.getSecurityBean().getViewRefIdForFormTypeRefId(formType.getFormTypeRefId(), call);
        ApplicationViewModel view = Services.getSecurityBean().getApplicationViewByRef((int) viewRefId, call);
        for (ApplicationControlModel control : view.getSortedControls()) {
            try {
                String parameterValue = request.getParameter(control.getHtmlLabel());
                if (request.getParameter(control.getHtmlLabel()) != null) {
                    control.getBaseModel(form, call).setValue(control.getModel(), parameterValue);
                    Object valueSet = control.getBaseModel(form, call).getValue(control.getModel());
                    if (valueSet instanceof DisplayModel && Converter.isEmpty(((DisplayModel) valueSet).getDisplay())) {
                        if (((DisplayModel) valueSet).getId() > 0L) {
                            control.getBaseModel(form, call).setValue(control.getModel(), ReferenceServer.getDisplayModel(((DisplayModel) valueSet).getId()));
                        }
                    }
                }
            } catch (Exception ex) {
                Log.exception(ex);
            }
        }
        String toolbarhtml = "";
        String tabshtml = "";
        if (!embedded) {
            ToolbarProcessing toolbar = new ToolbarProcessing();
            List<ApplicationControlModel> controls = Services.getClinicalBean().getSortedControlsByFormType(toolbarFormTypeRefId, call);
            List<ApplicationControlModel> toolbarcontrols = getTabOrToolbarControls(controls, false);
            toolbarhtml = toolbar.getToolbarHtml(toolbarcontrols, call);
            if (visitId > 0L) {
                toolbarhtml = toolbarhtml + "<br/> \n<div id='breakline' class='tabline'>&nbsp;</div>\n" + getPatientHeader(visitId, call);
            }
            tabshtml = toolbar.getTabsHtml(getTabOrToolbarControls(controls, true), formType.getFormTypeRefId(), call);
            js.append("\n");
            for (ApplicationControlModel tbc : toolbarcontrols) {
                if (!tbc.isSeparator()) {
                    js.append(ExtUtil.getButtonJavascript(tbc, true, true));
                }
            }
        }
        String errors = "";
        if (action.hasErrors(session)) {
            errors = getErrorDisplay(action.getErrors(session).getSortedRecords());
        }
        DefaultHtmlForm htmlform = new DefaultHtmlForm("", form, view, action, request);
        long acmid = Converter.convertInteger(request.getParameter("acm"));
        if (acmid > 0L) {
            ApplicationControlModel callingControl = SecurityServer.getApplicationControl(acmid);
            if (callingControl.getDefaultActionRefId() == ActionReference.SYSTEMPREPAREFORM.getRefId() || callingControl.getDefaultActionRefId() == ActionReference.SYSTEMPREPAREFROMSELECTION.getRefId()) {
                htmlform.setDisplayOkCancel(true);
            }
        }
        FormModel formTypeSettings = new FormModel();
        if (embedded) {
            htmlform.setDisplayOkCancel(false);
        }
        if (formType.getSettingsFormId() > 0L) {
            formTypeSettings = ClinicalServer.getFormFromCache(formType.getSettingsFormId(), call);
            if (formTypeSettings.hasRecordItemRefId(40034956L)) {
                htmlform.setDisplayOkCancel(formTypeSettings.getBooleanValueForRecordItem(40034956L));
            }
        }
        ProcessUtil util = new ProcessUtil(htmlform);
        htmlform.start(true);
        FormModel listSettingsForm = ContentUtil.createSettingsForm(formType.getFormTypeRefId());
        listSettingsForm.setPatientId(form.getPatientId());
        listSettingsForm.setVisitId(form.getVisitId());
        if (form.hasPatientQueryModel()) {
            Log.debug(form.getQueryModels().size() + " " + form.getPatientQueryModel().getId());
        } else {
            Log.debug("no patient for " + form.getId() + " p" + form.getPatientId());
        }
        util.processForm(listSettingsForm, form, 0L, BaseService.createServiceCall());
        String formend = "<input type=\"hidden\" name=\"value(formTypeRefId)\" value=\"" + formType.getFormTypeRefId() + "\"/>";
        if (visitId > 0L) {
            formend += "<input type=\"hidden\" name=\"value(vid)\" value=\"" + visitId + "\"/>";
            formend += "<input type=\"hidden\" name=\"vid\" value=\"" + visitId + "\"/>";
        }
        if (embedded) {
            formend += "<input type=\"hidden\" name=\"value(embedded)\" value=\"1\"/>";
        }
        if (originalControl.getFormDisplayTypeRef().isNotNew()) {
            formend += "<input type=\"hidden\" name=\"formdisplaytype\" value=\"" + originalControl.getFormDisplayTypeRef().getId() + "\"/>";
        }
        htmlform.setFormHiddenFields(formend);
        String formhtml = htmlform.end(true);
        js.append(htmlform.getJavascript());
        String html = getTemplate(formType, formTypeSettings, js.toString(), formhtml, toolbarhtml, tabshtml, errors, embedded, call);
        if (!useTemplate) {
            html = html.replace("<head>", "<head><base target=\"self\"/>");
        }
        action.setErrors(session, null);
        return html.trim();
    }

    /**
     * 
     * @param htmlTemplate
     * @return
     */
    public static String getErrorDisplay(List<FormRecordModel> records) {
        StringBuffer sb = new StringBuffer();
        sb.append("<p style=\"color:red\">");
        boolean first = true;
        for (FormRecordModel record : records) {
            if (!first) {
                sb.append("<br>");
            }
            sb.append(record.getValueString());
            first = false;
        }
        sb.append("</p>");
        return sb.toString();
    }

    /**
     * 
     * @param javascript
     * @param html
     * @return
     */
    public static String getTemplate(FormTypeModel formType, FormModel formTypeSettings, String javascript, String formhtml, String toolbarhtml, String tabshtml, String errors, boolean embedded, ServiceCall call) throws Exception {
        String html = "<html>Nothing found</html>";
        FormModel settingsForm = WelcomeAction.getPortalUserSettingsForm(call);
        long fileId = settingsForm.giveFormRecord(RecordItemReference.SYSTEMPORTALDEFAULTFORMSTYLESHEETFILE.getRefId()).getValueInt();
        Log.debug("form " + settingsForm.getId() + " file " + fileId + " form type " + formType.getFormTypeRefId());
        String htmlTemplate = getFormTemplate(formType, formTypeSettings, call);
        if (Converter.isEmpty(htmlTemplate)) {
            if (fileId > 0L) {
                htmlTemplate = new String(Services.getSystemBean().getFileContents(fileId, call).getBytes(), "UTF-8");
            } else {
                htmlTemplate = getDefaultTemplate();
            }
        }
        if (htmlTemplate.contains("<!--patientos_messages-->")) {
            htmlTemplate = htmlTemplate.replace("<!--patientos_messages-->", errors);
        } else {
            htmlTemplate = htmlTemplate.replace("<!--patientos_form-->", errors + "<!--patientos_form-->");
        }
        if (htmlTemplate.contains("<!--patientos_toolbar-->")) {
            htmlTemplate = htmlTemplate.replace("<!--patientos_toolbar-->", toolbarhtml);
        } else {
            if (!embedded) {
                htmlTemplate = htmlTemplate.replace("<!--patientos_form-->", toolbarhtml + "<!--patientos_form-->");
            }
        }
        if (htmlTemplate.contains("<!--patientos_tabs-->")) {
            htmlTemplate = htmlTemplate.replace("<!--patientos_tabs-->", tabshtml);
        } else {
            if (!embedded) {
                htmlTemplate = htmlTemplate.replace("<!--patientos_form-->", tabshtml + "<!--patientos_form-->");
            }
        }
        if (htmlTemplate.contains("<!--patientos_javascript-->")) {
            htmlTemplate = htmlTemplate.replace("<!--patientos_javascript-->", javascript);
        } else {
            htmlTemplate = htmlTemplate.replace("<!--patientos_form-->", "<script>" + javascript + "</script><br><!--patientos_form-->");
        }
        if (htmlTemplate.contains("<!--patientos_form-->")) {
            html = htmlTemplate.replace("<!--patientos_form-->", formhtml);
        } else {
            html = "<html>" + formhtml + "</html>";
        }
        if (htmlTemplate.contains("<!--patientos_logo-->")) {
            html = html.replace("!--patientos_logo-->", "<img src=\"images/logo.png\" width=\"245\" height=\"65\" alt=\"logo\" />");
        }
        if (html.contains("<!--title-->")) {
            html = html.replace("<!--title-->", formType.getLabelOrDisplay());
        }
        html = html.replace("<a href=\"http://www.patientos.com\">PatientOS Inc.</a>", "");
        return html;
    }

    /**
     * 
     * @param formType
     * @return
     * @throws Exception
     */
    public static String getFormTemplate(FormTypeModel formType, FormModel formTypeSettings, ServiceCall call) throws Exception {
        String html = "";
        if (formTypeSettings.isNotNew()) {
            if (formTypeSettings.hasValueForRecordItem(40034932L)) {
                html = formTypeSettings.getStringValueForRecordItem(40034932L);
            } else if (formTypeSettings.hasValueForRecordItem(40034931L)) {
                long fileId = formTypeSettings.getIntValueForRecordItem(40034931L);
                html = new String(SystemServer.getFileContents(fileId, call).getBytes(), "UTF-8");
            }
            for (FormRecordModel record : formTypeSettings.getRecords()) {
                if (record.getRecordItemRef().getDisplay().startsWith("Standard Web Page Substitute")) {
                    String sub = record.getRecordItemRef().getDisplay().replace("Standard Web Page Substitute ", "").replace(" (Standard Web Settings)", "");
                    html = html.replace("<!--" + sub.toLowerCase() + "-->", record.getValueString());
                }
            }
        }
        return html;
    }

    public static List<ApplicationControlModel> getTabOrToolbarControls(List<ApplicationControlModel> controls, boolean tab) {
        List<ApplicationControlModel> tabControls = new ArrayList<ApplicationControlModel>();
        List<ApplicationControlModel> toolbarControls = new ArrayList<ApplicationControlModel>();
        for (ApplicationControlModel control : controls) {
            if (control.getDefaultActionRefId() == ActionReference.SYSTEMDISPLAYINTERACTIVEFORM.getRefId() && control.isRequired()) {
                if (control.getActionFormTypeRef().isNotNew()) {
                    tabControls.add(control);
                }
            } else {
                toolbarControls.add(control);
            }
        }
        if (tab) {
            return tabControls;
        } else {
            return toolbarControls;
        }
    }

    /**
     * 
     * @return
     */
    public static String getDefaultTemplate() {
        StringBuffer sb = new StringBuffer(128);
        sb.append("<html>\n" + "<head>\n" + "<link rel=\"stylesheet\" type=\"text/css\" href=\"ext/resources/css/ext-all.css\"> \n" + "<script type=\"text/javascript\" src=\"ext/adapter/ext/ext-base.js\"></script> \n" + "<script type=\"text/javascript\" src=\"ext/ext-all-debug.js\"></script>  \n" + "<script type=\"text/javascript\">    \n" + "Ext.BLANK_IMAGE_URL = 'ext/resources/images/default/s.gif'; \n" + "Ext.onReady(function(){\n" + "    Ext.QuickTips.init();\n" + "    Ext.form.Field.prototype.msgTarget = 'side';\n" + " <!--patientos_javascript-->\n" + " });\n" + " </script>\n" + " <body>\n" + " <!--patientos_form-->\n" + " </body>\n" + "</html>\n");
        return sb.toString();
    }

    /**
     * 
     * @return
     */
    public static String getPatientHeader(long visitId, ServiceCall call) throws Exception {
        PatientModel patient = PatientServer.getPatientForVisitId(visitId);
        String patientName = patient.getPatientName();
        String sex = patient.getGenderRef().getDisplay();
        String dob = patient.getBirthDt().toString(DateTimeModel.getDefaultShortDateYearFormat());
        String mrn = Converter.convertDisplayString(patient.giveIdentifier(call.getMrnIdentifierSourceRefId()).getIdvalue());
        String weight = "Unknown";
        String bsa = "N/A";
        String age = patient.getAge();
        String location = "";
        String registered = "";
        String visittype = "";
        if (patient.hasVisit()) {
            if (Converter.isNotEmpty(patient.getVisit().getLocationRef().getDisplay())) {
                location = patient.getVisit().getLocationRef().getDisplay();
            }
            if (Converter.isNotEmpty(patient.getVisit().getVisitTypeRef().getDisplay())) {
                visittype = patient.getVisit().getVisitTypeRef().getDisplay();
            }
            if (patient.getVisit().getAdmitDt().isNotNull()) {
                registered = patient.getVisit().getAdmitDt().toString(DateTimeModel.getDefaultShortDateTimeFormat());
            }
        }
        String s = "<table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" align=\"left\" border=\"0\">";
        s = s + "<tbody><tr><td valign=\"bottom\" width=\"300\"><font face=\"Verdana\" size=\"6\"><strong>" + patientName;
        s = s + "</strong></font></td><td valign=\"bottom\" width=\"40\"><p align=\"right\"><font color=\"#cccccc\"><small>DOB:&nbsp;</small></font> </p></td><td valign=\"bottom\" width=\"50\"><b>" + dob;
        s = s + "</b></td><td valign=\"bottom\" width=\"40\"><p align=\"right\"><strong>" + sex + " &nbsp;</strong></p></td><td valign=\"bottom\" width=\"50\"><b>" + age;
        s = s + "</b></td><td valign=\"bottom\" width=\"40\"><p align=\"right\"><font color=\"#cccccc\"><small></small></font> </p></td><td valign=\"bottom\" width=\"50\"><b>";
        s = s + "</b></td></tr><tr><td valign=\"bottom\"><font color=\"#cccccc\"><small>MRN:&nbsp;</small></font><b>" + mrn;
        s = s + "</b></td><td valign=\"bottom\"><p align=\"right\"><font color=\"#cccccc\"><small>Visit:&nbsp;</small></font></p></td><td valign=\"bottom\"><b>" + " &nbsp;" + visittype;
        s = s + "</b></td><td valign=\"bottom\"><p align=\"right\"><font color=\"#cccccc\"><small>Location:</small></font></p></td><td valign=\"bottom\"><b>" + location + " &nbsp;" + registered;
        s = s + "</b></td><td valign=\"bottom\"></td><td></td></tr></tbody></table>";
        return s;
    }
}
