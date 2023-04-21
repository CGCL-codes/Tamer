package isql;

import static org.junit.Assert.*;
import isql.array.Array;
import isql.groupby.GroupKey;
import isql.groupby.Groups;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.junit.Test;

/**
 * @author SHZ Nov 4, 2011
 *
 */
public class GroupByTest {

    /**
	 * Test method for {@link isql.GroupBy#apply(isql.ColumnValuesWithRowsNumber)}.
	 * @throws ISQLException 
	 */
    @Test
    public void testApply() throws ISQLException {
        ColumnKey nameKey = new ColumnKey("tb1", "name");
        ColumnKey cityKey = new ColumnKey("tb1", "city");
        ColumnKey ageKey = new ColumnKey("tb1", "age");
        GroupBy groupBy = new GroupBy(Arrays.asList(nameKey, ageKey));
        Map<ColumnKey, Array> mapValuesByColumn = new TreeMap<ColumnKey, Array>();
        mapValuesByColumn.put(nameKey, new Array(new String[] { "John", "Arthur", "Louise", "John" }, ColumnType.TEXT));
        mapValuesByColumn.put(cityKey, new Array(new String[] { "San Diego", "Minneapolis", "Dallas", "Detroit" }, ColumnType.TEXT));
        mapValuesByColumn.put(ageKey, new Array(new Double[] { 31.0, 44.0, 27.0, 31.0 }, ColumnType.NUMERIC));
        Groups groups = groupBy.apply(new ColumnValuesWithRowsNumber(mapValuesByColumn));
        List<GroupKey> groupKeys = groups.getGroupKeysInOriginalOrder();
        assertEquals(3, groupKeys.size());
        GroupKey key1 = groupKeys.get(0);
        Object[] values1 = key1.getArrColumnValue();
        assertEquals(Double.valueOf(31.0), values1[0]);
        assertEquals("John", values1[1]);
        GroupKey key2 = groupKeys.get(1);
        Object[] values2 = key2.getArrColumnValue();
        assertEquals(Double.valueOf(44.0), values2[0]);
        assertEquals("Arthur", values2[1]);
        GroupKey key3 = groupKeys.get(2);
        Object[] values3 = key3.getArrColumnValue();
        assertEquals(Double.valueOf(27.0), values3[0]);
        assertEquals("Louise", values3[1]);
    }
}
