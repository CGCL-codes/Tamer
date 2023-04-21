package test.net.sf.karatasi.databaseoperations.edit;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import net.sf.karatasi.database.Category;
import net.sf.karatasi.database.DBValueException;
import net.sf.karatasi.database.Database;
import net.sf.karatasi.databaseoperations.edit.CardListEntry;
import net.sf.karatasi.databaseoperations.edit.CardsOfCategoryTableModel;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import test.net.sf.karatasi.TestHelpers;

/**  Test cases for the {@see CardsOfCategoryTableModel}.
 *
 * @author <a href="mailto:kussinger@sourceforge.net">Mathias Kussinger</a>
 */
public class CardsOfCategoryTableModelTest {

    /** Load database driver.
     * @throws ClassNotFoundException if the JDBC driver could not be loaded.
     */
    @BeforeClass
    public static void loadJDBCDriver() throws ClassNotFoundException {
        Database.loadJDBCDriver();
    }

    /** Test creation and data retrieval.
     *
     */
    @Test
    public final void testConstructTableModel() throws IOException, SQLException, DBValueException {
        Locale.setDefault(Locale.ENGLISH);
        final File testDatabaseFile = TestHelpers.prepareDataDirectoryAndCopyDatabase("l_test5.db");
        final Database testDatabase = new Database(testDatabaseFile);
        testDatabase.open();
        Assert.assertTrue("Database must exist.", testDatabase.isHealthy());
        final Category testCategory = testDatabase.getCategoryById(5);
        Assert.assertNotNull("Category must exist.", testCategory);
        final CardsOfCategoryTableModel model = new CardsOfCategoryTableModel(testCategory);
        Assert.assertNotNull("model instance must exist.", testCategory);
        Assert.assertEquals("columns count has to be 1", 1, model.getColumnCount());
        Assert.assertEquals("rows count has to be 5", 5, model.getRowCount());
        CardListEntry entry = (CardListEntry) model.getValueAt(0, 1);
        Assert.assertNull("out of bounds has to return null", entry);
        entry = (CardListEntry) model.getValueAt(0, -1);
        Assert.assertNull("out of bounds has to return null", entry);
        entry = (CardListEntry) model.getValueAt(-1, 0);
        Assert.assertNull("out of bounds has to return null", entry);
        entry = (CardListEntry) model.getValueAt(5, 0);
        Assert.assertNull("out of bounds has to return null", entry);
        entry = (CardListEntry) model.getValueAt(0, 0);
        Assert.assertEquals("id has to match", 905, entry.getId());
        Assert.assertEquals("leftColumn has to match", "eifersüchtig", entry.getLeftColumn());
        Assert.assertEquals("rightColumn has to match", "", entry.getRightColumn());
        Assert.assertEquals("committedFlag has to match", false, entry.isCommitted());
        testDatabase.close();
        TestHelpers.removeDataDirectory();
    }
}
