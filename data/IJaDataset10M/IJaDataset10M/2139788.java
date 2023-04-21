package org.coode.oae.ui.action;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import org.coode.oae.ui.OWLCalculations;
import org.protege.editor.core.ui.workspace.Workspace;
import org.protege.editor.owl.ui.action.ProtegeOWLAction;
import org.semanticweb.owl.inference.OWLReasonerException;
import uk.ac.manchester.mae.ProtegeEvaluator;

/**
 * @author Luigi Iannone
 * 
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Mar 17, 2008
 */
public class EvaluateAction extends ProtegeOWLAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * @see org.protege.editor.core.plugin.ProtegePluginInstance#dispose()
	 */
    public void dispose() throws Exception {
    }

    /**
	 * @see org.protege.editor.core.plugin.ProtegePluginInstance#initialise()
	 */
    public void initialise() throws Exception {
    }

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent arg0) {
        ProtegeEvaluator protegeEvaluator = null;
        try {
            protegeEvaluator = new ProtegeEvaluator(this.getOWLModelManager());
            protegeEvaluator.evaluate(true);
            OWLCalculations.setLastEvaluationReport(protegeEvaluator.getReport());
            this.getOWLWorkspace().showResultsView("org.coode.oae.EvaluationResults", true, Workspace.BOTTOM_RESULTS_VIEW);
        } catch (OWLReasonerException e) {
            JOptionPane.showMessageDialog(this.getWorkspace(), "Exception in creating the reasoner, impossible to evaluate the formulas");
            e.printStackTrace();
        } finally {
            if (protegeEvaluator != null) {
                protegeEvaluator.getReport();
            }
        }
    }
}
