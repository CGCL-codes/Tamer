package com.clanwts.bncs.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * This is the main Class that provides hashing functions,
 * CheckRevision, Exe Info, etc provided by BNLS.
 *
 * Static Methods Allow it to be accessible by any thread
 *
 * @throws HashException - If error caused by Hashing or retrieval (bad key, etc)
 *
 */
public class HashMain {

    private static final Logger log = LoggerFactory.getLogger(HashMain.class);

    public static int CRevChecks[] = new int[0x0B];

    public static int getVerByte(int prod) {
        if (prod <= 0) return 0;
        if (prod > Constants.prods.length + 1) return 0;
        log.debug("JBLS", ">>> [" + Constants.prods[prod - 1] + "] Verbyte");
        return Constants.IX86verbytes[prod - 1];
    }

    public static CheckrevisionResults getChecksum(int prod, String formula, int dll) {
        return getChecksum(prod, formula, dll, System.getProperty("user.dir"));
    }

    public static CheckrevisionResults getChecksum(int prod, String formula, int dll, String filepath) {
        return getRevision(prod, formula, "ver-IX86-" + dll + ".mpq", Constants.PLATFORM_INTEL, 2, filepath);
    }

    public static CheckrevisionResults getRevision(int prod, String formula, String dll, byte platform, int ver) {
        return getRevision(prod, formula, dll, platform, ver, System.getProperty("user.dir"));
    }

    public static CheckrevisionResults getRevision(int prod, String formula, String dll, byte platform, int ver, String filepath) {
        String[] files = null;
        try {
            CRevChecks[prod - 1]++;
            log.info(">>> [" + Constants.prods[prod - 1] + "] Version Check V" + ver);
            switch(ver) {
                case 1:
                    files = CheckRevisionV1.getFiles(prod, platform, filepath);
                    return CheckRevisionV1.checkRevision(formula, prod, platform, dll, filepath);
                case 2:
                    files = CheckRevisionV2.getFiles(prod, platform, filepath);
                    return CheckRevisionV2.checkRevision(formula, prod, platform, dll, filepath);
                default:
                    files = CheckRevisionV2.getFiles(prod, platform, filepath);
                    return CheckRevisionV2.checkRevision(formula, prod, platform, dll, filepath);
            }
        } catch (FileNotFoundException e) {
            log.error("Hash Exception(version check): \n\r" + "[CheckRevision] Files Not Found/Accessible (" + Constants.prods[prod - 1] + ") (" + files[0] + ", " + files[1] + ", " + files[2] + ")");
        } catch (IOException e) {
            log.error("Hash Exception(version check): [CheckRevision] IOException (" + Constants.prods[prod - 1] + ")");
        }
        return null;
    }

    public static CheckrevisionResults getRevision(int prod, String formula, String dll, long filetime, String filepath) {
        if (dll.matches("ver-IX86-[0-7].mpq") == true) return getRevision(prod, formula, dll, Constants.PLATFORM_INTEL, 2, filepath);
        if (dll.matches("ver-XMAC-[0-7].mpq") == true) return getRevision(prod, formula, dll, Constants.PLATFORM_MACOSX, 2, filepath);
        if (dll.matches("ver-PMAC-[0-7].mpq") == true) return getRevision(prod, formula, dll, Constants.PLATFORM_POWERPC, 2, filepath);
        if (dll.matches("IX86ver[0-7].mpq") == true) return getRevision(prod, formula, dll, Constants.PLATFORM_INTEL, 1, filepath);
        if (dll.matches("XMACver[0-7].mpq") == true) return getRevision(prod, formula, dll, Constants.PLATFORM_MACOSX, 1, filepath);
        if (dll.matches("PMACver[0-7].mpq") == true) return getRevision(prod, formula, dll, Constants.PLATFORM_POWERPC, 1, filepath);
        if (dll.matches("lockdown-IX86-[0-1][0-9].mpq") == true) return getRevision(prod, formula, dll, Constants.PLATFORM_INTEL, 3, filepath);
        log.warn("Unknown archive: " + dll + ", Filetime: 0x" + Long.toHexString(filetime));
        return null;
    }

    public static CheckrevisionResults getRevision(int prod, String formula, String dll, long filetime) {
        return getRevision(prod, formula, dll, filetime, System.getProperty("user.dir"));
    }
}
