package net.sf.myra.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This class display the result of a <code>ResultParser</code>.
 * 
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 1598 $ $Date:: 2008-12-17 11:39:02#$
 */
public class ResultDialog extends JDialog {

    private static final long serialVersionUID = -2735542611281637260L;

    /**
	 * Default constructor.
	 * 
	 * @param parent the parent frame.
	 * @param result the text to be shown.
	 */
    public ResultDialog(JFrame parent, String name, String result) {
        super(parent, name, true);
        JTextArea text = new JTextArea(result);
        text.setEditable(false);
        JScrollPane output = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        output.setPreferredSize(new Dimension(300, 300));
        output.setBorder(BorderFactory.createTitledBorder("Output"));
        getContentPane().add(output, "Center");
        JButton close = new JButton("Close");
        close.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        getContentPane().add(close, "South");
        setPreferredSize(new Dimension(300, 300));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Dimension screen = getToolkit().getScreenSize();
        setLocation((screen.width / 2) - 150, (screen.height / 2) - 150);
        pack();
        setVisible(true);
    }
}
