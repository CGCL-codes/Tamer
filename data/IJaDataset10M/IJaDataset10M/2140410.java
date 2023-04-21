package org.rjam.net.command.server.impl;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.GeneralSecurityException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.rjam.net.api.IConnection;
import org.rjam.net.api.server.IServer;
import org.rjam.net.command.IGenericResponseCode;
import org.rjam.net.command.server.api.ICommand;
import org.rjam.net.command.server.api.ICommandFactory;
import org.rjam.net.command.server.api.ICommandProcessor;
import org.rjam.net.command.server.api.IRequestContext;
import org.rjam.net.command.server.api.IRequestContextFactory;
import org.rjam.net.impl.server.AbstractProcessor;

public abstract class AbstractCommandProcessor extends AbstractProcessor implements ICommandProcessor {

    private static final long serialVersionUID = 1L;

    private IRequestContextFactory requestContextFactory = new DefaultRequestContextFactory();

    private ICommandFactory commandFactory;

    public IRequestContextFactory getRequestContextFactory() {
        return requestContextFactory;
    }

    public void setRequestContextFactory(IRequestContextFactory requestContextFactory) {
        this.requestContextFactory = requestContextFactory;
    }

    public AbstractCommandProcessor() {
        super();
    }

    public AbstractCommandProcessor(ICommandFactory factory) {
        this();
        setCommandFactory(factory);
    }

    public ICommandFactory getCommandFactory() {
        return commandFactory;
    }

    public void setCommandFactory(ICommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public void run() {
        IConnection con = getConnection();
        running = true;
        String lastLine = null;
        int nullCnt = 0;
        while (running && !stopping) {
            try {
                String line = con.readLine();
                if (line == null) {
                    logDebug("read null??? EOF reached? Connection must be closed by client or network stack error.");
                    if (++nullCnt > 2) {
                        stop();
                    }
                } else {
                    nullCnt = 0;
                    logDebug("received line=" + line);
                    if (lastLine != null && line.length() > 0 && line.charAt(0) == '.') {
                        if (line.length() > 1) {
                            line = lastLine + line.substring(1);
                        } else {
                            line = lastLine;
                        }
                        logDebug("dot cmd =" + line);
                    } else {
                        lastLine = line;
                    }
                    IRequestContext context = getRequestContextFactory().getRequestContext(line);
                    ICommand command = getCommandFactory().getCommand(context);
                    if (command == null) {
                        reply(IGenericResponseCode.REPLY_500_ERROR, "Not a valid command (" + line + ").");
                    } else {
                        if (command.isAutherized(this, context)) {
                            command.execute(this, context);
                        }
                    }
                }
            } catch (SocketTimeoutException e) {
            } catch (SocketException e) {
                if (!(e.toString().indexOf("Connection reset") >= 0)) {
                    logError("Error in run", e);
                } else {
                    logInfo("Connection closed:" + con.getSocket());
                }
                stop();
            } catch (IOException e) {
                logError("Error in run", e);
                stop();
            } catch (Exception e) {
                logError("Error in run", e);
                try {
                    replyError(e);
                } catch (IOException ex) {
                    logError("Error trying to reply to client", ex);
                    stop();
                }
            } catch (Throwable t) {
                logError("Error in run", t);
                try {
                    replyError(t);
                } catch (IOException ex) {
                    logError("Error trying to reply to client", ex);
                    stop();
                }
            }
        }
        running = false;
        getServer().removeClient(this);
        con.close();
    }

    private void replyError(Throwable t) throws IOException {
        String msg = t.toString();
        msg = msg.replace('\n', ' ').replace('\r', ' ');
        reply(REPLY_500_ERROR, "An error occured processing the request. Error =" + msg);
    }

    public void reply(int responseCode, String text) throws IOException {
        reply(translateResponseCode(responseCode) + " " + text);
    }

    public void reply(String text) throws IOException {
        IConnection con = getConnection();
        con.writeLine(text);
        con.flush();
    }

    public void negotiateSecureChannel() throws IOException, GeneralSecurityException {
        reply(REPLY_300_TEMPOARY_OK, "Negotiate Secure Channel");
        IConnection con = getConnection();
        if (!con.isSecure()) {
            IServer svr = getServer();
            SSLContext ctx = svr.getSSLContext();
            SSLSocketFactory factory = ctx.getSocketFactory();
            Socket socket = con.getSocket();
            SSLSocket sslSocket = (SSLSocket) factory.createSocket(socket, socket.getInetAddress().getHostName(), socket.getPort(), true);
            sslSocket.setUseClientMode(true);
            con.setSocket(sslSocket);
            reply(REPLY_300_TEMPOARY_OK, "Send Password");
            String pw = con.readLine();
            svr.verifyPassword(pw);
        }
    }
}
