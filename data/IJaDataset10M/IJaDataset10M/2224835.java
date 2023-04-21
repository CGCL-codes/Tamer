package fr.fg.client.core;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import fr.fg.client.ajax.Action;
import fr.fg.client.ajax.ActionCallback;
import fr.fg.client.ajax.ActionCallbackAdapter;
import fr.fg.client.data.AnswerData;
import fr.fg.client.data.BuildingData;
import fr.fg.client.data.PlayerStarSystemData;
import fr.fg.client.i18n.Formatter;
import fr.fg.client.i18n.StaticMessages;
import fr.fg.client.openjwt.core.Config;
import fr.fg.client.openjwt.ui.JSButton;
import fr.fg.client.openjwt.ui.JSComboBox;
import fr.fg.client.openjwt.ui.JSDialog;
import fr.fg.client.openjwt.ui.JSLabel;
import fr.fg.client.openjwt.ui.JSList;
import fr.fg.client.openjwt.ui.JSOptionPane;
import fr.fg.client.openjwt.ui.JSRowLayout;
import fr.fg.client.openjwt.ui.SelectionListener;

public class BuildBuildingDialog extends JSDialog implements SelectionListener, ClickListener, ActionCallback {

    private static final String[][] FILTERS = { { BuildingData.CIVILIAN_INFRASTRUCTURES, BuildingData.CORPORATIONS, BuildingData.TRADE_PORT, BuildingData.EXPLOITATION0, BuildingData.EXPLOITATION1, BuildingData.EXPLOITATION2, BuildingData.EXPLOITATION3, BuildingData.REFINERY, BuildingData.EXTRACTOR_CENTER, BuildingData.LABORATORY, BuildingData.RESEARCH_CENTER, BuildingData.SPACESHIP_YARD, BuildingData.DEFENSIVE_DECK, BuildingData.STOREHOUSE, BuildingData.FACTORY }, { BuildingData.CIVILIAN_INFRASTRUCTURES, BuildingData.CORPORATIONS, BuildingData.TRADE_PORT, BuildingData.EXPLOITATION0, BuildingData.EXPLOITATION1, BuildingData.EXPLOITATION2, BuildingData.EXPLOITATION3, BuildingData.REFINERY, BuildingData.EXTRACTOR_CENTER, BuildingData.LABORATORY, BuildingData.RESEARCH_CENTER, BuildingData.SPACESHIP_YARD, BuildingData.DEFENSIVE_DECK, BuildingData.STOREHOUSE, BuildingData.FACTORY }, { BuildingData.CIVILIAN_INFRASTRUCTURES, BuildingData.CORPORATIONS, BuildingData.TRADE_PORT, BuildingData.EXPLOITATION0, BuildingData.EXPLOITATION1, BuildingData.EXPLOITATION2, BuildingData.EXPLOITATION3, BuildingData.REFINERY, BuildingData.EXTRACTOR_CENTER, BuildingData.LABORATORY, BuildingData.RESEARCH_CENTER, BuildingData.SPACESHIP_YARD, BuildingData.DEFENSIVE_DECK, BuildingData.STOREHOUSE, BuildingData.FACTORY }, { BuildingData.CIVILIAN_INFRASTRUCTURES, BuildingData.CORPORATIONS, BuildingData.TRADE_PORT }, { BuildingData.EXPLOITATION0, BuildingData.EXPLOITATION1, BuildingData.EXPLOITATION2, BuildingData.EXPLOITATION3, BuildingData.REFINERY, BuildingData.EXTRACTOR_CENTER }, { BuildingData.LABORATORY, BuildingData.RESEARCH_CENTER }, { BuildingData.SPACESHIP_YARD, BuildingData.DEFENSIVE_DECK }, { BuildingData.STOREHOUSE, BuildingData.FACTORY } };

    private static final int FILTER_ALL = 0, FILTER_LEVEL_1 = 1, FILTER_UPGRADES = 2;

    private ResourcesManager resourcesManager;

    private ResearchManager researchManager;

    private PlayerStarSystemData systemData;

    private JSButton okBt, cancelBt;

    private JSComboBox filterComboBox;

    private JSList buildingsList;

    private Action currentAction;

    private int buildSlot;

    public BuildBuildingDialog(ResourcesManager resourcesManager, ResearchManager researchManager) {
        super("Construction de bâtiments", true, true, true);
        this.resourcesManager = resourcesManager;
        this.researchManager = researchManager;
        StaticMessages messages = GWT.create(StaticMessages.class);
        ArrayList<String> classes = new ArrayList<String>();
        classes.add("Tous");
        classes.add("Niveau 1");
        classes.add("Amélioration");
        classes.add("Civil");
        classes.add("Exploitation");
        classes.add("Recherche");
        classes.add("Militaire");
        classes.add("Divers");
        filterComboBox = new JSComboBox();
        filterComboBox.setPixelWidth(120);
        filterComboBox.setItems(classes);
        filterComboBox.addSelectionListener(this);
        JSLabel filterLabel = new JSLabel("&nbsp;Filtrer bâtiments&nbsp;");
        filterLabel.setAlignment(JSLabel.ALIGN_RIGHT);
        buildingsList = new JSList();
        buildingsList.setPixelSize(400, 294);
        buildingsList.addSelectionListener(this);
        okBt = new JSButton(messages.ok());
        okBt.setPixelWidth(100);
        okBt.addClickListener(this);
        cancelBt = new JSButton(messages.cancel());
        cancelBt.setPixelWidth(100);
        cancelBt.addClickListener(this);
        JSRowLayout layout = new JSRowLayout();
        layout.addComponent(filterLabel);
        layout.addComponent(filterComboBox);
        layout.addRow();
        layout.addComponent(buildingsList);
        layout.addRowSeparator(5);
        layout.addComponent(okBt);
        layout.addComponent(cancelBt);
        layout.setRowAlignment(JSRowLayout.ALIGN_CENTER);
        setComponent(layout);
        centerOnScreen();
        setDefaultCloseOperation(DESTROY_ON_CLOSE);
    }

    public void show(PlayerStarSystemData systemData, int buildSlot) {
        this.systemData = systemData;
        this.buildSlot = buildSlot;
        int buildingsCount = 0;
        for (String type : BuildingData.BUILDINGS) for (int level = 0; level < 5; level++) buildingsCount += systemData.getBuildingsCount(type, level);
        for (int i = 0; i < systemData.getBuildsCount(); i++) if (systemData.getBuildAt(i).getLevel() == 0) buildingsCount++;
        if (buildingsCount >= systemData.getBuildingLand()) filterComboBox.setSelectedIndex(FILTER_UPGRADES); else filterComboBox.setSelectedIndex(FILTER_ALL);
        setVisible(true);
        updateBuildingsList();
    }

    public void selectionChanged(Widget sender, int newValue, int oldValue) {
        if (sender == filterComboBox) {
            updateBuildingsList();
        }
    }

    public void onClick(Widget sender) {
        if (sender == okBt) {
            if (currentAction != null && currentAction.isPending()) return;
            BuildingUI buildingUI = (BuildingUI) buildingsList.getSelectedItem();
            if (buildingUI.toString().contains("unavailable")) {
                JSOptionPane.showMessageDialog("Vous ne pouvez pas lancer la construction de ce bâtiment.", "Erreur", JSOptionPane.OK_OPTION, JSOptionPane.WARNING_MESSAGE, null);
                return;
            }
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("system", String.valueOf(systemData.getId()));
            if (buildingUI.type == null) {
                if (buildSlot == 0) {
                    if (systemData.getBuildsCount() > 1) {
                        params.put("build_id", String.valueOf(systemData.getBuildAt(1).getType()));
                        params.put("build_level", String.valueOf(systemData.getBuildAt(1).getLevel()));
                    } else {
                        params.put("build_id", "");
                        params.put("build_level", "0");
                    }
                } else {
                    if (systemData.getBuildsCount() > 0) {
                        params.put("build_id", systemData.getBuildAt(0).getType());
                        params.put("build_level", String.valueOf(systemData.getBuildAt(0).getLevel()));
                    } else {
                        params.put("build_id", "");
                        params.put("build_level", "0");
                    }
                }
                params.put("queue_id", "");
                params.put("queue_level", "0");
            } else {
                if (buildSlot == 0) {
                    params.put("build_id", buildingUI.type);
                    params.put("build_level", String.valueOf(buildingUI.level));
                    if (systemData.getBuildsCount() > 1) {
                        params.put("queue_id", String.valueOf(systemData.getBuildAt(1).getType()));
                        params.put("queue_level", String.valueOf(systemData.getBuildAt(1).getLevel()));
                    } else {
                        params.put("queue_id", "");
                        params.put("queue_level", "0");
                    }
                } else {
                    params.put("build_id", String.valueOf(systemData.getBuildAt(0).getType()));
                    params.put("build_level", String.valueOf(systemData.getBuildAt(0).getLevel()));
                    params.put("queue_id", buildingUI.type);
                    params.put("queue_level", String.valueOf(buildingUI.level));
                }
            }
            currentAction = new Action("systems/build", params, this);
        } else if (sender == cancelBt) {
            setVisible(false);
        }
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (!visible) {
            resourcesManager = null;
            filterComboBox.removeSelectionListener(this);
            filterComboBox = null;
            okBt.removeClickListener(this);
            okBt = null;
            cancelBt.removeClickListener(this);
            cancelBt = null;
            buildingsList.removeSelectionListener(this);
            buildingsList = null;
            currentAction = null;
        }
    }

    public void onFailure(String error) {
        ActionCallbackAdapter.onFailureDefaultBehavior(error);
    }

    public void onSuccess(AnswerData data) {
        setVisible(false);
        UpdateManager.UPDATE_CALLBACK.onSuccess(data);
    }

    private void updateBuildingsList() {
        int filter = filterComboBox.getSelectedIndex();
        ArrayList<BuildingUI> buildings = new ArrayList<BuildingUI>();
        buildings.add(new BuildingUI(null, 0));
        for (String building : FILTERS[filter]) {
            if (building.equals(BuildingData.CORPORATIONS) || building.equals(BuildingData.RESEARCH_CENTER) || building.equals(BuildingData.REFINERY) || building.equals(BuildingData.TRADE_PORT) || building.equals(BuildingData.EXTRACTOR_CENTER) || building.equals(BuildingData.FACTORY)) {
                boolean found = false;
                for (int i = buildSlot - 1; i >= 0; i--) if (systemData.getBuildAt(i).getType().equals(building)) {
                    if (systemData.getBuildAt(i).getLevel() != 4 && filter != FILTER_LEVEL_1 && isBuildingUnlocked(building, systemData.getBuildAt(i).getLevel() + 1)) buildings.add(new BuildingUI(building, systemData.getBuildAt(i).getLevel() + 1));
                    found = true;
                    break;
                }
                if (!found) for (int level = 0; level < 5; level++) if (systemData.getBuildingsCount(building, level) > 0) {
                    if (level != 4 && filter != FILTER_LEVEL_1 && isBuildingUnlocked(building, level + 1)) buildings.add(new BuildingUI(building, level + 1));
                    found = true;
                    break;
                }
                if (!found && filter != FILTER_UPGRADES && isBuildingUnlocked(building, 0)) buildings.add(new BuildingUI(building, 0));
            } else {
                if (filter != FILTER_UPGRADES && isBuildingUnlocked(building, 0)) buildings.add(new BuildingUI(building, 0));
                if (filter != FILTER_LEVEL_1) {
                    for (int level = 1; level < 5; level++) {
                        int previousLevelBuildingsCount = systemData.getBuildingsCount(building, level - 1);
                        for (int i = 0; i < buildSlot; i++) {
                            if (systemData.getBuildAt(i).getType().equals(building)) {
                                if (systemData.getBuildAt(i).getLevel() == level) previousLevelBuildingsCount--; else if (systemData.getBuildAt(i).getLevel() == level - 1) previousLevelBuildingsCount++;
                            }
                        }
                        if (previousLevelBuildingsCount > 0 && isBuildingUnlocked(building, level)) buildings.add(new BuildingUI(building, level));
                    }
                }
            }
        }
        buildingsList.setItems(buildings);
    }

    private boolean isBuildingUnlocked(String type, int level) {
        for (int requirement : BuildingData.getRequiredTechnologies(type, level)) {
            if (!researchManager.hasResearchedTechnology(requirement)) return false;
        }
        return true;
    }

    private class BuildingUI {

        private String type;

        private int level;

        public BuildingUI(String type, int level) {
            this.type = type;
            this.level = level;
        }

        @Override
        public String toString() {
            if (type == null) {
                return "<table unselectable=\"on\" class=\"action\" " + "cellspacing=\"0\">" + "<tr unselectable=\"on\">" + "<td unselectable=\"on\" valign=\"top\">" + "<div unselectable=\"on\" class=\"spaceship\" style=\"height: 25px;\"></div></td>" + "<td unselectable=\"on\" valign=\"center\" " + "style=\"padding: 2px 5px;\" class=\"small\"><div class=\"title\" " + "unselectable=\"on\">Aucun bâtiment</div>" + "</td></tr></table>";
            }
            boolean available = true;
            int[] buildingCost = BuildingData.getCost(type, level);
            String cost = "<div class=\"small right\" style=\"float: right;\" " + "unselectable=\"on\">";
            int creditsCost = (int) Math.ceil(buildingCost[4] * BuildingData.getProduction(BuildingData.TRADE_PORT, systemData));
            long currentCredits = resourcesManager.getCurrentCredits();
            if (creditsCost > 0) {
                cost += "<b style=\"color: " + (creditsCost <= currentCredits ? "#00c000" : "red") + " !important;\">" + Formatter.formatNumber(creditsCost, true) + "</b>" + "&nbsp;<img src=\"" + Config.getMediaUrl() + "images/misc/blank.gif\" class=\"resource credits\"/><br/>";
                if (creditsCost > currentCredits) available = false;
            }
            for (int i = 0; i < 4; i++) {
                int resourceCost = buildingCost[i];
                long currentResource = resourcesManager.getCurrentResource(systemData.getId(), i);
                if (resourceCost > 0) {
                    cost += "<b style=\"color: " + (resourceCost <= currentResource ? "#00c000" : "red") + " !important;\">" + Formatter.formatNumber(resourceCost, true) + "</b>&nbsp;<img src=\"" + Config.getMediaUrl() + "images/misc/blank.gif\" " + "class=\"resource r" + i + "\"/><br/>";
                    if (resourceCost > currentResource) available = false;
                }
            }
            cost += "</div>";
            String deposit = "";
            if (level == 0) {
                if (type.startsWith(BuildingData.EXPLOITATION)) {
                    int inUse = 0;
                    for (int i = 0; i < 5; i++) inUse += systemData.getBuildingsCount(type, i);
                    for (int i = 0; i < systemData.getBuildsCount(); i++) inUse += systemData.getBuildAt(i).getType().equals(type) && systemData.getBuildAt(i).getLevel() == 0 ? 1 : 0;
                    int resourceIndex = Integer.parseInt(type.substring(BuildingData.EXPLOITATION.length()));
                    deposit = "<div class=\"emphasize\" style=\"clear: both;\">" + "<span style=\"float: right;\">" + (systemData.getAvailableResource(resourceIndex) - inUse) + " / " + systemData.getAvailableResource(resourceIndex) + " " + Utilities.parseSmilies(":r" + (resourceIndex + 1) + ":") + "</span>" + "Gisements disponibles</div>";
                    if (systemData.getAvailableResource(resourceIndex) <= inUse) available = false;
                } else if (type.equals(BuildingData.CIVILIAN_INFRASTRUCTURES)) {
                    int inUse = 0;
                    for (int i = 0; i < 5; i++) inUse += systemData.getBuildingsCount(type, i);
                    for (int i = 0; i < systemData.getBuildsCount(); i++) inUse += systemData.getBuildAt(i).getType().equals(type) && systemData.getBuildAt(i).getLevel() == 0 ? 1 : 0;
                    deposit = "<div class=\"emphasize\" style=\"clear: both;\">" + "<span style=\"float: right;\">" + (systemData.getAvailableSpace() - inUse) + " / " + systemData.getAvailableSpace() + "</span>Espace disponible</div>";
                    if (systemData.getAvailableSpace() <= inUse) available = false;
                }
            }
            String desc = cost + "<div class=\"title\" unselectable=\"on\">" + BuildingData.getName(type, level) + "</div>" + "<div class=\"x-small\" unselectable=\"on\" style=\"padding-right: 80px;\">" + BuildingData.getDesc(type, level, level > 0) + deposit + "</div>";
            return "<table unselectable=\"on\"" + (available ? "" : " class=\"unavailable\"") + "cellspacing=\"0\" style=\"width: 100%;\">" + "<tr unselectable=\"on\">" + "<td unselectable=\"on\" valign=\"top\" style=\"width: 54px;\">" + "<div unselectable=\"on\" class=\"control " + BuildingData.getClassName(type) + (available ? "" : " disabled") + "\"></div></td><td unselectable=\"on\" valign=\"center\" " + "style=\"padding: 2px 5px;\" class=\"small\">" + desc + "</td></tr></table>";
        }
    }
}
