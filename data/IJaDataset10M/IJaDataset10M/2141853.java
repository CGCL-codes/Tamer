package org.hardtokenmgmt.core.token;

import iaik.pkcs.pkcs11.Session;
import iaik.pkcs.pkcs11.Slot;
import iaik.pkcs.pkcs11.State;
import iaik.pkcs.pkcs11.Token;
import iaik.pkcs.pkcs11.TokenException;
import iaik.pkcs.pkcs11.Session.UserType;
import iaik.pkcs.pkcs11.Token.SessionReadWriteBehavior;
import iaik.pkcs.pkcs11.Token.SessionType;
import iaik.pkcs.pkcs11.wrapper.PKCS11Exception;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import org.hardtokenmgmt.core.log.LocalLog;
import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * <p>
 * Class in charge on creating the native calls to the NetId pkcs11 dll.
 * </p>
 * 
 * <p>
 * Contains two methods that needs to be called outside standard PKCS11:
 * UnblockPIN and InitToken because of difference in the PKCS11 standard
 * and the EID card usage.
 * </p>
 *
 * 
 * 
 * @author Philip Vendil 19 dec 2007
 *
 * @version $Id$
 */
public class NetIdConnector {

    private static final String NETID_DDL = "iidp11.dll";

    /**
	 * JNA interface towards the eToken.dll
	 * 
	 * @author Philip Vendil 18 nov 2010
	 *
	 * @version $Id$
	 */
    private interface NetIdLibrary extends Library {

        NetIdLibrary INSTANCE = (NetIdLibrary) Native.loadLibrary(NETID_DDL, NetIdLibrary.class);

        int C_UnlockPIN(int sessionId, byte[] pUKData, int pUKDataLength, byte[] pINData, int pINDataLength);

        int C_UnblockPIN(int sessionId, byte[] pINData, int pINDataLength, byte[] pUKData, int pUKDataLength);

        int C_InitToken(int slotId, byte[] pUKData, int pUKDataLength, String profile);
    }

    public static void unBlockPIN(Session session, Slot slot, String newPIN, String pUK) throws TokenException {
        try {
            boolean isAboveVersion5 = isNetIdAboveVersion5(slot);
            byte[] pINData = newPIN.getBytes("UTF-8");
            byte[] pUKData = pUK.getBytes("UTF-8");
            int sessionId = (int) session.getSessionHandle();
            int retval;
            if (isAboveVersion5) {
                retval = NetIdLibrary.INSTANCE.C_UnlockPIN(sessionId, pUKData, pUKData.length, pINData, pINData.length);
            } else {
                retval = NetIdLibrary.INSTANCE.C_UnblockPIN(sessionId, pINData, pINData.length, pUKData, pUKData.length);
            }
            if (retval != 0) {
                throw new TokenException("Error when calling NetId unblock PIN method, errorcode : " + retval);
            }
        } catch (UnsupportedEncodingException e) {
            LocalLog.getLogger().log(Level.SEVERE, "Error performing NetId unblockPIN call : " + e.getMessage(), e);
            throw new TokenException(e.getMessage(), e);
        }
    }

    public static void initToken(Slot slot, String profile, String tokenLabel, String pUK, String sOPIN) throws TokenException {
        try {
            editIIDIni(pUK, tokenLabel, profile);
            byte[] sOPINData = sOPIN.getBytes("UTF-8");
            int result = NetIdLibrary.INSTANCE.C_InitToken((int) slot.getSlotID(), sOPINData, sOPINData.length, profile);
            editIIDIni("", "", profile);
            if (result != 0) {
                throw new TokenException("NetId initialization failed: Errorcode : " + new PKCS11Exception(result).getMessage());
            }
        } catch (UnsupportedEncodingException e) {
            LocalLog.getLogger().log(Level.SEVERE, "Error performing NetId initToken call : " + e.getMessage(), e);
            throw new TokenException(e.getMessage(), e);
        }
    }

    public static void initTokenWithUserPIN(Token token, String profile, String userPIN) throws TokenException {
        Session session = null;
        try {
            session = token.openSession(SessionType.SERIAL_SESSION, SessionReadWriteBehavior.RW_SESSION, null, null);
            if (!session.getSessionInfo().getState().toString().equals(State.RW_USER_FUNCTIONS.toString())) {
                session.login(UserType.USER, userPIN.toCharArray());
            }
            byte[] sOPINData = "".getBytes("UTF-8");
            int result = NetIdLibrary.INSTANCE.C_InitToken((int) token.getSlot().getSlotID(), sOPINData, sOPINData.length, profile);
            if (result != 0) {
                throw new TokenException("NetId user PIN initialization failed: Errorcode : " + new PKCS11Exception(result).getMessage());
            }
        } catch (UnsupportedEncodingException e) {
            LocalLog.getLogger().log(Level.SEVERE, "Error performing NetId initToken call : " + e.getMessage(), e);
            throw new TokenException(e.getMessage(), e);
        } finally {
            if (session != null) {
                session.closeSession();
            }
        }
    }

    private static void editIIDIni(String pUK, String label, String profile) {
        try {
            String inipath = getINIPath();
            String section = "[" + profile + "]";
            boolean sectionNotFound = false;
            FileInputStream fis = new FileInputStream(inipath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = 0;
            while ((b = fis.read()) != -1) {
                baos.write(b);
            }
            fis.close();
            FileOutputStream fos = new FileOutputStream(inipath);
            PrintWriter out = new PrintWriter(fos);
            BufferedReader br = new BufferedReader(new StringReader(new String(baos.toByteArray(), "UTF-8")));
            String line = "";
            while ((line = br.readLine()) != null) {
                out.println(line);
                if (line.trim().equals(section)) {
                    break;
                }
            }
            if (line == null) {
                sectionNotFound = true;
            }
            boolean pukExisted = false;
            boolean labelExisted = false;
            do {
                line = br.readLine();
                if (line != null) {
                    if (line.trim().startsWith("[")) {
                        break;
                    } else if (line.startsWith("Puk")) {
                        pukExisted = true;
                        out.println("Puk=" + pUK);
                    } else if (line.startsWith("Label")) {
                        labelExisted = true;
                        out.println("Label=" + label);
                    } else {
                        out.println(line);
                    }
                }
            } while (line != null);
            if (!pukExisted && !sectionNotFound) {
                out.println("Puk=" + pUK);
            }
            if (!labelExisted && !sectionNotFound) {
                out.println("Label=" + label);
                out.println("");
            }
            if (line != null && line.startsWith("[")) {
                out.println(line);
            }
            while ((line = br.readLine()) != null) {
                out.println(line);
            }
            if (sectionNotFound) {
                out.println("");
                out.println(section);
                out.println("Puk=" + pUK);
                out.println("Label=" + label);
                out.println("");
            }
            out.flush();
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            LocalLog.getLogger().log(Level.SEVERE, "Error accessing c:\\windows\\iid.ini when trying to initialize card", e);
        } catch (IOException e) {
            LocalLog.getLogger().log(Level.SEVERE, "Error accessing c:\\windows\\iid.ini when trying to initialize card", e);
        }
    }

    public static void editIIDIniEntry(String key, String value, String profile) {
        try {
            String inipath = getINIPath();
            String section = "[" + profile + "]";
            boolean sectionNotFound = false;
            FileInputStream fis = new FileInputStream(inipath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = 0;
            while ((b = fis.read()) != -1) {
                baos.write(b);
            }
            fis.close();
            FileOutputStream fos = new FileOutputStream(inipath);
            PrintWriter out = new PrintWriter(fos);
            BufferedReader br = new BufferedReader(new StringReader(new String(baos.toByteArray(), "UTF-8")));
            String line = "";
            while ((line = br.readLine()) != null) {
                out.println(line);
                if (line.trim().equals(section)) {
                    break;
                }
            }
            if (line == null) {
                sectionNotFound = true;
            }
            boolean entryExisted = false;
            do {
                line = br.readLine();
                if (line != null) {
                    if (line.trim().startsWith("[")) {
                        break;
                    } else if (line.startsWith(key)) {
                        entryExisted = true;
                        out.println(key + "=" + value);
                    } else {
                        out.println(line);
                    }
                }
            } while (line != null);
            if (!entryExisted && !sectionNotFound) {
                out.println(key + "=" + value);
            }
            if (line != null && line.startsWith("[")) {
                out.println(line);
            }
            while ((line = br.readLine()) != null) {
                out.println(line);
            }
            if (sectionNotFound) {
                out.println("");
                out.println(section);
                out.println(key + "=" + value);
                out.println("");
            }
            out.flush();
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            LocalLog.getLogger().log(Level.SEVERE, "Error accessing c:\\windows\\iid.ini when trying to initialize card", e);
        } catch (IOException e) {
            LocalLog.getLogger().log(Level.SEVERE, "Error accessing c:\\windows\\iid.ini when trying to initialize card", e);
        }
    }

    private static String getINIPath() throws IOException {
        String inipath = null;
        if (System.getenv("APPDATA") != null) {
            inipath = getIniOrCfg(System.getenv("APPDATA") + "\\iid\\iid");
        } else {
            inipath = getIniOrCfg(System.getProperty("user.home") + "\\Application Data\\iid\\iid");
        }
        if (System.getProperty("os.name").equals("Windows Vista")) {
            inipath = getIniOrCfg(System.getProperty("user.home") + "\\AppData\\Roaming\\iid\\iid");
        }
        File f = new File(inipath);
        File directoryName = f.getParentFile();
        if (!directoryName.exists()) {
            directoryName.mkdir();
        }
        if (!f.exists()) {
            f.createNewFile();
        }
        return inipath;
    }

    /**
	 * Method that tries to find both .cfg and .ini files.
	 * 
	 * @param pathWithoutPostfiz the full path with out .ini or .cfg
	 * @return the full path if found.
	 */
    private static String getIniOrCfg(String pathWithoutPostfiz) {
        if ((new File(pathWithoutPostfiz + ".ini")).exists()) {
            return pathWithoutPostfiz + ".ini";
        }
        return pathWithoutPostfiz + ".cfg";
    }

    private static boolean isNetIdAboveVersion5(Slot slot) throws TokenException {
        byte majorVersion = slot.getModule().getInfo().getLibraryVersion().getMajor();
        return majorVersion >= 5;
    }
}
