package owlwatcher.view;

import javax.swing.tree.DefaultMutableTreeNode;
import owlwatcher.project.Project;
import com.hp.hpl.jena.ontology.OntClass;

/**
 * 
 * @author peter
 * created Nov 18, 2006
 *
 */
class ConcreteTreeNode {

    private OntClass contents;

    private Project project;

    private DataViewer viewer;

    private ClassPane classPane;

    private DefaultMutableTreeNode container;

    /**
     * Constructor
     * @param oc the OntClass displayed by this node
     * @param p the Project associated with this pane 
     * @param d the Dataview that will provide a time for any events.
     */
    ConcreteTreeNode(OntClass oc, Project p, DataViewer d, ClassPane c) {
        contents = oc;
        project = p;
        viewer = d;
        classPane = c;
    }

    /**
     * 
     * @return the class displayed by this node
     */
    public OntClass getContents() {
        return contents;
    }

    public void setContainer(DefaultMutableTreeNode c) {
        container = c;
    }

    /**
     * @return the local name of the OntClass
     */
    public String toString() {
        return contents.getLocalName();
    }

    /**
     * Queues an event specified by contents to the project's event cache.
     *
     */
    public void recordHit() {
        project.addEvent(contents, viewer.getTime(), viewer);
    }

    /**
     * Process the command specified by the command that was set in the class pane. 
     */
    public void processCommand() {
        processCommand(classPane.getNodeSelectionCommand());
    }

    /**
     * Process the command, operating on the contents of this node.
     * @param c the command to process
     */
    public void processCommand(ClassPane.Command c) {
        switch(c) {
            case addsubclass:
                project.processEventCache();
                final String addChildString = "Add SubClass to " + contents.getLocalName();
                String newName = (String) classPane.showInputDialog(addChildString, "Name of the new subclass");
                if (newName != null) {
                    newName = "#" + newName;
                    OntClass newClass = project.addSubClass(contents, newName);
                    ConcreteTreeNode newCNode = new ConcreteTreeNode(newClass, project, viewer, classPane);
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(newCNode);
                    newCNode.setContainer(child);
                    container.insert(child, 0);
                    classPane.treeModel.insertNodeInto(child, container, 0);
                }
                break;
            case deletesubclass:
                project.processEventCache();
                if (project.hasInstances(contents)) {
                    classPane.showWarning("Currently can't delete classes with instances", "Can't Delete");
                } else if (project.isImported(contents)) {
                    classPane.showWarning("Can't delete imported Term", "Can't Delete");
                } else if (container.getChildCount() != 0) {
                    classPane.showWarning("Can't delete a term with children;\n Delete or move them first", "Can't Delete");
                } else {
                    if (classPane.showQuery("Delete?", "Are you sure you want to...")) {
                        project.deleteClass(contents);
                        classPane.treeModel.removeNodeFromParent(container);
                    }
                }
                break;
            case showinstances:
                project.processEventCache();
                if (project.hasInstances(contents)) {
                    InstancesDialog id = classPane.getInstancesDialog();
                    id.setInstances(contents, project);
                    id.setVisible(true);
                } else classPane.showInformation(contents.getLocalName() + " has no instances", "No Instances");
                break;
            default:
                {
                    recordHit();
                }
        }
    }
}
