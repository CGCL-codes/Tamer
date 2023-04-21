package org.openmobster.core.mobileCloud.android_native.framework;

import android.os.AsyncTask;
import org.openmobster.core.mobileCloud.android.errors.ErrorHandler;
import org.openmobster.core.mobileCloud.android.errors.SystemException;
import org.openmobster.core.mobileCloud.api.ui.framework.Services;
import org.openmobster.core.mobileCloud.api.ui.framework.command.AppException;
import org.openmobster.core.mobileCloud.api.ui.framework.command.Command;
import org.openmobster.core.mobileCloud.api.ui.framework.command.CommandContext;
import org.openmobster.core.mobileCloud.api.ui.framework.command.CommandService;

/**
 * @author openmobster@gmail.com
 *
 */
final class BackgroundAsyncTask extends AsyncTask<CommandContext, Integer, CommandContext> {

    @Override
    protected CommandContext doInBackground(CommandContext... input) {
        CommandContext commandContext = input[0];
        try {
            CommandService service = Services.getInstance().getCommandService();
            Command command = service.findUICommand(commandContext.getTarget());
            try {
                command.doAction(commandContext);
            } catch (AppException ape) {
                service.reportAppException(commandContext, ape);
                return commandContext;
            }
            return commandContext;
        } catch (Exception e) {
            ErrorHandler.getInstance().handle(new SystemException(this.getClass().getName(), "execute", new Object[] { "Message:" + e.getMessage(), "Exception:" + e.toString(), "Target Command:" + commandContext.getTarget() }));
            commandContext.setAttribute("system_error", e);
            return commandContext;
        }
    }

    @Override
    protected void onPostExecute(CommandContext result) {
        CommandService service = Services.getInstance().getCommandService();
        Command command = service.findUICommand(result.getTarget());
        Exception systemError = (Exception) result.getAttribute("system_error");
        if (systemError != null) {
            ShowError.showGenericError(result);
            return;
        }
        if (result.hasErrors()) {
            command.doViewError(result);
            return;
        }
        command.doViewAfter(result);
    }
}
