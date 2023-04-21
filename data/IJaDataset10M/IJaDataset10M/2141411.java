package org.dcm4chex.archive.web.maverick.mcmc.model;

import java.util.Collection;
import java.util.Iterator;
import org.dcm4chex.archive.ejb.interfaces.InstanceLocal;
import org.dcm4chex.archive.ejb.util.InstanceCollector;

/**
 * @author franz.willer
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MediaStudies {

    private InstanceCollector collector = new InstanceCollector();

    public MediaStudies(Collection col) {
        addAll(col);
    }

    /**
	 * @param col2
	 */
    public void addAll(Collection col) {
        Iterator iter = col.iterator();
        while (iter.hasNext()) {
        }
    }
}
