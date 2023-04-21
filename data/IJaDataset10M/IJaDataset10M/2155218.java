package es.gavab.jmh.analysis.table.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import es.gavab.jmh.analysis.table.CompactSheetCreator;
import es.gavab.jmh.analysis.table.ComplexTable;
import es.gavab.jmh.analysis.table.ComplexTableTransformer;
import es.gavab.jmh.analysis.table.NumberFormat;
import es.gavab.jmh.analysis.table.PropertySelector;
import es.gavab.jmh.analysis.table.SheetTable;
import es.gavab.jmh.analysis.table.TextSpaceSheetFormatter;
import es.gavab.jmh.analysis.table.NumberFormat.NumberType;
import es.gavab.jmh.analysis.table.PropertySelector.PropType;
import es.gavab.jmh.util.Properties;

public class ComplexTableTransformerTest {

    public static void main(String[] args) {
        List<Properties> rows = new ArrayList<Properties>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 3; k++) {
                    Properties row = new Properties();
                    row.put("i", i);
                    row.put("j", j);
                    row.put("k", k);
                    row.put("l", 0);
                    rows.add(row);
                }
            }
        }
        List<Properties> cols = new ArrayList<Properties>();
        List<NumberFormat> numberFormats = new ArrayList<NumberFormat>();
        Properties col1 = new Properties();
        col1.put("prop", "objValue");
        col1.put("comp", "Dev");
        cols.add(col1);
        numberFormats.add(new NumberFormat(NumberType.DECIMAL));
        Random r = new Random();
        double[][] values = new double[rows.size()][cols.size()];
        for (int i = 0; i < rows.size(); i++) {
            for (int j = 0; j < cols.size(); j++) {
                values[i][j] = r.nextDouble() * 10;
            }
        }
        ComplexTable table = new ComplexTable(rows, cols, values);
        table.setNumberFormats(numberFormats);
        CompactSheetCreator csc = new CompactSheetCreator();
        SheetTable sheet = csc.createSheet(table);
        TextSpaceSheetFormatter sf = new TextSpaceSheetFormatter();
        sf.format(sheet);
        System.out.println(sf.getFormattedTable());
        ComplexTableTransformer ctt = new ComplexTableTransformer();
        ctt.setColumnProperties(new PropertySelector(PropType.ROW, "j"), new PropertySelector(PropType.ROW, "i"));
        table = ctt.transform(table);
        sheet = csc.createSheet(table);
        sf.format(sheet);
        System.out.println(sf.getFormattedTable());
    }
}
