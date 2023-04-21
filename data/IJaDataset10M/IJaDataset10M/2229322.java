package org.inqle.data.sampling;

import java.net.URI;
import java.util.Dictionary;
import java.util.List;
import org.inqle.user.data.rdf.RDF;
import org.inqle.user.data.rdf.jena.NamedModel;
import thewebsemantic.Id;
import thewebsemantic.Namespace;

/**
 * Implementations of this interface contain information related to
 * the execution of a Sampling algorithm plus subsequent machine
 * learning experimentation.  ISampler objects are intended to be
 * persistable to RDF.  They are persisted under 3 circumstances:
 *  * They might be saved periodically in the course of executing a 
 *    sampling strategy
 *  * They might be saved as a template for future experimentation, 
 *    as they can serve as a starting point for future learning cycles.
 *  * They might be saved at the end of an experiment, to represent 
 *    the final results of one run through the learning cycle.
 * 
 * In order to be persistable, implementations should follow the Jenabean 
 * conventions:  Here is how you do this:
 *  * Before the class name, add an annotation like this
 *  @Namespace("http://my.base.uri.com/")
 *  
 * The Jenabean engine will assume that all properties adhering to the 
 * Javabean convention will be persistable
 * 
 * @author David Donohue
 * Dec 26, 2007
 */
public interface ISampler {

    public static final String PROPERTY_WEIGHT = "weight";

    public static final String URI_UNKNOWN_SUBJECT = RDF.INQLE + "unknownSubject";

    /**
	 * Get the List of NamedModels which are candidates for sampling.
	 * @return the list of choosable NamedModels
	 */
    public List<NamedModel> getAvailableNamedModels();

    /**
	 * Set the List of NamedModel which are candidates for sampling.
	 * @param the list of choosable NamedModels
	 */
    public void setAvailableNamedModels(List<NamedModel> availableNamedModels);

    /**
	 * Get the List of IDs of NamedModel which have been selected for 
	 * this sampling run.
	 * @return the list of selected NamedModels
	 */
    public List<NamedModel> getSelectedNamedModels();

    /**
	 * Set the List of NamedModel URIs from which to extract data.
	 * @param the list of selected NamedModels
	 */
    public void setSelectedNamedModels(List<NamedModel> selectedNamedModels);

    public void setDataColumns(List<DataColumn> dataColumns);

    public List<DataColumn> getDataColumns();

    public DataColumn getSubjectDataColumn();

    public void setSubjectDataColumn(DataColumn subjectDataColumn);

    public DataColumn getLabelDataColumn();

    public void setLabelDataColumn(DataColumn labelDataColumn);

    public DataTable getResultDataTable();

    public void setResultDataTable(DataTable resultDataTable);

    public Dictionary<?, ?> getProperties();

    /**
	 * Prepare this object for archiving.
	 * Remove any attributes stored in this object, which are not of 
	 * historical value.  This method is called before storing a ISampler object
	 * after experimentation.  Examples of such attributes include
	 * choosable data models,
	 */
    public void removeInterimData();

    /**
	 * Copy all fields from the originalSampler to this sampler
	 * @param originalSampler
	 */
    public void replicate(ISampler originalSampler);

    /**
	 * Copy all fields from the originalSampler to this sampler,
	 * including the ID
	 * @param originalSampler
	 */
    public void clone(ISampler originalSampler);

    @Id
    public String getId();

    public void setProperties(Dictionary<?, ?> properties);
}
