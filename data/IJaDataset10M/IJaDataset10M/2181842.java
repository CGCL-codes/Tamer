package ru.amse.timkina.manager.disc;

import ru.amse.timkina.manager.filesystem.IDirectory;
import ru.amse.timkina.manager.filesystem.IFile;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by IntelliJ IDEA.
 * User: Ira
 * Date: 04.10.2008
 * Time: 13:16:47
 * To change this template use File | Settings | File Templates.
 */
public class File implements IFile {

    private java.io.File myFile;

    public File(java.io.File file) {
        myFile = file;
    }

    public java.io.File getMyFile() {
        return myFile;
    }

    public IDirectory getParent() {
        if (myFile.getParentFile() != null) {
            return ((IDirectory) Disk.toFile(myFile.getParentFile()));
        }
        return null;
    }

    public long getSize() {
        return myFile.length();
    }

    public Calendar getModificationTime() {
        Calendar result = new GregorianCalendar();
        result.setTimeInMillis(myFile.lastModified());
        return result;
    }

    public String getName() {
        return myFile.getName();
    }

    public String getFullPath() {
        return myFile.getAbsolutePath();
    }

    public boolean isDirectory() {
        return myFile.isDirectory();
    }

    public void delete() {
        myFile.delete();
    }

    public void move(IDirectory newParent) {
        myFile.renameTo(new java.io.File(newParent.getFullPath() + myFile.getName()));
    }
}
