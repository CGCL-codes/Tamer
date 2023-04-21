package org.hip.vif.forum.search;

import org.hip.vif.core.util.AbstractMessages;

/**
* Bundle specific messages.
* 
* @author Luthiger
* Created: 04.06.2011
*/
public class Messages extends AbstractMessages {

    private static final String BASE_NAME = "messages";

    @Override
    protected ClassLoader getLoader() {
        return getClass().getClassLoader();
    }

    @Override
    protected String getBaseName() {
        return BASE_NAME;
    }
}
