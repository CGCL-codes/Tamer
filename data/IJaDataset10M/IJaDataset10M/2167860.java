package tcl.lang;

import java.util.*;
import java.io.IOException;

/**
 * This class implements the built-in "socket" command in Tcl.
 */
class SocketCmd implements Command {

    private static final String validCmds[] = { "-async", "-myaddr", "-myport", "-server" };

    static final int OPT_ASYNC = 0;

    static final int OPT_MYADDR = 1;

    static final int OPT_MYPORT = 2;

    static final int OPT_SERVER = 3;

    /**
     * This procedure is invoked to process the "socket" Tcl command.
     * See the user documentation for details on what it does.
     *
     * @param interp the current interpreter.
     * @param argv command arguments.
     */
    public void cmdProc(Interp interp, TclObject argv[]) throws TclException {
        boolean server = false;
        boolean async = false;
        String channelId = "";
        String myaddr = "";
        String script = "";
        String host = "";
        int myport = 0;
        int port = 0;
        int index;
        int i;
        for (i = 1; (i < argv.length); i++) {
            if ((argv[i].toString().length() > 0) && (argv[i].toString().charAt(0) == '-')) {
                index = TclIndex.get(interp, argv[i], validCmds, "option", 0);
            } else {
                break;
            }
            switch(index) {
                case OPT_ASYNC:
                    {
                        if (server) {
                            throw new TclException(interp, "cannot set -async option for server sockets");
                        }
                        async = true;
                        break;
                    }
                case OPT_MYADDR:
                    {
                        i++;
                        if (i >= argv.length) {
                            throw new TclException(interp, "no argument given for -myaddr option");
                        }
                        myaddr = argv[i].toString();
                        break;
                    }
                case OPT_MYPORT:
                    {
                        i++;
                        if (i >= argv.length) {
                            throw new TclException(interp, "no argument given for -myport option");
                        }
                        myport = getPort(interp, argv[i]);
                        break;
                    }
                case OPT_SERVER:
                    {
                        if (async) {
                            throw new TclException(interp, "cannot set -async option for server sockets");
                        }
                        server = true;
                        i++;
                        if (i >= argv.length) {
                            throw new TclException(interp, "no argument given for -server option");
                        }
                        script = argv[i].toString();
                        break;
                    }
                default:
                    {
                        throw new TclException(interp, "bad option \"" + argv[i] + "\", must be -async, -myaddr, -myport," + " or -server");
                    }
            }
        }
        if (server) {
            host = myaddr;
            if (myport != 0) {
                throw new TclException(interp, "Option -myport is not valid for servers");
            }
        } else if ((i + 1) < argv.length) {
            host = argv[i].toString();
            i++;
        } else {
            errorWrongNumArgs(interp, argv[0].toString());
        }
        if (i == argv.length - 1) {
            port = getPort(interp, argv[i]);
        } else {
            errorWrongNumArgs(interp, argv[0].toString());
        }
        if (server) {
            TclObject scr = TclString.newInstance(script);
            ServerSocketChannel sock = new ServerSocketChannel(interp, myaddr, port, scr);
            TclIO.registerChannel(interp, sock);
            interp.setResult(sock.getChanName());
        } else {
            try {
                SocketChannel sock = new SocketChannel(interp, TclIO.RDWR, myaddr, myport, async, host, port);
                TclIO.registerChannel(interp, sock);
                interp.setResult(sock.getChanName());
            } catch (IOException e) {
                throw new TclException(interp, "cannot open socket: " + e.getMessage());
            }
        }
    }

    /**
     * A unique error msg is printed for socket. Call this methods
     * instead of the standard TclException.wrongNumArgs().
     *
     * @param interp the current interpreter.
     * @param cmd the name of the command (extracted form argv[0] of cmdProc)
     */
    private static void errorWrongNumArgs(Interp interp, String cmdName) throws TclException {
        throw new TclException(interp, "wrong # args: should be either:\n" + cmdName + " ?-myaddr addr? ?-myport myport? ?-async? host port\n" + cmdName + " -server command ?-myaddr addr? port");
    }

    /**
     * TclSockGetPort -> getPort
     *
     * Maps from a string, which could be a service name, to a port.
     * Unfortunately, Java does not provide any way to access the
     * getservbyname functionality so we are limited to port numbers.
     *
     */
    private static int getPort(Interp interp, TclObject tobj) throws TclException {
        int num = Util.getInt(interp, tobj.toString());
        if (num > 0xFFFF) throw new TclException(interp, "couldn't open socket: port number too high");
        return num;
    }
}
