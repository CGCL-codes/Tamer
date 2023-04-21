package cn.ekuma.epos.qkorder.ordermanager;

import java.util.EventObject;

/**
 *
 * @author Administrator
 */
public class PageChangeEvent extends EventObject {

    int pageIndex;

    public PageChangeEvent(Object source, int inPageIndex) {
        super(source);
        this.pageIndex = inPageIndex;
    }

    public int getPageIndex() {
        return pageIndex;
    }
}
