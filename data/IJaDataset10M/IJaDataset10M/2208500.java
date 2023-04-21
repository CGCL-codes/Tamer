package com.sks.dao.house.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.sks.bean.pojo.Favorites;
import com.sks.dao.base.DaoSupport;
import com.sks.dao.house.FavoritesDao;

@Repository("favoritesDao")
@Transactional
public class FavoritesDaoImpl extends DaoSupport<Favorites> implements FavoritesDao {
}
