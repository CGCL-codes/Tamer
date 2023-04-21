package jp.co.withone.osgi.gadget.bundlecontroller.balloon.impl;

import java.awt.TrayIcon;
import jp.co.withone.osgi.gadget.bundlecontroller.balloon.BalloonViewService;

/**
 * @author takada
 *
 */
public class BallloonViewServiceImpl implements BalloonViewService {

    /** Icon in Tray */
    TrayIcon trayIcon = null;

    /**
     * constructor.
     * @param trayIcon TrayIcon
     */
    public BallloonViewServiceImpl(TrayIcon trayIcon) {
        this.trayIcon = trayIcon;
    }

    /**
     * DisplayMessage.
     * Display Message using Balloon from TrayIcon.
     * @param messageType MessageType
     * @param caption     display caption
     * @param text        display text
     */
    public void displayMessage(TrayIcon.MessageType messageType, String caption, String text) {
        if (messageType == null && caption == null && text == null) {
            return;
        }
        if (trayIcon != null) {
            trayIcon.displayMessage(caption, text, messageType);
        }
    }
}
