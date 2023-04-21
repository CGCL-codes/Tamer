package org.tcpfile.net.p2p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcpfile.crypto.CryptServerAccessor;
import org.tcpfile.main.Misc;
import org.tcpfile.net.Connection;
import org.tcpfile.net.SendableObject;
import org.tcpfile.utils.SynchronizedSoftHashMap;

/**
 * This is the super class of all Descriptors.<br>
 * It just contains basic information about a file, by which it is
 * identifiable over the FileIdentifiere.
 * It just stores the hash and the size.<br>
 * As a Sendable it is sent around to request a File.<br>
 * @author Stivo
 *
 */
public class FileDescriptor extends SendableObject {

    private static final long serialVersionUID = -9008524790914022319L;

    private static Logger log = LoggerFactory.getLogger(FileDescriptor.class);

    public static final long BLOCKSIZE = 512 * 1024;

    public static final SynchronizedSoftHashMap<String, SQLFileDescriptor> sqlfds = new SynchronizedSoftHashMap<String, SQLFileDescriptor>(10);

    public static final SynchronizedSoftHashMap<String, SubHashFileDescriptor> shfds = new SynchronizedSoftHashMap<String, SubHashFileDescriptor>(5);

    public String hash;

    public long size = -10;

    public FileDescriptor() {
        super();
    }

    public FileDescriptor(FileIdentifier fi) {
        this(fi.hash, fi.size);
    }

    public FileDescriptor(String hash, long size) {
        super();
        this.hash = hash;
        this.size = size;
    }

    public String toString() {
        String returns = super.toString();
        returns += "hash " + hash + " and Size " + size;
        return returns;
    }

    public static String convertPathToMultiPlatform(String input) {
        if (Misc.isWindows) input = input.replace("\\", "/");
        return input;
    }

    public void handle(final Connection con) {
        log.trace("Entering");
        Runnable r = new Runnable() {

            public void run() {
                try {
                    SubHashFileDescriptor sh = getFileIdentifier().getSubHashFileDescriptor(true, true, true);
                    if (!sh.sent) {
                        sh.sent = true;
                        sh.addToSendQueue(con.contact);
                    }
                } catch (RuntimeException e) {
                    log.info("Friend {} requested file {} which is not here", con.contact, getFileIdentifier());
                }
            }
        };
        CryptServerAccessor.addBackgroundWork(r);
    }

    public FileIdentifier getFileIdentifier() {
        log.trace("Entering");
        return new FileIdentifier(hash, size);
    }

    public int getPriority() {
        return -50;
    }

    public String getLocalPath() {
        throw new RuntimeException("Has to be overridden");
    }
}
