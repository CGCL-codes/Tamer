package net.sourceforge.jfreeplayer.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.jfreeplayer.httpserver.request.HttpRequest;
import net.sourceforge.jfreeplayer.httpserver.request.RequestException;
import net.sourceforge.jfreeplayer.httpserver.service.HttpService;
import net.sourceforge.jfreeplayer.httpserver.service.HttpServiceManager;

public class HttpServer implements Runnable {

    protected static final String LOG_NAME = HttpServer.class.getName();

    protected static final Logger logger = Logger.getLogger(LOG_NAME);

    Thread thread;

    public void launch() {
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        try {
            logger.info("starting server");
            ServerSocket server = new ServerSocket(8080);
            while (true) {
                Socket socket = server.accept();
                BufferedReader input = new BufferedReader(new InputStreamReader((socket.getInputStream())));
                OutputStream output = socket.getOutputStream();
                HttpRequest message = null;
                try {
                    message = HttpRequest.parse(input);
                } catch (RequestException e) {
                    logger.log(Level.INFO, "Invalid message received", e);
                    closeSocket(socket);
                    continue;
                }
                HttpService service = HttpServiceManager.getService(message);
                if (service == null) {
                    logger.info("no service for url " + message.getUrl());
                    closeSocket(socket);
                    continue;
                } else {
                    service.connect(message, input, output);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "General server error", e);
        }
    }

    private void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error closing connection", e);
        }
    }
}
