package jenes.chromosome;

/**
 * An AlleleSet represents an alphabet of object gene allele values. Each {@link ObjectChromosome.Gene} of an 
 * {@link ObjectChromosome} has an allele set containing all the object values it can contain. 
 * It can be implemented by implementing this interface and its defined methods.   
 * 
 * @param T 
 * @author Luigi Troiano
 * @author Pierpaolo Lombardi
 * @author Giuseppe Pascale
 * @author Thierry Bodhuin
 * 
 * @version 1.0
 * 
 * @since 1.0
 * 
 * @see jenes.chromosome.ObjectChromosome
 */
public interface AlleleSet<T> {

    /**
	 * Gets the allele value at the specified position
	 * <p>
	 * @param pos the index of the desidered allele value
	 * @return the allele value at the specified position
	 */
    public abstract T getElementAt(int pos);

    /**
     * Provides the position of element within the set.
     *
     * @param element the element to look for
     * @return the position within the set, -1 if the element does not exist
     */
    public int indexOf(T element);

    /**
	 * Gets a random allele value within this alphabet.
	 * The allele value returned has to be a copy of the value in the allele set.
	 * <p>
	 * @return the random value selected
	 */
    public abstract T getRandomValue();

    /**
	 * Returns the alphabet size
	 * <p>
	 * @return the alphabet size
	 */
    public abstract int size();
}
