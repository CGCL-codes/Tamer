package com.volantis.synergetics.testtools.mock.libraries.org.osgi.service.log;

import org.osgi.service.http.HttpService;
import org.osgi.service.http.HttpContext;
import org.osgi.service.log.LogService;

/**
 * Triggers auto generation of classes within <code>org.osgi.service.log</code>
 * and contained packages for which the source is not available.
 *
 * @mock.generate library="true"
 */
public class OSGiLogServiceLibrary {

    /**
     * @mock.generate interface="true"
     */
    public LogService logService;
}
