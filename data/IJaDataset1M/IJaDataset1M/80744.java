package com.hardcode.gdbms.engine.instruction;

/**
 * Adaptador
 *
 * @author Fernando Gonz�lez Cort�s
 */
public class WhereAdapter extends Adapter {

    /**
	 * Obtiene la expresi�n del where
	 *
	 * @return Expression
	 */
    public Expression getExpression() {
        return (Expression) getChilds()[0];
    }
}
