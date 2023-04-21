package addressbook.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Properties;
import rogatkin.HttpUtils;
import addressbook.AddressBookFrame;
import addressbook.servlet.model.UserOperations.DuplicateUser;

public class IoHelper {

    protected URL repositoryBase;

    public IoHelper(String rootName, String nodeName, Properties property) {
        try {
            File f = new File(rootName + File.separatorChar + AddressBookFrame.PROGRAMNAME + File.separatorChar + nodeName);
            if (f.exists() == false) f.mkdirs(); else if (f.isFile()) {
                if (f.delete()) f.mkdirs();
            }
            repositoryBase = f.toURI().toURL();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public InputStream getInStream(String userName) throws IOException {
        URLConnection uc = new URL(repositoryBase, URLEncoder.encode(HttpUtils.htmlEncode(userName), "utf-8")).openConnection();
        if (uc instanceof HttpURLConnection) {
            HttpURLConnection htuc = (HttpURLConnection) uc;
            htuc.connect();
            if (htuc.getResponseCode() == HttpURLConnection.HTTP_OK) return htuc.getInputStream(); else htuc.disconnect();
        } else return uc.getInputStream();
        return null;
    }

    public OutputStream getOutStream(String userName, boolean create) throws DuplicateUser, IOException {
        try {
            File pf = new File(new URL(repositoryBase, URLEncoder.encode(HttpUtils.htmlEncode(userName), "utf-8")).toURI());
            if (pf.exists() && create) throw new DuplicateUser(userName);
            return new FileOutputStream(pf);
        } catch (URISyntaxException use) {
            use.printStackTrace();
        }
        return null;
    }

    public void backup(String userName) throws IOException {
        try {
            File tf = new File(new URL(repositoryBase, URLEncoder.encode(HttpUtils.htmlEncode(userName + ".bak"), "utf-8")).toURI());
            tf.delete();
            File sf = new File(new URL(repositoryBase, URLEncoder.encode(HttpUtils.htmlEncode(userName), "utf-8")).toURI());
            if (sf.exists() && sf.renameTo(tf) == false) throw new IOException("Can't rename " + sf + " to " + tf);
        } catch (URISyntaxException use) {
            use.printStackTrace();
        }
    }

    public boolean delete(String userName) {
        return delete(userName, false);
    }

    public boolean delete(String userName, boolean bak) {
        try {
            File sf = new File(new URL(repositoryBase, URLEncoder.encode(HttpUtils.htmlEncode(userName), "utf-8")).toURI());
            if (bak && sf.delete()) sf = new File(new URL(repositoryBase, URLEncoder.encode(HttpUtils.htmlEncode(userName + ".bak"), "utf-8")).toURI());
            return sf.delete();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public File getFile(String name) {
        try {
            return new File(new URL(repositoryBase, URLEncoder.encode(HttpUtils.htmlEncode(name), "utf-8")).toURI());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
