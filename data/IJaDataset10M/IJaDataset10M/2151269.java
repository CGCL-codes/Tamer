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
public class PCFx_is__Value extends PCFx {

    public PCFx_is__Value() {
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
        startAt.isGoodArgsCnt(4, 5);
        Node xnode;
        boolean expected = true;
        switch(startAt.size()) {
            case 4:
                startAt.requirePCode(2, PCoder.PC_IN);
                xnode = startAt.getSubNode(3, Node.TYPE_LIST | Node.TYPE_STRING | Node.TYPE_QEXPR | Node.TYPE_QLAZY | Node.TYPE_LAZY);
                break;
            default:
                startAt.requirePCode(2, PCoder.PC_NOT);
                startAt.requirePCode(3, PCoder.PC_IN);
                xnode = startAt.getSubNode(4, Node.TYPE_LIST | Node.TYPE_STRING | Node.TYPE_QEXPR | Node.TYPE_QLAZY | Node.TYPE_LAZY);
                expected = false;
                break;
        }
        int typePossible = 0;
        if (xnode.isVString()) {
            typePossible = Node.TYPE_STRING;
        } else {
            typePossible = Node.VTYPE_EQUALISABLE;
        }
        Node snode = startAt.getSubNode(1, typePossible);
        int startPos = 0;
        int p = -1;
        if (xnode.isNodeType(Node.TYPE_LIST)) {
            int i = startPos;
            while (i < xnode.size()) {
                Node znode = xnode.elementAt(i);
                if (znode.isValidPair()) {
                    znode = Node.getPairValue(znode);
                }
                if (Node.equalsNodes(znode, snode)) {
                    p = i;
                    break;
                }
                i++;
            }
        } else if (xnode.isNodeType(Node.TYPE_STRING)) {
            if (!snode.isVString()) {
                throw new InterpreterException(StdErrors.Argument_type_mismatch);
            }
            p = xnode.getString().indexOf(snode.getString(), startPos);
        } else {
            int i = startPos;
            Node snode2 = Node.quoteEncoded_decode(snode);
            while (i < xnode.size()) {
                Node znode = xnode.elementAt(i);
                if (Node.equalsNodes(znode, snode2)) {
                    p = i;
                    break;
                }
                i++;
            }
        }
        return new Node((p >= 0) == expected ? Node.TRUE : Node.FALSE);
    }
}
