package cn.ekuma.epos.panel;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JToolBar;
import cn.ekuma.data.dao.I_ParentNode;
import cn.ekuma.data.dao.bean.IKeyed;
import cn.ekuma.data.dao.bean.I_BaseBean;
import cn.ekuma.data.dao.bean.I_ViewBean;
import cn.ekuma.data.ui.swing.AbstractDAOJEditor;
import cn.ekuma.data.ui.swing.AbstractDTOTableModel;
import cn.ekuma.data.ui.swing.JEditorWarpDialog;
import cn.ekuma.data.ui.swing.ObjectSelectChangeListener;
import cn.ekuma.data.ui.swing.ParentNodeChangeListener;
import cn.ekuma.data.ui.swing.dnd.BeanListTransferable;
import cn.ekuma.data.ui.swing.dnd.BeanTransferable;
import cn.ekuma.epos.datalogic.I_DataLogicERP;
import cn.ekuma.epos.report.JTableReportDialog;
import com.openbravo.beans.JCalendarDialog;
import com.openbravo.data.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.user.EditorCreator;
import com.openbravo.format.Formats;
import com.openbravo.pos.base.AppLocal;
import com.openbravo.pos.base.AppView;
import com.openbravo.pos.base.BeanFactoryApp;
import com.openbravo.pos.base.BeanFactoryException;
import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.panels.QuickTimerFilter;
import com.openbravo.pos.panels.event.BaseBeanChangedListener;
import com.openbravo.pos.panels.event.TimeCycleChangedListener;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import org.jdesktop.swingx.JXTable;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JSplitPane;
import java.awt.Component;

public abstract class AbstractJFinishingPanel<T extends I_BaseBean, E extends I_BaseBean> extends JPanel implements JPanelView, BeanFactoryApp, EditorCreator, TimeCycleChangedListener, ListSelectionListener, DragGestureListener, DragSourceListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8842624968131384340L;

    private JToolBar jToolBar1;

    private QuickTimerFilter timerFilter;

    private JLabel jLabel3;

    protected JTextField jTxtStartDate;

    private JButton btnDateStart;

    private JLabel jLabel4;

    protected JTextField jTxtEndDate;

    private JButton btnDateEnd;

    private JButton jButton1;

    private JButton jButton5;

    protected JPanel jNavigator;

    private JPanel jNavigatorToolBar;

    private JButton button;

    protected JPanel panel_1;

    protected JTextField inputCode;

    protected CardLayout cl1;

    private JButton btnNewButton_1;

    private JButton btnNewButton_2;

    private JButton btnNewButton_3;

    protected JXTable jXTable1;

    protected JEditorWarpDialog editorDialog;

    protected I_DataLogicERP dlSales;

    protected AbstractDTOTableModel<T> tableModel;

    protected Object[] beforeFilterParas;

    protected int timeCycleType;

    private JScrollPane scrollPane;

    private JButton btnNewButton_4;

    protected JTabbedPane linkTabPanel;

    private JSplitPane splitPane;

    private JButton btnNewButton_7;

    AbstractDAOJEditor editor;

    protected IKeyed keyed;

    protected AppView app;

    /**
	 * Create the panel.
	 */
    public AbstractJFinishingPanel(AbstractDTOTableModel<T> tableModel) {
        this.tableModel = tableModel;
        initComponents();
        jXTable1.setModel(tableModel);
        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(jXTable1, DnDConstants.ACTION_COPY_OR_MOVE, this);
        cl1 = (CardLayout) (jNavigator.getLayout());
    }

    public void init(AppView app) throws BeanFactoryException {
        this.app = app;
        dlSales = (I_DataLogicERP) app.getBean("com.openbravo.pos.forms.I_DataLogicSales");
        configNavigator(jNavigatorToolBar, jNavigator);
        initFinishPanel(app);
        editor = getEditor();
        if (editor == null) {
            btnNewButton_1.setEnabled(false);
            btnNewButton_2.setEnabled(false);
            btnNewButton_3.setEnabled(false);
        } else editorDialog = new JEditorWarpDialog(this, dlSales, editor);
    }

    protected abstract void initFinishPanel(AppView app) throws BeanFactoryException;

    private void initComponents() {
        setLayout(new BorderLayout(0, 0));
        JPanel panel = new JPanel();
        add(panel, BorderLayout.NORTH);
        panel.setLayout(new BorderLayout(0, 0));
        jToolBar1 = new javax.swing.JToolBar();
        panel.add(jToolBar1, BorderLayout.NORTH);
        timerFilter = new QuickTimerFilter();
        panel.add(timerFilter, BorderLayout.CENTER);
        timerFilter.addTimeCycleChangedListener(this);
        jLabel3 = new javax.swing.JLabel();
        jTxtStartDate = new javax.swing.JTextField();
        btnDateStart = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jTxtEndDate = new javax.swing.JTextField();
        btnDateEnd = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jToolBar1.setRollover(true);
        btnNewButton_1 = new JButton("");
        btnNewButton_1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jbtnNewActionPerformed(e);
            }
        });
        btnNewButton_1.setPreferredSize(new Dimension(55, 25));
        btnNewButton_1.setIcon(new ImageIcon(AbstractJFinishingPanel.class.getResource("/com/openbravo/images/editnew.png")));
        jToolBar1.add(btnNewButton_1);
        btnNewButton_2 = new JButton("");
        btnNewButton_2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jbtnDeleteActionPerformed(e);
            }
        });
        btnNewButton_2.setPreferredSize(new Dimension(55, 25));
        btnNewButton_2.setIcon(new ImageIcon(AbstractJFinishingPanel.class.getResource("/com/openbravo/images/editdelete.png")));
        jToolBar1.add(btnNewButton_2);
        btnNewButton_3 = new JButton("");
        btnNewButton_3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jbtnSaveActionPerformed(e);
            }
        });
        btnNewButton_3.setPreferredSize(new Dimension(55, 25));
        btnNewButton_3.setIcon(new ImageIcon(AbstractJFinishingPanel.class.getResource("/com/openbravo/images/edit.png")));
        jToolBar1.add(btnNewButton_3);
        btnNewButton_7 = new JButton("");
        btnNewButton_7.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JTableReportDialog.showReport(AbstractJFinishingPanel.this, jXTable1);
            }
        });
        btnNewButton_7.setIcon(new ImageIcon(AbstractJFinishingPanel.class.getResource("/com/openbravo/images/fileprint.png")));
        btnNewButton_7.setPreferredSize(new Dimension(50, 20));
        jToolBar1.add(btnNewButton_7);
        configToolBar(jToolBar1);
        jLabel3.setText(AppLocal.getIntString("Label.StartDate"));
        jToolBar1.add(jLabel3);
        jTxtStartDate.setMinimumSize(new java.awt.Dimension(6, 15));
        jTxtStartDate.setPreferredSize(new java.awt.Dimension(150, 22));
        jToolBar1.add(jTxtStartDate);
        btnDateStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png")));
        btnDateStart.setFocusable(false);
        btnDateStart.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDateStart.setPreferredSize(new java.awt.Dimension(55, 25));
        btnDateStart.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDateStart.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateStartActionPerformed(evt);
            }
        });
        jToolBar1.add(btnDateStart);
        jLabel4.setText(AppLocal.getIntString("Label.EndDate"));
        jToolBar1.add(jLabel4);
        jTxtEndDate.setPreferredSize(new java.awt.Dimension(150, 22));
        jToolBar1.add(jTxtEndDate);
        btnDateEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png")));
        btnDateEnd.setFocusable(false);
        btnDateEnd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDateEnd.setPreferredSize(new java.awt.Dimension(55, 25));
        btnDateEnd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDateEnd.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateEndActionPerformed(evt);
            }
        });
        jToolBar1.add(btnDateEnd);
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1rightarrow.png")));
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setPreferredSize(new java.awt.Dimension(55, 25));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png")));
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setPreferredSize(new java.awt.Dimension(55, 25));
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);
        btnNewButton_4 = new JButton("");
        btnNewButton_4.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                m_jResert1ActionPerformed(e);
            }
        });
        btnNewButton_4.setIcon(new ImageIcon(AbstractJFinishingPanel.class.getResource("/com/openbravo/images/reload.png")));
        jToolBar1.add(btnNewButton_4);
        jNavigatorToolBar = new JPanel();
        panel.add(jNavigatorToolBar, BorderLayout.EAST);
        jNavigatorToolBar.setLayout(new BoxLayout(jNavigatorToolBar, BoxLayout.LINE_AXIS));
        button = new JButton(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png")));
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jNavigator.setVisible(!jNavigator.isVisible());
                if (jNavigator.isVisible()) {
                    cl1.show(jNavigator, "QBFPanel");
                }
            }
        });
        panel_1 = new JPanel();
        jNavigatorToolBar.add(panel_1);
        inputCode = new JTextField();
        inputCode.setPreferredSize(new Dimension(100, 21));
        panel_1.add(inputCode);
        inputCode.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                m_jBarcode1ActionPerformed(e);
                inputCode.selectAll();
            }
        });
        JButton button_3 = new JButton(new ImageIcon(AbstractJFinishingPanel.class.getResource("/com/openbravo/images/apply.png")));
        button_3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                m_jBarcode1ActionPerformed(e);
            }
        });
        panel_1.add(button_3);
        jNavigatorToolBar.add(button);
        jNavigator = new JPanel();
        jNavigator.setPreferredSize(new Dimension(240, 10));
        add(jNavigator, BorderLayout.EAST);
        jNavigator.setLayout(new CardLayout(0, 0));
        splitPane = new JSplitPane();
        splitPane.setOneTouchExpandable(true);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        add(splitPane, BorderLayout.CENTER);
        scrollPane = new JScrollPane();
        splitPane.setLeftComponent(scrollPane);
        jXTable1 = new JXTable();
        jXTable1.setColumnControlVisible(true);
        jXTable1.setAutoCreateRowSorter(true);
        jXTable1.setAutoResizeMode(JXTable.AUTO_RESIZE_OFF);
        jXTable1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        scrollPane.setViewportView(jXTable1);
        linkTabPanel = new JTabbedPane(JTabbedPane.TOP);
        splitPane.setRightComponent(linkTabPanel);
        splitPane.setDividerLocation(500);
        jNavigator.setVisible(false);
        jXTable1.getSelectionModel().addListSelectionListener(this);
        jXTable1.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = jXTable1.getSelectedRow();
                    if (selectedRow >= 0 && editor != null) editObjectAt(jXTable1.convertRowIndexToModel(selectedRow));
                }
            }
        });
    }

    protected void m_jResert1ActionPerformed(ActionEvent e) {
        beforeFilterParas = null;
        timeCycleType = QuickTimerFilter.TIMERCYCLETYPE_Null;
        jTxtStartDate.setText(null);
        jTxtEndDate.setText(null);
        timerFilter.resert();
    }

    protected void jbtnSaveActionPerformed(ActionEvent e) {
        int selectedRow = jXTable1.getSelectedRow();
        if (selectedRow >= 0) editObjectAt(jXTable1.convertRowIndexToModel(selectedRow));
    }

    protected void editObjectAt(int row) {
        try {
            editorDialog.activate();
            editorDialog.writeValueEdit(convectToEditModel(tableModel.getObj(row)));
        } catch (BasicException e) {
            new MessageInf(e).show(this);
            return;
        }
        if (editorDialog.getRetObj() != null) {
            tableModel.setObject(row, convectToViewModel((E) editorDialog.getRetObj()));
            tableModel.fireTableRowsUpdated(row, row);
        }
    }

    protected void jbtnNewActionPerformed(ActionEvent e) {
        try {
            editorDialog.activate();
        } catch (BasicException ex) {
            new MessageInf(ex).show(this);
            return;
        }
        editorDialog.writeValueInsert(null);
        if (editorDialog.getRetObj() != null) {
            tableModel.add(convectToViewModel((E) editorDialog.getRetObj()));
        }
    }

    protected void jbtnDeleteActionPerformed(ActionEvent e) {
        int[] selectedRows = jXTable1.getSelectedRows();
        if (selectedRows.length == 0) return;
        int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannadelete"), AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.NO_OPTION) {
            return;
        }
        int[] rows = selectedRows;
        int j;
        for (int i = 0; i < selectedRows.length; i++) {
            j = jXTable1.convertRowIndexToModel(selectedRows[i]);
            rows[i] = j;
        }
        removeSelectedObjects(rows);
    }

    protected boolean removeSelectedObjects(int[] rows) {
        List<T> objs = new ArrayList();
        for (int i : rows) {
            objs.add(tableModel.getObj(i));
        }
        try {
            if (dlSales != null) dlSales.delete(objs);
            tableModel.remove(objs);
        } catch (BasicException ex) {
            new MessageInf(ex).show(this);
            return false;
        }
        return true;
    }

    protected abstract void m_jBarcode1ActionPerformed(ActionEvent e);

    protected abstract void configNavigator(JPanel jNavigatorToolBar, JPanel jNavigator);

    public abstract void reQuery();

    protected abstract void configToolBar(JToolBar jToolBar12);

    protected abstract AbstractDAOJEditor<E> getEditor();

    protected abstract T convectToViewModel(E obj);

    protected abstract E convectToEditModel(T obj) throws BasicException;

    protected void jButton5ActionPerformed(ActionEvent evt) {
    }

    protected void jButton1ActionPerformed(ActionEvent evt) {
        try {
            beforeFilterParas = (Object[]) createValue();
            reQuery();
        } catch (BasicException e) {
            new MessageInf(e).show(this);
        }
    }

    private void btnDateStartActionPerformed(java.awt.event.ActionEvent evt) {
        Date date;
        try {
            date = (Date) Formats.TIMESTAMP.parseValue(jTxtStartDate.getText());
        } catch (BasicException e) {
            date = null;
        }
        date = JCalendarDialog.showCalendarTimeHours(this, date);
        if (date != null) {
            jTxtStartDate.setText(Formats.TIMESTAMP.formatValue(date));
        }
    }

    private void btnDateEndActionPerformed(java.awt.event.ActionEvent evt) {
        Date date;
        try {
            date = (Date) Formats.TIMESTAMP.parseValue(jTxtEndDate.getText());
        } catch (BasicException e) {
            date = null;
        }
        date = JCalendarDialog.showCalendarTimeHours(this, date);
        if (date != null) {
            jTxtEndDate.setText(Formats.TIMESTAMP.formatValue(date));
        }
    }

    @Override
    public Object getBean() {
        return this;
    }

    public T getSelectedObj() {
        int selectedRow = jXTable1.getSelectedRow();
        if (selectedRow >= 0) return tableModel.getObj(jXTable1.convertRowIndexToModel(selectedRow));
        return null;
    }

    protected void fireBaseBeanChanged(IKeyed key, T object) {
        this.keyed = key;
        Component selectTab = linkTabPanel.getSelectedComponent();
        if (selectTab != null && selectTab instanceof BaseBeanChangedListener) {
            BaseBeanChangedListener linstener = (BaseBeanChangedListener) selectTab;
            linstener.baseBeanChanged(keyed, object);
        } else if (selectTab != null && selectTab instanceof ObjectSelectChangeListener) {
            ObjectSelectChangeListener linstener = (ObjectSelectChangeListener) selectTab;
            linstener.valueSelectChanged(object);
        }
    }

    public final void addParentNodeChangeListener(ParentNodeChangeListener<T> l) {
        listenerList.add(ParentNodeChangeListener.class, l);
    }

    public final void removeParentNodeChangeListener(ParentNodeChangeListener<T> l) {
        listenerList.remove(ParentNodeChangeListener.class, l);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedRow = jXTable1.getSelectedRow();
        if (selectedRow >= 0) {
            final T obj = (T) tableModel.getObj(jXTable1.convertRowIndexToModel(selectedRow));
            I_ParentNode parent = new I_ParentNode() {

                @Override
                public IKeyed getParentKey() {
                    return obj;
                }

                @Override
                public Class getParentClass() {
                    return editor.getEditClass();
                }
            };
            for (ParentNodeChangeListener l : listenerList.getListeners(ParentNodeChangeListener.class)) l.parentNodeValueChange(AbstractDAOJEditor.STATE_Edit, parent);
            fireBaseBeanChanged(obj, obj);
        }
    }

    @Override
    public void dragEnter(DragSourceDragEvent dsde) {
    }

    @Override
    public void dragOver(DragSourceDragEvent dsde) {
    }

    @Override
    public void dropActionChanged(DragSourceDragEvent dsde) {
    }

    @Override
    public void dragExit(DragSourceEvent dse) {
    }

    @Override
    public void dragDropEnd(DragSourceDropEvent dsde) {
    }

    @Override
    public void dragGestureRecognized(DragGestureEvent dge) {
        int[] selectedRows = jXTable1.getSelectedRows();
        List<I_ViewBean> rows = new ArrayList();
        for (int i = 0; i < selectedRows.length; i++) {
            rows.add((T) tableModel.getObj(jXTable1.convertRowIndexToModel(selectedRows[i])));
        }
        Transferable transferable = null;
        if (dge.getTriggerEvent().isAltDown() && rows.size() == 1) transferable = new BeanTransferable(rows.get(0)); else transferable = new BeanListTransferable(rows);
        dge.startDrag(null, transferable, this);
    }
}
