package net.openchrom.chromatogram.msd.classifier.supplier.wnc.ui.internal.provider;

import net.openchrom.chromatogram.msd.classifier.supplier.wnc.model.IWncIon;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class WncResultsTableSorter extends ViewerSorter {

    private int propertyIndex;

    private static final int ASCENDING = 0;

    private int direction = ASCENDING;

    public WncResultsTableSorter() {
        propertyIndex = 0;
        direction = ASCENDING;
    }

    public void setColumn(int column) {
        if (column == this.propertyIndex) {
            direction = 1 - direction;
        } else {
            this.propertyIndex = column;
            direction = ASCENDING;
        }
    }

    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        IWncIon wnc1 = (IWncIon) e1;
        IWncIon wnc2 = (IWncIon) e2;
        int sortOrder;
        switch(propertyIndex) {
            case 0:
                sortOrder = (wnc2.getName().codePointAt(0) > wnc1.getName().codePointAt(0)) ? 1 : -1;
                break;
            case 1:
                sortOrder = (wnc2.getIon() > wnc1.getIon()) ? 1 : -1;
                break;
            case 2:
                sortOrder = (wnc2.getPercentageSumIntensity() > wnc1.getPercentageSumIntensity()) ? 1 : -1;
                break;
            case 3:
                sortOrder = (wnc2.getPercentageMaxIntensity() > wnc1.getPercentageMaxIntensity()) ? 1 : -1;
                break;
            default:
                sortOrder = 0;
        }
        if (direction == ASCENDING) {
            sortOrder = -sortOrder;
        }
        return sortOrder;
    }
}
