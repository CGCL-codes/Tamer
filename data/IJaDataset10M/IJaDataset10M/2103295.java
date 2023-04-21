package org.uweschmidt.wiimote.whiteboard.calibration;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.media.jai.PerspectiveTransform;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.uweschmidt.wiimote.whiteboard.WiimoteWhiteboard;
import org.uweschmidt.wiimote.whiteboard.ds.IRDot;
import org.uweschmidt.wiimote.whiteboard.ds.Wiimote;
import org.uweschmidt.wiimote.whiteboard.util.Util;
import Personal.Evento;

public class WiimoteCalibration {

    private static final CalibrationState[][] VALID_STATES = { { CalibrationState.UPPER_LEFT, CalibrationState.UPPER_RIGHT, CalibrationState.LOWER_RIGHT, CalibrationState.LOWER_LEFT }, { CalibrationState.UPPER_LEFT, CalibrationState.TOP_MIDDLE, CalibrationState.BOTTOM_MIDDLE, CalibrationState.LOWER_LEFT }, { CalibrationState.TOP_MIDDLE, CalibrationState.UPPER_RIGHT, CalibrationState.LOWER_RIGHT, CalibrationState.BOTTOM_MIDDLE }, { CalibrationState.UPPER_LEFT, CalibrationState.UPPER_RIGHT, CalibrationState.EAST_MIDDLE, CalibrationState.WEST_MIDDLE }, { CalibrationState.WEST_MIDDLE, CalibrationState.EAST_MIDDLE, CalibrationState.LOWER_RIGHT, CalibrationState.LOWER_LEFT }, { CalibrationState.WEST_MIDDLE, CalibrationState.CENTER, CalibrationState.BOTTOM_MIDDLE, CalibrationState.LOWER_LEFT }, { CalibrationState.UPPER_LEFT, CalibrationState.TOP_MIDDLE, CalibrationState.CENTER, CalibrationState.WEST_MIDDLE }, { CalibrationState.TOP_MIDDLE, CalibrationState.UPPER_RIGHT, CalibrationState.EAST_MIDDLE, CalibrationState.CENTER }, { CalibrationState.CENTER, CalibrationState.EAST_MIDDLE, CalibrationState.LOWER_RIGHT, CalibrationState.BOTTOM_MIDDLE } };

    public static final GraphicsDevice DEFAULT_SCREEN = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

    public static enum CalibrationEvent {

        SCREEN_CHANGED, STARTED, FINISHED, ABORTED, LOADED, SAVED
    }

    ;

    public static interface CalibrationEventListener {

        public void calibrationEvent(CalibrationEvent e);
    }

    public static enum CalibrationState {

        DONE(0, 0, null), CENTER(.5, .5, DONE), EAST_MIDDLE(.9, .5, CENTER), WEST_MIDDLE(.1, .5, EAST_MIDDLE), TOP_MIDDLE(.5, .1, WEST_MIDDLE), BOTTOM_MIDDLE(.5, .9, TOP_MIDDLE), LOWER_LEFT(.1, .9, BOTTOM_MIDDLE), LOWER_RIGHT(.9, .9, LOWER_LEFT), UPPER_RIGHT(.9, .1, LOWER_RIGHT), UPPER_LEFT(.1, .1, UPPER_RIGHT), PENDING(0, 0, UPPER_LEFT);

        public static final CalibrationState REGULAR_END = LOWER_LEFT;

        private final double xMargin, yMargin;

        private final CalibrationState next;

        private CalibrationState(double xMargin, double yMargin, CalibrationState next) {
            this.xMargin = xMargin;
            this.yMargin = yMargin;
            this.next = next;
        }

        public int getX(Rectangle bounds) {
            return bounds.x + (int) Math.round(bounds.width * xMargin);
        }

        public int getY(Rectangle bounds) {
            return bounds.y + (int) Math.round(bounds.height * yMargin);
        }

        public CalibrationState getNext() {
            return next;
        }
    }

    ;

    private static final double EPS = .05;

    private final CalibrationFrame calibrationFrame;

    private final Set<CalibrationEventListener> listener = new LinkedHashSet<CalibrationEventListener>();

    private GraphicsDevice screen = null;

    private int screenNumber = -1;

    private Rectangle bounds;

    private int sc = 0;

    private boolean stepChange = false;

    private boolean checkPoints = false;

    private Collection<Wiimote> wiimotes;

    private CalibrationState state = CalibrationState.PENDING;

    private Map<Wiimote, Point2D> last = new HashMap<Wiimote, Point2D>();

    private Map<String, PerspectiveTransform> transformer = new LinkedHashMap<String, PerspectiveTransform>();

    private Map<Wiimote, Map<CalibrationState, Point2D>> points = new LinkedHashMap<Wiimote, Map<CalibrationState, Point2D>>();

    private Map<String, Double[]> finals = new LinkedHashMap<String, Double[]>();

    public WiimoteCalibration() {
        calibrationFrame = new CalibrationFrame();
    }

    public boolean setScreen(GraphicsDevice screen) {
        if (!inProgress()) {
            this.screen = screen;
            bounds = screen.getDefaultConfiguration().getBounds();
            calibrationFrame.setBounds(bounds);
            GraphicsDevice[] gds = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
            for (int i = 0; i < gds.length; i++) {
                if (gds[i] == screen) {
                    screenNumber = i + 1;
                    break;
                }
            }
            state = CalibrationState.PENDING;
            transformer.clear();
            notifyListener(CalibrationEvent.SCREEN_CHANGED);
            return true;
        } else {
            System.err.println("Calibration in Progress.");
            return false;
        }
    }

    public GraphicsDevice getScreen() {
        return screen;
    }

    public int getScreenNumber() {
        return screenNumber;
    }

    @SuppressWarnings("serial")
    private class CalibrationFrame extends JFrame {

        private final ImageIcon VISIBLE = new ImageIcon(WiimoteCalibration.class.getResource("resources/icons/francisco-ok.png"));

        private final ImageIcon NOT_VISIBLE = new ImageIcon(WiimoteCalibration.class.getResource("resources/icons/francisco-warning.png"));

        private final Image CROSS_HAIR = new ImageIcon(WiimoteCalibration.class.getResource("resources/icons/francisco-crosshair.png")).getImage();

        public CalibrationFrame() {
            super("Calibration");
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            setBackground(Color.WHITE);
            ((JPanel) getContentPane()).setOpaque(true);
            setLayout(null);
            setUndecorated(true);
            setAlwaysOnTop(true);
            addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        state = CalibrationState.PENDING;
                        if (screen.getFullScreenWindow() == calibrationFrame) screen.setFullScreenWindow(null);
                        setVisible(false);
                        notifyListener(CalibrationEvent.ABORTED);
                    }
                }
            });
        }

        private JLabel statusLabel(Icon icon, int id, int x, int y) {
            JLabel l = new JLabel(icon, SwingConstants.LEFT);
            if (wiimotes.size() > 1) l.setText(String.valueOf(id));
            int w = 100;
            int h = icon.getIconHeight();
            x = x - icon.getIconWidth() / 2;
            y = y - wiimotes.size() * h / 2;
            l.setBounds(x, y + (id - 1) * h, w, h);
            return l;
        }

        public void finished(CalibrationState s) {
            int x = s.getX(bounds) - bounds.x;
            int y = s.getY(bounds) - bounds.y;
            for (Wiimote wiimote : points.keySet()) {
                add(statusLabel(points.get(wiimote).get(s) != null ? VISIBLE : NOT_VISIBLE, wiimote.getId(), x, y));
            }
            repaint();
        }

        public void reset() {
            getContentPane().removeAll();
            JLabel info = Util.newComponent(JLabel.class, "infoLabel");
            info.setFont(info.getFont().deriveFont(20f));
            info.setHorizontalAlignment(SwingConstants.CENTER);
            int w = bounds.width;
            int h = 200;
            info.setBounds(bounds.width / 2 - w / 2, bounds.height / 3 - h / 2, w, h);
            add(info);
            Util.getResourceMap(WiimoteCalibration.class).injectComponents(this);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (state != CalibrationState.DONE && state != CalibrationState.PENDING) {
                int x = state.getX(bounds) - bounds.x;
                int y = state.getY(bounds) - bounds.y;
                g.drawImage(CROSS_HAIR, x - CROSS_HAIR.getWidth(null) / 2 + 1, y - CROSS_HAIR.getHeight(null) / 2 + 1, null);
            }
        }
    }

    public void start(Collection<Wiimote> wiimotes) {
        last.clear();
        points.clear();
        finals.clear();
        calibrationFrame.reset();
        stepChange = false;
        sc = 0;
        checkPoints = false;
        state = CalibrationState.PENDING.getNext();
        this.wiimotes = wiimotes;
        for (Wiimote wiimote : wiimotes) {
            points.put(wiimote, new LinkedHashMap<CalibrationState, Point2D>());
            last.put(wiimote, new Point2D.Double(-1, -1));
        }
        if (screen == null) setScreen(DEFAULT_SCREEN);
        if (Util.MAC_OS_X && screen == DEFAULT_SCREEN) screen.setFullScreenWindow(calibrationFrame);
        calibrationFrame.repaint();
        calibrationFrame.setVisible(true);
        notifyListener(CalibrationEvent.STARTED);
    }

    public boolean inProgress() {
        return !isPending() && !isDone();
    }

    public boolean isPending() {
        return state == CalibrationState.PENDING;
    }

    public boolean isDone() {
        return state == CalibrationState.DONE;
    }

    public Map<String, Double[]> getFinals() {
        return finals;
    }

    public boolean isCalibrated(Wiimote wiimote) {
        return transformer.containsKey(wiimote.getAddress());
    }

    public boolean isAnyCalibrated(Collection<Wiimote> wiimotes) {
        for (Wiimote wiimote : wiimotes) {
            if (isCalibrated(wiimote)) return true;
        }
        return false;
    }

    public boolean process(Map<Wiimote, IRDot[]> data) {
        if (inProgress()) {
            for (Wiimote wiimote : points.keySet()) {
                Point2D lastP = last.get(wiimote);
                Point2D currentP = data.get(wiimote)[0];
                if (currentP != null && lastP.distance(currentP) > EPS) {
                    points.get(wiimote).put(state, currentP);
                    last.put(wiimote, currentP);
                    stepChange = true;
                }
            }
            if (stepChange && ++sc > 5) {
                sc = 0;
                stepChange = false;
                CalibrationState current = state;
                state = state.getNext();
                calibrationFrame.finished(current);
                if (checkPoints || current == CalibrationState.REGULAR_END) {
                    checkPoints = true;
                    if (calculateQuadsForWiimotes()) {
                        state = CalibrationState.DONE;
                        calculateTransformation();
                        if (screen.getFullScreenWindow() == calibrationFrame) screen.setFullScreenWindow(null);
                        calibrationFrame.setVisible(false);
                        System.out.println("Calibrato");
                        Evento.getInterfaccia().notifyCalibration();
                        notifyListener(CalibrationEvent.FINISHED);
                        return true;
                    } else {
                    }
                }
            }
            if (state == CalibrationState.DONE) {
                state = CalibrationState.PENDING;
                if (screen.getFullScreenWindow() == calibrationFrame) screen.setFullScreenWindow(null);
                calibrationFrame.setVisible(false);
                notifyListener(CalibrationEvent.ABORTED);
                new Thread(new Runnable() {

                    public void run() {
                        JOptionPane.showMessageDialog(null, Util.getResourceMap(WiimoteCalibration.class).getString("coverageError"), Util.getResourceMap(WiimoteCalibration.class).getString("calibrationFailed"), JOptionPane.ERROR_MESSAGE);
                    }
                }).start();
            }
            return true;
        }
        return false;
    }

    private static boolean checkExist(Map<CalibrationState, Point2D> p, CalibrationState... states) {
        for (CalibrationState s : states) {
            if (p.get(s) == null) return false;
        }
        return true;
    }

    private boolean calculateQuadsForWiimotes() {
        boolean success = true;
        Set<CalibrationState> check = new HashSet<CalibrationState>();
        for (Wiimote wiimote : points.keySet()) {
            boolean ok = false;
            for (int i = 0; i < VALID_STATES.length; i++) {
                if (checkExist(points.get(wiimote), VALID_STATES[i])) {
                    Map<CalibrationState, Point2D> calibrated = points.get(wiimote);
                    CalibrationState[] states = VALID_STATES[i];
                    check.addAll(Arrays.asList(states));
                    finals.put(wiimote.getAddress(), new Double[] { calibrated.get(states[0]).getX(), calibrated.get(states[0]).getY(), calibrated.get(states[1]).getX(), calibrated.get(states[1]).getY(), calibrated.get(states[2]).getX(), calibrated.get(states[2]).getY(), calibrated.get(states[3]).getX(), calibrated.get(states[3]).getY(), (double) states[0].getX(bounds), (double) states[0].getY(bounds), (double) states[1].getX(bounds), (double) states[1].getY(bounds), (double) states[2].getX(bounds), (double) states[2].getY(bounds), (double) states[3].getX(bounds), (double) states[3].getY(bounds) });
                    ok = true;
                    break;
                }
            }
            success = success && ok;
        }
        return success && check.containsAll(Arrays.asList(VALID_STATES[0]));
    }

    private void calculateTransformation() {
        transformer.clear();
        for (String address : finals.keySet()) {
            Double[] d = finals.get(address);
            transformer.put(address, PerspectiveTransform.getQuadToQuad(d[0], d[1], d[2], d[3], d[4], d[5], d[6], d[7], d[8], d[9], d[10], d[11], d[12], d[13], d[14], d[15]));
        }
    }

    public Map<String, PerspectiveTransform> getTransformer() {
        return transformer;
    }

    public IRDot warp(int i, Wiimote wiimote, Map<Wiimote, IRDot[]> data) {
        final PerspectiveTransform transform = transformer.get(wiimote.getAddress());
        if (transform == null || data.get(wiimote)[i] == null) return null; else {
            return (IRDot) transform.transform(data.get(wiimote)[i], new IRDot(data.get(wiimote)[i]));
        }
    }

    private Map<Wiimote, IRDot[]> warpIndividually(Map<Wiimote, IRDot[]> data) {
        Map<Wiimote, IRDot[]> warped = new LinkedHashMap<Wiimote, IRDot[]>();
        IRDot[] p;
        for (Wiimote wiimote : data.keySet()) {
            warped.put(wiimote, p = new IRDot[4]);
            for (int i = 0; i < 4; i++) {
                p[i] = warp(i, wiimote, data);
            }
        }
        return warped;
    }

    public Point[] warp(Map<Wiimote, IRDot[]> data) {
        Point[] warped = new Point[4];
        if (isDone() && isAnyCalibrated(data.keySet())) {
            IRDot[][] cluster = PointClusterer.cluster(warpIndividually(data));
            for (int i = 0; i < cluster.length; i++) {
                if (cluster[i] == null) break;
                double x = 0, y = 0;
                int c = 0;
                for (IRDot p : cluster[i]) {
                    x += p.getX();
                    y += p.getY();
                    c++;
                    break;
                }
                warped[i] = new Point((int) Math.round(x / c), (int) Math.round(y / c));
            }
        } else {
            System.err.println("Not calibrated.");
        }
        return warped;
    }

    /**
	 * @return true if data is valid and could be loaded, false otherwise
	 */
    public boolean load(InputStream is) throws IOException {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String address = null;
            while ((address = in.readLine()) != null) {
                Double[] d = new Double[16];
                finals.put(address, d);
                for (int i = 0; i < 4; i++) {
                    String[] p = in.readLine().split(" ");
                    for (int j = 0; j < 2; j++) {
                        d[i * 2 + j + 0] = Double.valueOf(p[j + 0]);
                        d[i * 2 + j + 8] = Double.valueOf(p[j + 2]);
                    }
                }
            }
            calculateTransformation();
            state = CalibrationState.DONE;
            notifyListener(CalibrationEvent.LOADED);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void save(OutputStream os) throws IOException {
        PrintStream out = new PrintStream(os);
        for (String address : finals.keySet()) {
            out.print(address);
            Double[] d = finals.get(address);
            for (int i = 0; i < 4; i++) {
                out.printf(Locale.ENGLISH, "\n%f %f %.0f %.0f", d[i * 2 + 0 + 0], d[i * 2 + 1 + 0], d[i * 2 + 0 + 8], d[i * 2 + 1 + 8]);
            }
            out.println();
        }
        out.close();
        notifyListener(CalibrationEvent.SAVED);
    }

    public void addCalibrationEventListener(CalibrationEventListener l) {
        listener.add(l);
    }

    public void removeCalibrationEventListener(CalibrationEventListener l) {
        listener.remove(l);
    }

    private void notifyListener(CalibrationEvent e) {
        WiimoteWhiteboard.getLogger().info("Calibration Event: " + e);
        for (CalibrationEventListener l : listener) {
            l.calibrationEvent(e);
        }
    }
}
