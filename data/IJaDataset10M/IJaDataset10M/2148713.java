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
public class PCFx_TEMP extends PCFx {

    private int myPCode = PCoder.PC_ERR;

    public void setPCode(int pCode) {
        this.myPCode = pCode;
    }

    public int getPCode() {
        return this.myPCode;
    }

    public PCFx_TEMP() {
    }

    public PCFx_TEMP(int pCode) {
        setPCode(pCode);
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
        throw new InterpreterException(StdErrors.extend(StdErrors.Syntax_error, PCoder.getKey(myPCode)));
    }
}
