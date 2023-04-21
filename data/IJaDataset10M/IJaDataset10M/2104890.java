package net.sf.kpex.gui.builtins;

import java.applet.Applet;
import net.sf.kpex.io.IO;
import net.sf.kpex.prolog.FunBuiltin;
import net.sf.kpex.prolog.JavaObject;
import net.sf.kpex.prolog.Prog;

/**
 * detects if applet and gets applet container
 */
@Deprecated
public class GetApplet extends FunBuiltin {

    public GetApplet() {
        super("get_applet", 1);
    }

    @Override
    public int exec(Prog p) {
        if (null == IO.applet) {
            return 0;
        }
        Applet applet = IO.applet;
        return putArg(0, new JavaObject(applet), p);
    }
}
