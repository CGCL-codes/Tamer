package com.rapidminer.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import com.rapidminer.tools.FileSystemService;
import com.rapidminer.tools.LogService;
import com.vlsolutions.swing.docking.DockingContext;
import com.vlsolutions.swing.docking.ws.Workspace;
import com.vlsolutions.swing.docking.ws.WorkspaceException;

/**
 * 
 * @author Simon Fischer
 *
 */
public class Perspective {

    private final String name;

    private final Workspace workspace = new Workspace();

    private boolean userDefined = false;

    ;

    private final ApplicationPerspectives owner;

    public Perspective(ApplicationPerspectives owner, String name) {
        this.name = name;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void store(DockingContext dockingContext) {
        try {
            workspace.loadFrom(dockingContext);
        } catch (WorkspaceException e) {
            LogService.getRoot().log(Level.WARNING, "Cannot save workspace: " + e, e);
        }
    }

    protected void apply(DockingContext dockingContext) {
        try {
            workspace.apply(dockingContext);
        } catch (WorkspaceException e) {
            LogService.getRoot().log(Level.WARNING, "Cannot apply workspace: " + e, e);
        }
    }

    File getFile() {
        return FileSystemService.getUserConfigFile("vlperspective-" + (isUserDefined() ? "user-" : "predefined-") + name + ".xml");
    }

    public void save() {
        File file = getFile();
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            workspace.writeXML(out);
        } catch (Exception e) {
            LogService.getRoot().log(Level.WARNING, "Cannot save perspective to " + file + ": " + e, e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public void load() {
        LogService.getRoot().fine("Loading perspective: " + getName());
        File file = getFile();
        if (!file.exists()) {
            return;
        }
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            workspace.readXML(in);
        } catch (Exception e) {
            if (!userDefined) {
                LogService.getRoot().log(Level.WARNING, "Cannot read perspective from " + file + ": " + e + ". Restoring default.", e);
                owner.restoreDefault(getName());
            } else {
                LogService.getRoot().log(Level.WARNING, "Cannot read perspective from " + file + ": " + e + ". Clearing perspective.", e);
                workspace.clear();
            }
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public void setUserDefined(boolean b) {
        this.userDefined = b;
    }

    public boolean isUserDefined() {
        return this.userDefined;
    }

    public void delete() {
        File file = getFile();
        if (file.exists()) {
            file.delete();
        }
    }
}
