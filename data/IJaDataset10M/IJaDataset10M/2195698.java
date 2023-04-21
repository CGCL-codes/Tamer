package de.jmda.util.jaxb;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;

public class SchemaGenerator {

    /**
	 * Constructor injection.
	 */
    private JAXBContext jaxbContext;

    /**
	 * Maps namespaceURI strings to filename strings. {@link
	 * InternalSchemaOutputResolver} instance will use this map to determine the
	 * schema filename for a given namespaceURI string.
	 *
	 * Constructor injection.
	 */
    private Map<String, String> namespaceURI_schemaFilename;

    private File mostRecentlyGeneratedSchemaFile;

    private Set<File> generatedSchemaFiles = new HashSet<File>();

    public SchemaGenerator(JAXBContext jaxbContext, Map<String, String> namespaceURI_schemaFilename) {
        this.jaxbContext = jaxbContext;
        this.namespaceURI_schemaFilename = namespaceURI_schemaFilename;
    }

    public void generateSchema() throws IOException {
        jaxbContext.generateSchema(new InternalSchemaOutputResolver());
    }

    public File getMostRecentlyGeneratedSchemaFile() {
        return mostRecentlyGeneratedSchemaFile;
    }

    public Set<File> getGeneratedSchemafiles() {
        return generatedSchemaFiles;
    }

    private class InternalSchemaOutputResolver extends SchemaOutputResolver {

        private final Logger LOGGER = Logger.getLogger(InternalSchemaOutputResolver.class);

        @Override
        public Result createOutput(String namespaceURI, String suggestedFilename) throws IOException {
            String schemaFilename = namespaceURI_schemaFilename.get(namespaceURI);
            LOGGER.debug("namespaceURI: " + namespaceURI + ", " + "filename: " + schemaFilename + ", " + "suggestedFilename: " + suggestedFilename);
            if (null == schemaFilename) {
                schemaFilename = suggestedFilename;
            }
            LOGGER.debug("mapped namespaceURI [" + namespaceURI + "] to " + "filename [" + schemaFilename + "]");
            mostRecentlyGeneratedSchemaFile = new File(schemaFilename);
            generatedSchemaFiles.add(mostRecentlyGeneratedSchemaFile);
            return new StreamResult(mostRecentlyGeneratedSchemaFile);
        }
    }
}
