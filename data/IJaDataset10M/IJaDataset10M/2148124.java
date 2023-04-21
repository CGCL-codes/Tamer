package com.mycila.testing.plugins.jetty.locator;

import java.io.File;
import java.io.FileNotFoundException;
import com.google.common.collect.Lists;

class AntFileLocator implements FileLocator {

    /**
     * {@inheritDoc}
     * 
     * @see com.mycila.testing.plugins.jetty.locator.FileLocator#locate(java.lang.String)
     */
    public Iterable<File> locate(final String path) throws FileNotFoundException {
        return Lists.newArrayList(new RegFileLocator().locate(new AntPath(path).toRegex()));
    }
}
