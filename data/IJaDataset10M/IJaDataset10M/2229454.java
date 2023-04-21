package net.sf.mavenizer.analyser.scanner;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author <a href="mailto:cedric-vidal@users.sourceforge.net">C&eacute;dric Vidal</a>
 *
 */
public class DefaultScanner implements Scanner {

    private FileFilter fileFilter;

    static final String EXTENSION_JAR = ".jar";

    private StreamHandler scannerListener = null;

    private UnitHandler unitListener = null;

    public void scan(File file) throws ScannerException {
        if (!file.exists()) {
            throw new ScannerException(new FileNotFoundException(file.toString()));
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles(getFileFilter());
            for (int i = 0; i < files.length; i++) {
                File childFile = files[i];
                scan(childFile);
            }
        } else if (file.getName().endsWith(EXTENSION_JAR)) {
            scanJarFile(file);
        } else {
            throw new ScannerException(file + " is neither a directory nor ends with the .jar extension");
        }
    }

    protected FileFilter getFileFilter() {
        if (fileFilter == null) {
            fileFilter = new ScannerFileFilter();
        }
        return fileFilter;
    }

    private void scanJarFile(File file) throws ScannerException {
        unitListener().unit(file);
        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
            ZipEntry e = zis.getNextEntry();
            while (e != null) {
                String name = e.getName();
                if (name.endsWith(".class")) {
                    scannerListener().stream(file, zis);
                }
                e = zis.getNextEntry();
            }
        } catch (Exception e) {
            throw new ScannerException(e);
        }
    }

    public StreamHandler scannerListener() {
        if (scannerListener == null) {
            throw new IllegalStateException("ScannerListener is null");
        }
        return scannerListener;
    }

    public StreamHandler getScannerListener() {
        return scannerListener;
    }

    public void setScannerListener(StreamHandler scannerListener) {
        this.scannerListener = scannerListener;
    }

    public UnitHandler unitListener() {
        if (unitListener == null) {
            throw new IllegalStateException("UnitListener is null");
        }
        return unitListener;
    }

    public UnitHandler getUnitListener() {
        return unitListener;
    }

    public void setUnitListener(UnitHandler unitListener) {
        this.unitListener = unitListener;
    }

    public void setFileFilter(FileFilter fileFilter) {
        this.fileFilter = fileFilter;
    }
}
