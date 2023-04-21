package com.antlersoft.query;

import java.util.ArrayList;
import java.util.List;

public interface ValueObject extends Bindable {

    public ValueContext getContext();

    public List getValueCollection();

    public static List NO_SUBS = new ArrayList();
}
