package au.edu.archer.metadata.msf.mss.tool;

import java.io.File;
import java.io.IOException;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.Diagnostician;
import au.edu.archer.metadata.msf.mss.MetadataSchema;
import au.edu.archer.metadata.msf.mss.Model;
import au.edu.archer.metadata.msf.mss.NamedNode;
import au.edu.archer.metadata.msf.mss.Node;
import au.edu.archer.metadata.msf.mss.SchemaProfile;
import au.edu.archer.metadata.msf.mss.util.MSSResourceUtils;
import au.edu.archer.metadata.msf.mss.util.MSSSchemaUtils;
import au.edu.archer.metadata.msf.mss.util.MSSStructureException;

/**
 * Command line class for the Schema Profile creation / update tool
 * 
 * @author scrawley@itee.uq.edu.au
 */
public class SchemaProfileTool {

    private boolean debug = false;

    private boolean validate = true;

    private boolean stdout = false;

    private boolean owl = false;

    private boolean xml = false;

    private boolean create = false;

    private boolean update = false;

    private boolean replace = false;

    private String schemaName = null;

    private boolean allSchemas = false;

    private File resourceFile;

    private static void usage() {
        System.err.println("Usage: schemaprofiletool [--debug] [--schema <name> | --allschemas] " + "       (--xml | --owl) (--create | --update | --replace) <input.mss>");
    }

    public static void main(String[] args) {
        SchemaProfileTool tool = new SchemaProfileTool();
        try {
            tool.run(args);
        } catch (Throwable ex) {
            tool.error("Fatal error", ex);
        }
    }

    private void run(String[] args) {
        int firstArg;
        int i;
        for (i = 0; i < args.length; i++) {
            String opt = args[i];
            if (!opt.startsWith("-")) {
                break;
            } else if (opt.equals("--help")) {
                usage();
                System.err.println("Creates or updates a schema profile for a MetadataSchema in <input.mss>.");
                System.err.println("If the operation succeeds, the <input.mss> resource will be updated.");
                System.err.println("     --create        a new profile is created.");
                System.err.println("     --update        an existing profile is updated.");
                System.err.println("     --replace       an existing profile is replaced.");
                System.err.println("     --schema <name> selects the schema by name.");
                System.err.println("     --allschemas    apply operation to all schemas in a Model.");
                System.err.println("     --xml           the target is an XML Schema profile.");
                System.err.println("     --owl           the target is an OWL profile.");
                System.err.println("     --stdout        write updated updated resource to standard output.");
                System.err.println("     --debug         enable debugging and stacktraces.");
                System.err.println("     --novalidate    turns off validation of the output schema.");
                return;
            } else if (opt.equals("--debug")) {
                debug = true;
            } else if (opt.equals("--create")) {
                create = true;
            } else if (opt.equals("--update")) {
                update = true;
            } else if (opt.equals("--replace")) {
                replace = true;
            } else if (opt.equals("--xml")) {
                xml = true;
            } else if (opt.equals("--owl")) {
                owl = true;
            } else if (opt.equals("--novalidate")) {
                validate = false;
            } else if (opt.equals("--stdout")) {
                stdout = true;
            } else if (opt.equals("--allschemas")) {
                allSchemas = true;
            } else if (opt.equals("--schema")) {
                if (++i >= args.length) {
                    usageError("no schema name");
                }
                schemaName = args[i];
            } else if (opt.equals("--")) {
                i++;
                break;
            } else {
                usageError("unknown option '" + opt + "'");
            }
        }
        firstArg = i;
        int opCount = (create ? 1 : 0) + (update ? 1 : 0) + (replace ? 1 : 0);
        if (opCount != 1) {
            usageError("one (and only one) of --create, --replace or --update required");
        }
        if (xml == owl) {
            if (xml) {
                usageError("--owl and --xml are mutually exclusive");
            } else {
                usageError("--owl or --xml must be specified");
            }
        }
        if (allSchemas && schemaName != null) {
            usageError("--allschemas and --schema cannot be used together");
        }
        if (firstArg >= args.length) {
            usageError("missing <input.mss> argument");
        }
        if (firstArg < args.length - 1) {
            usageError("too many arguments");
        }
        Node sourceNode = null;
        resourceFile = new File(args[firstArg]);
        try {
            URI uri = URI.createFileURI(resourceFile.getAbsolutePath());
            sourceNode = MSSResourceUtils.loadNode(uri);
        } catch (IOException ex) {
            error("Unexpected exception while loading '" + resourceFile + "'", ex);
        }
        ISchemaProfileCreator<?> isc = owl ? new OWLProfileCreator() : xml ? new XMLSchemaProfileCreator() : null;
        if (isc == null) {
            error("Profile operations supported for this kind of profile");
        }
        try {
            MetadataSchema schema = null;
            Model model = null;
            if (sourceNode instanceof MetadataSchema) {
                schema = (MetadataSchema) sourceNode;
                if (schemaName != null && !schemaName.equals(schema.getName())) {
                    error("Schema name in MSS resource is '" + schema.getName() + "'");
                }
            } else if (sourceNode instanceof Model) {
                model = (Model) sourceNode;
                if (!allSchemas) {
                    schema = MSSSchemaUtils.lookupSchema(model, schemaName);
                    if (schema == null) {
                        error("No schema '" + schemaName + "' found in the Model in the MSS resource");
                    }
                }
            } else {
                error("Profile operations on this kind of MSS resource");
            }
            if (schema != null) {
                doOperation(schema, isc);
            } else {
                for (NamedNode child : model.getContents()) {
                    if (child instanceof MetadataSchema) {
                        doOperation((MetadataSchema) child, isc);
                    }
                }
            }
            output(sourceNode);
        } catch (IOException ex) {
            error("Problem saving MSS resource", ex);
        } catch (ProfileCreatorException ex) {
            error("Problem creating or updating " + isc.getProfileTypeName(), ex);
        } catch (MSSStructureException ex) {
            error("Problem locating MetadataSchema or " + isc.getProfileTypeName(), ex);
        }
    }

    /**
     * Apply an create/update/replace operation to a schema's profile.
     * @param schema the schema
     * @param isc the schema creator handle
     * @throws ProfileCreatorException
     * @throws MSSStructureException
     */
    private void doOperation(MetadataSchema schema, ISchemaProfileCreator<?> isc) throws ProfileCreatorException, MSSStructureException {
        if (create) {
            if (isc.hasProfiles(schema)) {
                error("Schema already has " + isc.getProfileTypeName() + "(s)");
            } else {
                schema.getProfiles().add(isc.createProfile(schema));
            }
        } else if (replace) {
            SchemaProfile oldProfile = isc.getProfile(schema);
            SchemaProfile newProfile = isc.createProfile(schema);
            schema.getProfiles().remove(oldProfile);
            schema.getProfiles().add(newProfile);
        } else {
            SchemaProfile profile = isc.getProfile(schema);
            isc.updateProfile(profile);
        }
    }

    private void output(Node node) throws IOException {
        if (validate) {
            Diagnostician diagnostician = new Diagnostician();
            Diagnostic diagnostic = diagnostician.validate(node);
            if (diagnostic.getSeverity() >= Diagnostic.ERROR) {
                System.err.println("Output file contains validation errors: " + "use the schema editor's 'Validate' function to view them.");
            }
        }
        if (stdout) {
            MSSResourceUtils.saveToStream(node, System.out);
        } else {
            MSSResourceUtils.saveAsResource(node, resourceFile, true);
        }
    }

    private void error(String message, Throwable ex) {
        if (debug) {
            ex.printStackTrace();
        }
        System.err.println(message + ": " + ex.getMessage());
        System.exit(1);
    }

    private void usageError(String message) {
        System.err.println(message);
        usage();
        System.exit(1);
    }

    private void error(String message) {
        System.err.println(message);
        System.exit(1);
    }
}
