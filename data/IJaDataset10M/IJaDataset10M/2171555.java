package dbj;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import dbj.Select;
import dbj.Update;
import dbj.ReadData;
import dbj.SetSelect;
import dbj.SetUpdate;

/**
 * Testklass f�r prestandatest av Dao och databaser
 */
public class TablePerformanceTest {

    private static String TESTTABLE1 = "dbj_test_person";

    private Connection conn = null;

    /**
	 */
    @Before
    protected void setUp() {
        try {
            this.conn = DbConnection.getInstance().getConnection();
        } catch (Exception e) {
            System.out.println("Fel i DbTest: " + e.getMessage());
        }
    }

    /**
	 */
    @After
    protected void tearDown() {
        try {
            this.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
	 */
    @Test
    public void deleteAllFromTable() {
        try {
            Update dbUpdate = new Update(null, TESTTABLE1);
            dbUpdate.delete(new SetUpdate(), this.conn);
            dbUpdate.doCommit(this.conn);
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail("Error: " + e.getMessage());
        }
    }

    /**
	 */
    @Test
    public void insertIntoTable() {
        try {
            this.deleteAllFromTable();
            Update dbUpdate = new Update(null, TESTTABLE1);
            SetUpdate setUpdate = new SetUpdate();
            for (int i = 0; i < 100000; i++) {
                setUpdate.clear();
                setUpdate.addData("KUNDNR", new Integer(i + 4000));
                setUpdate.addData("ORDERNR", new Integer((i + 25 * 2)));
                setUpdate.addData("FNAMN", "Fuuba" + i + 1);
                setUpdate.addData("ENAMN", "Euuba" + i + 1);
                setUpdate.addData("ANTAL", new Integer(i + 10));
                dbUpdate.insert(setUpdate, this.conn);
                if ((i % 100) == 0) dbUpdate.doCommit(this.conn);
            }
            dbUpdate.doCommit(this.conn);
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail("Error: " + e.getMessage());
        }
    }

    /**
	 */
    @Test
    public void updateIntoTable() {
        try {
            Update dbUpdate = new Update(null, TESTTABLE1);
            SetUpdate setUpdate = new SetUpdate();
            for (int i = 0; i < 100000; i++) {
                setUpdate.clear();
                setUpdate.addKey("KUNDNR", new Integer(i + 4000));
                setUpdate.addKey("ORDERNR", new Integer((i + 25 * 2)));
                setUpdate.addData("ANTAL", new Integer(i + 50));
                dbUpdate.update(setUpdate, this.conn, false);
                if ((i % 100) == 0) dbUpdate.doCommit(this.conn);
            }
            dbUpdate.doCommit(this.conn);
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail("Error: " + e.getMessage());
        }
    }

    @Test
    public void selectFromTable() {
        try {
            Select dbSelect = new Select(null, TESTTABLE1);
            SetSelect setSelect = new SetSelect();
            setSelect.setOrderBy("KUNDNR, ORDERNR");
            List<ReadData> list = dbSelect.select(setSelect, this.conn);
            int antal = 0;
            int rader = 0;
            for (ReadData rd : list) {
                antal += rd.getInt("ANTAL");
                rader++;
            }
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail("Error: " + e.getMessage());
        }
    }
}
