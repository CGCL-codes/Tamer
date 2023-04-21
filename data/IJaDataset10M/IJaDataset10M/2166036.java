package org.fudaa.fudaa.commun;

import java.awt.Color;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Hashtable;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JTextPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.rtf.RTFEditorKit;
import com.memoire.bu.BuInformationsDocument;
import com.memoire.bu.BuResource;
import org.fudaa.ctulu.CtuluLibString;

/**
 * Editeur de rapport.
 * 
 * @version $Revision: 1.15 $ $Date: 2006-09-19 15:01:56 $ by $Author: deniger $
 * @author Guillaume Desnoix
 */
public class FudaaEditeurRapport extends JTextPane {

    public static final int ASCII = 0;

    public static final int HTML = 1;

    public static final int RTF = 2;

    private final int type_ = RTF;

    static Hashtable commandes = new Hashtable();

    public Action getCommand(final String _s) {
        return (Action) commandes.get(_s);
    }

    public FudaaEditeurRapport() {
        super();
        EditorKit ek = null;
        switch(type_) {
            case ASCII:
                ek = new StyledEditorKit();
                break;
            case HTML:
                ek = new HTMLEditorKit();
                break;
            case RTF:
                ek = new RTFEditorKit();
                break;
            default:
        }
        if (ek == null) {
            return;
        }
        final Document dk = ek.createDefaultDocument();
        setEditorKit(ek);
        setDocument(dk);
        final Action[] actions = this.getActions();
        for (int i = 0; i < actions.length; i++) {
            final Action a = actions[i];
            final Object o = a.getValue(Action.NAME);
            commandes.put(o, a);
        }
    }

    public void setDefaultBorder() {
        switch(type_) {
            case HTML:
                setBorder(new EmptyBorder(12, 0, 12, 0));
                break;
            case ASCII:
            case RTF:
                setBorder(new CompoundBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new LineBorder(Color.lightGray, 1)), new EmptyBorder(2, 2, 2, 2)));
                break;
            default:
        }
    }

    public void setDefaultStyle() {
        final Style defaultStyle = getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setFontFamily(defaultStyle, "SansSerif");
        StyleConstants.setFontSize(defaultStyle, 12);
    }

    public void insereTexte(final String _text) {
        final Style defaultStyle = getStyle(StyleContext.DEFAULT_STYLE);
        insereTexte(_text, defaultStyle);
    }

    public void insereTexte(final String _text, final Style _style) {
        final Document dk = getDocument();
        try {
            dk.insertString(dk.getLength(), _text, _style);
        } catch (final BadLocationException ex) {
        }
    }

    public void insereIcone(final Icon _icon) {
        insereIcone("S" + System.currentTimeMillis(), _icon, "");
    }

    public void insereIcone(final Icon _icon, final String _text) {
        insereIcone("S" + System.currentTimeMillis(), _icon, _text);
    }

    public void insereIcone(final String _name, final Icon _icon, final String _text) {
        if (type_ == HTML) {
            return;
        }
        final Document dk = getDocument();
        final Style defaultStyle = getStyle(StyleContext.DEFAULT_STYLE);
        final Style iconStyle = addStyle(_name + "-icon", defaultStyle);
        StyleConstants.setIcon(iconStyle, _icon);
        StyleConstants.setAlignment(iconStyle, StyleConstants.ALIGN_CENTER);
        try {
            dk.insertString(dk.getLength(), CtuluLibString.LINE_SEP, iconStyle);
        } catch (final BadLocationException ex) {
        }
        if (!"".equals(_text)) {
            final Style textStyle = addStyle(_name + "-text", defaultStyle);
            StyleConstants.setAlignment(textStyle, StyleConstants.ALIGN_CENTER);
            StyleConstants.setItalic(iconStyle, true);
            StyleConstants.setFontSize(iconStyle, 10);
            try {
                dk.insertString(dk.getLength(), _text + CtuluLibString.LINE_SEP, textStyle);
            } catch (final BadLocationException ex) {
            }
        }
    }

    public void insereEnteteEtude(final BuInformationsDocument _id) {
        final Document dk = getDocument();
        final Style defaultStyle = getStyle(StyleContext.DEFAULT_STYLE);
        final Style boldStyle = addStyle("bold", defaultStyle);
        StyleConstants.setBold(boldStyle, true);
        final Style titleStyle = addStyle("title", boldStyle);
        StyleConstants.setFontSize(titleStyle, 16);
        StyleConstants.setSpaceBelow(titleStyle, 10);
        StyleConstants.setAlignment(titleStyle, StyleConstants.ALIGN_CENTER);
        final Style smallStyle = addStyle("small", defaultStyle);
        StyleConstants.setFontSize(smallStyle, 10);
        insereIcone(_id.logo);
        try {
            dk.insertString(dk.getLength(), _id.name, titleStyle);
            dk.insertString(dk.getLength(), CtuluLibString.LINE_SEP, defaultStyle);
            dk.insertString(dk.getLength(), BuResource.BU.getString("Version") + getDot(), boldStyle);
            dk.insertString(dk.getLength(), _id.version + CtuluLibString.LINE_SEP, defaultStyle);
            dk.insertString(dk.getLength(), BuResource.BU.getString("Date") + getDot(), boldStyle);
            dk.insertString(dk.getLength(), _id.date + CtuluLibString.LINE_SEP, defaultStyle);
            dk.insertString(dk.getLength(), BuResource.BU.getString("Auteur") + getDot(), boldStyle);
            dk.insertString(dk.getLength(), _id.author + " ", defaultStyle);
            dk.insertString(dk.getLength(), "(" + _id.contact + ")\n", smallStyle);
            dk.insertString(dk.getLength(), BuResource.BU.getString("Organisme") + getDot(), boldStyle);
            dk.insertString(dk.getLength(), _id.organization + CtuluLibString.LINE_SEP, defaultStyle);
        } catch (final BadLocationException ex) {
        }
    }

    private String getDot() {
        return ": ";
    }

    public String getSource() {
        if (type_ == RTF) {
            return "not available";
        }
        final Document dk = getDocument();
        final EditorKit ek = getEditorKit();
        final Writer out = new StringWriter();
        try {
            ek.write(out, dk, 0, dk.getLength());
        } catch (final BadLocationException ex) {
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        return out.toString();
    }
}
