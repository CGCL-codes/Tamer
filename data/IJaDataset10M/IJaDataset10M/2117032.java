package net.sourceforge.ondex.parser.tableparser.tableEmulators;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.ondex.parser.tableparser.TableEmulator;

public class FlatFileTableEmulator implements TableEmulator {

    private boolean casheEntireContent = false;

    private String fileName;

    private ArrayList<ArrayList<String>> cashe = null;

    private String colRegex;

    private List<String> currentRow;

    private int currentRowIndex = 0;

    private DataInputStream in;

    private BufferedReader br;

    public FlatFileTableEmulator(String fileName, String colSeparatorRegex, boolean casheEntireContent) {
        init(fileName, colSeparatorRegex, casheEntireContent);
    }

    public FlatFileTableEmulator(String fileName, String colSeparatorRegex) {
        init(fileName, colSeparatorRegex, false);
    }

    private void init(String fileName, String colSeparatorRegex, boolean casheEntireContent) {
        this.colRegex = colSeparatorRegex;
        this.casheEntireContent = casheEntireContent;
        this.fileName = fileName;
        try {
            FileInputStream fstream = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            if ((strLine = br.readLine()) == null) return;
            currentRow = new ArrayList<String>(Arrays.asList(strLine.split(colRegex)));
            if (!casheEntireContent) {
                return;
            }
            cashe = new ArrayList<ArrayList<String>>();
            do {
                cashe.add(new ArrayList<String>(Arrays.asList(strLine.split(colRegex))));
            } while ((strLine = br.readLine()) != null);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public String getData(int column, int row) {
        if (casheEntireContent) return getCashedData(column, row);
        if (row == currentRowIndex) {
            return currentRow.get(column);
        } else if (row > currentRowIndex) {
            try {
                String strLine = null;
                while (row > currentRowIndex && (strLine = br.readLine()) != null) {
                    currentRow = new ArrayList<String>(Arrays.asList(strLine.split(colRegex)));
                    currentRowIndex++;
                }
                if (strLine == null) return null;
                return currentRow.get(column);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        try {
            in.close();
            FileInputStream fstream = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));
            String strLine = null;
            while (row > currentRowIndex && (strLine = br.readLine()) != null) {
                currentRow = new ArrayList<String>(Arrays.asList(strLine.split(colRegex)));
                currentRowIndex++;
            }
            if (strLine == null) return null;
            return currentRow.get(column);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getCashedData(int column, int row) {
        ArrayList<String> temp = cashe.get(row);
        if (temp == null) return null;
        return temp.get(column);
    }

    public void close() {
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
