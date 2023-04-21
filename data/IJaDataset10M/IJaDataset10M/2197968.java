package au.edu.archer.metadata.msf.mss.tool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.eclipse.emf.common.util.URI;
import au.edu.archer.metadata.msf.mss.MetadataSchema;
import au.edu.archer.metadata.msf.mss.Node;
import au.edu.archer.metadata.msf.mss.XMLSchemaProfile;
import au.edu.archer.metadata.msf.mss.util.MSSResourceException;
import au.edu.archer.metadata.msf.mss.util.MSSResourceUtils;
import au.edu.archer.metadata.msf.mss.util.MSSSchemaUtils;
import au.edu.archer.metadata.msf.mss.util.MSSStructureException;

/**
 * Command-line class for the MSS Schema-to-XSD generator.  Note that we only handle
 * MetadataSchemas at the moment, not Models.
 * 
 * @author scrawley@itee.uq.edu.au
 */
public class XSDGeneratorTool {

    private static void error(String message) {
        System.err.println(message);
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            error("Usage: xsdgenerate <schema.mss> | <model.mss>");
        }
        File file = new File(args[0]);
        if (!file.exists()) {
            error("File does not exist: " + file);
        }
        if (!file.isFile()) {
            error("Not a file: " + file);
        }
        URI fileURI = URI.createFileURI(file.getAbsolutePath());
        try {
            Node node = MSSResourceUtils.loadNode(fileURI);
            if (node instanceof MetadataSchema) {
                generateSchema((MetadataSchema) node);
            } else {
                error("Input MSS resource is not a MetadataSchema");
            }
        } catch (MSSResourceException ex) {
            error("MSS resource error: " + ex.getMessage());
        } catch (IOException ex) {
            error("IO error: " + ex.getMessage());
        } catch (Throwable ex) {
            ex.printStackTrace(System.err);
            error("Generator error: " + ex.getLocalizedMessage());
        }
    }

    private static void generateSchema(MetadataSchema schema) throws GenerationException, IOException {
        if (MSSSchemaUtils.needsFlattening(schema)) {
            error("Cannot generate an XSD from an un-flattened Metadata Schema '" + schema.getName() + "'");
        }
        String res;
        XMLSchemaProfile profile = null;
        try {
            profile = MSSSchemaUtils.getXMLSchemaProfile(schema);
            if (profile == null) {
                error("Cannot find an XML Schema profile in Metadata Schema '" + schema.getName() + "'");
            }
            res = new XSDGenerator().generate(profile);
        } catch (MSSStructureException ex) {
            throw new GenerationException(ex.getMessage(), ex);
        }
        File file = new File(MSSResourceUtils.defaultXSDFileName(profile));
        Writer w = null;
        try {
            w = new BufferedWriter(new FileWriter(file));
            w.write(res);
            w.close();
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException ex) {
                }
            }
        }
    }
}
