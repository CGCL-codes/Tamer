package com.nokia.ats4.appmodel.model.persistence.impl;

import com.nokia.ats4.appmodel.model.domain.statemachine.GuardCondition;
import com.nokia.ats4.appmodel.model.persistence.TransitionEdge;
import com.nokia.ats4.appmodel.model.domain.Keyword;
import com.nokia.ats4.appmodel.model.domain.UserActionCommand;
import com.nokia.ats4.appmodel.model.domain.Weight;
import com.nokia.ats4.appmodel.model.domain.statemachine.Activity;
import com.nokia.ats4.appmodel.model.domain.statemachine.impl.DynamicFlagActivity;
import com.nokia.ats4.appmodel.model.domain.statemachine.impl.DynamicFlagGuard;
import generated.GuardConditions.Condition;
import generated.Kendo;
import generated.Keywords;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implements TransitionEdge. This is used when loading model from XML.
 *
 * @author Jani Vesterinen
 * @version $Revision: 59 $
 */
public class LoadTransitionEdgeImpl implements TransitionEdge {

    private long id;

    private long sourceNode;

    private long targetNode;

    private String eventId = null;

    private String sourcePort = null;

    private String targetPort = null;

    private Weight weight = null;

    private String name = null;

    private String description = null;

    private List<UserActionCommand> userAction = new ArrayList<UserActionCommand>();

    private List<GuardCondition> guardConditions = new ArrayList<GuardCondition>();

    private List<Activity> activities = new ArrayList<Activity>();

    /**
     * Creates new instance of TransitionEdge
     * @param transition Kendo.Transitions.Transition
     */
    public LoadTransitionEdgeImpl(Kendo.Product.Transitions.Transition transition) {
        id = transition.getId();
        eventId = transition.getEventId();
        sourceNode = transition.getFrom();
        targetNode = transition.getTo();
        sourcePort = transition.getFromport();
        targetPort = transition.getToport();
        weight = new Weight(transition.getWeight() != null ? transition.getWeight().floatValue() : 0);
        name = transition.getName();
        description = transition.getDescription();
        if (transition.getKeywords() != null) {
            for (Keywords.Keyword keyword : transition.getKeywords().getKeyword()) {
                boolean isGenerated = keyword.isGenerated() == null ? false : keyword.isGenerated();
                userAction.add(new UserActionCommand(new Keyword(keyword.getType()), keyword.getParameter(), isGenerated));
            }
        }
        if (transition.getGuardConditions() != null) {
            for (Condition c : transition.getGuardConditions().getCondition()) {
                if (GuardCondition.GuardType.FLAG.toString().equals(c.getType())) {
                    GuardCondition guard = new DynamicFlagGuard(c.getKey(), c.isRequiredValue());
                    guardConditions.add(guard);
                }
            }
        }
        if (transition.getActivities() != null) {
            for (Kendo.Product.Transitions.Transition.Activities.Activity a : transition.getActivities().getActivity()) {
                if (Activity.ActivityType.SET_FLAG_VALUE.toString().equals(a.getType())) {
                    Activity act = new DynamicFlagActivity(a.getKey(), a.isValue());
                    activities.add(act);
                }
            }
        }
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public String getSourcePortName() {
        return sourcePort;
    }

    @Override
    public String getTargetPortName() {
        return targetPort;
    }

    @Override
    public Weight getWeight() {
        return weight;
    }

    @Override
    public long getSourceNodeId() {
        return sourceNode;
    }

    @Override
    public long getTargetNodeId() {
        return targetNode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Iterator<UserActionCommand> getUserActionCommands() {
        return userAction.iterator();
    }

    @Override
    public List<GuardCondition> getGuardConditions() {
        return this.guardConditions;
    }

    @Override
    public List<Activity> getActivities() {
        return this.activities;
    }
}
