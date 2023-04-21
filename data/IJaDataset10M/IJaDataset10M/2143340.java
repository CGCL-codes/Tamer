package sk.naive.talker.command;

import sk.naive.talker.*;
import sk.naive.talker.adapter.TagConsts;
import sk.naive.talker.util.Utils;
import java.rmi.RemoteException;

/**
 *
 * @author <a href="mailto:rytier@naive.deepblue.sk">Martin "Rytier" Kerni</a>
 * @version $Revision: 1.6 $ $Date: 2005/01/18 20:51:30 $
 */
public class Clear extends AbstractCommand {

    public void exec() throws CommandException, RemoteException {
        user.send(Utils.tag(TagConsts.CLEAR));
    }
}
