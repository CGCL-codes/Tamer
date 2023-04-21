package com.divosa.eformulieren.toolkit.renderer.xforms;

import java.awt.image.renderable.RenderContext;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import com.divosa.eformulieren.core.exception.ToolkitAppException;
import com.divosa.eformulieren.core.service.WidgetService;
import com.divosa.eformulieren.core.spring.SpringApplicationContext;
import com.divosa.eformulieren.domain.domeinobject.Scheme;
import com.divosa.eformulieren.domain.domeinobject.Widget;
import com.divosa.eformulieren.domain.domeinobject.WidgetAttribute;
import com.divosa.eformulieren.domain.domeinobject.WidgetStruct;
import com.divosa.eformulieren.domain.domeinobject.WidgetStructLoc;
import com.divosa.eformulieren.domain.domeinobject.WidgetType;
import com.divosa.eformulieren.domain.util.XPath;
import com.divosa.eformulieren.toolkit.exception.ToolkitRendererException;
import com.divosa.eformulieren.toolkit.orbeon.OrbeonProperties;
import com.divosa.eformulieren.toolkit.renderer.DocumentRenderer;
import com.divosa.eformulieren.toolkit.renderer.RendererConstants;
import com.divosa.eformulieren.toolkit.renderer.RendererFactory;
import com.divosa.eformulieren.toolkit.renderer.WidgetRenderer;
import com.divosa.eformulieren.util.StringUtil;
import com.divosa.eformulieren.util.constant.Constants;
import com.divosa.eformulieren.util.helper.FileHelper;
import com.divosa.eformulieren.util.xslt.XSLTUtil;
import com.divosa.security.domain.Group;
import com.divosa.security.exception.AuthenticationException;
import com.divosa.security.exception.ObjectNotFoundException;
import com.divosa.security.service.SecurityService;

public abstract class XFormsDocumentRenderer extends DocumentRenderer {

    protected static final Scheme SCHEME = Scheme.XFORMS;

    private Document xformsModel;

    protected Document getXformsModel() {
        return xformsModel;
    }

    @Override
    protected Scheme getScheme() {
        return SCHEME;
    }

    /**
     * Overrides the super buildXml method.
     * @throws ToolkitAppException
     * 
     * @see com.divosa.eformulieren.toolkit.renderer.DocumentRenderer#buildXml(com.divosa.eformulieren.domain.domeinobject.WidgetStruct)
     * This method first checks whether the object model passed in already contains an overview tab or not. If not, it
     * creates one and adds it to the model. After this, the super method is called to enter the template method procedure.
     * @objectModel the object model as a WidgetStruct tree
     */
    @Override
    public StringBuilder buildXml(WidgetStruct objectModel) throws ObjectNotFoundException, AuthenticationException, ToolkitAppException {
        List<WidgetStruct> overviewTabs = getChildWidgetStructsByWidgetTypeAndWidgetAttributeValue(objectModel, WidgetType.TAB, RendererConstants.WIDGET_ATTRIBUTE_NAME_KIND_OF_TAB, RendererConstants.WIDGET_ATTRIBUTE_VALUE_OVERVIEW);
        if (overviewTabs != null && !overviewTabs.isEmpty()) {
        } else {
            List<WidgetStruct> allBlocks = new ArrayList<WidgetStruct>();
            getDeepChildWidgetStructs(allBlocks, objectModel, WidgetType.BLOCK);
            boolean overviewBlockOrButtonBlockFound = false;
            for (WidgetStruct block : allBlocks) {
                String description = block.getChildWidget().getDescription();
                if (description != null && (description.startsWith("**Overzicht") || description.startsWith("**Knoppen"))) {
                    overviewBlockOrButtonBlockFound = true;
                    break;
                }
            }
            if (!overviewBlockOrButtonBlockFound) {
                WidgetAttribute addOverview = objectModel.getChildWidget().getWidgetAttribute("addOverview");
                WidgetService widgetService = getWidgetService();
                Widget widget = new Widget();
                widget.setWidgetType(widgetService.getWidgetTypeWithWidgetTypeTags(WidgetType.TAB.getName()));
                WidgetAttribute overviewName = objectModel.getChildWidget().getWidgetAttribute("overviewName");
                if (overviewName != null && overviewName.getValue() != null && !"".equals(overviewName.getValue())) {
                    widget.setDescription(overviewName.getValue());
                } else {
                    widget.setDescription("Overview");
                }
                Set<WidgetAttribute> was = new HashSet<WidgetAttribute>();
                WidgetAttribute relevance = new WidgetAttribute();
                relevance.setName("relevance");
                WidgetAttribute overviewRelevance = objectModel.getChildWidget().getWidgetAttribute("overviewRelevance");
                if (overviewRelevance != null && overviewRelevance.getValue() != null && !"".equals(overviewRelevance.getValue())) {
                    relevance.setValue(overviewRelevance.getValue());
                } else {
                    relevance.setValue("true()");
                }
                was.add(relevance);
                WidgetAttribute kindOfTab = new WidgetAttribute(RendererConstants.WIDGET_ATTRIBUTE_NAME_KIND_OF_TAB, RendererConstants.WIDGET_ATTRIBUTE_VALUE_OVERVIEW);
                was.add(kindOfTab);
                was.add(addOverview);
                widget.setWidgetAttributes(was);
                WidgetStruct newWidgetStruct = new WidgetStructLoc(widget, objectModel.getVersion());
                newWidgetStruct.setParentWidgetStruct(objectModel);
                newWidgetStruct.setChildWidgetTypeTag(widget.getWidgetType().getWidgetTypeTag(Scheme.XFORMS));
                newWidgetStruct.setFormWidget(objectModel.getFormWidget());
                objectModel.getChildWidgetStructs().add(newWidgetStruct);
            }
        }
        return super.buildXml(objectModel);
    }

    @Override
    protected void buildOpeningHtmlTagWithNamespaces() throws ToolkitRendererException {
        String xhtmlHtmlInXsl = getStartTag(XHTML_PREFIX, TAG_HTML, null).toString();
        String xhtmlHtmlFromFile = null;
        try {
            xhtmlHtmlFromFile = FileHelper.loadStringFromFileOnClasspath("xforms/xforms-html-html.txt");
        } catch (FileNotFoundException e) {
            String message = "Error loading file xforms-html-html.xml form the classpath";
            LOGGER.error(message + ": " + e.getMessage());
            throw new ToolkitRendererException(message + ": " + e.getMessage(), e);
        }
        replace(xhtmlHtmlInXsl, xhtmlHtmlFromFile, null);
    }

    @Override
    protected void buildHtmlBody() throws AuthenticationException, ToolkitAppException, ObjectNotFoundException {
        String xhtmlBodyInXsl = getStartTagNoFill(XHTML_PREFIX, TAG_BODY, null).toString();
        StringBuilder xhtmlBodyFromFile;
        try {
            xhtmlBodyFromFile = new StringBuilder(FileHelper.loadStringFromFileOnClasspath("xforms/xforms-html-body.xml"));
        } catch (FileNotFoundException e) {
            String message = "Error loading file xforms-html-body.xml form the classpath";
            LOGGER.error(message + ": " + e.getMessage());
            throw new ToolkitRendererException(message + ": " + e.getMessage(), e);
        }
        replaceBodyPlaceHolders(xhtmlBodyFromFile);
        replace(xhtmlBodyInXsl, xhtmlBodyFromFile.toString(), null);
        getObjectModel();
    }

    @Override
    protected void buildHtmlHead() throws ObjectNotFoundException, AuthenticationException, ToolkitAppException {
        super.buildHtmlHead();
        addXFormsModel();
        addCssStyling();
    }

    @Override
    protected void buildHtmlHeadTitle() throws ToolkitRendererException, AuthenticationException {
        String xhtmlTitleEmptyTag = getStartTagNoFill(XHTML_PREFIX, TAG_TITLE, null).toString();
        String xhtmlTitleFilledTag = getStartTag(XHTML_PREFIX, TAG_TITLE, null).append(getObjectModel().getChildWidget().getDescription()).append(getEndTag(XHTML_PREFIX, TAG_TITLE)).toString();
        replace(xhtmlTitleEmptyTag, xhtmlTitleFilledTag, null);
    }

    @Override
    protected void addCssStyling() throws ToolkitRendererException, AuthenticationException, AuthenticationException {
        String endOfHead = getEndTag(XHTML_PREFIX, TAG_HEAD).toString();
        StringBuilder sb = new StringBuilder("<xhtml:link rel=\"stylesheet\" type=\"text/css\" href=\"/apps/e-formulieren/forms/css/default.css\"/>");
        WidgetAttribute css = getObjectModel().getChildWidget().getWidgetAttribute("css");
        if (css != null && css.getValue() != null && !css.getValue().equals("")) {
            sb.append("<xhtml:link rel=\"stylesheet\" type=\"text/css\" href=\"/apps/e-formulieren/forms/css/").append(css.getValue()).append(".css\"/>");
        } else {
            Group group = getObjectModel().getChildWidget().getGroup();
            if (group != null && group.getCss() != null && !group.getCss().equals("")) {
                sb.append("<xhtml:link rel=\"stylesheet\" type=\"text/css\" href=\"/apps/e-formulieren/forms/css/").append(group.getCss()).append("\"/>");
            }
            sb.append("<xhtml:link rel=\"stylesheet\" type=\"text/css\" href = \"/apps/e-formulieren/forms/css/{{instance('form-instance')/formcontent/form-info/@gemeente}}.css\"/>");
        }
        insertBefore(endOfHead, sb.toString());
    }

    @Override
    protected StringBuilder loadXmlTemplate() throws ToolkitRendererException {
        String xformsTemplate;
        try {
            xformsTemplate = FileHelper.loadStringFromFileOnClasspath("xforms/xformsTemplate.xml");
        } catch (FileNotFoundException e) {
            String message = "Error loading file xformsTemplate.xml form the classpath";
            LOGGER.error(message + ": " + e.getMessage());
            throw new ToolkitRendererException(message + ": " + e.getMessage(), e);
        }
        return new StringBuilder(xformsTemplate);
    }

    protected void addXFormsModel() throws ObjectNotFoundException, AuthenticationException, ToolkitAppException {
        xformsModel = constructXFormsModel();
        xformsModel.asXML();
        String endOfTitle = getEndTag(XHTML_PREFIX, TAG_TITLE).toString();
        String modelFromFile = null;
        try {
            modelFromFile = FileHelper.loadStringFromFileOnClasspath("xforms/xforms-model.xml");
        } catch (FileNotFoundException e) {
            String message = "Error loading file xforms-model.xml form the classpath";
            LOGGER.error(message + ": " + e.getMessage());
            throw new ToolkitRendererException(message + ": " + e.getMessage(), e);
        }
        insertAfter(endOfTitle, modelFromFile);
        List<WidgetStruct> tabWidgetStructs = getChildWidgetStructsByWidgetType(getObjectModel(), WidgetType.TAB);
        insertFormInstanceInXFormsModel();
        insertPrefilledDataIntoFormInstance();
        insertRepeatableBlocksFunctionallity();
        if (renderOgone()) {
            insertOgoneActions();
            insertOgoneBindings();
        }
        boolean renderUploadFunctionality = renderUploadFunctionality();
        if (renderUploadFunctionality) {
            insertUploadFunctionalityInstances();
        }
        insertBrokerFunctionality(renderUploadFunctionality);
        insertBindings();
        if (tabWidgetStructs != null && !tabWidgetStructs.isEmpty()) {
            insertBefore("</xforms:model>", getTabFunctionality(tabWidgetStructs).toString());
        }
        insertAfter("</xforms:model>", getJavascriptDeclarationCode().toString());
        replace(RendererConstants.PLACEHOLDER_AMOUNT_OF_TABS, String.valueOf(tabWidgetStructs.size()), null);
        replace(RendererConstants.PLACEHOLDER_ORBEON_LOCALHOST_URL, OrbeonProperties.getPropertyStore().getProperty(OrbeonProperties.ORBEON_LOCALHOST_URL), null);
        replacePlaceholderWithStringFromFile(RendererConstants.PLACEHOLDER_FILL_EMPTY_DATES, "xforms/fillEmptyDates.xml", null);
    }

    private StringBuilder getJavascriptDeclarationCode() throws ToolkitRendererException, AuthenticationException {
        StringBuilder sb = new StringBuilder();
        List<String> files = new ArrayList<String>();
        String scriptLocation = "/config/scripts/";
        files.add("functions_navigatie.js");
        if (renderOgone()) {
            files.add("ogone.js");
        }
        for (String file : files) {
            sb.append("<xhtml:script language = \"javascript\" type = \"text/javascript\" src = \"" + scriptLocation + file + "\"/>");
        }
        return sb;
    }

    private StringBuilder getTabFunctionality(List<WidgetStruct> tabWidgetStructs) throws ObjectNotFoundException, ToolkitRendererException, AuthenticationException {
        StringBuilder tabFunctionalityTemplate = null;
        try {
            tabFunctionalityTemplate = new StringBuilder(FileHelper.loadStringFromFileOnClasspath("xforms/xforms-tab-functionality.xml"));
        } catch (FileNotFoundException e) {
            String message = "Error loading file xforms-tab-functionality.xml form the classpath";
            LOGGER.error(message + ": " + e.getMessage());
            throw new ToolkitRendererException(message + ": " + e.getMessage(), e);
        }
        replace("%%TABS_SELECTED_BINDINGS%%", getTabsSelectedBindings(tabWidgetStructs).toString(), tabFunctionalityTemplate);
        replace("%%TABS_NOTSELECTED_BINDINGS%%", getTabsNotSelectedBindings(tabWidgetStructs).toString(), tabFunctionalityTemplate);
        replace("%%BUTTON_RELEVANCE_BINDING%%", getButtonsBinding(tabWidgetStructs).toString(), tabFunctionalityTemplate);
        replace("%%TABS_ACTIVE_BINDINGS%%", getTabsNotActiveBindings(tabWidgetStructs).toString(), tabFunctionalityTemplate);
        replace("%%TABS_FINISHED_BINDINGS%%", getTabsFinishedBindings(tabWidgetStructs).toString(), tabFunctionalityTemplate);
        return tabFunctionalityTemplate;
    }

    /**
     * Construct the model. A node 'form' is constructed and calls are made to other methods to add child nodes 'formcontent'
     * and 'formcontrol'.
     * 
     * @return the form node as a DOM4J Element
     * @throws ToolkitRendererException the Exception thrown if anything went wrong
     */
    private Document constructXFormsModel() throws ToolkitRendererException, AuthenticationException {
        Document form = DocumentHelper.createDocument();
        Element formElement = form.addElement("form");
        String formName = ((Widget) getObjectModel().getChildWidget()).getDescription();
        formElement.addAttribute("name", formName);
        form.getRootElement().add(createXFormsModelFormContentElement());
        form.getRootElement().add(createXFormsModelFormControlElement());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(form.asXML());
        }
        return form;
    }

    /**
     * Create a formcontent node in the model. After creation, calls are made to other methods to add child nodes to this
     * node; a prefilleddata node, a constants node and a widgetTree node.
     * 
     * return the formcontent node as a DOM4J Element
     * 
     * @throws ToolkitRendererException the Exception thrown if anything went wrong
     */
    private Element createXFormsModelFormContentElement() throws ToolkitRendererException, AuthenticationException {
        Document formContentDocument = DocumentHelper.createDocument();
        Element formcontent = formContentDocument.addElement("formcontent");
        formcontent.add(createFormInfoElement());
        if (renderOgone()) {
            formcontent.add(createOgoneResultElement());
        }
        formcontent.add(createConstantsElement());
        formcontent.add(createXFormsModelWidgetTreeElement());
        if (renderOgone()) {
            formcontent.addElement("formcontentCacheKey");
        }
        return formContentDocument.getRootElement();
    }

    private boolean renderOgone() throws ToolkitRendererException, AuthenticationException {
        boolean renderOgone = false;
        List<WidgetStruct> widgetStructs = new ArrayList<WidgetStruct>();
        getDeepChildWidgetStructs(widgetStructs, getObjectModel(), WidgetType.OGONE);
        if (!widgetStructs.isEmpty()) {
            renderOgone = true;
        }
        return renderOgone;
    }

    private boolean renderUploadFunctionality() throws ToolkitRendererException, AuthenticationException {
        List<WidgetStruct> widgetStructs = new ArrayList<WidgetStruct>();
        List<WidgetStruct> widgetStructsWT = new ArrayList<WidgetStruct>();
        getDeepChildWidgetStructs(widgetStructsWT, getObjectModel(), WidgetType.BLOCK);
        for (WidgetStruct widgetStruct2 : widgetStructsWT) {
            String description = widgetStruct2.getChildWidget().getDescription();
            if (description != null && !description.isEmpty() && description.startsWith("**Upload")) {
                widgetStructs.add(widgetStruct2);
                break;
            }
        }
        return widgetStructs.isEmpty() ? false : true;
    }

    private void insertOgoneActions() throws ToolkitRendererException, AuthenticationException {
        String formPlaceHolder = "%%load_cached_model_actions%%";
        StringBuilder sb = new StringBuilder();
        try {
            String ogoneGetCachedModel = FileHelper.loadStringFromFileOnClasspath("xforms/ogone-get-cached-model.xml");
            String formPlaceHolder2 = "%%TOGGLE_OGONE_TAB%%";
            StringBuilder sb2 = new StringBuilder(ogoneGetCachedModel);
            StringBuilder sb3 = new StringBuilder();
            List<WidgetStruct> widgetStructs = new ArrayList<WidgetStruct>();
            getDeepChildWidgetStructs(widgetStructs, getObjectModel(), WidgetType.OGONE);
            WidgetStruct tabWidgetStruct = getContainingParentWidgetStruct(widgetStructs.get(0), WidgetType.TAB);
            addActivateLogic(sb3, tabWidgetStruct.getChildWidget());
            replace(formPlaceHolder2, sb3.toString(), sb2);
            sb.append(sb2);
        } catch (FileNotFoundException e) {
            String message = "Error loading file xforms/ogone-get-cached-model.xml";
            LOGGER.error(message + ": " + e.getMessage());
            throw new ToolkitRendererException(message + ": " + e.getMessage(), e);
        }
        replace(formPlaceHolder, sb.toString(), null);
    }

    private void insertOgoneBindings() throws ToolkitRendererException, AuthenticationException {
        replacePlaceholderWithStringFromFile(RendererConstants.PLACEHOLDER_OGONE_BINDINGS, "xforms/ogone-bindings.xml", null);
    }

    private void insertBrokerBindings(List<WidgetStruct> brokerConnectionWidgetStructs) {
        StringBuilder brokerBindings = new StringBuilder();
        brokerBindings.append(getInitialBrokerBinding("centralEmail"));
        brokerBindings.append(getInitialBrokerBinding("saveGensource"));
        if (!brokerConnectionWidgetStructs.isEmpty()) {
            for (WidgetStruct brokerConnection : brokerConnectionWidgetStructs) {
                brokerBindings.append(getInitialBrokerBinding(String.valueOf(brokerConnection.getChildWidget().getId())));
            }
        }
        if (brokerBindings != null) {
            replace(RendererConstants.PLACEHOLDER_BROKER_BINDINGS, brokerBindings.toString(), null);
        }
    }

    private StringBuilder getInitialBrokerBinding(String brID) {
        StringBuilder brokerBinding = new StringBuilder();
        brokerBinding.append("<xforms:bind nodeset = \"instance('broker-instance')/brokerConnections/brokerConnection[@brID = '");
        brokerBinding.append(brID);
        brokerBinding.append("']/active\" relevant = \". = 'true'\"/>");
        brokerBinding.append("<xforms:bind nodeset = \"instance('broker-instance')/brokerConnections/brokerConnection[@brID = '");
        brokerBinding.append(brID);
        brokerBinding.append("']/inactive\" relevant = \". = 'true'\"/>");
        return brokerBinding;
    }

    private void insertUploadFunctionalityInstances() throws ToolkitRendererException {
        replacePlaceholderWithStringFromFile(RendererConstants.PLACEHOLDER_UPLOAD_INSTANCE, "xforms/upload-instances.xml", null);
    }

    private void insertBrokerFunctionality(boolean renderUploadFunctionality) throws ToolkitRendererException, AuthenticationException {
        List<WidgetStruct> brokerConnections = new ArrayList<WidgetStruct>();
        getDeepChildWidgetStructs(brokerConnections, getObjectModel(), WidgetType.BROKER);
        if (brokerConnections != null && !brokerConnections.isEmpty()) {
            insertBrokerInstanceInXFormsModel(brokerConnections);
            insertBrokerBindings(brokerConnections);
            insertBrokerSubmissionInXFormsModel(brokerConnections, renderUploadFunctionality);
        }
        insertStandardBrokerFunctionality(renderUploadFunctionality);
    }

    protected void insertStandardBrokerFunctionality(boolean renderUploadFunctionality) throws ToolkitRendererException {
        StringBuilder insertBrokerconnectionInFormcontentXml = loadInsertBrokerconnectionInFormcontentXml(renderUploadFunctionality);
        replace(RendererConstants.PLACEHOLDER_BROKERCONNECTION_ID, RendererConstants.VALUE_BROKERCONNECTION_DUMMY_ID, insertBrokerconnectionInFormcontentXml);
        replace(RendererConstants.PLACEHOLDER_INSERT_DUMMY_BROKER_CONNECTION_INTO_FORMCONTENT, insertBrokerconnectionInFormcontentXml.toString(), null);
    }

    /**
     * Replaces the placeholder %%BROKER_CONNECTIONS%% in in xmlTemplate with the xform model.
     * 
     * @throws AuthenticationException
     * @throws ToolkitRendererException
     */
    private void insertBrokerInstanceInXFormsModel(List<WidgetStruct> brokerConnectionWidgetStructs) throws ToolkitRendererException, AuthenticationException {
        String formPlaceHolder = "%%BROKER_CONNECTIONS%%";
        Document doc = DocumentHelper.createDocument();
        Element brokerConnections = doc.addElement("brokerConnections");
        Element brokerConnection = brokerConnections.addElement("brokerConnection");
        brokerConnection.addAttribute("brID", RendererConstants.VALUE_BROKERCONNECTION_DUMMY_ID);
        brokerConnection = brokerConnections.addElement("brokerConnection");
        brokerConnection.addAttribute("brID", "centralEmail");
        brokerConnection.addElement("centralEmail");
        brokerConnection.addElement("active").addText("true");
        brokerConnection.addElement("inactive").addText("false");
        brokerConnection = brokerConnections.addElement("brokerConnection");
        brokerConnection.addAttribute("brID", "saveGensource");
        brokerConnection.addElement("saveGensource");
        brokerConnection.addElement("active").addText("true");
        brokerConnection.addElement("inactive").addText("false");
        if (!brokerConnectionWidgetStructs.isEmpty()) {
            for (WidgetStruct widgetStruct : brokerConnectionWidgetStructs) {
                Widget broker = widgetStruct.getChildWidget();
                brokerConnection = brokerConnections.addElement("brokerConnection");
                brokerConnection.addAttribute("brID", String.valueOf(broker.getId()));
                brokerConnection.addElement("active").addText("true");
                brokerConnection.addElement("inactive").addText("false");
                if (broker.getWidgetAttribute("ownEmail") != null && Constants.TRUE.equals(broker.getWidgetAttribute("ownEmail").getValue())) {
                    brokerConnection.addElement("ownEmail");
                }
                if (broker.getWidgetAttribute("centralEmail") != null && Constants.TRUE.equals(broker.getWidgetAttribute("centralEmail").getValue())) {
                    Element centralEmail = brokerConnection.addElement("centralEmail");
                }
                if (broker.getWidgetAttribute("mijnoverheid") != null && Constants.TRUE.equals(broker.getWidgetAttribute("mijnoverheid").getValue())) {
                    Element mijnoverheid = brokerConnection.addElement("mijnoverheid");
                }
                if (broker.getWidgetAttribute("saveGensource") != null && Constants.TRUE.equals(broker.getWidgetAttribute("saveGensource").getValue())) {
                    Element saveGensource = brokerConnection.addElement("saveGensource");
                }
                if (broker.getWidgetAttribute("services") != null && !broker.getWidgetAttribute("services").getValue().isEmpty()) {
                    Element services = brokerConnection.addElement("services");
                    services.addText(broker.getWidgetAttribute("services").getValue());
                }
                if (broker.getWidgetAttribute("saveDatabase") != null && Constants.TRUE.equals(broker.getWidgetAttribute("saveDatabase").getValue())) {
                    Element saveDatabase = brokerConnection.addElement("saveDatabase");
                }
            }
        }
        replace(formPlaceHolder, brokerConnections.asXML(), null);
    }

    private void insertBrokerSubmissionInXFormsModel(List<WidgetStruct> brokerConnectionWidgetStructs, boolean renderUploadFunctionality) throws ToolkitRendererException, AuthenticationException {
        if (!brokerConnectionWidgetStructs.isEmpty()) {
            String formPlaceHolder = RendererConstants.PLACEHOLDER_BROKER_SUBMISSION;
            StringBuilder sb = new StringBuilder();
            StringBuilder brokerSubmissionXml = loadBrokerSubmissionXml(renderUploadFunctionality);
            for (WidgetStruct widgetStruct : brokerConnectionWidgetStructs) {
                String brokerSubmissionString = brokerSubmissionXml.toString();
                StringBuilder brokerSubmissionStringBuilder = new StringBuilder(brokerSubmissionString);
                replace(RendererConstants.PLACEHOLDER_BROKERCONNECTION_ID, String.valueOf(widgetStruct.getChildWidget().getId()), brokerSubmissionStringBuilder);
                replace(RendererConstants.PLACEHOLDER_ORBEON_LOCALHOST_URL, OrbeonProperties.getPropertyStore().getProperty(OrbeonProperties.ORBEON_LOCALHOST_URL), brokerSubmissionStringBuilder);
                sb.append(brokerSubmissionStringBuilder);
            }
            replace(formPlaceHolder, sb.toString(), null);
        }
    }

    private StringBuilder loadBrokerSubmissionXml(boolean renderUploadFunctionality) throws ToolkitRendererException, AuthenticationException {
        StringBuilder brokerSubmission = new StringBuilder(loadStringFromFileOnClasspath("xforms/broker-submission.xml"));
        StringBuilder insertBrokerconnectionInFormcontent = loadInsertBrokerconnectionInFormcontentXml(renderUploadFunctionality);
        replace(RendererConstants.PLACEHOLDER_INSERT_BROKERCONNECTION_IN_FORMCONTENT, insertBrokerconnectionInFormcontent.toString(), brokerSubmission);
        return brokerSubmission;
    }

    private StringBuilder loadInsertBrokerconnectionInFormcontentXml(boolean renderUploadFunctionality) throws ToolkitRendererException {
        StringBuilder insertBrokerconnectionInFormcontent = new StringBuilder(loadStringFromFileOnClasspath("xforms/insert-brokerconnection-in-formcontent.xml"));
        if (renderUploadFunctionality) {
            String insertUploadedFilesIntoBrokerconnection = loadStringFromFileOnClasspath("xforms/insert-uploaded-files-into-brokerconnection.xml");
            replace(RendererConstants.PLACEHOLDER_INSERT_UPLOADED_FILES_IN_BROKERCONNECTION, insertUploadedFilesIntoBrokerconnection, insertBrokerconnectionInFormcontent);
        }
        return insertBrokerconnectionInFormcontent;
    }

    /**
     * Replaces the placeholder %%FORM_INSTANCE%% in in xmlTemplate with the xform model.
     */
    private void insertFormInstanceInXFormsModel() {
        String formPlaceHolder = "%%FORM_INSTANCE%%";
        String xmlDeclararion = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        replace(formPlaceHolder, xformsModel.asXML().substring(xmlDeclararion.length()), null);
    }

    private void insertRepeatableBlocksFunctionallity() throws ToolkitRendererException, AuthenticationException {
        insertRepeatableBlockInitialFilling("gensource");
        insertRepeatableBlockTemplatesInXFormsModel();
    }

    private void insertRepeatableBlockInitialFilling(String source) throws ToolkitRendererException, AuthenticationException {
        String formPlaceHolder = "%%repeatable_blocks%%";
        List<Node> nodes = getRepeatableBlockNodes();
        if (nodes != null && !nodes.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Node node : nodes) {
                String repeatable = node.valueOf("@repeatable");
                List<Node> fieldNotEmptyWidgetsInRepeatable;
                try {
                    fieldNotEmptyWidgetsInRepeatable = getWidgetsWithFieldAttributeNotEmptyWithinRepeatableNode(repeatable);
                } catch (ToolkitRendererException e) {
                    continue;
                }
                stringBuilder.append("<xforms:action ev:event = \"xforms-ready\" if = \"instance('exchangedata-instance')/prefilleddata/dkdintserv/" + source + "//repeatable-" + repeatable + "/" + repeatable + "\">");
                stringBuilder.append("<xforms:delete ev:event = \"DOMActivate\" context = \"instance('form-instance')/formcontent/widgetTree/widget//widget[@repeatable='" + repeatable + "']\" nodeset = \"widget\" at = \"1\"/>");
                stringBuilder.append("</xforms:action>");
                stringBuilder.append("<xforms:action ev:event = \"xforms-ready\" xxforms:iterate = \"instance('exchangedata-instance')/prefilleddata/dkdintserv/" + source + "//repeatable-" + repeatable + "/" + repeatable + "\">");
                stringBuilder.append("<xforms:setvalue ref = \"instance('repeatable-" + repeatable + "-instance')/widget/@blockNumber\" value = \"instance('repeatable-" + repeatable + "-instance')/widget/@blockNumber + 1\"/>");
                stringBuilder.append("<xforms:insert ev:event = \"DOMActivate\" context = \"instance('form-instance')/formcontent/widgetTree/widget//widget[@repeatable='" + repeatable + "']\" nodeset = \"widget\" at = \"last()\" position = \"after\" origin = \"instance('repeatable-" + repeatable + "-instance')/widget\"/>");
                for (Node node2 : fieldNotEmptyWidgetsInRepeatable) {
                    stringBuilder.append("<xforms:setvalue ref = \"instance('form-instance')/formcontent/widgetTree/widget//widget[@repeatable='" + repeatable + "']/widget[last()]//widget[@field='" + node2.valueOf("@field") + "']/value\" value = \"context()/" + node2.valueOf("@field") + "\"/>");
                }
                stringBuilder.append("</xforms:action>");
            }
            replace(formPlaceHolder, stringBuilder.toString(), null);
        }
    }

    /**
     * Replaces the placeholder %%REPEATABLE_BLOCK_TEMPLATE_INSTANCES%% in xmlTemplate with the xform model.
     * 
     * @throws AuthenticationException
     * @throws ToolkitRendererException
     */
    private void insertRepeatableBlockTemplatesInXFormsModel() throws ToolkitRendererException, AuthenticationException {
        String formPlaceHolder = "%%REPEATABLE_BLOCK_TEMPLATE_INSTANCES%%";
        String xmlDeclararion = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        List<Node> nodes = getRepeatableBlockNodes();
        if (nodes != null && !nodes.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Node node : nodes) {
                try {
                    String repeatable = node.valueOf("@repeatable");
                    stringBuilder.append("<xforms:instance id=\"repeatable-").append(repeatable).append("-instance\">");
                    String repeatableBlock = XSLTUtil.transform(node.asXML(), "xsl/filterAttributesInWidgetTree.xsl");
                    stringBuilder.append(repeatableBlock.substring(xmlDeclararion.length()));
                    stringBuilder.append("</xforms:instance>");
                } catch (TransformerConfigurationException e) {
                    String message = "A TransformerConfigurationException occurred during filtering attributes fromthe object model: [" + getObjectModelAsString() + "]";
                    LOGGER.error(message);
                    throw new ToolkitRendererException(message, e);
                } catch (TransformerException e) {
                    String message = "A TransformerException occurred during filtering attributes fromthe object model: [" + getObjectModelAsString() + "]";
                    LOGGER.error(message);
                    throw new ToolkitRendererException(message, e);
                }
            }
            replace(formPlaceHolder, stringBuilder.toString(), null);
        }
    }

    /**
     * Get all repeatable blocks in the object model.
     * 
     * @return the list with all repeatable blocks in the object model
     * @throws ToolkitRendererException
     * @throws AuthenticationException
     */
    private List<Node> getRepeatableBlockNodes() throws AuthenticationException {
        String repeatableBlocksXPath = "//widget[@repeatable!='']";
        List<Node> nodes = null;
        try {
            nodes = getWidgets(repeatableBlocksXPath);
        } catch (ToolkitRendererException e) {
            LOGGER.info("No repeatable blocks found.");
        }
        return nodes;
    }

    private List<Node> getWidgetsWithFieldAttributeNotEmptyWithinRepeatableNode(String repeatable) throws ToolkitRendererException, AuthenticationException {
        String widgets = "//widget[@repeatable='" + repeatable + "']/widget//widget[@field!='']";
        List<Node> nodes = getWidgets(widgets);
        return nodes;
    }

    /**
     * Insert all bindings for the nodes in the model. This method first collects the bindings for input controls, then
     * collects bindings for controls that have a relevance with (an)other control(s) and finishes with collecting bindings
     * for elements that should be calculated. After collecting, the bindings are sorted and inserted into the xmlTemplate.
     * The bindings are put in the location of the placeholder string %%CONTROLS_BINDING%%.
     * 
     * @throws ObjectNotFoundException the Exception thrown when an object is searched for in the repository but cannot be
     * found
     * @throws ToolkitRendererException the Exception thrown if anything went wrong
     */
    private void insertBindings() throws ObjectNotFoundException, ToolkitRendererException, AuthenticationException {
        List<String> inputElementsBindings = insertInputElementsBindings();
        List<String> relevanceBindings = insertRelevanceBindings();
        List<String> calculateBindings = insertCalculateBindings();
        List<String> relevanceBindingsNew = new ArrayList<String>();
        List<String> inputElementsBindingsNew = new ArrayList<String>();
        List<Node> repeatableBlocks = getRepeatableBlockNodes();
        List<String> repeatableBlockBindings = new ArrayList<String>();
        if (repeatableBlocks != null && !repeatableBlocks.isEmpty()) {
            List<String> repeatableBlockReferences = new ArrayList<String>();
            for (Node node : repeatableBlocks) {
                Widget dummy = new Widget();
                dummy.setId(Long.parseLong(node.valueOf("@value")));
                dummy.setWidgetType(new WidgetType(node.valueOf("@widgetType")));
                Set<WidgetAttribute> widgetAttributes = new HashSet<WidgetAttribute>();
                widgetAttributes.add(new WidgetAttribute("dkd", node.valueOf("@dkd")));
                dummy.setWidgetAttributes(widgetAttributes);
                String absRef = getAbsoluteReferenceAttribute(dummy).getValue();
                if (!absRef.endsWith("]")) {
                    absRef = absRef + "[1]";
                }
                absRef = absRef + "/widget";
                repeatableBlockReferences.add(absRef);
            }
            List<String> relevanceBindingsToRemove = new ArrayList<String>();
            List<String> inputElementsBindingsToRemove = new ArrayList<String>();
            for (String string : repeatableBlockReferences) {
                StringBuilder repeatableBlockString = new StringBuilder("<xforms:bind nodeset = \"").append(string).append("\">");
                String referenceString = repeatableBlockString.substring(0, repeatableBlockString.length() - 2);
                for (String inputString : inputElementsBindings) {
                    if (inputString.startsWith(referenceString.toString())) {
                        StringBuilder inputStringBuilder = new StringBuilder(inputString);
                        String replaceString = inputString.substring(0, inputString.indexOf(referenceString) + referenceString.length());
                        StringBuilder newStringBuilder = new StringBuilder("<xforms:bind nodeset = \"");
                        String partAfterReplaceString = inputString.substring(inputString.indexOf(replaceString) + replaceString.length());
                        partAfterReplaceString = partAfterReplaceString.substring(partAfterReplaceString.indexOf("/") + 1);
                        replaceString = inputString.substring(0, inputString.indexOf(partAfterReplaceString));
                        if (partAfterReplaceString.startsWith("\"")) {
                            newStringBuilder.append(".");
                        } else if (inputString.substring(inputString.indexOf(replaceString) + replaceString.length()).startsWith("/")) {
                            replaceString = replaceString + "/";
                        }
                        replace(replaceString, newStringBuilder.toString(), inputStringBuilder);
                        String tmp1 = inputStringBuilder.substring(inputStringBuilder.indexOf("\"") + "\"".length());
                        tmp1 = tmp1.substring(0, tmp1.indexOf("\""));
                        String tmp2 = null;
                        if (".".equals(tmp1)) {
                            tmp2 = string;
                        } else {
                            tmp2 = string + "/" + tmp1;
                        }
                        if (inputStringBuilder.indexOf(tmp2) > -1) {
                            replace(tmp2, ".", inputStringBuilder);
                        }
                        if (tmp2.endsWith("/value")) {
                            String tmp2Parent = tmp2.substring(0, tmp2.indexOf("/value"));
                            while (inputStringBuilder.indexOf(tmp2Parent) > -1) {
                                replace(tmp2Parent, "./parent::widget", inputStringBuilder);
                            }
                        }
                        String repeatableBlockPath = referenceString.substring(referenceString.indexOf("instance('form-instance'"));
                        if (!repeatableBlockPath.endsWith("]")) {
                            repeatableBlockPath = repeatableBlockPath + "[1]";
                        }
                        String relativeInputWidgetPath = inputStringBuilder.toString().substring(inputStringBuilder.toString().indexOf("nodeset = \"") + "nodeset = \"".length(), inputStringBuilder.toString().indexOf("/value\""));
                        String absoluteInputWidgetPath = repeatableBlockPath + "/" + relativeInputWidgetPath;
                        String replacementString = ".";
                        replace(absoluteInputWidgetPath, replacementString, inputStringBuilder);
                        replace("/value castable as", " castable as", inputStringBuilder);
                        repeatableBlockString.append(inputStringBuilder.toString());
                        LOGGER.info("SSS " + inputStringBuilder);
                        inputElementsBindingsToRemove.add(inputString);
                    }
                }
                for (String relevanceString : relevanceBindings) {
                    if (relevanceString.startsWith(referenceString.toString())) {
                        StringBuilder relevanceStringBuilder = new StringBuilder(relevanceString);
                        String replaceString = relevanceString.substring(0, relevanceString.indexOf(referenceString) + referenceString.length());
                        StringBuilder newStringBuilder = new StringBuilder("<xforms:bind nodeset = \"");
                        String partAfterReplaceString = relevanceString.substring(relevanceString.indexOf(replaceString) + replaceString.length());
                        partAfterReplaceString = partAfterReplaceString.substring(partAfterReplaceString.indexOf("/") + 1);
                        replaceString = relevanceString.substring(0, relevanceString.indexOf(partAfterReplaceString));
                        if (relevanceString.substring(relevanceString.indexOf(replaceString) + replaceString.length()).startsWith("\"")) {
                            newStringBuilder.append(".");
                        } else if (relevanceString.substring(relevanceString.indexOf(replaceString) + replaceString.length()).startsWith("/")) {
                            replaceString = replaceString + "/";
                        }
                        replace(replaceString, newStringBuilder.toString(), relevanceStringBuilder);
                        String tmp1 = relevanceStringBuilder.substring(relevanceStringBuilder.indexOf("\"") + "\"".length());
                        tmp1 = tmp1.substring(0, tmp1.indexOf("\""));
                        String tmp2 = null;
                        if (".".equals(tmp1)) {
                            tmp2 = string;
                        } else {
                            tmp2 = string + "/" + tmp1;
                        }
                        if (relevanceStringBuilder.indexOf(tmp2) > -1) {
                            replace(tmp2, ".", relevanceStringBuilder);
                        }
                        if (tmp2.endsWith("/value")) {
                            String tmp2Parent = tmp2.substring(0, tmp2.indexOf("/value"));
                            while (relevanceStringBuilder.indexOf(tmp2Parent) > -1) {
                                replace(tmp2Parent, "./parent::widget", relevanceStringBuilder);
                            }
                        }
                        String repeatableBlockPath = referenceString.substring(referenceString.indexOf("instance('form-instance'"));
                        if (!repeatableBlockPath.endsWith("]")) {
                            repeatableBlockPath = repeatableBlockPath + "[1]";
                        }
                        String replacementString = "ancestor::widget[@blockNumber != '']";
                        replace(repeatableBlockPath, replacementString, relevanceStringBuilder);
                        repeatableBlockString.append(relevanceStringBuilder.toString());
                        relevanceBindingsToRemove.add(relevanceString);
                    }
                }
                repeatableBlockString.append("</xforms:bind>");
                repeatableBlockBindings.add(repeatableBlockString.toString());
            }
            if (!inputElementsBindingsToRemove.isEmpty()) {
                outerLoop: for (String inputString : inputElementsBindings) {
                    for (String removeString : inputElementsBindingsToRemove) {
                        if (inputString.equals(removeString)) {
                            continue outerLoop;
                        }
                    }
                    inputElementsBindingsNew.add(inputString);
                }
            } else {
                inputElementsBindingsNew = inputElementsBindings;
            }
            if (!relevanceBindingsToRemove.isEmpty()) {
                outerLoop: for (String relevanceString : relevanceBindings) {
                    for (String removeString : relevanceBindingsToRemove) {
                        if (relevanceString.equals(removeString)) {
                            continue outerLoop;
                        }
                    }
                    relevanceBindingsNew.add(relevanceString);
                }
            } else {
                relevanceBindingsNew = relevanceBindings;
            }
        } else {
            inputElementsBindingsNew = inputElementsBindings;
            relevanceBindingsNew = relevanceBindings;
        }
        List<String> allBindings = new ArrayList<String>();
        allBindings.addAll(inputElementsBindingsNew);
        allBindings.addAll(relevanceBindingsNew);
        allBindings.addAll(repeatableBlockBindings);
        allBindings.addAll(calculateBindings);
        if (!allBindings.isEmpty() && allBindings.size() > 1) {
            Collections.sort(allBindings, new Comparator() {

                public int compare(Object o1, Object o2) {
                    String s1 = (String) o1;
                    String s2 = (String) o2;
                    return s1.compareTo(s2);
                }
            });
        }
        if (!allBindings.isEmpty()) {
            StringBuilder bindingStringBuilder = new StringBuilder();
            for (String string : allBindings) {
                bindingStringBuilder.append(string);
            }
            String formPlaceHolder = "%%CONTROLS_BINDING%%";
            replace(formPlaceHolder, bindingStringBuilder.toString(), null);
        }
    }

    /**
     * Collect bindings for controls that can have a relevance with (an)other control(s). This method returns a list of
     * strings that are the bindings.
     * 
     * @throws ObjectNotFoundException the Exception thrown if an object is searched for it the repository but cannot be
     * found.
     * @throws ToolkitRendererException the Exception thrown if anything went wrong
     */
    private List<String> insertRelevanceBindings() throws ObjectNotFoundException, ToolkitRendererException, AuthenticationException {
        List<WidgetStruct> widgetStructs = new ArrayList<WidgetStruct>();
        getDeepChildWidgetStructs(widgetStructs, getObjectModel(), WidgetType.TAB);
        getDeepChildWidgetStructs(widgetStructs, getObjectModel(), WidgetType.BLOCK);
        List<String> bindingStringList = new ArrayList<String>();
        if (widgetStructs != null && !widgetStructs.isEmpty()) {
            for (WidgetStruct container : widgetStructs) {
                Set<XPath> relevances = container.getChildWidget().getXPathsFromWidgetAttribute("relevance");
                if (relevances != null && !relevances.isEmpty()) {
                    if (!container.getChildWidget().hasWidgetAttribute("kindOfTab") || !container.getChildWidget().getWidgetAttribute("kindOfTab").getValue().equals("Overview")) {
                        replaceDotsButNotDotDotsInXPaths(relevances, container.getChildWidget());
                        updateRelativeReferencesInXPaths(container, relevances);
                    }
                    StringBuilder localStringBuilder = new StringBuilder();
                    localStringBuilder.append("<xforms:bind nodeset = \"");
                    localStringBuilder.append(getAbsoluteReferenceAttribute(container.getChildWidget()).getValue());
                    localStringBuilder.append("\" relevant = \"");
                    addXPathBindings(relevances, localStringBuilder);
                    localStringBuilder.append("\"/>");
                    bindingStringList.add(localStringBuilder.toString());
                }
            }
        }
        return bindingStringList;
    }

    private void replaceBodyPlaceHolders(StringBuilder xhtmlBodyFromFile) throws AuthenticationException, ToolkitAppException, ObjectNotFoundException {
        String orbeon_localhost_url = OrbeonProperties.getPropertyStore().getProperty(OrbeonProperties.ORBEON_LOCALHOST_URL);
        if (StringUtils.isEmpty(orbeon_localhost_url)) {
            String context_root = OrbeonProperties.getPropertyStore().getProperty(OrbeonProperties.ORBEON_CONTEXT_ROOT);
            if (!StringUtils.isEmpty(context_root)) {
                orbeon_localhost_url = new StringBuilder("/").append(context_root).toString();
            }
        }
        replace(RendererConstants.PLACEHOLDER_ORBEON_LOCALHOST_URL, orbeon_localhost_url, xhtmlBodyFromFile);
        replaceNavigationPlaceHolder(xhtmlBodyFromFile);
        replaceBodyContent(xhtmlBodyFromFile);
        replaceGoogleAnalyticsCode(xhtmlBodyFromFile);
    }

    private void replaceBodyContent(StringBuilder xhtmlBodyFromFile) throws AuthenticationException, ToolkitAppException, ObjectNotFoundException {
        StringBuilder sb = new StringBuilder();
        WidgetRenderer formRenderer = RendererFactory.getWidgetRenderer(getObjectModel().getChildWidget(), SCHEME);
        formRenderer.render(getObjectModel(), sb, RendererConstants.RENDER_MODE_NORMAL);
        replace("%%BODY_CONTENT%%", sb.toString(), xhtmlBodyFromFile);
    }

    /**
     * Replaces placeholder %%NAV_TABS%%.
     * 
     * @param xhtmlBodyFromFile
     */
    private void replaceNavigationPlaceHolder(StringBuilder xhtmlBodyFromFile) throws ToolkitRendererException, AuthenticationException {
        List<WidgetStruct> tabWidgetStructs = getChildWidgetStructsByWidgetType(getObjectModel(), WidgetType.TAB);
        if (tabWidgetStructs != null && !tabWidgetStructs.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (WidgetStruct tabWidgetStruct : tabWidgetStructs) {
                Widget tabWidget = tabWidgetStruct.getChildWidget();
                sb.append("<!-- TAB ").append(tabWidget.getId()).append(" -->");
                addTabState(sb, tabWidget, "finished");
                addTabState(sb, tabWidget, "notactive");
                addTabState(sb, tabWidget, "selected");
                addTabState(sb, tabWidget, "notselected");
            }
            replace("%%NAV_TABS%%", sb.toString(), xhtmlBodyFromFile);
        } else {
            replace("%%NAV_TABS%%", "", xhtmlBodyFromFile);
        }
    }

    /**
     * Replace the right Google analytics code
     * 
     * @param xhtmlBodyFromFile
     * @throws ToolkitRendererException
     */
    private void replaceGoogleAnalyticsCode(StringBuilder xhtmlBodyFromFile) throws ToolkitRendererException, AuthenticationException {
        WidgetAttribute google = getObjectModel().getChildWidget().getWidgetAttribute("google");
        String googleAnalyticsCode = "";
        if (google == null || google.getValue() == null || "".equals(google.getValue())) {
            String env = OrbeonProperties.getPropertyStore().getProperty(OrbeonProperties.PROP_ENVIRONMENT);
            googleAnalyticsCode = OrbeonProperties.getPropertyStore().getProperty(env + OrbeonProperties.DOT + OrbeonProperties.ANALYTICS_CODE);
        } else {
            googleAnalyticsCode = google.getValue();
            String env = OrbeonProperties.getPropertyStore().getProperty(OrbeonProperties.PROP_ENVIRONMENT);
            String fileName = OrbeonProperties.getPropertyStore().getProperty(env + OrbeonProperties.DOT + OrbeonProperties.ORBEON_DIR) + OrbeonProperties.getPropertyStore().getProperty(env + OrbeonProperties.DOT + OrbeonProperties.HOME_PAGE_FILE);
            String searchText = "</xhtml:body>";
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                while (reader.ready()) {
                    sb.append(reader.readLine());
                }
                reader.close();
                String fileText = sb.toString();
                if (fileText != null && fileText.indexOf(googleAnalyticsCode) == -1) {
                    String javascriptFragment = "<script type=\"text/javascript\"> try { var pageTracker = _gat._getTracker(\"%%GOOGLE_ANALYTICS%%\"); pageTracker._trackPageview(); } catch(err) {} </script>";
                    javascriptFragment = javascriptFragment.replaceAll("%%GOOGLE_ANALYTICS%%", googleAnalyticsCode);
                    fileText = fileText.replaceAll(searchText, "\n" + javascriptFragment + "\n" + searchText);
                    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                    writer.write(fileText);
                    writer.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        replace("%%GOOGLE_ANALYTICS%%", googleAnalyticsCode, xhtmlBodyFromFile);
    }

    private void addTabState(StringBuilder sb, Widget tabWidget, String state) throws ToolkitRendererException, AuthenticationException {
        sb.append("<!-- ").append(state).append(" -->");
        sb.append("<xforms:group ref=\"instance('form-instance')/formcontrol/tabs/tab[@value='").append(tabWidget.getId()).append("']/").append(state).append("\">");
        sb.append("<xhtml:li>");
        sb.append("<xforms:trigger appearance=\"minimal\" class=\"").append(state).append("\">");
        sb.append("<xforms:label ref=\"instance('form-instance')/formcontrol/tabs/tab[@value='").append(tabWidget.getId()).append("']/label\"/>");
        if (state.equals("notselected")) {
            addActivateLogic(sb, tabWidget);
        }
        sb.append("</xforms:trigger>");
        sb.append("</xhtml:li>");
        sb.append("</xforms:group>");
    }

    private void addActivateLogic(StringBuilder sb, Widget tabWidget) throws ToolkitRendererException, AuthenticationException {
        sb.append("<xforms:action ev:event=\"DOMActivate\">");
        sb.append("<xforms:setvalue ref=\"instance('form-instance')/formcontrol/selectedtab\" value=\"").append(tabWidget.getId()).append("\"/>");
        List<WidgetStruct> tabWidgetStructs = getChildWidgetStructsByWidgetType(getObjectModel(), WidgetType.TAB);
        for (WidgetStruct tabWidgetStruct : tabWidgetStructs) {
            Long tabWidgetLocalId = tabWidgetStruct.getChildWidget().getId();
            sb.append("<xforms:setvalue ref=\"instance('form-instance')/formcontrol/tabs/tab[@value='");
            sb.append(tabWidgetLocalId);
            sb.append("']/@selected\">");
            sb.append(tabWidgetLocalId == tabWidget.getId() ? "true" : "false");
            sb.append("</xforms:setvalue>");
        }
        sb.append("<xxforms:script>scrollUp(0);</xxforms:script>");
        sb.append("<xforms:toggle ev:event=\"DOMActivate\" case=\"");
        sb.append(tabWidget.getDescription());
        sb.append("\"/>");
        sb.append("</xforms:action>");
    }

    private Element createOgoneResultElement() throws ToolkitRendererException, AuthenticationException {
        StringBuilder sb = new StringBuilder();
        try {
            StringBuilder ogoneresult = new StringBuilder(FileHelper.loadStringFromFileOnClasspath("xforms/ogone.xml"));
            String gemeentePlaceHolder = "%%GEMEENTE%%";
            String gemeente = null;
            List<WidgetStruct> widgetStructs = new ArrayList<WidgetStruct>();
            getDeepChildWidgetStructs(widgetStructs, getObjectModel(), WidgetType.OGONE);
            WidgetStruct ogoneWidgetStruct = widgetStructs.get(0);
            Widget ogoneWidget = ogoneWidgetStruct.getChildWidget();
            if (ogoneWidget.hasWidgetAttribute("gemeente") && !ogoneWidget.getWidgetAttribute("gemeente").getValue().isEmpty()) {
                gemeente = ogoneWidget.getWidgetAttribute("gemeente").getValue();
            } else {
                SecurityService securityService = (SecurityService) SpringApplicationContext.getBean("securityService");
                gemeente = securityService.getGroupOfLoggedInApplicationUser().getName();
            }
            replace(gemeentePlaceHolder, gemeente, ogoneresult);
            sb.append(ogoneresult);
        } catch (FileNotFoundException e) {
            String message = "Error loading file xforms/ogone.xml";
            LOGGER.error(message + ": " + e.getMessage());
            throw new ToolkitRendererException(message + ": " + e.getMessage(), e);
        }
        try {
            return DocumentHelper.parseText(sb.toString()).getRootElement();
        } catch (DocumentException e) {
            String message = "Error creating document out of " + sb.toString();
            throw new ToolkitRendererException(message, e);
        }
    }

    /**
     * Create a Element that holds data about the form. It holds information like: - Username that saved the last version
     * (user-name). - Name of the group this form belongs to (group-name). - Timestamp when the form was deployed
     * (modified-date).
     * 
     * @return Element holding the items as described here above.
     * @throws AuthenticationException - When retrieving security settings failes.
     * @throws ToolkitRendererException
     */
    private Element createFormInfoElement() throws AuthenticationException, ToolkitRendererException {
        SecurityService securityService = (SecurityService) SpringApplicationContext.getBean("securityService");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentFormattedDate = sdf.format(new Date());
        Document formGroupDocument = DocumentHelper.createDocument();
        Element formGroupElement = formGroupDocument.addElement("form-info");
        formGroupElement.addAttribute("group-name", securityService.getGroupOfLoggedInApplicationUser().getName());
        formGroupElement.addAttribute("user-name", securityService.getLoggedInApplicationUser().getUser().getUsername());
        formGroupElement.addAttribute("modified-date", currentFormattedDate);
        formGroupElement.addAttribute("form-name", getObjectModel().getChildWidget().getDescription());
        formGroupElement.addAttribute("gemeente", "");
        return formGroupDocument.getRootElement();
    }

    /**
     * Create a constants node in the model and collect all constants in the object model and add nodes for them under the
     * constants node.
     * 
     * @return the constants node, with a child node per constant
     * @throws ToolkitRendererException the Exception thrown if anything went wrong
     */
    private Element createConstantsElement() throws ToolkitRendererException, AuthenticationException {
        Document constantsDocument = DocumentHelper.createDocument();
        Element constants = constantsDocument.addElement("constants");
        Set<String> constantsSet = new HashSet<String>();
        collectAttributeValuesByAttributeName(getObjectModel(), constantsSet, "constant", null);
        for (String string : constantsSet) {
            constants.addElement(string);
        }
        return constantsDocument.getRootElement();
    }

    /**
     * Collect bindings for input controls. This method returns a list of strings that are the bindings.
     * 
     * @return a list of all input control bindings (strings)
     * @throws ObjectNotFoundException the Exception thrown if an object is searched for it the repository but cannot be
     * found.
     * @throws ToolkitRendererException the Exception thrown if anything went wrong
     */
    private List<String> insertInputElementsBindings() throws ObjectNotFoundException, ToolkitRendererException, AuthenticationException {
        List<String> inputElementsBindings = new ArrayList<String>();
        inputElementsBindings.addAll(insertInputElementsBindings(WidgetType.INPUT_TEXT));
        inputElementsBindings.addAll(insertInputElementsBindings(WidgetType.TEXTAREA));
        inputElementsBindings.addAll(insertInputElementsBindings(WidgetType.SELECT_ONE));
        inputElementsBindings.addAll(insertInputElementsBindings(WidgetType.SELECT_MULTIPLE));
        return inputElementsBindings;
    }

    private List<String> insertCalculateBindings() throws ObjectNotFoundException, ToolkitRendererException, AuthenticationException {
        List<String> calculateBindings = new ArrayList<String>();
        if (renderOgone()) {
            StringBuilder bindings = new StringBuilder();
            bindings.append("<xforms:bind nodeset = \"instance('form-instance')/formcontent/ogone/ogoneinput/amount/waarde\"");
            List<WidgetStruct> widgetStructs = new ArrayList<WidgetStruct>();
            getDeepChildWidgetStructs(widgetStructs, getObjectModel(), WidgetType.OGONE);
            WidgetStruct ogoneWidgetStruct = widgetStructs.get(0);
            Widget ogoneWidget = ogoneWidgetStruct.getChildWidget();
            WidgetAttribute amount = ogoneWidget.getWidgetAttribute("amount");
            if (amount != null && !"".equals(amount.getValue())) {
                bindings.append(" calculate=\"");
                String sign = "@";
                List<String> parts = findAllParts(amount.getValue(), sign);
                if (parts != null && !parts.isEmpty()) {
                    for (String part : parts) {
                        String resultLocal = part;
                        String partLocal = part;
                        Set<Widget> widgets = new HashSet<Widget>();
                        while (partLocal.indexOf(sign) > -1) {
                            String descriptionReference = null;
                            if (partLocal.contains(sign + "[")) {
                                String partLocalTemp = partLocal;
                                descriptionReference = partLocalTemp.substring(partLocalTemp.indexOf("[") + 1, partLocalTemp.indexOf("]"));
                            } else {
                                descriptionReference = StringUtil.getStringBetween(partLocal, sign, "([a-zA-Z0-9_]*)");
                            }
                            Widget widget = getWidgetService().getWidgetByDescriptionFormAndVersion(descriptionReference, getObjectModel().getFormWidget(), getObjectModel().getVersion());
                            String widgetPath = getWidgetPath(widget);
                            widgets.add(widget);
                            WidgetAttribute xsdType = widget.getWidgetAttribute("xsdType");
                            if (xsdType != null && !"".equals(xsdType.getValue())) {
                                widgetPath = "number(" + widgetPath + ")";
                            }
                            if (partLocal.contains(sign + "[")) {
                                resultLocal = resultLocal.replaceFirst(sign + "\\[" + descriptionReference + "\\]", widgetPath);
                            } else {
                                resultLocal = resultLocal.replaceFirst(sign + descriptionReference, widgetPath);
                            }
                            partLocal = partLocal.replaceFirst(sign, "");
                        }
                        bindings.append(resultLocal);
                    }
                }
                bindings.append("\"");
            }
            bindings.append("/>");
            calculateBindings.add(bindings.toString());
        }
        return calculateBindings;
    }

    /**
     * Collect bindings for controls that can take a user input. The binding will be formed like: <xforms:bind nodeset ='...'
     * type='...' required='...' readonly='...' constraint='...'/>, in which the type is the type of the control, like string
     * or integer, required indicates whether the control should be filled in by the user or not, readonly indicates whether
     * the user can edit the control and constraint is used to put an input constraint on the control. Required, readonly and
     * constraint follow xpath syntax and therefore evaluate to being either true or false.
     * 
     * @param widgetType the type of the input control
     * @return list of (string) bindings for all (input) nodes in the model that are of the specified WidgetType
     * @throws ObjectNotFoundException the Exception thrown if an object is searched for it the repository but cannot be
     * found.
     * @throws ToolkitRendererException the Exception thrown if anything went wrong
     */
    private List<String> insertInputElementsBindings(WidgetType widgetType) throws ObjectNotFoundException, ToolkitRendererException, AuthenticationException {
        List<WidgetStruct> widgetStructs = new ArrayList<WidgetStruct>();
        getDeepChildWidgetStructs(widgetStructs, getObjectModel(), widgetType);
        StringBuilder type_bindings = null;
        List<String> bindingStringList = new ArrayList<String>();
        for (WidgetStruct widgetStruct : widgetStructs) {
            type_bindings = new StringBuilder();
            Widget childWidget = widgetStruct.getChildWidget();
            type_bindings.append("<xforms:bind nodeset = \"").append(getAbsoluteReferenceAttribute(childWidget).getValue()).append("\"");
            WidgetAttribute xsdType = childWidget.getWidgetAttribute("xsdType");
            if (xsdType != null && !"".equals(xsdType.getValue())) {
                type_bindings.append(" type=\"").append(((WidgetAttribute) childWidget.getWidgetAttribute("xsdType")).getValue()).append("\"");
            }
            WidgetAttribute required = childWidget.getWidgetAttribute("mandatory");
            if (required != null && !"".equals(required.getValue())) {
                type_bindings.append(" required=\"");
                Set<XPath> xpaths = childWidget.getXPathsFromWidgetAttribute("mandatory");
                replaceDotsButNotDotDotsInXPaths(xpaths, childWidget);
                updateRelativeReferencesInXPaths(widgetStruct, xpaths);
                if (xpaths != null && !xpaths.isEmpty()) {
                    type_bindings.append("(");
                    addXPathBindings(xpaths, type_bindings);
                    type_bindings.append(")");
                }
                type_bindings.append("\"");
            }
            WidgetAttribute readonly = childWidget.getWidgetAttribute("readonly");
            if (readonly != null && !"".equals(readonly.getValue())) {
                type_bindings.append(" readonly=\"");
                Set<XPath> xpaths = childWidget.getXPathsFromWidgetAttribute("readonly");
                replaceDotsButNotDotDotsInXPaths(xpaths, childWidget);
                updateRelativeReferencesInXPaths(widgetStruct, xpaths);
                if (xpaths != null && !xpaths.isEmpty()) {
                    type_bindings.append("(");
                    addXPathBindings(xpaths, type_bindings);
                    type_bindings.append(")");
                }
                type_bindings.append("\"");
            }
            WidgetAttribute constraint = childWidget.getWidgetAttribute("constraint");
            if (constraint != null && !"".equals(constraint.getValue())) {
                type_bindings.append(" constraint=\"");
                Set<XPath> xpaths = childWidget.getXPathsFromWidgetAttribute("constraint");
                replaceDotsButNotDotDotsInXPaths(xpaths, childWidget);
                updateRelativeReferencesInXPaths(widgetStruct, xpaths);
                if (xpaths != null && !xpaths.isEmpty()) {
                    type_bindings.append("(");
                    addXPathBindings(xpaths, type_bindings);
                    type_bindings.append(")");
                }
                type_bindings.append("\"");
            }
            type_bindings.append("/>");
            bindingStringList.add(type_bindings.toString());
        }
        return bindingStringList;
    }

    /**
     * Search the specified 'widgetStruct', which is a tree, recursively for (child) Widgets with WidgetAtrributes with the
     * specified name and collect the values. The values are put in the specified Set 'attSet'. This method calls itself
     * recursively, but breaks the recursiveness at widgets with the specified breakAttributeName (leaves those nodes out of
     * the search).
     * 
     * @param widgetStruct the widgetStruct to check whether the child Widget has a WidgetAttribute with the specified
     * 'attributeName'
     * @param attSet the String Set to hold the values of the WidgetAttributes found
     * @param attributeName the name of the WidgetAttribute to search for
     */
    protected void collectAttributeValuesByAttributeName(WidgetStruct widgetStruct, Set<String> attSet, String attributeName, String breakAttributeName) {
        Widget childWidget = widgetStruct.getChildWidget();
        if (breakAttributeName == null || (!childWidget.hasWidgetAttribute(breakAttributeName) || childWidget.getWidgetAttribute(breakAttributeName).getValue().isEmpty())) {
            WidgetAttribute wa = childWidget.getWidgetAttribute(attributeName);
            if (wa != null) {
                attSet.add(wa.getValue());
            }
            for (WidgetStruct childWidgetStruct : widgetStruct.getChildWidgetStructs()) {
                collectAttributeValuesByAttributeName(childWidgetStruct, attSet, attributeName, breakAttributeName);
            }
        }
    }

    /**
     * Create a node for the model of the form WidgetStruct. This method creates a Document with a root node of 'widgetTree'
     * and then calls getObjectModelAsString() to derive a String representation of the object model. If found, the model is
     * added to the document. If not found, a ToolkitRendererException is thrown.
     * 
     * @return an Element of syntax <widgetTree><object_model/></widgetTree>
     * @throws ToolkitRendererException
     */
    private Element createXFormsModelWidgetTreeElement() throws ToolkitRendererException, AuthenticationException {
        Document widgetTreeDocument = DocumentHelper.createDocument();
        Element widgetTree = widgetTreeDocument.addElement("widgetTree");
        try {
            widgetTree.add(DocumentHelper.parseText(XSLTUtil.transform(getObjectModelAsString(), "xsl/filterAttributesInWidgetTree.xsl")).getRootElement());
        } catch (DocumentException e) {
            String message = "A DocumentException occurred during parsing of the object model: [" + getObjectModelAsString() + "]";
            LOGGER.error(message);
            throw new ToolkitRendererException(message, e);
        } catch (TransformerConfigurationException e) {
            String message = "A TransformerConfigurationException occurred during filtering attributes fromthe object model: [" + getObjectModelAsString() + "]";
            LOGGER.error(message);
            throw new ToolkitRendererException(message, e);
        } catch (TransformerException e) {
            String message = "A TransformerException occurred during filtering attributes fromthe object model: [" + getObjectModelAsString() + "]";
            LOGGER.error(message);
            throw new ToolkitRendererException(message, e);
        }
        return widgetTreeDocument.getRootElement();
    }

    /**
     * Create an element in the model for binding the paginate buttons to.
     * 
     * @return the Root element of the model
     * @throws DocumentException the exception thrown if something goes wrong in constructing the model
     */
    private Element createXFormsModelFormControlElement() throws ToolkitRendererException, AuthenticationException {
        Document formcontrolDocument = DocumentHelper.createDocument();
        Element formcontrol = formcontrolDocument.addElement("formcontrol");
        Element tabs = null;
        Element buttons = null;
        List<WidgetStruct> tabWidgetStructs = getChildWidgetStructsByWidgetType(getObjectModel(), WidgetType.TAB);
        if (tabWidgetStructs != null && !tabWidgetStructs.isEmpty()) {
            tabs = formcontrol.addElement("tabs");
            Widget firstTab = ((WidgetStruct) tabWidgetStructs.get(0)).getChildWidget();
            for (WidgetStruct tabWidgetStruct : tabWidgetStructs) {
                Element tab = tabs.addElement("tab");
                tab.addAttribute("value", String.valueOf(tabWidgetStruct.getChildWidget().getId()));
                tab.addAttribute("clickable", tabWidgetStruct.getChildWidget().getId() == firstTab.getId() ? "false" : "true");
                tab.addAttribute("selected", tabWidgetStruct.getChildWidget().getId() == firstTab.getId() ? "true" : "false");
                tab.addElement("label").addText(tabWidgetStruct.getChildWidget().getDescription());
                buttons = tab.addElement("buttons");
                int tabIndex = tabWidgetStructs.indexOf(tabWidgetStruct);
                if (tabIndex > 0) {
                    Element previousActive = buttons.addElement("button");
                    previousActive.addAttribute("value", XFormsPaginateButtonRenderer.PREVIOUS_ACTIVE_STEP_VALUE);
                    previousActive.addElement("label").addText(XFormsPaginateButtonRenderer.PREVIOUS_ACTIVE_STEP_LABEL);
                }
                if (tabIndex < (tabWidgetStructs.size() - 1)) {
                    Element nextActive = buttons.addElement("button");
                    nextActive.addAttribute("value", XFormsPaginateButtonRenderer.NEXT_ACTIVE_STEP_VALUE);
                    nextActive.addElement("label").addText(XFormsPaginateButtonRenderer.NEXT_ACTIVE_STEP_LABEL);
                }
                List<WidgetStruct> buttonWidgetStructs = getChildWidgetStructsByWidgetType(tabWidgetStruct, WidgetType.PAGINATE_BUTTON);
                if (tabWidgetStructs != null && !tabWidgetStructs.isEmpty()) {
                    for (WidgetStruct buttonWidgetStruct : buttonWidgetStructs) {
                        Element button = buttons.addElement("button");
                        button.addAttribute("value", String.valueOf(new StringBuilder().append(buttonWidgetStruct.getChildWidget().getId())));
                        button.addElement("label").addText(buttonWidgetStruct.getChildWidget().getDescription());
                    }
                }
                tab.addElement("selected");
                tab.addElement("notactive");
                tab.addElement("notselected");
                tab.addElement("finished");
            }
        }
        return formcontrolDocument.getRootElement();
    }

    /**
     * Replaces %%TABS_SELECTED_BINDINGS%% in xmlTemplate with:
     * 
     * <xforms:bind nodeset= "instance('form-instance')/formcontrol/tabs/tab[@value='1']/selected" relevant=
     * "instance('form-instance')/formcontrol/tabs/tab[@value='1']/@selected='true'" /> <xforms:bind nodeset=
     * "instance('form-instance')/formcontrol/tabs/tab[@value='2']/selected" relevant=
     * "instance('form-instance')/formcontrol/tabs/tab[@value='2']/@selected='true'" /> <xforms:bind nodeset=
     * "instance('form-instance')/formcontrol/tabs/tab[@value='3']/selected" relevant=
     * "instance('form-instance')/formcontrol/tabs/tab[@value='3']/@selected='true'" /> <xforms:bind nodeset=
     * "instance('form-instance')/formcontrol/tabs/tab[@value='4']/selected" relevant=
     * "instance('form-instance')/formcontrol/tabs/tab[@value='4']/@selected='true'" /> <xforms:bind nodeset=
     * "instance('form-instance')/formcontrol/tabs/tab[@value='5']/selected" relevant=
     * "instance('form-instance')/formcontrol/tabs/tab[@value='5']/@selected='true'" />
     */
    private StringBuilder getTabsSelectedBindings(List<WidgetStruct> tabWidgetStructs) {
        StringBuilder sb = new StringBuilder();
        for (WidgetStruct tabWidgetStruct : tabWidgetStructs) {
            Widget childWidget = tabWidgetStruct.getChildWidget();
            sb.append("<xforms:bind nodeset=\"instance('form-instance')/formcontrol/tabs/tab[@value='");
            sb.append(childWidget.getId());
            sb.append("']/selected\" relevant = \"instance('form-instance')/formcontrol/tabs/tab[@value='");
            sb.append(childWidget.getId());
            sb.append("']/@selected='true'\"/>");
        }
        return sb;
    }

    private StringBuilder getTabsNotSelectedBindings(List<WidgetStruct> tabWidgetStructs) throws ObjectNotFoundException, ToolkitRendererException, AuthenticationException {
        StringBuilder sb = new StringBuilder();
        for (WidgetStruct tabWidgetStruct : tabWidgetStructs) {
            Widget childWidget = tabWidgetStruct.getChildWidget();
            sb.append("<xforms:bind nodeset = \"instance('form-instance')/formcontrol/tabs/tab[@value='");
            sb.append(childWidget.getId());
            sb.append("']/notselected\" relevant = \"");
            sb.append("not (instance('form-instance')/formcontrol/tabs/tab[@value='");
            sb.append(childWidget.getId());
            sb.append("']/@selected='true')");
            Set<XPath> relevances = childWidget.getXPathsFromWidgetAttribute("relevance");
            if (!tabWidgetStruct.getChildWidget().hasWidgetAttribute("kindOfTab") || !tabWidgetStruct.getChildWidget().getWidgetAttribute("kindOfTab").getValue().equals("Overview")) {
                replaceDotsButNotDotDotsInXPaths(relevances, tabWidgetStruct.getChildWidget());
                updateRelativeReferencesInXPaths(tabWidgetStruct, relevances);
            }
            if (relevances != null && !relevances.isEmpty()) {
                sb.append("and (");
                addXPathBindings(relevances, sb);
                sb.append(") ");
            }
            sb.append("\"/>");
        }
        return sb;
    }

    private StringBuilder getButtonsBinding(List<WidgetStruct> tabWidgetStructs) throws ObjectNotFoundException, ToolkitRendererException, AuthenticationException {
        StringBuilder sb = new StringBuilder();
        for (WidgetStruct tabWidgetStruct : tabWidgetStructs) {
            int tabIndex = tabWidgetStructs.indexOf(tabWidgetStruct);
            if (tabIndex < (tabWidgetStructs.size() - 1)) {
                sb.append("<xforms:bind nodeset = \"instance('form-instance')/formcontrol/tabs/tab[@value='");
                sb.append(tabWidgetStruct.getChildWidget().getId());
                sb.append("']/buttons/button[@value='");
                sb.append(XFormsPaginateButtonRenderer.NEXT_ACTIVE_STEP_VALUE);
                sb.append("']\" relevant = \"");
                int i = 0;
                StringBuilder sbLocal = new StringBuilder();
                for (WidgetStruct tab : tabWidgetStructs) {
                    if (i > tabIndex) {
                        if (i > (tabIndex + 1)) {
                            sbLocal.append(" or ");
                        }
                        sbLocal.append("not(exf:relevant(instance('form-instance')/formcontrol/tabs/tab[@value='" + tab.getChildWidget().getId() + "']/notactive))");
                    }
                    i++;
                }
                if (sbLocal.length() > 0) {
                    sb.append(sbLocal);
                } else {
                    sb.append("true()");
                }
                sb.append("\"/>");
            }
        }
        return sb;
    }

    private StringBuilder getTabsFinishedBindings(List<WidgetStruct> tabWidgetStructs) throws ObjectNotFoundException, ToolkitRendererException, AuthenticationException {
        StringBuilder sb = new StringBuilder();
        for (WidgetStruct tabWidgetStruct : tabWidgetStructs) {
            Widget childWidget = tabWidgetStruct.getChildWidget();
            sb.append("<xforms:bind nodeset = \"instance('form-instance')/formcontrol/tabs/tab[@value='");
            sb.append(childWidget.getId());
            sb.append("']/finished\" relevant = \"false()\"/>");
        }
        return sb;
    }

    private StringBuilder getTabsNotActiveBindings(List<WidgetStruct> tabWidgetStructs) throws ObjectNotFoundException, ToolkitRendererException, AuthenticationException {
        StringBuilder sb = new StringBuilder();
        for (WidgetStruct tabWidgetStruct : tabWidgetStructs) {
            Widget childWidget = tabWidgetStruct.getChildWidget();
            LOGGER.info(childWidget.getDescription());
            sb.append("<xforms:bind nodeset = \"instance('form-instance')/formcontrol/tabs/tab[@value='");
            sb.append(childWidget.getId());
            sb.append("']/notactive\" relevant = \"");
            Set<XPath> relevances = childWidget.getXPathsFromWidgetAttribute("relevance");
            if (!tabWidgetStruct.getChildWidget().hasWidgetAttribute("kindOfTab") || !tabWidgetStruct.getChildWidget().getWidgetAttribute("kindOfTab").getValue().equals("Overview")) {
                replaceDotsButNotDotDotsInXPaths(relevances, tabWidgetStruct.getChildWidget());
                updateRelativeReferencesInXPaths(tabWidgetStruct, relevances);
            }
            if (relevances != null && !relevances.isEmpty()) {
                sb.append("not (");
                addXPathBindings(relevances, sb);
                sb.append(")");
            } else {
                sb.append("false");
            }
            sb.append("\"/>");
        }
        return sb;
    }
}
