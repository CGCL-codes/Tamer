package it.ame.permflow.core;

import it.ame.permflow.util.Logger;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Tables {

    final ArrayList<Table> tables;

    public Table curHumTable = null;

    public Table curPermTable = null;

    public Table baseHum = null;

    public Table basePerm = null;

    private HashMap<Table, String[]> ranges;

    public static DecimalFormat formatters[];

    public static final int FORMATTER_WHOLE = 0;

    public static final int FORMATTER_DECIMAL = 1;

    public static final int FORMATTER_CENTESIMAL = 2;

    public Tables() {
        tables = new ArrayList<Table>();
        ranges = new HashMap<Table, String[]>();
        formatters = new DecimalFormat[3];
        formatters[0] = new DecimalFormat("#####");
        formatters[1] = new DecimalFormat("####0.0");
        formatters[2] = new DecimalFormat("####0.00");
    }

    public Table get(int index) {
        return tables.get(index);
    }

    public Table get(String name) {
        for (int i = 0; i < tables.size(); ++i) if (tables.get(i).name.compareToIgnoreCase(name) == 0) return tables.get(i);
        return null;
    }

    public ArrayList<Table> getList() {
        return tables;
    }

    public void add(Table t) {
        tables.add(t);
        String[] range = new String[5];
        float[] div = { 1.0f, 10.0f, 100.0f };
        range[0] = formatters[t.getFormatter()].format(t.getTo(0) / div[t.getFormatter()]);
        int maxI = 1;
        float max = 0.0f;
        for (int i = 1; i < t.size(); ++i) if (t.getTo(i) > max) {
            max = t.getTo(i);
            maxI = i;
        }
        range[4] = formatters[t.getFormatter()].format(t.getTo(maxI) / div[t.getFormatter()]);
        t.setMax((int) t.getFrom(t.size() - 1), (int) t.getTo(maxI) / div[t.getFormatter()]);
        float incr = max / 4;
        for (int j = 1; j <= 3; ++j) {
            range[j] = formatters[t.getFormatter()].format(incr * j / div[t.getFormatter()]);
        }
        ranges.put(t, range);
    }

    public void updateRange(Table t) {
        String[] range = new String[5];
        float[] div = { 1.0f, 10.0f, 100.0f };
        range[0] = formatters[t.getFormatter()].format(t.getTo(0) / div[t.getFormatter()]);
        int maxI = 1;
        float max = 0.0f;
        for (int i = 1; i < t.size(); ++i) if (t.getTo(i) > max) {
            max = t.getTo(i);
            maxI = i;
        }
        range[4] = formatters[t.getFormatter()].format(t.getTo(maxI) / div[t.getFormatter()]);
        t.setMax((int) t.getFrom(t.size() - 1), (int) t.getTo(maxI) / div[t.getFormatter()]);
        float incr = max / 4;
        for (int j = 1; j <= 3; ++j) {
            range[j] = formatters[t.getFormatter()].format(incr * j / div[t.getFormatter()]);
        }
        ranges.remove(t);
        ranges.put(t, range);
    }

    public void del(int index) {
        Table t = tables.remove(index);
        ranges.remove(t);
        if (curHumTable == t) curHumTable = null;
        if (curPermTable == t) curPermTable = null;
    }

    public int size() {
        return tables.size();
    }

    public String[] getHumRange() {
        if (curHumTable == null) return ranges.get(baseHum); else return ranges.get(curHumTable);
    }

    public String[] getPermRange() {
        if (curPermTable == null) return ranges.get(basePerm); else return ranges.get(curPermTable);
    }

    public float getHumMaxY() {
        if (curHumTable != null) return curHumTable.maxY; else return baseHum.maxY;
    }

    public float getHumRawMaxY() {
        float[] div = { 1.0f, 10.0f, 100.0f };
        if (curHumTable != null) return curHumTable.maxY * div[curHumTable.getFormatter()]; else return baseHum.maxY * div[baseHum.getFormatter()];
    }

    public float getPermMaxY() {
        if (curPermTable != null) return curPermTable.maxY; else return basePerm.maxY;
    }

    public float getPermRawMaxY() {
        float[] div = { 1.0f, 10.0f, 100.0f };
        if (curPermTable != null) return curPermTable.maxY * div[curPermTable.getFormatter()]; else return basePerm.maxY * div[basePerm.getFormatter()];
    }

    public int calcHumValue(int val) {
        if (curHumTable != null) return curHumTable.calcValue(baseHum.calcValue(val)); else return baseHum.calcValue(val);
    }

    public int calcPermValue(int val) {
        if (curPermTable != null) return curPermTable.calcValue(basePerm.calcValue(val)); else return basePerm.calcValue(val);
    }

    public DecimalFormat getHumFormatter() {
        if (curHumTable != null) return formatters[curHumTable.getFormatter()]; else return formatters[baseHum.getFormatter()];
    }

    public DecimalFormat getPermFormatter() {
        if (curPermTable != null) return formatters[curPermTable.getFormatter()]; else return formatters[basePerm.getFormatter()];
    }

    public void generateBase() {
        Table t = new Table("Base Umidita", FORMATTER_WHOLE);
        t.add(0, 0);
        t.add(550, 0);
        t.add(725, 260);
        t.add(1236, 390);
        t.add(1610, 610);
        t.add(2439, 820);
        t.add(3234, 1270);
        t.add(3765, 1620);
        t.add(4031, 1850);
        t.add(4095, 2100);
        add(t);
        t = new Table("Base Permeabilita", FORMATTER_DECIMAL);
        t.add(0, 0);
        t.add(8333, 24999);
        add(t);
    }

    public void save() throws IOException {
        DataOutputStream dosTables = new DataOutputStream(new FileOutputStream(new File("tables.dat")));
        for (Table tmp : tables) {
            dosTables.writeBytes(tmp.getName() + "~");
            dosTables.writeBytes(tmp.getFormatter() + "~");
            for (Table.Pair p : tmp.getCells()) dosTables.writeBytes(p.from + "|" + p.to + "|");
            dosTables.writeBytes("\r\n");
        }
        dosTables.close();
    }

    public boolean load() throws IOException {
        try {
            File file = new File("tables.dat");
            if (!file.exists()) {
                generateBase();
                baseHum = tables.get(0);
                basePerm = tables.get(1);
                return false;
            }
            BufferedReader brTables = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            StringTokenizer lineToken;
            StringTokenizer tableToken;
            Table tmpTable = null;
            String curLine = brTables.readLine();
            while (curLine != null) {
                lineToken = new StringTokenizer(curLine, "~");
                tmpTable = new Table(lineToken.nextToken(), Integer.parseInt(lineToken.nextToken()));
                tableToken = new StringTokenizer(lineToken.nextToken(), "|");
                while (tableToken.hasMoreElements()) tmpTable.add(Float.valueOf(tableToken.nextToken()), Float.valueOf(tableToken.nextToken()));
                curLine = brTables.readLine();
                add(tmpTable);
            }
            baseHum = tables.get(0);
            basePerm = tables.get(1);
        } catch (Exception e) {
            Logger.reportException(e);
        }
        return false;
    }
}
