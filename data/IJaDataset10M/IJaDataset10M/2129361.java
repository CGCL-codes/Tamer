package org.knopflerfish.service.desktop;

import org.osgi.framework.Bundle;

public interface BundleFilter {

    public boolean accept(Bundle b);
}
