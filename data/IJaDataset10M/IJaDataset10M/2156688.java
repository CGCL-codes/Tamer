package net.sourceforge.jnipp.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RuntimeExecStreamHandler extends Thread {

    private InputStream is = null;

    public RuntimeExecStreamHandler(InputStream is) {
        this.is = is;
    }

    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String buffer = null;
            while ((buffer = br.readLine()) != null) System.out.println(buffer);
        } catch (IOException ex) {
        }
    }
}
