package net.sf.dpdesktop.gui;

import com.google.inject.Inject;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import net.sf.dpdesktop.gui.util.DateTableCellRenderer;
import net.sf.dpdesktop.gui.util.EasyTable;
import net.sf.dpdesktop.gui.util.LogItemTableCellRenderer;
import net.sf.dpdesktop.gui.util.TimeTableCellRenderer;
import net.sf.dpdesktop.module.settings.LanguageModel;
import net.sf.dpdesktop.module.settings.LocaleListener;
import net.sf.dpdesktop.module.tracking.TrackingComponent;
import net.sf.dpdesktop.service.container.Container;
import net.sf.dpdesktop.service.log.LogItem;
import net.sf.dpdesktop.module.tracking.TrackingListener;
import net.sf.dpdesktop.service.log.LogItemRepository;
import net.sf.dpdesktop.service.log.LogItemRepositoryListener;
import org.jdesktop.swingx.table.TableColumnExt;

/**
 *
 * @author Heiner Reinhardt
 */
public class HistoryPane extends javax.swing.JPanel implements TrackingComponent, LocaleListener, LogItemRepositoryListener {

    private final DefaultTableModel model;

    private boolean isBillableTimeFeatureEnabled = false;

    private TableColumnExt logItemColumn;

    private final LanguageModel languageModel;

    private final LogItemRepository logItemRepository;

    @Override
    public void updateLocale(LanguageModel languageModel) {
        for (ColumnEnum head : ColumnEnum.values()) {
            table.getColumnExt(head).setHeaderValue(head.getTitle(languageModel));
        }
        table.setToolTipText(null);
        this.listChanged(logItemRepository.getList());
    }

    public enum ColumnEnum {

        DATE {

            @Override
            public String getTitle(LanguageModel languageModel) {
                return languageModel.getString("HistoryPane.table.column.header.date");
            }

            @Override
            public Object getValue(LanguageModel languageModel, LogItem logItem) {
                return logItem.get("date", languageModel.getString("HistoryPane.table.unknownValue"));
            }
        }
        , OBJECTIVE {

            @Override
            public String getTitle(LanguageModel languageModel) {
                return languageModel.getString("HistoryPane.table.column.header.objective");
            }

            @Override
            public Object getValue(LanguageModel languageModel, LogItem logItem) {
                return logItem;
            }
        }
        , SUMMARY {

            @Override
            public String getTitle(LanguageModel languageModel) {
                return languageModel.getString("HistoryPane.table.column.header.summary");
            }

            @Override
            public Object getValue(LanguageModel languageModel, LogItem logItem) {
                return logItem.get("summary", languageModel.getString("HistoryPane.table.unknownValue"));
            }
        }
        , WORKED_TIME {

            @Override
            public String getTitle(LanguageModel languageModel) {
                return languageModel.getString("HistoryPane.table.column.header.workedTime");
            }

            @Override
            public Object getValue(LanguageModel languageModel, LogItem logItem) {
                return logItem.get("workedTime", languageModel.getString("HistoryPane.table.unknownValue"));
            }
        }
        , BILLABLE_TIME {

            @Override
            public String getTitle(LanguageModel languageModel) {
                return languageModel.getString("HistoryPane.table.column.header.billableTime");
            }

            @Override
            public Object getValue(LanguageModel languageModel, LogItem logItem) {
                return logItem.get("billableTime", languageModel.getString("HistoryPane.table.unknownValue"));
            }
        }
        , COMMENT {

            @Override
            public String getTitle(LanguageModel languageModel) {
                return languageModel.getString("HistoryPane.table.column.header.comment");
            }

            @Override
            public Object getValue(LanguageModel languageModel, LogItem logItem) {
                return logItem.get("comment", languageModel.getString("HistoryPane.table.unknownValue"));
            }
        }
        ;

        public abstract String getTitle(LanguageModel languageModel);

        public abstract Object getValue(LanguageModel languageModel, LogItem logItem);
    }

    @Override
    public void addTrackingListener(final TrackingListener trackingListener) {
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    int row = table.getSelectedRow();
                    int col = 1;
                    if (row >= 0) {
                        trackingListener.containerChanged(((LogItem) table.getValueAt(row, col)).getContainer());
                    }
                }
            }
        });
    }

    /** Creates new form HistoryPane */
    @Inject
    public HistoryPane(LogItemRepository logItemRepository, LanguageModel myLanguageModel) {
        this.logItemRepository = logItemRepository;
        this.languageModel = myLanguageModel;
        initComponents();
        model = new DefaultTableModel();
        for (ColumnEnum head : ColumnEnum.values()) {
            model.addColumn(head);
        }
        this.table.setModel(model);
        for (ColumnEnum head : ColumnEnum.values()) {
            table.getColumnExt(head.toString()).setIdentifier(head);
        }
        languageModel.addLocaleListener(this);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (logItemColumn != null) {
                    EasyTable easyTable = new EasyTable();
                    int row = table.getSelectedRow();
                    int col = logItemColumn.getModelIndex();
                    if (row < 0) {
                        return;
                    }
                    LogItem item = (LogItem) table.getValueAt(row, col);
                    easyTable.addRow(ColumnEnum.OBJECTIVE.getTitle(languageModel), item.getContainer().getName());
                    easyTable.addRow(ColumnEnum.DATE.getTitle(languageModel), item.get("date", languageModel.getString("HistoryPane.table.unknownValue")));
                    easyTable.addRow(ColumnEnum.SUMMARY.getTitle(languageModel), item.get("summary", languageModel.getString("HistoryPane.table.unknownValue")));
                    easyTable.addRow(ColumnEnum.COMMENT.getTitle(languageModel), item.get("comment", languageModel.getString("HistoryPane.table.unknownValue")));
                    easyTable.addRow(ColumnEnum.WORKED_TIME.getTitle(languageModel), item.get("workedTime", languageModel.getString("HistoryPane.table.unknownValue")));
                    if (isBillableTimeFeatureEnabled) {
                        easyTable.addRow(ColumnEnum.BILLABLE_TIME.getTitle(languageModel), item.get("billableTime", languageModel.getString("HistoryPane.table.unknownValue")));
                    }
                    easyTable.addAnnotation(languageModel.getString("HistoryPane.table.tooltip.annotation0.text"));
                    table.setToolTipText(easyTable.toString());
                }
            }
        });
        logItemRepository.addLogItemRepositoryListener(this);
        table.getColumnExt(ColumnEnum.BILLABLE_TIME).setVisible(false);
        table.getColumnExt(ColumnEnum.COMMENT).setVisible(false);
    }

    public void listChanged(List<LogItem> logList) {
        for (int i = 0; i < table.getRowCount(); ) {
            model.removeRow(0);
        }
        for (LogItem logItem : logList) {
            Vector vector = new Vector();
            for (ColumnEnum columnEnum : ColumnEnum.values()) {
                vector.addElement(columnEnum.getValue(languageModel, logItem));
            }
            model.addRow(vector);
        }
        table.getColumnExt(ColumnEnum.BILLABLE_TIME).setCellRenderer(new TimeTableCellRenderer());
        table.getColumnExt(ColumnEnum.WORKED_TIME).setCellRenderer(new TimeTableCellRenderer());
        table.getColumnExt(ColumnEnum.OBJECTIVE).setCellRenderer(new LogItemTableCellRenderer());
        table.getColumnExt(ColumnEnum.DATE).setCellRenderer(new DateTableCellRenderer(languageModel));
        table.setSortOrder(ColumnEnum.DATE, SortOrder.DESCENDING);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new org.jdesktop.swingx.JXTable();
        annotationLabel = new javax.swing.JLabel();
        setAutoscrolls(true);
        table.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4", "Title 5" }));
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setColumnControlVisible(true);
        table.setEditable(false);
        table.setRequestFocusEnabled(false);
        jScrollPane1.setViewportView(table);
        annotationLabel.setFont(annotationLabel.getFont().deriveFont(annotationLabel.getFont().getStyle() & ~java.awt.Font.BOLD, annotationLabel.getFont().getSize() - 1));
        annotationLabel.setText(languageModel.getString("HistoryPane.annotationLabel.text"));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(annotationLabel).addContainerGap(60, Short.MAX_VALUE)).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(annotationLabel)));
    }

    private javax.swing.JLabel annotationLabel;

    private javax.swing.JScrollPane jScrollPane1;

    private org.jdesktop.swingx.JXTable table;
}
