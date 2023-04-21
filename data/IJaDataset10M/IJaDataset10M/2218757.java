package de.knowwe.core.report;

import de.knowwe.core.user.UserContext;

public interface MessageRenderer {

    public String postRenderMessage(Message m, UserContext user, String source);

    public String preRenderMessage(Message m, UserContext user, String source);
}
