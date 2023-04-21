package gwt.cassowary.client.EDU.Washington.grad.gjb;

public class ExCLInternalError extends ExCLError {

    private static final long serialVersionUID = 1L;

    public ExCLInternalError(String s) {
        description_ = s;
    }

    public String description() {
        return "(ExCLInternalError) " + description_;
    }

    private String description_;
}
