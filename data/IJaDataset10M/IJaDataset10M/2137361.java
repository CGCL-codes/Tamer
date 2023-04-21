package org.upfrost.idiom;

import java.io.Serializable;

public class Order implements Serializable {

    private String m_column;

    private boolean m_asc;

    public Order(String column, boolean asc) {
        m_column = column;
        m_asc = asc;
    }

    public String getColumn() {
        return m_column;
    }

    public void setColumn(String column) {
        m_column = column;
    }

    public boolean isAsc() {
        return m_asc;
    }

    public void setAsc(boolean asc) {
        m_asc = asc;
    }
}
