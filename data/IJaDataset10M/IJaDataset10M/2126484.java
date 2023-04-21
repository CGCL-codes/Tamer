package com.triplea.rolap.plugins;

import java.net.URI;

/**
 * @author: kiselev
 */
public interface IRequest {

    public String getParameter(String name);

    public String getPaloHeader();

    public URI uri();

    public String version();
}
