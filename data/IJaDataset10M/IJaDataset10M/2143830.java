package dsb.bar.tks.server.dao;

/**
 * Interface defining a DAO which manages an automatically numbered entity.
 * <p>
 * The automatic numbering is applicable to the business key of an entity, not
 * it's database primary key.
 * 
 * @param <T>
 *            The type of the automatic number.
 */
public interface AutoNumberedEntityDAO<T> {

    /**
	 * Get the next number which would have been the default next key.
	 * 
	 * @return The next number.
	 */
    public long getNextNumber();
}
