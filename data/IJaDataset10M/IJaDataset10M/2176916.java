package edu.udo.scaffoldhunter.view.plot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.Rectangle2D;
import java.text.ParseException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;
import edu.udo.scaffoldhunter.util.I18n;

/**
 * @author Michael Hesse
 *
 */
public class LegendPanel extends JPanel implements ModelChangeListener {

    /**
     * 
     * @author Michael Hesse
     *
     */
    class ColorDistribution extends JPanel {

        Color startColor, endColor;

        double startValue, endValue;

        /**
         * 
         */
        public ColorDistribution() {
            startColor = Color.white;
            endColor = Color.black;
            startValue = 0.0;
            endValue = 1.0;
            setOpaque(false);
        }

        /**
         * 
         * @param startColor
         */
        public void setStartColor(Color startColor) {
            this.startColor = startColor;
        }

        /**
         * 
         * @param endColor
         */
        public void setEndColor(Color endColor) {
            this.endColor = endColor;
        }

        /**
         * 
         * @param startValue
         */
        public void setStartValue(double startValue) {
            this.startValue = startValue;
        }

        /**
         * 
         * @param endValue
         */
        public void setEndValue(double endValue) {
            this.endValue = endValue;
        }

        @Override
        public void paint(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            double boxHeight = getHeight() / 11.0;
            double boxWidth = 10.0;
            int fontHeight = metrics.getHeight();
            for (int i = 0; i <= 10; i++) {
                double value = (endValue - startValue) / 10.0 * i + startValue;
                String text = " " + value;
                int width = metrics.stringWidth(text);
                if (width > boxWidth) boxWidth = width;
            }
            for (int i = 0; i <= 10; i++) {
                int j = 10 - i;
                double value = (endValue - startValue) / 10.0 * i + startValue;
                String text;
                try {
                    text = numberFormatter.valueToString(value);
                } catch (ParseException e) {
                    e.printStackTrace();
                    text = "" + value;
                }
                double xPos = 50;
                double yPos = boxHeight * 0.5 + j * boxHeight;
                g.drawString(text, (int) xPos, (int) ((yPos) + fontHeight * 0.3));
                g.drawLine(35, (int) yPos, 42, (int) yPos);
            }
            Rectangle2D rect = new Rectangle2D.Double(5, boxHeight * 0.5, 30, 10 * boxHeight - 1);
            GradientPaint gp = new GradientPaint(0, 0, endColor, 0, getHeight(), startColor);
            g2d.setPaint(gp);
            g2d.fill(rect);
            g2d.setColor(Color.BLACK);
            g2d.draw(rect);
        }
    }

    /**
     * 
     * @author Micha
     *
     */
    class SizeDistribution extends JPanel {

        int startSize, endSize;

        double startValue, endValue;

        /**
         * 
         */
        public SizeDistribution() {
            startSize = 1;
            endSize = 20;
            startValue = 0.0;
            endValue = 1.0;
            setOpaque(false);
        }

        /**
         * 
         * @param startSize
         */
        public void setStartSize(int startSize) {
            this.startSize = startSize;
        }

        /**
         * 
         * @param endSize
         */
        public void setEndSize(int endSize) {
            this.endSize = endSize;
        }

        /**
         * 
         * @param startValue
         */
        public void setStartValue(double startValue) {
            this.startValue = startValue;
        }

        /**
         * 
         * @param endValue
         */
        public void setEndValue(double endValue) {
            this.endValue = endValue;
        }

        @Override
        public void paint(Graphics g) {
            super.paintComponent(g);
            int boxCount = endSize - startSize;
            if (boxCount > 10) {
                boxCount /= 2;
            }
            boxCount++;
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            double boxHeight = getHeight() / 10.0;
            double boxWidth = 10.0;
            int fontHeight = metrics.getHeight();
            double valueStep = (endValue - startValue) / boxCount;
            for (int i = 0; i <= boxCount; i++) {
                double value = valueStep * i + valueStep / 2 + startValue;
                String text = " " + value;
                int width = metrics.stringWidth(text);
                if (width > boxWidth) boxWidth = width;
            }
            for (int i = 0; i < boxCount; i++) {
                int j = boxCount - 1 - i;
                double value = valueStep * i + valueStep / 2 + startValue;
                String text;
                try {
                    text = numberFormatter.valueToString(value);
                } catch (ParseException e) {
                    e.printStackTrace();
                    text = "" + value;
                }
                double xPos = 40;
                double yPos = boxHeight * 0.3 + j * boxHeight;
                g.drawString(text, (int) xPos, (int) (yPos) + fontHeight / 2);
            }
            double dotStep = ((double) (endSize - startSize + 1)) / ((double) boxCount);
            for (int i = 0; i < boxCount; i++) {
                int j = boxCount - 1 - i;
                int dotSize = (int) (dotStep * i + startSize);
                int xPos = (int) (5 + boxHeight / 2 - dotSize / 2);
                int yPos = (int) (boxHeight / 2 + j * boxHeight - dotSize / 2);
                g.setColor(Color.BLUE);
                g.fillOval(xPos, yPos, dotSize, dotSize);
            }
        }
    }

    JLabel xLabel, yLabel, zLabel, colorLabel, sizeLabel;

    Box colorPanel, sizePanel;

    ColorDistribution colorDistribution;

    SizeDistribution sizeDistribution;

    NumberFormatter numberFormatter = new NumberFormatter();

    /**
     * 
     */
    public LegendPanel() {
        super();
        {
            xLabel = new JLabel(" ");
            xLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            xLabel.setVisible(false);
        }
        {
            yLabel = new JLabel(" ");
            yLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            yLabel.setVisible(false);
        }
        {
            zLabel = new JLabel(" ");
            zLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            zLabel.setVisible(false);
        }
        {
            colorPanel = Box.createVerticalBox();
            colorLabel = new JLabel(" ");
            colorLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            colorPanel.add(colorLabel);
            colorDistribution = new ColorDistribution();
            colorPanel.add(colorDistribution);
            colorDistribution.setPreferredSize(new Dimension(100, 200));
            colorPanel.setVisible(false);
            colorPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        }
        {
            sizePanel = Box.createVerticalBox();
            sizeLabel = new JLabel(" ");
            sizeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            sizePanel.add(sizeLabel);
            sizeDistribution = new SizeDistribution();
            sizePanel.add(sizeDistribution);
            sizeDistribution.setPreferredSize(new Dimension(100, 200));
            sizePanel.setVisible(false);
            sizePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        }
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Box box = Box.createVerticalBox();
        box.add(xLabel);
        box.add(yLabel);
        box.add(zLabel);
        box.add(colorPanel);
        box.add(sizePanel);
        setLayout(new GridLayout(1, 1));
        add(box);
    }

    @Override
    public void modelChanged(Model model, int channel, boolean moreToCome) {
        switch(channel) {
            case PlotPanel3D.X_CHANNEL:
                if (model.hasData(channel)) {
                    String text = "<html><b><u>" + I18n.get("PlotView.Mappings.XAxis") + "</u></b><br>" + model.getChannelTitle(channel) + "<br>" + I18n.get("PlotView.Legend.Domain") + ": " + model.getDataMin(channel) + " - " + model.getDataMax(channel) + "</html>";
                    xLabel.setText(text);
                    xLabel.setVisible(true);
                } else {
                    xLabel.setText(" ");
                    xLabel.setVisible(false);
                }
                break;
            case PlotPanel3D.Y_CHANNEL:
                if (model.hasData(channel)) {
                    String text = "<html><b><u>" + I18n.get("PlotView.Mappings.YAxis") + "</u></b><br>" + model.getChannelTitle(channel) + "<br>" + I18n.get("PlotView.Legend.Domain") + ": " + model.getDataMin(channel) + " - " + model.getDataMax(channel) + "</html>";
                    yLabel.setText(text);
                    yLabel.setVisible(true);
                } else {
                    yLabel.setText(" ");
                    yLabel.setVisible(false);
                }
                break;
            case PlotPanel3D.Z_CHANNEL:
                if (model.hasData(channel)) {
                    String text = "<html><b><u>" + I18n.get("PlotView.Mappings.ZAxis") + "</u></b><br>" + model.getChannelTitle(channel) + "<br>" + I18n.get("PlotView.Legend.Domain") + ": " + model.getDataMin(channel) + " - " + model.getDataMax(channel) + "</html>";
                    zLabel.setText(text);
                    zLabel.setVisible(true);
                } else {
                    zLabel.setText(" ");
                    zLabel.setVisible(false);
                }
                break;
            case PlotPanel3D.COLOR_CHANNEL:
                if (model.hasData(channel)) {
                    String text = "<html><b><u>" + I18n.get("PlotView.DotColor") + "</u></b><br>" + model.getChannelTitle(channel) + "<br>" + I18n.get("PlotView.Legend.Domain") + ": " + model.getDataMin(channel) + " - " + model.getDataMax(channel) + "</html>";
                    colorLabel.setText(text);
                    colorDistribution.setStartValue(model.getDataMin(channel));
                    colorDistribution.setEndValue(model.getDataMax(channel));
                    colorPanel.setVisible(true);
                } else {
                    colorLabel.setText(" ");
                    colorDistribution.setStartValue(0.0);
                    colorDistribution.setEndValue(1.0);
                    colorPanel.setVisible(false);
                }
                colorPanel.repaint();
                break;
            case PlotPanel3D.SIZE_CHANNEL:
                if (model.hasData(channel)) {
                    String text = "<html><b><u>" + I18n.get("PlotView.DotSize") + "</u></b><br>" + model.getChannelTitle(channel) + "<br>" + I18n.get("PlotView.Legend.Domain") + ": " + model.getDataMin(channel) + " - " + model.getDataMax(channel) + "</html>";
                    sizeLabel.setText(text);
                    sizeDistribution.setStartValue(model.getDataMin(channel));
                    sizeDistribution.setEndValue(model.getDataMax(channel));
                    sizePanel.setVisible(true);
                } else {
                    sizeLabel.setText(" ");
                    sizeDistribution.setStartValue(0.0);
                    sizeDistribution.setEndValue(1.0);
                    sizePanel.setVisible(false);
                }
                sizePanel.repaint();
                break;
        }
    }

    /**
     * 
     * @param startColor
     */
    public void setStartColor(Color startColor) {
        colorDistribution.setStartColor(startColor);
        colorPanel.repaint();
    }

    /**
     * 
     * @param endColor
     */
    public void setEndColor(Color endColor) {
        colorDistribution.setEndColor(endColor);
        colorPanel.repaint();
    }

    /**
     * 
     * @param startSize
     */
    public void setStartSize(int startSize) {
        sizeDistribution.setStartSize(startSize);
        sizePanel.repaint();
    }

    /**
     * 
     * @param endSize
     */
    public void setEndSize(int endSize) {
        sizeDistribution.setEndSize(endSize);
        sizePanel.repaint();
    }
}
