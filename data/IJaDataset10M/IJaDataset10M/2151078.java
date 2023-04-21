package com.organic.maynard.outliner.actions;

import com.organic.maynard.outliner.*;
import com.organic.maynard.outliner.util.preferences.*;
import com.organic.maynard.outliner.util.undo.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Window;
import java.awt.datatransfer.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import com.organic.maynard.util.string.*;

/**
 * @author  $Author: maynardd $
 * @version $Revision: 1.3 $, $Date: 2002/12/11 05:57:35 $
 */
public class DefaultAction extends AbstractAction {

    public void actionPerformed(ActionEvent e) {
        OutlinerCellRendererImpl textArea = null;
        boolean isIconFocused = true;
        Component c = (Component) e.getSource();
        if (c instanceof OutlineButton) {
            textArea = ((OutlineButton) c).renderer;
        } else if (c instanceof OutlineLineNumber) {
            textArea = ((OutlineLineNumber) c).renderer;
        } else if (c instanceof OutlineCommentIndicator) {
            textArea = ((OutlineCommentIndicator) c).renderer;
        } else if (c instanceof OutlinerCellRendererImpl) {
            textArea = (OutlinerCellRendererImpl) c;
            isIconFocused = false;
        } else if (c instanceof JTextArea) {
            originalDefaultAction(e, (JTextArea) c);
            return;
        }
        if (textArea == null) {
            return;
        }
        Node node = textArea.node;
        JoeTree tree = node.getTree();
        OutlineLayoutManager layout = tree.getDocument().panel.layout;
        if (isIconFocused) {
        } else {
            defaultActionText(e, textArea, tree, layout);
        }
    }

    private static void originalDefaultAction(ActionEvent e, JTextArea textArea) {
        String content = e.getActionCommand();
        int mod = e.getModifiers();
        if ((content != null) && (content.length() > 0) && ((mod & ActionEvent.ALT_MASK) == (mod & ActionEvent.CTRL_MASK))) {
            char ch = content.charAt(0);
            switch(ch) {
                case KeyEvent.VK_ENTER:
                case KeyEvent.VK_TAB:
                case KeyEvent.VK_ESCAPE:
                    return;
            }
            if ((ch >= 0x20) && (ch != 0x7F)) {
                textArea.replaceSelection(content);
            }
        }
    }

    public static void defaultActionText(ActionEvent e, OutlinerCellRendererImpl textArea, JoeTree tree, OutlineLayoutManager layout) {
        Node currentNode = textArea.node;
        int mod = e.getModifiers();
        boolean isControlDown = (mod & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK;
        boolean isAltDown = (mod & ActionEvent.ALT_MASK) == ActionEvent.ALT_MASK;
        boolean isMetaDown = (mod & ActionEvent.META_MASK) == ActionEvent.META_MASK;
        if (!currentNode.isEditable()) {
            if (!isControlDown && !isAltDown && !isMetaDown) {
                Outliner.outliner.getToolkit().beep();
            }
            return;
        }
        if (isControlDown || isAltDown || isMetaDown) {
            return;
        }
        String content = e.getActionCommand();
        if ((content != null) && (content.length() > 0) && ((mod & ActionEvent.ALT_MASK) == (mod & ActionEvent.CTRL_MASK))) {
            char ch = content.charAt(0);
            switch(ch) {
                case KeyEvent.VK_ENTER:
                case KeyEvent.VK_TAB:
                case KeyEvent.VK_ESCAPE:
                    return;
            }
            if ((ch >= 0x20) && (ch != 0x7F)) {
                textArea.replaceSelection(content);
            }
        }
        int caretPosition = textArea.getCaretPosition();
        String oldText = currentNode.getValue();
        String newText = textArea.getText();
        currentNode.setValue(newText);
        UndoableEdit undoable = tree.getDocument().getUndoQueue().getIfEdit();
        if ((undoable != null) && (undoable.getNode() == currentNode) && (!undoable.isFrozen())) {
            undoable.setNewText(newText);
            undoable.setNewPosition(caretPosition);
            undoable.setNewMarkPosition(caretPosition);
        } else {
            tree.getDocument().getUndoQueue().add(new UndoableEdit(currentNode, oldText, newText, tree.getCursorPosition(), caretPosition, tree.getCursorMarkPosition(), caretPosition));
        }
        tree.setEditingNode(currentNode);
        tree.setCursorMarkPosition(textArea.getCaret().getMark());
        tree.setCursorPosition(caretPosition, false);
        tree.getDocument().setPreferredCaretPosition(caretPosition);
        if (textArea.getPreferredSize().height != textArea.height || !currentNode.isVisible()) {
            layout.draw(currentNode, OutlineLayoutManager.TEXT);
        }
    }
}
