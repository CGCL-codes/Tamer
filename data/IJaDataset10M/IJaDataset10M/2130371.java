package org.xhtmlrenderer.render;

import java.util.ArrayList;
import java.util.List;
import org.xhtmlrenderer.layout.LayoutContext;

public class ContentLimitContainer {

    private ContentLimitContainer _parent;

    private int _initialPageNo;

    private List _contentLimits = new ArrayList();

    private PageBox _lastPage;

    public ContentLimitContainer(LayoutContext c, int startAbsY) {
        _initialPageNo = getPage(c, startAbsY).getPageNo();
    }

    public int getInitialPageNo() {
        return _initialPageNo;
    }

    public int getLastPageNo() {
        return _initialPageNo + _contentLimits.size() - 1;
    }

    public ContentLimit getContentLimit(int pageNo) {
        return getContentLimit(pageNo, false);
    }

    private ContentLimit getContentLimit(int pageNo, boolean addAsNeeded) {
        if (addAsNeeded) {
            while (_contentLimits.size() < (pageNo - _initialPageNo + 1)) {
                _contentLimits.add(new ContentLimit());
            }
        }
        int target = pageNo - _initialPageNo;
        if (target >= 0 && target < _contentLimits.size()) {
            return (ContentLimit) _contentLimits.get(pageNo - _initialPageNo);
        } else {
            return null;
        }
    }

    public void updateTop(LayoutContext c, int absY) {
        PageBox page = getPage(c, absY);
        getContentLimit(page.getPageNo(), true).updateTop(absY);
        ContentLimitContainer parent = getParent();
        if (parent != null) {
            parent.updateTop(c, absY);
        }
    }

    public void updateBottom(LayoutContext c, int absY) {
        PageBox page = getPage(c, absY);
        getContentLimit(page.getPageNo(), true).updateBottom(absY);
        ContentLimitContainer parent = getParent();
        if (parent != null) {
            parent.updateBottom(c, absY);
        }
    }

    public PageBox getPage(LayoutContext c, int absY) {
        PageBox page;
        PageBox last = getLastPage();
        if (last != null && absY >= last.getTop() && absY < last.getBottom()) {
            page = last;
        } else {
            page = c.getRootLayer().getPage(c, absY);
            setLastPage(page);
        }
        return page;
    }

    private PageBox getLastPage() {
        ContentLimitContainer c = this;
        while (c.getParent() != null) {
            c = c.getParent();
        }
        return c._lastPage;
    }

    private void setLastPage(PageBox page) {
        ContentLimitContainer c = this;
        while (c.getParent() != null) {
            c = c.getParent();
        }
        c._lastPage = page;
    }

    public ContentLimitContainer getParent() {
        return _parent;
    }

    public void setParent(ContentLimitContainer parent) {
        _parent = parent;
    }

    public boolean isContainsMultiplePages() {
        return _contentLimits.size() > 1;
    }

    public String toString() {
        return "[initialPageNo=" + _initialPageNo + ", limits=" + _contentLimits + "]";
    }
}
