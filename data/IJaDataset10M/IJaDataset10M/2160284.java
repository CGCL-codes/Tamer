package btree;

import bplustree.ElementoIndice;
import treeoutput.TreeOutput;
import treeoutput.TreeOutputList;

public interface IBTree {

    /**
	 * Inserta el dato dato
	 * si el dato ya existe en el arbol no hace nada
	 * @param dato
	 */
    public abstract TreeOutputList insert(String dato);

    /**
	 * Inserta el dato dato
	 * si el dato ya existe en el arbol no hace nada
	 * @param dato
	 */
    public abstract TreeOutputList insert(String dato, double value, float peso, int max_frecuencia);

    /**
	 * Elimina el parametro dato del arbol
	 * Si no lo encuentra no
	 * @param dato
	 */
    public abstract TreeOutputList delete(String dato);

    /**
	 * Busca el dato dato en el arbol
	 * Si el dato existe devuelve el dato
	 * Si el dato no existe devuelve null
	 * @param dato
	 * @return
	 */
    public abstract String search(String dato);

    /**
	 * Imprime en pantalla la lista ordenada de elementos del arbol
	 *
	 */
    public abstract void printlist();

    /**
	 * Imprime en pantalla el arbol con vista por niveles
	 *
	 */
    public abstract void printview();

    /**
	 * Devuelve el TreeOutput correspondiente al arbol recorrido primero en profundidad con mensaje standard 
	 * @return
	 */
    public abstract TreeOutput output();

    /**
	 * Devuelve el TreeOutput correspondiente al arbol recorrido primero en profundidad con mensaje custom 
	 * @return
	 */
    public abstract TreeOutput output(String mensaje);

    /**
	 * Devuelve el TreeOutput correspondiente al arbol recorrido primero a lo ancho con mensaje standard 
	 * @return
	 */
    public abstract TreeOutput outputBF();

    /**
	 * Devuelve el TreeOutput correspondiente al arbol recorrido primero a lo ancho con mensaje custom 
	 * @return
	 */
    public abstract TreeOutput outputBF(String mensaje);

    /**
	 * resetea los nodos activos poniendolos como inactivos. Para uso privado en el paso a paso luego de insertar un paso
	 *
	 */
    abstract void resetActive();

    public abstract boolean buscar(String key, double valor, float peso, int max_frecuencia);

    public abstract boolean buscar(ElementoIndice elemBusco);
}
