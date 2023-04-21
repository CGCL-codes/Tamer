package progranet.model.service.impl;

import java.util.ArrayList;
import java.util.List;
import progranet.omg.cmof.InstanceSpecification;
import progranet.omg.core.types.Constraint;
import progranet.omg.core.types.Property;

public class Messages {

    public static enum Severity {

        INFO, WARNING, ERROR
    }

    public static enum Kind {

        VALUE_ADD, VALUE_REMOVE, VALUE_COLLECTION_LOWER, VALUE_COLLECTION_UPPER, VALUE_REQUIRED, VALUE_IS_NOT_COLLECTION, VALUE_TYPE_MISMATCH, CONSTRAINT_EVALUATION, CONSTRAINT_EXCEPTION, VALIDATION_ERROR, REVISION_CONFLICT, DELETED_VALUE_MODIFICATION, READ_ONLY_ELEMENT_MODIFICATION, READ_ONLY_VALUE_MODIFICATION, ELEMENT_ALREADY_EXISTS, EVENT_NOT_REMOVABLE, NO_SUCH_PROPERTY, NO_SUCH_TYPE, XML_SYNTAX
    }

    private List<Message> messages = new ArrayList<Message>();

    private boolean error = false;

    public Messages() {
    }

    public boolean isError() {
        return this.error;
    }

    public List<Message> getMessages() {
        return this.messages;
    }

    public Message add(Severity severity, Kind message, InstanceSpecification context) {
        Message mess = new Message();
        this.messages.add(mess);
        mess.severity = severity;
        if (severity == Severity.ERROR) this.error = true;
        mess.message = message;
        mess.context = context == null ? null : context.getId();
        return mess;
    }

    public Message add(Severity severity, Constraint cause, InstanceSpecification context) {
        Message mess = this.add(severity, Kind.CONSTRAINT_EXCEPTION, context);
        mess.constraintCause = cause;
        return mess;
    }

    public Message add(Severity severity, Exception cause, Kind message, InstanceSpecification context) {
        Message mess = this.add(severity, message, context);
        mess.cause = cause;
        return mess;
    }

    public Message add(Severity severity, Kind message, InstanceSpecification context, Property property) {
        Message mess = this.add(severity, message, context);
        mess.property = property;
        return mess;
    }

    public Message add(Severity severity, Kind message, InstanceSpecification context, Property property, InstanceSpecification reference, String value) {
        Message mess = this.add(severity, message, context, property);
        mess.value = value;
        mess.reference = reference == null ? null : (reference.getId() == null ? "<<new>>" : reference.getId());
        return mess;
    }

    public String toString() {
        String str = "<messages>";
        for (Message message : messages) {
            if (message.getSeverity() != Severity.INFO) str = str + message.toString() + "\n";
        }
        str = str + "</messages>";
        return str;
    }

    public class Message {

        private Severity severity;

        private Kind message;

        private String context;

        private Property property;

        private String value;

        private String reference;

        private Exception cause;

        private Constraint constraintCause;

        private String ext;

        public Severity getSeverity() {
            return severity;
        }

        public Kind getMessage() {
            return message;
        }

        public String getContext() {
            return context;
        }

        public Property getProperty() {
            return property;
        }

        public String getValue() {
            return value;
        }

        public Exception getCause() {
            return cause;
        }

        public Constraint getConstraintCause() {
            return constraintCause;
        }

        public String getReference() {
            return reference;
        }

        public Message setExt(String ext) {
            this.ext = ext;
            return this;
        }

        public String getExt() {
            return this.ext;
        }

        public String toString() {
            String str = "<message";
            if (this.severity != null) str = str + " severity=\"" + this.severity + "\"";
            if (this.message != null) str = str + " message=\"" + this.message + "\"";
            if (this.context != null) str = str + " context=\"" + this.context + "\"";
            if (this.property != null) str = str + " property=\"" + this.property + "\"";
            if (this.value != null) str = str + " value=\"" + this.value + "\"";
            if (this.reference != null) str = str + " reference=\"" + this.reference + "\"";
            if (this.ext != null) str = str + " ext=\"" + this.ext + "\"";
            str = str + ">";
            if (this.cause != null) str = str + this.cause; else if (this.constraintCause != null) str = str + this.constraintCause;
            str = str + "</message>";
            return str;
        }
    }
}
