package com.webcamtracker.ui.target;

import com.webcamtracker.color.segmentation.ColorSegmentator;
import com.webcamtracker.color.segmentation.Segment;
import com.webcamtracker.color.segmentation.Tracker;
import com.webcamtracker.image.color.rgb.RGBColor;
import com.webcamtracker.ui.UIDrawableCanvas;
import com.webcamtracker.ui.UITracker;
import com.webcamtracker.ui.actions.SelectTargetAction;
import com.webcamtracker.ui.utils.ImageUtils;
import com.webcamtracker.ui.utils.WindowUtilities;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.TreeSet;

public class UITargetModel extends JPanel {

    private int counter = 0;

    private MouseListener mouseListener;

    private Point point1;

    private Point point2;

    private UIDrawableCanvas targetModel;

    private UIDrawableCanvas.Drawable targetmodelDrawable;

    private UIDrawableCanvas processedTargetModel;

    private UITracker uiTracker;

    private final Tracker tracker;

    private UIDrawableCanvas workingArea;

    private InnerUIColorHistogramIndex histogram;

    private InnerModelInfoPanel modelInfo;

    private JTextField textField;

    private boolean zoom = true;

    public UITargetModel(UITracker uiTracker, Tracker tracker) {
        this.uiTracker = uiTracker;
        this.tracker = tracker;
        this.workingArea = uiTracker.getWorkingArea();
        this.targetModel = new UIDrawableCanvas(new Dimension(320, 240));
        this.processedTargetModel = new UIDrawableCanvas(new Dimension(320, 240));
        this.histogram = new InnerUIColorHistogramIndex();
        this.modelInfo = new InnerModelInfoPanel();
        setupUI();
    }

    public void setupUI() {
        this.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));
        ScrollPane pane = new ScrollPane();
        pane.add(histogram);
        JPanel model = new JPanel();
        textField = new JTextField();
        model.setLayout(new BorderLayout());
        model.add(targetModel);
        model.add(modelInfo, BorderLayout.SOUTH);
        model.add(textField, BorderLayout.NORTH);
        panel.add(WindowUtilities.titledComponent("Target", model));
        panel.add(WindowUtilities.titledComponent("Processed Target", processedTargetModel));
        panel.add(WindowUtilities.titledComponent("Histogram", pane));
        this.add(BorderLayout.CENTER, panel);
        this.add(BorderLayout.WEST, setupButton());
    }

    public void clearTarget() {
        tracker.removeTarget();
    }

    public void selectTarget() {
        point1 = null;
        point2 = null;
        counter = 0;
        if (mouseListener != null) {
            uiTracker.getWorkingArea().removeMouseListener(mouseListener);
        }
        mouseListener = new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                counter = counter + 1;
                if (counter % 2 == 0) {
                    point1 = e.getPoint();
                } else {
                    point2 = e.getPoint();
                }
                workingArea.repaint();
            }
        };
        targetmodelDrawable = new UIDrawableCanvas.Drawable() {

            public void paint(Graphics graphics) {
                Color color = graphics.getColor();
                if (point2 != null && point1 != null) {
                    graphics.setColor(Color.RED);
                    graphics.drawRect((int) Math.min(point1.getX(), point2.getX()), (int) Math.min(point1.getY(), point2.getY()), (int) Math.abs(point2.getX() - point1.getX()), (int) Math.abs(point2.getY() - point1.getY()));
                }
                graphics.setColor(Color.GREEN);
                if (point2 != null) {
                    graphics.fillOval((int) point2.getX(), (int) point2.getY(), 5, 5);
                }
                if (point1 != null) {
                    graphics.fillOval((int) point1.getX(), (int) point1.getY(), 5, 5);
                }
                graphics.setColor(color);
            }
        };
        workingArea.add(targetmodelDrawable);
        uiTracker.getWorkingArea().addMouseListener(mouseListener);
    }

    public void pickTargetModel() {
        if (point1 != null && point2 != null) {
            if (((point2.x - point1.x) % 2) != 0) {
                point2.x++;
            }
            if (((point2.y - point1.y) % 2) != 0) {
                point2.y++;
            }
            int x = (int) Math.min(point1.getX(), point2.getX());
            int y = (int) Math.min(point1.getY(), point2.getY());
            int witdh = (int) Math.abs(point2.getX() - point1.getX());
            int height = (int) Math.abs(point2.getY() - point1.getY());
            final BufferedImage targetModelImage = uiTracker.getWorkingArea().getImage().getSubimage(x, y, witdh, height);
            targetModel.clear();
            targetModel.add(new UIDrawableCanvas.Drawable() {

                public void paint(Graphics g) {
                    g.drawImage(targetModelImage, 0, 0, null);
                }
            });
            workingArea.remove(targetmodelDrawable);
            final String name = textField.getText();
            final BufferedImage processedImage = tracker.setTarget(ImageUtils.clone(targetModelImage), x, y, name == null ? "NULL" : name, zoom);
            processedTargetModel.clear();
            processedTargetModel.add(new UIDrawableCanvas.Drawable() {

                public void paint(Graphics g) {
                    g.drawImage(processedImage, 0, 0, null);
                }
            });
            ColorSegmentator segmentator = new ColorSegmentator();
            segmentator.segment(processedImage);
            histogram.setSegments(segmentator.getSegments());
            targetModel.repaint();
            processedTargetModel.repaint();
            histogram.repaint();
            this.repaint();
        }
    }

    private JPanel setupButton() {
        JButton selectTarget = new JButton();
        selectTarget.setAction(new SelectTargetAction(this));
        JPanel result = new JPanel();
        result.setLayout(new GridLayout(5, 1));
        result.add(selectTarget);
        return result;
    }

    public class InnerModelInfoPanel extends JPanel {

        private JCheckBox zoomCheckBox = new JCheckBox(" Zoom:", true);

        public InnerModelInfoPanel() {
            add(zoomCheckBox);
            zoomCheckBox.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    zoom = zoomCheckBox.isSelected();
                }
            });
        }
    }

    public static class InnerUIColorHistogramIndex extends JPanel {

        private int barWidth = 20;

        Collection<Segment> hits = new TreeSet<Segment>();

        InnerUIColorHistogramIndex() {
        }

        public void setSegments(Collection<Segment> hits) {
            this.hits = hits;
        }

        public Dimension getPreferredSize() {
            return new Dimension(200, barWidth * hits.size());
        }

        public void paint(Graphics graphics) {
            super.paint(graphics);
            if (hits != null) {
                int y = 0;
                int totalH = 0;
                for (Segment hit : hits) {
                    totalH += hit.getSize();
                }
                for (Segment segment : hits) {
                    RGBColor rgbcolor = (RGBColor) segment.getColor();
                    Color color = new Color(rgbcolor.getRed(), rgbcolor.getGreen(), rgbcolor.getBlue());
                    graphics.setColor(color);
                    graphics.fillRect(0, y, segment.getSize() * 2, barWidth);
                    graphics.setColor(Color.BLACK);
                    graphics.drawString("Id: " + segment.getId() + " (%" + (segment.getSize() * 100) / totalH + ")", 0, y + barWidth / 2);
                    y = y + barWidth;
                }
            }
        }
    }
}
