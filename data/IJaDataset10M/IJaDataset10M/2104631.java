package net.sourceforge.vietocr;

import com.apple.eawt.*;
import com.apple.eawt.AppEvent.*;
import java.io.File;

/**
 *  Mac OS X functionality for VietOCR.
 *
 *@author     Quan Nguyen
 *@modified   February 27, 2011
 */
class MacOSXApplication {

    private static final int ZOOM_LIMIT = 60;

    Application app = null;

    /**
     *  Constructor for the MacOSXApplication object.
     *
     *@param  vietOCR  calling instance of VietOCR
     */
    public MacOSXApplication(final Gui vietOCR) {
        app = Application.getApplication();
        app.setAboutHandler(new AboutHandler() {

            @Override
            public void handleAbout(AboutEvent ae) {
                vietOCR.about();
            }
        });
        app.setOpenFileHandler(new OpenFilesHandler() {

            @Override
            public void openFiles(OpenFilesEvent ofe) {
                File droppedFile = ofe.getFiles().get(0);
                if (droppedFile.isFile() && vietOCR.promptToSave()) {
                    vietOCR.openFile(droppedFile);
                }
            }
        });
        app.setPreferencesHandler(new PreferencesHandler() {

            @Override
            public void handlePreferences(PreferencesEvent pe) {
                vietOCR.jMenuItemOptionsActionPerformed(null);
            }
        });
        app.setQuitHandler(new QuitHandler() {

            @Override
            public void handleQuitRequestWith(QuitEvent qe, QuitResponse qr) {
                vietOCR.quit();
            }
        });
    }
}
