package auto_test.tests.table_checker;

import java.util.List;
import jtq.core.TableChecker;
import jtq.core.ADatabase.DatabaseEnum;
import junit.framework.Assert;
import org.junit.Test;
import auto_test.setup.Globals;
import auto_test.tables.many_fields.Row;
import auto_test.tables.many_fields.Table;

public class CheckManyColumnsSqlServer {

    @Test
    public void test_01() {
        if (Globals.DATABASE.getDatabaseType() == DatabaseEnum.SqlServer) {
            TableChecker tableChecker = new TableChecker(new Table(), new Row());
            List<String> messages = tableChecker.validate();
            for (String message : messages) System.out.println(message);
            Assert.assertTrue(messages.size() == 0);
        }
    }
}
