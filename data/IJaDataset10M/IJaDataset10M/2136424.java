package net.tourbook.ui.views.tourBook;

import java.util.ArrayList;
import java.util.HashSet;
import net.tourbook.database.TourDatabase;
import net.tourbook.ui.TreeViewerItem;

public class TVITourBookTour extends TVITourBookItem {

    long tourId;

    long tourTypeId;

    long colStartDistance;

    short colTimeInterval;

    HashSet<Long> sqlTagIds;

    HashSet<Long> sqlMarkerIds;

    /**
	 * id's for the tags or <code>null</code> when tags are not available
	 */
    private ArrayList<Long> _tagIds;

    /**
	 * id's for the markers or <code>null</code> when markers are not available
	 */
    private ArrayList<Long> _markerIds;

    public TVITourBookTour(final TourBookView view, final TreeViewerItem parentItem) {
        super(view);
        setParentItem(parentItem);
    }

    @Override
    protected void fetchChildren() {
    }

    public long getColumnStartDistance() {
        return colStartDistance;
    }

    public short getColumnTimeInterval() {
        return colTimeInterval;
    }

    public ArrayList<Long> getMarkerIds() {
        if (sqlMarkerIds != null && _markerIds == null) {
            _markerIds = new ArrayList<Long>(sqlMarkerIds);
        }
        return _markerIds;
    }

    public ArrayList<Long> getTagIds() {
        if (sqlTagIds != null && _tagIds == null) {
            _tagIds = new ArrayList<Long>(sqlTagIds);
        }
        return _tagIds;
    }

    @Override
    public Long getTourId() {
        return tourId;
    }

    /**
	 * @return Returns the tour type id of the tour or {@link TourDatabase#ENTITY_IS_NOT_SAVED} when
	 *         the tour type is not set.
	 */
    public long getTourTypeId() {
        return tourTypeId;
    }

    /**
	 * tour items do not have children
	 */
    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    protected void remove() {
    }

    public void setMarkerIds(final HashSet<Long> markerIds) {
        sqlMarkerIds = markerIds;
    }

    public void setTagIds(final HashSet<Long> tagIds) {
        sqlTagIds = tagIds;
    }
}
