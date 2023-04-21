package com.silentlexx.ffuudbconv;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldsInfo {

    final String SQL_f = "sql_fields.csv";

    String templ_file;

    boolean loadfromT = true;

    public final String PATERN = ";";

    public final String KEY = "\\{(.+?)\\}";

    String SQL_s = "";

    static final int max = 32;

    int lines = 0;

    int fields = 0;

    int sql_n = 0;

    int csv_n = 0;

    int tmp_n = 0;

    int sID = 1;

    int[] sql_csv = new int[max];

    String[] tmp_s = new String[max];

    String[] csv_fields = new String[max];

    String[] sql_fields = new String[max];

    Debug D;

    FieldsInfo(String f) {
        D = Main.D;
        templ_file = f;
        D.p("Create 'fields_info' object");
        Init();
    }

    void Init() {
        D.p("Init 'fields_info' object");
        for (int i = 0; i < max; i++) {
            sql_csv[i] = 0;
        }
        loadTrafaret();
        if (loadfromT) {
            loadFromTrafaret();
        } else {
            loadFromCSV();
        }
    }

    void loadFromTrafaret() {
        Pattern pattern = Pattern.compile(KEY);
        Matcher matcher = pattern.matcher(SQL_s);
        String b;
        int i = 0;
        while (matcher.find()) {
            b = matcher.group().substring(1, matcher.group().length() - 1);
            if (checkKey(b, i)) {
                D.p(i + " SQL key added: " + b);
                addSQL(i, b);
                i++;
            }
            sql_n = i;
        }
    }

    boolean checkKey(String s, int m) {
        for (int n = 0; n < m; n++) {
            if (sql_fields[n].equals(s)) {
                return false;
            }
        }
        return true;
    }

    void loadFromCSV() {
        FileRead f = new FileRead(SQL_f);
        f.Open();
        parseSQL(f.Readln());
        f.Close();
    }

    public void setID(int id) {
        sID = id;
    }

    public int getID() {
        return sID;
    }

    void loadTrafaret() {
        FileRead f = new FileRead(templ_file);
        f.Open();
        String s;
        while (!f.isEOF()) {
            s = f.Readln();
            if (s != null) SQL_s = SQL_s + s + "\n";
        }
        f.Close();
        D.p("String ext: " + SQL_s);
    }

    public void setLines(int n) {
        lines = n;
    }

    public int getLines() {
        return lines;
    }

    public String getTrafaret() {
        return SQL_s;
    }

    public String[] getCsvList() {
        return csv_fields;
    }

    public String[] getSqlList() {
        return sql_fields;
    }

    void parseSQL(String s) {
        String[] bs;
        bs = s.split(PATERN);
        sql_n = bs.length;
        for (int i = 0; i < sql_n; i++) {
            addSQL(i, bs[i]);
            D.p(i + " SQL Field Added: " + bs[i]);
        }
    }

    public int parseCVS(String CSV_s) {
        String[] bs;
        bs = CSV_s.split(PATERN);
        csv_n = bs.length;
        for (int i = 0; i < csv_n; i++) {
            addCSV(i, bs[i]);
            D.p(i + " CSV Field Added: " + bs[i]);
        }
        return csv_n;
    }

    void addCSV(int n, String s) {
        csv_fields[n] = new String();
        csv_fields[n] = s;
    }

    void addSQL(int n, String s) {
        sql_fields[n] = new String();
        sql_fields[n] = s;
    }

    public String getCSV(int n) {
        if (n >= 0 && n < csv_n) {
            return csv_fields[n];
        } else return null;
    }

    public boolean setSC(int n, int i) {
        if (n >= 0 && n < max) {
            sql_csv[n] = i;
            return true;
        } else return false;
    }

    public int getSC(int n) {
        if (n >= 0 && n < max) {
            return sql_csv[n];
        } else return 0;
    }

    public boolean setCSV(int n, String s) {
        if (n >= 0 && n < csv_n) {
            csv_fields[n] = s;
            return true;
        } else return false;
    }

    public String getSQL(int n) {
        if (n >= 0 && n < sql_n) {
            return sql_fields[n];
        } else return null;
    }

    public boolean setSQL(int n, String s) {
        if (n >= 0 && n < sql_n) {
            sql_fields[n] = s;
            return true;
        } else return false;
    }

    public int getNumSQL() {
        return sql_n;
    }

    public int getNumCSV() {
        return csv_n;
    }
}
