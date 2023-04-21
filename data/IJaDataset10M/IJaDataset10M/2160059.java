package com.googlecode.gwahtzee.gui.mainmenu;

import org.newdawn.slick.Input;
import java.util.ArrayList;
import java.util.List;

public class Menu {

    private List<Button> buttons;

    public Menu() {
        this.buttons = new ArrayList<Button>();
    }

    public void update(Input input, int mouseX, int mouseY, int delta) {
        for (Button button : buttons) {
            button.update(mouseX, mouseY, delta);
        }
        for (Button button : buttons) {
            if (button.isMouseOver()) {
                if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                    button.click();
                }
            }
        }
    }

    public void render() {
        for (Button button : buttons) {
            button.render();
        }
    }

    public void addButton(Button button) {
        buttons.add(button);
    }
}
