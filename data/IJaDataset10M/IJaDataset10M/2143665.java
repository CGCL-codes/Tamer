package gui.dutsForms;

import gui.xmlClasses.Text;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import domain.core.GenericStudyFacade;

/** 
 * @author Gilles for FARS Design
 */
public class SubjForm extends DUTSStudyForm {

    private JPanel northPanel;

    private JPanel southPanel;

    private JPanel easternSouthPanel;

    private JScrollPane jScrollPane1;

    private JLabel descriptionLabel;

    private JTextField coeffTextField;

    private JLabel coeffLabel;

    private JTextField aliasTextField;

    private JTextField nameTextField;

    private JLabel aliasLabel;

    private JLabel nameLabel;

    private JTextArea descriptionTextArea;

    private JPanel westernSouthPanel;

    /** 
	 * the currently selected department. 
	 */
    private String currentDep;

    /** 
	 * the currently selected YOS.
	 */
    private String currentYOS;

    /** 
	 * the currently selected semester
	 */
    private String currentSem;

    /**
	 * the currently selected teaching unit.
	 * the displayed subjects are the ones of this teaching unit
	 */
    private String currentTU;

    /** 
	 * the currently selected subject
	 */
    private String currentSubject;

    public SubjForm(Text dUTSLabels, GenericStudyFacade studyFacade) {
        super(dUTSLabels, studyFacade);
        initGUI();
    }

    @Override
    public void filterBy(Object f, Map<String, String> criteria) {
        if (criteria.size() != 1) {
            throw new IllegalArgumentException("Invalid Number of values in criteria");
        } else if (!criteria.containsKey(Filtrable.YOS_CRITERIA_KEY)) {
            throw new IllegalArgumentException("Missing key " + "Filtrable.YOS_CRITERIA_KEY " + "in criteria");
        }
        this.currentYOS = criteria.get(Filtrable.YOS_CRITERIA_KEY);
        this.populate();
    }

    @Override
    public void clearFields() {
        super.clearFields();
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void populate() {
    }

    @Override
    public void update() {
        super.update();
    }

    /**
	 * initializes the graphic components of this form
	 */
    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            this.setLayout(thisLayout);
            setPreferredSize(new Dimension(400, 300));
            {
                northPanel = new JPanel();
                GroupLayout topPanelLayout = new GroupLayout((JComponent) northPanel);
                northPanel.setLayout(topPanelLayout);
                this.add(northPanel, BorderLayout.NORTH);
                northPanel.setPreferredSize(new java.awt.Dimension(400, 55));
                {
                    aliasTextField = new JTextField();
                }
                {
                    nameTextField = new JTextField();
                }
                {
                    aliasLabel = new JLabel();
                    aliasLabel.setText("Alias");
                    aliasLabel.setSize(34, 21);
                    aliasLabel.setFont(new java.awt.Font("Tahoma", 0, 12));
                }
                {
                    nameLabel = new JLabel();
                    nameLabel.setText("Nom");
                    nameLabel.setSize(55, 21);
                    nameLabel.setFont(new java.awt.Font("Tahoma", 0, 12));
                }
                {
                    coeffLabel = new JLabel();
                    coeffLabel.setText("Coefficient");
                    coeffLabel.setFont(new java.awt.Font("Tahoma", 0, 12));
                    coeffLabel.setSize(81, 21);
                }
                {
                    coeffTextField = new JTextField();
                    coeffTextField.setSize(109, 21);
                }
                topPanelLayout.setHorizontalGroup(topPanelLayout.createSequentialGroup().addGap(7).addGroup(topPanelLayout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, topPanelLayout.createSequentialGroup().addComponent(aliasLabel, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE).addGap(21)).addComponent(nameLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(topPanelLayout.createParallelGroup().addComponent(aliasTextField, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE).addComponent(nameTextField, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)).addGap(31).addComponent(coeffLabel, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(coeffTextField, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE).addContainerGap(117, Short.MAX_VALUE));
                topPanelLayout.setVerticalGroup(topPanelLayout.createSequentialGroup().addGap(8).addGroup(topPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nameTextField, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(nameLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(coeffLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE).addComponent(coeffTextField, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(topPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(aliasTextField, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(aliasLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
            }
            {
                southPanel = new JPanel();
                BorderLayout bottomPanelLayout = new BorderLayout();
                southPanel.setLayout(bottomPanelLayout);
                this.add(southPanel, BorderLayout.CENTER);
                {
                    westernSouthPanel = new JPanel();
                    southPanel.add(westernSouthPanel, BorderLayout.WEST);
                    westernSouthPanel.setPreferredSize(new java.awt.Dimension(80, 174));
                    {
                        descriptionLabel = new JLabel();
                        westernSouthPanel.add(descriptionLabel);
                        descriptionLabel.setText("Description");
                        descriptionLabel.setSize(80, 21);
                        descriptionLabel.setPreferredSize(new java.awt.Dimension(65, 14));
                        descriptionLabel.setFont(new java.awt.Font("Tahoma", 0, 12));
                    }
                }
                {
                    easternSouthPanel = new JPanel();
                    BorderLayout easternSouthPanelLayout = new BorderLayout();
                    easternSouthPanel.setLayout(easternSouthPanelLayout);
                    southPanel.add(easternSouthPanel, BorderLayout.CENTER);
                    easternSouthPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
                    {
                        jScrollPane1 = new JScrollPane();
                        easternSouthPanel.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(299, 169));
                        {
                            descriptionTextArea = new JTextArea();
                            jScrollPane1.setViewportView(descriptionTextArea);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
