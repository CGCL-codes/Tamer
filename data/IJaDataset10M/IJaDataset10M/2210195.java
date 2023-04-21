package org.columba.mail.gui.composer.html;

import java.awt.Font;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.ChangedCharSetException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTML;
import org.columba.mail.gui.composer.AbstractEditorController;
import org.columba.mail.gui.composer.ComposerController;
import org.columba.mail.gui.composer.html.util.FormatInfo;

/**
 * Controller part of controller-view frame work for composing html messages
 * 
 * @author Karl Peder Olesen
 * 
 */
public class HtmlEditorController extends AbstractEditorController implements DocumentListener, CaretListener {

    /** JDK 1.4+ logging framework logger, used for logging. */
    private static final Logger LOG = Logger.getLogger("org.columba.mail.gui.composer.html");

    /** Main view (WYSIWYG) */
    protected HtmlEditorView view;

    /**
	 * Default constructor.
	 */
    public HtmlEditorController(ComposerController controller) {
        super(controller);
        view = new HtmlEditorView(null);
        view.addCaretListener(this);
    }

    /**
	 * Installs this controller as DocumentListener on the view
	 */
    public void installListener() {
        view.installListener(this);
    }

    public void updateComponents(boolean b) {
        if (b) {
            if (this.getController().getModel().getBodyText() != null) {
                this.setViewText(this.getController().getModel().getBodyText());
            }
        } else {
            if (view.getText() != null) {
                this.getController().getModel().setBodyText(view.getText());
            }
        }
    }

    /**
	 * Toggle bold font in the view on/off
	 */
    public void toggleBold() {
        view.toggleBold();
    }

    /**
	 * Toggle italic font in the view on/off
	 */
    public void toggleItalic() {
        view.toggleItalic();
    }

    /**
	 * Toggle underline font in the view on/off
	 */
    public void toggleUnderline() {
        view.toggleUnderline();
    }

    /**
	 * Toggle strikeout font in the view on/off
	 */
    public void toggleStrikeout() {
        view.toggleStrikeout();
    }

    /**
	 * Toggle teletyper font (type written text) in the view on/off
	 */
    public void toggleTeleTyper() {
        view.toggleTeleTyper();
    }

    /**
	 * Sets alignment in the view to left, center or right
	 * 
	 * @param align
	 *            One of StyleConstants.ALIGN_LEFT, StyleConstants.ALIGN_CENTER
	 *            or StyleConstants.ALIGN_RIGHT
	 */
    public void setAlignment(int align) {
        view.setTextAlignment(align);
        boolean textSelected = false;
        String text = view.getSelectedText();
        if (text == null) {
            textSelected = false;
        } else if (text.length() > 0) {
            textSelected = true;
        }
        int pos = view.getCaretPosition();
        setChanged();
        notifyObservers(new FormatInfo(view.getHtmlDoc(), pos, textSelected));
    }

    /**
	 * Sets paragraph format for selected paragraphs or current paragraph if no
	 * text is selected
	 * 
	 * @param tag
	 *            Html tag specifying the format to set
	 */
    public void setParagraphFormat(HTML.Tag tag) {
        view.setParagraphFormat(tag);
    }

    /**
	 * Method for inserting a break (BR) element
	 */
    public void insertBreak() {
        view.insertBreak();
    }

    public boolean isCutActionEnabled() {
        if (view.getSelectedText() == null) {
            return false;
        }
        if (view.getSelectedText().length() > 0) {
            return true;
        }
        return false;
    }

    public boolean isCopyActionEnabled() {
        if (view.getSelectedText() == null) {
            return false;
        }
        if (view.getSelectedText().length() > 0) {
            return true;
        }
        return false;
    }

    public boolean isPasteActionEnabled() {
        return true;
    }

    public boolean isDeleteActionEnabled() {
        if (view.getSelectedText() == null) {
            return false;
        }
        if (view.getSelectedText().length() > 0) {
            return true;
        }
        return false;
    }

    public boolean isSelectAllActionEnabled() {
        return true;
    }

    public boolean isRedoActionEnabled() {
        return false;
    }

    public boolean isUndoActionEnabled() {
        return false;
    }

    public void cut() {
        view.cut();
    }

    public void copy() {
        view.copy();
    }

    public void paste() {
        view.paste();
    }

    public void delete() {
        view.replaceSelection("");
    }

    public void redo() {
    }

    public void undo() {
    }

    public void selectAll() {
        view.selectAll();
    }

    public JComponent getComponent() {
        return view;
    }

    public Font getViewFont() {
        return view.getFont();
    }

    public void setViewFont(Font f) {
        view.setFont(f);
    }

    public String getViewText() {
        return view.getText();
    }

    @Override
    public void setCaretPosition(int position) {
        view.setCaretPosition(position);
    }

    @Override
    public void moveCaretPosition(int position) {
        view.moveCaretPosition(position);
    }

    public void setViewText(String text) {
        try {
            loadHtmlIntoView(text, false);
        } catch (ChangedCharSetException ccse) {
            try {
                loadHtmlIntoView(text, true);
            } catch (IOException e) {
                LOG.severe("Error setting view content, " + "even after ignore charset spec: " + e.getMessage());
            }
        } catch (IOException e) {
            LOG.severe("Error setting view content: " + e.getMessage());
        }
    }

    /**
	 * Private utility for loading html into the view. Is called from
	 * setViewText. <br>
	 * The method works mostly as calling view.setText() directly, but is
	 * necessary to be able to handle ChangedCharsetExceptions
	 * 
	 * @param text
	 *            Text to load into the view
	 * @param ignoreCharset
	 *            If set to true, charset specifications in the html will be
	 *            ignore
	 * @throws IOException
	 */
    private void loadHtmlIntoView(String text, boolean ignoreCharset) throws IOException {
        Document doc = view.getDocument();
        try {
            doc.remove(0, doc.getLength());
            if ((text == null) || (text.equals(""))) {
                return;
            }
            if (ignoreCharset) {
                view.getHtmlDoc().putProperty("IgnoreCharsetDirective", Boolean.TRUE);
            }
            Reader r = new StringReader(text);
            EditorKit kit = view.getEditorKit();
            kit.read(r, doc, 0);
        } catch (BadLocationException e) {
            LOG.severe("Error deleting old view content: " + e.getMessage());
            return;
        }
    }

    public JTextPane getViewUIComponent() {
        return view;
    }

    public void setViewEnabled(boolean enabled) {
        view.setEnabled(enabled);
    }

    public void changedUpdate(DocumentEvent e) {
    }

    public void insertUpdate(DocumentEvent e) {
    }

    public void removeUpdate(DocumentEvent e) {
    }

    /**
	 * Used to update actions (cut, copy, etc.) via the focusManager. This is
	 * done since charet updates may have coursed text selections etc. to
	 * change, which in turn should enable/disable cut, copy, etc. actions.
	 * <p>
	 * This method also notifies all observers which are specific to the HTML
	 * component only. This includes almost all actions in package
	 * org.columba.mail.gui.composer.html.action
	 * <p>
	 * The information to the observers contains information about the format at
	 * the current caret position and about text selections. The information is
	 * encapsulated in a FormatInfo object.
	 * 
	 * @see javax.swing.event.CaretListener#caretUpdate(javax.swing.event.CaretEvent)
	 */
    public void caretUpdate(CaretEvent e) {
        boolean textSelected = false;
        String text = view.getSelectedText();
        if (text == null) {
            textSelected = false;
        } else if (text.length() > 0) {
            textSelected = true;
        }
        int pos = e.getDot();
        setChanged();
        notifyObservers(new FormatInfo(view.getHtmlDoc(), pos, textSelected));
    }
}
