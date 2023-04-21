package tj;

import java.util.*;
import june.tree.*;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.*;

public class JuneTree extends CommonTree {

    /**
	 * The enclosing block of code that defines the scope for this node.
	 */
    public JuneTree block;

    /**
	 * The set of possible entities to which this node might refer, such as the
	 * variable, method, or class.
	 */
    public Set<Object> entities;

    public Set<String> imports;

    /**
	 * If this is a block, then non-null, a map from IDs to nodes.
	 */
    public Map<String, Set<JuneTree>> symbols;

    /**
	 * TODO Use this instead of 'symbols'. Really? It won't cover labels.
	 */
    public JuneClass blockClass;

    /**
	 * TODO Need expected types and given types as separate lists (and dependent
	 * types for overloaded potential method matches?).
	 */
    public Class<?> type;

    public JuneTree(Token payload) {
        super(payload);
    }

    public void addEntity(Object entity) {
        if (entities == null) {
            entities = new HashSet<Object>();
        }
        Log.info("addEntity " + entity);
        entities.add(entity);
    }

    public void addSymbol(String id, JuneTree node) {
        Set<JuneTree> nodes = symbols.get(id);
        if (nodes == null) {
            nodes = new HashSet<JuneTree>();
            symbols.put(id, nodes);
        }
        nodes.add(node);
    }

    @SuppressWarnings("unchecked")
    public Iterable<JuneTree> getChildren() {
        return children == null ? Collections.emptyList() : children;
    }

    public JuneType getEntityType() {
        return null;
    }

    public void initScript() {
        imports = new HashSet<String>(Arrays.asList("java.lang", "java.lang.System"));
    }

    public boolean isBlock() {
        return symbols != null;
    }
}
