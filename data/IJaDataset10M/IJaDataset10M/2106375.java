package es.atareao.queensboro.file;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.SQLException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import es.atareao.alejandria.gui.ErrorDialog;
import es.atareao.alejandria.gui.ExtensionFilter;
import es.atareao.alejandria.lib.FileUtils;
import es.atareao.alejandria.lib.GeneradorUUID;
import es.atareao.queensboro.db.ConectorH2;

/**
 *
 * @author Lorenzo Carbonell
 */
public class CompactDb {

    /**
     * 
     * @param application
     * @param applicationName
     * @param applicationExtension
     * @param userName
     * @param password
     * @param SqlURL
     */
    public CompactDb(JFrame application, String applicationName, String applicationExtension, String userName, char[] password, String SqlURL) {
        this.setApp(application);
        this.setApplicationName(applicationName);
        this.setApplicationExtension(applicationExtension);
        this.setUserName(userName);
        this.setPassword(password);
        this.setSqlURL(SqlURL);
        this.setOpened(false);
        this.existsTemporalDir();
    }

    public CompactDb(JFrame application, String applicationName, String applicationExtension, String SqlURL, boolean recuperate) {
        String userName = "a";
        char[] pwd = { 'a' };
        this.setApp(application);
        this.setApplicationName(applicationName);
        this.setApplicationExtension(applicationExtension);
        this.setUserName(userName);
        this.setPassword(pwd);
        this.setSqlURL(SqlURL);
        this.setOpened(false);
        this.setRecuperate(recuperate);
        this.existsTemporalDir();
    }

    /**
     * 
     */
    private void existsTemporalDir() {
        File dir = FileUtils.getApplicationPath();
        final String como = this.getAppTemp();
        FilenameFilter ff = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                if ((name != null) && (name.length() >= como.length())) {
                    String izquierda = name.substring(0, como.length());
                    if (izquierda.equals(como)) {
                        return true;
                    }
                }
                return false;
            }
        };
        File[] files = dir.listFiles(ff);
        if (files.length > 0) {
            for (File file : files) {
                String dbName = getDbNameInDir(file);
                if ((this._recuperate) && (dbName != null)) {
                    File dbFile = FileUtils.addPathFile(file.getAbsolutePath(), dbName);
                    String db = dbFile.getAbsolutePath();
                    if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this.getApp(), "Hay un archivo pendiente de recuperar, ¿Desea recuperarlo?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
                        try {
                            this.setConector(new ConectorH2(this.getUserName(), this.getPassword(), db, ConectorH2.CREATE_YES));
                            this.getConector().openConnection();
                            this.saveAs();
                            this.getConector().closeConnection();
                        } catch (SQLException ex) {
                            ErrorDialog.manejaError(ex);
                        }
                    }
                }
                FileUtils.deleteDirectory(file);
            }
        }
    }

    private String getDbNameInDir(File dir) {
        File[] files = dir.listFiles();
        if (files.length > 0) {
            String fileName = files[0].getName();
            int pos = fileName.indexOf(".");
            return fileName.substring(0, pos);
        }
        return null;
    }

    /**
     * 
     * @return 
     */
    public boolean create() {
        this.closeFile();
        ExtensionFilter ef = new ExtensionFilter(this.getAppNameExt(), this.getAppExt());
        JFileChooser jfc = new JFileChooser(this.getDefaultDir());
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.setFileFilter(ef);
        if (JFileChooser.APPROVE_OPTION == jfc.showSaveDialog(this.getApp())) {
            File file = this.adecuateFile(jfc.getSelectedFile());
            this.setFile(file);
            this.setDefaultDir(file.getParentFile());
            return this.createFile(file);
        }
        return false;
    }

    /**
     *
     * @return
     */
    public boolean create(File file) {
        this.setFile(file);
        this.setDefaultDir(file.getParentFile());
        return this.createFile(file);
    }

    /**
     * 
     * @return 
     */
    public boolean open() {
        this.closeFile();
        ExtensionFilter ef = new ExtensionFilter(this.getAppNameExt(), this.getAppExt());
        JFileChooser jfc = new JFileChooser(this.getDefaultDir());
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.setFileFilter(ef);
        if (JFileChooser.APPROVE_OPTION == jfc.showOpenDialog(this.getApp())) {
            File file = this.adecuateFile(jfc.getSelectedFile());
            this.setFile(file);
            this.setDefaultDir(file.getParentFile());
            return this.openFile(file);
        }
        return false;
    }

    /**
     * 
     * @param file
     * @return 
     */
    public boolean open(File file) {
        this.closeFile();
        this.setFile(file);
        this.setDefaultDir(file.getParentFile());
        return this.openFile(file);
    }

    /**
     * 
     * @return 
     */
    public boolean close() {
        if (this.getConector() != null) {
            if (this.getFile() != null) {
                return this.closeFile();
            }
        }
        return false;
    }

    /**
     * 
     * @return 
     */
    public boolean save() {
        if (this.getConector() != null) {
            if (this.getFile() != null) {
                return this.saveFile(this.getFile());
            }
        }
        return false;
    }

    /**
     * 
     * @return 
     */
    public boolean saveAs() {
        ExtensionFilter ef = new ExtensionFilter(this.getAppNameExt(), this.getAppExt());
        JFileChooser jfc = new JFileChooser(this.getDefaultDir());
        jfc.setFileFilter(ef);
        jfc.setAcceptAllFileFilterUsed(false);
        if (JFileChooser.APPROVE_OPTION == jfc.showSaveDialog(this.getApp())) {
            File file = this.adecuateFile(jfc.getSelectedFile());
            if (file.exists()) {
                if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this.getApp(), "Ya existe una archivo con ese nombre, ¿Desea sobreescribirlo?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
                    if (!file.delete()) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            this.setDefaultDir(file.getParentFile());
            this.saveFile(file);
            this.closeFile();
            this.openFile(file);
            return true;
        }
        return false;
    }

    private File adecuateFile(File file) {
        File path = file.getParentFile();
        String fileName = file.getName();
        String ext = FileUtils.getExtension(file);
        if ((ext == null) || (ext.length() == 0)) {
            fileName += this.getAppExt();
            return FileUtils.addPathFile(path, new File(fileName));
        }
        if (ext.equals(this.getAppExt())) {
            fileName = FileUtils.getFileNameWithoutExtension(file);
            fileName += this.getAppExt();
            return FileUtils.addPathFile(path, new File(fileName));
        }
        return file;
    }

    private String getAppNameExt() {
        StringBuffer sb = new StringBuffer();
        sb.append("Archivos ");
        sb.append(this.getApplicationName().toLowerCase());
        sb.append(" (");
        sb.append(this.getAppExt());
        sb.append(")");
        return sb.toString();
    }

    private String getAppExt() {
        StringBuffer sb = new StringBuffer();
        sb.append(".");
        sb.append(this.getApplicationExtension().toLowerCase());
        return sb.toString();
    }

    private String getAppTemp() {
        StringBuffer sb = new StringBuffer();
        sb.append("temporal_");
        sb.append(this.getApplicationName().toLowerCase());
        sb.append("_");
        return sb.toString();
    }

    private String getAppNameForm() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getApplicationName().substring(0, 1).toUpperCase());
        sb.append(this.getApplicationName().substring(1, this.getApplicationName().length()).toLowerCase());
        return sb.toString();
    }

    private File createTemporalDir() {
        return FileUtils.addPathFile(FileUtils.getApplicationPath().getAbsolutePath(), this.getAppTemp() + GeneradorUUID.crearUUID());
    }

    private File createTemporalDb(File file) {
        return FileUtils.addPathFile(this.getTemporalDir().getAbsolutePath(), FileUtils.getFileNameWithoutExtension(file.getName()));
    }

    private boolean createFile(File file) {
        if (file.exists()) {
            if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this.getApp(), "Ya existe una archivo con ese nombre, ¿Desea sobreescribirlo?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
                if (!file.delete()) {
                    return false;
                }
            } else {
                return false;
            }
        }
        try {
            this.setFile(file);
            this.getApp().setTitle(this.getAppNameForm() + " - " + file.getAbsolutePath());
            this.setModificado(false);
            String sql = FileUtils.readTextFromJar(this.getSqlURL());
            File parentdir = this.getTemporalDir();
            String db = this.getTemporalDb().getAbsolutePath();
            if (parentdir.exists()) {
                FileUtils.deleteDirectory(parentdir);
            }
            this.setConector(new ConectorH2(this.getUserName(), this.getPassword(), db, ConectorH2.CREATE_YES));
            this.getConector().openConnection();
            this.getConector().execute(sql);
            (this.getConector()).saveScript(file.getAbsolutePath());
            return true;
        } catch (SQLException ex) {
            ErrorDialog.manejaError(ex);
        }
        return false;
    }

    private boolean openFile(File file) {
        try {
            this.setFile(file);
            this.getApp().setTitle(this.getAppNameForm() + " - " + file.getAbsolutePath());
            this.setModificado(false);
            File parentdir = this.getTemporalDir();
            String db = this.getTemporalDb().getAbsolutePath();
            if (parentdir.exists()) {
                FileUtils.deleteDirectory(parentdir);
            }
            this.setConector(new ConectorH2(this.getUserName(), this.getPassword(), db, ConectorH2.CREATE_YES));
            this.getConector().openConnection();
            (this.getConector()).loadScript(file.getAbsolutePath());
            this.setOpened(true);
            return true;
        } catch (SQLException ex) {
            ErrorDialog.manejaError(ex);
        }
        return false;
    }

    private boolean closeFile() {
        if (this.isModificado()) {
            if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this.getApp(), "El archivo ha sido modificado, ¿Quiere guardarlo?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
                this.saveFile(this.getFile());
            }
        }
        if (this.getConector() != null) {
            try {
                this.getConector().closeConnection();
                if (this.getFile() != null) {
                    File tmpdir = this.getTemporalDir();
                    if (tmpdir.exists()) {
                        FileUtils.deleteDirectory(tmpdir);
                    }
                }
                this.setFile(null);
                this.setTemporalDb(null);
                this.setTemporalDir(null);
                this.setConector(null);
                this.setModificado(false);
                this.setOpened(false);
                return true;
            } catch (SQLException ex) {
                ErrorDialog.manejaError(ex, true);
            }
        }
        return false;
    }

    private boolean saveFile(File file) {
        if (this.getConector() != null) {
            if (file.exists()) {
                file.delete();
            }
            try {
                (this.getConector()).saveScript(file.getAbsolutePath());
                this.setModificado(false);
                return true;
            } catch (SQLException ex) {
                ErrorDialog.manejaError(ex);
            }
        }
        return false;
    }

    private String _applicationName;

    private String _applicationExtension;

    private String _sqlURL;

    private ConectorH2 _conector;

    private boolean _modificado;

    private File _file;

    private File _temporalDir;

    private File _temporalDb;

    private File _defaultDir;

    private JFrame _app;

    private String _userName;

    private char[] _password;

    private boolean _opened;

    private boolean _recuperate = true;

    public File getFile() {
        return _file;
    }

    public void setFile(File file) {
        this._file = file;
        if (file != null) {
            this.setTemporalDir(this.createTemporalDir());
            this.setTemporalDb(this.createTemporalDb(file));
        } else {
            this.setTemporalDb(null);
            this.setTemporalDir(null);
        }
    }

    public File getTemporalDir() {
        return _temporalDir;
    }

    public void setTemporalDir(File temporalDir) {
        this._temporalDir = temporalDir;
    }

    public File getTemporalDb() {
        return _temporalDb;
    }

    public void setTemporalDb(File temporalDb) {
        this._temporalDb = temporalDb;
    }

    public boolean isModificado() {
        return _modificado;
    }

    public void setModificado(boolean modificado) {
        this._modificado = modificado;
    }

    public ConectorH2 getConector() {
        return _conector;
    }

    public void setConector(ConectorH2 conector) {
        this._conector = conector;
    }

    public File getDefaultDir() {
        return _defaultDir;
    }

    public void setDefaultDir(File defaultDir) {
        this._defaultDir = defaultDir;
    }

    public JFrame getApp() {
        return _app;
    }

    public void setApp(JFrame app) {
        this._app = app;
    }

    public String getApplicationName() {
        return _applicationName;
    }

    public void setApplicationName(String applicationName) {
        this._applicationName = applicationName;
    }

    public String getApplicationExtension() {
        return _applicationExtension;
    }

    public void setApplicationExtension(String applicationExtension) {
        this._applicationExtension = applicationExtension;
    }

    public String getSqlURL() {
        return _sqlURL;
    }

    public void setSqlURL(String sqlURL) {
        this._sqlURL = sqlURL;
    }

    public String getUserName() {
        return _userName;
    }

    public void setUserName(String userName) {
        this._userName = userName;
    }

    public char[] getPassword() {
        return _password;
    }

    public void setPassword(char[] password) {
        this._password = password;
    }

    /**
     * @return the _opened
     */
    public boolean isOpened() {
        return _opened;
    }

    /**
     * @param opened the _opened to set
     */
    public void setOpened(boolean opened) {
        this._opened = opened;
    }

    /**
     * @return the _recuperate
     */
    public boolean isRecuperate() {
        return _recuperate;
    }

    /**
     * @param recuperate the _recuperate to set
     */
    public void setRecuperate(boolean recuperate) {
        this._recuperate = recuperate;
    }
}
