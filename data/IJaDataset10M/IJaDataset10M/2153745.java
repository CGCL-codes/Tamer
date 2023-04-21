package org.jaffa.modules.messaging.components.messageviewer;

import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.modules.messaging.components.messageviewer.dto.MessageViewerOutDto;
import org.jaffa.modules.messaging.components.messageviewer.dto.MessageViewerInDto;

/** Interface for MessageViewer components.
 */
public interface IMessageViewer {

    /** Returns the details for Message.
     * @param input The criteria based on which an object will be retrieved.
     * @throws ApplicationExceptions This will be thrown if the criteria contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The object details. A null indicates, the object was not found.
     */
    public MessageViewerOutDto read(MessageViewerInDto input) throws FrameworkException, ApplicationExceptions;

    /** Changes the priority of a Message.
     * @param input The criteria based on which an object will be retrieved.
     * @throws ApplicationExceptions This will be thrown if the criteria contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The new messageId, in case the implementation were to recreate the message.
     */
    public String changePriority(MessageViewerInDto input, Long newPriority) throws FrameworkException, ApplicationExceptions;

    /** Returns the messageId to be used for retrieving the BusinessEventLogs for a Message.
     * @param input The criteria based on which an object will be retrieved.
     * @throws ApplicationExceptions This will be thrown if the criteria contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The messageId.
     */
    public String getMessageIdForBusinessEventLog(MessageViewerInDto input) throws FrameworkException, ApplicationExceptions;

    /** This should be invoked, when done with the component. */
    public void destroy();
}
