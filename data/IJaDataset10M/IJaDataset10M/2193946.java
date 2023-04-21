package org.merlotxml.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.tolven.logging.TolvenLogger;

/**
 * This is a util class for creating java Manifest files for packing in jars
 *
 * @author Kelly A.Campbell
 *
 */
public class ManifestMaker {

    public static String MANIFEST_DEF_SPEC_TITLE = "";

    public static String MANIFEST_DEF_SPEC_VERS = "";

    public static String MANIFEST_DEF_SPEC_VEND = "ChannelPoint, Inc.";

    public static String MANIFEST_DEF_IMPL_TITLE = "";

    public static String MANIFEST_DEF_IMPL_VERS = "";

    public static String MANIFEST_DEF_IMPL_VEND = "ChannelPoint, Inc.";

    public static final String MANIFEST_VERSION = "Manifest-Version: ";

    public static final String REQUIRED_VERSION = "Required-Version: ";

    public static final String NAME = "Name: ";

    public static final String SEALED = "Sealed: ";

    public static final String SPECIFICATION_TITLE = "Specification-Title: ";

    public static final String SPECIFICATION_VERSION = "Specification-Version: ";

    public static final String SPECIFICATION_VENDOR = "Specification-Vendor: ";

    public static final String IMPL_TITLE = "Implementation-Title: ";

    public static final String IMPL_VERSION = "Implementation-Version: ";

    public static final String IMPL_VENDOR = "Implementation-Vendor: ";

    public static final String MAIN_CLASS = "Main-Class: ";

    public static final String CLASS_PATH = "Class-Path: ";

    public static void main(String[] args) {
        ManifestMaker maker = new ManifestMaker(args);
        maker.run();
    }

    public static void printUsage(PrintStream out) {
        out.println("ManifestMaker usage:");
        out.println("  ManifestMaker [options] <manifestfile>");
        out.println("  Options:");
        out.println("           -st <spectitle>   Specification title");
        out.println("           -sv <specversion> Specification version");
        out.println("           -sc <specvendor>  Specification vendor (company)");
        out.println("           -it <impltitle>   Implementation title");
        out.println("           -iv <implversion> Implementation version");
        out.println("           -ic <implvendor>  Implementation vendor (company)");
        out.println("           -mc <mainclass>   Main class");
        out.println("           -cp <classpath>   Classpath additions");
    }

    protected String _manifestVersion = "1.0";

    protected String _spectitle = MANIFEST_DEF_SPEC_TITLE;

    protected String _specvers = MANIFEST_DEF_SPEC_VERS;

    protected String _specvend = MANIFEST_DEF_SPEC_VEND;

    protected String _impltitle = MANIFEST_DEF_IMPL_TITLE;

    protected String _implvers = MANIFEST_DEF_IMPL_VERS;

    protected String _implvend = MANIFEST_DEF_IMPL_VEND;

    protected String _mainclass = null;

    protected String _classpath = null;

    protected String _manifestFilename = null;

    public ManifestMaker(String[] args) {
        try {
            String arg;
            for (int i = 0; i < args.length; i++) {
                arg = args[i];
                if (arg.startsWith("-")) {
                    if (arg.equals("-st")) {
                        setSpecTitle(getNextArg(args, i++));
                    } else if (arg.equals("-sv")) {
                        setSpecVersion(getNextArg(args, i++));
                    } else if (arg.equals("-sc")) {
                        setSpecVendor(getNextArg(args, i++));
                    } else if (arg.equals("-it")) {
                        setImplTitle(getNextArg(args, i++));
                    } else if (arg.equals("-iv")) {
                        setImplVersion(getNextArg(args, i++));
                    } else if (arg.equals("-ic")) {
                        setImplVendor(getNextArg(args, i++));
                    } else if (arg.equals("-mc")) {
                        setMainClass(getNextArg(args, i++));
                    } else if (arg.equals("-cp")) {
                        setClassPath(getNextArg(args, i++));
                    } else {
                        throw new Exception("Unknown option: " + arg);
                    }
                } else {
                    if (i + 1 < args.length) {
                        throw new Exception("Unknown argument: " + arg);
                    } else {
                        _manifestFilename = arg;
                    }
                }
            }
        } catch (Exception ex) {
            TolvenLogger.error(ex.getMessage(), ManifestMaker.class);
            printUsage(System.err);
            System.exit(-1);
        }
    }

    public static String getNextArg(String[] args, int i) throws Exception {
        if (i + 1 < args.length) {
            String arg = args[i];
            String narg = args[i + 1];
            if (narg.startsWith("-")) {
                throw new Exception(arg + " requires an argument");
            }
            return args[i + 1];
        }
        return null;
    }

    public void setSpecTitle(String s) {
        _spectitle = s;
    }

    public void setSpecVersion(String s) {
        _specvers = s;
    }

    public void setSpecVendor(String s) {
        _specvend = s;
    }

    public void setImplTitle(String s) {
        _impltitle = s;
    }

    public void setImplVersion(String s) {
        _implvers = s;
    }

    public void setImplVendor(String s) {
        _implvend = s;
    }

    public void setMainClass(String s) {
        _mainclass = s;
    }

    public void setClassPath(String s) {
        _classpath = s;
    }

    public void run() {
        try {
            PrintStream out;
            if (_manifestFilename != null) {
                File f = new File(_manifestFilename);
                if (f.exists()) {
                    throw new Exception("Will not overwrite existing file: " + _manifestFilename);
                }
                f.createNewFile();
                out = new PrintStream(new FileOutputStream(f));
            } else {
                out = System.out;
            }
            if (_implvers == null || _implvers.trim().equals("")) {
                _implvers = getBuildID();
            }
            print(out, MANIFEST_VERSION, _manifestVersion);
            print(out, SPECIFICATION_TITLE, _spectitle);
            print(out, SPECIFICATION_VERSION, _specvers);
            print(out, SPECIFICATION_VENDOR, _specvend);
            print(out, IMPL_TITLE, _impltitle);
            print(out, IMPL_VERSION, _implvers);
            print(out, IMPL_VENDOR, _implvend);
            print(out, MAIN_CLASS, _mainclass);
            print(out, CLASS_PATH, _classpath);
        } catch (Exception ex) {
            TolvenLogger.error(ex.getMessage(), ManifestMaker.class);
            System.exit(-1);
        }
    }

    protected void print(PrintStream out, String header, String value) {
        if (value != null && !value.trim().equals("")) {
            out.println(header + value);
        }
    }

    public static String getBuildID() {
        Date d = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd-HHmmss-z");
        String date = f.format(d);
        String hostname, username, os, version, arch;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (Exception ex) {
            hostname = "unknown";
        }
        username = System.getProperty("user.name");
        os = System.getProperty("os.name");
        version = System.getProperty("os.version");
        arch = System.getProperty("os.arch");
        String BUILDID = date + " (" + username + "@" + hostname + " [" + os + " " + version + " " + arch + "] )";
        return BUILDID;
    }
}
