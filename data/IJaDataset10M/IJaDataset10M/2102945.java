package cards;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import table.TableModel;

public class BacklogCreatedCommand extends Command {

    private BacklogModel newCard;

    private TableModel parent;

    private Rectangle bounds;

    public BacklogCreatedCommand(BacklogModel newCard, TableModel parent, Rectangle bounds) {
        this.newCard = newCard;
        this.parent = parent;
        this.bounds = bounds;
        setLabel("IndexCard creation");
    }

    public boolean canExecute() {
        return newCard != null & parent != null && bounds != null;
    }

    public void execute() {
        newCard.setLocation(bounds.getLocation());
        Dimension size = bounds.getSize();
        if (size.width > 0 && size.height > 0) newCard.setSize(size);
        redo();
    }

    public void redo() {
        parent.addChild(newCard);
    }

    public void undo() {
        parent.removeChild(newCard);
    }
}
