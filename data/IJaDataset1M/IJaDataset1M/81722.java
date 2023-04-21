package yahoofinance.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import yahoofinance.test.data.RunAllDataObjectsTests;
import yahoofinance.test.db.RunAllDatabaseTests;
import yahoofinance.test.spreadsheet.SpreadsheetTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({ RunAllDataObjectsTests.class, RunAllDatabaseTests.class, SpreadsheetTests.class })
public class TestSuite {
}
