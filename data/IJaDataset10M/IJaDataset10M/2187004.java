package htmlrendererprototype;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

/**
 *
 * @author hamish
 */
public class Renderer {

    JFrame frame = null;

    JPanel panel;

    JScrollBar scroll;

    double scale = 1;

    public Renderer() {
    }

    public void doStuff(Node root) {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        panel = new ThingPanel(root);
        panel.setSize(600, 600);
        panel.setVisible(true);
        scroll = new JScrollBar(JScrollBar.VERTICAL);
        scroll.addAdjustmentListener(new AdjustmentListener() {

            public void adjustmentValueChanged(AdjustmentEvent e) {
                panel.repaint();
            }
        });
        frame.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    scale *= 0.95;
                    panel.repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    scale *= 1d / 0.95;
                    panel.repaint();
                }
            }
        });
        frame.getContentPane().add(scroll, BorderLayout.EAST);
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    class ThingPanel extends JPanel {

        private Node root;

        private Image im;

        public ThingPanel(Node root) {
            this.root = root;
            im = null;
        }

        @Override
        public void setBounds(int x, int y, int width, int height) {
            if (getWidth() != width) im = null;
            super.setBounds(x, y, width, height);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHints(Config.getRenderingHints());
            if (im == null) im = getImage(root, getWidth());
            int y = (im.getHeight(null) * scroll.getValue()) / scroll.getMaximum();
            AffineTransform at = AffineTransform.getScaleInstance(scale, scale);
            g2.transform(at);
            g2.drawImage(im, 0, -y, null);
        }
    }

    public Image getImage(Node root, int width) {
        xpos = 0;
        ypos = 0;
        RenderTransform rt = new RenderTransform(width);
        BufferedImage im = new BufferedImage(width, 10000, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();
        g.setRenderingHints(Config.getRenderingHints());
        paintNode(g, root, rt);
        return im.getSubimage(0, 0, im.getWidth(), ypos + 20);
    }

    public void paintNode(Graphics2D g, Node n, RenderTransform rt) {
        if (n instanceof TextNode) {
            paintString(g, (TextNode) n, rt);
        } else if (n instanceof TagNode) {
            paintBlock(g, (TagNode) n, rt);
        }
    }

    int xpos = 0;

    int ypos = 0;

    public void paintString(Graphics2D g, TextNode n, RenderTransform rt) {
        String str = n.str;
        g.setFont(rt.getFont());
        g.setColor(rt.colour);
        FontMetrics fm = g.getFontMetrics();
        int w = fm.stringWidth(str);
        int h = fm.getHeight();
        if (xpos < rt.getIndent()) xpos = rt.getIndent();
        if (xpos > rt.getIndent() && xpos + w > rt.maxWidth - 40) {
            ypos += h;
            xpos = rt.getIndent();
        }
        if (str.equals("_")) if (xpos == rt.getIndent()) return; else str = " ";
        g.drawString(str, xpos + rt.xoffset + 20, ypos + rt.yoffset + 20);
        if (rt.underline) {
            g.setStroke(new BasicStroke(1));
            g.drawLine((int) (xpos + rt.xoffset + 20), (int) (ypos + rt.yoffset + 20), (int) (xpos + rt.xoffset + 20 + w), (int) (ypos + rt.yoffset + 20));
        }
        xpos += w;
    }

    public void paintBlock(Graphics2D g, TagNode n, RenderTransform parentRT) {
        RenderTransform rt = parentRT.clone();
        if (n.getName().equals("TITLE")) {
            StringBuffer buf = new StringBuffer();
            for (Node c : n.getChildren()) {
                buf.append(((TextNode) c).str);
            }
            frame.setTitle(buf.toString());
            return;
        }
        if (n.getName().equals("BR") || n.getName().equals("P")) {
            ypos += g.getFontMetrics().getHeight();
            xpos = 0;
        } else if (n.getName().equals("B") || n.getName().equals("STRONG")) {
            rt.bold = true;
        } else if (n.getName().equals("EM") || n.getName().equals("I")) {
            rt.italic = true;
        } else if (n.getName().equals("CODE") || n.getName().equals("TT")) {
            rt.fontFamily = "Courier";
            rt.fontSize -= 2;
        } else if (n.getName().equals("FONT")) {
            if (n.hasProperty("SIZE")) {
                String sizeStr = n.getProperty("SIZE");
                if (sizeStr.charAt(0) == '-') {
                    float newSize = Integer.parseInt(sizeStr);
                    rt.fontSize += newSize;
                } else if (sizeStr.charAt(0) == '+') {
                    float newSize = Integer.parseInt(sizeStr.substring(1));
                    rt.fontSize += newSize;
                } else {
                    float newSize = Integer.parseInt(sizeStr);
                    rt.fontSize = newSize;
                }
            }
        } else if (n.getName().equals("SUB")) {
            rt.setSubscript();
        } else if (n.getName().equals("SUP")) {
            rt.setSuperscript();
        } else if (n.getName().equals("H1")) {
            rt.fontSize *= 2;
            rt.bold = true;
            xpos = 0;
            ypos += rt.fontSize;
        } else if (n.getName().equals("H2")) {
            rt.fontSize *= 1.8;
            rt.bold = true;
            xpos = 0;
            ypos += rt.fontSize;
        } else if (n.getName().equals("H3")) {
            rt.fontSize *= 1.6;
            rt.bold = true;
            xpos = 0;
            ypos += rt.fontSize;
        } else if (n.getName().equals("H4")) {
            rt.fontSize *= 1.4;
            rt.bold = true;
            xpos = 0;
            ypos += rt.fontSize;
        } else if (n.getName().equals("H5")) {
            rt.fontSize *= 1.2;
            xpos = 0;
            ypos += rt.fontSize;
        } else if (n.getName().equals("H6")) {
        } else if (n.getName().equals("A")) {
            rt.underline = true;
            rt.colour = Color.BLUE;
        } else if (n.getName().equals("HR")) {
            xpos = 0;
            ypos += 2 * rt.fontSize;
            g.setColor(Color.DARK_GRAY);
            g.setStroke(new BasicStroke(2));
            g.drawLine(20, ypos, rt.maxWidth - 40, ypos);
            ypos += rt.fontSize;
        } else if (n.getName().equals("OL") || n.getName().equals("DL") || n.getName().equals("BLOCKQUOTE")) {
            rt.increaseIndent(20);
        } else if (n.getName().equals("LI")) {
        } else if (n.getName().equals("TABLE")) {
        }
        for (Node c : n.getChildren()) {
            paintNode(g, c, rt);
        }
        if (n.getName().equals("H1") || n.getName().equals("H2") || n.getName().equals("H3") || n.getName().equals("H4") || n.getName().equals("H5") || n.getName().equals("H6") || n.getName().equals("TABLE") || n.getName().equals("TR") || n.getName().equals("LI") || n.getName().equals("DD") || n.getName().equals("DT") || n.getName().equals("PRE")) {
            ypos += rt.fontSize;
            xpos = 0;
        }
    }

    private void paintTable(Graphics2D g, TagNode table, RenderTransform parentRT) {
        float[] colWidths = getTableColumnWidths(table, parentRT.maxWidth);
        System.out.println(Arrays.toString(colWidths));
        List<Node> prefix = new ArrayList<Node>();
        for (Node x : table.getChildren()) {
            if (!(x instanceof TagNode)) {
                prefix.add(x);
                continue;
            }
            TagNode row = ((TagNode) x);
            if (!row.getName().equals("TR")) {
                prefix.add(x);
                continue;
            }
            int i = -1;
            for (Node y : row.getChildren()) {
                i++;
                if (!(y instanceof TagNode)) {
                    prefix.add(y);
                    continue;
                }
                TagNode col = ((TagNode) y);
                if (!(col.getName().equals("TD") || col.getName().equals("TH"))) {
                    prefix.add(y);
                    continue;
                }
                RenderTransform rt = parentRT.clone();
                if (i > 0) rt.increaseIndent((int) colWidths[i - 1]);
                rt.maxWidth = (int) colWidths[i];
                paintBlock(g, col, rt);
            }
        }
    }

    private float[] getTableColumnWidths(TagNode table, int maxWidth) {
        assert table.getName().equals("TABLE");
        List<List<Float>> rows = new ArrayList<List<Float>>();
        for (Node x : table.getChildren()) {
            if (!(x instanceof TagNode)) continue;
            TagNode row = ((TagNode) x);
            if (!row.getName().equals("TR")) continue;
            List<Float> cols = new ArrayList<Float>();
            for (Node y : row.getChildren()) {
                if (!(y instanceof TagNode)) continue;
                TagNode col = ((TagNode) y);
                if (!(col.getName().equals("TD") || col.getName().equals("TH"))) continue;
                float colWidth = getWidth(y);
                if (col.hasProperty("COLSPAN")) {
                    int span = Integer.parseInt(col.getProperty("COLSPAN"));
                    for (int i = 0; i < span; i++) cols.add(colWidth / span);
                } else {
                    cols.add(colWidth);
                }
            }
            rows.add(cols);
        }
        int numCols = 0;
        for (List<Float> row : rows) {
            if (row.size() > numCols) numCols = row.size();
        }
        float[] colWidths = new float[numCols];
        for (List<Float> row : rows) {
            for (int i = 0; i < Math.min(numCols, row.size()); i++) {
                if (row.get(i) > colWidths[i]) colWidths[i] = row.get(i);
            }
        }
        float sum = 0;
        for (int i = 0; i < colWidths.length; i++) {
            sum += colWidths[i];
        }
        float mult = maxWidth / sum;
        for (int i = 0; i < colWidths.length; i++) {
            colWidths[i] = mult * colWidths[i];
        }
        return colWidths;
    }

    private int getWidth(Node n) {
        return 1;
    }
}
