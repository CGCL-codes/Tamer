package org.dengues.designer.ui.process.commands;

import org.dengues.designer.ui.editors.AbstractGEFCommand;
import org.dengues.designer.ui.process.models.CompBlockExit;
import org.dengues.designer.ui.process.models.CompProcess;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf Qiang.Zhang.Adolf@gmail.com 2008-7-17 qiang.zhang $
 * 
 */
public class CompBlockExitCreateCommand extends AbstractGEFCommand {

    private final CompProcess process;

    private final CompBlockExit newNote;

    private final Rectangle constraint;

    private CompBlockExit oldNote;

    /**
     * Qiang.Zhang.Adolf@gmail.com CompBlockExitCreateCommand constructor comment.
     * 
     * @param process
     * @param block
     * @param constraint
     */
    public CompBlockExitCreateCommand(CompProcess process, CompBlockExit block, Rectangle constraint) {
        setLabel("Create Block Exit");
        this.constraint = constraint;
        this.newNote = block;
        this.process = process;
        this.element = block;
    }

    @Override
    public void execute() {
        if (newNote.equals(oldNote)) {
            return;
        }
        newNote.setLocation(constraint.getLocation());
        process.addCompBlockExit(newNote);
    }

    @Override
    public void undo() {
        process.removeCompBlockExit(newNote);
    }
}
