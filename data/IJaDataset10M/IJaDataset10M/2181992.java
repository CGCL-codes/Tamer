package com.peterhi.servlet;

import java.io.Console;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import javolution.util.FastCollection;
import javolution.util.FastSet;
import javolution.util.FastCollection.Record;
import com.peterhi.servlet.classrooms.ClassroomsComponent;
import com.peterhi.servlet.cmd.AccCommand;
import com.peterhi.servlet.cmd.AuthCommand;
import com.peterhi.servlet.cmd.BatchCommand;
import com.peterhi.servlet.cmd.ClsCommand;
import com.peterhi.servlet.cmd.CommandConstants;
import com.peterhi.servlet.cmd.DumpCommand;
import com.peterhi.servlet.cmd.ExitCommand;
import com.peterhi.servlet.nio.DatagramComponent;
import com.peterhi.servlet.nio.NioComponent;
import com.peterhi.servlet.persist.PrevaylerComponent;

public class Kernel implements Runnable, CommandConstants {

    private static final Logger logger = Logger.getLogger(Kernel.class.getSimpleName());

    private static Kernel instance;

    /**
	 * @param args
	 * @throws Exception 
	 */
    public static void main(String[] args) {
        try {
            String file = args[0];
            Properties props = new Properties();
            props.load(new FileInputStream(file));
            Kernel kernel = new Kernel(props);
            kernel.run();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private FastCollection<Component> components = new FastSet<Component>();

    private Map<String, KernelCommand> commands = new HashMap<String, KernelCommand>();

    private Properties props;

    private Console console = System.console();

    private boolean running;

    public static Kernel getInstance() {
        return instance;
    }

    public Kernel(Properties props) {
        this.props = props;
        initComponents();
        initCommands();
        logger.info("kernel started");
    }

    public void executeCommand(String line) {
        if (line == null || line.length() <= 0) {
            return;
        }
        StringTokenizer tok = new StringTokenizer(line);
        String name = tok.nextToken();
        KernelCommand cmd = commands.get(name);
        if (cmd == null) {
            console.printf("unknown command\n");
            return;
        }
        try {
            if (cmd.interact(this, console, props, tok)) {
                cmd.run();
            } else {
                console.printf("syntax error\n");
                cmd.usage();
            }
        } catch (InterruptedException ex) {
            console.printf("cancelled\n");
        }
        if (cmd != null) {
            cmd.clean();
        }
    }

    public <T extends Component> T getComponent(Class<T> c) {
        for (Record r = components.head(), end = components.tail(); (r = r.getNext()) != end; ) {
            Component cur = (Component) components.valueOf(r);
            if (cur.getClass() == c) {
                return c.cast(cur);
            }
        }
        return null;
    }

    private void initCommands() {
        synchronized (commands) {
            commands.put(authCommand, new AuthCommand());
            commands.put(exitCommand, new ExitCommand());
            commands.put(accCommand, new AccCommand());
            commands.put(clsCommand, new ClsCommand());
            commands.put(dumpCommand, new DumpCommand());
            commands.put(batchCommand, new BatchCommand());
        }
    }

    private void initComponents() {
        synchronized (components) {
            components.add(new NioComponent(this, props));
            components.add(new PrevaylerComponent(this, props));
            components.add(new DatagramComponent(this, props));
            components.add(new ClassroomsComponent(this, props));
        }
        instance = this;
        for (Record r = components.head(), end = components.tail(); (r = r.getNext()) != end; ) {
            Component cur = (Component) components.valueOf(r);
            cur.onConfigure();
        }
    }

    private void login() {
        try {
            executeCommand(authCommand);
        } catch (ConsoleAuthException ex) {
            ex.printStackTrace();
            shutdown();
            return;
        }
    }

    private void mainLoop() {
        while (running) {
            String line = console.readLine("peterhi>");
            executeCommand(line);
        }
    }

    public void run() {
        running = true;
        login();
        mainLoop();
    }

    public void shutdown() {
        running = false;
        System.exit(0);
    }
}
