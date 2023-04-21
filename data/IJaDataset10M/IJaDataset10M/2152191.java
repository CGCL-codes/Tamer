package plugin.typeinterfaces;

/**
 * Implementing classes can sink data, might be used for piping as well
 * 
 * @author skomp
 * 
 */
public interface DataSink<E> {

    /**
	 * sink the src to this sink
	 * 
	 * @param src
	 *            the <code>DataStreamSource</code> to sink
	 */
    public abstract Object compute(E obj);
}
