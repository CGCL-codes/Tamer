package icreate.servlets.engines;

public class EngineBean {

    /**
     * Holds value of property status.
     */
    private String status;

    /**
     * Holds value of property name.
     */
    private String name;

    /**
     * Holds value of property error.
     */
    private String error;

    /**
     * Holds value of property interval.
     */
    private String interval;

    /**
     * Holds value of property start.
     */
    private long start;

    /**
     * Holds value of property secs.
     */
    private long secs;

    /** Creates a new instance of EngineBean */
    public EngineBean() {
    }

    /**
     * Getter for property status.
     * @return Value of property status.
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for property error.
     * @return Value of property error.
     */
    public String getError() {
        return this.error;
    }

    /**
     * Setter for property error.
     * @param error New value of property error.
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Getter for property interval.
     * @return Value of property interval.
     */
    public String getInterval() {
        return this.interval;
    }

    /**
     * Setter for property interval.
     * @param interval New value of property interval.
     */
    public void setInterval(String interval) {
        this.interval = interval;
    }

    /**
     * Getter for property start.
     * @return Value of property start.
     */
    public long getStart() {
        return this.start;
    }

    /**
     * Setter for property start.
     * @param start New value of property start.
     */
    public void setStart(long start) {
        this.start = start;
    }

    /**
     * Getter for property secs.
     * @return Value of property secs.
     */
    public long getSecs() {
        return this.secs;
    }

    /**
     * Setter for property secs.
     * @param secs New value of property secs.
     */
    public void setSecs(long secs) {
        this.secs = secs;
    }
}
