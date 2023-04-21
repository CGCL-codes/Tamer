package org.nakedobjects.examples.orders.domain;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.annotation.DescribedAs;
import org.nakedobjects.applib.annotation.Disabled;
import org.nakedobjects.applib.annotation.MaxLength;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.applib.annotation.TypicalLength;
import org.nakedobjects.applib.annotation.When;
import org.nakedobjects.applib.util.TitleBuffer;
import org.nakedobjects.applib.clock.Clock;
import org.nakedobjects.examples.orders.services.CustomerRepository;

public class Customer extends AbstractDomainObject {

    /**
     * Defines the title that will be displayed on the user
     * interface in order to identity this object.
     */
    public String title() {
        TitleBuffer t = new TitleBuffer();
        if (getFirstName() != null) {
            t.append(getFirstName()).append(getLastName());
        }
        return t.toString();
    }

    private String firstName;

    @DescribedAs("Given or christian name")
    @TypicalLength(20)
    @MaxLength(100)
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private String lastName;

    @DescribedAs("Family name or surname")
    @MaxLength(100)
    @TypicalLength(30)
    @Named("Surname")
    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private Integer customerNumber;

    @Disabled(When.ONCE_PERSISTED)
    public Integer getCustomerNumber() {
        return this.customerNumber;
    }

    public void setCustomerNumber(Integer customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String validateCustomerNumber(Integer customerNumber) {
        return null;
    }

    private List<Order> orders = new ArrayList<Order>();

    public List<Order> getOrders() {
        return this.orders;
    }

    @SuppressWarnings("unused")
    private void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void addToOrders(Order order) {
        getOrders().add(order);
    }

    public void removeFromOrders(Order order) {
        getOrders().remove(order);
    }

    private Order lastOrder;

    @Disabled
    public Order getLastOrder() {
        return this.lastOrder;
    }

    public void setLastOrder(Order lastOrder) {
        this.lastOrder = lastOrder;
    }

    public void modifyLastOrder(Order lastOrder) {
        setLastOrder(lastOrder);
    }

    public void clearLastOrder() {
        setLastOrder(null);
    }

    public void placeOrder(Product p, @Named("Quantity") Integer quantity) {
        Order order = (Order) getContainer().newTransientInstance(Order.class);
        order.modifyCustomer(this);
        order.modifyProduct(p);
        order.setOrderDate(new Date(Clock.getTime()));
        order.setQuantity(quantity);
        addToOrders(order);
        modifyLastOrder(order);
        getContainer().persist(order);
    }

    public String validatePlaceOrder(Product p, Integer quantity) {
        if (quantity < 1 || quantity > 100) {
            return "Quantity must be between 1 and 100";
        }
        return null;
    }

    public Object[] defaultPlaceOrder() {
        Product lastProductOrdered = null;
        if (getLastOrder() != null) {
            lastProductOrdered = getLastOrder().getProduct();
        }
        return new Object[] { lastProductOrdered, new Integer(1) };
    }

    public String disablePlaceOrder() {
        return !isPersistent(this) ? "Save object first" : null;
    }

    private CustomerRepository customerRepository;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected CustomerRepository getCustomerRepository() {
        return this.customerRepository;
    }

    /**
     * Injected by the application container.
     */
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
}
