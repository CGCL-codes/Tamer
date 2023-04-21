package scam.repository.database.pool;

import java.sql.*;
import java.text.*;
import java.util.Date;

/**
* A wrapper class that holds a Connection.  This class
* keeps track of the time the connection was created, how
* often it has been used, and its current allocation state.
*
* @see ConnectionPool
* @see Connection
* @version $Revision: 1.1.1.1 $
*/
public class PoolEntry {

    private static int handleCounter = 0;

    public static final DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss.SSS");

    /**
   * The state in which the entry is available for allocation
   */
    public static final int AVAILABLE = 1;

    /**
   * The state which an entry has when it is currently
   * allocated to a client.
   */
    public static final int IN_USE = 2;

    private Connection con;

    private long creationTime;

    private int timesUsed;

    private int state;

    private int handle;

    /**
   * Creates a new pool entry containing the specified
   * connection
   * @param con the connection
   */
    public PoolEntry(Connection con) {
        this.con = con;
        this.creationTime = System.currentTimeMillis();
        this.timesUsed = 0;
        this.state = AVAILABLE;
        this.handle = handleCounter++;
    }

    /**
   * Returns true if the connection is still valid
   */
    public boolean isValid() {
        boolean result = false;
        if (con != null) {
            try {
                result = !con.isClosed();
            } catch (SQLException ignore) {
            }
        }
        return result;
    }

    /**
   * Returns the connection
   */
    public Connection getConnection() {
        return con;
    }

    /**
   * Returns the creation time in milliseconds
   */
    public long getCreationTime() {
        return creationTime;
    }

    /**
   * Returns the number of times the resource has been used
   */
    public int getTimesUsed() {
        return timesUsed;
    }

    /**
   * Changes the entry state to IN_USE and
   * increments the times used counter.
   */
    public void allocate() {
        state = IN_USE;
        timesUsed++;
    }

    /**
   * Closes the underlying resource.
   */
    public void close() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ignore) {
            }
        }
    }

    /**
   * Returns the allocation state of the entry.
   */
    public int getState() {
        return state;
    }

    /**
   * Sets the entry's state
   */
    public void setState(int newState) {
        state = newState;
    }

    /**
   * Returns the entry's handle, which is a sequentially
   * assigned number that uniquely identifies this entry.
   */
    public int getHandle() {
        return handle;
    }

    /**
   * Returns the entry as a string
   */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(handle);
        buffer.append(":");
        buffer.append(con);
        buffer.append(",created=");
        buffer.append(timeFormat.format(new java.util.Date(creationTime)));
        buffer.append(",used=");
        buffer.append(timesUsed);
        buffer.append(",state=");
        switch(state) {
            case AVAILABLE:
                buffer.append("AVAILABLE");
                break;
            case IN_USE:
                buffer.append("IN_USE");
                break;
        }
        return buffer.toString();
    }
}
