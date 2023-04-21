package tests.othertypes.enum;

import java.sql.SQLException;

import junit.framework.TestCase;
import mysqltest.domain.custom.OtherTypes;

import org.apache.log4j.Logger;

import tests.SqlMapSingleton;

import com.ibatis.sqlmap.client.SqlMapClient;

public class SetTests extends TestCase {

  private static Logger logger = Logger.getLogger(SetTests.class);

  private SqlMapClient sqlMap = SqlMapSingleton.getInstance().getSqlMap();

  public SetTests(final String txt) {
    super(txt);
  }

  public void testNull() throws SQLException {
    storeAndRetrieve(null);
  }

  public void testEmpty() throws SQLException {
    storeAndRetrieve("");
  }

  public void testFirstValue() throws SQLException {
    storeAndRetrieve("one");
  }

  public void testLastValue() throws SQLException {
    storeAndRetrieve("seven");
  }

  public void testMediumValue() throws SQLException {
    storeAndRetrieve("three");
  }

  public void testTwoValue() throws SQLException {
    storeAndRetrieve("two,four");
  }

  public void testTwoUnorderedValues() throws SQLException {
    storeAndRetrieve("four,two", "two,four");
  }

  public void testFullSetValues() throws SQLException {
    storeAndRetrieve("three,four,two,seven,one,five",
        "one,two,three,four,five,seven");
  }

  private void storeAndRetrieve(final String store) throws SQLException {
    storeAndRetrieve(store, store);
  }

  private void storeAndRetrieve(final String store, final String expected)
      throws SQLException {

    try {
      sqlMap.startTransaction();
      logger.debug("I will update with value: " + store);

      OtherTypes ity = new OtherTypes(1);
      ity.setIncluded(store);
      int rowsUpdated = sqlMap.update("updateOtherTypes", ity);
      assertEquals(rowsUpdated, 1);

      sqlMap.commitTransaction();
    } finally {
      sqlMap.endTransaction();
    }

    try {
      sqlMap.startTransaction();
      logger.debug("I will select for value: " + store);

      OtherTypes ity = (OtherTypes) sqlMap.queryForObject("selectOtherTypes",
          new OtherTypes(1));
      logger.debug("ity=" + ity);

      assertEquals(expected, ity.getIncluded());

      sqlMap.commitTransaction();
    } finally {
      sqlMap.endTransaction();
    }
  }

}
