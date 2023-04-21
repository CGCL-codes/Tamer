package rete;

import java.util.*;

/**
 * Una condici�n individual en el antecedente de una regla. Contiene signo (nada o negativa para decir
 * que es una afirmaci�n o negaci�n), identificador, atributo y valor. Cada uno es un Campo, que puede
 * ser una variable o constante. Admite subcondiciones.
 * @author agustin
 *
 */
public class Condition {

    public String signo;

    public Campo identificador;

    public Campo atributo;

    public Campo valor;

    private LinkedList<Condition> subcondiciones;

    public Condition(String signo, Campo identificador, Campo atributo, Campo valor) {
        this.signo = signo;
        this.identificador = identificador;
        this.atributo = atributo;
        this.valor = valor;
        this.subcondiciones = new LinkedList<Condition>();
    }

    public LinkedList<Condition> DarListaSubcondiciones() {
        return subcondiciones;
    }
}
