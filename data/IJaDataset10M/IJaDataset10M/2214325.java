package de.spieleck.app.jacson.filter;

import de.spieleck.config.ConfigNode;
import de.spieleck.config.ConfigVerify.Acceptor;
import de.spieleck.app.jacson.JacsonException;
import de.spieleck.app.jacson.JacsonRegistry;
import de.spieleck.app.jacson.JacsonConfigException;
import de.spieleck.app.jacson.util.ConfigUtil;

/**
 * Filter that converts chunks to all uppercase or all lowercase.
 * @author fsn
 * @version $Id: CaseFilter.java 45 2005-10-08 22:55:04Z pcs $
 * @jacson:plugin subtype="modify"
 */
public class CaseFilter extends FilterBase implements Acceptor {

    public static final String TOLOWER_NODE = "tolower";

    public static final String TOUPPER_NODE = "toupper";

    /**
     * if true incoming chunks are contered to lowercase
     * @jacson:param name="tolower" default="false"
     */
    protected boolean toLower;

    /**
     * if true incoming chunks are converted to uppercase
     * @jacson:param name="toupper" default="false"
     */
    boolean toUpper;

    public void init(ConfigNode config, JacsonRegistry registry) throws JacsonConfigException {
        ConfigUtil.verify(config, this);
        toUpper = config.getBoolean(TOLOWER_NODE, false);
        toLower = config.getBoolean(TOUPPER_NODE, false);
        if (toLower ^ toUpper) throw new JacsonConfigException("CaseFilter needs tolower/toupper");
    }

    public boolean accept(ConfigNode node) {
        String name = node.getName();
        return TOLOWER_NODE.equals(name) || TOUPPER_NODE.equals(name);
    }

    public void putChunk(String chunk) throws JacsonException {
        if (chunk == null) {
            drain.putChunk(chunk);
        } else if (toLower) {
            drain.putChunk(chunk.toLowerCase());
        } else {
            drain.putChunk(chunk.toUpperCase());
        }
    }
}
