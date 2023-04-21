package gui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import com.sun.perseus.model.Stop;
import utils.ResourcesHandler;

public class GUIMainMenu extends Canvas implements Runnable, CommandListener {

    private Command back;

    private Command select;

    private int numOptions;

    private int optionFocus;

    private boolean optionSelected;

    Thread t;

    public GUIMainMenu() {
        optionSelected = false;
        t = null;
        select = new Command(ResourcesHandler.getInstance().getText("select"), Command.OK, 0);
        back = new Command(ResourcesHandler.getInstance().getText("back"), Command.BACK, 1);
        addCommand(select);
        addCommand(back);
        setCommandListener(this);
        numOptions = 4;
        optionFocus = 0;
    }

    public void commandAction(Command command, Displayable diplayable) {
        if (command == select) {
            stop();
            ResourcesHandler.getInstance().playSound("selectoption");
            switch(optionFocus) {
                case 0:
                    optionSelected = true;
                    GUIHandler.getInstance().newGame();
                    break;
                case 1:
                    optionSelected = true;
                    GUIHandler.getInstance().resumeGame2();
                    break;
                case 2:
                    break;
                case 3:
                    GUIHandler.getInstance().showScreen(GUIScreens.SETTINGS);
                    break;
            }
        }
        if (command == back) GUIHandler.getInstance().showScreen(GUIScreens.STARTSCREEN);
    }

    protected void paint(Graphics graphics) {
        graphics.setColor(200, 200, 200);
        graphics.fillRect(0, 0, getWidth(), 18);
        graphics.setColor(106, 106, 106);
        graphics.fillRect(0, 18, getWidth(), 1);
        graphics.setColor(255, 255, 255);
        graphics.fillRect(1, 17, getWidth() - 1, 1);
        graphics.setColor(0, 0, 0);
        graphics.drawImage(ResourcesHandler.getInstance().getImage("menu_splash"), 0, 19, Graphics.LEFT | Graphics.TOP);
        graphics.drawString(ResourcesHandler.getInstance().getText("select_option"), getWidth() / 2, 13, Graphics.BASELINE | Graphics.HCENTER);
        if (optionFocus == 0) graphics.drawImage(ResourcesHandler.getInstance().getImage("menu_newgame_focus"), 0, 30, Graphics.LEFT | Graphics.TOP); else graphics.drawImage(ResourcesHandler.getInstance().getImage("menu_newgame"), 0, 30, Graphics.LEFT | Graphics.TOP);
        if (optionFocus == 1) graphics.drawImage(ResourcesHandler.getInstance().getImage("menu_continue_focus"), 0, 65, Graphics.LEFT | Graphics.TOP); else graphics.drawImage(ResourcesHandler.getInstance().getImage("menu_continue"), 0, 65, Graphics.LEFT | Graphics.TOP);
        if (optionFocus == 2) graphics.drawImage(ResourcesHandler.getInstance().getImage("menu_scores_focus"), 0, 100, Graphics.LEFT | Graphics.TOP); else graphics.drawImage(ResourcesHandler.getInstance().getImage("menu_scores"), 0, 100, Graphics.LEFT | Graphics.TOP);
        if (optionFocus == 3) graphics.drawImage(ResourcesHandler.getInstance().getImage("menu_settings_focus"), 0, 135, Graphics.LEFT | Graphics.TOP); else graphics.drawImage(ResourcesHandler.getInstance().getImage("menu_settings"), 0, 135, Graphics.LEFT | Graphics.TOP);
    }

    protected void keyPressed(int keyCode) {
        switch(getGameAction(keyCode)) {
            case UP:
                ResourcesHandler.getInstance().playSound("changeoption");
                optionFocus--;
                if (optionFocus < 0) optionFocus = numOptions - 1;
                break;
            case DOWN:
                ResourcesHandler.getInstance().playSound("changeoption");
                optionFocus++;
                if (optionFocus >= numOptions) optionFocus = 0;
                break;
            case FIRE:
                commandAction(select, this);
                break;
        }
    }

    public void start() {
        if (t != null) if (t.isAlive()) t.interrupt();
        t = new Thread(this);
        t.start();
    }

    public void stop() {
        optionSelected = true;
        if (t != null) if (t.isAlive()) t.interrupt();
    }

    public void run() {
        optionSelected = false;
        while (!optionSelected) {
            long cycleStartTime = System.currentTimeMillis();
            repaint();
            long timeSinceStart = (cycleStartTime - System.currentTimeMillis());
            if (timeSinceStart < GUIHandler.MS_PER_FRAME) {
                try {
                    Thread.sleep(GUIHandler.MS_PER_FRAME - timeSinceStart);
                } catch (java.lang.InterruptedException e) {
                }
            }
        }
    }
}
