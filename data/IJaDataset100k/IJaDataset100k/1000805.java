package reader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class FontChooserDialog extends JDialog {

    FontChooserPanel panel = new FontChooserPanel(this);

    public FontChooserDialog(final JFrame frame, final JButton b) {
        super(frame, Main.res.getString("font"), true);
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                setVisible(true);
            }
        });
        setContentPane(panel);
        pack();
        setLocation(GUI.desktopBounds.width / 2 - getWidth() / 2, GUI.desktopBounds.height / 2 - getHeight() / 2);
    }

    public void setNewFont() {
        panel.setNewFont();
    }
}
