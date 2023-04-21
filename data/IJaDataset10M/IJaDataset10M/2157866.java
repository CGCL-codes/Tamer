package org.argouml.uml.diagram.botl_rule.ui.BOTL;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Vector;
import javax.swing.JComboBox;
import org.argouml.kernel.Project;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.botl_model.ui.UMLBOTLSourceDiagram;
import org.argouml.uml.diagram.botl_rule.ui.UMLBOTLRuleDiagram;
import org.argouml.uml.ui.UMLUserInterfaceComponent;
import ru.novosoft.uml.MElementEvent;

/**
 * 
 * @author Janos Kovats
 *
 * This is a model class for the PropPanelUMLBOTLRuleDiagram
 */
public class BOTLSourcesModel extends JComboBox implements UMLUserInterfaceComponent {

    private boolean updating = false;

    public BOTLSourcesModel() {
        this.addActionListener(this);
    }

    private Project getCurrentProject() {
        ProjectBrowser pb = ProjectBrowser.TheInstance;
        return pb.getProject();
    }

    private Object getTarget() {
        ProjectBrowser pb = ProjectBrowser.TheInstance;
        return pb.getDetailsTarget();
    }

    private ArgoDiagram getActiveDiagram() {
        ProjectBrowser pb = ProjectBrowser.TheInstance;
        return pb.getActiveDiagram();
    }

    /**
	 * calls the setSource method from the rule diagram
	 * @param source
	 */
    private void setSource(UMLBOTLSourceDiagram source) {
        ArgoDiagram d = getActiveDiagram();
        if (d instanceof UMLBOTLRuleDiagram) ((UMLBOTLRuleDiagram) d).setSource(source);
    }

    /**
	 * calls the getSource method from the rule diagram
    */
    private UMLBOTLSourceDiagram getSource() {
        ArgoDiagram d = getActiveDiagram();
        if (d instanceof UMLBOTLRuleDiagram) {
            return ((UMLBOTLRuleDiagram) d).getSource();
        }
        return null;
    }

    /**
	 * update the list from source diagrams
	 */
    private void updateSourceNames() {
        updating = true;
        try {
            this.removeAllItems();
            Vector diagrams = getCurrentProject().getDiagrams();
            Vector diagramNames = new Vector();
            for (int i = 0; i < diagrams.size(); i++) {
                Object d = diagrams.elementAt(i);
                if (d instanceof UMLBOTLSourceDiagram) {
                    diagramNames.addElement(((UMLBOTLSourceDiagram) d).getName());
                }
            }
            Collections.sort(diagramNames);
            for (int i = 0; i < diagramNames.size(); i++) {
                this.addItem(diagramNames.elementAt(i));
            }
            this.setSelectedItem(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updating = false;
    }

    /**
	 * This method selects the current source diagram in the combobox
	 *
	 */
    private void selectCurrentDiagram() {
        if (getSource() != null) {
            this.setSelectedItem((String) (getSource().getName()));
        }
    }

    /**
	 * This method updates the Diagram
	 */
    private void updateSource() {
        String sel = (String) this.getSelectedItem();
        Project p = getCurrentProject();
        Vector diagrams = p.getDiagrams();
        int i = 0;
        boolean found = false;
        while ((i < diagrams.size()) && (found == false)) {
            Object sd = diagrams.elementAt(i);
            if (sd instanceof UMLBOTLSourceDiagram) {
                if (((UMLBOTLSourceDiagram) sd).getName().equals(sel)) {
                    found = true;
                    setSource(((UMLBOTLSourceDiagram) sd));
                }
            }
            i++;
        }
    }

    /**
	 * if a new diagram selected update the diagram
     */
    public void actionPerformed(ActionEvent e) {
        if (!updating) {
            updateSource();
        }
    }

    /**
	 * if target changed, update the list and select the current diagram in this
     */
    public void targetChanged() {
        if (updatingDiagram) return;
        updatingDiagram = true;
        try {
            updateSourceNames();
            selectCurrentDiagram();
            BOTLObjectHelper.updateDiagramElements();
        } catch (Exception e) {
            e.printStackTrace();
        }
        updatingDiagram = false;
    }

    /**
	 * if target changed, update the list and select the current diagram in this
     */
    public static boolean updatingDiagram;

    public void targetReasserted() {
        if (updatingDiagram) return;
        updatingDiagram = true;
        try {
            updateSourceNames();
            selectCurrentDiagram();
            BOTLObjectHelper.updateDiagramElements();
        } catch (Exception e) {
            e.printStackTrace();
        }
        updatingDiagram = false;
    }

    public void propertySet(MElementEvent arg0) {
    }

    public void roleAdded(MElementEvent arg0) {
    }

    public void roleRemoved(MElementEvent arg0) {
    }

    public void listRoleItemSet(MElementEvent arg0) {
    }

    public void removed(MElementEvent arg0) {
    }

    public void recovered(MElementEvent arg0) {
    }
}
