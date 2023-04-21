package uk.ac.lkl.migen.system.cdst.model.event;

import uk.ac.lkl.migen.system.ai.um.IndicatorClass;
import uk.ac.lkl.migen.system.server.UserSet;

public class TeacherModelAdapter implements TeacherModelListener {

    @Override
    public void indicatorSelected(TeacherModelEvent<IndicatorClass> e) {
    }

    @Override
    public void indicatorDeselected(TeacherModelEvent<IndicatorClass> e) {
    }

    @Override
    public void userSetHighlighted(TeacherModelEvent<UserSet> e) {
    }

    @Override
    public void userSetUnhighlighted(TeacherModelEvent<UserSet> e) {
    }

    @Override
    public void maxTimeChanged(TeacherModelEvent<Long> e) {
    }
}
