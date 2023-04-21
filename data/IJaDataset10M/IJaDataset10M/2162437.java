package org.tolven.app.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author John Churin
 * A message indicating that a row in MenuDataVersion needs to be updated.
 */
public class MenuDataVersionMessage implements Serializable {

    private long accountId;

    private String element;

    private Date minDate;

    private Date maxDate;

    private String role;

    /**
     * The normal constructor takes a MenuData entity and extracts the listPath and dates that it uses to compute the date extent.
     * @param md MenuData 
     */
    public MenuDataVersionMessage(MenuData md) {
        String element = md.getListPath();
        setAccountId(md.getAccount().getId());
        setElement(element);
    }

    public MenuDataVersionMessage(String element, long accountId) {
        setAccountId(accountId);
        setElement(element);
    }

    /**
     * Apply date parameters of MenuData to this MenuDataVersionMessage
     * @param md
     */
    public void applyMenuData(MenuData md) {
        setMinMaxDate(md.getDate01());
        setMinMaxDate(md.getDate02());
        setMinMaxDate(md.getDate03());
        setMinMaxDate(md.getDate04());
        setRole(md.getMenuStructure().getRole());
    }

    /**
	 * Set the minimum and maximum date associated with this list. 
	 * If the supplied date is before the current min date, then the minimum is changed.
	 * If the supplied date is after the current max date, then the maximum is changed.
	 * @param date
	 */
    public void setMinMaxDate(Date date) {
        if (date != null && (this.minDate == null || this.minDate.after(date))) {
            this.minDate = date;
        }
        if (date != null && (this.maxDate == null || this.maxDate.before(date))) {
            this.maxDate = date;
        }
    }

    /**
	 * Get the minimum date
	 * @return
	 */
    public Date getMinDate() {
        return minDate;
    }

    /**
	 * Get the maximum date
	 * @return
	 */
    public Date getMaxDate() {
        return minDate;
    }

    /**
	 * @param minDate
	 */
    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    /**
	 * @param maxDate
	 */
    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }
}
