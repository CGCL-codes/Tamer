package knn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Lista de valores sorteados no KNN.
 * @author Samir Coutinho Costa <samirfor@gmail.com>
 */
public class Lista {

    private ArrayList<Valor> valor;

    public Lista() {
        valor = new ArrayList<Valor>();
    }

    public Lista(ArrayList<Valor> valor) {
        this.valor = valor;
    }

    public ArrayList<Valor> getValor() {
        return valor;
    }

    public void setValor(ArrayList<Valor> valor) {
        this.valor = valor;
    }

    public int size() {
        return valor.size();
    }

    public void add(Valor v) {
        valor.add(v);
    }

    public void ordenar() {
        Collections.sort(valor, new Comparator<Valor>() {

            @Override
            public int compare(Valor v1, Valor v2) {
                if (v1.getDistancia() < v2.getDistancia()) {
                    return -1;
                }
                if (v1.getDistancia() > v2.getDistancia()) {
                    return 1;
                }
                return 0;
            }
        });
    }

    @Override
    public String toString() {
        String s;
        s = "Lista: ";
        for (int i = 0; i < size(); i++) {
            s += "\nv[" + i + "] = " + valor.get(i);
        }
        return s;
    }
}
