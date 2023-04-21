package com.jpark.jamse;

import java.util.Hashtable;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

/**
 * Player Control Canvas
 * @author klumw
 */
public class ControlCanvas extends GameCanvas implements EventHandler {

    /** show the player*/
    public static final int MODE_PLAYER = 0;

    /** show the seek bar */
    public static final int MODE_SEEK = 1;

    /**
     * Midlet for this component
     */
    private JBMidlet midlet;

    /**
     * Background image
     * @see skin file description
     */
    private Image bgImage;

    /**
     * Button selected sprite
     */
    private Sprite btnSelSprite;

    /**
     * Button pressed sprite
     */
    private Sprite btnPressSprite;

    /**
     * Volume slider sprite
     */
    private Sprite volSprite;

    /**
     * Repeat sprite
     */
    private Sprite rptSprite;

    /**
     * Shuffle sprite
     */
    private Sprite shflSprite;

    /**
     * Button positon values
     * @see skin file description
     */
    private int[] btnValues;

    /**
     * Volume  position values
     * @see skin file description
     */
    private int[] volValues;

    /**
     * Repeat position values
     * @see skin file description
     */
    private int[] rptValues;

    /**
     * Shuffle position values
     * @see skin file description
     */
    private int[] shflValues;

    /**
     * Track name position values
     * @see skin file description
     */
    private int[] tnValues;

    /**
     * Track title position values
     * @see skin file description
     */
    private int[] ttValues;

    /**
     * Component x screen offset
     */
    private int dx;

    /**
     * Component y screen offset
     */
    private int dy;

    /**
     * Track name font
     * @see skin file description
     */
    private Font trackNameFont;

    /**
     * Track time font
     * @see skin file description
     */
    private Font trackTimeFont;

    /**
     * Track name color
     * @see skin file description
     */
    private int[] tnColor;

    /**
     * Track time color
     * @see skin file description
     */
    private int[] ttColor;

    /**
     * Track time string
     */
    private String ttStr;

    /**
     * Track name string
     */
    private String tnStr;

    /**
     * Shuffle button status
     */
    public boolean shufflePressed;

    /**
     * Repeat button status
     */
    public boolean repeatPressed;

    /**
     * Status of player - true playing
     */
    private boolean playState;

    /**
     * Status for pause state - true paused
     */
    private boolean isPaused;

    /**
     * Elapsed player time
     */
    private int elapsed;

    /** 
     * Total song time
     */
    private int totalTime;

    /**
     * Count direction for song title
     * ticker
     */
    private short tnDir;

    /**
     * Offset for song title
     * - used for ticker functionality
     */
    private int tnOffset;

    /**Pause at track name ticker reverse*/
    private int offsetPause;

    /**
     * Off-screen buffer
     */
    private Graphics graphics;

    /** Key Mapper */
    private KeyMapper keyMapper;

    /** volume value*/
    private int volume;

    /** no updates on shuffle or repeat */
    private boolean noButtonUpdate;

    /** no volume update */
    private boolean noVolumeUpdate;

    /** show newMode e.g. player,seek etc.*/
    private int mode = -1;

    /** seek progress bar value*/
    private double seekPos;

    /**
     * Array of button actions for Player control.
     */
    private static final int[] btnAct = { JBMidlet.ACT_BTN_BACK, JBMidlet.ACT_BTN_PLAY, JBMidlet.ACT_BTN_PAUSE, JBMidlet.ACT_BTN_STOP, JBMidlet.ACT_BTN_NEXT };

    /**
     * Mapping array that maps keyboard keys to stored actions.
     */
    private static final int[] ACTION_KEYS = { Canvas.KEY_NUM0, Canvas.KEY_NUM1, Canvas.KEY_NUM2, Canvas.KEY_NUM3, Canvas.KEY_NUM4, Canvas.KEY_NUM5, Canvas.KEY_NUM6, Canvas.KEY_NUM7, Canvas.KEY_NUM8, Canvas.KEY_NUM9, Canvas.KEY_POUND, Canvas.KEY_STAR };

    /** Paint all constant*/
    public static final int PAINT_MODE_ALL = 0;

    /** Paint time field only constant*/
    public static final int PAINT_MODE_TIME = 1;

    /** Paint track name only constant*/
    public static final int PAINT_MODE_NAME = 2;

    /** Canvas dimensions with menu*/
    private int cWidth, cHeight;

    /**
     * Creates a new instance of ControlCanvas.
     * @param midlet - MIDlet instance for this canvas
     */
    public ControlCanvas(JBMidlet midlet) {
        super(false);
        this.midlet = midlet;
        keyMapper = new KeyMapper(this);
        setMode(MODE_PLAYER);
        addMenuCommands();
        volume = 128;
        boolean isFromStore = Store.getStore().isSkinFromStore();
        if (!isFromStore) {
            tnStr = "JAM SE " + JBMidlet.VERSION;
        }
        setSkin(isFromStore);
    }

    public void addMenuCommands() {
        this.addCommand(JBMidlet.CMD_MENU);
        if (ApplicationHelper.isSEJP8AndGreater()) {
            this.addCommand(JBMidlet.CMD_SELECT);
        }
        this.addCommand(JBMidlet.CMD_EXIT);
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int newMode) {
        this.mode = newMode;
        switch(newMode) {
            case MODE_PLAYER:
                break;
            case MODE_SEEK:
                seekPos = (getSongElapsedTimeSeconds() > 0 && getSongElapsedTimeSeconds() < getSongTotalTimeSeconds()) ? (double) getSongElapsedTimeSeconds() / (double) getSongTotalTimeSeconds() : 0;
                paintOverlayBackground();
                paintGauge(seekPos);
                break;
        }
        this.setCommandListener(midlet);
    }

    /**
     * Get the skin height from the
     * file name. We try to support
     * original Bemused files as 
     * good as possible
     * @return height of skin
     */
    public int getSkinHeight() {
        int height = 176;
        if (Store.getStore().getSkinDataString("skinsize", 0) != null) {
            int[] values = Store.getStore().getSkinDataAsIntValues("skinsize");
            return values[1];
        }
        String name = Store.getStore().getSkinDataString("skinname", 0);
        if (name != null && name.length() > 7) {
            name = name.substring(0, name.length() - 4);
            if (name.endsWith("_fs")) {
                height = 188;
            } else if (name.endsWith("_iw")) {
                height = 144;
            }
        }
        return height;
    }

    /**
     * Sets the skin for this canvas.
     * On invalid skin data,
     * the method sets the default skin.
     * @param fromstore - if set the optional skin from
     * the store will be set
     */
    public void setSkin(boolean fromstore) {
        this.setFullScreenMode(Store.getStore().isFullScreen(fromstore));
        cWidth = getWidth();
        cHeight = getHeight();
        try {
            Store.getStore().loadSkinData(fromstore);
            btnValues = Store.getStore().getSkinDataAsIntValues("buttons");
            shflValues = Store.getStore().getSkinDataAsIntValues("shuffle");
            Image skinImg = Store.getStore().getSkinImage(fromstore);
            int skinWidth = skinImg.getWidth();
            int skinHeight = getSkinHeight();
            bgImage = Image.createImage(skinWidth, skinHeight);
            bgImage.getGraphics().drawImage(skinImg, 0, 0, Graphics.TOP | Graphics.LEFT);
            dx = (this.cWidth - bgImage.getWidth()) / 2;
            dy = (this.cHeight - bgImage.getHeight()) / 2;
            int width = getAbsLen(btnValues[0], btnValues[2]);
            width = width - (width % 5);
            int height = getAbsLen(btnValues[1], btnValues[3]);
            int y = skinHeight;
            Image selBtnImg = Image.createImage(skinImg, 0, y, width, height, Sprite.TRANS_NONE);
            btnSelSprite = new Sprite(selBtnImg, (width) / 5, height);
            y += height;
            Image pressBtnImg = Image.createImage(skinImg, 0, y, width, height, Sprite.TRANS_NONE);
            btnPressSprite = new Sprite(pressBtnImg, width / 5, height);
            btnPressSprite.setVisible(false);
            volValues = Store.getStore().getSkinDataAsIntValues("volume");
            y += height;
            width = volValues[4];
            height = volValues[5];
            Image volSliderImg = Image.createImage(skinImg, 0, y, width, height, Sprite.TRANS_NONE);
            volSprite = new Sprite(volSliderImg, width, height);
            volSprite.setRefPixelPosition(0, 0);
            int x = 2 * width;
            rptValues = Store.getStore().getSkinDataAsIntValues("repeat");
            width = getAbsLen(rptValues[0], rptValues[2]);
            height = getAbsLen(rptValues[1], rptValues[3]);
            Image rptImg = Image.createImage(skinImg, x, y, width, height, Sprite.TRANS_NONE);
            rptSprite = new Sprite(rptImg, width, height);
            x += width;
            width = getAbsLen(shflValues[0], shflValues[2]);
            height = getAbsLen(shflValues[1], shflValues[3]);
            Image shflImg = Image.createImage(skinImg, x, y, width, height, Sprite.TRANS_NONE);
            shflSprite = new Sprite(shflImg, width, height);
            trackNameFont = getSkinFont(Store.getStore().getSkinDataAsIntValues("tracknamefont")[0]);
            trackTimeFont = getSkinFont(Store.getStore().getSkinDataAsIntValues("tracktimefont")[0]);
            tnValues = Store.getStore().getSkinDataAsIntValues("trackname");
            ttValues = Store.getStore().getSkinDataAsIntValues("tracktime");
            tnColor = Store.getStore().getSkinDataAsIntValues("tracknamecolour");
            ttColor = Store.getStore().getSkinDataAsIntValues("tracktimecolour");
            graphics = this.getGraphics();
            setVolume(volume);
            rptSprite.setPosition(dx + rptValues[0], dy + rptValues[1]);
            shflSprite.setPosition(dx + shflValues[0], dy + shflValues[1]);
            this.setButton(1);
            String[] author = Store.getStore().getSkinDataStringArray("author");
            if (author != null && author[0] != null && author[0].length() > 0) {
                tnStr = "Design: ";
                for (int i = 0; i < author.length; i++) {
                    tnStr += author[i] + " ";
                }
            }
            if (fromstore != Store.getStore().isSkinFromStore()) {
                Store.getStore().setSkinFromStore(fromstore);
                Store.getStore().save();
            }
        } catch (Exception ex) {
            if (fromstore) {
                setSkin(false);
                midlet.showError("Invalid  Skin data", this, false);
            } else {
                midlet.showError("Unable to load Skin,", null, true);
            }
        }
    }

    /**
     * Paint method used for a full canvas paint.
     * @param graphics Graphics object
     */
    public void paint(Graphics graphics) {
        canvasPaint(PAINT_MODE_ALL);
    }

    /**
     * Paints this canvas using the off-screen buffer.
     * Does a full paint.
     * @param newMode paint newMode used e.g PAINT_MODE_ALL
     */
    public synchronized void canvasPaint(int mode) {
        switch(getMode()) {
            case MODE_PLAYER:
                paintPlayer(mode);
                break;
            case MODE_SEEK:
                paintGauge(seekPos);
                break;
        }
        this.flushGraphics();
    }

    private void paintPlayer(int mode) {
        if (mode == PAINT_MODE_NAME) {
            graphics.setClip(dx + Math.min(tnValues[0], tnValues[2]), dy + Math.min(tnValues[1], tnValues[3]), getAbsLen(tnValues[2], tnValues[0]), getAbsLen(tnValues[3], tnValues[1]));
        } else {
            graphics.setClip(0, 0, cWidth, cHeight);
        }
        int[] bgColor = Store.getStore().getBackgroundColor();
        graphics.setColor(bgColor[0], bgColor[1], bgColor[2]);
        graphics.fillRect(0, 0, this.cWidth, this.cHeight);
        graphics.drawImage(bgImage, dx, dy, Graphics.TOP | Graphics.LEFT);
        volSprite.paint(graphics);
        int x = dx + btnValues[0] + (btnSelSprite.getFrame() * btnSelSprite.getWidth());
        btnSelSprite.setRefPixelPosition(x, dy + btnValues[1]);
        btnPressSprite.paint(graphics);
        btnSelSprite.paint(graphics);
        shflSprite.setVisible(shufflePressed);
        shflSprite.paint(graphics);
        rptSprite.setVisible(repeatPressed);
        rptSprite.paint(graphics);
        graphics.setFont(trackTimeFont);
        graphics.setColor(ttColor[0], ttColor[1], ttColor[2]);
        int minutes = elapsed / 60;
        int seconds = elapsed % 60;
        if (minutes > 99) {
            ttStr = "--:--";
        } else {
            ttStr = ((minutes < 10) ? "0" : "") + minutes + ":" + ((seconds < 10) ? "0" : "") + seconds;
        }
        graphics.drawString(ttStr, dx + getAlignedXPos(ttStr, graphics.getFont(), ttValues), dy + 2 + getAlignedYPos(graphics.getFont(), ttValues), Graphics.TOP | Graphics.LEFT);
        graphics.setFont(trackNameFont);
        graphics.setColor(tnColor[0], tnColor[1], tnColor[2]);
        graphics.setClip(dx + Math.min(tnValues[0], tnValues[2]), dy + Math.min(tnValues[1], tnValues[3]), getAbsLen(tnValues[2], tnValues[0]), getAbsLen(tnValues[3], tnValues[1]));
        String tickerStr = tnStr;
        if (graphics.getFont().stringWidth(tickerStr) < (tnValues[2] - tnValues[0])) {
            graphics.drawString(tickerStr, dx + getAlignedXPos(tickerStr, graphics.getFont(), tnValues), dy + getAlignedYPos(graphics.getFont(), tnValues), Graphics.TOP | Graphics.LEFT);
        } else {
            int offset = tnOffset < tickerStr.length() ? tnOffset : 0;
            offset = offset < 0 ? 0 : offset;
            if (!Store.getStore().getTickerOn()) {
                tnOffset = 0;
                offsetPause = 1;
            } else if (tnOffset < 0 && offsetPause < 1) {
                tnOffset = 0;
                offsetPause = 8;
                tnDir = 1;
            }
            String sub = tickerStr.substring(offset);
            graphics.drawString(sub, dx + tnValues[0], dy + getAlignedYPos(graphics.getFont(), tnValues), Graphics.TOP | Graphics.LEFT);
            if (graphics.getFont().stringWidth(sub) < (tnValues[2] - tnValues[0]) && offsetPause < 1) {
                tnDir = -1;
                offsetPause = 8;
            }
        }
    }

    private synchronized void paintGauge(double value) {
        graphics = this.getGraphics();
        graphics.setClip(0, 0, getWidth(), getHeight());
        Image barBackgrnd = Store.getStore().getIcons()[18];
        int inset = 10;
        int wx = 4;
        int wxOffset = 2;
        int wHeight = 12;
        int y = (getHeight() - 12) / 2;
        int len = getWidth() - 2 * inset;
        if (len > 120) {
            len = 120;
            inset = (getWidth() - len) / 2;
        }
        Image bgrnd = Image.createImage(wx, wHeight);
        bgrnd.getGraphics().drawRegion(barBackgrnd, 2, 0, wx, wHeight, Sprite.TRANS_NONE, 0, 0, Graphics.TOP | Graphics.LEFT);
        Image grnBar = Image.createImage(wx, wHeight);
        grnBar.getGraphics().drawRegion(barBackgrnd, 6, 0, wx, wHeight, Sprite.TRANS_NONE, 0, 0, Graphics.TOP | Graphics.LEFT);
        graphics.setColor(0xFFFFFF);
        graphics.setFont(Font.getDefaultFont());
        graphics.drawString("Seek Position:", inset, y - Font.getDefaultFont().getHeight() - 2, Graphics.TOP | Graphics.LEFT);
        graphics.drawImage(barBackgrnd, inset, y, Graphics.TOP | Graphics.LEFT);
        for (int i = 1; i * wx < len; i++) {
            graphics.drawImage(bgrnd, i * wx + inset + wxOffset, y, Graphics.TOP | Graphics.LEFT);
        }
        int glen = (int) (value * (len - 4));
        for (int j = 0; j * grnBar.getWidth() <= glen; j++) {
            graphics.drawImage(grnBar, inset + wxOffset + (j * grnBar.getWidth()), y, Graphics.TOP | Graphics.LEFT);
        }
    }

    public double getSeekPos() {
        return seekPos;
    }

    private void paintOverlayBackground() {
        graphics.setClip(0, 0, getWidth(), getHeight());
        Image backgrnd = Store.getStore().getIcons()[19];
        for (int jx = 0; jx <= getWidth() / 12; jx++) {
            for (int jy = 0; jy <= getHeight() / 12; jy++) {
                graphics.drawImage(backgrnd, jx * 12, jy * 12, Graphics.TOP | Graphics.LEFT);
            }
        }
    }

    /**
     * Get difference of two pixel values
     * as absolute value.
     * @param x1 axis value 1
     * @param x2 axis value 2
     * @return distance between axis values
     */
    private int getAbsLen(int x1, int x2) {
        int len = Math.max(x1, x2) - Math.min(x2, x1);
        return len;
    }

    /**
     * Get the skin font for
     * given skin font id (1-6).
     * Font type is implementation dependent.
     * @param id skin font id
     * @return Font type for given skin font id
     */
    private Font getSkinFont(int id) {
        switch(id) {
            case 1:
                return Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
            case 2:
                return Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
            case 3:
                return Font.getFont(Font.FACE_SYSTEM, Font.STYLE_ITALIC, Font.SIZE_SMALL);
            case 4:
                return Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
            case 5:
                return Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
            case 6:
                return Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
            default:
                return Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        }
    }

    /**
     * Returns Y pos with vertical Font center alignment.
     * @param f Font used
     * @param value Recangle containing string for given font.
     * @return Aligned Y position
     */
    private int getAlignedYPos(Font f, int[] value) {
        int y = value[1];
        int abslen = getAbsLen(value[1], value[3]);
        if (abslen < f.getHeight()) {
            return y;
        } else {
            int diff = abslen - f.getHeight();
            if (diff > 0) {
                diff /= 2;
            }
            return y + diff;
        }
    }

    /**
     * Returns X pos for horizontal string alignment.
     * @param str String to render
     * @param f Font used
     * @param value String rectangle
     * @return Horizontal center aligned x pos
     */
    private int getAlignedXPos(String str, Font f, int[] value) {
        int strLen = f.stringWidth(str);
        int x = value[0];
        int len = getAbsLen(value[0], value[2]);
        int diff = len - strLen;
        if (diff > 0) {
            diff /= 2;
        }
        return x + diff;
    }

    /**
     * Keypressed handler
     * @param keyCode key code
     */
    protected void keyPressed(int keyCode) {
        keyboardAction(keyMapper.keyPressed(keyCode));
    }

    /**
     * Action for a key press event or
     * a menu list selection
     * @param key - key mapping for this action
     */
    public void keyboardAction(int key) {
        switch(key) {
            case Canvas.LEFT:
                nextButton(true);
                break;
            case Canvas.RIGHT:
                nextButton(false);
                break;
            case Canvas.UP:
                if (getMode() == MODE_PLAYER) {
                    handleAction(JBMidlet.ACT_MENU_VOL_UP);
                } else {
                    nextGauge(true);
                }
                break;
            case Canvas.DOWN:
                if (getMode() == MODE_PLAYER) {
                    handleAction(JBMidlet.ACT_MENU_VOL_DWN);
                } else {
                    nextGauge(false);
                }
                break;
            case Canvas.FIRE:
                if (!ApplicationHelper.isSEJP8AndGreater()) {
                    if (getMode() == MODE_PLAYER) {
                        pressedButton(true);
                        int action = btnAct[btnSelSprite.getFrame()];
                        handleAction(action);
                    } else {
                        midlet.commandAction(JBMidlet.CMD_OK, this);
                    }
                }
                break;
            case Canvas.KEY_NUM0:
            case Canvas.KEY_NUM1:
            case Canvas.KEY_NUM2:
            case Canvas.KEY_NUM3:
            case Canvas.KEY_NUM4:
            case Canvas.KEY_NUM5:
            case Canvas.KEY_NUM6:
            case Canvas.KEY_NUM7:
            case Canvas.KEY_NUM8:
            case Canvas.KEY_NUM9:
            case Canvas.KEY_POUND:
            case Canvas.KEY_STAR:
                int[] actions = Store.getStore().getActions();
                for (int i = 0; i < ACTION_KEYS.length; i++) {
                    if (key == ACTION_KEYS[i] && actions[i] > 0) {
                        midlet.handleAction(actions[i]);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void nextGauge(boolean right) {
        if (right) {
            seekPos += 0.1;
            if (seekPos > 1) {
                seekPos = 1;
            }
        } else {
            seekPos -= 0.1;
            if (seekPos < 0) {
                seekPos = 0;
            }
            paintGauge(seekPos);
        }
    }

    /**
     * Handle player and mapped actions for this control.
     * @param action, Action token to be executed
     * @return If the action token was executed by this method true is returned.
     */
    public boolean handleAction(int action) {
        int cmd = -1;
        Object argument = null;
        switch(action) {
            case JBMidlet.ACT_BTN_PRESS:
                if (getMode() == MODE_PLAYER) {
                    pressedButton(true);
                    handleAction(btnAct[btnSelSprite.getFrame()]);
                } else {
                    midlet.commandAction(JBMidlet.CMD_OK, this);
                }
                Callback cb = new Callback(this) {

                    public void execute() {
                        pressedButton(false);
                    }
                };
                midlet.addDelayedEvent(cb, 700);
                break;
            case JBMidlet.ACT_BTN_BACK:
                cmd = ProtocolHandler.CMD_PREV;
                break;
            case JBMidlet.ACT_BTN_NEXT:
                cmd = ProtocolHandler.CMD_NEXT;
                break;
            case JBMidlet.ACT_BTN_PAUSE:
                cmd = ProtocolHandler.CMD_PAUS;
                break;
            case JBMidlet.ACT_BTN_PLAY:
                cmd = ProtocolHandler.CMD_STRT;
                break;
            case JBMidlet.ACT_BTN_STOP:
                cmd = ProtocolHandler.CMD_STOP;
                playState = false;
                elapsed = 0;
                break;
            case JBMidlet.ACT_MENU_VOL_DWN:
                nextVolume(false);
                cmd = ProtocolHandler.CMD_VOLM;
                argument = ApplicationHelper.getValueAsByteArray(getVolume());
                noVolumeUpdate = true;
                break;
            case JBMidlet.ACT_MENU_VOL_UP:
                nextVolume(true);
                cmd = ProtocolHandler.CMD_VOLM;
                argument = ApplicationHelper.getValueAsByteArray(getVolume());
                noVolumeUpdate = true;
                break;
            case JBMidlet.ACT_MENU_SHFL:
                shufflePressed = !shufflePressed;
                cmd = ProtocolHandler.CMD_SHFL;
                argument = ApplicationHelper.getBooleanToByteArray(shufflePressed);
                noButtonUpdate = true;
                break;
            case JBMidlet.ACT_MENU_RPT:
                repeatPressed = !repeatPressed;
                cmd = ProtocolHandler.CMD_REPT;
                argument = ApplicationHelper.getBooleanToByteArray(repeatPressed);
                noButtonUpdate = true;
                break;
            default:
                return false;
        }
        if (cmd > -1) {
            midlet.getEventManager().enqueue(new Event(this, Event.TYPE_BM, cmd, argument));
        }
        switch(cmd) {
            case ProtocolHandler.CMD_NEXT:
            case ProtocolHandler.CMD_PREV:
            case ProtocolHandler.CMD_STRT:
            case ProtocolHandler.CMD_PAUS:
                midlet.addDelayedEvent(midlet.infoEvent, 1000);
        }
        if (isShown()) {
            canvasPaint(PAINT_MODE_ALL);
        }
        return true;
    }

    /**
     * Key Repeat handler
     * @param keyCode key code
     */
    protected void keyRepeated(int keyCode) {
    }

    /**
     * Key released handler.
     * @param keyCode key code
     */
    protected void keyReleased(int keyCode) {
        switch(keyMapper.keyReleased(keyCode)) {
            case Canvas.FIRE:
                pressedButton(false);
                break;
        }
        canvasPaint(PAINT_MODE_ALL);
    }

    /**
     * Set a specific player button.
     * @param btn button index
     */
    public void setButton(int btn) {
        if (btn < btnSelSprite.getFrameSequenceLength()) {
            btnSelSprite.setFrame(btn);
            btnPressSprite.setFrame(btn);
        }
    }

    /**
     * Set the next player button.
     * @param left if true move left else move right
     */
    public void nextButton(boolean left) {
        int idx = btnSelSprite.getFrame();
        if (left) {
            if (idx == 0) {
                idx = 4;
            } else {
                --idx;
            }
        } else {
            if (idx == 4) {
                idx = 0;
            } else {
                idx++;
            }
        }
        this.setButton(idx);
    }

    /**
     * Move volume slider to next position.
     * @param up - if true move up else move down
     */
    public void nextVolume(boolean up) {
        int step = 16;
        int val = getVolume() / step;
        val *= step;
        if (up && getVolume() < 255) {
            setVolume(val + step);
        } else if (up == false && getVolume() > 0) {
            setVolume(val - step);
        }
        canvasPaint(PAINT_MODE_ALL);
    }

    /**
     * Button pressed action.
     * @param pressed if true show pressed button else show unpressed button
     */
    public void pressedButton(boolean pressed) {
        if (pressed) {
            btnPressSprite.setFrame(btnSelSprite.getFrame());
            btnPressSprite.setPosition(btnSelSprite.getRefPixelX(), btnSelSprite.getRefPixelY());
        }
        btnPressSprite.setVisible(pressed);
        btnSelSprite.setVisible(!pressed);
    }

    /**
     * Return the logical volume (0-255).
     * @return Logical volume
     */
    public int getVolume() {
        return volume;
    }

    /**
     * Set graphical volume level.
     *
     * @param vol volume level range (0-255)
     */
    public void setVolume(int vol) {
        this.volume = vol;
        if (volume < 0) {
            volume = 0;
        }
        if (volume > 255) {
            volume = 255;
        }
        int pos;
        boolean isVertical = getAbsLen(volValues[1], volValues[3]) > getAbsLen(volValues[0], volValues[2]);
        int steps;
        if (isVertical) {
            steps = getAbsLen(volValues[1], volValues[3]);
            pos = steps * (255 - volume) / 255;
            volSprite.setPosition(dx + volValues[0], dy + Math.min(volValues[1], volValues[3]) + pos);
        } else {
            steps = getAbsLen(volValues[0], volValues[2]);
            pos = steps * volume / 255;
            volSprite.setPosition(dx + Math.min(volValues[0], volValues[2]) + pos, dy + volValues[1]);
        }
    }

    /**
     * Event handler interface method
     * @param manager - Event manager
     * @param event - Event received
     */
    public void receiveEvent(EventManager manager, Event event) {
        if (event.getType() == Event.TYPE_BM_RX && (this != event.getSource())) {
            switch(event.getSubType()) {
                case ProtocolHandler.CMD_INFO:
                    updateInfo((Hashtable) event.getValue());
                    if (getMode() == MODE_PLAYER) {
                        canvasPaint(PAINT_MODE_ALL);
                    }
                    break;
                case ProtocolHandler.CMD_GVOL:
                    if (!noVolumeUpdate) {
                        setVolume(((Integer) event.getValue()).intValue());
                    } else {
                        noVolumeUpdate = false;
                    }
                    break;
                case ProtocolHandler.CMD_STRT:
                    playState = true;
                    break;
                default:
                    manager.invokeNext(event);
            }
        } else {
            manager.invokeNext(event);
        }
    }

    /**
     * Updates the player states with the server data
     * received
     * @param info - player info data received from server
     */
    private void updateInfo(Hashtable info) {
        if (!((String) info.get("TNAME")).equals(tnStr)) {
            tnStr = (String) info.get("TNAME");
            tnDir = 1;
            tnOffset = 0;
        }
        if (!noButtonUpdate) {
            shufflePressed = ((Boolean) info.get("SHFL")).booleanValue();
            repeatPressed = ((Boolean) info.get("RPT")).booleanValue();
        } else {
            noButtonUpdate = false;
        }
        playState = ((Boolean) info.get("PLAYSTATE")).booleanValue();
        isPaused = ((Boolean) info.get("PAUSE")).booleanValue();
        elapsed = ((Integer) info.get("CURLEN")).intValue();
        totalTime = ((Integer) info.get("SONGLEN")).intValue();
    }

    public String getSongTitle() {
        return tnStr;
    }

    public int getSongTotalTimeSeconds() {
        return totalTime;
    }

    public int getSongElapsedTimeSeconds() {
        return elapsed;
    }

    /**
     * Update player with local data
     * e.g. update player time independently
     * from the server.
     */
    public void update() {
        if (getMode() == MODE_PLAYER) {
            updatePlayState();
            updateTicker();
            canvasPaint(PAINT_MODE_ALL);
        }
    }

    /**
     * Updates the player time
     * if player is not paused
     * or stopped.
     *
     */
    public void updatePlayState() {
        if (playState && !isPaused) {
            elapsed++;
        }
    }

    /**
     * Updates the track name ticker
     * on reverse pause. That means the
     * ticker stops at the end of each ticker turn
     * pausing for the set offsetpause time
     *
     */
    public void updateTicker() {
        if (Store.getStore().getTickerOn()) {
            if (offsetPause > 0) {
                --offsetPause;
            }
            if (offsetPause == 0) {
                tnOffset += tnDir;
            }
        } else {
            tnOffset = 0;
            offsetPause = 1;
        }
    }

    /**
     * Debug method
     * @param msg - msg to print
     */
    private void log(String msg) {
        ApplicationHelper.log(msg);
    }
}
