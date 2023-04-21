package org.xmlsh.commands.posix;

import java.io.File;
import java.util.Date;
import java.util.List;
import org.xmlsh.core.Options;
import org.xmlsh.core.XCommand;
import org.xmlsh.core.XValue;

public class touch extends XCommand {

    @Override
    public int run(List<XValue> args) throws Exception {
        Options opts = new Options("r=reference:");
        opts.parse(args);
        args = opts.getRemainingArgs();
        String sRefFile = opts.getOptString("r", null);
        File refFile = sRefFile == null ? null : getFile(sRefFile);
        long modtime = refFile != null ? refFile.lastModified() : (new Date()).getTime();
        for (XValue arg : args) {
            File file = getFile(arg);
            file.createNewFile();
            file.setLastModified(modtime);
        }
        return 0;
    }
}
