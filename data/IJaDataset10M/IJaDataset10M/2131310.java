package satmule.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import satmule.domain.Chunk;
import satmule.domain.Efile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author JMartinC
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GetChunk {

    static Log log = LogFactory.getLog(GetChunk.class);

    /** 
	 * gets a transferable chunk from the database
	 * */
    public static Chunk getChunk() throws Exception {
        DBConnectionManager dbm = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        Chunk ret = null;
        String SQL = "SELECT C.FILEHASH,C.STARTOFF,C.LENGTH,C.DATA,C.CHUNKTIME FROM CHUNK C,EDONKEYFILE F WHERE C.FILEHASH=F.FILEHASH AND F.STORE='remoteClient'";
        try {
            dbm = DBConnectionManager.getInstance();
            conn = dbm.getConnection("satmule");
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(SQL);
            stmt.setFetchSize(1);
            ResultSet r = stmt.executeQuery();
            while (r.next()) {
                String hash = r.getString("FILEHASH");
                long startOffset = r.getLong("STARTOFF");
                long length = r.getLong("LENGTH");
                byte data[] = r.getBytes("DATA");
                java.util.Date chunktime = new java.util.Date(r.getDate("CHUNKTIME").getTime());
                ret = new Chunk(hash, data.length, startOffset, data, chunktime);
            }
            r.close();
            stmt.close();
            dbm.freeConnection("satmule", conn);
        } catch (Exception e) {
            log.error("SQL error: " + SQL, e);
            Exception excep;
            if (dbm == null) excep = new Exception("Could not obtain pool object DbConnectionManager"); else if (conn == null) excep = new Exception("The Db connection pool could not obtain a database connection"); else {
                excep = new Exception("SQL Error : " + SQL + " error: " + e);
                dbm.freeConnection("satmule", conn);
            }
            throw excep;
        }
        return ret;
    }

    /**
	 * returns a chunk of data for that file if its store is remoteClient
	 * @param fileHash the hash of the file to get chunks
	 * @return a chunk of that file
	 * @throws Exception
	 */
    public static Chunk getChunk(String fileHash) throws Exception {
        DBConnectionManager dbm = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        Chunk ret = null;
        String SQL = "SELECT C.FILEHASH,C.STARTOFF,C.LENGTH,C.DATA,C.CHUNKTIME FROM CHUNK C,EDONKEYFILE F WHERE C.FILEHASH=? AND C.FILEHASH=F.FILEHASH AND F.STORE='remoteClient'";
        try {
            dbm = DBConnectionManager.getInstance();
            conn = dbm.getConnection("satmule");
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(SQL);
            stmt.setString(1, fileHash);
            stmt.setFetchSize(1);
            ResultSet r = stmt.executeQuery();
            while (r.next()) {
                String hash = r.getString("FILEHASH");
                long startOffset = r.getLong("STARTOFF");
                long length = r.getLong("LENGTH");
                byte data[] = r.getBytes("DATA");
                java.util.Date chunktime = new java.util.Date(r.getDate("CHUNKTIME").getTime());
                ret = new Chunk(hash, data.length, startOffset, data, chunktime);
            }
            r.close();
            stmt.close();
            dbm.freeConnection("satmule", conn);
        } catch (Exception e) {
            log.error("SQL error: " + SQL, e);
            Exception excep;
            if (dbm == null) excep = new Exception("Could not obtain pool object DbConnectionManager"); else if (conn == null) excep = new Exception("The Db connection pool could not obtain a database connection"); else {
                excep = new Exception("SQL Error : " + SQL + " error: " + e);
                dbm.freeConnection("satmule", conn);
            }
            throw excep;
        }
        return ret;
    }

    /**
	 * Gets a pending chunk for a file. Pending chunks are that whose dont have last sent date
	 * registered in database
	 * @param fileHash
	 * @return
	 * @throws Exception
	 */
    public static Chunk getPendingChunk(String fileHash) throws Exception {
        DBConnectionManager dbm = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        Chunk ret = null;
        String SQL = "SELECT C.FILEHASH,C.STARTOFF,C.LENGTH,C.DATA,C.CHUNKTIME FROM CHUNK C,EDONKEYFILE F WHERE C.FILEHASH=? AND C.FILEHASH=F.FILEHASH AND F.STORE='remoteClient' AND SENT IS NULL";
        try {
            dbm = DBConnectionManager.getInstance();
            conn = dbm.getConnection("satmule");
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(SQL);
            stmt.setString(1, fileHash);
            stmt.setFetchSize(1);
            ResultSet r = stmt.executeQuery();
            while (r.next()) {
                String hash = r.getString("FILEHASH");
                long startOffset = r.getLong("STARTOFF");
                long length = r.getLong("LENGTH");
                byte data[] = r.getBytes("DATA");
                java.util.Date chunktime = new java.util.Date(r.getDate("CHUNKTIME").getTime());
                ret = new Chunk(hash, data.length, startOffset, data, chunktime);
            }
            r.close();
            stmt.close();
            dbm.freeConnection("satmule", conn);
        } catch (Exception e) {
            log.error("SQL error: " + SQL, e);
            Exception excep;
            if (dbm == null) excep = new Exception("Could not obtain pool object DbConnectionManager"); else if (conn == null) excep = new Exception("The Db connection pool could not obtain a database connection"); else {
                excep = new Exception("SQL Error : " + SQL + " error: " + e);
                dbm.freeConnection("satmule", conn);
            }
            throw excep;
        }
        return ret;
    }

    /** sets all files of state unknown and reset all sent dates of pending chunks not confirmed�
	 * 
	 * @throws Exception
	 */
    public static void resetTransmisions() throws Exception {
        DBConnectionManager dbm = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmtResetChunks = null;
        String SQL = "UPDATE EDONKEYFILE SET STATE=? WHERE STATE<>?";
        String SQL2 = "UPDATE CHUNK SET SENT=NULL WHERE SENT IS NOT NULL";
        try {
            dbm = DBConnectionManager.getInstance();
            conn = dbm.getConnection("satmule");
            stmt = conn.prepareStatement(SQL);
            stmt.setInt(1, Efile.STATE_UNKNOWN);
            stmt.setInt(2, Efile.STATE_UNKNOWN);
            stmtResetChunks = conn.prepareStatement(SQL2);
            stmt.executeUpdate();
            stmtResetChunks.executeUpdate();
            conn.commit();
            stmt.close();
            stmtResetChunks.close();
            dbm.freeConnection("satmule", conn);
        } catch (Exception e) {
            log.error("SQL error: " + SQL, e);
            Exception excep;
            if (dbm == null) excep = new Exception("Could not obtain pool object DbConnectionManager"); else if (conn == null) excep = new Exception("The Db connection pool could not obtain a database connection"); else {
                conn.rollback();
                excep = new Exception("SQL Error : " + SQL + " error: " + e);
                dbm.freeConnection("satmule", conn);
            }
            throw excep;
        }
    }

    /**
	 * gets a file of store type remoteClient with state given by parameter and numchunks >0
	 * @param type
	 * @return
	 * @throws Exception
	 */
    public static String selectNewTransferableFile(int type) throws Exception {
        DBConnectionManager dbm = null;
        String ret = null;
        String SQL = "SELECT F.FILEHASH AS THEFILEHASH, COUNT(*) NUMCHUNKS FROM EDONKEYFILE F,CHUNK C WHERE F.STORE='remoteClient' AND STATE=? AND C.FILEHASH=F.FILEHASH GROUP BY F.FILEHASH HAVING NUMCHUNKS>0";
        Connection conn = null;
        PreparedStatement stmt = null;
        log.debug("select new transferable file of type " + type);
        try {
            dbm = DBConnectionManager.getInstance();
            conn = dbm.getConnection("satmule");
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(SQL);
            stmt.setInt(1, type);
            stmt.setFetchSize(1);
            ResultSet r = stmt.executeQuery();
            if (r.next()) {
                ret = r.getString("THEFILEHASH");
            }
            r.close();
            stmt.close();
            dbm.freeConnection("satmule", conn);
        } catch (Exception e) {
            log.error("SQL error: " + SQL, e);
            Exception excep;
            if (dbm == null) excep = new Exception("Could not obtain pool object DbConnectionManager"); else if (conn == null) excep = new Exception("The Db connection pool could not obtain a database connection"); else {
                excep = new Exception("SQL Error : " + SQL + " error: " + e);
                dbm.freeConnection("satmule", conn);
            }
            throw excep;
        }
        return ret;
    }

    /**
	 * update last sent date for that chunk
	 * @param c
	 * @return
	 * @throws Exception
	 */
    public static Chunk updateLastSend(Chunk c) throws Exception {
        DBConnectionManager dbm = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        Chunk ret = null;
        String SQL = "UPDATE CHUNK SET SENT=? WHERE FILEHASH=? AND STARTOFF=? AND LENGTH=?";
        log.debug("update chunk last sent for chunk " + c.getHash() + " startoff " + c.getStartOffset());
        try {
            dbm = DBConnectionManager.getInstance();
            conn = dbm.getConnection("satmule");
            stmt = conn.prepareStatement(SQL);
            stmt.setDate(1, new java.sql.Date(c.getLastSend().getTime()));
            stmt.setString(2, c.getHash());
            stmt.setLong(3, c.getStartOffset());
            stmt.setLong(4, c.getSize());
            stmt.executeUpdate();
            conn.commit();
            stmt.close();
            dbm.freeConnection("satmule", conn);
        } catch (Exception e) {
            log.error("Error while updating chunk " + c.getHash() + "offset:" + c.getStartOffset() + "SQL error: " + SQL, e);
            Exception excep;
            if (dbm == null) excep = new Exception("Could not obtain pool object DbConnectionManager"); else if (conn == null) excep = new Exception("The Db connection pool could not obtain a database connection"); else {
                conn.rollback();
                excep = new Exception("SQL Error : " + SQL + " error: " + e);
                dbm.freeConnection("satmule", conn);
            }
            throw excep;
        }
        return ret;
    }

    public static void main(String args[]) {
        try {
            String hash = selectNewTransferableFile(Efile.STATE_UNKNOWN);
            System.out.println("transferable file: " + hash);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
