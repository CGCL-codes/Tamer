package perun.client.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.rmi.RemoteException;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import perun.client.Viewer;
import perun.client.util.GBCreator;
import perun.common.log.Log;
import perun.isle.IsleRI;
import perun.isle.PopulationRI;
import perun.isle.UpdateStatistics;
import perun.isle.task.Task;
import perun.isle.task.TaskRegistryRI;

/**
 * This component displays bunch of isle specific information. The design of this
 * component is quite straightforward and the only thing worth mentioning is the
 * performed tasks table. It is active and upon selecting a row in this table the
 * corresponding task's solvers are indicated in the islespace map.
 */
public class IsleInformationViewer extends JPanel implements Viewer {

    private IsleRI isle;

    private UpdateStatistics stats;

    private IsleViewer parentViewer = null;

    private final IsleInfoViewer isleInfo;

    private final IsleStatsViewer isleStats;

    private final JTable tasksTable;

    /**
	 * Creates new standalone IsleInformationViewer panel.
	 * @param isle Reference to the isle whose contents should be displayed
	 */
    public IsleInformationViewer(IsleRI isle) {
        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new GridBagLayout());
        isleInfo = new IsleInfoViewer(this);
        isleStats = new IsleStatsViewer(this);
        tasksTable = new JTable() {

            public void valueChanged(ListSelectionEvent e) {
                super.valueChanged(e);
                if (parentViewer != null) parentViewer.indicateTask((String) getValueAt(getSelectedRow(), 0));
            }
        };
        tasksTable.setModel(taskListTM);
        JPanel panelTasks = new JPanel();
        panelTasks.setLayout(new GridLayout());
        panelTasks.setBorder(BorderFactory.createTitledBorder("Tasks"));
        panelTasks.add(new JScrollPane(tasksTable));
        add(isleInfo, new GBCreator(0, 0).fill(GridBagConstraints.HORIZONTAL).gb);
        add(isleStats, new GBCreator(0, 1).fill(GridBagConstraints.HORIZONTAL).gb);
        add(panelTasks, new GBCreator(0, 2).fill().weight(1, 1).gb);
        this.isle = isle;
    }

    /**
	 * Creates new IsleInformationViewer panel that is part of an IsleViewer.
	 * In this case the performed tasks table tries to indicate the selected
	 * task in the IsleViewer's map components.
	 * @param isle Reference to the isle whose contents should be displayed
	 * @param parentViewer Reference to the parent window
	 */
    public IsleInformationViewer(IsleRI isle, IsleViewer parentViewer) {
        this(isle);
        this.parentViewer = parentViewer;
    }

    /**
	 * Changes the isle whose contents are displayed.
	 * @param isle Reference to the new isle
	 */
    public void setIsle(IsleRI isle) {
        this.isle = isle;
        refreshAll();
    }

    private Task[] tasks = null;

    private final TableModel taskListTM = new AbstractTableModel() {

        private final String taskColumns[][] = { { "Task class", TaskRegistryRI.INFORMATION_TYPE }, { "Enabled", TaskRegistryRI.INFORMATION_ENABLED }, { "Counter", TaskRegistryRI.INFORMATION_COUNTER } };

        private final Class[] types = new Class[] { Object.class, Boolean.class, Integer.class };

        public Class getColumnClass(int columnIndex) {
            return types[columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public int getColumnCount() {
            return taskColumns.length;
        }

        public String getColumnName(int column) {
            return taskColumns[column][0];
        }

        public int getRowCount() {
            return (tasks == null) ? 0 : tasks.length;
        }

        public Object getValueAt(int row, int col) {
            try {
                String inf = tasks[row].getInformation(taskColumns[col][1]);
                if (col != 1) return inf;
                return Boolean.valueOf(inf);
            } catch (Exception e) {
                return null;
            }
        }
    };

    public UpdateStatistics getStats() {
        return stats;
    }

    public IsleRI getIsle() {
        return isle;
    }

    /**
	 * This method refreshes the viewer's state according to the changes in the population.
	 */
    public void refresh() {
        if (isle == null) {
            clearAll();
            return;
        }
        try {
            PopulationRI population = isle.getPopulationRI();
            stats = population.getStatisticsRI();
            isleInfo.refresh();
            isleStats.refresh();
            tasks = isle.getTaskRegistry().getAllTasks();
            tasksTable.revalidate();
            tasksTable.repaint();
        } catch (RemoteException re) {
            Log.exception(Log.INFO, "Exception while updating statistics", re);
        }
    }

    private void clearAll() {
        isleInfo.clear();
        isleStats.clear();
    }

    /**
	 * This method refreshes the viewer's state according to the changes in the population.
	 * It also forces the viewer to refresh all the locally cached components of the isle.
	 */
    public void refreshAll() {
        refresh();
    }
}
