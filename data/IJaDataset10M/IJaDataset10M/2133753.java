package kaos.rescue;

import jason.JasonException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import kaos.env.RescueEnvironment;
import rescuecore.Connection;
import rescuecore.InputBuffer;
import rescuecore.TCPConnection;
import rescuecore.commands.Command;

public class RescueConnection extends Thread {

    private RescueAgent agent;

    private Connection connection;

    private volatile boolean running = true;

    private volatile boolean alive = true;

    public RescueConnection(RescueAgent agent) throws JasonException {
        this.agent = agent;
        try {
            InetAddress address = InetAddress.getByName(RescueEnvironment.host);
            connection = new TCPConnection(address, RescueEnvironment.port);
            agent.setConnection(connection);
            if (launch()) {
                start();
                return;
            }
        } catch (IOException e) {
        }
        throw new JasonException();
    }

    private boolean launch() throws IOException {
        rescuecore.RescueMessage message = new rescuecore.RescueMessage();
        message.append(agent.generateConnectCommand());
        connection.send(message.toByteArray());
        boolean success = false;
        try {
            byte[] reply = connection.receive(60000);
            InputBuffer in = new InputBuffer(reply);
            Command[] messages = in.readCommands();
            for (Command command : messages) {
                if (success) {
                    agent.handleMessage(command);
                } else {
                    if (command.getType() == rescuecore.RescueConstants.KA_CONNECT_OK) {
                        success = agent.handleConnectOK(command);
                    } else if (command.getType() == rescuecore.RescueConstants.KA_CONNECT_ERROR) {
                        String reason = agent.handleConnectError(command).trim();
                        Logger.getLogger("launcher").info("Error connecting " + agent + ": " + reason);
                        return false;
                    }
                }
            }
        } catch (InterruptedException e) {
        }
        return success;
    }

    public void run() {
        while (running) {
            try {
                byte[] message = connection.receive(1000);
                if (message != null) {
                    Command[] messages = new InputBuffer(message).readCommands();
                    for (Command command : messages) agent.handleMessage(command);
                }
            } catch (IOException e) {
                Logger.getLogger("launcher").log(Level.SEVERE, e.getMessage(), e);
            } catch (InterruptedException e) {
            }
        }
        synchronized (this) {
            alive = false;
            notifyAll();
        }
    }

    public void kill() {
        running = false;
        interrupt();
    }

    public void killAndWait() {
        kill();
        synchronized (this) {
            while (alive) {
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                }
            }
        }
        connection.close();
    }
}
