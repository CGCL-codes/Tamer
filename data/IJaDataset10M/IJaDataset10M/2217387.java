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
public class PCFx_remove_static extends PCFx {

    public PCFx_remove_static() {
    }

    private void getRelIndex_for_list(int plageStart, String key, int maxS) throws Exception {
        if ((plageStart < 0) || (plageStart >= maxS)) {
            throw new InterpreterException(StdErrors.extend(StdErrors.Out_of_range, "" + plageStart + (key != null ? ":\"" + key + "\"" : "")));
        }
    }

    private void getRelIndex_for_string(int plageStart, int maxS) throws Exception {
        if ((plageStart < 0) || (plageStart >= maxS)) {
            throw new InterpreterException(StdErrors.extend(StdErrors.Out_of_range, "" + plageStart));
        }
    }

    private final void removeChar_(Node xnode, int index) {
        StringBuffer stmp = new StringBuffer(xnode.getString());
        stmp.delete(index, index + 1);
        xnode.setString(stmp.toString());
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
        startAt.requirePCode(2, PCoder.PC_FROM);
        startAt.requireNodeType(3, Node.TYPE_SYMBOL);
        startAt.isGoodArgsCnt(4);
        Node inode = startAt.getSubNode(1, Node.TYPE_LIST | Node.TYPE_NUMBER | Node.TYPE_STRING);
        Node xnode = startAt.getSubNode(3, Node.VTYPE_LISTABLE_STATIC | Node.VTYPE_STRINGS);
        if (xnode.isFinalNode()) {
            throw new InterpreterException(StdErrors.Illegal_access_to_final_value);
        }
        if (xnode.getQType() == Node.TYPE_LIST) {
            switch(inode.getType()) {
                case Node.TYPE_NUMBER:
                    {
                        int index = (int) inode.getNumber();
                        getRelIndex_for_list(index, null, xnode.size());
                        xnode.removeElementAt(index);
                        break;
                    }
                case Node.TYPE_STRING:
                    {
                        int index = xnode.findPairIndex(inode.getString());
                        getRelIndex_for_list(index, inode.getString(), xnode.size());
                        xnode.removeElementAt(index);
                        break;
                    }
                case Node.TYPE_LIST:
                    {
                        int maxS = xnode.size();
                        for (int i = 0; i < inode.size(); i++) {
                            Node enode = inode.getSubNode(i, Node.TYPE_NUMBER | Node.TYPE_STRING);
                            if (enode.isVNumber()) {
                                int index = (int) enode.getNumber();
                                getRelIndex_for_list(index, null, maxS);
                                xnode.removeElementAt(index - i);
                            } else if (inode.isVString()) {
                                int index = xnode.findPairIndex(inode.getString());
                                getRelIndex_for_list(index, inode.getString(), xnode.size());
                                xnode.removeElementAt(index);
                            }
                        }
                        break;
                    }
                default:
                    throw new InterpreterException(StdErrors.Internal_error);
            }
        } else if (xnode.getType() == Node.TYPE_STRING) {
            inode.requireNodeType(Node.TYPE_LIST | Node.TYPE_NUMBER);
            if (inode.getType() == Node.TYPE_NUMBER) {
                int index = (int) inode.getNumber();
                getRelIndex_for_string(index, xnode.getString().length());
                removeChar_(xnode, index);
            } else if (inode.getType() == Node.TYPE_LIST) {
                int maxS = xnode.getString().length();
                for (int i = 0; i < inode.size(); i++) {
                    Node enode = inode.getSubNode(i, Node.TYPE_NUMBER);
                    int index = (int) enode.getNumber();
                    getRelIndex_for_string(index, maxS);
                    removeChar_(xnode, index - i);
                }
            }
        } else {
            switch(inode.getType()) {
                case Node.TYPE_NUMBER:
                    {
                        int index = (int) inode.getNumber();
                        getRelIndex_for_list(index, null, xnode.size());
                        xnode.removeElementAt(index);
                        break;
                    }
                case Node.TYPE_LIST:
                    {
                        int maxS = xnode.size();
                        for (int i = 0; i < inode.size(); i++) {
                            Node enode = inode.getSubNode(i, Node.TYPE_NUMBER);
                            int index = (int) enode.getNumber();
                            getRelIndex_for_list(index, null, maxS);
                            xnode.removeElementAt(index - i);
                        }
                        break;
                    }
                default:
                    throw new InterpreterException(StdErrors.Internal_error);
            }
        }
        return null;
    }
}
