package domain.core;

import java.util.Set;
import persistence.DAO.ClassSessionDAO;
import persistence.DAO.StudentsGroupDAO;
import persistence.DAO.SubjectModelDAO;
import persistence.DAO.TeacherDAO;
import persistence.core.AbstractDAOFactory;
import persistence.core.JdbcDAOFactory;
import persistence.core.AbstractDAOFactory.FactoryType;
import persistence.exception.DBConnectionException;
import persistence.exception.SelectException;

/** 
 * This class belongs to the domain layer of the application
 * This class gathers all the informations of an EES
 * @author Oana Garbasevschi for FARS Design
 * @author Ivan VALIMAHAMED for FARS Design
 */
public class ElementaryEducationSession implements ElementaryEducationSessionIf {

    /**
	 * 
	 */
    private Integer id;

    private boolean statutory;

    private Teacher teacher;

    private SubjectModel subjectModel;

    private StudentsGroup studentsGroup;

    private Set<ClassSession> classSession;

    JdbcDAOFactory jdbc;

    /**
	 * 
	 * @param subjectModel		subject model of the EES
	 * @param teacher			teacher of the EES
	 * @param studentsGroup		students group of the EES
	 * @param statutory			statutory state of the EES
	 */
    public ElementaryEducationSession(SubjectModel subjectModel, Teacher teacher, StudentsGroup studentsGroup, boolean statutory) {
        this.subjectModel = subjectModel;
        this.teacher = teacher;
        this.studentsGroup = studentsGroup;
        this.statutory = statutory;
        jdbc = (JdbcDAOFactory) AbstractDAOFactory.getDAOInstance(FactoryType.JDBC_FACTORY);
    }

    /**
	 * 
	 * @param statutory		statutory state of the EES
	 */
    public ElementaryEducationSession(boolean statutory) {
        this.statutory = statutory;
        jdbc = (JdbcDAOFactory) AbstractDAOFactory.getDAOInstance(FactoryType.JDBC_FACTORY);
    }

    /**
	 * 
	 */
    public ElementaryEducationSession() {
        jdbc = (JdbcDAOFactory) AbstractDAOFactory.getDAOInstance(FactoryType.JDBC_FACTORY);
    }

    /** 
	 * @return the id of the EES
	 */
    public Integer getId() {
        return this.id;
    }

    /** 
	 * @param id new id of the EES
	 */
    public void setId(Integer id) {
        this.id = id;
    }

    /** 
	 * @return the statutory state of the EES
	 */
    public boolean getStatutory() {
        return this.statutory;
    }

    /** 
	 * @param statutory new statutory state of the EES
	 */
    public void setStatutory(boolean statutory) {
        this.statutory = statutory;
    }

    /**
	 * @return the teacher of the EES
	 */
    public Teacher getTeacher() {
        return this.teacher;
    }

    /** 
	 * @param teacher new teacher of the EES
	 */
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    /** 
	 * @return the subject model of the EES
	 */
    public SubjectModel getSubjectModel() {
        return this.subjectModel;
    }

    /** 
	 * @param subjectModel new subject model of the EES
	 */
    public void setSubjectModel(SubjectModel subjectModel) {
        this.subjectModel = subjectModel;
    }

    /**
	 * @return the students group of the EES
	 */
    public StudentsGroup getStudentsGroup() {
        return this.studentsGroup;
    }

    /** 
	 * @param studentsGroup new Students group of the EES
	 */
    public void setStudentsGroup(StudentsGroup studentsGroup) {
        this.studentsGroup = studentsGroup;
    }

    /** 
	 * @return the list of class sessions of the EES
	 */
    public Set<ClassSession> getClassSession() {
        return this.classSession;
    }

    /** 
	 * @param classSession new list of class session of the EES
	 */
    public void setClassSession(Set<ClassSession> classSession) {
        this.classSession = classSession;
    }

    /**
	 * @return the teacher of the EES
	 * @throws DBConnectionException
	 * @throws SelectException
	 */
    protected Teacher obtainTeacher() throws DBConnectionException, SelectException {
        TeacherDAO t = jdbc.getTeacherDAO();
        if (this.teacher == null) this.teacher = t.findByEesId(this.id);
        return this.teacher;
    }

    /** 
	 * @return the subject model of the EES
	 * @throws DBConnectionException 
	 * @throws SelectException 
	 */
    protected SubjectModel obtainSubjectModel() throws DBConnectionException, SelectException {
        SubjectModelDAO smDAO = jdbc.getSubjectModelDAO();
        if (this.subjectModel == null) {
            this.subjectModel = smDAO.findByEES(id);
        }
        return this.subjectModel;
    }

    /**
	 * @return the students group of the EES
	 * @throws DBConnectionException
	 * @throws SelectException
	 */
    public StudentsGroup obtainStudentsGroup() throws DBConnectionException, SelectException {
        StudentsGroupDAO sgDAO = jdbc.getStudentsGroupDAO();
        if (this.studentsGroup == null) {
            this.studentsGroup = sgDAO.findByEesId(this.id);
        }
        return this.studentsGroup;
    }

    /**
	 * If the list is not loaded, load of the list from
	 * the database
	 * @return the list of class sessions of the EES
	 * @throws DBConnectionException
	 * @throws SelectException
	 */
    protected Set<ClassSession> obtainClassSession() throws DBConnectionException, SelectException {
        ClassSessionDAO csDAO = jdbc.getClassSessionDAO();
        if (this.classSession == null) {
            this.classSession = csDAO.findByEesId(this.id);
        }
        return this.classSession;
    }
}
