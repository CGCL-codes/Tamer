package com.conicsoft.bdkJ.math;

public class size3d<TVal> extends size2d<TVal> {

    protected TVal m_deep;

    public size3d(TVal __w, TVal __h, TVal __d) {
        super(__w, __h);
        m_deep = __d;
    }

    public TVal get_deep() {
        return m_deep;
    }

    public void set_deep(TVal __d) {
        m_deep = __d;
    }

    public void copy(size3d<TVal> __sz) {
        super.copy(__sz);
        set_deep(__sz.get_deep());
    }
}
