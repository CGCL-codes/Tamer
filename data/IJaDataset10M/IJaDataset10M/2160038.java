package com.whstudio.common.pattern;

public interface Subscriber {

    Integer getId();

    String getName();

    String getSex();

    void update(Observable o, Object arg);
}
