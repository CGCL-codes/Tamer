package org.project.trunks.webform;

import org.project.trunks.utilities.*;
import org.project.trunks.navigation.*;
import javax.servlet.http.*;
import java.util.*;
import org.project.trunks.xml.*;
import org.project.trunks.factory.*;
import org.project.trunks.user.*;
import org.project.trunks.user.*;
import org.project.trunks.generation.GenXml;
import org.xml.sax.*;
import org.apache.commons.logging.*;
import org.apache.commons.logging.Log;
import java.io.*;

public class CompleteForm extends WebForm implements IPage, IWebForm {

    /**
   * Logger
   */
    private static Log log = LogFactory.getLog(CompleteForm.class);

    protected String errorMessage;

    protected String jspPage;

    protected String label;

    Vector vWebForm;

    Vector vWebFormProperties;

    protected int wfe_height = 200;

    protected int wfe_width = 60;

    public CompleteForm() {
        vWebForm = new Vector();
        vWebFormProperties = new Vector();
    }

    public CompleteForm(XmlObject xo, HttpServletRequest request) {
        this();
        init(xo, request);
    }

    /**
   * Load values for this Xmlselect layer object
   * @param xmlObject
   */
    public void init(XmlObject xo, HttpServletRequest request) {
        try {
            IGenericUserSession userSession = (IGenericUserSession) SessionManager.getInstance().getSession(request);
            errorMessage = "";
            this.ID = xo.getID();
            connect_id = xo.getChildValue("CONNECT_ID");
            label = xo.getAttributeValue("LABEL");
            width = StringUtilities.getNString(xo.getAttributeValue("WIDTH"));
            height = StringUtilities.getNString(xo.getAttributeValue("HEIGHT"));
            try {
                wfe_width = Integer.parseInt(StringUtilities.getNString(xo.getAttributeValue("WFE_WIDTH")));
            } catch (Exception e) {
            }
            try {
                wfe_height = Integer.parseInt(StringUtilities.getNString(xo.getAttributeValue("WFE_HEIGHT")));
            } catch (Exception e) {
            }
            jspPage = StringUtilities.getNString(xo.getChildValue("JSP_PAGE"));
            jspCustomButtons = StringUtilities.getNString(xo.getChildValue("JSP_CUSTOM_BUTTONS_L"));
            if (StringUtilities.getNString(jspCustomButtons).equals("")) jspCustomButtons = StringUtilities.getNString(xo.getChildValue("JSP_CUSTOM_BUTTONS"));
            jspCustomButtonsOnCenter = StringUtilities.getNString(xo.getChildValue("JSP_CUSTOM_BUTTONS_C"));
            jspCustomButtonsOnRight = StringUtilities.getNString(xo.getChildValue("JSP_CUSTOM_BUTTONS_R"));
            jspCustomHiddenVariables = StringUtilities.getNString(xo.getChildValue("JSP_CUSTOM_HIDDEN_VARIABLES"));
            jspCustomContext = StringUtilities.getNString(xo.getChildValue("JSP_CUSTOM_CONTEXT"));
            jspCustomContentLeft = StringUtilities.getNString(xo.getChildValue("JSP_CUSTOM_CONTENT_LEFT"));
            jspCustomContentRight = StringUtilities.getNString(xo.getChildValue("JSP_CUSTOM_CONTENT_RIGHT"));
            bDisplayTitle = StringUtilities.stringToBoolean(xo.getAttributeValue("DISPLAY_TITLE"), true);
            bSave = StringUtilities.stringToBoolean(xo.getAttributeValue("SAVE"), true);
            bReloadAfterSave = StringUtilities.stringToBoolean(xo.getAttributeValue("RELOAD_AFTER_SAVE"), false);
            ServletToolKit servletToolkit = new ServletToolKit();
            String path = servletToolkit.getHostAndContextURL(request);
            Vector vChildren = VectorUtil.getNVector(xo.getVChildren());
            for (int i = 0; i < vChildren.size(); i++) {
                XmlObject xoChild = (XmlObject) vChildren.elementAt(i);
                boolean childWebFormIncluded = StringUtilities.stringToBoolean(xoChild.getAttributeValue("INCLUDED"), true);
                if (xoChild.getName().equals("FORM")) {
                    String id_form = xoChild.getID();
                    boolean isFormList = StringUtilities.stringToBoolean(xoChild.getAttributeValue("FORM_LIST"), false);
                    Form form = WebFormManager.getForm(id_form, request, isFormList);
                    if (form == null) throw new Exception("L'objet ['" + id_form + "'] n'est pas d�fini dans form.xml");
                    if (!childWebFormIncluded) {
                        form.setManager(form);
                    } else form.setManager(this);
                    form.setIncluded(childWebFormIncluded);
                    this.getVWebForm().addElement(form);
                    request.getSession().setAttribute("FORM_" + id_form, form);
                    String id_formMaster = StringUtilities.getNString(xoChild.getAttributeValue("MASTER_FORM"));
                    form.setId_master(id_formMaster);
                    gestWebFormProperties(xoChild);
                } else if (xoChild.getName().equals("LIST")) {
                    String id_list = xoChild.getID();
                    StandardList list1 = WebFormManager.getList(id_list, true, request);
                    if (list1 == null) throw new Exception("L'objet ['" + id_list + "'] n'est pas d�fini dans list.xml");
                    if (!childWebFormIncluded) {
                        list1.setManager(list1);
                    } else list1.setManager(this);
                    list1.setIncluded(childWebFormIncluded);
                    this.getVWebForm().addElement(list1);
                    request.getSession().setAttribute("LIST_" + id_list, list1);
                    gestWebFormProperties(xoChild);
                } else if (xoChild.getName().equals("DETAIL")) {
                    String id_detail = xoChild.getID();
                    Detail detail = WebFormManager.getDetail(id_detail, request);
                    if (detail == null) throw new Exception("L'objet ['" + id_detail + "'] n'est pas d�fini dans detail.xml");
                    if (!childWebFormIncluded) {
                        detail.setManager(detail);
                    } else detail.setManager(this);
                    detail.setIncluded(childWebFormIncluded);
                    String id_masterForm = StringUtilities.getNString(xoChild.getAttributeValue("MASTER_FORM"));
                    detail.setId_master(id_masterForm);
                    request.getSession().setAttribute("LIST_" + id_detail, detail);
                    this.getVWebForm().addElement(detail);
                    gestWebFormProperties(xoChild);
                }
            }
        } catch (Exception e) {
            log.error(" CompleteForm - EXCEPTION : '" + e.getMessage() + "'");
            errorMessage = e.getMessage();
        }
    }

    /**
   * loadFromDB
   * @param currentRow TRow
   * @param request HttpServletRequest
   * @throws java.lang.Exception
   */
    public void loadFromDB(TRow currentRow, HttpServletRequest request) throws Exception {
        try {
            log.info("<<< CompleteForm.loadFromDB - BEGIN");
            this.setErrorMessage("");
            if (currentRow == null) currentRow = new TRow();
            for (int i = 0; i < vWebForm.size(); i++) {
                if (vWebForm.elementAt(i) instanceof Form) {
                    Form form = (Form) vWebForm.elementAt(i);
                    if (form.isFormList()) {
                        form.loadListFromDB();
                        String pkOfFirst = form.getValueOfFirstOfList();
                        form.loadFromDB(StringUtilities.parseString(pkOfFirst, Form.sepPK_VAL));
                    } else {
                        String id_formMaster = form.getId_master();
                        if (!id_formMaster.equals("")) {
                            Form formMaster = (Form) getWebFormForId(id_formMaster);
                            form.retrieveFields(formMaster.getTRow());
                            form.loadFromDB(StringUtilities.parseString(formMaster.getValueForPkField(), Form.sepPK_VAL));
                        } else {
                            form.retrieveFields(currentRow);
                            form.loadFromDB(currentRow.getVFields());
                            if (form.isLoadSuccess() || !StringUtilities.getNString(form.getErrorMessage()).equals("")) {
                                form.postInitForm(true, request);
                            } else {
                                log.info("<<<< CompleteForm.loadFromDB - openForm - Le form n'existe pas en DB -> pr�senter un nouveau record");
                                form.loadForNewRow(request);
                            }
                        }
                    }
                    form.postTreatment(true, request, null);
                } else if (vWebForm.elementAt(i) instanceof Detail) {
                    Detail detail = (Detail) vWebForm.elementAt(i);
                    String id_formMaster = detail.getId_master();
                    Form form = (Form) getWebFormForId(id_formMaster);
                    detail.loadFromDB(form.getTRow());
                    detail.postTreatment(true, request, null);
                } else if (vWebForm.elementAt(i) instanceof StandardList) {
                    StandardList list = (StandardList) vWebForm.elementAt(i);
                    list.loadFromDB();
                    list.postTreatment(true, request, null);
                }
            }
        } catch (Exception e) {
            log.error("CompleteForm.loadFromDB - EXCEPTION : '" + e.getMessage() + "'", e);
            this.setErrorMessage(e.getMessage());
        } finally {
            log.info("<<< CompleteForm.loadFromDB - END");
        }
    }

    /**
   * loadForNewRow
   * @param currentRow TRow
   * @param request HttpServletRequest
   * @throws java.lang.Exception
   */
    public void loadForNewRow(TRow currentRow, HttpServletRequest request) throws Exception {
        try {
            log.info("<<< CompleteForm.loadForNewRow - BEGIN");
            this.setErrorMessage("");
            if (currentRow == null) currentRow = new TRow();
            for (int i = 0; i < vWebForm.size(); i++) {
                if (vWebForm.elementAt(i) instanceof Form) {
                    Form form = (Form) vWebForm.elementAt(i);
                    form.retrieveFields(currentRow);
                    form.loadForNewRow();
                } else if (vWebForm.elementAt(i) instanceof Detail) {
                    Detail detail = (Detail) vWebForm.elementAt(i);
                } else if (vWebForm.elementAt(i) instanceof StandardList) {
                    StandardList list = (StandardList) vWebForm.elementAt(i);
                    list.loadFromDB();
                }
            }
        } catch (Exception e) {
            log.error("CompleteForm.loadForNewRow - EXCEPTION : '" + e.getMessage() + "'", e);
            this.setErrorMessage(e.getMessage());
        } finally {
            log.info("<<< CompleteForm.loadForNewRow - END");
        }
    }

    /**
   * getFormForId
   * @param id_form
   * @return
   * @throws java.lang.Exception
   */
    public WebForm getWebFormForId(String id_webform) throws Exception {
        WebForm form = (WebForm) VectorUtil.findObjectIn(this.vWebForm, id_webform);
        if (form == null) throw new Exception("L'objet ['" + id_webform + "'] ne correspond � aucun WebForm d�fini pour cet �l�ment CFORM");
        return form;
    }

    /**
   * gestWebFormProperties
   * @param xoChild
   */
    protected void gestWebFormProperties(XmlObject xoChild) {
        WebFormProperties wfp = new WebFormProperties();
        int height = 200;
        String sHeight = xoChild.getAttributeValue("HEIGHT");
        try {
            if (!StringUtilities.getNString(sHeight).equals("")) wfp.setHeight(Integer.parseInt(sHeight));
        } catch (Exception e) {
            log.error("CompleteForm.gestWebFormProperties - Invalid value [" + sHeight + "] for HEIGHT of LIST ['" + xoChild.getID() + "']");
        }
        String sWidth = xoChild.getAttributeValue("WIDTH");
        try {
            if (!StringUtilities.getNString(sWidth).equals("")) wfp.setWidth(Integer.parseInt(sWidth));
        } catch (Exception e) {
            log.error("CompleteForm.gestWebFormProperties - Invalid value [" + sWidth + "] for WIDTH of LIST ['" + xoChild.getID() + "']");
        }
        wfp.setDiv(StringUtilities.stringToBoolean(xoChild.getAttributeValue("DIV"), true));
        wfp.setDivOpen(StringUtilities.stringToBoolean(xoChild.getAttributeValue("DIV_OPEN"), false));
        wfp.setVisible(StringUtilities.stringToBoolean(xoChild.getAttributeValue("VISIBLE"), true));
        wfp.setCollapsible(StringUtilities.stringToBoolean(xoChild.getAttributeValue("COLLAPSIBLE"), true));
        this.getVWebFormProperties().addElement(wfp);
    }

    /**
   * getCompleteForm
   * @param xo
   * @param request
   * @return
   * @throws java.lang.Exception
   */
    public static CompleteForm getCompleteForm(XmlObject xo, HttpServletRequest request) throws Exception {
        if (xo == null) {
            log.info("<<< CompleteForm.getCompleteForm - object NOT found !");
            return null;
        }
        String CFORM_OBJECT = StringUtilities.getNString(xo.getAttributeValue("CFORM_OBJECT"));
        log.info("<<< CompleteForm.getCompleteForm - DATA_OBJECT = '" + CFORM_OBJECT + "'");
        if (!CFORM_OBJECT.equals("")) {
            CompleteForm cform = (CompleteForm) new DataObjectFactory("data_object").getResource(CFORM_OBJECT);
            cform.init(xo, request);
            return cform;
        } else return new CompleteForm(xo, request);
    }

    /**
   * getState
   * @return
   */
    public String getState() {
        for (int i = 0; i < vWebForm.size(); i++) {
            if (((IWebForm) vWebForm.elementAt(i)).getState().equals(TRowState.MODIFIED)) return TRowState.MODIFIED;
        }
        return TRowState.UNCHANGED;
    }

    /**
   * setAllFieldsValue
   * - Peut �tre appel� par une liste ( manager.setAllFieldsValue() )
   * @param request
   * @return
   */
    public boolean setAllFieldsValue(HttpServletRequest request) throws Exception {
        RequestManager rm = new RequestManager(request);
        for (int i = 0; i < vWebForm.size(); i++) {
            WebForm webForm = ((WebForm) vWebForm.elementAt(i));
            if (webForm.isIncluded()) webForm.setAllFieldsValue(request);
            WebFormProperties wfp = ((WebFormProperties) vWebFormProperties.elementAt(i));
            wfp.setDivOpen(StringUtilities.stringToBoolean(rm.getParameter(webForm.getID() + ".bDivOpen")));
        }
        return true;
    }

    /**
   * saveWebFormProperties
   * @param request
   * @throws java.lang.Exception
   */
    public void saveWebFormProperties(HttpServletRequest request) throws Exception {
        if (vWebFormProperties != null && vWebForm.size() == vWebFormProperties.size()) {
            RequestManager rm = new RequestManager(request);
            for (int i = 0; i < vWebForm.size(); i++) {
                IWebForm webForm = ((IWebForm) vWebForm.elementAt(i));
                WebFormProperties wfp = ((WebFormProperties) vWebFormProperties.elementAt(i));
                wfp.setDivOpen(StringUtilities.stringToBoolean(rm.getParameter(webForm.getID() + ".bDivOpen")));
            }
        }
    }

    /**
   * select
   * @param request
   */
    public void select(HttpServletRequest request) throws Exception {
        log.info("<<< CompleteForm.select >>>");
        RequestManager rm = new RequestManager(request);
        saveWebFormProperties(request);
        String id_form = rm.getParameter("ID_FORM");
        Form form = (Form) request.getSession().getAttribute("FORM_" + id_form);
        form.select(request);
        reloadDetails(form);
    }

    /**
   * reloadDetails
   * @param form Form
   */
    public void reloadDetails(Form form) throws Exception {
        log.info("<<< CompleteForm.reloadDetails >>>");
        IWebForm webForm = null;
        StringUtilities su = new StringUtilities();
        for (int iW = 0; iW < getVWebForm().size(); iW++) {
            webForm = (IWebForm) getVWebForm().elementAt(iW);
            if (webForm instanceof Detail && su.getNString(((Detail) webForm).getId_master()).equals(form.getID())) ((Detail) webForm).loadFromDB(form.getTRow()); else if (webForm instanceof Form && su.getNString(((Form) webForm).getId_master()).equals(form.getID())) ((Form) webForm).loadFromDB(StringUtilities.parseString(form.getValueForPkField(), Form.sepPK_VAL));
        }
    }

    /**
   * save
   * @param request
   * @return
   */
    public boolean save(HttpServletRequest request) throws Exception {
        IWebForm webForm = null;
        for (int iW = 0; iW < getVWebForm().size(); iW++) {
            webForm = (IWebForm) getVWebForm().elementAt(iW);
            if (webForm.isIncluded()) {
                boolean doAddPostTreatment = false;
                if (webForm instanceof Form && ((Form) webForm).getState().equals(TRowState.NEW)) doAddPostTreatment = true;
                webForm.save(request);
                if (doAddPostTreatment) addPostTreatment(((Form) webForm));
            }
        }
        if (bReloadAfterSave) {
            log.info("<<<<< CompleteForm.save OK --> reload...");
            loadFromDB(this.rowOpener, request);
        }
        return true;
    }

    /**
   * Traitement � r�aliser apr�s l'ajout d'un CFORM ou plut�t du form principal
   * Les d�tails li�s (via l'attribut MASTER_FORM) � ce form sont bien charg�s
   * mais les FK_Field ne sont pas remplis
   * @param form
   */
    public void addPostTreatment(Form formMaster) throws Exception {
        log.info("<<<<< CompleteForm.addPostTreatment - ID : '" + this.ID + "' - master form : '" + formMaster.getID() + "' - BEGIN ");
        for (int i = 0; i < vWebForm.size(); i++) {
            WebForm webForm = ((WebForm) vWebForm.elementAt(i));
            if (webForm.getId_master().equals(formMaster.getID())) {
                if (webForm instanceof Form) {
                    ((Form) webForm).retrieveFields(formMaster.getTRow());
                    ((Form) webForm).setFkValues(formMaster.getTRow());
                } else if (webForm instanceof Detail) {
                    ((Detail) webForm).retrieveFields(formMaster.getTRow());
                    ((Detail) webForm).initDetailForTRowForm(formMaster.getTRow());
                } else if (webForm instanceof StandardList) {
                    ((StandardList) webForm).retrieveFields(formMaster.getTRow());
                }
            }
        }
        log.info("<<<<< CompleteForm.addPostTreatment - ID : '" + this.ID + "' - master form : '" + formMaster.getID() + "' - END ");
    }

    /**
   * process
   * Traitement � effectuer lors du rechargement de la page
   * apr�s un back par exemple.
   */
    public void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("<<<<< CompleteForm.process - ID : '" + this.ID + "' - BEGIN ");
        for (int i = 0; i < vWebForm.size(); i++) {
            ((WebForm) vWebForm.elementAt(i)).process(request, response);
        }
        log.info("<<<<< CompleteForm.process - ID : '" + this.ID + "' - END ");
    }

    /**
   * postTreatment
   * @param bLoading boolean
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @throws java.lang.Exception
   */
    public void postTreatment(boolean bLoading, HttpServletRequest request, HttpServletResponse response) throws Exception {
    }

    public void exportToXml(GenXml genXml) throws Exception {
        IWebForm webForm = null;
        for (int iW = 0; iW < getVWebForm().size(); iW++) {
            webForm = (IWebForm) getVWebForm().elementAt(iW);
            webForm.exportToXml(genXml);
        }
    }

    public String getJspPage() {
        return jspPage;
    }

    public void setJspPage(String jspPage) {
        this.jspPage = jspPage;
    }

    public Vector getVWebForm() {
        return vWebForm;
    }

    public void setVWebForm(Vector vWebForm) {
        this.vWebForm = vWebForm;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public java.util.Vector getVWebFormProperties() {
        return vWebFormProperties;
    }

    public void setVWebFormProperties(java.util.Vector vWebFormProperties) {
        this.vWebFormProperties = vWebFormProperties;
    }

    public int getWfe_height() {
        return wfe_height;
    }

    public void setWfe_height(int wfe_height) {
        this.wfe_height = wfe_height;
    }

    public int getWfe_width() {
        return wfe_width;
    }

    public void setWfe_width(int wfe_width) {
        this.wfe_width = wfe_width;
    }
}
