package abstrasy.pcfx;

import abstrasy.Heap;
import abstrasy.Interpreter;
import abstrasy.Node;
import abstrasy.interpreter.InterpreterException;
import abstrasy.interpreter.StdErrors;

/**
 * Abstrasy Interpreter
 * 
 * Copyright : Copyright (c) 2006-2012, Luc Bruninx.
 *
 * Concédée sous licence EUPL, version 1.1 uniquement (la «Licence»).
 * 
 * Vous ne pouvez utiliser la présente oeuvre que conformément à la Licence.
 * Vous pouvez obtenir une copie de la Licence à l’adresse suivante:
 *
 *   http://www.osor.eu/eupl
 *
 * Sauf obligation légale ou contractuelle écrite, le logiciel distribué sous
 * la Licence est distribué "en l’état", SANS GARANTIES OU CONDITIONS QUELLES
 * QU’ELLES SOIENT, expresses ou implicites.
 * 
 * Consultez la Licence pour les autorisations et les restrictions
 * linguistiques spécifiques relevant de la Licence.
 *
 *
 * @author Luc Bruninx
 * @version 1.0
 */
public class PCFx_return extends PCFx {

    public PCFx_return() {
    }

    /**
	 * eval
	 *
	 * @param startAt Node
	 * @return Node
	 * @throws Exception
	 * @todo Implémenter cette méthode abstrasy.PCFx
	 */
    public Node eval(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(1, 2);
        Interpreter interpreter = Interpreter.mySelf();
        if ((!interpreter.isTerminalNode()) || interpreter.getCallerSignature() == 0) {
            throw new InterpreterException(StdErrors.Return_misplaced);
        }
        if (startAt.size() == 2) {
            Node rnode = startAt.getSubNode(1, Node.VTYPE_VALUABLE);
            try {
                Heap.setRETURN(rnode);
            } catch (Exception ex) {
                throw new InterpreterException(StdErrors.Return_register_error);
            }
        }
        if (interpreter.getBreakCode() != Interpreter.BREAKCODE_TAIL) {
            interpreter.setBreakCode(Interpreter.BREAKCODE_RETURN);
        }
        return null;
    }
}
