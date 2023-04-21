package com.belmont.backup;

import java.io.*;
import java.util.*;
import java.sql.*;
import java.net.URL;
import java.text.*;
import com.marimba.intf.util.IConfig;
import com.marimba.intf.application.*;

public class BackupApplication implements IBackupConstants, IEventObserver, Runnable {

    long nextBackupTime = 0;

    String scheduleString = null;

    Thread scheduleThread;

    URL baseURL;

    Properties props;

    BackupClient backup;

    RestoreClient restore;

    IBackupServer server;

    boolean networkMode = true;

    IConfig channelConfig;

    IApplicationContext context;

    File confdir;

    File root;

    FileDatabase db;

    String host = "localhost";

    int port = DEFAULT_PORT;

    String serverURL;

    String session;

    boolean connected = true;

    String connectStatus;

    public BackupApplication(IApplicationContext context) {
        this.context = context;
        this.channelConfig = context.getConfiguration();
        this.root = new File(context.getDataDirectory());
        this.host = context.getChannelURL().getHost();
    }

    public BackupApplication(File root, boolean networkMode) {
        this.root = root;
        root.mkdirs();
        this.networkMode = networkMode;
    }

    public void setIncludeFilter(String f[]) {
        if (backup != null) {
            if (f != null && f.length == 0) {
                f = null;
            }
            backup.includeFilter = f;
        }
    }

    public void setExcludeFilter(String f[]) {
        if (backup != null) {
            if (f != null && f.length == 0) {
                f = null;
            }
            backup.excludeFilter = f;
        }
    }

    public URL getBaseURL() {
        if (context == null) {
            return null;
        } else {
            return context.getBaseURL();
        }
    }

    public String getSession() {
        return session;
    }

    public boolean getConnected() {
        return connected;
    }

    public String getConnectStatus() {
        return connectStatus;
    }

    public String getUUID() {
        String uuid = getString("backup.uuid", null);
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
            setString("backup.uuid", uuid);
        }
        return uuid;
    }

    public BackupClient getBackup() {
        return backup;
    }

    public RestoreClient getRestore() {
        return restore;
    }

    public IBackupServer getServer() {
        return server;
    }

    public long nextScheduledBackup() {
        return nextBackupTime;
    }

    void setSchedule() {
        if (server == null) {
            return;
        }
        String sched = server.getConfig().getBackupSchedule();
        if ((sched == null && scheduleString == null) || (sched.equals(scheduleString) && nextBackupTime > System.currentTimeMillis())) {
            return;
        }
        scheduleString = sched;
        if (sched == null) {
            nextBackupTime = 0;
            if (scheduleThread != null) {
                scheduleThread.interrupt();
            }
            Producer.getInstance().sendNotify(this, NOTIFY_SCHEDULE_CHANGED, null);
            return;
        }
        long nx = nextBackupTime;
        nextBackupTime = Utils.nextScheduleTime(sched);
        if (nextBackupTime == 0) {
            Utils.log(LOG_ERROR, "Error parsing schedule time" + sched);
        } else if (nx != nextBackupTime) {
            Producer.getInstance().sendNotify(this, NOTIFY_SCHEDULE_CHANGED, null);
            if (scheduleThread != null) {
                scheduleThread.interrupt();
            } else {
                scheduleThread = new Thread(this);
                scheduleThread.start();
            }
        }
    }

    public void run() {
        long nexttime = nextBackupTime;
        while (nexttime > 0) {
            long tm = System.currentTimeMillis();
            if (nexttime < tm) {
                break;
            }
            try {
                Thread.sleep(nexttime - tm);
                runScheduledBackup();
                setSchedule();
            } catch (InterruptedException ex) {
                if (nexttime != nextBackupTime) {
                    nexttime = nextBackupTime;
                } else {
                    nexttime = 0;
                }
            }
        }
        scheduleThread = null;
    }

    public synchronized boolean connect() {
        if (session != null && connected) {
            return true;
        }
        try {
            session = server.connect(BACKUP_PROTOCOL_VERSION, getUUID(), Utils.getHostName(), System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("user.name"));
            server.refresh();
            backup.setSession(session);
            connected = true;
            return true;
        } catch (IOException ex) {
            Utils.log(LOG_ERROR, "Error connecting to server", ex);
            connectStatus = "Error connecting to server: " + ex.toString();
            connected = false;
            return false;
        }
    }

    void runScheduledBackup() {
        Utils.log(LOG_INFO, "Running scheduled backup");
        String dirs[] = server.getConfig().getBackupDirectories();
        if (dirs == null || dirs.length == 0) {
            Utils.log(LOG_INFO, "No backup directories found in scheduled backup");
            return;
        }
        if (!connect()) {
            Utils.log(LOG_ERROR, "Error connecting to server.");
            return;
        }
        int success = 0;
        for (int i = 0; i < dirs.length; i++) {
            Utils.log(LOG_INFO, "Backing up " + dirs[i]);
            File d = new File(dirs[i]);
            if (!d.exists() || !d.isDirectory()) {
                Utils.log(LOG_ERROR, "Not a valid directory: " + dirs[i]);
                continue;
            }
            try {
                if (backup.backup(d, server)) {
                    Utils.log(LOG_INFO, "Backup of " + dirs[i] + " succesful");
                    success++;
                } else {
                    Utils.log(LOG_ERROR, "Backup of " + dirs[i] + " failed");
                }
            } catch (Exception ex) {
                Utils.log(LOG_ERROR, "Exception caught while backing up " + dirs[i], ex);
            }
        }
        if (success < dirs.length) {
            Utils.log(LOG_ERROR, "Only backed up " + success + " out of " + dirs.length + " directories.");
        } else {
            Utils.log(LOG_INFO, "All directories backed up.");
            setString(CONFIG_BACKUP_SCHEDULE_LASTRUN, Long.toString(System.currentTimeMillis()));
        }
    }

    public String getServerURL() {
        return serverURL;
    }

    public void init() throws IOException {
        db = new FileDatabase();
        confdir = new File(root, "backup");
        confdir.mkdirs();
        if (getString(CONFIG_HOST, null) != null) {
            host = getString(CONFIG_HOST);
        }
        try {
            String p = getString(CONFIG_PORT, null);
            if (p != null) {
                port = Integer.parseInt(p.trim());
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        if (networkMode) {
            server = new BackupServerClient(confdir, host, port);
            serverURL = host + ":" + port;
        } else {
            server = new FileBackupServer(new File(root, "serverDir"));
            serverURL = "local";
        }
        db.init(new File(confdir, CONFIG_DB_NAME).getAbsolutePath());
        backup = new BackupClient(confdir);
        restore = new RestoreClient(confdir);
        backup.setDatabase(db);
        restore.setDatabase(db);
        Utils.openLog(confdir, "backupclient.log");
        Config cf = server.getConfig();
        cf.addObserver(this, NOTIFY_CONFIG_CHANGED, NOTIFY_CONFIG_CHANGED);
        setSchedule();
    }

    static class NamedVector extends Vector {

        String name;

        NamedVector(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }

        public NamedVector get(String name) {
            int l = size();
            for (int i = 0; i < l; i++) {
                Object v = elementAt(i);
                if (v instanceof NamedVector) {
                    NamedVector nv = (NamedVector) v;
                    if (nv.name.equals(name)) {
                        return nv;
                    }
                }
            }
            return null;
        }
    }

    public Vector getTreeData(String root, String backupname) throws SQLException {
        ResultSet rs = db.getBackupEntries(backupname);
        NamedVector r = new NamedVector(root);
        SimpleDateFormat dfm = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z");
        try {
            while (rs.next()) {
                String path = rs.getString(1);
                String fullPath = path;
                String ck = rs.getString(2);
                long size = rs.getLong(3);
                long tm = rs.getLong(4);
                int idx;
                NamedVector dtable = r;
                String dir = path;
                while ((idx = path.indexOf('/')) != -1) {
                    dir = path.substring(0, idx);
                    path = path.substring(idx + 1);
                    NamedVector v = dtable.get(dir);
                    if (v == null) {
                        v = new NamedVector(dir);
                        dtable.addElement(v);
                    } else {
                        dtable = v;
                    }
                }
                dtable.addElement(new com.belmont.backup.gui.CheckBoxNode(path, fullPath, dfm.format(new java.util.Date(tm)), ck, size, false));
            }
        } finally {
            rs.close();
        }
        return r;
    }

    public void stop() {
        if (session != null) {
            try {
                server.disconnect(session);
            } catch (IOException ex) {
                Utils.log(LOG_ERROR, "error in stop", ex);
            }
            session = null;
        }
        db.close();
        if (context == null) {
            System.exit(0);
        } else {
            if (scheduleString != null) {
                long tm = Utils.nextScheduleTime(scheduleString);
                if (tm != 0) {
                    context.startLater(tm, null);
                }
            }
            context.stop();
        }
    }

    public void setString(String key, String value) {
        if (key == null) {
            return;
        }
        if (channelConfig != null) {
            channelConfig.setProperty(key, value);
        } else {
            loadProperties();
            if (value == null) {
                props.remove(key);
            } else {
                props.setProperty(key, value);
            }
            saveProperties();
        }
    }

    public String getString(String key) {
        return getString(key, key);
    }

    public String getString(String key, String defval) {
        if (channelConfig != null) {
            String v = channelConfig.getProperty(key);
            if (v == null) {
                return defval;
            } else {
                return v;
            }
        } else {
            loadProperties();
            String v = ((props == null) ? null : props.getProperty(key));
            if (v == null) {
                return defval;
            } else {
                return v;
            }
        }
    }

    void loadProperties() {
        if (props == null) {
            try {
                FileInputStream in = new FileInputStream(new File(root, "properties.txt"));
                try {
                    props = new Properties();
                    props.load(in);
                } finally {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                props = new Properties();
            }
        }
    }

    void saveProperties() {
        if (props == null) {
            return;
        }
        try {
            FileOutputStream out = new FileOutputStream(new File(root, "properties.txt"));
            try {
                props.store(out, "BackupApplication");
            } finally {
                out.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void notify(Object sender, int msg, Object arg) {
        switch(msg) {
            case NOTIFY_CONFIG_CHANGED:
                setSchedule();
                break;
            case NOTIFY_BACKUP_START:
                Utils.log(LOG_INFO, "BACKUP_START " + arg);
                break;
            case NOTIFY_RESTORE_START:
                Utils.log(LOG_INFO, "RESTORE START " + arg);
                break;
            case NOTIFY_BACKUP_FAILED:
            case NOTIFY_PARTIAL_BACKUP:
            case NOTIFY_RESTORE_FAILED:
                Utils.log(LOG_INFO, "OPERATION FAILED");
                break;
            case NOTIFY_BACKUP_DONE:
            case NOTIFY_RESTORE_DONE:
                Utils.log(LOG_INFO, "BACKUP DONE");
                break;
            case NOTIFY_FILE_COUNT_CHANGED:
                Utils.log(LOG_INFO, "FILE COUNT CHANGE " + arg);
                break;
            case NOTIFY_COMPLETED_FILE_COUNT_CHANGED:
                Utils.log(LOG_INFO, "FILE COUNT CHANGED " + arg);
                break;
            case NOTIFY_SCANNING_START:
                Utils.log(LOG_INFO, "SCANNING FILE " + arg);
                break;
            case NOTIFY_SCANNING_STOP:
                break;
            case NOTIFY_ERROR_FILE:
            case NOTIFY_FILE_ERROR:
                Utils.log(LOG_ERROR, "FILE ERROR " + arg);
                break;
            case NOTIFY_EXCEPTION:
                Utils.log(LOG_ERROR, "Received exception: ", ((Throwable) arg));
                break;
            case NOTIFY_START_FILE:
                Utils.log(LOG_INFO, "START FILE " + arg);
                break;
            case NOTIFY_COMPLETED_FILE:
                Utils.log(LOG_INFO, "COMPLETED FILE " + arg);
                break;
            case NOTIFY_TRANSFER_SIZE:
                Utils.log(LOG_INFO, "TRANSFER SIZE " + arg);
                break;
            case NOTIFY_TRANSFER_COMPLETE:
                Utils.log(LOG_INFO, "TRANSFER COMPLETE");
                break;
            case NOTIFY_TRANSFER_PROGRESS:
                break;
            default:
                break;
        }
    }

    public static void main(String args[]) throws Exception {
        File root = null;
        boolean networkMode = false;
        boolean verbose = false;
        String backupPath = null;
        String restorePath = null;
        String restoreTarget = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-root")) {
                root = new File(args[++i]);
            } else if (args[i].equals("-network")) {
                networkMode = true;
            } else if (args[i].equals("-backup")) {
                backupPath = args[++i];
            } else if (args[i].equals("-restore")) {
                restorePath = args[++i];
                restoreTarget = args[++i];
            } else if (args[i].equals("-verbose")) {
                verbose = true;
            }
        }
        if (root == null) {
            Utils.log(LOG_INFO, "No root specified.");
            System.exit(1);
        }
        if (backupPath == null && restorePath == null) {
            Utils.log(LOG_INFO, "No backup or restore path specified.");
            System.exit(1);
        }
        BackupApplication app = new BackupApplication(root, networkMode);
        app.init();
        if (verbose) {
            app.getBackup().addObserver(app, 0, 0);
            app.getRestore().addObserver(app, 0, 0);
        }
        boolean success = false;
        String session = app.getServer().connect(BACKUP_PROTOCOL_VERSION, app.getUUID(), "localhost", System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("user.name"));
        if (backupPath != null) {
            Utils.log(LOG_INFO, "Starting backup");
            app.getBackup().setSession(session);
            success = app.getBackup().backup(new File(backupPath), app.getServer());
        } else if (restorePath != null) {
            Utils.log(LOG_INFO, "Starting restore");
            success = app.getRestore().restore(null, restorePath, new File(restoreTarget), app.getServer(), session);
        }
        app.getServer().disconnect(session);
        if (success) {
            Utils.log(LOG_INFO, "Operation successful");
            System.exit(0);
        } else {
            Utils.log(LOG_INFO, "Operation failed");
            System.exit(1);
        }
    }
}
