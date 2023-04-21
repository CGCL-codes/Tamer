package net.kano.joustsim.oscar.oscar.service.icbm.ft;

import net.kano.joustsim.Screenname;
import net.kano.joscar.rvcmd.SegmentedFilename;
import java.io.File;

public class DefaultFileMapper implements FileMapper {

    private final Screenname screenname;

    private final String userDir;

    public DefaultFileMapper(Screenname screenname, String property) {
        this.screenname = screenname;
        userDir = property;
    }

    public File getDestinationFile(SegmentedFilename filename) {
        return new File(userDir, filename.toNativeFilename());
    }

    public File getUnspecifiedFilename() {
        String numStr = "";
        File file;
        int tried = 2;
        do {
            String name = "File from " + screenname.getFormatted() + numStr;
            file = new File(name);
            numStr = "-" + tried;
            tried++;
        } while (tried < 100000 && file.exists());
        return file;
    }
}
