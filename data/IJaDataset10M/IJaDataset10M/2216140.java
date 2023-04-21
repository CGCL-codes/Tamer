package edu.whitman.halfway.jigs.gui.shared;

import edu.whitman.halfway.jigs.*;
import org.apache.log4j.Logger;
import java.awt.*;
import java.beans.*;
import javax.swing.*;

public class CategoryDialog extends JDialog implements PropertyChangeListener {

    protected Logger log = Logger.getLogger(CategoryDialog.class.getName());

    protected Category category;

    protected JCheckBox visible;

    protected JTextField name;

    protected JOptionPane optionPane;

    public CategoryDialog(Component comp) {
        super(JOptionPane.getFrameForComponent(comp), "Edit Category Attributes", true);
        Font menuFont = new Font(null, Font.PLAIN, 12);
        JPanel p = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        p.setLayout(gridbag);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;
        c.weightx = 0;
        c.insets = new Insets(2, 2, 2, 2);
        gridAdd(p, newLabel("Category Name", menuFont), 0, 0, 1, c, gridbag);
        c.weightx = 0.5;
        name = new JTextField(40);
        gridAdd(p, name, 1, 0, 3, c, gridbag);
        Object[] options = { "Cancel", "OK" };
        optionPane = new JOptionPane(p, JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null, options, options[1]);
        optionPane.addPropertyChangeListener(this);
        setContentPane(optionPane);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.pack();
        this.setSize(new Dimension(300, 170));
    }

    protected JLabel newLabel(String s, Font font) {
        JLabel jl = new JLabel(s);
        jl.setVerticalAlignment(JLabel.TOP);
        jl.setForeground(Color.black);
        jl.setFont(font);
        return jl;
    }

    protected void gridAdd(Container root, Component obj, int x, int y, int width, GridBagConstraints c, GridBagLayout gridbag) {
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = width;
        gridbag.setConstraints(obj, c);
        root.add(obj);
    }

    public void show(Category obj) {
        this.category = obj;
        name.setText(obj.getName());
        setVisible(true);
    }

    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (isVisible() && (e.getSource() == optionPane) && (prop.equals(JOptionPane.VALUE_PROPERTY) || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
            Object value = optionPane.getValue();
            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                return;
            }
            optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
            if (value.equals("OK")) {
                if (!validTextValue(name, "Name")) return; else category.setName(name.getText().trim());
            } else {
                category = null;
            }
            setVisible(false);
        }
    }

    public Category getCategory() {
        return category;
    }

    protected boolean validTextValue(JTextField obj, String name) {
        if (obj.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Invalid Name in " + name + " Field.", "Error", JOptionPane.ERROR_MESSAGE);
            obj.selectAll();
            obj.requestFocus();
            return false;
        }
        return true;
    }
}
