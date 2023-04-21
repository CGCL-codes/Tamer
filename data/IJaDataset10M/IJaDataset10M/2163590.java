package org.book4j.logging;

public interface ILoggerFactory {

    public ILogger CreateLogger(String name);

    public ILogger CreateLogger(Class clazz);
}
