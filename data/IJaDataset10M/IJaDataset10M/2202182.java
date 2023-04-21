package edu.upmc.opi.caBIG.caTIES.client.vr.query.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import org.apache.log4j.Logger;
import edu.upmc.opi.caBIG.caTIES.client.vr.desktop.CaTIES_Aesthetics;
import edu.upmc.opi.caBIG.caTIES.client.vr.desktop.CaTIES_Desktop;
import edu.upmc.opi.caBIG.caTIES.client.vr.desktop.CaTIES_EntityEvent;
import edu.upmc.opi.caBIG.caTIES.client.vr.desktop.CaTIES_EntitySelectionListener;
import edu.upmc.opi.caBIG.caTIES.client.vr.desktop.CaTIES_Messages;
import edu.upmc.opi.caBIG.caTIES.client.vr.query.actions.DeleteCaseSetAction;
import edu.upmc.opi.caBIG.caTIES.client.vr.query.actions.NewCaseSetAction;
import edu.upmc.opi.caBIG.caTIES.client.vr.query.actions.OpenCaseSetAction;
import edu.upmc.opi.caBIG.caTIES.client.vr.query.actions.RenameCaseSetAction;
import edu.upmc.opi.caBIG.caTIES.client.vr.utils.widgets.JMessageBar;
import edu.upmc.opi.caBIG.caTIES.client.vr.utils.widgets.JTitledPanel;
import edu.upmc.opi.caBIG.caTIES.middletier.CaTIES_OrderSetImpl;
import edu.upmc.opi.caBIG.caTIES.middletier.CaTIES_DocumentImpl;
import edu.upmc.opi.caBIG.caTIES.middletier.CaTIES_UserImpl;

public class CaseSetPanel extends JPanel implements CaTIES_EntitySelectionListener {

    private JList caseSetList;

    private JMessageBar status;

    CaseSetListRenderer caseSetListRenderer = new CaseSetListRenderer();

    private Set<String> pathReportUUIDs = new TreeSet<String>();

    public CaseSetPanel() {
        super();
        initGUI();
        loadCaseSets();
        hookListeners();
    }

    private void hookListeners() {
        caseSetList.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (caseSetList.getSelectedIndex() >= 0) {
                    if (e.getKeyCode() == KeyEvent.VK_F2) {
                        RenameCaseSetAction a = new RenameCaseSetAction((CaTIES_OrderSetImpl) caseSetList.getSelectedValue()) {

                            @Override
                            protected void refresh() {
                                CaseSetPanel.this.refresh();
                            }
                        };
                        a.actionPerformed(null);
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) (new OpenCaseSetAction(CaseSetPanel.this, (CaTIES_OrderSetImpl) caseSetList.getSelectedValue())).actionPerformed(null);
                }
            }
        });
        caseSetList.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    (new OpenCaseSetAction(CaseSetPanel.this, (CaTIES_OrderSetImpl) caseSetList.getSelectedValue())).actionPerformed(null);
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) showPopup(e);
            }

            private void showPopup(MouseEvent e) {
                caseSetList.setSelectedIndex(caseSetList.locationToIndex(e.getPoint()));
                if (caseSetList.getSelectedIndex() >= 0) {
                    JPopupMenu popup = createCaseSetPopup((CaTIES_OrderSetImpl) caseSetList.getSelectedValue());
                    popup.show(CaseSetPanel.this, e.getX(), e.getY());
                }
            }
        });
    }

    public void loadCaseSets() {
        CaTIES_UserImpl user = CaTIES_Desktop.getCurrentUser();
        if (user == null) return;
        if (user.isHonestBroker()) titledPanel.setTitle(CaTIES_Messages.HB_CASESET_TITLE);
        DefaultListModel lm = new DefaultListModel();
        List<CaTIES_OrderSetImpl> orders = CaTIES_Desktop.getInstance().getCaseSets();
        if (orders == null) {
            status.setOpaque(true);
            status.setMessage(JMessageBar.MSG_TYPE_ERROR, "Error loading case sets");
            return;
        }
        for (CaTIES_OrderSetImpl o : orders) {
            lm.addElement(o);
        }
        caseSetList.setModel(lm);
        if (lm.getSize() > 0) {
            caseSetList.setSelectedIndex(0);
            status.setMessage(JMessageBar.MSG_TYPE_NOMESSAGE, CaTIES_Messages.DRAG_TO_CASESET);
        } else {
            status.setMessage(JMessageBar.MSG_TYPE_NOMESSAGE, CaTIES_Messages.NO_CASESETS);
        }
        status.setOpaque(false);
    }

    public boolean isPresentInOrder(String reportUUID) {
        return pathReportUUIDs.contains(reportUUID);
    }

    private void initGUI() {
        createCaseList();
        status = new JMessageBar(JMessageBar.MSG_TYPE_NOMESSAGE, "");
        JScrollPane scroll = new JScrollPane(caseSetList);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(status, BorderLayout.NORTH);
        contentPanel.add(scroll, BorderLayout.CENTER);
        titledPanel = new JTitledPanel("My Case Sets", contentPanel);
        titledPanel.setTitleComponent(new JLabel(CaTIES_Aesthetics.ICON_CASESET_16), SwingConstants.LEFT);
        titledPanel.addTitleComponent(createNewCaseSetButton(), SwingConstants.RIGHT);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        status.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        contentPanel.setBorder(CaTIES_Aesthetics.contentInnerBorder);
        contentPanel.setOpaque(true);
        contentPanel.setBackground(Color.white);
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, CaTIES_Aesthetics.getFrameBorderColor()));
        this.setLayout(new BorderLayout());
        this.add(titledPanel, BorderLayout.CENTER);
        this.setMinimumSize(new Dimension(50, 130));
    }

    private JComponent createNewCaseSetButton() {
        JButton b = new JButton("New");
        b.setIcon(CaTIES_Aesthetics.ICON_CASESET_ADD);
        b.setToolTipText("Create a new case set");
        b.addActionListener(new NewCaseSetAction());
        if (CaTIES_Desktop.noDefaultProtocolSelected()) {
            b.setToolTipText(CaTIES_Messages.NO_DEFAULT_PROTOCOL);
        }
        return b;
    }

    private JPopupMenu createCaseSetPopup(CaTIES_OrderSetImpl caseSet) {
        JPopupMenu caseListPopup = new JPopupMenu();
        JMenuItem mi = new JMenuItem(new NewCaseSetAction());
        mi.setIcon(CaTIES_Aesthetics.ICON_CASESET_ADD);
        caseListPopup.add(mi);
        caseListPopup.add(new OpenCaseSetAction(this, caseSet));
        caseListPopup.add(new RenameCaseSetAction((CaTIES_OrderSetImpl) caseSetList.getSelectedValue()) {

            @Override
            protected void refresh() {
                CaseSetPanel.this.refresh();
            }
        });
        caseListPopup.add(new DeleteCaseSetAction(this, caseSet));
        return caseListPopup;
    }

    private void createCaseList() {
        caseSetList = new JList();
        caseSetList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        caseSetList.setVisibleRowCount(-1);
        caseSetList.setCellRenderer(caseSetListRenderer);
        caseSetList.setTransferHandler(new ReportTransferHandler(this));
    }

    public void checkCaseSetsContainingReport(CaTIES_DocumentImpl report) {
        caseSetListRenderer.setCheckedOrders(getOrdersContainingReport(report));
        caseSetList.revalidate();
        caseSetList.repaint();
    }

    public void uncheckAllCaseSets() {
        caseSetListRenderer.setCheckedOrders(new ArrayList());
        caseSetList.revalidate();
        caseSetList.repaint();
    }

    public void refresh() {
        loadCaseSets();
        caseSetList.revalidate();
        caseSetList.repaint();
    }

    private List getOrdersContainingReport(CaTIES_DocumentImpl report) {
        List<CaTIES_OrderSetImpl> orders = new ArrayList<CaTIES_OrderSetImpl>();
        if (report == null) return orders;
        for (int i = 0; i < caseSetList.getModel().getSize(); i++) {
            CaTIES_OrderSetImpl o = (CaTIES_OrderSetImpl) caseSetList.getModel().getElementAt(i);
            if (o.containsReport(report)) orders.add(o);
        }
        return orders;
    }

    Logger logger = Logger.getLogger(CaseSetPanel.class);

    private JTitledPanel titledPanel;

    public boolean hasValidDropState() {
        return caseSetList.getModel().getSize() > 0;
    }

    public void showMessage(int msgType, String msg) {
        status.setMessage(msgType, msg);
    }

    public void entitySelected(CaTIES_EntityEvent entity) {
        if (entity.getEntityData() instanceof CaTIES_DocumentImpl) {
            checkCaseSetsContainingReport((CaTIES_DocumentImpl) (entity.getEntityData()));
        } else {
            uncheckAllCaseSets();
        }
    }

    public void entityUpdated(CaTIES_EntityEvent entity) {
    }
}
