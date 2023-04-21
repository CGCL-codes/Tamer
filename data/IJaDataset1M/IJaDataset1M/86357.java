package de.creepsmash.common.messages.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Message from client for selling a tower.
 * 
 * @author andreas
 *
 */
public class SellTowerMessage extends ClientMessage implements GameMessage {

    /**
	 * regular expression for message-parsing.
	 */
    private static final String REGEXP_SELL_TOWER = "SELL_TOWER\\s([0-9]+)";

    /**
	 * pattern for regular expression.
	 */
    public static final Pattern PATTERN = Pattern.compile(REGEXP_SELL_TOWER);

    private Integer towerId;

    /**
	 * @return the tower id
	 */
    public Integer getTowerId() {
        return this.towerId;
    }

    /**
	 * @param towerId the id of the tower
	 */
    public void setTowerId(Integer towerId) {
        this.towerId = towerId;
    }

    /**
	 * Initializes Message with data from string.
	 * 
	 * @param messageString the message-string
	 */
    @Override
    public void initWithMessage(String messageString) {
        Matcher matcher = PATTERN.matcher(messageString);
        if (matcher.matches()) {
            this.setTowerId(Integer.valueOf(matcher.group(1)));
        }
    }

    /**
	 * @return the message string
	 */
    @Override
    public String getMessageString() {
        return "SELL_TOWER " + this.getTowerId();
    }
}
