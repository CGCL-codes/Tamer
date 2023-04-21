package com.daveoxley.cnery.entities;

import com.daveoxley.cbus.CGateException;
import com.daveoxley.cbus.CGateSession;
import com.daveoxley.cbus.Group;
import com.daveoxley.cnery.util.LookupTable;
import com.daveoxley.cnery.util.Sun;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import javax.faces.model.SelectItem;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.jboss.seam.Component;
import org.jboss.seam.contexts.Contexts;

/**
 *
 * @author Dave Oxley <dave@daveoxley.co.uk>
 */
@MappedSuperclass
public abstract class AbstractCondition extends BaseEntity implements Serializable {

    private char actionType;

    private char timeAfterBefore;

    private char timeWhen;

    private char timePlusMinus;

    private int timeMinutes;

    private String dependGroup;

    private Scene dependScene;

    private char sceneGroupOnOff;

    private static final LookupTable<Character, String> BEFORE = new LookupTable<Character, String>('B', "before");

    private static final LookupTable<Character, String> AFTER = new LookupTable<Character, String>('A', "after");

    private static final HashMap<Character, LookupTable<Character, String>> timeAfterBeforeMap;

    private static final SelectItem[] timeAfterBeforeOptions;

    private static final LookupTable<Character, String> SUNSET = new LookupTable<Character, String>('S', "sunset");

    private static final LookupTable<Character, String> SUNRISE = new LookupTable<Character, String>('R', "sunrise");

    private static final LookupTable<Character, String> MIDNIGHT = new LookupTable<Character, String>('M', "midnight");

    private static final HashMap<Character, LookupTable<Character, String>> timeWhenMap;

    private static final SelectItem[] timeWhenOptions;

    private static final LookupTable<Character, String> PLUS = new LookupTable<Character, String>('P', "plus");

    private static final LookupTable<Character, String> MINUS = new LookupTable<Character, String>('M', "minus");

    private static final HashMap<Character, LookupTable<Character, String>> timePlusMinusMap;

    private static final SelectItem[] timePlusMinusOptions;

    private static final LookupTable<Character, String> GROUP_ON = new LookupTable<Character, String>('O', "on");

    private static final LookupTable<Character, String> GROUP_OFF = new LookupTable<Character, String>('F', "off");

    private static final LookupTable<Character, String> SCENE_ACTIVE = new LookupTable<Character, String>('A', "active");

    private static final LookupTable<Character, String> SCENE_INACTIVE = new LookupTable<Character, String>('I', "inactive");

    private static final HashMap<Character, LookupTable<Character, String>> sceneGroupOnOffMap;

    private static final SelectItem[] groupOnOffOptions;

    private static final SelectItem[] sceneOnOffOptions;

    static {
        timeAfterBeforeMap = new HashMap<Character, LookupTable<Character, String>>();
        timeAfterBeforeMap.put(BEFORE.getKey(), BEFORE);
        timeAfterBeforeMap.put(AFTER.getKey(), AFTER);
        timeAfterBeforeOptions = new SelectItem[2];
        timeAfterBeforeOptions[0] = new SelectItem(BEFORE.getKey(), BEFORE.getDescription());
        timeAfterBeforeOptions[1] = new SelectItem(AFTER.getKey(), AFTER.getDescription());
        timeWhenMap = new HashMap<Character, LookupTable<Character, String>>();
        timeWhenMap.put(SUNSET.getKey(), SUNSET);
        timeWhenMap.put(SUNRISE.getKey(), SUNRISE);
        timeWhenMap.put(MIDNIGHT.getKey(), MIDNIGHT);
        timeWhenOptions = new SelectItem[3];
        timeWhenOptions[0] = new SelectItem(SUNSET.getKey(), SUNSET.getDescription());
        timeWhenOptions[1] = new SelectItem(SUNRISE.getKey(), SUNRISE.getDescription());
        timeWhenOptions[2] = new SelectItem(MIDNIGHT.getKey(), MIDNIGHT.getDescription());
        timePlusMinusMap = new HashMap<Character, LookupTable<Character, String>>();
        timePlusMinusMap.put(PLUS.getKey(), PLUS);
        timePlusMinusMap.put(MINUS.getKey(), MINUS);
        timePlusMinusOptions = new SelectItem[2];
        timePlusMinusOptions[0] = new SelectItem(PLUS.getKey(), PLUS.getDescription());
        timePlusMinusOptions[1] = new SelectItem(MINUS.getKey(), MINUS.getDescription());
        sceneGroupOnOffMap = new HashMap<Character, LookupTable<Character, String>>();
        sceneGroupOnOffMap.put(GROUP_ON.getKey(), GROUP_ON);
        sceneGroupOnOffMap.put(GROUP_OFF.getKey(), GROUP_OFF);
        sceneGroupOnOffMap.put(SCENE_ACTIVE.getKey(), SCENE_ACTIVE);
        sceneGroupOnOffMap.put(SCENE_INACTIVE.getKey(), SCENE_INACTIVE);
        groupOnOffOptions = new SelectItem[2];
        groupOnOffOptions[0] = new SelectItem(GROUP_ON.getKey(), GROUP_ON.getDescription());
        groupOnOffOptions[1] = new SelectItem(GROUP_OFF.getKey(), GROUP_OFF.getDescription());
        sceneOnOffOptions = new SelectItem[2];
        sceneOnOffOptions[0] = new SelectItem(SCENE_ACTIVE.getKey(), SCENE_ACTIVE.getDescription());
        sceneOnOffOptions[1] = new SelectItem(SCENE_INACTIVE.getKey(), SCENE_INACTIVE.getDescription());
    }

    @Column(name = "action_type")
    public char getActionType() {
        return actionType;
    }

    public void setActionType(char actionType) {
        this.actionType = actionType;
    }

    @Column(name = "time_after_before")
    public char getTimeAfterBefore() {
        return timeAfterBefore;
    }

    public void setTimeAfterBefore(char timeAfterBefore) {
        this.timeAfterBefore = timeAfterBefore;
    }

    @Column(name = "time_when")
    public char getTimeWhen() {
        return timeWhen;
    }

    public void setTimeWhen(char timeWhen) {
        this.timeWhen = timeWhen;
    }

    @Column(name = "time_plus_minus")
    public char getTimePlusMinus() {
        return timePlusMinus;
    }

    public void setTimePlusMinus(char timePlusMinus) {
        this.timePlusMinus = timePlusMinus;
    }

    @Column(name = "time_minutes")
    public int getTimeMinutes() {
        return timeMinutes;
    }

    public void setTimeMinutes(int timeMinutes) {
        this.timeMinutes = timeMinutes;
    }

    @Column(name = "depend_group")
    public String getDependGroup() {
        return dependGroup;
    }

    public void setDependGroup(String dependGroup) {
        this.dependGroup = dependGroup;
    }

    @ManyToOne(targetEntity = Scene.class)
    @JoinColumn(name = "depend_scene_id")
    public Scene getDependScene() {
        return dependScene;
    }

    public void setDependScene(Scene dependScene) {
        this.dependScene = dependScene;
    }

    @Column(name = "scene_group_on_off")
    public char getSceneGroupOnOff() {
        return sceneGroupOnOff;
    }

    public void setSceneGroupOnOff(char sceneGroupOnOff) {
        this.sceneGroupOnOff = sceneGroupOnOff;
    }

    @Transient
    public String getTimeAfterBeforeDescription() {
        LookupTable<Character, String> lookup = timeAfterBeforeMap.get(getTimeAfterBefore());
        return lookup == null ? "" : lookup.getDescription();
    }

    @Transient
    public static SelectItem[] getTimeAfterBeforeOptions() {
        return timeAfterBeforeOptions;
    }

    @Transient
    public String getTimeWhenDescription() {
        LookupTable<Character, String> lookup = timeWhenMap.get(getTimeWhen());
        return lookup == null ? "" : lookup.getDescription();
    }

    @Transient
    public static SelectItem[] getTimeWhenOptions() {
        return timeWhenOptions;
    }

    @Transient
    public String getTimePlusMinusDescription() {
        LookupTable<Character, String> lookup = timePlusMinusMap.get(getTimePlusMinus());
        return lookup == null ? "" : lookup.getDescription();
    }

    @Transient
    public static SelectItem[] getTimePlusMinusOptions() {
        return timePlusMinusOptions;
    }

    @Transient
    public String getSceneGroupOnOffDescription() {
        LookupTable<Character, String> lookup = sceneGroupOnOffMap.get(getSceneGroupOnOff());
        return lookup == null ? "" : lookup.getDescription();
    }

    @Transient
    public static SelectItem[] getGroupOnOffOptions() {
        return groupOnOffOptions;
    }

    @Transient
    public static SelectItem[] getSceneOnOffOptions() {
        return sceneOnOffOptions;
    }

    @Transient
    public String getDependGroupName() {
        try {
            String address = getDependGroup();
            if (address == null) return "";
            CGateSession cGateSession = (CGateSession) Contexts.getApplicationContext().get("cGateSession");
            Group group = (Group) cGateSession.getCGateObject(address);
            if (group != null) return group.getName();
        } catch (Exception e) {
            new CGateException(e);
        }
        return "";
    }

    @Transient
    public String getDependSceneName() {
        Scene scene = getDependScene();
        if (scene != null) return scene.getName();
        return "";
    }

    public GregorianCalendar getActionGregorian(GregorianCalendar currentTime) throws Exception {
        if (getActionType() != 'T') throw new IllegalStateException();
        int minutes = getTimeMinutes();
        if (getTimePlusMinus() == MINUS.getKey()) {
            minutes *= -1;
        } else if (getTimePlusMinus() != PLUS.getKey()) throw new IllegalStateException();
        Sun sun = (Sun) Component.getInstance("sun");
        GregorianCalendar adjustedTime = (GregorianCalendar) currentTime.clone();
        adjustedTime.add(Calendar.MINUTE, minutes * -1);
        GregorianCalendar actionTime;
        if (getTimeAfterBefore() == AFTER.getKey()) {
            if (getTimeWhen() == SUNSET.getKey()) {
                actionTime = sun.previousSunset(adjustedTime);
            } else if (getTimeWhen() == SUNRISE.getKey()) {
                actionTime = sun.previousSunrise(adjustedTime);
            } else if (getTimeWhen() == MIDNIGHT.getKey()) {
                actionTime = (GregorianCalendar) adjustedTime.clone();
                actionTime.set(Calendar.HOUR_OF_DAY, 0);
                actionTime.set(Calendar.MINUTE, 0);
                actionTime.set(Calendar.SECOND, 0);
                actionTime.set(Calendar.MILLISECOND, 0);
            } else throw new IllegalStateException();
        } else if (getTimeAfterBefore() == BEFORE.getKey()) {
            if (getTimeWhen() == SUNSET.getKey()) {
                actionTime = sun.nextSunset(adjustedTime);
            } else if (getTimeWhen() == SUNRISE.getKey()) {
                actionTime = sun.nextSunrise(adjustedTime);
            } else if (getTimeWhen() == MIDNIGHT.getKey()) {
                actionTime = (GregorianCalendar) adjustedTime.clone();
                actionTime.add(Calendar.DAY_OF_YEAR, 1);
                actionTime.set(Calendar.HOUR_OF_DAY, 0);
                actionTime.set(Calendar.MINUTE, 0);
                actionTime.set(Calendar.SECOND, 0);
                actionTime.set(Calendar.MILLISECOND, 0);
            } else throw new IllegalStateException();
        } else throw new IllegalStateException();
        actionTime.add(Calendar.MINUTE, minutes);
        return actionTime;
    }

    @Transient
    public String getActiveActionTime() throws Exception {
        if (getActionType() != 'T') return "";
        GregorianCalendar currentTime = new GregorianCalendar();
        GregorianCalendar previousActionTime = getActionGregorian(currentTime);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String previousActionTimeString = "Today at ";
        if (previousActionTime.get(Calendar.DAY_OF_YEAR) < currentTime.get(Calendar.DAY_OF_YEAR)) previousActionTimeString = "Yesterday at "; else if (previousActionTime.get(Calendar.DAY_OF_YEAR) > currentTime.get(Calendar.DAY_OF_YEAR)) previousActionTimeString = "Tomorrow at ";
        return previousActionTimeString + formatter.format(previousActionTime.getTime());
    }

    public GregorianCalendar getNextActionGregorian(GregorianCalendar currentTime) throws Exception {
        if (getActionType() != 'T') throw new IllegalStateException();
        int minutes = getTimeMinutes();
        if (getTimePlusMinus() == MINUS.getKey()) {
            minutes *= -1;
        } else if (getTimePlusMinus() != PLUS.getKey()) throw new IllegalStateException();
        Sun sun = (Sun) Component.getInstance("sun");
        GregorianCalendar adjustedTime = (GregorianCalendar) currentTime.clone();
        adjustedTime.add(Calendar.MINUTE, minutes * -1);
        GregorianCalendar actionTime;
        if (getTimeWhen() == SUNSET.getKey()) {
            actionTime = sun.nextSunset(adjustedTime);
        } else if (getTimeWhen() == SUNRISE.getKey()) {
            actionTime = sun.nextSunrise(adjustedTime);
        } else if (getTimeWhen() == MIDNIGHT.getKey()) {
            actionTime = (GregorianCalendar) adjustedTime.clone();
            actionTime.add(Calendar.DAY_OF_YEAR, 1);
            actionTime.set(Calendar.HOUR_OF_DAY, 0);
            actionTime.set(Calendar.MINUTE, 0);
            actionTime.set(Calendar.SECOND, 0);
            actionTime.set(Calendar.MILLISECOND, 0);
        } else throw new IllegalStateException();
        actionTime.add(Calendar.MINUTE, minutes);
        return actionTime;
    }

    @Transient
    public String getNextActionTime() throws Exception {
        if (getActionType() != 'T') return "";
        GregorianCalendar currentTime = new GregorianCalendar();
        GregorianCalendar nextActionTime = getNextActionGregorian(currentTime);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String nextActionTimeString = "Today at ";
        if (nextActionTime.get(Calendar.DAY_OF_YEAR) > currentTime.get(Calendar.DAY_OF_YEAR)) nextActionTimeString = "Tomorrow at ";
        return nextActionTimeString + formatter.format(nextActionTime.getTime());
    }
}
