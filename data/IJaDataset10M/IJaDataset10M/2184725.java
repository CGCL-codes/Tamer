package org.ccnx.ccn.utils;

import java.io.IOException;
import java.util.logging.Level;
import org.ccnx.ccn.CCNHandle;
import org.ccnx.ccn.KeyManager;
import org.ccnx.ccn.config.SystemConfiguration;
import org.ccnx.ccn.impl.CCNFlowControl.SaveType;
import org.ccnx.ccn.impl.support.Log;
import org.ccnx.ccn.io.content.Header.HeaderObject;
import org.ccnx.ccn.profiles.VersioningProfile;
import org.ccnx.ccn.profiles.metadata.MetadataProfile;
import org.ccnx.ccn.protocol.ContentName;
import org.ccnx.ccn.protocol.ContentObject;
import org.ccnx.ccn.protocol.MalformedContentNameStringException;

@Deprecated
public class updateheader {

    public static void usage() {
        System.out.println("usage: updateheader [-log level] <ccnname> [<ccnname>*>]\n Assumes content is in a repository.");
    }

    public static void moveHeader(String ccnxName, CCNHandle handle) throws MalformedContentNameStringException, IOException {
        ContentName fileName = ContentName.parse(ccnxName);
        ContentName fileVersionedName = null;
        if (VersioningProfile.hasTerminalVersion(fileName)) {
            fileVersionedName = fileName;
        } else {
            ContentObject fileObject = VersioningProfile.getLatestVersion(fileName, null, SystemConfiguration.getDefaultTimeout(), null, handle);
            if (null == fileObject) {
                System.out.println("Cannot find file " + fileName + " to update. Skipping.");
                return;
            }
            fileVersionedName = fileObject.name().subname(0, fileName.count() + 1);
        }
        HeaderObject newHeader = new HeaderObject(MetadataProfile.headerName(fileVersionedName), null, handle);
        newHeader.updateInBackground();
        HeaderObject oldHeader = new HeaderObject(MetadataProfile.oldHeaderName(fileVersionedName), null, handle);
        oldHeader.updateInBackground();
        oldHeader.waitForData(SystemConfiguration.getDefaultTimeout());
        if (!oldHeader.available()) {
            System.out.println("No old-style header found. Skipping " + ccnxName);
            oldHeader.cancelInterest();
            newHeader.cancelInterest();
            return;
        }
        if (newHeader.available()) {
            System.out.println("Already have a new header: " + newHeader.getVersionedName() + ", skipping file " + ccnxName);
        } else {
            newHeader.cancelInterest();
            newHeader.setupSave(SaveType.REPOSITORY);
            newHeader.save(oldHeader.header());
            newHeader.close();
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        int arg = 0;
        if ((args.length == 0) || ((args.length >= 1) && ((args[0].equals("--help")) || (args[0].equals("-h"))))) {
            usage();
            System.exit(1);
        }
        if ((args.length > 2) && (args[0].equals("-log"))) {
            Log.setDefaultLevel(Level.parse(args[1]));
            arg += 2;
        }
        CCNHandle handle = CCNHandle.getHandle();
        for (int i = arg; i < args.length; ++i) {
            try {
                moveHeader(args[i], handle);
            } catch (Exception e) {
                System.out.println("Exception processing file " + args[i] + ": " + e);
                e.printStackTrace();
            }
        }
        handle.close();
        KeyManager.closeDefaultKeyManager();
    }
}
