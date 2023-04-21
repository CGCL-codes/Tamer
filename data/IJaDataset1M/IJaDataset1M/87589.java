package au.edu.archer.metadata.mde.schema;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.log4j.Logger;
import au.edu.archer.metadata.msf.mss.util.MSSResourceUtils;
import au.edu.archer.metadata.spi.SchemaRepository;
import au.edu.archer.metadata.spi.SchemaRepositoryException;

/**
 * A base class for SchemaRepository adapters
 *
 * @author scrawley@itee.uq.edu.au
 */
public abstract class AbstractSchemaRepository implements SchemaRepository {

    protected static final String HTTP_PROTOCOL = "http";

    protected static final String HTTPS_PROTOCOL = "https";

    protected static final String FILE_PROTOCOL = "file";

    protected static final String MSS_EXTN = MSSResourceUtils.MSS_EXTENSION;

    protected static final String XSD_EXTN = MSSResourceUtils.XSD_EXTENSION;

    protected static final String FILE_PREFIX = FILE_PROTOCOL + ":";

    protected final Logger logger = Logger.getLogger(AbstractSchemaRepository.class);

    /**
     * Remove a nominated extension from a name, if present.
     * 
     * @param name the name to be processed.
     * @param extn the extension (suffix) to remove.
     * @return the name with the nominated extension removed
     */
    protected String removeExtension(String name, String extn) {
        if (name.endsWith(extn)) {
            name = name.substring(0, name.length() - extn.length());
        }
        return name;
    }

    /**
     * Read the text of a schema from a local file into a supplied buffer.
     * The 'sysname' string is not used to find the schema here.  We simply
     * use the supplied 'file'.
     * 
     * @param sysname the notional system name
     * @param file the schema file
     * @throws SchemaRepositoryException
     */
    protected Schema readLocalSchema(String sysname, File file) throws SchemaRepositoryException {
        try {
            StringBuilder sb = new StringBuilder((int) file.length());
            BufferedReader in = new BufferedReader(new FileReader(file));
            String tmp = null;
            while ((tmp = in.readLine()) != null) {
                sb.append(tmp).append("\n");
            }
            in.close();
            logger.debug("Read schema for '" + sysname + "' from '" + file + "': got " + sb.length() + " characters.");
            return new Schema(sysname, sb.toString(), 0L);
        } catch (IOException ex) {
            throw new SchemaRepositoryException(sysname + ": Error loading schema: " + ex.getMessage(), ex);
        }
    }
}
