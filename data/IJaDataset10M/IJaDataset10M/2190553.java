package pl.otros.logview.accept;

import org.junit.Test;
import pl.otros.logview.LogData;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HigherIdAcceptConditionTest extends SelectionAwareAcceptConditionTestBase {

    @Test
    public void testAccept() {
        HigherIdAcceptCondition acceptCondition = new HigherIdAcceptCondition(table, dataTableModel);
        table.getSelectionModel().addListSelectionListener(acceptCondition);
        table.getSelectionModel().setSelectionInterval(3, 3);
        LogData[] logData = dataTableModel.getLogData();
        for (LogData logData2 : logData) {
            if (logData2.getId() > 3) {
                assertTrue(acceptCondition.accept(logData2));
            } else {
                assertFalse(acceptCondition.accept(logData2));
            }
        }
    }
}
