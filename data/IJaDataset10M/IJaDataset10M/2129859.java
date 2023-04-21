package net.sf.brightside.moljac.metamodel.beans;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import net.sf.brightside.moljac.core.beans.BaseBean;
import net.sf.brightside.moljac.metamodel.Book;
import net.sf.brightside.moljac.metamodel.Order;
import net.sf.brightside.moljac.metamodel.OrderLine;

@Entity
public class OrderLineBean extends BaseBean implements OrderLine {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3711011299745367536L;

    private Book book;

    private Order order;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, targetEntity = BookBean.class)
    public Book getBook() {
        return book;
    }

    @ManyToOne(targetEntity = OrderBean.class)
    public Order getOrder() {
        return order;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
