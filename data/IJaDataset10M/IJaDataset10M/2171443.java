package net.sf.staccatocommons.defs.partial;

/**
 * Interface for accessing the fourth element of a tuple
 * 
 * @author flbulgarelli
 * 
 * @param <A>
 */
public interface FourthAware<A> {

    /**
   * @return the fourth component
   */
    A fourth();

    /**
   * Synonym for {@link #fourth()}
   * 
   * @return the fourth component
   */
    A _3();
}
