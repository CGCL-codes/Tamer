package com.mia.sct.view.splash;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import com.mia.sct.build.MiaBuildInfo;
import com.mia.sct.util.MIAImageLoader;
import com.mia.sct.util.MIAImageUtils;

/**
 * MiaSplashPanel
 * 
 * Mia splash panel
 *
 * @author Devon Bryant
 * @since Feb 23, 2009
 */
@SuppressWarnings("serial")
public class MiaSplashPanel extends JPanel implements ImageObserver {

    private static Logger logger = Logger.getLogger(MiaSplashPanel.class);

    private BufferedImage sepiaSplashImage = null;

    private BufferedImage mainSplashImage = null;

    private Dimension splashPanelDim = new Dimension(500, 350);

    private float alpha = 0.0f;

    private Animator animator = null;

    private static final String copyright = "Copyright (C) 2007-2009  Devon Bryant";

    private static final String license = "This program is free software: you can redistribute it and/or modify " + "it under\nthe terms of the GNU General Public License (http://www.gnu.org/licenses/)";

    private BufferedImage copyrightImage = null;

    private BufferedImage licenseImage = null;

    private BufferedImage versionImage = null;

    private int copyrightXLoc = 0;

    private int copyrightYLoc = 0;

    private int licenseXLoc = 0;

    private int licenseYLoc = 0;

    private int versionXLoc = 0;

    /**
	 * Constructor
	 */
    public MiaSplashPanel() {
        initSplashPanel();
    }

    private void initSplashPanel() {
        setPreferredSize(splashPanelDim);
        try {
            sepiaSplashImage = MIAImageLoader.getInstance().getBufferedImage("mia-splash-sepia.png");
            mainSplashImage = MIAImageLoader.getInstance().getBufferedImage("mia-splash.png");
            splashPanelDim.setSize(mainSplashImage.getWidth(this), mainSplashImage.getHeight(this));
            Font font = new Font("Arial", Font.PLAIN, 12);
            copyrightImage = MIAImageUtils.getTextImage(copyright, font, Color.WHITE);
            font = new Font("Arial", Font.PLAIN, 11);
            licenseImage = MIAImageUtils.getTextImageMultiLine(license, font, Color.LIGHT_GRAY);
            font = new Font("Arial", Font.BOLD, 11);
            String versionText = "Version " + MiaBuildInfo.VERSION + " (" + MiaBuildInfo.BUILD_ID + ")";
            versionImage = MIAImageUtils.getTextImage(versionText, font, Color.LIGHT_GRAY);
            copyrightXLoc = (splashPanelDim.width / 2) - (copyrightImage.getWidth() / 2);
            licenseXLoc = (splashPanelDim.width / 2) - (licenseImage.getWidth() / 2);
            versionXLoc = splashPanelDim.width - versionImage.getWidth() - 4;
            licenseYLoc = splashPanelDim.height - licenseImage.getHeight() - 4;
            copyrightYLoc = licenseYLoc - copyrightImage.getHeight() - 2;
        } catch (Exception exc) {
            logger.error("MiaSplashPanel.initSplashPanel():", exc);
        }
        animator = PropertySetter.createAnimator(3000, this, "alpha", 1.0f);
    }

    /**
	 * Start the splash panel animator
	 */
    public void startAnimator() {
        if (animator != null) {
            animator.start();
        }
    }

    /**
	 * Get the main image alpha value
	 * @return the alpha value
	 */
    public float getAlpha() {
        return alpha;
    }

    /**
	 * Set the main image alpha value
	 * @param inAlpha the alpha to set
	 */
    public void setAlpha(float inAlpha) {
        alpha = inAlpha;
        repaint();
    }

    public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
        boolean result = false;
        if (arg1 == ImageObserver.HEIGHT) {
            splashPanelDim.setSize(splashPanelDim.width, arg5);
        } else if (arg1 == ImageObserver.WIDTH) {
            splashPanelDim.setSize(arg4, splashPanelDim.height);
        }
        return result;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaintMode();
        if (getAlpha() < 1.0f) {
            g2.drawImage(sepiaSplashImage, 0, 0, null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha()));
        }
        g2.drawImage(mainSplashImage, 0, 0, null);
        g2.drawImage(copyrightImage, copyrightXLoc, copyrightYLoc, null);
        g2.drawImage(licenseImage, licenseXLoc, licenseYLoc, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
        g2.drawImage(versionImage, versionXLoc, 210, null);
    }

    @Override
    public Dimension getPreferredSize() {
        return splashPanelDim;
    }
}
