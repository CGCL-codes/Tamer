package com.cellulargames.model;

public class Carta {

    public int valor;

    public int figura;

    public boolean hold;

    public static final int J = 11;

    public static final int Q = 12;

    public static final int K = 13;

    public static final int A = 14;

    public Carta() {
    }

    public Carta(int valor, int figura) {
        super();
        this.valor = valor;
        this.figura = figura;
    }

    /**
	 * 
	 * @Override toString()
	 */
    public String toString() {
        return "[ v:" + valor + ", f: " + Figura.toString(figura) + " h:" + hold + " ]";
    }

    /**
	 * 
	 * @Override equals()
	 */
    public boolean equals(Object c) {
        return this.valor == ((Carta) c).valor;
    }
}
