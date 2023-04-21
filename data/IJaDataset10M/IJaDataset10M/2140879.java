package org.wangfy.hy.hynet.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HynetExampleClient implements Runnable {

    private static long sum = 0L;

    private Object gate = new Object();

    private int id;

    private SocketChannel sckchannel;

    public HynetExampleClient(int id) throws IOException {
        this.id = id;
        sckchannel = SocketChannel.open();
        InetAddress addr = InetAddress.getLocalHost();
        InetSocketAddress sckAddr = new InetSocketAddress(addr, 8082);
        sckchannel.socket().setReuseAddress(true);
        sckchannel.socket().setKeepAlive(true);
        sckchannel.connect(sckAddr);
    }

    private PrintWriter getWriter(Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        return new PrintWriter(new OutputStreamWriter(out));
    }

    private BufferedReader getReader(Socket socket) throws IOException {
        InputStream in = socket.getInputStream();
        return new BufferedReader(new InputStreamReader(in));
    }

    public void run() {
        try {
            BufferedReader reader = this.getReader(sckchannel.socket());
            PrintWriter writer = this.getWriter(sckchannel.socket());
            String msg = "GET / HTTP/1.1 \r\n";
            writer.write(msg);
            writer.flush();
            long start = System.currentTimeMillis();
            sckchannel.socket().shutdownOutput();
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            System.out.println("[CLIENT] " + Thread.currentThread().getName() + " -finish receive - " + sb.toString());
            sckchannel.socket().shutdownInput();
            long stop = System.currentTimeMillis();
            long consume = stop - start;
            synchronized (gate) {
                sum += consume;
            }
            System.out.println("[CLIENT] This time == " + (stop - start) + " ms ( " + id + " )" + " Average Consume [" + sum / id + "]");
            System.out.println();
        } catch (IOException e) {
            System.err.println("[CLIENT] " + Thread.currentThread().getName() + " -exception happened when interact with server" + e.getMessage());
        } finally {
            try {
                sckchannel.close();
            } catch (IOException e) {
                System.err.println("[CLIENT] " + "exception happened when close channel" + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length <= 0) {
            System.out.println("Usage java classname number");
            System.exit(1);
        }
        int size = Integer.valueOf(args[0]).intValue();
        ExecutorService exec = Executors.newFixedThreadPool(size);
        for (int index = 1; index <= size; index++) {
            exec.execute(new HynetExampleClient(index));
        }
        exec.shutdown();
    }
}
