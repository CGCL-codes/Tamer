package org.yarl.db;

import java.io.Serializable;
import java.util.Collection;
import org.apache.log4j.Logger;

/**
 * @author pxbalch
 *
 */
public class EquipmentJoin implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Logger log4j = Logger.getLogger(EquipmentJoin.class);

    private WorkoutType workoutType;

    private Collection<Equipment> equipments;

    public EquipmentJoin() {
        log4j.debug("default constructor");
    }

    public void setWorkoutType(WorkoutType workoutType) {
        this.workoutType = workoutType;
    }

    public WorkoutType getWorkoutType() {
        return workoutType;
    }

    public void setEquipments(Collection<Equipment> equipments) {
        this.equipments = equipments;
    }

    public Collection<Equipment> getEquipments() {
        return equipments;
    }

    public int compareTo(EquipmentJoin ej) {
        log4j.debug("equipmentJoin.compareTo");
        return getWorkoutType().getName().compareTo(ej.getWorkoutType().getName());
    }
}
