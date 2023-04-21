package org.gzigzag;

import java.awt.*;
import java.applet.*;
import java.io.*;
import java.util.*;

/** An applet showing the split Nile view.
 * This just takes a lot of code from ZZApplet -- bad style. Well, this is
 * just an intermediate: When we start making real demos using the Dump
 * stuff, ZZApplet probably needs to be rewritten, and NileApplet will be
 * totally obsolete.
 */
public class NileApplet extends Applet {

    public static final String rcsid = "$Id: NileApplet.java,v 1.4 2001/01/03 16:47:50 raulir Exp $";

    public void init() {
        ZZSpace space = new ZZCacheDimSpace(new DummyStreamSet());
        ZZCell home = space.getHomeCell();
        home.N("d.1").setText("First text");
        home.N("d.1").setText("Second text");
        ZZDefaultSpace.create(home);
        setLayout(new BorderLayout());
        ZZWindows.init(space, this);
        ZZCell topl = ZZWindows.startCell().s("d.1", 1).s("d.2", 1);
        ZZCell a = topl.s("d.1", 2);
        ZZCell b = topl.s("d.2", 1).s("d.1", 1);
        ZZCursorReal.set(a, home.s("d.1", 1));
        ZZCursorReal.set(b, home.s("d.1", 2));
        ZZCursorReal.setColor(a, Color.red);
        ZZCursorReal.setColor(b, Color.yellow);
        org.gzigzag.module.SplitNileDemo.updateSpace(a, a, b);
        ZZObsTrigger.runObsQueue();
    }
}
