package net.sf.unofficialjunit.csv;

import java.util.Arrays;
import java.util.List;
import net.sf.unofficialjunit.csv.filters.BooleanFilter;
import net.sf.unofficialjunit.csv.filters.StringFilter;

/**
 * test reading a string and a boolean from a csv file
 * 
 * @author Georg
 * 
 */
public class CSVParameterized_GetDataWithFilter_Test extends CSVParameterized_GetData_Test_Base {

    /**
     * initialise the fixture and from the file test.csv and the expected
     * values.
     */
    @Override
    List<Object[]> getFixture() {
        return CSVParameterised.getData(CSVParameterized_GetDataWithFilter_Test.class.getResource("test.csv"), ",", new StringFilter(1), new BooleanFilter(2));
    }

    @Override
    List<Object[]> getExpected() {
        return Arrays.asList(new Object[][] { { "test", Boolean.TRUE }, { "more", Boolean.FALSE }, { "billy", Boolean.TRUE } });
    }
}
