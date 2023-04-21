package au.edu.qut.yawl.editor.swing.data;

import java.util.List;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JEditorPane;
import javax.swing.border.EtchedBorder;

public class XMLEditorPane extends JEditorPane implements AbstractXMLStyledDocumentValidityListener {

    private static final Color VALID_COLOR = Color.GREEN.darker().darker();

    private static final Color INVALID_COLOR = Color.RED.darker();

    private static final Font COURIER = new Font("Monospaced", Font.PLAIN, 12);

    private static final Color DISABLED_BACKGROUND = Color.LIGHT_GRAY;

    private Color enabledBackground;

    public XMLEditorPane() {
        setBorder(new EtchedBorder());
        setFont(COURIER);
        enabledBackground = this.getBackground();
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.setBackground(enabledBackground);
        } else {
            this.setBackground(DISABLED_BACKGROUND);
        }
        super.setEnabled(enabled);
    }

    public void setDocument(AbstractXMLStyledDocument document) {
        super.setDocument(document);
        subscribeForValidityEvents();
    }

    public boolean isContentValid() {
        return ((AbstractXMLStyledDocument) getDocument()).isContentValid();
    }

    public void validate() {
        ((AbstractXMLStyledDocument) getDocument()).publishValidity();
    }

    protected void subscribeForValidityEvents() {
        acceptValiditySubscription(this);
    }

    public void acceptValiditySubscription(AbstractXMLStyledDocumentValidityListener subscriber) {
        ((AbstractXMLStyledDocument) getDocument()).subscribe(subscriber);
    }

    public void setText(String text) {
        super.setText(text);
        validate();
    }

    public List getProblemList() {
        return ((AbstractXMLStyledDocument) this.getDocument()).getProblemList();
    }

    public void setTargetVariableName(String targetVariableName) {
        ((AbstractXMLStyledDocument) getDocument()).setPreAndPostEditorText("<" + targetVariableName + ">", "</" + targetVariableName + ">");
    }

    public void documentValidityChanged(boolean documentValid) {
        setForeground(documentValid ? VALID_COLOR : INVALID_COLOR);
    }
}
