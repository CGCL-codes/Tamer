package com.nullfish.lib.vfs.tag_db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.nullfish.lib.vfs.ConnectionCache;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;
import com.nullfish.lib.vfs.exception.VFSSqlException;
import com.nullfish.lib.vfs.tag_db.command.FileRemovedCommand;

public class TagDataBase {

    private UpdaterThread thread = new UpdaterThread();

    private String dir;

    private List commandQueue = new ArrayList();

    /**
	 * テーブル作成SQL
	 */
    private static final String CREATE_FILE_TAG_TABLE_QUERY = "CREATE CACHED TABLE IF NOT EXISTS file_tag (" + "file VARCHAR(1024) NOT NULL," + "directory VARCHAR(1024)," + "tag VARCHAR(128) NOT NULL" + ")";

    private static final String CREATE_INDEX_FILE_QUERY = "CREATE INDEX IF NOT EXISTS idx_file ON file_tag(file)";

    private static final String CREATE_INDEX_DIRECTORY_QUERY = "CREATE INDEX IF NOT EXISTS idx_directory ON file_tag(directory)";

    private static final String CREATE_INDEX_TAG_QUERY = "CREATE INDEX IF NOT EXISTS idx_tag ON file_tag(tag)";

    /**
	 * ファイル名検索クエリ
	 */
    private PreparedStatement selectByFileStatement;

    private static final String SELECT_BY_FILE_QUERY = "SELECT file, directory, tag FROM file_tag " + "WHERE file = ? ORDER BY tag";

    /**
	 * ファイル名検索クエリ
	 */
    private PreparedStatement selectByDirectoryStatement;

    private static final String SELECT_BY_DIRECTORY_QUERY = "SELECT file, directory, tag FROM file_tag " + "WHERE directory = ? ORDER BY tag";

    /**
	 * タグ検索クエリ
	 */
    private PreparedStatement selectByTagStatement;

    private static final String SELECT_BY_TAG_QUERY = "SELECT file FROM file_tag " + "WHERE tag like ? GROUP BY file ORDER BY file";

    /**
	 * タグ一覧クエリ
	 */
    private PreparedStatement selectTagStatement;

    private static final String SELECT_TAG_QUERY = "SELECT tag FROM file_tag " + "GROUP BY tag ORDER BY tag";

    /**
	 * タグ使用有無設定
	 */
    public static final String CONFIG_USE_TAG = "use_tag";

    /**
	 * タグDBディレクトリ設定
	 */
    public static final String CONFIG_FILEFISH_DB_DIR = "filefish_db_dir";

    private static final String SELECT_FILE_QUERY = "SELECT file FROM file_tag GROUP BY file";

    public TagDataBase(String dir) throws VFSSqlException {
        try {
            this.dir = dir;
            Connection conn = ConnectionCache.getConnection(dir);
            initTable(conn);
            initStatement(conn);
            thread.start();
        } catch (Exception e) {
            throw new VFSSqlException(e);
        }
    }

    private void initTable(Connection conn) throws SQLException {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.execute(CREATE_FILE_TAG_TABLE_QUERY);
            stmt.execute(CREATE_INDEX_FILE_QUERY);
            stmt.execute(CREATE_INDEX_DIRECTORY_QUERY);
            stmt.execute(CREATE_INDEX_TAG_QUERY);
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
            }
        }
    }

    private void initStatement(Connection conn) throws SQLException {
        selectByFileStatement = conn.prepareStatement(SELECT_BY_FILE_QUERY);
        selectByTagStatement = conn.prepareStatement(SELECT_BY_TAG_QUERY);
        selectByDirectoryStatement = conn.prepareStatement(SELECT_BY_DIRECTORY_QUERY);
        selectTagStatement = conn.prepareStatement(SELECT_TAG_QUERY);
    }

    public synchronized void close() {
        try {
            selectByFileStatement.close();
        } catch (Exception e) {
        }
        try {
            selectByTagStatement.close();
        } catch (Exception e) {
        }
        try {
            selectByDirectoryStatement.close();
        } catch (Exception e) {
        }
        try {
            selectTagStatement.close();
        } catch (Exception e) {
        }
        thread.stopThread();
    }

    public void addCommand(TagDataBaseCommand command) {
        commandQueue.add(command);
        synchronized (commandQueue) {
            commandQueue.notify();
        }
    }

    public synchronized List findTags(VFile file) throws SQLException {
        ResultSet rs = null;
        List rtn = new ArrayList();
        try {
            selectByFileStatement.setString(1, file.getSecurePath());
            rs = selectByFileStatement.executeQuery();
            while (rs.next()) {
                rtn.add(rs.getString(3));
            }
            return rtn;
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
    }

    public synchronized List findFile(String tag, VFS vfs) throws SQLException, VFSException {
        ResultSet rs = null;
        List rtn = new ArrayList();
        try {
            selectByTagStatement.setString(1, tag);
            rs = selectByTagStatement.executeQuery();
            while (rs.next()) {
                rtn.add(vfs.getFile(rs.getString(1)));
            }
            return rtn;
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
    }

    public synchronized Map findTagsInDirectory(VFile dir) throws SQLException, VFSException {
        ResultSet rs = null;
        Map rtn = new HashMap();
        try {
            selectByDirectoryStatement.setString(1, dir.getSecurePath());
            rs = selectByDirectoryStatement.executeQuery();
            while (rs.next()) {
                String file = rs.getString(1);
                String tag = rs.getString(3);
                List tagList = (List) rtn.get(file);
                if (tagList == null) {
                    tagList = new ArrayList();
                    rtn.put(file, tagList);
                }
                tagList.add(tag);
            }
            return rtn;
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
    }

    public synchronized List findAllTags() throws VFSException {
        ResultSet rs = null;
        List rtn = new ArrayList();
        try {
            rs = selectTagStatement.executeQuery();
            while (rs.next()) {
                rtn.add(rs.getString(1));
            }
            return rtn;
        } catch (SQLException e) {
            throw new VFSIOException(e);
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
    }

    private class UpdaterThread extends Thread {

        private boolean running = true;

        public void run() {
            while (running) {
                try {
                    if (commandQueue.size() > 0) {
                        TagDataBaseCommand command = (TagDataBaseCommand) commandQueue.remove(0);
                        command.execute(ConnectionCache.getConnection(dir));
                    } else {
                        synchronized (commandQueue) {
                            commandQueue.wait();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void stopThread() {
            running = false;
            synchronized (commandQueue) {
                commandQueue.notify();
            }
        }
    }

    public synchronized ResultSet executeSelect(String sql) throws VFSException {
        Statement stmt = null;
        try {
            stmt = ConnectionCache.getConnection(dir).createStatement();
            return stmt.executeQuery(sql);
        } catch (Exception e) {
            throw new VFSIOException(e);
        }
    }

    public void clean() throws VFSException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Connection conn = ConnectionCache.getConnection(dir);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SELECT_FILE_QUERY);
            while (rs.next()) {
                String fileName = rs.getString(1);
                VFile file = VFS.getInstance().getFile(fileName);
                if (!file.exists()) {
                    new FileRemovedCommand(file).execute(conn);
                }
            }
        } catch (Exception e) {
            throw new VFSIOException(e);
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
            }
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
    }
}
