package org.ais.convert.hir;

import java.util.ArrayList;
import org.ais.convert.Region;

public class HirsContext {

    ArrayList hirs = new ArrayList();

    ArrayList sortedHirs = new ArrayList();

    CMRegion biggestHir;

    int biggestHirIndex = -1;

    public void addHir(CMRegion hir) {
        hirs.add(hir);
        int hirsIndex = hirs.size() - 1;
        hir.indexInNaturalList = hirsIndex;
        if (biggestHir == null) {
            biggestHir = hir;
        } else {
            if (hir.getCount() > biggestHir.getCount()) {
                biggestHir = hir;
            }
        }
        biggestHirIndex = hirsIndex;
    }
}
