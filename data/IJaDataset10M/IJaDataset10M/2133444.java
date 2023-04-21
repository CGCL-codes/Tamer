package org.contineo.core.doxter.filesystem;

import java.io.File;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.contineo.core.FileBean;
import org.contineo.core.doxter.Storer;
import org.contineo.util.Context;
import org.contineo.util.config.BackupConfig;
import org.contineo.util.config.SettingsConfig;

/**
 * This class is an implementation of the Storer interface to persist documents
 * in the filesystem.
 * 
 * @author Michael Scholz
 */
public class FSStorer implements Storer {

    protected static Log logger = LogFactory.getLog(FSStorer.class);

    private SettingsConfig settingsConfig;

    public FSStorer() {
    }

    public SettingsConfig getSettingsConfig() {
        return settingsConfig;
    }

    public void setSettingsConfig(SettingsConfig settingsConfig) {
        this.settingsConfig = settingsConfig;
    }

    /**
     * @see org.contineo.core.doxter.Storer#store(java.io.InputStream,
     *      java.lang.String, java.lang.String)
     */
    public boolean store(InputStream stream, String menupath, String filename, String version) {
        try {
            String path = new StringBuilder(settingsConfig.getValue("docdir")).append("/").append(menupath).append(File.separator).toString();
            FileBean.createDir(path);
            FileBean.writeFile(stream, new StringBuilder(path).append(filename).toString());
            BackupConfig conf = (BackupConfig) Context.getInstance().getBean(BackupConfig.class);
            if (conf.isEnabled()) {
                String backupPath = conf.getLocation();
                FileBean.copyDir(path, new StringBuilder(backupPath).append(menupath).toString());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    public boolean delete(String menupath) {
        SettingsConfig settings = (SettingsConfig) Context.getInstance().getBean(SettingsConfig.class);
        String path = settings.getValue("docdir") + "/";
        BackupConfig backup = (BackupConfig) Context.getInstance().getBean(BackupConfig.class);
        String backupPath = backup.getLocation();
        boolean delOrg = FileBean.deleteDir(new StringBuilder(path).append(menupath).toString());
        boolean delBac = true;
        if (backup.isEnabled()) {
            delBac = FileBean.deleteDir(new StringBuilder(backupPath).append(menupath).toString());
        }
        return (delOrg && delBac);
    }

    public boolean restoreAll() {
        SettingsConfig settings = (SettingsConfig) Context.getInstance().getBean(SettingsConfig.class);
        String path = settings.getValue("docdir") + "/";
        BackupConfig backup = (BackupConfig) Context.getInstance().getBean(BackupConfig.class);
        String backupPath = backup.getLocation();
        boolean varBack = false;
        boolean deleted = FileBean.deleteDir(path);
        if (deleted) {
            boolean copied = FileBean.copyDir(backupPath, path);
            if (copied) {
                varBack = true;
            } else {
                varBack = false;
            }
        } else {
            varBack = false;
        }
        return varBack;
    }
}
