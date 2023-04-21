package abstrasy.pcfx;

import abstrasy.interpreter.InterpreterException;
import abstrasy.Node;
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
public class PCFx_atomic_true_static extends PCFx {

    public PCFx_atomic_true_static() {
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
        startAt.isGoodArgsCnt(2);
        Node xnode = startAt.getSubNode(1, Node.TYPE_NUMBER);
        if (xnode.isFinalNode()) {
            throw new InterpreterException(StdErrors.Illegal_access_to_final_value);
        }
        boolean loops = true;
        double nvalue;
        double ovalue2;
        double ovalue = xnode.getNumber();
        while (loops) {
            nvalue = Node.TRUE;
            ovalue2 = xnode.compareAndSwap_number(ovalue, nvalue);
            loops = ovalue2 != ovalue;
            ovalue = ovalue2;
        }
        return null;
    }
}
