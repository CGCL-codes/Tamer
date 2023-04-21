package chat.model;

public abstract class Id<T extends Id<T>> {

    /** get identifier, the identifier property name must be "id" */
    public abstract int id();

    /**
	 * set identifier
	 * 
	 * @return self
	 */
    public T id(int id_) {
        throw new UnsupportedOperationException();
    }
}
