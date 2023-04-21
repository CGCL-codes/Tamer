package test.hibernate21;

import java.util.Calendar;
import java.util.List;

/** * @hibernate.query name="myNamedQuery" * 	query = "from myClass where myProperty = 'X'" */
public class Query extends Persistent implements Updateable {

    private List items;

    private Human customer;

    private Calendar date;

    private String updateComment;

    /**	 * Constructor for Order.	 */
    public Query() {
        super();
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Human getCustomer() {
        return customer;
    }

    public void setCustomer(Human customer) {
        this.customer = customer;
    }

    public String getUpdateComment() {
        return updateComment;
    }

    public void setUpdateComment(String string) {
        updateComment = string;
    }
}
