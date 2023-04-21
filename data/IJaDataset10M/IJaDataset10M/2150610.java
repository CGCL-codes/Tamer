package com.mkk.kenji1016.dao;

import com.mkk.kenji1016.domain.Activity;
import com.mkk.kenji1016.domain.ActivityCategory;
import java.util.List;

/**
 * User: mkk
 * Date: 11-7-28
 * Time: 下午10:31
 */
public interface ActivityDao extends Dao<Activity> {

    List<ActivityCategory> findAvailableCategories(Long userId);
}
