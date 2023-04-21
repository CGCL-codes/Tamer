package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keys {

    int buffer[];

    int buffer_size;

    int max_buffer_size;

    Game game;

    public Keys(Game g, int size) {
        game = g;
        buffer_size = 0;
        max_buffer_size = size;
        buffer = new int[size];
        for (int i = 0; i < size; i++) {
            buffer[i] = -1;
        }
        System.out.println("Keyboard initialized");
    }

    public void ProcessKey(KeyEvent e, boolean pressed) {
        int key = e.getKeyCode();
        if (pressed) AddKey(key); else RemoveKey(key);
    }

    public void AddKey(int key) {
        if (buffer_size >= max_buffer_size) return;
        for (int i = 0; i < buffer_size; i++) {
            if (buffer[i] == key) return;
        }
        buffer[buffer_size] = key;
        buffer_size++;
    }

    public void RemoveKey(int key) {
        if (buffer_size <= 0) return;
        boolean key_found = false;
        int i = 0;
        for (; i < buffer_size; i++) {
            if (buffer[i] == key) {
                key_found = true;
                break;
            }
        }
        if (!key_found) return;
        for (int j = i; j < (buffer_size - 1); j++) {
            buffer[j] = buffer[j + 1];
        }
        buffer_size--;
    }

    public boolean IsPressed(int key) {
        for (int i = 0; i < buffer_size; i++) {
            if (buffer[i] == key) return true;
        }
        return false;
    }
}
