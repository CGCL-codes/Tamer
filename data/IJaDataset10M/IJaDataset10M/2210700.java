package com.melloware.jukes.util;

import java.awt.Graphics;
import java.awt.Window;
import com.jgoodies.uif.splash.ImageSplash;
import com.jgoodies.uif.splash.SplashProvider;

/**
 * <p>A wrapper for <code>com.jgoodies.uif.splash.ImageSplash</code> which is
 * removing flickering when note or progress changes.</p>
 *
 * <p>The reason of flickering is default lightweigt component's <code>update(Graphics)</code>
 * method which clears the background before <code>paint(Graphics)</code> method invocation.</p>
 *
 * <p>Other solution is to ask JGoodies author to override
 * <code>ImageSplash.update(Graphics)</code>: i.e. <code>void update(Graphics g) {paint(g);}</code>
 * </p>
 *
 * Copyright (c) 1999-2007 Melloware, Inc. <http://www.melloware.com>
 * @author Emil A. Lefkof III <info@melloware.com>
 */
public class NoFlickerSplashWrapper extends Window implements SplashProvider {

    private final ImageSplash imageSplash;

    /**
     * Creates ImageSplashWrapper and initializes it with wrapped ImageSplash
     * size/location values.
     *
     * @param anImageSplash     image splash screen to wrap.
     */
    public NoFlickerSplashWrapper(ImageSplash anImageSplash) {
        super(anImageSplash.getOwner());
        imageSplash = anImageSplash;
        setSize(imageSplash.getSize());
        setLocation(imageSplash.getLocation());
    }

    /**
     * Sets the note.
     *
     * @param note splash note.
     *
     * @see SplashProvider#setNote(java.lang.String)
     */
    public void setNote(String note) {
        imageSplash.setNote(note);
        repaint();
    }

    /**
     * Sets the progress.
     *
     * @param percent progress percent value (0 <= ... <= 100).
     *
     * @see SplashProvider#setProgress(int)
     */
    public void setProgress(int percent) {
        imageSplash.setProgress(percent);
        repaint();
    }

    /**
     * Invoked to close splash &amp; release resources.
     *
     * @see SplashProvider#closeSplash()
     */
    public void closeSplash() {
        dispose();
    }

    /**
     * Invoked to open splash.
     *
     * @see SplashProvider#openSplash()
     */
    public void openSplash() {
        setVisible(true);
    }

    /**
     * Paints the container.
     *
     * @param g the specified Graphics window.
     */
    public void paint(Graphics g) {
        imageSplash.paint(g);
    }

    /**
     * Updates the container.
     *
     * @param g the specified Graphics window.
     */
    public void update(Graphics g) {
        paint(g);
    }
}
