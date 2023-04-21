package ow.tool.memcached;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import ow.dht.DHTConfiguration;
import ow.dht.DHTFactory;
import ow.dht.memcached.Item;
import ow.dht.memcached.Memcached;
import ow.messaging.Signature;
import ow.messaging.util.AccessController;
import ow.tool.emulator.EmulatorControllable;
import ow.tool.memcached.commands.AddCommand;
import ow.tool.memcached.commands.AppendCommand;
import ow.tool.memcached.commands.CasCommand;
import ow.tool.memcached.commands.DecrCommand;
import ow.tool.memcached.commands.DeleteCommand;
import ow.tool.memcached.commands.FlushAllCommand;
import ow.tool.memcached.commands.GetCommand;
import ow.tool.memcached.commands.GetsCommand;
import ow.tool.memcached.commands.HelpCommand;
import ow.tool.memcached.commands.IncrCommand;
import ow.tool.memcached.commands.InitCommand;
import ow.tool.memcached.commands.LocaldataCommand;
import ow.tool.memcached.commands.PrependCommand;
import ow.tool.memcached.commands.QuitCommand;
import ow.tool.memcached.commands.ReplaceCommand;
import ow.tool.memcached.commands.SetCommand;
import ow.tool.memcached.commands.StatsCommand;
import ow.tool.memcached.commands.StatusCommand;
import ow.tool.memcached.commands.VerbosityCommand;
import ow.tool.memcached.commands.VersionCommand;
import ow.tool.util.shellframework.Command;
import ow.tool.util.shellframework.Interruptible;
import ow.tool.util.shellframework.MessagePrinter;
import ow.tool.util.shellframework.Shell;
import ow.tool.util.shellframework.ShellServer;
import ow.tool.util.toolframework.AbstractDHTBasedTool;

/**
 * The main class of memcached.
 */
public final class Main extends AbstractDHTBasedTool<Item> implements EmulatorControllable, Interruptible {

    public static final String VERSION = "1.2.6";

    private static final String COMMAND = "owmemcached";

    private static final int DEFAULT_PORT = 11211;

    private static final Class[] COMMANDS = { GetCommand.class, GetsCommand.class, SetCommand.class, AddCommand.class, ReplaceCommand.class, AppendCommand.class, PrependCommand.class, CasCommand.class, DeleteCommand.class, IncrCommand.class, DecrCommand.class, StatsCommand.class, FlushAllCommand.class, VersionCommand.class, VerbosityCommand.class, QuitCommand.class, InitCommand.class, StatusCommand.class, LocaldataCommand.class, HelpCommand.class };

    private static final List<Command<Memcached>> commandList;

    private static final Map<String, Command<Memcached>> commandTable;

    static {
        commandList = ShellServer.createCommandList(COMMANDS);
        commandTable = ShellServer.createCommandTable(commandList);
    }

    private Thread mainThread = null;

    protected void usage(String command) {
        super.usage(command, "[-p <shell port>] [--acl <ACL file>] [-n]");
    }

    public static void main(String[] args) {
        (new Main()).start(args);
    }

    protected void start(String[] args) {
        Shell<Memcached> stdioShell = null;
        stdioShell = this.init(args, System.in, System.out, true);
        if (stdioShell != null) {
            stdioShell.run();
        }
    }

    /**
	 * Implements {@link EmulatorControllable#invoke(int, String[], PrintStream)
	 * EmulatorControllable#invoke}.
	 */
    public Writer invoke(String[] args, PrintStream out) {
        Shell<Memcached> stdioShell = this.init(args, null, out, false);
        if (stdioShell != null) return stdioShell.getWriter(); else return null;
    }

    private Shell<Memcached> init(String[] args, InputStream in, PrintStream out, boolean interactive) {
        int shellPort = DEFAULT_PORT;
        AccessController ac = null;
        boolean disableStdin = false;
        this.mainThread = Thread.currentThread();
        Options opts = this.getInitialOptions();
        opts.addOption("p", "port", true, "port number");
        opts.addOption("A", "acl", true, "access control list file");
        opts.addOption("n", "disablestdin", false, "disable standard input");
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(opts, args);
        } catch (ParseException e) {
            System.out.println("There is an invalid option.");
            e.printStackTrace();
            System.exit(1);
        }
        parser = null;
        opts = null;
        String optVal;
        optVal = cmd.getOptionValue('p');
        if (optVal != null) {
            shellPort = Integer.parseInt(optVal);
        }
        optVal = cmd.getOptionValue("A");
        if (optVal != null) {
            try {
                ac = new AccessController(optVal);
            } catch (IOException e) {
                System.err.println("An Exception thrown:");
                e.printStackTrace();
                return null;
            }
        }
        if (cmd.hasOption('n')) {
            disableStdin = true;
        }
        DHTConfiguration config = DHTFactory.getDefaultConfiguration();
        config.setImplementationName("memcached");
        config.setMultipleValuesForASingleKey(false);
        config.setDoReputOnReplicas(true);
        Memcached dht = null;
        try {
            dht = (Memcached) super.initialize(Signature.APPLICATION_ID_MEMCACHED, (short) 0x10000, config, COMMAND, cmd);
        } catch (Exception e) {
            System.err.println("An Exception thrown:");
            e.printStackTrace();
            return null;
        }
        cmd = null;
        MessagePrinter errPrinter = new ErrorPrinter();
        ShellServer<Memcached> shellServ = new ShellServer<Memcached>(commandTable, commandList, null, errPrinter, errPrinter, dht, shellPort, ac);
        shellServ.addInterruptible(this);
        Shell<Memcached> stdioShell = null;
        if (disableStdin) {
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
            }
        } else {
            stdioShell = new Shell<Memcached>(in, out, shellServ, dht, interactive);
        }
        return stdioShell;
    }

    public void interrupt() {
        if (this.mainThread != null && !this.mainThread.equals(Thread.currentThread())) this.mainThread.interrupt();
    }

    private static class ErrorPrinter implements MessagePrinter {

        public void execute(PrintStream out, String hint) {
            out.print("ERROR" + Shell.CRLF);
            out.flush();
        }
    }
}
