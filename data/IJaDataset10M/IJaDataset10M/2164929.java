package org.fuin.auction.command.api.base;

import static org.fuin.auction.common.OperationResultType.ERROR;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.fuin.auction.common.AbstractOperationResult;

/**
 * The user's password was not equal to the old password sent with the command.
 */
public final class UserChangePasswordMismatchResult extends AbstractOperationResult {

    private static final long serialVersionUID = 100L;

    /** Unique ID of the result. */
    public static final int CODE = 105;

    /**
	 * Default constructor.
	 */
    public UserChangePasswordMismatchResult() {
        super(CODE, ERROR, "The old password is not equal to the stored password");
    }

    @Override
    public final String toTraceString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        appendAbstractCommandResult(builder);
        return builder.toString();
    }
}
