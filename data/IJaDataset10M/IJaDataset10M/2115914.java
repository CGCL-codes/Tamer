package lug.gui.archetype;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.prefs.Preferences;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import lug.serenity.npc.model.skills.GeneralSkill;
import lug.serenity.npc.random.archetype.Archetype;
import lug.util.RandomFactory;
import luggy.gui.AutoSelectTextFocusListener;
import org.apache.commons.lang.StringUtils;

/**
 * @author Luggy
 */
public class ArchetypeDetailsPanel extends JPanel implements DocumentListener {

    public static final String AUTHOR_KEY = "Author";

    private static final int TEXTFIELD_SIZE = 20;

    private JTextField nameField;

    private JTextField authorField;

    private JTextField descriptionField;

    private Archetype dataModel;

    private Preferences prefs = Preferences.userNodeForPackage(getClass());

    private boolean valueIsAdjusting = false;

    public interface ComponentNames {

        public static final String AUTHOR_FIELD_NAME = "ArchetypeDetailsPanel.authorField";

        public static final String DESCRIPTION_FIELD_NAME = "ArchetypeDetailsPanel.descriptionField";

        public static final String NAME_FIELD_NAME = "ArchetypeDetailsPanel.nameField";
    }

    private List<ChangeListener> nameChangeListeners = new ArrayList<ChangeListener>();

    /**
	 * Add a listener that gets fired whenever the name field changes.
	 * @param listener
	 */
    public void addNameChangeListener(ChangeListener listener) {
        nameChangeListeners.add(listener);
    }

    /**
	 * Remove a listener that gets fired whenever the name field changes.
	 * @param listener
	 */
    public void removeNameChangeListener(ChangeListener listener) {
        nameChangeListeners.remove(listener);
    }

    /**
	 * @param archetype
	 */
    public ArchetypeDetailsPanel(Archetype archetype) {
        super();
        this.dataModel = archetype;
        build();
        populate();
    }

    /**
	 * 
	 */
    private void build() {
        setLayout(new GridBagLayout());
        nameField = new JTextField(TEXTFIELD_SIZE);
        nameField.addFocusListener(new AutoSelectTextFocusListener());
        nameField.setName(ComponentNames.NAME_FIELD_NAME);
        add(new JLabel("Name :"), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 2, 0, 2), 0, 0));
        add(nameField, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 2), 0, 0));
        descriptionField = new JTextField(TEXTFIELD_SIZE);
        descriptionField.addFocusListener(new AutoSelectTextFocusListener());
        add(new JLabel("Description :"), new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 2, 0, 2), 0, 0));
        add(descriptionField, new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 2), 0, 0));
        descriptionField.setName(ComponentNames.DESCRIPTION_FIELD_NAME);
        authorField = new JTextField(TEXTFIELD_SIZE);
        authorField.addFocusListener(new AutoSelectTextFocusListener());
        add(new JLabel("Author :"), new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 2, 0, 2), 0, 0));
        add(authorField, new GridBagConstraints(1, 2, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 2), 0, 0));
        authorField.setName(ComponentNames.AUTHOR_FIELD_NAME);
        add(new JLabel(""), new GridBagConstraints(0, 3, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        nameField.getDocument().addDocumentListener(this);
        authorField.getDocument().addDocumentListener(this);
        descriptionField.getDocument().addDocumentListener(this);
        authorField.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent e) {
                if (!valueIsAdjusting && !StringUtils.isEmpty(authorField.getText())) {
                    prefs.put(AUTHOR_KEY, authorField.getText());
                }
            }
        });
    }

    /**
	 * @return true if the data within this GUI is valid.
	 */
    public boolean isValid() {
        return !StringUtils.isEmpty(nameField.getText()) && !StringUtils.isEmpty(authorField.getText());
    }

    private void populate() {
        valueIsAdjusting = true;
        nameField.setText(dataModel.getName());
        String storedAuthor = prefs.get(AUTHOR_KEY, null);
        if (!StringUtils.isEmpty(dataModel.getAuthor())) {
            authorField.setText(dataModel.getAuthor());
        } else if (!StringUtils.isEmpty(storedAuthor)) {
            authorField.setText(storedAuthor);
        } else {
            authorField.setText(System.getProperty("user.name"));
        }
        descriptionField.setText(dataModel.getDescription());
        valueIsAdjusting = false;
    }

    /**
	 * Class launching point
	 * 
	 * @param args
	 *            command line arguments.
	 */
    public static void main(String[] args) {
        try {
            Archetype arche = new Archetype();
            arche.setName("Name");
            arche.setAuthor("Author");
            arche.setDescription("Description");
            Random r = RandomFactory.getRandom();
            for (GeneralSkill key : GeneralSkill.values()) {
                arche.getGeneralSkillBiases().get(key).getWeighting().setValue(r.nextInt(7) + 1);
            }
            ArchetypeDetailsPanel panel = new ArchetypeDetailsPanel(arche);
            JFrame win = new JFrame("Skill Bias Panel");
            win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            win.add(panel);
            win.setSize(640, 480);
            win.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Notifies interested listeners when the name field changes.
	 */
    private void fireNameChangeListeners() {
        ChangeEvent event = new ChangeEvent(this);
        for (ChangeListener cl : nameChangeListeners) {
            cl.stateChanged(event);
        }
    }

    public void changedUpdate(DocumentEvent e) {
        if (!valueIsAdjusting) {
            populateModelFromUI();
            fireNameChangeListeners();
        }
    }

    public void insertUpdate(DocumentEvent e) {
        if (!valueIsAdjusting) {
            populateModelFromUI();
            fireNameChangeListeners();
        }
    }

    public void removeUpdate(DocumentEvent e) {
        if (!valueIsAdjusting) {
            populateModelFromUI();
            fireNameChangeListeners();
        }
    }

    /**
	 * Populates the data in the UI field into the model
	 */
    private void populateModelFromUI() {
        dataModel.setName(nameField.getText());
        dataModel.setAuthor(authorField.getText());
        dataModel.setDescription(descriptionField.getText());
    }

    /**
	 * @param dataModel2
	 */
    public void setArchetype(Archetype dataModel2) {
        this.dataModel = dataModel2;
        valueIsAdjusting = true;
        nameField.setText(dataModel.getName());
        authorField.setText(dataModel.getAuthor());
        descriptionField.setText(dataModel.getDescription());
        valueIsAdjusting = false;
    }
}
