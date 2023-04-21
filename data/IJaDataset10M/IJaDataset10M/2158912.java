package com.performance.dao;

import com.jxva.dao.BaseDao;
import com.performance.model.AlbumDesc;

/**
 * 
 * @author  The Jxva Framework Foundation
 * @since   1.0
 * @version 2010-02-10 10:00:24 by Automatic Generate Toolkit
 */
public class AlbumDescDao extends BaseDao {

    public AlbumDesc getAlbumDesc(int albumDescId) {
        return dao.get(AlbumDesc.class, albumDescId);
    }

    public int save(AlbumDesc albumDesc) {
        return dao.save(albumDesc);
    }

    public int update(AlbumDesc albumDesc) {
        return dao.update(albumDesc);
    }

    public int delete(AlbumDesc albumDesc) {
        return dao.delete(albumDesc);
    }

    public int delete(int albumDescId) {
        return dao.delete(AlbumDesc.class, albumDescId);
    }

    public int saveOrUpdate(AlbumDesc albumDesc) {
        return dao.saveOrUpdate(albumDesc);
    }
}
