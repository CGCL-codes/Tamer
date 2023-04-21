package org.openscience.jmol.io;

import org.openscience.jmol.DisplayControl;
import org.openscience.jmol.ChemFile;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.Reader;

abstract class DefaultChemFileReader implements ChemFileReader {

    /**
   * Create an ChemFile reader.
   *
   * @param input source of ChemFile data
   */
    protected DefaultChemFileReader(DisplayControl control, Reader input) {
        this.control = control;
        this.input = new BufferedReader(input);
    }

    /**
   * Adds a reader listener.
   *
   * @param l the reader listener to add.
   */
    public void addReaderListener(ReaderListener l) {
        listenerList.addElement(l);
    }

    /**
   * Removes a reader listener.
   *
   * @param l the reader listener to remove.
   */
    public void removeReaderListener(ReaderListener l) {
        listenerList.removeElement(l);
    }

    /**
   * Sends a frame read event to the reader listeners.
   */
    protected void fireFrameRead() {
        for (int i = 0; i < listenerList.size(); ++i) {
            ReaderListener listener = (ReaderListener) listenerList.elementAt(i);
            if (readerEvent == null) {
                readerEvent = new ReaderEvent(this);
            }
            listener.frameRead(readerEvent);
        }
    }

    /**
   * Sets whether bonds are enabled in the files and frames which are read.
   *
   * @param bondsEnabled if true, enables bonds.
   */
    public void setBondsEnabled(boolean bondsEnabled) {
        this.bondsEnabled = bondsEnabled;
    }

    /**
   * An event to be sent to listeners. Lazily initialized.
   */
    private ReaderEvent readerEvent = null;

    /**
   * Holder of reader event listeners.
   */
    private Vector listenerList = new Vector();

    /**
   * Whether bonds are enabled in the files and frames read.
   */
    protected boolean bondsEnabled = true;

    /**
   * The source for data.
   */
    protected BufferedReader input;

    DisplayControl control;
}
