package uk.ac.man.cs.mig.coode.owlviz.command;

import uk.ac.man.cs.mig.coode.owlviz.ui.OWLVizIcons;
import uk.ac.man.cs.mig.coode.owlviz.ui.OWLVizTab;
import uk.ac.man.cs.mig.util.graph.ui.GraphComponent;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Iterator;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Feb 11, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class HideAllClassesCommand extends AbstractAction {

    private OWLVizTab tab;

    public HideAllClassesCommand(OWLVizTab tab) {
        super("Hide all classes", OWLVizIcons.getIcon(OWLVizIcons.HIDE_ALL_CLASSES_ICON));
        putValue(AbstractAction.SHORT_DESCRIPTION, "Hide all classes");
        this.tab = tab;
    }

    /**
	 * Invoked when an action occurs.
	 */
    public void actionPerformed(ActionEvent e) {
        for (Iterator it = tab.getGraphComponents().iterator(); it.hasNext(); ) {
            GraphComponent graphComponent = (GraphComponent) it.next();
            graphComponent.getVisualisedObjectManager().hideAll();
        }
    }
}
