package soluciones.practi03;

import java.util.ArrayList;
import java.util.List;
import problemas.ProblemaViajante;
import soluciones.EstrategiaSolucion;
import esquemas.EsquemaVoraz;

/**
 * Clase que da solucin al Problema del Viajante mediante la tcnica
 * voraz, extendiendo la clase abstracta EsquemaVZ. El objetivo de
 * esta clase es recorrer todos los nodos del grafo con un
 * recorrido mnimo. Implementa la interfaz <code>EstrategiaSolucion</code>
 * para que las soluciones puedan ser medidas; por tanto han de
 * implementarse los mtodos de dicha interfaz,
 * <code>procesamientoInicial()</code>,
 * <code>procesamientoFinal()</code> y <code>solucion()</code>.
 * 
 * @version 2.2, 17/10/2008
 * @see EstrategiaSolucion
 * @see EstrategiaSolucion#procesamientoInicial()
 * @see EstrategiaSolucion#procesamientoFinal()
 * @see EstrategiaSolucion#solucion()
 */
public class ViajanteVoraz extends EsquemaVoraz implements EstrategiaSolucion {

    /**
     * Recuerda los visitados.
     */
    private boolean[] visitado;

    /**
     * Almacena la distancia mnima.
     */
    private int distancia;

    /**
     * Almacena los nodos por los que va pasando.
     */
    private List<Integer> camino;

    /**
     * La ciudad por la que se empieza.
     */
    private int origen;

    /**
     * Ciudad de la que se parte en cada paso.
     */
    private int ciudadActual;

    /**
     * Ciudad a la que se llega en cada paso.
     */
    private int ciudadSiguiente;

    /**
     * Nmero de nodos que se llevan recorridos.
     */
    private int recorridos;

    /**
     * El grafo que representa los caminos entre las ciudades. 
	 */
    private int[][] grafo;

    /**
	 * Carga en el atributo privado <code>grafo</code> la matriz
	 * pasada en el problema y establece la ciudad de origen.
	 */
    private int tamaño;

    /**
	 * Tamaño del problema
	 * @param g
	 * @param origen
	 */
    public ViajanteVoraz(ProblemaViajante g, int origen) {
        grafo = g.getGrafo();
        this.origen = origen;
    }

    public ViajanteVoraz(ProblemaViajante g) {
        this(g, 0);
    }

    /**
     * Devuelve una cadena con la distancia recorrida y el camino
     */
    public String toString() {
        return "Solución al problema del viajante mediante la técnica voraz\n\t" + "Distancia: " + distancia + "\n\tCamino: " + camino;
    }

    /**
     * Inicializa el vector visitado, y los atributos recorridos,
     * ciudadActual, distancia y camino
     */
    protected void inicializa() {
        tamaño = grafo[0].length;
        visitado = new boolean[tamaño];
        camino = new ArrayList<Integer>();
        visitado[origen] = true;
        ciudadActual = origen;
        camino.add(ciudadActual);
        distancia = 0;
        recorridos = 1;
    }

    /**
     * Indica si el problema ha terminado, es decir, si se han recorrido
     * todas las ciudades. En caso afirmativo, hay que llevar al viajero
     * de nuevo a la ciudad origen.
     */
    protected boolean fin() {
        if (recorridos >= tamaño) {
            distancia += grafo[ciudadActual][origen];
            camino.add(origen);
            return true;
        }
        return false;
    }

    /**
	 * Selecciona el candidato que est ms cerca del nodo origen, y lo marca
	 * como candidato destino. Cuando selecciona el candidato lo elimina de
	 * entre los futuros posibles.
	 */
    protected void seleccionaYElimina() {
        int distancia = Integer.MAX_VALUE;
        for (int i = 0; i < tamaño; i++) {
            if (!visitado[i]) {
                if (distancia > grafo[ciudadActual][i]) {
                    distancia = grafo[ciudadActual][i];
                    ciudadSiguiente = i;
                }
            }
        }
        visitado[ciudadSiguiente] = true;
        recorridos++;
    }

    /**
	 * Devuelve si el candidato seleccionado nos llevar a una solucin correcta.
	 */
    protected boolean prometedor() {
        return true;
    }

    /**
	 * Modifica los atributos distancia, camino y ciudadActual.
	 */
    protected void anotaEnSolucion() {
        distancia += grafo[ciudadActual][ciudadSiguiente];
        camino.add(ciudadSiguiente);
        ciudadActual = ciudadSiguiente;
    }

    /**
	 * Sin cdigo.
	 */
    public void procesamientoInicial() {
    }

    /**
	 * Llama al mtodo que resuelve el problema mediante una tcnica voraz.
	 */
    public void solucion() {
        voraz();
    }

    /**
	 * Sin cdigo.
	 */
    public void procesamientoFinal() {
    }
}
