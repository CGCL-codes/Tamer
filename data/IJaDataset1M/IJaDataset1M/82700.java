package it.unipg.bipod.web;

/**
 * Wrapper class used to add page splitting functions to an array.
 * 
 * @author Lorenzo Porzi
 *
 * @param <E> The type of the wrapped array.
 */
public class ListManager<E> {

    private E[] list;

    private int pageSize;

    private int page;

    private int numberOfPages;

    /**
	 * Constructs a ListManager that wraps the given array.
	 * 
	 * @param list the array to be wrapped
	 * @param pageSize the number of array entries in a page
	 */
    public ListManager(E[] list, int pageSize) {
        this.list = list;
        this.pageSize = pageSize;
        this.page = 0;
        this.numberOfPages = list.length / pageSize;
        if (list.length % pageSize != 0) this.numberOfPages += 1;
    }

    /**
	 * @return the list wrapped in this object
	 */
    public E[] getList() {
        return list;
    }

    /**
	 * Sets the current page number.
	 * 
	 * @param page the current page number.
	 */
    public void setPage(int page) {
        this.page = page;
    }

    /**
	 * @return the current page number.
	 */
    public int getPage() {
        return page;
    }

    /**
	 * @return the index of the first element of the current page
	 */
    public int getFirstRow() {
        return page * pageSize;
    }

    /**
	 * @return the number of array entries in a page
	 */
    public int getPageSize() {
        return pageSize;
    }

    /**
	 * @return the number of pages present in this ListManager
	 */
    public int getNumberOfPages() {
        return numberOfPages;
    }

    /**
	 * @return the length of the wrapped array
	 */
    public int getSize() {
        return list.length;
    }
}
