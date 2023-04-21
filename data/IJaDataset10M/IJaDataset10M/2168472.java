package thread.ch03;

import java.io.DataInputStream;
import java.net.Socket;

public class AsyncReadSocket extends Thread {

    private Socket s;

    private StringBuffer result;

    public AsyncReadSocket(Socket s) {
        this.s = s;
        result = new StringBuffer();
    }

    public void run() {
        DataInputStream is = null;
        try {
            is = new DataInputStream(s.getInputStream());
        } catch (Exception e) {
        }
        while (true) {
            try {
                char c = is.readChar();
                appendResult(c);
            } catch (Exception e) {
            }
        }
    }

    public synchronized String getResult() {
        String retval = result.toString();
        result = new StringBuffer();
        return retval;
    }

    public synchronized void appendResult(char c) {
        result.append(c);
    }
}
