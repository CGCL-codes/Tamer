package com.ibm.aglets.tahiti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TahitiDaemonClient {

    private static boolean _verbose = false;

    private static boolean _dont_connect = false;

    private static int _control_port_num = 5545;

    private static String hostname;

    private static boolean _banner_version_OK = false;

    private static int _socket_timeout = 10000;

    private static String _banner_string = "TahitiDaemon";

    private static String _version_string = "1.0";

    private static String helpMsg = "help                    Display this message. \n" + "quit                    Disconnect from the server and quit. \n" + "shutdown                Shutdown the server and quit. \n" + "reboot                  Reboot the server and quit. \n" + "list                    List all aglets in the server. \n" + "msg on|off              Turns message printing on/off, default is off. \n" + "debug on|off            Debug output on/off, default is off. \n" + "create [codeBase] name  Create new aglet. \n" + "<aglet> dispatch URL    Dispatch the aglet to the URL. \n" + "<aglet> clone           Clone the aglet. \n" + "<aglet> dispose         Dispose the aglet. \n" + "<aglet> dialog          Request a dialog to interact with.\n" + "<aglet> property        Display properties of the aglet.\n" + "Note: <aglet> is a left most string listed in the result of list command. ";

    public static void main(String[] args) throws IOException {
        String prompt = ">";
        Socket clientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        parseArgs(args);
        if (_dont_connect) System.exit(1);
        try {
            clientSocket = new Socket(hostname, _control_port_num);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientSocket.setSoTimeout(_socket_timeout);
        } catch (UnknownHostException e) {
            System.err.println("\nUnknown host: " + hostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("\nCouldn't get I/O for the connection to: " + hostname);
            System.exit(1);
        }
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer = "start";
        String fromUser;
        Thread.currentThread().setPriority(1);
        try {
            fromServer = in.readLine();
        } catch (IOException ex) {
            System.err.println("\nFailure reading from: " + hostname + ":" + _control_port_num);
            if (_verbose) {
                ex.printStackTrace();
            }
            System.exit(1);
        }
        try {
            if (fromServer != null) {
                if (_verbose) {
                    System.out.println("Server banner: " + fromServer);
                }
                if (fromServer.equals(_banner_string)) {
                    if (_verbose) {
                        System.out.println("Banner check OK.");
                    }
                    if ((fromServer = in.readLine()) != null) {
                        if (_verbose) {
                            System.out.println("Server version: " + fromServer);
                        }
                        if (fromServer.equals(_version_string)) {
                            if (_verbose) {
                                System.out.println("Version check OK.");
                            }
                            _banner_version_OK = true;
                        } else {
                            System.out.println("Version mismatch.");
                            System.out.println("Client version is: " + _version_string);
                            System.out.println("Server version is: " + fromServer);
                        }
                    }
                } else {
                    System.out.println("Banner check failed.");
                    System.out.println("Client banner is: " + _banner_string);
                    System.out.println("Server banner is: " + fromServer);
                }
            }
        } catch (IOException ex) {
            System.err.println("\nFailure reading from: " + hostname + ":" + _control_port_num);
            if (_verbose) {
                ex.printStackTrace();
            }
            System.exit(1);
        }
        if (_banner_version_OK) {
            try {
                if ((fromServer = in.readLine()) != null) {
                    if (_verbose) {
                        System.out.println("Server: " + fromServer);
                    }
                }
            } catch (IOException ex) {
                System.err.println("\nFailure reading from: " + hostname + ":" + _control_port_num);
                if (_verbose) {
                    ex.printStackTrace();
                }
                System.exit(1);
            }
            while (true) {
                try {
                    System.out.print(prompt + " ");
                    System.out.flush();
                    fromUser = stdIn.readLine();
                    if (fromUser != null) {
                        if (_verbose) System.out.println("Client: " + fromUser);
                        if ("help".equalsIgnoreCase(fromUser)) {
                            System.out.println(helpMsg);
                            System.out.flush();
                        }
                        if ("quit".equalsIgnoreCase(fromUser)) {
                            System.out.println("Closing client connection");
                            System.out.flush();
                            break;
                        }
                        out.println(fromUser);
                    }
                    fromServer = in.readLine();
                    if (_verbose) {
                        System.out.println("Server: " + fromServer);
                    }
                    if (fromServer.equals("shutting down")) {
                        System.out.println("Server shutting down, closing client connection.");
                        break;
                    } else if (fromServer.equals("rebooting")) {
                        System.out.println("Server rebooting, closing client connection.");
                        break;
                    } else {
                        System.out.println(fromServer);
                        while (!((fromServer = in.readLine()).equals("done."))) {
                            System.out.println(fromServer);
                        }
                    }
                } catch (IOException ex) {
                    System.err.println("\nFailure reading from: " + hostname + ":" + _control_port_num);
                    if (_verbose) {
                        ex.printStackTrace();
                    }
                    break;
                } catch (Throwable ex) {
                    if (_verbose) {
                        ex.printStackTrace();
                    }
                    break;
                }
            }
        }
        out.close();
        in.close();
        stdIn.close();
        clientSocket.close();
        System.exit(1);
    }

    private static void usage() {
        System.err.println("\nTahitiDaemonClient [-verbose] [-host <hostname>] [-controlport <port>] [-help]\n");
        System.err.println("Connects to a Tahiti Daemon and provides an interface to control it.\n");
        System.err.println("options:");
        System.err.println("    -verbose         verbose output");
        System.err.println("    -host <hostname>, default is localhost");
        System.err.println("    -controlport <port number>, default is port " + _control_port_num);
        System.err.println("    -timeout <timeout>, in milliseconds, 0 (zero) is infinite, default is " + _socket_timeout);
        System.err.println("    -help            prints this message");
        _dont_connect = true;
    }

    private static void parseArgs(String[] args) {
        if (_verbose) System.out.println("Parsing Args...\n");
        if (args.length <= 0) return;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-help")) {
                usage();
                break;
            } else if (args[i].equalsIgnoreCase("-verbose")) {
                _verbose = true;
            } else if (args[i].equalsIgnoreCase("-controlport")) {
                if (i + 1 >= args.length) {
                    usage();
                    break;
                }
                i++;
                try {
                    _control_port_num = Integer.parseInt(args[i]);
                } catch (NumberFormatException ex) {
                    System.err.println("\nError! controlport <" + args[i] + "> is invalid, it  must be an integer ");
                    _dont_connect = true;
                    break;
                }
            } else if (args[i].equalsIgnoreCase("-timeout")) {
                if (i + 1 >= args.length) {
                    usage();
                    break;
                }
                i++;
                try {
                    _socket_timeout = Integer.parseInt(args[i]);
                } catch (NumberFormatException ex) {
                    System.err.println("\nError! timeout <" + args[i] + "> is invalid, it  must be an integer ");
                    _dont_connect = true;
                    break;
                }
            } else if (args[i].equalsIgnoreCase("-host")) {
                if (i + 1 >= args.length) {
                    System.err.println("\nError! hostname was not specified after -host argument");
                    usage();
                    break;
                }
                i++;
                hostname = args[i];
            } else {
                System.err.println("\nUnknown argument: " + args[i] + "\n");
                usage();
                break;
            }
        }
    }
}
