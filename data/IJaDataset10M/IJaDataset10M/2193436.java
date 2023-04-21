package org.fuin.auction.command.api.base;

import org.fuin.auction.common.Operation;
import org.fuin.auction.common.OperationResult;

/**
 * Service for sending commands to the command server - This is implemented
 * directly by the command server.<br>
 * <br>
 * CAUTION: The {@link OperationResult} is NOT meant to be a way to return
 * arbitrary results.<br>
 * <br>
 * Sending a command should be in most cases a fire-and-forget affair returning
 * simply a kind of void result. The only other use case that may include a
 * return value is a command that creates a new aggregate and then returns a new
 * ID. If the client side creates the aggregate IDs there is even no need for
 * that.
 */
public interface AuctionCommandService {

    /**
	 * Sends a command to the server.
	 * 
	 * @param command
	 *            Command.
	 * 
	 * @return Result.
	 */
    public OperationResult send(Operation command);
}
