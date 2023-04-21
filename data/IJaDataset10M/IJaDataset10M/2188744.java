package org.rjam.gui.api;

import java.util.Date;
import org.rjam.gui.ISelector;
import org.rjam.gui.beans.Row;

public interface IRowUpdateListener {

    public void clear();

    public void addRow(Row row, String[] order);

    public void updateEndDate(Date endDate);

    public void updateView();

    public void setMaxItemCount(int maxItemCount);

    public int getMaxItemCount();

    public void setMaxAge(long age, ISelector selector);

    public long getMaxAge();
}
