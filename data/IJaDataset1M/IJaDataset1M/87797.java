package uit.pubguru.dbaccess;

import uit.pubguru.utility.PubGuruLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import uit.pubguru.dto.ConferencePCMemberDTO;
import uit.pubguru.dto.database.PubGuruDB;

/**
 * NOTE: don't set the index value automatic increasing
 * @author Loc Do
 * @author Nghiep Tran
 * @author Huong Tran
 */
public class ConferencePCMemberMapper extends MapperDB {

    public ConferencePCMemberMapper() throws Exception {
        super();
    }

    public ConferencePCMemberMapper(Connection con) {
        super(con);
    }

    /**
     * insert a new ConferencePCMember object into the DB
     * @param ConferencePCMemberDTO
     * @return the last inserted ConferencePCMember id
     * @throws Exception
     */
    public int insertObj(ConferencePCMemberDTO objDTO) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("INSERT INTO " + PubGuruDB.DBNAME + "." + "Conference_PCMember(idConference," + " idPCMember, year)");
            sql.append(" VALUES(?,?, ?)");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, objDTO.getIdConference());
            stmt.setInt(2, objDTO.getIdPCMember());
            stmt.setInt(3, objDTO.getYear());
            stmt.execute();
            ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            int insertedID = -1;
            if (rs != null && rs.next()) insertedID = rs.getInt(1);
            stmt.close();
            return insertedID;
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) for (Object stackTraceElement : arrObj) PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
            throw ex;
        }
    }

    /**
     * check if the specified ConferencePCMember exist in the DB
     * @param ConferencePCMemberDTO
     * @return -1 if not found
     * @return  n if found existed
     * @throws Exception
     */
    public int isExisted(ConferencePCMemberDTO objDTO) throws Exception {
        int idObj = -1;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT * FROM " + PubGuruDB.DBNAME + "." + "Conference_PCMember");
            sql.append(" WHERE idConference=? AND idPCMember=?");
            Connection con = this.getConnection();
            if (con != null) {
                PreparedStatement stmt = con.prepareStatement(sql.toString());
                stmt.setInt(1, objDTO.getIdConference());
                stmt.setInt(2, objDTO.getIdPCMember());
                ResultSet rs = stmt.executeQuery();
                if ((rs != null) && (rs.next())) {
                    idObj = 1;
                }
                stmt.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) for (Object stackTraceElement : arrObj) PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
            throw ex;
        }
        return idObj;
    }

    /**
     * update the existing record in DB
     * @param ConferencePCMemberDTO
     * @return 1 if update successfully
     * @return 0 if update unsuccessfully
     * @throws Exception
     */
    public int updateObj(ConferencePCMemberDTO objDTO) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("UPDATE " + PubGuruDB.DBNAME + "." + "Conference_PCMember");
            sql.append(" SET year=?");
            sql.append(" WHERE idConference=? AND idPCMember=?");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, objDTO.getYear());
            stmt.setInt(2, objDTO.getIdConference());
            stmt.setInt(3, objDTO.getIdPCMember());
            Boolean isOk = stmt.execute();
            stmt.close();
            if (isOk) return 1; else return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) for (Object stackTraceElement : arrObj) PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
            throw ex;
        }
    }
}
