package net.sf.hibernate4gwt.sample.client.command;

import net.sf.hibernate4gwt.sample.client.core.ApplicationParameters;
import com.google.gwt.user.client.Command;

/**
 * Force message list refresh
 * @author bruno.marchesson
 *
 */
public class RefreshMessageListCommand implements Command {

    public void execute() {
        ApplicationParameters.getInstance().getApplication().getMessageBoard().refresh();
    }
}
