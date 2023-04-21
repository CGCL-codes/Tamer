package elliott803.machine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Encapsulates the information that makes up the system dump file
 *
 * @author Baldwin
 */
public class Dump implements Serializable {

    private static final long serialVersionUID = 1L;

    public String name;

    public String version;

    public Date timestamp;

    public boolean busy;

    public long acc;

    public long ar;

    public long br;

    public long ir;

    public int scr;

    public int scr2;

    public boolean overflow;

    public boolean fpOverflow;

    public long[] core;

    public Dump(Computer computer) {
        name = computer.name;
        version = computer.version;
        timestamp = new Date();
        computer.dump(this);
        computer.cpu.dump(this);
        computer.core.dump(this);
    }

    public void write() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS");
        String filename = "elliott-" + df.format(timestamp) + ".core";
        try {
            OutputStream stream = new DeflaterOutputStream(new FileOutputStream(filename));
            write(stream);
            stream.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void write(OutputStream stream) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(stream);
        out.writeObject(this);
        out.flush();
    }

    public static Dump readDump(File file) {
        Dump dump = null;
        try {
            InputStream stream = new InflaterInputStream(new FileInputStream(file));
            dump = readDump(stream);
            stream.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return dump;
    }

    public static Dump readDump(InputStream stream) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(stream);
        Dump dump = (Dump) in.readObject();
        return dump;
    }
}
