package annone.engine.defs;

import annone.engine.Visibility;

public class ClassDef extends InheritableComponentDef {

    public ClassDef(String qualifiedName, ComponentDef inheritsDef, Visibility visibility) {
        super(qualifiedName, inheritsDef, visibility);
    }
}
