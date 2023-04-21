package jadex.contractnet.common;

import jadex.util.SimplePropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.Date;

/**
 * The order for purchasing or selling books.
 */
public class Order {

    /** The state open. */
    public static final String OPEN = "open";

    /** The state done. */
    public static final String DONE = "done";

    /** The state failed. */
    public static final String FAILED = "failed";

    /** The book title. */
    protected String title;

    /** The deadline. */
    protected Date deadline;

    /** The limit price. */
    protected int limit;

    /** The startprice. */
    protected int startprice;

    /** The starttime. */
    protected long starttime;

    /** The execution price. */
    protected Integer exeprice;

    /** The execution date. */
    protected Date exedate;

    /** The flag indicating if it is a buy (or sell) order. */
    protected boolean buyorder;

    /** The helper object for bean events. */
    public SimplePropertyChangeSupport pcs;

    /**
	 * Create a new order.
	 *
	 * @param title	The title.
	 * @param deadline The deadline.
	 * @param limit	The limit.
	 * @param start	The start price
	 */
    public Order(String title, Date deadline, int start, int limit, boolean buyorder) {
        this.title = title;
        this.deadline = deadline;
        this.startprice = start;
        this.limit = limit;
        this.buyorder = buyorder;
        this.starttime = System.currentTimeMillis();
        this.pcs = new SimplePropertyChangeSupport(this);
    }

    /**
	 * Get the title.
	 *
	 * @return The title.
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * Set the title.
	 *
	 * @param title The title.
	 */
    public void setTitle(String title) {
        String oldtitle = this.title;
        this.title = title;
        pcs.firePropertyChange("title", oldtitle, title);
    }

    /**
	 * Get the deadline.
	 *
	 * @return The deadline.
	 */
    public Date getDeadline() {
        return deadline;
    }

    /**
	 * Set the deadline.
	 *
	 * @param deadline The deadline.
	 */
    public void setDeadline(Date deadline) {
        Date olddeadline = this.deadline;
        this.deadline = deadline;
        pcs.firePropertyChange("deadline", olddeadline, deadline);
    }

    /**
	 * Get the limit.
	 *
	 * @return The limit.
	 */
    public int getLimit() {
        return limit;
    }

    /**
	 * Set the limit.
	 *
	 * @param limit The limit.
	 */
    public void setLimit(int limit) {
        int oldlimit = this.limit;
        this.limit = limit;
        pcs.firePropertyChange("limit", oldlimit, limit);
    }

    /**
	 * Getter for startprice
	 *
	 * @return Returns startprice.
	 */
    public int getStartPrice() {
        return startprice;
    }

    /**
	 * Setter for startprice.
	 *
	 * @param startprice The Order.java value to set
	 */
    public void setStartPrice(int startprice) {
        int oldstartprice = this.startprice;
        this.startprice = startprice;
        pcs.firePropertyChange("startPrice", oldstartprice, startprice);
    }

    /**
	 * Get the start time.
	 *
	 * @return The start time.
	 */
    public long getStartTime() {
        return starttime;
    }

    /**
	 * Set the start time.
	 *
	 * @param starttime The start time.
	 */
    public void setStartTime(long starttime) {
        long oldstarttime = this.starttime;
        this.starttime = starttime;
        pcs.firePropertyChange("startTime", new Long(oldstarttime), new Long(starttime));
    }

    /**
	 * Get the execution price.
	 *
	 * @return The execution price.
	 */
    public Integer getExecutionPrice() {
        return exeprice;
    }

    /**
	 * Set the execution price.
	 *
	 * @param exeprice The execution price.
	 */
    public void setExecutionPrice(Integer exeprice) {
        Integer oldexeprice = this.exeprice;
        this.exeprice = exeprice;
        pcs.firePropertyChange("executionPrice", oldexeprice, exeprice);
    }

    /**
	 * Get the execution date.
	 *
	 * @return The execution date.
	 */
    public Date getExecutionDate() {
        return exedate;
    }

    /**
	 * Set the execution date.
	 *
	 * @param exedate The execution date.
	 */
    public void setExecutionDate(Date exedate) {
        Date oldexedate = this.exedate;
        this.exedate = exedate;
        pcs.firePropertyChange("executionDate", oldexedate, exedate);
    }

    /**
	 * Test if it is a buyorder.
	 *
	 * @return True, if buy order.
	 */
    public boolean isBuyOrder() {
        return buyorder;
    }

    /**
	 * Set the order type.
	 *
	 * @param buyorder True for buyorder.
	 */
    public void setBuyOrder(boolean buyorder) {
        boolean oldbuyorder = this.buyorder;
        this.buyorder = buyorder;
        pcs.firePropertyChange("buyOrder", oldbuyorder ? Boolean.TRUE : Boolean.FALSE, buyorder ? Boolean.TRUE : Boolean.FALSE);
    }

    /**
	 * Get the order state.
	 *
	 * @return The order state.
	 */
    public String getState() {
        String state = FAILED;
        if (exedate != null) {
            state = DONE;
        } else if (System.currentTimeMillis() < deadline.getTime()) {
            state = OPEN;
        }
        return state;
    }

    /**
	 * Get a string representation of the order.
	 */
    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append(isBuyOrder() ? "Buy '" : "Sell '");
        sbuf.append(getTitle());
        sbuf.append("'");
        return sbuf.toString();
    }

    /**
	 * Add a PropertyChangeListener to the listener list.
	 * The listener is registered for all properties.
	 *
	 * @param listener The PropertyChangeListener to be added.
	 */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
	 * Remove a PropertyChangeListener from the listener list.
	 * This removes a PropertyChangeListener that was registered
	 * for all properties.
	 *
	 * @param listener The PropertyChangeListener to be removed.
	 */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
