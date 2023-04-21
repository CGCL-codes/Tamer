package org.fao.geonet.lib.wmc.om;

import org.jdom.Namespace;

/**
 *
 * @author etj
 */
public class WMC {

    public static final Namespace XLINKNS = Namespace.getNamespace("xlink", "http://www.w3.org/1999/xlink");

    /** Standard OGC namespace for WMC documents */
    public static final Namespace WMCNS = Namespace.getNamespace("http://www.opengeospatial.net/context");

    /** Alternate namespace for WMC docs used sometime by non-compliant servers. */
    public static final Namespace OGWMCNS = Namespace.getNamespace("http://www.opengis.net/context");
}
