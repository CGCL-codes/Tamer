package abstrasy.pcfx;

import abstrasy.Node;

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
public class PCFx_quote extends PCFx {

    public PCFx_quote() {
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
        Node xnode = new Node(startAt.getSubNode(1, Node.VTYPE_VALUABLE).secure());
        return xnode.letQuoted(true).letFinal(false);
    }
}
