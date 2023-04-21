package org.ccnx.ccn.utils;

import java.util.logging.Level;
import org.ccnx.ccn.KeyManager;
import org.ccnx.ccn.impl.support.Log;
import org.ccnx.ccn.impl.support.Tuple;
import org.ccnx.ccn.protocol.KeyLocator;
import org.ccnx.ccn.protocol.PublisherPublicKeyDigest;

/**
 * Command line utility for listing stored identity data (key locators). These
 * are currently stored insecurely: TODO store as signed data.
 * 
 * NOTE: We don't actually expect users to use this tool to view their identities; their
 * identities (even in the simple sense in which we use that term now) will usually be managed
 * by software and users never see them. This tool is for testing and debugging.
 */
public class ccnwhoami {

    public static void usage() {
        System.err.println("usage: ccnwhoami [-q] [-as <pathToKeystore> [-name <friendly name]] (-q == quiet)");
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            if ((args.length >= 1) && (args[0].equals("-h"))) {
                usage();
                return;
            }
            int offset = 0;
            if ((args.length > 1) && (args[0].equals("-q"))) {
                Log.setDefaultLevel(Level.WARNING);
                offset++;
            }
            if (args.length - offset > 4) {
                usage();
                return;
            }
            Tuple<Integer, KeyManager> tuple = CreateUserData.keyManagerAs(args, offset);
            KeyManager km = ((null == tuple) || (null == tuple.second())) ? KeyManager.getDefaultKeyManager() : tuple.second();
            PublisherPublicKeyDigest[] ids = km.getAvailableIdentities();
            KeyLocator kl = null;
            System.out.println("Key ID					Identity");
            for (int i = 0; i < ids.length; ++i) {
                kl = km.getStoredKeyLocator(ids[i]);
                if (null != kl) {
                    System.out.println(ids[i] + "					" + kl);
                } else {
                    System.out.println(ids[i]);
                }
            }
            km.close();
            KeyManager.closeDefaultKeyManager();
        } catch (Exception e) {
            handleException("Error: cannot initialize device. ", e);
            System.exit(-3);
        }
    }

    protected static void handleException(String message, Exception e) {
        Log.warning(message + " Exception: " + e.getClass().getName() + ": " + e.getMessage());
        Log.warningStackTrace(e);
    }
}
