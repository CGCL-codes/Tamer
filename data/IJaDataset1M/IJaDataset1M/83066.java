package persistence.core;

import persistence.DAO.AcademicYearDAO;
import persistence.DAO.ClassRoomDAO;
import persistence.DAO.ClassSessionDAO;
import persistence.DAO.DepartmentDAO;
import persistence.DAO.EESDao;
import persistence.DAO.ExtraClassSessionDAO;
import persistence.DAO.GroupTypeDAO;
import persistence.DAO.HolidaysDAO;
import persistence.DAO.HolidaysTypeDAO;
import persistence.DAO.PeriodDAO;
import persistence.DAO.SemesterDAO;
import persistence.DAO.SessionTypeDAO;
import persistence.DAO.StatusDAO;
import persistence.DAO.StudentsGroupDAO;
import persistence.DAO.SubjectDAO;
import persistence.DAO.SubjectModelDAO;
import persistence.DAO.TeacherDAO;
import persistence.DAO.TeachingUnitDAO;
import persistence.DAO.YearOfStudyDAO;
import persistence.exception.DBConnectionException;
import persistence.exception.XmlIOException;
import persistence.tools.OracleJDBConnector;

/** 
 * JDBC Factory: Supply all JDBC Data Acces Object and Connection/Disconnection with the Oracle DBMS
 * @author Florent Revy for FARS Design
 */
public class JdbcDAOFactory extends AbstractDAOFactory {

    private static JdbcDAOFactory jdbcFactory;

    private OracleJDBConnector jdbcConnector;

    private EESDao eesDAO;

    private PeriodDAO periodDAO;

    private DepartmentDAO departmentDAO;

    private YearOfStudyDAO yosDAO;

    private AcademicYearDAO academicYear;

    private SemesterDAO semesterDAO;

    private TeachingUnitDAO teachingUnitDAO;

    private SubjectDAO subjectDAO;

    private SessionTypeDAO sessionTypeDAO;

    private TeacherDAO TeacherDAO;

    private HolidaysTypeDAO holidaysTypeDAO;

    private HolidaysDAO holidaysDAO;

    private ExtraClassSessionDAO extraClassSessionDAO;

    private ClassSessionDAO classSessionDAO;

    private StatusDAO statusDAO;

    private ClassRoomDAO classRoomDAO;

    private StudentsGroupDAO studentsGroupDAO;

    private SubjectModelDAO subjectModelDAO;

    private GroupTypeDAO groupTypeDAO;

    /** 
	 * @return the unique instance of the JdbcDAOFactory
	 * @throws DBConnectionException 
	 */
    public static JdbcDAOFactory getInstance() {
        if (jdbcFactory == null) jdbcFactory = new JdbcDAOFactory();
        return jdbcFactory;
    }

    /**
	 * Constructor
	 */
    private JdbcDAOFactory() {
        initDao();
    }

    /**
	 * Initialization of all the JDBC DAOs and the connector
	 */
    private void initDao() {
        this.eesDAO = new EESDao();
        this.periodDAO = new PeriodDAO();
        this.departmentDAO = new DepartmentDAO();
        this.yosDAO = new YearOfStudyDAO();
        this.academicYear = new AcademicYearDAO();
        this.semesterDAO = new SemesterDAO();
        this.teachingUnitDAO = new TeachingUnitDAO();
        this.subjectDAO = new SubjectDAO();
        this.sessionTypeDAO = new SessionTypeDAO();
        this.TeacherDAO = new TeacherDAO();
        this.holidaysTypeDAO = new HolidaysTypeDAO();
        this.holidaysDAO = new HolidaysDAO();
        this.extraClassSessionDAO = new ExtraClassSessionDAO();
        this.classSessionDAO = new ClassSessionDAO();
        this.statusDAO = new StatusDAO();
        this.classRoomDAO = new ClassRoomDAO();
        this.studentsGroupDAO = new StudentsGroupDAO();
        this.subjectModelDAO = new SubjectModelDAO();
        this.groupTypeDAO = new GroupTypeDAO();
    }

    /**
	 * Proceed to the connection with the Oracle 10g database
	 * @throws DBConnectionException
	 * @throws XmlIOException 
	 */
    public void connect() throws DBConnectionException, XmlIOException {
        this.jdbcConnector = OracleJDBConnector.getInstance();
        if (!this.jdbcConnector.isConnected()) this.jdbcConnector.connect();
    }

    /**
	 * Proceed to the disconnection with the Oracle 10g database
	 * @throws DBConnectionException
	 */
    public void disconnect() throws DBConnectionException {
        if (this.jdbcConnector.isConnected()) this.jdbcConnector.closeConnection();
    }

    /**
	 * Getter on Period DAO Object
	 * @return PeriodDAO Object
	 */
    public PeriodDAO getPeriodDAO() {
        return this.periodDAO;
    }

    /**
	 * Getter on EES DAO Object
	 * @return EESDAO Object
	 */
    public EESDao getEESDAO() {
        return this.eesDAO;
    }

    /**
	 * Getter on Department DAO Object
	 * @return DepartmentDAO Object
	 */
    public DepartmentDAO getDepartmentDAO() {
        return this.departmentDAO;
    }

    /**
	 * Getter on Year Of Study DAO Object
	 * @return YearOfStudyDAO Object
	 */
    public YearOfStudyDAO getYearOfStudyDAO() {
        return this.yosDAO;
    }

    /**
	 * Getter on Academic Year DAO Object
	 * @return AcademicYearDAO Object
	 */
    public AcademicYearDAO getAcademicYearDAO() {
        return this.academicYear;
    }

    /**
	 * Getter on Semester Year DAO Object
	 * @return SemesterDAO Object
	 */
    public SemesterDAO getSemesterDAO() {
        return this.semesterDAO;
    }

    /**
	 * Getter on Teaching Unit DAO Object
	 * @return TeachingUnitDAO Object
	 */
    public TeachingUnitDAO getTeachingUnitDAO() {
        return this.teachingUnitDAO;
    }

    /**
	 * Getter on Subject DAO Object
	 * @return SubjectDAO Object
	 */
    public SubjectDAO getSubjectDAO() {
        return this.subjectDAO;
    }

    /**
	 * Getter on Session Type DAO Object
	 * @return SessionTypeDAO Object
	 */
    public SessionTypeDAO getSessionTypeDAO() {
        return this.sessionTypeDAO;
    }

    /**
	 * Getter on Subject Model DAO Object
	 * @return SubjectModelDAO Object
	 */
    public SubjectModelDAO getSubjectModelDAO() {
        return this.subjectModelDAO;
    }

    /**
	 * Getter on Teacher DAO Object
	 * @return TeacherDAO Object
	 */
    public TeacherDAO getTeacherDAO() {
        return this.TeacherDAO;
    }

    /**
	 * Getter on Students Groupe DAO Object
	 * @return StudentsGroupDAO Object
	 */
    public StudentsGroupDAO getStudentsGroupDAO() {
        return this.studentsGroupDAO;
    }

    /**
	 * Getter on Classroom DAO Object
	 * @return ClassRoomDAO Object
	 */
    public ClassRoomDAO getClassRoomDAO() {
        return this.classRoomDAO;
    }

    /**
	 * Getter on status DAO Object
	 * @return StatusDAO Object
	 */
    public StatusDAO getStatusDAO() {
        return this.statusDAO;
    }

    /**
	 * Getter on Class Session DAO Object
	 * @return ClassSessionDAO Object
	 */
    public ClassSessionDAO getClassSessionDAO() {
        return this.classSessionDAO;
    }

    /**
	 * Getter on Extra Class Session DAO Object
	 * @return ExtraClassSessionDAO Object
	 */
    public ExtraClassSessionDAO getExtraClassSessionDAO() {
        return this.extraClassSessionDAO;
    }

    /**
	 * Getter on Holidays DAO Object
	 * @return HolidaysDAO Object
	 */
    public HolidaysDAO getHolidaysDAO() {
        return this.holidaysDAO;
    }

    /**
	 * Getter on Holidays Type DAO Object
	 * @return HolidaysTypeDAO Object
	 */
    public HolidaysTypeDAO getHolidaysTypeDAO() {
        return this.holidaysTypeDAO;
    }

    /**
	 * Getter on Group Type DAO Object
	 * @return the groupTypeDAO Object
	 */
    public GroupTypeDAO getGroupTypeDAO() {
        return this.groupTypeDAO;
    }
}
