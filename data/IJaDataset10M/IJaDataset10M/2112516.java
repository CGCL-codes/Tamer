package reconcile.weka.gui.beans;

import java.util.EventListener;

/**
 * Interface to something that can accept instance events
 *
 * @author <a href="mailto:mhall@cs.waikato.ac.nz">Mark Hall</a>
 * @version $Revision: 1.1 $
 */
public interface InstanceListener extends EventListener {

    /**
   * Accept and process an instance event
   *
   * @param e an <code>InstanceEvent</code> value
   */
    void acceptInstance(InstanceEvent e);
}
