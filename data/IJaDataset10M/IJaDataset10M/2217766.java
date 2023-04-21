package org.simin.smim.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Date;
import org.junit.Test;
import org.simin.smim.model.Message;

public class TestServer extends Thread {

    @Test
    public void test() throws IOException, Exception {
        ServerSocket serverSocket = new ServerSocket(1219);
        Handler h = new Handler(serverSocket.accept());
        h.run();
        Pipe p = new Pipe();
        for (int i = 0; i < 10; i++) {
            Message m = new Message();
            m.setSender(p.getLocal());
            m.setReceiver("localhost");
            m.setSendTime(new Date());
            m.setContent("我家宝贝是小猪");
            p.send(m);
        }
    }
}
