package uk.ac.lkl.migen.system.server.handler.UserSet.ExpresserModel;

import java.sql.SQLException;
import java.util.List;
import uk.ac.lkl.common.util.restlet.*;
import uk.ac.lkl.common.util.restlet.server.*;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModel;
import uk.ac.lkl.migen.system.server.UserSet;
import uk.ac.lkl.migen.system.server.manipulator.GoalEventOccurrenceTableManipulator;
import uk.ac.lkl.migen.system.task.goal.GoalEventOccurrence;

public class GoalEventOccurrenceHandler extends EntityHandler<GoalEventOccurrenceTableManipulator> {

    private LinkingTableManipulator<ExpresserModel, GoalEventOccurrence> linker;

    public GoalEventOccurrenceHandler(GoalEventOccurrenceTableManipulator manipulator) throws RestletException {
        super(manipulator);
        linker = new LinkingTableManipulator<ExpresserModel, GoalEventOccurrence>(manipulator.getManager(), ExpresserModel.class, GoalEventOccurrence.class);
    }

    public EntityId<GoalEventOccurrence> addGoalEventOccurrence(EntityId<UserSet> userSetId, EntityId<ExpresserModel> modelId, GoalEventOccurrence occurrence, EntityMapper mapper) throws SQLException, RestletException {
        return linker.addLink(modelId, occurrence, mapper);
    }

    public void removeGoalEventOccurrence(EntityId<UserSet> userSetId, EntityId<ExpresserModel> modelId, EntityId<GoalEventOccurrence> goalEventId) throws RestletException {
        throw new RestletException("Cannot remove Intervention");
    }

    public GoalEventOccurrence getGoalEventOccurrence(EntityId<UserSet> userSetId, EntityId<ExpresserModel> modelId, EntityId<GoalEventOccurrence> goalEventOccurrenceId, EntityMapper mapper) throws SQLException, RestletException {
        return manipulator.selectObject(goalEventOccurrenceId, mapper);
    }

    public void setGoalEventOccurrence(EntityId<UserSet> userSetId, EntityId<ExpresserModel> modelId, EntityId<GoalEventOccurrence> goalEventId, GoalEventOccurrence occurrence, EntityMapper mapper) throws RestletException {
        throw new RestletException("Cannot update Goal Event");
    }

    public List<GoalEventOccurrence> getGoalEventOccurrenceList(EntityId<UserSet> userSetId, EntityId<ExpresserModel> modelId, EntityFilter<GoalEventOccurrence> filter, EntityMapper mapper) throws SQLException, RestletException {
        return linker.getChildEntityList(modelId, filter, mapper);
    }

    public List<GoalEventOccurrence> getGoalEventOccurrenceList(EntityId<UserSet> userSetId, EntityId<ExpresserModel> modelId, EntityMapper mapper) throws SQLException, RestletException {
        return linker.getChildEntityList(modelId, null, mapper);
    }
}
