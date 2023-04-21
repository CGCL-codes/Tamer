package com.mobile.mcontenu.tl.displays;

import com.mobile.mcontenu.tl.TLMContenuMidlet;
import java.io.IOException;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.StringItem;

/**
 *
 * @author Mhamed
 */
public class LineDisplay extends BaseDisplay {

    public LineDisplay(TLMContenuMidlet midletTLM) {
        super(midletTLM);
    }

    public void run() {
        Alert alert = null;
        try {
            Image logo = Image.createImage("/tl_logo.png");
            mainScreen.append(new ImageItem(null, logo, ImageItem.LAYOUT_CENTER, null));
            StringItem title = new StringItem("", "Transports publics");
            Font fontTitle = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
            title.setFont(fontTitle);
            StringItem subTitle = new StringItem("", "de la région lausanoise");
            Font fontSubTitle = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_LARGE);
            subTitle.setFont(fontSubTitle);
            mainScreen.append(title);
            mainScreen.append(subTitle);
            mainScreen.setCommandListener(new CommandListener() {

                public void commandAction(Command c, Displayable d) {
                    if (c.getCommandType() == Command.EXIT) {
                        midletTLM.notifyDestroyed();
                    }
                    if (c.getCommandType() == Command.BACK) {
                        LinesChoicesDisplay linesChoices = new LinesChoicesDisplay(midletTLM);
                        linesChoices.run();
                    }
                    if (c.getCommandType() == Command.OK) {
                    }
                }
            });
        } catch (Exception ex) {
            alert = new Alert("Erreur", "Une erreur est survenue", null, AlertType.ERROR);
        }
        if (alert == null) {
            display.setCurrent(mainScreen);
        } else {
            display.setCurrent(alert, mainScreen);
        }
    }
}
