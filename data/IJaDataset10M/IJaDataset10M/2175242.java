package com.ecortex.emergent.api;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class EmergentColumn {

    private static final Pattern p = Pattern.compile("(.)([^\\[]+)(?:\\[([^\\]]*)\\](?:\\<([^\\>]*)\\>)?)?");

    private static final String DimSeparator = "[:,]";

    public String Name;

    public Class BaseClass;

    public Integer[] Indices;

    private EmergentColumn() {
        Name = null;
        BaseClass = null;
        Indices = null;
    }

    private EmergentColumn copy() {
        return copy(Indices);
    }

    private EmergentColumn copy(Integer[] NewIndices) {
        EmergentColumn NewColumn = new EmergentColumn();
        NewColumn.Name = Name;
        NewColumn.BaseClass = BaseClass;
        if (NewIndices != null) {
            NewColumn.Indices = new Integer[NewIndices.length];
            for (int i = 0; i < NewIndices.length; i++) NewColumn.Indices[i] = NewIndices[i];
        } else NewColumn.Indices = null;
        return NewColumn;
    }

    private Object createInstance(String Value) {
        return EmergentType.ConvertString(Value, BaseClass);
    }

    private Object createMatrixInstance() {
        Object Instance = null;
        if (Indices != null) {
            int[] intIndices = new int[Indices.length];
            for (int i = 0; i < intIndices.length; i++) intIndices[i] = Indices[i].intValue();
            Instance = Array.newInstance(BaseClass, intIndices);
        }
        return Instance;
    }

    private void setMatrixValue(Object Matrix, String Value) {
        Object[] FinalArray = getFinalArray(Matrix);
        FinalArray[Indices[Indices.length - 1]] = createInstance(Value);
    }

    private String getMatrixValue(Object Matrix) {
        Object[] FinalArray = getFinalArray(Matrix);
        return EmergentType.FormatObject(FinalArray[Indices[Indices.length - 1]]);
    }

    private String getValue(Object Field) {
        return EmergentType.FormatObject(Field);
    }

    private Object[] getFinalArray(Object Matrix) {
        Object CurrentObject = Matrix;
        for (int i = 0; i < Indices.length - 1; i++) CurrentObject = ((Object[]) CurrentObject)[Indices[i]];
        return (Object[]) CurrentObject;
    }

    public static Object[][] Lines2Table(ArrayList<String[]> Lines) {
        if (Lines.size() < 2) throw new EmergentException("Need at least a header row and one data row");
        EmergentColumn[] ExpandedColumns = Headers2Columns(Lines.get(0), true);
        EmergentColumn[] CompactColumns = Headers2Columns(Lines.get(0), false);
        Integer[] Map = EmergentColumn.GetColumnMap(ExpandedColumns, CompactColumns);
        Object[][] Table = new Object[Lines.size()][CompactColumns.length];
        Columns2Names(CompactColumns, Table[0]);
        for (int row = 1; row < Lines.size(); row++) {
            for (int destcol = 0; destcol < CompactColumns.length; destcol++) Table[row][destcol] = CompactColumns[destcol].createMatrixInstance();
            for (int col = 0; col < ExpandedColumns.length; col++) {
                if (ExpandedColumns[col].Indices != null) ExpandedColumns[col].setMatrixValue(Table[row][Map[col]], Lines.get(row)[col]); else Table[row][Map[col]] = ExpandedColumns[col].createInstance(Lines.get(row)[col]);
            }
        }
        return Table;
    }

    private static EmergentColumn[] Headers2Columns(String[] Headers, Boolean IsExpanded) throws EmergentException {
        ArrayList<EmergentColumn> Cols = new ArrayList<EmergentColumn>();
        for (int i = 0; i < Headers.length; i++) {
            EmergentColumn NewCol = ParseColumnHeader(Headers[i], IsExpanded);
            if (NewCol != null) Cols.add(NewCol);
        }
        return (EmergentColumn[]) Cols.toArray(new EmergentColumn[Cols.size()]);
    }

    private static void Columns2Names(EmergentColumn[] Compact, Object[] Names) {
        for (int i = 0; i < Compact.length; i++) Names[i] = Compact[i].Name;
    }

    private static EmergentColumn ParseColumnHeader(String Header, Boolean IsExpanded) throws EmergentException {
        Matcher m = p.matcher(Header);
        EmergentColumn c = null;
        if (m.matches() && (m.groupCount() == 4)) {
            String BaseClassString = m.group(1);
            String ColName = m.group(2);
            String ExpandedDims = m.group(3);
            String CompactDims = m.group(4);
            if (IsExpanded || (ExpandedDims == null) || (CompactDims != null)) {
                c = new EmergentColumn();
                c.Name = ColName;
                if (BaseClassString.equals("$")) c.BaseClass = String.class; else if (BaseClassString.equals("%")) c.BaseClass = Float.class; else if (BaseClassString.equals("#")) c.BaseClass = Double.class; else if (BaseClassString.equals("|")) c.BaseClass = Integer.class; else if (BaseClassString.equals("@")) c.BaseClass = Byte.class; else if (BaseClassString.equals("&")) c.BaseClass = Object.class; else c.BaseClass = Object.class;
                String DimString = null;
                String[] Dims = null;
                if (IsExpanded) DimString = ExpandedDims; else DimString = CompactDims;
                if (DimString != null) {
                    Integer NumDims = 0;
                    Integer i = 0;
                    try {
                        Dims = DimString.split(DimSeparator);
                        NumDims = Integer.parseInt(Dims[0]);
                        if (!NumDims.equals(Dims.length - 1)) throw new EmergentException("Array dimension does not match number of indices");
                    } catch (NumberFormatException e) {
                        throw new EmergentException("Invalid array dimension: " + Dims[0], e);
                    }
                    try {
                        c.Indices = new Integer[NumDims];
                        for (i = 0; i < NumDims; i++) {
                            c.Indices[i] = Integer.parseInt(Dims[i + 1]);
                        }
                    } catch (NumberFormatException e) {
                        throw new EmergentException("Invalid index: " + Dims[i + 1], e);
                    }
                }
            }
        } else throw new EmergentException("Column header " + Header + " is not valid.");
        return c;
    }

    public static ArrayList<String[]> Table2Lines(Object[][] TableData) {
        ArrayList<String[]> Lines = new ArrayList<String[]>();
        if (TableData.length < 2) throw new EmergentException("Need at least a header row and one data row");
        EmergentColumn[] CompactColumns = Object2Columns(TableData[0], TableData[1]);
        EmergentColumn[] ExpandedColumns = ExpandColumns(CompactColumns);
        Integer[] Map = GetColumnMap(ExpandedColumns, CompactColumns);
        Lines.add(Columns2Headers(ExpandedColumns, CompactColumns, Map));
        for (int row = 1; row < TableData.length; row++) {
            String[] NewRow = new String[ExpandedColumns.length];
            for (int col = 0; col < ExpandedColumns.length; col++) {
                if (ExpandedColumns[col].Indices != null) {
                    NewRow[col] = ExpandedColumns[col].getMatrixValue(TableData[row][Map[col]]);
                } else NewRow[col] = ExpandedColumns[col].getValue(TableData[row][Map[col]]);
            }
            Lines.add(NewRow);
        }
        return Lines;
    }

    private static EmergentColumn[] Object2Columns(Object[] ColumnNames, Object[] RowData) {
        EmergentColumn[] ColumnArray = new EmergentColumn[ColumnNames.length];
        for (int n = 0; n < ColumnArray.length; n++) ColumnArray[n] = new EmergentColumn();
        for (int i = 0; i < ColumnNames.length; i++) {
            Object ColData = RowData[i];
            ColumnArray[i].Name = (String) ColumnNames[i];
            ColumnArray[i].BaseClass = EmergentType.GetBaseClass(ColData);
            if (ColData.getClass().isArray()) {
                Integer NumDims = EmergentType.ArrayDimensions(ColData);
                ColumnArray[i].Indices = new Integer[NumDims];
                Object CurrentObject = ColData;
                for (int d = 0; d < NumDims; d++) {
                    ColumnArray[i].Indices[d] = ((Object[]) CurrentObject).length;
                    CurrentObject = ((Object[]) CurrentObject)[0];
                }
            } else ColumnArray[i].Indices = null;
        }
        return ColumnArray;
    }

    private static EmergentColumn[] ExpandColumns(EmergentColumn[] Compact) {
        ArrayList<EmergentColumn> Expanded = new ArrayList<EmergentColumn>();
        for (int i = 0; i < Compact.length; i++) {
            if (Compact[i].Indices != null) {
                Integer[] CurrentIndices = new Integer[Compact[i].Indices.length];
                RecursiveMatrixExpansion(Compact[i], CurrentIndices, 0, Expanded);
            } else Expanded.add(Compact[i].copy());
        }
        return (EmergentColumn[]) Expanded.toArray(new EmergentColumn[Expanded.size()]);
    }

    private static void RecursiveMatrixExpansion(EmergentColumn Compact, Integer[] CurrentIndices, Integer Dimension, ArrayList<EmergentColumn> Expanded) {
        Integer WorkingDimension = Compact.Indices.length - Dimension - 1;
        if (Dimension < Compact.Indices.length) {
            for (CurrentIndices[WorkingDimension] = 0; CurrentIndices[WorkingDimension] < Compact.Indices[WorkingDimension]; CurrentIndices[WorkingDimension]++) RecursiveMatrixExpansion(Compact, CurrentIndices, Dimension + 1, Expanded);
        } else Expanded.add(Compact.copy(CurrentIndices));
    }

    private static String[] Columns2Headers(EmergentColumn[] ExpandedColumns, EmergentColumn[] CompactColumns, Integer[] Map) {
        HashSet<String> h = new HashSet<String>();
        ArrayList<String> Headers = new ArrayList<String>();
        Headers.add("_H:");
        for (int i = 0; i < ExpandedColumns.length; i++) {
            if (ExpandedColumns[i].Indices != null) {
                Boolean Governing = h.add(ExpandedColumns[i].Name);
                Headers.add(ExpandedColumns[i].FormatMatrix(Governing, CompactColumns[Map[i]]));
            } else Headers.add(ExpandedColumns[i].Format());
        }
        return (String[]) Headers.toArray(new String[Headers.size()]);
    }

    private String FormatMatrix(Boolean Governing, EmergentColumn Compact) {
        String Result = Format();
        Result += "[" + FormatIndex() + "]";
        if (Governing) Result += "<" + Compact.FormatIndex() + ">";
        return Result;
    }

    private String FormatIndex() {
        String Result = Indices.length + ":";
        for (int i = 0; i < Indices.length; i++) {
            if (i > 0) Result += ",";
            Result += Indices[i];
        }
        return Result;
    }

    private String Format() {
        String Prefix = "&";
        if (BaseClass.equals(String.class)) Prefix = "$"; else if (BaseClass.equals(Integer.class)) Prefix = "|"; else if (BaseClass.equals(Byte.class)) Prefix = "@"; else if (BaseClass.equals(Float.class)) Prefix = "%"; else if (BaseClass.equals(Double.class)) Prefix = "#"; else if (BaseClass.equals(Object.class)) Prefix = "&";
        return Prefix + Name;
    }

    private static Integer[] GetColumnMap(EmergentColumn[] e, EmergentColumn[] c) {
        HashMap<String, Integer> h = new HashMap<String, Integer>();
        for (int i = 0; i < c.length; i++) h.put(c[i].Name, i);
        Integer[] Map = new Integer[e.length];
        for (int i = 0; i < e.length; i++) {
            Map[i] = h.get(e[i].Name);
            if (e[i].Indices != null) {
                if (e[i].Indices.length == c[Map[i]].Indices.length) {
                    for (int j = 0; j < e[i].Indices.length; j++) {
                        if (e[i].Indices[j] >= c[Map[i]].Indices[j]) {
                            throw new EmergentException("Array index out of bounds on column \"" + e[i].Name + "\"");
                        }
                    }
                } else throw new EmergentException("Dimensions of matrix column \"" + c[Map[i]].Name + "\" do not match data object \"" + e[i].Name + "\"");
            }
        }
        return Map;
    }
}
