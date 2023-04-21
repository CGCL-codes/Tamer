package xhack.ui.basic;

import xhack.ui.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.Timer;
import xhack.game.*;
import xhack.menu.*;
import xhack.object.*;
import xhack.object.Map;
import xhack.util.*;

/**
 * Container panel for display areas -- map, messages, status and menu.  Each
 * area is represented by a subclass of {@link xhack.ui.basic.Panel}:
 * {@link xhack.ui.basic.MapPanel}, {@link xhack.ui.basic.MessagePanel},
 * {@link xhack.ui.basic.StatusPanel} and {@link xhack.ui.basic.MenuPanel}.
 */
public class MainPanel extends JComponent {

    protected MapPanel mapPanel;

    protected StatusPanel statusPanel;

    protected MessagePanel messagePanel;

    protected MenuPanel menuPanel;

    protected Panel borderedMap;

    protected Panel borderedMenu;

    protected Panel borderedStatus;

    protected Panel borderedMessage;

    public static final int STATUS_LINES = 30;

    protected Map map;

    protected HackMenu menu;

    public final Color foreground = Colours.colours[Colours.GRAY];

    public final Color background = Colours.colours[Colours.BLACK];

    protected boolean statusChanged = false;

    protected boolean menuChanged = false;

    protected boolean messageChanged = false;

    protected boolean playerMoved = false;

    protected double statusWidth = 130;

    protected double messageHeight = 80;

    protected double edgeOffset = 20;

    protected float borderWidth = 3;

    protected float cornerArc = 20;

    protected FadeRunner fade;

    protected boolean showMenu = false;

    protected Vector effects = new Vector();

    protected Vector effectQ;

    protected Timer timer;

    protected BasicUI frame;

    public MainPanel(BasicUI frame) throws HeadlessException {
        this.frame = frame;
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void jbInit() throws java.lang.Exception {
        setSize(Preferences.getDisplayWidth(), Preferences.getDisplayHeight());
        this.setPreferredSize(new Dimension(getWidth(), getHeight()));
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map m) {
        map = m;
        mapPanel.setMap(m);
    }

    public void setMenu(HackMenu m) {
        menu = m;
        menuPanel.setMenu(m);
    }

    public MenuPanel getMenuPanel() {
        return menuPanel;
    }

    public void setWorld(World w) {
        statusPanel.setWorld(w);
    }

    private void init() throws Exception {
        setSize(Preferences.getDisplayWidth(), Preferences.getDisplayHeight());
        fade = new FadeRunner();
        mapPanel = new MapPanel(map);
        borderedMap = new BorderedPanel(mapPanel);
        menuPanel = new MenuPanel(menu, frame);
        borderedMenu = new BorderedPanel(menuPanel);
        statusPanel = new StatusPanel(frame.getGame().getWorld());
        borderedStatus = new BufferedPanel(statusPanel);
        messagePanel = new MessagePanel();
        borderedMessage = new BufferedPanel(messagePanel);
        this.setPreferredSize(new Dimension(getWidth(), getHeight()));
    }

    public void zoomIn(int factor) {
        mapPanel.zoomIn(factor);
        mapChanged();
        redraw();
    }

    public void zoomOut(int factor) {
        mapPanel.zoomOut(factor);
        mapChanged();
        redraw();
    }

    public void zoomIn() {
        mapPanel.zoomIn(1);
        mapChanged();
        redraw();
    }

    public void zoomOut() {
        mapPanel.zoomOut(1);
        mapChanged();
        redraw();
    }

    public void shift(int dir) {
        mapPanel.shift(dir);
        mapChanged();
        redraw();
    }

    /**
   * Shift the map display so that the player can be seen
   *
   */
    public void shiftToPlayer() {
        mapPanel.shiftToPlayer();
        mapChanged();
        playerMoved = false;
    }

    public void toggleTileUI() {
        Preferences.setTextOnly(!Preferences.getTextOnly());
        mapPanel.initTiles();
        mapChanged();
        redraw();
    }

    public void printMessage(String m, Color c) {
        messagePanel.printMessage(m, c);
        messageChanged();
    }

    public void test() {
        animateTimer(0, 0, 10, 10, Tiles.findTileID('@', Colours.WHITE));
    }

    /**
   * Calculate the width of the map area
   * @return int
   */
    public int getMapWidth() {
        return (int) (this.getBounds().getWidth() - statusWidth - 3 * edgeOffset);
    }

    /**
   * Calculate the height of the map area
   * @return int
   */
    public int getMapHeight() {
        return (int) (this.getBounds().getHeight() - messageHeight - 2 * edgeOffset);
    }

    public void redraw() {
        if (playerMoved) shiftToPlayer();
        repaint();
    }

    public void showMenu(boolean b) {
        menuChanged();
        if (b) {
            showMenu = true;
            fade.up();
        } else fade.down();
        redraw();
    }

    public void showMenu(HackMenu m) {
        if (m != null) {
            if (menu == null) {
                fade.up();
            }
            menu = m;
            menuPanel = new MenuPanel(m, frame);
            borderedMenu = new BorderedPanel(menuPanel);
            showMenu = true;
            menuChanged();
        } else {
            if (map == null) frame.ip.setSplashContext(); else frame.ip.setGameContext();
            fade.down();
        }
        redraw();
    }

    public void toggleMenu() {
        showMenu = !showMenu;
        if (showMenu) {
            showMenu(true);
        } else if (map == null) {
        } else {
            showMenu(false);
        }
    }

    public void menuChanged() {
        menuChanged = true;
        borderedMenu.changed();
    }

    public void mapChanged() {
        playerMoved = true;
        borderedMap.changed();
    }

    public void statusChanged() {
        statusChanged = true;
        borderedStatus.changed();
    }

    public void messageChanged() {
        messageChanged = true;
        borderedMessage.changed();
    }

    public void menuFade() {
        fade.start();
    }

    public int getFrameWidth() {
        return this.getBounds().width;
    }

    public int getFrameHeight() {
        return this.getBounds().height;
    }

    public void resize() {
        this.setSize(getFrameWidth(), getFrameHeight());
    }

    public void paint(Graphics g) {
        Log.log.begin(DefaultLogger.PAINTING, "");
        Graphics2D g2 = (Graphics2D) g;
        FontRenderContext frc = g2.getFontRenderContext();
        Dimension d = getSize();
        int w = (int) d.getWidth();
        int h = (int) d.getHeight();
        g2.setPaint(background);
        g2.fillRect(0, 0, w, h);
        if (map == null) {
            g2.drawImage(Tiles.getSplashImage(), null, (getFrameWidth() - Tiles.SPLASH_WIDTH) / 2, (getFrameHeight() - Tiles.SPLASH_HEIGHT) / 2);
        } else {
            paintMap(g2);
            paintStatus(g2);
            paintMessages(g2);
        }
        if (showMenu) paintMenu(g2, frc);
        Log.log.done();
    }

    private void paintMessages(Graphics2D g2) {
        Log.log.begin(DefaultLogger.PAINTING, "messages");
        BufferedImage messageImage = borderedMessage.draw(getWidth(), (int) messageHeight);
        g2.drawImage(messageImage, null, 0, getMapHeight());
        Log.log.done();
    }

    private void paintStatus(Graphics2D g2) {
        Log.log.begin(DefaultLogger.PAINTING, "status");
        BufferedImage statusImage = borderedStatus.draw(getWidth() - getMapWidth(), getMapHeight());
        g2.drawImage(statusImage, null, getMapWidth(), 0);
        Log.log.done();
    }

    private void paintMap(Graphics2D g2) {
        Log.log.begin(DefaultLogger.PAINTING, "map");
        BufferedImage mapImage = borderedMap.draw(getMapWidth(), getMapHeight());
        g2.drawImage(mapImage, null, 0, 0);
        Log.log.done();
    }

    private void paintMenu(Graphics2D g2, FontRenderContext frc) {
        Log.log.begin(DefaultLogger.PAINTING, "menu");
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fade.alpha);
        g2.setComposite(ac);
        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;
        BufferedImage menuImage = borderedMenu.draw(getWidth(), getHeight());
        float w = menuPanel.getWidth();
        float h = menuPanel.getHeight();
        float topX = centerX - w / 2;
        float topY = centerY - h / 2;
        g2.drawImage(menuImage, null, (int) topX, (int) topY);
        Log.log.done();
    }

    class FadeRunner implements Runnable {

        private static final int DOWN = 0;

        private static final int UP = 1;

        private static final float ALPHA_INC = 0.05f;

        Thread thread = null;

        float alpha;

        int alphaDirection = UP;

        public void start() {
            alpha = 0.0f;
            thread = new Thread(this);
            thread.start();
        }

        public void up() {
            alphaDirection = UP;
            alpha = 0;
            start();
        }

        public void down() {
            alphaDirection = DOWN;
            alpha = 1.0f;
            start();
        }

        public synchronized void stop() {
            thread = null;
        }

        public boolean adjustAlpha() {
            if (alphaDirection == UP) {
                if ((alpha += ALPHA_INC) > .99) {
                    alpha = 1.0f;
                    return false;
                } else return true;
            } else if (alphaDirection == DOWN) {
                if ((alpha -= ALPHA_INC) < 0.01) {
                    alpha = 0;
                    return false;
                } else return true;
            }
            return false;
        }

        public void run() {
            Thread me = Thread.currentThread();
            while (thread == me) {
                repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    break;
                }
                if (!adjustAlpha()) break;
            }
            thread = null;
            if (alphaDirection == DOWN) {
                showMenu = false;
            }
        }
    }

    /**
   * Used with AnimateTimer()
   * @param row int
   * @param col int
   * @param row2 int
   * @param col2 int
   * @param tileID int
   * @return Vector
   */
    private Vector createAnimationQ(int row, int col, int row2, int col2, int tileID) {
        Vector effectQ = new Vector();
        int rise = row2 - row;
        int run = col2 - col;
        float slope = Math.min(Math.abs((float) rise / (float) run), Math.abs((float) run / (float) rise));
        float partial = 0;
        int rowInc = 1;
        if (rise < 0) rowInc = -1;
        int colInc = 1;
        if (run < 0) colInc = -1;
        rise = Math.abs(rise);
        run = Math.abs(run);
        while (row >= 0 && row < map.rows() && col >= 0 && col < map.cols() && (row != row2 || col != col2)) {
            int effect[] = new int[3];
            effect[0] = row;
            effect[1] = col;
            effect[2] = tileID;
            effectQ.add(effect);
            partial += slope;
            if (run >= rise) col += colInc; else row += rowInc;
            if (partial >= 0.99) {
                partial -= 1;
                if (run >= rise) row += rowInc; else col += colInc;
            }
        }
        return effectQ;
    }

    public void animateTimer(int startRow, int startCol, int endRow, int endCol, int tileID) {
        effectQ = createAnimationQ(startRow, startCol, endRow, endCol, tileID);
        timer = new Timer(50, new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (!effectQ.isEmpty()) {
                    effects.clear();
                    effects.add(effectQ.remove(0));
                    repaint();
                } else {
                    timer.stop();
                    repaint();
                    effects.clear();
                    frame.unlock();
                }
            }
        });
        frame.lock();
        timer.start();
    }

    class TestRunner implements Runnable {

        Thread thread = null;

        public void start() {
            thread = new Thread(this);
            thread.start();
        }

        public synchronized void stop() {
            thread = null;
        }

        public void run() {
        }
    }
}
