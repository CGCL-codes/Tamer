package com.bugfree4j.per.common.db.filestore.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import org.apache.struts.upload.FormFile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.FileCopyUtils;
import com.bugfree4j.app.WebApp;
import com.bugfree4j.commons.Sequence;
import com.bugfree4j.dao.BugfileDAO;
import com.bugfree4j.dao.DAOConstants;
import com.bugfree4j.domain.Bugfile;
import com.bugfree4j.per.common.db.filestore.DbFile;
import com.bugfree4j.struts.form.BuginfoFormBean;
import com.bugfree4j.tools.DateUtil;

public class DbFileImpl implements DbFile {

    private DataSource ds = null;

    public DbFileImpl(DataSource dataSource) {
        this.ds = dataSource;
    }

    /**
	 * @param args
	 * @throws IOException
	 */
    public static void main(String[] args) throws IOException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/bugfreej");
        dataSource.setUsername("root");
        dataSource.setPassword("ASGC007");
        DbFile dFile = new DbFileImpl(dataSource);
        String filename2 = "E:\\ǡ��������� - ����.mp3";
        OutputStream out;
        try {
            out = new BufferedOutputStream(new FileOutputStream(filename2));
            FileCopyUtils.copy(dFile.query("/20060417/ǡ��������� - ����.mp3"), out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int save(final String filename, final InputStream filecontent) {
        int ret = 0;
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        if (!this.isExist(filename)) {
            ret = jdbcTemplate.update("INSERT INTO sys_files_storage(filename,content) values(?,?)", new PreparedStatementSetter() {

                public void setValues(PreparedStatement ps) throws SQLException {
                    try {
                        BufferedInputStream in = new BufferedInputStream(filecontent);
                        ps.setString(1, filename);
                        ps.setBinaryStream(2, in, in.available());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            return -1;
        }
        return ret;
    }

    public InputStream query(String filename) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        Object ret = jdbcTemplate.query("SELECT content FROM sys_files_storage WHERE filename like ?", new Object[] { filename }, new ResultSetExtractor() {

            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                rs.next();
                return rs.getBinaryStream("content");
            }
        });
        if (ret != null) return ((InputStream) ret); else return null;
    }

    public int delete(final String filename) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        int ret = jdbcTemplate.update("DELETE FROM sys_files_storage WHERE filename like ?", new PreparedStatementSetter() {

            public void setValues(PreparedStatement ps) throws SQLException {
                try {
                    ps.setString(1, filename);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        return ret;
    }

    public boolean isExist(String filename) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        int ret = jdbcTemplate.queryForInt("SELECT count(*) FROM sys_files_storage WHERE filename like ?", new Object[] { filename });
        if (ret > 0) return true; else return false;
    }

    /**
	 * �ϴ��ļ�������
	 * 
	 * @param fm
	 * @param request
	 * @return
	 * @throws Exception
	 */
    public static boolean AddFile(BuginfoFormBean fm, HttpServletRequest request, String currentUserName) throws Exception {
        boolean ret = false;
        if (fm.getBugfilename1().getFileName().length() == 0 && fm.getBugfilename2().getFileName().length() == 0) {
            return ret;
        }
        if (fm.getBugfilename1().getFileName().length() > 0) uploadOneFile(fm.getBugfilename1(), fm.getBugfiletitle1(), Long.parseLong(fm.getBugid()), request, currentUserName);
        if (fm.getBugfilename2().getFileName().length() > 0) uploadOneFile(fm.getBugfilename2(), fm.getBugfiletitle2(), Long.parseLong(fm.getBugid()), request, currentUserName);
        return ret;
    }

    private static void uploadOneFile(FormFile formfile, String filetitle, long bugid, HttpServletRequest request, String currentUserName) throws Exception {
        String filename = null;
        String realFileName = new String(formfile.getFileName());
        if (filetitle == null || filetitle.length() <= 0) filetitle = formfile.getFileName();
        realFileName = createFileName(realFileName);
        String dir = DateUtil.formatDate(new Date(), "yyyyMMdd");
        filename = "/" + dir + "/" + realFileName;
        if (filename != null) {
            try {
                BufferedInputStream in;
                try {
                    DbFile dFile = new DbFileImpl(WebApp.getDataSource());
                    in = new BufferedInputStream(formfile.getInputStream());
                    dFile.save(filename, in);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                BugfileDAO dao = (BugfileDAO) WebApp.getBean(DAOConstants.BUGFILE_DAO);
                Bugfile bugfile = new Bugfile();
                bugfile.setBugid(bugid);
                bugfile.setFilename(filename);
                bugfile.setFilesize(Integer.toString(formfile.getFileSize()));
                bugfile.setFiletitle(filetitle);
                bugfile.setFiletype(formfile.getContentType());
                bugfile.setAdduser(currentUserName);
                bugfile.setAdddate(new Date());
                dao.insert(bugfile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean ifFileSizeTooLarge(BuginfoFormBean fm) {
        boolean ret = false;
        if (fm.getBugfilename1().getFileName().length() == 0 && fm.getBugfilename2().getFileName().length() == 0) {
            return ret;
        }
        if (fm.getBugfilename1().getFileName().length() > 0) if (fm.getBugfilename1().getFileSize() >= 7340032) ret = true;
        if (fm.getBugfilename2().getFileName().length() > 0) if (fm.getBugfilename2().getFileSize() >= 7340032) ret = true;
        return ret;
    }

    /**
	 * �ж�ָ����·��(path)���Ƿ����ָ�����ļ�(filename)��������򷵻�filename
	 * �����ڣ���������һ�����кżӵ�filename�󷵻�
	 * 
	 * @param path
	 * @param filename
	 * @return ��path��Ψһ���ļ���
	 * @throws SQLException
	 */
    private static String createFileName(String filename) throws SQLException {
        String retfilename = null;
        String realname;
        DbFile dFile = new DbFileImpl(WebApp.getDataSource());
        try {
            if (dFile.isExist(filename)) {
                realname = filename + (int) (Math.random() * 1000);
                retfilename = createFileName(realname);
            } else {
                return filename;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retfilename;
    }
}
