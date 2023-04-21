package skycastle.chat.channel;

import skycastle.chat.event.ChatEventRecipient;
import skycastle.chat.ChatAvatar;

/**
 * A chat channel (also known as chat room).
 * <p/>
 * Can act as a target for chat commands.
 * <p/>
 * Holds a number of participants, that get the messages told to the room.
 * <p/>
 * IDEA: Support connecting to external chat services such as IRC from the client, to more easily access support and tie in with
 * existing communities, and offline users.
 * <p/>
 * IDEA: One type of channel might be the local surroundings channel, which shows anyone aroung an agent in the world,
 * displaying messages from more distant people with greyer color / smaller font.
 *
 * @author Hans H�ggstr�m
 */
public interface Channel extends ChatEventRecipient {

    /**
     * @return the unique identifier for the chat channel.  Contains any possible preceding # -sign
     *         (that way we can have channels without a preceding # sign too).
     */
    String getChannelIdentifier();

    /**
     * Add the specified avatar to this channel.  The avatar will get events from this channel.
     *
     * @param chatAvatar the avatar to connect.
     */
    void join(ChatAvatar chatAvatar);

    /**
     * Remove the specified avatar from this channel.
     *
     * @param chatAvatar the avatar to remove.
     */
    void leave(ChatAvatar chatAvatar);

    /**
     *
     * @param chatAvatar the agent to check
     * @return true if the specified agent is in this channel, false if not.
     */
    boolean isInChannel(final ChatAvatar chatAvatar);
}
