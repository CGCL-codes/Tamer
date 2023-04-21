package PaqTADLineales.PaqCola;

import PaqElemento.*;
import PaqTADVacioException.*;
import PaqTADNodo.*;

public class Cola_Dinamica implements Cola, Cloneable {

    private Nodo NodoCabeza;

    private Nodo NodoFinal;

    public int longitud;

    public Cola_Dinamica() {
        NodoCabeza = null;
        NodoFinal = null;
        longitud = 0;
    }

    public boolean EsVacia() {
        return (NodoCabeza == null);
    }

    public void EnCola(Elemento x) {
        Nodo Nuevo;
        if (x != null) {
            Nuevo = new Nodo(x, null);
            if (NodoCabeza == null) NodoCabeza = Nuevo; else NodoFinal.Siguiente = Nuevo;
            NodoFinal = Nuevo;
        }
        longitud++;
    }

    public Elemento Cabeza() throws TADVacioException {
        if (NodoCabeza == null) throw new TADVacioException(); else return NodoCabeza.Info;
    }

    public void Resto() throws TADVacioException {
        if (NodoCabeza == null) throw new TADVacioException(); else NodoCabeza = NodoCabeza.Siguiente;
        if (NodoCabeza == null) NodoFinal = null;
        longitud--;
    }

    public Object clone() {
        Cola_Dinamica c1 = null;
        try {
            c1.longitud = longitud;
        } catch (NullPointerException exc) {
        }
        Nodo aux1, aux2;
        try {
            c1 = (Cola_Dinamica) super.clone();
        } catch (CloneNotSupportedException e) {
        }
        if (!EsVacia()) {
            c1.NodoCabeza = (Nodo) NodoCabeza.clone();
            if (NodoCabeza == NodoFinal) c1.NodoFinal = c1.NodoCabeza; else {
                c1.NodoFinal = (Nodo) NodoFinal.clone();
                aux1 = c1.NodoCabeza;
                aux2 = NodoCabeza.Siguiente;
                while (aux2 != NodoFinal) {
                    aux1.Siguiente = (Nodo) aux2.clone();
                    aux2 = aux2.Siguiente;
                    aux1 = aux1.Siguiente;
                }
                aux1.Siguiente = c1.NodoFinal;
            }
        }
        return c1;
    }

    public String toString() {
        String s = new String("[");
        Nodo actual = NodoCabeza;
        while (actual != null) {
            s += actual.Info.toString();
            actual = actual.Siguiente;
            if (actual != null) s += ", ";
        }
        s += "]";
        return s;
    }

    public int Ultimo() {
        try {
            Elemento a = NodoFinal.Info;
            if (this.EsVacia()) return -1; else return ((Entero) a).Dato();
        } catch (NullPointerException exc) {
        }
        return -1;
    }

    public int Longitud() {
        return longitud;
    }

    public boolean Iguales(Cola c) {
        Nodo aux = NodoCabeza;
        Cola clon = (Cola) c.clone();
        if (Longitud() != clon.Longitud()) return false; else {
            try {
                while (!clon.EsVacia()) {
                    if (!aux.Info.Iguales(clon.Cabeza())) return false; else {
                        aux = aux.Siguiente;
                        clon.Resto();
                    }
                }
            } catch (TADVacioException exc) {
            }
        }
        return true;
    }

    public void EnCola(int vector[]) {
        for (int i = 0; i < vector.length; i++) this.EnCola(new Entero(vector[i]));
    }
}
