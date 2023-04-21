package org.gzigzag.module;

import org.gzigzag.*;
import java.util.*;
import java.awt.*;

/** An flob factory that shows awt notes.
 */
public class AwtNoteFlobFactory extends AwtArtefactFlobFactory {

    static AwtNileView awtnile = new AwtNileView();

    private AwtDraggable dragObj = null;

    public AwtNoteFlobFactory(AwtMetrics awtm) {
        super(awtm);
    }

    public AwtNoteFlobFactory() {
        super();
    }

    public void setDragging(AwtDraggable dobj) {
        dragObj = dobj;
    }

    public Flob renderFlob(FlobSet into, ZZCell c, ZZCell handleCell, float fract, int x, int y, int d, int w, int h) {
        ZZCell sp_cur = c.h("d.clone");
        if (sp_cur.s("d.cursor-cargo", -1) != null && dragObj == null) {
            ZZCell naccursed = ZZCursorReal.get(sp_cur);
            Rectangle rect = new Rectangle(x, y, w, h);
            awtnile.raster(into, sp_cur, naccursed, rect, d);
        }
        return null;
    }
}
