package org.jaffa.modules.messaging.components.queueviewer;

import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.modules.messaging.components.messageviewer.dto.MessageViewerInDto;
import org.jaffa.modules.messaging.components.queueviewer.dto.QueueViewerOutDto;
import org.jaffa.modules.messaging.components.queueviewer.dto.QueueViewerInDto;

/** Interface for QueueViewer components.
 */
public interface IQueueViewer {

    /** Returns the details for Queue.
     * @param input The criteria based on which an object will be retrieved.
     * @throws ApplicationExceptions This will be thrown if the criteria contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The object details. A null indicates, the object was not found.
     */
    public QueueViewerOutDto read(QueueViewerInDto input) throws FrameworkException, ApplicationExceptions;

    /** Deletes a Message.
     * @param input The criteria based on which an object will be retrieved.
     * @throws ApplicationExceptions This will be thrown if the criteria contains invalid data.
     * @throws FrameworkException Indicates some system error.
     */
    public void deleteMessage(MessageViewerInDto input) throws ApplicationExceptions, FrameworkException;

    /** Resubmits a Message.
     * @param input The criteria based on which an object will be retrieved.
     * @throws ApplicationExceptions This will be thrown if the criteria contains invalid data.
     * @throws FrameworkException Indicates some system error.
     */
    public void resubmitMessage(MessageViewerInDto input) throws ApplicationExceptions, FrameworkException;

    /** This should be invoked, when done with the component. */
    public void destroy();
}
