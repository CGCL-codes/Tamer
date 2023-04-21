package buseylab.findpoint;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

public class ProgressDraw extends JLabel {

    JFrame f = new JFrame();

    Container contentPane = f.getContentPane();

    JProgressBar progressBar = new JProgressBar(0, 100);

    Point point;

    Point hint;

    BufferedImage img;

    int SIZE = 256;

    int MARKER_SIZE = 6;

    int smallSceneWidth = 512;

    int smallSceneHeight = 342;

    public ProgressDraw(BufferedImage img, Point point, Point hint) {
        super();
        this.img = img;
        this.point = point;
        this.hint = hint;
        initComponents();
        f.setVisible(true);
    }

    public void setImage(BufferedImage img) {
        setIcon(new ImageIcon(img));
        this.img = img;
        repaint();
    }

    public void setPoint(Point point) {
        this.point = point;
        repaint();
    }

    public void setHint(Point hint) {
        this.hint = hint;
        repaint();
    }

    public void setProgress(String progressString, int progress, int maximum) {
        progressBar.setString(progressString);
        progressBar.setMaximum(maximum);
        progressBar.setValue(progress);
        progressBar.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (img != null) {
            g.setColor(Color.GREEN);
            if (point != null) {
                g.drawLine(point.x - MARKER_SIZE, point.y, point.x + MARKER_SIZE, point.y);
                g.drawLine(point.x, point.y - MARKER_SIZE, point.x, point.y + MARKER_SIZE);
                g.drawLine(point.x - MARKER_SIZE, point.y - MARKER_SIZE, point.x + MARKER_SIZE, point.y + MARKER_SIZE);
                g.drawLine(point.x - MARKER_SIZE, point.y + MARKER_SIZE, point.x + MARKER_SIZE, point.y - MARKER_SIZE);
                g.setColor(Color.RED);
                g.drawRect((int) (hint.x - SIZE / 2.0), (int) (hint.y - SIZE / 2.0), SIZE, SIZE);
            }
        }
    }

    private void initComponents() {
        this.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        this.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        progressBar = new javax.swing.JProgressBar();
        f.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jScrollPane1.setViewportView(this);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(f.getContentPane());
        f.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE).add(progressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        f.pack();
    }

    /**
     * This method lets us kill this class when it is not needed anymore
     */
    public void die() {
        img = null;
        f.dispose();
    }
}
