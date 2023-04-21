package com.aurorasoftworks.signal.tools.core.context.loader;

import com.aurorasoftworks.signal.tools.core.context.ContextProcessingException;

public interface IContextLoader<T> {

    T loadContext(String contextLocation, IContextLoaderListener listener) throws ContextProcessingException;

    T loadContext(String contextLocation, IContextLoaderListener listener, ClassLoader classLoader) throws ContextProcessingException;
}
