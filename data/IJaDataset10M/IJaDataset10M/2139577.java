package astcentric.editor.eclipse.plugin;

import java.util.HashMap;
import java.util.Map;
import astcentric.structure.basic.AST;
import astcentric.structure.basic.id.ASTID;

class ASTStore {

    private final Map<ASTID, AST> _asts = new HashMap<ASTID, AST>();

    void store(AST ast) {
        _asts.put(ast.getInfo().getID(), ast);
    }

    AST retrieve(ASTID id) {
        return _asts.get(id);
    }
}
