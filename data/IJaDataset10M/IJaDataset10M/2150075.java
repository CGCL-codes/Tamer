package org.jazzteam.action.search;

import org.jazzteam.util.Database;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchActionTest {

    private Database db;

    private ResultSet result;

    @Before
    public void setUp() throws Exception {
        db = new Database();
    }

    @Test
    public void testCredit() throws SQLException {
        result = db.query("SELECT * FROM credit;");
        Assert.assertTrue(db.getRow(result) > 0);
    }

    @Test
    public void testBank() throws SQLException {
        result = db.query("SELECT * FROM bank;");
        Assert.assertTrue(db.getRow(result) > 0);
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }
}
