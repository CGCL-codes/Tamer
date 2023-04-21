package org.itsnat.feashow;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatDocumentTemplate;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLTextAreaElement;

public abstract class FeatureTreeNode {

    public static final int NONE_PANEL = 0;

    public static final int EXAMPLE_PANEL = 1;

    public static final int DOC_PANEL = 2;

    public static final int CODE_PANEL = 3;

    protected FeatureTreeBuilder treeBuilder;

    protected int currentPanel = NONE_PANEL;

    protected Element logElem;

    protected boolean hasExample;

    protected boolean hasExplanation;

    protected boolean hasSourceCode;

    protected String featureName;

    protected String treeNodeText;

    protected String title;

    public FeatureTreeNode() {
    }

    public void init(FeatureTreeBuilder treeBuilder, boolean hasExample, boolean hasExplanation, boolean hasSourceCode, String featureName, String treeNodeText, String title) {
        this.treeBuilder = treeBuilder;
        this.hasExample = hasExample;
        this.hasExplanation = hasExplanation;
        this.hasSourceCode = hasSourceCode;
        this.featureName = featureName;
        this.treeNodeText = treeNodeText;
        this.title = title;
    }

    public int getPanelCode(String panelName) {
        if (panelName.equals("ex")) return EXAMPLE_PANEL; else if (panelName.equals("code")) return CODE_PANEL; else if (panelName.equals("doc")) return DOC_PANEL; else if (panelName.equals("default")) return getFirstPanel(); else return NONE_PANEL;
    }

    public int getFirstPanel() {
        if (hasExample) return EXAMPLE_PANEL; else if (hasSourceCode) return CODE_PANEL; else if (hasExplanation) return DOC_PANEL;
        return 0;
    }

    public Element getTabElement(int panel) {
        switch(panel) {
            case FeatureTreeNode.EXAMPLE_PANEL:
                return treeBuilder.getExampleTabElement();
            case FeatureTreeNode.CODE_PANEL:
                return treeBuilder.getCodeTabElement();
            case FeatureTreeNode.DOC_PANEL:
                return treeBuilder.getDocTabElement();
            default:
                return null;
        }
    }

    public boolean hasExample() {
        return hasExample;
    }

    public boolean hasSourceCode() {
        return hasSourceCode;
    }

    public boolean hasExplanation() {
        return hasExplanation;
    }

    public String getFeatureName() {
        return featureName;
    }

    public String getTreeNodeText() {
        return treeNodeText;
    }

    public String getTitle() {
        return title;
    }

    public FeatureTreeBuilder getFeatureTreeBuilder() {
        return treeBuilder;
    }

    public ItsNatDocument getItsNatDocument() {
        return treeBuilder.getItsNatDocument();
    }

    public String getExamplePanelName() {
        return getFeatureName() + ".ex";
    }

    public String getDocPanelName() {
        return getFeatureName() + ".doc";
    }

    public String getCodePanelName() {
        return getFeatureName() + ".code";
    }

    public String toString() {
        return getTreeNodeText();
    }

    public void endPanels() {
        endCurrentPanel();
    }

    public void endCurrentPanel() {
        if (currentPanel == EXAMPLE_PANEL) endExamplePanel();
        this.currentPanel = NONE_PANEL;
        this.logElem = null;
    }

    public String getPanelName(int panel) {
        switch(panel) {
            case FeatureTreeNode.EXAMPLE_PANEL:
                return getExamplePanelName();
            case FeatureTreeNode.DOC_PANEL:
                return getDocPanelName();
            case FeatureTreeNode.CODE_PANEL:
                return getCodePanelName();
        }
        return null;
    }

    public abstract void startExamplePanel();

    public abstract void endExamplePanel();

    public boolean hasLog() {
        return (logElem != null);
    }

    public void log(String text) {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        if (logElem == null) {
            this.logElem = doc.getElementById("logId");
            if (logElem == null) throw new RuntimeException("Internal Error: Log element is not defined");
        }
        logElem.removeAttribute("style");
        int len = logElem.getChildNodes().getLength();
        if (len <= 30) {
            if (len == 30) text = "Reached the log view limit!!";
            Element msgElem = doc.createElement("div");
            msgElem.appendChild(doc.createTextNode(text));
            logElem.appendChild(msgElem);
        }
    }

    public Element getFeatureBoxElement() {
        return treeBuilder.getFeatureBoxElement();
    }

    public void startCodePanel() {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Element parent = getFeatureBoxElement();
        NodeList textAreas = parent.getElementsByTagName("textarea");
        for (int i = 0; i < textAreas.getLength(); i++) {
            HTMLTextAreaElement textAreaElem = (HTMLTextAreaElement) textAreas.item(i);
            String classAttr = textAreaElem.getAttribute("class");
            textAreaElem.setAttribute("class", classAttr + ":collapse:nogutter");
            if (classAttr.startsWith("html")) SyntaxHighlighter.highlightMarkup(textAreaElem, itsNatDoc); else if (classAttr.startsWith("xml")) SyntaxHighlighter.highlightMarkup(textAreaElem, itsNatDoc); else SyntaxHighlighter.highlightJava(textAreaElem, itsNatDoc);
        }
    }

    public void selectFeature() {
        String featTitle = getTitle();
        ItsNatDOMUtil.setTextContent(treeBuilder.getFeatureTitleElement(), featTitle);
        String docTitle = featTitle + ". ItsNat: Feature Set & Examples";
        ItsNatDocument itsNatDoc = getItsNatDocument();
        if (itsNatDoc.isLoading() && itsNatDoc.getItsNatDocumentTemplate().isFastLoadMode()) ItsNatDOMUtil.setTextContent(treeBuilder.getTitleElement(), docTitle); else itsNatDoc.addCodeToSend("document.title =\"" + docTitle + "\";");
        initTab(treeBuilder.getExampleTabElement(), hasExample());
        initTab(treeBuilder.getCodeTabElement(), hasSourceCode());
        initTab(treeBuilder.getDocTabElement(), hasExplanation());
        initTabPermalink(treeBuilder.getExampleTabPermalinkElement(), hasExample(), "ex");
        initTabPermalink(treeBuilder.getCodeTabPermalinkElement(), hasSourceCode(), "code");
        initTabPermalink(treeBuilder.getDocTabPermalinkElement(), hasExplanation(), "doc");
        selectPanel(getFirstPanel());
    }

    public void initTabPermalink(HTMLAnchorElement link, boolean isVisible, String tabName) {
        if (isVisible) {
            ItsNatDocumentTemplate mainTemplate = getItsNatDocument().getItsNatDocumentTemplate();
            link.setHref("?itsnat_doc_name=" + mainTemplate.getName() + "&feature=" + getFeatureName() + "." + tabName);
        } else link.setHref("javascript:void(0);");
    }

    public void selectPanel(int panel) {
        endCurrentPanel();
        this.currentPanel = panel;
        String panelName = getPanelName(panel);
        treeBuilder.getItsNatFreeInclude().includeFragment(panelName, false);
        switch(currentPanel) {
            case EXAMPLE_PANEL:
                startExamplePanel();
                break;
            case CODE_PANEL:
                startCodePanel();
                break;
        }
        if (hasExample()) setTabNotActive(treeBuilder.getExampleTabElement());
        if (hasSourceCode()) setTabNotActive(treeBuilder.getCodeTabElement());
        if (hasExplanation()) setTabNotActive(treeBuilder.getDocTabElement());
        Element tabElem = getTabElement(panel);
        setTabActive(tabElem);
    }

    public void setTabNotActive(Element elem) {
        elem.removeAttribute("style");
    }

    public void setTabActive(Element elem) {
        elem.setAttribute("style", "background: #ED752A; border: 3px #ED752A solid;");
    }

    public void initTab(Element elem, boolean visible) {
        if (visible) elem.removeAttribute("style"); else elem.setAttribute("style", "visibility: hidden;");
    }

    public void setJoystickMode(boolean value) {
    }
}
