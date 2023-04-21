package sf2.io.impl.nio;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import sf2.core.Config;
import sf2.core.ConfigException;
import sf2.core.Event;
import sf2.io.MessageException;
import sf2.io.MessageServer;
import sf2.io.event.ConnEvent;
import sf2.io.event.ConnFailedEvent;
import sf2.io.event.RecvEvent;
import sf2.io.event.RecvFailedEvent;
import sf2.io.impl.MessageCommon;
import sf2.io.impl.StreamObjectInputStream;
import sf2.log.Logging;

public class NioMessageServer implements MessageServer, MessageCommon {

    protected Map<Integer, BlockingQueue<Event>> bindMap = new HashMap<Integer, BlockingQueue<Event>>();

    protected BlockingQueue<SelectableChannel> workerQueue = new LinkedBlockingQueue<SelectableChannel>();

    protected List<CanceledEntry> canceled = new LinkedList<CanceledEntry>();

    protected Map<SocketChannel, StreamObjectInputStream> streamMap = new HashMap<SocketChannel, StreamObjectInputStream>();

    protected List<BlockingQueue<Event>> failQueues = new LinkedList<BlockingQueue<Event>>();

    protected Selector selector;

    protected ServerSocketChannel serverChannel;

    protected DatagramChannel bcastChannel;

    protected InetAddress serverAddr;

    protected Logging logging;

    protected ExecutorService acceptExecutor;

    protected ExecutorService workerExecutor;

    protected boolean keepSilent;

    protected String prefix;

    protected int port, bcastPort;

    protected int numWorkers;

    protected int maxBcastLength;

    public NioMessageServer() {
    }

    public void configure(boolean prioHigh) throws MessageException {
        try {
            logging = Logging.getInstance();
            Config config = Config.search();
            numWorkers = config.getInt(PROP_NUM_SERVER_WORKERS, DEFAULT_NUM_SERVER_WORKERS);
            maxBcastLength = config.getInt(PROP_MAX_BCAST_LENGTH, DEFAULT_MAX_BCAST_LENGTH);
            keepSilent = config.getBoolean(PROP_KEEP_SILENT, DEFAULT_KEEP_SILENT);
            if (prioHigh) {
                port = config.getInt(PROP_PRIO_PORT, DEFAULT_PRIO_PORT);
                bcastPort = config.getInt(PROP_PRIO_UDP_PORT, DEFAULT_PRIO_UDP_PORT);
            } else {
                port = config.getInt(PROP_PORT, DEFAULT_PORT);
                bcastPort = config.getInt(PROP_UDP_PORT, DEFAULT_UDP_PORT);
            }
            boolean cleanup = config.getBoolean(PROP_CLEANUP_STORE_PATH, DEFAULT_CLEANUP_STORE_PATH);
            prefix = config.get(PROP_SERVER_STORE_PATH, DEFAULT_SERVER_STORE_PATH);
            if (!prioHigh) {
                File target = new File(prefix);
                if (cleanup) {
                    logging.config(LOG_NAME, "NIO server: cleanup the store path");
                    recursiveDelete(target);
                }
                if (!target.exists()) target.mkdirs();
            }
            if (config.has(PROP_BIND_ADDR)) {
                serverAddr = InetAddress.getByName(config.get(PROP_BIND_ADDR));
                logging.config(LOG_NAME, "NIO server: bind to " + serverAddr);
            } else if (config.has(PROP_BIND_NIC)) {
                String nic = config.get(PROP_BIND_NIC);
                boolean preferIPv4 = config.getBoolean(PROP_BIND_PREFER_IPV4, DEFAULT_BIND_PREFER_IPV4);
                Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
                while (e.hasMoreElements()) {
                    NetworkInterface ni = e.nextElement();
                    if (nic.equals(ni.getName())) {
                        Enumeration<InetAddress> e2 = ni.getInetAddresses();
                        while (e2.hasMoreElements()) {
                            InetAddress addr = e2.nextElement();
                            if ((preferIPv4 && addr instanceof Inet4Address) || (!preferIPv4 && addr instanceof Inet6Address)) {
                                serverAddr = addr;
                                logging.config(LOG_NAME, "bind to NIC " + nic + " (addr=" + serverAddr + ")");
                            }
                        }
                        break;
                    }
                }
                if (serverAddr == null) {
                    throw new MessageException("bindAddr or bindNIC not found. ");
                }
            } else {
                serverAddr = InetAddress.getLocalHost();
            }
            logging.config(LOG_NAME, "NIO server: port=" + port + " (" + (prioHigh ? "high" : "normal") + ")");
            logging.config(LOG_NAME, "NIO server: updPort=" + bcastPort + " (" + (prioHigh ? "high" : "normal") + ")");
            logging.config(LOG_NAME, "NIO server: numWorkers=" + numWorkers);
            logging.config(LOG_NAME, "NIO server: maxBcastLength=" + maxBcastLength);
            logging.config(LOG_NAME, "NIO server: cleanupStorePath=" + cleanup);
            logging.config(LOG_NAME, "NIO server: storePath=" + prefix);
            logging.config(LOG_NAME, "NIO server: keepSilent=" + keepSilent);
        } catch (ConfigException e) {
            throw new MessageException(e);
        } catch (UnknownHostException e) {
            throw new MessageException(e);
        } catch (SocketException e) {
            throw new MessageException(e);
        }
    }

    protected void recursiveDelete(File file) {
        if (file.isDirectory()) {
            File[] childs = file.listFiles();
            if (childs != null) {
                for (File f : childs) recursiveDelete(f);
            }
        }
        file.delete();
    }

    public void start() throws MessageException {
        createSelector();
        createServerChannel();
        createBroadcastChannel();
        createWorkers();
        logging.config(LOG_NAME, "NIO server: start");
    }

    public void shutdown() {
        try {
            logging.warning(LOG_NAME, "shutdown() is not tested.");
            acceptExecutor.shutdown();
            workerExecutor.shutdown();
            for (SelectionKey key : selector.keys()) {
                key.channel().close();
            }
            serverChannel.close();
        } catch (IOException e) {
            if (keepSilent) logging.warning(LOG_NAME, "failed on shutdown"); else e.printStackTrace();
        }
    }

    public void addConnMonitor(BlockingQueue<Event> queue) {
        synchronized (failQueues) {
            failQueues.add(queue);
        }
    }

    public void removeFailureMonitor(BlockingQueue<Event> queue) {
        synchronized (failQueues) {
            failQueues.remove(queue);
        }
    }

    protected void createSelector() throws MessageException {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            throw new MessageException(e);
        }
    }

    protected void createServerChannel() throws MessageException {
        try {
            serverChannel = ServerSocketChannel.open();
            serverChannel.socket().bind(new InetSocketAddress(serverAddr, port));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            throw new MessageException(e);
        }
    }

    protected void createBroadcastChannel() throws MessageException {
        try {
            bcastChannel = DatagramChannel.open();
            bcastChannel.socket().bind(new InetSocketAddress(bcastPort));
            bcastChannel.socket().setBroadcast(true);
            bcastChannel.configureBlocking(false);
            bcastChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            throw new MessageException(e);
        }
    }

    protected void createWorkers() {
        workerExecutor = Executors.newFixedThreadPool(numWorkers);
        for (int i = 0; i < numWorkers; i++) {
            workerExecutor.execute(new WorkerTask());
        }
        acceptExecutor = Executors.newSingleThreadExecutor();
        acceptExecutor.execute(new AccpetTask(this));
    }

    public void bind(int tag, BlockingQueue<Event> q) {
        synchronized (bindMap) {
            bindMap.put(tag, q);
        }
        logging.config(LOG_NAME, "NIO server: bind name=" + tag);
    }

    public void unbind(int tag) {
        synchronized (bindMap) {
            bindMap.remove(tag);
        }
        logging.config(LOG_NAME, "NIO server: UNbind name=" + tag);
    }

    public InetAddress getLocalHost() throws UnknownHostException {
        return serverAddr;
    }

    class AccpetTask implements Runnable {

        protected MessageServer server;

        public AccpetTask(MessageServer server) {
            this.server = server;
        }

        public void run() {
            try {
                while (true) {
                    int ret = selector.select();
                    registerCanceledKeys();
                    if (ret > 0) {
                        for (Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i.hasNext(); ) {
                            SelectionKey k = i.next();
                            i.remove();
                            processKey(k);
                        }
                    }
                }
            } catch (IOException e) {
                if (keepSilent) logging.warning(LOG_NAME, "accept task error"); else e.printStackTrace();
            }
        }

        protected void processKey(SelectionKey key) {
            try {
                if (key.isAcceptable()) {
                    SocketChannel channel = serverChannel.accept();
                    if (channel == null) return;
                    logging.debug(LOG_NAME, "NIO server: accept " + channel);
                    synchronized (canceled) {
                        canceled.add(new CanceledEntry(channel));
                    }
                    synchronized (streamMap) {
                        streamMap.put(channel, new StreamObjectInputStream(channel.socket().getInputStream(), channel, server, prefix));
                    }
                    workerQueue.put(channel);
                    ConnEvent ce = new ConnEvent(channel.socket().getInetAddress());
                    for (BlockingQueue<Event> q : failQueues) q.put(ce);
                } else if (key.isReadable()) {
                    key.cancel();
                    SelectableChannel channel = key.channel();
                    synchronized (canceled) {
                        canceled.add(new CanceledEntry(channel));
                    }
                    channel.configureBlocking(true);
                    workerQueue.put(channel);
                } else {
                    logging.debug(LOG_NAME, "key is in unknown state.");
                }
            } catch (IOException e) {
                logging.debug(LOG_NAME, "NIO server: close conn from=" + ((SocketChannel) key.channel()).socket().getInetAddress());
                try {
                    if (!keepSilent) e.printStackTrace();
                    key.cancel();
                    SelectableChannel channel = key.channel();
                    synchronized (streamMap) {
                        streamMap.remove(channel);
                    }
                    if (channel instanceof SocketChannel) {
                        ConnFailedEvent fe = new ConnFailedEvent(((SocketChannel) channel).socket().getInetAddress());
                        synchronized (failQueues) {
                            for (BlockingQueue<Event> q : failQueues) q.put(fe);
                        }
                    }
                    channel.close();
                } catch (IOException e2) {
                } catch (InterruptedException e2) {
                }
            } catch (InterruptedException e) {
            }
        }

        protected void registerCanceledKeys() {
            synchronized (canceled) {
                for (Iterator<CanceledEntry> i = canceled.iterator(); i.hasNext(); ) {
                    CanceledEntry e = i.next();
                    e.setCleaned();
                    if (e.canRegister()) {
                        SelectableChannel channel = e.getChannel();
                        try {
                            channel.configureBlocking(false);
                            channel.register(selector, SelectionKey.OP_READ);
                        } catch (IOException ex) {
                            try {
                                if (channel instanceof SocketChannel) {
                                    ConnFailedEvent fe = new ConnFailedEvent(((SocketChannel) channel).socket().getInetAddress());
                                    synchronized (failQueues) {
                                        for (BlockingQueue<Event> q : failQueues) q.put(fe);
                                    }
                                }
                                channel.close();
                            } catch (IOException ex2) {
                            } catch (InterruptedException ex2) {
                            }
                        }
                        i.remove();
                    }
                }
            }
        }
    }

    class WorkerTask implements Runnable {

        public void run() {
            try {
                while (true) {
                    SelectableChannel channel = workerQueue.take();
                    if (channel instanceof SocketChannel) {
                        handleRequest((SocketChannel) channel);
                    } else if (channel instanceof DatagramChannel) {
                        handleBroadcast((DatagramChannel) channel);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        protected void handleRequest(SocketChannel channel) {
            try {
                StreamObjectInputStream in = null;
                synchronized (streamMap) {
                    in = streamMap.get(channel);
                }
                int tag = (Integer) in.readUnshared();
                Object msg = in.readUnshared();
                in.readFutures();
                BlockingQueue<Event> destQ = null;
                synchronized (bindMap) {
                    destQ = bindMap.get(tag);
                }
                InetAddress sender = channel.socket().getInetAddress();
                logging.debug(LOG_NAME, "NIO server: RECEIVE from=" + sender + ", destName=" + tag + ", msg=" + msg);
                if (destQ != null) {
                    destQ.put(new RecvEvent(sender, false, tag, msg));
                } else {
                    logging.severe(LOG_NAME, "NioMessageServer - unbound message!  sender=" + sender + ", name=" + tag + ", msg=" + msg);
                }
                setProcessed(channel);
                selector.wakeup();
            } catch (IOException e) {
                discardChannel(e, channel, true);
            } catch (ClassNotFoundException e) {
                discardChannel(e, channel, true);
            } catch (InterruptedException e) {
            }
        }

        protected void discardChannel(Exception e, SocketChannel channel, boolean outputTrace) {
            try {
                if (outputTrace && !keepSilent) e.printStackTrace();
                logging.debug(LOG_NAME, "NIO server: close conn from=" + channel.socket().getInetAddress());
                ConnFailedEvent fe = new ConnFailedEvent(channel.socket().getInetAddress());
                for (BlockingQueue<Event> q : failQueues) q.put(fe);
                synchronized (streamMap) {
                    streamMap.remove(channel);
                }
                synchronized (canceled) {
                    for (Iterator<CanceledEntry> i = canceled.iterator(); i.hasNext(); ) {
                        CanceledEntry ce = i.next();
                        if (ce.getChannel().equals(channel)) {
                            i.remove();
                            break;
                        }
                    }
                }
                channel.close();
            } catch (IOException e2) {
            } catch (InterruptedException e2) {
            }
        }

        protected void setProcessed(SelectableChannel channel) {
            synchronized (canceled) {
                for (CanceledEntry e : canceled) {
                    if (e.getChannel().equals(channel)) {
                        e.setProcessed();
                    }
                }
            }
        }

        protected void handleBroadcast(DatagramChannel channel) {
            try {
                byte[] buf = new byte[maxBcastLength];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                channel.socket().receive(packet);
                ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(packet.getData(), packet.getOffset(), packet.getLength()));
                int tag = (Integer) in.readUnshared();
                Object msg = in.readUnshared();
                InetAddress sender = packet.getAddress();
                logging.debug(LOG_NAME, "NIO server: BROADCAST from=" + sender + ", tag=" + tag + ", msg=" + msg);
                BlockingQueue<Event> destQ = null;
                synchronized (bindMap) {
                    destQ = bindMap.get(tag);
                }
                if (destQ != null) {
                    destQ.put(new RecvEvent(sender, true, tag, msg));
                } else {
                    logging.severe(LOG_NAME, " unbounded object - tag=" + tag + ", msg=" + msg);
                }
                setProcessed(channel);
                selector.wakeup();
            } catch (IOException e) {
                if (keepSilent) logging.warning(LOG_NAME, "broadcast failed on I/O"); else e.printStackTrace();
            } catch (ClassNotFoundException e) {
                if (keepSilent) logging.warning(LOG_NAME, "broadcast failed on classNotFound"); else e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class CanceledEntry {

        protected SelectableChannel channel;

        protected boolean cleaned;

        protected boolean processed;

        public CanceledEntry(SelectableChannel channel) {
            this.channel = channel;
            processed = false;
            cleaned = false;
        }

        public SelectableChannel getChannel() {
            return channel;
        }

        public void setProcessed() {
            processed = true;
        }

        public void setCleaned() {
            cleaned = true;
        }

        public boolean canRegister() {
            return cleaned && processed;
        }
    }
}
