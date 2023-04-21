package be.vds.jtbdive.client.view.core.dive;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Window;
import java.util.Collection;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.view.core.dive.profile.DiveProfileGraphicDetailPanel;
import be.vds.jtbdive.client.view.events.DiveSiteSelectionListener;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveEquipment;
import be.vds.jtbdive.core.core.DiveSite;

public class DivePanel extends JPanel implements Observer {

    private static final long serialVersionUID = -4604205753089428698L;

    private Window parentWindow;

    private DiverManagerFacade diverManagerFacade;

    private Dive currentDive;

    private DiveParametersPanel diveParametersPanel;

    private DiveSiteManagerFacade diveLocationManagerFacade;

    private DiveProfileGraphicDetailPanel profilePanel;

    private EquipmentParameterPanel equipmentParameterPanel;

    private JTabbedPane tabbedPane;

    private LogBookManagerFacade logBookManagerFacade;

    private DocumentsParameterPanel documentsParameterPanel;

    private Set<DiveSiteSelectionListener> diveSiteSelectionListeners = new HashSet<DiveSiteSelectionListener>();

    public DivePanel(Window parentWindow, LogBookManagerFacade logBookManagerFacade, DiverManagerFacade diverManagerFacade, DiveSiteManagerFacade diveLocationManagerFacade) {
        this.parentWindow = parentWindow;
        this.logBookManagerFacade = logBookManagerFacade;
        this.diverManagerFacade = diverManagerFacade;
        this.diveLocationManagerFacade = diveLocationManagerFacade;
        init();
    }

    private void init() {
        this.setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM, JTabbedPane.WRAP_TAB_LAYOUT);
        diveParametersPanel = new DiveParametersPanel(parentWindow, logBookManagerFacade, diverManagerFacade, diveLocationManagerFacade);
        tabbedPane.addTab("parameters", UIAgent.getInstance().getIcon(UIAgent.ICON_PARAMETER_16), diveParametersPanel);
        profilePanel = new DiveProfileGraphicDetailPanel(logBookManagerFacade);
        tabbedPane.addTab("profile", UIAgent.getInstance().getIcon(UIAgent.ICON_GRAPH_16), profilePanel);
        equipmentParameterPanel = new EquipmentParameterPanel(parentWindow, logBookManagerFacade);
        tabbedPane.addTab("equipment", UIAgent.getInstance().getIcon(UIAgent.ICON_MASK_16), equipmentParameterPanel);
        documentsParameterPanel = new DocumentsParameterPanel(logBookManagerFacade);
        tabbedPane.addTab("documents", UIAgent.getInstance().getIcon(UIAgent.ICON_ATTACHEMENT_16), documentsParameterPanel);
        this.add(tabbedPane, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        I18nResourceManager i18Mgr = I18nResourceManager.sharedInstance();
        int index = tabbedPane.indexOfComponent(equipmentParameterPanel);
        tabbedPane.setTitleAt(index, i18Mgr.getString("equipment"));
        index = tabbedPane.indexOfComponent(diveParametersPanel);
        tabbedPane.setTitleAt(index, i18Mgr.getString("parameters"));
        index = tabbedPane.indexOfComponent(profilePanel);
        tabbedPane.setTitleAt(index, i18Mgr.getString("dive.profile"));
        index = tabbedPane.indexOfComponent(documentsParameterPanel);
        tabbedPane.setTitleAt(index, i18Mgr.getString("documents"));
    }

    public void setCurrentDive(Dive dive) {
        this.currentDive = dive;
        if (currentDive == null) {
            clear();
        } else {
            updateData();
        }
        if (currentDive == null || currentDive.getDiveSite() == null) {
            fireDiveSiteSelected(null);
        } else {
            fireDiveSiteSelected(currentDive.getDiveSite());
        }
    }

    private void updateData() {
        if (null == currentDive) {
            clear();
        } else {
            diveParametersPanel.setDive(currentDive);
            profilePanel.setDiveProfile(currentDive.getDiveProfile(), currentDive);
            DiveEquipment equipment = currentDive.getDiveEquipment();
            if (equipment == null) {
                equipmentParameterPanel.setDiveEquipment(null, currentDive);
            } else {
                equipmentParameterPanel.setDiveEquipment(currentDive.getDiveEquipment(), currentDive);
            }
            documentsParameterPanel.setDocuments(currentDive.getDocuments(), currentDive);
        }
    }

    private void clear() {
        diveParametersPanel.clear();
        profilePanel.clear();
        equipmentParameterPanel.clear();
        diveParametersPanel.setDive(null);
        profilePanel.setDiveProfile(null, null);
        equipmentParameterPanel.setDiveEquipment(null, null);
        documentsParameterPanel.setDocuments(null, null);
    }

    public Dive getCurrentDive() {
        return currentDive;
    }

    @Override
    public String toString() {
        if (currentDive == null || currentDive.getId() == -1) {
            return "dive panel for New Dive...";
        } else {
            return "dive panel for " + currentDive;
        }
    }

    public void addDiveSiteSelectionListener(DiveSiteSelectionListener diveSiteSelectionListener) {
        diveSiteSelectionListeners.add(diveSiteSelectionListener);
    }

    public void removeDiveSiteSelectionListener(DiveSiteSelectionListener diveSiteSelectionListener) {
        diveSiteSelectionListeners.remove(diveSiteSelectionListener);
    }

    private void fireDiveSiteSelected(DiveSite diveSite) {
        for (DiveSiteSelectionListener listener : diveSiteSelectionListeners) {
            listener.diveSiteSelected(diveSite);
        }
    }

    public Collection<DiveSiteSelectionListener> getDiveSiteSelectionListeners() {
        return diveSiteSelectionListeners;
    }

    public void removeAllListeners() {
        diveSiteSelectionListeners.clear();
    }

    public void updateUnitsLabels() {
        diveParametersPanel.updateUnits();
        profilePanel.updateUnits();
        equipmentParameterPanel.updateUnits();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o.equals(UnitsAgent.getInstance()) && arg.equals(UnitsAgent.UNITS_CHANGED)) {
            updateUnitsLabels();
        }
    }
}
