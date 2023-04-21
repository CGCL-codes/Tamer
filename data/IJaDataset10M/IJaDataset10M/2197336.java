package com.tt.bnct.dao.cache;

import java.util.Calendar;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Repository;
import com.tt.bnct.dao.PathwayEntryDao;
import com.tt.bnct.domain.Pathway;
import com.tt.bnct.domain.PathwayEntry;

@Repository("pathwayEntryDao")
public class CachingPathwayEntryDao implements PathwayEntryDao {

    private int cacheInvalidationDays;

    @Resource
    private PathwayEntryDao keggPathwayEntryDao;

    @Resource
    private PathwayEntryDao jdbcPathwayEntryDao;

    /** {@inheritDoc} */
    public void createPathwayEntry(Pathway pathway, PathwayEntry pathwayEntry) {
        jdbcPathwayEntryDao.createPathwayEntry(pathway, pathwayEntry);
    }

    /** {@inheritDoc} */
    public PathwayEntry findPathwayEntry(Pathway pathway) {
        PathwayEntry pathwayEntry = jdbcPathwayEntryDao.findPathwayEntry(pathway);
        if (pathwayEntry.getGenesEntries().isEmpty() && pathwayEntry.getPathways().isEmpty()) {
            pathwayEntry = keggPathwayEntryDao.findPathwayEntry(pathway);
            createPathwayEntry(pathway, pathwayEntry);
        } else {
            Calendar now = Calendar.getInstance();
            Calendar createDate = Calendar.getInstance();
            createDate.setTime(pathwayEntry.getGenesEntries().get(0).getCreateDate());
            createDate.add(Calendar.DAY_OF_MONTH, cacheInvalidationDays);
            if (now.after(createDate)) {
                pathwayEntry = keggPathwayEntryDao.findPathwayEntry(pathway);
                deletePathwayEntry(pathway);
                createPathwayEntry(pathway, pathwayEntry);
            }
        }
        return pathwayEntry;
    }

    /** {@inheritDoc} */
    public void deletePathwayEntry(Pathway pathway) {
        jdbcPathwayEntryDao.deletePathwayEntry(pathway);
    }

    @Required
    public void setCacheInvalidationDays(int cacheInvalidationDays) {
        this.cacheInvalidationDays = cacheInvalidationDays;
    }
}
