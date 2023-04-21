package com.elibera.gateway.app;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.SessionFactory;
import com.elibera.gateway.elements.RedirectClient;
import com.elibera.gateway.elements.UpdateBlockingDataThread;
import com.elibera.gateway.elements.UpdateServerEntryThread;
import com.elibera.gateway.elements.WerbungThread;
import com.elibera.gateway.threading.ServerIO;
import com.elibera.gateway.threading.ServerThreadExecutor;
import com.elibera.util.Log;

/**
 * @author meisi
 *
 */
public class Server {

    public static int MAX_CACHE_OF_RSS = 1000 * 60 * 1;

    public static int MAX_CLIENTS_ONLINE = 5000;

    public static String[] ALTERNATIVE_SERVERS = {};

    public static String GATEWAY_NAME = "eLibera";

    public static String GATEWAY_VERSION = "1";

    public static boolean USE_DB_LOGGING = false;

    public static boolean USE_DB = false;

    public static boolean USE_DB_ADVERTISING = false;

    public static boolean USE_CHECK_FOR_BLOCKED_USERS = false;

    public static boolean USE_CHECK_FOR_DB_SERVER_ENTRIES = false;

    public static HashMap<String, ResourceBundle> msgs = new HashMap<String, ResourceBundle>();

    public static ServerThreadExecutor executor;

    public static int MAX_SIZE_FOR_SOCKET_QUEUE = 5000;

    public static int MAX_SIZE_FOR_CLIENT_OBJECT_POOL = 5100;

    public static int STARTING_SIZE_FOR_CLIENT_OBJECT_POOL = 4000;

    public static int MIN_CLIENT_OBJECTS_IN_POOL = 10000;

    public static int MAX_PROCESSING_THREADS = 50;

    public static int SIZE_OF_DB_SAVING_QUEUE = 5000;

    public static int SIZE_OF_DB_MERGE_QUEUE = 5000;

    public static int SIZE_OF_DB_DELETING_QUEUE = 500;

    public static int MAX_DB_SAVING_THREADS = 5;

    public static int MAX_DB_DELETING_THREADS = 1;

    public static int MAX_DB_MERGING_THREADS = 2;

    public static int MAX_REQUEST_THREADS = 1;

    public static boolean BYTE_PREPARSER_ACTIVATE = true;

    public static boolean BYTE_PREPARSER_COMPRESS_PREPARSED = true, BYTE_PREPARSER_USE_FOR_ALL_ZIP_PACKAGES = true;

    public static int MINIMUM_CLIENT_VERSION = 1;

    public static int BLOCKING_USER_CALCULATIONS_PER_DAY = 1;

    public static int SERVER_ENTRY_CALCULATIONS_PER_DAY = 1;

    /**
	 * gibt an wie lange für einen Response aus Client Sicht gewartet wird
	 */
    public static int MAX_TIME_PENDING_RESPONSE = 2 * 60 * 1000;

    public static String[] FORCE_BLOCK_HTTP_URLS = null, FORCE_VALID_HTTP_URLS = null, FORCE_VALID_PLATFORM_SERVER_URLS = null;

    public static void startServer(int port, ResourceBundle bundle) {
        try {
            SessionFactory sessionFactory = null;
            if (!USE_DB) {
                MAX_DB_SAVING_THREADS = 0;
                MAX_DB_DELETING_THREADS = 0;
                SIZE_OF_DB_DELETING_QUEUE = 0;
                SIZE_OF_DB_SAVING_QUEUE = 0;
                SIZE_OF_DB_MERGE_QUEUE = 0;
                MAX_DB_MERGING_THREADS = 0;
            } else sessionFactory = HibernateUtil.getSessionFactory();
            try {
                FORCE_BLOCK_HTTP_URLS = bundle.getString("FORCE_BLOCK_HTTP_URLS").split(";");
                if (FORCE_BLOCK_HTTP_URLS.length <= 0) FORCE_BLOCK_HTTP_URLS = null;
            } catch (Exception e) {
            }
            try {
                FORCE_VALID_HTTP_URLS = bundle.getString("FORCE_VALID_HTTP_URLS").split(";");
                if (FORCE_VALID_HTTP_URLS.length <= 0) FORCE_VALID_HTTP_URLS = null;
            } catch (Exception e) {
            }
            try {
                FORCE_VALID_PLATFORM_SERVER_URLS = bundle.getString("FORCE_VALID_PLATFORM_SERVER_URLS").split(";");
                if (FORCE_VALID_PLATFORM_SERVER_URLS.length <= 0) FORCE_VALID_PLATFORM_SERVER_URLS = null;
            } catch (Exception e) {
            }
            Queues.initWerbungsQueue(!USE_DB_ADVERTISING ? 1 : MAX_CLIENTS_ONLINE);
            try {
                BYTE_PREPARSER_ACTIVATE = HelperStd.parseBoolean(bundle.getString("BYTE_PREPARSER_ACTIVATE"), BYTE_PREPARSER_ACTIVATE);
                BYTE_PREPARSER_COMPRESS_PREPARSED = HelperStd.parseBoolean(bundle.getString("BYTE_PREPARSER_COMPRESS_PREPARSED"), BYTE_PREPARSER_COMPRESS_PREPARSED);
                BYTE_PREPARSER_USE_FOR_ALL_ZIP_PACKAGES = HelperStd.parseBoolean(bundle.getString("BYTE_PREPARSER_USE_FOR_ALL_ZIP_PACKAGES"), BYTE_PREPARSER_USE_FOR_ALL_ZIP_PACKAGES);
            } catch (Exception e) {
            }
            executor = new ServerThreadExecutor(MAX_SIZE_FOR_SOCKET_QUEUE, MAX_SIZE_FOR_CLIENT_OBJECT_POOL, STARTING_SIZE_FOR_CLIENT_OBJECT_POOL, RedirectClient.class, MAX_CLIENTS_ONLINE, MIN_CLIENT_OBJECTS_IN_POOL, ALTERNATIVE_SERVERS, MAX_PROCESSING_THREADS, MAX_REQUEST_THREADS, SIZE_OF_DB_SAVING_QUEUE, SIZE_OF_DB_DELETING_QUEUE, SIZE_OF_DB_MERGE_QUEUE, MAX_DB_SAVING_THREADS, MAX_DB_DELETING_THREADS, MAX_DB_MERGING_THREADS, sessionFactory, USE_DB ? new HibernateUtil() : null);
            ServerSocketChannel ssc = null;
            boolean listening = true;
            try {
                ssc = ServerSocketChannel.open();
                ssc.configureBlocking(false);
                InetSocketAddress isa = new InetSocketAddress(port);
                ssc.socket().bind(isa);
                ssc.configureBlocking(true);
            } catch (IOException e) {
                Log.error("Could not listen on port: " + port, e);
                System.exit(-1);
            }
            if (RedirectServer.extension != null) RedirectServer.extension.doStartupThreads(executor, bundle);
            if (USE_DB_ADVERTISING) {
                executor.exec.newThread(new WerbungThread()).start();
                Log.info("Werbung Thread has been started!");
            }
            if (USE_CHECK_FOR_BLOCKED_USERS) {
                BlockingTasks.reFillUserBlockingMaps();
                executor.exec.newThread(new UpdateBlockingDataThread()).start();
                Log.info("Blocking User Information have been read to memory, and thread was started! Execution:" + BLOCKING_USER_CALCULATIONS_PER_DAY + " per Day");
            }
            if (USE_CHECK_FOR_DB_SERVER_ENTRIES) {
                BlockingTasks.reFillServerEntryMap();
                executor.exec.newThread(new UpdateServerEntryThread()).start();
                Log.info("Server Entries have been read to memory, and thread was started! Execution:" + SERVER_ENTRY_CALCULATIONS_PER_DAY + " per Day");
            }
            bundle = null;
            Log.info("server startet on port:" + port);
            while (listening) {
                SocketChannel soc = ssc.accept();
                if (RedirectServer.extension != null) soc = RedirectServer.extension.acceptNewSocket(soc);
                if (soc == null) continue;
                executor.newSocketConnection(soc);
            }
            ssc.close();
            executor.stop();
            executor = null;
        } catch (Exception e) {
            Log.error("Error while server start-up", e);
        }
    }

    /**
	 * translates a key
	 * @param key
	 * @param lang
	 * @return
	 */
    public static String translate(String key, String lang) {
        try {
            String tkey = lang + key;
            String t = translationCache.get(tkey);
            if (t != null) return t;
            ResourceBundle bundle = msgs.get(lang);
            if (bundle == null) {
                bundle = (ResourceBundle) ResourceBundle.getBundle("messages", new Locale(lang));
                msgs.put(lang, bundle);
            }
            t = bundle.getString(key);
            translationCache.put(tkey, t);
            return t;
        } catch (Exception e) {
        }
        return key;
    }

    private static ConcurrentHashMap<String, String> translationCache = new ConcurrentHashMap<String, String>(50);

    /**
	 * liest daten vom stream
	 * @param length
	 * @param in
	 * @return
	 * @throws IOException
	 */
    public static byte[] readData(int length, RedirectClient s) throws IOException {
        return ServerIO.readData(s.requestBuffer, length, s.socket);
    }

    /**
	 * reads a normal ISO-8859-1 String Line
	 * @param is
	 * @return
	 * @throws IOException
	 */
    public static String readLine(RedirectClient s) throws IOException {
        return ServerIO.readLine(s.requestBuffer, s.socket, s.bb, s.readLine);
    }

    public static String readLineNonBlocking(RedirectClient s) throws IOException, ConnectException {
        return ServerIO.readLineNonBlocking(s.socket, s.bb, s.readLine);
    }

    /**
	 * liest eine encodierte Zeile
	 * @param s
	 * @return
	 * @throws IOException
	 */
    public static String readEncodedLine(RedirectClient s) throws IOException {
        return ServerIO.readEncodedLine(s.requestBuffer, s.socket, s.bb, s.readLine, s.encoding);
    }
}
