package net.sf.drftpd.master.command.plugins;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import net.sf.drftpd.DuplicateElementException;
import net.sf.drftpd.ObjectNotFoundException;
import net.sf.drftpd.SlaveUnavailableException;
import net.sf.drftpd.master.BaseFtpConnection;
import net.sf.drftpd.master.FtpRequest;
import net.sf.drftpd.master.command.CommandManager;
import net.sf.drftpd.master.command.CommandManagerFactory;
import org.drftpd.commands.CommandHandler;
import org.drftpd.commands.CommandHandlerFactory;
import org.drftpd.commands.ImproperUsageException;
import org.drftpd.commands.Reply;
import org.drftpd.commands.ReplyPermissionDeniedException;
import org.drftpd.commands.UnhandledCommandException;
import org.drftpd.dynamicdata.KeyNotFoundException;
import org.drftpd.master.RemoteSlave;
import org.drftpd.plugins.SiteBot;
import org.drftpd.slave.SlaveStatus;
import org.tanesha.replacer.ReplacerEnvironment;
import com.Ostermiller.util.StringTokenizer;

/**
 * @author mog
 * @author zubov
 * @version $Id: SlaveManagement.java 1764 2007-08-04 02:01:21Z tdsoul $
 */
public class SlaveManagement implements CommandHandler, CommandHandlerFactory {

    public void unload() {
    }

    public void load(CommandManagerFactory initializer) {
    }

    private Reply doSITE_KICKSLAVE(BaseFtpConnection conn) {
        if (!conn.getUserNull().isAdmin()) {
            return Reply.RESPONSE_530_ACCESS_DENIED;
        }
        if (!conn.getRequest().hasArgument()) {
            return Reply.RESPONSE_501_SYNTAX_ERROR;
        }
        RemoteSlave rslave;
        try {
            rslave = conn.getGlobalContext().getSlaveManager().getRemoteSlave(conn.getRequest().getArgument());
        } catch (ObjectNotFoundException e) {
            return new Reply(200, "No such slave");
        }
        if (!rslave.isOnline()) {
            return new Reply(200, "Slave is already offline");
        }
        rslave.setOffline("Slave kicked by " + conn.getUserNull().getName());
        return Reply.RESPONSE_200_COMMAND_OK;
    }

    /**
     * Lists all slaves used by the master
     * USAGE: SITE SLAVES
     */
    private Reply doSITE_SLAVES(BaseFtpConnection conn) throws ReplyPermissionDeniedException {
        boolean showMore = conn.getRequest().hasArgument() && (conn.getRequest().getArgument().equalsIgnoreCase("more"));
        if (showMore && !conn.getUserNull().isAdmin()) {
            throw new ReplyPermissionDeniedException();
        }
        Collection slaves = conn.getGlobalContext().getSlaveManager().getSlaves();
        Reply response = new Reply(200, "OK, " + slaves.size() + " slaves listed.");
        for (Iterator iter = conn.getGlobalContext().getSlaveManager().getSlaves().iterator(); iter.hasNext(); ) {
            RemoteSlave rslave = (RemoteSlave) iter.next();
            if (showMore) {
                response.addComment(rslave.moreInfo());
            }
            ReplacerEnvironment env = new ReplacerEnvironment();
            env.add("slave", rslave.getName());
            try {
                SlaveStatus status = rslave.getSlaveStatusAvailable();
                SiteBot.fillEnvSlaveStatus(env, status, conn.getGlobalContext().getSlaveManager());
                response.addComment(conn.jprintf(SlaveManagement.class, "slaves", env));
            } catch (SlaveUnavailableException e) {
                response.addComment(conn.jprintf(SlaveManagement.class, "slaves.offline", env));
            }
        }
        return response;
    }

    private Reply doSITE_REMERGE(BaseFtpConnection conn) {
        if (!conn.getUserNull().isAdmin()) {
            return Reply.RESPONSE_530_ACCESS_DENIED;
        }
        if (!conn.getRequest().hasArgument()) {
            return Reply.RESPONSE_501_SYNTAX_ERROR;
        }
        RemoteSlave rslave;
        try {
            rslave = conn.getGlobalContext().getSlaveManager().getRemoteSlave(conn.getRequest().getArgument());
        } catch (ObjectNotFoundException e) {
            return new Reply(200, "No such slave");
        }
        if (!rslave.isAvailable()) {
            return new Reply(200, "Slave is still merging from initial connect");
        }
        try {
            rslave.fetchRemergeResponseFromIndex(rslave.issueRemergeToSlave(conn.getCurrentDirectory().getPath()));
        } catch (IOException e) {
            rslave.setOffline("IOException during remerge()");
            return new Reply(200, "IOException during remerge()");
        } catch (SlaveUnavailableException e) {
            rslave.setOffline("Slave Unavailable during remerge()");
            return new Reply(200, "Slave Unavailable during remerge()");
        }
        return Reply.RESPONSE_200_COMMAND_OK;
    }

    /**
     * Usage: site slave slavename [set,addmask,delmask]
     * @throws ImproperUsageException
     */
    private Reply doSITE_SLAVE(BaseFtpConnection conn) throws ImproperUsageException {
        if (!conn.getUserNull().isAdmin()) {
            return Reply.RESPONSE_530_ACCESS_DENIED;
        }
        Reply response = new Reply(200);
        ReplacerEnvironment env = new ReplacerEnvironment();
        FtpRequest ftpRequest = conn.getRequest();
        if (!ftpRequest.hasArgument()) {
            throw new ImproperUsageException();
        }
        String argument = ftpRequest.getArgument();
        StringTokenizer arguments = new StringTokenizer(argument);
        if (!arguments.hasMoreTokens()) {
            throw new ImproperUsageException();
        }
        String slavename = arguments.nextToken();
        env.add("slavename", slavename);
        RemoteSlave rslave = null;
        try {
            rslave = conn.getGlobalContext().getSlaveManager().getRemoteSlave(slavename);
        } catch (ObjectNotFoundException e) {
            response.addComment(conn.jprintf(SlaveManagement.class, "slave.notfound", env));
            return response;
        }
        if (!arguments.hasMoreTokens()) {
            if (!rslave.getMasks().isEmpty()) {
                env.add("masks", rslave.getMasks());
                response.addComment(conn.jprintf(SlaveManagement.class, "slave.masks", env));
            }
            response.addComment(conn.jprintf(SlaveManagement.class, "slave.data.header", env));
            Map props = rslave.getProperties();
            for (Iterator iter = props.keySet().iterator(); iter.hasNext(); ) {
                Object key = iter.next();
                Object value = props.get(key);
                env.add("key", key);
                env.add("value", value);
                response.addComment(conn.jprintf(SlaveManagement.class, "slave.data", env));
            }
            return response;
        }
        String command = arguments.nextToken();
        if (command.equalsIgnoreCase("set")) {
            if (arguments.countTokens() != 2) {
                throw new ImproperUsageException();
            }
            String key = arguments.nextToken();
            String value = arguments.nextToken();
            rslave.setProperty(key, value);
            env.add("key", key);
            env.add("value", value);
            response.addComment(conn.jprintf(SlaveManagement.class, "slave.set.success", env));
            return response;
        } else if (command.equalsIgnoreCase("unset")) {
            if (arguments.countTokens() != 1) {
                throw new ImproperUsageException();
            }
            String key = arguments.nextToken();
            env.add("key", key);
            String value;
            try {
                value = rslave.removeProperty(key);
            } catch (KeyNotFoundException e) {
                response.addComment(conn.jprintf(SlaveManagement.class, "slave.unset.failure", env));
                return response;
            }
            env.add("value", value);
            response.addComment(conn.jprintf(SlaveManagement.class, "slave.unset.success", env));
            return response;
        } else if (command.equalsIgnoreCase("addmask")) {
            if (arguments.countTokens() != 1) {
                throw new ImproperUsageException();
            }
            String mask = arguments.nextToken();
            env.add("mask", mask);
            try {
                rslave.addMask(mask);
                response.addComment(conn.jprintf(SlaveManagement.class, "slave.addmask.success", env));
                return response;
            } catch (DuplicateElementException e) {
                return new Reply(501, conn.jprintf(SlaveManagement.class, "slave.addmask.dupe", env));
            }
        } else if (command.equalsIgnoreCase("delmask")) {
            if (arguments.countTokens() != 1) {
                throw new ImproperUsageException();
            }
            String mask = arguments.nextToken();
            env.add("mask", mask);
            if (rslave.removeMask(mask)) {
                return new Reply(200, conn.jprintf(SlaveManagement.class, "slave.delmask.success", env));
            }
            return new Reply(501, conn.jprintf(SlaveManagement.class, "slave.delmask.failed", env));
        }
        throw new ImproperUsageException();
    }

    public Reply execute(BaseFtpConnection conn) throws UnhandledCommandException, ReplyPermissionDeniedException, ImproperUsageException {
        String cmd = conn.getRequest().getCommand();
        if ("SITE KICKSLAVE".equals(cmd)) {
            return doSITE_KICKSLAVE(conn);
        }
        if ("SITE SLAVES".equals(cmd)) {
            return doSITE_SLAVES(conn);
        }
        if ("SITE REMERGE".equals(cmd)) {
            return doSITE_REMERGE(conn);
        }
        if ("SITE SLAVE".equals(cmd)) {
            return doSITE_SLAVE(conn);
        }
        if ("SITE ADDSLAVE".equals(cmd)) {
            return doSITE_ADDSLAVE(conn);
        }
        if ("SITE DELSLAVE".equals(cmd)) {
            return doSITE_DELSLAVE(conn);
        }
        throw UnhandledCommandException.create(SlaveManagement.class, conn.getRequest());
    }

    private Reply doSITE_DELSLAVE(BaseFtpConnection conn) throws ImproperUsageException {
        if (!conn.getUserNull().isAdmin()) {
            return Reply.RESPONSE_530_ACCESS_DENIED;
        }
        Reply response = new Reply(200);
        ReplacerEnvironment env = new ReplacerEnvironment();
        FtpRequest ftpRequest = conn.getRequest();
        if (!ftpRequest.hasArgument()) {
            throw new ImproperUsageException();
        }
        String argument = ftpRequest.getArgument();
        StringTokenizer arguments = new StringTokenizer(argument);
        if (!arguments.hasMoreTokens()) {
            throw new ImproperUsageException();
        }
        String slavename = arguments.nextToken();
        env.add("slavename", slavename);
        try {
            conn.getGlobalContext().getSlaveManager().getRemoteSlave(slavename);
        } catch (ObjectNotFoundException e) {
            response.addComment(conn.jprintf(SlaveManagement.class, "delslave.notfound", env));
            return response;
        }
        conn.getGlobalContext().getSlaveManager().delSlave(slavename);
        response.addComment(conn.jprintf(SlaveManagement.class, "delslave.success", env));
        return response;
    }

    private Reply doSITE_ADDSLAVE(BaseFtpConnection conn) throws ImproperUsageException {
        if (!conn.getUserNull().isAdmin()) {
            return Reply.RESPONSE_530_ACCESS_DENIED;
        }
        Reply response = new Reply(200);
        ReplacerEnvironment env = new ReplacerEnvironment();
        FtpRequest ftpRequest = conn.getRequest();
        if (!ftpRequest.hasArgument()) {
            throw new ImproperUsageException();
        }
        StringTokenizer arguments = new StringTokenizer(ftpRequest.getArgument());
        if (!arguments.hasMoreTokens()) {
            throw new ImproperUsageException();
        }
        String slavename = arguments.nextToken();
        env.add("slavename", slavename);
        if (arguments.hasMoreTokens()) {
            throw new ImproperUsageException();
        }
        try {
            conn.getGlobalContext().getSlaveManager().getRemoteSlave(slavename);
            return new Reply(501, conn.jprintf(SlaveManagement.class, "addslave.exists"));
        } catch (ObjectNotFoundException e) {
        }
        conn.getGlobalContext().getSlaveManager().newSlave(slavename);
        response.addComment(conn.jprintf(SlaveManagement.class, "addslave.success", env));
        return response;
    }

    public CommandHandler initialize(BaseFtpConnection conn, CommandManager initializer) {
        return this;
    }

    public String[] getFeatReplies() {
        return null;
    }
}
