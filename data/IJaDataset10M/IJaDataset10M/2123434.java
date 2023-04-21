package annone.engine;

import annone.util.AnnoneException;
import annone.util.Text;

public class InterruptedChannelException extends AnnoneException {

    private static final long serialVersionUID = -1312359655725057627L;

    public InterruptedChannelException() {
        super(Text.get("Channel interrupted."));
    }

    public InterruptedChannelException(String message) {
        super(message);
    }

    public InterruptedChannelException(String message, Throwable cause) {
        super(message, cause);
    }
}
