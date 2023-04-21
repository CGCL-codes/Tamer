package Java3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.opengl.*;

public class TourGL extends JFrame implements WindowListener {

    private static int DEFAULT_FPS = 80;

    private static final int PWIDTH = 512;

    private static final int PHEIGHT = 512;

    private TourCanvasGL canvas;

    private JTextField shapesLeftTF;

    private JTextField jtfTime;

    public TourGL(long period) {
        super("TourGL");
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add(makeRenderPanel(period), BorderLayout.CENTER);
        JPanel ctrls = new JPanel();
        ctrls.setLayout(new BoxLayout(ctrls, BoxLayout.X_AXIS));
        shapesLeftTF = new JTextField("Shapes Left: 5");
        shapesLeftTF.setEditable(false);
        ctrls.add(shapesLeftTF);
        jtfTime = new JTextField("Time Spent: 0 secs");
        jtfTime.setEditable(false);
        ctrls.add(jtfTime);
        c.add(ctrls, BorderLayout.SOUTH);
        addWindowListener(this);
        pack();
        setVisible(true);
    }

    private JPanel makeRenderPanel(long period) {
        JPanel renderPane = new JPanel();
        renderPane.setLayout(new BorderLayout());
        renderPane.setOpaque(false);
        renderPane.setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        canvas = makeCanvas(period);
        renderPane.add("Center", canvas);
        canvas.setFocusable(true);
        canvas.requestFocus();
        renderPane.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent evt) {
                Dimension d = evt.getComponent().getSize();
                canvas.reshape(d.width, d.height);
            }
        });
        return renderPane;
    }

    private TourCanvasGL makeCanvas(long period) {
        GLCapabilities caps = new GLCapabilities();
        AWTGraphicsDevice dev = new AWTGraphicsDevice(null);
        AWTGraphicsConfiguration awtConfig = (AWTGraphicsConfiguration) GLDrawableFactory.getFactory().chooseGraphicsConfiguration(caps, null, dev);
        GraphicsConfiguration config = null;
        if (awtConfig != null) config = awtConfig.getGraphicsConfiguration();
        return new TourCanvasGL(this, period, PWIDTH, PHEIGHT, config, caps);
    }

    public void setShapesLeft(int no) {
        shapesLeftTF.setText("Shapes Left: " + no);
    }

    public void setTimeSpent(long t) {
        jtfTime.setText("Time Spent: " + t + " secs");
    }

    public void windowActivated(WindowEvent e) {
        canvas.resumeGame();
    }

    public void windowDeactivated(WindowEvent e) {
        canvas.pauseGame();
    }

    public void windowDeiconified(WindowEvent e) {
        canvas.resumeGame();
    }

    public void windowIconified(WindowEvent e) {
        canvas.pauseGame();
    }

    public void windowClosing(WindowEvent e) {
        canvas.stopGame();
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public static void main(String[] args) {
        int fps = DEFAULT_FPS;
        if (args.length != 0) fps = Integer.parseInt(args[0]);
        long period = (long) 1000.0 / fps;
        System.out.println("fps: " + fps + "; period: " + period + " ms");
        new TourGL(period * 1000000L);
    }
}
