package org.privale.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map.Entry;

public class FileManager {

    /**
	 * statics
	 */
    private static HashMap<String, FileManager> ClaimedDirs;

    private static FileManagerCleaner Cleaner;

    public static synchronized void startCleanUpThread(final long val) {
        if (Cleaner == null) {
            Cleaner = new FileManagerCleaner(val);
        }
        Cleaner.Go();
    }

    public static synchronized void stopCleanUpThread() {
        if (Cleaner != null) {
            Cleaner.Stop();
        }
    }

    public static synchronized void CheckStrays() {
        for (Entry e : ClaimedDirs.entrySet()) {
            FileManager fm = (FileManager) e.getValue();
            if (fm.Stray) {
                System.out.println("WARNING: Stray directory found! " + fm.DirFile.getPath());
            }
        }
    }

    public static synchronized FileManager getDir(String dir) throws IOException {
        return getDir(dir, false);
    }

    public static synchronized void CleanAll() {
        for (Entry e : ClaimedDirs.entrySet()) {
            FileManager f = (FileManager) e.getValue();
            f.CleanUp();
        }
    }

    public static boolean diffFiles(File a, File b) throws IOException {
        boolean ok = true;
        ByteBuffer abuf = ByteBuffer.allocate(1024);
        ByteBuffer bbuf = ByteBuffer.allocate(1024);
        FileInputStream ais = new FileInputStream(a);
        FileInputStream bis = new FileInputStream(b);
        FileChannel aic = ais.getChannel();
        FileChannel bic = bis.getChannel();
        do {
            abuf.clear();
            aic.read(abuf);
            abuf.flip();
            bbuf.clear();
            bic.read(bbuf);
            bbuf.flip();
            if (!bbuf.equals(abuf)) {
                ok = false;
            }
        } while (ok && aic.position() != aic.size() && bic.position() != bic.size());
        aic.close();
        bic.close();
        return ok;
    }

    protected static synchronized FileManager getSerialDir(String dir) throws IOException {
        return getDir(dir, true);
    }

    private static synchronized FileManager getDir(String dir, boolean stray) throws IOException {
        if (ClaimedDirs == null) {
            ClaimedDirs = new HashMap<String, FileManager>();
        }
        File dirfile = new File(dir);
        FileManager fm = ClaimedDirs.get(dirfile.getPath());
        if (fm == null) {
            fm = new FileManager(dirfile.getPath(), stray);
            ClaimedDirs.put(dirfile.getPath(), fm);
        } else {
            if (fm.Stray) {
                fm.Stray = stray;
            }
        }
        return fm;
    }

    /**
	 * Instances
	 */
    public boolean HardDir;

    private HashMap<File, LinkedList<WeakReference<SmartFile>>> UsedFiles;

    private File DirFile;

    private long CurrentIndex;

    private boolean Stray;

    private FileManager(String dir, boolean stray) throws IOException {
        HardDir = false;
        Stray = stray;
        if (dir == null) {
            throw new IOException("Selected directory must not be null!");
        }
        if (dir.equals("")) {
            throw new IOException("Selected directory must not be empty string!");
        }
        DirFile = new File(dir);
        if (DirFile.exists() && !DirFile.isDirectory()) {
            throw new IOException("Selected directory is a file!");
        }
        if (!DirFile.exists()) {
            if (!DirFile.mkdir()) {
                throw new IOException("Failed to create directory!");
            }
        }
        UsedFiles = new HashMap<File, LinkedList<WeakReference<SmartFile>>>();
        File[] files = DirFile.listFiles();
        for (int cnt = 0; cnt < files.length; cnt++) {
            File f = files[cnt];
            if (f.isFile()) {
                UsedFiles.put(f, null);
            }
        }
    }

    public File createNewFile(String prefix, String suffix) throws IOException {
        SmartFile r = null;
        boolean fnd = false;
        for (long cnt = 0; cnt < Long.MAX_VALUE && !fnd; cnt++) {
            String name = DirFile.getPath() + File.separator + prefix + CurrentIndex + "." + suffix;
            r = new SmartFile(name, this);
            synchronized (UsedFiles) {
                if (!UsedFiles.containsKey(r)) {
                    if (!r.createNewFile()) {
                        throw new IOException("Failed to create the selected file! " + r.getPath());
                    }
                    fnd = true;
                    File keyfile = new File(r.getPath());
                    WeakReference<SmartFile> wr = new WeakReference<SmartFile>(r);
                    LinkedList<WeakReference<SmartFile>> l = new LinkedList<WeakReference<SmartFile>>();
                    l.add(wr);
                    UsedFiles.put(keyfile, l);
                }
            }
            if (CurrentIndex == Long.MAX_VALUE) {
                CurrentIndex = 0;
            } else {
                CurrentIndex++;
            }
        }
        if (!fnd) {
            throw new IOException("Could not find a new file name.. Good lord this is impressive!");
        }
        return r;
    }

    @SuppressWarnings("unchecked")
    protected void deleteFile(File f) {
        synchronized (UsedFiles) {
            LinkedList<WeakReference<SmartFile>> l = UsedFiles.get(f);
            if (l != null) {
                Iterator i = l.iterator();
                while (i.hasNext()) {
                    WeakReference<SmartFile> wr = (WeakReference<SmartFile>) i.next();
                    if (wr.get() == f || wr.get() == null) {
                        i.remove();
                    }
                }
                if (l.size() == 0) {
                    UsedFiles.remove(f);
                }
            }
        }
    }

    protected void RegisterHardReference(SmartFile f) {
        File keyfile = new File(f.getPath());
        WeakReference<SmartFile> wr = new WeakReference<SmartFile>(f);
        synchronized (UsedFiles) {
            LinkedList<WeakReference<SmartFile>> l = UsedFiles.get(keyfile);
            if (l == null) {
                l = new LinkedList<WeakReference<SmartFile>>();
                UsedFiles.put(keyfile, l);
            }
            l.add(wr);
        }
    }

    public String getPath() {
        return DirFile.getPath();
    }

    public File takeFile(File f, String prefix, String suffix) throws IOException {
        File nf = createNewFile(prefix, suffix);
        FileInputStream fis = new FileInputStream(f);
        FileChannel fic = fis.getChannel();
        FileOutputStream fos = new FileOutputStream(nf);
        FileChannel foc = fos.getChannel();
        foc.transferFrom(fic, 0, fic.size());
        foc.close();
        fic.close();
        f.delete();
        return nf;
    }

    /**
	 * WARNING: getFile files will not be automatically deleted
	 * once references have been reclaimed!
	 * @param name
	 * @return
	 */
    public File getFile(String name) {
        String path = DirFile.getPath() + File.separator + name;
        System.out.println("WARNING: Getting hard file: " + path);
        File f = new File(path);
        return f;
    }

    @SuppressWarnings("unchecked")
    public void CleanUp() {
        CheckStrays();
        if (!HardDir) {
            synchronized (UsedFiles) {
                Set<Entry<File, LinkedList<WeakReference<SmartFile>>>> s = UsedFiles.entrySet();
                Iterator i = s.iterator();
                while (i.hasNext()) {
                    Entry e = (Entry) i.next();
                    boolean delete = false;
                    LinkedList<WeakReference<SmartFile>> l = (LinkedList<WeakReference<SmartFile>>) e.getValue();
                    if (l != null) {
                        Iterator li = l.iterator();
                        while (li.hasNext()) {
                            WeakReference wr = (WeakReference) li.next();
                            if (wr.get() == null) {
                                li.remove();
                            }
                        }
                        if (l.size() == 0) {
                            delete = true;
                        }
                    } else {
                        delete = true;
                    }
                    File f = (File) e.getKey();
                    if (delete) {
                        System.out.println("WARNING: Deleting: " + f.getPath());
                        i.remove();
                        f.delete();
                    } else {
                        System.out.println("NOT DELETING: " + f.getPath());
                    }
                }
            }
        }
    }

    public void SplitFile(File in, File out0, File out1, long pos) throws IOException {
        FileInputStream fis = new FileInputStream(in);
        FileOutputStream fos = new FileOutputStream(out0);
        FileChannel fic = fis.getChannel();
        FileChannel foc = fos.getChannel();
        foc.transferFrom(fic, 0, pos);
        foc.close();
        fos = new FileOutputStream(out1);
        foc = fos.getChannel();
        foc.transferFrom(fic, 0, fic.size() - pos);
        foc.close();
        fic.close();
    }

    public File copyFile(File f) throws IOException {
        File t = createNewFile("fm", "cpy");
        FileOutputStream fos = new FileOutputStream(t);
        FileChannel foc = fos.getChannel();
        FileInputStream fis = new FileInputStream(f);
        FileChannel fic = fis.getChannel();
        foc.transferFrom(fic, 0, fic.size());
        foc.close();
        fic.close();
        return t;
    }
}
