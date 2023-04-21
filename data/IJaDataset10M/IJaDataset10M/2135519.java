package net.sf.bluex.components;

import java.io.File;
import java.util.Arrays;
import java.util.Vector;

/**
 *
 * @author Blue
 */
public class BlueXFile implements Comparable {

    private File file;

    public BlueXFile(File file) {
        this.file = file;
    }

    /**
     * Overrided compareto method :)
     * @param pathname
     * @return
     */
    public int compareTo(Object obj) {
        File other = ((BlueXFile) obj).getFile();
        if (BlueXStatics.fsv.isDrive(other)) return file.getAbsolutePath().compareTo(other.getAbsolutePath()); else if (file.isDirectory() && !other.isDirectory()) return -1; else if (!file.isDirectory() && other.isDirectory()) return 1;
        return file.compareTo(other);
    }

    public File getFile() {
        return file;
    }

    public static BlueXFile[] getArrayOfBlueXFiles(File[] files) {
        if (files != null) {
            BlueXFile[] bxf = new BlueXFile[files.length];
            for (int i = 0; i < files.length; i++) bxf[i] = new BlueXFile(files[i]);
            return bxf;
        } else return null;
    }

    public static Vector<BlueXFile> getVectorOfBlueXFiles(Vector<File> files) {
        if (files != null) {
            Vector<BlueXFile> bxf = new Vector<BlueXFile>(files.size());
            for (File file : files) bxf.add(new BlueXFile(file));
            return bxf;
        } else return null;
    }

    public static BlueXFile[] getArrayOfBlueXFiles(Vector<File> files) {
        if (files != null) {
            BlueXFile[] bxf = new BlueXFile[files.size()];
            for (int i = 0; i < files.size(); i++) bxf[i] = new BlueXFile(files.elementAt(i));
            return bxf;
        } else return null;
    }

    @Override
    public String toString() {
        return BlueXStatics.getFileName(file);
    }

    public static BlueXFile[] getRoots() {
        File[] files = File.listRoots();
        BlueXFile[] bxf;
        if (BlueXStatics.isWindowsOS()) {
            Vector<File> vectBFX = new Vector<File>();
            for (File file : files) {
                String absPath = file.getAbsolutePath();
                if (!absPath.equals("A:\\") && !absPath.equals("B:\\")) vectBFX.add(file);
            }
            bxf = BlueXFile.getArrayOfBlueXFiles(vectBFX);
        } else bxf = getArrayOfBlueXFiles(files);
        Arrays.sort(bxf);
        return bxf;
    }
}
