package com.elibera.ccs.editorsmall;

import java.applet.Applet;
import java.io.InputStream;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext.NamedStyle;
import com.elibera.ccs.app.ContentPackageEditor;
import com.elibera.ccs.app.MLEConfig;
import com.elibera.ccs.img.BinaryContainer;
import com.elibera.ccs.mlos.HelperMLOsSimple;
import com.elibera.ccs.mlos.MLOLister;
import com.elibera.ccs.panel.InterfaceEditorButtonPanel;
import com.elibera.ccs.panel.ActionButtonPanelSmall;
import com.elibera.ccs.parser.HelperParser;
import com.elibera.ccs.parser.HelperXMLParserSimple;
import com.elibera.ccs.parser.InterfaceDocContainer;
import com.elibera.ccs.parser.InterfaceDocParser;
import com.elibera.util.scorm.ContentPackage;

/**
 * @author meisi
 *
 */
public class ContainerSmall implements InterfaceDocContainer {

    private NamedStyle fontStyle;

    private DefaultStyledDocument doc;

    private javax.swing.JTextPane textPane;

    public ActionButtonPanelSmall editorButtonPanel;

    private MLEConfig conf;

    public String xml = "";

    private DocParserSmall docParser;

    public EditorPanelSmall ep;

    public ContainerSmall(MLEConfig conf) {
        this.conf = conf;
        textPane = new javax.swing.JTextPane();
        docParser = new DocParserSmall(this);
        docParser.mloLister = new MLOListerSmall(this);
        doc = (DefaultStyledDocument) textPane.getDocument();
        editorButtonPanel = new ActionButtonPanelSmall(this, false, false, false);
        ep = new EditorPanelSmall(this, editorButtonPanel);
        reset();
    }

    public BinaryContainer getBinaryContainer() {
        return conf.binaries;
    }

    public NamedStyle getCurFontStyle() {
        return fontStyle;
    }

    public InterfaceDocParser getDocParser() {
        return docParser;
    }

    public DefaultStyledDocument getDocument() {
        return doc;
    }

    public InterfaceEditorButtonPanel getEditorButtonPanel() {
        return editorButtonPanel;
    }

    public long getNewObjectID() {
        return MLEConfig.conf.getNewObjectID();
    }

    public JTextPane getTextPane() {
        return this.textPane;
    }

    public InputStream openSystemRessource(String url) {
        if (conf != null) return conf.openSystemRessource(url);
        return null;
    }

    public void store() {
        this.xml = HelperParser.parseXMLOutOfDocument(doc, docParser);
        System.out.println("-----------------------------");
        System.out.println(xml);
        System.out.println("-----------------------------");
        reloadDocument();
        textPane.repaint();
    }

    public void reloadDocument() {
        try {
            doc.remove(0, doc.getLength());
        } catch (Exception e) {
        }
        reset();
        HelperXMLParserSimple.checkForXMLTags(xml, doc, docParser);
        textPane.updateUI();
    }

    public void setNewXML(String xml) {
        this.xml = xml;
    }

    public void reset() {
        try {
            doc.remove(0, doc.getLength());
        } catch (Exception e) {
        }
        fontStyle = com.elibera.ccs.mlos.MLOFont.getStandardFontStyle();
        docParser.reset();
        HelperMLOsSimple.resetForNewXMLBuild(docParser.mloLister);
        HelperMLOsSimple.registerStandardStyles(docParser.mloLister);
        try {
            doc.insertString(0, " ", fontStyle);
            doc.setParagraphAttributes(0, doc.getLength(), fontStyle, true);
            doc.remove(0, 1);
        } catch (Exception ee) {
        }
    }

    public JPanel getEditorPanel() {
        return ep;
    }

    public ContentPackageEditor getCurrentLernobjekt() {
        return conf.lernobjekt;
    }

    public void setLernobjekt(ContentPackageEditor lo) {
    }

    /**
	 * gibt das MLE Config Objekt zurück
	 * @return
	 */
    public MLEConfig getMLEConfig() {
        return conf;
    }
}
