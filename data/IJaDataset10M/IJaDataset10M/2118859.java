package net.sourceforge.code2uml.unitdata;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import javax.swing.SwingWorker;

/**
 * Represents a background thread which gets qualified names of 
 * classes/interfaces/enums defined in given files. Fires propertyChangeEvent
 * with name "progress", old value null and new value ProgressData to inform
 * about progress of reading those files.
 * <br/><br/>
 * Note: SwingWorker is designed to be executed only once. This means that also
 * NamesWorker should be executed only once, further executions will not result in
 * invoking doInBackground().
 *
 * @author Mateusz Wenus
 */
public class NamesWorker extends SwingWorker<Collection<String>, Object> implements Observer {

    private UnitsRetriever retriever;

    private Collection<String> filePaths;

    /** 
     * Creates a new instance of NamesWorker which will read files <code>
     * filePaths</code>. 
     *
     * @param filePaths paths to files to read qualified names from
     */
    public NamesWorker(Collection<String> filePaths) {
        this.filePaths = filePaths;
        retriever = new UnitsRetrieverImpl();
        retriever.addObserver(this);
    }

    /**
     * Returns qualified names of classes/interfaces/enums defined in files
     * given at construction time. Fires PropertyChangeEvents with name 
     * "progress", old value null and new value - an instance of ProgressData
     * to notify about progress of processing files.
     *
     * @return qualified names of classes/interfaces/enums defined in files
     *         given at construction time
     */
    protected Collection<String> doInBackground() {
        return retriever.retrieveNames(filePaths);
    }

    /**
     * Called when underlying UnitsRetriever notifies about progress of its job. 
     * Should not be called malually.
     *
     * @param o Observable which has changed
     * @param arg argument passed by Observable
     */
    public void update(Observable o, Object arg) {
        firePropertyChange("progress", null, arg);
    }
}
