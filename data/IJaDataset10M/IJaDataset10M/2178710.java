package org.gruposp2p.aularest.service;

import java.util.List;
import org.gruposp2p.aularest.model.Absence;
import org.gruposp2p.aularest.model.Address;
import org.gruposp2p.aularest.model.Competence;
import org.gruposp2p.aularest.model.Itemcalificable;
import org.gruposp2p.aularest.model.Itemcalificabletype;
import org.gruposp2p.aularest.model.Link;
import org.gruposp2p.aularest.model.Score;
import org.gruposp2p.aularest.model.Student;
import org.gruposp2p.aularest.model.Subject;
import org.gruposp2p.aularest.model.Tutor;
import org.gruposp2p.aularest.model.controller.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import org.gruposp2p.aularest.model.Course;
import org.gruposp2p.aularest.model.Coursegroup;
import org.gruposp2p.aularest.utils.AulaLogger;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

/**
 *
 * @author jj
 */
public class AulaDataServiceImpl implements AulaDataService {

    private static final Logger logger = AulaLogger.getLogger(AulaDataServiceImpl.class.getName());

    private AbsenceJpaController absenceJpaController;

    private AddressJpaController addressJpaController;

    private CompetenceJpaController competenceJpaController;

    private CourseJpaController courseJpaController;

    private CoursegroupJpaController coursegroupJpaController;

    private ItemcalificableJpaController itemcalificableJpaController;

    private ItemcalificabletypeJpaController itemcalificabletypeJpaController;

    private ScoreJpaController scoreJpaController;

    private StudentJpaController studentJpaController;

    private SubjectJpaController subjectJpaController;

    private TutorJpaController tutorJpaController;

    public AulaDataServiceImpl() {
        courseJpaController = new CourseJpaController();
        courseJpaController.findCourseEntities();
        subjectJpaController = new SubjectJpaController();
        subjectJpaController.findSubjectEntities();
    }

    public void destroyCoursegroupCollection(Collection<Coursegroup> coursegroupCollection) throws Exception {
        if (coursegroupCollection != null && !coursegroupCollection.isEmpty()) {
            for (Coursegroup coursegroup : coursegroupCollection) {
                destroyCoursegroup(coursegroup);
            }
        }
    }

    public static Collection<Coursegroup> getCoursegroupCollection(Coursegroup Coursegroup) {
        Collection<Coursegroup> CoursegroupCollection = new ArrayList<Coursegroup>();
        CoursegroupCollection.add(Coursegroup);
        return CoursegroupCollection;
    }

    public Coursegroup findCoursegroup(Integer id) {
        return getCoursegroupJpaController().findCoursegroup(id);
    }

    public void destroyCourseCollection(Collection<Course> courseCollection) throws Exception {
        if (courseCollection != null && !courseCollection.isEmpty()) {
            for (Course course : courseCollection) {
                destroyCoursegroupCollection(course.getCoursegroupCollection());
                destroyCourse(course);
            }
        }
    }

    public static Collection<Course> getCourseCollection(Course course) {
        Collection<Course> CourseCollection = new ArrayList<Course>();
        CourseCollection.add(course);
        return CourseCollection;
    }

    public void destroyCourse(Course course) throws Exception {
        getCourseJpaController().destroy(course.getId());
    }

    public Course findCourse(Integer id) {
        return getCourseJpaController().findCourse(id);
    }

    public void destroyCoursegroup(Coursegroup coursegroup) throws Exception {
        getCoursegroupJpaController().destroy(coursegroup.getId());
    }

    public void putCourse(Course course, Course xmlUpdatedCourse) throws Exception {
        course.setDescription(xmlUpdatedCourse.getDescription());
        course.setName(xmlUpdatedCourse.getName());
        course.setLink(xmlUpdatedCourse.getLink());
        getCourseJpaController().edit(course);
    }

    public void putCoursegroup(Coursegroup coursegroup, Coursegroup xmlUpdatedCoursegroup) throws Exception {
        if (!coursegroup.getCourseId().toString().equals(xmlUpdatedCoursegroup.getCourseLink().getId())) {
            Course newCourse = findCourse(new Integer(xmlUpdatedCoursegroup.getCourseLink().getId()));
            if (newCourse == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, COURSE_NOT_FOUND_MESSAGE);
            coursegroup.setCourseId(newCourse);
        }
        coursegroup.setDescription(xmlUpdatedCoursegroup.getDescription());
        coursegroup.setName(xmlUpdatedCoursegroup.getName());
        coursegroup.setLink(xmlUpdatedCoursegroup.getLink());
        getCoursegroupJpaController().edit(coursegroup);
    }

    public List<Course> findAllCourses() {
        return getCourseJpaController().findCourseEntities();
    }

    public List<Coursegroup> findAllCoursegroups() {
        return getCoursegroupJpaController().findCoursegroupEntities();
    }

    public Course createCourse(Course course) throws Exception {
        getCourseJpaController().create(course);
        return course;
    }

    public Coursegroup createCoursegroup(Coursegroup Coursegroup) throws Exception {
        getCoursegroupJpaController().create(Coursegroup);
        return Coursegroup;
    }

    public Collection<Coursegroup> createCoursegroups(Collection<Coursegroup> coursegroups) throws Exception {
        Collection<Coursegroup> coursegroupCreatedCollection = new ArrayList<Coursegroup>();
        for (Coursegroup coursegroup : coursegroups) {
            Course course = findCourse(new Integer(coursegroup.getCourseLink().getId()));
            if (course == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, COURSE_NOT_FOUND_MESSAGE);
            coursegroup.setCourseId(course);
            Coursegroup createdCoursegroup = createCoursegroup(coursegroup);
            coursegroupCreatedCollection.add(createdCoursegroup);
        }
        return coursegroupCreatedCollection;
    }

    public Collection<Course> createCourses(Collection<Course> courses) throws Exception {
        Collection<Course> courseCreatedCollection = new ArrayList<Course>();
        for (Course Course : courses) {
            Course createdCourse = createCourse(Course);
            courseCreatedCollection.add(createdCourse);
        }
        return courseCreatedCollection;
    }

    public static Collection<Absence> getAbsenceCollection(Absence absence) {
        Collection<Absence> absenceCollection = new ArrayList<Absence>();
        absenceCollection.add(absence);
        return absenceCollection;
    }

    public Absence findAbsence(Integer id) {
        return getAbsenceJpaController().findAbsence(id);
    }

    public List<Absence> findAllAbsences() {
        return getAbsenceJpaController().findAbsenceEntities();
    }

    public Absence createAbsence(Absence absence) throws Exception {
        getAbsenceJpaController().create(absence);
        return absence;
    }

    public Collection<Absence> createAbsences(Collection<Absence> absences) throws Exception {
        Collection<Absence> absenceCreatedCollection = new ArrayList<Absence>();
        for (Absence absence : absences) {
            absence.setJustified(absence.getIsJustified());
            absence.setDate(absence.getXmlDate().toGregorianCalendar().getTime());
            Student student = findStudent(new Integer(absence.getStudentLink().getId()));
            if (student == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, STUDENT_NOT_FOUND_MESSAGE);
            absence.setStudentId(student);
            Subject subject = findSubject(new Integer(absence.getSubjectLink().getId()));
            if (subject == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, SUBJECT_NOT_FOUND_MESSAGE);
            absence.setSubjectId(subject);
            Absence createdAbsence = createAbsence(absence);
            absenceCreatedCollection.add(createdAbsence);
        }
        return absenceCreatedCollection;
    }

    public void putAbsence(Absence absence, Absence xmlUpdatedAbsence) throws Exception {
        absence.setDate(xmlUpdatedAbsence.getXmlDate().toGregorianCalendar().getTime());
        absence.setDescription(xmlUpdatedAbsence.getDescription());
        absence.setJustified(xmlUpdatedAbsence.getIsJustified());
        Student student = findStudent(new Integer(xmlUpdatedAbsence.getStudentLink().getId()));
        if (student == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, STUDENT_NOT_FOUND_MESSAGE);
        absence.setStudentId(student);
        Subject subject = findSubject(new Integer(xmlUpdatedAbsence.getSubjectLink().getId()));
        if (subject == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, SUBJECT_NOT_FOUND_MESSAGE);
        absence.setSubjectId(subject);
        absence.setLink(xmlUpdatedAbsence.getLink());
        getAbsenceJpaController().edit(absence);
    }

    public void destroyAbsence(Absence absence) throws Exception {
        getAbsenceJpaController().destroy(absence.getId());
    }

    public static Collection<Address> getAbsenceCollection(Address address) {
        Collection<Address> addressCollection = new ArrayList<Address>();
        addressCollection.add(address);
        return addressCollection;
    }

    public Address findAddress(Integer id) {
        return getAddressJpaController().findAddress(id);
    }

    public List<Address> findAllAddresses() {
        return getAddressJpaController().findAddressEntities();
    }

    public Address createAddress(Address address) throws Exception {
        getAddressJpaController().create(address);
        return address;
    }

    public Collection<Address> createAddresses(Collection<Address> addresses) throws Exception {
        Collection<Address> addressCreatedCollection = new ArrayList<Address>();
        for (Address address : addresses) {
            if (address.getTutorLink() != null) {
                Tutor tutor = findTutor(new Integer(address.getTutorLink().getId()));
                if (tutor == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, TUTOR_NOT_FOUND_MESSAGE);
                address.setTutorId(tutor);
            }
            if (address.getStudentLink() != null) {
                Student student = findStudent(new Integer(address.getStudentLink().getId()));
                if (student == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, STUDENT_NOT_FOUND_MESSAGE);
                address.setStudentId(student);
            }
            Address createdAddress = createAddress(address);
            addressCreatedCollection.add(createdAddress);
        }
        return addressCreatedCollection;
    }

    public void putAddress(Address address, Address xmlUpdatedAddress) throws Exception {
        if (xmlUpdatedAddress.getTutorLink() != null) {
            Tutor tutor = findTutor(new Integer(xmlUpdatedAddress.getTutorLink().getId()));
            if (tutor == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, TUTOR_NOT_FOUND_MESSAGE);
            address.setTutorId(tutor);
        }
        if (xmlUpdatedAddress.getStudentLink() != null) {
            Student student = findStudent(new Integer(xmlUpdatedAddress.getStudentLink().getId()));
            if (student == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, STUDENT_NOT_FOUND_MESSAGE);
            address.setStudentId(student);
        }
        address.setName(xmlUpdatedAddress.getName());
        address.setPostalcode(xmlUpdatedAddress.getPostalcode());
        address.setProvince(xmlUpdatedAddress.getProvince());
        address.setTown(xmlUpdatedAddress.getTown());
        address.setLink(xmlUpdatedAddress.getLink());
        getAddressJpaController().edit(address);
    }

    public void destroyAddress(Address address) throws Exception {
        getAddressJpaController().destroy(address.getId());
    }

    public static Collection<Competence> getCompetenceCollection(Competence competence) {
        Collection<Competence> competenceCollection = new ArrayList<Competence>();
        competenceCollection.add(competence);
        return competenceCollection;
    }

    public Competence findCompetence(Integer id) {
        return getCompetenceJpaController().findCompetence(id);
    }

    public List<Competence> findAllCompetences() {
        return getCompetenceJpaController().findCompetenceEntities();
    }

    public Competence createCompetence(Competence competence) throws Exception {
        getCompetenceJpaController().create(competence);
        return competence;
    }

    public Collection<Competence> createCompetences(Collection<Competence> competences) throws Exception {
        Collection<Competence> competenceCreatedCollection = new ArrayList<Competence>();
        for (Competence competence : competences) {
            Competence createdCompetence = createCompetence(competence);
            competenceCreatedCollection.add(createdCompetence);
        }
        return competenceCreatedCollection;
    }

    public void putCompetence(Competence competence, Competence xmlUpdatedCompetence) throws Exception {
        competence.setName(xmlUpdatedCompetence.getName());
        competence.setDescription(xmlUpdatedCompetence.getDescription());
        competence.setLink(xmlUpdatedCompetence.getLink());
        getCompetenceJpaController().edit(competence);
    }

    public void destroyCompetence(Competence competence) throws Exception {
        getCompetenceJpaController().destroy(competence.getId());
    }

    public static Collection<Itemcalificable> getItemcalificableCollection(Itemcalificable itemcalificable) {
        Collection<Itemcalificable> itemcalificableCollection = new ArrayList<Itemcalificable>();
        itemcalificableCollection.add(itemcalificable);
        return itemcalificableCollection;
    }

    public Itemcalificable findItemcalificable(Integer id) {
        return getItemcalificableJpaController().findItemcalificable(id);
    }

    public List<Itemcalificable> findAllItemcalificables() {
        return getItemcalificableJpaController().findItemcalificableEntities();
    }

    public Itemcalificable createItemcalificable(Itemcalificable itemcalificable) throws Exception {
        getItemcalificableJpaController().create(itemcalificable);
        return itemcalificable;
    }

    public Collection<Itemcalificable> createItemcalificables(Collection<Itemcalificable> itemcalificables) throws Exception {
        Collection<Itemcalificable> itemcalificableCreatedCollection = new ArrayList<Itemcalificable>();
        for (Itemcalificable itemcalificable : itemcalificables) {
            Link coursegroupLink = itemcalificable.getCoursegroupLink();
            if (coursegroupLink != null) {
                Coursegroup coursegroup = findCoursegroup(new Integer(coursegroupLink.getId()));
                if (coursegroup == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, COURSEGROUP_NOT_FOUND_MESSAGE);
                itemcalificable.setCoursegroupId(coursegroup);
            }
            Link itemcalificabletype = itemcalificable.getItemcalificabletypeLink();
            if (itemcalificabletype != null) {
                Itemcalificabletype itemtype = findItemcalificabletype(new Integer(itemcalificabletype.getId()));
                if (itemtype == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, ITEMCALIFICABLETYPE_NOT_FOUND_MESSAGE);
                itemcalificable.setItemcalificabletypeId(itemtype);
            }
            Link subjectLink = itemcalificable.getSubjectLink();
            if (subjectLink != null) {
                Subject subject = findSubject(new Integer(subjectLink.getId()));
                if (subject == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, SUBJECT_NOT_FOUND_MESSAGE);
                itemcalificable.setSubjectId(subject);
            }
            Collection<Link> competencesLinks = itemcalificable.getCompetencesLinks();
            if (competencesLinks != null) {
                Collection<Competence> competences = new ArrayList<Competence>();
                for (Link competenceLink : competencesLinks) {
                    Competence competence = findCompetence(new Integer(competenceLink.getId()));
                    if (competence == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, COMPETENCE_NOT_FOUND_MESSAGE);
                    competences.add(competence);
                }
                itemcalificable.setCompetenceCollection(competences);
            }
            itemcalificable.setDate(itemcalificable.getXMLGregorianCalendar().toGregorianCalendar().getTime());
            Itemcalificable createdItemcalificable = createItemcalificable(itemcalificable);
            itemcalificableCreatedCollection.add(createdItemcalificable);
        }
        return itemcalificableCreatedCollection;
    }

    public void putItemcalificable(Itemcalificable itemcalificable, Itemcalificable xmlUpdatedItemcalificable) throws Exception {
        Link coursegroup = xmlUpdatedItemcalificable.getCoursegroupLink();
        Coursegroup group = findCoursegroup(new Integer(coursegroup.getId()));
        if (group == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, COURSEGROUP_NOT_FOUND_MESSAGE);
        itemcalificable.setCoursegroupId(group);
        Link itemcalificabletypeLink = xmlUpdatedItemcalificable.getItemcalificabletypeLink();
        Itemcalificabletype itemtype = findItemcalificabletype(new Integer(itemcalificabletypeLink.getId()));
        if (itemtype == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, ITEMCALIFICABLETYPE_NOT_FOUND_MESSAGE);
        itemcalificable.setItemcalificabletypeId(itemtype);
        Link subjectLink = xmlUpdatedItemcalificable.getSubjectLink();
        Subject subject = findSubject(new Integer(subjectLink.getId()));
        if (subject == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, SUBJECT_NOT_FOUND_MESSAGE);
        itemcalificable.setSubjectId(subject);
        Collection<Link> competencesLinks = xmlUpdatedItemcalificable.getCompetencesLinks();
        Collection<Competence> competences = new ArrayList<Competence>();
        if (competencesLinks != null) {
            for (Link competenceLink : competencesLinks) {
                Competence competence = findCompetence(new Integer(competenceLink.getId()));
                if (competence == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, COMPETENCE_NOT_FOUND_MESSAGE);
                competences.add(competence);
            }
        }
        itemcalificable.setCompetenceCollection(competences);
        itemcalificable.setDate(itemcalificable.getXMLGregorianCalendar().toGregorianCalendar().getTime());
        itemcalificable.setName(xmlUpdatedItemcalificable.getName());
        itemcalificable.setDescription(xmlUpdatedItemcalificable.getDescription());
        itemcalificable.setLink(xmlUpdatedItemcalificable.getLink());
        getItemcalificableJpaController().edit(itemcalificable);
    }

    public void destroyItemcalificable(Itemcalificable itemcalificable) throws Exception {
        getItemcalificableJpaController().destroy(itemcalificable.getId());
    }

    public static Collection<Itemcalificabletype> getItemcalificabletypeCollection(Itemcalificabletype itemcalificabletype) {
        Collection<Itemcalificabletype> itemcalificabletypeCollection = new ArrayList<Itemcalificabletype>();
        itemcalificabletypeCollection.add(itemcalificabletype);
        return itemcalificabletypeCollection;
    }

    public Itemcalificabletype findItemcalificabletype(Integer id) {
        return getItemcalificabletypeJpaController().findItemcalificabletype(id);
    }

    public List<Itemcalificabletype> findAllItemcalificabletypes() {
        return getItemcalificabletypeJpaController().findItemcalificabletypeEntities();
    }

    public Itemcalificabletype createItemcalificabletype(Itemcalificabletype itemcalificabletype) throws Exception {
        getItemcalificabletypeJpaController().create(itemcalificabletype);
        return itemcalificabletype;
    }

    public Collection<Itemcalificabletype> createItemcalificabletypes(Collection<Itemcalificabletype> itemcalificabletypes) throws Exception {
        Collection<Itemcalificabletype> itemcalificabletypeCreatedCollection = new ArrayList<Itemcalificabletype>();
        for (Itemcalificabletype itemcalificabletype : itemcalificabletypes) {
            Itemcalificabletype creatputemcalificabletype = createItemcalificabletype(itemcalificabletype);
            itemcalificabletypeCreatedCollection.add(creatputemcalificabletype);
        }
        return itemcalificabletypeCreatedCollection;
    }

    public void putItemcalificabletype(Itemcalificabletype itemcalificabletype, Itemcalificabletype xmlUpdatedItemcalificabletype) throws Exception {
        itemcalificabletype.setName(xmlUpdatedItemcalificabletype.getName());
        itemcalificabletype.setPercent(xmlUpdatedItemcalificabletype.getPercent());
        itemcalificabletype.setDescription(xmlUpdatedItemcalificabletype.getDescription());
        itemcalificabletype.setLink(xmlUpdatedItemcalificabletype.getLink());
        getItemcalificabletypeJpaController().edit(itemcalificabletype);
    }

    public void destroyItemcalificabletype(Itemcalificabletype itemcalificabletype) throws Exception {
        getItemcalificabletypeJpaController().destroy(itemcalificabletype.getId());
    }

    public static Collection<Score> getScoreCollection(Score score) {
        Collection<Score> scoreCollection = new ArrayList<Score>();
        scoreCollection.add(score);
        return scoreCollection;
    }

    public Score findScore(Integer id) {
        return getScoreJpaController().findScore(id);
    }

    public List<Score> findAllScores() {
        return getScoreJpaController().findScoreEntities();
    }

    public Score createScore(Score score) throws Exception {
        getScoreJpaController().create(score);
        return score;
    }

    public Collection<Score> createScores(Collection<Score> scores) throws Exception {
        Collection<Score> scoreCreatedCollection = new ArrayList<Score>();
        for (Score score : scores) {
            Link studentLink = score.getStudentLink();
            Student student = findStudent(new Integer(studentLink.getId()));
            if (student == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, STUDENT_NOT_FOUND_MESSAGE);
            score.setStudentId(student);
            Link itemcalificableLink = score.getItemcalificableLink();
            Itemcalificable itemcalificable = findItemcalificable(new Integer(itemcalificableLink.getId()));
            if (itemcalificable == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, ITEMCALIFICABLE_NOT_FOUND_MESSAGE);
            score.setItemcalificableId(itemcalificable);
            Score createdScore = createScore(score);
            scoreCreatedCollection.add(createdScore);
        }
        return scoreCreatedCollection;
    }

    public void putScore(Score score, Score xmlUpdatedScore) throws Exception {
        Link studentLink = xmlUpdatedScore.getStudentLink();
        Student student = findStudent(new Integer(studentLink.getId()));
        if (student == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, STUDENT_NOT_FOUND_MESSAGE);
        score.setStudentId(student);
        Link itemcalificableLink = xmlUpdatedScore.getItemcalificableLink();
        Itemcalificable itemcalificable = findItemcalificable(new Integer(itemcalificableLink.getId()));
        if (itemcalificable == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, ITEMCALIFICABLE_NOT_FOUND_MESSAGE);
        score.setItemcalificableId(itemcalificable);
        score.setValue(xmlUpdatedScore.getValue());
        score.setLink(xmlUpdatedScore.getLink());
        getScoreJpaController().edit(score);
    }

    public void destroyScore(Score score) throws Exception {
        getScoreJpaController().destroy(score.getId());
    }

    public static Collection<Student> getStudentCollection(Student Student) {
        Collection<Student> studentCollection = new ArrayList<Student>();
        studentCollection.add(Student);
        return studentCollection;
    }

    public Student findStudent(Integer id) {
        return getStudentJpaController().findStudent(id);
    }

    public List<Student> findAllStudents() {
        return getStudentJpaController().findStudentEntities();
    }

    public Student createStudent(Student student) throws Exception {
        getStudentJpaController().create(student);
        return student;
    }

    public Collection<Student> createStudents(Collection<Student> students) throws Exception {
        Collection<Student> studentCreatedCollection = new ArrayList<Student>();
        for (Student student : students) {
            Coursegroup group = findCoursegroup(new Integer(student.getCoursegroupLink().getId()));
            if (group == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, COURSEGROUP_NOT_FOUND_MESSAGE);
            student.setCoursegroupId(group);
            Student createdStudent = createStudent(student);
            studentCreatedCollection.add(createdStudent);
        }
        return studentCreatedCollection;
    }

    public void putStudent(Student studentOri, Student xmlUpdatedStudent) throws Exception {
        if (!studentOri.getCoursegroupId().getId().toString().equals(xmlUpdatedStudent.getCoursegroupLink().getId())) {
            Coursegroup newCorsegroup = findCoursegroup(new Integer(xmlUpdatedStudent.getCoursegroupLink().getId()));
            if (newCorsegroup == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, COURSEGROUP_NOT_FOUND_MESSAGE);
            studentOri.setCoursegroupId(newCorsegroup);
        }
        Collection<Link> absencesLinks = xmlUpdatedStudent.getAbsencesLinks();
        Collection<Absence> absences = new ArrayList<Absence>();
        if (absencesLinks != null) {
            for (Link absenceLink : absencesLinks) {
                Absence absence = findAbsence(new Integer(absenceLink.getId()));
                if (absence == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, ABSENCE_NOT_FOUND_MESSAGE);
                absences.add(absence);
            }
        }
        studentOri.setAbsenceCollection(absences);
        Collection<Link> tutorsLinks = xmlUpdatedStudent.getTutorsLinks();
        Collection<Tutor> tutors = new ArrayList<Tutor>();
        if (tutorsLinks != null) {
            for (Link tutorLink : tutorsLinks) {
                Tutor tutor = findTutor(new Integer(tutorLink.getId()));
                if (tutor == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, TUTOR_NOT_FOUND_MESSAGE);
                tutors.add(tutor);
            }
        }
        studentOri.setTutorCollection(tutors);
        Collection<Link> addressesLinks = xmlUpdatedStudent.getAddressesLinks();
        Collection<Address> addresses = new ArrayList<Address>();
        if (addressesLinks != null) {
            for (Link addressLink : addressesLinks) {
                Address address = findAddress(new Integer(addressLink.getId()));
                if (address == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, ADDRESS_NOT_FOUND_MESSAGE);
                addresses.add(address);
            }
        }
        studentOri.setAddressCollection(addresses);
        studentOri.setName(xmlUpdatedStudent.getName());
        studentOri.setSurnames(xmlUpdatedStudent.getSurnames());
        studentOri.setBirthdate(xmlUpdatedStudent.getXmlDate().toGregorianCalendar().getTime());
        getStudentJpaController().edit(studentOri);
        studentOri.setEmail(xmlUpdatedStudent.getEmail());
        studentOri.setPhone1(xmlUpdatedStudent.getPhone1());
        studentOri.setPhone2(xmlUpdatedStudent.getPhone2());
        studentOri.setLink(xmlUpdatedStudent.getLink());
        getStudentJpaController().edit(studentOri);
    }

    public void destroyStudent(Student student) throws Exception {
        Collection<Score> scores = student.getScoreCollection();
        for (Score score : scores) {
            destroyScore(score);
        }
        Collection<Address> addresses = student.getAddressCollection();
        for (Address address : addresses) {
            destroyAddress(address);
        }
        Collection<Absence> absences = student.getAbsenceCollection();
        for (Absence absence : absences) {
            destroyAbsence(absence);
        }
        getStudentJpaController().destroy(student.getId());
    }

    public static Collection<Subject> getSubjectCollection(Subject subject) {
        Collection<Subject> subjectCollection = new ArrayList<Subject>();
        subjectCollection.add(subject);
        return subjectCollection;
    }

    public Subject findSubject(Integer id) {
        return getSubjectJpaController().findSubject(id);
    }

    public List<Subject> findAllSubjects() {
        return getSubjectJpaController().findSubjectEntities();
    }

    public Subject createSubject(Subject subject) throws Exception {
        getSubjectJpaController().create(subject);
        return subject;
    }

    public Collection<Subject> createSubjects(Collection<Subject> subjects) throws Exception {
        Collection<Subject> subjectCreatedCollection = new ArrayList<Subject>();
        for (Subject subject : subjects) {
            Course course = findCourse(new Integer(subject.getCourseLink().getId()));
            if (course == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, COURSE_NOT_FOUND_MESSAGE);
            subject.setCourseId(course);
            Subject createdSubject = createSubject(subject);
            subjectCreatedCollection.add(createdSubject);
        }
        return subjectCreatedCollection;
    }

    public void putSubject(Subject subjectOri, Subject xmlUpdatedSubject) throws Exception {
        subjectOri.setName(xmlUpdatedSubject.getName());
        if (!subjectOri.getCourseId().getId().toString().equals(xmlUpdatedSubject.getCourseLink().getId())) {
            Course course = findCourse(new Integer(xmlUpdatedSubject.getCourseLink().getId()));
            if (course == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, COURSE_NOT_FOUND_MESSAGE);
            subjectOri.setCourseId(course);
        }
        Collection<Link> coursegroupsLinks = xmlUpdatedSubject.getSubjectCoursegroupCollection();
        Collection<Coursegroup> groups = new ArrayList<Coursegroup>();
        if (coursegroupsLinks != null) {
            for (Link group : coursegroupsLinks) {
                Coursegroup coursegroup = findCoursegroup(new Integer(group.getId()));
                if (coursegroup == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, COURSEGROUP_NOT_FOUND_MESSAGE);
                groups.add(coursegroup);
            }
        }
        subjectOri.setCoursegroupCollection(groups);
        subjectOri.setDescription(xmlUpdatedSubject.getDescription());
        subjectOri.setLink(xmlUpdatedSubject.getLink());
        getSubjectJpaController().edit(subjectOri);
    }

    public void destroySubject(Subject subject) throws Exception {
        getSubjectJpaController().destroy(subject.getId());
    }

    public static Collection<Tutor> getTutorCollection(Tutor tutor) {
        Collection<Tutor> tutorCollection = new ArrayList<Tutor>();
        tutorCollection.add(tutor);
        return tutorCollection;
    }

    public Tutor findTutor(Integer id) {
        return getTutorJpaController().findTutor(id);
    }

    public List<Tutor> findAllTutors() {
        return getTutorJpaController().findTutorEntities();
    }

    public Tutor createTutor(Tutor tutor) throws Exception {
        getTutorJpaController().create(tutor);
        return tutor;
    }

    public Collection<Tutor> createTutors(Collection<Tutor> tutors) throws Exception {
        Collection<Tutor> tutorCreatedCollection = new ArrayList<Tutor>();
        for (Tutor tutor : tutors) {
            Collection<Link> addresses = tutor.getAddressesLinks();
            if (addresses != null) {
                Collection<Address> addressCollection = new ArrayList<Address>();
                for (Link link : addresses) {
                    Address address = findAddress(new Integer(link.getId()));
                    if (address == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, ADDRESS_NOT_FOUND_MESSAGE);
                    addressCollection.add(address);
                }
                tutor.setAddressCollection(addressCollection);
            }
            Collection<Link> students = tutor.getStudentsLinks();
            if (students != null) {
                Collection<Student> studentCollection = new ArrayList<Student>();
                for (Link link : students) {
                    Student student = findStudent(new Integer(link.getId()));
                    if (student == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, STUDENT_NOT_FOUND_MESSAGE);
                    studentCollection.add(student);
                }
                tutor.setStudentCollection(studentCollection);
            }
            Tutor createdTutor = createTutor(tutor);
            tutorCreatedCollection.add(createdTutor);
        }
        return tutorCreatedCollection;
    }

    public void putTutor(Tutor tutorOri, Tutor xmlUpdatedTutor) throws Exception {
        Collection<Link> addresses = xmlUpdatedTutor.getAddressesLinks();
        if (addresses != null) {
            Collection<Address> addressCollection = new ArrayList<Address>();
            for (Link link : addresses) {
                Address address = findAddress(new Integer(link.getId()));
                if (address == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, ADDRESS_NOT_FOUND_MESSAGE);
                addressCollection.add(address);
            }
            tutorOri.setAddressCollection(addressCollection);
        }
        Collection<Link> students = xmlUpdatedTutor.getStudentsLinks();
        if (students != null) {
            Collection<Student> studentCollection = new ArrayList<Student>();
            for (Link link : students) {
                Student student = findStudent(new Integer(link.getId()));
                if (student == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, STUDENT_NOT_FOUND_MESSAGE);
                studentCollection.add(student);
            }
            tutorOri.setStudentCollection(studentCollection);
        }
        tutorOri.setName(xmlUpdatedTutor.getName());
        tutorOri.setSurnames(xmlUpdatedTutor.getSurnames());
        tutorOri.setDescription(xmlUpdatedTutor.getDescription());
        tutorOri.setEmail(xmlUpdatedTutor.getEmail());
        tutorOri.setPhone1(xmlUpdatedTutor.getPhone1());
        tutorOri.setPhone2(xmlUpdatedTutor.getPhone2());
        tutorOri.setProfession(xmlUpdatedTutor.getProfession());
        tutorOri.setLink(xmlUpdatedTutor.getLink());
        getTutorJpaController().edit(tutorOri);
    }

    public void destroyTutor(Tutor tutor) throws Exception {
        getTutorJpaController().destroy(tutor.getId());
    }

    private AbsenceJpaController getAbsenceJpaController() {
        if (absenceJpaController == null) {
            absenceJpaController = new AbsenceJpaController();
        }
        return absenceJpaController;
    }

    private AddressJpaController getAddressJpaController() {
        if (addressJpaController == null) {
            addressJpaController = new AddressJpaController();
        }
        return addressJpaController;
    }

    private CompetenceJpaController getCompetenceJpaController() {
        if (competenceJpaController == null) {
            competenceJpaController = new CompetenceJpaController();
        }
        return competenceJpaController;
    }

    private CourseJpaController getCourseJpaController() {
        if (courseJpaController == null) {
            courseJpaController = new CourseJpaController();
        }
        return courseJpaController;
    }

    private CoursegroupJpaController getCoursegroupJpaController() {
        if (coursegroupJpaController == null) {
            coursegroupJpaController = new CoursegroupJpaController();
        }
        return coursegroupJpaController;
    }

    private ItemcalificableJpaController getItemcalificableJpaController() {
        if (itemcalificableJpaController == null) {
            itemcalificableJpaController = new ItemcalificableJpaController();
        }
        return itemcalificableJpaController;
    }

    private ItemcalificabletypeJpaController getItemcalificabletypeJpaController() {
        if (itemcalificabletypeJpaController == null) {
            itemcalificabletypeJpaController = new ItemcalificabletypeJpaController();
        }
        return itemcalificabletypeJpaController;
    }

    private ScoreJpaController getScoreJpaController() {
        if (scoreJpaController == null) {
            scoreJpaController = new ScoreJpaController();
        }
        return scoreJpaController;
    }

    private StudentJpaController getStudentJpaController() {
        if (studentJpaController == null) {
            studentJpaController = new StudentJpaController();
        }
        return studentJpaController;
    }

    private SubjectJpaController getSubjectJpaController() {
        if (subjectJpaController == null) {
            subjectJpaController = new SubjectJpaController();
        }
        return subjectJpaController;
    }

    private TutorJpaController getTutorJpaController() {
        if (tutorJpaController == null) {
            tutorJpaController = new TutorJpaController();
        }
        return tutorJpaController;
    }
}
