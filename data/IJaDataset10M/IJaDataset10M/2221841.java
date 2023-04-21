package org.openorb.orb.iiop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.activity.Disposable;
import org.omg.CONV_FRAME.CodeSetComponent;
import org.omg.CONV_FRAME.CodeSetComponentInfo;
import org.openorb.orb.util.Trace;
import org.openorb.util.HexPrintStream;
import org.openorb.util.NumberCache;

/**
 * This class initializes the codeset database for the client and the
 * server side.
 * This class is also used to create the static CodeSetDatabase class.
 *
 * The origin of the OSF registry lies in the DCE environment:
 *    http://www.opengroup.org/dce/info/
 * A description of the OSF charset registry can be found here:
 *    http://www.opengroup.org/tech/rfc/rfc40.2.html
 * The latest OSF charset registry database file can be found here:
 *    ftp://ftp.opengroup.org/pub/code_set_registry
 * Introduction to i18n
 *    http://www.debian.org/doc/manuals/intro-i18n/index.html
 * Another good page for charset/codeset information:
 *    http://oss.software.ibm.com/icu/charset/index.html
 *
 * The following codesets supported by Java are not covered by
 * the OSF charset and codeset registry:
 *<ul>
 *   <li>Big5_HKSCS
 *   <li>Big5_Solaris
 *   <li>Cp737
 *   <li>Cp775
 *   <li>Cp858
 *   <li>Cp860
 *   <li>Cp865
 *   <li>Cp933
 *   <li>Cp942C
 *   <li>Cp943C
 *   <li>Cp949C
 *   <li>Cp1046
 *   <li>Cp1123
 *   <li>Cp1124
 *   <li>Cp1140
 *   <li>Cp1141
 *   <li>Cp1142
 *   <li>Cp1143
 *   <li>Cp1144
 *   <li>Cp1145
 *   <li>Cp1146
 *   <li>Cp1147
 *   <li>Cp1148
 *   <li>Cp1149
 *   <li>Cp1258
 *   <li>GB18030
 *   <li>GBK
 *   <li>ISCII91
 *   <li>ISO2022CN
 *   <li>ISO2022CN_CNS
 *   <li>ISO2022CN_GB
 *   <li>ISO2022JP
 *   <li>ISO2022KR
 *   <li>ISO8859_13
 *   <li>JISAutoDetect
 *   <li>Johab
 *   <li>KOI8_R
 *   <li>MS874
 *   <li>MS932
 *   <li>MS936
 *   <li>MS949
 *   <li>MS950
 *   <li>MacArabic
 *   <li>MacCentralEurope
 *   <li>MacCroatian
 *   <li>MacCyrillic
 *   <li>MacDingbat
 *   <li>MacGreek
 *   <li>MacHebrew
 *   <li>MacIceland
 *   <li>MacRoman
 *   <li>MacRomania
 *   <li>MacSymbol
 *   <li>MacThai
 *   <li>MacTurkish
 *   <li>MacUkraine
 *</ul>
 *
 * @author <a href="michael@rumpfonline.de">Michael Rumpf</a>
 *
 * Date         Author             Changes
 * 18/07/02     E. Putrycz         Changed CodeSet names and lookups to uppercase
 */
public class CodeSetDatabaseInitializer implements LogEnabled, Initializable, Disposable {

    /**
     * The synchronization object for creating the nameToId map.
     */
    private static Object s_sync_table = new Object();

    /**
     * This hash map stores the name to codeset id relation.
     */
    private static HashMap s_name_to_id = null;

    /**
     * The CodeSetComponentInfo instance containing the client codesets.
     */
    private CodeSetComponentInfo m_clientCodeSets;

    /**
     * The CodeSetComponentInfo instance containing the server codesets.
     */
    private CodeSetComponentInfo m_serverCodeSets;

    /**
     * The logger instance for this class.
     */
    private Logger m_logger;

    /**
     * Provide this class with a logger.
     */
    public void enableLogging(Logger logger) {
        m_logger = logger;
    }

    /**
     * Returns the logger that has been passed to enableLogging.
     */
    public Logger getLogger() {
        return m_logger;
    }

    /**
     * Default constructor.
     */
    public CodeSetDatabaseInitializer() {
    }

    /**
     * Initialize the codeset database.
     */
    public void initialize() {
        if (s_name_to_id == null) {
            synchronized (s_sync_table) {
                s_name_to_id = new HashMap();
                CodeSetDatabase.populateNameToIdMap(s_name_to_id);
            }
        }
        m_serverCodeSets = initCodeSets(true);
        m_clientCodeSets = initCodeSets(false);
    }

    /**
     * Delete any member fields.
     */
    public void dispose() {
        m_clientCodeSets = null;
        m_serverCodeSets = null;
        m_logger = null;
    }

    /**
     * Returns the client codeset component info.
     *
     * @return client codeset info.
     */
    public CodeSetComponentInfo getClientCodeSets() {
        return m_clientCodeSets;
    }

    /**
     * Returns the server codeset component info.
     *
     * @return server codeset info.
     */
    public CodeSetComponentInfo getServerCodeSets() {
        return m_serverCodeSets;
    }

    /**
     * Initialize the code sets.
     *
     * @param server True for server side, false for client side.
     * @return A CodeSetComponentInfo instance with a native
     * and an array of conversion codesets.
     */
    private CodeSetComponentInfo initCodeSets(boolean server) {
        String sysenc = System.getProperty("file.encoding");
        ArrayList al;
        int csid = 0;
        int align = 0;
        try {
            String canenc = CodeSetDatabase.canonicalize(sysenc);
            al = (ArrayList) s_name_to_id.get(canenc.toUpperCase());
            if (al == null) {
                if (getLogger() != null && getLogger().isWarnEnabled()) {
                    getLogger().warn("No codesets found for system encoding \'" + sysenc + "\' (canonicalized to \'" + canenc + "\').");
                }
            } else {
                csid = ((Integer) al.get(0)).intValue();
                align = CodeSetDatabase.getAlignmentFromId(csid);
                if (getLogger() != null && getLogger().isDebugEnabled() && Trace.isHigh()) {
                    getLogger().debug((server ? "Server" : "Client") + "NativeCodeSet=" + sysenc + " ( id=0x" + HexPrintStream.toHex(csid) + ", align=" + align + " )");
                }
            }
        } catch (final UnsupportedEncodingException ex) {
            if (getLogger() != null) {
                final String msg = "Error while canonicalizing the system encoding \'" + sysenc + "\'.";
                getLogger().error(msg);
            }
        }
        if (csid == 0 || align > 1) {
            csid = 0x05010001;
            if (getLogger() != null && getLogger().isDebugEnabled() && Trace.isHigh()) {
                getLogger().debug("Can't find \'" + sysenc + "\' in codeset database. Using fallback char data codeset " + CodeSetDatabase.getNameFromId(csid) + ".");
            }
        } else {
            if (getLogger() != null && getLogger().isDebugEnabled() && Trace.isHigh()) {
                getLogger().debug("Using char data codeset \'" + CodeSetDatabase.getNameFromId(csid) + "\'.");
            }
        }
        CodeSetComponent csc_char = new CodeSetComponent(csid, allCompatCodeSets(csid, true, server));
        int iso88591cs = 0x00010001;
        if (csc_char.native_code_set != iso88591cs) {
            for (int i = 0; i < csc_char.conversion_code_sets.length; ++i) {
                if (iso88591cs == csc_char.conversion_code_sets[i]) {
                    iso88591cs = 0;
                    break;
                }
            }
            if (iso88591cs != 0) {
                if (getLogger() != null && getLogger().isDebugEnabled() && Trace.isHigh()) {
                    getLogger().debug("Adding \'" + CodeSetDatabase.getNameFromId(iso88591cs) + "\' to the list of conversion codesets for char data.");
                }
                int[] tmp = new int[csc_char.conversion_code_sets.length + 1];
                System.arraycopy(csc_char.conversion_code_sets, 0, tmp, 0, csc_char.conversion_code_sets.length);
                tmp[csc_char.conversion_code_sets.length] = iso88591cs;
                csc_char.conversion_code_sets = tmp;
            }
        }
        int asciics = 0x00010020;
        if (csc_char.native_code_set != asciics) {
            for (int i = 0; i < csc_char.conversion_code_sets.length; ++i) {
                if (asciics == csc_char.conversion_code_sets[i]) {
                    asciics = 0;
                    break;
                }
            }
            if (asciics != 0) {
                if (getLogger() != null && getLogger().isDebugEnabled() && Trace.isHigh()) {
                    getLogger().debug("Adding \'" + CodeSetDatabase.getNameFromId(asciics) + "\' to the list of conversion codesets for char data.");
                }
                int[] tmp = new int[csc_char.conversion_code_sets.length + 1];
                System.arraycopy(csc_char.conversion_code_sets, 0, tmp, 0, csc_char.conversion_code_sets.length);
                tmp[csc_char.conversion_code_sets.length] = asciics;
                csc_char.conversion_code_sets = tmp;
            }
        }
        csid = 0x00010109;
        if (getLogger() != null && getLogger().isDebugEnabled() && Trace.isHigh()) {
            getLogger().debug("Using wchar data codeset \'" + CodeSetDatabase.getNameFromId(csid) + "\'.");
        }
        CodeSetComponent csc_wchar = new CodeSetComponent(csid, allCompatCodeSets(csid, false, server));
        return new CodeSetComponentInfo(csc_char, csc_wchar);
    }

    /**
     * Return IDs of compatible codesets, the codesets with the largest number
     * of common charsets are output first.
     * This could certainly be further beautified, but currently isn't worth the
     * effort because this method is only used internally. The method used externally
     * is CodeSet.compatible and friends.
     *
     * @param codesetID Codeset to match by similarity.
     * @param singleByte if true return byte oriented charsets only.
     * @param server if true returns 'selected server' codesets only.
     * @return compatible codesets.
     */
    private int[] allCompatCodeSets(int codesetID, boolean singleByte, boolean server) {
        CodeSet entry = CodeSetDatabase.getCodeSetFromId(codesetID);
        if (entry == null) {
            return new int[0];
        }
        short[] charsetIDs = entry.getCharsets();
        if (charsetIDs.length == 1) {
            int[] list = CodeSetDatabase.getCodeSetsFromCharset(charsetIDs[0]);
            int[] ret = new int[list.length];
            int upto = 0;
            for (int i = 0; i < list.length; ++i) {
                CodeSet cs = CodeSetDatabase.getCodeSetFromId(list[i]);
                if ((!server || cs.forServer()) && cs.getName() != null && cs.getCharsets().length == 1 && (!singleByte || cs.getAlignment() <= 1) && cs.getId() != codesetID) {
                    ret[upto++] = cs.getId();
                }
            }
            if (upto < ret.length) {
                int[] tmp = new int[upto];
                System.arraycopy(ret, 0, tmp, 0, upto);
                return tmp;
            }
            return ret;
        }
        ArrayList[] cp = new ArrayList[charsetIDs.length - 1];
        for (int i = 0; i < cp.length; ++i) {
            cp[i] = new ArrayList();
        }
        HashSet examined = new HashSet();
        examined.add(entry);
        int total = 0;
        for (int i = 0; i < charsetIDs.length; ++i) {
            int[] list = CodeSetDatabase.getCodeSetsFromCharset(charsetIDs[0]);
            CodeSet csi = CodeSetDatabase.getCodeSetFromId(list[i]);
            for (int j = 0; j < list.length; ++j) {
                CodeSet csj = CodeSetDatabase.getCodeSetFromId(list[j]);
                if ((!server || csi.forServer()) && csi.getName() != null && (!singleByte || csj.getAlignment() <= 1) && examined.add(csj)) {
                    int count = 0;
                    for (int k = 0, l = 0; k < entry.getCharsets().length && k < csi.getCharsets().length; ) {
                        if (entry.getCharsets()[k] == csi.getCharsets()[l]) {
                            ++count;
                            ++l;
                        } else if (entry.getCharsets()[k] < csi.getCharsets()[l]) {
                            ++i;
                        } else {
                            ++j;
                        }
                        ++k;
                    }
                    if (count >= 2) {
                        cp[cp.length - count + 2].add(NumberCache.getInteger(csj.getId()));
                        ++total;
                    }
                }
            }
        }
        int[] ret = new int[total];
        int upto = 0;
        for (int i = 0; i < cp.length; ++i) {
            Iterator itt = cp[i].iterator();
            while (itt.hasNext()) {
                ret[upto++] = ((Integer) itt.next()).intValue();
            }
        }
        return ret;
    }

    /**
     * Show how to use this class from the command line.
     */
    private static void usage() {
        System.err.println("usage: java org.openorb.orb.iiop.CodeSetDatabase" + " <database file> [-i]");
        System.err.println();
        System.err.println("  This program converts a codeset registry obtained from");
        System.err.println("  ftp://ftp.opengroup.org/pub/code_set_registry into a class");
        System.err.println("  org.openorb.orb.iiop.CodeSetDatabase, which is used to" + " initialize the");
        System.err.println("  codeset database.");
        System.err.println("  <database file>   OSF registry file.");
        System.err.println("  -i                turns on interactive mode.");
    }

    /**
     * Creates the CodeSetDatabase class.
     * Uses codeset database as argument, e.g. cs_registry_1_2h.txt. If the bulk mode is used,
     * the old version of the CodeSetDatabase class is used to get the encoding names.
     * In interactive mode the name is also taken from the old version, if one is present and
     * the user can enter a new name or take over the old one.
     * If there is no old version of CodeSetDatabase the name has to be entered by the user.
     */
    public static void main(String[] args) {
        if (args.length < 1 && args.length > 2) {
            usage();
            return;
        }
        boolean bInteractive = false;
        if (args.length == 2) {
            System.out.println("Starting in interactive mode...");
            bInteractive = true;
        } else {
            System.out.println("Starting in bulk mode...");
        }
        File fi = new File(args[0]);
        if (!fi.isFile() || !fi.canRead()) {
            System.err.println("Cannot open \"" + args[0] + "\" for reading.");
            System.exit(1);
        }
        File fo = new File("CodeSetDatabase.java");
        if (fo.exists() && !fo.isFile()) {
            System.err.println("Unable to overwrite non-file CodeSetDatabase.java");
            return;
        }
        try {
            BufferedReader usr = new BufferedReader(new InputStreamReader(System.in));
            if (fo.isFile()) {
                if (bInteractive) {
                    System.out.print("Overwrite CodeSetDatabase.java? (yes): ");
                    System.out.flush();
                    if (usr.readLine().length() != 0) {
                        return;
                    }
                }
            }
            BufferedReader in = new BufferedReader(new FileReader(fi));
            Hashtable codesetIDDB = new java.util.Hashtable();
            Hashtable encodingToCodesetID = new java.util.Hashtable();
            Hashtable charsetIDToCodesetID = new java.util.Hashtable();
            Hashtable useCodeset = new java.util.Hashtable();
            String line;
            while ((line = in.readLine()) != null) {
                if (!line.equals("start")) {
                    continue;
                }
                System.out.println();
                System.out.println("-----------------------------------------------------------------------");
                line = in.readLine();
                System.out.println(line);
                String strDescription = line.substring("Short Description\t".length());
                line = in.readLine();
                System.out.println(line);
                int codesetID = Integer.parseInt(line.substring("Registered Value\t0x".length()), 16);
                line = in.readLine();
                System.out.println(line);
                java.util.ArrayList arrayList = new java.util.ArrayList();
                int lidx = line.indexOf("0x") - 1;
                int idx = line.indexOf(':', lidx);
                while (idx >= 0) {
                    arrayList.add(Short.decode(line.substring(lidx + 1, idx)));
                    lidx = idx;
                    idx = line.indexOf(':', lidx + 1);
                }
                arrayList.add(Short.decode(line.substring(lidx + 1)));
                short[] charsetIDs = new short[arrayList.size()];
                for (int i = 0; i < arrayList.size(); i++) {
                    charsetIDs[i] = ((Short) arrayList.get(i)).shortValue();
                }
                line = in.readLine();
                System.out.println(line);
                int iMaxSize = Integer.parseInt(line.substring("Max Bytes per Character\t".length()));
                while (!(line = in.readLine()).equals("end")) {
                    System.out.println(line);
                }
                System.out.println();
                CodeSet entry = CodeSetDatabase.getCodeSetFromId(codesetID);
                boolean bServer = false;
                String strEncoding = null;
                String strCanonical = null;
                int iAlignment = -1;
                if (entry != null && entry.getAlignment() >= 0) {
                    strEncoding = entry.getName();
                    iAlignment = entry.getAlignment();
                    try {
                        strCanonical = CodeSetDatabase.canonicalize(entry.getName());
                        if (bInteractive) {
                            System.out.print("Reuse old? encoding=\"" + entry.getName() + "\" align=" + entry.getAlignment() + " (yes): ");
                            if (usr.readLine().length() == 0) {
                                useCodeset.put(NumberCache.getInteger(codesetID), Boolean.TRUE);
                            } else {
                                useCodeset.put(NumberCache.getInteger(codesetID), Boolean.FALSE);
                            }
                        } else {
                            useCodeset.put(NumberCache.getInteger(codesetID), Boolean.TRUE);
                        }
                    } catch (UnsupportedEncodingException ex) {
                        if (bInteractive) {
                            System.out.print("Reuse old? unknown encoding=\"" + entry.getName() + "\" align=" + entry.getAlignment() + " (yes): ");
                            if (usr.readLine().length() == 0) {
                                useCodeset.put(NumberCache.getInteger(codesetID), Boolean.TRUE);
                            } else {
                                useCodeset.put(NumberCache.getInteger(codesetID), Boolean.FALSE);
                            }
                        }
                    }
                    System.out.flush();
                }
                Boolean useCS = (Boolean) useCodeset.get(NumberCache.getInteger(codesetID));
                if (bInteractive) {
                    if (useCS == null) {
                        while (true) {
                            System.out.print("Encoding name (blank to skip): ");
                            System.out.flush();
                            strEncoding = usr.readLine();
                            if (strEncoding.length() != 0) {
                                try {
                                    strCanonical = CodeSetDatabase.canonicalize(strEncoding);
                                    System.out.println("Canonical name: \"" + strCanonical + "\"");
                                    System.out.print("Store encoding? (yes): ");
                                    System.out.flush();
                                    iAlignment = discoverWidth(iMaxSize, strEncoding, -1, usr);
                                    if (usr.readLine().length() != 0 || iAlignment == 0) {
                                        useCodeset.put(NumberCache.getInteger(codesetID), Boolean.TRUE);
                                    }
                                } catch (UnsupportedEncodingException ex) {
                                    System.out.print("Unsupported encoding.Use anyhow? (yes),(a)gain,no: ");
                                    System.out.flush();
                                    line = usr.readLine();
                                    if (line.startsWith("a")) {
                                        continue;
                                    }
                                    iAlignment = discoverWidth(iMaxSize, strEncoding, -1, usr);
                                    if (usr.readLine().length() != 0 || iAlignment == 0) {
                                        useCodeset.put(NumberCache.getInteger(codesetID), Boolean.TRUE);
                                    } else {
                                        useCodeset.put(NumberCache.getInteger(codesetID), Boolean.FALSE);
                                    }
                                }
                                System.out.print("Use encoding in server profiles? (no): ");
                                System.out.flush();
                                bServer = (usr.readLine().length() != 0);
                            }
                            System.out.println();
                            break;
                        }
                        entry = new CodeSet(strDescription, codesetID, charsetIDs, iMaxSize, CodeSetDatabase.canonicalize(strEncoding), strEncoding, iAlignment, bServer);
                    }
                }
                boolean bUseCS = useCS != null ? useCS.booleanValue() : false;
                if (bUseCS) {
                    System.out.println("##### Adding the entry...");
                    codesetIDDB.put(NumberCache.getInteger(codesetID), entry);
                    java.util.ArrayList al = null;
                    for (int i = 0; i < charsetIDs.length; i++) {
                        al = (java.util.ArrayList) charsetIDToCodesetID.get(NumberCache.getShort(charsetIDs[i]));
                        if (al != null) {
                            al.add(NumberCache.getInteger(codesetID));
                        } else {
                            al = new java.util.ArrayList();
                            al.add(NumberCache.getInteger(codesetID));
                            charsetIDToCodesetID.put(NumberCache.getShort(charsetIDs[i]), al);
                        }
                    }
                    al = (java.util.ArrayList) encodingToCodesetID.get(strCanonical);
                    if (al != null) {
                        al.add(NumberCache.getInteger(codesetID));
                    } else {
                        al = new java.util.ArrayList();
                        al.add(NumberCache.getInteger(codesetID));
                        encodingToCodesetID.put(strCanonical, al);
                    }
                } else {
                    System.out.println("##### Skipping the entry...");
                }
                System.out.println("-----------------------------------------------------------------------");
                System.out.println();
            }
            PrintWriter pw = new PrintWriter(new FileWriter(fo));
            printDBToFile(pw, codesetIDDB, encodingToCodesetID, charsetIDToCodesetID);
        } catch (IOException ex) {
            System.err.println("IO Exception occoured");
        }
    }

    private static void printDBToFile(PrintWriter pw, Hashtable codesetIDDB, Hashtable encodingToCodesetID, Hashtable charsetIDToCodesetID) throws UnsupportedEncodingException {
        Comparator rev_comp = Collections.reverseOrder();
        pw.println("/*");
        pw.println("* Copyright (C) The Community OpenORB Project. All rights reserved.");
        pw.println("*");
        pw.println("* This software is published under the terms of The OpenORB Community " + "Software");
        pw.println("* License version 1.0, a copy of which has been included with this " + "distribution");
        pw.println("* in the LICENSE.txt file.");
        pw.println("*/");
        pw.println();
        pw.println("package org.openorb.orb.iiop;");
        pw.println();
        pw.println("import org.openorb.util.NumberCache;");
        pw.println();
        pw.println("/** CodeSetDatabase.java");
        pw.println(" * This class has been automatically generated on " + (new java.util.Date()) + ".");
        pw.println(" * Use \'java org.openorb.iiop.CodeSetDatabaseInitializer " + "src/main/org/openorb/orb/iiop/cs_registry1_2h.txt\'");
        pw.println(" * to generate it. DO NOT MODIFY MANUALLY !!!");
        pw.println(" *");
        pw.println(" * @author The CodeSetDatabaseInitializer tool.");
        pw.println(" */");
        pw.println("public class CodeSetDatabase");
        pw.println("{");
        pw.println();
        pw.println("    /**");
        pw.println("     * Convert an encoding name into it's canonical Java name.");
        pw.println("     */");
        pw.println("    public static String canonicalize( String encoding )");
        pw.println("        throws java.io.UnsupportedEncodingException");
        pw.println("    {");
        pw.println("        // Use this way to get the canonical encoding name.");
        pw.println("        // Internally the sun.io.Converters and");
        pw.println("        // sun.io.CharacterEncoding are used to convert the");
        pw.println("        // name. If we use these classes directly");
        pw.println("        // we would limit the number of supported JDKs to the");
        pw.println("        // Sun JDKs only. It isn't the most efficient way to");
        pw.println("        // create an OutputStreamWriter just for doing a String");
        pw.println("        // conversion, but JDKs before 1.4 provide no other ");
        pw.println("        // to get the canonical name.");
        pw.println("        java.io.OutputStreamWriter osw =" + " new java.io.OutputStreamWriter( System.out, encoding );");
        pw.println("        return osw.getEncoding();");
        pw.println("    }");
        pw.println();
        pw.println("    /**");
        pw.println("     * Populates the map between canonical codeset names");
        pw.println("     * and an ArrayList of codeset ids.");
        pw.println("     */");
        pw.println("    public static void populateNameToIdMap( java.util.HashMap map )");
        pw.println("    {");
        pw.println("        java.util.ArrayList al = null;");
        for (java.util.Enumeration e = encodingToCodesetID.keys(); e.hasMoreElements(); ) {
            String strEnc = (String) e.nextElement();
            pw.println();
            java.util.ArrayList al = (java.util.ArrayList) encodingToCodesetID.get(strEnc);
            Collections.sort(al, rev_comp);
            java.util.ListIterator li = al.listIterator();
            pw.println("        al = new java.util.ArrayList();");
            while (li.hasNext()) {
                pw.println("        al.add( NumberCache.getInteger( 0x" + HexPrintStream.toHex(((Integer) li.next()).intValue()) + " ) );");
            }
            pw.println("        map.put( \"" + CodeSetDatabase.canonicalize(strEnc).toUpperCase() + "\", al );");
        }
        pw.println("    }");
        pw.println();
        pw.println("    /**");
        pw.println("     * Return a CodeSet object for an OSF registry codeset id.");
        pw.println("     *");
        pw.println("     * @param id An OSF charset and codeset registry id.");
        pw.println("     * @return A CodeSet object.");
        pw.println("     */");
        pw.println("    public static CodeSet getCodeSetFromId( int id )");
        pw.println("    {");
        pw.println("        switch( id )");
        pw.println("        {");
        for (java.util.Enumeration e = codesetIDDB.keys(); e.hasMoreElements(); ) {
            Integer codesetID = (Integer) e.nextElement();
            CodeSet de = (CodeSet) codesetIDDB.get(codesetID);
            pw.println("            case 0x" + HexPrintStream.toHex(codesetID.intValue()) + ":");
            pw.println("              return new CodeSet( " + de + " );");
        }
        pw.println("            default:");
        pw.println("                return null;");
        pw.println("        }");
        pw.println("    }");
        pw.println();
        pw.println("    /**");
        pw.println("     * Return the alignment for an OSF registry codeset id.");
        pw.println("     * This method replaces the method CodeSet.getAlignmentFromId()");
        pw.println("     * and moves the functionality to the time when the class");
        pw.println("     * CodeSetDatabase is created from the OSF charset and");
        pw.println("     * codeset registry file.");
        pw.println("     *");
        pw.println("     * @param id An OSF charset and codeset registry id.");
        pw.println("     * @return The aligment for the specified codeset");
        pw.println("     * <ul>");
        pw.println("     * <li><code>-1</code>When the codeset id doesn't exist or the");
        pw.println("     * maximum size is 1, i.e.");
        pw.println("     * the codeset is a byte-oriented single-byte codeset</li>");
        pw.println("     * <li><code>0</code> When it is a byte-oriented multi-byte");
        pw.println("     * codeset</li>");
        pw.println("     * <li><code>&gt;= 1</code> When it is a fixed-length");
        pw.println("     * non-byte-oriented codeset (e.g. 2 for UCS codesets)</li>");
        pw.println("     * </ul>");
        pw.println("     */");
        pw.println("    public static int getAlignmentFromId( int id )");
        pw.println("    {");
        pw.println("        switch( id )");
        pw.println("        {");
        for (java.util.Enumeration e = codesetIDDB.keys(); e.hasMoreElements(); ) {
            Integer codesetID = (Integer) e.nextElement();
            CodeSet de = (CodeSet) codesetIDDB.get(codesetID);
            pw.println("            case 0x" + HexPrintStream.toHex(codesetID.intValue()) + ":");
            pw.println("                return " + de.getAlignment() + ";");
        }
        pw.println("            default:");
        pw.println("                return -1;");
        pw.println("        }");
        pw.println("    }");
        pw.println();
        pw.println("    /**");
        pw.println("     * Returns the name of a OSF charset and codeset registry entry.");
        pw.println("     *");
        pw.println("     * @param id An OSF charset and codeset registry id.");
        pw.println("     * @return The name of the codeset.");
        pw.println("     */");
        pw.println("    public static String getNameFromId( int id )");
        pw.println("    {");
        pw.println("        switch( id )");
        pw.println("        {");
        for (java.util.Enumeration e = codesetIDDB.keys(); e.hasMoreElements(); ) {
            Integer codesetID = (Integer) e.nextElement();
            CodeSet de = (CodeSet) codesetIDDB.get(codesetID);
            pw.println("            case 0x" + HexPrintStream.toHex(codesetID.intValue()) + ":");
            pw.println("                return \"" + de.getName() + "\";");
        }
        pw.println("            default:");
        pw.println("                return null;");
        pw.println("        }");
        pw.println("    }");
        pw.println();
        pw.println("    /**");
        pw.println("     * Returns the canonical Java name of a OSF charset");
        pw.println("     * and codeset registry entry.");
        pw.println("     *");
        pw.println("     * @param id An OSF charset and codeset registry id.");
        pw.println("     * @return The name of the codeset.");
        pw.println("     */");
        pw.println("    public static String getCanonicalNameFromId( int id )");
        pw.println("    {");
        pw.println("        switch( id )");
        pw.println("        {");
        for (java.util.Enumeration e = codesetIDDB.keys(); e.hasMoreElements(); ) {
            Integer codesetID = (Integer) e.nextElement();
            CodeSet de = (CodeSet) codesetIDDB.get(codesetID);
            pw.println("            case 0x" + HexPrintStream.toHex(codesetID.intValue()) + ":");
            pw.println("                return \"" + CodeSetDatabase.canonicalize(de.getName()) + "\";");
        }
        pw.println("            default:");
        pw.println("                return null;");
        pw.println("        }");
        pw.println("    }");
        pw.println();
        pw.println("    /**");
        pw.println("     * Returns the description of a OSF charset and codeset");
        pw.println("     * registry entry.");
        pw.println("     *");
        pw.println("     * @param id An OSF charset and codeset registry id.");
        pw.println("     * @return The OSF description of the codeset.");
        pw.println("     */");
        pw.println("    public static String getDescriptionFromId( int id )");
        pw.println("    {");
        pw.println("        switch( id )");
        pw.println("        {");
        for (java.util.Enumeration e = codesetIDDB.keys(); e.hasMoreElements(); ) {
            Integer codesetID = (Integer) e.nextElement();
            CodeSet de = (CodeSet) codesetIDDB.get(codesetID);
            pw.println("            case 0x" + HexPrintStream.toHex(codesetID.intValue()) + ":");
            pw.println("                return \"" + de.getDescription() + "\";");
        }
        pw.println("            default:");
        pw.println("                return null;");
        pw.println("        }");
        pw.println("    }");
        pw.println();
        pw.println("    /**");
        pw.println("     * Return an array of CodeSet objects that support the");
        pw.println("     * specified charset.");
        pw.println("     *");
        pw.println("     * @param id An OSF charset registry id.");
        pw.println("     * @return An array of codeset ids supporting the charset.");
        pw.println("     */");
        pw.println("    public static int[] getCodeSetsFromCharset( short charset )");
        pw.println("    {");
        pw.println("        switch( charset )");
        pw.println("        {");
        for (java.util.Enumeration e = charsetIDToCodesetID.keys(); e.hasMoreElements(); ) {
            Short charsetID = (Short) e.nextElement();
            java.util.ArrayList al = (java.util.ArrayList) charsetIDToCodesetID.get(charsetID);
            Collections.sort(al, rev_comp);
            java.util.ListIterator li = al.listIterator();
            pw.println("            case 0x" + HexPrintStream.toHex(charsetID.shortValue()) + ":");
            pw.println("                return new int[] {");
            while (li.hasNext()) {
                Integer codesetID = (Integer) li.next();
                pw.println("                    0x" + HexPrintStream.toHex(codesetID.intValue()) + (li.hasNext() ? ", " : ""));
            }
            pw.println("                };");
        }
        pw.println("            default:");
        pw.println("                return null;");
        pw.println("        }");
        pw.println("    }");
        pw.println("}");
        pw.flush();
    }

    /**
     * Try to find out the number of characters used for this encoding.
     *
     * @param maxSize Maximum value for the character width.
     * @param encoding Encoding for which to determine the character width.
     * @param prev ???.
     * @param usr ???.
     * @return Number of characters used in this encoding.
     */
    private static int discoverWidth(int maxSize, String encoding, int prev, BufferedReader usr) throws IOException {
        if (maxSize == 1) {
            return 1;
        }
        if (encoding == null) {
            System.out.print("Codeset width? 1, 2, 4, prefixed, (cancel): ");
            System.out.flush();
            String line = usr.readLine();
            if (line.equals("1")) {
                return 1;
            } else if (line.equals("2")) {
                return 2;
            } else if (line.equals("4")) {
                return 4;
            } else if (line.startsWith("p")) {
                return 0;
            } else {
                return -1;
            }
        }
        while (true) {
            switch(prev) {
                case 0:
                    System.out.print("Codeset width? 1, 2, 4, (prefixed), guess, RETURN: ");
                    break;
                case 1:
                    System.out.print("Codeset width? (1), 2, 4, prefixed, guess, RETURN: ");
                    break;
                case 2:
                    System.out.print("Codeset width? 1, (2), 4, prefixed, guess, RETURN: ");
                    break;
                case 4:
                    System.out.print("Codeset width? 1, 2, (4), prefixed, guess, RETURN: ");
                    break;
                default:
                    System.out.print("Codeset width? 1, 2, 4, prefixed, (guess), RETURN: ");
                    break;
            }
            String line = usr.readLine();
            if (line.length() == 0) {
                if (prev != -1) {
                    return prev;
                }
            } else {
                if (line.equals("1")) {
                    return 1;
                }
                if (line.equals("2")) {
                    return 2;
                }
                if (line.equals("4")) {
                    return 4;
                }
                if (line.startsWith("p")) {
                    return 0;
                }
                if (!line.startsWith("g")) {
                    return -1;
                }
            }
            System.out.println("Guessing char width");
            int[] len = new int[maxSize * 2 + 1];
            for (char c = Character.MIN_VALUE; c < Character.MAX_VALUE; ++c) {
                try {
                    ++len[String.valueOf(c).getBytes(encoding).length];
                } catch (Error ex) {
                    ++len[0];
                }
            }
            for (int i = 0; i < len.length; ++i) {
                if (i == 0) {
                    System.out.println("" + len[i] + " Errors.");
                } else {
                    if (len[i] > 0) {
                        System.out.println(len[i] + " of length " + i);
                    }
                }
            }
            System.out.println("String \"aa\" is length " + "aa".getBytes(encoding).length);
            if (len[1] != 0) {
                prev = (len[2] != 0) ? 0 : 1;
            }
        }
    }
}
