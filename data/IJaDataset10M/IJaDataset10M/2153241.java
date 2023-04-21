package br.usp.ime.lab.math;

public class DevolveMaior {

    /**
	 * Devolve maior numero dos parametros
	 * Devolve Zero caso ambos sejam nulos
	 * @param a
	 * @param b
	 * @return
	 */
    public Double devolveMaior(Double a, Double b) {
        if (a == null && b == null) {
            return new Double(0);
        }
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        if (a > b) return a;
        return b;
    }
}
