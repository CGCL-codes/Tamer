package su.msk.dunno.blame.screens;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.prototypes.IScreen;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.TrueTypeFont;
import su.msk.dunno.blame.support.listeners.EventManager;
import su.msk.dunno.blame.support.listeners.KeyListener;

public class HelpScreen implements IScreen {

    private static HelpScreen instance;

    EventManager helpEvents = new EventManager();

    private boolean isHelpOn;

    public static HelpScreen instance() {
        if (instance == null) instance = new HelpScreen();
        return instance;
    }

    private HelpScreen() {
        initEvents();
    }

    public void openHelp() {
        isHelpOn = true;
    }

    public boolean isOpen() {
        return isHelpOn;
    }

    public void initEvents() {
        helpEvents.addListener(Keyboard.KEY_ESCAPE, new KeyListener(0) {

            public void onKeyDown() {
                isHelpOn = false;
            }
        });
    }

    public void process() {
        isHelpOn = true;
        while (isHelpOn) {
            helpEvents.checkEvents();
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            GL11.glLoadIdentity();
            int k = Blame.height - 25;
            TrueTypeFont.instance().drawString("Arrow Keys/Numpad Keys - movement", 20, k, Color.WHITE);
            k -= 25;
            TrueTypeFont.instance().drawString("Tab - switch between characters", 20, k, Color.WHITE);
            k -= 25;
            TrueTypeFont.instance().drawString("F - shoot", 20, k, Color.WHITE);
            k -= 25;
            TrueTypeFont.instance().drawString("L - look", 20, k, Color.WHITE);
            k -= 25;
            TrueTypeFont.instance().drawString("I - open Inventory", 20, k, Color.WHITE);
            k -= 25;
            TrueTypeFont.instance().drawString("W - open Weapon", 20, k, Color.WHITE);
            k -= 25;
            TrueTypeFont.instance().drawString(", - pick up item from the floor", 20, k, Color.WHITE);
            k -= 25;
            TrueTypeFont.instance().drawString("D - drop item from the Inventory", 20, k, Color.WHITE);
            k -= 25;
            TrueTypeFont.instance().drawString("Esc - exit from current screen", 20, k, Color.WHITE);
            k -= 25;
            TrueTypeFont.instance().drawString("      (Weapon, Inventory, etc)", 20, k, Color.WHITE);
            k -= 25;
            TrueTypeFont.instance().drawString("F1 - open this screen", 20, k, Color.WHITE);
            k -= 25;
            TrueTypeFont.instance().drawString("F5 - follow partner on/off", 20, k, Color.WHITE);
            k -= 25;
            TrueTypeFont.instance().drawString("F6 - attack enemies on sight on/off", 20, k, Color.WHITE);
            k -= 25;
            TrueTypeFont.instance().drawString("F12 - exit game", 20, k, Color.WHITE);
            k -= 25;
            TrueTypeFont.instance().drawString("Enter - enter to Rebuild Station", 20, k, Color.WHITE);
            k -= 25;
            Display.sync(Blame.framerate);
            Display.update();
        }
    }
}
