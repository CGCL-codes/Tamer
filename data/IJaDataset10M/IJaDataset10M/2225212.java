package org.yarl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import net.sf.jweather.metar.Metar;
import net.sf.jweather.metar.MetarFetcher;
import net.sf.jweather.metar.MetarParseException;
import net.sf.jweather.metar.MetarParser;
import org.yarl.db.Course;
import org.yarl.db.CourseDelegate;
import org.yarl.db.Equipment;
import org.yarl.db.EquipmentDelegate;
import org.yarl.db.Person;
import org.yarl.db.PersonDelegate;
import org.yarl.db.SegmentJoin;
import org.yarl.db.Workout;
import org.yarl.db.WorkoutDelegate;
import org.yarl.db.WorkoutSubType;
import org.yarl.db.WorkoutType;
import org.yarl.db.WorkoutTypeDelegate;
import org.yarl.util.TimeUtil;
import org.apache.log4j.Logger;
import fi.mmm.yhteinen.swing.core.YModel;

/**
 * Workout model.
 * 
 * @author Prescott Balch
 */
public class WorkoutModel extends YModel {

    private static final long serialVersionUID = 1L;

    static Logger log4j = Logger.getLogger(WorkoutModel.class);

    private Date workoutDate;

    private Date workoutTime;

    private Person person;

    private WorkoutType workoutType;

    private String workoutTypeName;

    private WorkoutSubType workoutSubType;

    private String workoutSubTypeName;

    private Course course;

    private String courseName;

    private Equipment equipment;

    private String equipmentName;

    private String totalTime;

    private String averagePace;

    private boolean newWorkout = false;

    private Workout workout;

    private Collection<SegmentJoin> segments;

    public Person getDefaultPerson() {
        Person p = PersonDelegate.findDefaultPerson();
        return p;
    }

    /**
	 * Saves Workout data by calling "remote application" via delegate class:
	 */
    public void save() {
        log4j.debug("save");
        newWorkout = false;
        log4j.debug("saving: " + workout.toString());
        WorkoutDelegate.saveWorkout(getWorkout(), getSegments());
    }

    /**
	 * Deletes Workout by calling "remote application" via delegate class.
	 *
	 */
    public void delete() {
        log4j.debug("deleting workout: " + getWorkout().toString());
        WorkoutDelegate.delete(getWorkout(), getSegments());
        setNewWorkout();
    }

    /**
	 * Initializes model for a new Workout.
	 */
    public void setNewWorkout() {
        setWorkout(new Workout());
        log4j.debug("before messing with workout: " + getWorkout().toString());
        setWorkoutDate(getWorkout().getWorkoutDateTime());
        setWorkoutTime(getWorkout().getWorkoutStartTime());
        newWorkout = true;
        getWorkout().setPersonId(person.getPersonId());
        WorkoutType defaultWorkoutType = WorkoutTypeDelegate.findDefaultWorkoutType();
        getWorkout().setWorkoutTypeId(defaultWorkoutType.getWorkoutTypeId());
        getWorkout().setWorkoutSubTypeId(WorkoutTypeDelegate.findDefaultWorkoutSubType(defaultWorkoutType).getWorkoutSubTypeId());
        log4j.debug("newWorkout is " + getWorkout().toString());
        setSegments(new ArrayList<SegmentJoin>());
        addNewSegment();
        log4j.debug("newWorkout is true");
        log4j.debug("newWorkout is: " + getWorkout().toString());
    }

    /**
	 * @return if the Workout in the model is a new one (not saved yet).
	 */
    public boolean isNewWorkout() {
        log4j.debug("newWorkout = " + newWorkout);
        return newWorkout;
    }

    public Workout getWorkout() {
        return workout;
    }

    /**
	 * This method notifies WorkoutView that workout has changed.
	 */
    public void setWorkout(Workout workout) {
        if (workout == null) {
            log4j.debug("workout is null");
            this.workout = null;
            this.newWorkout = false;
        }
        log4j.debug("setWorkout() to " + workout.toString());
        this.newWorkout = false;
        this.workout = workout;
        resetInternalFields();
        notifyObservers("workout");
    }

    public void setWorkoutDate(Date workoutDate) {
        log4j.debug("workoutDate being set to " + workoutDate.toString());
        this.workoutDate = workoutDate;
        getWorkout().setWorkoutDateTime(workoutDate);
    }

    public Date getWorkoutDate() {
        return workoutDate;
    }

    public void setWorkoutTime(Date workoutTime) {
        log4j.debug("workoutTime being set to " + workoutTime.toString());
        this.workoutTime = workoutTime;
        getWorkout().setWorkoutStartTime(workoutTime);
    }

    public Date getWorkoutTime() {
        return workoutTime;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    public void setWorkoutType(WorkoutType workoutType) {
        if (workoutType == null) {
            return;
        }
        log4j.debug("workoutType is now: " + workoutType.toString());
        this.workoutType = workoutType;
        getWorkout().setWorkoutTypeId(workoutType.getWorkoutTypeId());
        log4j.debug("notifyObservers of workoutType change");
        notifyObservers("workoutType");
    }

    public WorkoutType getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutSubType(WorkoutSubType workoutSubType) {
        if (workoutSubType == null) {
            return;
        }
        log4j.debug("workoutSubType is now: " + workoutSubType.toString());
        this.workoutSubType = workoutSubType;
        getWorkout().setWorkoutSubTypeId(workoutSubType.getWorkoutSubTypeId());
        notifyObservers("workoutSubType");
    }

    public WorkoutSubType getWorkoutSubType() {
        return workoutSubType;
    }

    public void setCourse(Course course) {
        this.course = course;
        getWorkout().setCourseId(course.getCourseId());
        log4j.debug("course id is " + course.getCourseId() + " and name is " + course.getName());
        refreshMetarData(false);
        notifyObservers("course");
    }

    public Course getCourse() {
        return course;
    }

    public void setEquipment(Equipment equipment) {
        log4j.debug("equipment: " + equipment.toString());
        this.equipment = equipment;
        getWorkout().setEquipmentId(equipment.getEquipmentId());
        notifyObservers("equipment");
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setSegments(Collection<SegmentJoin> segments) {
        log4j.debug("setSegments");
        this.segments = segments;
        getWorkout().recalculate(segments);
        notifyObservers("segments");
        notifyObservers("workout");
    }

    public Collection<SegmentJoin> getSegments() {
        return segments;
    }

    private long getMaxSegmentNumber() {
        long max = 0;
        if (getSegments() == null) {
            return 1;
        }
        if (getSegments().size() > 0) {
            Iterator<SegmentJoin> iter = getSegments().iterator();
            while (iter.hasNext()) {
                SegmentJoin sj = (SegmentJoin) iter.next();
                if (sj.getSegment().getSegmentNumber() > max) {
                    max = sj.getSegment().getSegmentNumber();
                }
            }
        }
        return max + 1;
    }

    public void addNewSegment() {
        SegmentJoin newSegmentJoin = new SegmentJoin();
        newSegmentJoin.getSegment().setWorkoutId(getWorkout().getWorkoutId());
        newSegmentJoin.getSegment().setSegmentNumber(getMaxSegmentNumber());
        newSegmentJoin.getSegment().setPersonId(getWorkout().getPersonId());
        newSegmentJoin.setSegmentWorkoutType(WorkoutTypeDelegate.findWorkoutType(getWorkout().getWorkoutTypeId()));
        newSegmentJoin.setSegmentWorkoutSubType(WorkoutTypeDelegate.findWorkoutSubType(getWorkout().getWorkoutSubTypeId()));
        Collection<SegmentJoin> c = getSegments();
        c.add(newSegmentJoin);
        setSegments(c);
        log4j.debug("newSegmentJoin added: " + newSegmentJoin.toString());
    }

    public void recalculate() {
        log4j.debug("recalculate");
        if (getSegments() == null) {
            return;
        }
        if (getSegments().size() == 0) {
            return;
        }
        getWorkout().recalculate(getSegments());
        notifyObservers("workout");
        setTotalTime(TimeUtil.format(getWorkout().getTotalTime(), true));
        setAveragePace(TimeUtil.format(getWorkout().getAveragePace(), true));
        getWorkout().setAverageSpeed(TimeUtil.calculateSpeed(getWorkout().getTotalDistance(), getWorkout().getTotalTime()));
        notifyObservers("workout.averageSpeed");
        Iterator<SegmentJoin> iter = getSegments().iterator();
        while (iter.hasNext()) {
            SegmentJoin sj = (SegmentJoin) iter.next();
            sj.getSegment().recalculate();
            sj.setSegmentWorkoutType(WorkoutTypeDelegate.findWorkoutType(getWorkout().getWorkoutTypeId()));
            sj.setSegmentWorkoutSubType(WorkoutTypeDelegate.findWorkoutSubType(getWorkout().getWorkoutSubTypeId()));
        }
    }

    private void resetInternalFields() {
        log4j.debug("resetInternalFields");
        if (getWorkout().getWorkoutTypeId() > 0) {
            log4j.debug("workoutTypeId = " + getWorkout().getWorkoutTypeId());
            WorkoutType wt = WorkoutTypeDelegate.findWorkoutType(getWorkout().getWorkoutTypeId());
            if (wt != null) {
                setWorkoutType(wt);
                setWorkoutTypeName(wt.getName());
            }
        }
        if (getWorkout().getWorkoutSubTypeId() > 0) {
            log4j.debug("workoutSubTypeId = " + getWorkout().getWorkoutSubTypeId());
            WorkoutSubType wst = WorkoutTypeDelegate.findWorkoutSubType(getWorkout().getWorkoutSubTypeId());
            if (wst != null) {
                setWorkoutSubType(wst);
                setWorkoutSubTypeName(wst.getName());
            }
        }
        if (getWorkout().getCourseId() > 0) {
            log4j.debug("courseId = " + getWorkout().getCourseId());
            Course c = CourseDelegate.findCourse(getWorkout().getCourseId());
            if (c != null) {
                setCourse(c);
                setCourseName(c.getName());
            }
        }
        if (getWorkout().getEquipmentId() > 0) {
            log4j.debug("equipmentId = " + getWorkout().getEquipmentId());
            Equipment e = EquipmentDelegate.findEquipment(getWorkout().getEquipmentId());
            if (e != null) {
                setEquipment(e);
                setEquipmentName(e.getNickname());
            }
        }
        setTotalTime(TimeUtil.format(getWorkout().getTotalTime(), false));
        setAveragePace(TimeUtil.format(getWorkout().getAveragePace(), false));
        log4j.debug("totalTime = " + getTotalTime());
        log4j.debug("averagePace = " + getAveragePace());
    }

    public void setWorkoutTypeName(WorkoutType workoutType) {
        setWorkoutTypeName(workoutType.getName());
    }

    public void setWorkoutTypeName(String workoutTypeName) {
        log4j.debug("setting workoutTypeName to: " + workoutTypeName);
        this.workoutTypeName = workoutTypeName;
        if ((getWorkoutType() == null) || (!workoutTypeName.equals(getWorkoutType().getName()))) {
            setWorkoutType(WorkoutTypeDelegate.findWorkoutType(workoutTypeName));
        }
        log4j.debug("workoutTypeName is now " + this.workoutTypeName);
        notifyObservers("workoutTypeName");
    }

    public String getWorkoutTypeName() {
        return workoutTypeName;
    }

    public void setWorkoutSubTypeName(String workoutSubTypeName) {
        this.workoutSubTypeName = workoutSubTypeName;
        if ((getWorkoutSubType() == null) || (!workoutSubTypeName.equals(getWorkoutSubType().getName()))) {
            setWorkoutSubType(WorkoutTypeDelegate.findWorkoutSubType(workoutSubTypeName));
        }
        log4j.debug("workoutSubTypeName is now " + this.workoutSubTypeName);
        notifyObservers("workoutSubTypeName");
    }

    public String getWorkoutSubTypeName() {
        return workoutSubTypeName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
        if ((getCourse() == null) || (courseName.compareTo(getCourse().getName()) != 0)) {
            setCourse(CourseDelegate.findCourse(courseName));
        }
        log4j.debug("courseName is now " + this.courseName);
        notifyObservers("courseName");
    }

    public String getCourseName() {
        return courseName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
        if ((getEquipment() == null) || (!equipmentName.equals(getEquipment().getNickname()))) {
            setEquipment(EquipmentDelegate.findEquipment(equipmentName));
        }
        log4j.debug("equipmentName is now " + this.equipmentName);
        notifyObservers("equipmentName");
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
        notifyObservers("totalTime");
    }

    public String getAveragePace() {
        return averagePace;
    }

    public void setAveragePace(String averagePace) {
        this.averagePace = averagePace;
        notifyObservers("averagePace");
    }

    public void refreshMetarData(boolean updateWorkoutYesOrNo) {
        Metar metar;
        if ((getCourse().getWeatherStation() != null) && (getCourse().getWeatherStation().length() > 0)) {
            String metarData = MetarFetcher.fetch(getCourse().getWeatherStation());
            if (metarData.length() == 0) {
                return;
            }
            try {
                metar = MetarParser.parseRecord(metarData);
            } catch (MetarParseException e1) {
                log4j.warn("Could not get Metar data for station.");
                return;
            }
            if (updateWorkoutYesOrNo) {
                getWorkout().setTemperature(metar.getTemperatureInFahrenheit());
                getWorkout().setHumidity(metar.getHumidity());
                getWorkout().setWindDirection(metar.getWindDirectionLetters());
                getWorkout().setWindSpeed(metar.getWindSpeedInMPH());
                notifyObservers("workout");
            }
        }
    }
}
