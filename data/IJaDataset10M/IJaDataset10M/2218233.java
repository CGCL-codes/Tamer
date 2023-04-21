package com.qasystems.qstudio.java.gui.editor;

import com.qasystems.qstudio.java.gui.ProjectMember;
import com.qasystems.tools.sys.OS;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentListener;

/**
 * This class implements the editor pane. It uses
 * <tt>wingsoft.component.TextEditorComponent</tt> as editor.
 */
public class EditorControl extends JPanel implements EditorController {

    private JLabel TITLEBAR = new JLabel();

    private JEditTextArea EDITOR = new JEditTextArea();

    private ProjectMember CURRENT_MEMBER = null;

    /**
   * Default constructor
   *
   * @param ed the editor event listener
   */
    public EditorControl(DocumentListener documentListener, CaretListener caretListener) {
        super();
        EDITOR = new JEditTextArea();
        EDITOR.getDocument().addDocumentListener(documentListener);
        EDITOR.addCaretListener(caretListener);
        try {
            jbInit();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    /**
   * This method sets the cursor on the desired position, taking TAB
   * characters into account. The given column is measured in number
   * of characters. The method gotoLocation only operates on positions,
   * so it may be the case that column is smaller than the required
   * indentation. This is checked by comparing to the offset
   * of the cursor location, which is also a number of characters.
   *
   * @param row the row index
   * @param column the column index
   */
    public void setCaretPosition(int line, int column) {
    }

    /**
   * Load the given file into the editor. Sets the file name in the title bar.
   *
   * @param member the project member to load
   */
    public void loadMember(ProjectMember member) {
        syncCurrentlyLoadedMember();
        if ((member != null) && (member != CURRENT_MEMBER)) {
            if (member.getChangedText() == null) {
                EDITOR.loadFile(member.getFileName());
                member.setModified(false);
            } else {
                EDITOR.setText(member.getChangedText());
            }
            setTitle(member.getFile().getAbsolutePath());
            if (member.getFile().canWrite()) {
                EDITOR.setEditable(false);
            } else {
                EDITOR.setEditable(true);
            }
            EDITOR.scrollTo(0, 0);
        }
        CURRENT_MEMBER = member;
    }

    /**
   * Store the editor buffer to file.
   *
   * @param member the project member to store
   * @throws java.io.IOException
   */
    public void storeMemberToFile(ProjectMember member) throws java.io.IOException {
        if (member != null) {
            final ProjectMember prev = CURRENT_MEMBER;
            loadMember(member);
            EDITOR.save(member.getFileName());
            loadMember(prev);
            member.setModified(false);
            member.setChangedText(null);
        }
    }

    /**
   * Set the highlighted line.
   *
   * @param line the line to highlight
   */
    public void setColorLine(int line) {
        EDITOR.select(line);
    }

    /**
   * Unset the highlighted line.
   *
   * @param line the line to remove the highlight attribute from
   */
    public void unsetColorLine() {
    }

    /**
   * Request focus for the editor.
   */
    public void requestFocusText() {
    }

    /**
   * Sets the text in the editor.
   *
   * @param text the text to show in the editor
   */
    public void setText(String text) {
        EDITOR.setText(text);
    }

    /**
   * Marhs the text as read-only.
   *
   * @param b if <tt>true</tt> sets the editor to read-only.
   */
    public void setReadOnly(boolean b) {
        EDITOR.setEditable(b);
    }

    /**
   * Sets the title of the editor.
   *
   * @param title the title text
   */
    public void setTitle(String title) {
        if (title == null) {
            title = "";
        } else if (title.equals("")) {
            title = " ";
        } else {
        }
        TITLEBAR.setText(title);
    }

    /**
   * Sets the icon in the title bar.
   *
   * @param icon the title bar icon
   */
    public void setIcon(Icon icon) {
        TITLEBAR.setIcon(icon);
    }

    /**
   * Redraws the editor pane.
   */
    public void redrawText() {
        EDITOR.updateUI();
    }

    /**
   * Gets the text editor object.
   *
   * @return the text editor object
   */
    public JEditTextArea getEditor() {
        return (EDITOR);
    }

    /**
   * Initialization of panels, buttons, dialog etc. Called by the constructor.
   *
   */
    private void jbInit() {
        final Font plainFont = new Font(null, Font.ROMAN_BASELINE, 12);
        setLayout(new BorderLayout());
        if (OS.isUnix()) {
            EDITOR.setFont(plainFont);
        }
        EDITOR.setText("");
        setTitle("");
        TITLEBAR.setForeground(SystemColor.activeCaptionText);
        TITLEBAR.setBackground(SystemColor.activeCaption);
        TITLEBAR.setOpaque(true);
        add(TITLEBAR, BorderLayout.NORTH);
        add(EDITOR, BorderLayout.CENTER);
    }

    private void syncCurrentlyLoadedMember() {
        if (CURRENT_MEMBER != null) {
            if (CURRENT_MEMBER.isModified()) {
                CURRENT_MEMBER.setChangedText(EDITOR.getText());
            }
        }
    }
}
