package org.shestkoff.nimium.service.exception;

/**
 * Numium project
 * Time: 15.11.2009 9:03:52
 *
 * @author Vaily Shestkov
 */
public class OrderNotFound extends DeletingOrderException {

    public OrderNotFound() {
    }

    public OrderNotFound(String s) {
        super(s);
    }

    public OrderNotFound(String s, Throwable throwable) {
        super(s, throwable);
    }

    public OrderNotFound(Throwable throwable) {
        super(throwable);
    }
}
