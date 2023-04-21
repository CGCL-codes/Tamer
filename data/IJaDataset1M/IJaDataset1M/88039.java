package com.hs.dao;

import java.sql.Date;
import java.util.List;
import org.springframework.dao.DataAccessException;
import com.hs.domain.Attendance;
import com.hs.domain.Department;
import com.hs.domain.Employee;
import com.hs.domain.UnsolvedAttendance;
import com.hs.vo.AttendanceReportVO;
import com.hs.vo.AttendanceVO;

/**
 * @author <a href="mailto:guangzong@gmail.com">Guangzong Syu</a>
 * 
 */
public interface AttendanceDao {

    List<Object> getAttendances(Date from, Date to, Department department) throws DataAccessException;

    void saveAttendances(List<Attendance> attendances) throws DataAccessException;

    void saveUnsolvedAttendance(UnsolvedAttendance ua) throws DataAccessException;

    AttendanceVO findAttendance(Date date, Employee employee, boolean actural) throws DataAccessException;

    List<AttendanceVO> findAttendances(Date from, Date to, Employee employee, boolean actural) throws DataAccessException;

    boolean isAppliedLeave(Employee employee, Date date) throws DataAccessException;

    List<UnsolvedAttendance> findUnsolvedAttendances(Date from, Date to, Employee employee) throws DataAccessException;

    UnsolvedAttendance getUnsolvedAttendance(Long id) throws DataAccessException;

    List<Attendance> findAttendances(Date from, Date to) throws DataAccessException;

    List<AttendanceReportVO> populateAttendances(Date from, Date to, boolean actual) throws DataAccessException;

    void deleteUnsolvedAttendance(Long unsolvedAttendanceId) throws DataAccessException;

    void deleteAttendance(Long id) throws DataAccessException;

    void deleteAllAttendance() throws DataAccessException;
}
