package org.easypeas.mappings;

import java.io.IOException;
import java.net.URL;
import org.easypeas.mappings.annotated.AnnotatedMappings;

/**
 * Instantiates the mappings according to the processed URL. <br>
 * Currently only {@link AnnotatedMappings} are defined.
 * 
 * @see AnnotatedMappings
 * @author S Owen
 * 
 */
public class MappingsFactory {

    @SuppressWarnings("unused")
    private static MappingsFactory instance = new MappingsFactory();

    private static URL url;

    private Mappings mappings;

    /**
	 * Instantiates a new MappingsFactory. Private to enforce use of the
	 * getInstance method
	 */
    private MappingsFactory() {
    }

    /**
	 * Gets the single instance of MappingsFactory.
	 * 
	 * @return single instance of MappingsFactory
	 */
    public static MappingsFactory getInstance() {
        return instance;
    }

    /**
	 * Returns a {@link Mappings} implementation, which processes a
	 * configuration file pointed to by the URL.<br>
	 * Currently the only implementation of this is {@link AnnotatedMappings}
	 * 
	 * @param url
	 *            the url
	 * 
	 * @return the mappings
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public Mappings mappings(URL url) throws IOException {
        if (mappings == null || this.url == null || !url.toExternalForm().equals(this.url.toExternalForm())) {
            this.url = url;
            mappings = new AnnotatedMappings(url);
        }
        return mappings;
    }
}
