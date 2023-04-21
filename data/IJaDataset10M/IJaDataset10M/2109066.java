package com.luzan.db.dao;

import com.luzan.app.map.bean.MapFilter;
import com.luzan.app.map.bean.user.UserMapPoint;
import com.luzan.app.map.service.bean.MapRequest;
import com.luzan.bean.Pagination;
import com.luzan.bean.User;
import java.util.Collection;

/**
 * MapPoint DAO interface.
 *
 * @author Alexander Bondar
 */
public interface UserMapPointDAO extends GenericDAO<UserMapPoint> {

    public Collection<UserMapPoint> findPoints(User user, final Pagination pagination, MapRequest mapArea) throws DAOException;

    public Long countPoints(User user, MapRequest mapArea) throws DAOException;
}
