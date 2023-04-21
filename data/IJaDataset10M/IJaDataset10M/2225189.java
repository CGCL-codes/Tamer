package pogo.gene;

import java.util.*;

public class UpdateRevision {

    private static final String declare = "private static String revNumber =";

    private String filename = null;

    private String revision = null;

    private PogoString code;

    public UpdateRevision(String[] args) throws Exception {
        checkArguments(args);
        System.out.println("filename = " + filename + "  revision = " + revision);
        code = new PogoString(PogoUtil.readFile(filename));
        updateCode();
        PogoUtil.writeFile(filename, code.str);
    }

    private void updateCode() throws PogoException {
        int start = code.str.indexOf(declare);
        if (start < 0) throw new PogoException("Revision declaration not found !");
        int end = code.str.indexOf(";", start);
        start = code.str.indexOf("\"", start);
        if (start < 0 || end < 0) throw new PogoException("Syntax error in revision declaration !");
        start++;
        end = code.str.lastIndexOf("\"", end);
        if (start < 0 || end < 0) throw new PogoException("Syntax error in revision declaration !");
        String oldstr = code.str.substring(start, end);
        String newstr = buildNewRevision();
        System.out.println("Replacing");
        System.out.println(oldstr);
        System.out.println(newstr);
        code.replace(oldstr, newstr);
    }

    private String buildNewRevision() {
        Date date = new Date();
        return "Revision " + revision + "  -  " + date.toString();
    }

    private void checkArguments(String[] args) {
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-?")) displaySyntax(); else if (args[i].equals("-f")) filename = args[++i]; else if (args[i].equals("-r")) revision = args[++i];
            }
            if (filename == null || revision == null) displaySyntax();
        } catch (Exception e) {
            displaySyntax();
        }
    }

    private void displaySyntax() {
        System.out.println("java pogo.gene.UpdateRevision   " + "-f <file name>   -r <revision number>");
        System.exit(0);
    }

    public static void main(String args[]) {
        UpdateRevision client = null;
        try {
            client = new UpdateRevision(args);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
