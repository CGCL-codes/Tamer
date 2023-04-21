package org.wintersleep.util.spring.tx;

public interface FirstService {

    void required();

    void supports();

    void mandatory();

    void requiresNew();

    void notSupported();

    void never();

    void nested();
}
