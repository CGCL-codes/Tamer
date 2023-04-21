package org.rakiura.rak;

import java.util.EventListener;

/**
 * 
 * 
 *<br><br>
 * ProgressListener.java<br>
 * Created: Sat Jun  2 01:20:16 2001<br>
 *
 * @author Mariusz   (mariusz@rakiura.org)
 * @version $Revision: 1.1 $ $Date: 2001/06/01 14:17:55 $
 */
public interface ProgressListener extends EventListener {

    void notify(ProgressEvent event);
}
