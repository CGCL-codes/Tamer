package jmax.toolkit;

/**
 * The interface for the classes that can own a selection (ex,
 * editor's window */
public interface SelectionOwner {

    /**
   * The selection owned by this object has been disactivated */
    public abstract void selectionDisactivated();

    /**
   * The selection owned by this object has been activated */
    public abstract void selectionActivated();
}
