package bp.screens;

import keys.*;
import bp.*;
import bp.interfaces.*;
import java.lang.*;
import javax.microedition.lcdui.*;

/**
 * The presentation screen.
 * 
 * 		@author Joaquim Rocha <jay@neei.uevora.pt>
 * 		@author Valerio Valerio <vdv@neei.uevora.pt>
 */
public class BluePadPresentationScreen extends Screen implements CommandListener, KeySender {

    private static final String DEFAULT_KEYS_CONF = "fullscreen:f5 next:right previous:left close:esc";

    private static final String DEFAULT_CELL_CONF = "fullscreen:1 next:right previous:left close:0";

    private static final String[] ACTIONS = { "fullscreen", "next", "previous", "close" };

    private Image img, img1, img2, img3;

    private BluePad bp;

    private Image logo;

    private KeyConfigurer pcKeyConf, cfKeyConf;

    private KeysConfigurationScreen keyConfScreen;

    private Command backCommand, optionCommand;

    private String btState;

    private BluePadBluetoothCom btc;

    private KeysMap keysMap;

    /**
     * Creates a presentation screen.
     * 
     * @param bp the main MIDlet.
     * @param btc the bluetooth communication object which is used to send the pressed keys to the computer.
     */
    public BluePadPresentationScreen(BluePad bp, BluePadBluetoothCom btc) {
        this.bp = bp;
        this.pcKeyConf = new KeyConfigurer("presentation", this.DEFAULT_KEYS_CONF);
        this.cfKeyConf = new KeyConfigurer("presentation_cell", this.DEFAULT_CELL_CONF);
        try {
            String folder = "/";
            if (this.getWidth() <= 200) {
                folder = "/small_icons/";
            }
            img = Image.createImage("/images" + folder + "fullscreen.png");
            img1 = Image.createImage("/images" + folder + "next.png");
            img2 = Image.createImage("/images" + folder + "previous.png");
            img3 = Image.createImage("/images" + folder + "close.png");
            this.logo = Image.createImage("/images/presentation_logo.png");
        } catch (Exception e) {
        }
        backCommand = new Command(this.bp.getMessage("back"), Command.BACK, 1);
        optionCommand = new Command(this.bp.getMessage("conf_keys_short"), this.bp.getMessage("conf_keys_long"), Command.ITEM, 1);
        this.addCommand(this.backCommand);
        this.addCommand(this.optionCommand);
        this.setCommandListener(this);
        this.btState = new String();
        this.btc = btc;
        this.keysMap = new KeysMap();
        byte[] b = new byte[1];
        b[0] = (byte) 25;
    }

    /**
     * Paints this screen.
     * 
     * @param g the the specified Graphics context.
     */
    public void paint(Graphics g) {
        g.setColor(255, 255, 255);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(this.logo, this.getWidth() / 2 - logo.getWidth() / 2, 0, Graphics.TOP | Graphics.LEFT);
        g.setColor(0, 0, 0);
        this.drawMenu(g, this.img, this.translateKey(this.cfKeyConf.getKey("fullscreen")), 1);
        this.drawMenu(g, this.img1, this.translateKey(this.cfKeyConf.getKey("next")), 2);
        this.drawMenu(g, this.img2, this.translateKey(this.cfKeyConf.getKey("previous")), 3);
        this.drawMenu(g, this.img3, this.translateKey(this.cfKeyConf.getKey("close")), 4);
    }

    /**
     * Draws a menu in <code>g</code> with the icon <code>img</code> and the <code>assignedKey</code> corresponding to some action.
     * 
     * @param g the the specified Graphics context.
     * @param img the icon correponding to some action.
     * @param assignedKey the assigned key that will perform some action.
     * @param position the position/order in which this menu is drawn (starting at 1).
     */
    public void drawMenu(Graphics g, Image img, String assignedKey, int position) {
        this.makeMediumFont(g);
        int verticalPos = this.logo.getHeight() - img.getHeight() + img.getHeight() * position + img.getHeight() / 2 * position;
        int horizontalPos = this.getWidth() / 2 - (img.getWidth() * 2);
        g.drawImage(img, horizontalPos, verticalPos, Graphics.TOP | Graphics.LEFT);
        g.drawString(assignedKey, 15 + img.getWidth() + horizontalPos, verticalPos, Graphics.TOP | Graphics.LEFT);
    }

    /**
     * Called when a key is repeated (held down).
     * 
     * @param keyCode the code of the key that has been held down.
     */
    public void keyRepeated(int keyCode) {
        this.keyPressed(keyCode);
    }

    /**
     * Called when a key is pressed.
     * 
     * @param keyCode the key code of the key that was pressed.
     */
    public void keyPressed(int keyCode) {
        String pressed = "";
        if (keyCode == this.RIGHT || keyCode == -4 || this.getKeyName(keyCode).toLowerCase().equals("right")) {
            pressed = "right";
        } else if (keyCode == this.LEFT || keyCode == -3 || this.getKeyName(keyCode).toLowerCase().equals("left")) {
            pressed = "left";
        } else if (keyCode == this.UP || keyCode == -1 || this.getKeyName(keyCode).toLowerCase().equals("up")) {
            pressed = "up";
        } else if (keyCode == this.DOWN || keyCode == -2 || this.getKeyName(keyCode).toLowerCase().equals("down")) {
            pressed = "down";
        } else pressed = this.figureOut(this.getKeyName(keyCode));
        for (int i = 0; i < this.ACTIONS.length; i++) {
            if ((this.cfKeyConf.getKey(this.ACTIONS[i]).trim()).toLowerCase().equals(pressed.toLowerCase())) {
                String pcKey = this.pcKeyConf.getKey(this.ACTIONS[i]);
                int kernelKey = this.keysMap.getKernelKeyNum(pcKey.trim());
                System.out.println("PC KEY: " + pcKey + " KERNEL: " + kernelKey);
                this.btc.sendKey("" + kernelKey);
                break;
            }
        }
        this.repaint();
    }

    private String figureOut(String k) {
        if (!k.toUpperCase().startsWith("SOFT")) {
            for (int i = 0; i < k.length(); i++) {
                try {
                    int s = (new Integer(0)).parseInt("" + k.charAt(i));
                    return "" + s;
                } catch (NumberFormatException e) {
                }
            }
        }
        return k;
    }

    /**
     * From interface <code>CommandListener</code>. Indicates that a command event has occurred on Displayable d.
     * 
     * @param c a <code>Command</code> object identifying the command.
     * @param d the <code>Displayable</code> on which this event has occurred.
     */
    public void commandAction(Command c, Displayable d) {
        if (c == backCommand) {
            this.cfKeyConf.saveConf();
            this.cfKeyConf.reload();
            this.bp.setState(this.bp.MAIN_MENU_SCREEN);
        } else if (c == optionCommand) {
            this.keyConfScreen = new KeysConfigurationScreen(pcKeyConf, cfKeyConf, "/images/presentation_logo.png", this.DEFAULT_KEYS_CONF, this.DEFAULT_CELL_CONF, this.bp, this.ACTIONS, this);
            this.bp.setCurrentScreen(this.keyConfScreen);
        }
    }

    private String translateKey(String key) {
        key = key.toLowerCase();
        try {
            try {
                return this.bp.getMessage(key);
            } catch (NullPointerException ex) {
            }
            return this.bp.getMessage(key);
        } catch (NullPointerException e) {
            return key;
        }
    }

    private byte[] int2ByteArray(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }

    /**
     * From the <code>KeySender</code> interface.
     * <p>
	 * Writes bluetooth state in the graphics g.
	 * 
	 * @param g the graphics in which the state whould be written.
	 * @param state the state that whould be written.
	 */
    public void writeBTState(Graphics g, String state) {
        g.setColor(0, 0, 0);
        g.drawString(state, this.getWidth() - 20, this.getHeight() - 20, Graphics.TOP | Graphics.RIGHT);
    }

    /**
     * From the <code>KeySender</code> interface.
     * <p>
     * Sets this class' bluetooth state.
     * 
     * @param btState the bluetooth state to be set.
     */
    public void setBTState(String btState) {
        this.btState = btState;
    }

    /**
     * From the <code>KeySender</code> interface.
     * <p>
     * Gets the kernel key number corresponding to the key name keyName.
     * 
     * @param keyName the key name that corresponds to some kernel key number.
     * @return the kernel key number.
     */
    public int mapKey(String keyName) {
        return this.keysMap.getKernelKeyNum(keyName);
    }
}
