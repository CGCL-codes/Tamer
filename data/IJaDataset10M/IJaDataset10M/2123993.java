package uk.ac.lkl.migen.system.cdst.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import uk.ac.lkl.migen.system.cdst.model.UserSetIndicatorModel;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModel;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModelImpl;
import uk.ac.lkl.migen.system.expresser.test.StandaloneExpresserTest;
import uk.ac.lkl.migen.system.server.DummyUser;
import uk.ac.lkl.migen.system.server.UserSet;
import uk.ac.lkl.migen.system.task.TaskIdentifier;

public class TestUserSetIndicatorModel extends StandaloneExpresserTest implements TestAbstractTeacherToolModelConstants {

    @Test(expected = IllegalArgumentException.class)
    public void testAddModel() {
        UserSet user = new UserSet(new DummyUser());
        UserSetIndicatorModel model = new UserSetIndicatorModel(user, now);
        model.addIndicatorModel(expresserModel1);
        assertEquals(1, model.getModelCount());
        model.addIndicatorModel(expresserModel2);
        assertEquals(2, model.getModelCount());
        model.addIndicatorModel(expresserModel3);
        assertEquals(3, model.getModelCount());
        model.addIndicatorModel(expresserModel1);
    }

    @Test
    public void testAutoSelectMostRecentlyUpdatedModel() {
        UserSet user = new UserSet(new DummyUser());
        UserSetIndicatorModel model = new UserSetIndicatorModel(user, now);
        assertEquals(null, model.getMostRecentlyUpdatedExpresserModel());
        model.addIndicatorModel(expresserModel1);
        model.addIndicatorModel(expresserModel2);
        model.addEventIndicatorOccurrence(expresserModel1, evOccurrence100);
        assertEquals(expresserModel1, model.getMostRecentlyUpdatedExpresserModel());
        assertEquals(expresserModel2, model.getMostRecentlyAddedExpresserModel());
        model.addEventIndicatorOccurrence(expresserModel2, evOccurrence200);
        assertEquals(expresserModel2, model.getMostRecentlyUpdatedExpresserModel());
        assertEquals(expresserModel2, model.getMostRecentlyAddedExpresserModel());
        model.addStateIndicatorOccurrence(expresserModel1, stOccurrence300);
        assertEquals(expresserModel1, model.getMostRecentlyUpdatedExpresserModel());
        assertEquals(expresserModel2, model.getMostRecentlyAddedExpresserModel());
        model.addStateIndicatorOccurrence(expresserModel1, stOccurrence400);
        assertEquals(expresserModel1, model.getMostRecentlyUpdatedExpresserModel());
        assertEquals(expresserModel2, model.getMostRecentlyAddedExpresserModel());
        model.addStateIndicatorOccurrence(expresserModel2, stOccurrence500);
        assertEquals(expresserModel2, model.getMostRecentlyUpdatedExpresserModel());
        assertEquals(expresserModel2, model.getMostRecentlyAddedExpresserModel());
    }

    @Test
    public void testIsWorkingOnTask() {
        UserSet user = new UserSet(new DummyUser());
        UserSetIndicatorModel model = new UserSetIndicatorModel(user, inTheFuture);
        assertEquals(null, model.getMostRecentlyUpdatedExpresserModel());
        ExpresserModel traintrackModel = new ExpresserModelImpl() {

            public TaskIdentifier getTaskIdentifier() {
                return TaskIdentifier.TRAINTRACK;
            }
        };
        ExpresserModel gardenModel = new ExpresserModelImpl() {

            public TaskIdentifier getTaskIdentifier() {
                return TaskIdentifier.GARDEN;
            }
        };
        model.addIndicatorModel(traintrackModel);
        model.addIndicatorModel(gardenModel);
        assertFalse(model.isStudentWorkingOnTask(TaskIdentifier.TRAINTRACK));
        assertFalse(model.isStudentWorkingOnTask(TaskIdentifier.GARDEN));
        model.addEventIndicatorOccurrence(traintrackModel, evOccurrence100);
        assertTrue(model.isStudentWorkingOnTask(TaskIdentifier.TRAINTRACK));
        assertFalse(model.isStudentWorkingOnTask(TaskIdentifier.GARDEN));
        assertFalse(model.isStudentWorkingOnTask(TaskIdentifier.COLLABORATION_TRAINTRACK));
        model.addEventIndicatorOccurrence(gardenModel, evOccurrence200);
        model.addEventIndicatorOccurrence(traintrackModel, evOccurrence300);
        assertTrue(model.isStudentWorkingOnTask(TaskIdentifier.TRAINTRACK));
        assertFalse(model.isStudentWorkingOnTask(TaskIdentifier.GARDEN));
        model.addEventIndicatorOccurrence(gardenModel, evOccurrence400);
        assertFalse(model.isStudentWorkingOnTask(TaskIdentifier.TRAINTRACK));
        assertTrue(model.isStudentWorkingOnTask(TaskIdentifier.GARDEN));
    }

    @Test
    public void testGetStudentCurrentTask() {
        UserSet user = new UserSet(new DummyUser());
        UserSetIndicatorModel model = new UserSetIndicatorModel(user, inTheFuture);
        assertEquals(null, model.getMostRecentlyUpdatedExpresserModel());
        ExpresserModel traintrack = new ExpresserModelImpl() {

            public TaskIdentifier getTaskIdentifier() {
                return TaskIdentifier.TRAINTRACK;
            }
        };
        ExpresserModel garden = new ExpresserModelImpl() {

            public TaskIdentifier getTaskIdentifier() {
                return TaskIdentifier.GARDEN;
            }
        };
        model.addIndicatorModel(traintrack);
        model.addIndicatorModel(garden);
        assertFalse(model.getStudentCurrentTask() == TaskIdentifier.TRAINTRACK);
        assertFalse(model.getStudentCurrentTask() == TaskIdentifier.GARDEN);
        model.addEventIndicatorOccurrence(traintrack, evOccurrence100);
        assertTrue(model.getStudentCurrentTask() == TaskIdentifier.TRAINTRACK);
        assertFalse(model.getStudentCurrentTask() == TaskIdentifier.GARDEN);
        assertFalse(model.getStudentCurrentTask() == TaskIdentifier.COLLABORATION_TRAINTRACK);
        model.addEventIndicatorOccurrence(garden, evOccurrence200);
        model.addEventIndicatorOccurrence(traintrack, evOccurrence300);
        assertTrue(model.getStudentCurrentTask() == TaskIdentifier.TRAINTRACK);
        assertFalse(model.getStudentCurrentTask() == TaskIdentifier.GARDEN);
        model.addEventIndicatorOccurrence(garden, evOccurrence400);
        assertFalse(model.getStudentCurrentTask() == TaskIdentifier.TRAINTRACK);
        assertTrue(model.getStudentCurrentTask() == TaskIdentifier.GARDEN);
    }
}
