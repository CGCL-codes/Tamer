package ar.com.fdvs.dgarcia.lang.iterators.tree.treeorder;

import java.util.Iterator;

/**
 * Esta interfaz representa un orden de iteracion de los nodos de un arbol
 * 
 * @author D. Garcia
 * @param <N>
 *            Tipo de los nodos
 */
public interface TreeOrder<N> {

    /**
	 * Incluye los nodos pasados como un conjunto para que sean ordenados por esta instancia al
	 * iterar
	 * 
	 * @param nodes
	 *            Conjunto de nodos a agregar
	 */
    public void addNodes(Iterator<N> nodes);

    /**
	 * Devuelve el conjunto de nodos que debe recorrerse
	 * 
	 * @return El iterador actual
	 */
    public Iterator<N> getCurrentNodes();
}
