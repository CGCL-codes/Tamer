package com.trapezium.parse;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;

/**
 *  Reads lines into memory using JDK1.1 or JDK1.0. 
 *
 *  <P>
 *  First tries JDK1.1, switches to JDK1.0 if NoClassDefFound exception
 *  occurs.
 *
 *  @author          Johannes N. Johannsen
 *  @version         1.11, 8 Feb 1998
 */
class LineReader {

    BufferedReader br;

    InputStream inStream;

    public LineReader(InputStream inStream) {
        this.inStream = inStream;
        try {
            InputStreamReader irs = new InputStreamReader(inStream);
            br = new BufferedReader(irs);
        } catch (NoClassDefFoundError eee) {
            this.inStream = inStream;
        }
    }

    public String readLine() throws IOException {
        if (br != null) {
            return (br.readLine());
        } else {
            StringBuffer buf = new StringBuffer();
            int x;
            boolean terminatedWithR = false;
            boolean terminatedWithN = false;
            while ((x = inStream.read()) != -1) {
                if (x == '\r') {
                    terminatedWithR = true;
                    if (terminatedWithN) {
                        continue;
                    }
                    return (new String(buf));
                } else if (x == '\n') {
                    terminatedWithN = true;
                    if (terminatedWithR) {
                        continue;
                    }
                    return (new String(buf));
                } else {
                    terminatedWithR = terminatedWithN = false;
                    buf.append((char) x);
                }
            }
            if (buf.length() > 0) {
                return (new String(buf));
            } else {
                inStream.close();
                return (null);
            }
        }
    }
}
