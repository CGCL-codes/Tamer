package net.sf.xpontus.modules.gui.components;

import net.sf.xpontus.syntax.SyntaxEditorkit;
import javax.swing.plaf.basic.BasicEditorPaneUI;
import javax.swing.text.EditorKit;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 * @version 0.0.2
 */
public class XPontusEditorUI extends BasicEditorPaneUI {

    private final EditorKit editorKit;

    /** Creates a new instance of XPontusEditorUI
     * @param editor The text component
     * @param fileExtension The filename extension
     */
    public XPontusEditorUI(JTextComponent editor, String fileExtension) {
        editorKit = new SyntaxEditorkit(editor, fileExtension);
    }

    /**
     * Returns The editorkit to use for this text component
     * @param editor The text component
     * @return The editorkit to use for this text component
     */
    public EditorKit getEditorKit(JTextComponent editor) {
        return editorKit;
    }
}
