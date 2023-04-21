package net.jautomock.core.session;

import java.lang.reflect.AccessibleObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.jaoplib.annotations.NonNullArgs;
import net.jautomock.core.model.MethodCall;
import net.jautomock.core.model.MethodCallResult;
import net.jautomock.core.session.datasource.DataSource4SerializedMocks;
import net.jautomock.core.session.serializer.MockSerializer;
import net.jautomock.objectmapper.ObjectMapper;
import net.jautomock.util.JAutoMockEqualsBuilder;

/**
 * <p>
 * A class representing a single mocking session where we record or playback
 * mocks.
 * </p>
 * <p>
 * The communication with the data source and the serialization/deserialization
 * of objects is performed here.
 * </p>
 * 
 * @author Philipp Kumar
 */
public class MockingSession {

    private final DataSource4SerializedMocks mockDataSource;

    private final MockSerializer mockSerializer;

    private final Map<MethodCall, MethodCallResult> callResults;

    private final Map<MethodKey, Integer> invocationSequenceNumbers = new HashMap<MethodKey, Integer>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Creates a new mocking session with the given mock data source and the
     * given serializer. The data source is checked for existing mocks, these
     * are loaded if existent.
     */
    @NonNullArgs
    public MockingSession(DataSource4SerializedMocks dataSource, MockSerializer serializer) {
        this.mockDataSource = dataSource;
        this.mockSerializer = serializer;
        String serializedMocks = dataSource.retrieveSerializedMocks();
        if (serializedMocks.trim().isEmpty()) {
            this.callResults = new HashMap<MethodCall, MethodCallResult>();
        } else {
            this.callResults = deserializeMocks(serializedMocks);
        }
    }

    /**
     * Assigns a sequence number to the given call identified by calling class
     * and target method/constructor and then records the return value.
     */
    @NonNullArgs
    public void record(Class<?> caller, AccessibleObject target, Object[] parameterValues, MethodCallResult callResult) {
        MethodKey methodKey = new MethodKey(caller, target, parameterValues);
        Integer sequenceNumber = getNextSequenceNumber(methodKey);
        putReturnValue(new MethodCall(caller, target, parameterValues, sequenceNumber), callResult);
    }

    /**
     * Returns the recorded object for the given call identified by calling
     * class and target method. When called a second time with the same
     * arguments, the call with the next sequence number is returned.
     */
    @NonNullArgs
    public MethodCallResult playback(Class<?> caller, AccessibleObject target, Object[] parameterValues) {
        MethodKey methodKey = new MethodKey(caller, target, parameterValues);
        int sequenceNumber = getNextSequenceNumber(methodKey);
        MethodCall call = new MethodCall(caller, target, parameterValues, sequenceNumber);
        return getReturnValue(call);
    }

    @NonNullArgs
    public void updateArguments(Object[] arguments, MethodCallResult result) {
        List<Object> recordedArguments = result.getParameterValues();
        for (int i = 0; i < arguments.length; i++) {
            Object argument = arguments[i];
            Object recordedArgument = recordedArguments.get(i);
            this.objectMapper.map(recordedArgument, argument);
        }
    }

    /**
     * Use this objects serializer to serialize the given map.
     */
    private String serializeMocks(Map<MethodCall, MethodCallResult> returnValues) {
        return this.mockSerializer.serialize(returnValues);
    }

    /**
     * Use this objects serializer to deserialize the given String into a map.
     */
    @SuppressWarnings("unchecked")
    private Map<MethodCall, MethodCallResult> deserializeMocks(String serializedMocks) {
        return (Map<MethodCall, MethodCallResult>) this.mockSerializer.deserialize(serializedMocks);
    }

    private MethodCallResult getReturnValue(MethodCall call) {
        return this.callResults.get(call);
    }

    private void putReturnValue(MethodCall call, MethodCallResult callResult) {
        this.callResults.put(call, callResult);
    }

    private int getNextSequenceNumber(MethodKey methodKey) {
        Integer sequenceNumber = this.invocationSequenceNumbers.get(methodKey);
        if (sequenceNumber == null) {
            sequenceNumber = 0;
        }
        this.invocationSequenceNumbers.put(methodKey, sequenceNumber + 1);
        return sequenceNumber + 1;
    }

    public void resetSequenceNumbers() {
        this.invocationSequenceNumbers.clear();
    }

    public boolean mocksPresent() {
        return this.callResults.size() > 0;
    }

    public void saveAllMocks() {
        this.mockDataSource.saveSerializedMocks(serializeMocks(this.callResults));
    }

    public void purgeMocks() {
        this.mockDataSource.purge();
    }

    private static class MethodKey {

        private final Class<?> caller;

        private final AccessibleObject target;

        private final List<Object> argumentValues;

        @NonNullArgs
        public MethodKey(Class<?> caller, AccessibleObject target, Object[] argumentValues) {
            this.caller = caller;
            this.target = target;
            this.argumentValues = Arrays.asList(argumentValues);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof MethodKey)) {
                return false;
            }
            MethodKey key = (MethodKey) obj;
            return new JAutoMockEqualsBuilder().append(this.caller, key.caller).append(this.target, key.target).append(this.argumentValues, key.argumentValues).isEquals();
        }

        @Override
        public int hashCode() {
            return 1;
        }
    }
}
