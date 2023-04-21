package uk.ac.lkl.migen.system.server.manipulator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import uk.ac.lkl.common.util.reflect.GenericClass;
import uk.ac.lkl.common.util.reflect.StaticInstanceMap;
import uk.ac.lkl.common.util.reflect.StaticInstanceMapManager;
import uk.ac.lkl.common.util.restlet.EntityMapper;
import uk.ac.lkl.common.util.restlet.RestletException;
import uk.ac.lkl.common.util.restlet.server.EntityId;
import uk.ac.lkl.common.util.restlet.server.EntityManipulator;
import uk.ac.lkl.common.util.restlet.server.EntityTableManipulator;
import uk.ac.lkl.common.util.restlet.server.LinkingTableManipulator;
import uk.ac.lkl.common.util.restlet.server.TableManipulatorManager;
import uk.ac.lkl.migen.system.ai.reasoning.ReasonerType;
import uk.ac.lkl.migen.system.ai.um.TaskModelAttribute;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModel;
import uk.ac.lkl.migen.system.task.CommonMisconstruction;
import uk.ac.lkl.migen.system.task.ConstructionExpressionTask;
import uk.ac.lkl.migen.system.task.ConstructionType;
import uk.ac.lkl.migen.system.task.ExpectedConstruction;
import uk.ac.lkl.migen.system.task.ExpressionType;
import uk.ac.lkl.migen.system.task.TaskIdentifier;
import uk.ac.lkl.migen.system.task.VariablePresentationType;
import uk.ac.lkl.migen.system.task.goal.Goal;
import uk.ac.lkl.migen.system.util.StaticNumberObjectUtilities;

public class ConstructionExpressionTaskTableManipulator extends EntityTableManipulator<ConstructionExpressionTask> {

    private StaticInstanceMap<TaskIdentifier> taskIdentifierMap;

    private EntityManipulator<ExpresserModel> expresserModelManipulator;

    private LinkingTableManipulator<ConstructionExpressionTask, ExpectedConstruction> expectedConstructionsManipulator;

    private LinkingTableManipulator<ConstructionExpressionTask, CommonMisconstruction> commonMisconstructionsManipulator;

    private LinkingTableManipulator<ConstructionExpressionTask, Goal> goalManipulator;

    private LinkingTableManipulator<ConstructionExpressionTask, TaskModelAttribute> learningObjectiveManipulator;

    public ConstructionExpressionTaskTableManipulator(Connection connection) throws SQLException {
        super(connection, GenericClass.getSimple(ConstructionExpressionTask.class), "Task");
        try {
            taskIdentifierMap = StaticInstanceMapManager.getStaticInstanceMap(TaskIdentifier.class);
        } catch (IllegalArgumentException e) {
            throw new SQLException("Can't create static instance maps");
        }
    }

    @Override
    protected void extractManipulators(TableManipulatorManager manager) throws RestletException {
        expresserModelManipulator = manager.getManipulator(ExpresserModel.class);
        expectedConstructionsManipulator = manager.getLinkingManipulator(ConstructionExpressionTask.class, ExpectedConstruction.class);
        commonMisconstructionsManipulator = manager.getLinkingManipulator(ConstructionExpressionTask.class, CommonMisconstruction.class);
        goalManipulator = manager.getLinkingManipulator(ConstructionExpressionTask.class, Goal.class);
        learningObjectiveManipulator = manager.getLinkingManipulator(ConstructionExpressionTask.class, TaskModelAttribute.class);
    }

    @Override
    public String[] getColumnNames() {
        return new String[] { "taskIdentifierId", "constructionExpressionTypeId", "numberOfVariables", "variablePresentationTypeId", "constructionTypeId", "reasonerTypeId", "feedbackGeneratorTypeId" };
    }

    @Override
    protected ConstructionExpressionTask extractObject(ResultSet resultSet, EntityMapper mapper) throws SQLException, RestletException {
        EntityId<ConstructionExpressionTask> constructionExpressionId = getId(resultSet);
        int taskIdentifierId = resultSet.getInt("taskIdentifierId");
        TaskIdentifier taskIdentifier = taskIdentifierMap.getInstance(taskIdentifierId);
        EntityId<ExpresserModel> initialModelId = getId(resultSet, "initialModelId");
        ExpresserModel initialModel = expresserModelManipulator.selectObject(initialModelId, mapper);
        int expressionTypeId = resultSet.getInt("expressionTypeId");
        ExpressionType expressionType = StaticNumberObjectUtilities.get(ExpressionType.class, expressionTypeId);
        int numberOfVariables = resultSet.getInt("numberOfVariables");
        int variablePresentationTypeId = resultSet.getInt("variablePresentationTypeId");
        VariablePresentationType variablePresentationType = StaticNumberObjectUtilities.get(VariablePresentationType.class, variablePresentationTypeId);
        int constructionTypeId = resultSet.getInt("constructionTypeId");
        ConstructionType constructionType = StaticNumberObjectUtilities.get(ConstructionType.class, constructionTypeId);
        int reasonerTypeId = resultSet.getInt("reasonerTypeId");
        ReasonerType reasonerType = StaticNumberObjectUtilities.get(ReasonerType.class, reasonerTypeId);
        List<ExpectedConstruction> expectedConstructions = expectedConstructionsManipulator.getChildEntityList(constructionExpressionId, null, mapper);
        List<CommonMisconstruction> commonMisconstructions = commonMisconstructionsManipulator.getChildEntityList(constructionExpressionId, null, mapper);
        List<TaskModelAttribute> learningObjectives = new ArrayList<TaskModelAttribute>();
        learningObjectiveManipulator.getChildEntityList(constructionExpressionId, null, mapper);
        List<Goal> goals = new ArrayList<Goal>();
        goalManipulator.getChildEntityList(constructionExpressionId, null, mapper);
        ConstructionExpressionTask task = new ConstructionExpressionTask(taskIdentifier, expectedConstructions, commonMisconstructions, initialModel, expressionType, numberOfVariables, variablePresentationType, constructionType, reasonerType, learningObjectives, goals);
        return task;
    }

    @Override
    protected void populateInsertStatement(PreparedStatement statement, ConstructionExpressionTask task, EntityMapper mapper) throws SQLException {
        ConstructionType constructionType = task.getConstructionType();
        int constructionTypeId = constructionType.getId();
        statement.setInt(4, constructionTypeId);
        int numberOfVariables = task.getNumberOfVariables();
        statement.setInt(5, numberOfVariables);
        VariablePresentationType variablePresentationType = task.getVariablePresentationType();
        int variablePresentationTypeId = variablePresentationType.getId();
        statement.setInt(6, variablePresentationTypeId);
    }

    protected void processInsertedObject(ConstructionExpressionTask task, int id, EntityMapper mapper) throws SQLException, RestletException {
        List<ExpectedConstruction> expectedConstructions = task.getExpectedConstructionList();
        for (ExpectedConstruction expectedConstruction : expectedConstructions) expectedConstructionsManipulator.addLink(new EntityId<ConstructionExpressionTask>(id), expectedConstruction, mapper);
        List<CommonMisconstruction> commonMisconstructions = task.getCommonMisconstructionList();
        for (CommonMisconstruction commonMisconstruction : commonMisconstructions) commonMisconstructionsManipulator.addLink(new EntityId<ConstructionExpressionTask>(id), commonMisconstruction, mapper);
    }

    @Override
    protected void populateUpdateStatement(PreparedStatement statement, ConstructionExpressionTask task) throws SQLException {
    }

    public List<ConstructionExpressionTask> getTaskList(EntityMapper mapper) throws SQLException {
        return null;
    }
}
