package angry.courses.database;

import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(name = "CoursesDBService", serviceName = "CoursesDBWebService")
public class CoursesDBService implements ICoursesDB {

    Connection conn = null;

    public CoursesDBService() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new AngryDatabaseException(e.getMessage());
        }
        String url = "jdbc:mysql://localhost:3306/studentsdb?autoReconnect=true";
        try {
            conn = DriverManager.getConnection(url, "beaver", "angry");
        } catch (Exception e) {
            throw new AngryDatabaseException(e.getMessage());
        } finally {
            if (conn == null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    throw new AngryDatabaseException(e.getMessage());
                }
            }
        }
    }

    @WebMethod(operationName = "addCourse")
    public void addCourse(String name, String prof, int year, int sem) {
        try {
            Statement stm = conn.createStatement();
            String s = "INSERT into courses values (null,'" + name + "'," + year + "," + sem + ",'" + prof + "')";
            stm.executeUpdate(s);
        } catch (Exception e) {
            throw new AngryDatabaseException("Could not add the course " + name + ".\nExceptionTrace: " + e.getMessage());
        }
    }

    @WebMethod(operationName = "removeCourse")
    public void removeCourse(String name) {
        try {
            Statement stm = conn.createStatement();
            Statement stm2 = conn.createStatement();
            String remRelation = "SELECT r.rid FROM relations r, courses c WHERE r.cid=c.cid AND c.name='" + name + "'";
            ResultSet result = stm.executeQuery(remRelation);
            while (result.next()) {
                String ridStr = result.getString("rid");
                if (ridStr != null) stm2.executeUpdate("DELETE FROM relations WHERE rid=" + ridStr);
            }
            String remCourse = "DELETE FROM courses WHERE name='" + name + "'";
            stm.executeUpdate(remCourse);
        } catch (Exception e) {
            throw new AngryDatabaseException("Could not remove the course " + name + ".\nExceptionTrace: " + e.getMessage());
        }
    }

    @WebMethod(operationName = "getCourseNames1")
    public List<String> getCourseNames1() {
        ArrayList<String> courses = null;
        try {
            courses = new ArrayList<String>();
            Statement stm = conn.createStatement();
            String s = "SELECT * FROM courses";
            ResultSet result = stm.executeQuery(s);
            while (result.next()) {
                String str = result.getString("name");
                courses.add(str);
            }
        } catch (Exception e) {
            throw new AngryDatabaseException("Could not get the list of courses.\nExceptionTrace: " + e.getMessage());
        }
        return courses;
    }

    @WebMethod(operationName = "getCourseNames2")
    public List<String> getCourseNames2(int sem) {
        List<String> courseNames = null;
        try {
            courseNames = new ArrayList<String>();
            Statement stm = conn.createStatement();
            String s = "SELECT * FROM courses WHERE semester=" + sem;
            ResultSet result = stm.executeQuery(s);
            while (result.next()) {
                String str = result.getString("name");
                courseNames.add(str);
            }
        } catch (Exception e) {
            throw new AngryDatabaseException("Could not get the list of courses.\nExceptionTrace: " + e.getMessage());
        }
        return courseNames;
    }

    @WebMethod(operationName = "deleteStudent")
    public void deleteStudent(String name) {
        try {
            Statement stm = conn.createStatement();
            Statement stm2 = conn.createStatement();
            String remRelation = "SELECT r.rid FROM relations r, students s WHERE r.sid=s.sid AND s.name='" + name + "'";
            ResultSet result = stm.executeQuery(remRelation);
            while (result.next()) {
                String ridStr = result.getString("rid");
                if (ridStr != null) stm2.executeUpdate("DELETE FROM relations WHERE rid=" + ridStr);
            }
            String remCourse = "DELETE FROM students WHERE name='" + name + "'";
            stm.executeUpdate(remCourse);
        } catch (Exception e) {
            throw new AngryDatabaseException("Could not remove the student " + name + ".\nExceptionTrace: " + e.getMessage());
        }
    }

    @WebMethod(operationName = "validateStudent")
    public boolean validateStudent(String name, String regnr) {
        boolean status = false;
        try {
            Statement stm = conn.createStatement();
            String s = "SELECT * FROM students WHERE name='" + name + "' AND reg_no='" + regnr + "'";
            ResultSet result = stm.executeQuery(s);
            if (result.next()) {
                String sName = result.getString("name");
                String sRegNo = result.getString("reg_no");
                if (sName != null && sRegNo != null) status = true; else status = false;
            } else status = false;
        } catch (Exception e) {
            throw new AngryDatabaseException("Could not validate student " + name + ", identified by " + regnr + ".\nExceptionTrace: " + e.getMessage());
        }
        return status;
    }

    @WebMethod(operationName = "register")
    public void register(String name, String regnr, int year, List<String> courses) {
        try {
            if (name == "" || regnr == "") throw new AngryDatabaseException("Cannot register without name or regnr. Both credentials are mandatory");
            Statement stm = conn.createStatement();
            int sid = 0, cid = 0;
            String s = "INSERT INTO students VALUES (null, '" + name + "', '" + regnr + "', " + year + ")";
            stm.executeUpdate(s);
            ResultSet rs = stm.executeQuery("SELECT sid FROM students WHERE name='" + name + "' AND reg_no='" + regnr + "'");
            if (rs.next()) sid = rs.getInt("sid");
            for (int i = 0; i < courses.size(); i++) {
                rs = stm.executeQuery("SELECT cid FROM courses WHERE name='" + courses.get(i) + "'");
                if (rs.next()) cid = rs.getInt("cid");
                stm.executeUpdate("INSERT INTO relations VALUES(null, " + cid + ", " + sid + ")");
            }
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) throw new DuplicateRegNrException(e.getMessage()); else throw new AngryDatabaseException("Could not register student " + name + ", identified by registration number " + regnr + " and in year " + year + ".\nExceptionTrace: " + e.getMessage());
        }
    }

    @WebMethod(operationName = "modify")
    public void modify(String name, String regnr, int year, List<String> courses) {
        try {
            if (name == "" || regnr == "") throw new AngryDatabaseException("Cannot modify a registration without name or regnr specified. Both credentials are mandatory");
            Statement stm = conn.createStatement();
            int sid = 0, cid = 0;
            ResultSet rs = stm.executeQuery("SELECT sid FROM students WHERE name='" + name + "' AND reg_no='" + regnr + "'");
            if (rs.next()) sid = rs.getInt("sid");
            stm.executeUpdate("UPDATE students SET year_of_study=" + year + " WHERE sid=" + sid);
            stm.executeUpdate("DELETE FROM relations WHERE sid=" + sid);
            for (int i = 0; i < courses.size(); i++) {
                rs = stm.executeQuery("SELECT cid FROM courses WHERE name='" + courses.get(i) + "'");
                if (rs.next()) cid = rs.getInt("cid");
                stm.executeUpdate("INSERT INTO relations VALUES(null, " + cid + ", " + sid + ")");
            }
        } catch (Exception e) {
            throw new AngryDatabaseException("Could not modify the subscription of student " + name + ", identified by registration number " + regnr + " and in year " + year + ".\nExceptionTrace: " + e.getMessage());
        }
    }

    @WebMethod(operationName = "getYearOfStudy")
    public int getYearOfStudy(String name, String regnr) {
        int yos = -1;
        try {
            if (name == "" || regnr == "") throw new AngryDatabaseException("Cannot get year of study for a registration without name or regnr specified. Both credentials are mandatory");
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT year_of_study FROM students WHERE name='" + name + "' AND reg_no='" + regnr + "'");
            if (rs.next()) yos = rs.getInt("year_of_study");
        } catch (Exception e) {
            throw new AngryDatabaseException("Could not get the year of study for student " + name + ", identified by registration number " + regnr + ".\nExceptionTrace: " + e.getMessage());
        }
        return yos;
    }

    @WebMethod(operationName = "getCoursesForStudent")
    public List<String> getCoursesForStudent(String name, int semester) {
        List<String> coursesList = new ArrayList<String>();
        try {
            if (name == "") throw new AngryDatabaseException("Cannot get courses for a student without name specified");
            Statement stm = conn.createStatement();
            Statement stm2 = conn.createStatement();
            ResultSet sRs = stm.executeQuery("SELECT sid FROM students WHERE name='" + name + "'");
            if (sRs.next()) {
                int sid = sRs.getInt("sid");
                ResultSet rRs = stm.executeQuery("SELECT cid FROM relations WHERE sid=" + sid);
                while (rRs.next()) {
                    int cid = rRs.getInt("cid");
                    ResultSet cRs = stm2.executeQuery("SELECT name FROM courses WHERE cid=" + cid + " AND semester=" + semester);
                    if (cRs.next()) {
                        coursesList.add(cRs.getString("name"));
                    }
                }
            }
        } catch (Exception e) {
            throw new AngryDatabaseException("Could not get the courses list for student " + name + " on semester " + semester + ".\nExceptionTrace: " + e.getMessage());
        }
        return coursesList;
    }

    @WebMethod(operationName = "getStudent")
    public Student getStudent(String name) {
        try {
            if (name == "") throw new AngryDatabaseException("Cannot get student for a registration without name specified");
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM students WHERE name='" + name + "'");
            if (rs.next()) {
                return new Student(name, rs.getString("reg_no"), rs.getInt("year_of_study"));
            }
        } catch (Exception e) {
            throw new AngryDatabaseException("Could not get the student " + name + ".\nExceptionTrace: " + e.getMessage());
        }
        return null;
    }

    @WebMethod(operationName = "getStudentNames")
    public List<String> getStudentNames() {
        ArrayList<String> students = null;
        try {
            students = new ArrayList<String>();
            Statement stm = conn.createStatement();
            String s = "SELECT * FROM students";
            ResultSet result = stm.executeQuery(s);
            while (result.next()) {
                String str = result.getString("name");
                students.add(str);
            }
        } catch (Exception e) {
            throw new AngryDatabaseException("Could not get the list of students.\nExceptionTrace: " + e.getMessage());
        }
        return students;
    }

    @WebMethod(operationName = "getCourse")
    public Course getCourse(String name) {
        try {
            if (name == "") throw new AngryDatabaseException("Cannot get course with empty name");
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM courses WHERE name='" + name + "'");
            if (rs.next()) {
                return new Course(name, rs.getString("professor"), rs.getInt("year"), rs.getInt("semester"));
            }
        } catch (Exception e) {
            throw new AngryDatabaseException("Could not get the course details for " + name + ".\nExceptionTrace: " + e.getMessage());
        }
        return null;
    }

    @WebMethod(operationName = "getStudentsForCourse")
    public List<String> getStudentsForCourse(String name) {
        List<String> studentsList = new ArrayList<String>();
        try {
            if (name == "") throw new AngryDatabaseException("Cannot get students for a unspecified course");
            Statement stm = conn.createStatement();
            Statement stm2 = conn.createStatement();
            ResultSet cRs = stm.executeQuery("SELECT cid FROM courses WHERE name='" + name + "'");
            if (cRs.next()) {
                int cid = cRs.getInt("cid");
                ResultSet rRs = stm.executeQuery("SELECT sid FROM relations WHERE cid=" + cid);
                while (rRs.next()) {
                    int sid = rRs.getInt("sid");
                    ResultSet sRs = stm2.executeQuery("SELECT name FROM students WHERE sid=" + sid);
                    if (sRs.next()) {
                        studentsList.add(sRs.getString("name"));
                    }
                }
            }
        } catch (Exception e) {
            throw new AngryDatabaseException("Could not get the list of students for the course " + name + ".\nExceptionTrace: " + e.getMessage());
        }
        return studentsList;
    }

    @WebMethod(operationName = "getCourses")
    public List<Course> getCourses(int year, int semester) {
        ArrayList<Course> courses = null;
        try {
            courses = new ArrayList<Course>();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM courses WHERE year=" + year + " AND semester=" + semester);
            while (rs.next()) {
                courses.add(new Course(rs.getString("name"), rs.getString("professor"), year, semester));
            }
        } catch (Exception e) {
            throw new AngryDatabaseException("Could not get the courses for the year of study " + year + ".\nExceptionTrace: " + e.getMessage());
        }
        return courses;
    }
}
