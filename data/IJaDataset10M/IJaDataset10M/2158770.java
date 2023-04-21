package br.gov.frameworkdemoiselle.pagination;

/**
 * Context interface reserved for pagination purposes.
 * <p>
 * In order to use this, just add the line below in the code:
 * <p>
 * <code>@Inject PaginationContext paginationContext;</code>
 * 
 * @author SERPRO
 * @see Pagination
 */
public interface PaginationContext {

    /**
	 * Retrieves the pagination according to the class type specified.
	 * 
	 * @param clazz a {@code Class} type
	 * @return Pagination
	 */
    Pagination getPagination(Class<?> clazz);

    /**
	 * Retrieves the pagination according to the class type specified. If not existing, creates the pagination whenever
	 * {@code create} parameter is true.
	 * 
	 * @param clazz a {@code Class} type
	 * @param create determines whether pagination must always be returned
	 * @return Pagination
	 */
    Pagination getPagination(Class<?> clazz, boolean create);
}
