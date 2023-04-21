package compresor;

/**
 * Representa un nodo del �rbol de Huffman, que se implementa sobre un vector (por eso el nodo no tiene
 * "punteros" en el sentido cl�sico).
 * 
 * @author Ing. Valerio Frittelli
 * @version Octubre de 2004
 */
public class NodoHuffman {

    private int frecuencia;

    private int padre;

    private boolean esIzquierdo;

    private int izq, der;

    /**
     *  Constructor por defecto. Pone padre, izq y der en -1, y los demas en su valor por default
     */
    public NodoHuffman() {
        padre = -1;
        izq = der = -1;
    }

    /**
     *  Constructor. Inicializa los atributos seg�n los par�metros
     */
    public NodoHuffman(int f, int p, boolean e, int i, int d) {
        frecuencia = f;
        padre = p;
        esIzquierdo = e;
        izq = i;
        der = d;
    }

    /**
     *  Acceso a la frecuencia
     *  @return el valor de la frecuencia
     */
    public int getFrecuencia() {
        return frecuencia;
    }

    /**
     *  Cambia el valor de la frecuencia
     *  @param x nueva frecuencia
     */
    public void setFrecuencia(int x) {
        frecuencia = x;
    }

    /**
     *  Acceso al indice del padre
     *  @return el indice del padre
     */
    public int getPadre() {
        return padre;
    }

    /**
     *  Cambia el valor del indice del padre
     *  @param x nuevo indice del padre
     */
    public void setPadre(int x) {
        padre = x;
    }

    /**
     *  Acceso al indicador de hijo izquierdo o no
     *  @return el valor del flag de hijo izquierdo (true: es hijo izquierdo - false: es hijo derecho)
     */
    public boolean isLeft() {
        return esIzquierdo;
    }

    /**
     *  Cambia el flag indicador de hijo izquierdo
     *  @param x nuevo valor del flag
     */
    void setLeft(boolean x) {
        esIzquierdo = x;
    }

    /**
     *  Acceso al indice del hijo izquierdo
     *  @return el indice del hijo izquierdo
     */
    public int getIzquierdo() {
        return izq;
    }

    /**
     *  Cambia el valor del indice del hijo izquierdo
     *  @param x nuevo indice del hijo izquierdo
     */
    public void setIzquierdo(int x) {
        izq = x;
    }

    /**
     *  Acceso al indice del hijo derecho
     *  @return el indice del hijo derecho
     */
    public int getDerecho() {
        return der;
    }

    /**
     *  Cambia el valor del indice del hijo derecho
     *  @param x nuevo indice del hijo derecho
     */
    public void setDerecho(int x) {
        der = x;
    }

    @Override
    public String toString() {
        return "Frecuencia: " + frecuencia + "Izquierdo?: " + esIzquierdo;
    }
}
