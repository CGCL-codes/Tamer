package org.milligan.eccles.tags;

import org.apache.log4j.Category;
import org.milligan.eccles.EcclesException;
import org.milligan.eccles.EcclesReturnValue;
import org.milligan.eccles.RunState;
import org.milligan.eccles.Tag;

/**
 * A tag to send a log message to the console
 * @author Ian Tomey
 *
 */
public class LogTag extends Tag {

    protected static final Category log = Category.getInstance("RunLog");

    public LogTag() {
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    private String level;

    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getTagName() {
        return "log";
    }

    /**
	 *
	 * @return
	 */
    public EcclesReturnValue doStartTag(RunState state) throws EcclesException {
        log.info(state.evaluate(message, String.class));
        return SKIP_CHILDREN;
    }

    /**
	 * Tag properties for output
	 */
    protected static final String tagSpecificProperties[] = new String[] { "message", "level" };

    /**
	 * Get a list of properties to be output
	 * @return list of properties
	 */
    public String[] getTagPropertiesForDisplay() {
        return tagSpecificProperties;
    }
}
