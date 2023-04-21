package bp.screens;

import bp.interfaces.*;
import keys.*;
import bp.*;
import international.*;
import java.lang.*;
import javax.microedition.lcdui.*;

/**
 * The screen where one can choose and see the specific current keys configuration and choose to costumize it.
 * 
 * 		@author Joaquim Rocha <jay@neei.uevora.pt>
 * 		@author Valerio Valerio <vdv@neei.uevora.pt>
 */
public class KeysConfigurationScreen extends Screen implements CommandListener {

    private String defaultPCKeysConf, defaultCellKeysConf;

    private Image logo;

    private KeyConfigurer pcKeyConf, cpKeyConf;

    private Command backCommand, pcConfCommand, cpConfCommand;

    private BluePad bp;

    private String[] actions;

    private Graphics g;

    private int choice;

    private String keyChosen;

    private String keyToConfig;

    private KeySender keySender;

    /**
     * Constructs a new <code>KeysConfigurationScreen</code> using specifics arguments.
     * 
     * @param pcKeyConf the computer keys configuration.
     * @param cpKeyConf the cell phone keys configuration.
     * @param logoPath the logo or banner path corresponding to the {@link KeySender} which is configured by this keys configuration.
     * @param defaultPCKeysConf the default computer keys configuration for the {@link KeySender} ks in the form <code>"action1:key1 action2:key2 ..."</code>
     * @param defaultCellKeysConf the default cell keys configuration for the {@link KeySender} ks in the form <code>"action1:key1 action2:key2 ..."</code>
     * @param bp the main MIDlet.
     * @param actions an array of actions. One action per array position.
     * @param ks the {@link KeySender} which has the keys configuration expressed in this screen.
     */
    public KeysConfigurationScreen(KeyConfigurer pcKeyConf, KeyConfigurer cpKeyConf, String logoPath, String defaultPCKeysConf, String defaultCellKeysConf, BluePad bp, String[] actions, KeySender ks) {
        this.pcKeyConf = pcKeyConf;
        this.cpKeyConf = cpKeyConf;
        this.bp = bp;
        this.actions = actions;
        this.choice = 0;
        this.keyChosen = null;
        this.defaultPCKeysConf = defaultPCKeysConf;
        this.defaultCellKeysConf = defaultCellKeysConf;
        this.keySender = ks;
        try {
            logo = Image.createImage(logoPath);
        } catch (Exception e) {
        }
        this.backCommand = new Command(this.bp.getMessage("back"), Command.BACK, 1);
        this.pcConfCommand = new Command(this.bp.getMessage("conf_pc_keys"), this.bp.getMessage("conf_pc_keys_long"), Command.ITEM, 1);
        this.cpConfCommand = new Command(this.bp.getMessage("conf_cp_keys"), this.bp.getMessage("conf_cp_keys_long"), Command.ITEM, 1);
        this.addCommand(this.backCommand);
        this.addCommand(this.pcConfCommand);
        this.addCommand(this.cpConfCommand);
        this.setCommandListener(this);
    }

    /**
     * Paints this screen.
     * 
     * @param g the the specified Graphics context.
     */
    public void paint(Graphics g) {
        this.makeMediumFont(g);
        g.setColor(255, 255, 255);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(this.logo, this.getWidth() / 2 - logo.getWidth() / 2, 0, Graphics.TOP | Graphics.LEFT);
        g.setColor(0, 0, 0);
        int verticalPos = this.logo.getHeight();
        g.drawString(this.bp.getMessage("action"), 10, verticalPos, Graphics.TOP | Graphics.LEFT);
        g.drawString("PC", this.getWidth() / 2, verticalPos, Graphics.TOP | Graphics.HCENTER);
        g.drawString(this.bp.getMessage("phone"), this.getWidth() - 10, verticalPos, Graphics.TOP | Graphics.RIGHT);
        g.drawLine(10, verticalPos + 20, this.getWidth() - 10, verticalPos + 20);
        this.makeSmallFont(g);
        for (int i = 0; i < this.actions.length; i++) {
            if (this.choice == i) g.setColor(0, 125, 212); else g.setColor(0, 0, 0);
            this.drawKeyConf(g, this.actions[i], this.pcKeyConf.getKey(this.actions[i]), this.cpKeyConf.getKey(this.actions[i]), i);
        }
    }

    /**
     * Draws a specific key configuration expressed in this screen.
     * 
     * @param g the Graphics object in which the configuration is drawn.
     * @param actionName the action name that this configuration represents.
     * @param pcKeyName the computer key name assigned to this configuration.
     * @param cpKeyName the cell phone key name assigned to this configuration.
     * @param position the position or order (starting at 1) in which this configuration should be drawn.
     */
    public void drawKeyConf(Graphics g, String actionName, String pcKeyName, String cpKeyName, int position) {
        int verticalPos = this.logo.getHeight() + 10 * position;
        int horizontalPos = 10;
        if (verticalPos + 20 > this.getHeight()) {
            horizontalPos += this.getWidth() / 2;
            System.out.println("Passed over screen!");
        }
        verticalPos += 20;
        String actionNameTrans = actionName;
        try {
            actionNameTrans = this.bp.getMessage(actionName);
        } catch (NullPointerException e) {
        }
        g.drawString(actionNameTrans, horizontalPos, verticalPos + 3, Graphics.TOP | Graphics.LEFT);
        String pc = new String();
        String cf = new String();
        try {
            pc = this.bp.getMessage(pcKeyName.toLowerCase());
        } catch (NullPointerException e) {
            pc = pcKeyName.toUpperCase();
        }
        try {
            cf = this.bp.getMessage(cpKeyName.toLowerCase());
        } catch (NullPointerException e) {
            cf = cpKeyName.toUpperCase();
        }
        g.drawString(pc, this.getWidth() / 2, verticalPos + 3, Graphics.TOP | Graphics.HCENTER);
        g.drawString(cf, this.getWidth() - 10, verticalPos + 3, Graphics.TOP | Graphics.RIGHT);
    }

    /**
     * Called when a key is pressed.
     * 
     * @param keyCode the key code of the key that was pressed.
     */
    public void keyPressed(int keyCode) {
        if (keyCode == KEY_NUM8 || keyCode == -2 || (this.getKeyName(keyCode).toLowerCase()).equals("down")) {
            this.choice += 1;
            if (this.choice == this.actions.length) {
                this.choice = 0;
            }
        } else if (keyCode == KEY_NUM2 || keyCode == -1 || (this.getKeyName(keyCode).toLowerCase()).equals("up")) {
            this.choice -= 1;
            if (this.choice == -1) {
                this.choice = this.actions.length - 1;
            }
        }
        this.repaint();
    }

    /**
     * From interface <code>CommandListener</code>. Indicates that a command event has occurred on Displayable d.
     * 
     * @param c a <code>Command</code> object identifying the command.
     * @param d the <code>Displayable</code> on which this event has occurred.
     */
    public void commandAction(Command c, Displayable d) {
        if (c == backCommand) {
            this.bp.setCurrentScreen((Displayable) this.keySender);
        } else if (c == cpConfCommand) {
            this.bp.setCurrentScreen(new ChooseKeyScreen(this.pcKeyConf, this.cpKeyConf, this.defaultPCKeysConf, this.defaultCellKeysConf, this, this.actions[this.choice], 1));
            System.out.println("I want to configure cellphone key " + this.actions[this.choice]);
        } else if (c == pcConfCommand) {
            this.bp.setCurrentScreen(new ChooseKeyScreen(this.pcKeyConf, this.cpKeyConf, this.defaultPCKeysConf, this.defaultCellKeysConf, this, this.actions[this.choice], 0));
            System.out.println("I want to configure PC " + this.actions[this.choice]);
        }
    }

    /**
     * Gets the translation correspondent to the label <code>msgId</code> from the locale defined in the main MIDlet.
     * 
     * @param msgId the label in the locale file defined in the main MIDlet.
     * @return the text correspondent to the label <code>msgId</code> from the locale defined in the main MIDlet
     */
    protected String getMessage(String msgId) {
        return this.bp.getMessage(msgId);
    }

    /**
     * Sets main MIDlet screen to be <code>d</code>.
     * 
     * @param d the <code>Displayable</code> to set as the main MIDlet screen.
     */
    protected void setBPScreen(Displayable d) {
        this.bp.setCurrentScreen(d);
    }
}
