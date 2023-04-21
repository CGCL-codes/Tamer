package gruntspud;

import gruntspud.connection.ConnectionProfile;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.netbeans.lib.cvsclient.file.OutputStreamProvider;
import org.netbeans.lib.cvsclient.file.WriteTextFilePreprocessor;

public class GruntspudWriteTextFilePreprocessor implements WriteTextFilePreprocessor {

    private static final int BUFFER_SIZE = 32768;

    private static String DEFAULT_LINE_SEPARATOR = System.getProperty("line.separator");

    private static final int CR = 1;

    private static final int CR_LF = 2;

    private static final int LF_CR = 3;

    private static final int UNKNOWN = 0;

    private ConnectionProfile profile;

    private GruntspudContext context;

    public GruntspudWriteTextFilePreprocessor(ConnectionProfile profile, GruntspudContext context) {
        this.profile = profile;
        this.context = context;
    }

    public void copyTextFileToLocation(InputStream pin, File file, OutputStreamProvider output) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        OutputStream debugOut = null;
        byte[] line = getSeparatorSequence().getBytes();
        Constants.CVS_LOG.debug("Using line separator " + debugSequence(getSeparatorSequence()) + " to " + file.getAbsolutePath());
        try {
            in = new BufferedInputStream(pin);
            out = new BufferedOutputStream(output.createOutputStream());
            if (context.getHost().getBooleanProperty(Constants.OPTIONS_SYSTEM_LOG_CVS_IO, false)) {
                File debugFile = new File(System.getProperty("user.home") + File.separator + "GRUNTSPUD.RECEIVED");
                Constants.IO_LOG.debug("Writing last received file to " + debugFile.getAbsolutePath());
                debugOut = new FileOutputStream(debugFile);
            }
            byte[] b = new byte[BUFFER_SIZE];
            int l = 0;
            int type = 0;
            for (int z = in.read(b); z > 0; z = in.read(b)) {
                if (debugOut != null) {
                    debugOut.write(b, 0, z);
                }
                if (line.length == 0) {
                    out.write(b, 0, z);
                } else {
                    for (int i = 0; i < z; i++) {
                        if (b[i] == 10) {
                            if (l != 13) {
                                out.write(line);
                            }
                            l = b[i];
                        } else {
                            if (b[i] == 13) {
                                out.write(line);
                            } else {
                                out.write(b[i]);
                            }
                        }
                        l = b[i];
                    }
                }
            }
            out.flush();
            if (debugOut != null) {
                debugOut.flush();
            }
        } finally {
            GruntspudUtil.closeStream(in);
            GruntspudUtil.closeStream(out);
            GruntspudUtil.closeStream(debugOut);
        }
    }

    private String debugSequence(String seq) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < seq.length(); i++) {
            if (buf.length() > 0) {
                buf.append(",");
            }
            buf.append(Integer.toHexString(seq.charAt(i)));
        }
        return buf.toString();
    }

    public String getSeparatorSequence() {
        switch(profile.getLineEndings()) {
            case ConnectionProfile.UNIX_LINE_ENDINGS:
                return "\n";
            case ConnectionProfile.WINDOWS_LINE_ENDINGS:
                return "\r\n";
            case ConnectionProfile.IGNORE_LINE_ENDINGS:
                return "";
            default:
                return DEFAULT_LINE_SEPARATOR;
        }
    }
}
