package es.phoneixs.prymd5.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class FileSeccion implements Serializable, SeccionInterface {

    private int comienzo, fin;

    private ArrayList<int[]> datos;

    /**
     * Crea una sección que solo tiene una línea de datos.
     * 
     * @param comienzo
     *                El posición de comienzo de donde se sacan los datos.
     * @param fin
     *                La posición donde terminan los datos.
     * @param datos
     *                Los datos de la sección.
     */
    public FileSeccion(int comienzo, int fin, int[] datos) {
        this.comienzo = comienzo;
        this.fin = fin;
        this.datos = new ArrayList<int[]>(1);
        this.datos.add(datos);
    }

    /**
     * Crea una sección que solo tiene una línea de datos.
     * 
     * @param comienzo
     *                El posición de comienzo de donde se sacan los datos.
     * @param fin
     *                La posición donde terminan los datos.
     * @param datos
     *                Los datos de la primera línea de datos de la sección.
     * @param datos2
     *                Los datos de la sgunda línea de datos de la sección.
     */
    public FileSeccion(int comienzo, int fin, int[] datos, int[] datos2) {
        this.comienzo = comienzo;
        this.fin = fin;
        this.datos = new ArrayList<int[]>(2);
        this.datos.add(datos);
        this.datos.add(datos2);
    }

    /**
     * Crea una sección que solo tiene una línea de datos.
     * 
     * @param comienzo
     *                El posición de comienzo de donde se sacan los datos.
     * @param fin
     *                La posición donde terminan los datos.
     * @param datos
     *                Los datos de los distintos hilos en formato de array.
     * @throws NullPointerException
     *                 Si el array de datos es null.
     */
    public FileSeccion(int comienzo, int fin, ArrayList<int[]> datos) throws NullPointerException {
        if (datos == null) {
            throw new NullPointerException("El parámetro datos no debe ser null.");
        }
        this.comienzo = comienzo;
        this.fin = fin;
        this.datos = datos;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SeccionInterface) {
            SeccionInterface s = (SeccionInterface) obj;
            return this.comienzo == s.getComienzo() && this.fin == s.getFin() && this.datosEquals(this.getDatos(), s.getDatos());
        } else {
            return false;
        }
    }

    private boolean datosEquals(int[][] datos2, int[][] datos3) {
        if (datos2.length != datos3.length) {
            return false;
        }
        int i = 0;
        boolean eq = true;
        while (i < datos2.length && eq) {
            eq = Arrays.equals(datos2[i], datos3[i]);
            i++;
        }
        return eq;
    }

    public int getLongitud() {
        return this.fin - this.comienzo;
    }

    public int getComienzo() {
        return this.comienzo;
    }

    public int getFin() {
        return this.fin;
    }

    @Override
    public String toString() {
        return "[" + this.comienzo + "-" + this.fin + ")";
    }

    public int[][] getDatos() {
        int[][] a = new int[this.datos.size()][];
        return this.datos.toArray(a);
    }

    public byte[][] getDatosByte() {
        byte[][] a = new byte[this.datos.size()][];
        int i = 0;
        for (int[] bs : this.datos) {
            a[i] = new byte[bs.length];
            for (int j = 0; j < bs.length; j++) {
                int entero = bs[j];
                a[i][j] = (byte) (entero);
            }
            i++;
        }
        return a;
    }

    public boolean isMultiple() {
        return this.datos.size() > 1;
    }

    public int getNumEbras() {
        return this.datos.size();
    }
}
