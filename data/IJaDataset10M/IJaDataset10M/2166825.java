package net.tourbook.tag;

import net.tourbook.data.TourTag;
import org.eclipse.jface.viewers.TreeViewer;

public class TVIPrefTag extends TVIPrefTagItem {

    private TourTag fTourTag;

    public TVIPrefTag(final TreeViewer tagViewer, final TourTag tourTag) {
        super(tagViewer);
        fTourTag = tourTag;
    }

    @Override
    protected void fetchChildren() {
    }

    public TourTag getTourTag() {
        return fTourTag;
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    protected void remove() {
    }

    public void setTourTag(final TourTag savedTag) {
        fTourTag = savedTag;
    }
}
