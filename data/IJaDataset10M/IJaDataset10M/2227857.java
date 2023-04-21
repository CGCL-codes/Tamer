package test.unit.other;

import static org.testng.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import org.sqlsplatter.tinyhorror.other.ArrayComparator;
import org.sqlsplatter.tinyhorror.other.ConnectionProperties;
import org.sqlsplatter.tinyhorror.other.exceptions.ErrCode;
import org.sqlsplatter.tinyhorror.other.exceptions.THSException;
import org.sqlsplatter.tinyhorror.values.ConstantValue;
import org.sqlsplatter.tinyhorror.values.DataType;
import org.testng.annotations.Test;

public class TestOther {

    @Test
    public void constantValueCompare() throws Throwable {
        List<ConstantValue[]> actual = new ArrayList<ConstantValue[]>();
        actual.add(new ConstantValue[] { new ConstantValue(DataType.TYPE_CHAR, "x") });
        actual.add(new ConstantValue[] { new ConstantValue(DataType.TYPE_CHAR, "i") });
        actual.add(new ConstantValue[] { new ConstantValue(DataType.TYPE_CHAR, "E") });
        actual.add(new ConstantValue[] { new ConstantValue(DataType.TYPE_CHAR, "o") });
        List<ConstantValue[]> expected = new ArrayList<ConstantValue[]>();
        expected.add(actual.get(2));
        expected.add(actual.get(1));
        expected.add(actual.get(3));
        expected.add(actual.get(0));
        Comparator<ConstantValue[]> arrayComparator = new ArrayComparator<ConstantValue>(actual);
        Collections.sort(actual, arrayComparator);
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void connectionProperties() throws THSException {
        Properties p;
        ConnectionProperties cp;
        p = new Properties();
        p.put(ConnectionProperties.NEW_LINE, "\r\n");
        cp = new ConnectionProperties(p);
        assertEquals(cp.newLine, "\r\n");
        try {
            p.put(ConnectionProperties.NEW_LINE, "\\capperi");
            cp = new ConnectionProperties(p);
            assert (false);
        } catch (THSException e) {
            assertEquals(e.errCode, ErrCode.PARAM_INVALID_NL);
        }
    }
}
