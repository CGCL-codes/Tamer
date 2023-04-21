package org.deri.wsml.eclipse.visualizer.graph.manipulator.rcactions;

import net.sourceforge.jpowergraph.Node;
import net.sourceforge.powerswing.localization.PBundle;
import org.deri.wsml.eclipse.visualizer.editors.Viz;
import org.deri.wsml.eclipse.visualizer.graph.node.AxiomNode;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.ontology.Axiom;
import org.wsmo.common.Namespace;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmostudio.runtime.LogManager;
import org.wsmostudio.runtime.WSMORuntime;

public class AddLogicalExpressionSelectionListener extends EntitySelectionListener {

    private Node node;

    private Viz viz;

    public AddLogicalExpressionSelectionListener(Composite theParent, Node theNode, Viz theViz, PBundle theMessages, Namespace theDefaultNamespace) {
        super(theParent, theMessages, theDefaultNamespace);
        this.node = theNode;
        this.viz = theViz;
    }

    public void widgetDefaultSelected(SelectionEvent e) {
        doStuff();
    }

    public void widgetSelected(SelectionEvent e) {
        doStuff();
    }

    public void doStuff() {
        Axiom axiom = ((AxiomNode) node).getAxiom();
        String leString = getLogicalExpressionString("AddLogicalExpressionSelectionListener.GetIDTitle", "", viz.getTopEntity());
        if (leString == null) {
            return;
        }
        LogicalExpressionFactory factory = WSMORuntime.getRuntime().getLogExprFactory();
        try {
            LogicalExpression le = factory.createLogicalExpression(leString, viz.getTopEntity());
            axiom.addDefinition(le);
            viz.makeDirty();
        } catch (Exception e) {
            LogManager.logError("Error creating logical expression : " + leString, e);
        }
    }
}
