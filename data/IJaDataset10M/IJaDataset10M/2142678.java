package com.student.dao.impl;

import java.util.List;
import org.springframework.stereotype.Component;
import com.student.dao.AttendanceDao;
import com.student.model.AttendanceRecord;
import com.student.model.AttendanceType;
import com.student.model.Student;
import com.student.util.PageInfo;

/**
 * 
 * @Description:TODO AttendanceDao��ʵ����
 * @Author:������
 * @createTime:2011-10-29-����04:03:29
 * @Author:�¸��٣�����
 * @createTime:2011-10-31-����13:10
 * @version: 1.0
 * 
 */
@Component("attendanceDao")
public class AttendanceDaoImpl extends HibernateDaoImpl implements AttendanceDao {

    /**
	 * @Author:�¸���  
	 * @createTime:2011-10-31-����13:10
	 */
    public void addAttendanceRecord(AttendanceRecord ar) throws Exception {
        this.save(ar);
    }

    /**
	 * @Author:�¸���
	 * @createTime:2011-10-31-����13:10
	 */
    public void deleteAttendanceRecord(Long arId) throws Exception {
        this.delete(AttendanceRecord.class, arId);
    }

    /**
	 * @Author:�¸���
	 * @createTime:2011-10-31-����13:10 ͨ��ѧ���ѧ�Ų鿴��ѧ��Ŀ�����ϸ
	 */
    public List<AttendanceRecord> getAttendDetailByStuId(String studentNumber) throws Exception {
        String hql = "FROM AttendanceRecord ard left join fetch ard.student where ard.student.studentNumber=" + studentNumber;
        List<AttendanceRecord> attendanceRecord = find(hql);
        return attendanceRecord;
    }

    public List<Student> getAttendStuListAll(Long collegeId, Long gradeId, Long progessionId, Long classId, String isAllAttend, String orderType, PageInfo pageInfo) throws Exception {
        String hql = "FROM Student s where 1=1";
        if (collegeId != null) {
            hql += " AND s.classInfo.profession.college.collegeId='" + collegeId + "'";
        }
        if (gradeId != null) {
            hql += "AND s.classInfo.gradeInfo.gradeId='" + gradeId + "'";
        }
        if (progessionId != null) {
            hql += "AND s.classInfo.profession.professionId='" + progessionId + "'";
        }
        if (classId != null) {
            hql += "AND s.classInfo.classId='" + classId + "'";
        }
        if (isAllAttend != null) {
            if (isAllAttend.trim().equals("yes")) {
                hql += "AND s.studentId in (select ar.student.studentId from AttendanceRecord ar where ar.attendanceType.attendanceTypeName!='���' and (select sum(ar2.count) from AttendanceRecord ar2 where ar2.student.studentId=ar.student.studentId)=0)";
            } else if (isAllAttend.trim().equals("no")) {
                hql += "AND s.studentId in (select ar.student.studentId from AttendanceRecord ar where ar.attendanceType.attendanceTypeName!='���' and (select sum(ar2.count) from AttendanceRecord ar2 where ar2.student.studentId=ar.student.studentId)>0)";
            }
        }
        if (orderType != null && !"".equals(orderType)) {
            String condition = "";
            if (orderType.equals("clazz")) {
                condition = " s.classInfo.profession.professionId,s.classInfo.className";
            } else if (orderType.equals("stuNumber")) {
                condition = " s.studentNumber";
            }
            hql += "ORDER BY" + condition;
        } else {
            hql += "ORDER BY s.studentId";
        }
        final String sql = hql;
        List<Student> stuList = null;
        if (pageInfo == null) {
            stuList = find(sql);
        } else {
            stuList = findPageByQuery(sql, null, pageInfo);
        }
        return stuList;
    }

    /**
	 * @Author:�¸���
	 * @createTime:2011-10-31-����13:10
	 */
    public Student getStuDetailById(String studentNumber) throws Exception {
        String hql = "FROM Student s where s.studentNumber=" + studentNumber;
        List<Student> student = find(hql);
        return student.get(0);
    }

    /**
	 * @Author:�¸���
	 * @createTime:2011-10-31-����13:10
	 */
    public void updateAttendStuInfo(Student stu) throws Exception {
        this.update(stu);
    }

    /**
	 * @Author:�¸���
	 * @createTime:2011-10-31-����13:10
	 */
    public void updateAttendanceRecord(AttendanceRecord ar) throws Exception {
        this.update(ar);
    }

    /**
	 * ���ID���һ�����ڼ�¼
	 */
    public AttendanceRecord getAttendanceRecordByArId(Long arId) throws Exception {
        AttendanceRecord ar = (AttendanceRecord) this.get(AttendanceRecord.class, arId);
        return ar;
    }

    /**
	 * ��ݿ�������ID���һ����������
	 */
    public AttendanceType getAttendanceTypeById(Long atId) throws Exception {
        AttendanceType at = (AttendanceType) this.get(AttendanceType.class, atId);
        return at;
    }

    /**
	 * ��ݿ��ڼ�¼ID�鴦���п�������
	 */
    public List<AttendanceType> getAllAttendanceType(Long arId) throws Exception {
        List<AttendanceType> list = null;
        AttendanceRecord ar = (AttendanceRecord) get(AttendanceRecord.class, arId);
        return list;
    }

    /**
	 * ����ܴβ�����м�¼
	 */
    public List<AttendanceRecord> getAttendRecordByWeek(Integer week, String studentNumber) throws Exception {
        String sql = "FROM AttendanceRecord ard left join fetch ard.student where ard.student.studentNumber=" + studentNumber + " AND ard.week=" + week;
        List<AttendanceRecord> list = this.find(sql);
        return list;
    }
}
