package org.charleslab.tools.page;

import java.util.Collection;

/**
 * 
 * @author chao.cheng
 * @createtime:Aug 6, 2008 8:55:08 AM
 */
public class ListModel {

    private Collection<?> list;

    private Page page;

    public Collection<?> getList() {
        return list;
    }

    public void setList(Collection<?> c) {
        this.list = c;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
