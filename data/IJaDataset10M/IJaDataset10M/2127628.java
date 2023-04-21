package ar.com.fdvs.dgarcia.lang_identificators;

/**
 * Esta clase represneta la identificacion de los objetos a traves de un criterio de igualdad
 * utilizando la propia igualdad que definen los objetos. Define su dominio de la misma manera que
 * {@link ar.com.fdvs.dgarcia.lang_identificators.HashIdentificator}. La raz�n para utilizar esta
 * clase es de performance. Esta clase llama preferentemente al metodo
 * {@link java.lang.Object#equals(java.lang.Object)} antes que a {@link java.lang.Object#hashCode()}
 * , por lo que debe ser usada solamente cuando el primero es m�s veloz que el segundo, y es
 * necesaria esa diferencia de velocida. En caso contrario utilizar la superclase.
 * 
 * Para que esta clase sea consistente es importante que el
 * {@link java.lang.Object#equals(java.lang.Object)} este definido coherentemente con el
 * {@link java.lang.Object#hashCode()}
 * 
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public class EqualsIdentificator extends HashIdentificator {

    /**
	 * Singleton
	 */
    public static final EqualsIdentificator instance = new EqualsIdentificator();

    /**
	 * Compara las dos instancias llamando a equals de la primera (en caso de no ser null). Si
	 * primera es null se llama al equals de la segunda
	 * 
	 * @param first
	 *            Instancia de refenrecia o null
	 * @param second
	 *            Instancia a comparar o null
	 * @return false si son conceptualmente distintas
	 */
    @Override
    public boolean areEquals(Object first, Object second) {
        if (first != null) {
            return first.equals(second);
        }
        if (second != null) {
            return second.equals(first);
        }
        return true;
    }

    /**
	 * Compara ambas instancias por equals, si son distintas se ordena por hashcode
	 * 
	 * @param first
	 *            Primer instancia o null
	 * @param second
	 *            Segunda instancia o null
	 * @return El delta que indica el orden
	 * @see ar.com.fdvs.dgarcia.lang_identificators.AbstractIdentificator#compare(Object, Object)
	 */
    @Override
    public int compare(Object first, Object second) {
        if (!this.areEquals(first, second)) {
            return super.compare(first, second);
        }
        return 0;
    }

    /**
	 * @param <T>
	 *            Tipo de elementos a identificar
	 * @return Devuelve la instancia de este singleton
	 */
    @SuppressWarnings("unchecked")
    public static <T> Identificator<T> getInstance() {
        return (Identificator<T>) instance;
    }
}
