package de.maramuse.soundcomp.shaping;

import de.maramuse.soundcomp.process.NamedSource;
import de.maramuse.soundcomp.process.ParameterMap;
import de.maramuse.soundcomp.process.ProcessElement;
import de.maramuse.soundcomp.process.SourceStore;
import de.maramuse.soundcomp.process.StandardParameters;
import de.maramuse.soundcomp.process.Stateful;
import de.maramuse.soundcomp.process.TypeMismatchException;
import de.maramuse.soundcomp.process.UnknownConnectionException;
import de.maramuse.soundcomp.process.ValueType;
import de.maramuse.soundcomp.process.StandardParameters.Parameter;
import de.maramuse.soundcomp.util.ReadOnlyMap;
import de.maramuse.soundcomp.util.ReadOnlyMapImpl;
import de.maramuse.soundcomp.util.NativeObjects;

public class Third implements ProcessElement, Stateful {

    public long nativeSpace = -1;

    public Third() {
        NativeObjects.registerNativeObject(this);
    }

    Third(boolean s) {
    }

    private static ReadOnlyMapImpl<Integer, ValueType> destMap = new ReadOnlyMapImpl<Integer, ValueType>(), sourceMap = new ReadOnlyMapImpl<Integer, ValueType>();

    static {
        sourceMap.put(StandardParameters.OUT.i, ValueType.STREAM);
        destMap.put(StandardParameters.IN.i, ValueType.STREAM);
        destMap.put(StandardParameters.NONLINEARITY.i, ValueType.STREAM);
    }

    private final ReadOnlyMapImpl<Integer, SourceStore> sourceStoreMap = new ReadOnlyMapImpl<Integer, SourceStore>();

    private static ParameterMap inputsMap = new ParameterMap();

    private static ParameterMap outputsMap = new ParameterMap();

    static {
        inputsMap.put(StandardParameters.NONLINEARITY);
        inputsMap.put(StandardParameters.IN);
        outputsMap.put(StandardParameters.OUT);
    }

    SourceStore nonlin, in;

    double nonlinval, inval, outval = 0d, _outval = 0d;

    String instanceName, abstractName;

    @Override
    public ReadOnlyMap<Integer, ValueType> getDestinationTypes() {
        return destMap;
    }

    @Override
    public void setSource(int connectionIndex, NamedSource source, int sourceIndex) throws UnknownConnectionException, TypeMismatchException {
        SourceStore ss = new SourceStore(source, sourceIndex);
        sourceStoreMap.put(connectionIndex, ss);
        if (!source.getSourceTypes().containsKey(sourceIndex)) throw new UnknownConnectionException("attempt to set invalid signal shaping source");
        if (connectionIndex == StandardParameters.IN.i) {
            in = ss;
        } else if (connectionIndex == StandardParameters.NONLINEARITY.i) {
            nonlin = ss;
        } else throw new UnknownConnectionException("attempt to provide unkown signal shaping parameter");
    }

    @Override
    public ReadOnlyMap<Integer, ValueType> getSourceTypes() {
        return sourceMap;
    }

    @Override
    public double getValue(int index) {
        return outval;
    }

    @Override
    public void advanceState() {
        nonlinval = Math.abs(nonlin.getValue());
        if (nonlinval > 1.0) nonlinval = 1.0;
        inval = in.getValue();
        if (inval < -1.0) inval = -1.0; else if (inval > 1.0) inval = 1.0;
        double a = 2 * nonlinval + 1d;
        double b = ((nonlinval < 0.25) ? (a - 1) : (4d / 27d * a * a * a));
        _outval = a * inval - b * inval * inval * inval;
    }

    @Override
    public void advanceOutput() {
        outval = _outval;
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    @Override
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    @Override
    public String getAbstractName() {
        return abstractName;
    }

    @Override
    public void setAbstractName(String abstractName) {
        this.abstractName = abstractName;
    }

    @Override
    public long getNativeSpace() {
        return nativeSpace;
    }

    @Override
    public ReadOnlyMap<Integer, SourceStore> getSourceMap() {
        return sourceStoreMap;
    }

    /**
   * @see de.maramuse.soundcomp.process.ProcessElement#clone()
   */
    @Override
    public Third clone() {
        Third c = new Third();
        c.abstractName = abstractName;
        return c;
    }

    @Override
    public ReadOnlyMap<String, Parameter> outputsByName() {
        return outputsMap;
    }

    @Override
    public ReadOnlyMap<String, Parameter> inputsByName() {
        return inputsMap;
    }
}
