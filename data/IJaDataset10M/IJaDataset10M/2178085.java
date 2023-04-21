package org.mati.geotech.model.qmap;

import org.mati.geotech.model.Rect;

public class GoogleMapPathMaker extends PathMaker {

    public String makePathFor(Rect cell, Rect start) {
        String path = "t";
        Rect r = new Rect(0, 0, 0, 0);
        r.setSameGeometry(start);
        while (cell.haveOverlap(r) && r.getWidth() > cell.getWidth()) {
            Rect[] sl = r.newFourBySplit();
            if (cell.haveOverlap(sl[0])) {
                r.setSameGeometry(sl[0]);
                path += "q";
            } else if (cell.haveOverlap(sl[1])) {
                r.setSameGeometry(sl[1]);
                path += "r";
            } else if (cell.haveOverlap(sl[2])) {
                r.setSameGeometry(sl[2]);
                path += "t";
            } else if (cell.haveOverlap(sl[3])) {
                r.setSameGeometry(sl[3]);
                path += "s";
            } else {
                System.out.println("error in: " + path);
                return null;
            }
        }
        return path;
    }
}
