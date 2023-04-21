package plexil;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

public class GlobalContext extends NodeContext {

    protected Map<String, GlobalDeclaration> commands;

    protected Map<String, GlobalDeclaration> lookups;

    protected Map<String, GlobalDeclaration> libraryNodes;

    static GlobalContext s_instance = null;

    static GlobalContext getGlobalContext() {
        if (s_instance == null) s_instance = new GlobalContext();
        return s_instance;
    }

    protected GlobalContext() {
        super(null, "_GLOBAL_CONTEXT_");
        commands = new HashMap<String, GlobalDeclaration>();
        lookups = new HashMap<String, GlobalDeclaration>();
        libraryNodes = new HashMap<String, GlobalDeclaration>();
    }

    public boolean isGlobalContext() {
        return true;
    }

    public GlobalDeclaration getCommandDeclaration(String name) {
        return commands.get(name);
    }

    public boolean isCommandName(String name) {
        GlobalDeclaration ln = getCommandDeclaration(name);
        return (ln != null);
    }

    public void addCommandName(PlexilTreeNode declaration, String name, Vector<VariableName> parm_spec, Vector<VariableName> return_spec) {
        commands.put(name, new GlobalDeclaration(declaration, name, NameType.COMMAND_NAME, parm_spec, return_spec));
    }

    public GlobalDeclaration getLookupDeclaration(String name) {
        return lookups.get(name);
    }

    public boolean isLookupName(String name) {
        GlobalDeclaration ln = getLookupDeclaration(name);
        return (ln != null);
    }

    public void addLookupName(PlexilTreeNode declaration, String name, Vector<VariableName> parm_spec, Vector<VariableName> return_spec) {
        lookups.put(name, new GlobalDeclaration(declaration, name, NameType.STATE_NAME, parm_spec, return_spec));
    }

    public GlobalDeclaration getLibraryNodeDeclaration(String name) {
        return libraryNodes.get(name);
    }

    public boolean isLibraryNodeName(String name) {
        GlobalDeclaration ln = getLibraryNodeDeclaration(name);
        return (ln != null);
    }

    public void addLibraryNode(PlexilTreeNode declaration, String name, Vector<VariableName> parm_spec) {
        libraryNodes.put(name, new GlobalDeclaration(declaration, name, NameType.LIBRARY_NODE_NAME, parm_spec, null));
    }
}

;
