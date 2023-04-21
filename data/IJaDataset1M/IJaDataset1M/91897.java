package au.gov.naa.digipres.dpr.ui.viewdata;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import au.gov.naa.digipres.dpr.core.Messages;
import au.gov.naa.digipres.dpr.model.quest.QuestAIP;
import au.gov.naa.digipres.dpr.model.reprocessingjob.ReprocessingDataObject;
import au.gov.naa.digipres.dpr.model.reprocessingjob.ReprocessingJob;
import au.gov.naa.digipres.dpr.model.transferjob.DataObject;
import au.gov.naa.digipres.dpr.util.DPRIcons;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Show a quest data object and all associated objects...
 * 
 * This includes the following:
 * <ul>
 * <li>Transfer Job</li>
 * <li>Transfer Job Data Object</li>
 * <li>Transfer Job AIPs</li>
 * <li>---Transfer Job Processing Records -> qf, pf, dr</li>
 * <li>---Transfer Job Data Object Processing Records -> qf, pf</li>
 * </ul>
 * 
 * It is expected that the processing records may have to be in another window
 * or some such, or this UI is going to get CLUTTERED.
 * 
 * 
 */
public class DataObjectDetailsFrame extends JFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private DataObject dataObject;

    private JButton transferJobQFRecordButton;

    private JButton transferJobPFRecordButton;

    private JLabel titleLabel;

    private DataObjectDetailsActions actions;

    public DataObjectDetailsFrame(DataObject dataObject) {
        super();
        setTitle(Messages.getString(DataObjectDetailsFrame.class, "QuestDataObjectDetailsFrame.DetailsPanelTitle"));
        this.dataObject = dataObject;
        actions = new DataObjectDetailsActions(this);
        initComponents();
        initLayout();
    }

    private void initComponents() {
        titleLabel = new JLabel(Messages.getString(this.getClass(), "QuestDataObjectDetailsFrame.Title"), DPRIcons.getIconByName("images/report48.png"), SwingConstants.LEFT);
        titleLabel.setFont(titleLabel.getFont().deriveFont(24.0f));
        transferJobQFRecordButton = new JButton(actions.getShowQFRecordsCommand());
        transferJobPFRecordButton = new JButton(actions.getShowPFRecordsCommand());
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        FormLayout layout = new FormLayout("fill:pref, 4dlu, fill:pref, 4dlu, fill:pref, 0dlu:grow", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        DataObjectInformationRenderer.writeDataObjectInformation(builder, dataObject.getTransferJob(), dataObject);
        builder.appendSeparator(Messages.getString(this.getClass(), "QuestDataObjectDetailsFrame.transferJobSectionTitle"));
        TransferJobInformationRenderer.writeTransferJobInformation(builder, dataObject.getTransferJob());
        builder.appendSeparator(Messages.getString(this.getClass(), "QuestDataObjectDetailsFrame.RecordsLabel"));
        builder.append(Messages.getString(this.getClass(), "QuestDataObjectDetailsFrame.showQFRecordsLabel"), transferJobQFRecordButton);
        builder.nextLine();
        builder.append(Messages.getString(this.getClass(), "QuestDataObjectDetailsFrame.showPFRecordsLabel"), transferJobPFRecordButton);
        builder.nextLine();
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.add(builder.getPanel(), BorderLayout.CENTER);
        contentPanel.add(titleLabel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public class AIPTableModel extends AbstractTableModel {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        public static final int AIP_ID_COLUMN = 0;

        public static final int STATUS_COLUMN = 1;

        public static final int TYPE_COLUMN = 2;

        public static final int IS_REPROCESSING_COLUMN = 3;

        private Class<?>[] columnClasses = { String.class, String.class, String.class, Boolean.class };

        private String[] columnNames = { Messages.getString(this.getClass(), "DataObectDetailsPanelAIPTableModel.AIPColumnName"), Messages.getString(this.getClass(), "DataObectDetailsPanelAIPTableModel.StatusColumnName"), Messages.getString(this.getClass(), "DataObectDetailsPanelAIPTableModel.TypeColumnName"), Messages.getString(this.getClass(), "DataObectDetailsPanelAIPTableModel.IsReprocessingColumnName") };

        private List<QuestAIP> resultsList;

        public AIPTableModel() {
            resultsList = new ArrayList<QuestAIP>();
        }

        public AIPTableModel(List<QuestAIP> results) {
            resultsList = results;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return resultsList.size();
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            QuestAIP aip = resultsList.get(rowIndex);
            if (aip != null) {
                switch(columnIndex) {
                    case AIP_ID_COLUMN:
                        return aip.getAipId();
                    case STATUS_COLUMN:
                        return aip.getStatus().getName();
                    case TYPE_COLUMN:
                        return aip.getType().getDescription();
                    case IS_REPROCESSING_COLUMN:
                        return new Boolean(aip.isReproccessedAIP());
                }
            }
            return null;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnClasses[columnIndex];
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        /**
		 * @param resultsList
		 *            The new value to set resultsList to.
		 */
        public void setResultsList(List<QuestAIP> resultsList) {
            this.resultsList = resultsList;
            fireTableDataChanged();
        }
    }

    public class ReprocessingJobTableModel extends AbstractTableModel {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        public static final int JOB_NUMBER_COLUMN = 0;

        public static final int STATUS_COLUMN = 1;

        public static final int CREATED_BY_COLUMN = 2;

        public static final int DATE_CREATED_COLUMN = 3;

        private Class<?>[] columnClasses = { String.class, String.class, String.class, Date.class };

        private String[] columnNames = { Messages.getString(DataObjectDetailsFrame.class, "DataObectDetailsPanelReprocessingJobTableModel.JobNumberColumn"), Messages.getString(DataObjectDetailsFrame.class, "DataObectDetailsPanelReprocessingJobTableModel.StatusColumn"), Messages.getString(DataObjectDetailsFrame.class, "DataObectDetailsPanelReprocessingJobTableModel.CreatedByColumn"), Messages.getString(DataObjectDetailsFrame.class, "DataObectDetailsPanelReprocessingJobTableModel.DateColumn") };

        private List<ReprocessingDataObject> reprocessingDataObjectJobList = new ArrayList<ReprocessingDataObject>();

        public ReprocessingJobTableModel(List<ReprocessingDataObject> reprocessingDataObjectJobList) {
            this.reprocessingDataObjectJobList = reprocessingDataObjectJobList;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return reprocessingDataObjectJobList.size();
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            ReprocessingDataObject reprocessingDataObject = reprocessingDataObjectJobList.get(rowIndex);
            ReprocessingJob reprocessingJob = reprocessingDataObject.getReprocessingJob();
            if (reprocessingJob != null) {
                switch(columnIndex) {
                    case JOB_NUMBER_COLUMN:
                        return reprocessingJob.getJobNumber().getFullJobNumberString();
                    case STATUS_COLUMN:
                        return reprocessingJob.getJobStatus().toString();
                    case CREATED_BY_COLUMN:
                        return reprocessingJob.getCreatedBy().getUserName();
                    case DATE_CREATED_COLUMN:
                        return reprocessingJob.getDateCreated();
                }
            }
            return null;
        }

        @Override
        public Class<?> getColumnClass(int column) {
            return columnClasses[column];
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        public ReprocessingDataObject getReprocessingDataObjectByRow(int rowIndex) {
            return reprocessingDataObjectJobList.get(rowIndex);
        }

        public void setReprocessingDataObjectJobList(List<ReprocessingDataObject> reprocessingDataObjectJobList) {
            this.reprocessingDataObjectJobList = reprocessingDataObjectJobList;
        }

        public List<ReprocessingDataObject> getReprocessingDataObjectJobList() {
            return reprocessingDataObjectJobList;
        }
    }

    public DataObject getDataObject() {
        return dataObject;
    }

    public void setDataObject(DataObject dataObject) {
        this.dataObject = dataObject;
    }
}
