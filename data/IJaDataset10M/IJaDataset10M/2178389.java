package org.mars_sim.msp.ui.swing.unit_window.structure;

import org.mars_sim.msp.core.manufacture.ManufactureProcess;
import org.mars_sim.msp.core.manufacture.ManufactureProcessInfo;
import org.mars_sim.msp.core.manufacture.ManufactureProcessItem;
import org.mars_sim.msp.core.structure.building.Building;
import org.mars_sim.msp.ui.swing.ImageLoader;
import org.mars_sim.msp.ui.swing.MarsPanelBorder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

/**
 * A panel showing information about a manufacturing process.
 */
public class ManufacturePanel extends JPanel {

    private ManufactureProcess process;

    private BoundedRangeModel workBarModel;

    private BoundedRangeModel timeBarModel;

    /**
	 * Constructor
	 * @param process the manufacturing process.
	 * @param showBuilding is the building name shown?
	 * @param processStringWidth the max string width to display for the process name.
	 */
    public ManufacturePanel(ManufactureProcess process, boolean showBuilding, int processStringWidth) {
        super();
        this.process = process;
        if (showBuilding) setLayout(new GridLayout(4, 1, 0, 0)); else setLayout(new GridLayout(3, 1, 0, 0));
        setBorder(new MarsPanelBorder());
        JPanel namePane = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 0));
        add(namePane);
        JButton cancelButton = new JButton(ImageLoader.getIcon("CancelSmall"));
        cancelButton.setMargin(new Insets(0, 0, 0, 0));
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                getManufactureProcess().getWorkshop().endManufacturingProcess(getManufactureProcess(), true);
            }
        });
        cancelButton.setToolTipText("Cancel manufacturing process");
        namePane.add(cancelButton);
        String name = process.getInfo().getName();
        if (name.length() > 0) {
            String firstLetter = name.substring(0, 1).toUpperCase();
            name = " " + firstLetter + name.substring(1);
        }
        if (name.length() > processStringWidth) name = name.substring(0, processStringWidth) + "...";
        JLabel nameLabel = new JLabel(name, JLabel.CENTER);
        namePane.add(nameLabel);
        if (showBuilding) {
            String buildingName = process.getWorkshop().getBuilding().getName();
            JLabel buildingNameLabel = new JLabel(buildingName, JLabel.CENTER);
            add(buildingNameLabel);
        }
        JPanel workPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        add(workPane);
        JLabel workLabel = new JLabel("Work: ", JLabel.LEFT);
        workPane.add(workLabel);
        JProgressBar workBar = new JProgressBar();
        workBarModel = workBar.getModel();
        workBar.setStringPainted(true);
        workPane.add(workBar);
        JPanel timePane = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        add(timePane);
        JLabel timeLabel = new JLabel("Time: ", JLabel.LEFT);
        timePane.add(timeLabel);
        JProgressBar timeBar = new JProgressBar();
        timeBarModel = timeBar.getModel();
        timeBar.setStringPainted(true);
        timePane.add(timeBar);
        update();
        setToolTipText(getToolTipString(process.getInfo(), process.getWorkshop().getBuilding()));
    }

    /**
     * Updates the panel's information.
     */
    public void update() {
        double workTimeRequired = process.getInfo().getWorkTimeRequired();
        double workTimeRemaining = process.getWorkTimeRemaining();
        int workProgress = 100;
        if (workTimeRequired > 0D) workProgress = (int) (100D * (workTimeRequired - workTimeRemaining) / workTimeRequired);
        workBarModel.setValue(workProgress);
        double timeRequired = process.getInfo().getProcessTimeRequired();
        double timeRemaining = process.getProcessTimeRemaining();
        int timeProgress = 100;
        if (timeRequired > 0D) timeProgress = (int) (100D * (timeRequired - timeRemaining) / timeRequired);
        timeBarModel.setValue(timeProgress);
    }

    /**
     * Gets the manufacture process.
     * @return process
     */
    public ManufactureProcess getManufactureProcess() {
        return process;
    }

    /**
     * Gets a tool tip string for a manufacturing process.
     * @param info the manufacture process info.
     * @param building the manufacturing building (or null if none).
     */
    public static String getToolTipString(ManufactureProcessInfo info, Building building) {
        StringBuilder result = new StringBuilder("<html>");
        result.append("Manufacturing Process: ").append(info.getName()).append("<br>");
        if (building != null) result.append("Manufacture Building: ").append(building.getName()).append("<br>");
        result.append("Effort Time Required: ").append(info.getWorkTimeRequired()).append(" millisols<br>");
        result.append("Process Time Required: ").append(info.getProcessTimeRequired()).append(" millisols<br>");
        result.append("Power Required: ").append(info.getPowerRequired()).append(" kW<br>");
        result.append("Building Tech Level Required: ").append(info.getTechLevelRequired()).append("<br>");
        result.append("Materials Science Skill Level Required: ").append(info.getSkillLevelRequired()).append("<br>");
        result.append("Process Inputs:<br>");
        Iterator<ManufactureProcessItem> i = info.getInputList().iterator();
        while (i.hasNext()) {
            ManufactureProcessItem item = i.next();
            result.append("&nbsp;&nbsp;").append(item.getName()).append(": ").append(getItemAmountString(item)).append("<br>");
        }
        result.append("Process Outputs:<br>");
        Iterator<ManufactureProcessItem> j = info.getOutputList().iterator();
        while (j.hasNext()) {
            ManufactureProcessItem item = j.next();
            result.append("&nbsp;&nbsp;").append(item.getName()).append(": ").append(getItemAmountString(item)).append("<br>");
        }
        result.append("</html>");
        return result.toString();
    }

    /**
     * Gets a string representing an manufacture process item amount.
     * @param item the manufacture process item.
     * @return amount string.
     */
    private static String getItemAmountString(ManufactureProcessItem item) {
        String result = "";
        if (ManufactureProcessItem.AMOUNT_RESOURCE.equals(item.getType())) result = item.getAmount() + " kg"; else result = Integer.toString((int) item.getAmount());
        return result;
    }
}
