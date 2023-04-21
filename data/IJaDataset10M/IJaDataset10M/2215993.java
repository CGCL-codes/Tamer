package wicket.table;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * A link to a page of a PageableListView.
 * 
 * @author Jonathan Locke
 * @author Eelco Hillenius
 * @author Martijn Dashorst
 * @param <T>
 *            type of model object
 * @author manuelbarzi
 * @version 20120113122543
 */
public class PagingNavigationLink<T> extends Link<T> {

    private static final long serialVersionUID = 1L;

    /** The pageable list view. */
    protected final IPageable pageable;

    /** The page of the PageableListView this link is for. */
    private final int pageNumber;

    /**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param pageable
	 *            The pageable component for this page link
	 * @param pageNumber
	 *            The page number in the PageableListView that this link links to. Negative
	 *            pageNumbers are relative to the end of the list.
	 */
    public PagingNavigationLink(final String id, final IPageable pageable, final int pageNumber) {
        super(id);
        setAutoEnable(true);
        this.pageNumber = pageNumber;
        this.pageable = pageable;
    }

    /**
	 * @see org.apache.wicket.markup.html.link.Link#onClick()
	 */
    @Override
    public void onClick() {
        pageable.setCurrentPage(getPageNumber());
    }

    /**
	 * Get pageNumber.
	 * 
	 * @return pageNumber.
	 */
    public final int getPageNumber() {
        return cullPageNumber(pageNumber);
    }

    /**
	 * Allows the link to cull the page number to the valid range before it is retrieved from the
	 * link
	 * 
	 * @param pageNumber
	 * @return culled page number
	 */
    protected int cullPageNumber(int pageNumber) {
        int idx = pageNumber;
        if (idx < 0) {
            idx = pageable.getPageCount() + idx;
        }
        if (idx > (pageable.getPageCount() - 1)) {
            idx = pageable.getPageCount() - 1;
        }
        if (idx < 0) {
            idx = 0;
        }
        return idx;
    }

    /**
	 * @return True if this page is the first page of the containing PageableListView
	 */
    public final boolean isFirst() {
        return getPageNumber() == 0;
    }

    /**
	 * @return True if this page is the last page of the containing PageableListView
	 */
    public final boolean isLast() {
        return getPageNumber() == (pageable.getPageCount() - 1);
    }

    /**
	 * Returns true if this PageableListView navigation link links to the given page.
	 * 
	 * @param page
	 *            The page
	 * @return True if this link links to the given page
	 * @see org.apache.wicket.markup.html.link.PageLink#linksTo(org.apache.wicket.Page)
	 */
    @Override
    public final boolean linksTo(final Page page) {
        return getPageNumber() == pageable.getCurrentPage();
    }
}
