package edu.cast.teseg.modelo;

/**
 *
 * @author edgarin
 */
public class ExtraerMin {

    public ExtraerMin() {
    }

    public static int getExtraerMin(int[] distancia, boolean[] marca) {
        int minimo = 999;
        int vertice = 0;
        for (int i = 1; i < distancia.length; i++) {
            if (minimo > distancia[i] && !marca[i]) {
                minimo = distancia[i];
                vertice = i;
            }
        }
        return vertice;
    }
}
