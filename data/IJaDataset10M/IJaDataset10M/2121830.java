package com.donaudymunch.webpaj.server;

import java.io.*;
import com.donaudymunch.webpaj.util.*;

public class ServerDaemon {

    /** initBeanFromSer() method reads in seriazed bean file and 
   *  intializes a bean
   *  @param String fileName the uri of the serialized bean file
   *  @return ServerBean
   */
    public static ServerBean initBeanFromSer(String fileName) {
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileName));
            ServerBean sBean = (ServerBean) is.readObject();
            is.close();
            return sBean;
        } catch (IOException ioe) {
            System.err.println("There was an IO exception " + ioe.getMessage());
            return null;
        } catch (ClassNotFoundException cnfe) {
            System.err.println("ClassNotFoundException " + cnfe.getMessage());
            return null;
        }
    }

    /** initBeanFromXML() method reads in xml file, parses and 
     *  intializes a bean.  It also stores the bean as a serizlized
     *  bean file (.ser) in the same directory with the same name  
     *  @param String fileName the uri of the serialized bean file
     *  @return ServerBean
     */
    public static ServerBean initBeanFromXML(String fileName) {
        ServerConfig sConf = new ServerConfig(fileName);
        ServerBean sBean = sConf.createServerBean();
        if (sBean == null) return null; else return sBean;
    }

    /** run() method uses the serverBean to initialize execution
     * @param serverBean the configuration needed to run a test
     */
    public static void runServerDaemon(ServerBean sBean) {
        ConnectionManager cm = new ConnectionManager(sBean);
        cm.run();
    }

    /**
     * the serverDaemon main function.  Reads command line arguments
     * and determines if called correctly.  If the user supplied 
     * a config file the function initializes the daemon process
     * @param args the command line argument.
     */
    public static void main(String args[]) {
        Arguments argopt = new Arguments();
        argopt.setUsage(new String[] { "usage: java webpaj (options) <Config file uri>", "options:", "-h  view this usage statement", "-s  config file is in serialized bean format" });
        if (args.length == 0) {
            argopt.printUsage();
            System.exit(1);
        }
        argopt.parseArgumentTokens(args, new char[] {});
        int c;
        String arg = null;
        ServerBean sBean = new ServerBean();
        int serializedBeanSwitch = 0;
        arg = argopt.getlistFiles();
        while ((c = argopt.getArguments()) != -1) {
            switch(c) {
                case 's':
                    if (arg == null) {
                        System.out.println("no configuration file specified");
                        System.exit(1);
                    }
                    System.out.println("Using Serialized Bean File: " + arg);
                    sBean = initBeanFromSer(arg);
                    serializedBeanSwitch = 1;
                    break;
                case 'h':
                    argopt.printUsage();
                    System.exit(1);
                    break;
                case '-':
                    argopt.printUsage();
                    System.exit(1);
                    break;
                case -1:
                    argopt.printUsage();
                    System.exit(1);
                default:
                    break;
            }
        }
        if (serializedBeanSwitch == 0) {
            if (arg == null) {
                System.out.println("no configuration file specified");
                System.exit(1);
            }
            System.out.println("Using XML Config File: " + arg);
            sBean = initBeanFromXML(arg);
        }
        if (sBean == null) {
            System.out.println("Couldn't run program, due to illformed" + " config file");
            System.exit(1);
        } else {
            runServerDaemon(sBean);
        }
    }
}
