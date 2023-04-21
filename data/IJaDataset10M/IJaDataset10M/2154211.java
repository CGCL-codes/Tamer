package com.emental.mindraider.core.concept.annotation;

import java.awt.Color;
import javax.swing.JEditorPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import com.emental.mindraider.MindRaiderConstants;
import com.emental.mindraider.core.concept.ConceptInterlinking;
import com.emental.mindraider.core.rest.resource.ConceptResource;
import com.emental.mindraider.ui.panels.bars.ConceptBar;
import com.emental.mindraider.utils.TWikifier;
import com.mindcognition.mindraider.commons.representation.twiki.TwikiToHtml;

/**
 * Textual concept annotation.
 *
 * @author Martin.Dvorak
 */
public class ObsoleteTextAnnotation implements ConceptAnnotationType {

    /**
     * text html head
     */
    public static String HTML_HEAD = " <head>" + "   <style type='text/css'>" + "     ul, ol {" + "         margin-top: 2px;" + "         margin-bottom: 2px;" + "         margin-left: 25px;" + "     }" + "     body {" + "         font-family: monospace; " + "         font-size: small;" + "     }" + "   </style>" + " </head>";

    /**
     * the label text constant.
     */
    public static final String LABEL_TEXT = MindRaiderConstants.MR_OWL_CONTENT_TYPE_TXT_LOCAL_NAME;

    /**
     * the preview edit pane.
     */
    private JSplitPane previewEditPane;

    /**
     * the editor text area.
     */
    private JTextArea editor;

    /**
     * the preview editor pane.
     */
    private JEditorPane preview;

    /**
     * Constructor
     *
     * @param previewEditPane
     *            the preview edit pane
     * @param editor
     *            the editor text area
     * @param preview
     *            the preview editor pane
     */
    public ObsoleteTextAnnotation(JSplitPane previewEditPane, JTextArea editor, JEditorPane preview) {
        this.previewEditPane = previewEditPane;
        this.editor = editor;
        this.preview = preview;
    }

    public String getOwlClass() {
        return MindRaiderConstants.MR_OWL_CONTENT_TYPE_TXT;
    }

    public String getLabel() {
        return LABEL_TEXT;
    }

    public Color getForegroundColor() {
        return Color.WHITE;
    }

    public Color getBackgroundColor() {
        return Color.BLACK;
    }

    public void setUpWysiwygTabbedPane(ConceptResource conceptResource, JTabbedPane wysiwygsTabbedPane) {
        wysiwygsTabbedPane.removeAll();
        wysiwygsTabbedPane.addTab("Text", previewEditPane);
        wysiwygsTabbedPane.setSelectedIndex(0);
    }

    public boolean isWysiwyg() {
        return false;
    }

    public void refreshPreview(String label) {
        String twikifiedAnnotation = TWikifier.getInstance().transform(editor.getText());
        String interlinkedAnnotation = ConceptInterlinking.translate(twikifiedAnnotation);
        String htmlContent = "<html>" + HTML_HEAD + "<body><font face='Courier'>\n" + TwikiToHtml.translate("---+++ " + label + "\n" + interlinkedAnnotation) + "\n</font></body>" + "</html>";
        preview.setText(htmlContent);
        preview.updateUI();
    }

    public void setUpConceptBar(ConceptBar conceptBar) {
    }
}
