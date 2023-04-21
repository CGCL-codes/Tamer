package jimo.osgi.modules.knowledge.api;

import java.util.List;

public abstract class MessageBaseSkeleton implements MessageBase {

    public void leaveMessage(String fromName, String toName, String message) {
    }

    public List getMessage(String toName, String fromName) {
        return null;
    }

    public List getMessage(String toName) {
        return null;
    }
}
