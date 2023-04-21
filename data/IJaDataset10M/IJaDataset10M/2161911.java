package org.mars_sim.msp.ui.swing.unit_window.structure;

import org.mars_sim.msp.core.resource.AmountResource;
import org.mars_sim.msp.core.resource.Part;
import org.mars_sim.msp.core.structure.construction.*;
import org.mars_sim.msp.ui.swing.MarsPanelBorder;
import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

/**
 * A panel displaying a list of construction sites at a settlement.
 */
public class ConstructionSitesPanel extends JPanel {

    private ConstructionManager manager;

    private List<ConstructionSite> sitesCache;

    private JPanel sitesListPane;

    private JScrollPane sitesScrollPane;

    /**
     * Constructor
     * @param manager the settlement construction manager.
     */
    public ConstructionSitesPanel(ConstructionManager manager) {
        super();
        this.manager = manager;
        setLayout(new BorderLayout());
        setBorder(new MarsPanelBorder());
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(titlePanel, BorderLayout.NORTH);
        JLabel titleLabel = new JLabel("Construction Sites");
        titlePanel.add(titleLabel);
        sitesScrollPane = new JScrollPane();
        sitesScrollPane.setPreferredSize(new Dimension(200, 75));
        sitesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(sitesScrollPane, BorderLayout.CENTER);
        JPanel sitesOuterListPane = new JPanel(new BorderLayout(0, 0));
        sitesScrollPane.setViewportView(sitesOuterListPane);
        sitesListPane = new JPanel();
        sitesListPane.setLayout(new BoxLayout(sitesListPane, BoxLayout.Y_AXIS));
        sitesOuterListPane.add(sitesListPane, BorderLayout.NORTH);
        sitesCache = manager.getConstructionSites();
        Iterator<ConstructionSite> i = sitesCache.iterator();
        while (i.hasNext()) sitesListPane.add(new ConstructionSitePanel(i.next()));
    }

    /**
     * Update the information on this panel.
     */
    public void update() {
        List<ConstructionSite> sites = manager.getConstructionSites();
        if (!sitesCache.equals(sites)) {
            Iterator<ConstructionSite> i = sites.iterator();
            while (i.hasNext()) {
                ConstructionSite site = i.next();
                if (!sitesCache.contains(site)) sitesListPane.add(new ConstructionSitePanel(site));
            }
            Iterator<ConstructionSite> j = sitesCache.iterator();
            while (j.hasNext()) {
                ConstructionSite site = j.next();
                if (!sites.contains(site)) {
                    ConstructionSitePanel panel = getConstructionSitePanel(site);
                    if (panel != null) sitesListPane.remove(panel);
                }
            }
            sitesScrollPane.validate();
            sitesCache.clear();
            sitesCache.addAll(sites);
        }
        Iterator<ConstructionSite> i = sites.iterator();
        while (i.hasNext()) {
            ConstructionSitePanel panel = getConstructionSitePanel(i.next());
            if (panel != null) panel.update();
        }
    }

    /**
     * Gets a construction site panel for a particular construction site.
     * @param site the construction site.
     * @return construction site panel or null if none found.
     */
    private ConstructionSitePanel getConstructionSitePanel(ConstructionSite site) {
        ConstructionSitePanel result = null;
        for (int x = 0; x < sitesListPane.getComponentCount(); x++) {
            Component component = sitesListPane.getComponent(x);
            if (component instanceof ConstructionSitePanel) {
                ConstructionSitePanel panel = (ConstructionSitePanel) component;
                if (panel.getConstructionSite().equals(site)) result = panel;
            }
        }
        return result;
    }

    /**
     * A panel displaying information about a particular construction site.
     */
    private static class ConstructionSitePanel extends JPanel {

        private ConstructionSite site;

        private JLabel statusLabel;

        private BoundedRangeModel workBarModel;

        /**
         * Constructor.
         * @param site the construction site.
         */
        private ConstructionSitePanel(ConstructionSite site) {
            super();
            this.site = site;
            setLayout(new BorderLayout(5, 5));
            setBorder(new MarsPanelBorder());
            statusLabel = new JLabel("Status: ", JLabel.LEFT);
            add(statusLabel, BorderLayout.NORTH);
            JPanel progressBarPanel = new JPanel();
            add(progressBarPanel, BorderLayout.CENTER);
            JProgressBar workBar = new JProgressBar();
            workBarModel = workBar.getModel();
            workBar.setStringPainted(true);
            progressBarPanel.add(workBar);
            update();
            setToolTipText(getToolTipString());
        }

        /**
         * Gets the construction site for this panel.
         * @return construction site.
         */
        private ConstructionSite getConstructionSite() {
            return site;
        }

        /**
         * Updates the panel information.
         */
        private void update() {
            String statusString = getStatusString();
            if (statusString.length() > 31) statusString = statusString.substring(0, 31) + "...";
            statusLabel.setText(statusString);
            int workProgress = 0;
            ConstructionStage stage = site.getCurrentConstructionStage();
            if (stage != null) {
                double completedWork = stage.getCompletedWorkTime();
                double requiredWork = stage.getInfo().getWorkTime();
                if (requiredWork > 0D) workProgress = (int) (100D * completedWork / requiredWork);
            }
            workBarModel.setValue(workProgress);
            setToolTipText(getToolTipString());
        }

        /**
         * Gets the status label string.
         * @return status string.
         */
        private String getStatusString() {
            String statusString = "";
            ConstructionStage stage = site.getCurrentConstructionStage();
            if (stage != null) {
                if (site.isUndergoingConstruction()) statusString = "Status: constructing " + stage.getInfo().getName(); else if (site.isUndergoingSalvage()) statusString = "Status: salvaging " + stage.getInfo().getName(); else if (site.hasUnfinishedStage()) {
                    if (stage.isSalvaging()) statusString = "Status: salvaging " + stage.getInfo().getName() + " unfinished"; else statusString = "Status: constructing " + stage.getInfo().getName() + " unfinished";
                } else statusString = "Status: " + stage.getInfo().getName() + " completed";
            } else statusString = "No construction";
            return statusString;
        }

        /**
         * Gets a tool tip string for the panel.
         */
        private String getToolTipString() {
            StringBuilder result = new StringBuilder("<html>");
            result.append(getStatusString()).append("<br>");
            ConstructionStage stage = site.getCurrentConstructionStage();
            if (stage != null) {
                ConstructionStageInfo info = stage.getInfo();
                result.append("Stage Type: ").append(info.getType()).append("<br>");
                if (stage.isSalvaging()) result.append("Work Type: salvage<br>"); else result.append("Work Type: Construction<br>");
                DecimalFormat formatter = new DecimalFormat("0.0");
                String requiredWorkTime = formatter.format(info.getWorkTime() / 1000D);
                result.append("Work Time Required: ").append(requiredWorkTime).append(" Sols<br>");
                String completedWorkTime = formatter.format(stage.getCompletedWorkTime() / 1000D);
                result.append("Work Time Completed: ").append(completedWorkTime).append(" Sols<br>");
                result.append("Architect Construction Skill Required: ").append(info.getArchitectConstructionSkill()).append("<br>");
                if ((info.getResources().size() > 0) && !stage.isSalvaging()) {
                    result.append("<br>Construction Resources:<br>");
                    Iterator<AmountResource> i = info.getResources().keySet().iterator();
                    while (i.hasNext()) {
                        AmountResource resource = i.next();
                        double amount = info.getResources().get(resource);
                        result.append("&nbsp;&nbsp;").append(resource.getName()).append(": ").append(amount).append(" kg<br>");
                    }
                }
                if (info.getParts().size() > 0) {
                    if (stage.isSalvaging()) result.append("<br>Salvagable Parts:<br>"); else result.append("<br>Construction Parts:<br>");
                    Iterator<Part> j = info.getParts().keySet().iterator();
                    while (j.hasNext()) {
                        Part part = j.next();
                        int number = info.getParts().get(part);
                        result.append("&nbsp;&nbsp;").append(part.getName()).append(": ").append(number).append("<br>");
                    }
                }
                if (info.getVehicles().size() > 0) {
                    if (stage.isSalvaging()) result.append("<br>Salvage Vehicles:<br>"); else result.append("<br>Construction Vehicles:<br>");
                    Iterator<ConstructionVehicleType> k = info.getVehicles().iterator();
                    while (k.hasNext()) {
                        ConstructionVehicleType vehicle = k.next();
                        result.append("&nbsp;&nbsp;Vehicle Type: ").append(vehicle.getVehicleType()).append("<br>");
                        result.append("&nbsp;&nbsp;Attachment Parts:<br>");
                        Iterator<Part> l = vehicle.getAttachmentParts().iterator();
                        while (l.hasNext()) {
                            result.append("&nbsp;&nbsp;&nbsp;&nbsp;").append(l.next().getName()).append("<br>");
                        }
                    }
                }
            }
            result.append("</html>");
            return result.toString();
        }
    }
}
