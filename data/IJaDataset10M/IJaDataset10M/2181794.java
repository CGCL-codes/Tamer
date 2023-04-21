package com.netx.generics.time;

import java.text.DateFormat;

public interface Formattable {

    public String format();

    public String format(DateFormat df);
}
