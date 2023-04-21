package com.appspot.ast.client.model;

/**
 * Created by IntelliJ IDEA.
 * User: Антон
 * Date: 23.05.2009
 * Time: 18:11:12
 */
public class GwtTag extends BaseGwtObject {

    private String header;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public void setName(String name) {
        header = name;
    }

    @Override
    public String getName() {
        return header;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (getKey() == null) return false;
        if (!(o instanceof GwtTag)) return false;
        final GwtTag tag = (GwtTag) o;
        return getKey().equals(tag.getKey());
    }
}
