package relaxngcc.datatype;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import relaxngcc.NGCCGrammar;

/**
 * "Resource" is a file used by datatype conversion routine.
 * Typically, this is a utility Java class.
 * 
 * @author
 *      Kohsuke Kawaguchi (kk@kohsuke.org)
 */
class Resource {

    private final DatatypeLibraryManager owner;

    /**
     * Resource name.
     */
    private final String name;

    /**
     * Contents of the resource.
     */
    private final Macro contents;

    /**
     * A flag for avoiding duplicate generation.
     * True if this resource has already been used.
     */
    private boolean used = false;

    Resource(DatatypeLibraryManager _owner, String _name, Macro _contents) {
        this.owner = _owner;
        this.name = _name;
        this.contents = _contents;
    }

    /**
     * Called by the datatype conversion routine to indicate
     * that this resource is in use for the given gramamr.
     */
    public void use(NGCCGrammar grammar) throws IOException, NoDefinitionException {
        if (!used) {
            FileWriter fos = new FileWriter(new File(owner.options.targetdir, name));
            Map resourceMacroDefs = new HashMap();
            resourceMacroDefs.put("packageDecl", getPackageDecl(grammar));
            fos.write(contents.toString(resourceMacroDefs));
            fos.close();
        }
        used = true;
    }

    public String getPackageDecl(NGCCGrammar grammar) {
        if (grammar.packageName.length() == 0) return ""; else return "package " + grammar.packageName + ";";
    }
}
