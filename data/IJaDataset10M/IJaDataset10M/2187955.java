package infrastructure;

import org.swiftdao.demo.MockJdbcDao;
import java.util.Map;
import java.util.TreeMap;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @author Wang Yuxing
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/ac-ut-encrypt.xml", "classpath:spring/ac-ut-ds.xml", "classpath:spring/ac-ut-dao.xml", "classpath:spring/ac-ut.xml" })
public class BaseJdbcDaoTest extends BaseDaoTest {

    MockJdbcDao jdbcDao = null;

    public BaseJdbcDaoTest() {
    }

    public void testExecuteSpNoInOut() {
        jdbcDao.execute("myspNoInOut");
    }

    public void testExecuteSpNoOut() {
        Map<String, Object> p = new TreeMap<String, Object>();
        p.put("s_key", "12003041727340");
        jdbcDao.execute("myspNoOut", p);
    }

    public void testExecuteSpRecordsetReturned() {
    }
}
