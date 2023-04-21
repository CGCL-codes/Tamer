package net.moep.ircservices.ejb;

import javax.ejb.Local;
import javax.ejb.Remote;
import net.moep.irc.ServerEvent;
import net.moep.ircservices.par.common.ReturnValue;

@Remote
@Local
public interface IrcServer {

    public ReturnValue handleEvent(ServerEvent event);
}
