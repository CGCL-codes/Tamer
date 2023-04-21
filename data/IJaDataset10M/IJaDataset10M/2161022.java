package clearcase;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
*
*/
public class StreamReaderThread extends Thread {

    public static final int EOF = -1;

    InputStream input;

    OutputStream output;

    String type;

    StreamReaderThread(InputStream input, OutputStream output, String type) {
        this.input = input;
        this.output = output;
        this.type = type;
        start();
    }

    public void run() {
        PrintWriter writer = null;
        InputStreamReader reader = null;
        try {
            writer = new PrintWriter(output);
            reader = new InputStreamReader(input);
            int c = 0;
            System.out.println("Reading " + type);
            while ((c = reader.read()) != EOF) {
                byte character = (byte) c;
                writer.write(character);
                if (character == '\n') {
                    writer.flush();
                }
            }
            System.out.println("Finished reading " + type);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
