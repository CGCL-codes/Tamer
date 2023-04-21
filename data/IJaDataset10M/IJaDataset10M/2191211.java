package net.stickycode.configured;

public interface ConfigurationListener {

    void resolve();

    void preConfigure();

    void configure();

    void postConfigure();
}
