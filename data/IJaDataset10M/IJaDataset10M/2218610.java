package jp.ne.nifty.iga.midori.shell.dir;

import jp.ne.nifty.iga.midori.shell.eng.MdShellEnv;
import java.io.*;

/**
 * JMichelle (Java Midori Shell) : path class
 */
public class MdShellDirFileFileSystem extends MdShellDirFileAbstract {

    private MdShellEnv shellenv = null;

    private File fileMySelf = null;

    public MdShellDirFileFileSystem(MdShellEnv shellenv, File file) {
        this.shellenv = shellenv;
        this.fileMySelf = file;
        setType(FILE);
        try {
            setName(MdShellDirFactory.native2uri(fileMySelf.getCanonicalPath()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        setFileLength(fileMySelf.length());
        this.lastModified = fileMySelf.lastModified();
        bCanWrite = fileMySelf.canWrite();
        bCanRead = fileMySelf.canRead();
    }

    public String getShortName() {
        return MdShellDirFactory.getShortName(getName());
    }

    public MdShellDirDirectoryAbstract getPath() {
        if (new File(MdShellDirFactory.uri2native(getName())).isDirectory()) {
            return (MdShellDirDirectoryAbstract) MdShellDirFactory.getInstance(shellenv, getName(), true);
        }
        String strPathWithoutShortName = MdShellDirFactory.getPathWithoutShortName(getName());
        return (MdShellDirDirectoryAbstract) MdShellDirFactory.getInstance(shellenv, strPathWithoutShortName, true);
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
        fileMySelf.setLastModified(lastModified);
    }

    public final InputStream getInputStream() {
        try {
            return new FileInputStream(fileMySelf);
        } catch (IOException ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
        return null;
    }

    public final OutputStream getOutputStream() {
        try {
            return new FileOutputStream(fileMySelf);
        } catch (IOException ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
        return null;
    }

    /**
	 * Get output stream for file.
	 *
	 * @param flag append flag. If set to true, open by append mode.
	 * @return output stream.
	 */
    public final OutputStream getOutputStream(boolean flag) {
        try {
            return new FileOutputStream(fileMySelf.getCanonicalPath(), flag);
        } catch (IOException ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
        return null;
    }

    public boolean delete() {
        return fileMySelf.delete();
    }
}
