package org.vramework.vow.sampleapps.exceptions;

import org.vramework.commons.exceptions.VRuntimeException;
import org.vramework.commons.logging.LogMessages;
import org.vramework.commons.resources.IMessage;

public class SampleAppsException extends VRuntimeException {

    private static final long serialVersionUID = -8973057300101577997L;

    public SampleAppsException(IMessage key, LogMessages additionalMessages) {
        super(key, additionalMessages);
    }

    public SampleAppsException(IMessage key, Object replacementKiller, Throwable cause, LogMessages additionalMessages) {
        super(key, replacementKiller, cause, additionalMessages);
    }

    public SampleAppsException(IMessage key, Object replacementKiller, Throwable cause) {
        super(key, replacementKiller, cause);
    }

    public SampleAppsException(IMessage key, Object replacementKiller) {
        super(key, replacementKiller);
    }

    public SampleAppsException(IMessage key, Object[] replacementValues, LogMessages additionalMessages) {
        super(key, replacementValues, additionalMessages);
    }

    public SampleAppsException(IMessage key, Object[] replacementValues, Throwable cause, LogMessages additionalMessages) {
        super(key, replacementValues, cause, additionalMessages);
    }

    public SampleAppsException(IMessage key, Object[] replacementValues, Throwable cause) {
        super(key, replacementValues, cause);
    }

    public SampleAppsException(IMessage key, Object[] replacementValues) {
        super(key, replacementValues);
    }

    public SampleAppsException(IMessage key, Throwable cause) {
        super(key, cause);
    }

    public SampleAppsException(IMessage key) {
        super(key);
    }

    public SampleAppsException(Throwable cause) {
        super(cause);
    }

    public SampleAppsException(VRuntimeException cause) {
        super(cause);
    }
}
