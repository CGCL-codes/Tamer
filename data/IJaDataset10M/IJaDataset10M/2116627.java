package net.dromard.filesynchronizer.gui.tree;

import java.io.File;
import net.dromard.filesynchronizer.treenode.FileSynchronizerTodoTaskTreeNode;
import net.dromard.filesynchronizer.treenode.FileSynchronizerTreeNode;

/**
 * File tree node object.
 * 
 * @author Pingus
 */
public class JFileSynchronizerTreeNode extends FileSynchronizerTodoTaskTreeNode {

    public JFileSynchronizerTreeNode(File source, File destination) {
        super(source, destination);
    }

    public FileSynchronizerTreeNode createNode(File source, File destination) {
        JFileSynchronizerTreeNode child = new JFileSynchronizerTreeNode(source, destination);
        if (!child.isLeaf() || child.getTodoTask() != FileSynchronizerTodoTaskTreeNode.TODO_NOTHING) {
            return child;
        }
        return null;
    }
}
