package example.btsppecho;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import java.util.Timer;
import java.util.TimerTask;

class ErrorScreen extends Alert {

    private static Image image;

    private static Display display;

    private static ErrorScreen instance = null;

    private ErrorScreen() {
        super("Error");
        setType(AlertType.ERROR);
        setTimeout(2000);
        setImage(image);
    }

    static void init(Image img, Display disp) {
        image = img;
        display = disp;
    }

    static void showError(String message, Displayable next) {
        if (instance == null) {
            instance = new ErrorScreen();
        }
        if (message == null) {
            message = "";
        }
        instance.setString(message);
        display.setCurrent(instance, next);
    }
}
