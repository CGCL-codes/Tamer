package com.loveazure.ui;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import com.loveazure.eo.UserCourse;

public class UserCourseModel extends AbstractTableModel {

    private Vector<UserCourse> data;

    private Vector<String> columnNames;

    public void addrow(UserCourse usercourse) {
        data.add(usercourse);
        fireTableDataChanged();
        System.out.println("invoke");
    }

    public UserCourseModel() {
        data = new Vector<UserCourse>();
        columnNames = new Vector<String>();
        columnNames.add("课程名称");
        columnNames.add("已学单词");
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        UserCourse rowdata = data.get(rowIndex);
        if (columnIndex == 0) {
            return rowdata.getCourse().getCourseName();
        } else {
            return rowdata.getWordLearned().size();
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    public void removeAll() {
        data.clear();
    }

    public UserCourse getRow(int rowIndex) {
        return data.get(rowIndex);
    }

    public void deleteRow(int selectedRow) {
        data.remove(selectedRow);
        fireTableDataChanged();
    }
}
