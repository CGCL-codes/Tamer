package unbbayes.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import unbbayes.gui.table.GUIPotentialTable;
import unbbayes.prs.Graph;
import unbbayes.prs.INode;
import unbbayes.prs.bn.ProbabilisticTable;
import unbbayes.prs.bn.TreeVariable;
import unbbayes.util.ResourceController;

/**
 * Default implementation of {@link ILikelihoodEvidenceDialogBuilder}
 * @author Shou Matsumoto
 *
 */
public class LikelihoodEvidenceDialogBuilder implements ILikelihoodEvidenceDialogBuilder {

    private NumberFormat numberFormat;

    private ResourceBundle resource = ResourceController.newInstance().getBundle(unbbayes.gui.resources.GuiResources.class.getName(), Locale.getDefault(), LikelihoodEvidenceDialogBuilder.class.getClassLoader());

    /**
	 * Default constructor must be public
	 */
    public LikelihoodEvidenceDialogBuilder() {
        this.setNumberFormat(NumberFormat.getInstance(Locale.US));
        this.getNumberFormat().setMaximumFractionDigits(2);
    }

    public JDialog buildDialog(Graph graph, final INode nodeToAddLikelihood, final Component owner) {
        final JTable table = this.buildMainTable(graph, nodeToAddLikelihood);
        JPanel mainPanel = this.buildMainPanel(graph, nodeToAddLikelihood, table);
        final JOptionPane optPane = new JOptionPane(mainPanel, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog ret = optPane.createDialog(owner, getResource().getString("likelihoodName"));
        ret.setResizable(true);
        ret.addComponentListener(this.buildLikelihoodEvidenceDialogComponentListener(graph, nodeToAddLikelihood, table, optPane));
        ret.pack();
        return ret;
    }

    /**
	 * Builds the center panel of the dialog in {@link #buildDialog(INode, Component)}
	 * @param graph 
	 * @param nodeToAddLikelihood : the node passed from {@link #buildDialog(INode, Component)}
	 * @param table : the table created in {@link #buildMainTable(INode)}
	 * @return a JPanel with a label and table.
	 */
    protected JPanel buildMainPanel(Graph graph, INode nodeToAddLikelihood, JTable table) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(nodeToAddLikelihood.toString());
        panel.add(label);
        panel.add(table);
        return panel;
    }

    /**
	 * Generates the main JTable which will display the states and likelihoods and 
	 * allow input to user.
	 * @param graph 
	 * @param nodeToAddLikelihood : the node passed from {@link #buildDialog(INode, Component)}
	 * @return : a JTable with states and likelihood distribution
	 */
    protected JTable buildMainTable(Graph graph, INode nodeToAddLikelihood) {
        ProbabilisticTable dummyTable = this.buildTableForLikelihoodEvidenceInput(graph, nodeToAddLikelihood);
        return new GUIPotentialTable(dummyTable).makeTable();
    }

    /**
	 * This method is used in {@link #buildMainTable(Graph, INode)} in order
	 * to create a dummy potential table to display to user, so that
	 * user can input values for likelhood evidence.
	 * @param graph 
	 * @param nodeToAddLikelihood : the node passed from {@link #buildDialog(INode, Component)}
	 * @return
	 */
    protected ProbabilisticTable buildTableForLikelihoodEvidenceInput(Graph graph, INode nodeToAddLikelihood) {
        ProbabilisticTable ret = new ProbabilisticTable();
        ret.addVariable(nodeToAddLikelihood);
        for (int i = 0; i < ret.tableSize(); i++) {
            ret.setValue(i, 1);
        }
        return ret;
    }

    /**
	 * Generates an instance of component listener which will commit changes on a table after the component is closed (i.e. hidden).
	 * @param graph 
	 * @param nodeToAddLikelihood
	 * @param table
	 * @param optPane
	 * @return {@link LikelihoodEvidenceDialogComponentListener}
	 */
    protected ComponentListener buildLikelihoodEvidenceDialogComponentListener(Graph graph, INode nodeToAddLikelihood, JTable table, JOptionPane optPane) {
        return new LikelihoodEvidenceDialogComponentListener(nodeToAddLikelihood, table, optPane);
    }

    /**
	 * @return the resource
	 */
    public ResourceBundle getResource() {
        return resource;
    }

    /**
	 * @param resource the resource to set
	 */
    public void setResource(ResourceBundle resource) {
        this.resource = resource;
    }

    /**
	 * Format used by the input dialog created in {@link #buildDialog(INode, Component)}
	 * @param numberFormat the numberFormat to set
	 */
    public void setNumberFormat(NumberFormat nuberFormat) {
        this.numberFormat = nuberFormat;
    }

    /**
	 * Format used by the input dialog created in {@link #buildDialog(INode, Component)}
	 * @return the numberFormat
	 */
    public NumberFormat getNumberFormat() {
        return numberFormat;
    }

    /**
	 * Component listener for the dialog generated in {@link LikelihoodEvidenceDialogBuilder#buildDialog(INode, Component)} 
	 * to commit changes on a table after the component is closed (i.e. hidden)
	 * @author Shou Matsumoto
	 */
    public class LikelihoodEvidenceDialogComponentListener implements ComponentListener {

        private final INode nodeToAddLikelihood;

        private final JTable table;

        private final JOptionPane optPane;

        /**
		 * @param nodeToAddLikelihood : the node to update
		 * @param table : the table to read
		 * @param optPane : the option pane related to the dialog of {@link LikelihoodEvidenceDialogBuilder#buildDialog(INode, Component)}.
		 * This is accessed in order to obtain the user selection
		 */
        public LikelihoodEvidenceDialogComponentListener(INode nodeToAddLikelihood, JTable table, JOptionPane optPane) {
            this.nodeToAddLikelihood = nodeToAddLikelihood;
            this.table = table;
            this.optPane = optPane;
        }

        public void componentHidden(ComponentEvent e) {
            if (optPane.getValue().equals(JOptionPane.OK_OPTION)) {
                float[] stateProbabilities = new float[nodeToAddLikelihood.getStatesSize()];
                try {
                    for (int i = 0; i < nodeToAddLikelihood.getStatesSize(); i++) {
                        stateProbabilities[i] = getNumberFormat().parse((String) table.getValueAt(i, 1)).floatValue();
                    }
                } catch (ParseException pe) {
                    pe.printStackTrace();
                    JOptionPane.showMessageDialog(null, pe.getMessage());
                    return;
                }
                float totalProbability = 0;
                for (int i = 0; i < nodeToAddLikelihood.getStatesSize(); i++) {
                    totalProbability += stateProbabilities[i];
                }
                if (totalProbability == 0.0) {
                    JOptionPane.showMessageDialog(null, getResource().getString("likelihoodName") + ": 0");
                    return;
                }
                for (int i = 0; i < nodeToAddLikelihood.getStatesSize(); i++) {
                    stateProbabilities[i] = stateProbabilities[i] / totalProbability;
                }
                ((TreeVariable) nodeToAddLikelihood).addLikeliHood(stateProbabilities);
            }
        }

        public void componentShown(ComponentEvent e) {
        }

        public void componentResized(ComponentEvent e) {
        }

        public void componentMoved(ComponentEvent e) {
        }
    }
}
