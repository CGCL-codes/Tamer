package org.sam.elementos;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

/**
 * Clase que implementa una caché de objetos.</br>
 * Los objetos que aquí almacenados deberán implementar el interface {@code PrototipoCacheable<T>}.
 * Esto permite: tanto reutilizar las antiguas instancias almacenadas, inicializandolas de nuevo,
 * como generar nuevas instancias, a partir del prototipo almacenado, cuando no quedan más.
 * 
 * @param <T> Tipo genérico de datos almacenados en la caché.
 */
public class Cache<T extends PrototipoCacheable<T>> {

    @SuppressWarnings("serial")
    private static class PrototipoDesconocidoException extends RuntimeException {

        PrototipoDesconocidoException(String str) {
            super(str);
        }
    }

    private static class Nodo<S> {

        PrototipoCacheable<S> prototipo;

        Queue<S> instancias;

        Nodo(PrototipoCacheable<S> prototipo) {
            this.prototipo = prototipo;
            this.instancias = new LinkedList<S>();
        }
    }

    private int elementos;

    private int tam;

    private int tamMax;

    private Map<Integer, Nodo<T>> prototipos;

    private static Comparator<Integer> COMPARADOR = new Comparator<Integer>() {

        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    };

    /**
	 * Constructor que genera una {@code Cache} con una capacidad determinada.
	 * @param tam valor de la capacidad deseada de la {@code Cache}.
	 */
    public Cache(int tam) {
        this.prototipos = new TreeMap<Integer, Nodo<T>>(COMPARADOR);
        this.elementos = 0;
        this.tam = tam;
        this.tamMax = (tam * 125) / 100;
    }

    /**
	 * Método que registra un {@code PrototipoCacheable<T>} en la {@code Cache}.
	 * @param prototipo registrado.
	 */
    public void addPrototipo(PrototipoCacheable<T> prototipo) {
        prototipos.put(prototipo.hashCode(), new Nodo<T>(prototipo));
    }

    /**
	 * Método que recupera una instancia del tipo solicitado, o genera una nueva 
	 * cuando no quedan.
	 * 
	 * @param tipo valor entero que define el tipo de instancia solicitado.
	 * @return Una instancia del tipo solicitado.
	 * @throws PrototipoDesconocidoException
	 * Si la {@code Cache} no contine ningún prototipo con el código solicitado.
	 */
    public T newObject(int tipo) throws PrototipoDesconocidoException {
        Nodo<T> nodo = prototipos.get(tipo);
        if (nodo == null) throw new PrototipoDesconocidoException("\nNo existe ningún prototipo con el identificador: " + tipo);
        T newObject;
        if (nodo.instancias.size() > 0) {
            elementos--;
            newObject = nodo.instancias.poll();
        } else {
            newObject = nodo.prototipo.clone();
        }
        newObject.reset();
        return newObject;
    }

    /**
	 * Método que almacena una instancia para su posterior reutilización.</br>
	 * En caso de que al almacenar esta última instancia, se sobrepase la capacidad máxima de la {@code Cache},
	 * se elimina un determinado número de instancias de cada tipo almacenando hasta llegar a la capacidad deseada.
	 * De esta forma, siempre habrá más instancias almacenadas de los tipos usados más recientemente.
	 * 
	 * @param elemento a almacenar.
	 * @throws PrototipoDesconocidoException
	 * Si la {@code Cache} no contine ningún prototipo con el código solicitado.
	 */
    public void cached(T elemento) throws PrototipoDesconocidoException {
        Nodo<T> nodo = prototipos.get(elemento.hashCode());
        if (nodo == null) throw new PrototipoDesconocidoException("Prototipo " + elemento.hashCode() + " desconcido");
        nodo.instancias.offer(elemento);
        elementos++;
        if (elementos > tamMax) while (elementos > tam) {
            for (Nodo<T> n : prototipos.values()) {
                if (n.instancias.size() > 0) {
                    n.instancias.poll();
                    elementos--;
                }
            }
        }
    }

    @Override
    public String toString() {
        String me = "Caché con capacidad para " + tam + " elementos:\n" + "\tElementos en cache:\t" + elementos + "\n" + "\n";
        return me;
    }
}
