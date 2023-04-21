package fedora.client.utility.export;

import java.io.*;
import java.util.*;
import fedora.client.FedoraClient;
import fedora.client.utility.export.AutoExporter;
import fedora.client.utility.AutoFinder;
import fedora.server.access.FedoraAPIA;
import fedora.server.management.FedoraAPIM;
import fedora.server.types.gen.Condition;
import fedora.server.types.gen.ComparisonOperator;
import fedora.server.types.gen.FieldSearchQuery;
import fedora.server.types.gen.FieldSearchResult;
import fedora.server.types.gen.ObjectFields;
import fedora.server.types.gen.RepositoryInfo;

/**
 * <p><b>Title:</b> Export.java</p>
 * <p><b>Description: A utility class to initiate an export of one or more objects.
 * This class provides static utility methods, and it is also called by
 * command line utilities.
 * 
 * This class calls AutoExporter.class which is reponsible for making
 * the API-M SOAP calls for the export.
 */
public class Export {

    public static String getDuration(long millis) {
        long tsec = millis / 1000;
        long h = tsec / 60 / 60;
        long m = (tsec - (h * 60 * 60)) / 60;
        long s = (tsec - (h * 60 * 60) - (m * 60));
        StringBuffer out = new StringBuffer();
        if (h > 0) {
            out.append(h + " hour");
            if (h > 1) out.append('s');
        }
        if (m > 0) {
            if (h > 0) out.append(", ");
            out.append(m + " minute");
            if (m > 1) out.append('s');
        }
        if (s > 0 || (h == 0 && m == 0)) {
            if (h > 0 || m > 0) out.append(", ");
            out.append(s + " second");
            if (s != 1) out.append('s');
        }
        return out.toString();
    }

    public static void one(FedoraAPIA apia, FedoraAPIM apim, String pid, String format, String exportContext, File dir) throws Exception {
        String fName = pid.replaceAll(":", "_") + ".xml";
        File file = new File(dir, fName);
        System.out.println("Exporting " + pid + " to " + file.getPath());
        AutoExporter.export(apia, apim, pid, format, exportContext, new FileOutputStream(file));
    }

    public static int multi(FedoraAPIA apia, FedoraAPIM apim, String fTypes, String format, String exportContext, File dir) throws Exception {
        int count = 0;
        if (fTypes.indexOf("D") != -1) {
            System.out.println("Exporting all FedoraBDefObjects");
            count += multi(apia, apim, 'D', format, exportContext, dir);
        }
        if (fTypes.indexOf("M") != -1) {
            System.out.println("Exporting all FedoraBMechObjects");
            count += multi(apia, apim, 'M', format, exportContext, dir);
        }
        if (fTypes.indexOf("O") != -1) {
            System.out.println("Exporting all FedoraObjects");
            count += multi(apia, apim, 'O', format, exportContext, dir);
        }
        System.out.println("Finished exporting.");
        return count;
    }

    public static int multi(FedoraAPIA apia, FedoraAPIM apim, char fType, String format, String exportContext, File dir) throws Exception {
        int count = 0;
        String fTypeString = "" + fType;
        FieldSearchQuery query = new FieldSearchQuery();
        Condition cond = new Condition();
        cond.setProperty("fType");
        cond.setOperator(ComparisonOperator.fromValue("eq"));
        cond.setValue(fTypeString);
        Condition[] conditions = new Condition[1];
        conditions[0] = cond;
        query.setConditions(conditions);
        query.setTerms(null);
        String[] resultFields = new String[1];
        resultFields[0] = "pid";
        FieldSearchResult result = AutoFinder.findObjects(apia, resultFields, 100, query);
        while (result != null) {
            ObjectFields[] ofs = result.getResultList();
            for (int i = 0; i < ofs.length; i++) {
                String pid = ofs[i].getPid();
                one(apia, apim, pid, format, exportContext, dir);
                count++;
            }
            String token = null;
            try {
                token = result.getListSession().getToken();
            } catch (Throwable th) {
            }
            if (token != null) {
                result = AutoFinder.resumeFindObjects(apia, token);
            } else {
                result = null;
            }
        }
        return count;
    }

    /**
     * Print error message and show usage for command-line interface.
     */
    public static void badArgs(String msg) {
        System.err.println("Command: fedora-export");
        System.err.println();
        System.err.println("Summary: Exports one or more objects from a Fedora repository.");
        System.err.println();
        System.err.println("Syntax:");
        System.err.println("  fedora-export HST:PRT USR PSS PID|FTYPS FORMAT ECONTEXT PATH PROTOCOL");
        System.err.println();
        System.err.println("Where:");
        System.err.println("  HST    is the repository hostname.");
        System.err.println("  PRT    is the repository port number.");
        System.err.println("  USR    is the id of the repository user.");
        System.err.println("  PSS    is the password of repository user.");
        System.err.println("  PID    is the id of the object to export from the source repository.");
        System.err.println("  FTYPS  is any combination of the characters O, D, and M, specifying");
        System.err.println("         which Fedora object type(s) should be exported. O=data objects,");
        System.err.println("         D=behavior definitions, and M=behavior mechanisms.");
        System.err.println("  FORMAT is the XML format to export ");
        System.err.println("         ('foxml1.0', 'metslikefedora1', or 'default')");
        System.err.println("  ECONTEXT is the export context (which indicates what use case");
        System.err.println("         the output should be prepared for.");
        System.err.println("         ('public', 'migrate', 'archive' or 'default')");
        System.err.println("  PATH   is the directory to export the object.");
        System.err.println("  PROTOCOL is the how to connect to repository, either http or https.");
        System.err.println();
        System.err.println("Examples:");
        System.err.println("fedora-export myrepo.com:8443 user pw demo:1 foxml1.0 migrate . https");
        System.err.println();
        System.err.println("  Exports demo:1 for migration in FOXML format ");
        System.err.println("  using the secure https protocol (SSL).");
        System.err.println("  (from myrepo.com:80 to the current directory).");
        System.err.println();
        System.err.println("fedora-export myrepo.com:80 user pw DMO default default /tmp/fedoradump http");
        System.err.println();
        System.err.println("  Exports all objects in the default export format and context ");
        System.err.println("  (from myrepo.com:80 to directory /tmp/fedoradump).");
        System.err.println();
        System.err.println("ERROR  : " + msg);
        System.exit(1);
    }

    /**
     * Command-line interface for doing exports.
     */
    public static void main(String[] args) {
        try {
            if (args.length != 8) {
                Export.badArgs("Wrong number of arguments.");
            }
            String[] hp = args[0].split(":");
            if (hp.length != 2) {
                Export.badArgs("First arg must be of the form 'host:portnum'");
            }
            String protocol = args[7];
            if ((!protocol.equals("http")) && (!protocol.equals("https"))) {
                Export.badArgs("PROTOCOL arg must be 'http' or 'https'");
            }
            String baseURL = protocol + "://" + hp[0] + ":" + Integer.parseInt(hp[1]) + "/fedora";
            FedoraClient fc = new FedoraClient(baseURL, args[1], args[2]);
            FedoraAPIA sourceRepoAPIA = fc.getAPIA();
            FedoraAPIM sourceRepoAPIM = fc.getAPIM();
            String exportFormat = args[4];
            String exportContext = args[5];
            if ((!exportFormat.equals("metslikefedora1")) && (!exportFormat.equals("foxml1.0")) && (!exportFormat.equals("default"))) {
                Export.badArgs("FORMAT arg must be 'metslikefedora1', 'foxml1.0', or 'default'");
            }
            if ((!exportContext.equals("public")) && (!exportContext.equals("migrate")) && (!exportContext.equals("archive")) && (!exportContext.equals("default"))) {
                Export.badArgs("ECONTEXT arg must be 'public', 'migrate', 'archive', or 'default'");
            }
            RepositoryInfo repoinfo = sourceRepoAPIA.describeRepository();
            StringTokenizer stoken = new StringTokenizer(repoinfo.getRepositoryVersion(), ".");
            if (new Integer(stoken.nextToken()).intValue() < 2 && ((!exportFormat.equals("metslikefedora1") && !exportFormat.equals("default")))) Export.badArgs("FORMAT arg must be 'metslikefedora1' or 'default' for pre-2.0 repository.");
            if (exportFormat.equals("default")) {
                exportFormat = null;
            }
            if (exportContext.equals("default")) {
                exportContext = null;
            }
            if (args[3].indexOf(":") == -1) {
                int count = Export.multi(sourceRepoAPIA, sourceRepoAPIM, args[3], exportFormat, exportContext, new File(args[6]));
                System.out.print("Exported " + count + " objects.");
            } else {
                Export.one(sourceRepoAPIA, sourceRepoAPIM, args[3], exportFormat, exportContext, new File(args[6]));
                System.out.println("Exported " + args[3]);
            }
        } catch (Exception e) {
            System.err.print("Error  : ");
            if (e.getMessage() == null) {
                e.printStackTrace();
            } else {
                System.err.print(e.getMessage());
            }
        }
    }
}
