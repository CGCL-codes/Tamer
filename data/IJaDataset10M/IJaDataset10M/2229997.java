package org.opentides.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.opentides.bean.SystemCodes;
import org.opentides.testsuite.BaseTidesTest;
import org.springframework.jdbc.core.RowMapper;

public class BaseCrudServiceTest extends BaseTidesTest {

    private SystemCodesService systemCodesService;

    private static final Logger _log = Logger.getLogger(BaseCrudServiceTest.class);

    private static final class SystemCodesMapper implements RowMapper {

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            SystemCodes sc = new SystemCodes();
            sc.setCategory(rs.getString("category_"));
            sc.setKey(rs.getString("key_"));
            sc.setValue(rs.getString("value_"));
            return sc;
        }
    }

    public void setSystemCodesService(SystemCodesService systemCodesService) {
        this.systemCodesService = systemCodesService;
    }

    public void testFindAll() {
        int expected = jdbcTemplate.queryForInt("SELECT count(*) FROM SYSTEM_CODES");
        int actual = systemCodesService.findAll().size();
        assertEquals(expected, actual);
        int prevCount = systemCodesService.findAll().size();
        createSystemCode();
        assertEquals(prevCount + 1, systemCodesService.findAll().size());
    }

    public void testFindAllPaging() {
        List<SystemCodes> scs1 = jdbcTemplate.query("SELECT * FROM SYSTEM_CODES limit 0,4", new SystemCodesMapper());
        List<SystemCodes> scs2 = systemCodesService.findAll(0, 4);
        assertEquals(scs1.size(), scs2.size());
        Assert.assertArrayEquals(scs1.toArray(), scs2.toArray());
    }

    public void testFindByExample() {
        SystemCodes example = new SystemCodes();
        example.setDisableProtection(true);
        example.setCategory("COUNTRY");
        List<SystemCodes> expected = jdbcTemplate.query("SELECT * FROM SYSTEM_CODES WHERE CATEGORY_='COUNTRY'", new SystemCodesMapper());
        List<SystemCodes> actual = systemCodesService.findByExample(example);
        assertEquals(expected.size(), actual.size());
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    public void testFindByExamplePaging() {
        SystemCodes example = new SystemCodes();
        example.setDisableProtection(true);
        example.setCategory("COUNTRY");
        List<SystemCodes> expected = jdbcTemplate.query("SELECT * FROM SYSTEM_CODES WHERE CATEGORY_ LIKE '%COUNTRY%' limit 4,7", new SystemCodesMapper());
        List<SystemCodes> actual = systemCodesService.findByExample(example, 4, 5);
        _log.debug("Size not exact match: " + expected.size());
        assertEquals(expected.size(), actual.size());
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    public void testFindByExampleExactMatch() {
        SystemCodes example = new SystemCodes();
        example.setDisableProtection(true);
        example.setCategory("COUNTRY");
        List<SystemCodes> expected = jdbcTemplate.query("SELECT * FROM SYSTEM_CODES WHERE CATEGORY_='COUNTRY'", new SystemCodesMapper());
        List<SystemCodes> actual = systemCodesService.findByExample(example, true);
        _log.debug("Size exact match: " + expected.size());
        assertEquals(expected.size(), actual.size());
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    public void testFindByExampleExactMatchPaging() {
        SystemCodes example = new SystemCodes();
        example.setDisableProtection(true);
        example.setCategory("OFFICE");
        List<SystemCodes> expected = jdbcTemplate.query("SELECT * FROM SYSTEM_CODES WHERE CATEGORY_='OFFICE' limit 4,5", new SystemCodesMapper());
        List<SystemCodes> actual = systemCodesService.findByExample(example, true, 4, 5);
        _log.debug("Size exact match: " + expected.size());
        assertEquals(expected.size(), actual.size());
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    public void testCountAll() {
        long expected = jdbcTemplate.queryForLong("SELECT count(*) FROM SYSTEM_CODES");
        long actual = systemCodesService.countAll();
        assertEquals(expected, actual);
        long prevCount = systemCodesService.countAll();
        createSystemCode();
        long currCount = systemCodesService.countAll();
        assertEquals(prevCount + 1, currCount);
    }

    public void testCountAllByExample() {
        SystemCodes example = new SystemCodes();
        example.setDisableProtection(true);
        example.setCategory("COUNTRY");
        long expected = jdbcTemplate.queryForLong("SELECT count(*) FROM SYSTEM_CODES WHERE CATEGORY_ LIKE '%COUNTRY%'");
        long actual = systemCodesService.countByExample(example);
        assertEquals(expected, actual);
        long prevCount = systemCodesService.countAll();
        createSystemCode();
        long currCount = systemCodesService.countAll();
        assertEquals(prevCount + 1, currCount);
    }

    public void testCountAllByExampleExactMatch() {
        SystemCodes example = new SystemCodes();
        example.setDisableProtection(true);
        example.setCategory("O");
        long expected = jdbcTemplate.queryForLong("SELECT count(*) FROM SYSTEM_CODES WHERE CATEGORY_ = 'O'");
        long actual = systemCodesService.countByExample(example, true);
        _log.debug("Count exact match: " + expected);
        assertEquals(expected, actual);
    }

    public void testLoadString() {
        try {
            systemCodesService.load("");
            fail("Should have thrown exception");
        } catch (Exception e) {
            _log.debug(e.getMessage());
        }
        try {
            systemCodesService.load("a123");
            fail("Should have thrown exception");
        } catch (Exception e) {
            _log.debug(e.getMessage());
        }
        assertNotNull(systemCodesService.load("9001"));
    }

    public void testLoad() {
        assertNotNull(systemCodesService.load(9001L));
    }

    public void testSave() {
        SystemCodes sc = new SystemCodes();
        sc.setDisableProtection(true);
        sc.setCategory("OFFICE");
        sc.setKey("ED");
        sc.setValue("Events Department");
        long prevCount = jdbcTemplate.queryForLong("SELECT count(*) FROM SYSTEM_CODES");
        systemCodesService.save(sc);
        long currCount = jdbcTemplate.queryForLong("SELECT count(*) FROM SYSTEM_CODES");
        assertEquals(prevCount + 1, currCount);
    }

    public void testDelete() {
        long prevCount = jdbcTemplate.queryForLong("SELECT count(*) FROM SYSTEM_CODES");
        systemCodesService.delete(9001L);
        long currCount = jdbcTemplate.queryForLong("SELECT count(*) FROM SYSTEM_CODES");
        assertEquals(prevCount - 1, currCount);
    }

    public void testDeleteString() {
        try {
            systemCodesService.delete("");
            fail("Should have thrown exception");
        } catch (Exception e) {
            _log.debug(e.getMessage());
        }
        try {
            systemCodesService.delete("a109");
            fail("Should have thrown exception");
        } catch (Exception e) {
            _log.debug(e.getMessage());
        }
        long prevCount = jdbcTemplate.queryForLong("SELECT count(*) FROM SYSTEM_CODES");
        systemCodesService.delete(String.valueOf(9001L));
        long currCount = jdbcTemplate.queryForLong("SELECT count(*) FROM SYSTEM_CODES");
        assertEquals(prevCount - 1, currCount);
    }

    private void createSystemCode() {
        jdbcTemplate.execute("INSERT INTO SYSTEM_CODES(CATEGORY_,KEY_,VALUE_) VALUES('OFFICE','HR','Human Resources')");
    }
}
