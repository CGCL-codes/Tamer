package org.mars_sim.msp.core.manufacture;

import org.jdom.Document;
import org.jdom.Element;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ManufactureConfig implements Serializable {

    private static final String PROCESS = "process";

    private static final String NAME = "name";

    private static final String TECH = "tech";

    private static final String SKILL = "skill";

    private static final String WORK_TIME = "work-time";

    private static final String PROCESS_TIME = "process-time";

    private static final String POWER_REQUIRED = "power-required";

    private static final String INPUTS = "inputs";

    private static final String OUTPUTS = "outputs";

    private static final String RESOURCE = "resource";

    private static final String AMOUNT = "amount";

    private static final String PART = "part";

    private static final String NUMBER = "number";

    private static final String EQUIPMENT = "equipment";

    private static final String VEHICLE = "vehicle";

    private static final String SALVAGE = "salvage";

    private static final String ITEM_NAME = "item-name";

    private static final String TYPE = "type";

    private static final String PART_SALVAGE = "part-salvage";

    private Document manufactureDoc;

    private List<ManufactureProcessInfo> manufactureProcessList;

    private List<SalvageProcessInfo> salvageList;

    /**
     * Constructor
     * @param manufactureDoc DOM document containing manufacture process configuration.
     */
    public ManufactureConfig(Document manufactureDoc) {
        this.manufactureDoc = manufactureDoc;
    }

    /**
     * Gets a list of manufacturing process information.
     * @return list of manufacturing process information.
     * @throws Exception if error getting info.
     */
    @SuppressWarnings("unchecked")
    List<ManufactureProcessInfo> getManufactureProcessList() {
        if (manufactureProcessList == null) {
            Element root = manufactureDoc.getRootElement();
            List<Element> processNodes = root.getChildren(PROCESS);
            manufactureProcessList = new ArrayList<ManufactureProcessInfo>(processNodes.size());
            for (Element processElement : processNodes) {
                ManufactureProcessInfo process = new ManufactureProcessInfo();
                manufactureProcessList.add(process);
                String name = "";
                name = processElement.getAttributeValue(NAME);
                process.setName(name);
                process.setTechLevelRequired(Integer.parseInt(processElement.getAttributeValue(TECH)));
                process.setSkillLevelRequired(Integer.parseInt(processElement.getAttributeValue(SKILL)));
                process.setWorkTimeRequired(Double.parseDouble(processElement.getAttributeValue(WORK_TIME)));
                process.setProcessTimeRequired(Double.parseDouble(processElement.getAttributeValue(PROCESS_TIME)));
                process.setPowerRequired(Double.parseDouble(processElement.getAttributeValue(POWER_REQUIRED)));
                Element inputs = processElement.getChild(INPUTS);
                List<ManufactureProcessItem> inputList = new ArrayList<ManufactureProcessItem>();
                process.setInputList(inputList);
                parseResources(inputList, inputs.getChildren(RESOURCE));
                parseParts(inputList, inputs.getChildren(PART));
                parseEquipment(inputList, inputs.getChildren(EQUIPMENT));
                parseVehicles(inputList, inputs.getChildren(VEHICLE));
                Element outputs = processElement.getChild(OUTPUTS);
                List<ManufactureProcessItem> outputList = new ArrayList<ManufactureProcessItem>();
                process.setOutputList(outputList);
                parseResources(outputList, outputs.getChildren(RESOURCE));
                parseParts(outputList, outputs.getChildren(PART));
                parseEquipment(outputList, outputs.getChildren(EQUIPMENT));
                parseVehicles(outputList, outputs.getChildren(VEHICLE));
            }
        }
        return manufactureProcessList;
    }

    /**
     * Parses the amount resource elements in a node list.
     * @param list the list to store the resources in.
     * @param resourceNodes the node list.
     * @throws Exception if error parsing resources.
     */
    private void parseResources(List<ManufactureProcessItem> list, List<Element> resourceNodes) {
        for (Element resourceElement : resourceNodes) {
            ManufactureProcessItem resourceItem = new ManufactureProcessItem();
            resourceItem.setType(ManufactureProcessItem.AMOUNT_RESOURCE);
            resourceItem.setName(resourceElement.getAttributeValue(NAME));
            resourceItem.setAmount(Double.parseDouble(resourceElement.getAttributeValue(AMOUNT)));
            list.add(resourceItem);
        }
    }

    /**
     * Parses the part elements in a node list.
     * @param list the list to store the parts in.
     * @param partNodes the node list.
     * @throws Exception if error parsing parts.
     */
    private void parseParts(List<ManufactureProcessItem> list, List<Element> partNodes) {
        for (Element partElement : partNodes) {
            ManufactureProcessItem partItem = new ManufactureProcessItem();
            partItem.setType(ManufactureProcessItem.PART);
            partItem.setName(partElement.getAttributeValue(NAME));
            partItem.setAmount(Integer.parseInt(partElement.getAttributeValue(NUMBER)));
            list.add(partItem);
        }
    }

    /**
     * Parses the equipment elements in a node list.
     * @param list the list to store the equipment in.
     * @param equipmentNodes the node list.
     * @throws Exception if error parsing equipment.
     */
    private void parseEquipment(List<ManufactureProcessItem> list, List<Element> equipmentNodes) {
        for (Element equipmentElement : equipmentNodes) {
            ManufactureProcessItem equipmentItem = new ManufactureProcessItem();
            equipmentItem.setType(ManufactureProcessItem.EQUIPMENT);
            equipmentItem.setName(equipmentElement.getAttributeValue(NAME));
            equipmentItem.setAmount(Integer.parseInt(equipmentElement.getAttributeValue(NUMBER)));
            list.add(equipmentItem);
        }
    }

    /**
     * Parses the vehicle elements in a node list.
     * @param list the list to store the vehicles in.
     * @param vehicleNodes the node list.
     * @throws Exception if error parsing vehicles.
     */
    private void parseVehicles(List<ManufactureProcessItem> list, List<Element> vehicleNodes) {
        for (Element vehicleElement : vehicleNodes) {
            ManufactureProcessItem vehicleItem = new ManufactureProcessItem();
            vehicleItem.setType(ManufactureProcessItem.VEHICLE);
            vehicleItem.setName(vehicleElement.getAttributeValue(NAME));
            vehicleItem.setAmount(Integer.parseInt(vehicleElement.getAttributeValue(NUMBER)));
            list.add(vehicleItem);
        }
    }

    /**
     * Gets a list of salvage process information.
     * @return list of salvage process information.
     * @throws Exception if error getting info.
     */
    @SuppressWarnings("unchecked")
    List<SalvageProcessInfo> getSalvageList() {
        if (salvageList == null) {
            Element root = manufactureDoc.getRootElement();
            List<Element> salvageNodes = root.getChildren(SALVAGE);
            salvageList = new ArrayList<SalvageProcessInfo>(salvageNodes.size());
            Iterator<Element> i = salvageNodes.iterator();
            while (i.hasNext()) {
                Element salvageElement = i.next();
                SalvageProcessInfo salvage = new SalvageProcessInfo();
                salvageList.add(salvage);
                String itemName = "";
                itemName = salvageElement.getAttributeValue(ITEM_NAME);
                salvage.setItemName(itemName);
                salvage.setType(salvageElement.getAttributeValue(TYPE));
                salvage.setTechLevelRequired(Integer.parseInt(salvageElement.getAttributeValue(TECH)));
                salvage.setSkillLevelRequired(Integer.parseInt(salvageElement.getAttributeValue(SKILL)));
                salvage.setWorkTimeRequired(Double.parseDouble(salvageElement.getAttributeValue(WORK_TIME)));
                List<Element> partSalvageNodes = salvageElement.getChildren(PART_SALVAGE);
                List<PartSalvage> partSalvageList = new ArrayList<PartSalvage>(partSalvageNodes.size());
                salvage.setPartSalvageList(partSalvageList);
                Iterator<Element> j = partSalvageNodes.iterator();
                while (j.hasNext()) {
                    Element partSalvageElement = j.next();
                    PartSalvage part = new PartSalvage();
                    partSalvageList.add(part);
                    part.setName(partSalvageElement.getAttributeValue(NAME));
                    part.setNumber(Integer.parseInt(partSalvageElement.getAttributeValue(NUMBER)));
                }
            }
        }
        return salvageList;
    }

    /**
     * Prepare object for garbage collection.
     */
    public void destroy() {
        manufactureDoc = null;
        if (manufactureProcessList != null) {
            Iterator<ManufactureProcessInfo> i = manufactureProcessList.iterator();
            while (i.hasNext()) {
                i.next().destroy();
            }
            manufactureProcessList.clear();
            manufactureProcessList = null;
        }
        if (salvageList != null) {
            Iterator<SalvageProcessInfo> j = salvageList.iterator();
            while (j.hasNext()) {
                j.next().destroy();
            }
            salvageList.clear();
            salvageList = null;
        }
    }
}
