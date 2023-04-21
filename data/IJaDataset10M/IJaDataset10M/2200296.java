package uk.ac.imperial.ma.metric.tools.scientificCalculator;

import javax.swing.JFrame;
import java.awt.Container;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import uk.ac.imperial.ma.metric.gui.MathematicalExpressionEntryPanel;

/**
 * This is the <code>JFrame</code> which contains the scientific calculator tool.
 *
 * @author Daniel J. R. May
 * @version 0.0.1
 */
public class ScientificCalculatorFrame extends JFrame implements ActionListener {

    private JMenuBar mbar;

    private JMenu mnuFile;

    private JMenuItem mitClose;

    private Container c;

    private BorderLayout bl;

    private JTextField jtxt;

    private ScientificCalculatorButtonsPanel scbp;

    private MathematicalExpressionEntryPanel meep;

    public ScientificCalculatorFrame() {
        super("Scientific Calculator");
        mitClose = new JMenuItem("Close");
        mitClose.addActionListener(this);
        mnuFile = new JMenu("File");
        mnuFile.addActionListener(this);
        mnuFile.add(mitClose);
        mbar = new JMenuBar();
        mbar.add(mnuFile);
        this.setJMenuBar(mbar);
        c = this.getContentPane();
        bl = new BorderLayout();
        c.setLayout(bl);
        jtxt = new JTextField("");
        jtxt.setHorizontalAlignment(JTextField.RIGHT);
        jtxt.setFont(new Font("SansSerif", Font.PLAIN, 24));
        try {
            meep = new MathematicalExpressionEntryPanel("Input: ", "", 0, MathematicalExpressionEntryPanel.INPUT_AT_TOP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        scbp = new ScientificCalculatorButtonsPanel(meep.getJTextField());
        c.add(meep, BorderLayout.CENTER);
        c.add(scbp, BorderLayout.SOUTH);
        this.setSize(700, 500);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == mitClose) {
            this.setVisible(false);
            this.dispose();
        }
    }
}
