package net.mikaboshi.file_upd_chk;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.mikaboshi.file_upd_chk.dao.DaoIF;
import net.mikaboshi.file_upd_chk.dao.DatabaseManager;
import net.mikaboshi.io.FileIterable;
import net.mikaboshi.jdbc.ResultSetHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileChecker {

    private static final Log logger = LogFactory.getLog(FileChecker.class);

    public FileChecker() {
    }

    public void checkNewFiles(FileCheckConfig config, boolean first) {
        if (logger.isTraceEnabled()) {
            logger.trace("checkNewFiles config = " + config);
        }
        DaoIF dao = null;
        try {
            dao = DatabaseManager.getInstance().getDao();
            FileIterable iter = new FileIterable(config.getDirectory(), config.createFilter(), config.isRecursive());
            dao.begin();
            for (File file : iter) {
                FileProperty prop = null;
                try {
                    prop = new FileProperty(file);
                } catch (FileNotFoundException e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(e.getMessage(), e);
                    }
                    continue;
                }
                FileProperty oldProp = dao.selectFileProperty(prop.getPath());
                if (oldProp == null) {
                    dao.insertFileProperty(prop);
                    if (!first) {
                        FileChangingLog changing = new FileChangingLog();
                        changing.setChangingType(FileChangingType.NEW);
                        changing.setDetected(System.currentTimeMillis());
                        changing.setLastModified(prop.getLastModified());
                        changing.setPath(prop.getPath());
                        changing.setSize(prop.getSize());
                        if (logger.isTraceEnabled()) {
                            logger.trace("new file detected : " + changing);
                        }
                        dao.insertFileChangingLog(changing);
                    }
                }
            }
            dao.commit();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (dao != null) {
                dao.rollback();
            }
        } finally {
            if (dao != null) {
                dao.release();
            }
        }
    }

    public void checkPeriodically() {
        if (logger.isTraceEnabled()) {
            logger.trace("checkPeriodically start");
        }
        DaoIF dao = null;
        List<FileCheckConfig> configList = null;
        try {
            dao = DatabaseManager.getInstance().getDao();
            configList = dao.selectAllConfig();
            dao.begin();
            FileUpdateCheckHandler handler = new FileUpdateCheckHandler(dao);
            dao.selectAllFileProperties(handler);
            for (Long id : handler.deletedPropertyIds) {
                dao.deleteFileProperty(id);
            }
            dao.commit();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (dao != null) {
                dao.rollback();
            }
        } finally {
            if (dao != null) {
                dao.release();
            }
        }
        if (configList != null) {
            for (FileCheckConfig config : configList) {
                checkNewFiles(config, false);
            }
        }
    }

    static class FileUpdateCheckHandler implements ResultSetHandler {

        private DaoIF dao;

        private List<Long> deletedPropertyIds = new ArrayList<Long>();

        FileUpdateCheckHandler(DaoIF dao) {
            this.dao = dao;
        }

        public void after() throws SQLException {
        }

        public void before(ResultSetMetaData resultsetmetadata) throws SQLException {
        }

        public void close() throws SQLException {
        }

        public void handle(ResultSet rs) throws SQLException {
            FileProperty prop = new FileProperty(rs.getLong("id"), rs.getString("path"), rs.getLong("last_modified"), rs.getLong("size"));
            File file = new File(prop.getPath());
            FileChangingType changingType = null;
            if (!file.exists()) {
                changingType = FileChangingType.DELETED;
                this.deletedPropertyIds.add(prop.getId());
            } else if (prop.getLastModified() == file.lastModified() && prop.getSize() == file.length()) {
                return;
            } else {
                changingType = FileChangingType.UPDATED;
            }
            FileChangingLog changing = new FileChangingLog();
            changing.setPath(prop.getPath());
            changing.setDetected(System.currentTimeMillis());
            changing.setLastModified(file.lastModified());
            changing.setSize(file.length());
            changing.setChangingType(changingType);
            if (logger.isTraceEnabled()) {
                logger.trace("changed or deleted file detected : " + changing);
            }
            this.dao.insertFileChangingLog(changing);
        }
    }
}
