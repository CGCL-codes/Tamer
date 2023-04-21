package com.entelience.probe;

import com.entelience.sql.Db;
import com.entelience.util.DateHelper;
import com.entelience.util.FileHelper;
import com.entelience.util.StringHelper;
import java.io.File;
import java.util.Properties;
import java.util.Date;
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.UserInfo;
import com.jcraft.jsch.UIKeyboardInteractive;

/**
 * Mirror files from *one* directory on an sftp server.
 *
 * Note that all files are downloaded via temporary files, which are only ever
 * renamed to the target filename if the file is downloaded successfully.
 *
 * This, hopefully, allows us to avoid integrating files which were previously
 * only partially downloaded.
 */
public class SftpMirror extends Mirror {

    private final String remoteRootDir;

    private final String server;

    private final int port;

    private final boolean recursive;

    private final Date before;

    private final Date after;

    protected final String pathToPrivateKey;

    protected final String passphrase;

    protected final String username;

    protected final String password;

    private JSch jsch = null;

    private Session session = null;

    private ChannelSftp sftp = null;

    /**
     * Helper class for log
     */
    public static class MyLogger implements com.jcraft.jsch.Logger {

        public boolean isEnabled(int level) {
            return true;
        }

        public void log(int level, String message) {
            _logger.debug("[JSch] - " + message);
        }
    }

    /**
     * Helper class to handle user interface interaction.
     */
    public class MyUserInfo implements UserInfo, UIKeyboardInteractive {

        private final SftpMirror mirror;

        public MyUserInfo(SftpMirror mirror) {
            this.mirror = mirror;
        }

        public String getName() {
            return mirror.username;
        }

        public String getPassword() {
            return mirror.password;
        }

        public boolean prompt(String msg) {
            return false;
        }

        public boolean retry() {
            return false;
        }

        public boolean promptYesNo(String msg) {
            mirror._logger.debug("promptYesNo: [" + msg + "]");
            return true;
        }

        public String getKeyfile() {
            return null;
        }

        public String getPassphrase() {
            return null;
        }

        public boolean promptPassphrase(String msg) {
            mirror._logger.debug("promptPassphrase: [" + msg + "]");
            return true;
        }

        public boolean promptPassword(String msg) {
            mirror._logger.debug("promptPassword: [" + msg + "]");
            return true;
        }

        public void showMessage(String msg) {
            mirror._logger.debug("showMessage: [" + msg + "]");
        }

        public String[] promptKeyboardInteractive(String destination, String name, String instruction, String prompt[], boolean echo[]) {
            if (prompt.length != 1 || echo[0]) return null;
            String response[] = new String[1];
            response[0] = mirror.password;
            return response;
        }
    }

    /**
     * Construct an SftpMirror for a remote ssh server.
     *
     * @param dir directory to CWD to once connected to the ssh server (absolute path).
     * @param server remote server hostname or ip address
     * @param port port to connect to on the remote server, usually 21
     * @param username username for login on the remote server
     * @param password password for login on the remote server (can be null if private key is set)
     * @param pathToPrivateKey the full path to the private key
     * @param passphrase the passphrase the private key
     */
    public SftpMirror(String dir, String server, int port, String username, String password, int maxFilesToImport, String pathToPrivateKey, String passphrase, boolean recursive, Date before, Date after) {
        this.remoteRootDir = dir;
        this.server = server;
        this.port = port;
        this.username = username;
        this.password = password;
        setMaxFilesToMirror(maxFilesToImport);
        this.recursive = recursive;
        this.before = before;
        this.after = after;
        this.pathToPrivateKey = pathToPrivateKey;
        this.passphrase = passphrase;
    }

    /**
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" remoteRootDir=[").append(remoteRootDir).append(']');
        sb.append(" server=[").append(server).append(']');
        sb.append(" port=[").append(port).append(']');
        sb.append(" username=[").append(username).append(']');
        sb.append(" private key=[").append(pathToPrivateKey).append(']');
        sb.append(" maxFiles=[").append(getMaxFilesToMirror()).append(']');
        sb.append(" recursive=[").append(recursive).append(']');
        if (after != null) sb.append(" after=[").append(after).append(']');
        if (before != null) sb.append(" before=[").append(before).append(']');
        return sb.toString();
    }

    /**
     */
    @Override
    public void configure(Map<String, String> params) throws Exception {
        throw new IllegalStateException("Not implemented.");
    }

    /**
     */
    @Override
    public void cliConfigure() throws Exception {
        throw new IllegalStateException("Not implemented.");
    }

    /**
     */
    @Override
    public void connect() throws Exception {
        if (sftp != null) throw new IllegalStateException("Already connected.");
        try {
            _logger.info("About to connect to SFTP server :" + this.toString());
            jsch = new JSch();
            if (_logger.getLevel() == org.apache.log4j.Level.DEBUG) JSch.setLogger(new MyLogger());
            if (StringHelper.nullify(pathToPrivateKey) != null) {
                _logger.debug("Using a private key for authentication (" + pathToPrivateKey + ")");
                if (!FileHelper.fileExists(pathToPrivateKey)) throw new ProbeException("Unable to file the private key file (" + pathToPrivateKey + ")");
                jsch.addIdentity(StringHelper.nullify(pathToPrivateKey), passphrase);
            }
            session = jsch.getSession(username, server, port);
            if (password != null) {
                _logger.debug("Adding user (password) authentication information");
                session.setUserInfo(new MyUserInfo(this));
            }
            Properties properties = new Properties();
            properties.put("StrictHostKeyChecking", "no");
            session.setConfig(properties);
            _logger.debug("Connecting to SFTP server (" + server + ")");
            session.connect();
            _logger.debug("Opening session with SFTP server (" + server + ")");
            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            _logger.debug("Logged in successful to ssh server (" + server + ") on port (" + port + ") with user (" + username + ")");
            sftp.cd(remoteRootDir);
            _logger.debug("Changed directory to (" + remoteRootDir + ")");
        } catch (Exception e) {
            try {
                disconnect();
            } catch (Exception ex) {
                _logger.warn("Failed to disconnect on error", ex);
            }
            throw new ProbeException("Failed to connect to the SFTP server (" + server + ")", e);
        }
    }

    private static final List<Pattern> listMatches = new ArrayList<Pattern>();

    /**
     * Add a pattern that matches files to be fetched from the remote ssh server.
     * Used in addition to select.
     */
    public void addFetch(Pattern p) {
        listMatches.add(p);
    }

    /**
     * List the files that match fetch patterns on the remote ssh server.
     * @param files input/output
     */
    @Override
    public void list(Db db, List<FileState> files) throws Exception {
        list(db, files, null);
    }

    private static final Pattern p_perm_read = Pattern.compile(".*r.*");

    /**
     */
    protected void list(Db db, List<FileState> files, String subDir) throws Exception {
        if (files == null) throw new IllegalArgumentException("files must not be null");
        if (sftp == null) throw new IllegalStateException("sftp has gone away");
        if (subDir != null) {
            String dir = remoteRootDir;
            if (subDir != null && !".".equals(subDir)) dir = dir + "/" + subDir;
            _logger.info("File listing for directory (" + dir + ")");
            try {
                sftp.cd(dir);
            } catch (Exception e) {
                throw new ProbeException("Failed to access directory (" + dir + ")", e);
            }
        }
        List<String> subDirs = new ArrayList<String>();
        List list = sftp.ls(".");
        for (int i = 0, size = list.size(); i < size; ++i) {
            com.jcraft.jsch.ChannelSftp.LsEntry lsEntry = (com.jcraft.jsch.ChannelSftp.LsEntry) list.get(i);
            String name = lsEntry.getFilename();
            if (".".equals(name) || "..".equals(name)) continue;
            SftpATTRS attrs = lsEntry.getAttrs();
            if (attrs.isDir()) {
                StringBuffer sb = new StringBuffer();
                if (subDir == null || ".".equals(subDir)) sb.append(name); else {
                    sb.append(subDir);
                    sb.append('/');
                    sb.append(name);
                }
                subDirs.add(sb.toString());
            } else {
                Matcher m_perm_read = p_perm_read.matcher(attrs.getPermissionsString());
                if (!m_perm_read.matches()) {
                    _logger.debug("Ignoring file " + name + " as it is not readable (" + attrs.getPermissionsString() + ")");
                    continue;
                }
                Date lastModified = new Date(((long) attrs.getMTime()) * 1000);
                boolean matches = listMatches.size() == 0;
                Iterator<Pattern> ipatterns = listMatches.iterator();
                while (!matches && ipatterns.hasNext()) {
                    Pattern p = ipatterns.next();
                    Matcher m = p.matcher(name);
                    if (m.find()) matches = true;
                }
                if (!matches) {
                    _logger.debug("Ignoring file " + name + " as it does not match required patterns.");
                    continue;
                }
                if (after != null) {
                    if (!lastModified.after(after)) {
                        _logger.info("Ignoring file " + name + " as its last modification date is before " + after);
                        continue;
                    }
                }
                if (before != null) {
                    if (!lastModified.before(before)) {
                        _logger.info("Ignoring file " + name + " as its last modification date is after " + before);
                        continue;
                    }
                }
                String filename = name;
                if (lastModified == null) {
                    filename = DateHelper.filenameString(DateHelper.now()) + "_CURRENT_" + name;
                } else {
                    filename = DateHelper.filenameString(lastModified) + "_VERSION_" + name;
                }
                _logger.info("Processing file (" + name + ")");
                RemoteFileState rfs = rfsdb.findOrAdd(filename, name, toUrl(subDir, name), subDir);
                rfs.server = server;
                rfs.last_modified = ((long) attrs.getMTime()) * 1000;
                rfs.addMetadata(new DateMetadata(lastModified));
                rfs.length = attrs.getSize();
                rfs.object = lsEntry;
                files.add(rfs);
            }
        }
        if (recursive) {
            _logger.debug("Processing sub directories");
            Iterator<String> i = subDirs.iterator();
            while (i.hasNext()) {
                list(db, files, i.next());
            }
        }
    }

    /**
     * Create a url for these files.  Used for storing objects uniquely in e_remote_file_state
     */
    private String toUrl(String subDir, String filename) {
        StringBuffer sb = new StringBuffer();
        sb.append("sftp://").append(server);
        if (port != 22) sb.append(':').append(port);
        if (remoteRootDir.length() > 0 && remoteRootDir.charAt(0) != '/') {
            sb.append('/');
        }
        sb.append(remoteRootDir);
        if (subDir != null && !".".equals(subDir)) {
            if (subDir.length() > 0) {
                sb.append('/').append(subDir);
            }
        }
        sb.append('/').append(filename);
        return sb.toString();
    }

    private static final int maxTries = 3;

    private static final int waitMilliseconds = 50000;

    /**
     * Returns true if the max-files hasn't been reached
     */
    @Override
    public synchronized boolean canStillFetchFile() {
        return !isMaxMirroredFilesReached();
    }

    /**
     * Sets importedFiles to 0
     */
    public synchronized void reinit() {
        resetMirroredFilesCount();
    }

    /**
     * Fetch files from the remote file server.
     *
     * If there's an error, disconnect and reconnect to the ftp server after waiting a short while.  Retry.
     *
     * @param remote Remote file state previously returned by list method in this mirror
     * @param dir Local directory to store retrieved files in.
     */
    @Override
    public MirrorReturn fetch(Db db, RemoteFileState remote, File dir, File localRootDir) throws Exception {
        if (remote == null) throw new IllegalArgumentException("remote must not be null");
        if (!(remote.object instanceof com.jcraft.jsch.ChannelSftp.LsEntry)) throw new IllegalArgumentException("remote.object must be of type com.jcraft.jsch.ChannelSftp.LsEntry");
        if (sftp == null) throw new IllegalStateException("sftp has gone away");
        String cwd = this.remoteRootDir;
        if (remote.subDir != null && !".".equals(remote.subDir)) {
            cwd = this.remoteRootDir + "/" + remote.subDir;
        }
        for (int ntries = 0; ntries < maxTries; ++ntries) {
            try {
                if (ntries > 0) {
                    _logger.info(remote.url + " try " + ntries + " sleeping " + waitMilliseconds + " ms");
                    disconnect();
                    try {
                        Thread.sleep(waitMilliseconds);
                    } catch (InterruptedException ie) {
                        _logger.debug("Got interrupted by something", ie);
                    }
                    connect();
                }
                _logger.info("Fetching remote " + remote);
                sftp.cd(cwd);
                String toPath = dir.getAbsolutePath() + File.separator + remote.filename;
                FileHelper.TempFileOutputStream tfos = FileHelper.viaTempFileOutputStream(toPath);
                try {
                    sftp.get(((com.jcraft.jsch.ChannelSftp.LsEntry) remote.object).getFilename(), tfos.fos);
                    File newFile = FileHelper.successTempFileOutputStream(tfos, toPath);
                    if (newFile == null) {
                        _logger.warn("Error storing " + remote.url);
                    } else {
                        LocalFileState local = lfsdb.findOrAdd(newFile.getName(), remote.orig_filename, localRootDir.getAbsolutePath(), remote.subDir, remote);
                        local.transferState(remote);
                        lfsdb.updateMetadata(local);
                        incrementMirroredFilesCount();
                        return new MirrorReturn(remote, local);
                    }
                } finally {
                    FileHelper.finallyTempFileOutputStream(tfos);
                }
            } catch (Exception e) {
                _logger.warn("Problem downloading " + remote.url, e);
                if (ntries == maxTries - 1) {
                    throw new Exception("Error downloading " + remote.url, e);
                } else continue;
            }
        }
        return null;
    }

    /**
     * Delete a file on the remote ssh server.
     *
     * @param remote Remote file state previously created by this mirror.
     * @return true if the delete command succeeded.
     */
    public boolean delete(RemoteFileState remote) throws Exception {
        if (remote == null) throw new IllegalArgumentException("remote must not be null");
        if (!(remote.object instanceof com.jcraft.jsch.ChannelSftp.LsEntry)) throw new IllegalArgumentException("remote.object must be of type com.jcraft.jsch.ChannelSftp.LsEntry");
        if (sftp == null) throw new IllegalStateException("client has gone away");
        com.jcraft.jsch.ChannelSftp.LsEntry file = (com.jcraft.jsch.ChannelSftp.LsEntry) remote.object;
        String cwd = this.remoteRootDir;
        if (remote.subDir != null && !".".equals(remote.subDir)) {
            cwd = this.remoteRootDir + "/" + remote.subDir;
        }
        sftp.cd(cwd);
        sftp.rm(file.getFilename());
        return true;
    }

    /**
     * Disconnect from the remote ssh server.
     */
    public void disconnect() {
        _logger.info("Disconnection from server (" + server + ")");
        try {
            try {
                if (sftp != null) sftp.disconnect();
            } catch (Exception e) {
                _logger.debug("Hiding exception ", e);
            }
            try {
                if (session != null) session.disconnect();
            } catch (Exception e) {
                _logger.debug("Hiding exception ", e);
            }
        } finally {
            jsch = null;
            session = null;
            sftp = null;
        }
    }

    /**
     */
    public Mirror clone() {
        SftpMirror cloned = new SftpMirror(remoteRootDir, server, port, username, password, 0, pathToPrivateKey, passphrase, recursive, before, after);
        return cloned;
    }

    public boolean isConnected() {
        return (sftp != null && session != null);
    }
}
