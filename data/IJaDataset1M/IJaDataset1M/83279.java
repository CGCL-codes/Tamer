package lablog.gui.editors;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import lablog.gui.render.GenericDefaultRenderer;
import lablog.util.GridBagHelper;
import lablog.util.model.GenericComboBoxModel;
import lablog.util.orm.auto.Experiment;
import lablog.util.orm.auto.ExperimentType;
import lablog.util.orm.auto.Person;
import lablog.util.orm.auto.Project;
import lablog.util.orm.auto.Setup;
import org.jdesktop.swingx.JXDatePicker;

@SuppressWarnings("serial")
public class ExperimentEditor extends AbstractEditor<Experiment> {

    private Experiment element;

    private GenericComboBoxModel<Person> authorModel;

    private GenericComboBoxModel<Setup> setupModel;

    private GenericComboBoxModel<Project> projectModel;

    private GenericComboBoxModel<ExperimentType> expTypeModel;

    private JComboBox authorCombo, projectCombo, expTypeCombo, setupCombo;

    private JXDatePicker createDate;

    private JLabel id;

    private JTextField name;

    private JTextArea text;

    public ExperimentEditor() {
        super(Experiment.class, "Edit document");
        initGUI();
    }

    @Override
    protected void initGUI() {
        JPanel editorPanel = new JPanel();
        GridBagHelper gbHelper = new GridBagHelper(editorPanel);
        id = new JLabel("");
        gbHelper.add(new JLabel("ID"), 0, 0);
        gbHelper.add(id, 1, 0, 2, 1.0);
        name = new JTextField();
        gbHelper.add(new JLabel("Name"), 0, 1);
        gbHelper.add(name, 1, 1, 2, 1.0);
        expTypeModel = new GenericComboBoxModel<ExperimentType>(ExperimentType.class);
        expTypeCombo = new JComboBox(expTypeModel);
        expTypeCombo.setRenderer(new GenericDefaultRenderer());
        gbHelper.add(new JLabel("Type"), 0, 2);
        gbHelper.add(expTypeCombo, 1, 2, 2, 1.0);
        gbHelper.add(new JLabel("Create date"), 0, 3);
        createDate = new JXDatePicker();
        gbHelper.add(createDate, 1, 3, 2, 1.0);
        authorModel = new GenericComboBoxModel<Person>(Person.class, GenericComboBoxModel.ADD_NO_ENTRY);
        authorCombo = new JComboBox(authorModel);
        authorCombo.setPrototypeDisplayValue(".................");
        authorCombo.setRenderer(new GenericDefaultRenderer());
        gbHelper.add(new JLabel("Author"), 0, 4);
        gbHelper.add(authorCombo, 1, 4, 2, 1.0);
        projectModel = new GenericComboBoxModel<Project>(Project.class, GenericComboBoxModel.ADD_NO_ENTRY);
        projectCombo = new JComboBox(projectModel);
        projectCombo.setPrototypeDisplayValue("............................	");
        projectCombo.setRenderer(new GenericDefaultRenderer());
        gbHelper.add(new JLabel("Project"), 0, 5);
        gbHelper.add(projectCombo, 1, 5, 2, 1.0);
        setupModel = new GenericComboBoxModel<Setup>(Setup.class, GenericComboBoxModel.ADD_NO_ENTRY);
        setupCombo = new JComboBox(setupModel);
        setupCombo.setPrototypeDisplayValue("............................	");
        setupCombo.setRenderer(new GenericDefaultRenderer());
        gbHelper.add(new JLabel("Setup"), 0, 6);
        gbHelper.add(setupCombo, 1, 6, 2, 1.0);
        gbHelper.add(new JLabel("Text"), 0, 7);
        text = new JTextArea("");
        text.setWrapStyleWord(true);
        text.setLineWrap(true);
        gbHelper.add(new JScrollPane(text), 0, 8, 3, 2, 1.0, 0.75);
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(400, 500));
        this.add(editorPanel, BorderLayout.CENTER);
    }

    @Override
    public void setElement(Experiment element) {
        this.element = element;
        if (element != null) {
            if (element.getId() != null) id.setText(Integer.toString(element.getId())); else id.setText("");
            name.setText(element.getName());
            authorModel.setSelectedItem(element.getPerson());
            projectModel.setSelectedItem(element.getProject());
            expTypeModel.setSelectedItem(element.getExperimentType());
            setupModel.setSelectedItem(element.getSetup());
            text.setText(element.getText());
            createDate.setDate(element.getDate());
        } else {
            id.setText("");
            name.setText("");
            authorModel.setSelectedItem(null);
            projectModel.setSelectedItem(null);
            expTypeModel.setSelectedItem(null);
            setupModel.setSelectedItem(null);
            text.setText("");
            createDate.setDate(null);
        }
    }

    @Override
    public Experiment getElement() {
        element.setName(name.getText().equals("") ? null : name.getText());
        element.setText(text.getText().equals("") ? null : text.getText());
        element.setDate(createDate.getDate());
        element.setPerson(authorModel.getTypedSelectedItem());
        element.setProject(projectModel.getTypedSelectedItem());
        element.setExperimentType(expTypeModel.getTypedSelectedItem());
        element.setSetup(setupModel.getTypedSelectedItem());
        return element;
    }
}
