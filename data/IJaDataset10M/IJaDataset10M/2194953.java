package de.inovox.pipeline.input;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import de.inovox.pipeline.document.ImportObject;

/**
 * Generic Pipeline Input
 * 
 * @author Carsten Burghardt
 * @version $Id: IPipelineInput.java 337 2008-01-29 19:41:04Z carsten $
 */
public interface IPipelineInput {

    /**
     * Init the Input with the current configuration
     * @param cmd
     */
    public void init(Configuration config);

    /**
     * Return true if the input supports the parameters and wants to provide data
     * @param cmd
     * @return true if the module feels responsible
     */
    public boolean isResponsible(Configuration config);

    /**
     * Provide an iterator for the ImportObject objects
     * @return
     */
    public Iterator iterator();

    /**
     * All Options that are supported by the input
     * @return
     */
    public List getSupportedOptions();

    /**
     * Delete the object from the input
     * @param object
     */
    public void delete(ImportObject object);

    /**
     * Clean up the object when all plug-ins are done and
     * no errors occurred
     * Does not delete the object
     * @param object
     */
    public void cleanup(ImportObject object);

    /**
     * Get the total amount of input objects for the current operation
     * @return count or -1 if the total amount is unknown
     */
    public int getTotalInputCount();

    /**
     * Shutdown the input
     */
    public void shutdown();

    /**
     * Get a name for this input module
     * This can be used to select an input module by name
     * @return
     */
    public String getName();
}
