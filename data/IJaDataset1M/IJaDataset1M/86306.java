package com.thegreatchina.im.msn.backend.listener;

import com.thegreatchina.im.msn.backend.cmd.server.QNG;

/**
 * @author jjp
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface QNGListener extends BackendListener {

    public void performPingReason(QNG qng);
}
