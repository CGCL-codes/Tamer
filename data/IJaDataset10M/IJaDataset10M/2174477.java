package org.personalsmartspace.pm.uiModel.test;

import static org.junit.Assert.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.personalsmartspace.pm.uiModel.api.platform.IUserAction;
import org.personalsmartspace.pm.uiModel.api.platform.IUserTask;
import org.personalsmartspace.pm.uiModel.api.platform.TaskModelData;
import org.personalsmartspace.pm.uiModel.api.platform.UserAction;
import org.personalsmartspace.pm.uiModel.impl.BestMatchingAction;
import org.personalsmartspace.pm.uiModel.impl.TaskAction;
import org.personalsmartspace.pm.uiModel.impl.TaskModelManager;

public class TaskModelTest {

    TaskModelManager manager;

    TaskModelData modelData;

    String symloc = "symLoc";

    String status = "status";

    String tod = "tod";

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.manager = new TaskModelManager();
        ;
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.manager = null;
    }

    void setContext(IUserAction action, Serializable symlocValue, Serializable statusValue, Serializable todValue) {
        HashMap<String, Serializable> context = new HashMap<String, Serializable>();
        context.put(symloc, symlocValue);
        context.put(status, statusValue);
        context.put(tod, todValue);
        action.setActionContext(context);
    }

    @Test
    public void testCreateModel() {
        System.out.println("1. create Actions ");
        IUserAction actionA = manager.createAction("volume", "volume=mute");
        setContext(actionA, "home", "free", "noon");
        actionA.setConfidenceLevel(99);
        System.out.println(" conf level = " + actionA.getConfidenceLevel());
        IUserAction actionB = manager.createAction("volume", "volume=unmute");
        setContext(actionB, "home", "free", "morning");
        IUserAction actionC = manager.createAction("volume", "volume=louder");
        setContext(actionC, "home", "free", "night");
        IUserAction actionD = manager.createAction("volume", "volume=ssshhh");
        IUserAction actionE = manager.createAction("volume", "volume=mute");
        setContext(actionE, "home", "busy", "night");
        IUserAction actionF = manager.createAction("volume", "volume=loud");
        setContext(actionF, "home", "free", "night");
        IUserAction actionH = manager.createAction("volume", "volume=ssshhh");
        IUserAction actionJ = manager.createAction("volume", "volume=lastSound");
        System.out.println("print Actions ");
        System.out.println("A " + actionA);
        System.out.println("B " + actionB);
        System.out.println("C " + actionC);
        System.out.println("D " + actionD);
        System.out.println("E " + actionE);
        System.out.println("F " + actionF);
        System.out.println("H " + actionH);
        System.out.println("J " + actionJ);
        System.out.println("2. Set links among Actions  ");
        this.manager.setNextActionLink(actionA, actionB, 12.0);
        this.manager.setNextActionLink(actionB, actionC, 21.2);
        this.manager.setNextActionLink(actionC, actionD, 10.1);
        this.manager.setNextActionLink(actionE, actionF, 12.0);
        this.manager.setNextActionLink(actionF, actionC, 21.2);
        this.manager.setNextActionLink(actionC, actionH, 45.1);
        this.manager.setNextActionLink(actionH, actionJ, 12.1);
        System.out.println("3. Create Tasks + assign userActions to userTasks  ");
        LinkedHashMap<IUserAction, Double> list = new LinkedHashMap<IUserAction, Double>();
        list.put(actionA, 12.0);
        list.put(actionB, 21.2);
        list.put(actionC, 10.1);
        list.put(actionD, 12.1);
        list.put(actionJ, null);
        IUserTask task1 = this.manager.createTask("task id 1", list);
        LinkedHashMap<IUserAction, Double> list2 = new LinkedHashMap<IUserAction, Double>();
        list2.put(actionE, 12.0);
        list2.put(actionF, 21.2);
        list2.put(actionC, 45.1);
        list2.put(actionH, 12.1);
        IUserTask task2 = this.manager.createTask("task id 2");
        task2.addActions(list2);
        assertEquals(task2.getActions().size(), 4);
        System.out.println("4. set links among Tasks ");
        this.manager.setNextTaskLink(task1, task2.getTaskID(), 12.0);
        task1 = null;
        task2 = null;
        task1 = this.manager.getTask("task id 1");
        task2 = this.manager.getTask("task id 2");
        System.out.println("MODEL CREATED ");
        printModel(this.manager.getTaskModelData());
        System.out.println("*********************** " + this.manager.getNextAction(actionC));
        System.out.println("TASKS ID= " + task1.getTaskID() + " userActions = " + task1.getActions().size());
        assertEquals(task1.getTaskID(), "task id 1");
        assertEquals(task1.getActions().size(), 5);
        System.out.println("TASKS ID= " + task2.getTaskID() + " userActions = " + task2.getActions().size());
        assertEquals(task2.getTaskID(), "task id 2");
        assertEquals(task2.getActions().size(), 4);
        System.out.println("task 1 links= " + this.manager.getNextTask(task1));
        assertEquals((this.manager.getNextTask(task1)).size(), 1);
        System.out.println("task 2 links= " + this.manager.getNextTask(task2));
        LinkedHashMap<IUserAction, Double> task1ActionsMap = (LinkedHashMap<IUserAction, Double>) task1.getActions();
        assertEquals(task1ActionsMap.size(), 5);
        System.out.println(" Model created ************* printing :");
        printModel(this.manager.model);
        System.out.println("########### Start testing retrievals #############");
        List<IUserAction> oneAction = manager.getActionsByType("volume", "volume=loud");
        System.out.println("\n 1 getActionsByType (volume,volume=loud). Size of Actions found=" + oneAction.size() + " action id:" + oneAction.get(0));
        assertEquals(oneAction.size(), 1);
        IUserAction oneUserAction = oneAction.get(0);
        System.out.println("getNextAction for " + oneUserAction + ":" + this.manager.getNextAction(oneUserAction));
        System.out.println("UserAction :" + oneUserAction + " belongs to " + this.manager.getTasks(oneUserAction).size() + " tasks " + this.manager.getTasks(oneUserAction));
        List<IUserAction> twoActions = manager.getActionsByType("volume", "volume=mute");
        System.out.println("\n 2 getActionsByType (volume,volume=mute)  . Size of Actions found=" + twoActions.size() + " action ids:" + twoActions);
        assertEquals(twoActions.size(), 2);
        for (IUserAction userAct : twoActions) {
            System.out.println("UserAction :" + userAct + " belongs to tasks " + this.manager.getTasks(userAct));
            System.out.println("getNextActions for " + userAct.getActionID() + "= " + this.manager.getNextAction(userAct));
        }
        Map<IUserAction, IUserTask> oneActionTwoNext = manager.getActionsAndTaskByType("volume", "volume=louder");
        System.out.println("\n 3.1 getActionsAndTaskByType (volume,volume=louder)  . Size of Actions found=" + oneActionTwoNext.size() + " action id:" + oneActionTwoNext);
        assertEquals(oneActionTwoNext.size(), 1);
        for (IUserAction userActTwoNext : oneActionTwoNext.keySet()) {
            System.out.println("UserAction :" + userActTwoNext + " belongs to task: " + oneActionTwoNext.get(userActTwoNext));
        }
        Map<IUserAction, IUserTask> twoActionsTasks = manager.getActionsAndTaskByType("volume", "volume=mute");
        System.out.println("\n 3.2 getActionsAndTaskByType (volume,volume=mute)  . Size of Actions found=" + twoActionsTasks.size() + " action id:" + twoActionsTasks);
        assertEquals(twoActionsTasks.size(), 2);
        for (IUserAction userActTwoNext : twoActionsTasks.keySet()) {
            System.out.println("UserAction :" + userActTwoNext + " belongs to task: " + twoActionsTasks.get(userActTwoNext));
        }
        List<IUserAction> lastTaskActionList = manager.getActionsByType("volume", "volume=lastSound");
        System.out.println("\n 4 getActionsByType (volume,volume=lastSound)  . Size of Actions found=" + lastTaskActionList.size() + " action id:" + lastTaskActionList);
        assertEquals(lastTaskActionList.size(), 1);
        for (IUserAction lastTaskAction : lastTaskActionList) {
            System.out.println("UserAction :" + lastTaskAction + " belongs to " + this.manager.getTasks(lastTaskAction).size() + " tasks " + this.manager.getTasks(lastTaskAction));
            System.out.println("getNextActions for " + lastTaskAction.getActionID() + "= " + this.manager.getNextAction(lastTaskAction));
        }
        Object[] allActions = (task1ActionsMap.keySet()).toArray();
        UserAction firstAction = (UserAction) allActions[0];
        System.out.println("\n Get transition probabilities");
        if (this.manager.getNextAction(firstAction).size() == 1) {
            Map<IUserAction, Double> nextActionList = (Map<IUserAction, Double>) this.manager.getNextAction(firstAction);
            Object[] act = (nextActionList.keySet()).toArray();
            System.out.println("key = " + act[0]);
            Double d = (Double) nextActionList.get(act[0]);
            System.out.println("next action = " + act[0] + " prob = " + d);
            System.out.println("only one more action exists " + firstAction.toString() + " " + nextActionList);
        }
        UserAction thirdAction = (UserAction) allActions[2];
        if (this.manager.getNextAction(thirdAction).size() == 1) {
            Map nextActions = this.manager.getNextAction(thirdAction);
            System.out.println("only one more action exists " + thirdAction.toString() + " next = " + nextActions);
        } else if (this.manager.getNextAction(thirdAction).size() > 1) {
            Map nextActions = this.manager.getNextAction(thirdAction);
            System.out.println("more than one userActions exists " + thirdAction.toString() + "  next=" + nextActions);
        }
        System.out.println("Action belong to Tasks: " + this.manager.getTasks(thirdAction));
        System.out.println("Action belong to Tasks:" + this.manager.getTasks(firstAction));
        System.out.println("\n End of Testing ");
    }

    @Test
    public void testMatchingFunctions() {
        this.manager.resetTaskModelData();
        System.out.println("**** Starting new Test: testMatchingFunctions ******* ");
        System.out.println("1. create Actions ");
        IUserAction actionA = manager.createAction("volume", "volume=mute");
        setContext(actionA, "home", "free", "noon");
        IUserAction actionB = manager.createAction("volume", "volume=mute");
        setContext(actionB, "home", "busy", "night");
        IUserAction actionC = manager.createAction("volume", "volume=mute");
        setContext(actionC, "bar", "sleeping", "morning");
        IUserAction actionD = manager.createAction("volume", "volume=ssshhh");
        IUserAction actionE = manager.createAction("volume", "volume=mute");
        setContext(actionE, "home", "notFree", "morning");
        IUserAction actionF = manager.createAction("volume", "volume=loud");
        setContext(actionF, "home", "eating", "morning");
        IUserAction actionG = manager.createAction("volume", "volume=mute");
        setContext(actionG, "bar", "sleeping", "morning");
        IUserAction actionH = manager.createAction("volume", "volume=ssshhh");
        IUserAction actionJ = manager.createAction("volume", "volume=lastSound");
        System.out.println("print Actions ");
        System.out.println("A " + actionA.getActionID());
        System.out.println("B " + actionB.getActionID());
        System.out.println("C " + actionC.getActionID());
        System.out.println("D " + actionD.getActionID());
        System.out.println("E " + actionE.getActionID());
        System.out.println("F " + actionF.getActionID());
        System.out.println("G " + actionG.getActionID());
        System.out.println("H " + actionH.getActionID());
        System.out.println("J " + actionJ.getActionID());
        System.out.println("2. Set links among Actions  ");
        this.manager.setNextActionLink(actionA, actionB, 12.0);
        this.manager.setNextActionLink(actionB, actionC, 21.2);
        this.manager.setNextActionLink(actionC, actionD, 10.1);
        this.manager.setNextActionLink(actionE, actionF, 12.0);
        this.manager.setNextActionLink(actionF, actionG, 21.2);
        this.manager.setNextActionLink(actionG, actionH, 45.1);
        this.manager.setNextActionLink(actionH, actionJ, 12.1);
        System.out.println("3. Create Tasks + assign userActions to userTasks  ");
        LinkedHashMap<IUserAction, Double> list = new LinkedHashMap<IUserAction, Double>();
        list.put(actionA, 12.0);
        list.put(actionB, 21.2);
        list.put(actionC, 10.1);
        list.put(actionD, 12.1);
        list.put(actionJ, null);
        IUserTask task1 = this.manager.createTask("task id 1", list);
        LinkedHashMap<IUserAction, Double> list2 = new LinkedHashMap<IUserAction, Double>();
        list2.put(actionE, 12.0);
        list2.put(actionF, 21.2);
        list2.put(actionG, 45.1);
        list2.put(actionH, 12.1);
        IUserTask task2 = this.manager.createTask("task id 2");
        task2.addActions(list2);
        assertEquals(task2.getActions().size(), 4);
        System.out.println("4. set links among Tasks ");
        this.manager.setNextTaskLink(task1, task2.getTaskID(), 12.0);
        System.out.println("***** MODEL CREATED *****  ");
        System.out.println("***** PRINT MODEL *****  ");
        printModel(this.manager.getTaskModelData());
        System.out.println("***** Test bestMatching.getPreviousAction methods ***** ");
        BestMatchingAction bestMatching = new BestMatchingAction();
        TaskAction taskAction1 = new TaskAction(task1, actionA);
        List<IUserAction> previousAction1 = bestMatching.getPreviousActions(taskAction1);
        if (taskAction1 != null && previousAction1 != null) {
            assertNull(previousAction1.get(0));
        }
        if (previousAction1 == null) {
            System.out.println("for Action " + actionA.getActionID() + " previousAction id = null");
        }
        TaskAction taskAction2 = new TaskAction(task1, actionB);
        List<IUserAction> previousAction2 = bestMatching.getPreviousActions(taskAction2);
        if (taskAction2 != null && previousAction2.size() >= 2) {
            System.out.println("for Action " + actionB.getActionID() + " previousAction1 id =" + previousAction2.get(0).getActionID());
            assertEquals("volume=volume=mute/1", previousAction2.get(0).getActionID());
        }
        TaskAction taskAction3 = new TaskAction(task2, actionH);
        List<IUserAction> previousAction3 = bestMatching.getPreviousActions(taskAction3);
        if (taskAction3 != null && previousAction3.size() >= 2) {
            System.out.println("for Action " + actionH.getActionID() + " previousAction id1 =" + previousAction3.get(0).getActionID() + " previousAction id2 =" + previousAction3.get(1).getActionID());
            assertEquals("volume=volume=mute/7", previousAction3.get(0).getActionID());
        }
        System.out.println("***** Test action identification in model based on context ***** ");
        HashMap<String, Serializable> currentContext = new HashMap<String, Serializable>();
        currentContext.put("symLoc", "home");
        currentContext.put("status", "free");
        currentContext.put("tod", "noon");
        String[] previous = new String[2];
        previous[0] = "volume";
        previous[1] = "volume=mute";
        Map<IUserAction, IUserTask> result = this.manager.identifyActionTaskInModel("volume", "volume=mute", currentContext, previous);
        System.out.println("results " + result);
        for (IUserAction act : result.keySet()) {
            System.out.println(" identified actID:  " + act.getActionID());
            assertEquals("volume=volume=mute/1", act.getActionID());
        }
        HashMap<String, Serializable> currentContext2 = new HashMap<String, Serializable>();
        currentContext2.put("symLoc", "home");
        currentContext2.put("status", "busy");
        String[] previous2 = new String[2];
        previous2[0] = "volume";
        previous2[1] = "volume=mute";
        Map<IUserAction, IUserTask> result2 = this.manager.identifyActionTaskInModel("volume", "volume=mute", currentContext2, previous2);
        System.out.println("results " + result);
        for (IUserAction act : result2.keySet()) {
            System.out.println(" identified actID:  " + act.getActionID());
            assertEquals("volume=volume=mute/2", act.getActionID());
        }
        HashMap<String, Serializable> currentContext3 = new HashMap<String, Serializable>();
        currentContext3.put("symLoc", "home");
        currentContext3.put("status", "busy");
        currentContext3.put("tod", "noon");
        String[] previous3 = null;
        Map<IUserAction, IUserTask> result3 = this.manager.identifyActionTaskInModel("volume", "volume=mute", currentContext2, previous2);
        System.out.println("results " + result3);
        if (result3.size() == 1) {
            for (IUserAction act : result3.keySet()) {
                System.out.println(" identified actID:  " + act.getActionID());
                assertEquals("volume=volume=mute/2", act.getActionID());
            }
        }
        HashMap<String, Serializable> currentContext4 = new HashMap<String, Serializable>();
        currentContext4.put("symLoc", "bar");
        currentContext4.put("status", "sleeping");
        currentContext4.put("tod", "morning");
        String[] previous4 = new String[2];
        previous4[0] = "volume";
        previous4[1] = "volume=loud";
        Map<IUserAction, IUserTask> result4 = this.manager.identifyActionTaskInModel("volume", "volume=mute", currentContext4, previous4);
        System.out.println("results " + result4);
        if (result4.size() == 1) {
            for (IUserAction act : result4.keySet()) {
                System.out.println(" identified actID:  " + act.getActionID() + " in task =" + result4.get(act));
                System.out.println(" for context :  " + currentContext4 + " and previous actions =" + previous4[0] + " " + previous4[1]);
            }
        }
        String[] previous5 = new String[4];
        previous5[0] = "volume";
        previous5[1] = "volume=mute";
        previous5[2] = "volume";
        previous5[3] = "volume=mute";
        Map<IUserAction, IUserTask> result5 = this.manager.identifyActionTaskInModel("volume", "volume=mute", currentContext4, previous5);
        System.out.println("results (based on 2 previous actions) " + result5);
        if (result5.size() == 1) {
            for (IUserAction act : result5.keySet()) {
                System.out.println(" identified actID:  " + act.getActionID() + " in task =" + result5.get(act));
                System.out.println(" for context :  " + currentContext4 + " and previous actions =" + previous5[0] + " " + previous5[1]);
                System.out.println(previous5[2] + " " + previous5[3]);
            }
        }
        Map<IUserAction, IUserTask> result6 = this.manager.identifyActionTaskInModel("volume", "volume=ssshhh", null, null);
        System.out.println("identify volume,volume=ssshhh in model (ctx=null, previous=null " + result6);
        printMap(result6);
    }

    @Test
    public void testIdentifyNextAction() {
        this.manager.resetTaskModelData();
        System.out.println("**** Starting new Test: testMatchingFunctions ******* ");
        System.out.println("1. create Actions ");
        IUserAction actionA = manager.createAction("volume", "mute");
        setContext(actionA, "home", "free", "noon");
        IUserAction actionB = manager.createAction("volume", "unmute");
        setContext(actionB, "home", "busy", "night");
        IUserAction actionC = manager.createAction("volume", "mute");
        setContext(actionC, "bar", "sleeping", "morning");
        IUserAction actionD = manager.createAction("volume", "ssshhh");
        IUserAction actionE = manager.createAction("volume", "mute");
        setContext(actionE, "home", "notFree", "morning");
        IUserAction actionF = manager.createAction("volume", "loud");
        setContext(actionF, "home", "eating", "morning");
        IUserAction actionG = manager.createAction("volume", "mute");
        setContext(actionG, "bar", "sleeping", "morning");
        IUserAction actionH = manager.createAction("volume", "ssshhh");
        IUserAction actionJ = manager.createAction("volume", "mute");
        setContext(actionJ, "home", "free", "noon");
        IUserAction actionK = manager.createAction("volume", "lastSound");
        System.out.println("2. Set links among Actions  ");
        this.manager.setNextActionLink(actionA, actionB, 12.0);
        this.manager.setNextActionLink(actionB, actionC, 21.2);
        this.manager.setNextActionLink(actionC, actionD, 10.1);
        this.manager.setNextActionLink(actionE, actionF, 12.0);
        this.manager.setNextActionLink(actionF, actionG, 21.2);
        this.manager.setNextActionLink(actionG, actionH, 45.1);
        this.manager.setNextActionLink(actionH, actionJ, 12.1);
        System.out.println("3. Create Tasks + assign userActions to userTasks  ");
        LinkedHashMap<IUserAction, Double> list = new LinkedHashMap<IUserAction, Double>();
        list.put(actionA, 0.0);
        list.put(actionB, 0.8);
        list.put(actionC, 0.9);
        list.put(actionD, 0.5);
        list.put(actionJ, 0.2);
        IUserTask task1 = this.manager.createTask("task id 1", list);
        LinkedHashMap<IUserAction, Double> list2 = new LinkedHashMap<IUserAction, Double>();
        list2.put(actionE, 0.0);
        list2.put(actionF, 0.99);
        list2.put(actionG, 1.0);
        list2.put(actionK, 1.0);
        list2.put(actionH, 0.89);
        IUserTask task2 = this.manager.createTask("task id 2");
        task2.addActions(list2);
        System.out.println("4. set links among Tasks ");
        this.manager.setNextTaskLink(task1, task2.getTaskID(), 12.0);
        System.out.println("***** MODEL CREATED *****  ");
        System.out.println("***** PRINT MODEL *****  ");
        printModel(this.manager.getTaskModelData());
        System.out.println("\n -------- Start a new prediction process 1------ ");
        System.out.println("  first identify in model an action with par = volume  and  value = mute ");
        HashMap<String, Serializable> currentContext = new HashMap<String, Serializable>();
        currentContext.put("symLoc", "home");
        currentContext.put("status", "free");
        currentContext.put("tod", "noon");
        System.out.println("current context " + currentContext);
        Map<IUserAction, IUserTask> identifiedActionTask1 = this.manager.identifyActionTaskInModel("volume", "mute", currentContext, null);
        System.out.println("Actions with highest scores identified ");
        for (IUserAction act : identifiedActionTask1.keySet()) {
            System.out.println("Action  " + act.getActionID());
        }
        System.out.println("Predict  next action for each action identified and find the one with the best score ");
        Map<String, Serializable> currentContext1 = new HashMap<String, Serializable>();
        currentContext1.put("symLoc", "home");
        currentContext1.put("status", "busy");
        currentContext1.put("tod", "night");
        System.out.println(" current context changed to " + currentContext1);
        IUserAction nextAction1 = this.manager.identifyNextAction(identifiedActionTask1, null, currentContext1);
        System.out.println("predicted action " + nextAction1.getActionID() + " with conf level :" + nextAction1.getConfidenceLevel());
        System.out.println("\n -------- Start a new prediction process 2------  ");
        List<IUserTask> ls2 = this.manager.getTasks(nextAction1);
        IUserTask userTask2 = ls2.get(0);
        Map<IUserAction, IUserTask> userTaskAction2 = new HashMap<IUserAction, IUserTask>();
        userTaskAction2.put(nextAction1, userTask2);
        IUserAction nextAction2 = this.manager.identifyNextAction(userTaskAction2, null, currentContext1);
        System.out.println("current action :" + nextAction1.getActionID());
        System.out.println("next action " + nextAction2.getActionID() + " with conf level :" + nextAction2.getConfidenceLevel());
        System.out.println("\n -------- Start a new prediction process 3------ ");
        List<IUserTask> ls = this.manager.getTasks(nextAction2);
        IUserTask userTask1 = ls.get(0);
        Map<IUserAction, IUserTask> userTaskAction = new HashMap<IUserAction, IUserTask>();
        userTaskAction.put(nextAction2, userTask1);
        currentContext1 = new HashMap<String, Serializable>();
        currentContext1.put("symLoc", "bar");
        currentContext1.put("status", "sleeping");
        currentContext1.put("tod", "morning");
        IUserAction nextAction3 = this.manager.identifyNextAction(userTaskAction, userTask1.getTaskID(), currentContext1);
        System.out.println("current action :" + nextAction2.getActionID());
        System.out.println("next action " + nextAction3.getActionID() + " with conf level :" + nextAction3.getConfidenceLevel());
        System.out.println("\n -------- Start a new prediction process 4------ ");
        Map<IUserAction, IUserTask> identifiedActsTasks = this.manager.identifyActionTaskInModel("volume", "ssshhh", null, null);
        IUserAction nextAction4 = this.manager.identifyNextAction(identifiedActsTasks, null, null);
        System.out.println("current action(s) :");
        printMap(identifiedActsTasks);
        if (nextAction4 != null) {
            System.out.println("next action " + nextAction4.getActionID() + " with conf level :" + nextAction4.getConfidenceLevel());
        }
        System.out.println("STARTING -------------  ");
        Map<IUserAction, IUserTask> identifiedActsTasks_Temp = this.manager.identifyActionTaskInModel("volume", "ssshhh", null, null);
        IUserTask task = null;
        IUserAction act_ = null;
        HashMap<IUserAction, Double> res = new HashMap<IUserAction, Double>();
        for (IUserAction act : identifiedActsTasks_Temp.keySet()) {
            task = identifiedActsTasks_Temp.get(act);
            act_ = act;
            res = this.manager.getNextAction(act_, identifiedActsTasks_Temp.get(act_));
            System.out.println("res " + res);
        }
        for (IUserAction act : res.keySet()) {
            System.out.println("action" + act.getActionID());
        }
    }

    void printModel(TaskModelData model) {
        int number = 1;
        HashMap<IUserTask, HashMap<String, Double>> tasks = model.getTaskList();
        Iterator<IUserTask> tasks_it = tasks.keySet().iterator();
        while (tasks_it.hasNext()) {
            IUserTask nextTask = (IUserTask) tasks_it.next();
            System.out.println("tasks :" + nextTask.getTaskID());
            Map<IUserAction, Double> actions = nextTask.getActions();
            Iterator<IUserAction> actions_it = actions.keySet().iterator();
            while (actions_it.hasNext()) {
                IUserAction nextAction = (IUserAction) actions_it.next();
                System.out.println("actions :" + nextAction.getActionID() + " trans prob:" + actions.get(nextAction) + " context:" + nextAction.getActionContext());
            }
            number++;
        }
    }

    private void printMap(Map<IUserAction, IUserTask> map) {
        for (IUserAction act : map.keySet()) {
            System.out.println("action key:" + act.getActionID() + " task value:" + (map.get(act)).getTaskID());
        }
    }
}
