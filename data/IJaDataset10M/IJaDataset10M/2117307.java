package net.sourceforge.plantuml.oregon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SmartKeyboard {

    private final Keyboard keyboard;

    private final List<String> history = new ArrayList<String>();

    public SmartKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public String input(Screen screen) throws NoInputException {
        final String s = keyboard.input();
        history.add(s);
        screen.print("<i>? " + s);
        return s;
    }

    public int inputInt(Screen screen) throws NoInputException {
        final String s = input(screen);
        if (s.matches("\\d+") == false) {
            screen.print("Please enter a valid number instead of " + s);
            throw new NoInputException();
        }
        return Integer.parseInt(s);
    }

    public boolean hasMore() {
        return keyboard.hasMore();
    }

    public List<String> getHistory() {
        return Collections.unmodifiableList(history);
    }
}
