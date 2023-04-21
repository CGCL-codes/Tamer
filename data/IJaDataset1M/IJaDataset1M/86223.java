package com.thyante.thelibrarian.components;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import com.thyante.thelibrarian.util.CollectionUtil;

/**
 * A text box that, for editing, opens a popup that lets the user enter
 * a list of lines.
 * 
 * @author Matthias-M. Christen
 */
public class MultilinePopupEditor implements IMultilinePopupEditorListener, FocusListener, KeyListener {

    /**
	 * The text control
	 */
    protected Text m_text;

    /**
	 * The list of lines
	 */
    protected List<String> m_listLines;

    /**
	 * Flag indicating whether the popup editor is open currently 
	 */
    protected boolean m_bIsEditorOpen;

    /**
	 * The popup window containing the editor
	 */
    private MultilinePopupEditorDialog m_popup;

    /**
	 * A runnable showing the editor popup
	 */
    private Runnable m_runShowDialog;

    /**
	 * Creates the control without initializing the UI representation.
	 */
    public MultilinePopupEditor() {
        m_text = null;
        m_listLines = new LinkedList<String>();
        m_popup = null;
        m_runShowDialog = null;
    }

    /**
	 * Creates the control.
	 * @param cmpParent The parent composite
	 * @param nStyle The style to apply to the {@link Text} control.
	 * @see Text
	 */
    public MultilinePopupEditor(Composite cmpParent, int nStyle) {
        this();
        createUI(cmpParent, nStyle);
    }

    /**
	 * Creates the component's UI.
	 * @param cmpParent The parent composite
	 * @param nStyle The style
	 * @return The control that represents the UI
	 */
    public Control createUI(Composite cmpParent, int nStyle) {
        configureText(new Text(cmpParent, nStyle));
        return m_text;
    }

    /**
	 * Returns the text control displaying the entered values.
	 * @return The text control
	 */
    public Control getControl() {
        return m_text;
    }

    /**
	 * Configures the given text box <code>text</code> to act as
	 * underlying control for the {@link MultilinePopupEditor}
	 * @param text The text box to configure as editor
	 */
    public void configureText(Text text) {
        m_text = text;
        m_text.setEditable(false);
        m_text.addFocusListener(this);
        m_text.addKeyListener(this);
    }

    /**
	 * Sets the lines.
	 * @param listLines
	 */
    public void setLines(List<String> listLines) {
        if (listLines == null) {
            m_listLines.clear();
            m_text.setText("");
        } else {
            m_listLines = listLines;
            StringBuffer sb = new StringBuffer();
            for (String strLine : listLines) {
                sb.append(strLine);
                sb.append(", ");
            }
            m_text.setText(sb.length() == 0 ? "" : sb.substring(0, sb.length() - 2));
        }
    }

    /**
	 * Returns the lines.
	 * @return the text lines contained in the component
	 */
    public List<String> getLines() {
        return CollectionUtil.copyOfList(m_listLines);
    }

    public void setData(Object objData) {
        m_text.setData(objData);
    }

    public Object getData() {
        return m_text.getData();
    }

    /**
	 * Shows the drop down.
	 */
    public void showDropDown() {
        if (m_popup == null) {
            m_popup = new MultilinePopupEditorDialog(m_text);
            m_popup.addPopupListener(this);
        }
        m_popup.setLines(m_listLines);
        m_popup.open();
    }

    public void onPopupOpen() {
        m_bIsEditorOpen = true;
    }

    public void onPopupClose(List<String> listLines) {
        setLines(listLines);
    }

    public void onListElementsChanged(List<ItemChange<String>> listChanges) {
    }

    public void focusGained(FocusEvent e) {
        if (!m_bIsEditorOpen) {
            if (m_runShowDialog == null) {
                m_runShowDialog = new Runnable() {

                    public void run() {
                        showDropDown();
                    }
                };
            }
            e.display.timerExec(300, m_runShowDialog);
        } else m_bIsEditorOpen = false;
    }

    public void focusLost(FocusEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.keyCode != SWT.ARROW_LEFT && e.keyCode != SWT.ARROW_RIGHT && e.keyCode != SWT.HOME && e.keyCode != SWT.END && e.keyCode != SWT.ESC && e.keyCode != SWT.SHIFT && e.keyCode != SWT.CONTROL && e.keyCode != SWT.ALT) {
            showDropDown();
        }
    }

    public void keyReleased(KeyEvent e) {
    }
}
