package com.usoog.commons.gamecore.message;

import com.twolattes.json.Entity;
import com.twolattes.json.Json;
import com.twolattes.json.Marshaller;
import com.twolattes.json.TwoLattes;
import com.twolattes.json.Value;
import com.usoog.commons.network.message.AbstractMessage;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * This message with contain an entire replay.
 * Perhaps the input/output might want to be compressed
 *	to save bandwidth and memory.
 *
 * The sender id is a userId for this message.
 *
 * @author Jimmy Axenhus
 * @author Hylke van der Schaaf
 */
@Entity
public class MessageReplay extends AbstractMessage {

    /**
	 * The unique KEY for this Message.
	 */
    public static final String KEY = "RP";

    /**
	 * The Marshaller needed for this object.
	 */
    private static Marshaller<MessageReplay> marshaller = TwoLattes.createMarshaller(MessageReplay.class);

    /**
	 * This is the entire replay.
	 */
    private String replay;

    /**
	 * A list of options belonging to this replay, like if it's public, a ladder
	 * game, single or multi player, etc.
	 */
    private Map<String, String> options;

    /**
	 * Default constructor.
	 */
    public MessageReplay() {
        super(KEY);
    }

    /**
	 * This will set an unique KEY for the replay message.
	 *
	 * @param senderId The sender of the replay. Can only be server.
	 * @param replay The replay data.
	 * @param options The options to pass.
	 */
    public MessageReplay(String replay, Map<String, String> options) {
        super(KEY);
        this.replay = replay;
        this.options = options;
    }

    @Override
    public String getMessage() {
        return getKey() + " " + marshaller.marshall(this).toString();
    }

    @Override
    public void initFromString(String message) throws UnsupportedEncodingException {
        MessageReplay unmarshall = marshaller.unmarshall((Json.Object) Json.fromString(message.substring(KEY.length())));
        replay = unmarshall.replay;
        options = unmarshall.options;
    }

    /**
	 * This will return the entire replay.
	 *
	 * @return The replay.
	 */
    @Value
    public String getReplay() {
        return replay;
    }

    /**
	 * The replay data to be set.
	 *
	 * @param replay The replay data to set.
	 */
    @Value
    public void setReplay(String replay) {
        this.replay = replay;
    }

    /**
	 * This will return the options of the message.
	 *
	 * @return The options.
	 */
    @Value
    public Map<String, String> getOptions() {
        return options;
    }

    /**
	 * This will set the options of this message.
	 *
	 * @param options The options to set.
	 */
    @Value
    public void setOptions(Map<String, String> options) {
        this.options = options;
    }
}
