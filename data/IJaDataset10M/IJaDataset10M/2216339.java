package mr.go.sgfilter;

/**
 * This interface represents types which are able to perform data processing in
 * place. Useful examples include: eliminating zeros, padding etc.
 * 
 * @author Marcin Rzeźnicki
 * @see SGFilter#appendPreprocessor(Preprocessor)
 */
public interface Preprocessor {

    /**
	 * Data processing method. Called on Preprocessor instance when its
	 * processing is needed
	 * 
	 * @param data
	 */
    void apply(double[] data);
}
