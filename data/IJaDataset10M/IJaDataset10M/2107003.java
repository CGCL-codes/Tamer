package com.cronopista.lightpacker.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import resources.ResourceBase;
import com.cronopista.lightpacker.Main;
import com.cronopista.lightpacker.GUI.ProgressMonitor;
import com.cronopista.util.FileUtils;

/**
 * @author Eduardo Rodr�guez
 * 
 */
public class InstallFileAction extends Action {

    private String sourceLocation;

    private String destination;

    public int execute(ProgressMonitor progressMonitor) {
        int res = OK;
        File out = new File(Main.getInstance().resolve(destination));
        File parentFile = out.getParentFile();
        if (!parentFile.exists()) parentFile.mkdirs();
        InputStream fis = ResourceBase.class.getResourceAsStream(sourceLocation);
        if (progressMonitor != null) progressMonitor.addMessage(Main.getInstance().translate("installing.copy", "filename", out.getName()), true);
        try {
            FileUtils.copyInputStream(fis, new FileOutputStream(out), progressMonitor);
        } catch (Exception e) {
            progressMonitor.addMessage(Main.getInstance().translate("installing.error", "filename", out.getName()), false);
            res = getErrorLevel();
        }
        return res;
    }

    public String getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(String sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getProgressSize() {
        return getBytes();
    }
}
