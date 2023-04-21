package org.ist_spice.mdcs.beans.description_processors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.ist_spice.mdcs.entities.OutputSpec;
import org.ist_spice.mdcs.entities.Region;
import org.ist_spice.mdcs.entities.ScheduleGroup;
import org.ist_spice.mdcs.entities.mediaElements.MediaContent;
import org.ist_spice.mdcs.interfaces.SMILMediaContentFactory_IF;
import org.ist_spice.mdcs.interfaces.SMILProcessorBean_IF;
import org.ist_spice.mdcs.util.SMILXMLUtil;
import org.ist_spice.mdcs.util.SMILXMLUtil_IF;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Stateless
@Remote(SMILProcessorBean_IF.class)
public class SMILProcessorBean implements SMILProcessorBean_IF {

    @EJB
    SMILMediaContentFactory_IF smcf;

    @PersistenceContext
    private EntityManager manager;

    public void createSMILMetaModel(Node smilDocNode, int outputSpecId) {
        SMILXMLUtil_IF smilParser;
        smilParser = SMILXMLUtil.getInstance();
        NodeList nodeList;
        Node tempNode;
        OutputSpec outputSpec = manager.find(OutputSpec.class, outputSpecId);
        Collection<Region> regions = outputSpec.getRegions();
        Region tempRegion;
        nodeList = smilParser.evaluateXPath(smilDocNode, SMILXMLUtil.FIND_REGION);
        if (nodeList.getLength() > 0 && regions == null) {
            regions = new ArrayList<Region>();
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            tempNode = nodeList.item(i);
            tempRegion = new Region();
            parseRegionXML(tempRegion, tempNode);
            System.out.println("Found Region with: " + tempRegion.getRegionID());
            regions.add(tempRegion);
            tempRegion.setOutputSpec(outputSpec);
            outputSpec.setRegions(regions);
        }
        nodeList = smilParser.evaluateXPath(smilDocNode, SMILXMLUtil.FIND_ROOT_PAR_AND_SEQ);
        ScheduleGroup tempSchedule = null;
        String tempNodeName;
        for (int i = 0; i < nodeList.getLength(); i++) {
            tempNode = nodeList.item(i);
            tempNodeName = tempNode.getNodeName();
            System.out.println("-------Found: " + tempNodeName + " length: " + nodeList.getLength());
            tempSchedule = new ScheduleGroup();
            if (tempNodeName.equalsIgnoreCase("seq")) {
                tempSchedule.setType(ScheduleGroup.SMIL_SEQUENCE);
            }
            if (tempNodeName.equalsIgnoreCase("par")) {
                tempSchedule.setType(ScheduleGroup.SMIL_PARALLEL);
            }
            outputSpec.setScheduleGroup(tempSchedule);
            parseScheduleSubElements(tempSchedule, tempNode);
        }
        if (tempSchedule != null) {
            traverseInTree(tempSchedule);
        } else {
            System.out.println("SCHEDULE_GROUP ======= null");
        }
    }

    private void traverseInTree(ScheduleGroup group) {
        System.out.println("This is a group of type: " + group.getType() + " groupID: " + group.getId());
        if (group.getParent() == null) {
            System.out.println("--Identified root node");
            group.setX(0);
            group.setY(0);
        }
        Collection<ScheduleGroup> tempSchedules = group.getScheduleGroups();
        Collection<MediaContent> tempContents = group.getContents();
        int arraysize = 0;
        if (tempSchedules != null) arraysize += tempSchedules.size();
        if (tempContents != null) arraysize += tempContents.size();
        System.out.println("--Arraysize: " + arraysize);
        Object[] childNodes = new Object[arraysize];
        if (tempSchedules != null) {
            for (Iterator<ScheduleGroup> i = tempSchedules.iterator(); i.hasNext(); ) {
                ScheduleGroup tempGroup = i.next();
                childNodes[tempGroup.getPositionInLevel()] = tempGroup;
            }
        }
        if (tempContents != null) {
            for (Iterator<MediaContent> i = tempContents.iterator(); i.hasNext(); ) {
                MediaContent tempContent = i.next();
                childNodes[tempContent.getPositionInLevel()] = tempContent;
            }
        }
        if (childNodes.length > 0) {
            Object tempObj = childNodes[0];
            if (tempObj instanceof MediaContent) {
                ((MediaContent) tempObj).setX(group.getX());
                ((MediaContent) tempObj).setY(group.getY());
            }
            if (tempObj instanceof ScheduleGroup) {
                ((ScheduleGroup) tempObj).setX(group.getX());
                ((ScheduleGroup) tempObj).setY(group.getY());
            }
            if (childNodes.length == 1) {
                if (tempObj instanceof ScheduleGroup) {
                    traverseInTree(((ScheduleGroup) tempObj));
                }
            }
            Object n1;
            Object n2;
            for (int i = 0; i < childNodes.length - 1; i++) {
                n2 = childNodes[i + 1];
                n1 = childNodes[i];
                if (group.getType() == ScheduleGroup.SMIL_PARALLEL) {
                    System.out.println("initial position SMIL_PARALLEL: " + i);
                    if (n1 instanceof MediaContent) {
                        System.out.println("mediaContent: " + ((MediaContent) n1).getId() + " " + ((MediaContent) n1).getX() + " " + ((MediaContent) n1).getY() + " " + ((MediaContent) n1).getScheduleGroup().getId() + " " + ((MediaContent) n1).getPositionInLevel());
                        if (n2 instanceof ScheduleGroup) {
                            ScheduleGroup sn2 = (ScheduleGroup) n2;
                            sn2.setX(group.getX());
                            sn2.setY(((MediaContent) n1).getY() + 1);
                            System.out.println("scheduleGroup: " + ((ScheduleGroup) n2).getId() + " " + ((ScheduleGroup) n2).getX() + " " + ((ScheduleGroup) n2).getY() + " Id: " + ((ScheduleGroup) n2).getId() + " pil: " + ((ScheduleGroup) n2).getPositionInLevel());
                            if ((i + 1) == childNodes.length - 1) {
                                System.out.println("traverse in tree - last PARRALEL");
                                traverseInTree(sn2);
                            }
                        }
                        if (n2 instanceof MediaContent) {
                            MediaContent mn2 = (MediaContent) n2;
                            mn2.setX(group.getX());
                            mn2.setY(((MediaContent) n1).getY() + 1);
                            System.out.println("mediaContent: " + ((MediaContent) n2).getId() + " " + ((MediaContent) n2).getX() + " " + ((MediaContent) n2).getY() + " " + ((MediaContent) n2).getScheduleGroup().getId() + " " + ((MediaContent) n2).getPositionInLevel());
                        }
                    }
                    if (n1 instanceof ScheduleGroup) {
                        System.out.println("scheduleGroup: " + ((ScheduleGroup) n1).getId() + " " + ((ScheduleGroup) n1).getX() + " " + ((ScheduleGroup) n1).getY() + " Id: " + ((ScheduleGroup) n1).getId() + " pil: " + ((ScheduleGroup) n1).getPositionInLevel());
                        if (n2 instanceof ScheduleGroup) {
                            ScheduleGroup sn2 = (ScheduleGroup) n2;
                            sn2.setX(group.getX());
                            System.out.println("traverse in tree - normal PARRALEL");
                            traverseInTree((ScheduleGroup) n1);
                            sn2.setY(((ScheduleGroup) n1).getY() + 1);
                            System.out.println("scheduleGroup: " + ((ScheduleGroup) n2).getId() + " " + ((ScheduleGroup) n2).getX() + " " + ((ScheduleGroup) n2).getY() + " Id: " + ((ScheduleGroup) n2).getId() + " pil: " + ((ScheduleGroup) n2).getPositionInLevel());
                            if ((i + 1) == childNodes.length - 1) {
                                System.out.println("traverse in tree - last PARRALEL");
                                traverseInTree(sn2);
                            }
                        }
                        if (n2 instanceof MediaContent) {
                            MediaContent mn2 = (MediaContent) n2;
                            mn2.setX(group.getX());
                            System.out.println("traverse in tree - normal PARRALEL");
                            traverseInTree((ScheduleGroup) n1);
                            mn2.setY(((ScheduleGroup) n1).getY() + 1);
                            System.out.println("mediaContent: " + ((MediaContent) n2).getId() + " " + ((MediaContent) n2).getX() + " " + ((MediaContent) n2).getY() + " " + ((MediaContent) n2).getScheduleGroup().getId() + " " + ((MediaContent) n2).getPositionInLevel());
                        }
                    }
                }
                if (group.getType() == ScheduleGroup.SMIL_SEQUENCE) {
                    System.out.println("initial position SMIL_SEQUENCE: " + i);
                    if (n1 instanceof MediaContent) {
                        System.out.println("mediaContent: " + ((MediaContent) n1).getId() + " " + ((MediaContent) n1).getX() + " " + ((MediaContent) n1).getY() + " " + ((MediaContent) n1).getScheduleGroup().getId() + " " + ((MediaContent) n1).getPositionInLevel());
                        if (n2 instanceof ScheduleGroup) {
                            ScheduleGroup sn2 = (ScheduleGroup) n2;
                            sn2.setX(((MediaContent) n1).getX() + 1);
                            sn2.setY(group.getY());
                            System.out.println("scheduleGroup: " + ((ScheduleGroup) n2).getId() + " " + ((ScheduleGroup) n2).getX() + " " + ((ScheduleGroup) n2).getY() + " Id: " + ((ScheduleGroup) n2).getId() + " pil: " + ((ScheduleGroup) n2).getPositionInLevel());
                            if ((i + 1) == childNodes.length - 1) {
                                System.out.println("traverse in tree - last SEQUENCE");
                                traverseInTree(sn2);
                            }
                        }
                        if (n2 instanceof MediaContent) {
                            MediaContent mn2 = (MediaContent) n2;
                            mn2.setX(((MediaContent) n1).getX() + 1);
                            mn2.setY(group.getY());
                            System.out.println("mediaContent: " + ((MediaContent) n2).getId() + " " + ((MediaContent) n2).getX() + " " + ((MediaContent) n2).getY() + " " + ((MediaContent) n2).getScheduleGroup().getId() + " " + ((MediaContent) n2).getPositionInLevel());
                        }
                    }
                    if (n1 instanceof ScheduleGroup) {
                        System.out.println("scheduleGroup: " + ((ScheduleGroup) n1).getId() + " " + ((ScheduleGroup) n1).getX() + " " + ((ScheduleGroup) n1).getY() + " Id: " + ((ScheduleGroup) n1).getId() + " pil: " + ((ScheduleGroup) n1).getPositionInLevel());
                        if (n2 instanceof ScheduleGroup) {
                            ScheduleGroup sn2 = (ScheduleGroup) n2;
                            sn2.setY(group.getY());
                            System.out.println("traverse in tree - normal SEQUENCE");
                            traverseInTree((ScheduleGroup) n1);
                            sn2.setX(((ScheduleGroup) n1).getX() + 1);
                            System.out.println("scheduleGroup: " + ((ScheduleGroup) n2).getId() + " " + ((ScheduleGroup) n2).getX() + " " + ((ScheduleGroup) n2).getY() + " Id: " + ((ScheduleGroup) n2).getId() + " pil: " + ((ScheduleGroup) n2).getPositionInLevel());
                            if (i + 1 == childNodes.length - 1) {
                                System.out.println("traverse in tree - last SEQUENCE");
                                traverseInTree(sn2);
                            }
                        }
                        if (n2 instanceof MediaContent) {
                            MediaContent mn2 = (MediaContent) n2;
                            mn2.setY(group.getY());
                            System.out.println("traverse in tree - normal SEQUENCE");
                            traverseInTree((ScheduleGroup) n1);
                            mn2.setX(((ScheduleGroup) n1).getX() + 1);
                            System.out.println("mediaContent: " + ((MediaContent) n2).getId() + " " + ((MediaContent) n2).getX() + " " + ((MediaContent) n2).getY() + " " + ((MediaContent) n2).getScheduleGroup().getId() + " " + ((MediaContent) n2).getPositionInLevel());
                        }
                    }
                }
            }
        }
        Object tempObj;
        if (tempSchedules == null && tempContents != null) {
            System.out.println("P,S -> C, reaching the recursion break condition");
            tempObj = childNodes[childNodes.length - 1];
            if (tempObj instanceof MediaContent) {
                group.setX(((MediaContent) tempObj).getX());
                group.setY(((MediaContent) tempObj).getY());
            }
        }
        if (tempSchedules != null && group.getType() == ScheduleGroup.SMIL_PARALLEL) {
            System.out.println("P -> S, reaching the recursion break condition");
            Iterator<ScheduleGroup> iter = tempSchedules.iterator();
            ScheduleGroup tempScheduleGroup;
            while (iter.hasNext()) {
                tempScheduleGroup = iter.next();
                if (group.getX() < tempScheduleGroup.getX()) group.setX(tempScheduleGroup.getX());
            }
            tempObj = childNodes[childNodes.length - 1];
            if (tempObj instanceof ScheduleGroup) {
                group.setY(((ScheduleGroup) tempObj).getY());
            }
            if (tempObj instanceof MediaContent) {
                group.setY(((MediaContent) tempObj).getY());
            }
        }
        if (tempSchedules != null && group.getType() == ScheduleGroup.SMIL_SEQUENCE) {
            System.out.println("S -> P reaching the recursion break condition");
            Iterator<ScheduleGroup> iter = tempSchedules.iterator();
            ScheduleGroup tempScheduleGroup;
            while (iter.hasNext()) {
                tempScheduleGroup = iter.next();
                if (group.getY() < tempScheduleGroup.getY()) group.setY(tempScheduleGroup.getY());
            }
            tempObj = childNodes[childNodes.length - 1];
            if (tempObj instanceof ScheduleGroup) {
                group.setX(((ScheduleGroup) tempObj).getX());
            }
            if (tempObj instanceof MediaContent) {
                group.setX(((MediaContent) tempObj).getX());
            }
        }
    }

    /**
	 * parse all subelements of the scheduleGroup
	 * 
	 * @param scheduleNode
	 */
    public void parseScheduleSubElements(ScheduleGroup scheduleGroup, Node scheduleNode) {
        System.out.println("entered parseScheduleSubElements");
        NodeList nodeList = scheduleNode.getChildNodes();
        if (nodeList != null) {
            System.out.println("number of subnodes are: " + nodeList.getLength());
        } else {
            System.out.println("nodeList = null");
        }
        ScheduleGroup tempSchedule = null;
        String tempNodeName;
        Node tempNode;
        boolean isScheduleGroup;
        int positionInLevelCounter = 0;
        for (int i = 0; i < nodeList.getLength(); i++) {
            isScheduleGroup = false;
            tempNode = nodeList.item(i);
            tempNodeName = tempNode.getNodeName();
            System.out.println("Found: " + tempNodeName);
            if (tempNodeName.equalsIgnoreCase("seq")) {
                tempSchedule = new ScheduleGroup();
                tempSchedule.setType(ScheduleGroup.SMIL_SEQUENCE);
                isScheduleGroup = true;
            }
            if (tempNodeName.equalsIgnoreCase("par")) {
                tempSchedule = new ScheduleGroup();
                tempSchedule.setType(ScheduleGroup.SMIL_PARALLEL);
                isScheduleGroup = true;
            }
            if (tempNodeName.equalsIgnoreCase("switch")) {
                tempSchedule = new ScheduleGroup();
                tempSchedule.setType(ScheduleGroup.SMIL_SWITCH);
                isScheduleGroup = true;
            }
            if (isScheduleGroup) {
                scheduleGroup.addScheduleGroup(tempSchedule);
                tempSchedule.setParent(scheduleGroup);
                tempSchedule.setPositionInLevel(positionInLevelCounter);
                positionInLevelCounter++;
                parseScheduleSubElements(tempSchedule, tempNode);
            } else {
                int medContentID = smcf.createMediaContent(tempNode);
                MediaContent medContent = manager.find(MediaContent.class, medContentID);
                if (medContent != null) {
                    scheduleGroup.addContent(medContent);
                    medContent.setScheduleGroup(scheduleGroup);
                    medContent.setPositionInLevel(positionInLevelCounter);
                    positionInLevelCounter++;
                }
            }
        }
        if (scheduleGroup.getContents() != null) {
            Iterator<MediaContent> iter = scheduleGroup.getContents().iterator();
            MediaContent tempContent = null;
            while (iter.hasNext()) {
                tempContent = iter.next();
                System.out.println("+++++++Found Content element with ID: " + tempContent.getId() + " MediaType: " + tempContent.getMediaType());
            }
        }
    }

    public void parseRegionXML(Region region, Node regionNode) {
        region.setNode(regionNode);
        System.out.println("RegionNode: " + regionNode.getNodeName());
        NamedNodeMap tempMap = regionNode.getAttributes();
        Node tempNode;
        if (tempMap.getLength() > 0) {
            tempNode = tempMap.getNamedItem(Region.REGION_ID);
            if (tempNode != null) region.setRegionID(tempNode.getNodeValue());
            tempNode = tempMap.getNamedItem(Region.HEIGHT);
            if (tempNode != null) region.setHeight(Integer.valueOf(tempNode.getNodeValue()));
            tempNode = tempMap.getNamedItem(Region.WIDTH);
            if (tempNode != null) region.setWidth(Integer.valueOf(tempNode.getNodeValue()));
        } else {
            System.out.println("the nodeMap is empty");
        }
    }
}
