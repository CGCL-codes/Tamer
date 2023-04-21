package calculator;

import java.io.IOException;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * @author Administrator
 */
public class Calculator extends MIDlet implements CommandListener {

    private Display display;

    private Form mainform;

    private Alert splashscreen;

    private TextField num1, num2;

    private ChoiceGroup op;

    private StringItem result;

    private Command exit = new Command("Exit", Command.EXIT, 1);

    private Command enter = new Command("OK", Command.OK, 1);

    public Calculator() {
        display = Display.getDisplay(this);
        mainform = new Form("Calculator");
        num1 = new TextField("Number1", "0", 5, TextField.NUMERIC);
        num2 = new TextField("Number2", "0", 5, TextField.NUMERIC);
        op = new ChoiceGroup("", ChoiceGroup.EXCLUSIVE, new String[] { "+", "-", "*", "/" }, null);
        result = new StringItem("=", "0");
        mainform.append(num1);
        mainform.append(op);
        mainform.append(num2);
        mainform.append(result);
        mainform.addCommand(exit);
        mainform.addCommand(enter);
        mainform.setCommandListener(this);
        Image splashimage = null;
        try {
            splashimage = Image.createImage("/calculator/images/splash.PNG");
            splashscreen = new Alert("Calculator", "Loading...", splashimage, AlertType.INFO);
        } catch (IOException ex) {
            ex.printStackTrace();
            splashscreen = new Alert("Calculator", "Loading...", null, AlertType.INFO);
        }
        splashscreen.setTimeout(2000);
    }

    public void startApp() {
        display.setCurrent(splashscreen, mainform);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable d) {
        if (c == enter) {
            int op1 = Integer.parseInt(num1.getString());
            int op2 = Integer.parseInt(num2.getString());
            int ans = 0;
            switch(op.getSelectedIndex()) {
                case 0:
                    ans = op1 + op2;
                    break;
                case 1:
                    ans = op1 - op2;
                    break;
                case 2:
                    ans = op1 * op2;
                    break;
                case 3:
                    ans = (int) (op1 / op2);
                    break;
            }
            result.setText(String.valueOf(ans));
        } else if (c == exit) {
            destroyApp(true);
            notifyDestroyed();
        }
    }
}
