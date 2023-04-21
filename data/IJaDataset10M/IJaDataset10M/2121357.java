package com.elibera.msgs.app;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.elibera.msgs.entity.Msg;
import com.elibera.msgs.entity.MsgBinary;
import com.elibera.util.Log;

/**
 * @author meisi
 *
 */
public class MessagingServer {

    public static String settingsDIR = "/home/matthias/data/eclipse/messagingserver/conf";

    public static String confDirName = "conf";

    public static String settingsName = "settings_messaging";

    public static long d_ = 0;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        int port = 4444;
        int port2 = 4444;
        int test = 0;
        try {
            Log.init(null);
            String sc = System.getProperty("com.elibera.msgs.app.MessagingServer.settingsDIR");
            if (sc != null && sc.length() > 0) settingsDIR = sc;
            if (settingsDIR.charAt(settingsDIR.length() - 1) != File.separatorChar) settingsDIR += File.separatorChar;
            settingsDIR = settingsDIR.replace('/', File.separatorChar);
            Log.info("Settings dir:" + settingsDIR);
            URL[] searchPath = { new URL("file:/" + settingsDIR), new URL("file:/" + settingsDIR.replace(File.separatorChar, '/')), new URL("file://" + settingsDIR), new URL("file://" + settingsDIR.replace(File.separatorChar, '/')), new URL("file://" + System.getProperty("user.dir") + File.separatorChar), new URL("file://" + System.getProperty("user.dir") + File.separatorChar + confDirName + File.separatorChar) };
            ResourceBundle bundle = (ResourceBundle) ResourceBundle.getBundle(settingsName, new Locale("en"), new URLClassLoader(searchPath));
            port = Integer.parseInt(bundle.getString("PORT"));
            try {
                port2 = Integer.parseInt(bundle.getString("PORT_SERVER_INTERFACE"));
                Server.MAX_SIZE_FOR_SOCKET_PROCESS_QUEUE = Integer.parseInt(bundle.getString("MAX_SIZE_FOR_SERVER_INTERFACE_SOCKET_QUEUES"));
                Server.MAX_SOCKET_SERVER_INTERFACE_PROCESSING_THREADS = Integer.parseInt(bundle.getString("MAX_SERVER_INTERFACE_THREADS"));
            } catch (Exception ee) {
            }
            ResourceBundle hibernateBundle = (ResourceBundle) ResourceBundle.getBundle(bundle.getString("HIBERNATE_CONFIG_FILE"), new Locale("en"), new URLClassLoader(searchPath));
            HibernateUtil.init(hibernateBundle);
            hibernateBundle = null;
            Server.MAX_SIZE_FOR_SOCKET_QUEUE = Integer.parseInt(bundle.getString("MAX_SIZE_FOR_SOCKET_QUEUE"));
            Server.MAX_SIZE_FOR_CLIENT_OBJECT_POOL = Integer.parseInt(bundle.getString("MAX_SIZE_FOR_CLIENT_OBJECT_POOL"));
            Server.STARTING_SIZE_FOR_CLIENT_OBJECT_POOL = Integer.parseInt(bundle.getString("STARTING_SIZE_FOR_CLIENT_OBJECT_POOL"));
            Server.MIN_CLIENT_OBJECTS_IN_POOL = Integer.parseInt(bundle.getString("MIN_CLIENT_OBJECTS_IN_POOL"));
            Server.MAX_PROCESSING_THREADS = Integer.parseInt(bundle.getString("MAX_PROCESSING_THREADS"));
            Server.MAX_CLIENTS_ONLINE = Integer.parseInt(bundle.getString("MAX_CLIENTS_ONLINE"));
            Server.ALTERNATIVE_SERVERS = bundle.getString("ALTERNATIVE_SERVERS").split(";");
            Server.MSG_ENCODING = bundle.getString("MSG_ENCODING");
            Server.MIN_WAIT_TIME_FOR_SENDING_ALIVE_BIT = Integer.parseInt(bundle.getString("MIN_WAIT_TIME_FOR_SENDING_ALIVE_BIT"));
            test = Integer.parseInt(bundle.getString("TEST"));
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
        Session sess = HibernateUtil.getSessionGenerall();
        Log.info("Server starting ...");
        try {
            HibernateUtil.executeUpdateQuery(sess.createQuery("delete from ClientOnline"), sess);
            HibernateUtil.executeUpdateQuery(sess.createQuery("delete from Msg Where onlineMsg=true"), sess);
            List list = sess.createQuery("select count(id) from Msg").list();
            Log.info("DB INFO: Waiting Msgs:" + list.get(0));
            list = sess.createQuery("select count(id) from ClientOnline").list();
            Log.info("DB INFO: Online Clients:" + list.get(0));
            list = sess.createQuery("select count(id) from MsgBinary").list();
            Log.info("DB INFO: Waiting Msg Binaries:" + list.get(0));
            if (test >= 1) {
                Log.info("Doing database tests");
                Msg msg = new Msg();
                msg.setCreateTime(System.currentTimeMillis());
                msg.setMessageDest("test");
                msg.setMessageEncoding("utf8");
                msg.setMessageType("system".getBytes());
                msg.setMessage("my msg".getBytes());
                HibernateUtil.executeSave(msg, sess);
                MsgBinary mb = new MsgBinary();
                mb.setContentType("test");
                mb.setData("some data".getBytes());
                mb.setMsg(msg);
                HibernateUtil.executeSave(mb, sess);
                sess.refresh(msg);
                Log.info("TEST: Msg and Binary created: " + msg.getId() + ", Binary-ID:" + mb.getId());
                if (test >= 2) {
                    Log.info("TEST: trying to delete the message, the binary should be deleted too");
                    HibernateUtil.executeDelete(msg, sess);
                    Log.info("TEST: both should be deleted, please check this in your DB");
                }
            }
            HibernateUtil.executeUpdateQuery(sess.createQuery("UPDATE Msg SET msgTaken=false"), sess);
            Log.info("Maximum Number of Clients: " + Server.MAX_CLIENTS_ONLINE);
            Log.info("Message encoding: " + Server.MSG_ENCODING);
        } catch (Exception e) {
            Log.error("Error during start-up", e);
        } finally {
            HibernateUtil.savelyCloseSession(sess);
        }
        Server.startServer(port, port2);
    }
}
