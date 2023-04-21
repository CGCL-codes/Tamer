package org.springframework.binding.validation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.CachingMapDecorator;
import org.springframework.util.ObjectUtils;

public class DefaultValidationResults implements ValidationResults {

    private final Set messages = new HashSet();

    private CachingMapDecorator messagesSubSets = new CachingMapDecorator() {

        protected Object create(Object key) {
            Set messagesSubSet = new HashSet();
            for (Iterator i = messages.iterator(); i.hasNext(); ) {
                ValidationMessage message = (ValidationMessage) i.next();
                if (key instanceof Severity && message.getSeverity().equals((Severity) key)) {
                    messagesSubSet.add(message);
                } else if (ObjectUtils.nullSafeEquals(message.getProperty(), key)) {
                    messagesSubSet.add(message);
                }
            }
            return Collections.unmodifiableSet(messagesSubSet);
        }
    };

    public DefaultValidationResults() {
    }

    public DefaultValidationResults(ValidationResults validationResults) {
        addAllMessages(validationResults);
    }

    public DefaultValidationResults(Collection validationMessages) {
        addAllMessages(validationMessages);
    }

    public void addAllMessages(ValidationResults validationResults) {
        addAllMessages(validationResults.getMessages());
    }

    public void addAllMessages(Collection validationMessages) {
        if (messages.addAll(validationMessages)) {
            messagesSubSets.clear();
        }
    }

    public void addMessage(ValidationMessage validationMessage) {
        if (messages.add(validationMessage)) {
            messagesSubSets.clear();
        }
    }

    public void addMessage(String field, Severity severity, String message) {
        addMessage(new DefaultValidationMessage(field, severity, message));
    }

    public void removeMessage(ValidationMessage message) {
        messages.remove(message);
        messagesSubSets.clear();
    }

    public boolean getHasErrors() {
        return getMessageCount(Severity.ERROR) > 0;
    }

    public boolean getHasWarnings() {
        return getMessageCount(Severity.WARNING) > 0;
    }

    public boolean getHasInfo() {
        return getMessageCount(Severity.INFO) > 0;
    }

    public int getMessageCount() {
        return messages.size();
    }

    public int getMessageCount(Severity severity) {
        return getMessages(severity).size();
    }

    public int getMessageCount(String fieldName) {
        return getMessages(fieldName).size();
    }

    public Set getMessages() {
        return Collections.unmodifiableSet(messages);
    }

    public Set getMessages(Severity severity) {
        return (Set) messagesSubSets.get(severity);
    }

    public Set getMessages(String fieldName) {
        return (Set) messagesSubSets.get(fieldName);
    }

    public String toString() {
        return new ToStringCreator(this).append("messages", getMessages()).toString();
    }
}
