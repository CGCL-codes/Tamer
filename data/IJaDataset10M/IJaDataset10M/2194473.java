package net.sf.refactorit.refactorings.undo;

public class UndoException extends Exception {

    String desc;

    Throwable nested;

    public UndoException(Throwable t, String desc) {
        super(desc);
        nested = t;
        this.desc = desc;
    }
}
