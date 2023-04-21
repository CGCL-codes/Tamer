package database.objects;

/**
 * Transa��o do schedule.
 * @author Landim - Arthur Landim
 *
 */
public class Transaction {

    public static final String RUNNING = "RODANDO";

    public static final String WAITING = "ESPERANDO";

    public static final String COMMITED = "COMMITED";

    public static final String ABORTED = "ABORTADA";

    /**
     * identificador da transa��o.
     */
    private Integer id;

    /**
     * timestamp.
     */
    private Integer timestamp;

    /**
     * timestamp.
     */
    private String state;

    /**
     * @return Returns the id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return this.id.equals(((Transaction) obj).getId());
    }

    /**
     * @return Returns the timestamp.
     */
    public Integer getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp The timestamp to set.
     */
    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return Returns the state.
     */
    public String getState() {
        return state;
    }

    /**
     * @param state The state to set.
     */
    public void setState(String state) {
        this.state = state;
    }
}
