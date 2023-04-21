package ru.amse.soultakov.ereditor.controller.undo.commands;

import ru.amse.soultakov.ereditor.controller.undo.ICommand;
import ru.amse.soultakov.ereditor.view.IViewable;

public class MoveCommand implements ICommand {

    private int x;

    private int y;

    private int oldX;

    private int oldY;

    private IViewable viewable;

    public MoveCommand(IViewable viewable, int x, int y) {
        this.x = x;
        this.y = y;
        this.oldX = viewable.getX();
        this.oldY = viewable.getY();
        this.viewable = viewable;
    }

    public void doIt() {
        viewable.setLocation(x, y);
    }

    public void undoIt() {
        viewable.setLocation(oldX, oldY);
    }
}
