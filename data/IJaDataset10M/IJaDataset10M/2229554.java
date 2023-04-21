package com.kenstevens.jei.session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.telnet.TelnetClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.kenstevens.jei.listener.WinAceListenerService;
import com.kenstevens.jei.main.JeiConfig;
import com.kenstevens.jei.model.EmpireServer;
import com.kenstevens.jei.parser.EmpireCommandOutputProcessor;

@Component
public class EmpireSessionServiceImpl implements Runnable, EmpireSessionService {

    @Autowired
    private EmpireCommandSenderThread sender;

    @Autowired
    private JeiConfig config;

    @Autowired
    private WinAceListenerService winAceListener;

    private final TelnetClient client = new TelnetClient();

    private final List<EmpireCommandOutputProcessor> processors = new ArrayList<EmpireCommandOutputProcessor>();

    private Thread currentThread = null;

    private boolean stopping = false;

    public void connect() throws SocketException, IOException {
        EmpireServer server = config.getEmpireServer();
        client.connect(server.getHostname(), server.getPort());
        sender.setTelnetClientWriter(new OutputStreamWriter(client.getOutputStream()));
    }

    public void registerResponseProcessor(EmpireCommandOutputProcessor processor) {
        processors.add(processor);
    }

    public void run() {
        sender.start();
        winAceListener.start();
        InputStream input = client.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        try {
            while (!stopping & currentThread != null && !currentThread.isInterrupted() && client.isConnected()) {
                String line = reader.readLine();
                for (EmpireCommandOutputProcessor p : processors) {
                    p.process(line);
                }
            }
            client.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
        } finally {
            winAceListener.stop();
            sender.stop();
        }
        currentThread = null;
    }

    public synchronized void start() {
        if (currentThread != null) {
            throw new IllegalStateException("Already started.");
        }
        currentThread = Thread.currentThread();
        run();
    }

    public synchronized void stop() {
        stopping = true;
        if (currentThread != null) {
            currentThread.interrupt();
        }
    }

    public EmpireCommandSenderThread getSender() {
        return sender;
    }

    public void setSender(EmpireCommandSenderThread sender) {
        this.sender = sender;
    }

    public EmpireServer getServer() {
        return config.getEmpireServer();
    }
}
