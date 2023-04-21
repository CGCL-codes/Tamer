package org.netbeams.sim.ysi;

import java.io.InputStream;
import java.io.IOException;

/**
 * 
 * @author Teresa L. Johnson <gamma.particle@gmail.com>
 *
 */
public class SondeSerialReader implements Runnable {

    private InputStream in;

    private static String cmd;

    public SondeSerialReader(InputStream in) {
        cmd = new String("");
        this.in = in;
    }

    public void run() {
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while ((len = this.in.read(buffer)) > -1) {
                cmd = new String(buffer, 0, len);
                char option = cmd.charAt(0);
                System.out.print(option);
                switch(option) {
                    case '1':
                        System.out.print(option);
                        SondeSerialWriter.interval = 1000;
                        break;
                    case '2':
                        System.out.print(option);
                        SondeSerialWriter.interval = 2000;
                        break;
                    case '3':
                        System.out.print(option);
                        SondeSerialWriter.interval = 3000;
                        break;
                    case '4':
                        System.out.print(option);
                        SondeSerialWriter.interval = 4000;
                        break;
                    default:
                }
                ;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
