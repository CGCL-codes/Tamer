package bzr;

import org.gjt.sp.jedit.EditPlugin;
import org.gjt.sp.jedit.jEdit;

public class BzrPlugin extends EditPlugin {

    public static String bzrPath() {
        return jEdit.getProperty("bzr.path", "bzr");
    }
}

;
