package net.sourceforge.nattable.support;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import net.sourceforge.nattable.config.IBodyConfig;
import net.sourceforge.nattable.config.SizeConfig;
import net.sourceforge.nattable.renderer.ICellRenderer;
import org.junit.Before;
import org.junit.Test;

public class ColumnGroupSupportTest {

    private ColumnGroupSupport columnGroupSupport;

    private ColumnTransformSupport columnReorderSupport;

    private Map<Integer, String> columnGroupLabels;

    @Before
    public void init() {
        columnReorderSupport = new ColumnTransformSupport(new IBodyConfig() {

            public int getColumnCount() {
                return 10;
            }

            public SizeConfig getColumnWidthConfig() {
                return null;
            }

            public int getRowCount() {
                return 0;
            }

            public SizeConfig getRowHeightConfig() {
                return null;
            }

            public ICellRenderer getCellRenderer() {
                return null;
            }
        });
        columnGroupSupport = new ColumnGroupSupport(columnReorderSupport);
        columnGroupLabels = new LinkedHashMap<Integer, String>();
        createColumnGroups();
    }

    @Test
    public void moveColumnsAcrossGroups() {
        Integer colIndex = new Integer(0);
        Integer newColIndex = new Integer(colIndex.intValue() + 2);
        Assert.assertEquals(1, columnGroupSupport.getColumnGroupMembers(columnGroupLabels.get(newColIndex)).size());
        columnGroupSupport.moveColumnBetweenGroups(columnGroupLabels.get(colIndex), colIndex.intValue(), columnGroupLabels.get(newColIndex), newColIndex.intValue());
        Assert.assertEquals(2, columnGroupSupport.getColumnGroupMembers(columnGroupLabels.get(newColIndex)).size());
    }

    private void createColumnGroups() {
        for (int i = 0; i < 4; i++) {
            String cgName = "ColumnGroup" + i;
            columnGroupLabels.put(Integer.valueOf(i), cgName);
            List<Integer> cols = new ArrayList<Integer>();
            cols.add(Integer.valueOf(i * 2));
            columnGroupSupport.addColumnGroup(cgName, cols);
        }
    }
}
