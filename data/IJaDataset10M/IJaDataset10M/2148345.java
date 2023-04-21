package com.calfater.mailcarbon.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;
import com.calfater.mailcarbon.constant.AccountType;
import com.calfater.mailcarbon.constant.MailConstant;

public class ConfigManager {

    private final String TRUE = "true";

    private final String FALSE = "false";

    private String _copySrcHostname = "";

    private String _copySrcLogin = "";

    private String _copySrcAccountType = AccountType.DEFAULT;

    private String _copySrcProtocol = MailConstant.IMAP_PROTOCOL;

    private String _copySrcPort = "";

    private String _copyDestHostname = "";

    private String _copyDestLogin = "";

    private String _copyDestAccountType = AccountType.DEFAULT;

    private String _copyDestProtocol = MailConstant.IMAP_PROTOCOL;

    private String _copyDestPort = "";

    private boolean _truncateChecked = false;

    private String _truncate = "";

    private boolean _defaultFolderChecked = false;

    private String _defaultFolder = "";

    private String _clearHostname = "";

    private String _clearLogin = "";

    private String _clearProtocol = "";

    private String _clearPort = "";

    public void save(File file) throws IOException {
        Properties properties = new Properties();
        properties.setProperty("copy.src.hostname", _copySrcHostname);
        properties.setProperty("copy.src.login", _copySrcLogin);
        properties.setProperty("copy.src.account_type", _copySrcAccountType);
        properties.setProperty("copy.src.protocol", _copySrcProtocol);
        properties.setProperty("copy.src.port", _copySrcPort);
        properties.setProperty("copy.dest.hostname", _copyDestHostname);
        properties.setProperty("copy.dest.login", _copyDestLogin);
        properties.setProperty("copy.dest.account_type", _copyDestAccountType);
        properties.setProperty("copy.dest.protocol", _copyDestProtocol);
        properties.setProperty("copy.dest.port", _copyDestPort);
        if (_truncateChecked) properties.setProperty("copy.opt.truncate_checked", TRUE); else properties.setProperty("copy.opt.truncate_checked", FALSE);
        properties.setProperty("copy.opt.truncate", _truncate);
        if (_defaultFolderChecked) properties.setProperty("copy.opt.default_folder_checked", TRUE); else properties.setProperty("copy.opt.default_folder_checked", FALSE);
        properties.setProperty("copy.opt.default_folder", _defaultFolder);
        properties.setProperty("clear.hostname", _clearHostname);
        properties.setProperty("clear.login", _clearLogin);
        properties.setProperty("clear.protocol", _clearProtocol);
        properties.setProperty("clear.port", _clearPort);
        properties.storeToXML(new FileOutputStream(file), "MainCarbon config file");
    }

    public void load(File file) throws FileNotFoundException, IOException {
        Properties properties = new Properties();
        properties.loadFromXML(new FileInputStream(file));
        _copySrcHostname = properties.getProperty("copy.src.hostname");
        _copySrcLogin = properties.getProperty("copy.src.login");
        _copySrcAccountType = properties.getProperty("copy.src.account_type");
        _copySrcProtocol = properties.getProperty("copy.src.protocol");
        _copySrcPort = properties.getProperty("copy.src.port");
        _copyDestHostname = properties.getProperty("copy.dest.hostname");
        _copyDestLogin = properties.getProperty("copy.dest.login");
        _copyDestAccountType = properties.getProperty("copy.dest.account_type");
        _copyDestProtocol = properties.getProperty("copy.dest.protocol");
        _copyDestPort = properties.getProperty("copy.dest.port");
        if (TRUE.equals(properties.getProperty("copy.opt.truncate_checked"))) _truncateChecked = true;
        if (FALSE.equals(properties.getProperty("copy.opt.truncate_checked"))) _truncateChecked = false;
        _truncate = properties.getProperty("copy.opt.truncate");
        if (TRUE.equals(properties.getProperty("copy.opt.default_folder_checked"))) _defaultFolderChecked = true;
        if (FALSE.equals(properties.getProperty("copy.opt.default_folder_checked"))) _defaultFolderChecked = false;
        _defaultFolder = properties.getProperty("copy.opt.default_folder");
        _clearHostname = properties.getProperty("clear.hostname");
        _clearLogin = properties.getProperty("clear.login");
        _clearProtocol = properties.getProperty("clear.protocol");
        _clearPort = properties.getProperty("clear.port");
    }

    /**
	 * @return the copySrcHostName
	 */
    public final String getCopySrcHostName() {
        return _copySrcHostname;
    }

    /**
	 * @param copySrcHostName
	 *            the copySrcHostName to set
	 */
    public final void setCopySrcHostName(String copySrcHostName) {
        _copySrcHostname = copySrcHostName;
    }

    /**
	 * @return the copySrcLogin
	 */
    public final String getCopySrcLogin() {
        return _copySrcLogin;
    }

    /**
	 * @param copySrcLogin
	 *            the copySrcLogin to set
	 */
    public final void setCopySrcLogin(String copySrcLogin) {
        _copySrcLogin = copySrcLogin;
    }

    /**
	 * @return the copySrcProtocol
	 */
    public final String getCopySrcProtocol() {
        return _copySrcProtocol;
    }

    /**
	 * @param copySrcProtocol
	 *            the copySrcProtocol to set
	 */
    public final void setCopySrcProtocol(String copySrcProtocol) {
        _copySrcProtocol = copySrcProtocol;
    }

    /**
	 * @return the copySrcPort
	 */
    public final String getCopySrcPort() {
        return _copySrcPort;
    }

    /**
	 * @param copySrcPort
	 *            the copySrcPort to set
	 */
    public final void setCopySrcPort(String copySrcPort) {
        _copySrcPort = copySrcPort;
    }

    /**
	 * @return the copyDestHostName
	 */
    public final String getCopyDestHostName() {
        return _copyDestHostname;
    }

    /**
	 * @param copyDestHostName
	 *            the copyDestHostName to set
	 */
    public final void setCopyDestHostName(String copyDestHostName) {
        _copyDestHostname = copyDestHostName;
    }

    /**
	 * @return the copyDestLogin
	 */
    public final String getCopyDestLogin() {
        return _copyDestLogin;
    }

    /**
	 * @param copyDestLogin
	 *            the copyDestLogin to set
	 */
    public final void setCopyDestLogin(String copyDestLogin) {
        _copyDestLogin = copyDestLogin;
    }

    /**
	 * @return the copyDestProtocol
	 */
    public final String getCopyDestProtocol() {
        return _copyDestProtocol;
    }

    /**
	 * @param copyDestProtocol
	 *            the copyDestProtocol to set
	 */
    public final void setCopyDestProtocol(String copyDestProtocol) {
        _copyDestProtocol = copyDestProtocol;
    }

    /**
	 * @return the copyDestPort
	 */
    public final String getCopyDestPort() {
        return _copyDestPort;
    }

    /**
	 * @param copyDestPort
	 *            the copyDestPort to set
	 */
    public final void setCopyDestPort(String copyDestPort) {
        _copyDestPort = copyDestPort;
    }

    /**
	 * @return the truncate
	 */
    public final String getTruncate() {
        return _truncate;
    }

    /**
	 * @param truncate
	 *            the truncate to set
	 */
    public final void setTruncate(String truncate) {
        _truncate = truncate;
    }

    /**
	 * @return the defaultFolder
	 */
    public final String getDefaultFolder() {
        return _defaultFolder;
    }

    /**
	 * @param defaultFolder
	 *            the defaultFolder to set
	 */
    public final void setDefaultFolder(String defaultFolder) {
        _defaultFolder = defaultFolder;
    }

    /**
	 * @return the clearHostName
	 */
    public final String getClearHostname() {
        return _clearHostname;
    }

    /**
	 * @param clearHostName
	 *            the clearHostName to set
	 */
    public final void setClearHostName(String clearHostName) {
        _clearHostname = clearHostName;
    }

    /**
	 * @return the clearLogin
	 */
    public final String getClearLogin() {
        return _clearLogin;
    }

    /**
	 * @param clearLogin
	 *            the clearLogin to set
	 */
    public final void setClearLogin(String clearLogin) {
        _clearLogin = clearLogin;
    }

    /**
	 * @return the clearProtocol
	 */
    public final String getClearProtocol() {
        return _clearProtocol;
    }

    /**
	 * @param clearProtocol
	 *            the clearProtocol to set
	 */
    public final void setClearProtocol(String clearProtocol) {
        _clearProtocol = clearProtocol;
    }

    /**
	 * @return the clearPort
	 */
    public final String getClearPort() {
        return _clearPort;
    }

    /**
	 * @param clearPort
	 *            the clearPort to set
	 */
    public final void setClearPort(String clearPort) {
        _clearPort = clearPort;
    }

    /**
	 * @return the _truncateChecked
	 */
    public final boolean isTruncateChecked() {
        return _truncateChecked;
    }

    /**
	 * @param checked
	 *            the _truncateChecked to set
	 */
    public final void setTruncateChecked(boolean checked) {
        _truncateChecked = checked;
    }

    /**
	 * @return the _defaultFolderChecked
	 */
    public final boolean isDefaultFolderChecked() {
        return _defaultFolderChecked;
    }

    /**
	 * @param folderChecked
	 *            the _defaultFolderChecked to set
	 */
    public final void setDefaultFolderChecked(boolean folderChecked) {
        _defaultFolderChecked = folderChecked;
    }

    @Override
    public String toString() {
        String str = "{";
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                str += field.getName() + "=" + field.get(this) + "; ";
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        str += "}";
        return str;
    }

    public void setCopySrcAccountType(String accountType) {
        _copySrcAccountType = accountType;
    }

    public void setCopyDestAccountType(String accountType) {
        _copyDestAccountType = accountType;
    }

    public String getCopySrcAccountType() {
        return _copySrcAccountType;
    }

    public String getCopyDestAccountType() {
        return _copyDestAccountType;
    }
}
