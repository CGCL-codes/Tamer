package vodoo.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.Timer;
import vodoo.gui.components.ArrowButton;
import vodoo.gui.components.ButtonListener;
import vodoo.gui.video.VideoInfoPanel;

public class MosaicPanel extends JPanel implements ButtonListener {

    private JPanel videoContainer;

    private JPanel sliderPanel;

    private JPanel leftArrow, rightArrow;

    private Timer animationTimer;

    private int leftVideoIndex;

    private JPanel fadeInPanel;

    public MosaicPanel() {
        setBackground(new Color(0x1e2635));
        setPreferredSize(new Dimension(50, 150));
        setSize(getPreferredSize());
        sliderPanel = new JPanel();
        sliderPanel.setOpaque(false);
        sliderPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        sliderPanel.setBounds(0, 0, 0, 150);
        videoContainer = new JPanel();
        videoContainer.setOpaque(false);
        videoContainer.setLayout(null);
        JPanel fadeOutPanel = new JPanel() {

            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(new Point(0, 0), new Color(0x1e2635), new Point(80, 0), new Color(0x1e2635, true)));
                g2d.fillRect(0, 0, 80, getHeight());
            }
        };
        fadeOutPanel.setOpaque(false);
        fadeOutPanel.setBounds(0, 0, 80, getHeight());
        videoContainer.add(fadeOutPanel);
        fadeInPanel = new JPanel() {

            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(new Point(0, 0), new Color(0x1e2635, true), new Point(80, 0), new Color(0x1e2635)));
                g2d.fillRect(0, 0, 80, getHeight());
            }
        };
        fadeInPanel.setOpaque(false);
        videoContainer.add(fadeInPanel);
        videoContainer.add(sliderPanel);
        leftArrow = createArrowButton(ArrowButton.ARROW_LEFT);
        rightArrow = createArrowButton(ArrowButton.ARROW_RIGHT);
        setLayout(new BorderLayout());
        add(videoContainer, BorderLayout.CENTER);
        add(leftArrow, BorderLayout.WEST);
        add(rightArrow, BorderLayout.EAST);
        animationTimer = new Timer(30, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int x = sliderPanel.getX();
                if (x < -leftVideoIndex * 120) sliderPanel.setLocation(x + 20, sliderPanel.getY()); else if (x > -leftVideoIndex * 120) sliderPanel.setLocation(x - 20, sliderPanel.getY());
            }
        });
        animationTimer.start();
    }

    protected void paintComponent(Graphics g) {
        g.setColor(new Color(0x1e2635));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public void addVideoPanel(VideoInfoPanel panel) {
        sliderPanel.add(panel);
        panel.setPreferredSize(new Dimension(120, 150));
        rebuild();
    }

    public void removeVideoPanel(VideoInfoPanel panel) {
        sliderPanel.remove(panel);
        rebuild();
    }

    public void setVideoPanels(VideoInfoPanel[] videos) {
        sliderPanel.removeAll();
        for (int i = 0; i < videos.length; i++) {
            videos[i].setPreferredSize(new Dimension(120, 150));
            sliderPanel.add(videos[i]);
        }
        leftVideoIndex = 0;
        sliderPanel.setLocation(0, sliderPanel.getY());
        rebuild();
    }

    public void buttonAction(ArrowButton source) {
        switch(source.getType()) {
            case ArrowButton.ARROW_LEFT:
                leftVideoIndex--;
                if (leftVideoIndex < 0) leftVideoIndex = 0;
                break;
            case ArrowButton.ARROW_RIGHT:
                leftVideoIndex++;
                if (leftVideoIndex >= sliderPanel.getComponentCount() - 1) leftVideoIndex = sliderPanel.getComponentCount() - 1;
                break;
        }
    }

    public void rebuild() {
        if (sliderPanel.getComponentCount() > 0 && leftVideoIndex >= sliderPanel.getComponentCount() - 1) leftVideoIndex = sliderPanel.getComponentCount() - 1;
        sliderPanel.setSize(120 * sliderPanel.getComponentCount(), 150);
        fadeInPanel.setBounds(videoContainer.getWidth() - 80, 0, 80, getHeight());
        repaint();
        revalidate();
    }

    private JPanel createArrowButton(int type) {
        ArrowButton button = new ArrowButton(type);
        button.addButtonListener(this);
        JPanel arrowPanel = new JPanel();
        int strut = (getHeight() - button.getHeight()) / 2;
        arrowPanel.setOpaque(false);
        arrowPanel.setLayout(new BoxLayout(arrowPanel, BoxLayout.Y_AXIS));
        arrowPanel.add(Box.createVerticalStrut(strut - 10));
        arrowPanel.add(button);
        arrowPanel.add(Box.createVerticalStrut(strut + 10));
        return arrowPanel;
    }
}
