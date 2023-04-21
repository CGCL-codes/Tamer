package de.maramuse.soundcomp.process;

import de.maramuse.soundcomp.process.StandardParameters.Parameter;
import de.maramuse.soundcomp.process.TimerMap.Event;
import de.maramuse.soundcomp.util.AdvancerRegistry;
import de.maramuse.soundcomp.util.ReadOnlyMap;
import de.maramuse.soundcomp.util.ReadOnlyMapImpl;

public class EventMonoFlop implements ProcessElement, Stateful {

    private AdvancerRegistry advancerRegistry;

    private String abstractName;

    private String instanceName;

    private long nativeSpace = -1L;

    private boolean state = false;

    private boolean state_int = false;

    private NamedSource source = null;

    private int sourceIndex = 0;

    private Event lastSwitchOffEvent = null;

    private static final ReadOnlyMapImpl<Integer, ValueType> sourceTypes = new ReadOnlyMapImpl<Integer, ValueType>();

    private static final ReadOnlyMapImpl<Integer, ValueType> destMap = new ReadOnlyMapImpl<Integer, ValueType>();

    private static ParameterMap inputsMap = new ParameterMap();

    private static ParameterMap outputsMap = new ParameterMap();

    static {
        inputsMap.put(StandardParameters.IN);
        outputsMap.put(StandardParameters.OUT);
    }

    static {
        sourceTypes.put(StandardParameters.OUT.i, ValueType.STREAM);
        destMap.put(StandardParameters.IN.i, ValueType.STREAM);
    }

    private EventMonoFlop() {
    }

    public Event getOnEvent() {
        return new Event() {

            @Override
            public void notifyElement(ProcessElement processElement) {
                double d = 0.0;
                synchronized (EventMonoFlop.this) {
                    if (lastSwitchOffEvent != null) advancerRegistry.removeEvent(lastSwitchOffEvent);
                    if (source != null) d = source.getValue(sourceIndex);
                    if (d > 0d) {
                        EventMonoFlop.this.setOn();
                        advancerRegistry.addEvent(advancerRegistry.currentTime() + d, lastSwitchOffEvent = getOffEvent(), EventMonoFlop.this);
                    }
                }
            }
        };
    }

    public Event getOffEvent() {
        return new Event() {

            @Override
            public void notifyElement(ProcessElement processElement) {
                synchronized (EventMonoFlop.this) {
                    EventMonoFlop.this.setOff();
                    if (lastSwitchOffEvent == this) lastSwitchOffEvent = null;
                }
            }
        };
    }

    @Override
    public ReadOnlyMap<Integer, ValueType> getDestinationTypes() {
        return destMap;
    }

    @Override
    public void setSource(int connectionIndex, NamedSource source, int sourceIndex) throws UnknownConnectionException, TypeMismatchException {
        if (connectionIndex != StandardParameters.IN.i) throw new UnknownConnectionException("EventMonoFlop only has standard input");
        if (source == null) {
            this.source = null;
            state_int = false;
            return;
        }
        ValueType vt = source.getSourceTypes().get(sourceIndex);
        if (vt == null) throw new UnknownConnectionException("EventMonoFlop failed to connect");
        if (vt != ValueType.DOUBLE && vt != ValueType.STREAM) throw new TypeMismatchException();
        this.source = source;
        this.sourceIndex = sourceIndex;
    }

    @Override
    public ReadOnlyMap<Integer, ValueType> getSourceTypes() {
        return sourceTypes;
    }

    @Override
    public double getValue(int index) {
        return state ? 1d : 0d;
    }

    @Override
    public void advanceOutput() {
        state = state_int;
    }

    @Override
    public void advanceState() {
    }

    @Override
    public String getAbstractName() {
        return abstractName;
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    @Override
    public void setAbstractName(String abstractName) {
        this.abstractName = abstractName;
    }

    @Override
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    @Override
    public long getNativeSpace() {
        return nativeSpace;
    }

    @Override
    public EventMonoFlop clone() {
        EventMonoFlop n = new EventMonoFlop();
        n.advancerRegistry = advancerRegistry;
        n.state = state;
        n.state_int = state_int;
        return n;
    }

    private void setOn() {
        state_int = true;
    }

    private void setOff() {
        state_int = false;
    }

    @Override
    public ReadOnlyMap<Integer, SourceStore> getSourceMap() {
        return ReadOnlyMapImpl.emptyMapIntSource;
    }

    @Override
    public ReadOnlyMap<String, Parameter> outputsByName() {
        return outputsMap;
    }

    @Override
    public ReadOnlyMap<String, Parameter> inputsByName() {
        return inputsMap;
    }

    /**
   * An event monoflop wants to register and unregister events. therefore, it needs
   * access to the advancerRegistry of the current compilation (as this is no longer
   * global).
   * @param advancerRegistry the AdvancerRegistry of the current compilation
   */
    public void setAdvancerRegistry(AdvancerRegistry advancerRegistry) {
        this.advancerRegistry = advancerRegistry;
    }
}
