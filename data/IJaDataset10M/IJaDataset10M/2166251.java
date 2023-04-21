package org.columba.core.print;

import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class cHGroup extends cPrintObject {

    List members;

    public cHGroup() {
        members = new Vector();
    }

    public void add(cPrintObject po) {
        po.setType(cPrintObject.GROUPMEMBER);
        members.add(po);
    }

    public void print(Graphics2D g) {
        cPrintObject act;
        computePositionAndSize();
        for (Iterator it = members.iterator(); it.hasNext(); ) {
            act = (cPrintObject) it.next();
            act.setLocation((cPoint) getDrawingOrigin().clone());
            act.setPage(page);
            act.print(g);
        }
    }

    public cSize getSize(cUnit width) {
        cUnit maxHeight = new cCmUnit();
        cSize act;
        for (int i = 0; i < members.size(); i++) {
            act = ((cPrintObject) members.get(i)).getSize(width);
            if (act.getHeight().getPoints() > maxHeight.getPoints()) {
                maxHeight = act.getHeight();
            }
        }
        maxHeight.addI(topMargin);
        maxHeight.addI(bottomMargin);
        return new cSize(new cCmUnit(), maxHeight);
    }
}
