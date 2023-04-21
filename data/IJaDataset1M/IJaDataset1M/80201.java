package abstrasy.pcfx;

import abstrasy.interpreter.InterpreterException;
import abstrasy.Node;
import abstrasy.PCoder;
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
public class PCFx_tuple extends PCFx {

    /**
     * respecte la protection de la finalité des listes.
     *
     */
    public PCFx_tuple() {
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
        int i = 1;
        startAt.isGoodArgsLength(false, 1);
        Node dnode = Node.createEmptyList().letFinal(true);
        while (i < startAt.size()) {
            Node xnode = startAt.getSubNode(i++, Node.VTYPE_VALUABLE);
            if (!xnode.isFinalNode()) {
                xnode = Node.createClone(xnode).letFinal(true);
            }
            dnode.addElement(xnode);
        }
        return dnode;
    }
}
