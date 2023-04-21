package org.jclamav;

import java.io.File;
import java.util.Collection;
import org.jclamav.util.CLIProcess;

public class JClamAV {

    static {
        System.load("/media/disk/development/netbeanProjects/JClamAVNative/dist/Release/GNU-Linux-x86/libJClamAVNative.so");
    }

    /**
     * Load phishing signatures. 
     */
    public static final int CL_DB_PHISHING = 0x2;

    /**
     * Initialize the phishing detection module and load .wdb and .pdb files. 
     */
    public static final int CL_DB_PHISHING_URLS = 0x8;

    /**
     * Load CVD files directly without unpacking them into a temporary directory. 
     */
    public static final int CL_DB_CVDNOTMP = 0x20;

    /**
     * Load signatures for Potentially Unwanted Applications.
     */
    public static final int CL_DB_PUA = 0x10;

    /**
     * This is an alias for a recommended set of scan options. 
     */
    public static final int CL_DB_STDOPT = CL_DB_PHISHING | CL_DB_PHISHING_URLS;

    /**
     * Use it alone if you want to disable support for special files. 
     */
    public static final int CL_SCAN_RAW = 0x0;

    /**
     * This flag enables transparent scanning of various archive formats.
     */
    public static final int CL_SCAN_ARCHIVE = 0x1;

    /**
     * Enable support for mail files.
     */
    public static final int CL_SCAN_MAIL = 0x2;

    /**
     * Enables support for OLE2 containers (used by MS Office and .msi files).
     */
    public static final int CL_SCAN_OLE2 = 0x4;

    /**
     * With this flag the library will mark encrypted archives as viruses 
     * (Encrypted.Zip, Encrypted.RAR). 
     */
    public static final int CL_SCAN_BLOCKENCRYPTED = 0x8;

    /**
     * This flag enables HTML normalisation (including ScrEnc decryption).
     */
    public static final int CL_SCAN_HTML = 0x10;

    /**
     * This flag enables deep scanning of Portable Executable files and allows 
     * libclamav to unpack executables compressed with run-time unpackers. 
     */
    public static final int CL_SCAN_PE = 0x20;

    /**
     * libclamav will try to detect broken executables and mark them as Broken.
     * Executable. 
     */
    public static final int CL_SCAN_BLOCKBROKEN = 0x40;

    /**
     * The mail scanner will download and scan URLs listed in a mail body. 
     * This flag should not be used on loaded servers. Due to potential problems 
     * please do not enable it by default but make it optional. 
     */
    public static final int CL_SCAN_MAILURL = 0x80;

    /**
     * Enable algorithmic detection of viruses.
     */
    public static final int CL_SCAN_ALGORITHMIC = 0x200;

    /**
     * Phishing module: always block SSL mismatches in URLs.
     */
    public static final int CL_SCAN_PHISHING_BLOCKSSL = 0x800;

    /**
     * Phishing module: always block cloaked URLs. 
     */
    public static final int CL_SCAN_PHISHING_BLOCKCLOAK = 0x1000;

    /**
     * Enable support for ELF files.
     */
    public static final int CL_SCAN_ELF = 0x2000;

    /**
     * Enables scanning within PDF files.
     */
    public static final int CL_SCAN_PDF = 0x4000;

    /**
     * This is an alias for a recommended set of scan options. You should use it 
     * to make your software ready for new features in the future versions of libclamav. 
     */
    public static final int CL_SCAN_STDOPT = (CL_SCAN_ARCHIVE | CL_SCAN_MAIL | CL_SCAN_OLE2 | CL_SCAN_HTML | CL_SCAN_PE | CL_SCAN_ALGORITHMIC | CL_SCAN_ELF);

    /**
     * Default max scan size 100MB.
     */
    private static final int MAX_SCAN_SIZE_DEFAULT = 100 * 1048576;

    /**
     * Default max file scan to scan 10MB.
     */
    private static final int MAX_FILE_SIZE_DEFAULT = 10 * 1048576;

    /**
     * Default maximum recursion level for archives 16.
     */
    private static final int MAX_REC_LEVEL_DEFAULT = 16;

    /**
     * Default maximum number of files to be scanned within a single archive 10000.
     */
    private static final int MAX_FILES_NUM_DEFAULT = 10000;

    /**
     * No authentication.
     */
    public static final String NONE_AUTH = "NONE";

    /**
     * GTK authentication.
     */
    public static final String GTK_AUTH = "gksu";

    /**
     * KDE authentication.
     */
    public static final String KDE_AUTH = "kdesu";

    /**
     * Vista authentication.
     */
    public static final String VISTA_AUTH = "UAC";

    /**
     * Mac OSX authentication.
     */
    public static final String MAC_AUTH = "Authenticate";

    /**
     * Flag to indicate if the update process is running.
     */
    private static boolean updateRunning = false;

    /**
     * Singleton instance.
     */
    private static JClamAV ref;

    /**
     * Private singleton instance constructor.
     * @param option anti virus init option.
     * @throws org.jclamav.JClamAVExcpetion
     */
    private JClamAV(int option) throws JClamAVExcpetion {
        initEngine(option);
    }

    /**
     * Get a singleton instance of the JClamAV anti virus engine. If an instance
     * does not already exist then one will be created using the default virus DB
     * loading options.
     * 
     * @return JClamAV anti virus engine.
     * @throws org.jclamav.JClamAVExcpetion
     */
    public static synchronized JClamAV getInstance() throws JClamAVExcpetion {
        if (updateRunning) throw new JClamAVExcpetion("Unable to start the anti virus engine " + "because the virus DB process is active.");
        if (ref == null) {
            ref = new JClamAV(CL_DB_STDOPT);
        }
        return ref;
    }

    public static String updateVirusDB(String auth) throws JClamAVExcpetion {
        if (ref != null) {
            throw new JClamAVExcpetion("Anti virus engine is currently active. " + "Anti virus engine must be shutdown before updating the virus database");
        }
        updateRunning = true;
        CLIProcess process = null;
        if (auth.equals("NONE")) {
            process = new CLIProcess("java");
        } else {
            process = new CLIProcess(auth + " java");
        }
        Thread thread = new Thread(process);
        thread.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
        }
        while (process.isRunning()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
            }
        }
        updateRunning = false;
        if (process.getErrorMessage() != null && process.getErrorMessage().length() > 0) {
            return process.getErrorMessage();
        } else {
            return process.getSuccessMessage();
        }
    }

    /**
     * Path to the anti virus DB files used by ClamAV.
     * @return path to DB files.
     */
    public synchronized String getDataBasePath() throws JClamAVExcpetion {
        if (ref == null) throw new JClamAVExcpetion("Anti virus engine not started. Please obtain a new instance");
        return nativeGetDataBasePath();
    }

    /**
     * Scan a file using the ClamAV anti virus engine.
     * 
     * @param path path too file.
     * @param maxScanSize during the scanning of archives this size will never 
     *                      be exceeded.
     * @param maxFileSize compressed files will only be decompressed and scanned 
     *                      up to this size.
     * @param maxRecLevel maximum recursion level for archives.
     * @param maxFiles  maximum number of files to be scanned within a single 
     *                      archive.
     * @param options configures the scan engine.
     * 
     * @return true if a virus has been found, false if the file is clean.
     */
    public synchronized boolean scanFile(String path, int maxScanSize, int maxFileSize, int maxRecLevel, int maxFiles, int options) throws JClamAVExcpetion {
        if (ref == null) throw new JClamAVExcpetion("Anti virus engine not started. Please obtain a new instance");
        int result = nativeScanFile(path, maxScanSize, maxFileSize, maxRecLevel, maxFiles, options);
        if (result == 0) {
            return false;
        } else if (result == 1) {
            return true;
        } else {
            throw new JClamAVExcpetion("Error while scanning file: " + nativeGetErrorMessage(result));
        }
    }

    /**
     * Scan a file using the ClamAV anti virus engine. Default scan config is
     * applied.
     * 
     * Max scan size = 100MB
     * Max file size = 10MB
     * Max recursion level for archives = 16MB
     * Max number of files within a single archive = 10000.
     * Default scan options are applied.
     * 
     * 
     * @param path path too file.
     * @return true if a virus has been found, false if the file is clean.
     */
    public synchronized boolean scanFile(String path) throws JClamAVExcpetion {
        return scanFile(path, MAX_SCAN_SIZE_DEFAULT, MAX_FILE_SIZE_DEFAULT, MAX_REC_LEVEL_DEFAULT, MAX_FILES_NUM_DEFAULT, CL_SCAN_STDOPT);
    }

    /**
     * Scan a file using the ClamAV anti virus engine.
     * 
     * @param file file to scan.
     * @param maxScanSize during the scanning of archives this size will never 
     *                      be exceeded.
     * @param maxFileSize compressed files will only be decompressed and scanned 
     *                      up to this size.
     * @param maxRecLevel maximum recursion level for archives.
     * @param maxFiles  maximum number of files to be scanned within a single 
     *                      archive.
     * @param options configures the scan engine.
     * 
     * @return true if a virus has been found, false if the file is clean.
     */
    public synchronized boolean scanFile(File file, int maxScanSize, int maxFileSize, int maxRecLevel, int maxFiles, int options) throws JClamAVExcpetion {
        return scanFile(file.getPath(), maxScanSize, maxFileSize, maxRecLevel, maxFiles, options);
    }

    /**
     * Scan a file using the ClamAV anti virus engine. Default scan config is
     * applied.
     * 
     * Max scan size = 100MB
     * Max file size = 10MB
     * Max recursion level for archives = 16MB
     * Max number of files within a single archive = 10000.
     * Default scan options are applied.
     * 
     * @param file file to scan.
     * 
     * @return true if a virus has been found, false if the file is clean.
     */
    public synchronized boolean scanFile(File file) throws JClamAVExcpetion {
        return scanFile(file, MAX_SCAN_SIZE_DEFAULT, MAX_FILE_SIZE_DEFAULT, MAX_REC_LEVEL_DEFAULT, MAX_FILES_NUM_DEFAULT, CL_SCAN_STDOPT);
    }

    /**
     * Scan a collection of files using the ClamAV anti virus engine. Default 
     * scan config is applied.
     * 
     * Max scan size = 100MB
     * Max file size = 10MB
     * Max recursion level for archives = 16MB
     * Max number of files within a single archive = 10000.
     * Default scan options are applied.
     * 
     * @param files collection of files to be scanned.
     * @return true if a virus has been found in any of the supplied files, false 
     * if the files are clean.
     */
    public synchronized boolean scanFile(Collection<File> files) throws JClamAVExcpetion {
        boolean result = false;
        for (File file : files) {
            if (scanFile(file)) {
                result = true;
            }
        }
        return result;
    }

    /**
     * The name of the last virus found by the anti virus system.
     * @return virus name or null if no virus has been found yet.
     */
    public synchronized String getLastVirusName() throws JClamAVExcpetion {
        if (ref == null) throw new JClamAVExcpetion("Anti virus engine not started. Please obtain a new instance");
        return nativeGetLastVirusName();
    }

    /**
     * initialise the anti virus engine.
     * @param option anti virus init option.
     * @throws org.jclamav.JClamAVExcpetion
     */
    private void initEngine(int option) throws JClamAVExcpetion {
        int result = nativeInitEngine(option);
        if (result != 0) {
            throw new JClamAVExcpetion("Native load database returned error: " + nativeGetErrorMessage(result));
        }
    }

    /**
     * Shut down the ClamAV engine.
     * To restart the ClamAM engine you must obtain a new instance of the 
     * JClamAV class.
     */
    public synchronized void shutdown() {
        if (isActive()) {
            nativeShutDownEngine();
            ref = null;
        }
    }

    /**
     * Check if the anti virus engine is active.
     * @return true if active.
     */
    public synchronized boolean isActive() {
        if (ref == null) {
            return false;
        }
        return true;
    }

    private native int nativeInitEngine(int option);

    private native String nativeGetErrorMessage(int code);

    private native void nativeShutDownEngine();

    private native String nativeGetDataBasePath();

    private native int nativeScanFile(String path, int maxScanSize, int maxFileSize, int maxRecLevel, int maxFiles, int options);

    private native String nativeGetLastVirusName();
}
