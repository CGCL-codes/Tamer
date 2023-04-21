package de.humanfork.treemerge.flatdiff.script;

import de.humanfork.treemerge.exception.ArgumentNullException;

/**
 * Skipp the count of items to make the Source look like the Destination.
 * Because this items are the same.
 * @author Ralph
 */
public class SkipCommand implements DiffCommand {

    /**
     * Count of items to make the Source look like the Destination.
     */
    private int count;

    /**
     * The Constructor.
     * @param count Count of items to make the Source look like the Destination.
     */
    public SkipCommand(final int count) {
        this.count = count;
    }

    /**
     * @return Count of items to make the Source look like the Destination.
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Call the Skipp method at visitor.
     * @param visitor the visitor
     */
    public void visit(final DiffCommandVisitor visitor) {
        if (visitor == null) throw new ArgumentNullException("visitor");
        visitor.skip(this);
    }

    /**
     * Descripe the Command.
     * @return description
     */
    @Override
    public String toString() {
        return "skip " + this.count;
    }
}
