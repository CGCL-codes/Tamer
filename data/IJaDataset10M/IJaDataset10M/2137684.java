package com.gnizr.db.dao.bookmark;

import java.io.Serializable;
import java.util.List;
import com.gnizr.db.dao.Bookmark;
import com.gnizr.db.dao.DaoResult;
import com.gnizr.db.dao.Folder;
import com.gnizr.db.dao.PointMarker;
import com.gnizr.db.dao.User;

public interface GeometryMarkerDao extends Serializable {

    public int createPointMarker(PointMarker pm);

    public PointMarker getPointMarker(int id);

    public boolean updatePointMarker(PointMarker pm);

    public boolean deletePointMarker(int id);

    public boolean addPointMarker(Bookmark bm, PointMarker ptMarker);

    public boolean removePointMarker(Bookmark bm, PointMarker ptMarker);

    public List<PointMarker> listPointMarkers(Bookmark bm);

    public DaoResult<Bookmark> pageBookmarksInFolder(Folder folder, int offset, int count);

    public DaoResult<Bookmark> pageBookmarksInArchive(User user, int offset, int count);
}
