package javax.microedition.lcdui;

import com.sun.midp.lcdui.*;
import com.sun.midp.configurator.Constants;
import com.sun.midp.chameleon.skins.*;
import com.sun.midp.chameleon.layers.PopupLayer;
import com.pspkvm.system.VMSettings;

/**
 * This is a popup layer that handles a sub-popup within the text tfContext
 * @author Amir Uval
 */
class VirtualKeyboard_qwert extends VirtualKeyboardInterface {

    /** instance of the virtual keyboard listener */
    VirtualKeyboardListener vkl;

    int kbX;

    int kbY;

    int kbWidth;

    int kbHeight;

    int fontW;

    int fontH;

    int buttonW;

    int buttonH;

    int fontWCenter;

    int fontHTop;

    int maxRows;

    int maxColumns;

    int fullColumns;

    int currentChar = 0;

    int currentKeyboard = 1;

    int candidateFieldHeight = 0;

    char itemIndexWhenPressed;

    char PRESS_OUT_OF_BOUNDS = 0;

    /** array of all available keys n the keyboard */
    char[][] keys;

    boolean inMetaKeys = false;

    boolean inShift = false;

    int currentMeta = 0;

    Image[] metaKeys = null;

    boolean textKbd = false;

    Font f;

    boolean select_active = false;

    /**
     * Virtual Keyboard constructor.
     * 
     * @param keys array of available keys for the keyboard
     * @param vkl the virtual keyboard listener
     * @param displayTextArea flag to indicate whether to display the text area
     */
    public VirtualKeyboard_qwert(char[][] keys, VirtualKeyboardListener vkl, boolean displayTextArea, int neededColumns, int neededRows) throws VirtualKeyboardException {
        if ("on".equals(com.pspkvm.system.VMSettings.get("com.pspkvm.virtualkeyboard.autoopen"))) {
            USE_VIRTUAL_KEYBOARD_OPEN_AUTO = true;
        }
        select_active = false;
        textKbd = displayTextArea;
        if (textKbd) {
            PADDING = 1;
        } else {
            PADDING = 2;
        }
        currentKeyboard = 0;
        this.vkl = vkl;
        kbX = PADDING;
        kbY = PADDING;
        kbWidth = vkl.getAvailableWidth() - 2 * PADDING;
        kbHeight = vkl.getAvailableHeight() - 2 * PADDING;
        f = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        fontW = f.charWidth('M');
        fontH = f.getHeight();
        if (textKbd) {
            buttonW = fontW + 8;
            buttonH = fontH + 8;
            fontHTop = (buttonH - fontH) / 2;
            fontWCenter = buttonW / 2;
        } else {
            buttonW = fontW * 3;
            buttonH = fontH * 3;
            fontHTop = (buttonH - fontH) / 2;
            fontWCenter = buttonW / 2;
        }
        candidateFieldHeight = 0;
        maxRows = (kbHeight - PADDING) / (buttonH + PADDING);
        if (textKbd) {
            if (neededColumns == 0) {
                maxColumns = (kbWidth - PADDING) / (buttonW + PADDING);
            } else {
                maxColumns = neededColumns;
            }
            kbWidth = maxColumns * (buttonW + PADDING) + PADDING + 1;
            if ((neededColumns == 10) && (kbWidth < (getMetakeyRowWidth() + (3 * META_PADDING)))) {
                kbWidth = getMetakeyRowWidth() + (3 * META_PADDING);
                buttonW = (kbWidth - PADDING * (maxColumns + 1)) / maxColumns;
            }
            kbX = (vkl.getAvailableWidth() - kbWidth) / 2;
        } else {
            maxColumns = 7;
            kbWidth = maxColumns * (buttonW + PADDING) + PADDING + 1;
            kbX = 0;
        }
        if (neededRows == 0) {
            int tmpMax = 0;
            for (int i = 0; i < keys.length; i++) {
                if (tmpMax < keys[i].length) tmpMax = keys[i].length;
            }
            neededRows = (tmpMax + maxColumns - 1) / maxColumns;
        }
        if (neededRows > maxRows) {
            System.err.println("Keys list is too long for this size of screen.");
            System.err.println("Please split your keyboard array to multiple arrays.");
            throw new VirtualKeyboardException("Keys list is too long for this size of screen.");
        }
        maxRows = neededRows;
        int neededHeight = 0;
        if (textKbd) {
            neededHeight = maxRows * (buttonH + PADDING) + 4 * PADDING + IMAGE_SIZE + META_PADDING * 4 + candidateFieldHeight;
            kbY = kbHeight - neededHeight - 4 * PADDING;
            kbHeight = neededHeight;
        } else {
            neededHeight = maxRows * (buttonH + PADDING) + 3 * PADDING;
            kbY = vkl.getAvailableHeight() - neededHeight;
            kbHeight = neededHeight;
        }
        this.keys = keys;
        if (textKbd) currentKeyboard = 1; else currentKeyboard = 0;
        fullColumns = keys[currentKeyboard].length / maxColumns;
        if (displayTextArea) {
            metaKeys = new Image[METAKEY_COUNT];
            metaKeys[OK_META_KEY] = Image.createImage(Imgs_qw_clip.ok, 0, Imgs_qw_clip.ok.length);
            metaKeys[CANCEL_META_KEY] = Image.createImage(Imgs_qw_clip.cancel, 0, Imgs_qw_clip.cancel.length);
            metaKeys[BACKSPACE_META_KEY] = Image.createImage(Imgs_qw_clip.backspace, 0, Imgs_qw_clip.backspace.length);
            metaKeys[SHIFT_META_KEY] = Image.createImage(Imgs_qw_clip.shift, 0, Imgs_qw_clip.shift.length);
            metaKeys[CAPS_META_KEY] = Image.createImage(Imgs_qw_clip.caps, 0, Imgs_qw_clip.caps.length);
            metaKeys[MODE_META_KEY] = Image.createImage(Imgs_qw_clip.mode, 0, Imgs_qw_clip.mode.length);
            metaKeys[CNINPUT_META_KEY] = Image.createImage(Imgs_qw_clip.cn, 0, Imgs_qw_clip.cn.length);
            metaKeys[CPY_META_KEY] = LongArrayHandler.createImage(Imgs_qw_clip.cpy_seg, Imgs_qw_clip.cpy_segpad);
            metaKeys[PST_META_KEY] = LongArrayHandler.createImage(Imgs_qw_clip.pst_seg, Imgs_qw_clip.pst_segpad);
            metaKeys[SEL_META_KEY] = LongArrayHandler.createImage(Imgs_qw_clip.sel_seg, Imgs_qw_clip.sel_segpad);
        }
    }

    /**
     * traverse the virtual keyboard according to key pressed.
     * 
     * @param type type of keypress
     * @param keyCode key code of key pressed
     */
    void traverse(int type, int keyCode) {
        System.out.println("VirtualK: keyCode=" + keyCode);
        if (!inMetaKeys) {
            if (type == EventConstants.RELEASED && keyCode != Constants.KEYCODE_SELECT) {
            } else {
                switch(keyCode) {
                    case Constants.KEYCODE_RIGHT:
                        currentChar++;
                        if (currentChar > keys[currentKeyboard].length - 1) {
                            currentChar = 0;
                        }
                        break;
                    case Constants.KEYCODE_LEFT:
                        currentChar--;
                        if (currentChar < 0) {
                            currentChar = keys[currentKeyboard].length - 1;
                        }
                        break;
                    case Constants.KEYCODE_UP:
                        currentChar = (currentChar - maxColumns);
                        if (currentChar < 0) {
                            currentChar = currentChar + (fullColumns + 1) * maxColumns;
                            if (currentChar > keys[currentKeyboard].length - 1) {
                                currentChar -= maxColumns;
                            }
                        }
                        break;
                    case Constants.KEYCODE_DOWN:
                        currentChar = (currentChar + maxColumns);
                        if (currentChar > keys[currentKeyboard].length - 1) {
                            currentChar = (currentChar - maxColumns);
                            inMetaKeys = true;
                        }
                        break;
                    case Constants.KEYCODE_SELECT:
                        vkl.virtualKeyEntered(type, keys[currentKeyboard][currentChar]);
                        if (inShift && type == EventConstants.PRESSED) {
                            inShift = false;
                            if (textKbd) {
                                currentKeyboard = 1;
                                vkl.virtualMetaKeyEntered(IM_CHANGED_KEY);
                            } else {
                                currentKeyboard = 0;
                            }
                        }
                        break;
                }
            }
        } else {
            if (type != EventConstants.RELEASED) {
                switch(keyCode) {
                    case Constants.KEYCODE_RIGHT:
                        currentMeta++;
                        if (currentMeta > metaKeys.length - 1) {
                            currentMeta = 0;
                        }
                        break;
                    case Constants.KEYCODE_LEFT:
                        currentMeta--;
                        if (currentMeta < 0) {
                            currentMeta = metaKeys.length - 1;
                        }
                        break;
                    case Constants.KEYCODE_UP:
                        inMetaKeys = false;
                        break;
                    case Constants.KEYCODE_DOWN:
                        inMetaKeys = false;
                        currentChar = (currentChar + maxColumns);
                        if (currentChar > keys[currentKeyboard].length - 1) {
                            currentChar = currentChar % maxColumns;
                        }
                        break;
                    case Constants.KEYCODE_SELECT:
                        switch(currentMeta) {
                            case SHIFT_META_KEY:
                                if (currentKeyboard == 1) {
                                    currentKeyboard = 2;
                                    vkl.virtualMetaKeyEntered(IM_CHANGED_KEY);
                                }
                                inShift = true;
                                inMetaKeys = false;
                                break;
                            case CAPS_META_KEY:
                                if (currentKeyboard == 1) {
                                    currentKeyboard = 2;
                                } else if (currentKeyboard == 2) {
                                    currentKeyboard = 1;
                                }
                                vkl.virtualMetaKeyEntered(IM_CHANGED_KEY);
                                break;
                            case MODE_META_KEY:
                                currentKeyboard = (currentKeyboard + 1) % keys.length;
                                vkl.virtualMetaKeyEntered(IM_CHANGED_KEY);
                                break;
                            case BACKSPACE_META_KEY:
                                vkl.virtualMetaKeyEntered(BACKSPACE_META_KEY);
                                break;
                            case OK_META_KEY:
                                vkl.virtualMetaKeyEntered(OK_META_KEY);
                                break;
                            case CANCEL_META_KEY:
                                vkl.virtualMetaKeyEntered(CANCEL_META_KEY);
                                break;
                            case CNINPUT_META_KEY:
                                vkl.virtualMetaKeyEntered(CNINPUT_META_KEY);
                                break;
                            case SEL_META_KEY:
                            case CPY_META_KEY:
                            case PST_META_KEY:
                                vkl.virtualMetaKeyEntered(currentMeta);
                                break;
                        }
                }
            }
        }
        if (type != EventConstants.RELEASED) {
            if (EventConstants.SYSTEM_KEY_CLEAR == KeyConverter.getSystemKey(keyCode)) {
                vkl.virtualMetaKeyEntered(BACKSPACE_META_KEY);
            } else {
                switch(keyCode) {
                    case Canvas.KEY_POUND:
                        vkl.virtualMetaKeyEntered(CNINPUT_META_KEY);
                        break;
                    case Canvas.KEY_STAR:
                        currentKeyboard = (currentKeyboard + 1) % keys.length;
                        vkl.virtualMetaKeyEntered(IM_CHANGED_KEY);
                        break;
                    case Canvas.KEY_NUM2:
                        vkl.virtualMetaKeyEntered(CURSOR_UP_META_KEY);
                        break;
                    case Canvas.KEY_NUM4:
                        vkl.virtualMetaKeyEntered(CURSOR_LEFT_META_KEY);
                        break;
                    case Canvas.KEY_NUM6:
                        vkl.virtualMetaKeyEntered(CURSOR_RIGHT_META_KEY);
                        break;
                    case Canvas.KEY_NUM8:
                        vkl.virtualMetaKeyEntered(CURSOR_DOWN_META_KEY);
                        break;
                    case Canvas.KEY_NUM5:
                        vkl.virtualMetaKeyEntered(CNINPUT_SELECT_META_KEY);
                        break;
                }
            }
        }
        vkl.repaintVK();
    }

    /**
     * paint the virtual keyboard on the screen
     * 
     * @param g The graphics context to paint to
     */
    protected void paint(Graphics g) {
        int actualHeight = kbHeight + candidateFieldHeight;
        g.setFont(f);
        g.setColor(LIGHT_GRAY);
        g.fillRect(0, 0, kbWidth, actualHeight);
        drawBorder(g, 0, 0, kbWidth - 1, actualHeight - 1);
        if (candidateFieldHeight > 0) {
            drawCandidateBar(g);
        }
        g.translate(0, candidateFieldHeight);
        drawKeys(g);
        g.translate(0, actualHeight - (IMAGE_SIZE + 4 * PADDING + 2 * META_PADDING) - candidateFieldHeight);
        if (textKbd) drawMetaKeys(g);
    }

    void drawCandidateBar(Graphics g) {
        g.setClip(0, 0, kbWidth - 2 * PADDING, candidateFieldHeight);
        g.translate(PADDING + 1, 2 * PADDING);
        vkl.paintCandidateBar(g, kbWidth - 3 * PADDING, candidateFieldHeight - 2 * PADDING);
        g.translate(-PADDING - 1, -PADDING);
        g.setClip(0, 0, kbWidth, kbHeight);
    }

    /**
     * draw keyboard keys
     * 
     * @param g The graphics context to paint to
     */
    void drawKeys(Graphics g) {
        int tmp;
        if (!textKbd) {
            currentKeyboard = 0;
        }
        for (int i = 0; i < maxRows; i++) {
            for (int j = 0; j < maxColumns; j++) {
                tmp = i * maxColumns + j;
                if (tmp >= keys[currentKeyboard].length) {
                    break;
                }
                if (currentChar == tmp && inMetaKeys == false) {
                    drawButton(g, j * (PADDING + buttonW) + PADDING, i * (PADDING + buttonH) + PADDING, buttonW, buttonH);
                } else {
                    drawBeveledButton(g, j * (PADDING + buttonW) + PADDING, i * (PADDING + buttonH) + PADDING, buttonW, buttonH);
                }
                g.setColor(TEXT_COLOR);
                g.drawString("" + keys[currentKeyboard][tmp], j * (PADDING + buttonW) + PADDING + fontWCenter, i * (PADDING + buttonH) + PADDING + fontHTop, g.HCENTER | g.TOP);
            }
        }
    }

    static final int METAKEY_COUNT = 10;

    int getMetakeyRowWidth() {
        return METAKEY_COUNT * (IMAGE_SIZE + (3 * META_PADDING)) + META_PADDING;
    }

    /**
     * draw keyboard meta keys
     * 
     * @param g The graphics context to paint to
     */
    void drawMetaKeys(Graphics g) {
        int mkWidth = getMetakeyRowWidth();
        int currX = (kbWidth - mkWidth) / 2 + 2 * META_PADDING;
        int currY = 0;
        if (inMetaKeys) {
            drawBorder(g, currX - 2 * META_PADDING, currY - 2 * META_PADDING, currX + mkWidth, currY + IMAGE_SIZE + 2 * META_PADDING);
        }
        for (int i = 0; i < metaKeys.length; i++) {
            if (currX + IMAGE_SIZE > kbWidth) {
                currX = PADDING;
                currY -= (IMAGE_SIZE + META_PADDING);
            }
            if (inMetaKeys && i == currentMeta) {
                drawButton(g, currX, currY, IMAGE_SIZE + 2 * META_PADDING, IMAGE_SIZE + 2 * META_PADDING);
            } else {
                drawBeveledButton(g, currX, currY, IMAGE_SIZE + 2 * META_PADDING, IMAGE_SIZE + 2 * META_PADDING);
            }
            g.drawImage(metaKeys[i], currX + META_PADDING, currY + META_PADDING, g.TOP | g.LEFT);
            currX += (IMAGE_SIZE + 2 * META_PADDING + 2);
            if (currX > kbWidth) {
                currX = META_PADDING;
                currY -= (IMAGE_SIZE + META_PADDING);
            }
        }
    }

    /**
     * draw a border
     * 
     * @param g The graphics context to paint to
     * @param x1 x-coordinate of the button's location
     * @param y1 y-coordinate of the button's location
     * @param x2 the x-coordinate at the width of the border
     * @param y2 the y-coordinate at the height of the border
     */
    private void drawBorder(Graphics g, int x1, int y1, int x2, int y2) {
        g.setColor(GRAY);
        g.drawLine(x1 + 2, y1 + 2, x1 + 2, y2 - 3);
        g.drawLine(x1 + 2, y1 + 2, x2 - 2, y1 + 2);
        g.drawLine(x1 + 2, y2 - 1, x2 - 1, y2 - 1);
        g.drawLine(x2 - 1, y1 + 2, x2 - 1, y2 - 1);
        g.setColor(WHITE);
        g.drawRect(x1 + 1, y1 + 1, x2 - x1 - 3, y2 - y1 - 3);
    }

    /**
     * draw a sunken border
     * 
     * @param g The graphics context to paint to
     * @param x1 x-coordinate of the button's location
     * @param y1 y-coordinate of the button's location
     * @param x2 the x-coordinate at the width of the border
     * @param y2 the y-coordinate at the height of the border
     */
    private void drawSunkedBorder(Graphics g, int x1, int y1, int x2, int y2) {
        g.setColor(WHITE);
        g.fillRect(x1 + 2, y1 + 2, x2 - x1 - 2, y2 - y1 - 2);
        g.setColor(GRAY);
        g.drawLine(x1 + 2, y1 + 2, x1 + 2, y2 - 2);
        g.drawLine(x1 + 2, y1 + 2, x2 - 2, y1 + 2);
        g.setColor(DARK_GRAY);
        g.drawLine(x1 + 3, y1 + 3, x1 + 3, y2 - 3);
        g.drawLine(x1 + 3, y1 + 3, x2 - 3, y1 + 3);
        g.setColor(LIGHT_GRAY);
        g.drawLine(x1 + 3, y2 - 2, x2 - 2, y2 - 2);
        g.drawLine(x2 - 2, y1 + 3, x2 - 2, y2 - 2);
    }

    /**
     * draw a button
     * 
     * @param g The graphics context to paint to
     * @param x x-coordinate of the button's location
     * @param y y-coordinate of the button's location
     * @param w the width of the button
     * @param h the height of the button
     */
    private void drawButton(Graphics g, int x, int y, int w, int h) {
        g.setColor(GRAY);
        g.drawLine(x + 1, y + h - 1, x + w, y + h - 1);
        g.drawLine(x + w - 1, y + 1, x + w - 1, y + h);
        g.setColor(DARK_GRAY);
        g.drawLine(x, y + h, x + w, y + h);
        g.drawLine(x + w, y, x + w, y + h);
        g.setColor(WHITE);
        g.drawLine(x, y, x + w - 1, y);
        g.drawLine(x, y, x, y + h - 1);
    }

    /**
     * draw a beveled button
     * 
     * @param g The graphics context to paint to
     * @param x x-coordinate of the button's location
     * @param y y-coordinate of the button's location
     * @param w the width of the button
     * @param h the height of the button
     */
    private void drawBeveledButton(Graphics g, int x, int y, int w, int h) {
        g.setColor(GRAY);
        g.drawLine(x + 1, y + h - 1, x + w, y + h - 1);
        g.drawLine(x + w - 1, y + 1, x + w - 1, y + h);
        g.setColor(WHITE);
        g.drawLine(x, y + h, x + w, y + h);
        g.drawLine(x + w, y, x + w, y + h);
        g.setColor(GRAY);
        g.drawLine(x, y, x + w - 1, y);
        g.drawLine(x, y, x, y + h - 1);
        g.setColor(WHITE);
        g.drawLine(x + 1, y + 1, x + w - 2, y + 1);
        g.drawLine(x + 1, y + 1, x + 1, y + h - 2);
    }

    /**
     * Helper function to determine the itemIndex at the x,y position
     *
     * @param x,y   pointer coordinates in menuLayer's space (0,0 means left-top
     *      corner) both value can be negative as menuLayer handles the pointer
     *      event outside its bounds
     * @return menuItem's index since 0, or PRESS_OUT_OF_BOUNDS, PRESS_ON_TITLE
     *
     */
    private boolean isKeyAtPointerPosition(int x, int y) {
        int ret = -1;
        int tmpX, tmpY, tmp;
        for (int i = 0; i < maxRows; i++) {
            for (int j = 0; j < maxColumns; j++) {
                tmp = i * maxColumns + j;
                if (tmp >= keys[currentKeyboard].length) {
                    break;
                }
                tmpX = x - (j * (PADDING + buttonW) + PADDING);
                tmpY = y - (i * (PADDING + buttonH) + PADDING);
                if ((tmpX >= 0) && (tmpY >= 0) && (tmpX < buttonW) && (tmpY < buttonH)) {
                    currentChar = tmp;
                    inMetaKeys = false;
                    return true;
                }
            }
        }
        if (metaKeys == null) {
            return false;
        }
        int mkWidth = metaKeys.length * (IMAGE_SIZE + 3 * META_PADDING) + META_PADDING;
        int currX = (kbWidth - mkWidth) / 2 + 2 * META_PADDING;
        int currY = kbHeight - (IMAGE_SIZE + 6 * META_PADDING);
        for (int i = 0; i < metaKeys.length; i++) {
            if (currX + IMAGE_SIZE > kbWidth) {
                currX = PADDING;
                currY -= (IMAGE_SIZE + META_PADDING);
            }
            tmpX = x - currX;
            tmpY = y - currY;
            if ((tmpX >= 0) && (tmpY >= 0) && (tmpX < (IMAGE_SIZE + 2 * META_PADDING)) && (tmpY < (IMAGE_SIZE + 2 * META_PADDING))) {
                currentMeta = i;
                inMetaKeys = true;
                return true;
            }
            currX += (IMAGE_SIZE + 2 * META_PADDING + 2);
            if (currX > kbWidth) {
                currX = META_PADDING;
                currY -= (IMAGE_SIZE + META_PADDING);
            }
        }
        return false;
    }

    /**
     * Handle input from a pen tap. Parameters describe
     * the type of pen event and the x,y location in the
     * layer at which the event occurred. Important : the
     * x,y location of the pen tap will already be translated
     * into the coordinate space of the layer.
     *
     * @param type the type of pen event
     * @param x the x coordinate of the event
     * @param y the y coordinate of the event
     */
    public boolean pointerInput(int type, int x, int y) {
        switch(type) {
            case EventConstants.PRESSED:
                if (isKeyAtPointerPosition(x, y)) {
                    traverse(type, Constants.KEYCODE_SELECT);
                    vkl.repaintVK();
                }
                break;
            case EventConstants.RELEASED:
                if (isKeyAtPointerPosition(x, y)) {
                    traverse(type, Constants.KEYCODE_SELECT);
                    vkl.repaintVK();
                }
                break;
        }
        return true;
    }

    private static final int WHITE = 0xffffff;

    private static final int DARK_GRAY = 0x666699;

    private static final int GRAY = 0x3366cc;

    private static final int LIGHT_GRAY = 0x6699ff;

    private static final int TEXT_COLOR = 0xCCFFFF;

    /** padding between rows of buttons */
    private int PADDING;

    /** padding used by the meta keys */
    private static final int META_PADDING = 2;

    /** size of meta icons */
    private static final int IMAGE_SIZE = 13;

    static final int OK_META_KEY = 0;

    static final int CANCEL_META_KEY = 1;

    static final int MODE_META_KEY = 2;

    static final int BACKSPACE_META_KEY = 3;

    static final int SHIFT_META_KEY = 4;

    static final int CAPS_META_KEY = 5;

    static final int CNINPUT_META_KEY = 6;

    static final int CPY_META_KEY = 7;

    static final int PST_META_KEY = 8;

    static final int SEL_META_KEY = 9;

    static final int CURSOR_UP_META_KEY = 10;

    static final int CURSOR_DOWN_META_KEY = 11;

    static final int CURSOR_LEFT_META_KEY = 12;

    static final int CURSOR_RIGHT_META_KEY = 13;

    static final int CNINPUT_SELECT_META_KEY = 14;

    static final int IM_CHANGED_KEY = 99;
}
