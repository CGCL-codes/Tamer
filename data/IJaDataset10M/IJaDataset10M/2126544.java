package com.nimium.ldap;

import java.util.*;
import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.ldap.*;
import java.io.*;
import com.nimium.Base64;
import org.vrspace.util.*;

/**
This class compares two LDAP trees and generates LDIF file which can be imported into destination LDAP.
*/
public class LDAPImport {

    public static void main(String[] args) {
        LDAPImport imp = new LDAPImport();
        imp.err = new PrintWriter(System.out);
        try {
            imp.load();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    PrintWriter out;

    PrintWriter err;

    DirContext ctx1;

    DirContext ctx2;

    String ldifFileName;

    String errFileName;

    String errFileEncoding;

    String login1;

    String passwd1;

    String ldapUrl1;

    String ldapRoot1;

    String login2;

    String passwd2;

    String ldapUrl2;

    String ldapRoot2;

    public static final String propFileName = "ldapimport.properties";

    /**
  Main method. Connects to the source ldap server, destination ldap server, synchronize.
  */
    public void load() throws Exception {
        Logger logger = new Logger();
        Properties props = new Properties();
        try {
            InputStream in = new FileInputStream(propFileName);
            props.load(in);
        } catch (IOException ioe) {
            Logger.logWarning("Can't load properties, using defaults - " + ioe);
            setDefaultProperties(props);
            saveProperties(props, propFileName);
        }
        ldifFileName = props.getProperty("ldiffile", "import.ldif");
        out = createOutputFile(ldifFileName);
        errFileName = props.getProperty("logfile", "ldapimport.log");
        if (err == null) {
            errFileEncoding = props.getProperty("ldiffile.encoding", "ISO-8859-2");
            err = createOutputFile(errFileName, errFileEncoding);
        }
        login1 = props.getProperty("src.login");
        passwd1 = props.getProperty("src.password");
        ldapUrl1 = props.getProperty("src.url");
        ldapRoot1 = props.getProperty("src.root");
        ctx1 = loginLdap(ldapUrl1, login1, passwd1);
        login2 = props.getProperty("dest.login");
        passwd2 = props.getProperty("dest.password");
        ldapUrl2 = props.getProperty("dest.url");
        ldapRoot2 = props.getProperty("dest.root");
        ctx2 = loginLdap(ldapUrl2, login2, passwd2);
        SearchControls ctls = new SearchControls();
        ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String filter = "(objectclass=*)";
        long time = System.currentTimeMillis();
        NamingEnumeration source = ctx1.search(ldapRoot1, filter, ctls);
        HashMap srcView = new HashMap();
        int srcCnt = 0;
        while (source.hasMore()) {
            srcCnt++;
            SearchResult obj = (SearchResult) source.next();
            Attributes attr = obj.getAttributes();
            String dn = obj.getName();
            if (dn.length() > 0) srcView.put(dn, attr);
        }
        Logger.logInfo("Total of " + srcCnt + " source LDAP entries selected in " + (System.currentTimeMillis() - time) + " ms");
        time = System.currentTimeMillis();
        HashMap destView = new HashMap();
        NamingEnumeration dest = ctx2.search(ldapRoot2, filter, ctls);
        int destCnt = 0;
        while (dest.hasMore()) {
            destCnt++;
            SearchResult obj = (SearchResult) dest.next();
            Attributes attr = obj.getAttributes();
            String dn = obj.getName();
            if (dn.length() > 0) destView.put(dn, attr);
        }
        Logger.logInfo("Total of " + destCnt + " destination LDAP entries selected in " + (System.currentTimeMillis() - time) + " ms");
        time = System.currentTimeMillis();
        HashMap added = new HashMap();
        HashMap removed = new HashMap();
        HashMap changed = new HashMap();
        Set keys = srcView.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            Attributes destRow = (Attributes) destView.get(key);
            Attributes srcRow = (Attributes) srcView.get(key);
            if (destRow == null) {
                Logger.logInfo("New: " + key);
                HashMap newAttr = new HashMap();
                NamingEnumeration ne = srcRow.getIDs();
                while (ne.hasMore()) {
                    String id = (String) ne.next();
                    newAttr.put(id, srcRow.get(id));
                }
                added.put(key, newAttr);
            } else {
                HashMap changes = new HashMap();
                NamingEnumeration ne = srcRow.getIDs();
                boolean dirty = false;
                while (ne.hasMore()) {
                    String id = (String) ne.next();
                    if (destRow.get(id) == null) {
                        Logger.logInfo("Changed " + key + " new attr " + srcRow.get(id));
                        changes.put(id, new Change(srcRow.get(id), Change.ADD));
                        dirty = true;
                    } else if (destRow.get(id).get() instanceof byte[] && srcRow.get(id).get() instanceof byte[] && destRow.get(id).get() != null && !destRow.get(id).get().equals(srcRow.get(id).get())) {
                        if (!Arrays.equals((byte[]) destRow.get(id).get(), (byte[]) srcRow.get(id).get())) {
                            String srcVal = Base64.encodeBytes((byte[]) srcRow.get(id).get());
                            String destVal = Base64.encodeBytes((byte[]) destRow.get(id).get());
                            Logger.logInfo("Changed binary " + key + " attr " + id + " old=" + destVal + " new=" + srcVal);
                            changes.put(id, new Change(srcRow.get(id), Change.REPLACE));
                            dirty = true;
                        }
                    } else if (destRow.get(id).get() instanceof String && srcRow.get(id).get() instanceof String && destRow.get(id).get() != null && !destRow.get(id).get().equals(srcRow.get(id).get())) {
                        String srcVal = (String) srcRow.get(id).get();
                        String destVal = (String) destRow.get(id).get();
                        int pos = destVal.indexOf(ldapRoot2);
                        if (pos > 0) {
                            destVal = destVal.substring(0, pos) + ldapRoot1;
                        }
                        if (!destVal.equals(srcVal)) {
                            Logger.logInfo("Changed String " + key + " attr " + id + " old=" + destRow.get(id) + " new=" + srcRow.get(id));
                            changes.put(id, new Change(srcRow.get(id), Change.REPLACE));
                            dirty = true;
                        }
                    } else if (!srcRow.get(id).equals(destRow.get(id))) {
                        Logger.logInfo("Changed " + key + " attr " + id + " old=" + destRow.get(id) + " new=" + srcRow.get(id));
                        changes.put(id, new Change(srcRow.get(id), Change.REPLACE));
                        dirty = true;
                    }
                }
                ne = destRow.getIDs();
                while (ne.hasMore()) {
                    String id = (String) ne.next();
                    if (srcRow.get(id) == null) {
                        Logger.logInfo("Changed " + key + " removed attr " + id);
                        changes.put(id, new Change(destRow.get(id), Change.DELETE));
                        dirty = true;
                    }
                }
                if (dirty) {
                    changed.put(key, changes);
                }
            }
        }
        keys = destView.keySet();
        it = keys.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            Attributes destRow = (Attributes) destView.get(key);
            Attributes srcRow = (Attributes) srcView.get(key);
            if (srcRow == null) {
                Logger.logInfo("Removed: " + key);
                removed.put(key, destRow);
            }
        }
        Logger.logInfo("Added: " + added.size() + " Removed: " + removed.size() + " Changed: " + changed.size() + " completed in " + (System.currentTimeMillis() - time) + " ms");
        time = System.currentTimeMillis();
        Iterator iDel = removed.keySet().iterator();
        while (iDel.hasNext()) {
            String key = (String) iDel.next();
            processChanges(key, Change.DELETE, null);
        }
        Iterator iNew = added.keySet().iterator();
        while (iNew.hasNext()) {
            String key = (String) iNew.next();
            HashMap changes = (HashMap) added.get(key);
            processChanges(key, Change.ADD, changes);
        }
        Iterator iChg = changed.keySet().iterator();
        while (iChg.hasNext()) {
            String key = (String) iChg.next();
            HashMap changes = (HashMap) changed.get(key);
            processChanges(key, Change.REPLACE, changes);
        }
        out.flush();
        out.close();
        Logger.logInfo("Output to " + ldifFileName + " written in " + (System.currentTimeMillis() - time) + " ms");
        err.flush();
        err.close();
        System.exit(0);
    }

    /**
  Process changes to one LDAP row.
  This method generates LDIF output for given parameters.
  @param key LDAP row unique identifier, usually dn.
  @param changeType 0 = add, 1 = modify, 2 = delete
  @param changes Map containing changes to the row. May be null for changeType = delete.
  */
    public void processChanges(String key, int changeType, HashMap changes) throws NamingException {
        String dn = key + "," + ldapRoot2;
        out.println("dn: " + dn);
        if (changeType == Change.ADD) {
            out.println("changetype: add");
        } else if (changeType == Change.REPLACE) {
            out.println("changetype: modify");
        } else if (changeType == Change.DELETE) {
            out.println("changetype: delete");
            out.println();
            return;
        }
        Iterator iFields = changes.entrySet().iterator();
        while (iFields.hasNext()) {
            Map.Entry entry = (Map.Entry) iFields.next();
            String attr = (String) entry.getKey();
            Object val = entry.getValue();
            if (val == null) {
                Logger.logError("null value for " + attr);
            } else {
                if (val instanceof String) {
                    String value = (String) val;
                    out.println(attr + ": " + value);
                } else if (val.getClass().isArray()) {
                    String[] value = (String[]) val;
                    for (int i = 0; i < value.length; i++) {
                        out.println(attr + ": " + value[i]);
                    }
                } else if (val instanceof Attribute) {
                    NamingEnumeration ne = ((Attribute) val).getAll();
                    while (ne.hasMore()) {
                        out.println(attr + ": " + ne.next());
                    }
                } else if (val instanceof Change) {
                    Change change = (Change) val;
                    out.println(change);
                    out.println("-");
                } else {
                    Logger.logError("ERROR: Unexpected value class for " + attr + " - " + val.getClass().getName());
                }
            }
        }
        out.println();
    }

    /**
  This class encapsulates change to a single attribute
  */
    public class Change {

        public static final int ADD = 0;

        public static final int REPLACE = 1;

        public static final int DELETE = 2;

        public int type;

        public Attribute value;

        public Change(Attribute value, int type) {
            this.value = value;
            this.type = type;
        }

        public String toString() {
            StringBuffer ret = new StringBuffer();
            if (type == REPLACE) {
                ret.append("replace: ");
                ret.append(value.getID());
            } else if (type == ADD) {
                ret.append("add: ");
                ret.append(value.getID());
            } else if (type == DELETE) {
                ret.append("delete: ");
                ret.append(value.getID());
                return ret.toString();
            }
            try {
                NamingEnumeration ne = value.getAll();
                while (ne.hasMore()) {
                    Object val = ne.next();
                    ret.append("\n");
                    ret.append(value.getID());
                    if (val instanceof byte[]) {
                        ret.append(":: ");
                        ret.append(Base64.encodeBytes((byte[]) val));
                    } else {
                        ret.append(": ");
                        ret.append(val);
                    }
                }
            } catch (NamingException e) {
                e.printStackTrace(err);
            }
            return ret.toString();
        }
    }

    /**
  Saves properties to file.
  */
    public void saveProperties(Properties props, String fileName) {
        try {
            OutputStream out = new FileOutputStream(fileName);
            props.store(out, "no comment");
            Logger.logInfo("Default properties saved to " + propFileName);
        } catch (IOException ioe) {
            Logger.logError("Can't save properties to " + fileName + " - " + ioe);
        }
    }

    /**
  Connect to LDAP server.
  @param ldapUrl URL of the server
  @param login LDAP admin login
  @param passwd LDAP admin password
  */
    public DirContext loginLdap(String ldapUrl, String login, String passwd) throws NamingException {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapUrl);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, login);
        env.put(Context.SECURITY_CREDENTIALS, passwd);
        DirContext ctx = new InitialDirContext(env);
        Logger.logInfo("Connected LDAP server " + ldapUrl + " as " + login);
        return ctx;
    }

    /**
  Create PrintWriter writing to file ldifFileName, file encoding UTF-8
  @see ldifFileName
  */
    PrintWriter createOutputFile() throws IOException {
        return createOutputFile(ldifFileName);
    }

    /**
  Create PrintWriter writing to specified file, file encoding UTF-8
  */
    PrintWriter createOutputFile(String fileName) throws IOException {
        return createOutputFile(fileName, "UTF-8");
    }

    /**
  Create PrintWriter writing to specified file with specified encoding
  @param fileName output file name
  @param encoding encoding to use
  */
    public PrintWriter createOutputFile(String fileName, String encoding) throws IOException {
        File outFile = new File(fileName);
        File oldFile = new File(fileName + ".old");
        if (outFile.exists()) {
            outFile.renameTo(oldFile);
        }
        FileOutputStream outStream = new FileOutputStream(fileName);
        OutputStreamWriter outWriter = new OutputStreamWriter(outStream, encoding);
        PrintWriter out = new PrintWriter(outWriter, true);
        return out;
    }

    /**
  Set default propeties.
  */
    public void setDefaultProperties(Properties props) {
        props.put("src.login", "cn=Manager,dc=replace,dc=source");
        props.put("src.password", "replaceme");
        props.put("src.url", "ldap://vampiresku.labos.nimium.local:389");
        props.put("src.root", "dc=replace,dc=me");
        props.put("dest.login", "cn=adm,dc=replace,dc=destination");
        props.put("dest.password", "replaceme");
        props.put("dest.url", "ldap://nosferatu.labos.nimium.local:389");
        props.put("dest.root", "ou=subnet,dc=replace,dc=destination");
    }
}
