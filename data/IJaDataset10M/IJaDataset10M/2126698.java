package org.frapuccino.htmleditor;

import java.awt.Cursor;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTML;
import org.frapuccino.htmleditor.common.ExtendedHTMLDocument;
import org.frapuccino.htmleditor.common.ExtendedHTMLEditorKit;

/**
 *
 * @author karlpeder
 * @author fdietz
 */
public class HtmlEditorView extends JTextPane {

    /** JDK 1.4+ logging framework logger, used for logging. */
    private static final Logger LOG = Logger.getLogger("org.frapuccino.htmleditor");

    /** HTML Document */
    private ExtendedHTMLDocument htmlDoc;

    /** Editor kit */
    private ExtendedHTMLEditorKit htmlKit;

    /** Action used to toggle bold font on/off */
    private StyledEditorKit.BoldAction actionFontBold;

    /** Action used to toggle italic font on/off */
    private StyledEditorKit.ItalicAction actionFontItalic;

    /** Action used to toggle underline font on/off */
    private StyledEditorKit.UnderlineAction actionFontUnderline;

    /** List of formatting supported (list contains HTML.Tag objects) */
    private List supportedFormats;

    /**
	 * Default constructor. Does initial setup such as creating an editor kit
	 *
	 * @param doc
	 *            HTML document
	 */
    public HtmlEditorView() {
        super();
        htmlKit = new ExtendedHTMLEditorKit();
        htmlKit.setDefaultCursor(new Cursor(Cursor.TEXT_CURSOR));
        htmlDoc = (ExtendedHTMLDocument) (htmlKit.createDefaultDocument());
        setEditorKit(htmlKit);
        setDocument(htmlDoc);
        actionFontBold = new StyledEditorKit.BoldAction();
        actionFontItalic = new StyledEditorKit.ItalicAction();
        actionFontUnderline = new StyledEditorKit.UnderlineAction();
        supportedFormats = new ArrayList();
        supportedFormats.add(HTML.Tag.P);
        supportedFormats.add(HTML.Tag.H1);
        supportedFormats.add(HTML.Tag.H2);
        supportedFormats.add(HTML.Tag.H3);
        supportedFormats.add(HTML.Tag.PRE);
        supportedFormats.add(HTML.Tag.ADDRESS);
        setTextAlignment(StyleConstants.ALIGN_LEFT);
    }

    /** Returns the underlying HTML document */
    public ExtendedHTMLDocument getHtmlDoc() {
        return htmlDoc;
    }

    /** Sets the charset to use */
    public void setCharset(Charset charset) {
        if (charset == null) {
            charset = Charset.forName(System.getProperty("file.encoding"));
        }
        setContentType("text/html; charset=\"" + charset.name() + "\"");
    }

    /**
	 * Toggles bold font on/off.
	 */
    public void toggleBold() {
        actionFontBold.actionPerformed(null);
    }

    /**
	 * Toggles italic font on/off
	 */
    public void toggleItalic() {
        actionFontItalic.actionPerformed(null);
    }

    /**
	 * Toggles underline font on/off
	 */
    public void toggleUnderline() {
        actionFontUnderline.actionPerformed(null);
    }

    /**
	 * Toggles strikeout (aka strike through) on/off
	 */
    public void toggleStrikeout() {
        MutableAttributeSet inputAttr = htmlKit.getInputAttributes();
        boolean isStrike = false;
        if (StyleConstants.isStrikeThrough(inputAttr)) {
            isStrike = true;
        }
        SimpleAttributeSet sas = new SimpleAttributeSet();
        StyleConstants.setStrikeThrough(sas, !isStrike);
        setCharacterFormat(sas);
    }

    /**
	 * Toggles tele typer (aka typewritten) on/off
	 */
    public void toggleTeleTyper() {
        MutableAttributeSet inputAttr = htmlKit.getInputAttributes();
        boolean isTeleTyper = false;
        Enumeration enumeration = inputAttr.getAttributeNames();
        while (enumeration.hasMoreElements()) {
            Object name = enumeration.nextElement();
            if ((name instanceof HTML.Tag) && (name.toString().equals(HTML.Tag.TT.toString()))) {
                isTeleTyper = true;
                break;
            }
        }
        SimpleAttributeSet sas = new SimpleAttributeSet();
        sas.addAttribute(HTML.Tag.TT, new SimpleAttributeSet());
        if (isTeleTyper) {
            removeCharacterFormat(sas);
        } else {
            setCharacterFormat(sas);
        }
    }

    /**
	 * Private utility for removing character format from current selection and
	 * from the input attributes, which defines the format of text typed at the
	 * current caret position
	 *
	 * @param attr
	 *            Attributeset containing the format to remove
	 */
    private void removeCharacterFormat(AttributeSet attr) {
        MutableAttributeSet inputAttr = htmlKit.getInputAttributes();
        inputAttr.removeAttributes(attr);
        int selStart = getSelectionStart();
        int selEnd = getSelectionEnd();
        for (int i = selStart; i < selEnd; i++) {
            SimpleAttributeSet currentAttr = new SimpleAttributeSet(htmlDoc.getCharacterElement(i).getAttributes());
            currentAttr.removeAttributes(attr);
            htmlDoc.setCharacterAttributes(i, 1, currentAttr, true);
        }
    }

    /**
	 * Private utility for setting character format on current selection and on
	 * the input attributes, which defines the format of text typed at the
	 * current caret position.
	 *
	 * @param attr
	 *            Attributeset containing the format to set
	 */
    private void setCharacterFormat(AttributeSet attr) {
        int selStart = getSelectionStart();
        int selEnd = getSelectionEnd();
        int selLength = selEnd - selStart;
        if (selLength != 0) {
            htmlDoc.setCharacterAttributes(selStart, selLength, attr, false);
        }
        MutableAttributeSet inputAttr = htmlKit.getInputAttributes();
        inputAttr.addAttributes(attr);
    }

    /**
	 * Private utility for setting paragraph format on current selection from an
	 * attribute set
	 *
	 * @param attr
	 *            Attributeset containing the format to set
	 */
    private void setParagraphFormat(AttributeSet attr) {
        int selStart = getSelectionStart();
        int selEnd = getSelectionEnd();
        int selLength = selEnd - selStart;
        htmlDoc.setParagraphAttributes(selStart, selLength, attr, false);
    }

    /**
	 * Sets alignment of the current paragraph to left, center or right.
	 *
	 * @param align
	 *            The alignment; one of: StyleConstants.ALIGN_LEFT,
	 *            StyleConstants.ALIGN_CENTER, StyleConstants.ALIGN_RIGHT
	 */
    public void setTextAlignment(int align) {
        boolean supported;
        switch(align) {
            case StyleConstants.ALIGN_LEFT:
                supported = true;
                break;
            case StyleConstants.ALIGN_CENTER:
                supported = true;
                break;
            case StyleConstants.ALIGN_RIGHT:
                supported = true;
                break;
            default:
                supported = false;
                break;
        }
        if (!supported) {
            LOG.severe("Alignment not set - alignment=" + align + " not supported");
            return;
        }
        MutableAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setAlignment(attr, align);
        setParagraphFormat(attr);
    }

    /**
	 * Sets format of selected paragraphs or current paragraph when no text is
	 * selected. <br>
	 * Formats (tags) supported are defined by the list supportedFormats.
	 *
	 * @param formatTag
	 *            Defines which format to set (H1, P etc.)
	 */
    public void setParagraphFormat(HTML.Tag formatTag) {
        if (formatTag == null) {
            LOG.severe("Format not set - formatTag = null");
            return;
        }
        if (!supportedFormats.contains(formatTag)) {
            LOG.severe("Format not set - <" + formatTag + "> not supported");
            return;
        }
        MutableAttributeSet attr = new SimpleAttributeSet();
        attr.addAttribute(StyleConstants.NameAttribute, formatTag);
        setParagraphFormat(attr);
    }

    /**
	 * Method for inserting a line break (br tag)
	 */
    public void insertBreak() {
        try {
            int caretPos = this.getCaretPosition();
            htmlKit.insertHTML(htmlDoc, caretPos, "<br>", 0, 0, HTML.Tag.BR);
            this.setCaretPosition(caretPos + 1);
        } catch (BadLocationException e) {
            LOG.severe("Error inserting br tag: " + e.getMessage());
        } catch (IOException e) {
            LOG.severe("Error inserting br tag: " + e.getMessage());
        }
    }
}
