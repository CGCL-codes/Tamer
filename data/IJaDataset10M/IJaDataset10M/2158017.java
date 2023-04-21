package repast.simphony.data.logging.gather;

import repast.simphony.data.logging.outputter.Outputter;
import repast.simphony.data.logging.StreamContainer;
import repast.simphony.engine.schedule.ScheduleParameters;

/**
 * A registry that simplifies the building of the logging framework by automatically building
 * gatherers as needed based on data mappings and their associated schedule times, object sources,
 * and data sets; along with providing utility methods for working with the framework.
 * 
 * @author Jerry Vos
 */
public interface BuildingLoggingRegistry extends LoggingRegistry {

    /**
	 * Adds a mapping to the registry. This will build a new DataGatherer for the mapping or return
	 * an equivalent one based on the ObjectsSource, ScheduleParameters, and data sets given.
	 * 
	 * @param columnName
	 *            the column name for the mapping
	 * @param objectsSource
	 *            the source of objects for which to apply the mapping to
	 * @param mapping
	 *            the mapping to add
	 * @param whenToGatherData
	 *            info on when to apply this mapping
	 * @param dataSets
	 *            the data sets this mapping applies to
	 * 
	 * @return a DataGatherer that contains the given mapping (and possibly others), along with the
	 *         other information passed in to this method
	 */
    public <T> DataGatherer<T> add(String columnName, DataMapping<?, T> mapping, ScheduleParameters scheduleParams, DataObjectSource<T> objectsSource, Object... dataSets);

    /**
	 * Links together a mapping and an outputter. This is performed by using a data set. This data
	 * set is added to both the Data Gathererers that use the specified and the specified outputter
	 * a data set. Also, if the Outputter is a {@link StreamContainer} the outputter's column will
	 * be added to the outputter.<p/>
	 * 
	 * The mapping and the outputter must already be in this registry.<p/>
	 * 
	 * Note: this is a convenience method, it does <em>not</em> directly link the outputter to the
	 * mapping. The only gatherers that will be linked to the outputter are those already contained
	 * in this registry.
	 * 
	 * @param mapping
	 *            a mapping that has already been added
	 * @param outputter
	 *            an outputter to link to the mapping
	 */
    public void bind(DataMapping<?, ?> mapping, Outputter outputter);
}
