package ua.org.nuos.sdms.clientgui.client.handlers;

import ua.org.nuos.sdms.clientgui.client.context.Messages;
import ua.org.nuos.sdms.clientgui.client.controller.GlobalBoard;
import ua.org.nuos.sdms.clientgui.client.events.ErrorMessageEvent;
import ua.org.nuos.sdms.clientgui.client.events.RemoveGroupEvent;
import ua.org.nuos.sdms.clientgui.client.events.ShowDialogMessageEvent;
import ua.org.nuos.sdms.clientgui.client.events.ShowSelectGroupEvent;
import ua.org.nuos.sdms.clientgui.client.listeners.DialogResultHandler;
import ua.org.nuos.sdms.clientgui.client.listeners.RemoveGroupListener;
import ua.org.nuos.sdms.clientgui.server.AppProxy;
import ua.org.nuos.sdms.middle.util.exception.NotGroupLeaderException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: dio
 * Date: 27.03.12
 * Time: 20:28
 * To change this template use File | Settings | File Templates.
 */
public class RemoveGroupHandler implements RemoveGroupListener {

    private long groupId;

    @Override
    public void remove(RemoveGroupEvent event) {
        this.groupId = event.getGroupId();
        ShowDialogMessageEvent e = new ShowDialogMessageEvent(getDialogResultHandler(), "Внимание!", "<b>Удалить группу?</b>");
        AppProxy.getInstance().getController(GlobalBoard.ID).fire(e);
    }

    private DialogResultHandler getDialogResultHandler() {
        return new DialogResultHandler() {

            @Override
            public void dialogResultProcess(boolean accepted) {
                if (accepted) {
                    try {
                        AppProxy.getInstance().getServiceLocator().getClientServiceBean().removeGroup(groupId);
                        AppProxy.getInstance().getModelLocator().setGroup(null);
                        AppProxy.getInstance().getController(GlobalBoard.ID).fire(new ShowSelectGroupEvent());
                    } catch (NotGroupLeaderException e) {
                        Logger.getAnonymousLogger().log(Level.WARNING, "Remove group error: user is not group leader", e);
                        AppProxy.getInstance().getControllerLocator().getController(GlobalBoard.ID).fire(new ErrorMessageEvent(Messages.REMOVE_GROUP_NOT_LEADER));
                    } catch (Exception ex) {
                        Logger.getAnonymousLogger().log(Level.WARNING, "Remove group error", ex);
                        AppProxy.getInstance().getControllerLocator().getController(GlobalBoard.ID).fire(new ErrorMessageEvent(Messages.ACTION_ERROR));
                    }
                }
            }
        };
    }
}
