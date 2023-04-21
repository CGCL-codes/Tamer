package gavator.core;

public interface UnaryProcedure<T> {

    /**
	 * Execute this procedure.
	 * 
	 * @param obj a parameter to this execution
	 */
    void run(T obj);
}
