package de.knowwe.core.correction;

import de.knowwe.core.append.PageAppendHandler;
import de.knowwe.core.user.UserContext;

/**
 * This append handler is a placebo (read: it does nothing) for loading
 * JavaScript and CSS for the TermReference correction functionality
 * via plugin.xml.
 *
 * @author Alex Legler
 * @created 20.11.2010
 */
public class CorrectionAppendHandler implements PageAppendHandler {

    @Override
    public String getDataToAppend(String topic, String web, UserContext user) {
        return "";
    }

    @Override
    public boolean isPre() {
        return false;
    }
}
