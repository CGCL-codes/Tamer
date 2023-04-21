package educate.sis.timetable;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class ConstraintManagerImpl {

    private int teacherMaxSlotPerDay = 5;

    public ConstraintManager getConstraintManager(ClassroomSlot classroom) {
        ConstraintManager constraintManager = new ConstraintManager();
        Constraint teacherConstraint = new TeacherConstraint(classroom);
        constraintManager.addConstraint(teacherConstraint);
        Constraint studentConstraint = new StudentConstraint(classroom);
        constraintManager.addConstraint(studentConstraint);
        Constraint slotConstraint = new SlotConstraint(classroom);
        constraintManager.addConstraint(slotConstraint);
        return constraintManager;
    }
}
