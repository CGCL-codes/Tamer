package it.freax.fpm.core.solver.specs;

import java.io.File;
import java.util.Calendar;

/**
 * FileSystemSpec: if an instance of this object has only a name, this object's
 * related file doesn't exists on disk.
 * 
 * @author kLeZ-hAcK
 */
public class FileSystemSpec {

    private String Name;

    private String Path;

    private String User;

    private String Group;

    private int[] Permissions;

    private Calendar LastModified;

    private long Size;

    private Spec Package;

    public String getGroup() {
        return this.Group;
    }

    public Calendar getLastModified() {
        return this.LastModified;
    }

    public String getName() {
        return this.Name;
    }

    public String getPath() {
        return this.Path;
    }

    public int[] getPermissions() {
        return this.Permissions;
    }

    public long getSize() {
        return this.Size;
    }

    public String getUser() {
        return this.User;
    }

    public void Load(File file) {
    }

    public Spec getPackage() {
        return this.Package;
    }
}
