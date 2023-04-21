package uit.pubguru.dbaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import uit.pubguru.dto.PaperDTO;
import uit.pubguru.dto.database.PubGuruDB;
import uit.pubguru.utility.IdDTO;
import uit.pubguru.utility.PubGuruLogger;

/**
 *
 * @author THNghiep
 */
public class RankMapper extends MapperDB {

    public RankMapper() throws Exception {
        super();
    }

    public RankMapper(Connection con) {
        super(con);
    }

    /**
     * check if row with provided id is existing in the database.
     * @param idAuthor
     * @param idKeyword
     * @return -1 if not existing, otherwise 1.
     * @throws Exception
     * 
     * Note: This method is deprecated because project get all id in rank table instead of check for it later.
     */
    public int isExistingdAuthorKeyword(int idAuthor, int idKeyword) throws Exception {
        int isExisting = -1;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT * FROM " + PubGuruDB.DBNAME + "." + "_Rank_Author_Keyword r");
            sql.append(" WHERE r.idAuthor = ? AND r.idKeyword = ?");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idAuthor);
            stmt.setInt(2, idKeyword);
            ResultSet rs = stmt.executeQuery();
            if ((rs != null) && (rs.next())) {
                isExisting = 1;
            }
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return isExisting;
    }

    public ArrayList getAuthorIdList() throws Exception {
        ArrayList authorIdList = new ArrayList();
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT r.idAuthor FROM " + PubGuruDB.DBNAME + "." + "_rank_author r");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            ResultSet rs = stmt.executeQuery();
            while ((rs != null) && (rs.next())) {
                authorIdList.add(rs.getInt("idAuthor"));
            }
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return authorIdList;
    }

    public ArrayList getPublicationDTOListByAuthorId(int idAuthor) throws Exception {
        ArrayList publicationList = new ArrayList();
        PaperDTO paperDTO = null;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT distinct p.idPaper, rp.citationCount FROM " + PubGuruDB.DBNAME + "." + "author_paper a");
            sql.append(" JOIN " + PubGuruDB.DBNAME + "." + "paper p ON a.idPaper = p.idPaper");
            sql.append(" JOIN " + PubGuruDB.DBNAME + "." + "_rank_paper rp on p.idPaper = rp.idPaper");
            sql.append(" WHERE a.idAuthor = ?");
            sql.append(" ORDER BY rp.citationCount desc");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idAuthor);
            ResultSet rs = stmt.executeQuery();
            while ((rs != null) && (rs.next())) {
                paperDTO = new PaperDTO();
                paperDTO.setCitationCount(rs.getInt("citationCount"));
                publicationList.add(paperDTO);
            }
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return publicationList;
    }

    public int saveIndexesAuthor(int idAuthor, int h_index, int g_index) throws Exception {
        int result = -1;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" insert into " + PubGuruDB.DBNAME + "." + "_rank_author(idAuthor, h_index, g_index)");
            sql.append(" value (?, ?, ?)");
            sql.append(" on duplicate key update");
            sql.append(" h_index = ?, g_index = ?");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idAuthor);
            stmt.setInt(2, h_index);
            stmt.setInt(3, g_index);
            stmt.setInt(4, h_index);
            stmt.setInt(5, g_index);
            result = stmt.executeUpdate();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return result;
    }

    public ArrayList getAuthorSubdomainIdList(int limit) throws Exception {
        ArrayList idList = new ArrayList();
        IdDTO idDTO = null;
        int offset = 0;
        int rowCount = 0;
        StringBuffer sql = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            sql = new StringBuffer();
            sql.append(" SELECT count(*) FROM " + PubGuruDB.DBNAME + "." + "_rank_author_subdomain r");
            stmt = getConnection().prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            if ((rs != null) && (rs.next())) {
                rowCount = rs.getInt(1);
            }
            stmt.close();
            sql = new StringBuffer();
            sql.append(" SELECT r.idAuthor, r.idSubdomain FROM " + PubGuruDB.DBNAME + "." + "_rank_author_subdomain r");
            sql.append(" order by r.idAuthor, r.idSubdomain limit ?, ?");
            while (offset < rowCount) {
                stmt = getConnection().prepareStatement(sql.toString());
                stmt.setInt(1, offset);
                stmt.setInt(2, limit);
                rs = stmt.executeQuery();
                while ((rs != null) && (rs.next())) {
                    idDTO = new IdDTO();
                    idDTO.setId1(rs.getInt("idAuthor"));
                    idDTO.setId2(rs.getInt("idSubdomain"));
                    idList.add(idDTO);
                }
                stmt.close();
                offset += limit;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return idList;
    }

    public ArrayList getPublicationDTOListByAuthorIdSubdomainId(int idAuthor, int idSubdomain) throws Exception {
        ArrayList publicationList = new ArrayList();
        PaperDTO paperDTO = null;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT distinct p.idPaper, rp.citationCount FROM " + PubGuruDB.DBNAME + "." + "author_paper a");
            sql.append(" JOIN " + PubGuruDB.DBNAME + "." + "paper p ON a.idPaper = p.idPaper");
            sql.append(" JOIN " + PubGuruDB.DBNAME + "." + "subdomain_paper sp ON p.idPaper = sp.idPaper");
            sql.append(" JOIN " + PubGuruDB.DBNAME + "." + "_rank_paper rp on p.idPaper = rp.idPaper");
            sql.append(" WHERE a.idAuthor = ?");
            sql.append(" AND sp.idSubdomain = ?");
            sql.append(" ORDER BY rp.citationCount desc");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idAuthor);
            stmt.setInt(2, idSubdomain);
            ResultSet rs = stmt.executeQuery();
            while ((rs != null) && (rs.next())) {
                paperDTO = new PaperDTO();
                paperDTO.setCitationCount(rs.getInt("citationCount"));
                publicationList.add(paperDTO);
            }
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return publicationList;
    }

    public int saveIndexesAuthorSubdomain(int idAuthor, int idSubdomain, int h_index, int g_index) throws Exception {
        int result = -1;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" insert into " + PubGuruDB.DBNAME + "." + "_rank_author_subdomain(idAuthor, idSubdomain, h_index, g_index)");
            sql.append(" value (?, ?, ?, ?)");
            sql.append(" on duplicate key update");
            sql.append(" h_index = ?, g_index = ?");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idAuthor);
            stmt.setInt(2, idSubdomain);
            stmt.setInt(3, h_index);
            stmt.setInt(4, g_index);
            stmt.setInt(5, h_index);
            stmt.setInt(6, g_index);
            result = stmt.executeUpdate();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return result;
    }

    public ArrayList getAuthorKeywordIdList(int limit) throws Exception {
        ArrayList idList = new ArrayList();
        IdDTO idDTO = null;
        int offset = 0;
        int rowCount = 0;
        StringBuffer sql = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            sql = new StringBuffer();
            sql.append(" SELECT count(*) FROM " + PubGuruDB.DBNAME + "." + "_rank_author_keyword r");
            stmt = getConnection().prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            if ((rs != null) && (rs.next())) {
                rowCount = rs.getInt(1);
            }
            stmt.close();
            sql = new StringBuffer();
            sql.append(" SELECT r.idAuthor, r.idKeyword FROM " + PubGuruDB.DBNAME + "." + "_rank_author_keyword r");
            sql.append(" order by r.idAuthor, r.idKeyword limit ?, ?");
            while (offset < rowCount) {
                stmt = getConnection().prepareStatement(sql.toString());
                stmt.setInt(1, offset);
                stmt.setInt(2, limit);
                rs = stmt.executeQuery();
                while ((rs != null) && (rs.next())) {
                    idDTO = new IdDTO();
                    idDTO.setId1(rs.getInt("idAuthor"));
                    idDTO.setId2(rs.getInt("idKeyword"));
                    idList.add(idDTO);
                }
                stmt.close();
                offset += limit;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return idList;
    }

    public ArrayList getPublicationDTOListByAuthorIdKeywordId(int idAuthor, int idKeyword) throws Exception {
        ArrayList publicationList = new ArrayList();
        PaperDTO paperDTO = null;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT distinct p.idPaper, rp.citationCount FROM " + PubGuruDB.DBNAME + "." + "author_paper a");
            sql.append(" JOIN " + PubGuruDB.DBNAME + "." + "paper p ON a.idPaper = p.idPaper");
            sql.append(" JOIN " + PubGuruDB.DBNAME + "." + "paper_keyword pk ON p.idPaper = pk.idPaper");
            sql.append(" JOIN " + PubGuruDB.DBNAME + "." + "_rank_paper rp on p.idPaper = rp.idPaper");
            sql.append(" WHERE a.idAuthor = ?");
            sql.append(" AND pk.idKeyword = ?");
            sql.append(" ORDER BY rp.citationCount desc");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idAuthor);
            stmt.setInt(2, idKeyword);
            ResultSet rs = stmt.executeQuery();
            while ((rs != null) && (rs.next())) {
                paperDTO = new PaperDTO();
                paperDTO.setCitationCount(rs.getInt("citationCount"));
                publicationList.add(paperDTO);
            }
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return publicationList;
    }

    public int saveIndexesAuthorKeyword(int idAuthor, int idKeyword, int h_index, int g_index) throws Exception {
        int result = -1;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" insert into " + PubGuruDB.DBNAME + "." + "_rank_author_keyword(idAuthor, idKeyword, h_index, g_index)");
            sql.append(" value (?, ?, ?, ?)");
            sql.append(" on duplicate key update");
            sql.append(" h_index = ?, g_index = ?");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idAuthor);
            stmt.setInt(2, idKeyword);
            stmt.setInt(3, h_index);
            stmt.setInt(4, g_index);
            stmt.setInt(5, h_index);
            stmt.setInt(6, g_index);
            result = stmt.executeUpdate();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return result;
    }

    public ArrayList getOrgIdList() throws Exception {
        ArrayList orgIdList = new ArrayList();
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT r.idOrg FROM " + PubGuruDB.DBNAME + "." + "_rank_org r");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            ResultSet rs = stmt.executeQuery();
            while ((rs != null) && (rs.next())) {
                orgIdList.add(rs.getInt("idOrg"));
            }
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return orgIdList;
    }

    public ArrayList getPublicationDTOListByOrgId(int idOrg) throws Exception {
        ArrayList paperDTOList = new ArrayList();
        PaperDTO paperDTO = null;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT distinct p.idPaper, rp.citationCount FROM " + PubGuruDB.DBNAME + "." + "author a");
            sql.append(" join " + PubGuruDB.DBNAME + "." + "author_paper ap on a.idAuthor = ap.idAuthor");
            sql.append(" join " + PubGuruDB.DBNAME + "." + "paper p on ap.idPaper = p.idPaper");
            sql.append(" JOIN " + PubGuruDB.DBNAME + "." + "_rank_paper rp on p.idPaper = rp.idPaper");
            sql.append(" where a.idOrg = ?");
            sql.append(" ORDER BY rp.citationCount desc");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idOrg);
            ResultSet rs = stmt.executeQuery();
            while ((rs != null) && (rs.next())) {
                paperDTO = new PaperDTO();
                paperDTO.setCitationCount(rs.getInt("citationCount"));
                paperDTOList.add(paperDTO);
            }
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return paperDTOList;
    }

    public int saveIndexesOrg(int idOrg, int h_index, int g_index) throws Exception {
        int result = -1;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" insert into " + PubGuruDB.DBNAME + "." + "_rank_org(idOrg, h_index, g_index)");
            sql.append(" value (?, ?, ?)");
            sql.append(" on duplicate key update");
            sql.append(" h_index = ?, g_index = ?");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idOrg);
            stmt.setInt(2, h_index);
            stmt.setInt(3, g_index);
            stmt.setInt(4, h_index);
            stmt.setInt(5, g_index);
            result = stmt.executeUpdate();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return result;
    }

    public ArrayList<IdDTO> getOrgSubdomainIdList(int limit) throws Exception {
        ArrayList idList = new ArrayList();
        IdDTO idDTO = null;
        int offset = 0;
        int rowCount = 0;
        StringBuffer sql = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            sql = new StringBuffer();
            sql.append(" SELECT count(*) FROM " + PubGuruDB.DBNAME + "." + "_rank_org_subdomain r");
            stmt = getConnection().prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            if ((rs != null) && (rs.next())) {
                rowCount = rs.getInt(1);
            }
            stmt.close();
            sql = new StringBuffer();
            sql.append(" SELECT r.idOrg, r.idSubdomain FROM " + PubGuruDB.DBNAME + "." + "_rank_org_subdomain r");
            sql.append(" order by r.idOrg, r.idSubdomain limit ?, ?");
            while (offset < rowCount) {
                stmt = getConnection().prepareStatement(sql.toString());
                stmt.setInt(1, offset);
                stmt.setInt(2, limit);
                rs = stmt.executeQuery();
                while ((rs != null) && (rs.next())) {
                    idDTO = new IdDTO();
                    idDTO.setId1(rs.getInt("idOrg"));
                    idDTO.setId2(rs.getInt("idSubdomain"));
                    idList.add(idDTO);
                }
                stmt.close();
                offset += limit;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return idList;
    }

    public ArrayList getPublicationDTOListByOrgIdSubdomainId(int idOrg, int idSubdomain) throws Exception {
        ArrayList paperDTOList = new ArrayList();
        PaperDTO paperDTO = null;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT distinct p.idPaper, rp.citationCount FROM " + PubGuruDB.DBNAME + "." + "author a");
            sql.append(" join " + PubGuruDB.DBNAME + "." + "author_paper ap on a.idAuthor = ap.idAuthor");
            sql.append(" join " + PubGuruDB.DBNAME + "." + "paper p on ap.idPaper = p.idPaper");
            sql.append(" join " + PubGuruDB.DBNAME + "." + "subdomain_paper sp on p.idPaper = sp.idPaper");
            sql.append(" JOIN " + PubGuruDB.DBNAME + "." + "_rank_paper rp on p.idPaper = rp.idPaper");
            sql.append(" where a.idOrg = ?");
            sql.append(" and sp.idSubdomain = ?");
            sql.append(" ORDER BY rp.citationCount desc");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idOrg);
            stmt.setInt(2, idSubdomain);
            ResultSet rs = stmt.executeQuery();
            while ((rs != null) && (rs.next())) {
                paperDTO = new PaperDTO();
                paperDTO.setCitationCount(rs.getInt("citationCount"));
                paperDTOList.add(paperDTO);
            }
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return paperDTOList;
    }

    public int saveIndexesOrgSubdomain(int idOrg, int idSubdomain, int h_index, int g_index) throws Exception {
        int result = -1;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" insert into " + PubGuruDB.DBNAME + "." + "_rank_org_subdomain(idOrg, idSubdomain, h_index, g_index)");
            sql.append(" value (?, ?, ?, ?)");
            sql.append(" on duplicate key update");
            sql.append(" h_index = ?, g_index = ?");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idOrg);
            stmt.setInt(2, idSubdomain);
            stmt.setInt(3, h_index);
            stmt.setInt(4, g_index);
            stmt.setInt(5, h_index);
            stmt.setInt(6, g_index);
            result = stmt.executeUpdate();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return result;
    }

    public ArrayList<IdDTO> getOrgKeywordIdList(int limit) throws Exception {
        ArrayList idList = new ArrayList();
        IdDTO idDTO = null;
        int offset = 0;
        int rowCount = 0;
        StringBuffer sql = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            sql = new StringBuffer();
            sql.append(" SELECT count(*) FROM " + PubGuruDB.DBNAME + "." + "_rank_org_keyword r");
            stmt = getConnection().prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            if ((rs != null) && (rs.next())) {
                rowCount = rs.getInt(1);
            }
            stmt.close();
            sql = new StringBuffer();
            sql.append(" SELECT r.idOrg, r.idKeyword FROM " + PubGuruDB.DBNAME + "." + "_rank_org_keyword r");
            sql.append(" order by r.idOrg, r.idKeyword limit ?, ?");
            while (offset < rowCount) {
                stmt = getConnection().prepareStatement(sql.toString());
                stmt.setInt(1, offset);
                stmt.setInt(2, limit);
                rs = stmt.executeQuery();
                while ((rs != null) && (rs.next())) {
                    idDTO = new IdDTO();
                    idDTO.setId1(rs.getInt("idOrg"));
                    idDTO.setId2(rs.getInt("idKeyword"));
                    idList.add(idDTO);
                }
                stmt.close();
                offset += limit;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return idList;
    }

    public ArrayList getPublicationDTOListByOrgIdKeywordId(int idOrg, int idKeyword) throws Exception {
        ArrayList paperDTOList = new ArrayList();
        PaperDTO paperDTO = null;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT distinct p.idPaper, rp.citationCount FROM " + PubGuruDB.DBNAME + "." + "author a");
            sql.append(" join " + PubGuruDB.DBNAME + "." + "author_paper ap on a.idAuthor = ap.idAuthor");
            sql.append(" join " + PubGuruDB.DBNAME + "." + "paper p on ap.idPaper = p.idPaper");
            sql.append(" join " + PubGuruDB.DBNAME + "." + "paper_keyword pk on p.idPaper = pk.idPaper");
            sql.append(" JOIN " + PubGuruDB.DBNAME + "." + "_rank_paper rp on p.idPaper = rp.idPaper");
            sql.append(" where a.idOrg = ?");
            sql.append(" and pk.idKeyword = ?");
            sql.append(" ORDER BY rp.citationCount desc");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idOrg);
            stmt.setInt(2, idKeyword);
            ResultSet rs = stmt.executeQuery();
            while ((rs != null) && (rs.next())) {
                paperDTO = new PaperDTO();
                paperDTO.setCitationCount(rs.getInt("citationCount"));
                paperDTOList.add(paperDTO);
            }
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return paperDTOList;
    }

    public int saveIndexesOrgKeyword(int idOrg, int idKeyword, int h_index, int g_index) throws Exception {
        int result = -1;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" insert into " + PubGuruDB.DBNAME + "." + "_rank_org_keyword(idOrg, idKeyword, h_index, g_index)");
            sql.append(" value (?, ?, ?, ?)");
            sql.append(" on duplicate key update");
            sql.append(" h_index = ?, g_index = ?");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idOrg);
            stmt.setInt(2, idKeyword);
            stmt.setInt(3, h_index);
            stmt.setInt(4, g_index);
            stmt.setInt(5, h_index);
            stmt.setInt(6, g_index);
            result = stmt.executeUpdate();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return result;
    }

    public ArrayList getJournalIdList() throws Exception {
        ArrayList journalIdList = new ArrayList();
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT r.idJournal FROM " + PubGuruDB.DBNAME + "." + "_rank_journal r");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            ResultSet rs = stmt.executeQuery();
            while ((rs != null) && (rs.next())) {
                journalIdList.add(rs.getInt("idJournal"));
            }
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return journalIdList;
    }

    public ArrayList getPublicationDTOListByJournalId(int idJournal) throws Exception {
        ArrayList paperDTOList = new ArrayList();
        PaperDTO paperDTO = null;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT distinct p.idPaper, rp.citationCount FROM " + PubGuruDB.DBNAME + "." + "paper p");
            sql.append(" JOIN " + PubGuruDB.DBNAME + "." + "_rank_paper rp on p.idPaper = rp.idPaper");
            sql.append(" where p.idJournal = ?");
            sql.append(" ORDER BY rp.citationCount desc");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idJournal);
            ResultSet rs = stmt.executeQuery();
            while ((rs != null) && (rs.next())) {
                paperDTO = new PaperDTO();
                paperDTO.setCitationCount(rs.getInt("citationCount"));
                paperDTOList.add(paperDTO);
            }
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return paperDTOList;
    }

    public int saveIndexesJournal(int idJournal, int g_index) throws Exception {
        int result = -1;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" insert into " + PubGuruDB.DBNAME + "." + "_rank_journal(idJournal, g_index)");
            sql.append(" value (?, ?)");
            sql.append(" on duplicate key update");
            sql.append(" g_index = ?");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idJournal);
            stmt.setInt(2, g_index);
            stmt.setInt(3, g_index);
            result = stmt.executeUpdate();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return result;
    }

    public ArrayList<IdDTO> getJournalSubdomainIdList(int limit) throws Exception {
        ArrayList idList = new ArrayList();
        IdDTO idDTO = null;
        int offset = 0;
        int rowCount = 0;
        StringBuffer sql = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            sql = new StringBuffer();
            sql.append(" SELECT count(*) FROM " + PubGuruDB.DBNAME + "." + "_rank_journal_subdomain r");
            stmt = getConnection().prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            if ((rs != null) && (rs.next())) {
                rowCount = rs.getInt(1);
            }
            stmt.close();
            sql = new StringBuffer();
            sql.append(" SELECT r.idJournal, r.idSubdomain FROM " + PubGuruDB.DBNAME + "." + "_rank_journal_subdomain r");
            sql.append(" order by r.idJournal, r.idSubdomain limit ?, ?");
            while (offset < rowCount) {
                stmt = getConnection().prepareStatement(sql.toString());
                stmt.setInt(1, offset);
                stmt.setInt(2, limit);
                rs = stmt.executeQuery();
                while ((rs != null) && (rs.next())) {
                    idDTO = new IdDTO();
                    idDTO.setId1(rs.getInt("idJournal"));
                    idDTO.setId2(rs.getInt("idSubdomain"));
                    idList.add(idDTO);
                }
                stmt.close();
                offset += limit;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return idList;
    }

    public ArrayList getPublicationDTOListByJournalIdSubdomainId(int idJournal, int idSubdomain) throws Exception {
        ArrayList paperDTOList = new ArrayList();
        PaperDTO paperDTO = null;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT distinct p.idPaper, rp.citationCount FROM " + PubGuruDB.DBNAME + "." + "paper p");
            sql.append(" JOIN " + PubGuruDB.DBNAME + "." + "subdomain_paper sp ON p.idPaper = sp.idPaper");
            sql.append(" JOIN " + PubGuruDB.DBNAME + "." + "_rank_paper rp on p.idPaper = rp.idPaper");
            sql.append(" where p.idJournal = ?");
            sql.append(" and sp.idSubdomain = ?");
            sql.append(" ORDER BY rp.citationCount desc");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idJournal);
            stmt.setInt(2, idSubdomain);
            ResultSet rs = stmt.executeQuery();
            while ((rs != null) && (rs.next())) {
                paperDTO = new PaperDTO();
                paperDTO.setCitationCount(rs.getInt("citationCount"));
                paperDTOList.add(paperDTO);
            }
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return paperDTOList;
    }

    public int saveIndexesJournalSubdomain(int idJournal, int idSubdomain, int g_index) throws Exception {
        int result = -1;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" insert into " + PubGuruDB.DBNAME + "." + "_rank_journal_subdomain(idJournal, idSubdomain, g_index)");
            sql.append(" value (?, ?, ?)");
            sql.append(" on duplicate key update");
            sql.append(" g_index = ?");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idJournal);
            stmt.setInt(2, idSubdomain);
            stmt.setInt(3, g_index);
            stmt.setInt(4, g_index);
            result = stmt.executeUpdate();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return result;
    }

    public ArrayList<IdDTO> getJournalKeywordIdList(int limit) throws Exception {
        ArrayList idList = new ArrayList();
        IdDTO idDTO = null;
        int offset = 0;
        int rowCount = 0;
        StringBuffer sql = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            sql = new StringBuffer();
            sql.append(" SELECT count(*) FROM " + PubGuruDB.DBNAME + "." + "_rank_journal_keyword r");
            stmt = getConnection().prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            if ((rs != null) && (rs.next())) {
                rowCount = rs.getInt(1);
            }
            stmt.close();
            sql = new StringBuffer();
            sql.append(" SELECT r.idJournal, r.idKeyword FROM " + PubGuruDB.DBNAME + "." + "_rank_journal_keyword r");
            sql.append(" order by r.idJournal, r.idKeyword limit ?, ?");
            while (offset < rowCount) {
                stmt = getConnection().prepareStatement(sql.toString());
                stmt.setInt(1, offset);
                stmt.setInt(2, limit);
                rs = stmt.executeQuery();
                while ((rs != null) && (rs.next())) {
                    idDTO = new IdDTO();
                    idDTO.setId1(rs.getInt("idJournal"));
                    idDTO.setId2(rs.getInt("idKeyword"));
                    idList.add(idDTO);
                }
                stmt.close();
                offset += limit;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return idList;
    }

    public ArrayList getPublicationDTOListByJournalIdKeywordId(int idJournal, int idKeyword) throws Exception {
        ArrayList paperDTOList = new ArrayList();
        PaperDTO paperDTO = null;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT distinct p.idPaper, rp.citationCount FROM " + PubGuruDB.DBNAME + "." + "paper p");
            sql.append(" JOIN " + PubGuruDB.DBNAME + "." + "paper_keyword pk ON p.idPaper = pk.idPaper");
            sql.append(" JOIN " + PubGuruDB.DBNAME + "." + "_rank_paper rp on p.idPaper = rp.idPaper");
            sql.append(" where p.idJournal = ?");
            sql.append(" and pk.idKeyword = ?");
            sql.append(" ORDER BY rp.citationCount desc");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idJournal);
            stmt.setInt(2, idKeyword);
            ResultSet rs = stmt.executeQuery();
            while ((rs != null) && (rs.next())) {
                paperDTO = new PaperDTO();
                paperDTO.setCitationCount(rs.getInt("citationCount"));
                paperDTOList.add(paperDTO);
            }
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return paperDTOList;
    }

    public int saveIndexesJournalKeyword(int idJournal, int idKeyword, int g_index) throws Exception {
        int result = -1;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" insert into " + PubGuruDB.DBNAME + "." + "_rank_journal_keyword(idJournal, idKeyword, g_index)");
            sql.append(" value (?, ?, ?)");
            sql.append(" on duplicate key update");
            sql.append(" g_index = ?");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idJournal);
            stmt.setInt(2, idKeyword);
            stmt.setInt(3, g_index);
            stmt.setInt(4, g_index);
            result = stmt.executeUpdate();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return result;
    }

    public ArrayList getConferenceIdList() throws Exception {
        ArrayList conferenceIdList = new ArrayList();
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT r.idConference FROM " + PubGuruDB.DBNAME + "." + "_rank_conference r");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            ResultSet rs = stmt.executeQuery();
            while ((rs != null) && (rs.next())) {
                conferenceIdList.add(rs.getInt("idConference"));
            }
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return conferenceIdList;
    }

    public ArrayList getPublicationDTOListByConferenceId(int idConference) throws Exception {
        ArrayList paperDTOList = new ArrayList();
        PaperDTO paperDTO = null;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT distinct p.idPaper, rp.citationCount FROM " + PubGuruDB.DBNAME + "." + "paper p");
            sql.append(" JOIN " + PubGuruDB.DBNAME + "." + "_rank_paper rp on p.idPaper = rp.idPaper");
            sql.append(" where p.idConference = ?");
            sql.append(" ORDER BY rp.citationCount desc");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idConference);
            ResultSet rs = stmt.executeQuery();
            while ((rs != null) && (rs.next())) {
                paperDTO = new PaperDTO();
                paperDTO.setCitationCount(rs.getInt("citationCount"));
                paperDTOList.add(paperDTO);
            }
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return paperDTOList;
    }

    public int saveIndexesConference(int idConference, int g_index) throws Exception {
        int result = -1;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" insert into " + PubGuruDB.DBNAME + "." + "_rank_conference(idConference, g_index)");
            sql.append(" value (?, ?)");
            sql.append(" on duplicate key update");
            sql.append(" g_index = ?");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idConference);
            stmt.setInt(2, g_index);
            stmt.setInt(3, g_index);
            result = stmt.executeUpdate();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return result;
    }

    public ArrayList<IdDTO> getConferenceSubdomainIdList(int limit) throws Exception {
        ArrayList idList = new ArrayList();
        IdDTO idDTO = null;
        int offset = 0;
        int rowCount = 0;
        StringBuffer sql = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            sql = new StringBuffer();
            sql.append(" SELECT count(*) FROM " + PubGuruDB.DBNAME + "." + "_rank_conference_subdomain r");
            stmt = getConnection().prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            if ((rs != null) && (rs.next())) {
                rowCount = rs.getInt(1);
            }
            stmt.close();
            sql = new StringBuffer();
            sql.append(" SELECT r.idConference, r.idSubdomain FROM " + PubGuruDB.DBNAME + "." + "_rank_conference_subdomain r");
            sql.append(" order by r.idConference, r.idSubdomain limit ?, ?");
            while (offset < rowCount) {
                stmt = getConnection().prepareStatement(sql.toString());
                stmt.setInt(1, offset);
                stmt.setInt(2, limit);
                rs = stmt.executeQuery();
                while ((rs != null) && (rs.next())) {
                    idDTO = new IdDTO();
                    idDTO.setId1(rs.getInt("idConference"));
                    idDTO.setId2(rs.getInt("idSubdomain"));
                    idList.add(idDTO);
                }
                stmt.close();
                offset += limit;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return idList;
    }

    public ArrayList getPublicationDTOListByConferenceIdSubdomainId(int idConference, int idSubdomain) throws Exception {
        ArrayList paperDTOList = new ArrayList();
        PaperDTO paperDTO = null;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT distinct p.idPaper, rp.citationCount FROM " + PubGuruDB.DBNAME + "." + "paper p");
            sql.append(" JOIN " + PubGuruDB.DBNAME + "." + "subdomain_paper sp ON p.idPaper = sp.idPaper");
            sql.append(" JOIN " + PubGuruDB.DBNAME + "." + "_rank_paper rp on p.idPaper = rp.idPaper");
            sql.append(" where p.idConference = ?");
            sql.append(" and sp.idSubdomain = ?");
            sql.append(" ORDER BY rp.citationCount desc");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idConference);
            stmt.setInt(2, idSubdomain);
            ResultSet rs = stmt.executeQuery();
            while ((rs != null) && (rs.next())) {
                paperDTO = new PaperDTO();
                paperDTO.setCitationCount(rs.getInt("citationCount"));
                paperDTOList.add(paperDTO);
            }
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return paperDTOList;
    }

    public int saveIndexesConferenceSubdomain(int idConference, int idSubdomain, int g_index) throws Exception {
        int result = -1;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" insert into " + PubGuruDB.DBNAME + "." + "_rank_conference_subdomain(idConference, idSubdomain, g_index)");
            sql.append(" value (?, ?, ?)");
            sql.append(" on duplicate key update");
            sql.append(" g_index = ?");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idConference);
            stmt.setInt(2, idSubdomain);
            stmt.setInt(3, g_index);
            stmt.setInt(4, g_index);
            result = stmt.executeUpdate();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return result;
    }

    public ArrayList<IdDTO> getConferenceKeywordIdList(int limit) throws Exception {
        ArrayList idList = new ArrayList();
        IdDTO idDTO = null;
        int offset = 0;
        int rowCount = 0;
        StringBuffer sql = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            sql = new StringBuffer();
            sql.append(" SELECT count(*) FROM " + PubGuruDB.DBNAME + "." + "_rank_conference_keyword r");
            stmt = getConnection().prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            if ((rs != null) && (rs.next())) {
                rowCount = rs.getInt(1);
            }
            stmt.close();
            sql = new StringBuffer();
            sql.append(" SELECT r.idConference, r.idKeyword FROM " + PubGuruDB.DBNAME + "." + "_rank_conference_keyword r");
            sql.append(" order by r.idConference, r.idKeyword limit ?, ?");
            while (offset < rowCount) {
                stmt = getConnection().prepareStatement(sql.toString());
                stmt.setInt(1, offset);
                stmt.setInt(2, limit);
                rs = stmt.executeQuery();
                while ((rs != null) && (rs.next())) {
                    idDTO = new IdDTO();
                    idDTO.setId1(rs.getInt("idConference"));
                    idDTO.setId2(rs.getInt("idKeyword"));
                    idList.add(idDTO);
                }
                stmt.close();
                offset += limit;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return idList;
    }

    public ArrayList getPublicationDTOListByConferenceIdKeywordId(int idConference, int idKeyword) throws Exception {
        ArrayList paperDTOList = new ArrayList();
        PaperDTO paperDTO = null;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT distinct p.idPaper, rp.citationCount FROM " + PubGuruDB.DBNAME + "." + "paper p");
            sql.append(" JOIN " + PubGuruDB.DBNAME + "." + "paper_keyword pk ON p.idPaper = pk.idPaper");
            sql.append(" JOIN " + PubGuruDB.DBNAME + "." + "_rank_paper rp on p.idPaper = rp.idPaper");
            sql.append(" where p.idConference = ?");
            sql.append(" and pk.idKeyword = ?");
            sql.append(" ORDER BY rp.citationCount desc");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idConference);
            stmt.setInt(2, idKeyword);
            ResultSet rs = stmt.executeQuery();
            while ((rs != null) && (rs.next())) {
                paperDTO = new PaperDTO();
                paperDTO.setCitationCount(rs.getInt("citationCount"));
                paperDTOList.add(paperDTO);
            }
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return paperDTOList;
    }

    public int saveIndexesConferenceKeyword(int idConference, int idKeyword, int g_index) throws Exception {
        int result = -1;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" insert into " + PubGuruDB.DBNAME + "." + "_rank_conference_keyword(idConference, idKeyword, g_index)");
            sql.append(" value (?, ?, ?)");
            sql.append(" on duplicate key update");
            sql.append(" g_index = ?");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.setInt(1, idConference);
            stmt.setInt(2, idKeyword);
            stmt.setInt(3, g_index);
            stmt.setInt(4, g_index);
            result = stmt.executeUpdate();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PubGuruLogger.logger.severe("EXCEPTION: " + ex.toString());
            Object[] arrObj = ex.getStackTrace();
            if (arrObj != null) {
                for (Object stackTraceElement : arrObj) {
                    PubGuruLogger.logger.severe("\tat " + stackTraceElement.toString());
                }
            }
            throw ex;
        }
        return result;
    }
}
