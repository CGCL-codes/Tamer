package org.jfonia.commands;

/**
 * Use of abstract class instead of interface to be able to hide execute() and
 * undo() methods. CommandStack handles these methods.
 *
 * @author Rik Bauwens
 */
public abstract class Command {

    protected abstract void execute();

    protected abstract void undo();
}
