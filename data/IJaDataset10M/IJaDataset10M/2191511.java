package de.inovox.pipeline.input;

/**
 * Interface for input modules that want to be called regularly by the framework
 * The interval can be set globally via configuration
 * 
 * @author Carsten Burghardt
 * @version $Id: ICallableUpdateInput.java 377 2008-02-10 22:35:00Z carsten $
 */
public interface ICallableUpdateInput extends IPipelineInput {

    /**
     * Return true if an update is available
     * @param lastCall the timestamp of the last time the input was called or 0 for the first call
     * @return
     */
    public boolean isUpdateAvailable(long lastCall);

    /**
     * Return true if the input should be called in future
     * @return
     */
    public boolean isContinueCalling();
}
