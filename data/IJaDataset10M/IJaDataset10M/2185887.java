package org.timothyb89.jtelirc.user;

import org.timothyb89.jtelirc.channel.Channel;
import org.timothyb89.jtelirc.message.UserMessage;

/**
 *
 * @author tim
 */
public interface UserListener {

    /**
	 * Called when a user sends a message (of some sort)
	 * @param u The user
	 * @param message the message sent
	 */
    public void onUserMessageReceived(User u, UserMessage message);

    /**
	 * Called when the user joins a channel. This only notifies when a user
	 * joins a channel the bot is already in.
	 *
	 * @param user The user
	 * @param c the channel joined
	 */
    public void onUserJoinedChannel(User user, Channel c);

    /**
	 * Called when the user leaves a channel. This only notifies when a user
	 * leaves a channel the client is already in.
	 * @param user The user
	 * @param c The channel left.
	 * @param msg The part message.
	 */
    public void onUserLeftChannel(User user, Channel c, String msg);

    /**
	 * Called when the user leaves (quits) the server.
	 * @param user The user
	 * @param message The quite message.
	 */
    public void onUserQuit(User user, String message);
}
