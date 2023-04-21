package backend.parser.aracyc;

import java.util.*;

class DataSink {

    private ArrayList<Vector<ArrayList<String>>> dataLines = new ArrayList<Vector<ArrayList<String>>>(500);

    private String[] ColumnNames;

    private int nCols = 0;

    private ArrayList<Integer> dataRowLineCounts = new ArrayList<Integer>(500);

    public DataSink() {
    }

    public void addDataRow(Vector<ArrayList<String>> row) {
        Vector<ArrayList<String>> newRow;
        newRow = new Vector<ArrayList<String>>(this.nCols);
        int k = 0;
        while (k < this.nCols) {
            newRow.add(k, new ArrayList<String>(10));
            ArrayList<String> pList = newRow.get(k);
            ArrayList<String> pGiven = row.get(k);
            int m = 0;
            while (m < pGiven.size()) {
                pList.add(pGiven.get(m));
                m++;
            }
            k++;
        }
        this.dataLines.add(newRow);
        int nLines = 1;
        int i = 0;
        while (i < this.nCols) {
            ArrayList<String> thisList = row.get(i);
            int thisSize = thisList.size();
            if (thisSize > nLines) nLines = thisSize;
            i++;
        }
        this.dataRowLineCounts.add(new Integer(nLines));
    }

    public int getRowCount() {
        return this.dataLines.size();
    }

    public ArrayList getdataLines() {
        return dataLines;
    }

    public void setColumnCount(int nCount) {
        this.nCols = nCount;
        setColumnSize(nCount);
    }

    public int getColumnCount() {
        return this.nCols;
    }

    private void setColumnSize(int nColSize) {
        this.ColumnNames = new String[nColSize];
        this.nCols = nColSize;
    }

    public void setColumnName(int nCol, String strName) {
        ColumnNames[nCol] = strName;
    }

    public String getColumnName(int nCol) {
        return ColumnNames[nCol];
    }

    public int getRowLineCount(int nRow) {
        return ((Integer) dataRowLineCounts.get(nRow)).intValue();
    }

    public Object[] getDataLine(int nLine) {
        if (nLine >= 0 && nLine < dataLines.size()) {
            Vector<ArrayList<String>> pArray = dataLines.get(nLine);
            return pArray.toArray();
        }
        return null;
    }

    public int getColumnNumber(String strColName) {
        int nRet = -1;
        String sTarget = strColName.toLowerCase();
        int i = 0;
        while (i < nCols) {
            String sCol = this.ColumnNames[i].toLowerCase();
            if (sCol.equals(sTarget)) return i;
            i++;
        }
        return nRet;
    }
}
