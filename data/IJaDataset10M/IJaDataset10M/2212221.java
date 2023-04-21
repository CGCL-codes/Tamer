package net.sourceforge.mapcraft.editor.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import net.sourceforge.mapcraft.map.*;
import net.sourceforge.mapcraft.map.elements.Area;

/**
 * Dialog for creating or editing a named area.
 * 
 * @author Samuel Penn
 */
public class AreaDialog extends JDialog {

    private Area area;

    private AreaSet set;

    private JPanel topPane;

    private JPanel bottomPane;

    private boolean isOkay = false;

    private JButton okay, cancel;

    private JTextField name, uri;

    private JComboBox parent;

    /**
     * Helper method to add a component to a GridBagLayout.
     * 
     * @param con       Container to add to.
     * @param g         GridBagLayout of container.
     * @param c         Constraints of the GridBagLayout.
     * @param cmp       Component to be added.
     * @param x         X coordinate to position component.
     * @param y         Y coordinate to position component.
     * @param w         Width of component.
     * @param h         Height of component.
     */
    private void add(Container con, GridBagLayout g, GridBagConstraints c, Component cmp, int x, int y, int w, int h) {
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = w;
        c.gridheight = h;
        g.setConstraints(cmp, c);
        con.add(cmp);
    }

    private void setupTopPane() {
        GridBagLayout g = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        topPane.setLayout(g);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.NORTH;
        add(topPane, g, c, new JLabel("Area name"), 0, 0, 1, 1);
        add(topPane, g, c, new JLabel("Parent"), 0, 1, 1, 1);
        add(topPane, g, c, new JLabel("uri"), 0, 2, 1, 1);
        add(topPane, g, c, name = new JTextField(area.getName()), 1, 0, 2, 1);
        add(topPane, g, c, uri = new JTextField(area.getUri()), 1, 2, 2, 1);
        String[] array = set.toNameArray();
        ArrayList list = new ArrayList();
        list.add("");
        for (int i = 0; i < array.length; i++) {
            if (!array[i].equals(area.getName())) {
                list.add(array[i]);
            }
        }
        int selection = 0;
        String parentName = "";
        if (area.getParent() != null) {
            parentName = area.getParent().getName();
        }
        for (int i = 0; i < list.size(); i++) {
            String n = (String) list.get(i);
            if (n.equals(parentName)) {
                selection = i;
                break;
            }
        }
        parent = new JComboBox(list.toArray());
        parent.setSelectedIndex(selection);
        add(topPane, g, c, parent, 1, 1, 2, 1);
    }

    private void setupBottomPane() {
        bottomPane.add(okay = new JButton("Apply"));
        bottomPane.add(cancel = new JButton("Cancel"));
        okay.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okay();
            }
        });
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                isOkay = false;
                setVisible(false);
            }
        });
    }

    /**
     * Create a new dialog displaying information on an area.
     * The user is able to edit this information as they see fit.
     * 
     * @param area      Area to be displayed.
     * @param set       Set of all areas known to the map.
     * @param frame     Parent frame.
     */
    AreaDialog(Area area, AreaSet set, JFrame frame) {
        super(frame, area.getName() + " (" + area.getId() + ")", true);
        this.area = area;
        this.set = set;
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPane = new JPanel(), BorderLayout.NORTH);
        getContentPane().add(bottomPane = new JPanel(), BorderLayout.SOUTH);
        setupTopPane();
        setupBottomPane();
        setSize(new Dimension(300, 150));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public String getName() {
        return name.getText();
    }

    public String getUri() {
        return uri.getText();
    }

    public String getParentName() {
        return (String) parent.getSelectedItem();
    }

    /**
     * User clicked 'Okay', hide dialog and return to caller.
     */
    public void okay() {
        isOkay = true;
        setVisible(false);
    }

    /**
     * Return true if user clicked 'Okay', false otherwise.
     */
    public boolean isOkay() {
        return isOkay;
    }
}
