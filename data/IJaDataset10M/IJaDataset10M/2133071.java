package persistence.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import persistence.core.DAOProduct;
import persistence.exception.DBConnectionException;
import persistence.exception.DeleteException;
import persistence.exception.InsertException;
import persistence.exception.SelectException;
import persistence.exception.UpdateException;
import persistence.exception.XmlIOException;
import persistence.tools.Criteria;
import persistence.tools.DeleteQuery;
import persistence.tools.InsertQuery;
import persistence.tools.OracleJDBConnector;
import persistence.tools.SelectQuery;
import persistence.tools.UpdateQuery;
import domain.core.Semester;

/** 
 * Data Object Acces to the SEMESTER Table
 * @author Florent Revy for FARS Design
 * @author Zakaria Taghy for FARS Design
 */
public class SemesterDAO extends DAOProduct<Semester> {

    public static final String TABLE_NAME = "SEMESTER";

    @Override
    public void delete(Semester obj) throws DeleteException, DBConnectionException, XmlIOException {
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        Criteria critDel = new Criteria();
        critDel.addCriterion("SEMESTER_ID", obj.getId());
        try {
            stmt.executeUpdate(new DeleteQuery(TABLE_NAME, critDel).toString());
            stmt.getConnection().commit();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                stmt.getConnection().rollback();
            } catch (SQLException e1) {
                throw new DBConnectionException("Rollback Exception :", e1);
            }
            throw new DeleteException(TABLE_NAME + " Deletion exception :", e);
        }
    }

    /**
	 * Return the Set of Semester linked to the Year Of Study in parameter 
	 * (or null if don't exist in data source)
	 * @param yosId of the year of study
	 * @return HashSet<Semester> linked to the year of study
	 * @throws DBConnectionException 
	 * @throws SelectException 
	 */
    public HashSet<Semester> findByYos(Integer yosId) throws DBConnectionException, SelectException {
        HashSet<Semester> semesterSet = null;
        Statement stmt;
        try {
            stmt = OracleJDBConnector.getInstance().getStatement();
        } catch (XmlIOException e1) {
            e1.printStackTrace();
            throw new DBConnectionException("Unable to get statement", e1);
        }
        Criteria critWhere = new Criteria();
        critWhere.addCriterion("YEAR_STUDY_ID", yosId);
        try {
            ResultSet result = stmt.executeQuery(new SelectQuery(TABLE_NAME, critWhere).toString());
            if (result != null) {
                semesterSet = new HashSet<Semester>();
                while (result.next()) {
                    Semester smstr = new Semester();
                    smstr.setName(result.getString("SEMESTER_NAME"));
                    smstr.setAcademicYear(null);
                    smstr.setDescription(result.getString("SEMESTER_DESCRIPTION"));
                    smstr.setId(result.getInt("SEMESTER_ID"));
                    smstr.setLevel(result.getInt("SEMESTER_LEVEL"));
                    smstr.setTeachingUnit(null);
                    smstr.setYearOfStudy(null);
                    GregorianCalendar sDate = new GregorianCalendar();
                    sDate.setTime(result.getDate("SEMESTER_STARTING_DATE"));
                    smstr.setSDate(sDate);
                    GregorianCalendar eDate = new GregorianCalendar();
                    sDate.setTime(result.getDate("SEMESTER_ENDING_DATE"));
                    smstr.setEDate(eDate);
                    semesterSet.add(smstr);
                }
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SelectException(TABLE_NAME + " Request Error", e);
        }
        return semesterSet;
    }

    /**
	 * Return the Semester with the name in parameter and linked to the Year Of Study and
	 * academic Year
	 * (or null if don't exist in data source)
	 * @param name the name of the semester to find
	 * @param yosID the year of study related
	 * @param acaYearID the academic year
	 */
    public Semester findByValues(String name, Integer yosID, Integer acaYearID) throws DBConnectionException, SelectException {
        Semester smstr = null;
        Statement stmt;
        try {
            stmt = OracleJDBConnector.getInstance().getStatement();
        } catch (XmlIOException e1) {
            e1.printStackTrace();
            throw new DBConnectionException("Unable to get statement", e1);
        }
        Criteria critWhere = new Criteria();
        critWhere.addCriterion("SEMESTER_NAME", name);
        critWhere.addCriterion("YEAR_STUDY_ID", yosID);
        critWhere.addCriterion("ACADEMIC_YEAR_ID", acaYearID);
        try {
            ResultSet result = stmt.executeQuery(new SelectQuery(DepartmentDAO.TABLE_NAME, critWhere).toString());
            if (result != null) {
                while (result.next()) {
                    smstr = new Semester();
                    smstr.setName(result.getString("SEMESTER_NAME"));
                    smstr.setAcademicYear(null);
                    smstr.setDescription(result.getString("SEMESTER_DESCRIPTION"));
                    smstr.setId(result.getInt("SEMESTER_ID"));
                    smstr.setLevel(result.getInt("SEMESTER_LEVEL"));
                    smstr.setTeachingUnit(null);
                    smstr.setYearOfStudy(null);
                    GregorianCalendar sDate = new GregorianCalendar();
                    sDate.setTime(result.getDate("SEMESTER_STARTING_DATE"));
                    smstr.setSDate(sDate);
                    GregorianCalendar eDate = new GregorianCalendar();
                    sDate.setTime(result.getDate("SEMESTER_ENDING_DATE"));
                    smstr.setEDate(eDate);
                }
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SelectException(TABLE_NAME + " Request Error", e);
        }
        return smstr;
    }

    @Override
    public Semester store(Semester obj) throws InsertException, DBConnectionException, XmlIOException {
        if (obj.getYearOfStudy().getId() == null || obj.getAcademicYear().getId() == null) throw new InsertException("Missing Foreign Key(s)");
        Semester toReturn = null;
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        List<Object> values = new ArrayList<Object>();
        values.add(0);
        values.add(obj.getYearOfStudy().getId());
        values.add(obj.getDescription());
        values.add(obj.getAcademicYear().getId());
        values.add(obj.getName());
        values.add(obj.getLevel());
        values.add(obj.getDescription());
        values.add(obj.getSDate());
        values.add(obj.getEDate());
        try {
            stmt.executeUpdate(new InsertQuery(TABLE_NAME, values).toString());
            toReturn = findByValues(obj.getName(), obj.getYearOfStudy().getId(), obj.getAcademicYear().getId());
            if (toReturn != null) {
                toReturn.setAcademicYear(obj.getAcademicYear());
                toReturn.setYearOfStudy(obj.getYearOfStudy());
                toReturn.setTeachingUnit(obj.getTeachingUnitList());
            } else throw new SelectException(TABLE_NAME + " Can't retieve record");
            stmt.getConnection().commit();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                stmt.getConnection().rollback();
            } catch (SQLException e1) {
                throw new DBConnectionException("Rollback Exception :", e1);
            }
            throw new InsertException(TABLE_NAME + " Insert Exception :", e);
        }
        return toReturn;
    }

    @Override
    public void update(Semester obj) throws UpdateException, DBConnectionException, XmlIOException {
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        Criteria newCrit = new Criteria();
        newCrit.addCriterion("SEMESTER_NAME", obj.getName());
        newCrit.addCriterion("SEMESTER_DESCRIPTION", obj.getDescription());
        newCrit.addCriterion("YEAR_STUDY_ID", obj.getYearOfStudy().getId());
        newCrit.addCriterion("ACADEMIC_YEAR_ID", obj.getAcademicYear().getId());
        newCrit.addCriterion("SEMESTER_LEVEL", obj.getLevel());
        newCrit.addCriterion("SEMESTER_STARTING_DATE", obj.getSDate());
        newCrit.addCriterion("SEMESTER_ENDING_DATE", obj.getEDate());
        Criteria critWhere = new Criteria();
        critWhere.addCriterion("SEMESTER_ID", obj.getId());
        try {
            stmt.executeUpdate(new UpdateQuery(TABLE_NAME, newCrit, critWhere).toString());
            stmt.getConnection().commit();
            stmt.close();
        } catch (SQLException e) {
            try {
                stmt.getConnection().rollback();
            } catch (SQLException e1) {
                throw new DBConnectionException(TABLE_NAME + " Rollback Exception :", e1);
            }
            throw new UpdateException(TABLE_NAME + " Update exception", e);
        }
    }
}
