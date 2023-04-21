package net.sourceforge.xsurvey.xscreator.dao.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.List;
import net.sourceforge.xsurvey.xscreator.dao.SurveyDao;
import net.sourceforge.xsurvey.xscreator.dao.XsltSkinDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * Tests {@link net.sourceforge.xsurvey.xscreator.dao.impl.impl.JdbcTemplateSurveyDao} class
 * @author Mariusz Padykula
 *
 */
public class JdbcTemplateSurveyDaoTest extends AbstractTransactionalDataSourceSpringContextTests {

    private SurveyDao surveyDao;

    private XsltSkinDao xsltSkinDao;

    public void testLoad() throws SQLException, IOException {
        InputStream isx = JdbcTemplateSurveyDaoTest.class.getResourceAsStream("skin1.xsl");
        assertNotNull(isx);
        xsltSkinDao.save("skin1.xsl", isx);
        @SuppressWarnings("unused") List<Long> idsx = getJdbcTemplate().query("SELECT xsltskin_id FROM xsltskin", new RowMapper() {

            @Override
            public Object mapRow(ResultSet rs, int numRow) throws SQLException {
                return rs.getLong("xsltskin_id");
            }
        });
        assertEquals(1, idsx.size());
        InputStream is = JdbcTemplateSurveyDaoTest.class.getResourceAsStream("example-survey1.xml");
        assertNotNull(is);
        surveyDao.save(idsx.get(0), "test survey", new GregorianCalendar(), new GregorianCalendar(), "new", convertStreamToString(is));
        @SuppressWarnings("unused") List<Long> ids = getJdbcTemplate().query("SELECT survey_id FROM survey", new RowMapper() {

            @Override
            public Object mapRow(ResultSet rs, int numRow) throws SQLException {
                return rs.getLong("survey_id");
            }
        });
        String xml = surveyDao.load(ids.get(ids.size() - 1));
        assertNotNull(xml);
        isx.close();
        is.close();
    }

    public void testSave() throws IOException {
        InputStream isx = JdbcTemplateSurveyDaoTest.class.getResourceAsStream("skin1.xsl");
        assertNotNull(isx);
        xsltSkinDao.save("skin1.xsl", isx);
        isx.close();
        @SuppressWarnings("unused") List<Long> idsx = getJdbcTemplate().query("SELECT xsltskin_id FROM xsltskin", new RowMapper() {

            @Override
            public Object mapRow(ResultSet rs, int numRow) throws SQLException {
                return rs.getLong("xsltskin_id");
            }
        });
        assertEquals(1, idsx.size());
        InputStream is = JdbcTemplateSurveyDaoTest.class.getResourceAsStream("example-survey1.xml");
        assertNotNull(is);
        surveyDao.save(idsx.get(0), "test survey", new GregorianCalendar(), new GregorianCalendar(), "new", convertStreamToString(is));
        @SuppressWarnings("unused") List<Long> ids = getJdbcTemplate().query("SELECT survey_id FROM survey", new RowMapper() {

            @Override
            public Object mapRow(ResultSet rs, int numRow) throws SQLException {
                return rs.getLong("survey_id");
            }
        });
        assertEquals(1, ids.size());
        String xml = surveyDao.load(ids.get(ids.size() - 1));
        assertNotNull(xml);
        is.close();
    }

    public void testUpdate() throws IOException {
        InputStream isx = JdbcTemplateSurveyDaoTest.class.getResourceAsStream("skin1.xsl");
        assertNotNull(isx);
        xsltSkinDao.save("skin1.xsl", isx);
        List<Long> idsx = getJdbcTemplate().query("SELECT xsltskin_id FROM xsltskin", new RowMapper() {

            @Override
            public Object mapRow(ResultSet rs, int numRow) throws SQLException {
                return rs.getLong("xsltskin_id");
            }
        });
        assertEquals(1, idsx.size());
        InputStream is = JdbcTemplateSurveyDaoTest.class.getResourceAsStream("example-survey1.xml");
        assertNotNull(is);
        surveyDao.save(idsx.get(0), "test survey", new GregorianCalendar(), new GregorianCalendar(), "new", convertStreamToString(is));
        @SuppressWarnings("unused") List<Long> ids = getJdbcTemplate().query("SELECT survey_id FROM survey", new RowMapper() {

            @Override
            public Object mapRow(ResultSet rs, int numRow) throws SQLException {
                return rs.getLong("survey_id");
            }
        });
        assertEquals(1, ids.size());
        InputStream is2 = JdbcTemplateSurveyDaoTest.class.getResourceAsStream("example-survey2.xml");
        assertNotNull(is2);
        surveyDao.update(ids.get(0), idsx.get(0), "test survey", new GregorianCalendar(), new GregorianCalendar(), "finished", convertStreamToString(is2));
        @SuppressWarnings("unused") List<Long> idup = getJdbcTemplate().query("SELECT survey_id FROM survey", new RowMapper() {

            @Override
            public Object mapRow(ResultSet rs, int numRow) throws SQLException {
                return rs.getLong("survey_id");
            }
        });
        assertEquals(1, idup.size());
        String xml = surveyDao.load(ids.get(ids.size() - 1));
        assertNotNull(xml);
        assertTrue(xml.contains("<xss:status>finished</xss:status>"));
        assertTrue(xml.contains("<xss:name>test survey</xss:name>"));
        assertFalse(xml.contains("<xss:question qid=\"2\">"));
        isx.close();
        is.close();
        is2.close();
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[] { "file:src/test/resources/testContext.xml", "file:src/main/webapp/WEB-INF/daoContext.xml", "file:src/main/webapp/WEB-INF/applicationContext.xml" };
    }

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        super.executeSqlScript("file:src/test/resources/cleardb.sql", true);
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

    public void setSurveyDao(SurveyDao surveyDao) {
        this.surveyDao = surveyDao;
    }

    public void setXsltSkinDao(XsltSkinDao xsltSkinDao) {
        this.xsltSkinDao = xsltSkinDao;
    }
}
