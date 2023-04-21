package se.kth.speech.skatta.designer;

import net.miginfocom.swing.MigLayout;
import se.kth.speech.skatta.util.ExtendedElement;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

class DropfieldForm extends QuestionForm implements DocumentListener {

    /**
     *
     */
    private static final long serialVersionUID = 5084383641729470879L;

    private JTextField m_nameField, m_backgroundField, m_leftField, m_rightField, m_topField, m_bottomField;

    private JLabel m_listComponent;

    public DropfieldForm(Designer d) {
        super(d);
        JLabel nameLabel = new JLabel("Name:");
        JLabel backgroundLabel = new JLabel("Background:");
        JLabel leftLabel = new JLabel("Left label:");
        JLabel rightLabel = new JLabel("Right label:");
        JLabel topLabel = new JLabel("Top label:");
        JLabel bottomLabel = new JLabel("Bottom label:");
        m_nameField = new JTextField("");
        m_nameField.setToolTipText("This name will identify the dropfield in the resultfile X and Y will be appended to the name to specify the axis.");
        m_backgroundField = new JTextField("#707070");
        m_backgroundField.setToolTipText("This can be a hexagonal colorcode on the form #RRGGBB or the path to an image that will be placed in the background of the dropfield");
        m_leftField = new JTextField("");
        m_leftField.setToolTipText("A label that will be displayed at the left of the dropfield");
        m_rightField = new JTextField("");
        m_rightField.setToolTipText("A label that will be displayed at the right of the dropfield");
        m_topField = new JTextField("");
        m_topField.setToolTipText("A label that will be displayed at the top of the dropfield");
        m_bottomField = new JTextField("");
        m_bottomField.setToolTipText("A label that will be displayed at the bottom of the dropfield");
        m_listComponent = new JLabel(getName());
        m_nameField.getDocument().addDocumentListener(this);
        setLayout(new MigLayout("fillx, insets 0, wrap 2", "[][fill, sg, grow]", "[align baseline, grow 0]"));
        add(nameLabel);
        add(m_nameField);
        add(backgroundLabel);
        add(m_backgroundField);
        add(leftLabel);
        add(m_leftField);
        add(rightLabel);
        add(m_rightField);
        add(topLabel);
        add(m_topField);
        add(bottomLabel);
        add(m_bottomField);
    }

    public void updateLocked() {
    }

    public JLabel getListComponent() {
        return m_listComponent;
    }

    public String getName() {
        String name = m_nameField.getText();
        if (name.length() == 0) return "<Unnamed Dropfield>";
        return name;
    }

    public void changedUpdate(DocumentEvent e) {
    }

    public void insertUpdate(DocumentEvent e) {
        m_listComponent.setText(getName());
        if (m_listComponent.getParent() != null && m_listComponent.getParent().getParent() != null && m_listComponent.getParent().getParent().getParent() != null) m_listComponent.getParent().getParent().getParent().repaint();
    }

    public void removeUpdate(DocumentEvent e) {
        insertUpdate(e);
    }

    public ExtendedElement save(ExtendedElement parent) {
        ExtendedElement e = parent.createChildWithName("dropfield");
        e.setAttribute("name", m_nameField.getText());
        e.setAttribute("background", m_backgroundField.getText());
        if (m_leftField.getText().length() != 0) e.setAttribute("left", m_leftField.getText());
        if (m_rightField.getText().length() != 0) e.setAttribute("right", m_rightField.getText());
        if (m_topField.getText().length() != 0) e.setAttribute("top", m_topField.getText());
        if (m_bottomField.getText().length() != 0) e.setAttribute("bottom", m_bottomField.getText());
        return e;
    }

    public void load(ExtendedElement e) {
        m_nameField.setText(e.attribute("name"));
        m_backgroundField.setText(e.attribute("background"));
        m_leftField.setText(e.attribute("left"));
        m_rightField.setText(e.attribute("right"));
        m_topField.setText(e.attribute("top"));
        m_bottomField.setText(e.attribute("bottom"));
    }
}
