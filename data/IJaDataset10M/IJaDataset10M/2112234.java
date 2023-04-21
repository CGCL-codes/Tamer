package com.web.robot.interfaces;

import java.util.List;
import com.web.robot.model.Bookmark;

/**
 * Bookmark database access service.
 * 
 * @author avasiljeva
 */
public interface BookmarkService {

    public void save(Bookmark bookmark);

    public List<Bookmark> findAll();
}
