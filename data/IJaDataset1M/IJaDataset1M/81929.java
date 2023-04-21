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
public class PCFx_insert_static extends PCFx {

    /**
     * respecte la protection de la finalité des listes
     * 
     */
    public PCFx_insert_static() {
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
        startAt.requirePCode(4, PCoder.PC_AT);
        startAt.isGoodArgsCnt(6);
        Node snode = null;
        Node xnode = null;
        int index = 0;
        startAt.requireNodeType(3, Node.TYPE_SYMBOL);
        snode = startAt.getSubNode(3, Node.VTYPE_STRINGS | Node.VTYPE_LISTABLE_STATIC);
        if (snode.isFinalNode()) {
            throw new InterpreterException(StdErrors.Illegal_access_to_final_value);
        }
        xnode = startAt.getSubNode(5, Node.TYPE_NUMBER);
        index = (int) Math.round(xnode.getNumber());
        Node nouvNode = new Node(startAt.getSubNode(1, (snode.isNodeType(Node.TYPE_STRING) ? Node.TYPE_STRING : Node.VTYPE_VALUABLE)).secure());
        int ssize;
        if (snode.isVString()) {
            ssize = snode.getString().length();
        } else {
            ssize = snode.size();
        }
        if (index >= 0) {
            if (index > ssize) {
                throw new InterpreterException(StdErrors.extend(StdErrors.Out_of_range, "" + index));
            } else {
                if (snode.getQType() == Node.TYPE_LIST) {
                    if (nouvNode.isValidPair()) {
                        throw new InterpreterException(StdErrors.Argument_type_mismatch);
                    }
                    if (index < ssize) {
                        snode.insertElementAt(Node.secure(nouvNode), index);
                        if (snode.isCircularList()) {
                            throw new InterpreterException(StdErrors.Illegal_circulare_definition);
                        }
                    } else {
                        snode.addElement(Node.secure(nouvNode));
                        if (snode.isCircularList()) {
                            throw new InterpreterException(StdErrors.Illegal_circulare_definition);
                        }
                    }
                } else if (snode.getType() == Node.TYPE_STRING) {
                    String sn = snode.getString();
                    if (index == 0) {
                        snode.setString(nouvNode.getString() + sn);
                    } else if (index == ssize) {
                        snode.setString(sn + nouvNode.getString());
                    } else {
                        String ss = sn.substring(0, index);
                        String se = sn.substring(index, sn.length());
                        snode.setString(ss + nouvNode.getString() + se);
                    }
                } else {
                    if (index < ssize) {
                        snode.insertElementAt(Node.quoteEncoded_decode(nouvNode), index);
                        if (snode.isCircularList()) {
                            throw new InterpreterException(StdErrors.Illegal_circulare_definition);
                        }
                    } else {
                        snode.addElement(Node.quoteEncoded_decode(nouvNode));
                        if (snode.isCircularList()) {
                            throw new InterpreterException(StdErrors.Illegal_circulare_definition);
                        }
                    }
                }
            }
        } else {
            throw new InterpreterException(StdErrors.extend(StdErrors.Out_of_range, "" + index));
        }
        return null;
    }
}
