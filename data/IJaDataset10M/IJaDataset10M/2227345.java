package location;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import dialog.WarningDialog;

/**
 *  Class to open a load-applet dialog window. The dialog is used for
 *  loading simulated JavaCard applets into the system.
 *
 *  @author Henrik Eriksson
 *  @version 0.02
 *
 *  @see Farmyard
 */
public class LoadAppletDialog extends JDialog {

    /**
   *  The name field.
   *  @serial
   */
    private JTextField applet_name_field = new JTextField(20);

    private static byte button_id_counter = 0;

    /**
   *  Constructs a load-applet dialog box. The dialog window will be created
   *  but not opened. Use the method <code>show()</code> to open and
   *  run the dialog.
   *
   *  @param parent the parent window
   *  @param farm the farm yard
   */
    public LoadAppletDialog(final JFrame parent, final Farmyard farm) {
        super(parent, "Load Simulated JavaCard Applet", true);
        getContentPane().setLayout(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        JLabel an = new JLabel("Applet name:");
        c.gridwidth = 1;
        c.ipadx = 5;
        getContentPane().add(an, c);
        c.ipadx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;
        applet_name_field.setText("Animal");
        applet_name_field.selectAll();
        getContentPane().add(applet_name_field, c);
        final JButton load = new JButton("Load");
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;
        c.insets = new Insets(25, 0, 0, 0);
        load.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final String applet_name = applet_name_field.getText();
                if (applet_name.equals("")) {
                    WarningDialog w = new WarningDialog(parent, "Empty name. You must provide a name for the applet.");
                    w.setVisible(true);
                    return;
                }
                try {
                    Class applet_class = Class.forName(applet_name);
                    Class cl = applet_class.getSuperclass();
                    while (cl != null && !cl.getName().equals("GenericAnimal")) cl = cl.getSuperclass();
                    if (cl == null) {
                        WarningDialog w = new WarningDialog(parent, "The animal applet must be subclass of GenericAnimal.");
                        w.setVisible(true);
                        return;
                    }
                } catch (ClassNotFoundException ex) {
                    Farmyard.playSound("resources/failure.au");
                    WarningDialog w = new WarningDialog(parent, "Class not found. Check the class name and CLASSPATH.");
                    w.setVisible(true);
                    return;
                }
                LoadAppletDialog.this.dispose();
                parent.repaint();
                Farmyard.playSound("../resources/inserted.au");
            }
        });
        getContentPane().add(load, c);
        applet_name_field.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                load.doClick();
            }
        });
        final JButton cancel = new JButton("Cancel");
        c.gridwidth = GridBagConstraints.REMAINDER;
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                LoadAppletDialog.this.dispose();
                parent.repaint();
            }
        });
        getContentPane().add(cancel, c);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
                parent.repaint();
            }
        });
        setResizable(false);
        setSize(300, 150);
        Dimension ss = getToolkit().getScreenSize();
        Dimension ws = getSize();
        setLocation((ss.width - ws.width) / 2, (ss.height - ws.height) / 2);
    }
}
