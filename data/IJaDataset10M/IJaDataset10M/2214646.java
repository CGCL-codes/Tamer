package tripleo.framework.svr.storage;

import java.util.*;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.*;
import tripleo.framework.fs.*;
import tripleo.histore.j3.HiStore_J3;
import tripleo.util.Assert;

public class ss1_handler {

    private String _ss;

    public ss1_handler(String s) {
        dh = FileSystem.openUnix(s);
        _ss = s;
        clean_temps();
    }

    /**
	 * 
	 */
    private void clean_temps() {
        FileDesc directory = dh.describe(_ss);
        if (!directory.exists()) try {
            directory.create();
        } catch (FileOpFailure e1) {
            e1.printStackTrace();
        }
        Iterator<FileDesc> X = dh.enumAll();
        FileDesc x;
        while (X.hasNext()) {
            x = X.next();
            if (x.name().startsWith("tmp_")) {
                try {
                    x.unlink();
                } catch (FileOpFailure e) {
                    System.err.println("Error deleting temporary file " + e);
                    e.printStackTrace();
                }
            }
        }
    }

    private String _finalize(String aTempCap, long aSize) {
        log.info("delete me " + aTempCap + " " + aSize);
        String X = _translate(aTempCap.substring(4));
        FileDesc fd1 = dh.describe(aTempCap);
        FileDesc fd2 = dh.describe(aTempCap);
        try {
            if (fd2.exists()) fd2.unlink();
            fd1.move(fd2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aTempCap;
    }

    private static transient Log log = LogFactory.getLog(HiStore_J3.class);

    private String _translate(String aCap) {
        return String.format("res__%s", new Object[] { aCap });
    }

    private String alloc_temp() {
        int alloced = mRandom.nextInt();
        byte n[] = new byte[4];
        n[0] = (byte) (alloced & 0xff000000);
        n[1] = (byte) (alloced & 0xff0000);
        n[2] = (byte) (alloced & 0xff00);
        n[3] = (byte) (alloced & 0xff);
        String long1 = Long.toString(alloced, 16);
        return (new StringBuilder("tmp_")).append(long1).toString();
    }

    public void create_file_req(CreateRequest Q, Channel CH) {
        try {
            FileDesc fd;
            String NC;
            do {
                NC = alloc_temp();
                fd = dh.describe(NC);
            } while (fd.exists());
            final int mode = Modes.FailIfExist | Modes.CreateAlways;
            final WriteHandle fh = fd.openWrite(mode);
            final FileReader rd = Q.RD();
            final long S = rd.available();
            while (rd.available() > 0L) fh.write(rd.read(0x4000000));
            fh.close();
            String R = _finalize(NC, S);
            CH.string_success(R);
        } catch (FileOpFailure e) {
            CH.fileop_failure(e);
        }
    }

    public void delete_file_req(DeleteFileRequest Q, Channel CH) {
        try {
            String translated = _translate(Q.CAP());
            FileDesc fileDesc = dh.describe(translated);
            fileDesc.unlink();
            CH.default_success();
        } catch (FileOpFailure e) {
            CH.fileop_failure(e);
        }
    }

    public void read_file_req(ReadFileRequest Q, Channel CH) {
        try {
            String translated = _translate(Q.CAP());
            FileDesc fileDesc = dh.describe(translated);
            ReadHandle f = fileDesc.openRead();
            f.seek(Q.startIndex());
            CH.fileread_sucess(new PartialStreamR(f, f.available()));
            f.close();
        } catch (FileOpFailure e) {
            CH.fileop_failure(e);
        }
    }

    private IDirHandler dh;

    static int _tmp = 1;

    private final Random mRandom = new Random();
}
