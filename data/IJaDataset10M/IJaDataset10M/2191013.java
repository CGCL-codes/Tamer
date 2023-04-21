package Sintactico.Arbol;

import Visitor.*;
import Contextual.*;

/**
 *
 * @author lidier
 */
public class AST_TypeName_Simple extends AST_TypeName {

    public String id;

    public AST_TypeName_Simple() {
    }

    public Object visit(visitor v) {
        return v.visitTypeName_Simple(this);
    }

    public void revisar(visitor_contextual v) {
        v.visitTypeName_Simple(this);
    }
}
