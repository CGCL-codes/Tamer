package at.portty.gui.Transparent;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import at.portty.gui.Main.Portty;
import at.portty.tools.GlobalConstants;

/**
 * A glasspane that can be used to notify the user (for a specified time) what the x and y
 * coordinates of their JFrame (window) are after they have moved it.
 * 
 * <p/>
 * Copyright (C) 2005 by Jon Lipsky
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. Y ou may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software d istributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
@SuppressWarnings("serial")
public class AboutGlassPane extends JPanel implements ActionListener, MouseListener {

    public String text1 = GlobalConstants.AboutText[0];

    public String text2 = GlobalConstants.AboutText[1];

    public String text3 = GlobalConstants.AboutText[2];

    public String text4 = GlobalConstants.AboutText[3];

    public String text5 = GlobalConstants.AboutText[4];

    private final float transparencyFactorMax = 0.75f;

    public boolean componentVisible = false;

    private Component previousGlassPane;

    private Timer timer;

    private int delay = 0;

    private final float length = 0.1f;

    private GradientPaint alphaMask;

    private BufferedImage contentBuffer;

    private BufferedImage reflectionBuffer;

    private Graphics2D contentGraphics;

    private Graphics2D reflectionGraphics;

    private final int minTimeDisplay = 50;

    private long startDisplayTime = 0;

    private Portty mainFrame;

    public AboutGlassPane(Portty aFrame) {
        if (aFrame != null) {
            mainFrame = aFrame;
            mainFrame.addMouseListener(this);
            addMouseListener(this);
            setOpaque(false);
            final java.awt.event.ActionListener actCancel = new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    stopDisplay();
                }
            };
            registerKeyboardAction(actCancel, "Cancel", KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JButton.WHEN_IN_FOCUSED_WINDOW);
        }
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int aDelay) {
        delay = aDelay;
    }

    public void stopDisplay() {
        if (componentVisible && (System.currentTimeMillis() - startDisplayTime) > minTimeDisplay) {
            timer.stop();
            componentVisible = false;
            setVisible(false);
            mainFrame.setGlassPane(previousGlassPane);
            previousGlassPane = null;
        }
    }

    public void actionPerformed(ActionEvent e) {
        stopDisplay();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = g.getFont();
        Font font1 = font.deriveFont(Font.PLAIN, 50);
        Font font2 = font.deriveFont(Font.PLAIN, 26);
        Font font3 = font.deriveFont(Font.PLAIN, 46);
        Font font4 = font.deriveFont(Font.BOLD, 16);
        Dimension size = getSize();
        int h = size.height;
        int w = size.width;
        int arc = 0;
        if (size.width > 300) {
            arc = 20;
        } else if (size.width > 150) {
            arc = 10;
        } else {
            arc = 3;
        }
        g.setFont(font1);
        FontMetrics metrics = g.getFontMetrics();
        Rectangle2D stringBounds = metrics.getStringBounds(text1, g);
        g.setFont(font2);
        metrics = g.getFontMetrics();
        int muHeight = (int) (stringBounds.getHeight() + metrics.getStringBounds(text2, g).getHeight());
        g.setFont(font3);
        metrics = g.getFontMetrics();
        muHeight = (int) (muHeight + metrics.getStringBounds(text3, g).getHeight());
        g.setFont(font4);
        metrics = g.getFontMetrics();
        muHeight = (int) (muHeight + metrics.getStringBounds(text4, g).getHeight() * 3);
        g.setFont(font1);
        metrics = g.getFontMetrics();
        stringBounds = metrics.getStringBounds(text1, g);
        g.setFont(font2);
        metrics = g.getFontMetrics();
        double muWidth = Math.max(stringBounds.getWidth(), metrics.getStringBounds(text2, g).getWidth());
        g.setFont(font3);
        metrics = g.getFontMetrics();
        muWidth = Math.max(muWidth, metrics.getStringBounds(text3, g).getWidth());
        g.setFont(font4);
        metrics = g.getFontMetrics();
        muWidth = Math.max(muWidth, metrics.getStringBounds(text4, g).getWidth());
        g.setFont(font1);
        metrics = g.getFontMetrics();
        int preferredWidth = (int) stringBounds.getWidth() + metrics.getHeight();
        int preferredHeight = muHeight + metrics.getHeight();
        w = Math.min(preferredWidth, w);
        h = Math.min(preferredHeight, h);
        RoundRectangle2D fillArea = new RoundRectangle2D.Double(0.0D, 0.0D, w, h, arc, arc);
        BufferedImage image = new BufferedImage(preferredWidth, preferredHeight, BufferedImage.TYPE_INT_ARGB);
        image = new BrushedMetalFilter(0xff4E4EB8, (int) (40.05 * Math.random()), (float) Math.random(), false, 0.88f).filter(image, image);
        int insetX = (size.width - w) / 2;
        int insetY = (size.height - h) / 2;
        g2d.translate(insetX, insetY);
        Composite composite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparencyFactorMax));
        Shape clip = g2d.getClip();
        g2d.setClip(fillArea);
        g2d.drawImage(image, 0, 0, null);
        g2d.setClip(clip);
        g2d.setComposite(composite);
        Color vWrapColor = new Color(175, 165, 225);
        g2d.setColor(vWrapColor);
        g2d.drawRoundRect(0, 0, w, h, arc, arc);
        int muSpace = metrics.getHeight() / 8;
        int mu = paintStringGlass(g, g2d, font1, text1, w, muSpace);
        mu = paintStringGlass(g, g2d, font2, text2, w, mu + muSpace);
        mu = paintStringGlass(g, g2d, font3, text3, w, mu + muSpace);
        mu = paintStringGlass(g, g2d, font4, text4, w, mu + muSpace * 3);
        mu = paintStringGlass(g, g2d, font4, text5, w, mu);
        g2d.translate(-insetX, -insetY);
        g2d.dispose();
    }

    /**
   * @param g
   * @param g2d
   * @param font1
   * @param w
   * @return Y-Pos of Paint-Line
   */
    private int paintStringGlass(Graphics g, Graphics2D g2d, Font font, String text, int w, int yPos) {
        FontMetrics metrics;
        Rectangle2D stringBounds;
        int x;
        g2d.setFont(font);
        metrics = g2d.getFontMetrics();
        stringBounds = metrics.getStringBounds(text, g);
        x = (w - (int) stringBounds.getWidth()) / 2;
        yPos += metrics.getHeight();
        g2d.setColor(new Color(0, 0, 0, 70));
        g2d.drawString(text, x + 2, yPos + 2);
        g2d.setColor(Color.yellow.brighter());
        g2d.drawString(text, x, yPos);
        return yPos;
    }

    public static void registerFrame(Portty aFrame) {
        new AboutGlassPane(aFrame);
    }

    public void displayFrame(int timePeriod) {
        if (!componentVisible) {
            previousGlassPane = mainFrame.getGlassPane();
            mainFrame.setGlassPane(this);
            setVisible(true);
            componentVisible = true;
        }
        if (timer == null) {
            timer = new Timer(timePeriod, this);
        } else {
            timer.stop();
            timer.setDelay(timePeriod);
        }
        startDisplayTime = System.currentTimeMillis();
        timer.start();
    }

    public void displayFrame() {
        displayFrame(getDelay());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        stopDisplay();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
