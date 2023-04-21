package Sintactico.Arbol;

import Visitor.*;
import Contextual.*;

/**
 *
 * @author lidier
 */
public class AST_Exp_TerminalBody_Lista extends AST_Exp_TerminalBody {

    public AST_Exp_TerminalBody N_Exp_Terminal;

    public AST_Exp_TerminalBody extN;

    public AST_Exp_TerminalBody_Lista() {
    }

    public AST_Exp_TerminalBody_Lista(AST_Exp_TerminalBody N_Exp_Terminal, AST_Exp_TerminalBody extN) {
        this.N_Exp_Terminal = N_Exp_Terminal;
        this.extN = extN;
    }

    @Override
    public Object visit(visitor v) {
        return v.visitExp_TerminalBody_Lista(this);
    }

    @Override
    public String revisar(visitor_contextual v, String clase, String tipo_ant) {
        return v.visitExp_TerminalBody_Lista(this, clase, tipo_ant);
    }
}
