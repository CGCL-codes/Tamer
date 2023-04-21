package xj3d.converter;

import java.io.*;
import org.ietf.uri.*;
import java.net.URL;
import java.net.MalformedURLException;
import org.web3d.util.ErrorReporter;
import org.web3d.vrml.export.*;
import org.web3d.vrml.sav.*;
import org.web3d.vrml.lang.*;
import org.web3d.net.content.VRMLFileNameMap;
import org.web3d.net.protocol.JavascriptResourceFactory;
import org.web3d.vrml.parser.VRMLParserFactory;
import org.web3d.vrml.parser.FactoryConfigurationError;

/**
 * A converter between X3D encodings.
 *
 * @author Alan Hudson
 * @version $Revision: 1.15 $
 */
public class Xj3DConv {

    private static final String USAGE = "Usage:  X3DConv [options] <input> <output>\n" + "options:  -method [fastest, smallest, lossy, strings]\n" + "          -quantizeParam n\n" + "          -upgrade\n";

    private static float PARAM_FLOAT_LOSSY = 0.001f;

    /** The parser factory that we are going to use. */
    private VRMLParserFactory parserFactory;

    /** The VRMLReader */
    private VRMLReader reader;

    private static Exporter writer;

    /** The compression method to use for binary */
    private int compressionMethod;

    /** The float lossy param */
    private float quantizeParam;

    /** Should we upgrade old protos to X3D native */
    private boolean upgradeContent;

    /** The error reporter to use */
    private ErrorReporter console;

    /**
     * Create an instance of the demo class.
     */
    public Xj3DConv() {
        setupProperties();
        console = new PlainTextErrorReporter();
        VRMLParserFactory parser_fac = null;
        try {
            parser_fac = VRMLParserFactory.newVRMLParserFactory();
        } catch (FactoryConfigurationError fce) {
            throw new RuntimeException("Failed to load factory");
        }
        reader = parser_fac.newVRMLReader();
        reader.setErrorReporter(console);
    }

    /**
     * Set the compression method to use for binary compression.
     *
     * @param method The compression method, defined in X3DBinarySerializer
     */
    public void setCompressionMethod(int method) {
        compressionMethod = method;
    }

    /**
     * Set the maximum desired quantization loss when using lossy compression.
     *
     * @param max The maximum loss
     */
    public void setQuantizationParam(float max) {
        quantizeParam = max;
    }

    /**
     * Should old VRML PROTO's like h-anim, geovrml be upgraded to X3D native
     * nodes.
     *
     * @param upgrade TRUE to upgrade content.
     */
    public void setUpgradeContent(boolean upgrade) {
        upgradeContent = upgrade;
    }

    /**
     * Go to the named URL location. No checking is done other than to make
     * sure it is a valid URL.
     *
     * @param url The URL to open
     */
    public int convert(URL url, String out) {
        return load(url, null, out);
    }

    /**
     * Load the named file. The file is checked to make sure that it exists
     * before calling this method.
     *
     * @param file The file to load
     */
    public int convert(File file, String out) {
        return load(null, file, out);
    }

    /**
     * Do all the parsing work. Convenience method for all to call internally
     *
     * @param is The inputsource for this reader
     * @return true if the world loaded correctly
     */
    private int load(URL url, File file, String out) {
        InputSource is = null;
        if (url != null) is = new InputSource(url); else if (file != null) is = new InputSource(file);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(out));
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return -1;
        }
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        int idxDot = out.lastIndexOf(".");
        if (idxDot < 0) {
            System.out.println("Unknown destination file type");
            return -1;
        }
        String encoding = out.substring(idxDot + 1);
        System.out.println("File: " + file + " Encoding: " + encoding);
        if (encoding.equals("x3db")) {
            writer = new X3DBinaryRetainedDirectExporter(bos, 3, 1, console, compressionMethod, quantizeParam);
            ((X3DBinaryRetainedDirectExporter) writer).setConvertOldContent(upgradeContent);
        } else if (encoding.equals("x3dv")) {
            writer = new X3DClassicRetainedExporter(bos, 3, 1, console);
            ((X3DClassicRetainedExporter) writer).setConvertOldContent(upgradeContent);
        } else if (encoding.equals("x3d")) {
            writer = new X3DXMLRetainedExporter(bos, 3, 1, console);
            ((X3DXMLRetainedExporter) writer).setConvertOldContent(upgradeContent);
        } else {
            System.out.println("Unknown destination encoding");
            return -1;
        }
        reader.setContentHandler(writer);
        reader.setRouteHandler(writer);
        reader.setScriptHandler(writer);
        reader.setProtoHandler(writer);
        try {
            reader.parse(is);
        } catch (Exception e) {
            StringBuffer buf = new StringBuffer("Error: ");
            if (e instanceof FieldException) {
                FieldException fe = (FieldException) e;
                String name = fe.getFieldName();
                if (name != null) {
                    buf.append("Field name: ");
                    buf.append(name);
                }
            }
            if (e instanceof VRMLParseException) {
                buf.append(" Line: ");
                buf.append(((VRMLParseException) e).getLineNumber());
                buf.append(" Column: ");
                buf.append(((VRMLParseException) e).getColumnNumber());
                buf.append('\n');
            } else if (e instanceof InvalidFieldFormatException) {
                buf.append(" Line: ");
                buf.append(((InvalidFieldFormatException) e).getLineNumber());
                buf.append(" Column: ");
                buf.append(((InvalidFieldFormatException) e).getColumnNumber());
                buf.append('\n');
            }
            if (e != null) {
                String txt = e.getMessage();
                buf.append(txt);
            }
            System.out.println(buf);
            e.printStackTrace();
            System.out.println("Exiting with error code: -1");
            return -1;
        }
        return 0;
    }

    /**
     * Set up the system properties needed to run the browser. This involves
     * registering all the properties needed for content and protocol
     * handlers used by the URI system. Only needs to be run once at startup.
     *
     * @param core The core representation of the browser
     * @param loader Loader manager for doing async calls
     */
    private void setupProperties() {
        System.setProperty("uri.content.handler.pkgs", "vlc.net.content");
        System.setProperty("uri.protocol.handler.pkgs", "vlc.net.protocol");
        URIResourceStreamFactory res_fac = URI.getURIResourceStreamFactory();
        if (!(res_fac instanceof JavascriptResourceFactory)) {
            res_fac = new JavascriptResourceFactory(res_fac);
            URI.setURIResourceStreamFactory(res_fac);
        }
        FileNameMap fn_map = URI.getFileNameMap();
        if (!(fn_map instanceof VRMLFileNameMap)) {
            fn_map = new VRMLFileNameMap(fn_map);
            URI.setFileNameMap(fn_map);
        }
    }

    /**
     * Create an instance of this class and run it. The single argument, if
     * supplied is the name of the file to load initially. If not supplied it
     * will start with a blank document.
     *
     * @param argv The list of arguments for this application.
     */
    public static void main(String[] args) {
        Xj3DConv conv = new Xj3DConv();
        int lastUsed = -1;
        int method = X3DBinarySerializer.METHOD_SMALLEST_NONLOSSY;
        float quantizeParam = PARAM_FLOAT_LOSSY;
        int pnum = 0;
        boolean upgradeContent = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                if (args[i].equals("-method")) {
                    pnum++;
                    i++;
                    if (args[i].equals("fastest")) {
                        System.out.println("Fasting parsing method");
                        method = X3DBinarySerializer.METHOD_FASTEST_PARSING;
                    } else if (args[i].equals("smallest")) {
                        System.out.println("Smallest parsing method");
                        method = X3DBinarySerializer.METHOD_SMALLEST_NONLOSSY;
                    } else if (args[i].equals("lossy")) {
                        System.out.println("Lossy parsing method");
                        method = X3DBinarySerializer.METHOD_SMALLEST_LOSSY;
                    } else if (args[i].equals("strings")) {
                        System.out.println("Strings method");
                        method = X3DBinarySerializer.METHOD_STRINGS;
                    } else {
                        System.out.println("Unknown compression method");
                    }
                } else if (args[i].equals("-quantizeParam")) {
                    pnum++;
                    i++;
                    String st = args[i];
                    quantizeParam = Float.parseFloat(st);
                } else if (args[i].equals("-upgrade")) {
                    System.out.println("Upgrading VRML PROTO content to X3D");
                    upgradeContent = true;
                } else {
                    System.out.println("Unknown option: " + args[i]);
                }
                pnum++;
            } else break;
        }
        if (args.length - pnum < 1) {
            System.out.println(Xj3DConv.USAGE);
            exit(-1);
        }
        conv.setCompressionMethod(method);
        conv.setQuantizationParam(quantizeParam);
        conv.setUpgradeContent(upgradeContent);
        String filename = null;
        String outfile = null;
        File fil2 = null;
        if (pnum == (args.length - 2)) {
            filename = args[pnum];
            outfile = args[pnum + 1];
        } else {
            filename = args[pnum];
            int pos = filename.lastIndexOf(".");
            if (pos < 0) {
                System.out.println(Xj3DConv.USAGE);
                exit(-1);
            }
            String currentEncoding = filename.substring(pos);
            if (!currentEncoding.equals(".x3dv")) {
                System.out.println("Converting to VRML Classic Encoding: " + outfile);
                outfile = filename.substring(0, pos) + ".x3dv";
            } else {
                outfile = filename.substring(0, pos) + ".x3d";
                System.out.println("Converting to XML Encoding: " + outfile);
            }
        }
        if (filename.equals(outfile)) {
            System.out.println("Cannot convert in place.  Operation aborted.");
            exit(-1);
        }
        File fil = new File(filename);
        int returnVal = 0;
        if (fil.exists()) {
            returnVal = conv.convert(fil, outfile);
        } else {
            try {
                URL url = new URL(filename);
                returnVal = conv.convert(url, outfile);
            } catch (MalformedURLException mfe) {
                System.out.println("Malformed URL: " + filename);
                exit(-1);
            }
        }
        exit(returnVal);
    }

    private static void exit(int status) {
        System.exit(status);
    }
}
