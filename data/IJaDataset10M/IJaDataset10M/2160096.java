package go.gui;

import go.board.gui.WoodTexture;
import go.board.gui.image.BlackStone;
import go.board.gui.image.GoBufferedImage;
import go.board.gui.image.WhiteStone;
import go.GoColor;
import go.GoGame;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import sgf.GameTree;
import sgf.GoNode;
import sgf.property.MoveProperty;
import sgf.property.NoTypeProperty;

public class TreePanel extends JPanel implements ComponentListener, MouseListener {

    private static final int SIZE = 20;

    private static final int TOP = 1;

    private static final int BOTTOM = 2;

    private static final int RIGHT = 4;

    private static final int LEFT = 8;

    private BufferedImage bgImage;

    private BufferedImage baseImage;

    private BufferedImage[] line;

    private BufferedImage black;

    private BufferedImage white;

    private BufferedImage emptyNode;

    private WoodTexture texture;

    private BufferedImage cursor;

    private BufferedImage gameInfo;

    private BufferedImage commentBlack;

    private BufferedImage commentWhite;

    private BufferedImage pivot;

    private int dx, dy;

    private ViewPort viewport;

    private GoGame goGame;

    public TreePanel(GoGame goGame) {
        this.goGame = goGame;
        addComponentListener(this);
        line = new BufferedImage[16];
        line[0] = null;
        line[TOP] = createLineImage(true, false, false, false);
        line[BOTTOM] = createLineImage(false, true, false, false);
        line[TOP + BOTTOM] = createLineImage(true, true, false, false);
        line[RIGHT] = createLineImage(false, false, true, false);
        line[TOP + RIGHT] = createLineImage(true, false, true, false);
        line[BOTTOM + RIGHT] = createLineImage(false, true, true, false);
        line[TOP + BOTTOM + RIGHT] = createLineImage(true, true, true, false);
        line[LEFT] = createLineImage(false, false, false, true);
        line[TOP + LEFT] = createLineImage(true, false, false, true);
        line[BOTTOM + LEFT] = createLineImage(false, true, false, true);
        line[TOP + BOTTOM + LEFT] = createLineImage(true, true, false, true);
        line[RIGHT + LEFT] = createLineImage(false, false, true, true);
        line[TOP + RIGHT + LEFT] = createLineImage(true, false, true, true);
        line[BOTTOM + RIGHT + LEFT] = createLineImage(false, true, true, true);
        line[TOP + BOTTOM + RIGHT + LEFT] = createLineImage(true, true, true, true);
        black = createStoneImage(new BlackStone());
        white = createStoneImage(new WhiteStone());
        BasicStroke stroke = new BasicStroke(1.0f);
        RenderingHints rhOn = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        RenderingHints rhOff = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        emptyNode = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gEmptyNode = emptyNode.createGraphics();
        gEmptyNode.setPaint(new Color(0, 0, 0, 0));
        gEmptyNode.fill(new Rectangle(SIZE, SIZE));
        gEmptyNode.setPaint(Color.BLACK);
        gEmptyNode.setRenderingHints(rhOn);
        gEmptyNode.setStroke(stroke);
        double SPACE = 4;
        gEmptyNode.draw(new Ellipse2D.Double(SPACE, SPACE, SIZE - SPACE * 2 - 1, SIZE - SPACE * 2 - 1));
        texture = WoodTexture.getInstance();
        cursor = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gCursor = cursor.createGraphics();
        gCursor.setPaint(new Color(0, 0, 0, 0));
        gCursor.fill(new Rectangle(SIZE, SIZE));
        gCursor.setPaint(Color.RED);
        gCursor.setRenderingHints(rhOff);
        gCursor.setStroke(stroke);
        gCursor.draw(new Rectangle2D.Double(0, 0, SIZE - 1, SIZE - 1));
        {
            gameInfo = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gGameInfo = gameInfo.createGraphics();
            gGameInfo.setPaint(new Color(0, 0, 0, 0));
            gGameInfo.fill(new Rectangle(SIZE, SIZE));
            gGameInfo.setPaint(Color.BLACK);
            gGameInfo.setStroke(stroke);
            gGameInfo.draw(new Rectangle2D.Double(2, 2, SIZE - 2 - 2 - 1, SIZE - 2 - 2 - 1));
        }
        {
            Font font = new Font(Font.SANS_SERIF, Font.BOLD, (int) (SIZE * 0.45));
            RenderingHints rhText = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            commentBlack = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gCB = commentBlack.createGraphics();
            gCB.setColor(Color.BLACK);
            gCB.setRenderingHints(rhText);
            gCB.setFont(font);
            FontRenderContext frcCB = gCB.getFontRenderContext();
            TextLayout layoutCB = new TextLayout("C", font, frcCB);
            layoutCB.draw(gCB, SIZE * 0.35f, SIZE * 0.65f);
            commentWhite = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gCW = commentWhite.createGraphics();
            gCW.setColor(Color.WHITE);
            gCW.setRenderingHints(rhText);
            gCW.setFont(font);
            FontRenderContext frcCW = gCW.getFontRenderContext();
            TextLayout layoutCW = new TextLayout("C", font, frcCW);
            layoutCW.draw(gCW, SIZE * 0.35f, SIZE * 0.65f);
        }
        {
            pivot = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gPivot = pivot.createGraphics();
            gPivot.setPaint(new Color(0, 0, 0, 0));
            gPivot.fill(new Rectangle(SIZE, SIZE));
            gPivot.setPaint(Color.RED);
            Polygon polygon = new Polygon();
            polygon.addPoint((int) (SIZE * 0.9), (int) (SIZE * 0.4));
            polygon.addPoint((int) (SIZE * 0.5), (int) (SIZE * 0.4));
            polygon.addPoint((int) (SIZE * 0.7), (int) (SIZE * 0.8));
            gPivot.fill(polygon);
        }
        viewport = new ViewPort(10, 10);
        this.addMouseListener(this);
    }

    private BufferedImage createStoneImage(GoBufferedImage goImage) {
        int d = 3;
        goImage.setSize(SIZE - (d * 2));
        BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHints(rh);
        g.setPaint(new Color(0, 0, 0, 0));
        g.fill(new Rectangle(SIZE, SIZE));
        g.drawImage(goImage.getBufferedImage(), null, d, d);
        return image;
    }

    private BufferedImage createLineImage(boolean top, boolean bottom, boolean right, boolean left) {
        BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        int L = 3;
        Graphics2D g = image.createGraphics();
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHints(rh);
        g.setPaint(new Color(0, 0, 0, 0));
        g.fill(new Rectangle(SIZE, SIZE));
        g.setPaint(Color.BLACK);
        BasicStroke stroke = new BasicStroke(1.0f);
        g.setStroke(stroke);
        if (top) {
            g.draw(new Line2D.Double(SIZE / 2, 0, SIZE / 2, L));
        }
        if (bottom) {
            g.draw(new Line2D.Double(SIZE / 2, SIZE - 1 - L, SIZE / 2, SIZE - 1));
        }
        if (right) {
            g.draw(new Line2D.Double(SIZE - 1 - L, SIZE / 2, SIZE - 1, SIZE / 2));
        }
        if (left) {
            g.draw(new Line2D.Double(0, SIZE / 2, L, SIZE / 2));
        }
        return image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        int w = getWidth();
        int h = getHeight();
        if (baseImage == null) {
            return;
        }
        int iw = baseImage.getWidth();
        int ih = baseImage.getHeight();
        int xc = viewport.getCurrentX();
        int yc = viewport.getCurrentY();
        if (xc * SIZE + SIZE / 2 < w / 2) {
            dx = 0;
        } else {
            dx = (iw - w) / 2;
        }
        if (yc * SIZE + SIZE / 2 < h / 2) {
            dy = 0;
        } else {
            dy = (ih - h) / 2;
        }
        g2.drawImage(baseImage, null, -dx, -dy);
    }

    public void update() {
        if (baseImage == null) {
            return;
        }
        GameTree tree = goGame.getGameTree();
        GoNode current = tree.getCurrentNode();
        viewport.update(current);
        int w = baseImage.getWidth();
        int h = baseImage.getHeight();
        Graphics2D g2 = baseImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2.drawImage(bgImage, null, 0, 0);
        int xn = viewport.getWidth();
        int yn = viewport.getHeight();
        for (int i = 0; i < xn; ++i) {
            for (int j = 0; j < yn; ++j) {
                int x = i * SIZE;
                int y = j * SIZE;
                GoNode n = viewport.getNode(i, j);
                if (n != null) {
                    drawNode(g2, x, y, n);
                }
            }
        }
        int xc = viewport.getCurrentX();
        int yc = viewport.getCurrentY();
        g2.drawImage(cursor, null, xc * SIZE, yc * SIZE);
        repaint();
    }

    private void drawNode(Graphics2D g, int x, int y, GoNode node) {
        int index = 0;
        if (node.hasParent() && !node.hasPrev()) {
            index += LEFT;
        }
        if (node.hasChild()) {
            index += RIGHT;
        }
        if (node.hasPrev()) {
            index += TOP;
        }
        if (node.hasNext()) {
            index += BOTTOM;
        }
        BufferedImage image = line[index];
        if (image != null) {
            g.drawImage(image, null, x, y);
        }
        if (node.hasMoveProperty()) {
            MoveProperty mp = node.getMoveProperty();
            if (mp.getMove().getColor() == GoColor.BLACK) {
                g.drawImage(black, null, x, y);
            } else {
                g.drawImage(white, null, x, y);
            }
        } else {
            g.drawImage(emptyNode, null, x, y);
        }
        if (node.hasGameInfoProperty()) {
            g.drawImage(gameInfo, null, x, y);
        }
        if (node.hasNoTypeProperty()) {
            NoTypeProperty ntp = node.getNoTypeProperty();
            if (ntp.hasComment()) {
                if (node.hasMoveProperty()) {
                    MoveProperty mp = node.getMoveProperty();
                    if (mp.getMove().getColor() == GoColor.BLACK) {
                        g.drawImage(commentWhite, null, x, y);
                    } else {
                        g.drawImage(commentBlack, null, x, y);
                    }
                } else {
                    g.drawImage(commentBlack, null, x, y);
                }
            }
        }
        if (goGame.getGameTree().checkPivot(node)) {
            g.drawImage(pivot, null, x, y);
        }
    }

    private void createBaseImage() {
        int wPanel = getWidth();
        int hPanel = getHeight();
        int nx = wPanel / SIZE + 1;
        int ny = hPanel / SIZE + 1;
        if (nx % 2 == 0) {
            nx += 1;
        }
        if (ny % 2 == 0) {
            ny += 1;
        }
        System.out.println("TreePanel :w, h:" + wPanel + "," + hPanel + " :nx,ny:" + nx + "," + ny);
        viewport = new ViewPort(nx, ny);
        int w = nx * SIZE;
        int h = ny * SIZE;
        bgImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        baseImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bgImage.createGraphics();
        texture.draw(g2, 0, 0, w, h);
        update();
        repaint();
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        System.out.println("TreePanel.componentResized");
        createBaseImage();
        repaint();
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int xn = (x + dx) / SIZE;
        int yn = (y + dy) / SIZE;
        GoNode n = viewport.getNode(xn, yn);
        if (n != null) {
            if (n.hasMoveProperty()) {
                MoveProperty mp = n.getMoveProperty();
                System.out.println("TreePanel:node:" + mp.getMove().toSgfString());
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    /**
     * 表示範囲にあるノードを二次元配列として保持する
     */
    class ViewPort {

        private int w, h;

        private GoNode[][] view;

        private int xc, yc;

        public ViewPort(int w, int h) {
            resize(w, h);
        }

        public void resize(int w, int h) {
            this.w = w;
            this.h = h;
            view = new GoNode[w][h];
            clear();
        }

        public void clear() {
            for (int i = 0; i < w; ++i) {
                for (int j = 0; j < h; ++j) {
                    view[i][j] = null;
                }
            }
        }

        public GoNode getNode(int x, int y) {
            return view[x][y];
        }

        public int getCurrentX() {
            return xc;
        }

        public int getCurrentY() {
            return yc;
        }

        public int getWidth() {
            return w;
        }

        public int getHeight() {
            return h;
        }

        public void update(GoNode current) {
            clear();
            int xDepth = current.getDepthX();
            int yDepth = current.getDepthY();
            if (w / 2 < xDepth) {
                xc = w / 2;
            } else {
                xc = xDepth;
            }
            if (h / 2 < yDepth) {
                yc = h / 2;
            } else {
                yc = yDepth;
            }
            view[xc][yc] = current;
            paintChildNode(current, xc, yc);
            paintSiblingNode(current, xc, yc);
            paintParentNode(current, xc, yc);
        }

        /**
         * 位置 (x0, y0) にあるノード node の子ノードをたどって配列にメモる<br>
         * メモるのは子ノードの兄弟の一番上だけ。
         * @param node
         * @param x0
         * @param y0
         */
        private void paintChildNode(GoNode node, int x0, int y0) {
            GoNode n = node;
            int x = x0;
            int y = y0;
            while (n.hasChild()) {
                n = n.getChild();
                x += 1;
                if (x < w) {
                    view[x][y] = n;
                } else {
                    return;
                }
            }
        }

        /**
         * 位置 (x0, y0) にあるノード node の兄弟ノードをたどって配列にメモる<br>
         * y0 は 0 以下の場合がある。そうしないと親の下の兄弟が表示されない。
         * @param node
         * @param x0
         * @param y0
         */
        private void paintSiblingNode(GoNode node, int x0, int y0) {
            GoNode n = node;
            int x = x0;
            int y = y0;
            while (n.hasNext()) {
                n = n.getNext();
                y += 1;
                if (y < h) {
                    if (y >= 0) {
                        view[x][y] = n;
                    }
                } else {
                    break;
                }
            }
            n = node;
            x = x0;
            y = y0;
            while (n.hasPrev()) {
                n = n.getPrev();
                y -= 1;
                if (y >= 0) {
                    view[x][y] = n;
                } else {
                    break;
                }
            }
        }

        /**
         * 位置 (x0, y0) にあるノード node の親ノードをたどって配列にメモる<br>
         * y0 は 0 以下の場合がある。そうしないと親の下の兄弟が表示されない。
         * @param node
         * @param x0
         * @param y0
         */
        private void paintParentNode(GoNode node, int x0, int y0) {
            GoNode n = node;
            int x = x0;
            int y = y0;
            while (n.hasPrev()) {
                n = n.getPrev();
                y -= 1;
            }
            if (n.hasParent()) {
                n = n.getParent();
                x -= 1;
                if (x >= 0) {
                    if (y >= 0) {
                        view[x][y] = n;
                    }
                    paintSiblingNode(n, x, y);
                    paintParentNode(n, x, y);
                }
            }
        }

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            for (int j = 0; j < h; ++j) {
                for (int i = 0; i < w; ++i) {
                    GoNode n = view[i][j];
                    if (n == null) {
                        str.append(".");
                    } else {
                        str.append("#");
                    }
                }
                str.append("\n");
            }
            return str.toString();
        }
    }
}
