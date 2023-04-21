package org.libreplan.business.planner.entities;

import java.util.Collection;
import java.util.Set;
import org.libreplan.business.scenarios.entities.Scenario;
import org.libreplan.business.workingday.IntraDayDate;

/**
 * Represents a container of day assignments. Its purpose is to the day
 * assignments for each scenario.
 *
 * @author Óscar González Fernández
 */
public interface IDayAssignmentsContainer<T extends DayAssignment> {

    Set<T> getDayAssignments();

    Scenario getScenario();

    void addAll(Collection<? extends T> assignments);

    void removeAll(Collection<? extends DayAssignment> assignments);

    void resetTo(Collection<T> assignments);

    IntraDayDate getIntraDayStart();

    void setIntraDayStart(IntraDayDate intraDayStart);

    IntraDayDate getIntraDayEnd();

    void setIntraDayEnd(IntraDayDate intraDayEnd);
}
