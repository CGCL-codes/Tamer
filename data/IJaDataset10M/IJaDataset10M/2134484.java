package org.openmobster.core.mobileCloud.android.module.dm;

import org.openmobster.core.mobileCloud.api.ui.framework.push.PushCommand;
import org.openmobster.core.mobileCloud.api.ui.framework.push.PushCommandContext;

/**
 * 
 * 
 * @author openmobster@gmail.com
 */
public final class HandleRPCPush implements PushCommand {

    public void handlePush(PushCommandContext commandContext) {
        System.out.println("Handle RPC PushCommand succesfull (DM)..........");
        System.out.println("ABC: " + commandContext.getAttribute("abc"));
        System.out.println("XYZ: " + commandContext.getAttribute("xyz"));
    }
}
