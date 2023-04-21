package domain.model;

import domain.model.numbers.Number;

/**
 * Operador de Mayor o Igual.
 */
public class Higher implements Relationship {

    private static Higher instance = new Higher();

    /**
     * Obtiene la �nica instancia necesaria disponible en tiempo de ejecuci�n.
     * @return Objeto instanciado de esta clase.
     */
    public static Higher getInstance() {
        return instance;
    }

    public boolean fulfil(Number a, Number b) {
        return a.higherEquals(b);
    }

    public String getString() {
        return ">=";
    }

    public Relationship getOpposite() {
        return Lower.getInstance();
    }
}
