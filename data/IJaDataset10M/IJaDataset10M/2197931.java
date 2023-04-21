package org.paradise.dms.dao.impl;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.paradise.dms.pojo.DormitoryCharge;
import org.paradise.dms.pojo.Student;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;
import com.dheaven.framework.dao.BaseHibernateDao;
import com.dheaven.framework.dao.DaoException;

/**
 * Description: DormitoryChargeDAO
 * 
 * @author kk
 * @Version1.0 Apr 15th, 2009 20:52:59 PM 闫克 （kurtyan777@gmail.com） created
 * 
 */
@Service
public class DormitoryChargeDAOImpl extends BaseHibernateDao<DormitoryCharge> {

    private static Logger log = Logger.getLogger(StudentBedLinkDAOImpl.class);

    /**
	 * Description 根据入学年份、交费年份、住宿状态、延期状态来分页查询学生交费信息
	 * 
	 * @Version1.0 2009-4-17 00:09:59 闫克（kurtyan777@gmail.com） 创建
	 * 
	 * @param enrollyear
	 * @param chargeyear
	 * @param chargestatus
	 * @param isextension
	 * @param startrow
	 * @param pagesize
	 * @return List including studentid, studentno, studentname,
	 *         dormitorychargefees
	 */
    @SuppressWarnings("unchecked")
    public List getDormitoryCharge(final String enrollyear, final String chargeyear, final int chargestatus, final int isextension, final int startrow, final int pagesize) {
        StringBuffer conditionQuery = new StringBuffer("where ");
        int statusTemp = 0;
        if (!"-1".equals(enrollyear)) {
            conditionQuery.append("student.studentenrollyear=:enrollyear and ");
            statusTemp = 1;
        }
        conditionQuery.append("dormitorycharge.dormitorychargeyear=:chargeyear and dormitorycharge.dormitorychargestatus=:chargestatus ");
        if (!"-1".equals(isextension + "")) {
            conditionQuery.append("and dormitorycharge.isextension=:isextension ");
            statusTemp += 2;
        }
        final int status = statusTemp;
        final String wheresql = conditionQuery.toString();
        return (List) this.getHibernateTemplate().executeFind(new HibernateCallback() {

            public List doInHibernate(Session session) {
                SQLQuery query = (SQLQuery) session.createSQLQuery("select student.studentid, student.studentno, student.studentname, dormitorycharge.dormitorychargefees,dormitorycharge.dormitorychargestatus,dormitorycharge.isextension,collegeinfo.collegeinfoname,dormitory.dormitorydisplayname from dormitorycharge Left Join student ON dormitorycharge.studentid = student.studentid Left Join collegeinfo ON student.collegeinfoid = collegeinfo.collegeinfoid Left Join studentbedlink ON dormitorycharge.studentid = studentbedlink.studentid Inner Join dormitory ON studentbedlink.dormitoryid = dormitory.dormitoryid " + wheresql + " limit :startrow,:pagesize");
                switch(status) {
                    case 1:
                        query.setString("enrollyear", enrollyear);
                        break;
                    case 2:
                        query.setInteger("isextension", isextension);
                        break;
                    case 3:
                        query.setString("enrollyear", enrollyear);
                        query.setInteger("isextension", isextension);
                }
                query.setString("chargeyear", chargeyear);
                query.setInteger("chargestatus", chargestatus);
                query.setInteger("startrow", startrow);
                query.setInteger("pagesize", pagesize);
                return query.list();
            }
        });
    }

    /**
	 * 
	 * Description:获得分页的总行数
	 * 
	 * @Version1.0 Apr 18, 2009 7:36:47 PM 李双江（paradise.lsj@gmail.com）创建
	 * @param enrollyear
	 *            学生学年
	 * @param feesyear
	 *            缴费学年
	 * @param chargestatus
	 *            缴费状态
	 * @param lodgechargetype
	 *            缴费列别（延期or正常住宿缴费）
	 * 
	 * @return
	 */
    public int getStudentLodgeChargeInfoRows(String enrollyear, String feesyear, String chargestatus, String lodgechargetype) {
        StringBuffer conditionQuery = new StringBuffer("where ");
        int statusTemp = 0;
        if (!"-1".equals(enrollyear)) {
            conditionQuery.append("student.studentenrollyear=:enrollyear and ");
            statusTemp = 1;
        }
        conditionQuery.append("dormitorycharge.dormitorychargeyear=:chargeyear and dormitorycharge.dormitorychargestatus=:chargestatus ");
        if (!"-1".equals(lodgechargetype + "")) {
            conditionQuery.append("and dormitorycharge.isextension=:isextension ");
            statusTemp += 2;
        }
        final int status = statusTemp;
        SQLQuery query = this.getSession().createSQLQuery("select student.studentid, student.studentno, student.studentname, dormitorycharge.dormitorychargefees,dormitorycharge.dormitorychargestatus,dormitorycharge.isextension,collegeinfo.collegeinfoname,dormitory.dormitorydisplayname from dormitorycharge Left Join student ON dormitorycharge.studentid = student.studentid Left Join collegeinfo ON student.collegeinfoid = collegeinfo.collegeinfoid Left Join studentbedlink ON dormitorycharge.studentid = studentbedlink.studentid Inner Join dormitory ON studentbedlink.dormitoryid = dormitory.dormitoryid " + conditionQuery.toString());
        switch(status) {
            case 1:
                query.setString("enrollyear", enrollyear);
                break;
            case 2:
                query.setInteger("isextension", Integer.parseInt(lodgechargetype));
                break;
            case 3:
                query.setString("enrollyear", enrollyear);
                query.setInteger("isextension", Integer.parseInt(lodgechargetype));
        }
        query.setString("chargeyear", feesyear);
        query.setInteger("chargestatus", Integer.parseInt(chargestatus));
        return query.list().size();
    }

    @SuppressWarnings("unchecked")
    public List<DormitoryCharge> getDormitoryChargeByStudentId(String studentid) throws DaoException {
        return this.find("from DormitoryCharge d where d.studentid=?", new Object[] { studentid });
    }

    /**
	 * 
	 * Description:查询所有满足条件的chargefee 导出到Excel中
	 * 
	 * @Version1.0 May 1, 2009 10:42:32 PM 李双江（paradise.lsj@gmail.com）创建
	 * @param enrollyear
	 * @param chargeyear
	 * @param chargestatus
	 * @param isextension
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public List getDormitoryChargeForExport(final String enrollyear, final String chargeyear, final int chargestatus, final int isextension) {
        StringBuffer conditionQuery = new StringBuffer("where ");
        int statusTemp = 0;
        if (!"-1".equals(enrollyear)) {
            conditionQuery.append("student.studentenrollyear=:enrollyear and ");
            statusTemp = 1;
        }
        conditionQuery.append("dormitorycharge.dormitorychargeyear=:chargeyear and dormitorycharge.dormitorychargestatus=:chargestatus ");
        if (!"-1".equals(isextension + "")) {
            conditionQuery.append("and dormitorycharge.isextension=:isextension ");
            statusTemp += 2;
        }
        final int status = statusTemp;
        final String wheresql = conditionQuery.toString();
        return (List) this.getHibernateTemplate().executeFind(new HibernateCallback() {

            public List doInHibernate(Session session) {
                SQLQuery query = (SQLQuery) session.createSQLQuery("select student.studentid, student.studentno, student.studentname,dormitorycharge.dormitorychargeyear, dormitorycharge.dormitorychargefees from dormitorycharge Left Join student ON dormitorycharge.studentid = student.studentid Left Join collegeinfo ON student.collegeinfoid = collegeinfo.collegeinfoid Left Join studentbedlink ON dormitorycharge.studentid = studentbedlink.studentid Inner Join dormitory ON studentbedlink.dormitoryid = dormitory.dormitoryid " + wheresql);
                switch(status) {
                    case 1:
                        query.setString("enrollyear", enrollyear);
                        break;
                    case 2:
                        query.setInteger("isextension", isextension);
                        break;
                    case 3:
                        query.setString("enrollyear", enrollyear);
                        query.setInteger("isextension", isextension);
                }
                query.setString("chargeyear", chargeyear);
                query.setInteger("chargestatus", chargestatus);
                return query.list();
            }
        });
    }

    /**
	 * 
	 * Description: 返回总行数，根据sql语句高级查询学生信息
	 * 
	 * @param searchstudentsql
	 * @return
	 */
    public int getCount(final String searchstudentsql) {
        return (Integer) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(org.hibernate.Session s) throws HibernateException, SQLException {
                String sqltosearch = "select count(*) from student,studentbedlink,dormitory where  student.studentind=1 and (" + searchstudentsql.substring(searchstudentsql.indexOf("where") + 5) + ")";
                BigInteger b = (BigInteger) s.createSQLQuery(sqltosearch).uniqueResult();
                System.out.println(sqltosearch);
                return b.intValue();
            }
        });
    }

    /**
	 * 
	 * Description:根据sql语句高级查询未交费学生信息
	 * 
	 * @Version1.0 2009-3-27 下午10:48:38 李双江（paradise.lsj@gmail.com）创建
	 * @param searchstudentsql
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public List<Student> findStudentBySQLSearchConditions(final String searchstudentsql, final int pageSize, final int startRow) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {

            public Object doInHibernate(org.hibernate.Session s) throws HibernateException, SQLException {
                String newSql = "select student.studentid,student.studentno,student.studentname,student.studentgender,";
                newSql += "student.studentclass,dormitory.dormitorydisplayname ";
                newSql += " from student,studentbedlink,dormitory where student.studentind=1 and (";
                newSql += searchstudentsql.substring(searchstudentsql.indexOf("where") + 5) + ")";
                String sqltosearch = newSql + " limit " + startRow + "," + pageSize;
                Query query = s.createSQLQuery(sqltosearch);
                return query.list();
            }
        });
    }

    /**
	 * 
	 * Description: 高级查询页面全选所有查询到的学生
	 * 
	 * @Version1.0 Apr 21, 2009 4:08:42 PM 郑旭（zhengxu2006@gmail.com）创建
	 * @param searchstudentsql
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public List getStudentBySQL(final String searchstudentsql) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {

            public Object doInHibernate(org.hibernate.Session s) throws HibernateException, SQLException {
                String newSql = "select student.studentid from student,studentbedlink,dormitory where student.studentind=1 and (";
                newSql += searchstudentsql.substring(searchstudentsql.indexOf("where") + 5) + ")";
                Query query = s.createSQLQuery(newSql);
                return query.list();
            }
        });
    }

    /**
	 * 
	 * Description: 根据学生ID和年份生成计费信息
	 * 
	 * @Version1.0 Apr 21, 2009 4:08:42 PM 郑旭（zhengxu2006@gmail.com）创建
	 * @param searchstudentsql
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public boolean exportToCharge(final String studentids, final String year) {
        return (Boolean) this.getHibernateTemplate().execute(new HibernateCallback() {

            public Boolean doInHibernate(Session session) {
                String arr[] = studentids.split(",");
                int now = 0;
                for (int i = 0; i < arr.length; ) {
                    StringBuffer sb = new StringBuffer(6000);
                    while (i < arr.length && i < (now + 1000)) {
                        sb.append(arr[i]).append(",");
                        i++;
                    }
                    String ids = sb.toString();
                    ids = ids.substring(0, ids.length() - 1);
                    SQLQuery query = (SQLQuery) session.createSQLQuery("call CreateDormitoryCharge(?, ?,?)");
                    query.setString(0, ids);
                    query.setInteger(1, Integer.parseInt(year));
                    query.setInteger(2, i - now);
                    now += 1000;
                    int count = query.executeUpdate();
                    if (count > 0) {
                        log.info("DMS_info:交费信息插入成功" + count);
                    } else {
                        log.error("DMS_error:交费信息插入失败");
                        return false;
                    }
                }
                return true;
            }
        });
    }
}
