package ces.coffice.addrslist.dao.iplm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Vector;
import java.sql.Timestamp;
import ces.coffice.addrslist.AddrException;
import ces.coffice.addrslist.vo.AddrslistEntry;
import ces.coffice.common.base.BaseDao;
import ces.coffice.common.base.BaseVo;
import ces.coffice.common.base.DbBase;
import ces.coral.dbo.DBOperation;
import ces.coral.log.Logger;
import ces.platform.system.common.IdGenerator;
import ces.platform.system.common.ValueAsc;

public class AddrslistMainDao extends DbBase implements BaseDao {

    static Logger logger = new Logger(AddrslistMainDao.class);

    public static final String ENTRY_TABLE = "coffice_addrslist_entry";

    private int id;

    private int folderId;

    private String surName;

    private String name;

    private String unit;

    private String dept;

    private String job;

    private String email;

    private String mobile;

    private String officePosition;

    private String comAddress;

    private String comTel;

    private String comZip;

    private String comFix;

    private String homeAddress;

    private String homeTel;

    private String homeZip;

    private String homePage;

    private int userId;

    private int orgId;

    private String birthday;

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
	 * @return
	 */
    public String getComAddress() {
        return this.comAddress;
    }

    /**
	 * @return
	 */
    public String getComFix() {
        return this.comFix;
    }

    /**
	 * @return
	 */
    public String getComTel() {
        return this.comTel;
    }

    /**
	 * @return
	 */
    public String getComZip() {
        return this.comZip;
    }

    /**
	 * @return
	 */
    public String getDept() {
        return this.dept;
    }

    /**
	 * @return
	 */
    public String getEmail() {
        return this.email;
    }

    /**
	 * @return
	 */
    public int getFolderId() {
        return this.folderId;
    }

    /**
	 * @return
	 */
    public String getHomeAddress() {
        return this.homeAddress;
    }

    /**
	 * @return
	 */
    public String getHomePage() {
        return this.homePage;
    }

    /**
	 * @return
	 */
    public String getHomeTel() {
        return this.homeTel;
    }

    /**
	 * @return
	 */
    public String getHomeZip() {
        return this.homeZip;
    }

    /**
	 * @return
	 */
    public String getJob() {
        return this.job;
    }

    /**
	 * @return
	 */
    public String getMobile() {
        return this.mobile;
    }

    /**
	 * @return
	 */
    public String getOfficePosition() {
        return this.officePosition;
    }

    /**
	 * @return
	 */
    public int getOrgId() {
        return this.orgId;
    }

    /**
	 * @return
	 */
    public String getSurName() {
        return this.surName;
    }

    /**
	 * @return
	 */
    public String getUnit() {
        return this.unit;
    }

    /**
	 * @param string
	 */
    public void setComAddress(String comAddress) {
        this.comAddress = comAddress;
    }

    /**
	 * @param string
	 */
    public void setComFix(String comFix) {
        this.comFix = comFix;
    }

    /**
	 * @param string
	 */
    public void setComTel(String comTel) {
        this.comTel = comTel;
    }

    /**
	 * @param string
	 */
    public void setComZip(String comZip) {
        this.comZip = comZip;
    }

    /**
	 * @param string
	 */
    public void setDept(String dept) {
        this.dept = dept;
    }

    /**
	 * @param string
	 */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
	 * @param i
	 */
    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    /**
	 * @param string
	 */
    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    /**
	 * @param string
	 */
    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    /**
	 * @param string
	 */
    public void setHomeTel(String homeTel) {
        this.homeTel = homeTel;
    }

    /**
	 * @param string
	 */
    public void setHomeZip(String homeZip) {
        this.homeZip = homeZip;
    }

    /**
	 * @param string
	 */
    public void setJob(String job) {
        this.job = job;
    }

    /**
	 * @param string
	 */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
	 * @param string
	 */
    public void setOfficePosition(String officePosition) {
        this.officePosition = officePosition;
    }

    /**
	 * @param i
	 */
    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    /**
	 * @param string
	 */
    public void setSurName(String surName) {
        this.surName = surName;
    }

    /**
	 * @param string
	 */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    public AddrslistMainDao(int id) {
        this.id = id;
    }

    /**
	 * @return
	 */
    public String getBirthday() {
        return this.birthday;
    }

    /**
	 * @param string
	 */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public AddrslistMainDao() {
        this(0);
    }

    public void doAddBatch(Collection entitys) throws AddrException {
    }

    public void doDelBatch() throws AddrException {
    }

    public void doUpdateBatch() throws AddrException {
    }

    public void doNew() throws AddrException {
    }

    /**
	 * ɾ��ͨѶ¼��Ŀ��Ϣ
	 * @param boolean
	 * @throws AddrException
	 */
    public boolean doDelete(BaseVo vo) throws AddrException {
        boolean success = false;
        AddrslistEntry addrsEntry = (AddrslistEntry) vo;
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        String sql = "delete from " + ENTRY_TABLE + " where id=?";
        try {
            dbo = createDBOperation();
            connection = dbo.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, addrsEntry.getId());
            ps.executeUpdate();
            success = true;
        } catch (Exception ex) {
            logger.error("ɾ��ͨѶ¼��Ŀ��Ϣʧ��,��ĿID " + addrsEntry.getId() + " " + ex.getMessage());
            throw new AddrException("ɾ��ͨѶ¼��Ŀ��Ϣʧ��!");
        } finally {
            close(resultSet, null, ps, connection, dbo);
        }
        return success;
    }

    /**
	 * ����ͨѶ¼��Ŀ��Ϣ
	 * @param void
	 * @throws AddrException
	 */
    public void doNew(BaseVo vo) throws AddrException {
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        AddrslistEntry addrsEntry = (AddrslistEntry) vo;
        String sql = "insert into " + ENTRY_TABLE + "(ID, FOLDER_ID, SURNAME, NAME, UNIT, DEPT, JOB, EMAIL, MOBILE, OFFICE_POSITION, COMPANY_ADDRES, COMPANY_TEL, COMPANY_ZIP, COMPANY_FIX, HOME_ADDRES, HOME_TEL, HOME_ZIP, HOMEPAGE, USER_ID, ORG_ID, BIRTHDAY) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            dbo = createDBOperation();
            connection = dbo.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, addrsEntry.getId());
            ps.setInt(2, addrsEntry.getFolderId());
            ps.setString(3, addrsEntry.getSurName());
            ps.setString(4, addrsEntry.getName());
            ps.setString(5, addrsEntry.getUnit());
            ps.setString(6, addrsEntry.getDept());
            ps.setString(7, addrsEntry.getJob());
            ps.setString(8, addrsEntry.getEmail());
            ps.setString(9, addrsEntry.getMobile());
            ps.setString(10, addrsEntry.getOfficePosition());
            ps.setString(11, addrsEntry.getComAddress());
            ps.setString(12, addrsEntry.getComTel());
            ps.setString(13, addrsEntry.getComZip());
            ps.setString(14, addrsEntry.getComFix());
            ps.setString(15, addrsEntry.getHomeAddress());
            ps.setString(16, addrsEntry.getHomeTel());
            ps.setString(17, addrsEntry.getHomeZip());
            ps.setString(18, addrsEntry.getHomePage());
            ps.setInt(19, addrsEntry.getUserId());
            ps.setInt(20, addrsEntry.getOrgId());
            ps.setTimestamp(21, addrsEntry.getBirthday() == null || "".equals(addrsEntry.getBirthday()) ? null : Timestamp.valueOf(addrsEntry.getBirthday() + " 00:00:00.000000000"));
            int resultCount = ps.executeUpdate();
            if (resultCount != 1) {
                throw new Exception("error");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("����ͨѶ¼��Ϣʧ��, " + ex.getMessage());
            throw new AddrException("����ͨѶ¼��Ϣʧ��!");
        } finally {
            close(resultSet, null, ps, connection, dbo);
        }
    }

    /**
	 * �޸�ͨѶ¼��Ŀ��Ϣ
	 * @param boolean
	 * @throws AddrException
	 */
    public boolean doUpdate(BaseVo vo) throws AddrException {
        boolean success = false;
        AddrslistEntry addrsEntry = (AddrslistEntry) vo;
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        String sql = "update " + ENTRY_TABLE + " set folder_id=?,surname=?,name=?," + "unit=?,dept=?,job=?,email=?,mobile=?,office_position=?,company_addres=?," + "company_tel=?,company_zip=?,company_fix=?,home_addres=?,home_tel=?," + "home_zip=?,homepage=?,user_id=?,org_id=?,birthday=? where id=?";
        try {
            dbo = createDBOperation();
            connection = dbo.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, addrsEntry.getFolderId());
            ps.setString(2, addrsEntry.getSurName());
            ps.setString(3, addrsEntry.getName());
            ps.setString(4, addrsEntry.getUnit());
            ps.setString(5, addrsEntry.getDept());
            ps.setString(6, addrsEntry.getJob());
            ps.setString(7, addrsEntry.getEmail());
            ps.setString(8, addrsEntry.getMobile());
            ps.setString(9, addrsEntry.getOfficePosition());
            ps.setString(10, addrsEntry.getComAddress());
            ps.setString(11, addrsEntry.getComTel());
            ps.setString(12, addrsEntry.getComZip());
            ps.setString(13, addrsEntry.getComFix());
            ps.setString(14, addrsEntry.getHomeAddress());
            ps.setString(15, addrsEntry.getHomeTel());
            ps.setString(16, addrsEntry.getHomeZip());
            ps.setString(17, addrsEntry.getHomePage());
            ps.setInt(18, addrsEntry.getUserId());
            ps.setInt(19, addrsEntry.getOrgId());
            ps.setTimestamp(20, addrsEntry.getBirthday() == null || "".equals(addrsEntry.getBirthday()) ? null : Timestamp.valueOf(addrsEntry.getBirthday() + " 00:00:00.000000000"));
            ps.setInt(21, addrsEntry.getId());
            ps.executeUpdate();
            success = true;
        } catch (Exception ex) {
            logger.error("�޸�ͨѶ¼��Ϣʧ��, ��ĿID " + addrsEntry.getId() + " " + ex.getMessage());
            throw new AddrException("�޸�ͨѶ¼��Ϣʧ��!");
        } finally {
            close(resultSet, null, ps, connection, dbo);
        }
        return success;
    }

    /**
	 * �õ�ͨѶ¼�б���Ϣ
	 * @param Vector
	 * @throws AddrException
	 */
    public Vector getEntities(String condition) throws AddrException {
        Vector vec = new Vector();
        AddrslistEntry addrsEntry = null;
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String sql = "select id,folder_id,surname,name,unit,dept,job,email,mobile," + "office_position,company_addres,company_tel,company_zip,company_fix," + "home_addres,home_tel,home_zip,homepage,user_id,org_id,birthday " + "from " + ENTRY_TABLE + " ";
        sql += (condition == null ? "" : condition);
        sql += " order by id";
        try {
            dbo = createDBOperation();
            connection = dbo.getConnection();
            ps = connection.prepareStatement(sql);
            result = ps.executeQuery();
            int i = 1;
            ValueAsc va = new ValueAsc(i);
            while (result.next()) {
                i = 1;
                va.setStart(i);
                addrsEntry = AddrslistMainDao.generateEntry(result, va);
                vec.addElement(addrsEntry);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("�õ�ͨѶ¼�б���Ϣʧ��, ��ĿID " + addrsEntry.getId() + " " + ex.getMessage());
            throw new AddrException("�õ�ͨѶ¼�б���Ϣʧ��!");
        } finally {
            close(result, null, ps, connection, dbo);
        }
        return vec;
    }

    /**
	 * �õ���ѡ���ͨѶ¼��Ŀ��Ϣ
	 * @param vo
	 * @throws AddrException
	 */
    public BaseVo getAddrsEntry(String id) throws AddrException {
        AddrslistEntry addrsEntry = null;
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String sql = "select id,folder_id,surname,name,unit,dept,job,email,mobile," + "office_position,company_addres,company_tel,company_zip,company_fix," + "home_addres,home_tel,home_zip,homepage,user_id,org_id,birthday " + "from " + ENTRY_TABLE + " where id=?";
        try {
            dbo = createDBOperation();
            connection = dbo.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));
            result = ps.executeQuery();
            int i = 1;
            ValueAsc va = new ValueAsc(i);
            while (result.next()) {
                i = 1;
                va.setStart(i);
                addrsEntry = AddrslistMainDao.generateEntry(result, va);
            }
        } catch (Exception ex) {
            logger.error("�õ���ѡ���ͨѶ¼��Ŀ��Ϣ, ��ĿID " + addrsEntry.getId() + " " + ex.getMessage());
            throw new AddrException("�õ���ѡ���ͨѶ¼��Ŀ��Ϣʧ��!");
        } finally {
            close(result, null, ps, connection, dbo);
        }
        return addrsEntry;
    }

    /**
	 * �õ���ѡ���ͨѶ¼��Ŀ��Ϣ
	 * @param vo
	 * @throws AddrException
	 */
    public BaseVo getEntity(String condition) throws AddrException {
        AddrslistEntry addrsEntry = null;
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rset = null;
        String sql = "select id,folder_id,surname,name,unit,dept,job,email,mobile," + "office_position,company_addres,company_tel,company_zip,company_fix," + "home_addres,home_tel,home_zip,homepage,user_id,org_id,birthday " + "from " + ENTRY_TABLE + " ";
        sql += (condition == null ? "" : condition);
        try {
            dbo = createDBOperation();
            connection = dbo.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            rset = preparedStatement.executeQuery();
            int i = 1;
            ValueAsc va = new ValueAsc(i);
            if (rset.next()) {
                i = 1;
                va.setStart(i);
                addrsEntry = AddrslistMainDao.generateEntry(rset, va);
            }
        } catch (Exception ex) {
            logger.error("�õ���ѡ���ͨѶ¼��Ŀ��Ϣ, ��ĿID " + addrsEntry.getId() + " " + ex.getMessage());
            throw new AddrException("�õ���ѡ���ͨѶ¼��Ŀ��Ϣʧ��!");
        } finally {
            close(rset, null, preparedStatement, connection, dbo);
        }
        return addrsEntry;
    }

    /**
     * ���ͨѶ¼����
     * @param result   ��ѯ���
     * @param v        ������
     * @return         ������ɵĶ���
     */
    public static AddrslistEntry generateEntry(ResultSet result, ValueAsc v) throws AddrException {
        AddrslistEntry addrsEntry = null;
        try {
            addrsEntry = new AddrslistEntry(result.getInt(v.next()));
            addrsEntry.setFolderId(result.getInt(v.next()));
            addrsEntry.setSurName(result.getString(v.next()));
            addrsEntry.setName(result.getString(v.next()));
            addrsEntry.setUnit(result.getString(v.next()));
            addrsEntry.setDept(result.getString(v.next()));
            addrsEntry.setJob(result.getString(v.next()));
            addrsEntry.setEmail(result.getString(v.next()));
            addrsEntry.setMobile(result.getString(v.next()));
            addrsEntry.setOfficePosition(result.getString(v.next()));
            addrsEntry.setComAddress(result.getString(v.next()));
            addrsEntry.setComTel(result.getString(v.next()));
            addrsEntry.setComZip(result.getString(v.next()));
            addrsEntry.setComFix(result.getString(v.next()));
            addrsEntry.setHomeAddress(result.getString(v.next()));
            addrsEntry.setHomeTel(result.getString(v.next()));
            addrsEntry.setHomeZip(result.getString(v.next()));
            addrsEntry.setHomePage(result.getString(v.next()));
            addrsEntry.setUserId(result.getInt(v.next()));
            addrsEntry.setOrgId(result.getInt(v.next()));
            addrsEntry.setBirthday(result.getString(v.next()));
        } catch (Exception ex) {
            logger.error("���ͨѶ¼����ʧ��, " + ex.getMessage());
            throw new AddrException("���ͨѶ¼����ʧ��!");
        }
        return addrsEntry;
    }

    /**
	 * �õ�����ͨѶ¼��Ŀ��ϢID
	 * @param boolean
	 * @throws AddrException
	 */
    public static int getNewID() throws Exception {
        return ((int) IdGenerator.getInstance().getId(IdGenerator.GEN_ID_COFFICE_ADDRS_ENTRY));
    }

    public void doDelBatch(Collection entitys) throws Exception {
    }

    public void doUpdateBatch(Collection entitys) throws Exception {
    }
}
