package to_do_o.core.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotOpenException;

/**
 * 
 * @author Ruediger Gad
 *
 */
public class RecordStoreUtil {

    public static void writeInputStreamToRecordStore(InputStream in, RecordStore rs) throws IOException, RecordStoreNotOpenException, RecordStoreFullException, RecordStoreException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read = in.read(buffer);
        while (read >= 0) {
            out.write(buffer, 0, read);
            read = in.read(buffer);
        }
        out.flush();
        rs.addRecord(out.toByteArray(), 0, out.toByteArray().length);
        out.close();
    }
}
