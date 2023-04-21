package de.maramuse.soundcomp.generator;

import java.util.Map;
import java.util.TreeMap;
import de.maramuse.soundcomp.process.NamedSource;
import de.maramuse.soundcomp.process.ParameterMap;
import de.maramuse.soundcomp.process.ProcessElement;
import de.maramuse.soundcomp.process.SourceStore;
import de.maramuse.soundcomp.process.StandardParameters;
import de.maramuse.soundcomp.process.TypeMismatchException;
import de.maramuse.soundcomp.process.UnknownConnectionException;
import de.maramuse.soundcomp.process.ValueType;
import de.maramuse.soundcomp.process.StandardParameters.Parameter;
import de.maramuse.soundcomp.util.NativeObjects;
import de.maramuse.soundcomp.util.ReadOnlyMap;

public class SawTooth extends UnitySawTooth {

    public SawTooth() {
        NativeObjects.registerNativeObject(this);
    }

    SawTooth(boolean s) {
    }

    private static final Map<Integer, ValueType> destTypeMap = new TreeMap<Integer, ValueType>();

    static {
        destTypeMap.put(StandardParameters.FREQUENCY.i, ValueType.STREAM);
        destTypeMap.put(StandardParameters.DUTYCYCLE.i, ValueType.STREAM);
        destTypeMap.put(StandardParameters.SYNC.i, ValueType.STREAM);
        destTypeMap.put(StandardParameters.SYNCPHASE.i, ValueType.STREAM);
        destTypeMap.put(StandardParameters.PHASEOFFSET.i, ValueType.STREAM);
    }

    protected static ParameterMap inputsMap = new ParameterMap();

    private static ParameterMap outputsMap = new ParameterMap();

    static {
        inputsMap.put(StandardParameters.FREQUENCY);
        inputsMap.put(StandardParameters.SYNC);
        inputsMap.put(StandardParameters.SYNCPHASE);
        inputsMap.put(StandardParameters.DUTYCYCLE);
        inputsMap.put(StandardParameters.PHASEOFFSET);
        outputsMap.put(StandardParameters.OUT);
    }

    protected double dutycycle = 0;

    protected double phaseoffset = 0;

    protected double effectivePhase;

    protected SourceStore dc = null;

    protected SourceStore po = null;

    @Override
    public void setSource(int connection, NamedSource source, int sourceIndex) throws UnknownConnectionException, TypeMismatchException {
        if (connection == StandardParameters.DUTYCYCLE.i) {
            if (source.getSourceTypes().containsKey(sourceIndex)) {
                dc = new SourceStore(source, sourceIndex);
            } else throw new UnknownConnectionException("source for SawTooth dutycycle unknown");
        } else if (connection == StandardParameters.PHASEOFFSET.i) {
            if (source.getSourceTypes().containsKey(sourceIndex)) {
                po = new SourceStore(source, sourceIndex);
            } else throw new UnknownConnectionException("source for SawTooth phaseoffset unknown");
        } else super.setSource(connection, source, sourceIndex);
    }

    @Override
    public void advanceState() {
        super.advanceState();
        if (dc != null) dutycycle = dc.getValue();
        if (po != null) phaseoffset = po.getValue();
        effectivePhase = phase + phaseoffset - Math.floor(phase + phaseoffset);
        _val = (effectivePhase < dutycycle ? (2 * effectivePhase / dutycycle - 1) : (2 * (1 - effectivePhase) / (1 - dutycycle) - 1));
    }

    /**
   * @see ProcessElement#clone()
   */
    @Override
    public SawTooth clone() {
        return new SawTooth();
    }

    @Override
    public ReadOnlyMap<String, Parameter> inputsByName() {
        return inputsMap;
    }

    @Override
    public ReadOnlyMap<String, Parameter> outputsByName() {
        return outputsMap;
    }
}
