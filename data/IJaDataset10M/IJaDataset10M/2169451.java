package com.ibm.celldt.ui.console;

import java.io.IOException;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleInputStream;
import org.eclipse.ui.console.IOConsoleOutputStream;
import com.ibm.celldt.utils.stream.IStreamListener;
import com.ibm.celldt.utils.stream.StreamObserver;
import com.ibm.celldt.utils.terminal.AbstractTerminalProvider;
import com.ibm.celldt.utils.terminal.AbstractTerminalReceiver;
import com.ibm.celldt.utils.terminal.ITerminalProvider;

/**
 * Connects an Eclipse IOConsole to a AbstractTerminalProvider.
 * 
 * @author Daniel Felix Ferber
 * @since 1.0
 */
public class TerminalToConsoleBridge extends AbstractTerminalReceiver {

    /**
	 * Listener for data received from console.
	 * Simple forward data to terminal provider.
	 */
    class ConsoleListener implements IStreamListener {

        TerminalToConsoleBridge bridge;

        public ConsoleListener(TerminalToConsoleBridge bridge) {
            this.bridge = bridge;
        }

        public void newBytes(byte[] bytes, int length) {
            bridge.writeData(bytes, length);
        }

        public void streamClosed() {
        }

        public void streamError(Exception e) {
        }
    }

    /**
	 * Console associated with the bridge.
	 */
    IOConsole console;

    IOConsoleInputStream fromConsoleStream;

    IOConsoleOutputStream toConsoleStream;

    /**
	 * Stream observer associated with the inputstream from the console.
	 */
    StreamObserver consoleInputObserver;

    ConsoleListener consoleListener;

    ITerminalProvider terminalProvider;

    public TerminalToConsoleBridge(ITerminalProvider provider, IOConsole console, String name) {
        super(provider);
        if (name == null) {
            name = this.getClass().getName();
        }
        this.console = console;
        this.fromConsoleStream = console.getInputStream();
        this.toConsoleStream = console.newOutputStream();
        this.consoleListener = new ConsoleListener(this);
        this.consoleInputObserver = new StreamObserver(this.fromConsoleStream, consoleListener, name);
        this.consoleInputObserver.start();
        this.terminalProvider = provider;
        this.terminalProvider.addListener(this);
    }

    public void disconnect() {
        terminalProvider.removeListener(this);
        consoleInputObserver.interrupt();
        terminalProvider = null;
        consoleInputObserver = null;
        consoleListener = null;
        fromConsoleStream = null;
        toConsoleStream = null;
    }

    public TerminalToConsoleBridge(AbstractTerminalProvider terminalProvider, IOConsole console) {
        this(terminalProvider, console, null);
    }

    public void receiveData(byte[] bytes, int length) {
        try {
            toConsoleStream.write(bytes, 0, length);
        } catch (IOException e) {
        }
    }

    public void receiveError(byte[] bytes, int length) {
        try {
            toConsoleStream.write(bytes, 0, length);
        } catch (IOException e) {
        }
    }

    public void receiveMetaMessage(String message) {
        try {
            toConsoleStream.write("( INFO: ");
            toConsoleStream.write(message);
            toConsoleStream.write(")\n");
        } catch (IOException e) {
        }
    }
}
