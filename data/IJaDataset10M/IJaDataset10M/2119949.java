package com.att.echarts;

public class AnyPortProperties extends PortProperties {

    public static final String PORT_TYPE = "PORT_TYPE";

    public static final String ANY_PORT = "ANY_PORT";

    public AnyPortProperties(AnyPort port) {
        super(port);
        setProperty(PORT_TYPE, ANY_PORT);
    }
}
