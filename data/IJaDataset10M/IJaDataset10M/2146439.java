package org.archive.crawler.settings;

import java.io.Serializable;
import java.util.logging.Level;

/**
 * A constraint that checks that an attribute value is of the right type
 *
 * @author John Erik Halse
 */
public class LegalValueTypeConstraint extends Constraint implements Serializable {

    private static final long serialVersionUID = 6106774072922858976L;

    /**
     * Constructs a new LegalValueListConstraint.
     *
     * @param level the severity level.
     * @param msg the default error message.
     */
    public LegalValueTypeConstraint(Level level, String msg) {
        super(level, msg);
    }

    /**
     * Constructs a new LegalValueListConstraint using default severity level
     * ({@link Level#WARNING}).
     *
     * @param msg the default error message.
     */
    public LegalValueTypeConstraint(String msg) {
        this(Level.SEVERE, msg);
    }

    /**
     * Constructs a new LegalValueListConstraint using default error message.
     *
     * @param level
     */
    public LegalValueTypeConstraint(Level level) {
        this(level, "Value of illegal type: ''{3}'', ''{4}'' was expected.");
    }

    /**
     * Constructs a new LegalValueListConstraint using default severity level
     * ({@link Level#WARNING}) and default error message.
     *
     */
    public LegalValueTypeConstraint() {
        this(Level.SEVERE);
    }

    public FailedCheck innerCheck(CrawlerSettings settings, ComplexType owner, Type definition, Object value) {
        FailedCheck res = null;
        if (!definition.getLegalValueType().isInstance(value)) {
            res = new FailedCheck(settings, owner, definition, value);
            res.messageArguments.add((value != null) ? value.getClass().getName() : "null");
            res.messageArguments.add(definition.getLegalValueType().getName());
        }
        return res;
    }
}
