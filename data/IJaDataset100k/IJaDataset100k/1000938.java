package gui.InfoHandler;

import javax.swing.*;
import java.awt.*;
import common.Configuration;
import phonebook.entry.Location;
import pim.PhonesTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import common.LocationCellRenderer;

public class AddNumberDialog extends JDialog implements ActionListener {

    JPanel jPanel1 = new JPanel();

    GridBagLayout gridBagLayout1 = new GridBagLayout();

    JLabel NumberLbl = new JLabel();

    JTextField NumberTf = new JTextField(20);

    JLabel LocationLbl = new JLabel();

    JComboBox LocationBox = new JComboBox(Location.getAvailableLocations());

    JButton CancelBtn = new JButton();

    JButton OkBtn = new JButton();

    public static final int WIDTH = 305;

    public static final int HEIGHT = 140;

    private PhonesTableModel m_model;

    public AddNumberDialog(PhonesTableModel m) {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        applyComponentOrientation(Configuration.getInstance().getComponentOrientation());
        setSize(AddNumberDialog.WIDTH, AddNumberDialog.HEIGHT);
        setLocationRelativeTo(null);
        m_model = m;
        setTitle(Configuration.messages.getString("IHVAddNumberTl"));
    }

    private void jbInit() throws Exception {
        OkBtn.setText(Configuration.messages.getString("IADAddBtn"));
        CancelBtn.setText(Configuration.messages.getString("IADCloseBtn"));
        OkBtn.addActionListener(this);
        CancelBtn.addActionListener(this);
        LocationLbl.setText(Configuration.messages.getString("ICDLocationLbl"));
        LocationBox.setRenderer(new LocationCellRenderer());
        NumberTf.setMinimumSize(new Dimension(224, 17));
        NumberTf.setPreferredSize(new Dimension(124, 17));
        NumberTf.setText("");
        NumberTf.setColumns(20);
        NumberLbl.setText(Configuration.messages.getString("ICDNumberLbl"));
        jPanel1.setMaximumSize(new Dimension(32767, 32767));
        jPanel1.setLayout(gridBagLayout1);
        this.getContentPane().add(jPanel1, BorderLayout.CENTER);
        jPanel1.add(NumberLbl, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(NumberTf, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 2, 5, 2), 0, 0));
        jPanel1.add(LocationLbl, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(LocationBox, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        jPanel1.add(OkBtn, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(29, 7, 5, 20), 0, 0));
        jPanel1.add(CancelBtn, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(27, 20, 7, 7), 0, 0));
    }

    /**
   * actionPerformed
   *
   * @param e ActionEvent
   */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(OkBtn.getText())) {
            String number = NumberTf.getText();
            Location loc = (Location) LocationBox.getSelectedItem();
            m_model.insertRow(number, loc);
            dispose();
        } else if (e.getActionCommand().equals(CancelBtn.getText())) {
            hide();
            dispose();
        }
    }
}
