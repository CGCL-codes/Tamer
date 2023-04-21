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
public class PCFx_is__Even extends PCFx {

    public PCFx_is__Even() {
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
        startAt.isGoodArgsLength(true, 2);
        double n = startAt.getSubNode(1, Node.TYPE_NUMBER).getNumber();
        return new Node(((n / 2) == Math.rint(n / 2)) ? Node.TRUE : Node.FALSE);
    }
}
