package org.nakedobjects.nof.reflect.remote.data;

public class DummyResultData implements ServerActionResultData {

    private final Data result;

    private final ObjectData[] updatesData;

    private final ObjectData persistedTarget;

    private final ObjectData[] persistedParameters;

    private String[] warnings;

    private String[] messages;

    public DummyResultData(final Data result, final ObjectData[] updatesData, final ObjectData persistedTarget, final ObjectData[] persistedParameters, final String[] messages, final String[] warnings) {
        this.result = result;
        this.updatesData = updatesData;
        this.persistedTarget = persistedTarget;
        this.persistedParameters = persistedParameters;
        this.messages = messages;
        this.warnings = warnings;
    }

    public Data getReturn() {
        return result;
    }

    public ObjectData getPersistedTarget() {
        return persistedTarget;
    }

    public ObjectData[] getPersistedParameters() {
        return persistedParameters;
    }

    public ObjectData[] getUpdates() {
        return updatesData;
    }

    public String[] getMessages() {
        return messages;
    }

    public String[] getWarnings() {
        return warnings;
    }
}
