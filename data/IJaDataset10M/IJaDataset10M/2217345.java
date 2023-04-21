package edu.ucdavis.genomics.metabolomics.util.thread;

import edu.ucdavis.genomics.metabolomics.util.Configable;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

/**
 * @author wohlgemuth
 *
 * Eine klasse die daf?r sorgt das thread abgearbeitet werden und selber als
 * thread laufen kann
 */
public class ThreadRunner implements Threadable, ThreadRunnable {

    List<Threadable> list = new Vector<Threadable>();

    List<Object> results = new Vector<Object>();

    /**
     * eine standard methode welche daf?r sorgt das keine nullpointer exceptio
     * ausgel?st wird
     *
     * @uml.property name="listener"
     * @uml.associationEnd multiplicity="(0 1)"
     */
    ThreadListener listener = new ThreadListener() {

        public void threadFinshed() {
        }

        public void startThread() {
        }

        public void setResult(Object o) {
        }
    };

    /**
     * definiert ob threading erlaubt werden soll oder nicht
     */
    boolean multithreading = true;

    int counter = 0;

    int currentCount = 0;

    /**
     * DOCUMENT ME!
     */
    private String name = getClass().getName();

    /**
     * DOCUMENT ME!
     */
    private Thread thread;

    /**
     * definiert die maximal anzahl an threads
     */
    private int maxThreads = 2;

    /**
     *
     */
    public ThreadRunner() {
        super();
        maxThreads = Runtime.getRuntime().availableProcessors();
        multithreading = new Boolean((Configable.CONFIG.getValue("server.threads.enable"))).booleanValue();
    }

    /**
     *
     */
    public ThreadRunner(boolean multiThreading) {
        super();
        maxThreads = Runtime.getRuntime().availableProcessors();
        multithreading = multiThreading;
    }

    /**
     * gint die anzahl der threads f?r dieses instanz zur?ck
     *
     * @return
     */
    public int getCountOfThreads() {
        return this.list.size();
    }

    /**
     * @see edu.ucdavis.genomics.metabolomics.binbase.binlib.algorythm.thread.Threadable#setListener(edu.ucdavis.genomics.metabolomics.binbase.binlib.algorythm.thread.ThreadListener)
     *
     * @uml.property name="listener"
     */
    public void setListener(ThreadListener listener) {
        this.listener = listener;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @uml.property name="maxThreads"
     */
    public final int getMaxThreads() {
        return maxThreads;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @uml.property name="multithreading"
     */
    public final boolean isMultithreading() {
        return multithreading;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name
     *            DOCUMENT ME!
     *
     * @uml.property name="name"
     */
    public final void setName(String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @uml.property name="name"
     */
    public final String getName() {
        return name;
    }

    /**
     * @see edu.ucdavis.genomics.metabolomics.binbase.binlib.algorythm.thread.ThreadRunnable#getResults()
     *
     * @uml.property name="results"
     */
    public Collection<Object> getResults() {
        return results;
    }

    /**
     * DOCUMENT ME!
     *
     * @param thread
     *            DOCUMENT ME!
     *
     * @uml.property name="thread"
     */
    public final void setThread(Thread thread) {
        this.thread = thread;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @uml.property name="thread"
     */
    public final Thread getThread() {
        return thread;
    }

    /**
     * f?gt einen neuen thread hinzu
     *
     * @param thread
     */
    public void addThreadable(Threadable thread) {
        this.list.add(thread);
    }

    /**
     * l?scht die liste
     *
     */
    public void clear() {
        list.clear();
    }

    /**
     * entfernt einen thread
     *
     * @param thread
     */
    public void removeThreadable(Threadable thread) {
        this.list.remove(thread);
    }

    /**
     * startet alle threads
     */
    public void run() {
        results.clear();
        this.counter = 0;
        ThreadListener liste = new ThreadRunnerThreadListener();
        for (int i = 0; i < this.list.size(); i++) {
            Threadable thread = this.list.get(i);
            thread.setListener(liste);
            Thread ins = new Thread(thread, thread.getName());
            thread.setThread(ins);
            if (multithreading == true) {
                ins.start();
            } else if (maxThreads == 1) {
                Thread.currentThread().setName(thread.getName());
                ins.run();
            } else {
                Thread.currentThread().setName(thread.getName());
                ins.run();
            }
            if (currentCount == maxThreads) {
                while (currentCount == maxThreads) {
                }
            }
        }
        while (counter < (this.list.size())) {
        }
        this.listener.threadFinshed();
    }

    /**
     * der interne threadlistener f?r diese klasse
     *
     * @author wohlgemuth
     *
     */
    private final class ThreadRunnerThreadListener implements ThreadListener {

        /**
         * @see edu.ucdavis.genomics.metabolomics.binbase.binlib.algorythm.thread.ThreadListener#setResult(java.lang.Object)
         */
        public void setResult(Object o) {
            if (o != null) {
                results.add(o);
            }
        }

        /**
         * @see edu.ucdavis.genomics.metabolomics.binbase.binlib.algorythm.thread.ThreadListener#startThread()
         */
        public void startThread() {
            currentCount++;
        }

        /**
         * @see edu.ucdavis.genomics.metabolomics.binbase.binlib.algorythm.thread.ThreadListener#threadFinshed()
         */
        public void threadFinshed() {
            counter++;
            currentCount--;
        }
    }

    public Object getIdentifier() {
        return null;
    }

    public void setIdentifier(Object o) {
    }
}
