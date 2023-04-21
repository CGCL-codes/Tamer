package com.lowagie.rups.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

/**
 * Action that tells an observable to close a file and/or releases
 * resources relevant for the file that is being closed.
 */
public class FileCloseAction implements ActionListener {

    /** An object that allows you to close a file. */
    protected Observable observable;

    /**
	 * Creates the close action.
	 * @param	observable	the object that expects you to close a file.
	 */
    public FileCloseAction(Observable observable) {
        this.observable = observable;
    }

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent evt) {
        observable.notifyObservers(this);
    }
}
