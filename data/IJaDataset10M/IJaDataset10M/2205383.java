package org.nakedobjects.runtime.transaction.messagebroker;

import java.util.List;

public interface MessageBroker {

    List<String> getMessages();

    String getMessagesCombined();

    List<String> getWarnings();

    String getWarningsCombined();

    void addWarning(String message);

    void addMessage(String message);

    void ensureEmpty();
}
