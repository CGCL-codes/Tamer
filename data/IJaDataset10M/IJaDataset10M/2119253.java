package com.teletalk.jadmin.gui.logv2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import com.teletalk.jadmin.gui.util.GuiUtils;
import com.teletalk.jadmin.gui.util.ResourceHandler;

/**
 * This class implements a table for displaying log messages.
 * 
 * @author Tobias L�fstrand
 * 
 * @since Alpha
 */
public final class LogTable extends JPanel implements MouseListener {

    /**
    * 
    */
    private static final long serialVersionUID = 1L;

    private boolean is211OrLater;

    private final JTable table;

    private final JScrollPane scrollPane;

    private final LogTableModel model;

    private LoggingEventDialog logMessageDialog = null;

    public boolean enabled = false;

    /** 
	 * Creates a new LogTable.
	 */
    public LogTable(boolean is211OrLater) {
        super(new BorderLayout());
        this.is211OrLater = is211OrLater;
        setBorder(GuiUtils.createTitleCompoundBevelBorder(""));
        model = new LogTableModel(is211OrLater);
        table = new JTable(model);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        LogLevelCellRenderer cell0Renderer = new LogLevelCellRenderer();
        table.setDefaultRenderer(table.getColumnClass(0), cell0Renderer);
        initColumnSizes();
        scrollPane = new JScrollPane(table);
        add(BorderLayout.CENTER, scrollPane);
        table.addMouseListener(this);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    }

    /**
	 * Initializes the column sizes of the table.
	 */
    private void initColumnSizes() {
        TableColumn column = null;
        for (int i = 0; i < 4; i++) {
            column = table.getColumnModel().getColumn(i);
            if (i == 0) column.setPreferredWidth(7); else if (i == 1) column.setPreferredWidth(100); else column.setPreferredWidth(200);
        }
    }

    /**
    */
    public boolean is211OrLater() {
        return is211OrLater;
    }

    /**
	 * Destroys this table.
	 */
    public void destroy() {
        try {
            if (logMessageDialog != null) {
                logMessageDialog.dispose();
                logMessageDialog = null;
            }
        } catch (Exception e) {
        }
    }

    /**
	 * Sets the title of this table.
	 */
    public void setTitle(String newTitle) {
        setBorder(GuiUtils.createTitleCompoundBevelBorder(" " + newTitle + " "));
    }

    /**
	 * Called when a new log message is received.
	 */
    public void newLoggingEvent(LoggingEvent loggingEvent) {
        synchronized (this.model) {
            model.addLoggingEvent(loggingEvent);
        }
    }

    /**
	 * Clears the table.
	 */
    public void clear() {
        synchronized (this.model) {
            model.setData(null);
        }
        validate();
    }

    /**
	 * Fills the table with the log messages in the specified list.
	 */
    public void setData(final java.util.List log) {
        synchronized (this.model) {
            model.setData(log);
        }
        validate();
    }

    /**
    */
    public void trimFirst(int trimCount) {
        synchronized (this.model) {
            model.trimFirst(trimCount);
        }
        validate();
    }

    /**
    * Gets the number of rows in the table.
    */
    public int getRowCount() {
        synchronized (this.model) {
            return this.model.getRowCount();
        }
    }

    /**
    * 
    * @return
    */
    public LoggingEvent getNextLoggingEvent() {
        synchronized (this.model) {
            int row = table.getSelectedRow();
            if (row > -1) {
                row++;
                if (row >= this.model.getRowCount()) row = 0;
                table.getSelectionModel().setSelectionInterval(row, row);
                return this.model.getLoggingEvent(row);
            }
        }
        return null;
    }

    /**
    * 
    * @return
    */
    public LoggingEvent getPreviousLoggingEvent() {
        synchronized (this.model) {
            int row = table.getSelectedRow();
            if (row > -1) {
                row--;
                if (row < 0) row = this.model.getRowCount() - 1;
                table.getSelectionModel().setSelectionInterval(row, row);
                return this.model.getLoggingEvent(row);
            }
        }
        return null;
    }

    /**
	 * Mouse click event handler method.
	 */
    public void mouseClicked(final MouseEvent e) {
        if (e.getClickCount() == 2) {
            LoggingEvent logMsg = null;
            synchronized (this.model) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    logMsg = (LoggingEvent) model.getValueAt(row, 0);
                }
            }
            if (logMsg != null) {
                Frame owner = JFrame.getFrames()[0];
                try {
                    if (logMessageDialog != null) {
                        logMessageDialog.dispose();
                        logMessageDialog = null;
                    }
                } catch (Exception ex) {
                }
                logMessageDialog = new LoggingEventDialog(owner, this, logMsg);
                Dimension dlgSize = logMessageDialog.getPreferredSize();
                Dimension frmSize = owner.getSize();
                Point loc = owner.getLocation();
                logMessageDialog.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
                logMessageDialog.setSize(dlgSize);
                logMessageDialog.setVisible(true);
                logMessageDialog = null;
            }
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    /**
	 * The table model that is used for the log messages table.
	 */
    static final class LogTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 1L;

        public static final String[] columnNames = { "Level", "Date", "Origin", "Message" };

        private java.util.List data = null;

        private SimpleDateFormat dateFormat;

        private boolean is211OrLater;

        /**
		 * Creates a new LogTableModel.
		 */
        public LogTableModel(boolean is211OrLater) {
            super();
            this.is211OrLater = is211OrLater;
            data = new ArrayList();
            dateFormat = (SimpleDateFormat) DateFormat.getDateTimeInstance();
            dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss.SSS");
        }

        /**
		 * Sets the data of the model.
		 */
        public void setData(final java.util.List data) {
            this.data.clear();
            if (data != null) this.data.addAll(data);
            fireTableDataChanged();
        }

        /**
       */
        public LoggingEvent getLoggingEvent(int row) {
            if (row < this.data.size()) {
                return (LoggingEvent) this.data.get(row);
            } else return null;
        }

        /**
       */
        public void trimFirst(int trimCount) {
            for (int i = 0; i < trimCount; i++) this.data.remove(i);
            fireTableDataChanged();
        }

        /**
		 * Adds a log message to the data in the model.
		 */
        public void addLoggingEvent(LoggingEvent loggingEvent) {
            data.add(loggingEvent);
            fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
        }

        /**
		 * Gets the number of columns in the table.
		 */
        public int getColumnCount() {
            return columnNames.length;
        }

        /**
		 * Gets the number of rows in the table.
		 */
        public int getRowCount() {
            if (data != null) return data.size(); else return 0;
        }

        /**
	     * Gets the name of the column with the specified index.
	     */
        public String getColumnName(final int col) {
            return columnNames[col];
        }

        /**
	     * Gets the value in the specified cell.
	     */
        public Object getValueAt(final int row, final int col) {
            try {
                if (row < data.size()) {
                    LoggingEvent loggingEvent = (LoggingEvent) data.get(row);
                    if (col == 0) return loggingEvent; else if (col == 1) return dateFormat.format(new Date(loggingEvent.timeStamp)); else if (col == 2) {
                        if ((loggingEvent.getLoggerName() != null) && this.is211OrLater) return loggingEvent.getLoggerName(); else return loggingEvent.getThreadName();
                    } else if (col == 3) return loggingEvent.getRenderedMessage();
                }
                return null;
            } catch (ArrayIndexOutOfBoundsException e) {
                return null;
            }
        }
    }

    /**
	 * The class responsible for rendering the cells of the log message table.
	 */
    static final class LogLevelCellRenderer extends DefaultTableCellRenderer {

        /**
       * 
       */
        private static final long serialVersionUID = 1L;

        private final String prefix = "/images/loglevel/";

        /**
		 * Creates a new LogLevelCellRenderer.
		 */
        public LogLevelCellRenderer() {
            super();
        }

        /**
		 * Gets the component for rendering the cell at the specified row and column.
		 */
        public Component getTableCellRendererComponent(final JTable table, Object value, final boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof LoggingEvent) {
                LoggingEvent loggingEvent = (LoggingEvent) value;
                JLabel label = new JLabel() {

                    private static final long serialVersionUID = 1L;

                    public void paint(Graphics g) {
                        Color bColor = table.getSelectionBackground();
                        if (isSelected) {
                            g.setColor(bColor);
                            g.fillRect(0, 0, getWidth(), getHeight());
                        }
                        super.paint(g);
                    }
                };
                label.setHorizontalAlignment(SwingConstants.CENTER);
                try {
                    if (loggingEvent.getLevel().toInt() == Level.DEBUG_INT) label.setIcon(ResourceHandler.getImageIcon(prefix + "debug.gif")); else if (loggingEvent.getLevel().toInt() == Level.INFO_INT) label.setIcon(ResourceHandler.getImageIcon(prefix + "info.gif")); else if (loggingEvent.getLevel().toInt() == Level.WARN_INT) label.setIcon(ResourceHandler.getImageIcon(prefix + "warning.gif")); else if (loggingEvent.getLevel().toInt() == Level.ERROR_INT) label.setIcon(ResourceHandler.getImageIcon(prefix + "error.gif")); else if (loggingEvent.getLevel().toInt() == Level.FATAL_INT) label.setIcon(ResourceHandler.getImageIcon(prefix + "critical.gif")); else label.setText(loggingEvent.getLevel().toString());
                } catch (Exception e) {
                    label.setText(loggingEvent.getLevel().toString());
                }
                label.setToolTipText("Loglevel " + loggingEvent.getLevel().toString());
                return label;
            } else {
                return super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
            }
        }
    }
}
