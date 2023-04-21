package sk.naive.talker.command;

import sk.naive.talker.*;
import sk.naive.talker.util.Utils;
import java.rmi.RemoteException;

/**
 * ReloadCommand.
 * @author <a href="mailto:virgo@naive.deepblue.sk">Richard "Virgo" Richter</a>
 * @version $Revision: 1.7 $ $Date: 2005/01/19 22:41:27 $
 */
public class Reload extends AbstractCommand {

    public void exec() throws CommandException, RemoteException {
        Utils.checkPermission(user, Consts.PERM_SUPERUSER);
        sendHelper().sendMessage(user, "reload.begin");
        ((ReloadableResources) commandDispatcher.getTalker().getProperties().get(ReloadableResources.TPROP_RELOAD_HELPER)).reload();
        sendHelper().sendMessage(user, "reload.end");
    }
}
