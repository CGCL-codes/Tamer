package com.apps.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import org.junit.Test;
import com.mlib.util.StringUtil;

public class TextCommandTest {

    @Test
    public void testCmd() throws UnknownHostException, IOException {
        Socket s = new Socket("localhost", 8401);
        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        for (int len = 10; len <= 100000; len *= 10) {
            String str = StringUtil.randomString(len);
            s.getOutputStream().write("0002".getBytes());
            s.getOutputStream().write(str.getBytes());
            s.getOutputStream().write("\r\n".getBytes());
            s.getOutputStream().flush();
            System.out.println(reader.readLine());
        }
        s.close();
    }
}
