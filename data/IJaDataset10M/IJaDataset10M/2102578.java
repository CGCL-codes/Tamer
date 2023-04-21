package com.neurogrid.bot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * Worker thread to process sockets for generartor<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   25/01/2002    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 *
 */
public class WorkerThread extends Thread {

    private BufferedReader o_in = null;

    private BufferedWriter o_out = null;

    private Socket o_socket = null;

    private Generator o_gen = null;

    private boolean o_running = false;

    public WorkerThread(Socket p_socket, Generator p_gen) throws Exception {
        o_gen = p_gen;
        o_socket = p_socket;
        o_in = new BufferedReader(new InputStreamReader(p_socket.getInputStream()));
        o_out = new BufferedWriter(new OutputStreamWriter(p_socket.getOutputStream()));
        o_out.write("talk to me!\n", 0, "talk to me!\n".length());
        o_out.flush();
    }

    public void run() {
        String x_line = null;
        String x_output = null;
        o_running = true;
        long x_time = 0L;
        while (o_running == true) {
            try {
                if (o_in.ready() == true) {
                    x_time = 0L;
                    x_line = o_in.readLine();
                    System.out.println(x_line);
                    try {
                        x_output = o_gen.generateResponse(x_line);
                    } catch (Exception e) {
                        e.printStackTrace();
                        x_output = e.getMessage();
                    }
                    o_out.write(x_output, 0, x_output.length());
                    o_out.write("\n", 0, "\n".length());
                    o_out.flush();
                }
                Thread.sleep(200);
                x_time += 200;
                Thread.yield();
                if (x_time > 1800000) throw new Exception("Socket Timed Out");
                if (o_out == null || o_in == null || o_socket == null) throw new Exception("Socket Failed - null detected");
                if (o_socket.isClosed() == true) throw new Exception("Socket Failed - closed");
                if (o_socket.isConnected() == false) throw new Exception("Socket Failed - not connected");
                if (o_socket.isInputShutdown() == true) throw new Exception("Socket Failed - input shutdown");
                if (o_socket.isOutputShutdown() == true) throw new Exception("Socket Failed - output shutdown");
            } catch (Exception e) {
                System.out.println("WorkerThread Exception: " + e.getMessage());
                try {
                    if (o_in != null) o_in.close();
                    if (o_out != null) o_out.close();
                    if (o_socket != null) o_socket.close();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
                o_running = false;
            }
        }
        System.out.println("WorkerThread ended");
    }
}
