package maze.redirector.common.cli;

import maze.common.adv_cli.CommonCliException;

/**
 * @author Normunds Mazurs (MAZE)
 * 
 */
public class RedirectorCommonCliException extends CommonCliException {

    public RedirectorCommonCliException(final String message) {
        super(message);
    }

    public RedirectorCommonCliException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RedirectorCommonCliException(final Throwable cause) {
        super(cause);
    }
}
