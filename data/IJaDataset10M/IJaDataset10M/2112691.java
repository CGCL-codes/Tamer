package gnu.classpath.tools.appletviewer;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;

public class ErrorApplet extends Applet {

    public ErrorApplet(String message) {
        setLayout(new BorderLayout());
        Button button = new Button(message);
        add(button);
    }
}
