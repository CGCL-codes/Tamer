package net.sf.josser;

import net.sf.josser.rdf.impl.Dump;

public class LongTask {

    private int lengthOfTask;

    private int current = 0;

    private boolean done = false;

    private boolean canceled = false;

    private String statMessage;

    public LongTask() {
        lengthOfTask = 100;
    }

    public void go() {
        SwingWorker worker = new SwingWorker() {

            public Object construct() {
                setCurrent(0);
                setDone(false);
                setCanceled(false);
                setStatMessage(null);
                return new ActualTask();
            }
        };
        worker.start();
    }

    public int getLengthOfTask() {
        return lengthOfTask;
    }

    public int getCurrent() {
        return current;
    }

    public void stop() {
        setCanceled(true);
        setStatMessage(null);
    }

    public boolean isDone() {
        return done;
    }

    public String getMessage() {
        return getStatMessage();
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public boolean isCanceled() {
        return canceled;
    }

    /**
	 * @param current the current to set
	 */
    private void setCurrent(int current) {
        this.current = current;
    }

    /**
	 * @param done the done to set
	 */
    private void setDone(boolean done) {
        this.done = done;
    }

    /**
	 * @param statMessage the statMessage to set
	 */
    private void setStatMessage(String statMessage) {
        this.statMessage = statMessage;
    }

    /**
	 * @return the statMessage
	 */
    private String getStatMessage() {
        return statMessage;
    }

    class ActualTask {

        ActualTask() {
            setDone(false);
            Josser.initProperties();
            boolean test = Josser.checkConnection();
            if (test) {
                System.out.println("Successfully connected to " + Josser.getEngine().toUpperCase() + " database " + Josser.getDB() + "@" + Josser.getHost() + " as user " + Josser.getUsername());
                Dump dmoz = new Dump(Josser.getPath());
                System.out.println("Going to parse RDF DMOZ dumps located under path" + " " + Josser.getPath() + " for all entries under category " + Josser.getTopicfilter());
                setCurrent(0);
                dmoz.getSpreparser().parse(Josser.getRChunk());
                setCurrent(33);
                dmoz.getSparser().parse(Josser.getWChunk());
                setCurrent(66);
                System.gc();
                dmoz.getCparser().parse(Josser.getRChunk());
                System.out.println("Parsed, filtered and imported DMOZ RDF dumps.");
                Josser.disconnect();
                setCurrent(100);
                setDone(true);
            } else {
                System.exit(1);
            }
            setDone(true);
        }
    }
}
