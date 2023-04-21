package com.sun.pept.protocol;

import com.sun.pept.ept.MessageInfo;
import java.util.*;

/**
 * <p>
 *
 * @author Dr. Harold Carr
 * </p>
 */
public interface MessageDispatcher {

    /**
 * <p>
 * Does ...
 * </p><p>
 *
 * </p><p>
 *
 * @param messageInfo ...
 * </p>
 */
    public void send(MessageInfo messageInfo);

    /**
 * <p>
 * Does ...
 * </p><p>
 *
 * </p><p>
 *
 * @param messageInfo ...
 * </p>
 */
    public void receive(MessageInfo messageInfo);
}
