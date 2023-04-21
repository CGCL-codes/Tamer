package com.vessosa.g15lastfmplayer.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import org.jdesktop.swingx.JXBusyLabel;
import com.jhlabs.image.InvertFilter;
import com.jhlabs.image.PointFilter;
import com.vessosa.g15lastfmplayer.controller.Controller;
import com.vessosa.g15lastfmplayer.util.mvc.AbstractView;
import com.vessosa.g15lastfmplayer.view.scrollfx.G15ScrollMusicFX;

public class MusicScroll extends ScrollingText implements AbstractView {

    private int progress;

    private boolean displayProgress;

    private JXBusyLabel busy;

    public MusicScroll() {
        busy = new JXBusyLabel(new Dimension(10, 10));
        busy.getBusyPainter().setPoints(14);
        busy.getBusyPainter().setTrailLength(7);
        busy.getBusyPainter().setAntialiasing(true);
        setScrollTextUtils(new G15ScrollMusicFX(this, true, 20, 70, 0, 0));
    }

    public void drawMainScreen(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(Color.black);
        g2.drawImage(LCDScreen.getMainImage(), 0, 0, null);
        if (displayProgress) drawProgress(g2);
        if (busy.isBusy()) g2.drawImage(getBusyImage(), 15, 33, null);
        for (int i = 0; i < this.animHandlers.size(); i++) {
            paintText(g2, this.animHandlers.get(i).getAnimProgress());
        }
        g2.dispose();
    }

    private Image getBusyImage() {
        int w = busy.getIcon().getIconWidth();
        int h = busy.getIcon().getIconHeight();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(w, h);
        Graphics2D g = image.createGraphics();
        busy.getIcon().paintIcon(null, g, 0, 0);
        PointFilter pf = new InvertFilter();
        pf.filter(image, image);
        g.dispose();
        return image;
    }

    private void drawProgress(Graphics g) {
        g.drawRoundRect(59, 40, 48, 2, 2, 2);
        int width = progress * 48 / 100;
        g.fillRoundRect(59, 40, width, 2, 2, 2);
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (propertyName.equals(Controller.MUSIC_NAME)) {
            setText((String) evt.getNewValue());
        } else if (propertyName.equals(Controller.MUSIC_PROGRESS)) {
            progress = (Integer) evt.getNewValue();
        } else if (propertyName.equals(Controller.SHOW_MUSIC_PROGRESS)) {
            displayProgress = (Boolean) evt.getNewValue();
        } else if (propertyName.equals(Controller.WORKING)) {
            busy.setBusy((Boolean) evt.getNewValue());
        }
    }
}
