package com.kongur.star.venus.dao.courses.impl;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.kongur.star.venus.common.page.Paginable;
import com.kongur.star.venus.dao.BaseDAO;
import com.kongur.star.venus.dao.courses.GroupCoursesRelaDAO;
import com.kongur.star.venus.domain.courses.CoursesDO;
import com.kongur.star.venus.domain.courses.GroupCoursesRelaDO;

@Repository("groupCoursesRelaDAO")
public class GroupCoursesRelaDAOImpl extends BaseDAO<GroupCoursesRelaDO> implements GroupCoursesRelaDAO {

    @Override
    public Long insertGroupCoursesRela(GroupCoursesRelaDO groupCoursesRelaDO) {
        return (Long) executeInsert("GroupCoursesRelaDAO.insertGroupCoursesRela", groupCoursesRelaDO);
    }

    @Override
    public Integer updateGroupCoursesRela(GroupCoursesRelaDO groupCoursesRelaDO) {
        return executeUpdate("GroupCoursesRelaDAO.updateGroupCoursesRela", groupCoursesRelaDO);
    }

    @Override
    public List<GroupCoursesRelaDO> selectGroupCoursesRela(GroupCoursesRelaDO groupCoursesRelaDO) {
        return this.selectObject("GroupCoursesRelaDAO.selectGroupCoursesRela", groupCoursesRelaDO);
    }

    @Override
    public GroupCoursesRelaDO selectGroupCoursesRelaById(Long id) {
        return null;
    }

    @Override
    public Paginable<GroupCoursesRelaDO> selectGroupCoursesRelaForPagin(Map<Object, Object> params, String pageNum, String pageSize) {
        return null;
    }

    @Override
    public List<CoursesDO> getCoursesByGroupId(Long groupId) {
        return getSqlMapClientTemplate().queryForList("GroupCoursesRelaDAO.getCoursesByGroupId", groupId);
    }

    @Override
    public int batchInsert(final List<GroupCoursesRelaDO> insertList) {
        return super.batchInsert("GroupCoursesRelaDAO.insertGroupCoursesRela", insertList);
    }

    @Override
    public int deleteGroupCoursesByGroupId(Long groupId) {
        return getSqlMapClientTemplate().delete("GroupCoursesRelaDAO.deleteGroupCoursesByGroupId", groupId);
    }

    @Override
    public List<CoursesDO> getAllCoursesByInGroup() {
        return getSqlMapClientTemplate().queryForList("GroupCoursesRelaDAO.getAllCoursesByInGroup");
    }
}
