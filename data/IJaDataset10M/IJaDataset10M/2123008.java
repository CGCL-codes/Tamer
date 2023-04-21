package org.deri.wsml.eclipse.visualizer.graph.manipulator.rcactions.ontology.attribute;

import java.util.Set;
import net.sourceforge.jpowergraph.Node;
import net.sourceforge.powerswing.localization.PBundle;
import org.deri.wsml.eclipse.visualizer.editors.Viz;
import org.deri.wsml.eclipse.visualizer.graph.manipulator.rcactions.EntitySelectionListener;
import org.deri.wsml.eclipse.visualizer.graph.node.ontology.InstanceNode;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.omwg.ontology.Attribute;
import org.omwg.ontology.Concept;
import org.omwg.ontology.Instance;
import org.omwg.ontology.Type;
import org.omwg.ontology.Value;
import org.wsmo.common.Namespace;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.common.exception.SynchronisationException;
import org.wsmostudio.runtime.LogManager;
import org.wsmostudio.runtime.WSMORuntime;
import org.wsmostudio.ui.editors.common.DataValueEditor;
import org.wsmostudio.ui.editors.common.WSMOChooser;

public class AddAttributeToInstanceSelectionListener extends EntitySelectionListener {

    private Node node;

    private Viz viz;

    public AddAttributeToInstanceSelectionListener(Composite theParent, Node theNode, Viz theViz, PBundle theMessages, Namespace theDefaultNamespace) {
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
        Instance parentInstance = ((InstanceNode) node).getInstance();
        WSMOChooser chooser = WSMOChooser.createAttributesChooser(viz.getEditorSite().getShell(), WSMORuntime.getRuntime());
        chooser.setDialogTitle("Select Attribute");
        Attribute attr = (Attribute) chooser.open();
        if (attr == null) {
            return;
        }
        boolean containsConcept = false;
        for (Type t : (Set<Type>) attr.listTypes()) {
            if (t instanceof Concept) {
                containsConcept = true;
                break;
            }
        }
        Value attrValue = null;
        if (containsConcept) {
            chooser = WSMOChooser.createInstanceChooser(viz.getEditorSite().getShell(), viz.getTopEntity());
            chooser.setDialogTitle("Select Attribute Instance Value");
            attrValue = (Value) chooser.open();
        } else {
            attrValue = DataValueEditor.createEditor(viz.getEditorSite().getShell());
        }
        if (attrValue == null) {
            return;
        }
        try {
            parentInstance.addAttributeValue(attr.getIdentifier(), attrValue);
        } catch (SynchronisationException e) {
            LogManager.logError("Error adding attribute value " + attrValue + " for " + attr, e);
        } catch (InvalidModelException e) {
            LogManager.logError("Error adding attribute value " + attrValue + " for " + attr, e);
        }
        viz.makeDirty();
    }
}
