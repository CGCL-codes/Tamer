package gov.lanl.repo;

import gov.lanl.repo.oaidb.srv.PutPostClient;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * RepoUtil.java
 * 
 * Generic frontend program for managing MySQL based
 * repositories put, update defined
 */
public class RepoUtil {

    public static void print_help(String operation) {
        if (operation.equals("update")) {
            System.out.println("RepoUtil -update -repourl [oaicat repository url] -id [identifier]  -xupdate [xupdate file]");
        } else if (operation.equals("bulkupdate")) {
            System.out.println("RepoUtil -bulkupdate -propfile [repospecific.properties] - set[set information optinal]  -metadataprefix [metadataPrefix] -xupdate [xupdate file]");
        } else if (operation.equals("put")) {
            System.out.println("RepoUtil -put -repourl [oaicat repository url]  -xmlfile [pmp xml file]");
        } else if (operation.equals("delete")) {
            System.out.println("RepoUtil -delete -repourl [oaicat repository url]  -id [identifier]");
        }
    }

    public static void main(String[] args) {
        String operation = null;
        int i = 0;
        String arg;
        String identifier = null;
        String xmlfile = null;
        String propfile = null;
        String repourl = null;
        String prefix = null;
        String xupdate = null;
        int vflag = 0;
        String set = null;
        try {
            while (i < args.length && args[i].startsWith("-")) {
                arg = args[i++];
                if ("-update".equals(arg)) {
                    operation = "update";
                } else if ("-put".equals(arg)) {
                    operation = "put";
                } else if ("-delete".equals(arg)) {
                    operation = "delete";
                } else if ("-help".equals(arg)) {
                    operation = "help";
                    print_help("update");
                    print_help("delete");
                    print_help("put");
                    print_help("bulkupdate");
                } else if (arg.equals("-id")) {
                    if (i < args.length) {
                        identifier = args[i++];
                    } else {
                        System.err.println("-id requires a identifier");
                    }
                } else if (arg.equals("-xmlfile")) {
                    if (i < args.length) {
                        xmlfile = args[i++];
                    } else {
                        System.err.println("-xmlfile requires a file");
                    }
                } else if (arg.equals("-xupdate")) {
                    if (i < args.length) {
                        xupdate = args[i++];
                    } else {
                        System.err.println("-xupdate requires a file");
                    }
                } else if (arg.equals("-propfile")) {
                    if (i < args.length) {
                        propfile = args[i++];
                    } else {
                        System.err.println("-propfile requires a file");
                    }
                } else if (arg.equals("-repourl")) {
                    if (i < args.length) {
                        repourl = args[i++];
                    } else {
                        System.err.println("-repourl requires a url");
                    }
                } else if (arg.equals("-set")) {
                    if (i < args.length) {
                        set = args[i++];
                    } else {
                        System.err.println("-set requires a string");
                    }
                } else if (arg.equals("-metadataprefix")) {
                    if (i < args.length) {
                        prefix = args[i++];
                    } else {
                        System.err.println("-prefix requires a string");
                    }
                }
            }
            if (operation.equals("update")) {
                if (repourl.equals(null)) {
                    throw new Exception("-repourl [url of repository] requered for update");
                }
                if (identifier.equals(null)) {
                    throw new Exception("-id [identifier of record requered]");
                }
                if (xupdate.equals(null)) {
                    throw new Exception("-xupdate [file with xupdate statment ] requered");
                }
                String xml = readFileFromDisk(xupdate);
                PutPostClient client = new PutPostClient();
                String result = client.execUpdate(repourl, identifier, xml);
                System.out.println(result);
                System.exit(0);
            } else if (operation.equals("delete")) {
                if (repourl.equals(null)) {
                    throw new Exception("-repourl [url of repository] requered for delete");
                }
                if (identifier.equals(null)) {
                    throw new Exception("-id [identifier of record required]");
                }
                PutPostClient client = new PutPostClient();
                String result = client.execDelete(repourl, identifier);
                System.out.println(result);
                System.exit(0);
            } else if (operation.equals("put")) {
                if (repourl.equals(null)) {
                    throw new Exception("-repourl [url of repository] required for put");
                }
                if (xmlfile.equals(null)) {
                    throw new Exception("-xmlfile [file with pmp xml  ] required");
                }
                String xml = readFileFromDisk(xmlfile);
                PutPostClient client = new PutPostClient();
                String result = client.execPut(repourl, xml);
                System.out.println(result);
                System.exit(0);
            } else if (operation.equals("bulkupdate")) {
                if (propfile.equals(null)) {
                    throw new Exception("-propfile [specific property file of repository] requered for update");
                }
                if (repourl.equals(null)) {
                    throw new Exception("-repourl [url of repository] requered for update");
                }
                if (prefix.equals(null)) {
                    throw new Exception("-metadataprefix [metadataprefix of repository] requered for update");
                }
                if (xupdate.equals(null)) {
                    throw new Exception("-xupdate [file with xupdate statment ] requered");
                }
                GenRegUpdateBulk b = new GenRegUpdateBulk();
                b.Exec(propfile, repourl, prefix, set, xupdate);
                vflag = 1;
                System.exit(0);
            }
            if (vflag == 0) {
                System.out.println("Supported operations:");
                print_help("update");
                print_help("delete");
                print_help("put");
                print_help("bulkupdate");
                throw new Exception("unsupported argument list");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFileFromDisk(String fileName) throws Exception {
        File file = new File(fileName);
        FileInputStream insr = new FileInputStream(file);
        byte[] fileBuffer = new byte[(int) file.length()];
        insr.read(fileBuffer);
        insr.close();
        return new String(fileBuffer);
    }

    public static String readFileFromInput() throws Exception {
        BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
        String inputLine;
        StringBuffer b = new StringBuffer();
        while ((inputLine = is.readLine()) != null) {
            b.append(inputLine);
        }
        is.close();
        return b.toString();
    }
}
