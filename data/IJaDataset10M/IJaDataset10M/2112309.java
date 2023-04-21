package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author Adam
 */
public class MotionGraphPanel extends JPanel {

    private BufferedImage buffImage = null;

    private double boundsWidth, boundsHeight;

    private double circleDiameter, circleRadius;

    private double xCenter, yCenter;

    private double xOld, yOld;

    private double xOut, yOut, zOut;

    private boolean zAxisExist = false;

    private Ellipse2D.Double oneGCircle;

    public MotionGraphPanel() {
        this.setBorder(javax.swing.BorderFactory.createTitledBorder("Motion Graph"));
        this.setPreferredSize(new Dimension(317, 317));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.buffImage == null) {
            this.buffImage = (BufferedImage) this.createImage((int) (this.getBounds().getWidth() - 2), (int) (this.getBounds().getHeight() - 2));
            this.boundsWidth = this.buffImage.getWidth();
            this.boundsHeight = this.buffImage.getHeight();
            this.xCenter = this.boundsWidth / 2;
            this.yCenter = this.boundsHeight / 2;
            this.xOld = this.xCenter;
            this.yOld = this.yCenter;
            this.circleDiameter = this.boundsWidth / 2;
            this.circleRadius = this.circleDiameter / 2;
            this.oneGCircle = new Ellipse2D.Double(xCenter - circleRadius, yCenter - circleRadius, circleDiameter, circleDiameter);
        }
        Graphics2D g2d = (Graphics2D) g;
        Graphics2D drawer = this.buffImage.createGraphics();
        RenderingHints drawerHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawerHints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        drawer.setRenderingHints(drawerHints);
        drawer.clearRect(0, 0, (int) boundsWidth, (int) boundsHeight);
        drawer.setColor(Color.BLUE);
        drawer.draw(this.oneGCircle);
        xOld = xCenter - xOut * circleRadius;
        yOld = yCenter + yOut * circleRadius;
        drawer.setColor(Color.BLACK);
        drawer.draw(new Line2D.Double(xCenter, yCenter, xOld, yOld));
        if (this.zAxisExist) {
            if (this.zOut > 0) {
                drawer.setColor(Color.RED);
                drawer.draw(new Ellipse2D.Double(xCenter - (circleRadius * zOut), yCenter - (circleRadius * zOut), circleDiameter * zOut, circleDiameter * zOut));
            } else {
                drawer.setColor(Color.GREEN);
                drawer.draw(new Ellipse2D.Double(xCenter - (circleRadius * -zOut), yCenter - (circleRadius * -zOut), circleDiameter * -zOut, circleDiameter * -zOut));
            }
        }
        g2d.drawImage(buffImage, null, 1, 1);
    }

    public double getXOut() {
        return xOut;
    }

    public void setXOut(double xOut) {
        this.xOut = xOut;
    }

    public double getYOut() {
        return yOut;
    }

    public void setYOut(double yOut) {
        this.yOut = yOut;
    }

    public double getZOut() {
        return zOut;
    }

    public void setZOut(double zOut) {
        this.zOut = zOut;
    }

    public boolean isZAxisExist() {
        return zAxisExist;
    }

    public void setZAxisExist(boolean zAxisExist) {
        this.zAxisExist = zAxisExist;
    }
}
