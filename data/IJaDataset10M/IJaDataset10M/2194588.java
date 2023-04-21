package com.onehao.network;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class UrlConnection2 {

    public static void main(String[] args) throws Exception {
        URL url = new URL("http://www.sohu.com");
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream("gen/sohu.html");
        byte[] buffer = new byte[2048];
        int length = 0;
        while (-1 != (length = is.read(buffer, 0, buffer.length))) {
            os.write(buffer);
        }
    }
}
