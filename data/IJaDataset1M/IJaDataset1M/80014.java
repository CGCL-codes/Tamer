package net.java.sip.communicator.gui.event;

import java.util.*;

/**
 * <p>Title: SIP COMMUNICATOR</p>
 * <p>Description:JAIN-SIP Audio/Video phone application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Organisation: LSIIT laboratory (http://lsiit.u-strasbg.fr) </p>
 * <p>Network Research Team (http://www-r2.u-strasbg.fr))</p>
 * <p>Louis Pasteur University - Strasbourg - France</p>
 * @author Emil Ivov (http://www.emcho.com)
 * @version 1.1
 *
 */
public class UserCallInitiationEvent extends EventObject {

    public UserCallInitiationEvent(String callee) {
        super(callee);
    }
}
