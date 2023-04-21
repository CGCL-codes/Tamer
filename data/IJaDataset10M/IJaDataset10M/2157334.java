package com.sitechasia.webx.components.jmx.util;

import com.sitechasia.webx.components.jmx.mbean.MBeanGeneric;

public interface EchoInterface extends MBeanGeneric {

    public String echo(String returnString);
}
