package net.maizegenetics.reports;

/**
 * Title:        TASSEL
 * Description:  A java program to deal with diversity
 * Copyright:    Copyright (c) 2000
 * Company:      USDA-ARS/NCSU
 * @author Ed Buckler
 * @version 1.0
 */
public interface TableReport {

    public Object[] getTableColumnNames();

    public Object[][] getTableData();

    public String getTableTitle();
}
