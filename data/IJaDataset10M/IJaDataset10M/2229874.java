package cn.houseout.snapscreen.demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ImageView extends JDialog {

    private BufferedImage original;

    public ImageView(JFrame frame, BufferedImage original) {
        super(frame);
        this.setTitle("Image Viewer - rotating");
        this.original = original;
        initUI();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(d);
    }

    private void initUI() {
        final SelectTx surface = new SelectTx();
        getContentPane().add(surface);
        surface.start();
    }

    class SelectTx extends JPanel implements Runnable {

        private static final long serialVersionUID = 5310530917337486621L;

        protected double angdeg = 0;

        public Thread thread;

        public SelectTx() {
            setBackground(Color.orange);
        }

        public void step() {
            angdeg += 5;
            if (angdeg == 360) {
                angdeg = 0;
            }
        }

        public void render(int w, int h, Graphics2D g2) {
            int iw = original.getWidth();
            int ih = original.getHeight();
            g2.rotate(Math.toRadians(angdeg), w / 2, h / 2);
            g2.translate(w / 2 - iw / 2, h / 2 - ih / 2);
            g2.setColor(Color.orange);
            g2.fillRect(0, 0, iw + 10, ih + 10);
            g2.drawImage(original, 5, 5, this);
        }

        public void paint(Graphics g) {
            Dimension d = getSize();
            Graphics2D g2 = (Graphics2D) g;
            g2.clearRect(0, 0, d.width, d.height);
            render(d.width, d.height, g2);
            g2.dispose();
            step();
        }

        public void start() {
            if (thread == null) {
                thread = new Thread(this);
                thread.setPriority(Thread.MIN_PRIORITY);
                thread.start();
            }
        }

        public void run() {
            Thread me = Thread.currentThread();
            while (thread == me) {
                repaint();
                try {
                    thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            thread = null;
        }
    }
}
