package com.kongur.star.venus.dao.system.ibatis;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.kongur.star.venus.dao.BaseDAO;
import com.kongur.star.venus.dao.system.YearDAO;
import com.kongur.star.venus.domain.courses.CourseYearDO;
import com.kongur.star.venus.domain.system.YearDO;

@Repository("yearDAO")
public class YearDAOImpl extends BaseDAO<YearDO> implements YearDAO {

    @Override
    public Integer insertYear(YearDO yearDO) {
        return (Integer) this.getSqlMapClientTemplate().insert("YearDAO.insertYear", yearDO);
    }

    @Override
    public Integer updateYear(YearDO yearDO) {
        return this.getSqlMapClientTemplate().update("YearDAO.updateYear", yearDO);
    }

    @Override
    public YearDO selectYearById(Integer year) {
        return (YearDO) this.getSqlMapClientTemplate().queryForObject("YearDAO.selectYearById", year);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<YearDO> getYearList(Map<String, Object> parms) {
        return this.getSqlMapClientTemplate().queryForList("YearDAO.getYearList", parms);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<YearDO> getYearListForQuery(Map<String, Object> parms) {
        return this.getSqlMapClientTemplate().queryForList("YearDAO.getYearListForQuery", parms);
    }

    @Override
    public int deleteYear(Map<String, Object> params) {
        return this.getSqlMapClientTemplate().delete("YearDAO.deleteYear", params);
    }

    @Override
    public int selectYearCountById(Integer year) {
        return (Integer) this.getSqlMapClientTemplate().queryForObject("YearDAO.selectYearCountById", year);
    }

    @Override
    public int resetCourseYear(Map<String, Object> params) {
        return this.getSqlMapClientTemplate().update("YearDAO.resetCourseYear", params);
    }

    @Override
    public Long insertCourseYear(CourseYearDO courseYear) {
        return (Long) this.getSqlMapClientTemplate().insert("YearDAO.insertCourseYear", courseYear);
    }

    @Override
    public int applicationCount(int year) {
        return (Integer) this.getSqlMapClientTemplate().queryForObject("YearDAO.application-count", year);
    }
}
