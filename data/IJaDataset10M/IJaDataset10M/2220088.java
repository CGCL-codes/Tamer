package org.schnelln.gui.modules;

/**
 * The Interface TextChangeListener.
 * 
 * @author Lukas Tischler [tischler.lukas_AT_gmail.com]
 */
public interface TextChangeListener {

    /**
	 * Text changed. Executed by an instance of {@link IStatusPanel}, if there
	 * was called {@link IStatusPanel#addText(int, String)} or
	 * {@link IStatusPanel#reset()}
	 */
    public void textChanged();
}
