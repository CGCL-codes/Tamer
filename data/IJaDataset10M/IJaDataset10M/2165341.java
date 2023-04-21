package dialog;

import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *  Class to open a warning dialog box.
 *
 *  @author Henrik Eriksson
 *  @version 0.02
 */
public class WarningDialog extends JDialog {

    /**
   *  Constructs a warning dialog box. The dialog window will be created
   *  but not opened. Use the method <code>show()</code> to open and
   *  run the dialog.
   *
   *  @param parent the parent window
   *  @param message the message to display in the dialog box
   */
    public WarningDialog(Frame parent, String message) {
        super(parent, "Warning", true);
        getContentPane().setLayout(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        JLabel ml = new JLabel(message);
        ml.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        c.gridwidth = GridBagConstraints.REMAINDER;
        getContentPane().add(ml, c);
        JButton cb = new JButton("Close");
        cb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                WarningDialog.this.dispose();
            }
        });
        c.insets = new Insets(30, 0, 0, 0);
        getContentPane().add(cb, c);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
            }
        });
        setResizable(false);
        FontMetrics fm = ml.getFontMetrics(ml.getFont());
        setSize(fm.stringWidth(message) + 50, 150);
        Dimension ss = getToolkit().getScreenSize();
        Dimension ws = getSize();
        setLocation((ss.width - ws.width) / 2, (ss.height - ws.height) / 2);
        getToolkit().beep();
    }
}
