package org.crud.android.command;

import org.openmobster.android.api.rpc.MobileService;
import org.openmobster.android.api.rpc.Request;
import org.openmobster.core.mobileCloud.android.service.Registry;
import org.openmobster.core.mobileCloud.android_native.framework.ViewHelper;
import org.openmobster.core.mobileCloud.api.ui.framework.Services;
import org.openmobster.core.mobileCloud.api.ui.framework.command.CommandContext;
import org.openmobster.core.mobileCloud.api.ui.framework.command.RemoteCommand;
import android.app.Activity;

/**
 * @author openmobster@gmail.com
 *
 */
public final class PlainPush implements RemoteCommand {

    public void doViewBefore(CommandContext commandContext) {
    }

    public void doAction(CommandContext commandContext) {
        try {
            Request request = new Request("/start/push");
            request.setAttribute("app-id", Registry.getActiveInstance().getContext().getPackageName());
            new MobileService().invoke(request);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public void doViewAfter(CommandContext commandContext) {
        Activity currentActivity = Services.getInstance().getCurrentActivity();
        ViewHelper.getOkModal(currentActivity, "Plain Push", "Push successfully triggered..Push Notification should be received in a bit").show();
    }

    public void doViewError(CommandContext commandContext) {
        Activity currentActivity = Services.getInstance().getCurrentActivity();
        ViewHelper.getOkModal(currentActivity, "App Error", this.getClass().getName() + " had an error!!").show();
    }
}
