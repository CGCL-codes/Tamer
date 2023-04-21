package tabdulin.sms.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class AboutDialog extends JDialog {

    private JFrame owner;

    private JPanel aboutPane;

    private JTextArea aboutTextArea;

    public AboutDialog(JFrame owner) {
        super(owner);
        this.owner = owner;
        initialize();
    }

    private void initialize() {
        setTitle("About");
        setLocation(this.owner.getX() + this.owner.getWidth(), this.owner.getY());
        setSize(new Dimension(252, 94));
        setName("aboutDialog");
        setResizable(false);
        setContentPane(getAboutPane());
    }

    /**
     * This method initializes aboutPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getAboutPane() {
        if (aboutPane == null) {
            aboutPane = new JPanel();
            aboutPane.setLayout(null);
            aboutPane.add(getAboutTextArea(), null);
        }
        return aboutPane;
    }

    /**
     * This method initializes aboutTextArea
     * 
     * @return javax.swing.JTextArea
     */
    private JTextArea getAboutTextArea() {
        if (aboutTextArea == null) {
            aboutTextArea = new JTextArea();
            aboutTextArea.setBounds(new Rectangle(1, 1, 240, 70));
            aboutTextArea.setText("Версия : 0.1.9.0\n" + "Автор : Талгат Абдулин aka talkin\n" + "Email: tabdulin@gmail.com\n" + "Условия использования: неограниченные");
            aboutTextArea.setBackground(new Color(238, 238, 238));
            aboutTextArea.setFont(new Font("UnDotum", Font.PLAIN, 12));
            aboutTextArea.setEditable(false);
        }
        return aboutTextArea;
    }
}
