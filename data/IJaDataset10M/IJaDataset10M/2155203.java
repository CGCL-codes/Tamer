package de.humanfork.treemerge.flatdiff.script;

import java.util.ArrayList;
import java.util.List;

/**
 * Visitor for diff commands who points out the differences descripted by an editscript.
 * @author Ralph
 *
 * @param <Type> type of diffed items.
 */
public class DifferentVisitor<Type extends Comparable<Type>> implements DiffCommandVisitor<Type> {

    /**
     * The edit script.
     */
    private EditScript editScript;

    /**
     * Found differences, denoted by the edit script.
     */
    private List<String> differences;

    /**
     * Counter in from list.
     */
    private int counterFrom = 0;

    /**
     * Counter in from list.
     */
    private int counterTo = 0;

    /**
     * c-tor.
     * @param editScript The script whose commands will be investigated.
     */
    public DifferentVisitor(final EditScript editScript) {
        super();
        this.editScript = editScript;
    }

    /**
     * Execute the script.
     * @return number of commands
     */
    public List<String> execute() {
        this.differences = new ArrayList<String>();
        for (DiffCommand<Type> command : this.editScript) {
            command.visit(this);
        }
        return differences;
    }

    /**
     * Visited by an delete command.
     * (visitor pattern)
     * @param deleteCommand the delete command.
     */
    public void delete(final DeleteCommand deleteCommand) {
        counterFrom += deleteCommand.getCount();
        differences.add("(Line: " + counterFrom + "/" + counterTo + ")" + deleteCommand.toString());
    }

    /**
     * Visited by an insert command.
     * (visitor pattern)
     * @param insertCommand the insert command.
     */
    public void insert(final InsertCommand insertCommand) {
        counterTo++;
        differences.add("(Line: " + counterFrom + "/" + counterTo + ")" + insertCommand.toString());
    }

    /**
     * Visited by an skip command.
     * (visitor pattern)
     * @param skipCommand the skip command.
     */
    public void skip(final SkipCommand skipCommand) {
        counterFrom += skipCommand.getCount();
        counterTo += skipCommand.getCount();
    }
}
