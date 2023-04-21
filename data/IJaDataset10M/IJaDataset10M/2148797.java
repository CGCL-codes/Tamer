package hu.sqooler.jdbc;

import java.io.*;
import java.util.*;

/**
 * Parse Oracle's Tnsnames.ora file
 * 
 */
public class TnsParser {

    /** Tnsnames.ora File object */
    private File tnsnamesora = null;

    /** Holds Tnsnames.ora file contents */
    private String filestr = "";

    /** Holds delimited Alias/Service name information */
    private String aliasstr = "";

    /** Standard End-Of-Line Carriage Return\Line feed */
    private String eol = "";

    /** Space identifier */
    private static final String SPACE_REP = " ";

    /** null identifier */
    private static final String NULL_REP = "";

    /** Comment identifier */
    private static final char COMMCHAR = '#';

    /** (char)5 identifier */
    private static final String STR_CHAR_5 = (char) 5 + "";

    private static TnsParser instance = null;

    private static final String HOST = "HOST";

    private static final String PORT = "PORT";

    private static final String CONNECT_DATA = "CONNECT_DATA";

    private static final String SID = "SID";

    private static final String SERVICE_NAME = "SERVICE_NAME";

    private static final String DESCRIPTION = "DESCRIPTION";

    private static final String ADDRESS = "ADDRESS";

    private long lastSize = 0;

    private long lastModified = 0;

    private String lastLocation = "";

    /**
    * Constructs TNSParse object used to parse Tnsnames.ora file
    * 
    * @param tnsFile:
    *                File object
    */
    private TnsParser(File tnsFile) throws Exception {
        this.tnsnamesora = tnsFile;
        this.eol = getEndOfLine();
        tnsReadAndParseFile();
    }

    public static TnsParser getInstance(String location) throws Exception {
        File tnsFile = new File(location + File.separator + "tnsnames.ora");
        if (!tnsFile.exists()) {
            throw new Exception("No tnsnames.ora in the location " + location);
        }
        if (instance == null || !instance.lastLocation.equals(location) || instance.lastSize != tnsFile.length() || instance.lastModified != tnsFile.lastModified()) {
            instance = new TnsParser(tnsFile);
            instance.lastSize = tnsFile.length();
            instance.lastModified = tnsFile.lastModified();
            instance.lastLocation = location;
        }
        return instance;
    }

    /**
    * Return clean and continuous (char)5 separated list of all connect strings in the file.
    * 
    * @return (char)5 separated list of all connect strings in the file.
    */
    public String getStringList() throws Exception {
        return filestr;
    }

    /**
    * Return End-Of-Line delimited list of Alias/Service names.
    * 
    * @return End-Of-Line delimited list of Alias/Service names
    */
    public String getAliasList() throws Exception {
        return aliasstr;
    }

    /**
    * Return full connect string. Full connect string begins with Alias/Service name and ends with
    * last ")".
    * 
    * @param alias Alias/Service name
    * @return Full connect string
    */
    public String getFullConnString(String alias) throws Exception {
        checkAliasExist(alias);
        return getConnectString(alias);
    }

    /**
    * Extracts a TCP string if available, builds the string as used by Oracle thin jdbc driver.
    * 
    * @param alias Alias/Service name
    * @return Fully concatenated Thin TCP string like <HOST>: <PORT>: <SID>
    * OR null if does not exist
    */
    public String extractTcpString(String alias) throws Exception {
        checkAliasExist(alias);
        String connstr = getConnectString(alias);
        if (!isTcpAvailable(connstr)) return null;
        return buildThinString(getFirstTcpString(connstr));
    }

    /**
    * Returns the host for this alias.
    * @param alias Alias/Service name
    * @return The host
    * @throws Exception
    */
    public String getHost(String alias) throws Exception {
        checkAliasExist(alias);
        String connstr = getConnectString(alias);
        if (!isTcpAvailable(connstr)) return null;
        return parseTcpString(getFirstTcpString(connstr), HOST);
    }

    /**
    * Returns the port number for this alias.
    * @param alias Alias/Service name
    * @return The port number
    * @throws Exception
    */
    public String getPort(String alias) throws Exception {
        checkAliasExist(alias);
        String connstr = getConnectString(alias);
        if (!isTcpAvailable(connstr)) return null;
        return parseTcpString(getFirstTcpString(connstr), PORT);
    }

    /**
    * Returns the connection data.
    * @param alias Alias/Service name
    * @return The connection data
    * @throws Exception
    */
    public String getConnectData(String alias) throws Exception {
        checkAliasExist(alias);
        String connstr = getConnectString(alias);
        if (!isTcpAvailable(connstr)) return null;
        String conn = parseTcpString(getFirstTcpString(connstr), SERVICE_NAME);
        if (conn.equals("")) conn = parseTcpString(getFirstTcpString(connstr), SID);
        if (conn.equals("")) throw new Exception("Either SERVICE_NAME nor SID defined in tnsnames.ora.");
        return conn;
    }

    /**
    * Returns all available databases.
    * @return All available databases
    * @throws Exception
    */
    public String[] getDatabaseNames() throws Exception {
        SortedMap map = new TreeMap(new Comparator() {

            public int compare(Object arg0, Object arg1) {
                return ((String) arg0).compareTo((String) arg1);
            }
        });
        String[] list = getAliasList().split(TnsParser.getEndOfLine());
        for (int i = 0; i < list.length; i++) map.put(list[i], "");
        int i = 0;
        for (Iterator iter = map.keySet().iterator(); iter.hasNext(); ) list[i++] = (String) iter.next();
        return list;
    }

    /**
    * Parses a connection string and returns all defined ADDRESSES.
    * 
    * @param alias Alias/Service name
    * @return End-Of-Line delimited list of all addresses including CONNECT_DATA
    *            (ADDRESS=(.....))(CONNECT_DATA=(...))
    */
    public String enumerateAddresses(String alias) throws Exception {
        checkAliasExist(alias);
        String connstr = getConnectString(alias);
        return enumerateAddressList(connstr);
    }

    /**
    * Only checks if all parenthesis "(" and ")" are matched
    * 
    * @param alias:
    *                Alias/Service name
    * @return boolean: true if valid, false otherwise
    */
    public boolean isConnStrValid(String alias) throws Exception {
        checkAliasExist(alias);
        String strtmp = getConnectString(alias);
        strtmp = strtmp + " ";
        int i = 0;
        int j = 0;
        int countera = 0;
        int counterb = 0;
        while (true) {
            i = strtmp.indexOf("(", i + 1);
            if (i == -1) break;
            countera = countera + 1;
        }
        while (true) {
            j = strtmp.indexOf(")", j + 1);
            if (j == -1) break;
            counterb = counterb + 1;
        }
        return countera == counterb;
    }

    /**
    * Return full connection string from &lt;ServiceName&gt; to to last ")"
    * 
    * @param alias:
    *                Alias/Service name
    * @return String: Full connection string
    */
    private String getConnectString(String alias) throws Exception {
        String retstr = "";
        String tokstr = "";
        String strtmp = alias.toUpperCase().trim();
        StringTokenizer st = new StringTokenizer(filestr, STR_CHAR_5);
        while (st.hasMoreTokens()) {
            tokstr = st.nextToken().trim();
            if (tokstr.startsWith(strtmp)) {
                if ((tokstr.charAt(strtmp.length()) == '.') || (tokstr.charAt(strtmp.length()) == '=')) {
                    retstr = tokstr;
                    break;
                }
            }
        }
        return retstr;
    }

    /**
    * Checks if Alias/Service name exists
    * 
    * @param alias:
    *                Alias/Service name
    * @throws Exception
    *                 if does not exist
    */
    private void checkAliasExist(String alias) throws Exception {
        String strtmp = getConnectString(alias);
        if (strtmp.equalsIgnoreCase("")) throw new Exception("Service name '" + alias.toUpperCase() + "' does not exist in file '" + tnsnamesora.getAbsolutePath() + "'");
    }

    /**
    * Checks if connection string has TCP protocol defined
    * 
    * @param connstr:
    *                Full connection string to test
    * @return boolean: true if TCP available, false otherwise
    */
    private boolean isTcpAvailable(String connstr) throws Exception {
        return connstr.indexOf("(PROTOCOL=TCP)") != -1;
    }

    /**
    * Returns first available TCP connection string used by getThinTcpConn()
    * 
    * @param connstr:
    *                Full connection string to parse
    * @return String: First available TCP connection string
    */
    private String getFirstTcpString(String connstr) throws Exception {
        String retstr = "";
        int i = 0;
        int j = 0;
        while (true) {
            i = connstr.indexOf("(" + ADDRESS + "=", j);
            if (i == -1) break;
            i = connstr.indexOf("=", i) + 1;
            j = connstr.indexOf("))", i) + 1;
            if ((i != -1) && (j != -1)) retstr = connstr.substring(i, j);
            if (retstr.indexOf("PROTOCOL=TCP") != -1) {
                i = connstr.indexOf("(" + CONNECT_DATA + "=", j);
                j = connstr.length();
                if ((i != -1) && (j != -1)) retstr = retstr + connstr.substring(i, j);
                break;
            }
            retstr = "";
        }
        return retstr;
    }

    /**
    * Parses TCP string and returns value of "HOST", "PORT" etc
    * 
    * @param tcpstring:
    *                Unparsed TCP string returned by getFirstTcpString()
    * @param getval:
    *                Value to get e.g. "HOST", "PORT" etc
    */
    private String parseTcpString(String tcpstring, String getval) throws Exception {
        String retstr = "";
        int i = 0;
        int j = 0;
        if (getval.equalsIgnoreCase(CONNECT_DATA)) {
            i = tcpstring.indexOf(getval);
            i = tcpstring.indexOf("=", i);
            i = tcpstring.indexOf("(", i);
            i = tcpstring.indexOf("=", i) + 1;
            j = tcpstring.indexOf(")", i);
            if ((i != -1) && (j != -1)) retstr = tcpstring.substring(i, j);
        } else {
            i = tcpstring.indexOf(getval);
            if (i == -1) return "";
            i = tcpstring.indexOf("=", i) + 1;
            j = tcpstring.indexOf(")", i);
            if ((i != -1) && (j != -1)) retstr = tcpstring.substring(i, j);
        }
        return retstr;
    }

    /**
    * Parses TCP string and returns a full concatenated string e.g. &lt;HOST&gt;: &lt;PORT&gt;:
    * &lt;SID&gt;
    * 
    * @param tcpstring:
    *                Unparsed TCP string returned by getFirstTcpString()
    */
    private String buildThinString(String tcpstring) throws Exception {
        String retstr = parseTcpString(tcpstring, HOST) + ":";
        retstr += parseTcpString(tcpstring, PORT) + ":";
        retstr += parseTcpString(tcpstring, CONNECT_DATA);
        return retstr;
    }

    private String enumerateAddressList(String connstr) throws Exception {
        String retstr = "";
        String strtmp = "";
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        while (true) {
            i = connstr.indexOf("(" + ADDRESS + "=", j);
            if (i == -1) break;
            j = connstr.indexOf("))", i);
            j = connstr.indexOf("(", j);
            if ((i != -1) && (j != -1)) {
                strtmp = connstr.substring(i, j);
                if (strtmp.indexOf("(PROTOCOL=BEQ)") != -1) strtmp = strtmp.substring(0, strtmp.indexOf("'))") + 3); else strtmp = strtmp.substring(0, strtmp.indexOf("))") + 2);
                retstr = retstr + strtmp;
                strtmp = "";
            }
            k = connstr.indexOf("(" + CONNECT_DATA + "=", j);
            l = connstr.indexOf("))", k) + 2;
            if ((k != -1) && (l != -1)) retstr = retstr + connstr.substring(k, l) + eol;
        }
        if (retstr.endsWith(eol)) retstr = retstr.substring(0, retstr.length() - 1);
        return retstr;
    }

    /**
    * @return String: End Of Line according to platform Get End Of Line according to
    * platform
    */
    private static String getEndOfLine() {
        return File.separator.equals("/") ? "\n" : "\r\n";
    }

    /** 
    * Read and parse Tnsnames.ora file.
    */
    private void tnsReadAndParseFile() throws Exception {
        tnsCheckFileValid();
        tnsReadFile();
        tnsRemoveAndReplace(SPACE_REP, NULL_REP);
        tnsRemoveComments();
        tnsRemoveAndReplace(eol, eol);
        tnsFixDescKeyword();
        tnsSeparateConnStrings();
        tnsRemoveAndReplace(eol, NULL_REP);
        tnsCorrectMultipleAliases();
        tnsSetAliasString();
    }

    private void tnsCheckFileValid() throws Exception {
        if (!tnsnamesora.isFile()) throw new Exception("File [" + tnsnamesora.getAbsolutePath() + "] does not exist or is not a normal file");
        if (!tnsnamesora.canRead()) throw new Exception("File [" + tnsnamesora.getAbsolutePath() + "] is not readable");
        if (tnsnamesora.length() == 0) throw new Exception("File [" + tnsnamesora.getAbsolutePath() + "] is empty");
    }

    /**
    * Read Tnsnames.ora file using FileReader. 
    * Check if file is of valid Tnsnames.ora format.
    */
    private void tnsReadFile() throws IOException, Exception {
        FileReader fr = new FileReader(tnsnamesora);
        long fsize = tnsnamesora.length();
        char[] cbuf = new char[(int) fsize];
        int offset = 0;
        int len = (int) fsize;
        fr.read(cbuf, offset, len);
        fr.close();
        String strtmp = new String(cbuf);
        filestr = "";
        filestr = strtmp.toUpperCase();
        tnsCheckFileFormat();
    }

    /**
    * Remove and replace strings from filestr.
    * @param remstr: String to be removed 
    * @param repstr: String to be replaced with
    */
    private void tnsRemoveAndReplace(String remstr, String repstr) throws Exception {
        String strtmp = filestr;
        filestr = "";
        if (strtmp.indexOf(remstr) != -1) {
            StringTokenizer st = new StringTokenizer(strtmp, remstr);
            while (st.hasMoreTokens()) {
                filestr = filestr + st.nextToken() + repstr;
            }
        }
        if (filestr.equalsIgnoreCase("")) filestr = strtmp;
        tnsCheckFileFormat();
    }

    /**
    * Remove all comment lines from filestr marked by "#".
    */
    private void tnsRemoveComments() throws Exception {
        String strtmp = filestr;
        filestr = "";
        if (strtmp.indexOf(COMMCHAR + "") != -1) {
            StringTokenizer st = new StringTokenizer(strtmp, eol);
            while (st.hasMoreTokens()) {
                String tokenstr = st.nextToken().trim();
                if (tokenstr.charAt(0) != COMMCHAR) filestr = filestr + tokenstr + eol;
            }
        }
        if (filestr.equalsIgnoreCase("")) filestr = strtmp;
        tnsCheckFileFormat();
    }

    /**
    * Fix =(DESCRIPTION keyword so that there are
    * no spaces and eol's between '=' and '(DESCRIPTION'
    */
    private void tnsFixDescKeyword() throws Exception {
        String strtmp = filestr;
        filestr = "";
        int i = 0;
        int j = 0;
        if (strtmp.indexOf("=" + eol + "(" + DESCRIPTION) != -1) {
            while (strtmp.indexOf("=" + eol + "(" + DESCRIPTION, i) != -1) {
                j = strtmp.indexOf("=" + eol + "(" + DESCRIPTION, i);
                filestr = filestr + strtmp.substring(i, j + 1);
                i = strtmp.indexOf("(", j);
                if (strtmp.indexOf("=" + eol + "(" + DESCRIPTION, i) == -1) filestr = filestr + strtmp.substring(i, strtmp.length());
            }
        }
        if (filestr.equalsIgnoreCase("")) filestr = strtmp;
        tnsCheckFileFormat();
    }

    /**
    * Separate all Connection strings using constant strchar5.
    */
    private void tnsSeparateConnStrings() throws Exception {
        String strtmp = filestr;
        filestr = "";
        boolean bExit = false;
        int i = 0;
        int j = 0;
        if (strtmp.indexOf("=(" + DESCRIPTION) != -1) {
            while (true) {
                j = strtmp.indexOf("=(" + DESCRIPTION, strtmp.indexOf("=(" + DESCRIPTION, i) + 20);
                if (j == -1) {
                    j = strtmp.length();
                    bExit = true;
                } else {
                    j = strtmp.substring(0, j).lastIndexOf(")") + 1;
                }
                filestr = filestr + strtmp.substring(i, j) + STR_CHAR_5;
                if (bExit) break;
                i = j;
            }
        }
        if (filestr.equalsIgnoreCase("")) filestr = strtmp;
        tnsCheckFileFormat();
    }

    /**
    * Correctly separate multiple aliases on one line
    */
    private void tnsCorrectMultipleAliases() throws Exception {
        String strtmp = filestr;
        filestr = "";
        String tokstr = "";
        String aliaslist = "";
        StringTokenizer st = new StringTokenizer(strtmp, STR_CHAR_5);
        int i = 0;
        int j = 0;
        while (st.hasMoreTokens()) {
            tokstr = st.nextToken();
            i = tokstr.indexOf("=", 0);
            j = tokstr.lastIndexOf(")") + 1;
            if (i != -1) {
                aliaslist = tokstr.substring(0, i);
                StringTokenizer st2 = new StringTokenizer(aliaslist, ",");
                while (st2.hasMoreTokens()) {
                    filestr = filestr + st2.nextToken() + tokstr.substring(i, j) + STR_CHAR_5;
                }
            }
        }
        if (filestr.equalsIgnoreCase("")) filestr = strtmp;
        tnsCheckFileFormat();
    }

    /**
    * Set End-Of-Line delimited aliasstr that contains all service names.
    */
    private void tnsSetAliasString() throws Exception {
        aliasstr = "";
        String tokstr = "";
        StringTokenizer st = new StringTokenizer(filestr, STR_CHAR_5);
        while (st.hasMoreTokens()) {
            tokstr = st.nextToken();
            if (tokstr.indexOf("=", 0) != -1) aliasstr = aliasstr + tokstr.substring(0, tokstr.indexOf("=", 0)) + eol;
        }
        if (aliasstr.endsWith(eol)) aliasstr = aliasstr.substring(0, aliasstr.length() - 1);
    }

    /**
    * Check filestr for Tnsnames.ora file keywords. If missing, throw Exception.
    */
    private void tnsCheckFileFormat() throws Exception {
        if (filestr.length() > 0) {
            if ((filestr.indexOf(DESCRIPTION) == -1) || (filestr.indexOf(ADDRESS) == -1) || (filestr.indexOf(CONNECT_DATA) == -1)) throw new Exception("File [" + tnsnamesora.getAbsolutePath() + "] is not an Oracle tnsnames.ora format file OR unable to parse");
        }
    }
}
