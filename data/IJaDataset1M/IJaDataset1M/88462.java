package icescrum2.service.impl;

import icescrum2.dao.model.IProduct;
import icescrum2.dao.model.IRemainingEstimationArray;
import icescrum2.dao.model.ISprint;
import icescrum2.dao.model.ITask;
import icescrum2.dao.model.impl.ProductBacklogItem;
import icescrum2.dao.model.impl.Release;
import icescrum2.dao.model.impl.RemainingEstimationArray;
import icescrum2.dao.model.impl.Sprint;
import icescrum2.dao.model.impl.Task;
import icescrum2.dao.model.impl.User;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ImportXML_FromR2_5_ServiceImpl extends ImportXMLServiceImpl {

    private Map<Integer, IRemainingEstimationArray> parseRe = new HashMap<Integer, IRemainingEstimationArray>();

    @Override
    public IProduct parseProduct(Element node) {
        IProduct p = super.parseProduct(node);
        this.parseRe.clear();
        return p;
    }

    @Override
    protected ISprint parseSprint(Element node) {
        NodeList nodes;
        Sprint sprint = new Sprint();
        sprint.setParentRelease((Release) parseRelease);
        sprint.setNumber(getInteger(node, "sprintNumber"));
        sprint.setGoal(getString(node, "sprintGoal"));
        sprint.setStartDate(getDate(node, "sprintStartDate"));
        sprint.setEndDate(getDate(node, "sprintEndDate"));
        sprint.setVelocity(getDouble(node, "sprintVelocity"));
        sprint.setEstimatedVelocity(getDouble(node, "sprintEstimatedVelocity"));
        sprint.setDailyWorkTime(getDouble(node, "sprintDailyWorkTime"));
        sprint.setState(getInteger(node, "sprintState"));
        ISprint _sprint = (ISprint) sprint;
        sprintDao.saveSprint(_sprint);
        parseSprint = sprint;
        nodes = node.getElementsByTagName("remainingEstimations");
        SortedMap<String, Integer> globalBurndown = new TreeMap<String, Integer>();
        if (sprint.getState().equals(ISprint.STATE_DONE)) {
            ((Sprint) parseSprint).setFinalRemainingEstimations(new RemainingEstimationArray());
        }
        for (int i = 0; i < nodes.getLength(); i++) {
            parseRemainingEstimations((Element) nodes.item(i), globalBurndown);
        }
        if (sprint.getState().equals(ISprint.STATE_DONE)) {
            parseSprint.getFinalRemainingEstimations().parseRemainingTimeMap(globalBurndown);
            taskDao.saveTaskEstimation(parseSprint.getFinalRemainingEstimations());
        }
        nodes = node.getElementsByTagName("story");
        for (int i = 0; i < nodes.getLength(); i++) parseStory((Element) nodes.item(i));
        parseSprint = null;
        return sprint;
    }

    @Override
    protected ITask parseTask(Element node) {
        Task task = new Task();
        task.setParentProductBacklogItem((ProductBacklogItem) parseStory);
        task.setLabel(getString(node, "taskLabel"));
        task.setNotes(getString(node, "taskNotes"));
        task.setState(getInteger(node, "taskState"));
        IRemainingEstimationArray re = parseRe.get(getInteger(node, "taskRemainingTime"));
        if (re == null) {
            re = new RemainingEstimationArray();
            re.estimateDay(parseStory.getParentSprint().getEndDate(), 0);
            taskDao.saveTaskEstimation(re);
        }
        task.setEstimations((RemainingEstimationArray) re);
        task.setCreator((User) parseUsers.get(getInteger(node, "taskCreator")));
        task.setOwneruser((User) parseUsers.get(getInteger(node, "taskOwner")));
        ITask _task = (ITask) task;
        taskDao.saveTask(_task);
        return task;
    }

    protected void parseRemainingEstimations(Element node, SortedMap<String, Integer> globalBurndown) {
        RemainingEstimationArray re = new RemainingEstimationArray();
        SortedMap<String, Integer> taskBurndown = new TreeMap<String, Integer>();
        Integer idRe = Integer.parseInt(node.getAttributes().getNamedItem("id").getNodeValue());
        String remTime = getString(node, "remainingTime");
        Date itDate = (Date) parseSprint.getStartDate().clone();
        if (remTime != null) {
            String[] remTimeArray = remTime.split(":");
            int i;
            Integer current;
            String key;
            for (i = 0; i < remTimeArray.length; i++) {
                current = Integer.parseInt(remTimeArray[i]);
                key = RemainingEstimationArray.dateFormat.format(itDate);
                itDate.setTime(itDate.getTime() + (24l * 60l * 60l * 1000l));
                if (parseSprint.getState().equals(ISprint.STATE_DONE)) {
                    if (globalBurndown.get(key) != null) {
                        globalBurndown.put(key, current + globalBurndown.get(key));
                    } else {
                        globalBurndown.put(key, current);
                    }
                }
                taskBurndown.put(key, current);
            }
        }
        re.parseRemainingTimeMap(taskBurndown);
        taskDao.saveTaskEstimation(re);
        parseRe.put(idRe, re);
    }
}
