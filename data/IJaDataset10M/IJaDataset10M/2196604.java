package at.portty.gui.Panel;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import at.portty.gui.ComboBox.WideComboBox;
import at.portty.gui.Dialogs.GUI_Input;
import at.portty.gui.Dialogs.TrackDataDialog;
import at.portty.gui.Main.Portty;
import at.portty.gui.Table.CenterRenderer;
import at.portty.gui.Table.DetailedModel;
import at.portty.gui.Table.PortTableModel;
import at.portty.gui.Table.ZebraJTable;
import at.portty.statistics.ByteWrapper;
import at.portty.statistics.ByteWrapper.BytesConv;
import at.portty.statistics.ListOfTranferredBytes;
import at.portty.tools.GlobalConstants;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class DetailedPanel extends JPanel {

    private final Portty jPortty;

    private JTextField txtDescription;

    private JTextField txtHost;

    private JTextField txtStatus;

    private JTextField txtSumTotal;

    private static JTextField txtTransferCur;

    private static JTextField txtTransferMax;

    public static WideComboBox cmbBytes;

    private JButton btnClear;

    private JButton btnTrack;

    public ZebraJTable tblDetails;

    private DetailedModel dm;

    private JLabel lblStatusIcon;

    private GUI_Input GUI_instance;

    static final String strToolTipThroughPut = "<html>Values are derived from <br><b>#1#</b> running instance(s).</html>";

    private static DetailedPanel pnlDetail;

    public DetailedPanel(Portty jPortGUI) {
        this.jPortty = jPortGUI;
        pnlDetail = this;
        initialize();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(new JScrollPane(buildLayout()));
        addActionListeners();
    }

    private void addActionListeners() {
        btnClear.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ListOfTranferredBytes.resetStatistics();
                dm.removeInactiveSession();
                dm.resetStatistics();
            }
        });
        cmbBytes.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                ByteWrapper.setCurrentUnit((BytesConv) cmbBytes.getSelectedItem());
                dm.fireTableDataChanged();
                sumTotal();
                updateThroughPut();
            }
        });
        btnTrack.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnTrack.setEnabled(false);
                new TrackDataDialog(jPortty);
            }
        });
    }

    private JPanel buildLayout() {
        FormLayout layout = new FormLayout("3dlu, f:p:g, 3dlu, p, 3dlu", "3dlu, p, 3dlu, b:p:g,3dlu, p, 3dlu");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        int startCol = 2;
        int rows = 2;
        int cols = startCol;
        cols = startCol;
        builder.add(getOverviewPanel(), cc.xy(cols, rows));
        cols++;
        cols++;
        builder.add(GUI_instance.getPortPanel(), cc.xy(cols, rows));
        cols = startCol;
        rows++;
        rows++;
        cols = startCol;
        builder.add(new JScrollPane(tblDetails), cc.xywh(cols, rows, 1, 3, "f, f"));
        cols++;
        cols++;
        builder.add(getTrackPanel(), cc.xy(cols, rows));
        rows++;
        rows++;
        builder.add(getBytesPanel(), cc.xy(cols, rows));
        return builder.getPanel();
    }

    private JPanel getBytesPanel() {
        FormLayout layout = new FormLayout("3dlu,r:d, 3dlu, p, 3dlu", "3dlu, 5*(p, 3dlu)");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        int startCol = 2;
        int colMax = 5;
        int rows = 2;
        int cols = startCol;
        cols = startCol;
        builder.add(new JLabel("Display as:"), cc.xy(cols, rows));
        cols++;
        cols++;
        builder.add(cmbBytes, cc.xy(cols, rows));
        cols = startCol;
        rows++;
        rows++;
        builder.add(new JLabel("current p. sec:"), cc.xy(cols, rows));
        cols++;
        cols++;
        builder.add(txtTransferCur, cc.xy(cols, rows));
        cols = startCol;
        rows++;
        rows++;
        builder.add(new JLabel("max p. sec:"), cc.xy(cols, rows));
        cols++;
        cols++;
        builder.add(txtTransferMax, cc.xy(cols, rows));
        cols = startCol;
        rows++;
        rows++;
        builder.add(new JLabel("Sum Activity:"), cc.xy(cols, rows));
        cols++;
        cols++;
        builder.add(txtSumTotal, cc.xy(cols, rows));
        rows++;
        rows++;
        cols = startCol;
        builder.add(btnClear, cc.xyw(cols, rows, colMax - startCol));
        builder.setBorder(BorderFactory.createTitledBorder("Bytes"));
        return builder.getPanel();
    }

    private JPanel getTrackPanel() {
        FormLayout layout = new FormLayout("3dlu,f:p:g, 3dlu", "3dlu, 1*(p, 3dlu)");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        int startCol = 2;
        int rows = 2;
        int cols = startCol;
        cols = startCol;
        builder.add(btnTrack, cc.xy(cols, rows));
        builder.setBorder(BorderFactory.createTitledBorder("Track Data"));
        return builder.getPanel();
    }

    private JPanel getOverviewPanel() {
        FormLayout layout = new FormLayout("r:d, 3dlu, d, 3dlu, d, 3dlu, f:p:g, c:p:g(0.25)", "3*(p, 3dlu), p");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        int startCol = 1;
        int colMax = 8;
        int rows = 1;
        int cols = startCol;
        cols = startCol;
        builder.add(new JLabel(GUI_Input.strDescription), cc.xy(cols, rows));
        cols++;
        cols++;
        builder.add(txtDescription, cc.xyw(cols, rows, colMax - cols));
        cols++;
        cols++;
        cols++;
        cols++;
        rows++;
        rows++;
        cols = startCol;
        builder.add(new JLabel(GUI_Input.strHost), cc.xy(cols, rows));
        cols++;
        cols++;
        builder.add(txtHost, cc.xy(cols, rows));
        cols++;
        cols++;
        builder.add(new JLabel("Status:"), cc.xy(cols, rows));
        cols++;
        cols++;
        builder.add(txtStatus, cc.xyw(cols, rows, colMax - cols));
        cols++;
        final JLabel lblSpace = new JLabel("");
        lblSpace.setPreferredSize(new Dimension(ImageProvider.getRunningLabelIcon().getIconWidth(), lblSpace.getPreferredSize().height));
        builder.add(lblSpace, cc.xy(cols, rows));
        builder.add(lblStatusIcon, cc.xywh(cols, rows, 1, 3, "c, c"));
        rows++;
        rows++;
        cols = startCol;
        builder.add(new JLabel(GUI_Input.strPortType), cc.xy(cols, rows));
        cols++;
        cols++;
        builder.add(GUI_instance.cmbType, cc.xy(cols, rows, "l,c"));
        rows++;
        rows++;
        cols = startCol;
        builder.add(GUI_instance.getLimitPanel(), cc.xyw(cols, rows, 3));
        cols++;
        cols++;
        cols++;
        cols++;
        builder.add(GUI_instance.getSourceMaskPanel(), cc.xyw(cols, rows, 4));
        return builder.getPanel();
    }

    public void updateFields() {
        PortTableModel dmParent = (PortTableModel) jPortty.tblOverview.getModel();
        int selRow = jPortty.tblOverview.getSelectedRow();
        if (selRow >= 0) {
            selRow = jPortty.tblOverview.convertRowIndexToModel(selRow);
            txtDescription.setText(dmParent.getDescription(selRow));
            txtHost.setText(dmParent.getHost(selRow));
            final String strLimit = dmParent.getLimit(selRow);
            if (strLimit.equals(GlobalConstants.strLimitAsUnlimited)) {
                GUI_instance.chkLimit.setSelected(false);
                GUI_instance.txtLimit.setText(GlobalConstants.strLimitDefault);
            } else {
                GUI_instance.chkLimit.setSelected(true);
                GUI_instance.txtLimit.setText(strLimit);
            }
            String strSourceMask = dmParent.getSourceMask(selRow);
            if (strSourceMask.equals(GlobalConstants.strSourceMask4AnyIPAddress)) {
                GUI_instance.chkSourceMask.setSelected(false);
                strSourceMask = GlobalConstants.strSourceMask4AnyIPAddress;
            } else {
                GUI_instance.chkSourceMask.setSelected(true);
            }
            GUI_instance.txtSourceMask.setText(GUI_instance.getFormat4TextField(strSourceMask));
            GUI_instance.setPorts(dmParent.getDestTCP(selRow), dmParent.getListTCP(selRow));
            GUI_instance.cmbType.setSelectedItem(dmParent.getType(selRow));
            txtStatus.setText(dmParent.getStatus(selRow));
            txtStatus.setToolTipText(txtStatus.getText());
            if (dmParent.isStartAllowed(selRow)) {
                lblStatusIcon.setIcon(ImageProvider.getStoppedLabelIcon());
            } else {
                lblStatusIcon.setIcon(ImageProvider.getRunningLabelIcon());
            }
            dm.setContent(dmParent.getContainer(selRow));
            jPortty.updateOperationButtons(selRow, false);
        }
    }

    public void updateButtons() {
        final boolean containsRows = tblDetails.getRowCount() > 0;
        jPortty.mnuRemoveInactive.setEnabled(containsRows);
    }

    private void initialize() {
        GUI_instance = GUI_Input.getInstance();
        txtDescription = new JTextField(20);
        txtDescription.setEditable(false);
        txtHost = new JTextField(20);
        txtHost.setEditable(false);
        GUI_instance.txtLimit.setEditable(false);
        GUI_instance.chkLimit.setEnabled(false);
        GUI_instance.chkSourceMask.setEnabled(false);
        GUI_instance.cmbType.setEnabled(false);
        txtStatus = new JTextField();
        txtStatus.setEditable(false);
        lblStatusIcon = new JLabel(ImageProvider.getRunningLabelIcon());
        Insets insets = txtStatus.getInsets();
        getSize();
        int top = insets.top;
        int left = insets.left;
        txtStatus.setBounds(left + 18, top + 10, txtStatus.getPreferredSize().width, txtStatus.getPreferredSize().height);
        txtSumTotal = new JTextField(ByteWrapper.format(0, false), 10);
        txtSumTotal.setHorizontalAlignment(JTextField.RIGHT);
        txtSumTotal.setEditable(false);
        txtSumTotal.setHorizontalAlignment(JTextField.RIGHT);
        txtTransferCur = new JTextField(ByteWrapper.format(0, true));
        txtTransferCur.setEditable(false);
        txtTransferCur.setHorizontalAlignment(JTextField.RIGHT);
        txtTransferMax = new JTextField(ByteWrapper.format(0, true));
        txtTransferMax.setEditable(false);
        txtTransferMax.setHorizontalAlignment(JTextField.RIGHT);
        btnClear = new JButton("Reset Active Stat");
        btnClear.setMnemonic('t');
        btnClear.setEnabled(true);
        btnTrack = new JButton("Track Data");
        btnTrack.setMnemonic('k');
        btnTrack.setEnabled(true);
        cmbBytes = new WideComboBox(new DefaultComboBoxModel(BytesConv.values()));
        cmbBytes.setPreferredWidth(cmbBytes.getPreferredSize().width);
        cmbBytes.setPreferredSize(new Dimension(10, (int) cmbBytes.getPreferredSize().getHeight()));
        dm = new DetailedModel(this);
        tblDetails = new ZebraJTable(dm) {

            @Override
            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {

                    @Override
                    public String getToolTipText(MouseEvent event) {
                        java.awt.Point p = event.getPoint();
                        int index = columnModel.getColumnIndexAtX(p.x);
                        int realIndex = columnModel.getColumn(index).getModelIndex();
                        if (realIndex == dm.colTimeActive) {
                            return DetailedModel.strTimeTooltip;
                        }
                        return null;
                    }
                };
            }
        };
        tblDetails.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblDetails.getTableHeader().setReorderingAllowed(false);
        for (int c = 0; c < tblDetails.getColumnCount(); c++) {
            tblDetails.packColumn(c, 0);
        }
        TableColumn colTime = tblDetails.getColumnModel().getColumn(dm.colTimeActive);
        colTime.setCellRenderer(new CenterRenderer());
        tblDetails.setFillsViewportHeight(false);
        tblDetails.setVisibleRowCount(10);
    }

    public void removeInactiveSession() {
        dm.removeInactiveSession();
        ListOfTranferredBytes.resetStatistics();
        sumTotal();
    }

    public void sumTotal() {
        double sum = 0;
        for (int row = 0; row < tblDetails.getRowCount(); row++) {
            sum += dm.getTotal4Row(row);
        }
        txtSumTotal.setText(ByteWrapper.format(sum, false));
    }

    public static void updateThroughPut() {
        String muString = strToolTipThroughPut.replaceAll("#1#", pnlDetail.jPortty.getRunningInstances() + "");
        txtTransferCur.setToolTipText(muString);
        txtTransferMax.setToolTipText(muString);
        if (pnlDetail.dm.hasActiveConnections()) {
            txtTransferCur.setText(ByteWrapper.format(ListOfTranferredBytes.getEstimatedTransferPerSecond(), true));
        } else {
            txtTransferCur.setText(ByteWrapper.format(0, false));
        }
        if (pnlDetail.dm.getRowCount() > 0) {
            txtTransferMax.setText(ByteWrapper.format(ListOfTranferredBytes.getMaxEstimatedBytesPerSecond(), true));
        } else {
            txtTransferMax.setText(ByteWrapper.format(0, false));
        }
    }
}
