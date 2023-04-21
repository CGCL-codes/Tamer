package ch.blackspirit.graphics.demo;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import ch.blackspirit.graphics.AWTCanvas;
import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.GraphicsContext;
import ch.blackspirit.graphics.GraphicsListener;
import ch.blackspirit.graphics.Image;
import ch.blackspirit.graphics.View;
import ch.blackspirit.graphics.jogl.CanvasFactory;

/**
 * @author Markus Koller
 */
public class SwingDemo {

    int lastX, lastY;

    int x, y;

    Image image;

    public static void main(String[] args) throws IOException {
        SwingDemo demo = new SwingDemo();
        demo.run();
    }

    public void run() throws IOException {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem item = new JMenuItem("Exit");
        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(item);
        bar.add(menu);
        frame.setJMenuBar(bar);
        final AWTCanvas canvas = new CanvasFactory().createAWTCanvas(true);
        canvas.setGraphicsListener(new GraphicsListener() {

            public void draw(View view, Graphics renderer) {
                renderer.clear();
                view.setCamera(-x, -y, 0);
                renderer.clear();
                for (int x = -2; x < 2; x++) {
                    for (int y = -2; y < 2; y++) {
                        renderer.translate(-x * 44, -y * image.getHeight());
                        renderer.drawImage(image, 44, image.getHeight(), 0, 0, 44, image.getHeight());
                        renderer.clearTransform();
                    }
                }
            }

            public void sizeChanged(GraphicsContext context, View view) {
                view.setSize(canvas.getWidth(), canvas.getHeight());
            }

            public void init(View view, Graphics renderer) {
                view.setSize(canvas.getWidth(), canvas.getHeight());
            }
        });
        frame.getContentPane().add(canvas.getComponent());
        frame.getContentPane().setPreferredSize(new Dimension(800, 600));
        frame.pack();
        image = canvas.getImageFactory().createImage(SwingDemo.class.getResource("/sprites/Crono - Walk (Left) 44x68.png"), true);
        canvas.getComponent().addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent e) {
                x += e.getX() - lastX;
                y += e.getY() - lastY;
                lastX = e.getX();
                lastY = e.getY();
                canvas.draw();
            }

            public void mouseMoved(MouseEvent e) {
            }
        });
        canvas.getComponent().addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
        frame.setVisible(true);
    }
}
