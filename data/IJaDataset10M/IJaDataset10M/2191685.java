package at.ac.arcs.itt.yau.libs.timeseries.api;

/**
 * Create new instances of various timeseries types.
 * There might be different implementations returning different implementations 
 * of the requested objects.
 * This instances of TimeSeriesFactory can be provided to generic implementations 
 * which in turn uses these Factory to create TimeSeries objects.
 * @author peter
 */
public interface TimeSeriesFactory {

    /**
     * Create a new empty instance.
     * The concrete implementation depends on the used TimeSeriesFactory.
     * @param clazz Class to be used in <code>AValue&lt;clazz&gt;</code> stored in the TimeSeries.
     * Keep in mind that in the moment (java6) no generics can be used as parameter. 
     * This means it is NOT possible to create TimeSeries&lt;Collection&lt;String&gt;&gt; with this factory!
     * @return A new instance
     */
    public <T> TimeSeries<T> newTimeSeries(Class<T> clazz);

    /**
     * Create a new empty instance.
     * The concrete implementation depends on the used TimeSeriesFactory.
     * @param clazz Class to be used in <code>AValue&lt;clazz&gt;</code> stored in the TimeSeries.
     * Keep in mind that in the moment (java6) no generics can be used as parameter.
     * This means it is NOT possible to create TimeSeries&lt;Collection&lt;String&gt;&gt; with this factory!
     * @return A new instance
     */
    @Deprecated
    public <T> InterpolatingTimeSeries<T> newInterpolatingTimeSeries(Class<T> clazz);
}
