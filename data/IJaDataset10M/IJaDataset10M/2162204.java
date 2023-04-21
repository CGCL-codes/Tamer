package org.jvc.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import simple.http.Request;
import simple.http.Response;

/**
 * @author Brett Wooldridge
 *
 */
public class CheckoutAction extends BaseAction {

    private static final int BUFFER_SIZE = 16384;

    private static final String QUERY_LATEST_VERSION = "SELECT files.version, files.length, files.data " + "      FROM files, file_label_mapping filemap " + "      WHERE files.file_name = ? AND files.parent_dir_id = ? AND" + "            filemap.file_id = files.file_id AND filemap.label_id = ? AND" + "            files.version = (SELECT MAX(files2.version) " + "                               FROM files files2 " + "                               WHERE files.file_id = files2.file_id)";

    private static final String QUERY_SPECIFIC_VERSION = "SELECT files.version, files.length, files.data " + "      FROM files, file_label_mapping filemap " + "      WHERE files.file_name = ? AND files.parent_dir_id = ? AND" + "            filemap.file_id = files.file_id AND filemap.label_id = ? AND" + "            files.version = ?";

    private Logger logger = Logger.getLogger(CheckoutAction.class);

    /**
    * Called by the JvcService to process a checkOut action.
    *
    * Use the following pattern to allow streaming of large result sets from MySQL:
    *
    *    stmt = conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
    *    stmt.setFetchSize(Integer.MIN_VALUE);
    *
    * note that it locks the tables involved until the data is done streaming.
    *
    * @param req the Requst object
    * @param resp the Response object
    * @param session the JvcSession associated with the request
    * @throws Exception thrown when any unrecoverable error is encountered
    */
    public void processAction(Request req, Response resp, JvcSession session) throws Exception {
        if ("GET".equals(req.getMethod())) {
            String file = req.getParameter(FILE);
            if (file != null && !"".equals(file.trim())) {
                PreparedStatement stmt = null;
                ResultSet results = null;
                try {
                    int unid = session.getSessionUNID();
                    int labelId = findLabelID(session, req.getParameter(LABEL));
                    int dirId = findDirectoryID(session, req.getParameter(DIRECTORY), labelId);
                    Connection con = session.getConnection();
                    String ver = req.getParameter(VERSION);
                    String query = (ver == null ? QUERY_LATEST_VERSION : QUERY_SPECIFIC_VERSION);
                    stmt = con.prepareStatement(query);
                    stmt.setString(1, file);
                    stmt.setInt(2, dirId);
                    stmt.setInt(3, labelId);
                    if (ver != null) {
                        stmt.setInt(4, Integer.parseInt(ver));
                    }
                    results = stmt.executeQuery();
                    if (results.next()) {
                        int version = results.getInt(1);
                        int length = results.getInt(2);
                        resp.setContentLength(length);
                        OutputStream writer = new BufferedOutputStream(resp.getOutputStream(), BUFFER_SIZE);
                        InputStream reader = new BufferedInputStream(results.getBinaryStream(3));
                        byte[] buff = new byte[BUFFER_SIZE];
                        while (true) {
                            int rc = reader.read(buff);
                            if (rc > 0) {
                                writer.write(buff, 0, rc);
                            } else if (rc < 0) {
                                writer.flush();
                                break;
                            }
                        }
                        reader.close();
                    }
                } catch (SQLException e) {
                    resp.setCode(SQL_ERROR);
                    resp.setText(jvcService.parseSqlException(e));
                    logger.error("Unable to return files list.", e);
                } finally {
                    cleanClose(null, stmt, results);
                }
            }
        }
    }

    /**
    * @see org.jvc.server.IRepositoryAction#preCommit(simple.http.Request, simple.http.Response, org.jvc.server.JvcSession)
    */
    public void preCommit(Request req, Response resp, JvcSession session) throws Exception {
    }

    /**
    * @see org.jvc.server.IRepositoryAction#postCommit(simple.http.Request, simple.http.Response, org.jvc.server.JvcSession)
    */
    public void postCommit(Request req, Response resp, JvcSession session) throws Exception {
    }
}
