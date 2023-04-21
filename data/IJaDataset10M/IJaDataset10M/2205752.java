package unbbayes.aprendizagem;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import unbbayes.prs.bn.LearningNode;

/**
 * Class which builds a screen showing a simbol representing a variable, a list
 * with possible states of that variable, and allows the user to change the
 * maximum number of parents of a variable.
 * @author Danilo Custódio da Silva
 * @version 1.0
 */
public class OptionsWindow extends JDialog {

    /** Serialization runtime version number */
    private static final long serialVersionUID = 0;

    private LearningNode variable;

    private JTextField txtMaxParents;

    private JTextField txtName;

    private JLabel lMaxParents;

    private JLabel lName;

    private JLabel lStates;

    private JPanel centerPanel;

    private JPanel northPanel;

    private JPanel namePanel;

    private JPanel maxPanel;

    private JPanel repetitionPanel;

    private JPanel statesPanel;

    private JList statesList;

    private DefaultListModel statesListModel;

    private JButton applyButton;

    private JButton cancelButton;

    private OptionsInterationController optionsController;

    /**
     * Constructor method of the screen. This screen has 2 main panels, pPainelCentro which
     * contains the panels with a list of states and buttons, and pPainelNorte which
     * contains a simbol of that variable and the maximum count of parents.
     * @param variable - The variables which we wish to obtain the informations.
     * (<code>LearningNode<code>)
     * @see LearningNode
     * @see JPanel
     * @see Container
     */
    public OptionsWindow(LearningNode variable) {
        super(new Frame(), "UnBBayes - Learning Module", true);
        this.variable = variable;
        Container container = getContentPane();
        container.add(getNorthPanel(), BorderLayout.NORTH);
        container.add(getCenterPanel(), BorderLayout.CENTER);
        optionsController = new OptionsInterationController(this, variable);
        pack();
        setResizable(false);
        setVisible(true);
    }

    private JPanel getNorthPanel() {
        northPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        northPanel.add(getNamePanel());
        northPanel.add(getMaxPanel());
        return northPanel;
    }

    private JPanel getCenterPanel() {
        centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.add(getStatesPanel());
        centerPanel.add(getRepetitionPanel());
        return centerPanel;
    }

    private JPanel getNamePanel() {
        namePanel = new JPanel(new GridLayout(1, 2, 10, 10));
        lName = new JLabel("          Nome");
        txtName = new JTextField(variable.getName());
        txtName.setEnabled(false);
        namePanel.add(lName);
        namePanel.add(txtName);
        return namePanel;
    }

    private JPanel getMaxPanel() {
        maxPanel = new JPanel(new GridLayout(1, 2));
        lMaxParents = new JLabel("Max :");
        txtMaxParents = new JTextField("" + variable.getNumeroMaximoPais());
        maxPanel.add(lMaxParents);
        maxPanel.add(txtMaxParents);
        return maxPanel;
    }

    private JPanel getStatesPanel() {
        statesPanel = new JPanel(new BorderLayout());
        lStates = new JLabel("States");
        statesListModel = new DefaultListModel();
        statesList = new JList(statesListModel);
        JScrollPane statesSP = new JScrollPane(statesList);
        statesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        java.util.List auxVector = (java.util.List) variable.getEstados();
        for (int j = 0; j < auxVector.size(); j++) {
            String auxState = (String) auxVector.get(j);
            statesListModel.addElement(auxState);
        }
        statesPanel.add(statesSP, BorderLayout.CENTER);
        statesPanel.add(lStates, BorderLayout.NORTH);
        return statesPanel;
    }

    private JPanel getRepetitionPanel() {
        repetitionPanel = new JPanel(new GridLayout(7, 1, 5, 5));
        applyButton = new JButton("Apply");
        cancelButton = new JButton("Cancel");
        repetitionPanel.add(new JLabel(""));
        repetitionPanel.add(new JLabel(""));
        repetitionPanel.add(new JLabel(""));
        repetitionPanel.add(applyButton);
        repetitionPanel.add(cancelButton);
        repetitionPanel.add(new JLabel(""));
        repetitionPanel.add(new JLabel(""));
        applyButton.addActionListener(applyEvent);
        cancelButton.addActionListener(cancelEvent);
        return repetitionPanel;
    }

    ActionListener applyEvent = new ActionListener() {

        public void actionPerformed(ActionEvent ae) {
            optionsController.applyEvent(txtMaxParents.getText());
        }
    };

    ActionListener cancelEvent = new ActionListener() {

        public void actionPerformed(ActionEvent ae) {
            optionsController.cancelEvent();
        }
    };
}
