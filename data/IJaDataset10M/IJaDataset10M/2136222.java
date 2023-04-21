package com.luzan.db.dao;

import com.luzan.app.map.bean.publik.PublicMapOriginal;
import com.luzan.app.map.service.bean.MapRequest;
import com.luzan.bean.Pagination;
import com.luzan.bean.User;
import java.util.Collection;

/**
 * MapOriginal DAO interface.
 *
 * @author Alexander Bondar
 */
public interface PublicMapOriginalDAO extends GenericDAO<PublicMapOriginal> {

    public Collection<PublicMapOriginal> findMaps(final Pagination pagination, final MapRequest mapArea) throws DAOException;

    public Collection<PublicMapOriginal> findMaps(final MapRequest mapArea) throws DAOException;

    public Long countMaps(final MapRequest mapArea) throws DAOException;

    public Long getMapOffset(final MapRequest mapArea, final String guid) throws DAOException;
}
