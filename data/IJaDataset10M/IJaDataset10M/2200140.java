package org.vikamine.app.rcp.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.vikamine.app.DMManager;
import org.vikamine.kernel.data.Ontology;
import org.vikamine.kernel.data.OntologyConstants;
import org.vikamine.kernel.data.RDFStatement;
import org.vikamine.kernel.data.RDFTripleStore;
import org.vikamine.kernel.subgroup.SG;
import org.vikamine.swing.util.Utils;

/**
 * The Class MarkAsAcausalSG.
 * 
 * @author Alex Plischke
 */
public class MarkAsAcausalSG extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Ontology onto = DMManager.getInstance().getOntology();
        SG sg = Utils.getCurrentSG();
        if (onto != null) {
            RDFTripleStore store = onto.getTripleStore();
            RDFStatement firstMatchingStatement = store.getFirstMatchingStatement(sg, OntologyConstants.ACAUSAL_SUBGROUP, RDFTripleStore.ANY_OBJECT);
            if (firstMatchingStatement == null) {
                RDFStatement statement = new RDFStatement(sg, OntologyConstants.ACAUSAL_SUBGROUP, true);
                store.addStatement(statement);
            } else {
                Boolean oldValue = (Boolean) firstMatchingStatement.getObject();
                RDFStatement statement = new RDFStatement(sg, OntologyConstants.ACAUSAL_SUBGROUP, !oldValue);
                store.addStatement(statement);
            }
        }
        return null;
    }
}
