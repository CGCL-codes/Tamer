package games.midhedava.server.entity;

import java.util.Arrays;
import java.util.List;
import marauroa.common.Log4J;
import marauroa.common.game.AttributeNotFoundException;
import marauroa.common.game.RPClass;
import org.apache.log4j.Logger;

public class Sign extends Entity {

    /** the logger instance. */
    private static final Logger logger = Log4J.getLogger(Sign.class);

    /**
	 * Classes of signs that players, NPCs etc. can walk over
	 * and where you can put items on if they are not placed
	 * on a collision tile.
	 */
    private static final List<String> NON_OBSTACLE_CLASSES = Arrays.asList("book_blue", "book_red", "transparent");

    public static void generateRPClass() {
        try {
            RPClass sign = new RPClass("sign");
            sign.isA("entity");
            sign.add("text", RPClass.LONG_STRING);
            sign.add("class", RPClass.STRING);
        } catch (RPClass.SyntaxException e) {
            logger.error("cannot generate RPClass", e);
        }
    }

    public Sign() throws AttributeNotFoundException {
        super();
        put("type", "sign");
    }

    public void setText(String text) {
        put("text", text);
    }

    /**
	 * States what type of sign this should be. This defines how
	 * it will look like in the client.
	 * @param clazz The sign class, e.g. "default" or "signpost".
	 */
    public void setClass(String clazz) {
        put("class", clazz);
    }

    @Override
    public boolean isObstacle(Entity entity) {
        return !(has("class") && NON_OBSTACLE_CLASSES.contains(get("class")));
    }
}
