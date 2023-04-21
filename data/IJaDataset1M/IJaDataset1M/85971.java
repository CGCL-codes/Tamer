package self.amigo.module;

import java.io.*;
import self.awt.*;

public class SaveInfo {

    public static final int EXISTING_NAME = 0;

    public static final int FILE_SYSTEM = 1;

    public static final int CLIPBOARD = 2;

    public final int type;

    public final File target;

    public SaveInfo(int kind, File file) {
        type = kind;
        target = file;
    }

    public Writer getWriter(OpenInfo src) {
        Writer wtr = null;
        if (type == CLIPBOARD) {
            wtr = ClipboardUtils.createClipboardWriter();
        } else {
            File file = target;
            if (type == EXISTING_NAME) file = src.source;
            try {
                wtr = new FileWriter(file);
            } catch (IOException err) {
                err.printStackTrace();
                throw new IllegalStateException(err.getMessage());
            }
        }
        if (wtr != null) wtr = new BufferedWriter(wtr);
        return wtr;
    }

    public OpenInfo getOpenInfoOnceSaved(OpenInfo orig) {
        if (type == EXISTING_NAME) return orig;
        if (type == CLIPBOARD) return orig;
        if (orig != null) {
            if (target.equals(orig.source)) return orig;
        }
        return new OpenInfo(OpenInfo.FILE_SYSTEM, target);
    }
}
