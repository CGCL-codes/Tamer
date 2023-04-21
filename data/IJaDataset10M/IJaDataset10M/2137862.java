package org.goldang.stringtuner;

import javax.microedition.lcdui.Displayable;

/** The main graphical panel of the application.
 * Serves primarily to respond to user commands, and to display basic
 * usage information.
*/
class KeyScreen extends AbstractToneScreen {

    private final KeyCanvas myCanvas;

    public KeyScreen(StringTuner app) {
        super(app);
        myCanvas = new KeyCanvas(this);
        myCanvas.addCommand(myTuneCmd);
        myCanvas.addCommand(myInfoCmd);
        myCanvas.addCommand(myAboutCmd);
        myCanvas.addCommand(myExitCmd);
    }

    Displayable getDisplayable() {
        return myCanvas;
    }

    void reset() {
        myCanvas.repaint();
    }
}
