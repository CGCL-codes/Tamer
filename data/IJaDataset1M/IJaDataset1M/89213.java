package com.thegreatchina.im.msn.backend.cmd.server;

import org.apache.log4j.Logger;
import com.thegreatchina.im.msn.backend.cmd.ServerCommand;

public class ServerCommandFactory {

    private static Logger logger = Logger.getLogger(ServerCommandFactory.class);

    private static ServerCommandFactory factory = new ServerCommandFactory();

    private ServerCommandFactory() {
    }

    public static ServerCommandFactory getServerCommandFactory() {
        return factory;
    }

    public static final String IGNORE_COMMAND[] = new String[] { "BPR ", "NOT ", "PRP ", "REM " };

    public static final String EXPIRED_COMMAND[] = new String[] { "ILN " };

    private FLN fln = new FLN();

    private NLN nln = new NLN();

    private RNG rng = new RNG();

    private CHL chl = new CHL();

    private BYE bye = new BYE();

    private IncomingMessage msg = new IncomingMessage();

    private NotificationData notiData = new NotificationData();

    private NOTIFICATION noti = new NOTIFICATION();

    private OUT_OTH oth = new OUT_OTH();

    private ADD_RL arl = new ADD_RL();

    private REM_RL rrl = new REM_RL();

    private XFR_NS xfr = new XFR_NS();

    private LST lst = new LST();

    private LSG lsg = new LSG();

    private ILN iln = new ILN();

    public ServerCommand getServerCommand(String line) {
        logger.debug("ServerCommandFactory.getServerCommand(");
        if (fln.isCommand(line)) {
            return new FLN();
        } else if (nln.isCommand(line)) {
            return new NLN();
        } else if (rng.isCommand(line)) {
            return new RNG();
        } else if (msg.isCommand(line)) {
            return new IncomingMessage();
        } else if (chl.isCommand(line)) {
            return new CHL();
        } else if (notiData.isCommand(line)) {
            return new NotificationData();
        } else if (noti.isCommand(line)) {
            return new NOTIFICATION();
        } else if (oth.isCommand(line)) {
            return new OUT_OTH();
        } else if (arl.isCommand(line)) {
            return new ADD_RL();
        } else if (rrl.isCommand(line)) {
            return new REM_RL();
        } else if (bye.isCommand(line)) {
            return new BYE();
        } else if (xfr.isCommand(line)) {
            return new XFR_NS();
        } else if (lst.isCommand(line)) {
            return new LST();
        } else if (lsg.isCommand(line)) {
            return new LSG();
        } else if (iln.isCommand(line)) {
            return new ILN();
        }
        return null;
    }

    public String ignore(String line) {
        for (String cmd : IGNORE_COMMAND) {
            if (line.indexOf(cmd) == 0) {
                return null;
            }
        }
        return line;
    }
}
