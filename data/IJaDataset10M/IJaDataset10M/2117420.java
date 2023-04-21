package com.ivis.xprocess.ui.tables.columns.definition.taskplanner;

public interface IRefresh {

    public void refreshASync();

    public void recreate();

    public void setDirty(boolean dirty);

    public void startedEditing(boolean started);

    public void changeToColumn(String columnName, int row);

    public void createInternalChangeRecord(String propertyName);
}
