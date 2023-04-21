package com.conicsoft.bdkJ.math;

public class size2d<TVal> {

    protected TVal m_width;

    protected TVal m_height;

    public size2d(TVal __w, TVal __h) {
        m_width = __w;
        m_height = __h;
    }

    public TVal get_width() {
        return m_width;
    }

    public TVal get_height() {
        return m_height;
    }

    public void set_width(TVal __w) {
        m_width = __w;
    }

    public void set_heigth(TVal __h) {
        m_height = __h;
    }

    public void copy(size2d<TVal> __sz) {
        set_width(__sz.get_width());
        set_heigth(__sz.get_height());
    }
}
