package PowersetThingy;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.HashMap;
import java.util.Hashtable;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import tools.Debug;
import tools.DebugListener;

/**
 * @author jslack
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DebugFrame extends JFrame implements DebugListener {

    private PowersetThingy tj;

    private DebugFrame self = this;

    private static final String resultType[] = { "Active Group", "Action Mode", "Selection Resolution", "Grow Direction", "Current Mark", "Navigation" };

    public static final int ACT_TYPE = 0;

    public static final int ACT_MODE = 1;

    public static final int ACT_SELECTION = 2;

    public static final int ACT_GROWDIR = 3;

    public static final int M_GROUP = 4;

    public static final int NAV_TYPE = 5;

    public JTextField result[];

    private JLabel resultLabel[];

    private JSlider sliderDebugLevel;

    private JTextArea debugOutput;

    private int currDebugLevel = 2;

    public static final String actionModeLabel[] = { "MOUSEOVER", "ST_FREEMOVE", "ST_FREEMOVEAGAIN", "ST_RESHAPE", "RECT_CREATE", "RECT_FREEMOVE", "RECT_FREEMOVEAGAIN", "RECT_RESHAPE", "PICK_SEQUENCE", "GROUP_MOUSESELECT", "GROUP_MOUSESELECT_RECT_CREATE", "GROUP_MOUSESELECT_RECT_FREEMOVE", "GROUP_MOUSESELECT_PRESSED" };

    public static final String actionTargetLabel[] = { "Horizontal", "Vertical", "Horizontal and Vertical", "Node", "Subtree" };

    static final int H_TARGET = 0;

    static final int V_TARGET = 1;

    static final int B_TARGET = 2;

    static final int NODE_TARGET = 3;

    static final int SUBTREE_TARGET = 4;

    /**
	 * @throws java.awt.HeadlessException
	 */
    public DebugFrame(PowersetThingy tj) {
        super();
        this.tj = tj;
        this.setResizable(false);
        initComponents();
    }

    /**
	 * @param gc
	 */
    public DebugFrame(PowersetThingy tj, GraphicsConfiguration gc) {
        super(gc);
        this.tj = tj;
        this.setResizable(false);
        initComponents();
    }

    /**
	 * @param title
	 * @throws java.awt.HeadlessException
	 */
    public DebugFrame(PowersetThingy tj, String title) {
        super(title);
        this.tj = tj;
        this.setResizable(false);
        initComponents();
    }

    /**
	 * @param title
	 * @param gc
	 */
    public DebugFrame(PowersetThingy tj, String title, GraphicsConfiguration gc) {
        super(title, gc);
        this.tj = tj;
        this.setResizable(false);
        initComponents();
    }

    private void initComponents() {
        setTitle("Debug");
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        debugOutput = new JTextArea(10, 30);
        debugOutput.setEditable(false);
        JScrollPane debugOutputPane = new JScrollPane(debugOutput);
        debugOutputPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        debugOutputPane.setPreferredSize(new Dimension(400, 250));
        getContentPane().add(debugOutputPane, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        sliderDebugLevel = new JSlider(0, 3, currDebugLevel);
        sliderDebugLevel.setSnapToTicks(true);
        sliderDebugLevel.setMajorTickSpacing(1);
        sliderDebugLevel.setPaintLabels(true);
        sliderDebugLevel.setPaintTicks(true);
        Hashtable dicDebugLabels = new Hashtable();
        dicDebugLabels.put(new Integer(0), new JLabel("Quiet"));
        dicDebugLabels.put(new Integer(3), new JLabel("EXTREMELY Verbose"));
        sliderDebugLevel.setLabelTable(dicDebugLabels);
        sliderDebugLevel.setPreferredSize(new Dimension(350, 50));
        sliderDebugLevel.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                int newDebugLevel = ((JSlider) e.getSource()).getValue();
                if (currDebugLevel == newDebugLevel) return;
                currDebugLevel = newDebugLevel;
                Debug.register(self, newDebugLevel);
            }
        });
        getContentPane().add(sliderDebugLevel, gbc);
        result = new JTextField[resultType.length];
        resultLabel = new JLabel[resultType.length];
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        for (int i = 0; i < resultType.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i + 2;
            resultLabel[i] = new JLabel(resultType[i]);
            getContentPane().add(resultLabel[i], gbc);
            gbc.gridx = 1;
            result[i] = new JTextField(25);
            result[i].setEditable(false);
            getContentPane().add(result[i], gbc);
        }
        pack();
        Debug.register(this, 2);
    }

    public void debugPrint(String theString) {
        debugOutput.append(theString + "\n");
        debugOutput.setCaretPosition(debugOutput.getText().length());
    }
}
