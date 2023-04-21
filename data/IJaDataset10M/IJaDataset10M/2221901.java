package com.customwars.client.ui.state;

import com.customwars.client.ui.slick.InputField;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.command.BasicCommand;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.StateBasedGame;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Show current Command binds and allow to remap them
 * Right click takes you back to the menu
 */
public class ControlBindingState extends CWState {

    private static final boolean DEBUG = false;

    private static final int LBL_FIELD_WIDTH = 130;

    private static final int INPUT_FIELD_WIDTH = 170;

    private static final int FIELD_HEIGHT = 20;

    private static final int COLUMN_MARGIN = 20;

    private static final int LEFT_MARGIN = 10;

    private static final int BIND_LIMIT = LEFT_MARGIN;

    private static final Point leftTop = new Point(LEFT_MARGIN, 50);

    private static final List<TextField> fields = new ArrayList<TextField>();

    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        int rowCount = 0;
        List commands = cwInput.getUniqueCommands();
        for (int i = 0; i < commands.size(); i++) {
            boolean hasNextCommand = i + 1 < commands.size();
            BasicCommand commandLeft = (BasicCommand) commands.get(i);
            BasicCommand commandRight = hasNextCommand ? (BasicCommand) commands.get(++i) : null;
            int px = leftTop.x;
            int py = leftTop.y + (rowCount * FIELD_HEIGHT);
            addLblAndField(container, commandLeft, px, py);
            if (hasNextCommand) {
                addLblAndField(container, commandRight, px + LBL_FIELD_WIDTH + INPUT_FIELD_WIDTH + COLUMN_MARGIN, py);
            }
            rowCount++;
        }
    }

    private void addLblAndField(GameContainer container, BasicCommand command, int x, int y) {
        TextField label = createLabel(container, x, y, command);
        fields.add(label);
        TextField inputField = createInputField(container, x + LBL_FIELD_WIDTH, y, command);
        fields.add(inputField);
    }

    private TextField createLabel(GameContainer container, int x, int y, BasicCommand command) {
        TextField label = new TextField(container, container.getDefaultFont(), x, y, LBL_FIELD_WIDTH, FIELD_HEIGHT, null);
        label.setText(command.getName());
        label.setAcceptingInput(false);
        label.setBorderColor(null);
        label.setCursorVisible(false);
        return label;
    }

    private TextField createInputField(GameContainer container, int x, int y, BasicCommand command) {
        InputField inputField = new InputField(container, x, y, INPUT_FIELD_WIDTH, FIELD_HEIGHT, command, cwInput);
        inputField.setBindingLimit(BIND_LIMIT);
        inputField.initDisplayText();
        inputField.setAcceptingInput(false);
        return inputField;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        cwInput.setActive(false);
        for (TextField field : fields) {
            field.setAcceptingInput(true);
        }
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        super.leave(container, game);
        for (TextField field : fields) {
            field.setAcceptingInput(false);
            field.setFocus(false);
        }
        cwInput.setActive(true);
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        g.drawString("Click on a key to select, Press any key to change, backspace clears the selected mappings", LEFT_MARGIN, 15);
        g.drawString("Limit = " + BIND_LIMIT + " right click to return to the previous menu.", LEFT_MARGIN, 30);
        for (TextField field : fields) {
            if (DEBUG) {
                if (field.hasFocus()) {
                    g.drawString(fields.indexOf(field) + " has focus", 150, 350);
                }
            }
            field.render(container, g);
        }
    }

    public void update(GameContainer container, int delta) throws SlickException {
        for (TextField field : fields) {
            if (field.hasFocus()) {
                if (fields.indexOf(field) % 2 != 0) {
                    field.setBorderColor(Color.blue);
                } else {
                    fields.get(fields.indexOf(field) + 1).setFocus(true);
                    fields.get(fields.indexOf(field) + 1).setBorderColor(Color.blue);
                }
            } else {
                if (DEBUG) {
                    field.setBorderColor(Color.white);
                } else {
                    field.setBorderColor(null);
                }
            }
        }
    }

    public int getID() {
        return 31;
    }
}
