package poker;

import java.util.Comparator;

/**
 *
 * @author jose
 */
public class Carta implements Comparator {

    int palo;

    int valor;

    /**
     * Creates a new instance of Carta
     */
    public Carta(int palo, int valor) {
        this.palo = palo;
        this.valor = valor;
    }

    public static Carta devolverCarta(int p, int v) {
        Carta c = new Carta(p, v);
        return c;
    }

    public void mostrarCarta() {
        System.out.println(palo + " " + valor);
    }

    public int getValor() {
        return valor;
    }

    public int getPalo() {
        return palo;
    }

    public String toString() {
        String temp = "";
        if (valor == 14) return temp = "As";
        if (valor == 2) return temp = "Dos";
        if (valor == 3) return temp = "Tres";
        if (valor == 4) return temp = "Cuatro";
        if (valor == 5) return temp = "Cinco";
        if (valor == 6) return temp = "Seis";
        if (valor == 7) return temp = "Siete";
        if (valor == 8) return temp = "Ocho";
        if (valor == 9) return temp = "Nueve";
        if (valor == 10) return temp = "Diez";
        if (valor == 11) return temp = "Sota";
        if (valor == 12) return temp = "Caballo";
        if (valor == 13) return temp = "Rey";
        temp = temp + " de ";
        if (palo == 1) return temp = temp + "Treboles";
        if (palo == 2) return temp = temp + "Picas";
        if (palo == 3) return temp = temp + "Rombos";
        if (palo == 4) return temp = temp + "Corazones";
        return temp;
    }

    public int compare(Object o1, Object o2) {
        Carta a = (Carta) o1;
        Carta b = (Carta) o2;
        return ((a.getValor() + a.getPalo() * 100) - (b.getValor() + b.getPalo() * 100));
    }
}
