package net.simpleframework.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.StringUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ConsoleThread extends Thread {

    public static void doInit() {
        if ((System.in != null) && (System.out != null)) {
            final ConsoleThread console = new ConsoleThread(System.in);
            console.setPrintStream(System.out);
            console.start();
        }
    }

    private final ICommand[] commands = new ICommand[] { new QuitCommand(), new GcCommand() };

    private final BufferedReader reader;

    private PrintStream printStream;

    public ConsoleThread(final InputStream inputStream) {
        setDaemon(true);
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public PrintStream getPrintStream() {
        return printStream;
    }

    private final Logger logger = ALoggerAware.getLogger(ConsoleThread.class);

    @Override
    public void run() {
        while (true) {
            try {
                final String text = reader.readLine();
                if (!StringUtils.hasText(text)) {
                    break;
                }
                for (final ICommand command : commands) {
                    if (command.execute(text.toLowerCase())) {
                        break;
                    }
                }
            } catch (final IOException e) {
                logger.error(e);
                break;
            }
        }
    }

    public void setPrintStream(final PrintStream printStream) {
        this.printStream = printStream;
    }

    interface ICommand {

        boolean execute(String command);
    }

    class QuitCommand implements ICommand {

        @Override
        public boolean execute(final String command) {
            if (command.equals("quit") || command.equals("exit")) {
                System.exit(0);
                return true;
            } else {
                return false;
            }
        }
    }

    class GcCommand implements ICommand {

        @Override
        public boolean execute(final String command) {
            if (command.equals("gc")) {
                String size = IoUtils.toFileSize(Runtime.getRuntime().totalMemory());
                getPrintStream().println("total memory: " + size);
                size = IoUtils.toFileSize(Runtime.getRuntime().freeMemory());
                getPrintStream().println("free memory before gc: " + size);
                getPrintStream().println();
                System.gc();
                System.out.println("garbage collection ok.");
                getPrintStream().println();
                size = IoUtils.toFileSize(Runtime.getRuntime().freeMemory());
                getPrintStream().println("free memory after gc: " + size);
                return true;
            } else {
                return false;
            }
        }
    }
}
