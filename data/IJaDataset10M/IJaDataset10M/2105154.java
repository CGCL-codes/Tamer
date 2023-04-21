package lpp.citytrans.server.exceptions;

public class RouteNotFoundException extends Exception {

    private static final long serialVersionUID = -6870509601650234005L;

    public RouteNotFoundException(String msg) {
        super(msg);
    }
}
