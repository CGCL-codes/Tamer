package com.codeberry.yws;

import com.codeberry.yws.exception.ContentException;
import com.codeberry.yws.exception.HandlerInitException;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class YConnectionHandler implements Runnable {

    private Socket socket;

    private ContextMap contextMap;

    private ContextHandler initHandler;

    private BufferedReader reader;

    private BufferedOutputStream output;

    private Request request;

    private Response response;

    public YConnectionHandler(Socket socket, ContextHandler initHandler, ContextMap contextMap) {
        this.socket = socket;
        this.initHandler = initHandler;
        this.contextMap = contextMap;
    }

    public void start() {
        String threadName = YConnectionHandler.class.getSimpleName();
        Thread t = new Thread(this, threadName);
        t.setDaemon(true);
        t.start();
    }

    public void run() {
        try {
            initInOutStreams();
            initRequestResponse();
            runInitHandler();
            runHandlers();
            response.flush();
        } finally {
            dispose(reader, output);
            System.out.println("handler ended");
        }
    }

    private void initInOutStreams() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new HandlerInitException(e);
        }
    }

    private void initRequestResponse() {
        try {
            request = new Request(reader);
            response = new Response(output);
        } catch (IOException e) {
            throw new HandlerInitException(e);
        }
    }

    private void runInitHandler() {
        if (initHandler != null) {
            try {
                request = initHandler.handle(request, response);
            } catch (IOException e) {
                throw new ContentException("Error running init handler");
            }
        }
    }

    private void runHandlers() {
        try {
            while (request != null) {
                ContextHandler contextHandler = contextMap.get(request.getFullPath());
                request = contextHandler.handle(request, response);
            }
        } catch (IOException e) {
            throw new ContentException("Error running content handler");
        }
    }

    private void dispose(BufferedReader reader, BufferedOutputStream output) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException ignore) {
            }
        }
        if (output != null) {
            try {
                output.close();
            } catch (IOException ignore) {
            }
        }
        try {
            socket.close();
        } catch (IOException ignore) {
        }
    }
}
