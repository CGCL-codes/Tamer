package org.gruposp2p.aularest.service;

import java.util.Collection;
import java.util.List;
import org.gruposp2p.aularest.model.Absence;
import org.gruposp2p.aularest.model.Address;
import org.gruposp2p.aularest.model.Competence;
import org.gruposp2p.aularest.model.Course;
import org.gruposp2p.aularest.model.Coursegroup;
import org.gruposp2p.aularest.model.Itemcalificable;
import org.gruposp2p.aularest.model.Itemcalificabletype;
import org.gruposp2p.aularest.model.Score;
import org.gruposp2p.aularest.model.Student;
import org.gruposp2p.aularest.model.Subject;
import org.gruposp2p.aularest.model.Tutor;

/**
 *
 * @author jj
 */
public interface AulaDataService {

    public static String COURSE_NOT_FOUND_MESSAGE = "The course doesn't exist";

    public static String COURSEGROUP_NOT_FOUND_MESSAGE = "The coursegroup doesn't exist";

    public static String ABSENCE_NOT_FOUND_MESSAGE = "The absence doesn't exist";

    public static String TUTOR_NOT_FOUND_MESSAGE = "The tutor doesn't exist";

    public static String ADDRESS_NOT_FOUND_MESSAGE = "The address doesn't exist";

    public static String STUDENT_NOT_FOUND_MESSAGE = "The student doesn't exist";

    public static String ITEMCALIFICABLE_NOT_FOUND_MESSAGE = "The itemcalificable doesn't exist";

    public static String ITEMCALIFICABLETYPE_NOT_FOUND_MESSAGE = "The itemcalificabletype doesn't exist";

    public static String SUBJECT_NOT_FOUND_MESSAGE = "The subject doesn't exist";

    public static String SCORE_NOT_FOUND_MESSAGE = "The score doesn't exist";

    public static String COMPETENCE_NOT_FOUND_MESSAGE = "The competence doesn't exist";

    public Course findCourse(Integer id);

    public List<Course> findAllCourses();

    public Course createCourse(Course course) throws Exception;

    public Collection<Course> createCourses(Collection<Course> courses) throws Exception;

    public void putCourse(Course course, Course xmlUpdatedCourse) throws Exception;

    public void destroyCourse(Course course) throws Exception;

    public Coursegroup findCoursegroup(Integer id);

    public List<Coursegroup> findAllCoursegroups();

    public Coursegroup createCoursegroup(Coursegroup coursegroup) throws Exception;

    public Collection<Coursegroup> createCoursegroups(Collection<Coursegroup> coursegroups) throws Exception;

    public void putCoursegroup(Coursegroup coursegroup, Coursegroup xmlUpdatedCoursegroup) throws Exception;

    public void destroyCoursegroup(Coursegroup coursegroup) throws Exception;

    ;

    public Absence findAbsence(Integer id);

    public List<Absence> findAllAbsences();

    public Absence createAbsence(Absence absence) throws Exception;

    public Collection<Absence> createAbsences(Collection<Absence> absence) throws Exception;

    public void putAbsence(Absence absence, Absence xmlUpdatedAbsence) throws Exception;

    public void destroyAbsence(Absence absence) throws Exception;

    public Address findAddress(Integer id);

    public List<Address> findAllAddresses();

    public Address createAddress(Address address) throws Exception;

    public Collection<Address> createAddresses(Collection<Address> address) throws Exception;

    public void putAddress(Address address, Address xmlUpdatedAddress) throws Exception;

    public void destroyAddress(Address address) throws Exception;

    public Competence findCompetence(Integer id);

    public List<Competence> findAllCompetences();

    public Competence createCompetence(Competence competence) throws Exception;

    public Collection<Competence> createCompetences(Collection<Competence> competences) throws Exception;

    public void putCompetence(Competence competence, Competence xmlUpdatedCompetence) throws Exception;

    public void destroyCompetence(Competence competence) throws Exception;

    public Itemcalificable findItemcalificable(Integer id);

    public List<Itemcalificable> findAllItemcalificables();

    public Itemcalificable createItemcalificable(Itemcalificable itemcalificable) throws Exception;

    public Collection<Itemcalificable> createItemcalificables(Collection<Itemcalificable> itemcalificables) throws Exception;

    public void putItemcalificable(Itemcalificable itemcalificable, Itemcalificable xmlUpdatedItemcalificable) throws Exception;

    public void destroyItemcalificable(Itemcalificable itemcalificable) throws Exception;

    public Itemcalificabletype findItemcalificabletype(Integer id);

    public List<Itemcalificabletype> findAllItemcalificabletypes();

    public Itemcalificabletype createItemcalificabletype(Itemcalificabletype itemcalificabletype) throws Exception;

    public Collection<Itemcalificabletype> createItemcalificabletypes(Collection<Itemcalificabletype> itemcalificabletypes) throws Exception;

    public void putItemcalificabletype(Itemcalificabletype itemcalificabletype, Itemcalificabletype xmlUpdatedItemcalificabletype) throws Exception;

    public void destroyItemcalificabletype(Itemcalificabletype itemcalificabletype) throws Exception;

    public Score findScore(Integer id);

    public List<Score> findAllScores();

    public Score createScore(Score score) throws Exception;

    public Collection<Score> createScores(Collection<Score> scores) throws Exception;

    public void putScore(Score score, Score xmlUpdatedScore) throws Exception;

    public void destroyScore(Score score) throws Exception;

    public Student findStudent(Integer id);

    public List<Student> findAllStudents();

    public Student createStudent(Student student) throws Exception;

    public Collection<Student> createStudents(Collection<Student> Students) throws Exception;

    public void putStudent(Student studentOri, Student xmlUpdatedStudent) throws Exception;

    public void destroyStudent(Student student) throws Exception;

    public Subject findSubject(Integer id);

    public List<Subject> findAllSubjects();

    public Subject createSubject(Subject subject) throws Exception;

    public Collection<Subject> createSubjects(Collection<Subject> subjects) throws Exception;

    public void putSubject(Subject subjectOri, Subject xmlUpdatedSubject) throws Exception;

    public void destroySubject(Subject subject) throws Exception;

    public Tutor findTutor(Integer id);

    public List<Tutor> findAllTutors();

    public Tutor createTutor(Tutor tutor) throws Exception;

    public Collection<Tutor> createTutors(Collection<Tutor> tutors) throws Exception;

    public void putTutor(Tutor tutorOri, Tutor xmlUpdatedTutor) throws Exception;

    public void destroyTutor(Tutor tutor) throws Exception;
}
