package agentgui.envModel.graph.controller;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import agentgui.core.agents.AgentClassElement4SimStart;
import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.ontologies.gui.OntologyInstanceViewer;
import agentgui.core.project.Project;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;

/**
 * GUI component for setting the properties of an ontology object representing the domain view of a grid component
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 *
 */
public class OntologySettingsDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1745551171293051322L;

    private Project project = null;

    private GraphEnvironmentController graphController = null;

    private Object element = null;

    private JPanel jPanelContent = null;

    private JButton jButtonApply = null;

    private JButton jButtonAbort = null;

    /** The OntologyInstanceViewer instance used for editing ontology objects */
    private OntologyInstanceViewer oiv = null;

    /**
	 * Constructor.
	 * @param project The simulation project
	 * @param controller the GraphEnvironmentController
	 * @param element The GraphElement containing the ontology object
	 */
    public OntologySettingsDialog(Project project, GraphEnvironmentController controller, Object element) {
        super(Application.MainWindow, Dialog.ModalityType.APPLICATION_MODAL);
        this.project = project;
        this.graphController = controller;
        this.element = element;
        initialize();
    }

    /**
	 * This method initializes this
	 */
    private void initialize() {
        this.setContentPane(getJPanelContent());
        if (element instanceof NetworkComponent) {
            this.setTitle("NetworkComponent " + ((NetworkComponent) element).getId());
        } else if (element instanceof GraphNode) {
            this.setTitle("PropagationPoint " + ((GraphNode) element).getId());
        }
        this.setSize(new Dimension(450, 450));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(getJButtonApply())) {
            oiv.save();
            if (element instanceof GraphNode) {
                ((GraphNode) element).setEncodedOntologyRepresentation(oiv.getConfigurationXML64()[0]);
            } else if (element instanceof NetworkComponent) {
                ((NetworkComponent) element).setEncodedOntologyRepresentation(oiv.getConfigurationXML64()[0]);
                DefaultListModel agents2Start = graphController.getAgents2Start();
                for (int i = 0; i < agents2Start.size(); i++) {
                    AgentClassElement4SimStart ac4s = (AgentClassElement4SimStart) agents2Start.get(i);
                    if (ac4s.getStartAsName().equals(((NetworkComponent) element).getId())) {
                        ac4s.setStartArguments(oiv.getConfigurationXML());
                        break;
                    }
                }
            }
            this.dispose();
        } else if (e.getSource().equals(getJButtonAbort())) {
            this.dispose();
        }
    }

    /**
	 * This method initializes jPanelContent	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanelContent() {
        if (jPanelContent == null) {
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = GridBagConstraints.BOTH;
            gridBagConstraints11.gridy = 0;
            gridBagConstraints11.weightx = 1.0;
            gridBagConstraints11.weighty = 1.0;
            gridBagConstraints11.gridwidth = 2;
            gridBagConstraints11.gridx = 0;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints1.gridy = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints.weightx = 1.0D;
            gridBagConstraints.anchor = GridBagConstraints.EAST;
            gridBagConstraints.gridy = 1;
            jPanelContent = new JPanel();
            jPanelContent.setLayout(new GridBagLayout());
            jPanelContent.add(getJButtonApply(), gridBagConstraints);
            jPanelContent.add(getJButtonAbort(), gridBagConstraints1);
            OntologyInstanceViewer oiv = getOIV();
            if (oiv == null) {
                jPanelContent.remove(this.getJButtonApply());
            } else {
                jPanelContent.add(oiv, gridBagConstraints11);
            }
        }
        return jPanelContent;
    }

    /**
	 * Creates a new OntologyInstancViewer instance and initiates it with the currently selected node / component
	 * @return
	 */
    private OntologyInstanceViewer getOIV() {
        if (element instanceof NetworkComponent) {
            NetworkComponent elemNetComp = (NetworkComponent) element;
            oiv = new OntologyInstanceViewer(project, this.graphController.getComponentTypeSettings().get(((NetworkComponent) element).getType()).getAgentClass());
            if (elemNetComp.getEncodedOntologyRepresentation() != null) {
                String[] encodedOntoRepresentation = new String[1];
                encodedOntoRepresentation[0] = elemNetComp.getEncodedOntologyRepresentation();
                oiv.setConfigurationXML64(encodedOntoRepresentation);
            }
        } else if (element instanceof GraphNode) {
            String[] ontoClassName = new String[1];
            ontoClassName[0] = this.graphController.getComponentTypeSettings().get("node").getAgentClass();
            if (ontoClassName[0] == null || ontoClassName[0].equals("")) {
                return null;
            }
            oiv = new OntologyInstanceViewer(project, ontoClassName);
            if (((GraphNode) element).getEncodedOntologyRepresentation() != null) {
                String[] encodedOntoRepresentation = new String[1];
                encodedOntoRepresentation[0] = ((GraphNode) element).getEncodedOntologyRepresentation();
                oiv.setConfigurationXML64(encodedOntoRepresentation);
            }
        }
        oiv.setAllowViewEnlargement(false);
        return oiv;
    }

    /**
	 * This method initializes jButtonApply	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonApply() {
        if (jButtonApply == null) {
            jButtonApply = new JButton();
            jButtonApply.setText("�bernehmen");
            jButtonApply.setText(Language.translate(jButtonApply.getText()));
            jButtonApply.setFont(new Font("Dialog", Font.BOLD, 12));
            jButtonApply.setForeground(new Color(0, 153, 0));
            jButtonApply.addActionListener(this);
        }
        return jButtonApply;
    }

    /**
	 * This method initializes jButtonAbort	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonAbort() {
        if (jButtonAbort == null) {
            jButtonAbort = new JButton();
            jButtonAbort.setText("Abbrechen");
            jButtonAbort.setText(Language.translate(jButtonAbort.getText()));
            jButtonAbort.setForeground(new Color(153, 0, 0));
            jButtonAbort.setFont(new Font("Dialog", Font.BOLD, 12));
            jButtonAbort.addActionListener(this);
        }
        return jButtonAbort;
    }
}
