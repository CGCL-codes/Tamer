package fi.kaila.suku.kontroller;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;
import javax.jnlp.BasicService;
import javax.jnlp.FileContents;
import javax.jnlp.FileOpenService;
import javax.jnlp.FileSaveService;
import javax.jnlp.PersistenceService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;
import fi.kaila.suku.swing.Suku;
import fi.kaila.suku.util.Resurses;
import fi.kaila.suku.util.SukuException;
import fi.kaila.suku.util.Utils;
import fi.kaila.suku.util.pojo.SukuData;

/**
 * The Class SukuKontrollerWebstartImpl.
 * 
 * @author FIKAAKAIL
 * 
 *         webstart implementation for kontroller
 */
public class SukuKontrollerWebstartImpl implements SukuKontroller {

    private static Logger logger = null;

    private FileContents fc = null;

    /**
	 * constructor sets environment for remote.
	 */
    public SukuKontrollerWebstartImpl() {
        logger = Logger.getLogger(this.getClass().getName());
        try {
            BasicService bs = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
            if (bs != null) {
                this.codebase = bs.getCodeBase().toString();
            }
        } catch (UnavailableServiceException e) {
            this.codebase = "http://localhost/suku/";
            e.printStackTrace();
        }
    }

    private String codebase = null;

    private String userno = null;

    private String schema = null;

    private boolean isConnected = false;

    @Override
    public void getConnection(String host, String dbname, String userid, String passwd) throws SukuException {
        schema = null;
        isConnected = false;
        String requri = this.codebase + "SukuServlet?userid=" + userid + "&passwd=" + passwd;
        int resu;
        try {
            URL url = new URL(requri);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            String encoding = uc.getContentEncoding();
            resu = uc.getResponseCode();
            if (resu == 200) {
                InputStream in;
                if ("gzip".equals(encoding)) {
                    in = new java.util.zip.GZIPInputStream(uc.getInputStream());
                } else {
                    in = uc.getInputStream();
                }
                byte b[] = new byte[1024];
                int pit = in.read(b);
                for (int i = 0; i < pit; i++) {
                    if (b[i] == '\n' || b[i] == '\r') {
                        pit = i;
                        break;
                    }
                }
                String aux = new String(b, 0, pit);
                String auxes[] = aux.split("/");
                this.userno = auxes[0];
                if (auxes.length > 1) {
                    Suku.serverVersion = auxes[1];
                }
                in.close();
                this.schema = userid;
                isConnected = true;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new SukuException(Resurses.getString("ERR_NOT_CONNECTED") + " [" + e.toString() + "]");
        }
    }

    @Override
    public void resetConnection() {
        isConnected = false;
    }

    @Override
    public String getPref(Object o, String key, String def) {
        String aux;
        try {
            PersistenceService ps = (PersistenceService) ServiceManager.lookup("javax.jnlp.PersistenceService");
            BasicService bs = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
            URL baseURL = bs.getCodeBase();
            URL editorURL = new URL(baseURL, key);
            FileContents fc = ps.get(editorURL);
            DataInputStream is = new DataInputStream(fc.getInputStream());
            aux = is.readUTF();
            is.close();
            return aux;
        } catch (FileNotFoundException fe) {
            return def;
        } catch (Exception e) {
            Utils.println(this, "Kaatui: e=" + e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void putPref(Object o, String key, String value) {
        PersistenceService ps;
        try {
            ps = (PersistenceService) ServiceManager.lookup("javax.jnlp.PersistenceService");
            BasicService bs = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
            URL baseURL = bs.getCodeBase();
            URL keyURL = new URL(baseURL, key);
            FileContents fc = null;
            try {
                fc = ps.get(keyURL);
                ps.delete(keyURL);
            } catch (FileNotFoundException fe) {
                System.out.println("putPref fnf " + fe.toString());
            }
            ps.create(keyURL, 1024);
            fc = ps.get(keyURL);
            DataOutputStream os = new DataOutputStream(fc.getOutputStream(false));
            os.writeUTF(value);
            os.flush();
            os.close();
        } catch (Exception e) {
            System.out.println("putPref e " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public SukuData getSukuData(String... params) throws SukuException {
        return KontrollerUtils.getSukuData(this.codebase, this.userno, params);
    }

    @Override
    public boolean openFile(String filter) {
        FileOpenService fos;
        InputStream iis = null;
        try {
            fos = (FileOpenService) ServiceManager.lookup("javax.jnlp.FileOpenService");
            fc = fos.openFileDialog(null, null);
            if (fc == null) {
                return false;
            }
            iis = fc.getInputStream();
            int resu = KontrollerUtils.openFile(this.codebase, this.userno, getFileName(), iis);
            if (resu == 200) {
                return true;
            }
            Utils.println(this, "openFile returned response " + resu);
        } catch (Exception e) {
            Utils.println(this, e.toString());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public SukuData getSukuData(SukuData request, String... params) throws SukuException {
        return KontrollerUtils.getSukuData(this.codebase, this.userno, request, params);
    }

    @Override
    public long getFileLength() {
        if (fc != null) {
            try {
                return fc.getLength();
            } catch (IOException e) {
                Utils.println(this, e.toString());
            }
        }
        return 0;
    }

    @Override
    public InputStream getInputStream() {
        if (fc != null) {
            try {
                return fc.getInputStream();
            } catch (IOException e) {
                Utils.println(this, "getInputStream " + e.toString());
                return null;
            }
        }
        Utils.println(this, "getInputStream not found");
        logger.severe("getInputStream not found");
        return null;
    }

    @Override
    public String getFileName() {
        if (fc != null) {
            try {
                return fc.getName();
            } catch (IOException e) {
                Utils.println(this, "getFileName: " + e.toString());
            }
        }
        return null;
    }

    @Override
    public boolean createLocalFile(String filter) {
        FileSaveService fos;
        try {
            fos = (FileSaveService) ServiceManager.lookup("javax.jnlp.FileSaveService");
            String[] extensions = new String[1];
            extensions[0] = filter;
            byte[] buffi = { '0', '1', '2', '3' };
            ByteArrayInputStream in = new ByteArrayInputStream(buffi);
            fc = fos.saveFileDialog(null, extensions, in, "FinFamily");
            return true;
        } catch (Exception e) {
            Utils.println(this, "createLocal: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public OutputStream getOutputStream() {
        return null;
    }

    @Override
    public InputStream openLocalFile(String path) {
        try {
            FileOpenService fos = (FileOpenService) ServiceManager.lookup("javax.jnlp.FileOpenService");
            fc = fos.openFileDialog(null, null);
            if (fc != null) {
                return fc.getInputStream();
            }
        } catch (Exception e1) {
            Utils.println(this, "openfile: " + e1.toString());
        }
        return null;
    }

    @Override
    public String getFilePath() {
        return null;
    }

    @Override
    public boolean isRemote() {
        return true;
    }

    @Override
    public boolean saveFile(String filter, InputStream in) {
        FileSaveService fos;
        try {
            fos = (FileSaveService) ServiceManager.lookup("javax.jnlp.FileSaveService");
            String[] extensions = new String[1];
            extensions[0] = Resurses.getString("FULL_PATHNAME") + " " + filter;
            fc = fos.saveFileDialog(null, extensions, in, "FinFamily" + filter);
            return true;
        } catch (Exception e) {
            Utils.println(this, "createLocal: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isWebStart() {
        return true;
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public String getSchema() {
        return this.schema;
    }

    @Override
    public void setSchema(String schema) {
    }
}
