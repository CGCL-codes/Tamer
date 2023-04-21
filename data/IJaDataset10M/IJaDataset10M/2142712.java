package org.homeunix.thecave.plugins.dashboard;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import org.homeunix.thecave.buddi.plugin.api.PreferenceAccess;
import ca.digitalcave.moss.swing.MossFrame;
import ca.digitalcave.moss.common.ClassLoaderFunctions;

/**
 * GUI of the dashboard, registered with listeners for the event handling
 * 
 * @author Santthosh
 *
 */
public class DashBoardFrame extends MossFrame {

    public static final long serialVersionUID = 1;

    protected DataPanel dataPanel;

    protected ChartPanel chartPanel;

    private JPanel mainPanel, titlePanel;

    protected JTabbedPane tabPanel;

    private JLabel dashLabel, freezeLabel;

    private MouseDragAdapter mouseAdapter;

    private FreezeAdapter freezeAdapter;

    private FrameAdapter frameAdapter;

    protected PreferenceAccess preferencesHandler;

    /**
	 * Build the dashboard main moss frame, register listeners (observers) for the 
	 * action events (We donot launch the frame yet! we are just building it)
	 * 
	 * @param preferencesHandler
	 */
    public DashBoardFrame(PreferenceAccess preferencesHandler) {
        super();
        this.preferencesHandler = preferencesHandler;
        mouseAdapter = new MouseDragAdapter(this);
        mouseAdapter.setPreferences(this.preferencesHandler);
        freezeAdapter = new FreezeAdapter(this);
        frameAdapter = new FrameAdapter(this);
        frameAdapter.setPreferences(this.preferencesHandler);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        tabPanel = new JTabbedPane();
        tabPanel.setFont(new java.awt.Font("Dialog", 0, 10));
        chartPanel = new ChartPanel(this);
        chartPanel.addMouseListener(mouseAdapter);
        chartPanel.addMouseMotionListener(mouseAdapter);
        tabPanel.addTab("Chart", chartPanel);
        dataPanel = new DataPanel(this);
        dataPanel.addMouseListener(mouseAdapter);
        dataPanel.addMouseMotionListener(mouseAdapter);
        tabPanel.addTab("Options", dataPanel);
        tabPanel.addMouseListener(mouseAdapter);
        tabPanel.addMouseMotionListener(mouseAdapter);
        mainPanel.add(tabPanel, BorderLayout.SOUTH);
        titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        dashLabel = new JLabel();
        dashLabel.setFont(new java.awt.Font("Dialog", 0, 11));
        dashLabel.setForeground(java.awt.Color.BLUE);
        dashLabel.setText(" Buddi Dashboard                                                       ");
        titlePanel.add(dashLabel, BorderLayout.WEST);
        freezeLabel = new JLabel();
        freezeLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        freezeLabel.setText("Freeze Dashboard");
        titlePanel.add(freezeLabel, BorderLayout.EAST);
        freezeLabel.addMouseListener(freezeAdapter);
        titlePanel.addMouseListener(mouseAdapter);
        titlePanel.addMouseMotionListener(mouseAdapter);
        mainPanel.add(titlePanel, BorderLayout.NORTH);
    }

    @Override
    public void init() {
        super.init();
        this.setIconImage(ClassLoaderFunctions.getImageFromClasspath("img/BuddiFrameIcon.gif"));
        this.setTitle("Dashboard");
        this.addComponentListener(frameAdapter);
        this.add(mainPanel);
    }

    /**
	 * This is a frame resize listener class, when the frame is resized it registers 
	 * the resized frame height and width to the preferences file. 
	 * 
	 * @author Santthosh
	 *
	 */
    private static class FrameAdapter implements ComponentListener {

        private DashBoardFrame frame;

        private PreferenceAccess preferences;

        static boolean RESIZE_SWITCH = false;

        public FrameAdapter(DashBoardFrame frame) {
            this.frame = frame;
        }

        public void componentHidden(ComponentEvent e) {
        }

        public void componentMoved(ComponentEvent e) {
        }

        public void componentResized(ComponentEvent e) {
            if (frame.chartPanel.launchLabel.getIcon() == null) return;
            if (RESIZE_SWITCH) {
                RESIZE_SWITCH = false;
                return;
            }
            RESIZE_SWITCH = true;
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    if (preferences != null) {
                        preferences.putPreference("org.homeunix.thecave.plugins.dashboard.WINDOW_HEIGHT", String.valueOf(frame.getHeight()));
                        preferences.putPreference("org.homeunix.thecave.plugins.dashboard.WINDOW_WIDTH", String.valueOf(frame.getWidth()));
                    }
                }
            });
        }

        /**
		 * @return the preferences
		 */
        public PreferenceAccess getPreferences() {
            return preferences;
        }

        /**
		 * @param preferences the preferences to set
		 */
        public void setPreferences(PreferenceAccess preferences) {
            this.preferences = preferences;
        }

        public void componentShown(ComponentEvent e) {
        }
    }

    /**
	 * This adapter listens for the click event on the freeze button, and accordingly
	 * flips the timer in the chart panel
	 * 
	 * @author Santthosh
	 *
	 */
    private static class FreezeAdapter implements MouseListener, MouseMotionListener {

        private DashBoardFrame frame;

        public FreezeAdapter(DashBoardFrame frame) {
            this.frame = frame;
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    if (frame.freezeLabel.getText().equalsIgnoreCase("Freeze Dashboard")) {
                        frame.chartPanel.timer.stop();
                        frame.freezeLabel.setText("Activate Dashboard");
                        frame.freezeLabel.setForeground(java.awt.Color.RED);
                    } else {
                        frame.chartPanel.timer.start();
                        frame.freezeLabel.setText("Freeze Dashboard");
                        frame.freezeLabel.setForeground(java.awt.Color.BLACK);
                    }
                }
            });
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
        }
    }

    /**
	 * Listens of the mouse move events to register the X and Y location of the dashboard
	 * frame
	 * 
	 * @author Santthosh
	 *
	 */
    private static class MouseDragAdapter implements MouseListener, MouseMotionListener {

        private static Point origin = new Point();

        private DashBoardFrame frame;

        private PreferenceAccess preferences;

        public MouseDragAdapter(DashBoardFrame frame) {
            this.frame = frame;
        }

        public void mousePressed(MouseEvent e) {
            origin.x = e.getPoint().x;
            origin.y = e.getPoint().y;
        }

        public void mouseDragged(MouseEvent e) {
            final Point mp = e.getPoint();
            final Point fp = frame.getLocationOnScreen();
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    frame.setLocation(fp.x + mp.x - origin.x, fp.y + mp.y - origin.y);
                    if (preferences != null) {
                        preferences.putPreference("org.homeunix.thecave.plugins.dashboard.LOCATION_X", String.valueOf(fp.x + mp.x - origin.x));
                        preferences.putPreference("org.homeunix.thecave.plugins.dashboard.LOCATION_Y", String.valueOf(fp.y + mp.y - origin.y));
                    }
                }
            });
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
            origin.x = 0;
            origin.y = 0;
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
        }

        /**
		 * @return the preferences
		 */
        public PreferenceAccess getPreferences() {
            return preferences;
        }

        /**
		 * @param preferences the preferences to set
		 */
        public void setPreferences(PreferenceAccess preferences) {
            this.preferences = preferences;
        }
    }

    /**
	 * @return the chartPanel
	 */
    public ChartPanel getChartPanel() {
        return chartPanel;
    }

    /**
	 * @param chartPanel the chartPanel to set
	 */
    public void setChartPanel(ChartPanel chartPanel) {
        this.chartPanel = chartPanel;
    }
}
