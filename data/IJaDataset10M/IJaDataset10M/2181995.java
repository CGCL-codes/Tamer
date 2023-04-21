package net.sf.josceleton.connection.api;

import net.sf.josceleton.core.api.async.Listener;
import net.sf.josceleton.core.api.entity.message.JointMessage;
import net.sf.josceleton.core.api.entity.message.UserMessage;

/**
 * @since 0.1
 */
public interface ConnectionListener extends Listener {

    void onJointMessage(JointMessage message);

    void onUserMessage(UserMessage message);
}
