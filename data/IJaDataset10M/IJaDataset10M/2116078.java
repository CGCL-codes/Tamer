package org.gzigzag;

import java.lang.reflect.*;

/** A simple class that prints the RCSIds for a number of ZZ classes.
 * Eventually this'll go to the structure but for now, this is a simple
 * enough way to make sure what someone is actually running.
 */
public class ChkVersion {

    public static final String rcsid = "$Id: ChkVersion.java,v 1.5 2000/09/19 10:31:58 ajk Exp $";

    static final String[] classes = new String[] { "ZZCell", "ZZCellScroll", "ZZDefaultSpace", "ZZError", "ZZSimpleClient2", "ZZApplet", "ZZ2DBlob", "ZZ2DCanvas", "ZZ2DCanvasRaster", "ZZ2DCanvasView", "ZZViewComponent", "ZZAbstractView", "ZZCellRenderer", "ZZCellRenderer1", "ZZCompound", "ZZSpace", "ZZEventQueue", "ZZExec", "ZZGlobal", "ZZHash", "ZZKeyBindings", "ZZKeyBindings1", "ZZLinkRenderer", "ZZLinkRenderer1", "ZZList", "ZZLocal", "ZZPath", "ZZPhotoView", "ZZRemote", "ZZSingleCellView", "ZZTextView", "ZZTraverseCB", "ZZXML1", "ZZUpdateManager" };

    static final void p(String s) {
        System.out.println(s);
    }

    public static void main(String argv[]) {
        p("GZigZag checkversion --- my own id: " + rcsid);
        p("Main id: " + Main.rcsid);
        for (int i = 0; i < classes.length; i++) {
            try {
                String n = "org.gzigzag." + classes[i];
                p(n);
                Class c = Class.forName(n);
                Field f = c.getField("rcsid");
                p((String) f.get(null));
            } catch (Exception e) {
                p(e.toString());
            }
        }
        System.exit(0);
    }
}
