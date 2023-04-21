package org.sysmhg.util;

import org.hibernate.Criteria;
import org.sysmhg.base.model.Customer;

public class Pagination {

    /**
	 * indice de registro a empezar la paginaci�n
	 */
    private Long start;

    /**
	 * cantidad de registros a tomar a partir de "start"
	 */
    private Long limit = Constants.PAGINATION_SIZE;

    /**
	 * cantidad total de registros en tabla  
	 */
    private Long totalCount;

    /**
	 * nombre del campo a ordenar
	 */
    private String fieldNameSort;

    /**
	 * ASC o DESC
	 */
    private String directionSort;

    public Pagination() {
    }

    public Pagination(Object start, Object limit, String sort, String dir) {
        super();
        this.start = Long.valueOf(String.valueOf(start));
        this.limit = Long.valueOf(String.valueOf(limit));
        this.fieldNameSort = sort;
        this.directionSort = dir;
    }

    public Pagination(String start2, String limit2, String sort) {
    }

    public void setStart(Object start) {
        this.start = Long.valueOf(String.valueOf(start));
    }

    public void setLimit(Object limit) {
        this.limit = Long.valueOf(String.valueOf(limit));
    }

    /**
	 * @return the start
	 */
    public Long getStart() {
        return start;
    }

    /**
	 * @param start the start to set
	 */
    public void setStart(Long start) {
        this.start = start;
    }

    /**
	 * @return the limit
	 */
    public Long getLimit() {
        return limit;
    }

    /**
	 * @param limit the limit to set
	 */
    public void setLimit(Long limit) {
        this.limit = limit;
    }

    /**
	 * @return the size
	 */
    public Long getTotalCount() {
        return totalCount;
    }

    /**
	 * @param size the size to set
	 */
    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    /**
	 * @param size the size to set
	 */
    public void setTotalCount(Object totalCount) {
        this.totalCount = GenericUtil.getDefaultLong(totalCount);
    }

    /**
	 * @return the fieldNameSort
	 */
    public String getFieldNameSort() {
        return fieldNameSort;
    }

    /**
	 * @param fieldNameSort the fieldNameSort to set
	 */
    public void setFieldNameSort(String fieldNameSort) {
        this.fieldNameSort = fieldNameSort;
    }

    /**
	 * @return the directionSort
	 */
    public String getDirectionSort() {
        return directionSort;
    }

    /**
	 * @param directionSort the directionSort to set
	 */
    public void setDirectionSort(String directionSort) {
        this.directionSort = directionSort;
    }
}
