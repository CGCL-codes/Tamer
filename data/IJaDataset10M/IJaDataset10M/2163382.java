package Sintactico.Arbol;

import Visitor.*;
import Contextual.*;

/**
 *
 * @author lidier
 */
public class AST_ExpSimpl_Negacion extends AST_ExpSimpl {

    public AST_Exp N_Exp;

    public AST_ExpSimpl_Negacion() {
    }

    @Override
    public Object visit(visitor v) {
        return v.visitExpSimpl_Negacion(this);
    }

    @Override
    public String revisar(visitor_contextual v, String clase) {
        return v.visitExpSimpl_Negacion(this, clase);
    }
}
