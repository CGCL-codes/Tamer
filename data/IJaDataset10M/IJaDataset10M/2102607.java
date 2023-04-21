package clutrfree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PiePanel extends JPanel {

    private static final long serialVersionUID = 7216756845861530224L;

    private int width, height;

    private ChartPies chart;

    private int diameterx, diametery;

    public PiePanel(ChartPies chart) {
        this.chart = chart;
        this.width = 600;
        this.height = 300;
        diameterx = 120;
        diametery = 120;
        this.setPreferredSize(new Dimension(width, height));
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g2);
        setBackground(Color.white);
        chart.draw(g2, width / 2 - diameterx / 2, height / 2 - diametery / 2, diameterx, diametery);
    }

    /**
     * Save the panel into a picture
     */
    public void exportPicture(String fileName, String extension) throws IOException {
        int hei = 1000;
        int wid = 2000;
        BufferedImage outputImage = new BufferedImage(wid, hei, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2 = (Graphics2D) outputImage.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.white);
        g2.fill(new Rectangle2D.Double(0, 0, wid, hei));
        paintComponent(g2);
        if (extension.equals("png") || extension.equals("jpg")) {
            File f = new File(fileName);
            ImageIO.write(outputImage, extension, f);
        }
        g2.dispose();
    }

    /**
     * zoom In
     */
    public void zoomIn() {
        System.out.println("zoom in");
        diameterx += 20;
        diametery += 20;
        this.width += 20;
        this.height += 20;
        this.setPreferredSize(new Dimension(width, height));
        this.repaint();
        this.revalidate();
    }

    /**
     * zoom Out
     */
    public void zoomOut() {
        diameterx -= 20;
        diametery -= 20;
        this.width -= 20;
        this.height -= 20;
        this.setPreferredSize(new Dimension(width, height));
        this.repaint();
        this.revalidate();
    }

    public void update(ChartPies chart) {
        this.chart = chart;
        this.repaint();
    }
}
