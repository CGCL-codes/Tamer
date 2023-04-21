package org.beanopen.f;

public interface Constant {

    public static final String VERSION = "F_1.0.0_alpha";

    public static final Package PACKAGE = Constant.class.getPackage();

    public static final String DEFAULT_ENCODING = "UTF-8";

    public static final String DEFAULT_CONFIG_LOCATION_PARAM = "classpath:config/spring/**.xm";
}
