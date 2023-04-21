package toolkit.levelEditor.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class BlockingDialog extends JDialog {

    private final JLabel label = new JLabel();

    public BlockingDialog(final Frame owner, final String title) {
        super(owner, title, true);
        setLayout(new BorderLayout());
        add(label);
        setSize(300, 200);
        setLocationRelativeTo(owner);
    }

    public void setMessage(final String string) {
        label.setText(string);
    }
}
