package cn.jsprun.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.net.ftp.FTPClient;
import sun.net.ftp.FtpLoginException;

public class FtpUtils {

    private FTPClient fc;

    private String ftphost;

    private String ftpuser;

    private String ftppass;

    private String ftppath;

    private int ftpport;

    private int silent;

    private String pasv;

    private String ftpssl;

    public void setFtpValues(String ftphost, String ftpuser, String ftppass, String ftppath, int ftpport, String ftpssl, int silent, String pasv) {
        this.ftphost = ftphost;
        this.ftppass = ftppass;
        this.ftppath = ftppath;
        this.pasv = pasv;
        this.ftpport = ftpport;
        this.ftpuser = ftpuser;
        this.silent = silent;
        this.ftpssl = ftpssl;
    }

    public boolean isEmpty() {
        if (ftphost == null || ftphost.equals("")) {
            return true;
        }
        return false;
    }

    public String connectToFtpServer() {
        if ((ftphost == null) || (ftphost.equals(""))) return "FTP�����������ò���ȷ!";
        if (fc != null) {
            try {
                fc.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fc = new FTPClient();
        if (ftpssl.equals("1")) {
            SSLServerSocketFactory sslserverfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLSocketFactory sslfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            fc.setServerSocketFactory(sslserverfactory);
            fc.setSocketFactory(sslfactory);
        }
        return connectToServer();
    }

    private String connectToServer() {
        try {
            fc.connect(this.ftphost, this.ftpport);
            fc.login(ftpuser, ftppass);
            if (pasv.equals("1")) {
                fc.pasv();
            }
            if (silent != 0) {
                fc.setDefaultTimeout(silent);
            }
            fc.changeWorkingDirectory(ftppath);
        } catch (FtpLoginException e) {
            return "û����FTP���������ӵ�Ȩ��,���û����������ò���ȷ!";
        } catch (IOException e) {
            return "��FTP����������ʧ��!";
        } catch (SecurityException e) {
            return "û��Ȩ����FTP����������";
        }
        return "";
    }

    public boolean isConnect() {
        if (fc == null || !fc.isConnected()) {
            return false;
        } else {
            try {
                String path = fc.printWorkingDirectory();
                if (path == null) {
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
            return true;
        }
    }

    public void closeFtpConnect() {
        if (fc != null) {
            try {
                fc.disconnect();
            } catch (Exception e) {
            } finally {
                fc = null;
            }
        }
    }

    public boolean dftp_mkdir(String newdir) {
        boolean makebool;
        try {
            makebool = fc.makeDirectory(newdir);
            return makebool;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean dftp_rmdir(String newdir) {
        try {
            return fc.removeDirectory(newdir);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean dftp_delete(String newdir) {
        try {
            return fc.deleteFile(newdir);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean dftp_site(String newdir) {
        String cmd_mkdir = "chmod 0777 " + ftppath + "/" + newdir + "\r\n";
        try {
            return fc.sendSiteCommand(cmd_mkdir);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean dftp_chdir(String dir) {
        boolean workboolean;
        try {
            workboolean = fc.changeWorkingDirectory(dir);
            return workboolean;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isWorkingDirectory(String dir) {
        try {
            String path = fc.printWorkingDirectory();
            if (path == null) {
                return false;
            }
            int index = path.lastIndexOf("/") + 1;
            path = path.substring(index);
            if (path.equals(dir)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean put(String sourcename, String targetname, boolean test) {
        if (test) {
            connectToServer();
        }
        try {
            fc.setBufferSize(3072);
            fc.setFileType(FTPClient.BINARY_FILE_TYPE);
            InputStream is = new FileInputStream(sourcename);
            boolean strore = fc.storeFile(targetname, is);
            is.close();
            return strore;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean get(String local_file, String remote_file) {
        try {
            fc.setBufferSize(2076);
            fc.setFileType(FTPClient.BINARY_FILE_TYPE);
            FileOutputStream os = new FileOutputStream(local_file);
            fc.retrieveFile(remote_file, os);
            os.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean readfile(String url, OutputStream os) {
        InputStream in = null;
        URL servletURL = null;
        try {
            servletURL = new URL(url);
            servletURL.openConnection();
            in = servletURL.openStream();
            if (os != null) {
                byte[] bytes = new byte[1024];
                int c;
                while ((c = in.read(bytes)) != -1) {
                    os.write(bytes, 0, c);
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
            }
            servletURL = null;
        }
    }
}
