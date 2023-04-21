package com.teletalk.jadmin.gui.util;

import java.applet.AudioClip;
import javax.swing.Icon;
import javax.swing.JButton;
import com.teletalk.jadmin.gui.AdminMainPanel;

/**
 * JButton subclass that implements a button that runs an animation when it changes state from disabled to enabled 
 * and from enabled to disabled.
 * 
 * @author Tobias L�fstrand
 * 
 * @since Alpha
 */
public final class AnimButton extends JButton {

    private static final long serialVersionUID = 1L;

    boolean buttonEnabled = false;

    final Icon enabledIcon;

    final Icon disabledIcon;

    final int nIcons;

    final Icon[] icons;

    long iconDelay;

    boolean changingState = false;

    long lastStateChange;

    AudioClip enableSound;

    AudioClip disableSound;

    /**
	 * Creates a new AnimButton.
	 */
    public AnimButton(final Icon enabledIcon, final Icon disabledIcon, final String imagePrefix, final String imageSuffix, final int nIcons) {
        super(disabledIcon);
        this.enabledIcon = enabledIcon;
        this.disabledIcon = disabledIcon;
        this.nIcons = nIcons;
        iconDelay = 35;
        setIcon(enabledIcon);
        setDisabledIcon(disabledIcon);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        icons = new Icon[nIcons];
        for (int i = 0; i < nIcons; i++) {
            icons[i] = ResourceHandler.getImageIcon(imagePrefix + i + imageSuffix);
        }
        setEnabled(false);
        lastStateChange = System.currentTimeMillis();
        enableSound = null;
        disableSound = null;
    }

    /**
	 * Creates a new AnimButton.
	 */
    public AnimButton(final Icon enabledIcon, final Icon disabledIcon, final Icon pressedIcon, final Icon rolloverIcon, final String imagePrefix, final String imageSuffix, final int nIcons) {
        this(enabledIcon, disabledIcon, imagePrefix, imageSuffix, nIcons);
        if (pressedIcon != null) setPressedIcon(pressedIcon);
        if (rolloverIcon != null) {
            setRolloverIcon(rolloverIcon);
            setRolloverEnabled(true);
        }
    }

    /**
	 * Enables the button by performing the animation squence.
	 */
    public synchronized boolean enableButton() {
        if (!changingState || (changingState && ((System.currentTimeMillis() - lastStateChange) > 5 * 1000))) {
            changingState = true;
            Thread fireAndForget = new Thread("AnimButton@" + hashCode()) {

                public void run() {
                    setRolloverEnabled(false);
                    if (!buttonEnabled) {
                        if (enableSound != null && AdminMainPanel.soundsEnabled) enableSound.play();
                        for (int i = 0; i < nIcons; i++) {
                            setDisabledIcon(icons[i]);
                            try {
                                sleep(iconDelay);
                            } catch (InterruptedException e) {
                            }
                        }
                        buttonEnabled = true;
                    }
                    setEnabled(true);
                    setDisabledIcon(disabledIcon);
                    setRolloverEnabled(true);
                    changingState = false;
                }
            };
            fireAndForget.setDaemon(true);
            fireAndForget.start();
            lastStateChange = System.currentTimeMillis();
            notifyAll();
            return true;
        }
        return false;
    }

    /**
	 * Disables the button by performing the animation squence in reverse.
	 */
    public synchronized boolean disableButton() {
        if (!changingState || (changingState && ((System.currentTimeMillis() - lastStateChange) > 5 * 1000))) {
            changingState = true;
            Thread fireAndForget = new Thread("AnimButton@" + hashCode()) {

                public void run() {
                    setRolloverEnabled(false);
                    setDisabledIcon(enabledIcon);
                    setEnabled(false);
                    if (buttonEnabled) {
                        if (disableSound != null) disableSound.play();
                        for (int i = nIcons - 1; i >= 0; i--) {
                            setDisabledIcon(icons[i]);
                            try {
                                sleep(iconDelay);
                            } catch (InterruptedException e) {
                            }
                        }
                        buttonEnabled = false;
                    }
                    setDisabledIcon(disabledIcon);
                    setRolloverEnabled(true);
                    changingState = false;
                }
            };
            fireAndForget.setDaemon(true);
            fireAndForget.start();
            lastStateChange = System.currentTimeMillis();
            notifyAll();
            return true;
        }
        return false;
    }

    /**
	 * Sets the sound that is to be played when the button is enabled.
	 */
    public void setEnableSound(AudioClip enableSound) {
        this.enableSound = enableSound;
    }

    /**
	 * Sets the sound that is to be played when the button is disabled.
	 */
    public void setDisableSound(AudioClip disableSound) {
        this.disableSound = disableSound;
    }

    /**
	 * Sets the delay in milliseconds between each icon used when animating the button.
	 */
    public void setIconDelay(long iconDelay) {
        this.iconDelay = iconDelay;
    }

    /**
	 * Waits for the button to complete the current state transition (animation).
	 */
    public synchronized void waitForStateChange(long timeout) throws InterruptedException {
        if (changingState) {
            wait(timeout);
        }
    }
}
