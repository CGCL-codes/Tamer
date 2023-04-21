package com.open_squad.openplan.commands;

import java.util.List;
import java.util.Set;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import com.open_squad.openplan.model.Lot;
import com.open_squad.openplan.model.Machine;

public class OrphanChildCommand extends Command {

    private Machine machine;

    private Lot child;

    private int index;

    public OrphanChildCommand() {
        super("Orphaned children");
    }

    public void execute() {
        machine.removeLot(child);
    }

    public void redo() {
        machine.removeLot(child);
    }

    public void setChild(Lot child) {
        this.child = child;
    }

    public void setParent(Machine parent) {
        machine = parent;
    }

    public void undo() {
        child.setMachine(machine);
        machine.addLot(child);
    }
}
