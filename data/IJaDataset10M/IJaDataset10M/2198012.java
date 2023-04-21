package org.servingMathematics.mqat.ui.WizardEditor;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.*;
import javax.swing.text.html.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.cyberneko.html.HTMLConfiguration;
import org.imsglobal.qti.dom.QTIDocument;
import org.imsglobal.qti.dom.TemplateDeclaration;
import org.xml.sax.InputSource;
import org.servingMathematics.mathqti.dom.MathQTIDocument;
import org.servingMathematics.mqat.io.MqatDocument;
import org.servingMathematics.mqat.io.MqatDocumentWrapper;
import org.servingMathematics.mqat.io.XMLFileHandler;
import org.servingMathematics.mqat.ui.AbstractBasicEditorPanel;
import org.servingMathematics.mqat.ui.LineNumbers;
import org.servingMathematics.mqat.ui.XmlSyntaxEditorPane;
import org.servingMathematics.mqat.ui.WizardEditor.QTIElementsSettingsDialogs.ObjectElementDialog;
import org.servingMathematics.mqat.util.*;
import org.servingMathematics.qti.domImpl.Utils;
import uk.ac.imperial.ma.mathsEngine.mathematics.MathAnnotation;
import uk.ac.imperial.ma.mathsEngine.parsers.ExistingVariable;
import uk.ac.imperial.ma.mathsEngine.parsers.ExistingVariables;
import uk.ac.imperial.ma.mathsEngine.parsers.ParserFactory;

/**
 * TODO Class description
 * TODO The whole class needs to be largely refactored !
 * 
 * @author <a href="mailto:j.kahovec@imperial.ac.uk">Jakub Kahovec</a>
 * @version 0.1
 */
public class QuestionTextPanel extends AbstractBasicEditorPanel implements ChangeListener, ActionListener {

    JToolBar tbrEditingControls;

    JEditorPane edpSource;

    JEditorPane edpHTMLView;

    JTabbedPane tbpItemBody;

    Element itemBodyTextElement;

    JPopupMenu mnuQtiElementEditMenu;

    javax.swing.text.Element currentTextElement;

    private JComboBox cmbInteractionType;

    private JComboBox cmbVariable;

    private static final int TAB_DESIGN = 0;

    private static final int TAB_SOURCE = 1;

    /**
     * The constructor.
     * 
     * @param docWrapper
     */
    public QuestionTextPanel(MqatDocumentWrapper docWrapper) {
        super(docWrapper);
        initGUI();
    }

    private void initGUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new java.awt.Dimension(527, 276));
        JPanel pnlSource = createSourcePanel();
        JPanel pnlDesign = createDesignPanel();
        tbpItemBody = new JTabbedPane(JTabbedPane.BOTTOM);
        tbpItemBody.addTab("Design", pnlDesign);
        tbpItemBody.addTab("Source", pnlSource);
        tbpItemBody.addChangeListener(this);
        add("Center", tbpItemBody);
        final KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        focusManager.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (focusManager.getFocusOwner() != null && focusManager.getFocusOwner().equals(tbpItemBody)) {
                    switch(tbpItemBody.getSelectedIndex()) {
                        case TAB_SOURCE:
                            if (!edpSource.hasFocus()) {
                                edpSource.requestFocus();
                            }
                            break;
                        case TAB_DESIGN:
                            if (!edpHTMLView.hasFocus()) {
                                edpHTMLView.requestFocus();
                            }
                            break;
                    }
                }
            }
        });
    }

    private JPanel createSourcePanel() {
        JPanel newPanel = new JPanel(new BorderLayout());
        edpSource = new XmlSyntaxEditorPane();
        edpSource.addFocusListener(this);
        edpSource.addKeyListener(this);
        JScrollPane scpSource = new JScrollPane(edpSource);
        LineNumbers lineNumbers = new LineNumbers(edpSource, scpSource);
        scpSource.getViewport().add(edpSource, null);
        scpSource.setRowHeaderView(lineNumbers);
        newPanel.add("Center", scpSource);
        return newPanel;
    }

    private JToolBar createEditingControlsToolBar() {
        JToolBar newToolBar = new JToolBar();
        newToolBar.setFloatable(false);
        newToolBar.setRollover(true);
        Object[] headingTags = { "h1", "h2", "h3", "h4", "h5", "h6" };
        JComboBox cmbHeadingTags = new JComboBox(headingTags);
        cmbHeadingTags.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                StyleAction sa = new StyleAction("headerTag", HTML.getTag((String) ((JComboBox) e.getSource()).getSelectedItem()));
                sa.actionPerformed(e);
            }
        });
        cmbHeadingTags.setMaximumSize(new Dimension(100, 25));
        newToolBar.addSeparator();
        JButton button;
        button = new JButton();
        button.setAction(new StyledEditorKit.BoldAction());
        ResourceManager.localizeItem("wizard_question_text_btn_bold", button);
        newToolBar.add(button);
        button = new JButton();
        button.setAction(new StyledEditorKit.ItalicAction());
        ResourceManager.localizeItem("wizard_question_text_btn_italic", button);
        newToolBar.add(button);
        button = new JButton();
        button.setAction(new StyledEditorKit.UnderlineAction());
        ResourceManager.localizeItem("wizard_question_text_btn_underline", button);
        newToolBar.add(button);
        button = new JButton();
        button.setAction(new StyleAction("emphasis", HTML.Tag.CITE));
        ResourceManager.localizeItem("wizard_question_text_btn_cite", button);
        newToolBar.add(button);
        newToolBar.addSeparator();
        JButton btnInteractionType = new JButton(ResourceManager.getString("wizard_question_text_btn_interaction_type"));
        btnInteractionType.setActionCommand("add_interaction");
        btnInteractionType.addActionListener(this);
        newToolBar.add(btnInteractionType);
        ListItem[] interactionTypes = { new ListItem(MqatDocument.TEXT_ENTRY_INTERACTION, "Text entry") };
        cmbInteractionType = new JComboBox(interactionTypes);
        cmbInteractionType.setMaximumSize(new Dimension(120, 25));
        cmbInteractionType.setSelectedIndex(0);
        newToolBar.add(cmbInteractionType);
        button = new JButton();
        ResourceManager.localizeItem("wizard_question_text_btn_image", button);
        button.setActionCommand("add_object");
        button.addActionListener(this);
        newToolBar.add(button);
        newToolBar.addSeparator();
        JButton btnVariable = new JButton(ResourceManager.getString("wizard_question_text_btn_variable"));
        btnVariable.setActionCommand("add_variable");
        btnVariable.addActionListener(this);
        newToolBar.add(btnVariable);
        cmbVariable = new JComboBox();
        cmbVariable.setMaximumSize(new Dimension(120, 25));
        newToolBar.add(cmbVariable);
        newToolBar.addSeparator();
        button = new JButton();
        ResourceManager.localizeItem("wizard_question_text_btn_equation", button);
        button.setActionCommand("add_equation");
        button.addActionListener(this);
        newToolBar.add(button);
        newToolBar.addSeparator();
        return newToolBar;
    }

    private JPanel createDesignPanel() {
        JPanel newPanel = new JPanel(new BorderLayout());
        edpHTMLView = new JEditorPane();
        edpHTMLView.addFocusListener(this);
        edpHTMLView.addKeyListener(this);
        edpHTMLView.setContentType("text/html");
        edpHTMLView.setEditorKit(new QuestionTextHTMLEditorKit());
        edpHTMLView.setTransferHandler(new TransferHandler() {

            public boolean importData(JComponent c, Transferable t) {
                try {
                    String identifier = (String) t.getTransferData(DataFlavor.stringFlavor);
                    boolean result = addPrintedVariableToQuestionText(identifier);
                    edpHTMLView.requestFocus();
                    return result;
                } catch (Exception ex) {
                    ExceptionHandler.handle(ex, ExceptionHandler.SILENT);
                }
                return false;
            }

            public boolean canImport(JComponent c, DataFlavor[] flavors) {
                for (int i = 0; i < flavors.length; i++) {
                    if (DataFlavor.stringFlavor.equals(flavors[i])) {
                        return true;
                    }
                }
                return false;
            }
        });
        mnuQtiElementEditMenu = createQtiElementPopupMenu();
        JScrollPane scpHTMLView = new JScrollPane(edpHTMLView);
        scpHTMLView.getViewport().add(edpHTMLView, null);
        tbrEditingControls = createEditingControlsToolBar();
        newPanel.add("North", tbrEditingControls);
        newPanel.add("Center", scpHTMLView);
        return newPanel;
    }

    protected boolean addPrintedVariableToQuestionText(String identifier) {
        try {
            HTMLDocument htmlDoc = (HTMLDocument) (edpHTMLView.getDocument());
            QuestionTextHTMLEditorKit k = (QuestionTextHTMLEditorKit) edpHTMLView.getEditorKit();
            String uid = CommonUtil.getUniqueID();
            Element newElement = itemBodyTextElement.getOwnerDocument().createElement(QuestionBodyElement.printedVariable.name());
            newElement.setAttribute("identifier", identifier);
            newElement.setAttribute(MqatDocument.UID, uid);
            newElement.setIdAttribute(MqatDocument.UID, true);
            itemBodyTextElement.appendChild(newElement);
            MutableAttributeSet emptySpaceAttr = new SimpleAttributeSet();
            emptySpaceAttr.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
            htmlDoc.insertString(edpHTMLView.getCaretPosition(), " ", emptySpaceAttr);
            k.insertHTML(htmlDoc, edpHTMLView.getCaretPosition(), "<p><" + QuestionBodyElement.printedVariable.name() + " " + MqatDocument.UID + "=" + uid + ">", 0, 0, QuestionBodyElement.printedVariable.getHtmlTag());
            return true;
        } catch (Exception ex) {
            ExceptionHandler.handle(ex, ExceptionHandler.SILENT);
        }
        return false;
    }

    /** 
     * Sets/removes the ID attribute to QTI children elements.
     *  
     * @param parentElement Element the element whose children are to be set/remove ID.
     * @param idAttrName String the name of the ID attribute .
     * @param set boolean if <code>set</code> is true the ID attribute is to be set.
     *  othwerise removed   
     */
    private void setIDToChildren(Element parentElement, String idAttrName, boolean set) {
        Element childElement;
        NodeList children = parentElement.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element) {
                childElement = (Element) children.item(i);
                boolean isQtiTag = false;
                for (QuestionBodyElement tag : QuestionBodyElement.values()) {
                    if (childElement.getNodeName().equals(tag.name())) {
                        isQtiTag = true;
                        break;
                    }
                }
                if (isQtiTag) {
                    if (set) {
                        childElement.setAttribute(idAttrName, new Long(CommonUtil.getUniqueID()).toString());
                        childElement.setIdAttribute(idAttrName, true);
                    } else {
                        childElement.setIdAttribute(idAttrName, false);
                        childElement.removeAttribute(idAttrName);
                    }
                } else {
                    setIDToChildren(childElement, idAttrName, set);
                }
            }
        }
    }

    private Element getXhtmlQuestionTextElement(String htmlQuestionText) {
        Element newItemBodyElement;
        DOMParser parser = new DOMParser(new HTMLConfiguration());
        try {
            parser.setFeature("http://cyberneko.org/html/features/balance-tags", false);
            parser.setFeature("http://cyberneko.org/html/features/insert-namespaces", true);
            parser.setProperty("http://cyberneko.org/html/properties/names/elems", "match");
            parser.setProperty("http://cyberneko.org/html/properties/names/attrs", "no-change");
            parser.setProperty("http://cyberneko.org/html/properties/namespaces-uri", QTIDocument.QTI_NAMESPACE_URI);
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(htmlQuestionText));
            XMLDocumentFilter[] filters = { new HTMLBodyToXTHML() };
            parser.setProperty("http://cyberneko.org/html/properties/filters", filters);
            parser.parse(is);
            newItemBodyElement = parser.getDocument().getDocumentElement();
            replacePsedoQTIElements(newItemBodyElement, itemBodyTextElement);
            setIDToChildren(newItemBodyElement, MqatDocument.UID, false);
        } catch (Exception ex) {
            ExceptionHandler.handle(ex, ExceptionHandler.NO_VISUAL);
            newItemBodyElement = itemBodyTextElement;
        }
        return newItemBodyElement;
    }

    public void focusLost(FocusEvent evt) {
        Element newItemBodyElement;
        if (evt.getComponent() == edpSource) {
            XmlHelper.setXmlText(getAssessmentItem().getItemBody(), XmlHelper.encloseFrag(edpSource.getText()));
        } else if (evt.getComponent() == edpHTMLView) {
            newItemBodyElement = getXhtmlQuestionTextElement(edpHTMLView.getText());
            XmlHelper.setXmlText(getAssessmentItem().getItemBody(), newItemBodyElement);
        }
    }

    private String getHtmlQuestionText(Element xhtmlQuestionTextElement) {
        String questionText;
        String originalQuestionText;
        originalQuestionText = XmlHelper.getXmlText(xhtmlQuestionTextElement, true, true);
        try {
            setIDToChildren(xhtmlQuestionTextElement, MqatDocument.UID, true);
            Element tmpItemBody = (Element) xhtmlQuestionTextElement.cloneNode(true);
            DOMHelper.removeChildrenOfNonHTMLElements(tmpItemBody);
            questionText = XmlHelper.getXmlText(tmpItemBody, true, true);
            DOMParser parser = new DOMParser(new HTMLConfiguration());
            parser.setFeature("http://cyberneko.org/html/features/balance-tags", false);
            parser.setProperty("http://cyberneko.org/html/properties/names/elems", "match");
            parser.setProperty("http://cyberneko.org/html/properties/names/attrs", "no-change");
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(questionText));
            StringWriter sw = new StringWriter();
            XMLDocumentFilter[] filters = { new QTIBodyToHTML(), new org.cyberneko.html.filters.Writer(sw, "UTF-8") };
            parser.setProperty("http://cyberneko.org/html/properties/filters", filters);
            parser.parse(is);
            questionText = sw.toString();
        } catch (Exception ex) {
            ExceptionHandler.handle(ex, ExceptionHandler.NO_VISUAL);
            questionText = originalQuestionText;
        }
        return questionText;
    }

    /**
     * Adds an extra space between two 'neighbour' QTI tags  in order that they 
     * could be edited easily.
     */
    private void addExtraSpacesBetweenElements() {
        HTMLDocument htmlDoc = (HTMLDocument) (edpHTMLView.getDocument());
        ElementIterator it = new ElementIterator(htmlDoc);
        javax.swing.text.Element currElement;
        Object previousTag = null;
        while ((currElement = it.next()) != null) {
            Object tag = currElement.getAttributes().getAttribute(StyleConstants.NameAttribute);
            boolean qtiElement = false;
            for (QuestionBodyElement bodyElement : QuestionBodyElement.values()) {
                if (tag.equals(bodyElement.getHtmlTag())) {
                    qtiElement = true;
                    break;
                }
            }
            if (qtiElement) {
                if ((previousTag != null) && (previousTag instanceof HTML.UnknownTag)) {
                    try {
                        htmlDoc.insertBeforeStart(currElement, "&nbsp;");
                        currElement = it.next();
                    } catch (Exception ex) {
                        ExceptionHandler.handle(ex, ExceptionHandler.SILENT);
                    }
                }
            }
            previousTag = tag;
        }
    }

    protected void updateVariablesList() {
        TemplateDeclaration[] variables = getAssessmentItem().getTemplateDeclarations();
        cmbVariable.removeAllItems();
        for (int i = 0; i < variables.length; i++) {
            cmbVariable.addItem(variables[i].getIdentifier());
        }
    }

    public void updateContent() {
        String questionText;
        try {
            updateVariablesList();
            itemBodyTextElement = (Element) getAssessmentItem().getItemBody().cloneNode(true);
            questionText = XmlHelper.getXmlText(itemBodyTextElement, true, true);
            edpSource.setText(questionText);
            questionText = getHtmlQuestionText(itemBodyTextElement);
            edpHTMLView.setText(questionText);
            addExtraSpacesBetweenElements();
            switch(tbpItemBody.getSelectedIndex()) {
                case TAB_DESIGN:
                    edpHTMLView.requestFocus();
                    break;
                case TAB_SOURCE:
                    edpSource.requestFocus();
                    break;
            }
        } catch (Exception ex) {
            ExceptionHandler.handle(ex, ExceptionHandler.NO_VISUAL);
        }
    }

    public void actionPerformed(ActionEvent e) {
        String actionCmd = e.getActionCommand();
        HTMLDocument htmlDoc = (HTMLDocument) (edpHTMLView.getDocument());
        QuestionTextHTMLEditorKit kit = (QuestionTextHTMLEditorKit) edpHTMLView.getEditorKit();
        MutableAttributeSet emptySpaceAttr = new SimpleAttributeSet();
        emptySpaceAttr.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
        String uid = CommonUtil.getUniqueID();
        Element newElement;
        if (actionCmd.equalsIgnoreCase("add_interaction")) {
            Object selectedItem = cmbInteractionType.getSelectedItem();
            int interactionType = ((Integer) ((ListItem) selectedItem).getData()).intValue();
            switch(interactionType) {
                case MqatDocument.TEXT_ENTRY_INTERACTION:
                    newElement = itemBodyTextElement.getOwnerDocument().createElement(QuestionBodyElement.textEntryInteraction.name());
                    newElement.setAttribute("expectedLength", "10");
                    newElement.setAttribute("responseIdentifier", "RESPONSE");
                    newElement.setAttribute(MqatDocument.UID, uid);
                    newElement.setIdAttribute(MqatDocument.UID, true);
                    itemBodyTextElement.appendChild(newElement);
                    try {
                        htmlDoc.insertString(edpHTMLView.getCaretPosition(), " ", emptySpaceAttr);
                        kit.insertHTML(htmlDoc, edpHTMLView.getCaretPosition(), "<p><" + QuestionBodyElement.textEntryInteraction.getHtmlTag() + "  " + MqatDocument.UID + "=" + uid + ">", 0, 0, QuestionBodyElement.textEntryInteraction.getHtmlTag());
                    } catch (Exception ex) {
                        ExceptionHandler.handle(ex, ExceptionHandler.SILENT);
                    }
                    mqatDocWrapper.setDirty(true);
                    break;
                case MqatDocument.CHOICE_INTERACTION:
                    newElement = itemBodyTextElement.getOwnerDocument().createElement(QuestionBodyElement.choiceInteraction.name());
                    newElement.setAttribute("shuffle", "false");
                    newElement.setAttribute("maxChoices", "1");
                    newElement.setAttribute("responseIdentifier", "RESPONSE");
                    newElement.setAttribute(MqatDocument.UID, uid);
                    newElement.setIdAttribute(MqatDocument.UID, true);
                    itemBodyTextElement.appendChild(newElement);
                    try {
                        htmlDoc.insertString(edpHTMLView.getCaretPosition(), " ", emptySpaceAttr);
                        kit.insertHTML(htmlDoc, edpHTMLView.getCaretPosition(), "<p><" + QuestionBodyElement.choiceInteraction.getHtmlTag() + " " + MqatDocument.UID + "=" + uid + ">", 0, 0, QuestionBodyElement.choiceInteraction.getHtmlTag());
                    } catch (Exception ex) {
                        ExceptionHandler.handle(ex, ExceptionHandler.SILENT);
                    }
                    mqatDocWrapper.setDirty(true);
                    break;
            }
        } else if (actionCmd.equals("add_variable")) {
            String identifier = (String) cmbVariable.getSelectedItem();
            if (identifier != null && identifier.trim().length() > 0) {
                addPrintedVariableToQuestionText(identifier);
            }
        } else if (actionCmd.equalsIgnoreCase("add_equation")) {
            newElement = itemBodyTextElement.getOwnerDocument().createElementNS(MathQTIDocument.MATHML_NAMESPACE_URI, QuestionBodyElement.math.name());
            newElement.setAttribute(MqatDocument.UID, uid);
            newElement.setIdAttribute(MqatDocument.UID, true);
            itemBodyTextElement.appendChild(newElement);
            try {
                htmlDoc.insertString(edpHTMLView.getCaretPosition(), " ", emptySpaceAttr);
                kit.insertHTML(htmlDoc, edpHTMLView.getCaretPosition(), "<p><" + QuestionBodyElement.math.getHtmlTag() + " " + MqatDocument.UID + "=" + uid + ">", 0, 0, QuestionBodyElement.math.getHtmlTag());
            } catch (Exception ex) {
                ExceptionHandler.handle(ex, ExceptionHandler.SILENT);
            }
            mqatDocWrapper.setDirty(true);
        } else if (actionCmd.equals("add_object")) {
            newElement = itemBodyTextElement.getOwnerDocument().createElement(QuestionBodyElement.object.name());
            newElement.setAttribute("type", "image/png");
            newElement.setAttribute("data", "");
            newElement.setAttribute(MqatDocument.UID, uid);
            newElement.setIdAttribute(MqatDocument.UID, true);
            itemBodyTextElement.appendChild(newElement);
            try {
                htmlDoc.insertString(edpHTMLView.getCaretPosition(), " ", emptySpaceAttr);
                kit.insertHTML(htmlDoc, edpHTMLView.getCaretPosition(), "<p><" + QuestionBodyElement.object.getHtmlTag() + "  " + MqatDocument.UID + "=" + uid + ">", 0, 0, QuestionBodyElement.object.getHtmlTag());
            } catch (Exception ex) {
                ExceptionHandler.handle(ex, ExceptionHandler.SILENT);
            }
            mqatDocWrapper.setDirty(true);
        } else if (actionCmd.equals("edit_qti_element")) {
            Element selectedQTIElement = DOMHelper.getElementById(itemBodyTextElement, MqatDocument.UID, (String) currentTextElement.getAttributes().getAttribute(MqatDocument.UID));
            if (editQTIElement(selectedQTIElement)) {
                mqatDocWrapper.setDirty(true);
                edpHTMLView.updateUI();
            }
        } else if (actionCmd.equalsIgnoreCase("delete_qti_element")) {
            String elementUID = (String) currentTextElement.getAttributes().getAttribute(MqatDocument.UID);
            Element selectedQTIElement = DOMHelper.getElementById(itemBodyTextElement, MqatDocument.UID, elementUID);
            selectedQTIElement.getParentNode().removeChild(selectedQTIElement);
            try {
                htmlDoc.remove(currentTextElement.getStartOffset(), (currentTextElement.getEndOffset() - currentTextElement.getStartOffset()));
            } catch (Exception ex) {
                ExceptionHandler.handle(ex, ExceptionHandler.SILENT);
            }
            mqatDocWrapper.setDirty(true);
            edpHTMLView.updateUI();
        }
        edpHTMLView.requestFocus();
    }

    /**
     * @param selectedQTIElement
     * @return true, if the element was edited.  
     */
    private boolean editQTIElement(Element selectedQTIElement) {
        if (selectedQTIElement == null) return false;
        QuestionBodyElement bodyElement;
        try {
            bodyElement = QuestionBodyElement.valueOf(selectedQTIElement.getNodeName());
        } catch (Exception ex) {
            return false;
        }
        switch(bodyElement) {
            case math:
                ExpressionEditorDialog dlgExpressionDialog = new ExpressionEditorDialog(getAssessmentItem(), selectedQTIElement);
                dlgExpressionDialog.setVisible(true);
                if (dlgExpressionDialog != null && dlgExpressionDialog.getOption() == JOptionPane.OK_OPTION) {
                    Element newExpressionElement = (Element) dlgExpressionDialog.getEpressionNodeAsMathML(MathAnnotation.ContentAndComputerese);
                    XmlHelper.setXmlText(selectedQTIElement, newExpressionElement);
                    dlgExpressionDialog.dispose();
                    return true;
                }
            case object:
                ObjectElementDialog dlgObjectElement = new ObjectElementDialog(selectedQTIElement);
                dlgObjectElement.setVisible(true);
                if (dlgObjectElement != null && dlgObjectElement.getOption() == JOptionPane.OK_OPTION) {
                    Element newElement = dlgObjectElement.getElement();
                    DOMHelper.replaceContent(selectedQTIElement, newElement);
                    DOMHelper.setAttributes(selectedQTIElement, newElement);
                    dlgObjectElement.dispose();
                    return true;
                }
        }
        return false;
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof JTabbedPane) {
            switch(tbpItemBody.getSelectedIndex()) {
                case TAB_DESIGN:
                    itemBodyTextElement = XMLFileHandler.getInstance().readElement(XmlHelper.encloseFrag(edpSource.getText()));
                    String questionText = getHtmlQuestionText(itemBodyTextElement);
                    edpHTMLView.setText(questionText);
                    break;
                case TAB_SOURCE:
                    itemBodyTextElement = getXhtmlQuestionTextElement(edpHTMLView.getText());
                    edpSource.setText(XMLFileHandler.getInstance().write(itemBodyTextElement, true, true, true));
                    break;
            }
        }
    }

    /**
     * Replaces the pseudo QTI elements in the new document, edited by HTMLPane, 
     * with real elements from the original document on which all DOM operations 
     * were performed during editing.
     * 
     * @param newElement
     * @param origElement
     */
    private void replacePsedoQTIElements(Element newElement, Element origElement) {
        Element childElement;
        Node replacingNode;
        String idAttr;
        NodeList children = newElement.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element) {
                childElement = (Element) children.item(i);
                idAttr = childElement.getAttribute(MqatDocument.UID);
                if (!idAttr.equalsIgnoreCase("")) {
                    replacingNode = DOMHelper.getElementById(origElement, MqatDocument.UID, idAttr);
                    if (replacingNode != null) {
                        Node importedNode = newElement.getOwnerDocument().importNode(replacingNode, true);
                        childElement.getParentNode().replaceChild(importedNode, childElement);
                    }
                }
                replacePsedoQTIElements(childElement, origElement);
            }
        }
    }

    private JPopupMenu createQtiElementPopupMenu() {
        JMenuItem menuItem;
        JPopupMenu popupMenu;
        popupMenu = new JPopupMenu(ResourceManager.getString("wizard_question_text_qti_element_popup_menu_title"));
        menuItem = new JMenuItem();
        ResourceManager.localizeItem("wizard_question_text_qti_element_popup_menu_edit", menuItem);
        menuItem.setActionCommand("edit_qti_element");
        menuItem.addActionListener(this);
        popupMenu.add(menuItem);
        menuItem = new JMenuItem();
        ResourceManager.localizeItem("wizard_question_text_qti_element_popup_menu_delete", menuItem);
        menuItem.setActionCommand("delete_qti_element");
        menuItem.addActionListener(this);
        popupMenu.add(menuItem);
        return popupMenu;
    }

    class QuestionTextHTMLEditorKit extends HTMLEditorKit {

        public ViewFactory getViewFactory() {
            return new QuestionTextHTMLFactory();
        }

        public Document createDefaultDocument() {
            StyleSheet sheet = getStyleSheet();
            StyleSheet styles = new StyleSheet();
            styles.addStyleSheet(sheet);
            HTMLDocument result = new ExtendedHTMLDocument(styles);
            result.setParser(getParser());
            result.setAsynchronousLoadPriority(4);
            result.setTokenThreshold(100);
            return result;
        }

        class QuestionTextHTMLFactory extends HTMLFactory {

            public View create(javax.swing.text.Element e) {
                Object o = e.getAttributes().getAttribute(StyleConstants.NameAttribute);
                if (o instanceof HTML.UnknownTag) {
                    HTML.UnknownTag tagx = (HTML.UnknownTag) o;
                    for (QuestionBodyElement tag : QuestionBodyElement.values()) {
                        if (tagx.equals(tag.getHtmlTag())) {
                            return new QTIItemView(e, tag.name());
                        }
                    }
                } else if (((HTML.Tag) o) == HTML.Tag.COMMENT) {
                    return new InvisibleView(e);
                }
                return super.create(e);
            }

            class QTIItemView extends FormView {

                String title;

                javax.swing.text.Element element;

                QTIItemView(javax.swing.text.Element element, String title) {
                    super(element);
                    this.title = title;
                    this.element = element;
                }

                protected Component createComponent() {
                    final JPanel c = new JPanel(new BorderLayout());
                    c.setToolTipText("Click the context menu button to popup a context menu");
                    c.addMouseListener(new MouseAdapter() {

                        public void mousePressed(MouseEvent e) {
                            showPopupMenu(e);
                        }

                        public void mouseReleased(MouseEvent e) {
                            showPopupMenu(e);
                        }

                        private void showPopupMenu(MouseEvent e) {
                            if (e.isPopupTrigger()) {
                                currentTextElement = element;
                                mnuQtiElementEditMenu.show(c, e.getX(), e.getY());
                            }
                        }
                    });
                    c.setBackground(new Color(255, 255, 255));
                    String uid = (String) element.getAttributes().getAttribute(MqatDocument.UID);
                    Element qtiElement = DOMHelper.getElementById(itemBodyTextElement, MqatDocument.UID, uid);
                    if (qtiElement == null) return c;
                    QuestionBodyElement tag = QuestionBodyElement.valueOf(qtiElement.getNodeName());
                    if (tag == null) return c;
                    switch(tag) {
                        case printedVariable:
                            c.add("Center", new JLabel("Variable:" + qtiElement.getAttribute("identifier")));
                            c.setMaximumSize(new Dimension(100, 50));
                            break;
                        case textEntryInteraction:
                            c.add("Center", new JLabel("Text entry"));
                            c.setMaximumSize(new Dimension(100, 50));
                            break;
                        case choiceInteraction:
                            c.add("Center", new JLabel("Choice"));
                            c.setMaximumSize(new Dimension(100, 50));
                            break;
                        case object:
                            String type = qtiElement.getAttribute("type");
                            String url = qtiElement.getAttribute("data");
                            Icon icon;
                            try {
                                icon = new ImageIcon(new URL(url));
                            } catch (MalformedURLException e1) {
                                icon = null;
                            }
                            if (icon == null) {
                                icon = ResourceManager.getFileAsImageIcon("imagePlaceholder.png", ResourceManager.RES_ICONS);
                            }
                            JLabel label = new JLabel(" " + type + " ", icon, JLabel.RIGHT);
                            label.setVerticalTextPosition(JLabel.BOTTOM);
                            label.setHorizontalTextPosition(JLabel.CENTER);
                            c.add("Center", label);
                            c.setMaximumSize(new Dimension(60, 50));
                            break;
                        case math:
                            ExistingVariables existingVariables = new ExistingVariables();
                            for (int i = 0; i < getAssessmentItem().getTemplateDeclarations().length; i++) {
                                ExistingVariable existingVariable = new ExistingVariable(getAssessmentItem().getTemplateDeclarations()[i].getIdentifier(), Utils.getBaseTypeAsString(getAssessmentItem().getTemplateDeclarations()[i].getBaseType()));
                                existingVariables.add(existingVariable);
                            }
                            uk.ac.imperial.ma.mathsEngine.parsers.Parser mathmlparser = ParserFactory.createParser(qtiElement, existingVariables);
                            String html;
                            if (!"".equals(mathmlparser.getComputerese())) {
                                uk.ac.imperial.ma.mathsEngine.parsers.Parser computereseParser = ParserFactory.createParser(mathmlparser.getComputerese(), existingVariables);
                                html = computereseParser.getExpression().getHTML();
                            } else {
                                html = "Empty expression";
                            }
                            c.add("Center", new JLabel(html));
                            c.setMaximumSize(new Dimension(100, 50));
                            break;
                    }
                    c.setAlignmentY(0.7f);
                    c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 5), BorderFactory.createLineBorder(Color.BLACK)));
                    return c;
                }
            }
        }
    }
}
