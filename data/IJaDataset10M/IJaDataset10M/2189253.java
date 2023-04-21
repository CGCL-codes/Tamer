package de.z8bn.ircg;

import java.io.*;
import java.net.*;
import java.util.*;
import org.jibble.pircbot.*;
import java.util.regex.*;
import de.z8bn.ircg.conf.*;

/**
 *
 * @author  Administrator
 */
public class IrcG {

    private final LinkedList<String> commands = new LinkedList<String>(Arrays.asList(new String[] { "IDENTIFY", "GET", "FIND", "GFIND", "GET", "SERVEME" }));

    private TaskPool taskPool = new TaskPool();

    private Map<String, ChatTask> incomingDCCMap = new HashMap<String, ChatTask>();

    private Map<String, FServDownloadTask> incomingFileTransferMap = new HashMap<String, FServDownloadTask>();

    private BotConfig cfg;

    private Map<String, ServerData> serversettings = new HashMap<String, ServerData>();

    private Map<String, BotUser> identities = new HashMap<String, BotUser>();

    private Map<String, User> usermap = new HashMap<String, User>();

    private File dest;

    private Timer timer = new Timer(true);

    private Map<String, PircBotImpl> ircbots = new HashMap<String, PircBotImpl>();

    /** Creates a new instance of IrcG */
    public IrcG(BotConfig cfg) {
        this.cfg = cfg;
        dest = new File(cfg.getDestinationDirectory());
        if (!dest.exists()) dest.mkdirs();
        if (!dest.isDirectory()) {
            System.err.println("The specified destination directory is unuseable");
            System.exit(3);
        }
        for (BotUser u : cfg.getUsers()) identities.put(u.getName(), u);
        for (ServerConfig sc : cfg.getServers()) {
            ServerData data = new ServerData();
            serversettings.put(sc.getName(), data);
            try {
                data.theBot = new PircBotImpl(sc, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private enum DCCCONTYPES {

        CRAWL_THREAD, CHECK_THREAD
    }

    ;

    private class PircBotImpl extends PircBot {

        private String DEFAULT_USER;

        private String serverName;

        private HashMap<Trigger, FservInf> fservs = new HashMap<Trigger, FservInf>();

        private HashMap<String, Trigger> triggermap = new HashMap<String, Trigger>();

        private LinkedHashSet<Trigger> fservTriggers = new LinkedHashSet<Trigger>();

        private LinkedList<String> ccontacted = new LinkedList<String>();

        private HashMap<String, Trigger> tgmap = new HashMap<String, Trigger>();

        private HashMap<String, LinkedList<Trigger>> waitingTriggers = new HashMap<String, LinkedList<Trigger>>();

        private HashMap<FFile, Trigger> queuedFiles = new HashMap<FFile, Trigger>();

        private HashMap<Trigger, DCCCONTYPES> contypemap = new HashMap<Trigger, DCCCONTYPES>();

        private LinkedList<QueueElement> sendQueue = new LinkedList<QueueElement>();

        private LinkedList<QueueElement> waitQueue = new LinkedList<QueueElement>();

        private LinkedList<QueueElement> queuedQueue = new LinkedList<QueueElement>();

        private Map<String, ChannelConfig> channelMap = new HashMap<String, ChannelConfig>();

        private ServerConfig cfg;

        private ServerData settings;

        private Map<ChannelConfig, Long> channelStamps = new HashMap<ChannelConfig, Long>();

        private Map<String, XDCCPack> xdccpeers = new HashMap<String, XDCCPack>();

        private Map<String, XDCCPack> xdccpacks = new HashMap<String, XDCCPack>();

        private Map<String, LinkedList<XDCCPack>> waitingPacks = new HashMap<String, LinkedList<XDCCPack>>();

        private TriggerFinder tfinder;

        public PircBotImpl(ServerConfig cfg, ServerData settings) throws Exception {
            this.cfg = cfg;
            this.settings = settings;
            setAutoNickChange(true);
            setName(cfg.getNickname());
            setLogin(cfg.getNickname());
            setFinger("IRCG v0.1");
            setVersion("IRCG v0.1");
            serverName = cfg.getHost() + ":" + cfg.getPort();
            String password = cfg.getServerPassword();
            if (password != null && password.length() != 0) connect(cfg.getHost(), cfg.getPort(), password); else connect(cfg.getHost(), cfg.getPort());
            IrcG.User def;
            synchronized (usermap) {
                DEFAULT_USER = uhname("", "", "", serverName + "$");
                while (usermap.containsKey(DEFAULT_USER)) DEFAULT_USER += "$";
                usermap.put(DEFAULT_USER, def = new IrcG.User(this, DEFAULT_USER));
            }
            def.addEventHandler(new CommandHandler());
            def.addEventHandler(tfinder = new TriggerFinder());
            taskPool.scheduleTaskWithFixedDelay(new TriggerTask(), 5000, 120000, "CHANNELMSGS");
            taskPool.scheduleTaskWithFixedDelay(new IdentExpiration(), 600000, 120000, TaskPool.QUEUE_ASYNC);
        }

        public String getServerName() {
            return serverName;
        }

        public String getDefaultUserName() {
            return DEFAULT_USER;
        }

        protected void onJoin(String channel, String sender, String login, String hostname) {
            if (getNick().equals(sender)) {
            }
        }

        protected void onKick(String channels, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
            if (getNick() == recipientNick) {
                ChannelConfig channel = channelMap.get(channels);
                String pass = channel.getKey();
                if (pass != null && pass.length() != 0) joinChannel(channel.getName(), pass); else joinChannel(channel.getName());
            }
        }

        protected void onServerResponse(int code, String response) {
            switch(code) {
                case ERR_BADCHANNELKEY:
                case ERR_BANNEDFROMCHAN:
                    break;
                default:
                    super.onServerResponse(code, response);
            }
        }

        protected void onMessage(String channel, String sender, String login, String hostname, String message) {
            String inline = Colors.removeFormattingAndColors(message);
            MessageEvent me = new MessageEvent(MessageEvent.MSGTYPE.CHANNEL, serverName, channel, sender, login, hostname, inline);
            handleMessageEvent(me);
        }

        protected void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
            if (getNick().equals(target)) {
                String inline = Colors.removeFormattingAndColors(notice);
                MessageEvent me = new MessageEvent(MessageEvent.MSGTYPE.NOTICE, serverName, null, sourceNick, sourceLogin, sourceHostname, inline);
                handleMessageEvent(me);
            }
        }

        protected void onPrivateMessage(String sender, String login, String hostname, String message) {
            String inline = Colors.removeFormattingAndColors(message);
            MessageEvent me = new MessageEvent(MessageEvent.MSGTYPE.PRIVMSG, serverName, null, sender, login, hostname, inline);
            handleMessageEvent(me);
        }

        private void handleMessageEvent(MessageEvent e) {
            User u = usermap.get(uhname(e));
            if (u == null) u = usermap.get(DEFAULT_USER);
            u.handleEvent(e);
        }

        protected void onIncomingChatRequest(DccChat chat) {
            String peer = uhname(chat.getNick(), chat.getLogin(), chat.getHostname(), serverName);
            if (incomingDCCMap.containsKey(peer)) {
                ChatTask ct = incomingDCCMap.get(peer);
                synchronized (ct) {
                    ct.setChat(chat);
                    ct.notify();
                }
            }
        }

        protected void onConnect() {
            settings.triggers = fservTriggers;
            settings.xdccpacks = xdccpacks;
            String nspw = cfg.getNickServPassword();
            if (nspw != null && nspw.length() != 0) {
                if (!getNick().equals(cfg.getNickname())) {
                }
                if (getNick().equals(cfg.getNickname())) {
                    sendMessage("nickserv", "IDENTIFY " + nspw);
                }
            }
            for (ChannelConfig chan : cfg.getChannels()) {
                channelMap.put(chan.getName(), chan);
                channelStamps.put(chan, new Long(0));
                String pass = chan.getKey();
                if (pass != null && pass.length() != 0) joinChannel(chan.getName(), pass); else joinChannel(chan.getName());
            }
        }

        protected void onIncomingFileTransfer(DccFileTransfer transfer) {
            String peer = uhname(transfer.getNick(), transfer.getLogin(), transfer.getHostname(), serverName);
            if (incomingFileTransferMap.containsKey(peer)) {
                FServDownloadTask d = incomingFileTransferMap.get(peer);
                if (d.isAcceptable(transfer)) d.handleFileTransfer(transfer);
            }
        }

        protected void onFileTransferFinished(DccFileTransfer transfer, Exception e) {
            String peer = uhname(transfer.getNick(), transfer.getLogin(), transfer.getHostname(), serverName);
            if (incomingFileTransferMap.containsKey(peer)) {
                FServDownloadTask d = incomingFileTransferMap.get(peer);
                d.handleFileTransferEnd(transfer, e);
            }
        }

        protected void onPart(String channels, String sender, String login, String hostname) {
            if (getNick() == sender) {
                ChannelConfig channel = channelMap.get(channels);
                String pass = channel.getKey();
                if (pass != null && pass.length() != 0) joinChannel(channel.getName(), pass); else joinChannel(channel.getName());
            }
        }

        private class TriggerTask extends Task {

            public Boolean call() {
                long now = System.currentTimeMillis();
                for (ChannelConfig c : channelMap.values()) {
                    if (c.isListAllowed()) if (Arrays.asList(settings.theBot.getChannels()).contains(c.getName()) && now > c.getListStamp() + 14400000) {
                        c.setListStamp(now);
                        settings.theBot.sendMessage(c.getName(), "!list");
                        tfinder.setListChannel(c.getName());
                        synchronized (this) {
                            try {
                                wait(120000);
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                }
                return true;
            }
        }
    }

    private class IdentExpiration extends Task {

        public Boolean call() {
            long now = System.currentTimeMillis();
            synchronized (usermap) {
                for (User u : usermap.values()) {
                    if (u.identity != null) if (now - u.identity.lastactive > 3600000) u.identity = null;
                }
            }
            return true;
        }
    }

    private class ServeThread extends Thread {

        private DccChat c;

        private FFile directory;

        private String server, channel;

        private Trigger trigger;

        private FservInf fsi;

        private XDCCPack pack;

        private int dirlvl = 0;

        private PircBot b;

        private String other;

        private boolean isFserv = true;

        public ServeThread(PircBot b, String nick) {
            setDaemon(true);
            this.c = c;
            other = nick;
            this.b = b;
        }

        public void run() {
            String inline;
            try {
                c = b.dccSendChatRequest(other, 120000);
                c.accept();
                c.sendLine("IRCG V0.01a");
                c.sendLine("Welcome to the party!");
                c.sendLine("[\\]");
                while ((inline = c.readLine()) != null) {
                    inline = inline.trim();
                    if (inline.length() == 0) continue;
                    String[] parts = inline.split("\\s+", 2);
                    if (parts[0].equalsIgnoreCase("exit")) {
                        c.close();
                        return;
                    } else if (parts[0].equalsIgnoreCase("help")) {
                        if (parts.length == 2) {
                            c.sendLine("There is no help for this command");
                        } else {
                            c.sendLine("Understood commands:");
                            c.sendLine("HELP   Lists all available commands");
                            c.sendLine("DIR    Lists all files in the current directory");
                            c.sendLine("CD     Changes the current directory");
                            c.sendLine("GET    Queues a file for retrival");
                            c.sendLine("STATS  Shows the stats of the selected fserv");
                            c.sendLine("FIND   Finds a file on this server");
                            c.sendLine("GFIND  Finds a file on all servers");
                            c.sendLine("QUEUES Lists all queued transfers");
                            c.sendLine("SENDS  Lists all ongoing transfers");
                            c.sendLine("DUMPDB Sends a list with all db entries");
                        }
                    } else if (parts[0].equalsIgnoreCase("dumpdb")) {
                        File tmp = File.createTempFile("dbdump", ".xml");
                        tmp.deleteOnExit();
                        dumpServers(tmp);
                        c.sendLine("Get ready to receive the file!");
                        b.dccSendFile(tmp, other, 120000);
                    } else if (parts[0].equalsIgnoreCase("queues")) {
                        QueueHolder qh = serversettings.get(b.getServer() + ":" + b.getPort()).queues;
                        synchronized (qh.queuedQueue) {
                            c.sendLine("Queued Files:");
                            c.sendLine("Remotely queued:");
                            for (QueueElement qe : qh.queuedQueue) {
                                c.sendLine(qe.toString());
                            }
                        }
                        synchronized (qh.waitQueue) {
                            c.sendLine("Locally queued:");
                            for (QueueElement qe : qh.waitQueue) {
                                c.sendLine(qe.toString());
                            }
                        }
                    } else if (parts[0].equalsIgnoreCase("sends")) {
                        QueueHolder qh = serversettings.get(b.getServer() + ":" + b.getPort()).queues;
                        synchronized (qh.sendQueue) {
                            c.sendLine("Ongoing Transfers:");
                            for (QueueElement qe : qh.sendQueue) {
                                c.sendLine(qe.toString());
                            }
                        }
                    } else if (parts[0].equalsIgnoreCase("get")) {
                        if (parts.length == 1) {
                            c.sendLine("Please specify a file for GET");
                        } else if (parts[1].startsWith("\\")) {
                            c.sendLine("Absolute GETs are not yet supported");
                        } else if (parts[1].indexOf("\\") != -1) {
                            c.sendLine("Relative GETs(that span directories) are not yet supported");
                        } else {
                            QueueHolder qh = serversettings.get(server).queues;
                            FFile cld = null;
                            String udc = parts[1].trim();
                            if (isFserv) {
                                if (dirlvl < 3) {
                                    c.sendLine("Please specify a file or directory (and not a whole server, channel or trigger");
                                    continue;
                                }
                                if (dirlvl == 3) {
                                    System.err.println(udc);
                                    for (FFile f : fsi.rootfiles) {
                                        System.err.println(f.getName());
                                        if (f.getName().equals(udc)) {
                                            System.err.print(".");
                                            cld = f;
                                            break;
                                        }
                                    }
                                } else for (FFile f : directory.getChildren()) if (f.getName().equals(udc)) {
                                    cld = f;
                                    break;
                                }
                                if (cld != null) {
                                    LinkedList<QueueElement> res = new LinkedList<QueueElement>();
                                    recget(res, cld, trigger);
                                    synchronized (qh.waitQueue) {
                                        qh.waitQueue.addAll(res);
                                    }
                                    c.sendLine("File(s) sucessfully queued");
                                } else {
                                    c.sendLine("File not found");
                                }
                            } else {
                                if (dirlvl != 3) {
                                    c.sendLine("Please specify a file (and not a whole server, channel or pack");
                                    continue;
                                }
                                for (FFile f : pack.files.keySet()) if (f.getName().equals(udc)) synchronized (qh.waitQueue) {
                                    QueueElement qe = new QueueElement(f, pack);
                                    qh.waitQueue.add(qe);
                                }
                            }
                        }
                    } else if (parts[0].equalsIgnoreCase("stats")) {
                        if (dirlvl < 1) {
                            c.sendLine("Please CD to the target server first!");
                            continue;
                        }
                        if (parts.length == 1) {
                            c.sendLine("Please specify the fserv to list!");
                            continue;
                        }
                        Trigger tmp = null;
                        String udc = unescape(parts[1]);
                        for (Trigger t : serversettings.get(server).triggers) if (t.toString().equals(udc) && t.getChannel().equals(channel)) tmp = t;
                        if (tmp != null) {
                            FservInf tfsi = tmp.getFServInformation();
                            BufferedReader bru = new BufferedReader(new StringReader(tfsi.toString()));
                            String inlt;
                            while ((inlt = bru.readLine()) != null) c.sendLine(inlt);
                            bru.close();
                        } else {
                            c.sendLine("this Trigger / fserv does not exist");
                        }
                    } else if (parts[0].equalsIgnoreCase("dir")) {
                        c.sendLine("IRCG V0.01a");
                        HashSet<String> reslines = new HashSet<String>();
                        if (parts.length == 2 && parts[1].indexOf("\\") != -1) {
                            c.sendLine("The pattern may not contain directory paths");
                            continue;
                        }
                        String pts = parts.length > 1 ? parts[1] : ".*";
                        Matcher m;
                        try {
                            m = Pattern.compile(pts).matcher("");
                        } catch (PatternSyntaxException e) {
                            c.sendLine("The Pattern that you specified did not compile");
                            continue;
                        }
                        if (pts.equals(".*")) pts = "*.*";
                        String outline, uesc;
                        switch(dirlvl) {
                            case 0:
                                c.sendLine("[\\" + pts + "]");
                                for (String key : serversettings.keySet()) {
                                    m.reset(key);
                                    if (m.matches()) c.sendLine(escape(key));
                                }
                                break;
                            case 1:
                                c.sendLine("[" + pathForLvl(dirlvl) + "\\" + pts + "]");
                                for (String channel : getChannels(server)) {
                                    m.reset(channel);
                                    if (m.matches()) c.sendLine(escape(channel));
                                }
                                break;
                            case 2:
                                c.sendLine("[" + pathForLvl(dirlvl) + "\\" + pts + "]");
                                for (Trigger trigger : getTriggers(server, channel)) {
                                    m.reset(uesc = trigger.toString());
                                    if (m.matches()) c.sendLine(escape(uesc));
                                }
                                break;
                            case 3:
                                c.sendLine("[" + pathForLvl(dirlvl) + "\\" + pts + "]");
                                if (isFserv) for (FFile file : fsi.rootfiles) {
                                    m.reset(uesc = file.getName());
                                    if (m.matches()) c.sendLine(uesc);
                                } else for (FFile file : pack.files.keySet()) {
                                    m.reset(uesc = file.getName());
                                    if (m.matches()) c.sendLine(uesc);
                                }
                                break;
                            default:
                                c.sendLine("[" + pathForLvl(dirlvl) + "\\" + pts + "]");
                                for (FFile f : directory.getChildren()) {
                                    m.reset(uesc = f.getName());
                                    if (m.matches()) c.sendLine(uesc);
                                }
                        }
                        c.sendLine("End of list");
                    } else if (parts[0].equalsIgnoreCase("find")) {
                        HashMap<Trigger, LinkedList<FFile>> res = find(b.getServer() + ":" + b.getPort(), parts[1]);
                        c.sendLine("IRCG V0.01a");
                        c.sendLine("search for " + parts[1]);
                        for (Trigger t : res.keySet()) {
                            LinkedList<FFile> files = res.get(t);
                            c.sendLine(t.getUser() + "@" + t.getChannel() + ": " + t.getTrigger());
                            c.sendLine("==========");
                            for (FFile f : files) c.sendLine(f.toString());
                            c.sendLine("----------");
                        }
                        c.sendLine("End of results");
                    } else if (parts[0].equalsIgnoreCase("gfind")) {
                        HashMap<String, HashMap<Trigger, LinkedList<FFile>>> res = gfind(parts[1]);
                        c.sendLine("IRCG V0.01a");
                        c.sendLine("search for " + parts[1]);
                        for (String s : res.keySet()) {
                            HashMap<Trigger, LinkedList<FFile>> tres = res.get(s);
                            c.sendLine("Server: " + s);
                            c.sendLine("<========>");
                            for (Trigger t : tres.keySet()) {
                                LinkedList<FFile> files = tres.get(t);
                                c.sendLine(t.getUser() + "@" + t.getChannel() + ": " + t.getTrigger());
                                c.sendLine("==========");
                                for (FFile f : files) c.sendLine(f.toString());
                                c.sendLine("----------");
                            }
                        }
                        c.sendLine("End of results");
                    } else if (parts[0].equalsIgnoreCase("cd")) {
                        c.sendLine("IRCG V0.01a");
                        String outline;
                        if (parts[1].indexOf("\\") != -1) {
                            if (parts[1].startsWith("\\")) {
                                boolean exists = true;
                                String oc = channel, os = server;
                                int odlvl = dirlvl;
                                Trigger ot = trigger;
                                FservInf ofsi = fsi;
                                FFile odir = directory;
                                XDCCPack opk = pack;
                                boolean oifs = isFserv;
                                dirlvl = 0;
                                String[] dp = parts[1].substring(1).split("\\\\", 4);
                                String udc;
                                if (dp.length != 1 || dp[0].trim().length() != 0) if (dp.length > 0) {
                                    udc = unescape(dp[0]);
                                    if (serversettings.containsKey(udc)) {
                                        server = udc;
                                        dirlvl++;
                                        if (dp.length > 1) {
                                            udc = unescape(dp[1]);
                                            if (getChannels(server).contains(udc)) {
                                                channel = udc;
                                                dirlvl++;
                                                if (dp.length > 2) {
                                                    trigger = null;
                                                    pack = null;
                                                    for (Trigger t : serversettings.get(server).triggers) if (t.toString().equals(udc = unescape(dp[2])) && t.getChannel().equals(channel)) trigger = t;
                                                    if (trigger != null) {
                                                        fsi = trigger.getFServInformation();
                                                        dirlvl++;
                                                        if (dp.length > 3) {
                                                            udc = unescape(dp[3]);
                                                            if (udc.endsWith("\\")) udc = udc.substring(0, udc.length());
                                                            directory = null;
                                                            for (FFile f : fsi.files) if (f.toString().equals(udc)) directory = f;
                                                            if (directory != null && directory.isDirectory()) {
                                                                isFserv = true;
                                                                for (FFile tmp = directory; tmp != null; tmp = tmp.getParent()) dirlvl++;
                                                            } else exists = false;
                                                        }
                                                    } else {
                                                        for (XDCCPack p : serversettings.get(server).xdccpacks.values()) if (p.packname.equals(udc = unescape(dp[2])) && p.channel.equals(channel)) pack = p;
                                                        if (pack != null) {
                                                            dirlvl++;
                                                            isFserv = false;
                                                            if (dp.length > 3) exists = false;
                                                        } else exists = false;
                                                    }
                                                }
                                            } else exists = false;
                                        }
                                    } else exists = false;
                                }
                                if (exists) {
                                    c.sendLine("[" + pathForLvl(dirlvl) + "]");
                                } else {
                                    channel = oc;
                                    server = os;
                                    dirlvl = odlvl;
                                    fsi = ofsi;
                                    trigger = ot;
                                    directory = odir;
                                    pack = opk;
                                    isFserv = oifs;
                                    c.sendLine("File not found");
                                }
                            } else {
                                c.sendLine("File not found(relative addressing (with \\) not yet supported)");
                            }
                        } else if (parts[1].equals("..")) {
                            if (dirlvl > 0) {
                                dirlvl--;
                                if (dirlvl > 3) directory = directory.getParent();
                                c.sendLine("[" + pathForLvl(dirlvl) + "]");
                            } else c.sendLine("File not found");
                        } else {
                            String udc;
                            switch(dirlvl) {
                                case 0:
                                    udc = unescape(parts[1]);
                                    if (serversettings.containsKey(udc)) {
                                        server = udc;
                                        dirlvl++;
                                        c.sendLine("[" + pathForLvl(dirlvl) + "]");
                                    } else {
                                        c.sendLine("File not found");
                                    }
                                    break;
                                case 1:
                                    udc = unescape(parts[1]);
                                    if (getChannels(server).contains(udc)) {
                                        channel = udc;
                                        dirlvl++;
                                        c.sendLine("[" + pathForLvl(dirlvl) + "]");
                                    } else {
                                        c.sendLine("File not found");
                                    }
                                    break;
                                case 2:
                                    udc = unescape(parts[1]);
                                    trigger = null;
                                    for (Trigger t : serversettings.get(server).triggers) if (t.toString().equals(udc) && t.getChannel().equals(channel)) trigger = t;
                                    if (trigger != null) {
                                        fsi = trigger.getFServInformation();
                                        dirlvl++;
                                        isFserv = true;
                                        c.sendLine("[" + pathForLvl(dirlvl) + "]");
                                    } else {
                                        for (XDCCPack p : serversettings.get(server).xdccpacks.values()) if (p.packname.equals(udc) && p.channel.equals(channel)) pack = p;
                                        if (pack != null) {
                                            dirlvl++;
                                            isFserv = false;
                                            c.sendLine("[" + pathForLvl(dirlvl) + "]");
                                        } else c.sendLine("File not found");
                                    }
                                    break;
                                case 3:
                                    udc = unescape(parts[1]);
                                    directory = null;
                                    for (FFile f : fsi.rootfiles) if (f.getName().equals(udc)) directory = f;
                                    if (directory != null && directory.isDirectory()) {
                                        dirlvl++;
                                        c.sendLine("[" + pathForLvl(dirlvl) + "]");
                                    } else {
                                        c.sendLine("File not found");
                                    }
                                    break;
                                default:
                                    udc = unescape(parts[1]);
                                    FFile tmp = null;
                                    for (FFile f : directory.getChildren()) if (f.getName().equals(udc)) tmp = f;
                                    if (tmp != null && tmp.isDirectory()) {
                                        directory = tmp;
                                        dirlvl++;
                                        c.sendLine("[" + pathForLvl(dirlvl) + "]");
                                    } else {
                                        c.sendLine("File not found");
                                    }
                                    break;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void recget(LinkedList<QueueElement> res, FFile start, Trigger t) {
            if (!start.isDirectory()) res.add(new QueueElement(start, t)); else for (FFile f : start.getChildren()) recget(res, f, t);
        }

        private String pathForLvl(int lvl) {
            String ret = "";
            switch(lvl) {
                default:
                case 3:
                    if (isFserv) ret = "\\" + escape(trigger.toString()); else ret = "\\" + escape(pack.packname);
                case 2:
                    ret = "\\" + escape(channel) + ret;
                case 1:
                    ret = "\\" + escape(server) + ret;
                case 0:
                    ret = ret.equals("") ? "\\" : ret;
            }
            if (lvl > 3) {
                FFile parent = directory;
                String tname = "";
                do {
                    tname = "\\" + escape(parent.getName()) + tname;
                    parent = parent.getParent();
                } while (parent != null);
                ret = ret + tname;
            }
            return ret;
        }

        private Set<String> getChannels(String server) {
            Set<String> res = new HashSet<String>();
            Set<Trigger> triggers = serversettings.get(server).triggers;
            synchronized (triggers) {
                for (Trigger t : triggers) res.add(t.getChannel());
            }
            return res;
        }

        private List<Trigger> getTriggers(String server, String channel) {
            List<Trigger> res = new LinkedList<Trigger>();
            Set<Trigger> triggers = serversettings.get(server).triggers;
            synchronized (triggers) {
                for (Trigger t : triggers) if (t.getChannel().equals(channel)) res.add(t);
            }
            return res;
        }

        private String unescape(String te) {
            try {
                return URLDecoder.decode(te, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return te;
        }

        private String escape(String te) {
            try {
                return URLEncoder.encode(te, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return te;
        }
    }

    private HashMap<Trigger, LinkedList<FFile>> find(String server, String pattern) throws PatternSyntaxException {
        HashMap<Trigger, LinkedList<FFile>> res = new HashMap<Trigger, LinkedList<FFile>>();
        Set<Trigger> triggers = serversettings.get(server).triggers;
        Matcher mt = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher("");
        boolean tss = pattern.contains("\\\\");
        for (Trigger t : triggers) {
            LinkedList<FFile> tmp = new LinkedList<FFile>();
            FservInf fsi = t.getFServInformation();
            for (FFile f : fsi.files) {
                String t2 = tss ? f.toString() : f.getName();
                mt.reset(t2);
                if (mt.find()) tmp.add(f);
            }
            if (!tmp.isEmpty()) res.put(t, tmp);
        }
        return res;
    }

    private HashMap<String, HashMap<Trigger, LinkedList<FFile>>> gfind(String pattern) throws PatternSyntaxException {
        HashMap<String, HashMap<Trigger, LinkedList<FFile>>> res = new HashMap<String, HashMap<Trigger, LinkedList<FFile>>>();
        for (String s : serversettings.keySet()) res.put(s, find(s, pattern));
        return res;
    }

    public class QueueElement {

        public Trigger trigger;

        public FFile file;

        public long lastCheck;

        public DccFileTransfer transfer;

        public boolean isFserv;

        public XDCCPack pack;

        public QueueElement(FFile f, Trigger t) {
            this.file = f;
            this.trigger = t;
            isFserv = true;
        }

        public QueueElement(FFile f, XDCCPack p) {
            this.file = f;
            this.pack = p;
            isFserv = false;
        }

        public String toString() {
            StringBuffer ret = new StringBuffer();
            ret.append(file).append(" on ").append(trigger);
            if (transfer != null) {
                ret.append(" (").append(transfer.getProgressPercentage() * 100).append("% done)");
            }
            return ret.toString();
        }
    }

    public void dumpServers(File out) {
        try {
            StringBuilder sb;
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(out), "UTF-8"));
            int idl = 0;
            pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            for (String server : serversettings.keySet()) {
                sb = new StringBuilder();
                Set<Trigger> tgs = serversettings.get(server).triggers;
                idl++;
                pw.println(indent(idl).append("<server name=\"").append(server).append("\">").toString());
                Map<String, LinkedList<Trigger>> ctr = new HashMap<String, LinkedList<Trigger>>();
                for (Trigger t : tgs) {
                    LinkedList<Trigger> ct = ctr.get(t.getChannel());
                    if (ct == null) {
                        ct = new LinkedList<Trigger>();
                        ctr.put(t.getChannel(), ct);
                    }
                    ct.add(t);
                }
                for (String channel : ctr.keySet()) {
                    idl++;
                    pw.println(indent(idl).append("<channel name=\"").append(channel).append("\">").toString());
                    LinkedList<Trigger> triggers = ctr.get(channel);
                    for (Trigger t : triggers) {
                        idl++;
                        pw.println(indent(idl).append("<trigger text=\"").append(t.getTrigger()).append("\" user=\"").append(t.getUser()).append("\">").toString());
                        for (FFile f : t.getFServInformation().rootfiles) {
                            recPrint(pw, f, idl + 1);
                        }
                        pw.println(indent(idl).append("</trigger>").toString());
                        idl--;
                    }
                    pw.println(indent(idl).append("</channel>").toString());
                    idl--;
                }
                pw.println(indent(idl).append("</server>").toString());
                idl--;
            }
            pw.println("</xml>");
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void recPrint(PrintWriter pw, FFile f, int ident) {
        if (f.isDirectory()) {
            pw.println(indent(ident).append("<directory name=\"").append(f.getName()).append("\">").toString());
            for (FFile t : f.getChildren()) recPrint(pw, t, ident + 1);
            pw.println(indent(ident).append("</directory>").toString());
        } else {
            pw.println(indent(ident).append("<file name=\"").append(f.getName()).append("\" size=\"").append(f.getSize()).append("\" />").toString());
        }
    }

    private StringBuilder indent(int lvl) {
        StringBuilder b = new StringBuilder();
        for (int x = 0; x < lvl; x++) b.append("  ");
        return b;
    }

    private class XDCCPack {

        public long lastcheck;

        public String packname;

        public String channel;

        public String user;

        public Map<FFile, Integer> files = new HashMap<FFile, Integer>();
    }

    private class ServerData {

        public PircBotImpl theBot;

        public QueueHolder queues;

        public LinkedHashSet<Trigger> triggers;

        public Map<String, XDCCPack> xdccpacks;
    }

    private class QueueHolder {

        public LinkedList<QueueElement> queuedQueue;

        public LinkedList<QueueElement> sendQueue;

        public LinkedList<QueueElement> waitQueue;

        public QueueHolder(LinkedList<QueueElement> w, LinkedList<QueueElement> q, LinkedList<QueueElement> s) {
            waitQueue = w;
            sendQueue = s;
            queuedQueue = q;
        }
    }

    public class Identity {

        public BotUser user;

        public String nick, hostname, login;

        public long lastactive;
    }

    public static void main(String[] args) throws Exception {
        File cfgFile = new File(System.getProperty("user.home"), ".ircgconfig");
        if (args.length > 0) for (String arg : args) if (arg.toLowerCase().startsWith("--config=")) cfgFile = new File(arg.substring(arg.indexOf("=" + 1)));
        BotConfig cfg = null;
        if (!cfgFile.exists() || cfgFile.isDirectory()) {
            System.err.println("Unable to open the configuration file, using builtin config");
            cfg = ConfigLoader.loadConfig(IrcG.class.getClassLoader().getResourceAsStream("de/z8bn/ircg/defaultconfig.xml"));
        } else try {
            cfg = ConfigLoader.loadConfig(cfgFile);
        } catch (Exception e) {
            System.err.println("An error occured while parsing the configuration file");
            System.exit(2);
        }
        IrcG ig = new IrcG(cfg);
    }

    private interface EventHandler {

        /** returns the priority of this Handler
         * Higher values imply a  higher priority
         */
        public int getPriority();

        /** returns true if this Event is interesting
         */
        public boolean isInteresting(Event e);

        /** handles an event
         * returns true if this event has been "caught"
         */
        public boolean handleEvent(User u, Event e, PircBotImpl impl);
    }

    private interface Event {
    }

    private static class MessageEvent implements Event {

        public enum MSGTYPE {

            PRIVMSG, NOTICE, CHANNEL
        }

        ;

        public MSGTYPE type;

        public String server, channel, sender, login, hostname, message;

        public MessageEvent(MSGTYPE type, String server, String channel, String sender, String login, String hostname, String message) {
            this.type = type;
            this.server = server;
            this.channel = channel;
            this.sender = sender;
            this.login = login;
            this.hostname = hostname;
            this.message = message;
        }
    }

    private String uhname(MessageEvent me) {
        return uhname(me.sender, me.login, me.hostname, me.server);
    }

    private String uhname(String user, String login, String host, String server) {
        return user + "!" + login + "@" + host + "#" + server;
    }

    private class FServDownloadTask extends ChatTask {

        private List<FFile> files = new LinkedList<FFile>();

        private Map<DccFileTransfer, FFile> tfmap = new HashMap<DccFileTransfer, FFile>();

        private Trigger trigger;

        private boolean isCanceled = false;

        private DccChat chat;

        private PircBotImpl impl;

        public void setChat(DccChat c) {
            synchronized (this) {
                chat = c;
                this.notify();
            }
        }

        public FServDownloadTask(PircBotImpl impl, Trigger t, FFile... files) {
            this.files.addAll(Arrays.asList(files));
            this.trigger = t;
            this.impl = impl;
        }

        public Boolean call() {
            boolean res = true;
            try {
                synchronized (files) {
                    if (!files.isEmpty()) {
                        List<FFile> queue = new LinkedList<FFile>(files);
                        queue.removeAll(tfmap.values());
                        if (!queue.isEmpty()) {
                            String peer = uhname(trigger.getUser(), trigger.getLogin(), trigger.getHost(), impl.getServerName());
                            try {
                                synchronized (incomingDCCMap) {
                                    incomingDCCMap.put(peer, this);
                                    impl.sendCTCPCommand(trigger.getUser(), trigger.getTrigger());
                                }
                                synchronized (this) {
                                    while (chat == null) try {
                                        wait();
                                    } catch (InterruptedException e) {
                                    }
                                }
                            } finally {
                                synchronized (incomingDCCMap) {
                                    incomingDCCMap.remove(peer);
                                }
                            }
                            chat.accept();
                            BufferedReader bur = chat.getBufferedReader();
                            String inline = bur.readLine();
                            FservInterface fsi = null;
                            if (inline == null) {
                                return false;
                            }
                            if (inline.matches(".*SysReset.*")) {
                                System.err.println("This must be a sysreset server:" + inline);
                                fsi = new SysResetFservInterface(chat, impl.getNick());
                            } else {
                                System.err.println("Unhandled Fserv: " + inline);
                                return false;
                            }
                            Transfer[] transfers = fsi.queues();
                            for (Transfer t : transfers) {
                                if (t.getOwner().equals(impl.getNick())) for (Iterator<FFile> i = queue.iterator(); i.hasNext(); ) {
                                    FFile f = i.next();
                                    if (f.getName().equalsIgnoreCase(t.getFile().getName())) i.remove();
                                }
                            }
                            for (FFile f : queue) {
                                fsi.get(f);
                            }
                            fsi.exit();
                            res = true;
                            return res;
                        }
                    } else {
                        res = false;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                res = false;
            } finally {
                isCanceled = !res;
            }
            return res;
        }

        public boolean addDownload(FFile file) {
            synchronized (files) {
                if (isCanceled) return false;
                files.add(file);
            }
            return true;
        }

        public List<FFile> getDownloadQueue() {
            synchronized (files) {
                return Collections.unmodifiableList(files);
            }
        }

        public boolean isCanceled() {
            return isCanceled;
        }

        public boolean isAcceptable(DccFileTransfer trans) {
            for (FFile f : files) if (f.getName().equalsIgnoreCase(trans.getFile().getName())) return true;
            return false;
        }

        public void handleFileTransfer(DccFileTransfer trans) {
            for (FFile f : files) if (f.getName().equalsIgnoreCase(trans.getFile().getName())) {
                tfmap.put(trans, f);
                trans.receive(dest, true);
            }
        }

        public void handleFileTransferEnd(DccFileTransfer trans, Exception e) {
            if (e == null) {
                synchronized (files) {
                    FFile f = tfmap.remove(trans);
                    files.remove(f);
                }
            } else {
                tfmap.remove(trans);
            }
        }
    }

    private abstract class ChatTask extends Task {

        public abstract void setChat(DccChat c);
    }

    private class CrawlerTask extends ChatTask {

        private Trigger trigger;

        private DccChat chat;

        private boolean needsDccServer = false;

        private PircBotImpl impl;

        private FservInterface fsi = null;

        public void setChat(DccChat c) {
            synchronized (this) {
                chat = c;
                this.notify();
                this.impl = impl;
            }
        }

        public CrawlerTask(Trigger t, PircBotImpl impl) {
            trigger = t;
            this.impl = impl;
        }

        public Boolean call() {
            impl.sendCTCPCommand(trigger.getUser(), trigger.getTrigger());
            String user = uhname(trigger.getUser(), trigger.getLogin(), trigger.getHost(), impl.getServerName());
            incomingDCCMap.put(user, this);
            User u = usermap.get(user);
            if (u == null) usermap.put(user, u = usermap.get(impl.getDefaultUserName()).clone(trigger.getUser(), impl));
            EventHandler eh;
            u.addEventHandler(eh = new EventHandler() {

                public int getPriority() {
                    return 500;
                }

                public boolean isInteresting(Event e) {
                    if (e instanceof MessageEvent && ((MessageEvent) e).type == MessageEvent.MSGTYPE.PRIVMSG) return true;
                    return false;
                }

                public boolean handleEvent(User u, Event e, PircBotImpl impl) {
                    MessageEvent me = (MessageEvent) e;
                    if (me.message.toLowerCase().contains("dccserver")) {
                        CrawlerTask.this.needsDccServer = true;
                        return true;
                    }
                    return false;
                }
            });
            try {
                synchronized (this) {
                    long start = System.currentTimeMillis();
                    try {
                        wait(20000);
                    } catch (InterruptedException e) {
                    }
                    if (needsDccServer) {
                        trigger.setNeedsDccServer(true);
                        return false;
                    } else if (chat == null) {
                        return false;
                    }
                }
            } finally {
                incomingDCCMap.remove(user);
                u.removeEventHandler(eh);
            }
            trigger.setLastCrawled(System.currentTimeMillis());
            try {
                chat.accept();
                BufferedReader bur = chat.getBufferedReader();
                String inline = bur.readLine();
                if (inline == null) {
                    return false;
                }
                if (inline.matches(".*SysReset.*")) {
                    System.err.println("This must be a sysreset server:" + inline);
                    fsi = new SysResetFservInterface(chat, impl.getNick());
                } else {
                    System.err.println("Unhandled Fserv: " + inline);
                    return false;
                }
                System.err.println("now Crawling on " + trigger.getUser() + " // " + trigger.getTrigger());
                LinkedList<FFile> res = new LinkedList<FFile>();
                FservInf inf = trigger.getFServInformation();
                inf.rootfiles = recdir(res, null);
                inf.files = res;
                inf.curqueue = fsi.currentqueueCount();
                inf.maxqueue = fsi.queueCount();
                inf.cursend = fsi.currentsendsCount();
                inf.maxsend = fsi.sendsCount();
                inf.myqueues = fsi.queues();
                inf.mysends = fsi.sends();
                inf.trigger = trigger;
                fsi.exit();
                System.err.println(inf);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        private LinkedList<FFile> recdir(LinkedList<FFile> res, FFile parent) throws IOException {
            FFile[] tasks = fsi.dir();
            LinkedList<FFile> rootnodes = null;
            if (parent == null) {
                rootnodes = new LinkedList<FFile>();
            }
            for (FFile f : tasks) {
                f.setParent(parent);
                if (parent == null) rootnodes.add(f);
                res.add(f);
                if (f.isDirectory()) {
                    if (fsi.cd(f)) {
                        recdir(res, f);
                        fsi.cd("..");
                    } else System.err.println("trying to change to not existing directory:" + f);
                }
            }
            return rootnodes;
        }
    }

    /** handles basic commands */
    private class CommandHandler implements EventHandler {

        public int getPriority() {
            return 0;
        }

        public boolean isInteresting(Event e) {
            if (e instanceof MessageEvent && ((MessageEvent) e).type == MessageEvent.MSGTYPE.PRIVMSG) return true;
            return false;
        }

        public boolean handleEvent(User u, Event e, PircBotImpl impl) {
            if (!(e instanceof MessageEvent)) return false;
            MessageEvent me = (MessageEvent) e;
            String msg = me.message;
            msg = msg.trim();
            int pos = msg.indexOf(" ");
            String[] replies = null;
            String cmd;
            String arg;
            if (pos == -1) {
                cmd = msg;
                arg = "";
            } else {
                cmd = msg.substring(0, pos).trim();
                arg = msg.substring(pos).trim();
            }
            if (!commands.contains(cmd.toUpperCase())) {
                return false;
            } else {
                if (cmd.equalsIgnoreCase("IDENTIFY")) {
                    String[] args = arg.split("\\s+");
                    if (args.length != 2) {
                        replies = new String[] { "Invalid Syntax for command IDENTIFY" };
                    } else if (identities.containsKey(args[0])) {
                        BotUser bu = identities.get(args[0]);
                        if (bu.getPassword().equals(args[1])) {
                            if (!u.name.equals(me.sender)) {
                                u = u.clone(me.sender, impl);
                                usermap.put(uhname(me), u);
                            }
                            Identity i = new Identity();
                            i.user = bu;
                            i.lastactive = System.currentTimeMillis();
                            i.nick = me.sender;
                            i.hostname = me.hostname;
                            i.login = me.login;
                            u.identity = i;
                            u.addEventHandler(new IDUpdateHandler());
                            replies = new String[] { "Welcome to the Brotherhood *g*" };
                        } else {
                            replies = new String[] { "Invalid credentials" };
                        }
                    } else {
                        replies = new String[] { "Invalid credentials" };
                    }
                } else if (cmd.equalsIgnoreCase("SERVEME")) {
                    Identity idt = u.identity;
                    if (idt != null && idt.hostname.equals(me.hostname) && idt.login.equals(me.login)) {
                        new ServeThread(impl, me.sender).start();
                        replies = new String[] { "Connecting to you ..." };
                    } else replies = new String[] { "Please IDENTIFY yourself first" };
                } else if (cmd.equalsIgnoreCase("GET")) {
                } else if (cmd.equalsIgnoreCase("FIND")) {
                } else if (cmd.equalsIgnoreCase("GFIND")) {
                }
            }
            if (replies.length == 0) return false;
            for (String r : replies) impl.sendNotice(me.sender, r);
            return true;
        }
    }

    /** handles the id update of authenticated users
     */
    private class IDUpdateHandler implements EventHandler {

        public int getPriority() {
            return 1000;
        }

        public boolean isInteresting(Event e) {
            if (e instanceof MessageEvent) return true;
            return false;
        }

        public boolean handleEvent(User u, Event e, PircBotImpl impl) {
            if (u.identity != null) u.identity.lastactive = System.currentTimeMillis();
            return false;
        }
    }

    /** Finds Triggers in Messages
     */
    private class TriggerFinder implements EventHandler {

        private String channel;

        public void setListChannel(String c) {
            channel = c;
        }

        public int getPriority() {
            return 500;
        }

        public boolean isInteresting(Event e) {
            if (e instanceof MessageEvent) return true;
            return false;
        }

        public boolean handleEvent(User u, Event e, PircBotImpl impl) {
            MessageEvent me = (MessageEvent) e;
            String inline = me.message;
            String[] tgs = PatternUtil.findFservTriggers(inline);
            if (tgs.length > 0) {
                synchronized (impl.fservTriggers) {
                    for (String tg : tgs) {
                        Trigger t = Trigger.parseTrigger(tg, channel);
                        t.setHost(me.hostname);
                        t.setLogin(me.login);
                        for (org.jibble.pircbot.User pu : Arrays.asList(impl.getUsers(channel))) if (pu.getNick().equals(t.getUser())) {
                            impl.fservTriggers.add(t);
                            taskPool.scheduleTaskWithFixedDelay(new CrawlerTask(t, impl), 5000, 86400000, uhname(me));
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    private class User {

        public Identity identity;

        private String name;

        private TreeSet<EventHandler> handlers = new TreeSet<EventHandler>(Collections.reverseOrder(new Comparator<EventHandler>() {

            public int compare(EventHandler o1, EventHandler o2) {
                return o1.getPriority() - o2.getPriority();
            }
        }));

        private PircBotImpl impl;

        public User(PircBotImpl impl, String name) {
            this.impl = impl;
            this.name = name;
        }

        public void addEventHandler(EventHandler h) {
            synchronized (handlers) {
                handlers.add(h);
            }
        }

        public boolean removeEventHandler(EventHandler h) {
            synchronized (handlers) {
                return handlers.remove(h);
            }
        }

        public void handleEvent(Event e) {
            synchronized (handlers) {
                for (EventHandler h : handlers) if (h.isInteresting((e))) if (h.handleEvent(this, e, impl)) break;
            }
        }

        /** creates a copy of this User with another name
         **/
        public User clone(String user, PircBotImpl impl) {
            User ret = new User(impl, user);
            ret.handlers = new TreeSet<EventHandler>(handlers);
            ret.identity = null;
            return ret;
        }
    }
}
