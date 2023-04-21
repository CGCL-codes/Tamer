package org.rjam.gui.api;

import java.io.IOException;
import java.util.List;
import org.rjam.alert.AlertGroup;
import org.rjam.gui.ISelector;
import org.rjam.gui.beans.Row;
import org.rjam.report.ExportManager;
import org.rjam.xml.Token;
import com.sun.image.codec.jpeg.ImageFormatException;

/**
 * Represents an Analysis (provides a view into data)
 * Contains three primay parts;
 * 
 * 1> Sql 
 * 2> Data Table
 * 3> Chart
 * 
 * @author Tony Bringardner
 *
 */
public interface IReport extends IRowUpdateListener {

    /**
	 * Set the 'Chart' as the selected component.
	 *
	 */
    public String getName();

    public String getDescription();

    public void setBusy(boolean b);

    public String getQueryType();

    public Token export(ExportManager manager) throws ImageFormatException, IOException;

    public void setSelector(ISelector selector);

    public ISelector getSelector();

    public boolean isBusy();

    public void addStats(String string);

    public String getStatsAsText();

    public String getAddedWhereClause(boolean isRolled);

    public void removeAged();

    /**
	 * @return the fields supported by the IReport (for reports and alerts)
	 */
    public String[] getFields();

    public void setDataManager(IDataManager dm);

    public IDataManager getDataManager();

    public List<AlertGroup> getAlertGroups();

    public void addAlertGroup(AlertGroup alert);

    public AlertGroup removeAlertGroup(String name);

    public int getRowCount();

    public void setAlertGroups(List<AlertGroup> list);

    public void evaluateAlerts(Row row);
}
