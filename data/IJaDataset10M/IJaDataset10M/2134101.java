package org.web3d.x3d.tools;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.regex.*;

/**
 *
 * @author  brutzman
 */
public class X3dDtdChecker {

    static String UsageMessage = "usage: java X3dDtdChecker sceneName.x3d [-verbose | -setFinalDTD | -setTransitionalDTD]";

    static String x3dFileName;

    static String scene, header = null;

    static String revisedScene = null;

    static final String TRANSITIONAL_30_DOCTYPE = "<!DOCTYPE X3D PUBLIC \"http://www.web3d.org/specifications/x3d-3.0.dtd\" \"file:///www.web3d.org/TaskGroups/x3d/translation/x3d-3.0.dtd\"";

    static final String TRANSITIONAL_31_DOCTYPE = "<!DOCTYPE X3D PUBLIC \"http://www.web3d.org/specifications/x3d-3.1.dtd\" \"file:///www.web3d.org/TaskGroups/x3d/translation/x3d-3.1.dtd\"";

    static final String FINAL_30_DOCTYPE = "<!DOCTYPE X3D PUBLIC \"ISO//Web3D//DTD X3D 3.0//EN\" \"http://www.web3d.org/specifications/x3d-3.0.dtd\"";

    static final String FINAL_31_DOCTYPE = "<!DOCTYPE X3D PUBLIC \"ISO//Web3D//DTD X3D 3.1//EN\" \"http://www.web3d.org/specifications/x3d-3.1.dtd\"";

    static final String FINAL_32_DOCTYPE = "<!DOCTYPE X3D PUBLIC \"ISO//Web3D//DTD X3D 3.2//EN\" \"http://www.web3d.org/specifications/x3d-3.2.dtd\"";

    static final String FINAL_33_DOCTYPE = "<!DOCTYPE X3D PUBLIC \"ISO//Web3D//DTD X3D 3.3//EN\" \"http://www.web3d.org/specifications/x3d-3.3.dtd\"";

    static final String WarningComment = "<!--Warning:  transitional DOCTYPE in source .x3d file-->\n";

    static final String WarningRegex = "<!--Warning:  transitional DOCTYPE in source \\.x3d file-->(\\s)*";

    static boolean setTransitionalDTD = false;

    static boolean setFinalDTD = false;

    static boolean foundNo_DTD = false;

    static boolean foundTransitional_30_DTD = false;

    static boolean foundTransitional_31_DTD = false;

    static boolean foundFinal_30_DTD = false;

    static boolean foundFinal_31_DTD = false;

    static boolean foundFinal_32_DTD = false;

    static boolean foundFinal_33_DTD = false;

    static boolean readOnlyFile = false;

    static boolean saveFile = true;

    static boolean verbose = false;

    static FileInputStream fis;

    static RandomAccessFile raf;

    static FileChannel fc;

    static ByteBuffer bb;

    /**
     * extracts the content of a file
     * @param fileName the name of the file to extract
     * @return String representing contents of the file
     */
    public static String getFileContent(String fileName) {
        try {
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException fnf) {
            System.out.println("[Error] [X3dDtdChecker] scene \"" + fileName + "\" not found.");
            System.out.println(UsageMessage);
            saveFile = false;
            exit(0);
        }
        try {
            raf = new RandomAccessFile(fileName, "rwd");
            fc = raf.getChannel();
            bb = ByteBuffer.allocate((int) fc.size());
            fc.read(bb);
            bb.flip();
        } catch (IOException ioe) {
            readOnlyFile = true;
        }
        if (raf == null) try {
            raf = new RandomAccessFile(fileName, "r");
            fc = raf.getChannel();
            bb = ByteBuffer.allocate((int) fc.size());
            fc.read(bb);
            bb.flip();
            System.out.println("[Warning] [X3dDtdChecker] ' + fileName + ' file is read-only.");
        } catch (IOException ioe) {
            System.out.println("[Error] [X3dDtdChecker] unable to read scene \"" + fileName + "\".");
            ioe.printStackTrace();
            System.out.println();
            saveFile = false;
            exit(0);
        }
        String returnString = new String(bb.array());
        bb = null;
        return returnString;
    }

    /**
     * resets the content of a file
     * @param revisedScene content being reset
     */
    public static void setFileContent(String revisedScene) {
        try {
            bb = ByteBuffer.wrap(revisedScene.getBytes());
            fc.truncate(revisedScene.length());
            fc.position(0);
            fc.write(bb);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if ((args != null) && (args.length >= 1) && (args.length <= 2)) {
            x3dFileName = args[0];
        } else {
            System.out.println(UsageMessage);
            saveFile = false;
            exit(-1);
        }
        scene = getFileContent(x3dFileName);
        int indexX3D = scene.indexOf("<X3D", 0);
        if (indexX3D > 0) header = scene.substring(0, indexX3D).trim(); else header = scene;
        if (args != null && args.length > 1) {
            for (int i = 1; i <= args.length - 1; i++) {
                if ((args[i].compareTo("-v") == 0) || (args[i].compareTo("-verbose") == 0)) {
                    verbose = true;
                } else if ((args[i].compareTo("-f") == 0) || (args[i].compareTo("-setFinalDTD") == 0)) {
                    setFinalDTD = true;
                } else if ((args[i].compareTo("-t") == 0) || (args[i].compareTo("-setTransitionalDTD") == 0)) {
                    setTransitionalDTD = true;
                } else {
                    System.out.println("[Error] [X3dDtdChecker] unrecognized command-line option \"" + args[i] + "\"");
                    System.out.println(UsageMessage);
                    saveFile = false;
                    exit(-1);
                }
            }
        }
        if (setFinalDTD && setTransitionalDTD) {
            System.out.println("[Error] [X3dDtdChecker] both -setFinalDTD and -setTransitionalDTD specified,");
            System.out.println("        only one operation allowed.");
            System.out.println(UsageMessage);
            saveFile = false;
            exit(-1);
        }
        String regexXmlHeader = "<\\?xml version=(\"|')1.(0|1)(\"|') encoding=(\"|')UTF-8(\"|')\\?>";
        Pattern patternXmlHeader = Pattern.compile(regexXmlHeader);
        Matcher matcherXmlHeader = patternXmlHeader.matcher(scene);
        if (matcherXmlHeader.find()) {
            System.out.print("[X3dDtdChecker] valid XML declaration found");
        } else {
            System.out.println("[Error] [X3dDtdChecker] no valid XML declaration found in scene!");
            System.out.println(header);
            foundNo_DTD = true;
            System.out.println(UsageMessage);
            if (!setFinalDTD && !setTransitionalDTD) {
                saveFile = false;
                exit(-1);
            }
        }
        String regexFinal30Doctype = "[^<][^!][^-][^-](\\s)?<!DOCTYPE X3D PUBLIC(\\s)+\"ISO//Web3D//DTD X3D 3.0//EN\"(\\s)+\"http://www.web3d.org/specifications/x3d-3.0.dtd\"(\\s)*(>|\\[)";
        Pattern patternFinal30Doctype = Pattern.compile(regexFinal30Doctype);
        Matcher matcherFinal30Doctype = patternFinal30Doctype.matcher(scene);
        String regexFinal31Doctype = "[^<][^!][^-][^-](\\s)?<!DOCTYPE X3D PUBLIC(\\s)+\"ISO//Web3D//DTD X3D 3.1//EN\"(\\s)+\"http://www.web3d.org/specifications/x3d-3.1.dtd\"(\\s)*(>|\\[)";
        Pattern patternFinal31Doctype = Pattern.compile(regexFinal31Doctype);
        Matcher matcherFinal31Doctype = patternFinal31Doctype.matcher(scene);
        String regexFinal32Doctype = "[^<][^!][^-][^-](\\s)?<!DOCTYPE X3D PUBLIC(\\s)+\"ISO//Web3D//DTD X3D 3.2//EN\"(\\s)+\"http://www.web3d.org/specifications/x3d-3.2.dtd\"(\\s)*(>|\\[)";
        Pattern patternFinal32Doctype = Pattern.compile(regexFinal32Doctype);
        Matcher matcherFinal32Doctype = patternFinal32Doctype.matcher(scene);
        String regexFinal33Doctype = "[^<][^!][^-][^-](\\s)?<!DOCTYPE X3D PUBLIC(\\s)+\"ISO//Web3D//DTD X3D 3.3//EN\"(\\s)+\"http://www.web3d.org/specifications/x3d-3.3.dtd\"(\\s)*(>|\\[)";
        Pattern patternFinal33Doctype = Pattern.compile(regexFinal33Doctype);
        Matcher matcherFinal33Doctype = patternFinal33Doctype.matcher(scene);
        String regexTransitional30Doctype = "[^<][^!][^-][^-](\\s)?<!DOCTYPE X3D PUBLIC(\\s)+\"http://www.web3d.org/specifications/x3d-3.0.dtd\"(\\s)+\"file:///www.web3d.org/TaskGroups/x3d/translation/x3d-3.0.dtd\"(\\s)*(>|\\[)";
        Pattern patternTransitional30Doctype = Pattern.compile(regexTransitional30Doctype);
        Matcher matcherTransitional30Doctype = patternTransitional30Doctype.matcher(scene);
        String regexTransitional31Doctype = "[^<][^!][^-][^-](\\s)?<!DOCTYPE X3D PUBLIC(\\s)+\"http://www.web3d.org/specifications/x3d-3.1.dtd\"(\\s)+\"file:///www.web3d.org/TaskGroups/x3d/translation/x3d-3.1.dtd\"(\\s)*(>|\\[)";
        Pattern patternTransitional31Doctype = Pattern.compile(regexTransitional31Doctype);
        Matcher matcherTransitional31Doctype = patternTransitional31Doctype.matcher(scene);
        String regexAnyDoctype = "[^<][^!][^-][^-](\\s)?<!DOCTYPE X3D PUBLIC";
        Pattern patternAnyDoctype = Pattern.compile(regexAnyDoctype);
        Matcher matcherAnyDoctype = patternAnyDoctype.matcher(scene);
        if (matcherFinal33Doctype.find()) {
            foundFinal_33_DTD = true;
            System.out.println(", final X3D 3.3 DOCTYPE found.");
            if (verbose) System.out.println(header);
        } else if (matcherFinal32Doctype.find()) {
            foundFinal_32_DTD = true;
            System.out.println(", final X3D 3.2 DOCTYPE found.");
            if (verbose) System.out.println(header);
        } else if (matcherFinal31Doctype.find()) {
            foundFinal_31_DTD = true;
            System.out.println(", final X3D 3.1 DOCTYPE found.");
            if (verbose) System.out.println(header);
        } else if (matcherFinal30Doctype.find()) {
            foundFinal_30_DTD = true;
            System.out.println(", final X3D 3.0 DOCTYPE found.");
            if (verbose) System.out.println(header);
        } else if (matcherTransitional30Doctype.find()) {
            foundTransitional_30_DTD = true;
            System.out.println(", transitional X3D 3.0 DOCTYPE found.");
            if (verbose) System.out.println(header);
        } else if (matcherTransitional31Doctype.find()) {
            foundTransitional_31_DTD = true;
            System.out.println(", transitional X3D 3.1 DOCTYPE found.");
            if (verbose) System.out.println(header);
        } else if (matcherAnyDoctype.find()) {
            System.out.println("\n[Error] Nonstandard X3D DOCTYPE found!");
            System.out.println(header);
            saveFile = false;
            exit(-1);
        } else {
            System.out.println("\n[Error] No X3D DOCTYPE found!");
            System.out.println(header);
            foundNo_DTD = true;
            if (!setFinalDTD && !setTransitionalDTD) {
                saveFile = false;
                exit(-1);
            }
        }
        matcherAnyDoctype.reset();
        int matchCount = 0;
        while (matcherAnyDoctype.find()) {
            matchCount++;
        }
        if (matchCount > 1) {
            System.out.print("[Warning] Multiple X3D DOCTYPEs found (" + matchCount + " total)");
            if ((setFinalDTD || setTransitionalDTD) && (readOnlyFile == false)) {
                System.out.println(", no DTD conversion attempted.");
            } else System.out.println(".");
            System.out.println(header);
            saveFile = false;
            exit(-1);
        }
        if (readOnlyFile) {
            saveFile = false;
            exit(0);
        }
        if (setFinalDTD) System.out.print("[X3dDtdChecker] set final DTD:  "); else if (setTransitionalDTD) System.out.print("[X3dDtdChecker] set transitional DTD:  ");
        if (setFinalDTD && foundTransitional_30_DTD) {
            matcherTransitional30Doctype.reset();
            revisedScene = matcherTransitional30Doctype.replaceFirst(FINAL_30_DOCTYPE);
            revisedScene = revisedScene.replaceAll(WarningRegex, "");
            System.out.println("scene reset to final X3D 3.0 DTD.");
            System.out.println(FINAL_30_DOCTYPE + ">");
            saveFile = true;
        } else if (setFinalDTD && foundTransitional_31_DTD) {
            matcherTransitional31Doctype.reset();
            revisedScene = matcherTransitional31Doctype.replaceFirst(FINAL_31_DOCTYPE);
            revisedScene = revisedScene.replaceAll(WarningRegex, "");
            System.out.println("scene reset to final X3D 3.1 DTD.");
            System.out.println(FINAL_31_DOCTYPE + ">");
            saveFile = true;
        } else if (setTransitionalDTD && foundFinal_30_DTD) {
            matcherFinal30Doctype.reset();
            revisedScene = matcherFinal30Doctype.replaceFirst(WarningComment + TRANSITIONAL_30_DOCTYPE);
            System.out.println("scene reset to transitional X3D DTD.");
            System.out.println(TRANSITIONAL_30_DOCTYPE + ">");
            saveFile = true;
        } else if (setTransitionalDTD && foundFinal_31_DTD) {
            matcherFinal31Doctype.reset();
            revisedScene = matcherFinal31Doctype.replaceFirst(WarningComment + TRANSITIONAL_31_DOCTYPE);
            System.out.println("scene reset to transitional X3D DTD.");
            System.out.println(TRANSITIONAL_31_DOCTYPE + ">");
            saveFile = true;
        } else if (foundNo_DTD) {
            System.out.println("no action taken, functionality not yet implemented...");
            saveFile = false;
        } else if (setFinalDTD || setTransitionalDTD) {
            System.out.println("no action necessary.");
            saveFile = false;
        }
        exit(0);
    }

    public static void exit(int exitCode) {
        if (saveFile == false) System.exit(exitCode);
        if (setFinalDTD || setTransitionalDTD) setFileContent(revisedScene);
        try {
            if (fc != null) {
                fc.close();
                fc = null;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(exitCode);
        }
        System.out.println();
        System.exit(exitCode);
    }
}
