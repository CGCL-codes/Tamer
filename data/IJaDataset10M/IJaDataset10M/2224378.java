package elliott803.hardware;

import java.io.IOException;
import java.io.InputStream;
import elliott803.machine.Computer;
import elliott803.telecode.Telecode;

/**
 * Tape reader.  Currently only supports 5-hole tape read as bytes from an InputStream.
 *
 * @author Baldwin
 */
public class Reader extends TapeDevice {

    InputStream inputTape = null;

    public Reader(Computer computer, int id) {
        super(computer, id);
    }

    public void setTape(InputStream tape) {
        if (inputTape != null) {
            try {
                inputTape.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        inputTape = tape;
        viewTape(tape);
        deviceReady();
    }

    public int read() {
        int ch = readCh();
        if (inputTape == null) {
            deviceWait();
            ch = readCh();
        }
        if (inputTape != null) {
            viewChar(ch);
        }
        return (ch & Telecode.CHAR_MASK);
    }

    private int readCh() {
        int ch = 0;
        if (inputTape != null) {
            try {
                ch = inputTape.read();
                if (ch == -1) {
                    setTape(null);
                    ch = 0;
                }
            } catch (IOException e) {
                System.err.println(e);
                setTape(null);
            }
        }
        return ch;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("READER ").append(id).append(":");
        sb.append(" tape=").append(inputTape);
        sb.append(" ").append(deviceBusy() ? "WAITING" : "READY");
        return sb.toString();
    }
}
