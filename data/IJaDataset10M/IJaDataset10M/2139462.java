package mx.bigdata.sat.cfd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import mx.bigdata.sat.common.CFDFactory;
import com.google.common.io.ByteStreams;

public final class CFD2Factory extends CFDFactory {

    public static CFD2 load(File file) throws Exception {
        InputStream in = new FileInputStream(file);
        try {
            return load(in);
        } finally {
            in.close();
        }
    }

    public static CFD2 load(InputStream in) throws Exception {
        return getCFD2(in);
    }

    private static CFD2 getCFD2(InputStream in) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteStreams.copy(in, baos);
        byte[] data = baos.toByteArray();
        if (getVersion(data).equals("2.2")) {
            return new CFDv22(new ByteArrayInputStream(data));
        } else {
            return new CFDv2(new ByteArrayInputStream(data));
        }
    }
}
