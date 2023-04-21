package org.privale.coreclients.userclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.LinkedList;
import org.privale.clients.BaseClient;
import org.privale.clients.UserClient;
import org.privale.coreclients.listclient.CoreList;
import org.privale.coreclients.networkselector.CoreSelector;
import org.privale.node.ClientBio;
import org.privale.node.RemoteDownloadConfig;
import org.privale.node.RemoteUploadConfig;
import org.privale.utils.ChannelReader;
import org.privale.utils.ChannelWriter;
import org.privale.utils.FileManager;
import org.privale.utils.network.FactoryAttachment;
import org.privale.utils.network.NetSelectorReference;
import org.privale.utils.network.ReadComplete;
import org.privale.utils.network.Transaction;

public class CoreUser extends BaseClient implements UserClient, ReadComplete {

    public static String LISTENPORT = "LISTENPORT";

    public static String STATEFILE = "STATEFILE";

    public static String ALLOWEDIPS = "ALLOWEDIPS";

    public static String DEFAULTPORT = "8857";

    public static long USERCLIENTID = 0x00300005L;

    public static byte SUCCESS = 1;

    public static byte FAIL = 2;

    public static int UPLOAD = 1;

    public static int DOWNLOAD = 2;

    public static int QUERYUPLOAD = 3;

    public static int QUERYDOWNLOAD = 4;

    public static int EXTENDLOCAL = 5;

    public static int SHUTDOWNNODE = 6;

    private Hashtable<Integer, File> CompleteDownloads;

    private Hashtable<Integer, File> CompleteUploads;

    private Hashtable<Integer, String> FailedDownloads;

    private Hashtable<Integer, String> FailedUploads;

    private NetSelectorReference NSel;

    private FileManager FM;

    private FileManager Temp;

    private Factory Fac;

    private Integer CurrentId;

    private String[] AllowedIP;

    public CoreUser() {
        super(USERCLIENTID);
        getBio().setPublic(false);
        getBio().setType(ClientBio.UsrType);
        CurrentId = Integer.MIN_VALUE;
        CompleteDownloads = new Hashtable<Integer, File>();
        CompleteUploads = new Hashtable<Integer, File>();
        FailedUploads = new Hashtable<Integer, String>();
        FailedDownloads = new Hashtable<Integer, String>();
    }

    public void UploadFailed(RemoteUploadConfig config, String msg) {
        synchronized (FailedUploads) {
            Integer idx = (Integer) config.ClientReference;
            if (idx != null) {
                FailedUploads.put(idx, msg);
            } else {
                System.out.println("ERROR: Failed upload does not have an index!");
            }
        }
    }

    public void UploadComplete(RemoteUploadConfig config, File data) {
        System.out.println("UPLOAD COMPLETE!!!!!!!!!!");
        synchronized (CompleteUploads) {
            Integer idx = (Integer) config.ClientReference;
            if (idx != null) {
                System.out.println("Index: " + idx);
                System.out.println("DATA IS===== " + data);
                CompleteUploads.put(idx, data);
            } else {
                System.out.println("ERROR: Completed upload does not have an index!");
            }
        }
    }

    public void DownloadFailed(RemoteDownloadConfig config, String msg) {
        synchronized (FailedDownloads) {
            Integer idx = (Integer) config.ClientReference;
            if (idx != null) {
                FailedDownloads.put(idx, msg);
            } else {
                System.out.println("ERROR: Failed download does not have an index!");
            }
        }
    }

    public void DownloadComplete(RemoteDownloadConfig config, File data) {
        synchronized (CompleteDownloads) {
            Integer idx = (Integer) config.ClientReference;
            if (idx != null) {
                System.out.println("DATA IS===== " + data);
                CompleteDownloads.put(idx, data);
            } else {
                System.out.println("ERROR: Complete download does not have an index!");
            }
        }
    }

    protected int UploadData(RemoteUploadConfig conf) {
        int r = getRequestID();
        conf.ClientReference = r;
        getNode().Upload(conf);
        return r;
    }

    protected int DownloadData(RemoteDownloadConfig conf) {
        int r = getRequestID();
        conf.ClientReference = r;
        getNode().Download(conf);
        return r;
    }

    protected FileManager getTemp() {
        return Temp;
    }

    protected synchronized int getRequestID() {
        int r = CurrentId;
        if (CurrentId == Integer.MAX_VALUE) {
            CurrentId = Integer.MIN_VALUE;
        } else {
            CurrentId++;
        }
        return r;
    }

    protected File BuildDownloadQuery(LinkedList<Integer> querylist) throws IOException {
        File f = getTemp().createNewFile("download", "query");
        ChannelWriter cw = new ChannelWriter(f);
        for (Integer i : querylist) {
            synchronized (FailedDownloads) {
                synchronized (CompleteDownloads) {
                    String failstr = FailedDownloads.get(i);
                    if (failstr != null) {
                        System.out.println("FAIL STRING! " + failstr);
                        FailedDownloads.remove(i);
                        cw.putInt(i);
                        cw.putByte(FAIL);
                        cw.putString(failstr);
                    } else {
                        File d = CompleteDownloads.get(i);
                        if (d != null) {
                            CompleteDownloads.remove(i);
                            cw.putInt(i);
                            cw.putByte(SUCCESS);
                            cw.putLongFile(d);
                            d.delete();
                        }
                    }
                }
            }
        }
        cw.close();
        return f;
    }

    protected File BuildUploadQuery(LinkedList<Integer> querylist) throws IOException {
        System.out.println("HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        File f = getTemp().createNewFile("upload", "query");
        ChannelWriter cw = new ChannelWriter(f);
        for (Integer i : querylist) {
            System.out.println("HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + i);
            synchronized (FailedUploads) {
                synchronized (CompleteUploads) {
                    String failstr = FailedUploads.get(i);
                    if (failstr != null) {
                        System.out.println("???????????HERE???????????");
                        FailedUploads.remove(i);
                        cw.putInt(i);
                        cw.putByte(FAIL);
                        cw.putString(failstr);
                    } else {
                        File d = CompleteUploads.get(i);
                        if (d != null) {
                            System.out.println(" ** ADDING " + i);
                            CompleteUploads.remove(i);
                            cw.putInt(i);
                            cw.putByte(SUCCESS);
                            cw.putLongFile(d);
                            d.delete();
                        } else {
                            System.out.println("NULL!! " + i);
                        }
                    }
                }
            }
        }
        cw.close();
        return f;
    }

    protected NetSelectorReference getSelector() {
        if (NSel == null) {
            NSel = (NetSelectorReference) getNode().getClientReference(CoreSelector.SELECTORID);
        }
        return NSel;
    }

    protected String[] getAllowedIPs() {
        return AllowedIP;
    }

    public void start() {
        BringUpThread up = new BringUpThread(this);
        Thread t = new Thread(up);
        t.start();
    }

    public void stop() {
        try {
            String file = getProperties().getProperty("STATEFILE");
            File f = null;
            if (file == null) {
                f = FM.createNewFile("state", "dat");
                getProperties().setProperty("STATEFILE", f.getPath());
            } else {
                f = new File(file);
            }
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            synchronized (CompleteDownloads) {
                oos.writeObject(CompleteDownloads);
            }
            synchronized (CompleteUploads) {
                oos.writeObject(CompleteUploads);
            }
            synchronized (FailedDownloads) {
                oos.writeObject(FailedDownloads);
            }
            synchronized (FailedUploads) {
                oos.writeObject(FailedUploads);
            }
            oos.writeObject(CurrentId);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class BringUpThread implements Runnable {

        private CoreUser C;

        public BringUpThread(CoreUser c) {
            C = c;
        }

        @SuppressWarnings("unchecked")
        public void run() {
            try {
                String allowedips = getProperties().getProperty(ALLOWEDIPS);
                if (allowedips == null) {
                    allowedips = "127.0.0.1";
                    getProperties().setProperty(ALLOWEDIPS, allowedips);
                }
                AllowedIP = allowedips.split(",");
                String file = getProperties().getProperty(STATEFILE);
                if (file != null) {
                    File f = new File(file);
                    FileInputStream fis = new FileInputStream(f);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    CompleteDownloads = (Hashtable<Integer, File>) ois.readObject();
                    CompleteUploads = (Hashtable<Integer, File>) ois.readObject();
                    FailedDownloads = (Hashtable<Integer, String>) ois.readObject();
                    FailedUploads = (Hashtable<Integer, String>) ois.readObject();
                    CurrentId = (Integer) ois.readObject();
                    ois.close();
                    System.out.println("CoreUser data successfully restored!");
                }
                Fac = new Factory(C);
                FM = FileManager.getDir("coreuser");
                FM.HardDir = true;
                Temp = FileManager.getDir(FM.getPath() + File.separator + "data");
                Temp.HardDir = false;
                String portstr = getProperties().getProperty(LISTENPORT);
                if (portstr == null) {
                    portstr = DEFAULTPORT;
                    getProperties().setProperty(LISTENPORT, portstr);
                }
                int port = Integer.valueOf(portstr);
                FactoryAttachment fa = new FactoryAttachment(getSelector().getNetSelector(), Fac, Fac.getProcPool(), "userfactory");
                getSelector().getNetSelector().BindServer(port, fa);
                getNode().RegisterClientBio(C, getBio());
            } catch (Exception e) {
                getNode().ClientNodeShutdown(C, "ERROR: Failed to bring up user client!  Check the port!");
                e.printStackTrace();
            }
        }
    }

    public void Complete(Transaction trans, Object readobj) {
        File conffile = (File) readobj;
        RemoteUploadConfig conf = new RemoteUploadConfig();
        conf.setFM(getTemp());
        try {
            ChannelReader cr = new ChannelReader(conffile);
            cr.Read(conf);
            cr.close();
            CoreList l = (CoreList) getNode().getClientReference(CoreList.LISTCLIENTID);
            if (l != null) {
                l.ExtendAll(conf);
            } else {
                System.out.println("ERROR: Could not extend lists!!  No ListClientFound!!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
