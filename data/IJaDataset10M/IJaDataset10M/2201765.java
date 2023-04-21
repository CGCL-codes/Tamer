package cz.cuni.mff.ksi.jinfer.runner;

/**
 *
 * @author rio
 */
public class MissingModuleException extends RuntimeException {

    private static final long serialVersionUID = 87942132L;

    public MissingModuleException(final String message) {
        super(message);
    }
}
