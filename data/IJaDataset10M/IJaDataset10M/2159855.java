package be.roam.jindy.ui.pagination;

/**
 * Extension of {@link Page} that allows it to be simply a so-called flow
 * breaker element.
 * <p>
 * A flow breaker is the part of a pagination that breaks up coherent parts.
 * Consider following pagination:
 * <code>PREV - 1 - ... - 6 - 7 - 8 - 9 - 10 - ... - 20 - NEXT</code>. In this
 * case the flow breakers appear just after the one and ten.
 * </p>
 * <p>
 * Have a look at {@link PaginationLayout} for a better example.
 * </p>
 * 
 * @author Kevin Wetzels
 * 
 */
public class PaginationItem extends Page {

    private static final long serialVersionUID = -8160602523355101539L;

    private boolean flowBreaker;

    private boolean nextPageItem;

    private boolean previousPageItem;

    public PaginationItem(Pagination pagination, int nr, boolean flowBreaker) {
        this(pagination, nr, flowBreaker, false, false);
    }

    public PaginationItem(Pagination pagination, int nr, boolean flowBreaker, boolean previousPageItem, boolean nextPageItem) {
        super(pagination, nr);
        this.flowBreaker = flowBreaker;
        this.previousPageItem = previousPageItem;
        this.nextPageItem = nextPageItem;
    }

    /**
	 * @return <code>true</code> when this item isn't actually an existing page
	 */
    public boolean isFlowBreaker() {
        return flowBreaker;
    }

    /**
	 * @return <code>true</code> when this item is the part of a pagination that could be labeled "Previous"
	 */
    public boolean isPreviousPageItem() {
        return previousPageItem;
    }

    /**
	 * @return <code>true</code> when this item is the part of a pagination that could be labeled "Next"
	 */
    public boolean isNextPageItem() {
        return nextPageItem;
    }
}
