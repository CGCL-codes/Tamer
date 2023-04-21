package lablog.gui.editors;

import java.awt.Dimension;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import lablog.gui.render.GenericDefaultRenderer;
import lablog.util.GridBagHelper;
import lablog.util.model.GenericComboBoxModel;
import lablog.util.orm.DatabaseHelper;
import lablog.util.orm.auto.Person;
import lablog.util.orm.auto.Setup;
import org.jdesktop.swingx.JXDatePicker;

@SuppressWarnings("serial")
public class SetupEditor extends AbstractEditor<Setup> {

    private JTextField name, id, location;

    private JTextArea description;

    private JXDatePicker createDate;

    private JComboBox creatorCombo, maintainerCombo;

    private GenericComboBoxModel<Person> creatorModel;

    private GenericComboBoxModel<Person> maintainerModel;

    private Setup element;

    public SetupEditor() {
        super(Setup.class, "setup");
    }

    @Override
    public void setElement(Setup element) {
        EntityManager em = null;
        try {
            if (element.getId() != null) {
                em = DatabaseHelper.instance().getEntityManager();
                element = em.merge(element);
                id.setText(element.getId() != null ? element.getId().toString() : "");
                name.setText(element.getName());
                location.setText(element.getLocation());
                createDate.setDate(element.getCreateDate() != null ? element.getCreateDate() : new Date());
                description.setText(element.getDescription());
                Person p = element.getCreator();
                if (p != null) creatorModel.setSelectedItem(p); else creatorModel.setSelectedItem(creatorModel.getElementAt(0));
                p = element.getMaintainer();
                if (p != null) maintainerModel.setSelectedItem(p); else maintainerModel.setSelectedItem(maintainerModel.getElementAt(0));
                this.element = element;
            } else {
                id.setText(null);
                name.setText(null);
                location.setText(null);
                description.setText(null);
                createDate.setDate(new Date());
                creatorModel.setSelectedItem(creatorModel.getElementAt(0));
                maintainerModel.setSelectedItem(maintainerModel.getElementAt(0));
                this.element = element;
            }
        } catch (Exception e) {
            this.element = null;
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    @Override
    public Setup getElement() {
        EntityManager em = null;
        try {
            element.setName(name.getText().equals("") ? null : name.getText());
            element.setLocation(location.getText().equals("") ? null : location.getText());
            element.setDescription(description.getText().equals("") ? null : description.getText());
            element.setCreateDate(createDate.getDate());
            element.setCreator(creatorModel.getTypedSelectedItem());
            element.setMaintainer(maintainerModel.getTypedSelectedItem());
        } catch (Exception e) {
            element = null;
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        return element;
    }

    @Override
    protected void initGUI() {
        GridBagHelper gbh = new GridBagHelper(this);
        gbh.add(new JLabel("ID"), 0, 0);
        id = new JTextField();
        id.setEnabled(false);
        gbh.add(id, 0, 1, 2, 0.0);
        gbh.add(new JLabel("Name"), 0, 2);
        gbh.add(name = new JTextField(), 0, 3, 2, 0.0);
        gbh.add(new JLabel("Location"), 0, 4);
        gbh.add(location = new JTextField(), 0, 5, 2, 0.0);
        gbh.add(new JLabel("Create Date"), 0, 6);
        gbh.add(createDate = new JXDatePicker(), 0, 7, 2, 0.0);
        gbh.add(new JLabel("Creator"), 0, 8);
        creatorModel = new GenericComboBoxModel<Person>(Person.class, GenericComboBoxModel.ADD_OTHER_ENTRY);
        creatorCombo = new JComboBox(creatorModel);
        creatorCombo.setRenderer(new GenericDefaultRenderer());
        gbh.add(creatorCombo, 0, 9, 2, 0.0);
        gbh.add(new JLabel("Maintainer"), 0, 9);
        maintainerModel = new GenericComboBoxModel<Person>(Person.class, GenericComboBoxModel.ADD_OTHER_ENTRY);
        maintainerCombo = new JComboBox(maintainerModel);
        maintainerCombo.setRenderer(new GenericDefaultRenderer());
        gbh.add(maintainerCombo, 0, 10, 2, 0.0);
        gbh.add(new JLabel(""), 0, 11, 2, 1, 0.0, 1.0);
        gbh.add(new JLabel("Description"), 2, 0);
        description = new JTextArea();
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(description, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gbh.add(descriptionScroll, 2, 1, 2, 12, 1.0, 1.0);
        descriptionScroll.setPreferredSize(description.getPreferredScrollableViewportSize());
        setPreferredSize(new Dimension(530, 400));
    }
}
