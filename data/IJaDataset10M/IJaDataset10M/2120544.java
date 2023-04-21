package org.nexopenframework.exception.event;

import org.springframework.context.ApplicationListener;

/**
 * <p>NexTReT Open Framework</p>
 * 
 * <p>Comments here</p>
 *
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public interface ExceptionListener extends ApplicationListener {

    /**
	 * @param _event
	 */
    public void handleException(ExceptionEvent _event);
}
