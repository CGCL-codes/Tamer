package org.suren.core.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.suren.core.gui.Body;
import org.suren.core.model.Queue;
import org.suren.core.model.TransferViewID;
import org.suren.core.os.UnixFile;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

public class Sftp {

    private JSch jsch;

    private static ChannelSftp sftp;

    private List<Queue> queues = new ArrayList<Queue>();

    private Session session;

    public static String[] types = { "Put", "Get" };

    public Sftp() {
    }

    public Session open(String user, String host, int port) {
        jsch = new JSch();
        try {
            session = jsch.getSession(user, host, port);
            session.setUserInfo(new UserInfoSimple(Body.rootPanel));
            session.connect();
            sftp = openChannel();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return session;
    }

    private ChannelSftp openChannel() throws JSchException {
        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();
        return channel;
    }

    public Session open(String user, String host) {
        return open(user, host, 22);
    }

    private Vector<ChannelSftp.LsEntry> ls(String path) {
        Vector<ChannelSftp.LsEntry> vector = null;
        try {
            if (queues.size() > 0) {
                ChannelSftp sftpTmp = openChannel();
                vector = sftpTmp.ls(path);
                sftpTmp.disconnect();
            } else {
                vector = sftp.ls(path);
            }
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
        return vector;
    }

    private String realpath(String path, String pwd) {
        String result = null;
        try {
            if (queues.size() > 0) {
                ChannelSftp sftpTmp = openChannel();
                cd(pwd, sftpTmp);
                result = sftpTmp.realpath(path);
                sftpTmp.disconnect();
            } else {
                result = sftp.realpath(path);
            }
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<UnixFile> list(String path) {
        List<UnixFile> list = new ArrayList<UnixFile>();
        Vector<ChannelSftp.LsEntry> vector = null;
        cd(path, null);
        vector = ls(path);
        List<ChannelSftp.LsEntry> entrys = vector.subList(0, vector.size());
        for (ChannelSftp.LsEntry entry : entrys) {
            if (".".equals(entry.getFilename()) || "..".equals(entry.getFilename())) continue;
            SftpATTRS attrs = entry.getAttrs();
            UnixFile file = new UnixFile(entry.getFilename());
            file.setDir(attrs.isDir());
            file.setPermissions(attrs.getPermissions());
            file.setPermissionsStr(attrs.getPermissionsString());
            file.setSize(attrs.getSize());
            file.setExtended(attrs.getExtended());
            file.setRealPath(realpath(entry.getFilename(), path));
            file.setAcTime(attrs.getATime());
            file.setAcTimeStr(attrs.getAtimeString());
            file.setmTime(attrs.getMTime());
            file.setmTimeStr(attrs.getMtimeString());
            list.add(file);
        }
        return list;
    }

    public void cd(String path, ChannelSftp chanel) {
        try {
            SftpATTRS attrs = attrs(path);
            if (attrs != null && attrs.isDir()) if (chanel != null) {
                chanel.cd(path);
            } else if (queues.size() > 0) {
                ChannelSftp sftpTmp = openChannel();
                sftpTmp.cd(path);
                sftpTmp.disconnect();
            } else {
                sftp.cd(path);
            }
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public String pwd() {
        String pwd = null;
        try {
            if (queues.size() > 0) {
                ChannelSftp sftpTmp = openChannel();
                pwd = sftpTmp.pwd();
                sftpTmp.disconnect();
            } else {
                pwd = sftp.pwd();
            }
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return pwd;
    }

    public String getHome() {
        String home = null;
        try {
            if (queues.size() > 0) {
                ChannelSftp sftpTmp = openChannel();
                home = sftpTmp.getHome();
                sftpTmp.disconnect();
            } else {
                home = sftp.getHome();
            }
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return home;
    }

    public void get(final UnixFile unixFile, final String dest, final JTable table) {
        final Sftp current = this;
        new Thread() {

            public void run() {
                try {
                    if (unixFile == null) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        sftp.get(queues.get(0).getUnixFile().getRealPath(), queues.get(0).getDest(), new SftpProgressMonitorSuRen(queues, current));
                    } else if (unixFile != null && unixFile.isDir()) {
                        String source = unixFile.getRealPath();
                        List<UnixFile> list = list(source);
                        for (UnixFile file : list) {
                            if (".".equals(file.getPath()) || "..".equals(file.getPath())) continue;
                            java.io.File destDir = new java.io.File(dest + "\\" + source.substring(source.lastIndexOf("/") + 1, source.length()));
                            if (!destDir.exists()) destDir.mkdir();
                            get(file, destDir.getPath(), table);
                        }
                    } else {
                        String source = unixFile.getRealPath();
                        String file = dest + "\\" + source.substring(source.lastIndexOf("/") + 1, source.length());
                        if (new java.io.File(file).exists() && JOptionPane.showConfirmDialog(Body.rootPanel, file + "is exists.") != JOptionPane.OK_OPTION) return;
                        TransferViewID viewID = new TransferViewID(System.currentTimeMillis(), types[1]);
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        model.addRow(new Object[] { viewID, source, source.substring(0, source.lastIndexOf("/") + 1), dest });
                        Queue queue = new Queue(viewID, table, unixFile, dest);
                        queues.add(queue);
                        if (queues.size() == 1) {
                            sftp.get(source, dest, new SftpProgressMonitorSuRen(queues, current));
                        }
                    }
                } catch (SftpException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public InputStream get(String source) {
        InputStream stream = null;
        try {
            stream = sftp.get(source);
        } catch (SftpException e) {
            e.printStackTrace();
        }
        return stream;
    }

    public java.io.File getFile(String source) {
        java.io.File file = null;
        FileOutputStream fileOut = null;
        InputStream is = get(source);
        if (is == null || source == null) return file;
        try {
            String name = source.substring(source.lastIndexOf("/") + 1, source.length());
            int index = name.indexOf(".");
            String prefix;
            String suffix;
            switch(index) {
                case -1:
                    prefix = name;
                    suffix = ".txt";
                    break;
                default:
                    prefix = name.substring(0, index);
                    suffix = name.substring(index, name.length());
                    break;
            }
            file = File.createTempFile(prefix.length() < 3 ? prefix + "suren" : prefix, suffix);
            fileOut = new FileOutputStream(file);
            int len;
            byte[] b = new byte[1024];
            while ((len = is.read(b)) != -1) {
                fileOut.write(b, 0, len);
            }
            is.close();
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return file;
    }

    public void put(final File source, final String dest, final JTable table) {
        final Sftp current = this;
        new Thread() {

            public void run() {
                try {
                    if (source == null) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        sftp.put(queues.get(0).getFile().getAbsolutePath(), queues.get(0).getDest(), new SftpProgressMonitorSuRen(queues, current));
                    } else if (source != null && source.isDirectory()) {
                        String sourcePath = source.getName();
                        File[] files = source.listFiles();
                        List<UnixFile> filesTmp = list(dest);
                        boolean flag = true;
                        for (UnixFile f : filesTmp) {
                            if (sourcePath.equals(f.getName())) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            mkdir(dest + "/" + source.getName());
                        }
                        for (File file : files) {
                            if (".".equals(file.getPath()) || "..".equals(file.getPath())) continue;
                            put(file, dest + "/" + source.getName(), table);
                        }
                    } else {
                        TransferViewID viewID = new TransferViewID(System.currentTimeMillis(), types[0]);
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        model.addRow(new Object[] { viewID, source.getAbsolutePath(), source.getParent(), dest });
                        Queue queue = new Queue(viewID, table, source, dest);
                        queues.add(queue);
                        if (queues.size() == 1) {
                            sftp.put(source.getAbsolutePath(), dest, new SftpProgressMonitorSuRen(queues, current));
                        }
                    }
                } catch (SftpException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void del(String path) {
        try {
            if (queues.size() > 0) {
                ChannelSftp sftpTmp = openChannel();
                sftpTmp.rm(path);
                sftpTmp.disconnect();
            } else {
                sftp.rm(path);
            }
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public void rename(String oldpath, String newpath) {
        try {
            if (queues.size() > 0) {
                ChannelSftp sftpTmp = openChannel();
                sftpTmp.rename(oldpath, newpath);
                sftpTmp.disconnect();
            } else {
                sftp.rename(oldpath, newpath);
            }
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public void mkdir(String path) {
        try {
            if (queues.size() > 0) {
                ChannelSftp sftpTmp = openChannel();
                sftpTmp.mkdir(path);
                sftpTmp.disconnect();
            } else {
                sftp.mkdir(path);
            }
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public SftpATTRS attrs(String path) {
        SftpATTRS attrs = null;
        try {
            if (queues.size() > 0) {
                ChannelSftp sftpTmp = openChannel();
                attrs = sftpTmp.lstat(path);
                sftpTmp.disconnect();
            } else {
                attrs = sftp.lstat(path);
            }
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return attrs;
    }
}
