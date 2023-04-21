package org.personalsmartspace.pm.uiModel.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.personalsmartspace.pm.uiModel.api.platform.IUserAction;
import org.personalsmartspace.pm.uiModel.api.platform.IUserTask;

public class BestMatchingAction {

    double transition_param = 0.33;

    double sameTask_param = 0.33;

    double context_param = 0.33;

    public BestMatchingAction() {
    }

    public BestMatchingAction(double transition_param_, double sameTask_param_, double context_param_) {
        transition_param = transition_param_;
        sameTask_param = sameTask_param_;
        context_param = context_param_;
    }

    public void setPredictionParams(double transition_param_, double sameTask_param_, double context_param_) {
        transition_param = transition_param_;
        sameTask_param = sameTask_param_;
        context_param = context_param_;
    }

    public List<TaskAction> bestMatchingIdentification(Map<IUserAction, IUserTask> actionsAndTaskList, HashMap<String, Serializable> currentContext, String lastPerformedActions[]) {
        String lastPerformedActionsArray[] = lastPerformedActions;
        List<TaskAction> winnerTaskAction = null;
        double scoreTotal = 0.0;
        List<Double> allscores = new ArrayList<Double>();
        Map<TaskAction, Double> candidates = new HashMap<TaskAction, Double>();
        for (IUserAction action : actionsAndTaskList.keySet()) {
            IUserTask task = actionsAndTaskList.get(action);
            TaskAction taskAction = new TaskAction(task, action);
            scoreTotal = calculateActionIdentificationScore(taskAction, currentContext, lastPerformedActionsArray);
            candidates.put(taskAction, scoreTotal);
        }
        winnerTaskAction = getMaxIdentificationScore(candidates);
        return winnerTaskAction;
    }

    public List<TaskAction> getMaxIdentificationScore(Map<TaskAction, Double> candidates) {
        List<TaskAction> results = new ArrayList<TaskAction>();
        Double maxScore = new Double(0.0);
        Double currentScore = new Double(0.0);
        for (TaskAction taskAct : candidates.keySet()) {
            currentScore = candidates.get(taskAct);
            if (currentScore >= maxScore) {
                maxScore = currentScore;
            }
        }
        for (TaskAction taskAct : candidates.keySet()) {
            Double score = candidates.get(taskAct);
            if (score.equals(maxScore)) {
                results.add(taskAct);
            }
        }
        return results;
    }

    public IUserAction getMaxPredictionScore(Map<IUserAction, Double> candidatesActsNScores) {
        IUserAction result = null;
        Double maxScore = new Double(0.0);
        Double currentScore = new Double(0.0);
        for (IUserAction userAct : candidatesActsNScores.keySet()) {
            currentScore = candidatesActsNScores.get(userAct);
            if (currentScore >= maxScore) {
                maxScore = currentScore;
                result = userAct;
            }
        }
        return result;
    }

    public Double calculateContextScore(HashMap<String, Serializable> currentContext, HashMap<String, Serializable> actionsContext) {
        Double score = 0.0;
        int counter = 0;
        Double finalScore = 0.0;
        if (actionsContext != null && currentContext != null && actionsContext.size() > 0) {
            for (String contextType : actionsContext.keySet()) {
                if (actionsContext.get(contextType) != null) {
                    Serializable actionValue = actionsContext.get(contextType);
                    String actionValueToString = actionValue.toString().toLowerCase();
                    if (currentContext.get(contextType) != null) {
                        Serializable currentCtxValue = currentContext.get(contextType);
                        if (currentCtxValue.toString() != null) {
                            String currentCtxValueToString = currentCtxValue.toString().toLowerCase();
                            if (currentCtxValueToString.equalsIgnoreCase(actionValueToString)) {
                                score = score + 1.0;
                            }
                        }
                    }
                }
            }
            finalScore = score / (actionsContext.size());
        }
        return finalScore;
    }

    private double calculateActionIdentificationScore(TaskAction taskAction, HashMap<String, Serializable> currentContext, String lastAction[]) {
        double score = 0.0;
        double context_param = 0.5;
        double prevAct_param = 0.5;
        double score_context = 0.0;
        double score_previousActions = 0.0;
        IUserAction action = taskAction.getAction();
        IUserTask task = taskAction.getTask();
        if (action.getActionContext() != null) {
            HashMap<String, Serializable> actionsCtx = action.getActionContext();
            if (currentContext != null && actionsCtx != null) {
                score_context = calculateContextScore(currentContext, actionsCtx);
            }
        }
        if (lastAction != null && lastAction.length == 2) {
            String paramName = lastAction[0];
            String value = lastAction[1];
            List<IUserAction> previousActionList = getPreviousActions(taskAction);
            IUserAction previousAction = previousActionList.get(0);
            if (previousAction != null) {
                String previousValue = previousAction.getvalue();
                if (previousAction.getparameterName().equals(paramName) && previousValue.equals(value)) {
                    score_previousActions = 0.5;
                }
            }
        }
        if (lastAction != null && lastAction.length == 4) {
            String lastParamName = lastAction[0];
            String lastValue = lastAction[1];
            String previousLastParamName = lastAction[2];
            String previousLastValue = lastAction[3];
            List<IUserAction> previousActionsList = getPreviousActions(taskAction);
            IUserAction previousAction = previousActionsList.get(0);
            if (previousAction != null) {
                if (previousAction.getparameterName().equals(lastParamName) && previousAction.getvalue().equals(lastValue)) {
                    score_previousActions = 0.5;
                    if (previousAction.getparameterName().equals(previousLastParamName) && previousAction.getvalue().equals(previousLastValue)) {
                        score_previousActions = score_previousActions + 0.5;
                    }
                }
            }
        }
        score = context_param * score_context + prevAct_param * score_previousActions;
        return score;
    }

    public double calculateActionPredictionScore(HashMap<IUserAction, Double> candidateNextActProbMap, boolean prevActionBelongsInSameTask, Map<String, Serializable> currentContext) {
        double score = 0.0;
        double trans_prob = 0.0;
        double sameTaskScore = 0.0;
        double contextScore = 0.0;
        if (candidateNextActProbMap.size() != 0) {
            List<IUserAction> candidateNextAct = new ArrayList<IUserAction>(candidateNextActProbMap.keySet());
            IUserAction candidateNextAction = candidateNextAct.get(0);
            if (candidateNextActProbMap.get(candidateNextAction) != null) {
                trans_prob = candidateNextActProbMap.get(candidateNextAction);
            }
            HashMap<String, Serializable> actionsCtx = candidateNextAction.getActionContext();
            contextScore = calculateContextScore((HashMap<String, Serializable>) currentContext, actionsCtx);
            if (prevActionBelongsInSameTask) sameTaskScore = 1;
            if (!prevActionBelongsInSameTask) sameTaskScore = 0;
            score = transition_param * trans_prob + sameTask_param * sameTaskScore + context_param * contextScore;
        }
        return score;
    }

    public List<IUserAction> getPreviousActions(TaskAction taskAction) {
        IUserAction previousAction = null;
        IUserAction firstResultAct = null;
        IUserAction secondResultAct = null;
        List<IUserAction> result = new ArrayList<IUserAction>();
        IUserTask currentTask = taskAction.getTask();
        IUserAction currentAction = taskAction.getAction();
        LinkedHashMap<IUserAction, Double> actionsMap = (LinkedHashMap<IUserAction, Double>) currentTask.getActions();
        for (IUserAction tempAction : actionsMap.keySet()) {
            if (tempAction.equals(currentAction)) {
                firstResultAct = previousAction;
            }
            previousAction = tempAction;
        }
        previousAction = null;
        for (IUserAction tempAction : actionsMap.keySet()) {
            if (tempAction.equals(firstResultAct)) {
                secondResultAct = previousAction;
            }
            previousAction = tempAction;
        }
        result.add(0, firstResultAct);
        result.add(1, secondResultAct);
        return result;
    }

    public IUserAction getLastPreviousAction(TaskAction taskAction) {
        IUserAction previousAction = null;
        IUserAction result = null;
        IUserTask currentTask = taskAction.getTask();
        IUserAction currentAction = taskAction.getAction();
        LinkedHashMap<IUserAction, Double> actionsMap = (LinkedHashMap<IUserAction, Double>) currentTask.getActions();
        for (IUserAction tempAction : actionsMap.keySet()) {
            if (tempAction.equals(currentAction)) {
                result = previousAction;
            }
            previousAction = tempAction;
        }
        return result;
    }
}
