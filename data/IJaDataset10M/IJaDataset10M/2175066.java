package edu.upmc.opi.caBIG.caTIES.client.vr.query.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import edu.upmc.opi.caBIG.caTIES.client.dispatcher.DeidentifiedDataServices;
import edu.upmc.opi.caBIG.caTIES.client.vr.desktop.CaTIES_Aesthetics;
import edu.upmc.opi.caBIG.caTIES.client.vr.desktop.CaTIES_Desktop;
import edu.upmc.opi.caBIG.caTIES.client.vr.desktop.CaTIES_EntityEvent;
import edu.upmc.opi.caBIG.caTIES.client.vr.desktop.CaTIES_EntitySelectionListener;
import edu.upmc.opi.caBIG.caTIES.database.Cloner;
import edu.upmc.opi.caBIG.caTIES.database.domain.impl.DocumentImpl;
import edu.upmc.opi.caBIG.caTIES.database.domain.impl.PatientImpl;
import edu.upmc.opi.caBIG.caTIES.middletier.CaTIES_DocumentImpl;

public class AvailableReportsPanel extends JPanel {

    CaTIES_DocumentImpl report;

    Border contentSelectionBorder;

    private JPanel contentPanel;

    private HashMap<JLabel, CaTIES_DocumentImpl> map = new HashMap<JLabel, CaTIES_DocumentImpl>();

    public CaTIES_DocumentImpl currentSelection;

    /**
     * The selection listeners.
     */
    private Vector selectionListeners = new Vector();

    public AvailableReportsPanel() {
        super();
        initGUI();
    }

    public void removeEntitySelectionListener(CaTIES_EntitySelectionListener listener) {
        selectionListeners.remove(listener);
    }

    public void addEntitySelectionListener(CaTIES_EntitySelectionListener listener) {
        if (selectionListeners.contains(listener)) return;
        selectionListeners.addElement(listener);
    }

    /**
     * Fire entity selection event.
     * 
     * @param node the node
     * @param nodeInfo      */
    private void fireEntitySelectionEvent(Object entity) {
        Vector vtemp = (Vector) selectionListeners.clone();
        for (int x = 0; x < vtemp.size(); x++) {
            CaTIES_EntitySelectionListener target = (CaTIES_EntitySelectionListener) vtemp.elementAt(x);
            target.entitySelected(new CaTIES_EntityEvent(this, entity));
        }
    }

    private void initGUI() {
        UIDefaults uidefs = UIManager.getLookAndFeelDefaults();
        contentSelectionBorder = BorderFactory.createCompoundBorder(new MatteBorder(2, 2, 2, 2, (Color) uidefs.get("List.selectionBackground")), new EmptyBorder(2, 2, 2, 2));
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        setOpaque(true);
        setBackground(Color.white);
        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.NORTH);
    }

    public void setReport(CaTIES_DocumentImpl document) {
        this.report = document;
        this.currentSelection = report;
        clearContents();
        if (currentSelection != null) loadReport();
    }

    private void clearContents() {
        contentPanel.removeAll();
        map.clear();
    }

    public void setSelection(CaTIES_DocumentImpl cr) {
        currentSelection = cr;
        clearContents();
        if (currentSelection != null) loadReport();
    }

    private void loadReport() {
        List<DocumentImpl> prs = getSortedReports();
        contentPanel.add(Box.createVerticalStrut(5));
        JLabel title = new JLabel("ALL REPORTS FOR THIS PATIENT(" + prs.size() + "):");
        title.setFont(CaTIES_Aesthetics.FONT_MESSAGE_BOLD);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(title);
        int currentCY = 0;
        JPanel yrReportsPanel = null;
        boolean unknownCreated = false;
        for (DocumentImpl r : prs) {
            if ((r.getCollectionYear() == null || r.getCollectionYear() == 0) && !unknownCreated) {
                unknownCreated = true;
                contentPanel.add(Box.createVerticalStrut(5));
                currentCY = r.getCollectionYear();
                JLabel l = new JLabel("Unknown Year");
                l.setAlignmentX(Component.LEFT_ALIGNMENT);
                l.setFont(CaTIES_Aesthetics.FONT_MESSAGE_BOLD);
                contentPanel.add(l);
                yrReportsPanel = new JPanel();
                yrReportsPanel.setOpaque(false);
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(yrReportsPanel, BorderLayout.WEST);
                panel.setOpaque(false);
                panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                contentPanel.add(panel);
            } else if (r.getCollectionYear() != currentCY) {
                contentPanel.add(Box.createVerticalStrut(5));
                currentCY = r.getCollectionYear();
                JLabel l = new JLabel("" + currentCY);
                l.setAlignmentX(Component.LEFT_ALIGNMENT);
                l.setFont(CaTIES_Aesthetics.FONT_MESSAGE_BOLD);
                contentPanel.add(l);
                yrReportsPanel = new JPanel();
                yrReportsPanel.setOpaque(false);
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(yrReportsPanel, BorderLayout.WEST);
                panel.setOpaque(false);
                panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                contentPanel.add(panel);
            }
            JLabel rl = new JLabel();
            rl.setTransferHandler(new GivenReportTransferHandler());
            final CaTIES_DocumentImpl cr;
            String label = "";
            if (r.equals(report.obj)) {
                rl.setIcon(CaTIES_Aesthetics.ICON_QUERY_24);
                label = report.getDisplayText();
                cr = report;
            } else {
                label = getTimeDiff(r);
                cr = new CaTIES_DocumentImpl(r);
                cr.obj.setOrganization(report.obj.getOrganization());
                cr.setDisplayText(report.getDisplayText() + " " + label);
                if (cr.getNullSafeIsQuarantined()) rl.setIcon(CaTIES_Aesthetics.ICON_QUARANTINED_REPORT_24); else if (CaTIES_Desktop.getInstance().isPartOfCaseSet(cr)) rl.setIcon(CaTIES_Aesthetics.ICON_REPORT_CHECK_24); else rl.setIcon(CaTIES_Aesthetics.ICON_REPORT_24);
            }
            rl.setVerticalTextPosition(JLabel.BOTTOM);
            rl.setHorizontalTextPosition(JLabel.CENTER);
            if (cr.equals(currentSelection)) {
                rl.setBorder(contentSelectionBorder);
                rl.setText(label);
            } else {
                rl.setText("<html><font color=blue><u>" + label + "</u></font></html>");
            }
            rl.addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent e) {
                    CaTIES_DocumentImpl r = map.get(e.getSource());
                    fireEntitySelectionEvent(r);
                    setSelection(cr);
                    if (cr != null) {
                        JComponent c = (JComponent) e.getSource();
                        GivenReportTransferHandler th = (GivenReportTransferHandler) c.getTransferHandler();
                        List<CaTIES_DocumentImpl> l = new ArrayList<CaTIES_DocumentImpl>();
                        l.add(currentSelection);
                        th.setReportList(l);
                        th.exportAsDrag(c, e, TransferHandler.COPY);
                    }
                }
            });
            yrReportsPanel.add(rl);
            map.put(rl, cr);
        }
        contentPanel.add(Box.createGlue());
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private String getTimeDiff(DocumentImpl r) {
        Date context = report.getCollectionDateTime();
        String sign = "+";
        long delta = r.getCollectionDateTime().getTime() - context.getTime();
        if (delta < 0) {
            sign = "-";
            delta = -delta;
        }
        long x = delta / 1000;
        long seconds = x % 60;
        x /= 60;
        long minutes = x % 60;
        x /= 60;
        long hours = x % 24;
        x /= 24;
        long days = x % 30;
        x /= 30;
        long months = x % 12;
        x /= 12;
        long years = x;
        if (years >= 1) {
            years = Math.abs(r.getCollectionYear() - report.getCollectionYear());
            return sign + years + " years";
        } else if (months >= 1) {
            if (days >= 15) months++;
            return sign + months + " months";
        } else {
            if (hours >= 12) days++;
            return sign + days + " days";
        }
    }

    private List<DocumentImpl> getSortedReports() {
        PatientImpl p;
        Collection<DocumentImpl> c;
        try {
            p = DeidentifiedDataServices.getPatient(report.obj.getPatient().getUuid(), report.obj.getOrganization());
            if (p != null) {
                c = p.getDocumentCollection();
            } else c = new ArrayList<DocumentImpl>();
        } catch (Exception e1) {
            e1.printStackTrace();
            c = new ArrayList<DocumentImpl>();
        }
        List<DocumentImpl> prs = new ArrayList<DocumentImpl>();
        for (DocumentImpl d : c) {
            prs.add(d);
        }
        Collections.sort(prs, new Comparator<DocumentImpl>() {

            public int compare(DocumentImpl o1, DocumentImpl o2) {
                int comp = o1.getCollectionYear().compareTo(o2.getCollectionYear());
                if (comp == 0) comp = o1.getCollectionDateTime().compareTo(o2.getCollectionDateTime());
                return comp;
            }
        });
        return prs;
    }

    public void refresh() {
        setSelection(currentSelection);
    }
}
