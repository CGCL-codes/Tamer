package ca.uwaterloo.fydp.xcde;

import ca.uwaterloo.fydp.ossp.impl.OSSPImpl;
import ca.uwaterloo.fydp.ossp.std.*;
import ca.uwaterloo.fydp.ossp.*;
import java.io.*;

public class XCDEServer {

    public static final int DEFAULT_PORT = 48879;

    public static final void main(String[] args) throws Exception {
        int port = DEFAULT_PORT;
        File store = null;
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equalsIgnoreCase("-p")) {
                if (++i < args.length) {
                    port = Integer.parseInt(args[i]);
                } else {
                    System.err.println("Option '-p' requires argument.");
                }
            } else if (args[i].equalsIgnoreCase("-s")) {
                if (++i < args.length) {
                    store = new File(args[i]);
                } else {
                    System.err.println("Option '-s' requires argument.");
                }
            } else {
                System.err.println("Unrecognized option: " + args[i]);
            }
        }
        OSSPState initialState = null;
        if (store == null || !store.exists()) {
            OSSPDirectory dir = new XCDERootDirectory();
            dir = (OSSPDirectory) (new OSSPDirectoryStimulusInitiateCreate(new OSSPDirectoryElement(".xcdechat.log", new XCDEDocument()))).apply(dir);
            initialState = dir;
        }
        OSSPImpl.getInstance().launchTCPServer(initialState, store, port);
    }
}
