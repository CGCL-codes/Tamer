package com.homeautomate.dao;

import java.util.List;
import com.homeautomate.timerange.IGroupOfTimeRange;

/**
 * Implémentation Hibernate
 * 
 * @author Gorille
 * 
 */
public class GroupOfTimeRangeDaoDb4oImpl extends AbstractDaoDb4oImpl<IGroupOfTimeRange> implements IGroupOfTimeRangeDao, AbstractDao<IGroupOfTimeRange> {

    public List<IGroupOfTimeRange> getAllGroupOfTimeRange() {
        return list(IGroupOfTimeRange.class);
    }
}
