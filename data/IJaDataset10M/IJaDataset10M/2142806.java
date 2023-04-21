package org.dwgsoftware.raistlin.logging.logkit;

import org.apache.log.LogTarget;

/**
 * LogTargetManager Interface.
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/06 00:58:21 $
 */
public interface LogTargetManager {

    /**
    * Find a logging target matching the suppled id.
    * @param id the target id
    */
    LogTarget getLogTarget(String id);
}
