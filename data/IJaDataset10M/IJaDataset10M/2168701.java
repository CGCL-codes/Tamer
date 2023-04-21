package net.sf.jftp.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamTokenizer;
import java.util.Date;
import java.util.Vector;
import net.sf.jftp.config.Settings;
import net.sf.jftp.system.StringUtils;
import net.sf.jftp.system.logging.Log;
import com.sun.xfile.XFile;
import com.sun.xfile.XFileInputStream;
import com.sun.xfile.XFileOutputStream;

public class NfsConnection implements BasicConnection {

    public static int buffer = 128000;

    private String url = "";

    private String host = "";

    private String path = "";

    private String pwd = "";

    private Vector listeners = new Vector();

    private String[] files;

    private String[] size = new String[0];

    private int[] perms = null;

    private String baseFile;

    private int fileCount;

    private boolean isDirUpload = false;

    private boolean shortProgress = false;

    private boolean dummy = false;

    public NfsConnection(String url) {
        this.url = url;
        host = url.substring(6);
        int x = host.indexOf("/");
        if (x >= 0) {
            host = host.substring(0, x);
        }
        Log.out("nfs host is: " + host);
    }

    public boolean login(String user, String pass) {
        Log.out("nfs login called: " + url);
        try {
            XFile xf = new XFile(url);
            if (xf.exists()) {
                Log.out("nfs url ok");
            } else {
                Log.out("WARNING: nfs url not found, cennection will fail!");
            }
            com.sun.nfs.XFileExtensionAccessor nfsx = (com.sun.nfs.XFileExtensionAccessor) xf.getExtensionAccessor();
            if (!nfsx.loginPCNFSD(host, user, pass)) {
                Log.out("login failed!");
                return false;
            } else {
                Log.debug("Login successful...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public String[] getExports() throws Exception {
        XFile xf = new XFile(url);
        com.sun.nfs.XFileExtensionAccessor nfsx = (com.sun.nfs.XFileExtensionAccessor) xf.getExtensionAccessor();
        String[] tmp = nfsx.getExports();
        if (tmp == null) {
            return new String[0];
        }
        for (int i = 0; i < tmp.length; i++) {
            Log.out("nfs export found: " + tmp[i]);
        }
        return tmp;
    }

    public int removeFileOrDir(String file) {
        try {
            String tmp = toNFS(file);
            XFile f = new XFile(tmp);
            if (!f.getAbsolutePath().equals(f.getCanonicalPath())) {
                Log.debug("WARNING: Skipping symlink, remove failed.");
                Log.debug("This is necessary to prevent possible data loss when removing those symlinks.");
                return -1;
            }
            if (f.exists() && f.isDirectory()) {
                cleanLocalDir(tmp);
            }
            if (!f.delete()) {
                return -1;
            } else {
                return 1;
            }
        } catch (IOException ex) {
            Log.debug("Error: " + ex.toString());
            ex.printStackTrace();
        }
        return -1;
    }

    private void cleanLocalDir(String dir) {
        dir = toNFS(dir);
        if (dir.endsWith("\\")) {
            Log.out("need to fix \\-problem!!!");
        }
        if (!dir.endsWith("/")) {
            dir = dir + "/";
        }
        XFile f2 = new XFile(dir);
        String[] tmp = f2.list();
        if (tmp == null) {
            return;
        }
        for (int i = 0; i < tmp.length; i++) {
            XFile f3 = new XFile(dir + tmp[i]);
            if (f3.isDirectory()) {
                cleanLocalDir(dir + tmp[i]);
                f3.delete();
            } else {
                f3.delete();
            }
        }
    }

    public void sendRawCommand(String cmd) {
    }

    public void disconnect() {
    }

    public boolean isConnected() {
        return true;
    }

    public String getPWD() {
        String tmp = toNFS(pwd);
        if (!tmp.endsWith("/")) {
            tmp = tmp + "/";
        }
        return tmp;
    }

    public boolean cdup() {
        String tmp = pwd;
        if (pwd.endsWith("/") && !pwd.equals("nfs://")) {
            tmp = pwd.substring(0, pwd.lastIndexOf("/"));
        }
        return chdir(tmp.substring(0, tmp.lastIndexOf("/") + 1));
    }

    public boolean mkdir(String dirName) {
        if (!dirName.endsWith("/")) {
            dirName = dirName + "/";
        }
        dirName = toNFS(dirName);
        File f = new File(dirName);
        boolean x = f.mkdir();
        fireDirectoryUpdate();
        return x;
    }

    public void list() throws IOException {
    }

    public boolean chdir(String p) {
        return chdir(p, true);
    }

    public boolean chdir(String p, boolean refresh) {
        if (p.endsWith("..")) {
            return cdup();
        }
        String tmp = toNFS(p);
        if (!tmp.endsWith("/")) {
            tmp = tmp + "/";
        }
        if (check(tmp) < 3) {
            return false;
        }
        pwd = tmp;
        if (refresh) {
            fireDirectoryUpdate();
        }
        return true;
    }

    private int check(String url) {
        int x = 0;
        for (int j = 0; j < url.length(); j++) {
            if (url.charAt(j) == '/') {
                x++;
            }
        }
        return x;
    }

    public boolean chdirNoRefresh(String p) {
        return chdir(p, false);
    }

    public String getLocalPath() {
        return path;
    }

    private String toNFS(String f) {
        String file;
        if (f.lastIndexOf("nfs://") > 0) {
            f = f.substring(f.lastIndexOf("nfs://"));
        }
        if (f.startsWith("nfs://")) {
            file = f;
        } else {
            file = getPWD() + f;
        }
        file = file.replace('\\', '/');
        Log.out("nfs url: " + file);
        return file;
    }

    public boolean setLocalPath(String p) {
        if (!p.startsWith("/") && !p.startsWith(":", 1)) {
            p = path + p;
        }
        File f = new File(p);
        if (f.exists()) {
            try {
                path = f.getCanonicalPath();
                path = path.replace('\\', '/');
                if (!path.endsWith("/")) {
                    path = path + "/";
                }
            } catch (IOException ex) {
                Log.debug("Error: can not get pathname (local)!");
                return false;
            }
        } else {
            Log.debug("(local) No such path: \"" + p + "\"");
            return false;
        }
        return true;
    }

    public String[] sortLs() {
        String dir = getPWD();
        if (check(toNFS(dir)) == 3) {
            try {
                files = getExports();
            } catch (Exception ex) {
                Log.debug("Can not list exports:" + ex.toString());
                ex.printStackTrace();
            }
        } else {
            XFile f = new XFile(dir);
            files = f.list();
        }
        if (files == null) {
            return new String[0];
        }
        size = new String[files.length];
        perms = new int[files.length];
        int accessible = 0;
        for (int i = 0; i < files.length; i++) {
            XFile f2 = new XFile(dir + files[i]);
            if (f2.isDirectory() && !files[i].endsWith("/")) {
                files[i] = files[i] + "/";
            }
            size[i] = "" + f2.length();
            if (f2.canWrite()) {
                accessible = FtpConnection.W;
            } else if (f2.canRead()) {
                accessible = FtpConnection.R;
            } else {
                accessible = FtpConnection.DENIED;
            }
            perms[i] = accessible;
        }
        return files;
    }

    public String[] sortSize() {
        return size;
    }

    public int[] getPermissions() {
        return perms;
    }

    public int handleUpload(String f) {
        upload(f);
        return 0;
    }

    public int handleDownload(String f) {
        download(f);
        return 0;
    }

    public int upload(String f) {
        String file = toNFS(f);
        if (file.endsWith("/")) {
            String out = StringUtils.getDir(file);
            uploadDir(file, getLocalPath() + out);
            fireActionFinished(this);
        } else {
            String outfile = StringUtils.getFile(file);
            work(getLocalPath() + outfile, file);
            fireActionFinished(this);
        }
        return 0;
    }

    public int download(String f) {
        String file = toNFS(f);
        if (file.endsWith("/")) {
            String out = StringUtils.getDir(file);
            downloadDir(file, getLocalPath() + out);
            fireActionFinished(this);
        } else {
            String outfile = StringUtils.getFile(file);
            work(file, getLocalPath() + outfile);
            fireActionFinished(this);
        }
        return 0;
    }

    private void downloadDir(String dir, String out) {
        try {
            fileCount = 0;
            shortProgress = true;
            baseFile = StringUtils.getDir(dir);
            XFile f2 = new XFile(dir);
            String[] tmp = f2.list();
            if (tmp == null) {
                return;
            }
            File fx = new File(out);
            fx.mkdir();
            for (int i = 0; i < tmp.length; i++) {
                tmp[i] = tmp[i].replace('\\', '/');
                XFile f3 = new XFile(dir + tmp[i]);
                if (f3.isDirectory()) {
                    if (!tmp[i].endsWith("/")) {
                        tmp[i] = tmp[i] + "/";
                    }
                    downloadDir(dir + tmp[i], out + tmp[i]);
                } else {
                    fileCount++;
                    fireProgressUpdate(baseFile, DataConnection.GETDIR + ":" + fileCount, -1);
                    work(dir + tmp[i], out + tmp[i]);
                }
            }
            fireProgressUpdate(baseFile, DataConnection.DFINISHED + ":" + fileCount, -1);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.debug("Transfer error: " + ex);
            fireProgressUpdate(baseFile, DataConnection.FAILED + ":" + fileCount, -1);
        }
        shortProgress = false;
    }

    private void uploadDir(String dir, String out) {
        try {
            isDirUpload = true;
            fileCount = 0;
            shortProgress = true;
            baseFile = StringUtils.getDir(dir);
            File f2 = new File(out);
            String[] tmp = f2.list();
            if (tmp == null) {
                return;
            }
            XFile fx = new XFile(dir);
            fx.mkdir();
            for (int i = 0; i < tmp.length; i++) {
                tmp[i] = tmp[i].replace('\\', '/');
                File f3 = new File(out + tmp[i]);
                if (f3.isDirectory()) {
                    if (!tmp[i].endsWith("/")) {
                        tmp[i] = tmp[i] + "/";
                    }
                    uploadDir(dir + tmp[i], out + tmp[i]);
                } else {
                    fileCount++;
                    fireProgressUpdate(baseFile, DataConnection.PUTDIR + ":" + fileCount, -1);
                    work(out + tmp[i], dir + tmp[i]);
                }
            }
            fireProgressUpdate(baseFile, DataConnection.DFINISHED + ":" + fileCount, -1);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.debug("Transfer error: " + ex);
            fireProgressUpdate(baseFile, DataConnection.FAILED + ":" + fileCount, -1);
        }
        isDirUpload = false;
        shortProgress = true;
    }

    private void work(String file, String outfile) {
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            boolean outflag = false;
            if (outfile.startsWith("nfs://")) {
                outflag = true;
                out = new BufferedOutputStream(new XFileOutputStream(outfile));
            } else {
                out = new BufferedOutputStream(new FileOutputStream(outfile));
            }
            if (file.startsWith("nfs://")) {
                in = new BufferedInputStream(new XFileInputStream(file));
            } else {
                in = new BufferedInputStream(new FileInputStream(file));
            }
            byte[] buf = new byte[buffer];
            int len = 0;
            int reallen = 0;
            while (true) {
                len = in.read(buf);
                if (len == StreamTokenizer.TT_EOF) {
                    break;
                }
                out.write(buf, 0, len);
                reallen += len;
                if (outflag) {
                    fireProgressUpdate(StringUtils.getFile(outfile), DataConnection.PUT, reallen);
                } else {
                    fireProgressUpdate(StringUtils.getFile(file), DataConnection.GET, reallen);
                }
            }
            fireProgressUpdate(file, DataConnection.FINISHED, -1);
        } catch (IOException ex) {
            Log.debug("Error with file IO (" + ex + ")!");
            fireProgressUpdate(file, DataConnection.FAILED, -1);
        } finally {
            try {
                out.flush();
                out.close();
                in.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void update(String file, String type, int bytes) {
        if (listeners == null) {
            return;
        } else {
            for (int i = 0; i < listeners.size(); i++) {
                ConnectionListener listener = (ConnectionListener) listeners.elementAt(i);
                listener.updateProgress(file, type, bytes);
            }
        }
    }

    public void addConnectionListener(ConnectionListener l) {
        listeners.add(l);
    }

    public void setConnectionListeners(Vector l) {
        listeners = l;
    }

    /** remote directory has changed */
    public void fireDirectoryUpdate() {
        if (listeners == null) {
            return;
        } else {
            for (int i = 0; i < listeners.size(); i++) {
                ((ConnectionListener) listeners.elementAt(i)).updateRemoteDirectory(this);
            }
        }
    }

    /** progress update */
    public void fireProgressUpdate(String file, String type, int bytes) {
        if (listeners == null) {
            return;
        } else {
            for (int i = 0; i < listeners.size(); i++) {
                ConnectionListener listener = (ConnectionListener) listeners.elementAt(i);
                if (shortProgress && Settings.shortProgress) {
                    if (type.startsWith(DataConnection.DFINISHED)) {
                        listener.updateProgress(baseFile, DataConnection.DFINISHED + ":" + fileCount, bytes);
                    } else if (isDirUpload) {
                        listener.updateProgress(baseFile, DataConnection.PUTDIR + ":" + fileCount, bytes);
                    } else {
                        listener.updateProgress(baseFile, DataConnection.GETDIR + ":" + fileCount, bytes);
                    }
                } else {
                    listener.updateProgress(file, type, bytes);
                }
            }
        }
    }

    public void fireActionFinished(NfsConnection con) {
        if (listeners == null) {
            return;
        } else {
            for (int i = 0; i < listeners.size(); i++) {
                ((ConnectionListener) listeners.elementAt(i)).actionFinished(con);
            }
        }
    }

    public int upload(String file, InputStream i) {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            file = toNFS(file);
            out = new BufferedOutputStream(new XFileOutputStream(file));
            in = new BufferedInputStream(i);
            byte[] buf = new byte[buffer];
            int len = 0;
            int reallen = 0;
            while (true) {
                len = in.read(buf);
                if (len == StreamTokenizer.TT_EOF) {
                    break;
                }
                out.write(buf, 0, len);
                reallen += len;
                fireProgressUpdate(StringUtils.getFile(file), DataConnection.PUT, reallen);
            }
            fireProgressUpdate(file, DataConnection.FINISHED, -1);
        } catch (IOException ex) {
            Log.debug("Error with file IO (" + ex + ")!");
            fireProgressUpdate(file, DataConnection.FAILED, -1);
            return -1;
        } finally {
            try {
                out.flush();
                out.close();
                in.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }

    public InputStream getDownloadInputStream(String file) {
        file = toNFS(file);
        Log.debug(file);
        try {
            return new BufferedInputStream(new XFileInputStream(file));
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.debug(ex.toString() + " @NfsConnection::getDownloadInputStream");
            return null;
        }
    }

    public Date[] sortDates() {
        return null;
    }

    public boolean rename(String from, String to) {
        Log.debug("Not implemented!");
        return false;
    }
}
