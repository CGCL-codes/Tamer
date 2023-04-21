package annone.engine;

import annone.util.AnnoneException;
import annone.util.Text;

public class EngineShutdownException extends AnnoneException {

    private static final long serialVersionUID = 2961994618855680246L;

    public EngineShutdownException() {
        super(Text.get("Engine is shutting down."));
    }

    public EngineShutdownException(String message) {
        super(message);
    }

    public EngineShutdownException(String message, Throwable cause) {
        super(message, cause);
    }
}
