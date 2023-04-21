package com.ingenta.clownbike;

import java.util.List;

public class PageFactory {

    private List _contents;

    private int _pageSize;

    private int _pageCount;

    public PageFactory(List contents, int pageSize) {
        if (contents == null) throw new IllegalArgumentException("contents is null");
        if (pageSize < 1) throw new IllegalArgumentException("pageSize too small");
        _contents = contents;
        _pageSize = pageSize;
        _pageCount = Math.max(1, (_contents.size() + _pageSize - 1) / _pageSize);
    }

    public Page create(int pageNumber) {
        if (0 < pageNumber && pageNumber < _pageCount + 1) {
            int first = (pageNumber - 1) * _pageSize;
            int last = Math.min(pageNumber * _pageSize, _contents.size());
            List contents = _contents.subList(first, last);
            Page page = new Page(_pageCount, pageNumber, contents);
            return page;
        }
        return null;
    }

    public Page createBefore(Page page) {
        return create(page.getPageNumber() - 1);
    }

    public Page createAfter(Page page) {
        return create(page.getPageNumber() + 1);
    }
}
