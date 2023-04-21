package abstrasy.pcfx;

import abstrasy.Heap;
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
public class PCFx_meta_symbol extends PCFx {

    public PCFx_meta_symbol() {
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
        Node snode = startAt.getSubNode(1, Node.TYPE_STRING | Node.TYPE_QSYMBOL);
        String str = snode.getString();
        if (str.indexOf('#') >= 0) {
            str = Node.replaceCSeq(str, "#", Heap.createSymbolReg());
        }
        if (Node.isStringSymbol(str)) {
            Node pnode = Node.createSymbol(str);
            pnode.setQuoted(true);
            return pnode;
        } else {
            throw new InterpreterException(StdErrors.extend(StdErrors.Invalid_symbol, snode.getString()));
        }
    }
}
