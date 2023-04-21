package potato.drawing;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import potato.vm.VM;

/**
 * @author Helge Horch
 * @author Daniel Ingalls
 *  
 * @author Frank Feinbube
 * @author Robert Wierschke
 * 
 * Next to do... put in other color models and try depth = 2, 4, 8
 * Also clean up the cursor spec to use real Squeak masks
 *
 * Actually, more than screen display: also handles keyboard and mouse input,
 * keybard mapping (<i>to</i> scancodes), and creates custom cursors from
 * NoteTaker specs.
 */
public class Screen {

    Dimension fExtent;

    private int fDepth;

    private JFrame fFrame;

    private JLabel fDisplay;

    private int fDisplayBits[];

    private MouseStatus fMouseStatus;

    private KeyboardQueue fKeyboardQueue;

    private Timer fHeartBeat;

    private boolean fScreenChanged;

    private Object fVMSemaphore;

    private static final boolean WITH_HEARTBEAT = true;

    private static final int FPS = 25;

    public Screen(String title, int width, int height, int depth, Object vmSema) {
        fVMSemaphore = vmSema;
        fExtent = new Dimension(width, height);
        fDepth = depth;
        fFrame = new JFrame(title);
        fFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel content = new JPanel(new BorderLayout());
        fDisplay = new JLabel(getNoDisplay());
        fDisplay.setSize(fExtent);
        content.add(fDisplay, BorderLayout.CENTER);
        fFrame.setContentPane(content);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        fFrame.setLocation((screen.width - fExtent.width) / 2, (screen.height - fExtent.height) / 2);
        fDisplay.addMouseMotionListener(fMouseStatus = new MouseStatus((VM) fVMSemaphore));
        fDisplay.addMouseListener(fMouseStatus);
        fDisplay.setFocusable(true);
        fDisplay.addKeyListener(fKeyboardQueue = new KeyboardQueue((VM) fVMSemaphore));
        fDisplay.setOpaque(true);
        fDisplay.getRootPane().setDoubleBuffered(false);
        getFrame().addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent evt) {
                System.exit(1);
            }
        });
    }

    private Icon getNoDisplay() {
        return new Icon() {

            public int getIconWidth() {
                return fExtent.width;
            }

            public int getIconHeight() {
                return fExtent.height;
            }

            public void paintIcon(Component c, Graphics g, int x, int y) {
            }
        };
    }

    public JFrame getFrame() {
        return fFrame;
    }

    public void setBits(int storage[], int depth) {
        fDepth = depth;
        fDisplay.setIcon(new ImageIcon(DisplayAdapter.createDisplayAdapter(storage, fExtent.width, fExtent.height, depth)));
        fDisplayBits = storage;
    }

    int[] getBits() {
        return fDisplayBits;
    }

    public void open() {
        fFrame.pack();
        fFrame.setVisible(true);
        if (WITH_HEARTBEAT) {
            fHeartBeat = new Timer(100 / FPS, new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    if (fScreenChanged) {
                        fScreenChanged = false;
                        Dimension extent = fDisplay.getSize();
                        fDisplay.paintImmediately(0, 0, extent.width, extent.height);
                    }
                }
            });
            fHeartBeat.start();
        }
    }

    public void close() {
        fFrame.setVisible(false);
        fFrame.dispose();
        if (WITH_HEARTBEAT) {
            fHeartBeat.stop();
        }
    }

    public void redisplay(boolean immediately, Rectangle area) {
        redisplay(immediately, area.x, area.y, area.width, area.height);
    }

    public void redisplay(boolean immediately, final int cornerX, final int cornerY, final int width, final int height) {
        fDisplay.repaint(cornerX, cornerY, width, height);
        fScreenChanged = true;
    }

    public void redisplay(boolean immediately) {
        fDisplay.repaint();
        fScreenChanged = true;
    }

    protected boolean scheduleRedisplay(boolean immediately, Runnable trigger) {
        if (immediately) {
            try {
                SwingUtilities.invokeAndWait(trigger);
                return true;
            } catch (InterruptedException e) {
                logRedisplayException(e);
            } catch (InvocationTargetException e) {
                logRedisplayException(e);
            }
            return false;
        } else {
            SwingUtilities.invokeLater(trigger);
            return true;
        }
    }

    protected void logRedisplayException(Exception ex) {
        Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, null, ex);
    }

    public void setCursor(int[] cursorBits, int BWMask) {
        fDisplay.setCursor(new SqueakCursor(cursorBits, BWMask).asCursor());
    }

    public Dimension getExtent() {
        return fDisplay.getSize();
    }

    public void setExtent(Dimension extent) {
        fDisplay.setSize(extent);
        fFrame.setSize(extent);
    }

    public Point getLastMousePoint() {
        return new Point(fMouseStatus.fX, fMouseStatus.fY);
    }

    public int getLastMouseButtonStatus() {
        return fMouseStatus.fButtons & 7 | fKeyboardQueue.modifierKeys();
    }

    public void setMousePoint(int x, int y) {
        Point origin = fDisplay.getLocationOnScreen();
        x += origin.x;
        y += origin.y;
        try {
            Robot robot = new Robot();
            robot.mouseMove(x, y);
        } catch (AWTException ex) {
            Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, "Mouse move to " + x + "@" + y + " failed.", ex);
        }
    }

    public int keyboardPeek() {
        return fKeyboardQueue.peek();
    }

    public int keyboardNext() {
        return fKeyboardQueue.next();
    }
}
