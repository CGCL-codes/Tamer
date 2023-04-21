package org.qtitools.qti.rendering.utils;

import java.io.InputStream;

/**
 * Implementation of {@link AbstractResourceResolver} that loads resources via
 * the {@link ClassLoader}, which is most likely the implementation we'll use
 * once everything has been tidied up.
 * 
 * @author   David McKain
 * @revision $Revision: 2215 $
 */
public final class ClassLoaderResourceResolver extends AbstractResourceResolver {

    private final Class<?> clazz;

    public ClassLoaderResourceResolver(final Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected InputStream getResource(String resourcePath) {
        return clazz.getResourceAsStream(resourcePath);
    }
}
