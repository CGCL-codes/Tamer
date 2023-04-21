package com.m4f.business.service.extended.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import com.google.appengine.api.datastore.Category;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.extended.ExtendedCourse;
import com.m4f.business.domain.extended.ExtendedSchool;
import com.m4f.business.service.extended.ifc.I18nExtendedCourseService;
import com.m4f.business.service.impl.I18nDAOBaseService;
import com.m4f.utils.i18n.dao.ifc.I18nDAOSupport;

public class ExtendedCourseServiceImpl extends I18nDAOBaseService implements I18nExtendedCourseService {

    private static final Logger LOGGER = Logger.getLogger(ExtendedCourseServiceImpl.class.getName());

    public ExtendedCourseServiceImpl(I18nDAOSupport dao) {
        super(dao);
    }

    @Override
    public ExtendedCourse createCourse() {
        return this.DAO.createInstance(ExtendedCourse.class);
    }

    @Override
    public long countCourses() throws Exception {
        return this.DAO.count(ExtendedCourse.class);
    }

    @Override
    public long countCoursesByOwner(Long mediationService) throws Exception {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("mediationService", mediationService);
        return this.DAO.count(ExtendedCourse.class, filter);
    }

    @Override
    public long countCoursesBySchool(ExtendedSchool school) throws Exception {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("school", school.getId());
        return this.DAO.count(ExtendedCourse.class, filter);
    }

    @Override
    public void delete(ExtendedCourse course, Locale locale) throws Exception {
        this.DAO.delete(course, locale);
    }

    @Override
    public void deleteLogic(ExtendedCourse course, Locale locale) throws Exception {
        course.setActive(false);
        this.DAO.saveOrUpdate(course, locale);
    }

    @Override
    public void deleteLogicBySchool(Long schoolId, Locale locale) throws Exception {
        Collection<ExtendedCourse> courses = this.getCoursesBySchool(schoolId, null, locale);
        for (ExtendedCourse course : courses) {
            this.deleteLogic(course, locale);
        }
    }

    @Override
    public void erasure() throws Exception {
        this.DAO.erasure(ExtendedCourse.class);
    }

    @Override
    public Collection<ExtendedCourse> getActiveCourses(String ordering, Locale locale) throws Exception {
        return this.DAO.findEntities(ExtendedCourse.class, locale, "active == activeParam", "Boolean activeParam", new Boolean[] { Boolean.TRUE }, null);
    }

    @Override
    public Collection<ExtendedCourse> getAllCourses(String ordering, Locale locale) throws Exception {
        return this.DAO.findAll(ExtendedCourse.class, locale, ordering);
    }

    @Override
    public ExtendedCourse getCourse(Long id, Locale locale) throws Exception {
        return this.DAO.findById(ExtendedCourse.class, locale, id);
    }

    @Override
    public Collection<ExtendedCourse> getCourses(String ordering, Locale locale, int init, int end) throws Exception {
        return this.DAO.findEntitiesByRange(ExtendedCourse.class, locale, init, end, ordering);
    }

    @Override
    public Collection<ExtendedCourse> getCoursesByOwner(Long mediationService, String ordering, Locale locale, int init, int end) {
        return this.findCoursesByMediatorlId(mediationService, ordering, locale, init, end);
    }

    @Override
    public Collection<ExtendedCourse> getCoursesByOwner(Long mediationService, String ordering, Locale locale) {
        return this.findCoursesByMediatorlId(mediationService, ordering, locale);
    }

    @Override
    public Collection<ExtendedCourse> getCoursesBySchool(Long schoolId, String ordering, Locale locale, int init, int end) {
        return this.findCoursesBySchoolId(schoolId, ordering, locale, init, end);
    }

    @Override
    public Collection<ExtendedCourse> getCoursesBySchool(Long schoolId, String ordering, Locale locale) {
        return this.findCoursesBySchoolId(schoolId, ordering, locale);
    }

    @Override
    public Collection<ExtendedCourse> getCoursesBySchool(ExtendedSchool school, String ordering, Locale locale) {
        return this.findCoursesBySchoolId(school.getId(), ordering, locale);
    }

    @Override
    public Collection<ExtendedCourse> getCoursesBySchoolByMediation(Long schoolId, Long mediationService, String ordering, Locale locale) {
        return this.findCoursesByMediatorlAndSchool(schoolId, mediationService, ordering, locale);
    }

    @Override
    public Collection<ExtendedCourse> getCoursesBySchool(ExtendedSchool school, String ordering, Locale locale, int init, int end) {
        return this.findCoursesBySchoolId(school.getId(), ordering, locale);
    }

    @Override
    public Collection<ExtendedCourse> getNewCourses(String ordering, Locale locale) {
        return this.DAO.findEntities(ExtendedCourse.class, locale, "created == updated && active == activeParam", "Boolean activeParam", new Boolean[] { Boolean.TRUE }, ordering);
    }

    @Override
    public void save(ExtendedCourse course, Locale locale) throws Exception {
        this.DAO.saveOrUpdate(course, locale);
    }

    public List<Category> getCoursesTags(Locale locale) {
        HashMap<String, Category> mapa = new HashMap<String, Category>();
        Collection<Category> collection = this.DAO.getCategories(ExtendedCourse.class, "tags", locale);
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            Set set = (Set) it.next();
            Object[] array = set.toArray();
            for (Object o : array) {
                if (o instanceof Category) {
                    Category c = (Category) o;
                    mapa.put(c.getCategory(), c);
                }
            }
        }
        return new ArrayList<Category>(mapa.values());
    }

    public long countCoursesByTowns(Long townId) throws Exception {
        return 0;
    }

    public Collection<ExtendedCourse> getCoursesByTowns(List<Long> townIds, Locale locale) throws Exception {
        ArrayList<ExtendedCourse> courses = new ArrayList<ExtendedCourse>();
        return courses;
    }

    /**************************************************************************
	 * 
	 *							PRIVATE METHODS
	 *
	 **************************************************************************/
    private Collection<ExtendedCourse> findCoursesBySchoolId(Long schoolId, String ordering, Locale locale) {
        return this.DAO.findEntities(ExtendedCourse.class, locale, "school == schoolParam", "Long schoolParam", new Object[] { schoolId }, ordering);
    }

    private Collection<ExtendedCourse> findCoursesBySchoolId(Long schoolId, String ordering, Locale locale, int init, int end) {
        String filter = "school == schoolParam";
        String params = "java.lang.Long schoolParam";
        return this.DAO.findEntitiesByRange(ExtendedCourse.class, locale, filter, params, new Long[] { schoolId }, init, end, ordering);
    }

    private Collection<ExtendedCourse> findCoursesByMediatorlId(Long mediatorId, String ordering, Locale locale, int init, int end) {
        String filter = "mediationService == mediatorParam";
        String params = "java.lang.Long mediatorParam";
        return this.DAO.findEntitiesByRange(ExtendedCourse.class, locale, filter, params, new Long[] { mediatorId }, init, end, ordering);
    }

    private Collection<ExtendedCourse> findCoursesByMediatorlId(Long mediatorId, String ordering, Locale locale) {
        String filter = "mediationService == mediatorParam";
        String params = "java.lang.Long mediatorParam";
        return this.DAO.findEntities(ExtendedCourse.class, locale, filter, params, new Long[] { mediatorId }, ordering);
    }

    private Collection<ExtendedCourse> findCoursesByMediatorlAndSchool(Long schoolId, Long mediatorId, String ordering, Locale locale) {
        String filter = "school == schoolParam && mediationService == mediatorParam";
        String params = "java.lang.Long schoolParam, java.lang.Long mediatorParam";
        return this.DAO.findEntities(ExtendedCourse.class, locale, filter, params, new Long[] { schoolId, mediatorId }, ordering);
    }
}
