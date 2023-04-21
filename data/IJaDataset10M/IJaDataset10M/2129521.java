package ar.com.fdvs.dgarcia.lang.iterators_space.basic;

import java.util.Iterator;
import java.util.NoSuchElementException;
import ar.com.fdvs.dgarcia.lang.iterators.PreSizedIterator;
import ar.com.fdvs.dgarcia.lang.iterators.ResetableIterator;

/**
 * Esta clase representa un iterador de una coleccion vacia por lo que no se puede iterar sobre
 * ningun elemento
 * 
 * @author D. Garcia
 */
public class EmptyIterator implements ResetableIterator<Object>, PreSizedIterator<Object> {

    /**
	 * Singleton
	 */
    private static final EmptyIterator instance = new EmptyIterator();

    /**
	 * Getter de la instancia que permite obtener este iterador sin tipo, asociado a un tipo
	 * particulars
	 * 
	 * @param <T>
	 *            Tipo de iterador necesitado
	 * @return La unica instancia de esta clase
	 */
    @SuppressWarnings("unchecked")
    public static <T> Iterator<T> getInstance() {
        return (Iterator<T>) instance;
    }

    /**
	 * @see java.util.Iterator#hasNext()
	 */
    public boolean hasNext() {
        return false;
    }

    /**
	 * @see java.util.Iterator#next()
	 */
    public Object next() {
        throw new NoSuchElementException();
    }

    /**
	 * @see java.util.Iterator#remove()
	 */
    public void remove() {
        throw new IllegalStateException();
    }

    public void reset() {
    }

    public int size() throws UnsupportedOperationException {
        return 0;
    }
}
