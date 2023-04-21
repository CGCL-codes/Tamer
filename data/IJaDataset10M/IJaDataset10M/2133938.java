package org.argouml.model;

/**
 * Dummy mementos are created where we haven't yet designed the correct
 * memento for undoing an operation. It acts as a marker of work to be done
 * to complete the Undo implementation.
 * It also allows the save action to enable at the correct time. If there
 * is no memento generated and fired back to the main application then
 * the save action will not enable.
 * @author Bob Tarling
 */
public class DummyModelMemento extends ModelMemento {

    @Override
    public void undo() {
    }

    @Override
    public void redo() {
    }
}
