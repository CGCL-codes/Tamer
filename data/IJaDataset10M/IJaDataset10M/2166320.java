package it.diamonds.engine.input;

import java.util.HashMap;
import static org.lwjgl.input.Keyboard.KEY_SLASH;
import static org.lwjgl.input.Keyboard.KEY_DOWN;
import static org.lwjgl.input.Keyboard.KEY_ESCAPE;
import static org.lwjgl.input.Keyboard.KEY_LEFT;
import static org.lwjgl.input.Keyboard.KEY_S;
import static org.lwjgl.input.Keyboard.KEY_A;
import static org.lwjgl.input.Keyboard.KEY_Y;
import static org.lwjgl.input.Keyboard.KEY_D;
import static org.lwjgl.input.Keyboard.KEY_R;
import static org.lwjgl.input.Keyboard.KEY_W;
import static org.lwjgl.input.Keyboard.KEY_T;
import static org.lwjgl.input.Keyboard.KEY_RIGHT;
import static org.lwjgl.input.Keyboard.KEY_UP;
import static org.lwjgl.input.Keyboard.KEY_PERIOD;
import static org.lwjgl.input.Keyboard.KEY_COMMA;

public class KeyMappings {

    private HashMap<Integer, Integer> keyMappings = new HashMap<Integer, Integer>();

    public static KeyMappings createForPlayerOne() {
        KeyMappings mappings = new KeyMappings();
        mappings.addMapping(KEY_ESCAPE, KeyEvent.ESCAPE);
        mappings.addMapping(KEY_W, KeyEvent.UP);
        mappings.addMapping(KEY_S, KeyEvent.DOWN);
        mappings.addMapping(KEY_A, KeyEvent.LEFT);
        mappings.addMapping(KEY_D, KeyEvent.RIGHT);
        mappings.addMapping(KEY_R, KeyEvent.BUTTON1);
        mappings.addMapping(KEY_T, KeyEvent.BUTTON2);
        mappings.addMapping(KEY_Y, KeyEvent.BUTTON3);
        return mappings;
    }

    public static KeyMappings createForPlayerTwo() {
        KeyMappings mappings = new KeyMappings();
        mappings.addMapping(KEY_ESCAPE, KeyEvent.ESCAPE);
        mappings.addMapping(KEY_UP, KeyEvent.UP);
        mappings.addMapping(KEY_DOWN, KeyEvent.DOWN);
        mappings.addMapping(KEY_LEFT, KeyEvent.LEFT);
        mappings.addMapping(KEY_RIGHT, KeyEvent.RIGHT);
        mappings.addMapping(KEY_COMMA, KeyEvent.BUTTON1);
        mappings.addMapping(KEY_PERIOD, KeyEvent.BUTTON2);
        mappings.addMapping(KEY_SLASH, KeyEvent.BUTTON3);
        return mappings;
    }

    public static KeyMappings createForGameLoop() {
        KeyMappings mappings = new KeyMappings();
        return mappings;
    }

    public void addMapping(int keyOgl, int keyEvent) {
        keyMappings.put(keyOgl, keyEvent);
    }

    public int translateKey(int keyOgl) {
        if (!keyMappings.containsKey(keyOgl)) {
            return KeyEvent.UNKNOWN;
        }
        return keyMappings.get(keyOgl);
    }
}
