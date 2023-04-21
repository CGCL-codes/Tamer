package net.sourceforge.x360mediaserve.dataManager.impl.config;

import java.io.Serializable;
import java.util.List;

public class DataManagerConfigModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String dirToAdd = "";

    private String dirToDelete = "";

    private List<String> dirs;

    private List<String> scanOptions;

    private String scanOption = "";

    private String scanningStatus = "";

    public List<String> getScanOptions() {
        return scanOptions;
    }

    public void setScanOptions(List<String> options) {
        this.scanOptions = options;
    }

    public String getDirToAdd() {
        return dirToAdd;
    }

    public void setDirToAdd(String dirToAdd) {
        this.dirToAdd = dirToAdd;
    }

    public String getDirToDelete() {
        return dirToDelete;
    }

    public void setDirToDelete(String dirToDelete) {
        this.dirToDelete = dirToDelete;
    }

    public List<String> getDirs() {
        return dirs;
    }

    public void setDirs(List<String> dirs) {
        this.dirs = dirs;
    }

    public String getScanOption() {
        return scanOption;
    }

    public void setScanOption(String scanOption) {
        this.scanOption = scanOption;
    }

    public String getScanningStatus() {
        return scanningStatus;
    }

    public void setScanningStatus(String scanningStatus) {
        this.scanningStatus = scanningStatus;
    }
}
