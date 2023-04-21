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
public class PCFx_count extends PCFx {

    public PCFx_count() {
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
        startAt.requirePCode(2, PCoder.PC_IN);
        startAt.isGoodArgsCnt(4);
        Node xnode = startAt.getSubNode(3, Node.VTYPE_STRINGS | Node.VTYPE_LISTABLE);
        int typePossible = 0;
        if (xnode.getType() == Node.TYPE_STRING) {
            typePossible = Node.VTYPE_STRINGS;
        } else {
            typePossible = Node.VTYPE_EQUALISABLE;
        }
        Node snode = startAt.getSubNode(1, typePossible);
        int res = 0;
        int p = -1;
        if (xnode.getQType() == Node.TYPE_LIST) {
            int i = 0;
            Node znode = null;
            while (i < xnode.size()) {
                znode = xnode.elementAt(i);
                if (znode.isValidPair()) {
                    znode = Node.getPairValue(znode);
                }
                if (Node.equalsNodes(znode, snode)) {
                    p = i;
                    res++;
                }
                i++;
            }
            return new Node(res);
        } else if (xnode.getType() == Node.TYPE_STRING) {
            snode.requireNodeType(Node.VTYPE_STRINGS);
            p = 0;
            String x = xnode.getString();
            String s = snode.getString();
            if (x.length() > 0 && s.length() > 0) {
                while (p >= 0) {
                    p = x.indexOf(s, p);
                    if (p >= 0) {
                        res++;
                        p += s.length();
                    }
                }
            }
            return new Node(res);
        } else {
            int i = 0;
            Node znode = null;
            Node snode2 = Node.quoteEncoded_decode(snode);
            while (i < xnode.size()) {
                znode = xnode.elementAt(i);
                if (Node.equalsNodes(znode, snode2)) {
                    res++;
                }
                i++;
            }
            return new Node(res);
        }
    }
}
