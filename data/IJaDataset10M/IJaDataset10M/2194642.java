package org.fuin.auction.command.server.handler;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.AggregateNotFoundException;
import org.fuin.auction.command.api.base.AggregateIdNotFoundResult;
import org.fuin.auction.command.api.base.UserChangePasswordCommand;
import org.fuin.auction.command.api.base.UserChangePasswordMismatchResult;
import org.fuin.auction.command.server.domain.PasswordMismatchException;
import org.fuin.auction.common.OperationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler for managing {@link UserChangePasswordCommand} commands.
 */
public abstract class AbstractUserChangePasswordCommandHandler extends AbstractUserCommandHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractUserChangePasswordCommandHandler.class);

    /**
	 * Changes the user's password.
	 * 
	 * @param command
	 *            Command to handle.
	 * 
	 * @return Result of the command.
	 */
    @CommandHandler
    public final OperationResult handle(final UserChangePasswordCommand command) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Handle command: " + command.toTraceString());
        }
        try {
            return handleIntern(command);
        } catch (final PasswordMismatchException ex) {
            LOG.info(ex.getMessage() + ": " + command.toTraceString());
            return new UserChangePasswordMismatchResult();
        } catch (final AggregateNotFoundException ex) {
            LOG.info(ex.getMessage() + ": " + command.toTraceString());
            return new AggregateIdNotFoundResult();
        }
    }

    /**
	 * Changes the user's password.
	 * 
	 * @param command
	 *            Valid command to handle.
	 * 
	 * @return Result of the command.
	 * 
	 * @throws PasswordMismatchException
	 *             The user's password was not equal to the old password sent
	 *             with the command.
	 */
    protected abstract OperationResult handleIntern(final UserChangePasswordCommand command) throws PasswordMismatchException;
}
