package tests.command.commands;

import java.util.Iterator;
import java.util.List;
import org.sepp.api.Sepp;
import org.sepp.channels.Channel;
import org.sepp.datatypes.Credentials;
import org.sepp.datatypes.SharedKeyCredentials;
import org.sepp.states.ProtocolStatesHandler;

public class CommandJoinGroup extends Command {

    /**
	 * 
	 */
    public CommandJoinGroup(Sepp sepp, List<String> errorList) {
        super(sepp, errorList);
    }

    /**
	 * 
	 */
    @Override
    public void executeCommand(List<String> params) throws Exception {
        Credentials credentials = null;
        int authentication = ProtocolStatesHandler.NO_AUTHENTICATION;
        if (params.get(2).equals("shared-key-authentication")) {
            authentication = ProtocolStatesHandler.SHARED_SECRET_KEY_AUTHENTICATION;
            credentials = new SharedKeyCredentials(params.get(3));
        }
        Channel channel = new Channel(params.get(0), params.get(1), CommandsConstants.contact_1, CommandsConstants.description_1, "", authentication);
        Iterator<Channel> groups = sepp.getObservedGroups().iterator();
        Channel group;
        while (groups.hasNext()) {
            group = groups.next();
            if (group.getId().equalsIgnoreCase(channel.getId())) {
                sepp.joinGroup(group.getId(), credentials);
                return;
            }
        }
    }
}
