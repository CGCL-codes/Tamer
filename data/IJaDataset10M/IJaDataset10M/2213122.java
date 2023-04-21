package rescuecore.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.text.NumberFormat;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import rescuecore.Memory;

public class Display extends JPanel {

    private Map map;

    private ObjectInspector inspector;

    private JLabel timestep;

    private JLabel score;

    private static final NumberFormat FORMAT = NumberFormat.getInstance();

    static {
        FORMAT.setMaximumFractionDigits(4);
        FORMAT.setMinimumFractionDigits(0);
        FORMAT.setMaximumIntegerDigits(100);
        FORMAT.setMinimumIntegerDigits(1);
    }

    public static Display showDisplay(Memory m) {
        return showDisplay(Map.defaultMap(m));
    }

    public static Display showDisplay(Map map) {
        final JFrame frame = new JFrame();
        Display result = new Display(map);
        frame.setContentPane(result);
        frame.pack();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                frame.setVisible(true);
            }
        });
        return result;
    }

    public Display(Memory m) {
        this(Map.defaultMap(m));
    }

    public Display(Map map) {
        super(new BorderLayout());
        this.map = map;
        inspector = new ObjectInspector();
        timestep = new JLabel("300");
        score = new JLabel("100.0000");
        JPanel top = new JPanel(new FlowLayout());
        top.add(new JLabel("Time: "));
        top.add(timestep);
        top.add(new JLabel("Score: "));
        top.add(score);
        add(top, BorderLayout.NORTH);
        add(inspector, BorderLayout.EAST);
        add(map, BorderLayout.CENTER);
        ObjectSelector selector = new ObjectSelector();
        map.addMouseListener(selector);
        selector.addObjectSelectionListener(inspector);
    }

    public Map getMap() {
        return map;
    }

    public void setTimestep(int t) {
        timestep.setText("" + t);
    }

    public void setScore(double d) {
        score.setText(FORMAT.format(d));
    }
}
