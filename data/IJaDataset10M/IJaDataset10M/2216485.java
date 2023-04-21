package de.psisystems.dmachinery.caches;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: stefanpudig
 * Date: Jul 28, 2009
 * Time: 7:06:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class CacheException extends Exception {

    public CacheException(Exception e) {
        super(e);
    }
}
