package hotpotato.acceptance;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class ReverseOrder implements Callable<Serializable>, Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;

    public ReverseOrder(String message) {
        this.message = message;
    }

    public Serializable call() {
        return new StringBuffer(message).reverse().toString();
    }
}
