package net.sourceforge.xsurvey.xscreator.dao.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import net.sourceforge.xsurvey.xscreator.dao.XsltSkinDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

public class JdbcTemplateXsltSkinDaoTest extends AbstractTransactionalDataSourceSpringContextTests {

    private XsltSkinDao xsltSkinDao;

    public void testLoad() {
        @SuppressWarnings("unused") List<Long> ids = getJdbcTemplate().query("SELECT xsltskin_id FROM xsltskin", new RowMapper() {

            @Override
            public Object mapRow(ResultSet rs, int numRow) throws SQLException {
                return rs.getLong("xsltskin_id");
            }
        });
        assertTrue(ids.size() > 0);
        String xml = xsltSkinDao.load(ids.get(ids.size() - 1));
        assertNotNull(xml);
    }

    public void testSave() {
        InputStream is = JdbcTemplateSurveyDaoTest.class.getResourceAsStream("skin1.xsl");
        assertNotNull(is);
        xsltSkinDao.save("skin1.xsl", is);
        @SuppressWarnings("unused") List<Long> ids = getJdbcTemplate().query("SELECT xsltskin_id FROM xsltskin", new RowMapper() {

            @Override
            public Object mapRow(ResultSet rs, int numRow) throws SQLException {
                return rs.getLong("xsltskin_id");
            }
        });
        assertTrue(ids.size() > 0);
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[] { "file:src/test/resources/testContext.xml", "file:src/main/webapp/WEB-INF/daoContext.xml", "file:src/main/webapp/WEB-INF/applicationContext.xml" };
    }

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        super.executeSqlScript("file:src/test/resources/cleardb.sql", true);
        super.executeSqlScript("file:src/test/resources/sampledata.sql", true);
    }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public void setXsltSkinDao(XsltSkinDao xsltSkinDao) {
        this.xsltSkinDao = xsltSkinDao;
    }
}
