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
public class PCFx_merge extends PCFx {

    /**
     * respecte la protection de la finalité des listes.
     *
     */
    public PCFx_merge() {
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
        startAt.isGoodArgsLength(false, 3);
        Node first = startAt.getSubNode(i++, Node.VTYPE_LISTABLE | Node.VTYPE_STRINGS | Node.TYPE_QSYMBOL);
        int vtype = first.getQType();
        int ntype = first.getType();
        boolean quoted = first.isQuoted();
        Node dnode2 = new Node(first);
        while (i < startAt.size()) {
            Node xnode = startAt.getSubNode(i++, vtype);
            if (xnode.isQuoted() != quoted) {
                throw new InterpreterException(StdErrors.Argument_quote_mismatch);
            }
            if (ntype == Node.TYPE_LIST && !quoted) {
                for (int iii = 0; iii < xnode.size(); ) {
                    Node enode = new Node(xnode.elementAt(iii++).secure());
                    dnode2.addElement(enode);
                    if (enode.isCircularList()) {
                        throw new InterpreterException(StdErrors.Illegal_circulare_definition);
                    }
                }
            } else if (ntype == Node.TYPE_STRING) {
                dnode2.setString(dnode2.getString() + xnode.getString());
            } else if (ntype == Node.TYPE_SYMBOL) {
                dnode2.setString(dnode2.getString() + PCoder.SEP + xnode.getString());
            } else {
                for (int iii = 0; iii < xnode.size(); ) {
                    dnode2.addElement(new Node(xnode.elementAt(iii++).secure()));
                }
                if (dnode2.isCircularList()) {
                    throw new InterpreterException(StdErrors.Illegal_circulare_definition);
                }
            }
        }
        return dnode2;
    }
}
