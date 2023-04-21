package com.limegroup.gnutella.gui.notify;

import javax.swing.SwingUtilities;
import com.limegroup.gnutella.gui.GUIMediator;

final class WindowsNotifyUser implements NotifyUser {

    /**
     * Placeholder used in the native code.
     */
    @SuppressWarnings("unused")
    private int _handler = 0;

    /**
     * The tooltip to use for the tray icon.
     */
    private String _tooltip;

    /**
     * Handle to the name of the image file.
     */
    private String _imageFileName;

    /**
     * Handle to the image icon.
     */
    private int _imageIconHandle = -1;

    public void addNotify() {
        String tip = GUIMediator.getStringResource("TRAY_TOOLTIP");
        updateNotify("FrostWire.ico", tip);
    }

    public void removeNotify() {
        nativeDisable();
    }

    public void updateNotify(final String imageFile, final String tooltip) {
        _tooltip = tooltip;
        if (_imageFileName == null || _imageIconHandle == -1 || !_imageFileName.equals(imageFile)) {
            _imageFileName = imageFile;
            _imageIconHandle = nativeLoadImage(imageFile);
        }
        if (_imageIconHandle == -1) return;
        nativeEnable(_imageIconHandle, _tooltip);
    }

    public void updateImage(final String imageFileName) {
        _imageFileName = imageFileName;
        updateNotify(_imageFileName, _tooltip);
    }

    public void updateDesc(final String desc) {
        updateNotify(_imageFileName, desc);
    }

    public void hideNotify() {
        nativeHide();
    }

    /**
     * This is used by the native method as a callback to restore
     * the application from the tray.
     */
    public void restoreApplication() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                GUIMediator.restoreView();
            }
        });
    }

    /**
     * This is the callback from the native code to exit the
     * application.
     */
    public void exitApplication() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                GUIMediator.shutdown();
            }
        });
    }

    /**
     * Callback from the native tray code that exits LimeWire only
     * after all current transfers are completed.
     */
    public void exitAfterTransfers() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                GUIMediator.shutdownAfterTransfers();
            }
        });
    }

    /**
     * Shows the about window to the user.
     */
    public void showAboutWindow() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                GUIMediator.showAboutWindow();
            }
        });
    }

    /**
     * Removes the system tray icon.
     *
     * @throws UnsatisfiedLinkError if the associated native method 
     *  could not be linked to successfully
     */
    private native synchronized void nativeDisable() throws UnsatisfiedLinkError;

    /**
     * Enables the system tray icon, using the specified image resource
     * and tooltip.  This method can also be used to modify the existing
     * system tray icon and tooltip.
     *
     * @param image the integer identifier of the image to use
     * @param tooltip the tooltip to display over the system tray icon
     */
    private native synchronized void nativeEnable(int image, String tooltip) throws UnsatisfiedLinkError;

    /**
     * Frees the specified image from memory.
     *
     * @param image the integer identifier of the native image
     */
    @SuppressWarnings("unused")
    private static native synchronized void nativeFreeImage(int image) throws UnsatisfiedLinkError;

    /**
     * Loads the image specified in the <tt>fileName</tt> argument
     * natively into memory.
     *
     * @param fileName the full pathname of the image file to load
     */
    private static native synchronized int nativeLoadImage(String fileName) throws UnsatisfiedLinkError;

    /**
     * Loads the image specified in the <tt>intResource</tt> argument
     * natively into memory.
     *
     * @param intResource the integer identifier of the image resource
     */
    @SuppressWarnings("unused")
    private static native synchronized int nativeLoadImageFromResource(int intResource) throws UnsatisfiedLinkError;

    /**
     * Hides the system tray icon.
     */
    private native synchronized void nativeHide() throws UnsatisfiedLinkError;
}
