package net.sf.kfgodel.dgarcia.lang.iterators;

import java.util.Iterator;

/**
 * Esta interfaz representa un iterador de elementos que ha sido extendido agregando metodos que
 * permiten tener una funcionalidad más completa en un iterador.
 * 
 * @version 1.0
 * @since 13/01/2007
 * @author D. Garcia
 * @param <T>
 *            Tipo de los elementos a iterar
 */
public interface PreSizedIterator<T> extends Iterator<T> {

    /**
	 * Indica la cantidad de elementos iterables por este iterador. Mediante este metodo, los
	 * iteradores que lo implementen, pueden ofrecer una vision de la cantidad de elementos que se
	 * iteraran, antes de ser iterados.
	 * 
	 * @return La cantidad de elementos iterables
	 * @throws UnsupportedOperationException
	 *             Si este iterador no permite saber la cantidad de elementos a iterar (dependera de
	 *             la subclase de iterador)
	 */
    public int size() throws UnsupportedOperationException;
}
