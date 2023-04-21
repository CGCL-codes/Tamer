package org.drftpd.plugins;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import net.sf.drftpd.FatalException;
import net.sf.drftpd.ObjectNotFoundException;
import net.sf.drftpd.event.DirectoryFtpEvent;
import net.sf.drftpd.event.Event;
import net.sf.drftpd.event.FtpListener;
import org.apache.log4j.Logger;
import org.drftpd.GlobalContext;
import org.drftpd.PropertyHelper;
import org.drftpd.master.ConnectionManager;
import f00f.net.irc.martyr.GenericCommandAutoService;
import f00f.net.irc.martyr.InCommand;
import f00f.net.irc.martyr.commands.MessageCommand;

/**
 * @version $Id: PreTime.java 1764 2007-08-04 02:01:21Z tdsoul $
 */
public class PreTime extends FtpListener {

    private static final Logger logger = Logger.getLogger(PreTime.class);

    private ConnectionManager _cm;

    private SiteBot _irc;

    private PreSiteBot _siteBot;

    private ArrayList datedDirs;

    private String prebot;

    public PreTime() throws FileNotFoundException, IOException {
        super();
    }

    public void actionPerformed(Event event) {
        if ("RELOAD".equals(event.getCommand())) {
            reload();
            return;
        }
        if (!(event instanceof DirectoryFtpEvent)) {
            return;
        }
        DirectoryFtpEvent dfe = (DirectoryFtpEvent) event;
        if (!getConnectionManager().getGlobalContext().getConfig().checkPathPermission("dirlog", dfe.getUser(), dfe.getDirectory())) {
            return;
        }
        try {
            if (dfe.getCommand().startsWith("MKD")) {
                String[] release = dfe.getDirectory().getPath().split("/");
                String releaseName;
                if (isDatedDir(release[1])) {
                    releaseName = release[3];
                    if (release.length > 4) {
                        return;
                    }
                } else {
                    releaseName = release[2];
                    if (release.length > 3) {
                        return;
                    }
                }
                if (releaseName == null) {
                    return;
                }
                _irc.getIRCConnection().sendCommand(new MessageCommand(getPreBot(), "!pred " + releaseName));
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
        }
    }

    public ConnectionManager getConnectionManager() {
        return _cm;
    }

    public String getPreBot() {
        return prebot;
    }

    public void init(GlobalContext gctx) {
        super.init(gctx);
        reload();
    }

    public boolean isDatedDir(String section) {
        for (Iterator iter = datedDirs.iterator(); iter.hasNext(); ) {
            if (((String) iter.next()).equals(section)) {
                return true;
            }
        }
        return false;
    }

    private void reload() {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("conf/pretime.conf"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        prebot = PropertyHelper.getProperty(props, "prebot");
        if (prebot == null) {
            throw new FatalException("prebot not set in prebot.conf");
        }
        datedDirs = new ArrayList();
        for (int i = 1; ; i++) {
            String temp = null;
            try {
                temp = PropertyHelper.getProperty(props, "DatedDir." + i);
            } catch (NullPointerException e2) {
                break;
            }
            datedDirs.add(temp);
        }
        if (_siteBot != null) {
            _siteBot.disable();
        }
        try {
            _irc = (SiteBot) _cm.getGlobalContext().getFtpListener(PreSiteBot.class);
            _siteBot = new PreSiteBot(_irc, this);
        } catch (ObjectNotFoundException e1) {
            logger.warn("Error loading sitebot component", e1);
        }
    }

    public void unload() {
    }

    public static class PreSiteBot extends GenericCommandAutoService {

        private SiteBot _irc;

        private PreTime _parent;

        protected PreSiteBot(SiteBot irc, PreTime parent) {
            super(irc.getIRCConnection());
            _irc = irc;
            _parent = parent;
        }

        protected void updateCommand(InCommand command) {
            if (!(command instanceof MessageCommand)) {
                return;
            }
            MessageCommand msgc = (MessageCommand) command;
            if (msgc.getSource().getNick().equals(_parent.getPreBot())) {
                if (msgc.isPrivateToUs(_irc.getIRCConnection().getClientState())) {
                    String[] msg = msgc.getMessage().split(" ");
                    if (msg[0].equals("!preds")) {
                        String releaseName = msg[1];
                        int releaseTime = Integer.parseInt(msg[2]);
                        int weeks = releaseTime / 604800;
                        releaseTime = releaseTime % 604800;
                        int days = releaseTime / 86400;
                        releaseTime = releaseTime % 86400;
                        int hours = releaseTime / 3600;
                        releaseTime = releaseTime % 3600;
                        int minutes = releaseTime / 60;
                        int seconds = releaseTime % 60;
                        String time = "-=PRETiME=- " + releaseName + " was pred ";
                        if (weeks != 0) {
                            time = time + weeks + " weeks ";
                        }
                        if (days != 0) {
                            time = time + days + " days ";
                        }
                        if (hours != 0) {
                            time = time + hours + " hours ";
                        }
                        if (minutes != 0) {
                            time = time + minutes + " minutes ";
                        }
                        if (seconds != 0) {
                            time = time + seconds + " seconds ";
                        }
                        _irc.say("", time + "ago");
                    }
                }
            }
        }
    }
}
