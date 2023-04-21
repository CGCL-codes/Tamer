package com.tekimpact.tuples4j;

import com.tekimpact.tuples.utils.EqualsUtils;
import java.util.List;

public class Triple<A, B, C> extends Pair<A, B> {

    protected C third = null;

    public Triple() {
        super();
    }

    public Triple(A first, B second, C third) {
        super(first, second);
        this.setThird(third);
    }

    @Override
    public List<Object> asList() {
        List<Object> lst = super.asList();
        lst.add(this.getThird());
        return lst;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(this.getClass().isInstance(obj))) {
            return false;
        }
        Triple var = (Triple) obj;
        if (!super.equals(var)) {
            return false;
        }
        if (EqualsUtils.areEqual(this.getThird(), var.getThird())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 89 * hash + (this.getThird() != null ? this.getThird().hashCode() : super.hashCode());
        return hash;
    }

    @Override
    public boolean isFullFilled() {
        return (this.getThird() != null) && super.isFullFilled();
    }

    public final C getThird() {
        return third;
    }

    public final void setThird(C third) {
        this.third = third;
    }
}
