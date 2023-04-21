package com.apps.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import org.junit.Test;

public class CommandTest {

    @Test
    public void testCmd() throws UnknownHostException, IOException {
        Socket s = new Socket("localhost", 8401);
        InputStream ins = s.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
        s.getOutputStream().write(("0001\r\n").getBytes());
        s.getOutputStream().flush();
        System.out.println(reader.readLine());
        s.getOutputStream().write(("1001-f yyyy\r\n").getBytes());
        s.getOutputStream().flush();
        System.out.println(reader.readLine());
        s.getOutputStream().write(("0002\r\n").getBytes());
        s.getOutputStream().flush();
        System.out.println(reader.readLine());
        s.close();
    }
}
