package eu.soa4all.execution.adapter.parser.types;

import java.util.HashSet;

/**
 * Class MessageSet
 */
public class MessageSet {

    private HashSet<MessageName> MessageNames;

    public MessageSet() {
    }

    ;

    public MessageSet(HashSet<MessageName> messageNames) {
        MessageNames = messageNames;
    }

    public HashSet<MessageName> getMessageNames() {
        return MessageNames;
    }
}
