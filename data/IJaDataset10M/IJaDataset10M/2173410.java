package PowersetViewer;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * Class which describes the frame to hold the mouseover settings.
 * 
 * @author jordanel
 */
public class MouseoverSettingsFrame extends JFrame {

    private PowersetViewer pv;

    private static final String title = "Mouseover Settings";

    private JLabel summaryExampleLabel;

    private JTextField summaryRegexTF;

    public MouseoverSettingsFrame(PowersetViewer pv) {
        super();
        this.pv = pv;
        this.setResizable(false);
        initComponents();
    }

    /**
	 * Class Constructor
	 * 
	 * @param pv
	 * @param gc
	 */
    public MouseoverSettingsFrame(PowersetViewer pv, GraphicsConfiguration gc) {
        super(gc);
        this.pv = pv;
        this.setResizable(false);
        initComponents();
    }

    /**
	 * Class Constructor with custom title
	 * 
	 * @param pv
	 * @param title
	 */
    public MouseoverSettingsFrame(PowersetViewer pv, String title) {
        super(title);
        this.pv = pv;
        this.setResizable(false);
        initComponents();
    }

    /**
	 * Class Constructor with custom graphics configuration
	 * @param pv
	 * @param title
	 * @param gc
	 */
    public MouseoverSettingsFrame(PowersetViewer pv, String title, GraphicsConfiguration gc) {
        super(title, gc);
        this.pv = pv;
        this.setResizable(false);
        initComponents();
    }

    /**
	 * Initialize and create all the GUI components
	 *
	 * @author jordanel
	 */
    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        this.setTitle(title);
        ((JFrame) this).setLocation(10, 30);
        this.getContentPane().setLayout(new GridBagLayout());
        gbc.ipadx = 0;
        gbc.ipady = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.gridy = 0;
        JLabel summaryRegexLabel = new JLabel("Summary Regex");
        gbc.gridx = 0;
        gbc.gridy = 0;
        getContentPane().add(summaryRegexLabel, gbc);
        summaryRegexTF = new JTextField(20);
        summaryRegexTF.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
                pv.summaryStringRegex = ((JTextField) e.getSource()).getText();
                updateExampleSummary();
            }

            public void keyReleased(KeyEvent e) {
                pv.summaryStringRegex = ((JTextField) e.getSource()).getText();
                updateExampleSummary();
            }

            public void keyPressed(KeyEvent e) {
                pv.summaryStringRegex = ((JTextField) e.getSource()).getText();
                updateExampleSummary();
            }
        });
        gbc.gridy = 1;
        gbc.gridx = 0;
        getContentPane().add(summaryRegexTF, gbc);
        summaryExampleLabel = new JLabel("eg. ");
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 2;
        getContentPane().add(summaryExampleLabel, gbc);
        gbc.gridy = 3;
        getContentPane().add(new JLabel("Legend: "), gbc);
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
        legendPanel.setPreferredSize(new Dimension(200, 200));
        for (int i = 0; i < pv.propertyData.getTotalAttributeCount(); i++) {
            gbc.gridy++;
            legendPanel.add(new JLabel("\t <" + i + "> = " + pv.propertyData.getAttributeName(i)), gbc);
        }
        JScrollPane legendSP = new JScrollPane(legendPanel);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        getContentPane().add(legendSP, gbc);
        updateExampleSummary();
        setSize(280, 350);
    }

    /**
	 * Update the example displayed under the regex text field
	 *
	 * @author jordanel
	 */
    public void updateExampleSummary() {
        summaryExampleLabel.setText("eg. " + pv.getSummaryStringForItem(0));
    }

    public void observe() {
        prepareToShow();
    }

    /**
	 * Prepare the frame for display.  Setup the regex string and update the example.
	 *
	 * @author jordanel
	 */
    public void prepareToShow() {
        summaryRegexTF.setText(pv.summaryStringRegex);
        updateExampleSummary();
    }
}
