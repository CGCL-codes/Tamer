package com.lineadecodigo.java.basico.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class EjecutarUnComando {

    public static String stream2String(InputStream is) throws IOException {
        if (is != null) {
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }

    public static void main(String[] args) {
        try {
            String cmd = "halt";
            Runtime.getRuntime().exec(cmd);
            String[] cmd2 = { "shutdown", "-s", "-t", "10" };
            Runtime.getRuntime().exec(cmd2);
            Process process = Runtime.getRuntime().exec("lsb_release -a");
            InputStream is = process.getInputStream();
            System.out.println(stream2String(is));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
