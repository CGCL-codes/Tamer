package alice.tucson.tools;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * E' il JDialog che viene visualizzato quando viene generata una eccezione
 * @version 1.0
 */
public class ExceptionDialog extends JDialog implements ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public ExceptionDialog(String nameException) {
        super();
        setTitle("Exception");
        setSize(450, 120);
        setLocationRelativeTo(null);
        Container c = getContentPane();
        c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));
        JPanel p1 = new JPanel();
        p1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p1.setLayout(new BoxLayout(p1, BoxLayout.LINE_AXIS));
        p1.add(new JLabel(nameException));
        JButton ok = new JButton("Ok");
        ok.addActionListener(this);
        JPanel p2 = new JPanel();
        p2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p2.setLayout(new BoxLayout(p2, BoxLayout.LINE_AXIS));
        p2.add(ok);
        c.add(p1);
        c.add(p2);
    }

    public void actionPerformed(ActionEvent ev) {
        dispose();
    }
}
