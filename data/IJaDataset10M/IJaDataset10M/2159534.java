package securus.client.main.tablemodel;

/**
 *
 * @author e.dovgopoliy
 */
public interface TableModelSearchListener {

    public enum Action {

        STARTED, FINISHED, FINDING
    }

    public void actionPerformed(Action action, String folder);
}
