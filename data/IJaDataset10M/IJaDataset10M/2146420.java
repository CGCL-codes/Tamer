package org.tango.pogo.pogo_gui.tools;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoDs.Except;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.StringTokenizer;
import java.util.ArrayList;

public class ParserTool {

    public static String readFile(String filename) throws DevFailed {
        String str = "";
        try {
            FileInputStream fid = new FileInputStream(filename);
            int nb = fid.available();
            byte[] inStr = new byte[nb];
            nb = fid.read(inStr);
            fid.close();
            if (nb > 0) str = takeOffWindowsChar(inStr);
        } catch (Exception e) {
            Except.throw_exception("READ_FAILED", e.toString(), "ParserTool.readFile()");
        }
        return str;
    }

    private static String takeOffWindowsChar(byte[] b_in) {
        int nb = 0;
        for (byte b : b_in) if (b != 13) nb++;
        byte[] b_out = new byte[nb];
        for (int i = 0, j = 0; i < b_in.length; i++) if (b_in[i] != 13) b_out[j++] = b_in[i];
        return new String(b_out);
    }

    private static String checkOsFormat(String code) {
        if (!Utils.osIsUnix()) return setWindowsFileFormat(code); else return code;
    }

    public static String setWindowsFileFormat(String code) {
        byte[] b = { 0xd, 0xa };
        String lsp = new String(b);
        code = code.replaceAll("\n", lsp);
        return code;
    }

    public static void writeFile(String filename, String code) throws DevFailed {
        try {
            code = checkOsFormat(code);
            FileOutputStream fidout = new FileOutputStream(filename);
            fidout.write(code.getBytes());
            fidout.close();
        } catch (Exception e) {
            Except.throw_exception("WRITE_FAILED", e.toString(), "ParserTool.readFile()");
        }
    }

    public static void modifyProtectedAreaID(String path, String fileName, String oldID, String newID) throws DevFailed {
        oldID = "PROTECTED REGION ID(" + oldID + ") ENABLED START";
        newID = "PROTECTED REGION ID(" + newID + ") ENABLED START";
        String code;
        try {
            code = readFile(path + '/' + fileName);
        } catch (DevFailed e) {
            return;
        }
        int start = code.indexOf(oldID);
        if (start < 0) return;
        int end = start + oldID.length();
        String newCode = code.substring(0, start) + newID + code.substring(end);
        writeFile(path + '/' + fileName, newCode);
    }

    public static void convertHTML(String filename) throws DevFailed {
        try {
            String code = readFile(filename);
            StringBuffer sb = new StringBuffer();
            int start = 0;
            int end;
            while ((end = code.indexOf("\"", start + 1)) > 0) {
                sb.append(code.substring(start, end)).append("\\");
                start = end;
            }
            sb.append(code.substring(start));
            code = sb.toString();
            sb = new StringBuffer("\"");
            start = 0;
            while ((end = code.indexOf("\n", start)) > 0) {
                sb.append(code.substring(start, end)).append("\\n\" + \n\"");
                start = end + 1;
            }
            sb.append(code.substring(start)).append("\\n\"");
            code = sb.toString();
            System.out.println(code);
        } catch (Exception e) {
            Except.throw_exception("READ_FAILED", e.toString(), "ParserTool.readFile()");
        }
    }

    public static void displaySyntax() {
        System.out.println("ParserTool <option> <filename>");
        System.out.println("    option: -html  convert html file to java String");
        System.exit(0);
    }

    public static void removeXmiKey(String key, String fileName) throws DevFailed {
        boolean modified = false;
        key = " " + key + "=\"";
        String code = readFile(fileName);
        StringTokenizer stk = new StringTokenizer(code, "\n");
        ArrayList<String> v = new ArrayList<String>();
        while (stk.hasMoreTokens()) {
            String line = stk.nextToken();
            int start;
            if ((start = line.indexOf(key)) > 0) {
                int end = line.indexOf("\"", start + key.length());
                if (end < 0) {
                    System.err.println("XMI syntax error !!!");
                    return;
                }
                line = line.substring(0, start) + line.substring(end + 1);
                modified = true;
            }
            v.add(line);
        }
        if (modified) {
            StringBuilder sb = new StringBuilder();
            for (String s : v) sb.append(s).append("\n");
            code = sb.toString();
            code = code.substring(0, code.length() - 1);
            writeFile(fileName, code);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public static void renameXmiKey(String srcKey, String newKey, String fileName) throws DevFailed {
        boolean modified = false;
        srcKey = " " + srcKey + "=";
        String code = readFile(fileName);
        StringTokenizer stk = new StringTokenizer(code, "\n");
        ArrayList<String> v = new ArrayList<String>();
        while (stk.hasMoreTokens()) {
            String line = stk.nextToken();
            int start;
            if ((start = line.indexOf(srcKey)) > 0) {
                int end = start + srcKey.length();
                if (end < 0) {
                    System.err.println("XMI syntax error !!!");
                    return;
                }
                start++;
                end--;
                line = line.substring(0, start) + newKey + line.substring(end);
                modified = true;
            }
            v.add(line);
        }
        if (modified) {
            StringBuilder sb = new StringBuilder();
            for (String s : v) sb.append(s).append("\n");
            code = sb.toString();
            code = code.substring(0, code.length() - 1);
            writeFile(fileName, code);
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) displaySyntax();
        try {
            if (args[0].equals("-html")) convertHTML(args[1]); else if (args[0].equals("-xmi-clean")) {
            }
        } catch (DevFailed e) {
            Except.print_exception_stack(e);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
