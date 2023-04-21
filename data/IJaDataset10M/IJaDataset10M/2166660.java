package xapc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import xapc.core.ApplicationStrings;

public class VersionHistoryDialog extends JDialog {

    private static final long serialVersionUID = -5889973005058289844L;

    private JTextArea ta = new JTextArea();

    private JScrollPane sp = new JScrollPane(ta);

    public VersionHistoryDialog() {
        super(XRGui.getInstance());
        setTitle("Versionsgeschichte...");
        getRootPane().registerKeyboardAction(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ta.setText(ApplicationStrings.APP_VERSION_HISTORY);
        ta.setEditable(false);
        ta.setFont(new JLabel().getFont());
        setContentPane(sp);
        setSize(400, 500);
        validate();
    }
}
