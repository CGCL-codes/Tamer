package net.sourceforge.fddtools.ui;

import java.io.IOException;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Stack;
import javax.swing.JTree;
import javax.swing.tree.MutableTreeNode;
import net.sourceforge.fddtools.internationalization.Messages;
import net.sourceforge.fddtools.model.DefaultFDDModel;
import net.sourceforge.fddtools.model.FDDElement;
import net.sourceforge.fddtools.model.Project;
import net.sourceforge.fddtools.model.TreeNodeTokenizer;

/**
 * Class to implement TreeBuilder, which know to build a FDD tree which is built
 * under an specified root.
 */
public class FDDSequenceTreeBuilder {

    private static final String PROJECT_DEFAULT_NAME = "FDDSequenceTreeBuilder.Proyect.DefaultName";

    /**
     * Because the elements are assumed to be sequencly arranged in an input
     * file, which is described in MS Project .csv file format specification in
     * mail Kenneth sent to Vernon, a stack is utilized to implement the tree
     * building easily Because FDDSequenceTreeBuilder knows parameter model is a
     * DefaultFDDModel, it's safe to convert model into DefaultFDDModel
     * 
     * @param model
     *                  TODO: Document this parameter!
     * @param source
     *                  TODO: Document this parameter!
     */
    public JTree buildTree(TreeNodeTokenizer source) {
        DefaultFDDModel model = new DefaultFDDModel(new Project(Messages.getInstance().getMessage(PROJECT_DEFAULT_NAME), 0, new Date(), ""));
        Stack holder = new Stack();
        try {
            if (source.hasMoreNodes()) {
                model.setRootFDDElement((FDDElement) source.nextNode());
                holder.push(model.getRoot());
            }
            while (source.hasMoreNodes()) {
                MutableTreeNode newNode = source.nextNode();
                MutableTreeNode topOfStack = (MutableTreeNode) holder.peek();
                while (!((FDDElement) newNode).isLegalParent((FDDElement) topOfStack)) {
                    holder.pop();
                    topOfStack = (MutableTreeNode) holder.peek();
                }
                model.addChildFDDElement((FDDElement) newNode, (FDDElement) topOfStack);
                holder.push(newNode);
            }
        } catch (NoSuchElementException e) {
            System.err.println(e.toString() + "\n" + "Try to fetch next node without query?");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            System.err.println("Error reading input file \n" + e.toString());
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println(e.toString() + "\n" + "Type mishandled?");
            e.printStackTrace();
            return null;
        }
        return new JTree(model);
    }
}
