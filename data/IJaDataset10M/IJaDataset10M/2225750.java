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
 * @version $Revision: 1.5 $, $Date: 2002/12/11 05:57:06 $
 */
public class DeleteAction extends AbstractAction {

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
        }
        Node node = textArea.node;
        JoeTree tree = node.getTree();
        OutlineLayoutManager layout = tree.getDocument().panel.layout;
        switch(e.getModifiers()) {
            case 0:
                if (isIconFocused) {
                    delete(tree, layout, true);
                } else {
                    deleteText(textArea, tree, layout);
                }
                break;
        }
    }

    public static void deleteText(OutlinerCellRendererImpl textArea, JoeTree tree, OutlineLayoutManager layout) {
        Node currentNode = textArea.node;
        if (!currentNode.isEditable()) {
            return;
        }
        int caretPosition = textArea.getCaretPosition();
        int markPosition = textArea.getCaret().getMark();
        if ((caretPosition == textArea.getText().length()) && (caretPosition == markPosition) && textArea.node.isLeaf()) {
            mergeWithNextVisibleNode(textArea, tree, layout);
        } else {
            String oldText = currentNode.getValue();
            String newText = null;
            int oldCaretPosition = textArea.getCaretPosition();
            int oldMarkPosition = textArea.getCaret().getMark();
            int newCaretPosition = -1;
            int newMarkPosition = -1;
            int startSelection = Math.min(oldCaretPosition, oldMarkPosition);
            int endSelection = Math.max(oldCaretPosition, oldMarkPosition);
            if (startSelection < 0 || endSelection < 0 || oldText.length() < 0) {
                String msg = "Error at TextKeyListener:keyPressed:VK_DELETE\n";
                msg = msg + "startSelection: -1\n";
                msg = msg + "oldCaretPosition: " + oldCaretPosition + "\n";
                msg = msg + "oldMarkPosition: " + oldMarkPosition + "\n";
                msg = msg + "oldText.length: " + oldText.length();
                System.out.println("Stan_Debug:\t" + msg);
                return;
            }
            if (startSelection != endSelection) {
                newCaretPosition = startSelection;
                newMarkPosition = startSelection;
                newText = oldText.substring(0, startSelection) + oldText.substring(endSelection, oldText.length());
            } else if (startSelection == oldText.length()) {
                newCaretPosition = oldText.length();
                newMarkPosition = oldText.length();
                newText = oldText;
            } else {
                newCaretPosition = startSelection;
                newMarkPosition = startSelection;
                newText = oldText.substring(0, newCaretPosition) + oldText.substring(newCaretPosition + 1, oldText.length());
            }
            UndoableEdit undoable = tree.getDocument().getUndoQueue().getIfEdit();
            if ((undoable != null) && (undoable.getNode() == currentNode) && (!undoable.isFrozen())) {
                undoable.setNewText(newText);
                undoable.setNewPosition(newCaretPosition);
                undoable.setNewMarkPosition(newMarkPosition);
            } else {
                UndoableEdit newUndoable = new UndoableEdit(currentNode, oldText, newText, oldCaretPosition, newCaretPosition, oldMarkPosition, newMarkPosition);
                newUndoable.setName("Delete Text");
                tree.getDocument().getUndoQueue().add(newUndoable);
            }
            currentNode.setValue(newText);
            tree.setEditingNode(currentNode);
            tree.setCursorMarkPosition(newMarkPosition);
            tree.setCursorPosition(newCaretPosition, false);
            tree.getDocument().setPreferredCaretPosition(newCaretPosition);
            textArea.setText(newText);
            textArea.setCaretPosition(newMarkPosition);
            textArea.moveCaretPosition(newCaretPosition);
            if (textArea.getPreferredSize().height != textArea.height || !currentNode.isVisible()) {
                layout.draw(currentNode, OutlineLayoutManager.TEXT);
            }
        }
    }

    private static void mergeWithNextVisibleNode(OutlinerCellRendererImpl textArea, JoeTree tree, OutlineLayoutManager layout) {
        Node currentNode = textArea.node;
        Node nextNode = tree.getNextNode(currentNode);
        if (nextNode == null) {
            return;
        }
        if (!nextNode.isEditable()) {
            return;
        }
        Node parent = currentNode.getParent();
        String nextNodeText = nextNode.getValue();
        String currentNodeText = currentNode.getValue();
        String newNextNodeText = currentNodeText + nextNodeText;
        UndoableEdit undoableEdit = new UndoableEdit(nextNode, nextNodeText, newNextNodeText, 0, currentNodeText.length(), 0, currentNodeText.length());
        CompoundUndoableReplace undoableReplace = new CompoundUndoableReplace(parent);
        undoableReplace.addPrimitive(new PrimitiveUndoableReplace(parent, currentNode, null));
        CompoundUndoableImpl undoable = new CompoundUndoableImpl(true);
        undoable.addPrimitive(undoableReplace);
        undoable.addPrimitive(undoableEdit);
        undoable.setName("Merge with Next Node");
        tree.getDocument().getUndoQueue().add(undoable);
        undoable.redo();
    }

    public static void delete(JoeTree tree, OutlineLayoutManager layout, boolean deleteMode) {
        Node youngestNode = tree.getYoungestInSelection();
        if (youngestNode == null) {
            return;
        }
        Node parent = youngestNode.getParent();
        CompoundUndoableReplace undoable = new CompoundUndoableReplace(parent, deleteMode);
        int startDeleting = 0;
        if (tree.isWholeDocumentSelected()) {
            if (tree.isDocumentEmpty()) {
                return;
            }
            Node newNode = new NodeImpl(tree, "");
            newNode.setDepth(0);
            undoable.addPrimitive(new PrimitiveUndoableReplace(parent, youngestNode, newNode));
            startDeleting++;
        }
        JoeNodeList nodeList = tree.getSelectedNodes();
        int deleteCount = 0;
        for (int i = startDeleting, limit = nodeList.size(); i < limit; i++) {
            Node node = nodeList.get(i);
            if (!node.isEditable()) {
                continue;
            }
            undoable.addPrimitive(new PrimitiveUndoableReplace(parent, node, null));
            deleteCount++;
        }
        if (!undoable.isEmpty()) {
            if (deleteCount == 1) {
                undoable.setName("Delete Node");
            } else {
                undoable.setName(new StringBuffer().append("Delete ").append(deleteCount).append(" Nodes").toString());
            }
            tree.getDocument().getUndoQueue().add(undoable);
            undoable.redo();
        }
        return;
    }
}
