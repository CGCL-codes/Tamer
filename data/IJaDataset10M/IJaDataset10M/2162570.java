package org.springframework.scheduling.quartz;

import java.io.IOException;
import java.io.InputStream;
import org.quartz.xml.JobSchedulingDataProcessor;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.NestedRuntimeException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

/**
 * Subclass of Quartz' JobSchedulingDataProcessor that considers
 * given filenames as Spring resource locations.
 *
 * @author Juergen Hoeller
 * @since 1.1
 * @see org.springframework.core.io.ResourceLoader
 */
public class ResourceJobSchedulingDataProcessor extends JobSchedulingDataProcessor implements ResourceLoaderAware {

    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = (resourceLoader != null ? resourceLoader : new DefaultResourceLoader());
    }

    protected InputStream getInputStream(String fileName) {
        try {
            return this.resourceLoader.getResource(fileName).getInputStream();
        } catch (IOException ex) {
            throw new JobSchedulingDataInitializationException(ex);
        }
    }

    /**
	 * Exception to be thrown if a resource cannot be loaded.
	 */
    public static class JobSchedulingDataInitializationException extends NestedRuntimeException {

        private JobSchedulingDataInitializationException(IOException ex) {
            super("Could not load job scheduling data XML file", ex);
        }
    }
}
