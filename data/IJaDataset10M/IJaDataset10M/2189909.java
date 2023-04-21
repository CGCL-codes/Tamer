package ru.adv.db.app.request;

import java.util.ArrayList;
import java.util.List;

/**
 * Calculates ordered list of {@link PageInfo} to fill pages later 
 * 
 * @author vic
 *
 */
public class PageSplitter {

    public static int MAX_DISTANCE_TO_FETCH_AT_ONCE = 500;

    private int itemsCount;

    private int pageSize;

    private int currentPage;

    private int siblingsCount;

    private int totalPages;

    private int firstItemOffset;

    private int maxItemToFetch;

    private List<PageInfo> pageInfos = new ArrayList<PageInfo>();

    private PageInfo currentPageInfo;

    public PageSplitter(int itemsCount, int pageSize, int currentPage, int siblingsCount) {
        super();
        this.itemsCount = itemsCount;
        this.pageSize = pageSize;
        this.siblingsCount = siblingsCount;
        this.totalPages = (itemsCount / pageSize) + (itemsCount % pageSize > 0 ? 1 : 0);
        this.currentPage = (0 < currentPage && currentPage <= totalPages) ? currentPage : 1;
        this.firstItemOffset = 0;
        this.maxItemToFetch = pageSize;
        init();
        if (pageInfos.size() > 1) {
            PageInfo first = pageInfos.get(0);
            PageInfo sibling = pageInfos.get(1);
            if (sibling.getFirstItemPosition() - first.getLastItemPosition() <= MAX_DISTANCE_TO_FETCH_AT_ONCE) {
                this.firstItemOffset = 0;
            } else {
                this.firstItemOffset = sibling.getFirstItemPosition() - 1;
            }
            PageInfo last = pageInfos.get(pageInfos.size() - 1);
            PageInfo lastSibling = pageInfos.get(pageInfos.size() - 2);
            if (last.getFirstItemPosition() - lastSibling.getLastItemPosition() <= MAX_DISTANCE_TO_FETCH_AT_ONCE) {
                this.maxItemToFetch = last.getLastItemPosition() - this.firstItemOffset;
            } else {
                this.maxItemToFetch = lastSibling.getLastItemPosition() - this.firstItemOffset;
            }
        } else if (pageInfos.size() == 1) {
            this.firstItemOffset = pageInfos.get(0).getFirstItemPosition();
            this.maxItemToFetch = pageSize;
        }
    }

    public PageInfo getCurrentPageInfo() {
        return currentPageInfo;
    }

    public int getItemsCount() {
        return itemsCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getSiblingsCount() {
        return siblingsCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<PageInfo> getPageInfos() {
        return pageInfos;
    }

    private void init() {
        if (itemsCount <= 0) {
            return;
        }
        currentPageInfo = createPageInfo(currentPage);
        pageInfos.add(currentPageInfo);
        for (int i = currentPage - 1; i >= Math.max(1, currentPage - siblingsCount); i--) {
            pageInfos.add(0, createPageInfo(i));
        }
        for (int i = currentPage + 1; i <= Math.min(currentPage + siblingsCount, totalPages); i++) {
            pageInfos.add(createPageInfo(i));
        }
        if (pageInfos.get(0).getPageNum() != 1) {
            pageInfos.add(0, createPageInfo(1));
        }
        if (!pageInfos.isEmpty() && pageInfos.get(pageInfos.size() - 1).getPageNum() != totalPages) {
            pageInfos.add(createPageInfo(totalPages));
        }
    }

    /**
     * Calculates the first item position from one we can start fill {@link PageInfo} elements
     * of {@link PageSplitter} 
     * @return
     */
    public int getFirstItemOffset() {
        return this.firstItemOffset;
    }

    /**
     * Calculates max item need to fill siblings and current {@link PageInfo} elements
     * @return
     */
    public int getMaxItemToFetch() {
        return maxItemToFetch;
    }

    PageInfo createPageInfo(int pageNum) {
        return new PageInfo(pageNum == currentPage, firstItemPosition(pageNum), lastItemPosition(pageNum), pageNum);
    }

    int firstItemPosition(int pageNum) {
        return (pageNum - 1) * pageSize + 1;
    }

    int lastItemPosition(int pageNum) {
        return Math.min(pageNum * pageSize, this.itemsCount);
    }
}
