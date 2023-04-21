package org.gridtrust.UcsService.Utils.gridmap;

import java.security.MessageDigest;
import java.util.Map;
import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.util.QuotedStringTokenizer;
import org.gridtrust.UcsService.UCSResource;
import org.gridtrust.UcsService.Utils.createuser.base32;

/**
 * @author maurizio colombo
 *
 */
public class GridMap extends org.globus.security.gridmap.GridMap {

    private static final String COMMENT_CHARS = "#";

    private static final char[] EMAIL_KEYWORD_1 = { 'e', '=' };

    private static final char[] EMAIL_KEYWORD_2 = { 'e', 'm', 'a', 'i', 'l', '=' };

    private static final char[] UID_KEYWORD = { 'u', 'i', 'd', '=' };

    private static final int EMAIL_KEYWORD_1_L = 2;

    private static final int EMAIL_KEYWORD_2_L = 6;

    private static final int UID_KEYWORD_L = 4;

    private static final String EMAIL_KEYWORD = "emailaddress=";

    private static final String USERID_KEYWORD = "userid=";

    /**
     * the location of the grid-mapfile loaded for the last time
     */
    public static String last_MAPFILE;

    static final Log logger = LogFactory.getLog(GridMap.class);

    protected Map map;

    private File file;

    private long lastModified;

    public void load(File file) throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            this.file = file;
            this.lastModified = file.lastModified();
            last_MAPFILE = this.file.getAbsolutePath();
            load(in);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public void refresh() throws IOException {
        if (this.file == null) {
            return;
        }
        if (this.file.lastModified() != this.lastModified) {
            load(this.file);
        }
    }

    /**
     * @author maurizio colombo
     *
     */
    static class GridMapEntry implements Serializable {

        String globusID;

        String[] userIDs;

        public String getFirstUserID() {
            return userIDs[0];
        }

        public String[] getUserIDs() {
            return userIDs;
        }

        public String getGlobusID() {
            return globusID;
        }

        public void setGlobusID(String globusID) {
            this.globusID = globusID;
        }

        public void setUserIDs(String[] userIDs) {
            this.userIDs = userIDs;
        }

        public boolean containsUserID(String userID) {
            if (userID == null) {
                return false;
            }
            for (int i = 0; i < userIDs.length; i++) {
                if (userIDs[i].equalsIgnoreCase(userID)) {
                    return true;
                }
            }
            return false;
        }

        public void addUserID(String userID) {
            if (containsUserID(userID)) return;
            String[] ids = new String[userIDs.length + 1];
            System.arraycopy(userIDs, 0, ids, 0, userIDs.length);
            ids[userIDs.length] = userID;
            userIDs = ids;
        }

        public void addUserIDs(String[] userIDs) {
            for (int i = 0; i < userIDs.length; i++) {
                addUserID(userIDs[i]);
            }
        }
    }

    private static boolean keyWordPresent(char[] args, int startIndex, char[] keyword, int length) {
        if (startIndex + length > args.length) {
            return false;
        }
        int j = startIndex;
        for (int i = 0; i < length; i++) {
            if (args[j] != keyword[i]) {
                return false;
            }
            j++;
        }
        return true;
    }

    public static String normalizeDN(String globusID) {
        if (globusID == null) {
            return null;
        }
        globusID = globusID.toLowerCase();
        char[] globusIdChars = globusID.toCharArray();
        StringBuffer normalizedDN = new StringBuffer();
        int i = 0;
        while (i < globusIdChars.length) {
            if (globusIdChars[i] == '/') {
                normalizedDN.append("/");
                if (keyWordPresent(globusIdChars, i + 1, EMAIL_KEYWORD_1, EMAIL_KEYWORD_1_L)) {
                    normalizedDN.append(EMAIL_KEYWORD);
                    i = i + EMAIL_KEYWORD_1_L;
                } else if (keyWordPresent(globusIdChars, i + 1, EMAIL_KEYWORD_2, EMAIL_KEYWORD_2_L)) {
                    normalizedDN.append(EMAIL_KEYWORD);
                    i = i + EMAIL_KEYWORD_2_L;
                } else if (keyWordPresent(globusIdChars, i + 1, UID_KEYWORD, UID_KEYWORD_L)) {
                    normalizedDN.append(USERID_KEYWORD);
                    i = i + UID_KEYWORD_L;
                }
                i++;
            } else {
                normalizedDN.append(globusIdChars[i]);
                i++;
            }
        }
        return normalizedDN.toString();
    }

    private void write(String credential, String user) throws IOException {
        String line;
        String path = last_MAPFILE;
        File temp = new File(path + "_temp");
        InputStream in = null;
        PrintWriter out = null;
        try {
            this.load(last_MAPFILE);
            in = new FileInputStream(new File(last_MAPFILE));
            out = new PrintWriter(new BufferedOutputStream(new FileOutputStream(temp, true)));
        } catch (Exception e) {
            e.getMessage();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        Map localMap = new HashMap();
        GridMapEntry entry;
        QuotedStringTokenizer tokenizer;
        StringTokenizer idTokenizer;
        String outline = new String();
        boolean check = false;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if ((line.length() == 0) || (COMMENT_CHARS.indexOf(line.charAt(0)) != -1)) {
                continue;
            }
            tokenizer = new QuotedStringTokenizer(line);
            String globusID = null;
            if (tokenizer.hasMoreTokens()) {
                globusID = tokenizer.nextToken();
            } else {
                throw new IOException("Globus ID not defined: " + line);
            }
            if (globusID.equals(credential)) {
                line = new String("\"" + credential + "\" " + user);
                check = true;
            }
            outline = outline + line + '\n';
        }
        if (check == false) {
            line = new String("\"" + credential + "\" " + user);
            outline = outline + line + '\n';
        }
        out.print(outline);
        out.close();
        this.file.delete();
        temp.renameTo(new File(path));
        load(path);
        in.close();
    }

    /**
     * adds a new user into the Grid-mapfile
     * @param peerIdentity the DN of the user
     * @param localuser the local system account paired with the user
     * @return true if it succeed
     * @throws Exception
     */
    public boolean forceUser(String peerIdentity, String localuser) throws Exception {
        try {
            this.write(peerIdentity, localuser);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * adds a new user into the Grid-mapfile
     * @param lastMapfile specifies the grid-mapfile
     * @param peerIdentity the DN of the user
     * @param localusers a list of the local system accounts paired with the user
     * @return true if it succeed
     * @throws Exception
     */
    public boolean forceUser(String lastMapfile, String peerIdentity, String localuser) throws Exception {
        String command = "sudo writeUserToGridMapfile ";
        Runtime runtime = Runtime.getRuntime();
        boolean out = false;
        try {
            Process p = runtime.exec(command + lastMapfile + " " + peerIdentity + " " + localuser);
            int exitVal = p.waitFor();
            logger.info("Process exitValue into writeUserToGridMapfile: " + exitVal);
            out = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    /**
     * adds a new user into the Grid-mapfile
     * @param peerIdentity the DN of the user
     * @param localusers a list of the local system accounts paired with the user
     * @return true if it succeed
     * @throws Exception
     */
    public boolean forceUser(String peerIdentity, String[] localusers) throws Exception {
        String aux = new String();
        for (int i = 0; i < localusers.length; i++) {
            if (i == 0) aux = aux + localusers[0]; else aux = aux + "," + localusers[i];
        }
        try {
            this.write(peerIdentity, aux);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
