package com.googlecode.kipler.reasoning;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import com.googlecode.kipler.container.dl.Individual;
import com.googlecode.kipler.syntax.concept.AtConcept;
import com.googlecode.kipler.syntax.concept.Concept;
import com.googlecode.kipler.syntax.concept.ConceptName;

public class ProofDialog extends JDialog {

    private JPanel contentPane;

    private DefaultMutableTreeNode currentNode;

    /**
	 * Create the frame.
	 */
    public ProofDialog() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        currentNode = new DefaultMutableTreeNode("root");
        JTree tree = new JTree(currentNode);
        contentPane.add(tree, BorderLayout.CENTER);
    }

    public void branchNominal() {
        DefaultMutableTreeNode child = new DefaultMutableTreeNode();
        currentNode.setUserObject("nominal");
        currentNode.add(child);
        currentNode = child;
    }

    public void branchUnion() {
        DefaultMutableTreeNode child = new DefaultMutableTreeNode();
        currentNode.setUserObject("union");
        currentNode.add(child);
        currentNode = child;
    }

    public void addLiteralClash(LiteralClashCondition clash, Concept interpolant) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[Literal clash: ");
        for (ConceptName cn : clash.getLiterals().getLiterals()) {
            buffer.append(new AtConcept(clash.getIndvidual().getName(), cn));
            buffer.append(",");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append("]");
        buffer.append(interpolant);
        buffer.append("[");
        currentNode.setUserObject(buffer.toString());
    }

    public void addNominalClash(NominalClashCondition clash, Concept interpolant) {
        currentNode.setUserObject("Nominal clash: " + clash.getPositive() + "," + clash.getNegative());
    }

    public void backtrack() {
        currentNode = (DefaultMutableTreeNode) currentNode.getParent();
    }
}
