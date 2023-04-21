package org.trebor.sarynpaint;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.applet.*;
import java.net.*;
import javax.sound.sampled.*;

/**
 * SarynPaint is a simple paint program for very young children.  It was
 * written for my dear friend Saryn who had at the time a particular
 * fondness for the color pink.
 *
 * @author Robert Harris
 */
public class SarynPaint extends JFrame {

    public static int buttonWidth = 85;

    public static int buttonHeight = 85;

    public static int alpha = 128;

    public static float smallScale = 30;

    public static float mediumScale = 50;

    public static float largeScale = 80;

    public static float shapeScale = mediumScale;

    public static int MOVEMODE = 0;

    public static int STICKYMODE = 1;

    public static int CLICKMODE = 2;

    public static int MODECOUNT = 3;

    public static Shape triangle = createRegularPoly(3);

    public static Shape square = normalize(new Rectangle2D.Float(0, 0, 1, 1));

    public static Shape pentagon = createRegularPoly(5);

    public static Shape hexagon = createRegularPoly(6);

    public static Shape circle = normalize(new Ellipse2D.Float(0, 0, 1, 1));

    public static Shape heart = createHeartShape();

    public static Shape star = createStar(5);

    public static Shape cat = createCatShape();

    public static Shape dog = createDogShape();

    public static Shape fish = createFishShape();

    JPanel paintArea = null;

    JPanel colorArea = null;

    JPanel shapeArea = null;

    Color paintColor = new Color(255, 0, 0, alpha);

    Shape paintShape = cat;

    Vector<Component> guiObjects = new Vector<Component>();

    JFrame frame = this;

    AudioClip lastPlayed = null;

    boolean soundEnabled = true;

    int inputMode = MOVEMODE;

    float paintScale = shapeScale;

    Calendar startTime = Calendar.getInstance();

    Color backGround = Color.WHITE;

    Font helpFont = new Font("Courier", Font.PLAIN, 40);

    Color helpColor = new Color(153, 153, 153);

    long helpDelay = 5000;

    String[] helpText = { "ESC          exits program", "SPACE        toggles sound", "DOUBLE CLICK clears screen", "DOUBLE CLICK on a color sets background", "ENTER        cycles input mode", "", "input modes:", "  MOVE   all movments paint", "  STICKY click toggles paint mode", "  CLICK  click and drag to paint" };

    SoundClip soundOn = new SoundClip("soundon");

    SoundClip soundOff = new SoundClip("soundoff");

    SoundClip moveMode = new SoundClip("movemode");

    SoundClip clickMode = new SoundClip("clickmode");

    SoundClip stickyMode = new SoundClip("stickymode");

    SoundClip welcome = new SoundClip("welcome");

    SoundClip goodBye = new SoundClip("goodBye");

    SoundClip[] modeSounds = { moveMode, stickyMode, clickMode };

    PalletItem[] shapePallet = { new ShapePalletItem("Triangle", triangle), new ShapePalletItem("Square", square), new ShapePalletItem("Pentagon", pentagon), new ShapePalletItem("Hexagon", hexagon), new ShapePalletItem("Circle", circle), new ShapePalletItem("Heart", heart), new ShapePalletItem("Star", star), new ShapePalletItem("Doggy", dog), new ShapePalletItem("Kitty", cat), new ShapePalletItem("Fishy", fish) };

    PalletItem[] colorPallet = { new ColorPalletItem("Black", Color.BLACK), new ColorPalletItem("Gray", new Color(128, 128, 128)), new ColorPalletItem("White", Color.WHITE), new ColorPalletItem("Red", Color.RED), new ColorPalletItem("Orange", new Color(255, 151, 0)), new ColorPalletItem("Yellow", Color.YELLOW), new ColorPalletItem("Green", Color.GREEN), new ColorPalletItem("Blue", Color.BLUE), new ColorPalletItem("Purple", new Color(0x75, 0x09, 0x91)), new ColorPalletItem("Pink", new Color(255, 64, 196)) };

    ActionPalletItem[] actionPallet = {};

    public static void main(String[] args) {
        try {
            new SarynPaint();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public SarynPaint() {
        paintShape = createHeartShape();
        constructFrame(getContentPane());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphicsDevice gv = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
        if (gv.isFullScreenSupported() && false) {
            setUndecorated(true);
            setVisible(true);
            pack();
            gv.setFullScreenWindow(this);
        } else {
            pack();
            setExtendedState(MAXIMIZED_BOTH);
            setVisible(true);
        }
        setPaintCursor();
        welcome.play();
    }

    public void constructFrame(Container frame) {
        frame.setLayout(new BorderLayout());
        frame.add(colorArea = new Pallet(colorPallet, BoxLayout.Y_AXIS), BorderLayout.WEST);
        frame.add(shapeArea = new Pallet(shapePallet, BoxLayout.Y_AXIS), BorderLayout.EAST);
        MouseInputAdapter mia = new MouseInputAdapter() {

            boolean sticky = false;

            public void mouseDragged(MouseEvent e) {
                drawShape(e);
            }

            public void mouseMoved(MouseEvent e) {
                if (inputMode == MOVEMODE || (inputMode == STICKYMODE && sticky)) drawShape(e);
            }

            public void mouseClicked(MouseEvent e) {
                sticky = !sticky;
                if (inputMode != STICKYMODE || sticky) drawShape(e);
                if (e.getClickCount() > 1) clearScreen(e);
            }

            void drawShape(MouseEvent e) {
                Graphics2D g = (Graphics2D) paintArea.getGraphics();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(paintColor);
                paintShape(paintShape, g, e.getX(), e.getY());
            }

            void clearScreen(MouseEvent e) {
                paintArea.repaint();
            }
        };
        paintArea = new JPanel() {

            public void paint(Graphics g) {
                g.setColor(backGround);
                g.fillRect(0, 0, getWidth(), getHeight());
                if (Calendar.getInstance().getTimeInMillis() - startTime.getTimeInMillis() < helpDelay) {
                    drawHelpText((Graphics2D) g);
                }
            }
        };
        paintArea.addMouseMotionListener(mia);
        paintArea.addMouseListener(mia);
        frame.add(paintArea, BorderLayout.CENTER);
        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                    try {
                        goodBye.play();
                        Thread.sleep(550);
                        System.exit(0);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
                if (e.getKeyChar() == KeyEvent.VK_SPACE) {
                    if (soundEnabled) soundOff.play();
                    soundEnabled = !soundEnabled;
                    if (soundEnabled) soundOn.play();
                }
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    inputMode++;
                    inputMode %= MODECOUNT;
                    modeSounds[inputMode].play();
                }
            }
        });
    }

    void drawHelpText(Graphics2D g) {
        g.setFont(helpFont);
        FontMetrics fm = g.getFontMetrics();
        int width = 0;
        int height = 0;
        for (int i = 0; i < helpText.length; ++i) {
            Rectangle2D bounds = fm.getStringBounds(helpText[i], g);
            height += bounds.getHeight();
            if (width < bounds.getWidth()) width = (int) bounds.getWidth();
        }
        g.setColor(computeMatchingColor(backGround));
        int x = (int) ((g.getClipBounds().getWidth() - width) / 2);
        int y = (int) ((g.getClipBounds().getHeight() - height) / 2);
        for (int i = 0; i < helpText.length; ++i) {
            g.drawString(helpText[i], x, y);
            y += fm.getHeight();
        }
    }

    Color computeMatchingColor(Color color) {
        float brightness = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), new float[3])[2];
        return brightness > 0.5 ? color.darker().darker() : color.brighter().brighter().brighter().brighter().brighter().brighter().brighter().brighter().brighter();
    }

    void setPaintCursor() {
        Shape shape = scale(paintShape, shapeScale, shapeScale);
        Rectangle2D bounds = shape.getBounds2D();
        shape = translate(shape, bounds.getWidth() / 2.0, bounds.getHeight() / 2.0);
        bounds = shape.getBounds2D();
        BufferedImage image = new BufferedImage((int) bounds.getWidth(), (int) bounds.getHeight() + 1, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(paintColor.darker());
        g.fill(shape);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Cursor cursor = tk.createCustomCursor(image, new Point((int) (bounds.getWidth() / 2), (int) (bounds.getHeight() / 2)), "name");
        paintArea.setCursor(cursor);
        for (int i = 0; i < guiObjects.size(); ++i) guiObjects.get(i).setCursor(cursor);
    }

    /** A generic pallet item base class. */
    public abstract class PalletItem extends JPanel {

        SoundClip sound = null;

        String name = null;

        PalletItem(String name) {
            this.name = name;
            sound = new SoundClip(name);
            guiObjects.add(this);
            MouseInputAdapter mia = new MouseInputAdapter() {

                public void mouseEntered(MouseEvent e) {
                    if (inputMode != CLICKMODE) selectItem(e);
                }

                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() > 1) doubleClicked(e);
                    if (inputMode == CLICKMODE) selectItem(e);
                }

                public void selectItem(MouseEvent e) {
                    sound.play();
                    selected(e);
                }
            };
            addMouseListener(mia);
            addMouseMotionListener(mia);
            setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        }

        abstract void selected(MouseEvent e);

        void doubleClicked(MouseEvent e) {
        }
    }

    /** A color pallet item. */
    public class ColorPalletItem extends PalletItem {

        Color color = null;

        ColorPalletItem(String name, Color color) {
            super(name);
            this.color = color;
        }

        void doubleClicked(MouseEvent e) {
            backGround = color;
            frame.repaint();
        }

        void selected(MouseEvent e) {
            paintColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            setPaintCursor();
            shapeArea.repaint();
        }

        public void paint(Graphics g) {
            g.setColor(color);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    /** A shape pallet item. */
    public class ShapePalletItem extends PalletItem {

        Shape shape = null;

        ShapePalletItem(String name, Shape shape) {
            super(name);
            this.shape = shape;
        }

        void selected(MouseEvent e) {
            paintShape = shape;
            setPaintCursor();
        }

        public void paint(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(backGround);
            Rectangle bounds = this.getBounds(new Rectangle());
            g.fillRect(0, 0, getWidth(), getHeight());
            Color shapeColor = new Color(paintColor.getRed(), paintColor.getGreen(), paintColor.getBlue());
            if (shapeColor.equals(backGround)) shapeColor = computeMatchingColor(shapeColor);
            g.setColor(shapeColor);
            paintShape(shape, g, getWidth() / 2.0, getHeight() / 2.0);
        }
    }

    /** An action pallet item (not currently working). */
    public class ActionPalletItem extends ShapePalletItem {

        ActionPalletItem(String name, Shape icon) {
            super(name, icon);
        }
    }

    /** A pallet of color or shape tools. */
    public class Pallet extends JPanel {

        Pallet(PalletItem[] palletItems, int axis) {
            setLayout(new BoxLayout(this, axis));
            for (int i = 0; i < palletItems.length; ++i) add(palletItems[i]);
        }

        public void paint(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            setBackground(backGround);
            super.paint(g);
        }
    }

    /** A sound clip which can be loaded and played. */
    public class SoundClip {

        AudioClip sound = null;

        String name = null;

        boolean loadAttempt = false;

        public SoundClip(String name) {
            this(name, false);
        }

        public SoundClip(String name, boolean preload) {
            this.name = name;
            if (preload) load();
        }

        public void play() {
            if (!soundEnabled) return;
            if (loadAttempt == false) load();
            if (sound != null) sound.play();
        }

        public void load() {
            try {
                loadAttempt = true;
                URL url = this.getClass().getResource("/sounds/" + name.toLowerCase() + ".wav");
                if (url != null) sound = Applet.newAudioClip(url);
            } catch (Exception e) {
                System.out.println("Exception: " + e);
            }
        }
    }

    public static Shape createHeartShape() {
        GeneralPath gp = new GeneralPath();
        gp.append(translate(circle, 0.5, 0), false);
        gp.append(translate(circle, 0, 0.5), false);
        gp.append(square, false);
        return normalize(rotate(gp, 225));
    }

    public static Shape createCatShape() {
        Area cat = new Area(circle);
        Area wisker = new Area(new Rectangle2D.Double(0, -.01, .3, .02));
        Area leftWiskers = new Area();
        leftWiskers.add(rotate(wisker, -20));
        leftWiskers.add(rotate(wisker, 20));
        leftWiskers.add(rotate(wisker, 20));
        Area rightWiskers = new Area();
        rightWiskers.add(rotate(wisker, 180));
        rightWiskers.add(rotate(wisker, -20));
        rightWiskers.add(rotate(wisker, -20));
        Area ear = new Area(translate(scale(triangle, .5, .5), 0.0, -0.6));
        translate(ear, .07, 0);
        cat.add(ear);
        rotate(cat, 60);
        translate(ear, -.14, 0);
        cat.add(ear);
        rotate(cat, -30);
        Area eye = new Area(scale(circle, 0.18, 0.18));
        eye.subtract(new Area(scale(circle, .06, .12)));
        translate(eye, -.15, -.1);
        cat.subtract(eye);
        translate(eye, .3, 0);
        cat.subtract(eye);
        cat.subtract(translate(leftWiskers, .08, .14));
        cat.subtract(translate(rightWiskers, -.08, .14));
        Area nose = new Area(createRegularPoly(3));
        rotate(nose, 180);
        scale(nose, .15, .15);
        translate(nose, 0, .1);
        cat.subtract(nose);
        scale(cat, 1.0, 0.85);
        return normalize(cat);
    }

    public static Shape createDogShape() {
        Area dog = new Area(circle);
        Area ear = new Area(scale(circle, .4, .7));
        rotate(ear, 20);
        translate(ear, -.5, -.2);
        dog.subtract(ear);
        scale(ear, -1, 1);
        dog.subtract(ear);
        scale(ear, -1, 1);
        translate(ear, -.05, 0);
        dog.add(ear);
        scale(ear, -1, 1);
        dog.add(ear);
        scale(ear, -1, 1);
        Area eye = new Area(scale(circle, 0.18, 0.18));
        eye.subtract(new Area(scale(circle, .12, .12)));
        translate(eye, -.15, -.1);
        dog.subtract(eye);
        translate(eye, .3, 0);
        dog.subtract(eye);
        Area snout = new Area(circle);
        scale(snout, .30, .30);
        translate(snout, 0, .2);
        dog.subtract(snout);
        Area nose = new Area(createRegularPoly(3));
        rotate(nose, 180);
        scale(nose, .20, .20);
        translate(nose, 0, .2);
        dog.add(nose);
        scale(dog, 0.90, 1.0);
        return normalize(dog);
    }

    public static Shape createFishShape() {
        Area fish = new Area();
        Area body = new Area(new Arc2D.Double(0.0, 0, 1.0, 1.0, 30, 120, Arc2D.CHORD));
        Rectangle2D bounds = body.getBounds2D();
        translate(body, -(bounds.getX() + bounds.getWidth() / 2), -bounds.getHeight());
        fish.add(body);
        scale(body, 1, -1);
        fish.add(body);
        Area eye = new Area(scale(circle, .13, .13));
        eye.subtract(new Area(scale(circle, .08, .08)));
        translate(eye, -.15, -.08);
        fish.subtract(eye);
        Area tail = new Area(normalize(rotate(triangle, 30)));
        scale(tail, .50, .50);
        translate(tail, .4, 0);
        fish.add(tail);
        return normalize(fish);
    }

    public static Shape createRegularPoly(int edges) {
        double radius = 1000;
        double theta = 0.75 * (2 * Math.PI);
        double dTheta = (2 * Math.PI) / edges;
        Polygon p = new Polygon();
        for (int edge = 0; edge < edges; ++edge) {
            p.addPoint((int) (Math.cos(theta) * radius), (int) (Math.sin(theta) * radius));
            theta += dTheta;
        }
        return normalize(p);
    }

    public static Shape createStar(int points) {
        double radius = 1000;
        double theta = 0.75 * (2 * Math.PI);
        double dTheta = (4 * Math.PI) / points;
        Polygon p = new Polygon();
        for (int point = 0; point < points; ++point) {
            p.addPoint((int) (Math.cos(theta) * radius), (int) (Math.sin(theta) * radius));
            theta += dTheta;
        }
        GeneralPath gp = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        gp.append(p, true);
        return normalize(gp);
    }

    public static Shape normalize(Shape shape) {
        Rectangle2D bounds = shape.getBounds2D();
        shape = translate(shape, -(bounds.getX() + bounds.getWidth() / 2), -(bounds.getY() + bounds.getHeight() / 2));
        bounds = shape.getBounds2D();
        double scale = bounds.getWidth() > bounds.getHeight() ? 1.0 / bounds.getWidth() : 1.0 / bounds.getHeight();
        return scale(shape, scale, scale);
    }

    public static Shape rotate(Shape shape, double degrees) {
        return AffineTransform.getRotateInstance(degrees / 180 * Math.PI).createTransformedShape(shape);
    }

    public static Shape translate(Shape shape, double x, double y) {
        return AffineTransform.getTranslateInstance(x, y).createTransformedShape(shape);
    }

    public static Shape scale(Shape shape, double x, double y) {
        return AffineTransform.getScaleInstance(x, y).createTransformedShape(shape);
    }

    public static Area rotate(Area area, double degrees) {
        area.transform(AffineTransform.getRotateInstance(degrees / 180 * Math.PI));
        return area;
    }

    public static Area translate(Area area, double x, double y) {
        area.transform(AffineTransform.getTranslateInstance(x, y));
        return area;
    }

    public static Area scale(Area area, double x, double y) {
        area.transform(AffineTransform.getScaleInstance(x, y));
        return area;
    }

    public Shape paintShape(Shape shape, Graphics2D g, double x, double y) {
        shape = translate(scale(shape, paintScale, paintScale), x, y);
        g.fill(shape);
        return shape;
    }
}
