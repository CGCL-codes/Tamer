package j2me_demo;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class TickerTest extends MIDlet implements CommandListener {

    private Display display;

    private List lsProducts;

    private Ticker tkSale;

    private Command cmExit;

    public TickerTest() {
        display = Display.getDisplay(this);
        cmExit = new Command("Exit", Command.SCREEN, 1);
        tkSale = new Ticker("Sale: Real Imitation Cuban Cigars...10 for $10");
        lsProducts = new List("Products", Choice.IMPLICIT);
        lsProducts.append("Wicker Chair", null);
        lsProducts.append("Coffee Table", null);
        lsProducts.addCommand(cmExit);
        lsProducts.setCommandListener(this);
        lsProducts.setTicker(tkSale);
    }

    public void startApp() {
        display.setCurrent(lsProducts);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable s) {
        if (c == List.SELECT_COMMAND) {
            switch(lsProducts.getSelectedIndex()) {
                case 0:
                    System.out.println("Chair selected");
                    break;
                case 1:
                    System.out.println("Table selected");
                    break;
            }
        } else if (c == cmExit) {
            destroyApp(true);
            notifyDestroyed();
        }
    }
}
