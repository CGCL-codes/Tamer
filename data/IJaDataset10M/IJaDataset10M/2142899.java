package org.iisc.mile.indickeyboards.windows;

import java.io.FileNotFoundException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.iisc.mile.indickeyboards.IndicKeyboards;
import org.iisc.mile.indickeyboards.ParseXML;
import org.iisc.mile.indickeyboards.PhoneticParseXML;
import org.iisc.mile.indickeyboards.UI;

public class InitWin implements KeyboardEventListener {

    public static final int ENTERKEY = 13;

    public static final int RPARANKEY = 40;

    public static final int NUM_9 = 57;

    public static final int ASTERISKKEY = 42;

    public static final int NUM_8 = 56;

    public static final int AMPKEY = 38;

    public static final int NUM_7 = 55;

    public static final int CAPKEY = 94;

    public static final int NUM_6 = 54;

    public static final int PERCENTAGEKEY = 37;

    public static final int NUM_5 = 53;

    public static final int DOLLARKEY = 36;

    public static final int NUM_4 = 52;

    public static final int HASHKEY = 35;

    public static final int NUM_3 = 51;

    public static final int ATKEY = 64;

    public static final int NUM_2 = 50;

    public static final int EXCLAIMKEY = 33;

    public static final int NUM_1 = 49;

    public static final int LPARANKEY = 41;

    public static final int NUM_0 = 48;

    public static final int PIPEKEY = 124;

    public static final int BKSLASHKEY = 220;

    public static final int PLUSKEY = 43;

    public static final int EQUALSKEY = 187;

    public static final int UNDERSCOREKEY = 95;

    public static final int MINUSKEY = 189;

    public static final int LTBRACKETKEY = 219;

    public static final int RTBRACEKEY = 125;

    public static final int RTBRACKETKEY = 221;

    public static final int DOUBLEQUOTEKEY = 34;

    public static final int SINGLEQUOTEKEY = 222;

    public static final int TILDEKEY = 126;

    public static final int BACKTICKKEY = 192;

    public static final int QUESTIONKEY = 63;

    public static final int FWSLASH = 191;

    public static final int GTKEY = 62;

    public static final int DOTKEY = 190;

    public static final int LTKEY = 60;

    public static final int COMAKEY = 188;

    public static final int COLONKEY = 58;

    public static final int SEMICOLONKEY = 186;

    public static final int SHIFTKEY = 16;

    public static final int F12KEY = 123;

    public static final int ALTKEY = 18;

    public static final int CTRLKEY = 17;

    private Boolean shiftPressed = false;

    private long withShiftPressed = NUM_0, throwAwayKey = ALTKEY;

    public static Boolean enable = false, altPressed = false;

    private Boolean ctrlPressed = false;

    private long withCtrlPressed = NUM_0;

    public void InitWinStart() throws FileNotFoundException {
        KeyboardHook kh = new KeyboardHook();
        kh.addEventListener(new InitWin());
    }

    public void GlobalKeyPressed(KeyboardEvent event) {
        String inputChar;
        if ((event.getVirtualKeyCode() == ALTKEY) && (event.getTransitionState())) {
            altPressed = true;
        }
        if (altPressed) {
            throwAwayKey = event.getVirtualKeyCode();
            System.out.println("ThrowAwayKEy = " + throwAwayKey);
            if (event.getVirtualKeyCode() == F12KEY) {
                throwAwayKey = F12KEY;
                enable = !enable;
                altPressed = !altPressed;
                System.out.println("keycode" + throwAwayKey);
                if (enable) {
                    Display.getDefault().syncExec(new Runnable() {

                        public void run() {
                            org.iisc.mile.indickeyboards.UI.item.setImage(org.iisc.mile.indickeyboards.UI.previousKeyboardIcon);
                            UI.tip.setMessage("Enabled");
                            UI.tip.setVisible(true);
                            UI.enableDisable.setText("Disable [Alt+F12]");
                            System.gc();
                        }
                    });
                    System.out.println("Software enabled");
                    ParseXML.previousConsonantFlag = 0;
                } else if (!enable) {
                    Display.getDefault().syncExec(new Runnable() {

                        public void run() {
                            org.iisc.mile.indickeyboards.UI.previousKeyboardIcon = org.iisc.mile.indickeyboards.UI.item.getImage();
                            Image image1 = new Image(Display.getCurrent(), IndicKeyboards.workingDirectory + "/resources/trayicon_disabled.ico");
                            org.iisc.mile.indickeyboards.UI.item.setImage(image1);
                            UI.tip.setMessage("Disabled");
                            UI.tip.setVisible(true);
                            UI.enableDisable.setText("Enable [Alt+F12]");
                            System.gc();
                        }
                    });
                    System.out.println("Software disabled... Press Alt + F12 to re-enable");
                }
            }
        }
        if (enable) {
            if ((event.getVirtualKeyCode() == CTRLKEY) && event.getTransitionState()) {
                ctrlPressed = true;
            }
            if (ctrlPressed) {
                withCtrlPressed = event.getVirtualKeyCode();
                if (withCtrlPressed == CTRLKEY) {
                } else {
                }
                return;
            }
            if ((event.getVirtualKeyCode() == SHIFTKEY) && (event.getTransitionState())) {
                shiftPressed = true;
            }
            if (shiftPressed) {
                withShiftPressed = event.getVirtualKeyCode();
                if ((event.getVirtualKeyCode() >= 65) && (event.getVirtualKeyCode() <= 90)) withShiftPressed = event.getVirtualKeyCode() + 32; else {
                    switch(event.getVirtualKeyCode()) {
                        case SEMICOLONKEY:
                            withShiftPressed = COLONKEY;
                            break;
                        case COMAKEY:
                            withShiftPressed = LTKEY;
                            break;
                        case DOTKEY:
                            withShiftPressed = GTKEY;
                            break;
                        case FWSLASH:
                            withShiftPressed = QUESTIONKEY;
                            break;
                        case BACKTICKKEY:
                            withShiftPressed = TILDEKEY;
                            break;
                        case SINGLEQUOTEKEY:
                            withShiftPressed = DOUBLEQUOTEKEY;
                            break;
                        case RTBRACKETKEY:
                            withShiftPressed = RTBRACEKEY;
                            break;
                        case LTBRACKETKEY:
                            withShiftPressed = 123;
                            break;
                        case MINUSKEY:
                            withShiftPressed = UNDERSCOREKEY;
                            break;
                        case EQUALSKEY:
                            withShiftPressed = PLUSKEY;
                            break;
                        case BKSLASHKEY:
                            withShiftPressed = PIPEKEY;
                            break;
                        case NUM_0:
                            withShiftPressed = LPARANKEY;
                            break;
                        case NUM_1:
                            withShiftPressed = EXCLAIMKEY;
                            break;
                        case NUM_2:
                            withShiftPressed = ATKEY;
                            break;
                        case NUM_3:
                            withShiftPressed = HASHKEY;
                            break;
                        case NUM_4:
                            withShiftPressed = DOLLARKEY;
                            break;
                        case NUM_5:
                            withShiftPressed = PERCENTAGEKEY;
                            break;
                        case NUM_6:
                            withShiftPressed = CAPKEY;
                            break;
                        case NUM_7:
                            withShiftPressed = AMPKEY;
                            break;
                        case NUM_8:
                            withShiftPressed = ASTERISKKEY;
                            break;
                        case NUM_9:
                            withShiftPressed = RPARANKEY;
                            break;
                    }
                }
                if (withShiftPressed == SHIFTKEY) {
                } else {
                    if (PhoneticParseXML.PhoneticFlag == 0) {
                        ParseXML test = new ParseXML();
                        if (withShiftPressed >= 97 && withShiftPressed <= 122) {
                            inputChar = new Character((char) withShiftPressed).toString().toUpperCase();
                        } else {
                            inputChar = new Character((char) withShiftPressed).toString();
                        }
                        test.getPattern(inputChar);
                        System.out.println("Key Pressed with shift: " + withShiftPressed);
                    } else {
                        PhoneticParseXML test1 = new PhoneticParseXML();
                        if (withShiftPressed >= 97 && withShiftPressed <= 122) {
                            inputChar = new Character((char) withShiftPressed).toString().toUpperCase();
                        } else {
                            inputChar = new Character((char) withShiftPressed).toString();
                        }
                        test1.getPhoneticPattern(inputChar);
                        System.out.println("Key Pressed with shift: " + withShiftPressed);
                    }
                }
            } else {
                if (event.getVirtualKeyCode() == ENTERKEY) {
                    PhoneticParseXML.previousConsonantFlag = 0;
                    ParseXML.previousConsonantFlag = 0;
                    ParseXML.tamil99count = 0;
                }
                int tempKeyCode = 0;
                if (PhoneticParseXML.PhoneticFlag == 0) {
                    ParseXML test = new ParseXML();
                    if (event.getVirtualKeyCode() <= 185) {
                        inputChar = new Character((char) event.getVirtualKeyCode()).toString().toLowerCase();
                    } else {
                        switch(event.getVirtualKeyCode()) {
                            case 186:
                                tempKeyCode = 59;
                                break;
                            case 222:
                                tempKeyCode = 39;
                                break;
                            case 188:
                                tempKeyCode = 44;
                                break;
                            case 190:
                                tempKeyCode = 46;
                                break;
                            case 191:
                                tempKeyCode = 47;
                                break;
                            case 219:
                                tempKeyCode = 91;
                                break;
                            case 221:
                                tempKeyCode = 93;
                                break;
                            case 192:
                                tempKeyCode = 96;
                                break;
                            case 189:
                                tempKeyCode = 45;
                                break;
                            case 187:
                                tempKeyCode = 61;
                                break;
                            case 220:
                                tempKeyCode = 92;
                                break;
                        }
                        inputChar = new Character((char) tempKeyCode).toString();
                    }
                    test.getPattern(inputChar);
                    System.out.println("Key Pressed: " + event.getVirtualKeyCode());
                } else {
                    PhoneticParseXML test1 = new PhoneticParseXML();
                    inputChar = new Character((char) event.getVirtualKeyCode()).toString().toLowerCase();
                    test1.getPhoneticPattern(inputChar);
                    System.out.println("Key Pressed: " + event.getVirtualKeyCode());
                }
            }
        }
    }

    public void GlobalKeyReleased(KeyboardEvent event) {
        if (enable) {
            if ((event.getVirtualKeyCode() == CTRLKEY) && (!event.getTransitionState())) {
                ctrlPressed = false;
                if (withCtrlPressed == NUM_0) {
                } else {
                }
            }
            if ((event.getVirtualKeyCode() == SHIFTKEY) && (!event.getTransitionState())) {
                shiftPressed = false;
                if (withShiftPressed == NUM_0) {
                } else {
                }
            }
        }
    }
}
