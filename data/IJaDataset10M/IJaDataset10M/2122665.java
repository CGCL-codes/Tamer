package com.yerihyo.yeritools.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import com.yerihyo.yeritools.collections.AlphaNumericComparator;
import com.yerihyo.yeritools.collections.CollectionsToolkit;
import com.yerihyo.yeritools.sql.SQLToolkit;
import com.yerihyo.yeritools.sql.TableConnection;

/**
 * 
 * @author __USER__
 */
public class ColumnColorMapper extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;

    private ColorMapperUI colorMapperUI = new ColorMapperUI();

    private TableConnection tableConnection;

    public static class ColumnColorMap {

        private String columnName;

        private SortedMap<String, Color> colorMap = null;

        public ColumnColorMap(String columnName) {
            this.setColumnName(columnName);
        }

        public ColumnColorMap(String columnName, Map<? extends String, ? extends Color> colorMap) {
            this.setColumnName(columnName);
            this.setColorMap(colorMap);
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public SortedMap<String, Color> getColorMap() {
            return colorMap;
        }

        public void setColorMap(Map<? extends String, ? extends Color> colorMap) {
            if (colorMap == null) {
                this.colorMap = null;
            }
            this.colorMap = new TreeMap<String, Color>();
            this.colorMap.putAll(colorMap);
        }

        public Collection<Color> getColorCollection() {
            SortedMap<String, Color> colorMap = this.getColorMap();
            if (colorMap == null) {
                return null;
            }
            return colorMap.values();
        }

        public int size() {
            return this.getColorMap().size();
        }

        public TimeSeries[] createEmptyTimeSeriesArray() {
            Map<String, Color> colorMap = this.getColorMap();
            TimeSeries[] timeSeriesArray = new TimeSeries[colorMap.size()];
            Iterator<String> iterator = colorMap.keySet().iterator();
            for (int i = 0; iterator.hasNext(); i++) {
                timeSeriesArray[i] = new TimeSeries(iterator.next(), Minute.class);
            }
            return timeSeriesArray;
        }

        public int indexOf(String columnColorName) {
            Map<String, Color> colorMap = this.getColorMap();
            Iterator<String> iterator = colorMap.keySet().iterator();
            for (int i = 0; iterator.hasNext(); i++) {
                String columnName = iterator.next();
                if (columnName.equals(columnColorName)) {
                    return i;
                }
            }
            return -1;
        }
    }

    ;

    public ColumnColorMap getDBColumnColorMap() {
        String columnName = this.getSelectedColumn();
        Map<String, Color> colorMap = this.getColorMap();
        ColumnColorMap dbColumnColorMap = new ColumnColorMap(columnName, colorMap);
        return dbColumnColorMap;
    }

    public void setDBColumnColorMap(ColumnColorMap dbColumnColorMap) {
        if (dbColumnColorMap == null) {
            return;
        }
        String columnName = dbColumnColorMap.getColumnName();
        this.setSelectedColumn(columnName);
        Map<String, Color> colorMap = dbColumnColorMap.getColorMap();
        this.setColorMap(colorMap);
    }

    /** Creates new form DBColumnColorMapper */
    public ColumnColorMapper(TableConnection tableConnection) {
        initComponents();
        this.tableConnection = tableConnection;
        yeriInit(tableConnection);
    }

    private void yeriInit(TableConnection tableConnection) {
        Component component = colorMapperUI;
        this.colorMapperScrollPane.setViewportView(component);
        List<String> columnNameList = null;
        try {
            columnNameList = SQLToolkit.getColumnNames(tableConnection);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        Collections.sort(columnNameList, new AlphaNumericComparator());
        for (String columnName : columnNameList) {
            this.columnNameComboBox.addItem(columnName);
        }
        this.columnNameComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                ColumnColorMapper dbColumnColorMapper = ColumnColorMapper.this;
                dbColumnColorMapper.columnSelectionChanged();
            }
        });
        if (this.columnNameComboBox.getItemCount() > 0) {
            this.columnNameComboBox.setSelectedIndex(0);
        }
    }

    public void setSelectedColumn(String columName) {
        this.columnNameComboBox.setSelectedItem(columName);
    }

    public String getSelectedColumn() {
        Object selectedColumn = this.columnNameComboBox.getSelectedItem();
        return (String) selectedColumn;
    }

    protected void columnSelectionChanged() {
        String columnName = getSelectedColumn();
        List<String> distinctValues = null;
        try {
            distinctValues = SQLToolkit.getDistinctStrings(tableConnection, columnName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.colorMapperUI.setNames(distinctValues.toArray(new String[0]));
    }

    public void setColorMap(Map<? extends String, ? extends Color> map) {
        this.colorMapperUI.setDataMap(map);
    }

    public Map<String, Color> getColorMap() {
        ColorLabelConfigPanel clcp = this.colorMapperUI.getColorLabelConfigPanel();
        return CollectionsToolkit.createHashMap(clcp.getLabelArray(), clcp.getColorArray());
    }

    public static void main(String[] args) {
        try {
            excelRun();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    private static void excelRun() throws SQLException {
        File testFolder = new File("C:/yeri/work/courses/200807/CarolynPensteinRose/visualization_DoctorPatient/");
        File file = new File(testFolder, "Combine_LIWC_basicchartdata.xls");
        String dbURLString = SQLToolkit.getExcelURL(file);
        Connection con = DriverManager.getConnection(dbURLString);
        String firstTableName = SQLToolkit.getTableNames(con).get(0);
        TableConnection tableConnection = new TableConnection(con, firstTableName);
        ColumnColorMapper dbColumnColorMapper = new ColumnColorMapper(tableConnection);
        SimpleOKDialog.show(null, "Test", dbColumnColorMapper, new Dimension(400, 800));
        System.out.println(dbColumnColorMapper.getSelectedColumn());
        Map<String, Color> resultMap = dbColumnColorMapper.getColorMap();
        System.out.println(CollectionsToolkit.mapToString(resultMap));
    }

    private void initComponents() {
        columnNameComboBox = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        colorMapperScrollPane = new javax.swing.JScrollPane();
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setText("Column:");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(colorMapperScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(columnNameComboBox, 0, 133, Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(columnNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(colorMapperScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE).addContainerGap()));
    }

    private javax.swing.JScrollPane colorMapperScrollPane;

    private javax.swing.JComboBox columnNameComboBox;

    private javax.swing.JLabel jLabel1;
}
