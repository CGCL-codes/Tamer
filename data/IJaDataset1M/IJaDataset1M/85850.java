package org.criticalfailure.anp.core.persistence.vo.translator.impl;

import java.util.HashSet;
import java.util.Set;
import org.criticalfailure.anp.core.application.entity.Alert;
import org.criticalfailure.anp.core.application.services.AlertService;
import org.criticalfailure.anp.core.domain.entity.Curriculum;
import org.criticalfailure.anp.core.domain.entity.SchoolCalendar;
import org.criticalfailure.anp.core.domain.entity.SchoolYear;
import org.criticalfailure.anp.core.domain.entity.Student;
import org.criticalfailure.anp.core.domain.factory.ISchoolYearFactory;
import org.criticalfailure.anp.core.persistence.Messages;
import org.criticalfailure.anp.core.persistence.storage.IDomainObjectPersister;
import org.criticalfailure.anp.core.persistence.vo.CurriculumVO;
import org.criticalfailure.anp.core.persistence.vo.SchoolCalendarVO;
import org.criticalfailure.anp.core.persistence.vo.SchoolYearVO;
import org.criticalfailure.anp.core.persistence.vo.StudentVO;
import org.criticalfailure.anp.core.persistence.vo.translator.IAssignmentValueObjectTranslator;
import org.criticalfailure.anp.core.persistence.vo.translator.ICurriculumValueObjectTranslator;
import org.criticalfailure.anp.core.persistence.vo.translator.ISchoolCalendarValueObjectTranslator;
import org.criticalfailure.anp.core.persistence.vo.translator.ISchoolYearValueObjectTranslator;
import org.criticalfailure.anp.core.persistence.vo.translator.IStudentValueObjectTranslator;
import org.criticalfailure.anp.core.persistence.vo.translator.ValueObjectTranslationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author pauly
 * 
 */
@Component("schoolYearValueObjectTranslator")
public class SchoolYearValueObjectTranslatorImpl implements ISchoolYearValueObjectTranslator {

    private Logger logger;

    @Autowired
    private IDomainObjectPersister domainObjectPersister;

    @Autowired
    private AlertService alertService;

    @Autowired
    private ISchoolYearFactory schoolYearFactory;

    @Autowired
    private ISchoolCalendarValueObjectTranslator schoolCalendarValueObjectTranslator;

    @Autowired
    private ICurriculumValueObjectTranslator curriculumValueObjectTranslator;

    @Autowired
    private IStudentValueObjectTranslator studentValueObjectTranslator;

    @Autowired
    private IAssignmentValueObjectTranslator assignmentValueObjectTranslator;

    public SchoolYearValueObjectTranslatorImpl() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    public SchoolYear translateFrom(SchoolYearVO vo) throws ValueObjectTranslationException {
        SchoolYear year = (SchoolYear) domainObjectPersister.getCachedObject(vo.getId());
        if (year == null) {
            year = schoolYearFactory.createSchoolYear();
            year.setId(vo.getId());
            domainObjectPersister.cacheObject(year);
            translateFromWithMerge(vo, year);
        }
        return year;
    }

    public void translateFromWithMerge(SchoolYearVO vo, SchoolYear obj) throws ValueObjectTranslationException {
        try {
            SchoolCalendar cal = (SchoolCalendar) domainObjectPersister.getCachedObject(vo.getSchoolCalendarId());
            if (cal == null) {
                SchoolCalendarVO scVO = domainObjectPersister.loadSchoolCalendar(vo.getSchoolCalendarId());
                cal = schoolCalendarValueObjectTranslator.translateFrom(scVO);
            }
            obj.setCalendar(cal);
            cal.setSchoolYear(obj);
        } catch (Exception e) {
            logger.error("Exception caught while trying to load calendar for school year: " + e.getLocalizedMessage(), e);
            alertService.addAlert(new Alert(Alert.Type.ERROR, this.getClass().getSimpleName(), Messages.getString("school.management.translator.school_year.error.school_calendar.text") + ": " + e.getLocalizedMessage()));
        }
        Set<Curriculum> curricula = new HashSet<Curriculum>();
        for (String curId : vo.getCurriculumIds()) {
            try {
                Curriculum cur = (Curriculum) domainObjectPersister.getCachedObject(curId);
                if (cur == null) {
                    CurriculumVO curVO = domainObjectPersister.loadCurriculum(curId);
                    cur = curriculumValueObjectTranslator.translateFrom(curVO);
                }
                curricula.add(cur);
            } catch (Exception e) {
                logger.error("Exception caught while trying to load curriculum for school year: " + e.getLocalizedMessage(), e);
                alertService.addAlert(new Alert(Alert.Type.ERROR, this.getClass().getSimpleName(), Messages.getString("school.management.translator.school_year.error.curriculum.text") + ": " + e.getLocalizedMessage()));
            }
        }
        obj.setCurricula(curricula);
        Set<Student> students = new HashSet<Student>();
        for (String studentId : vo.getStudentIds()) {
            try {
                Student student = (Student) domainObjectPersister.getCachedObject(studentId);
                if (student == null) {
                    StudentVO studentVO = domainObjectPersister.loadStudent(studentId);
                    student = studentValueObjectTranslator.translateFrom(studentVO);
                }
                students.add(student);
            } catch (Exception e) {
                logger.error("Exception caught while trying to load student for school year: " + e.getLocalizedMessage(), e);
                alertService.addAlert(new Alert(Alert.Type.ERROR, this.getClass().getSimpleName(), Messages.getString("school.management.translator.school_year.error.student.text") + ": " + e.getLocalizedMessage()));
            }
        }
        obj.setStudents(students);
        for (String assignmentId : vo.getAssignmentIds()) {
            obj.addAssignmentId(assignmentId);
        }
    }

    public SchoolYearVO translateTo(SchoolYear obj) {
        SchoolYearVO vo = new SchoolYearVO();
        vo.setId(obj.getId());
        vo.setSchoolCalendarId(obj.getCalendar().getId());
        Set<String> curriculumIds = new HashSet<String>();
        for (Curriculum cur : obj.getCurricula()) {
            curriculumIds.add(cur.getId());
        }
        vo.setCurriculumIds(curriculumIds);
        Set<String> studentIds = new HashSet<String>();
        for (Student student : obj.getStudents()) {
            studentIds.add(student.getId());
        }
        vo.setStudentIds(studentIds);
        vo.setAssignmentIds(new HashSet<String>(obj.getAssignmentIds()));
        return vo;
    }

    /**
     * @return the domainObjectPersister
     */
    public IDomainObjectPersister getDomainObjectPersister() {
        return domainObjectPersister;
    }

    /**
     * @param domainObjectPersister
     *            the domainObjectPersister to set
     */
    public void setDomainObjectPersister(IDomainObjectPersister domainObjectPersister) {
        this.domainObjectPersister = domainObjectPersister;
    }

    /**
     * @return the alertService
     */
    public AlertService getAlertService() {
        return alertService;
    }

    /**
     * @param alertService
     *            the alertService to set
     */
    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
    }

    /**
     * @return the schoolYearFactory
     */
    public ISchoolYearFactory getSchoolYearFactory() {
        return schoolYearFactory;
    }

    /**
     * @param schoolYearFactory
     *            the schoolYearFactory to set
     */
    public void setSchoolYearFactory(ISchoolYearFactory schoolYearFactory) {
        this.schoolYearFactory = schoolYearFactory;
    }

    /**
     * @return the schoolCalendarValueObjectTranslator
     */
    public ISchoolCalendarValueObjectTranslator getSchoolCalendarValueObjectTranslator() {
        return schoolCalendarValueObjectTranslator;
    }

    /**
     * @param schoolCalendarValueObjectTranslator
     *            the schoolCalendarValueObjectTranslator to set
     */
    public void setSchoolCalendarValueObjectTranslator(ISchoolCalendarValueObjectTranslator schoolCalendarValueObjectTranslator) {
        this.schoolCalendarValueObjectTranslator = schoolCalendarValueObjectTranslator;
    }

    /**
     * @return the curriculumValueObjectTranslator
     */
    public ICurriculumValueObjectTranslator getCurriculumValueObjectTranslator() {
        return curriculumValueObjectTranslator;
    }

    /**
     * @param curriculumValueObjectTranslator
     *            the curriculumValueObjectTranslator to set
     */
    public void setCurriculumValueObjectTranslator(ICurriculumValueObjectTranslator curriculumValueObjectTranslator) {
        this.curriculumValueObjectTranslator = curriculumValueObjectTranslator;
    }

    /**
     * @return the studentValueObjectTranslator
     */
    public IStudentValueObjectTranslator getStudentValueObjectTranslator() {
        return studentValueObjectTranslator;
    }

    /**
     * @param studentValueObjectTranslator
     *            the studentValueObjectTranslator to set
     */
    public void setStudentValueObjectTranslator(IStudentValueObjectTranslator studentValueObjectTranslator) {
        this.studentValueObjectTranslator = studentValueObjectTranslator;
    }

    /**
     * @return the assignmentValueObjectTranslator
     */
    public IAssignmentValueObjectTranslator getAssignmentValueObjectTranslator() {
        return assignmentValueObjectTranslator;
    }

    /**
     * @param assignmentValueObjectTranslator
     *            the assignmentValueObjectTranslator to set
     */
    public void setAssignmentValueObjectTranslator(IAssignmentValueObjectTranslator assignmentValueObjectTranslator) {
        this.assignmentValueObjectTranslator = assignmentValueObjectTranslator;
    }
}
