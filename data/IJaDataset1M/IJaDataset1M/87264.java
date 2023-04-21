package storm.integration.validation;

import java.util.ArrayList;
import java.util.List;

public class IntegrationException extends Exception {

    List<Throwable> reasons = new ArrayList<Throwable>();

    public IntegrationException() {
        super();
    }

    public IntegrationException(String msg) {
        super(msg);
    }

    public List<Throwable> getReasons() {
        return reasons;
    }
}
