package br.com.pbsoft.io.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import junit.framework.TestCase;
import org.junit.Test;
import br.com.pbsoft.io.DataTable;

public class DataTableTest extends TestCase {

    static String[] headers = { "Age", "Education", "Income", "Marital Status", "Purchase?" };

    private DataTable<String> dataTable;

    private void loadDataTable() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("data/sample1.dt")));
        try {
            dataTable = DataTable.load(reader, '\n', ';');
        } catch (IOException e) {
            throw e;
        } finally {
            reader.close();
        }
    }

    @Test
    public void testGetValueSet() {
        try {
            loadDataTable();
            Set<String> valueSet = dataTable.getValueSet(4);
            if (valueSet.size() != 2) failNotEquals("Value set for column 4 has more than 2 items.", 2, valueSet.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetValueList() {
        try {
            loadDataTable();
            List<String> valueList = dataTable.getValueList(4);
            if (!valueList.get(0).equals("will buy")) failNotEquals("First value for column 4 doesn't have the value 'will buy'.", "will buy", valueList.get(0)); else if (!valueList.get(1).equals("won't buy")) failNotEquals("Second value for column 4 doesn't have the value 'won't buy'.", "won't buy", valueList.get(1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFilter() {
        try {
            loadDataTable();
            dataTable.filter("Age", "< 18");
            if (dataTable.getRegisters().size() != 3) failNotEquals("Filter returned more than 3 registers with age < 18.", 3, dataTable.getRegisters().size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoad() {
        int i;
        String expectedHeader;
        try {
            loadDataTable();
            i = 0;
            for (String header : dataTable.getHeader().getColumns()) {
                expectedHeader = headers[i++];
                if (!header.equals(expectedHeader)) failNotEquals("Invalid header: " + header + ".", expectedHeader, header);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
