package org.blue.shard.graphics.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.blue.shard.graphics.input.keybinding.DummyKeyBindingAction;
import org.blue.shard.graphics.input.keybinding.IKeyBindingAction;
import org.lwjgl.input.Keyboard;

public class KeyMapper {

    private HashMap<Integer, IKeyBindingAction> keyMap = new HashMap<Integer, IKeyBindingAction>();

    private static final String KEY_BINDINGS_FILE = "keys.xml";

    private static Logger log = Logger.getLogger(KeyMapper.class);

    public KeyMapper() {
        loadKeyBindings();
    }

    public void loadKeyBindings() {
        log.debug("Loading properties from file [" + KEY_BINDINGS_FILE + "].");
        URL pathToProperties = ClassLoader.getSystemClassLoader().getResource(KEY_BINDINGS_FILE);
        Properties props = new Properties();
        InputStream input;
        try {
            input = new FileInputStream(new File(pathToProperties.getPath()));
            props.loadFromXML(input);
            input.close();
        } catch (FileNotFoundException e) {
            log.error("Unable to find [" + KEY_BINDINGS_FILE + "] file with configuration information.", e);
        } catch (InvalidPropertiesFormatException e) {
            log.error("File [" + KEY_BINDINGS_FILE + "] has incorrect or corrupted format.", e);
        } catch (IOException e) {
            log.error("IOException occured while reading file [" + KEY_BINDINGS_FILE + "].", e);
        }
        Enumeration keys = props.keys();
        log.info(props.size() + " keybinding(s) found");
        DummyKeyBindingAction dummy = new DummyKeyBindingAction();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            String actionClassName = "Action is not defined";
            try {
                actionClassName = (props.getProperty(key.toString()));
                Class clazz = Class.forName(actionClassName);
                IKeyBindingAction action;
                if (clazz.getName().equalsIgnoreCase(dummy.getClass().getName())) {
                    action = dummy;
                } else {
                    action = (IKeyBindingAction) clazz.newInstance();
                }
                keyMap.put(Integer.parseInt(key.toString(), 16), action);
                log.debug("Loaded instance of [" + action.toString() + "] action class");
            } catch (ClassNotFoundException e) {
                log.error("ClassNotFoundException while binding key:[" + key.toString() + "] to action:[" + actionClassName + "]", e);
            } catch (InstantiationException e) {
                log.error("InstantiationException while binding key:[" + key.toString() + "] to action:[" + actionClassName + "]", e);
            } catch (IllegalAccessException e) {
                log.error("IllegalAccessException while binding key:[" + key.toString() + "] to action:[" + actionClassName + "]", e);
            }
        }
        log.info(keyMap.size() + " key binding(s) loaded");
    }

    public void doKeyboardCheck() {
        int keyCode = getKeyPressed();
        if (keyCode == -1) {
            return;
        }
        System.out.println("Something pressed:" + Keyboard.getKeyName(keyCode));
        keyMap.get(new Integer(keyCode)).execute();
    }

    public int getKeyPressed() {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            return Keyboard.KEY_ESCAPE;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
            return Keyboard.KEY_1;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
            return Keyboard.KEY_2;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
            return Keyboard.KEY_3;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
            return Keyboard.KEY_4;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_5)) {
            return Keyboard.KEY_5;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_6)) {
            return Keyboard.KEY_6;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_7)) {
            return Keyboard.KEY_7;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_8)) {
            return Keyboard.KEY_8;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_9)) {
            return Keyboard.KEY_9;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_0)) {
            return Keyboard.KEY_0;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_MINUS)) {
            return Keyboard.KEY_MINUS;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_EQUALS)) {
            return Keyboard.KEY_EQUALS;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_BACK)) {
            return Keyboard.KEY_BACK;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
            return Keyboard.KEY_TAB;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            return Keyboard.KEY_Q;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            return Keyboard.KEY_W;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            return Keyboard.KEY_E;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
            return Keyboard.KEY_R;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_T)) {
            return Keyboard.KEY_T;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_Y)) {
            return Keyboard.KEY_Y;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_U)) {
            return Keyboard.KEY_U;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
            return Keyboard.KEY_I;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
            return Keyboard.KEY_O;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
            return Keyboard.KEY_P;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LBRACKET)) {
            return Keyboard.KEY_P;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_RBRACKET)) {
            return Keyboard.KEY_RBRACKET;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
            return Keyboard.KEY_RETURN;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            return Keyboard.KEY_LCONTROL;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            return Keyboard.KEY_A;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            return Keyboard.KEY_S;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            return Keyboard.KEY_D;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
            return Keyboard.KEY_F;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
            return Keyboard.KEY_G;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_H)) {
            return Keyboard.KEY_H;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
            return Keyboard.KEY_J;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
            return Keyboard.KEY_K;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
            return Keyboard.KEY_L;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_SEMICOLON)) {
            return Keyboard.KEY_SEMICOLON;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_APOSTROPHE)) {
            return Keyboard.KEY_APOSTROPHE;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_GRAVE)) {
            return Keyboard.KEY_GRAVE;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            return Keyboard.KEY_LSHIFT;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_BACKSLASH)) {
            return Keyboard.KEY_BACKSLASH;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
            return Keyboard.KEY_Z;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
            return Keyboard.KEY_X;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
            return Keyboard.KEY_C;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_V)) {
            return Keyboard.KEY_V;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
            return Keyboard.KEY_B;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_N)) {
            return Keyboard.KEY_N;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_M)) {
            return Keyboard.KEY_M;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_COMMA)) {
            return Keyboard.KEY_COMMA;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_PERIOD)) {
            return Keyboard.KEY_PERIOD;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_SLASH)) {
            return Keyboard.KEY_SLASH;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            return Keyboard.KEY_RSHIFT;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_MULTIPLY)) {
            return Keyboard.KEY_MULTIPLY;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
            return Keyboard.KEY_LMENU;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            return Keyboard.KEY_SPACE;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_CAPITAL)) {
            return Keyboard.KEY_CAPITAL;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_F1)) {
            return Keyboard.KEY_F1;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_F2)) {
            return Keyboard.KEY_F2;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_F3)) {
            return Keyboard.KEY_F3;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_F4)) {
            return Keyboard.KEY_F4;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_F5)) {
            return Keyboard.KEY_F5;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_F6)) {
            return Keyboard.KEY_F6;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_F7)) {
            return Keyboard.KEY_F7;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_F8)) {
            return Keyboard.KEY_F8;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_F9)) {
            return Keyboard.KEY_F9;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_F10)) {
            return Keyboard.KEY_F10;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_NUMLOCK)) {
            return Keyboard.KEY_NUMLOCK;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_SCROLL)) {
            return Keyboard.KEY_SCROLL;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD7)) {
            return Keyboard.KEY_NUMPAD7;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8)) {
            return Keyboard.KEY_NUMPAD8;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD9)) {
            return Keyboard.KEY_NUMPAD9;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)) {
            return Keyboard.KEY_SUBTRACT;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4)) {
            return Keyboard.KEY_NUMPAD4;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD5)) {
            return Keyboard.KEY_NUMPAD5;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6)) {
            return Keyboard.KEY_NUMPAD6;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_ADD)) {
            return Keyboard.KEY_ADD;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1)) {
            return Keyboard.KEY_NUMPAD1;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) {
            return Keyboard.KEY_NUMPAD2;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD3)) {
            return Keyboard.KEY_NUMPAD3;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD0)) {
            return Keyboard.KEY_NUMPAD0;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_DECIMAL)) {
            return Keyboard.KEY_DECIMAL;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_F12)) {
            return Keyboard.KEY_F12;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_F13)) {
            return Keyboard.KEY_F13;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_F14)) {
            return Keyboard.KEY_F14;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_F15)) {
            return Keyboard.KEY_F15;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_KANA)) {
            return Keyboard.KEY_KANA;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_CONVERT)) {
            return Keyboard.KEY_CONVERT;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_NOCONVERT)) {
            return Keyboard.KEY_NOCONVERT;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_YEN)) {
            return Keyboard.KEY_YEN;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPADEQUALS)) {
            return Keyboard.KEY_NUMPADEQUALS;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_CIRCUMFLEX)) {
            return Keyboard.KEY_CIRCUMFLEX;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_AT)) {
            return Keyboard.KEY_AT;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_COLON)) {
            return Keyboard.KEY_COLON;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_UNDERLINE)) {
            return Keyboard.KEY_UNDERLINE;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_KANJI)) {
            return Keyboard.KEY_KANJI;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_STOP)) {
            return Keyboard.KEY_STOP;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_AX)) {
            return Keyboard.KEY_AX;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_UNLABELED)) {
            return Keyboard.KEY_UNLABELED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPADENTER)) {
            return Keyboard.KEY_NUMPADENTER;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
            return Keyboard.KEY_RCONTROL;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPADCOMMA)) {
            return Keyboard.KEY_NUMPADCOMMA;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_DIVIDE)) {
            return Keyboard.KEY_DIVIDE;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_SYSRQ)) {
            return Keyboard.KEY_SYSRQ;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_RMENU)) {
            return Keyboard.KEY_RMENU;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_PAUSE)) {
            return Keyboard.KEY_PAUSE;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_HOME)) {
            return Keyboard.KEY_HOME;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            return Keyboard.KEY_UP;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_PRIOR)) {
            return Keyboard.KEY_PRIOR;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            return Keyboard.KEY_LEFT;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            return Keyboard.KEY_RIGHT;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_END)) {
            return Keyboard.KEY_END;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            return Keyboard.KEY_DOWN;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_NEXT)) {
            return Keyboard.KEY_NEXT;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_INSERT)) {
            return Keyboard.KEY_INSERT;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_DELETE)) {
            return Keyboard.KEY_DELETE;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LMETA)) {
            return Keyboard.KEY_LMETA;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_RMETA)) {
            return Keyboard.KEY_RMETA;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_APPS)) {
            return Keyboard.KEY_APPS;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_POWER)) {
            return Keyboard.KEY_POWER;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_SLEEP)) {
            return Keyboard.KEY_SLEEP;
        } else {
            return -1;
        }
    }
}
