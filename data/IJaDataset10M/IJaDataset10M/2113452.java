package com.garmin.fit;

public class SessionMesg extends Mesg implements MesgWithEvent {

    public SessionMesg() {
        super(Factory.createMesg(MesgNum.SESSION));
    }

    public SessionMesg(final Mesg mesg) {
        super(mesg);
    }

    /**
    * Get message_index field
    * Comment: Selected bit is set for the current session.
    *
    * @return message_index
    */
    public Integer getMessageIndex() {
        return getFieldIntegerValue(254);
    }

    /**
    * Set message_index field
    * Comment: Selected bit is set for the current session.
    *
    * @param messageIndex
    */
    public void setMessageIndex(Integer messageIndex) {
        setFieldValue("message_index", messageIndex);
    }

    /**
    * Get timestamp field
    * Units: s
    * Comment: Sesson end time.
    *
    * @return timestamp
    */
    public DateTime getTimestamp() {
        return timestampToDateTime(getFieldLongValue(253));
    }

    /**
    * Set timestamp field
    * Units: s
    * Comment: Sesson end time.
    *
    * @param timestamp
    */
    public void setTimestamp(DateTime timestamp) {
        setFieldValue("timestamp", timestamp.getTimestamp());
    }

    /**
    * Get event field
    * Comment: session
    *
    * @return event
    */
    public Event getEvent() {
        Short value = getFieldShortValue(0);
        if (value == null) return null;
        return Event.getByValue(value);
    }

    /**
    * Set event field
    * Comment: session
    *
    * @param event
    */
    public void setEvent(Event event) {
        setFieldValue("event", event.value);
    }

    /**
    * Get event_type field
    * Comment: stop
    *
    * @return event_type
    */
    public EventType getEventType() {
        Short value = getFieldShortValue(1);
        if (value == null) return null;
        return EventType.getByValue(value);
    }

    /**
    * Set event_type field
    * Comment: stop
    *
    * @param eventType
    */
    public void setEventType(EventType eventType) {
        setFieldValue("event_type", eventType.value);
    }

    /**
    * Get start_time field
    *
    * @return start_time
    */
    public DateTime getStartTime() {
        return timestampToDateTime(getFieldLongValue(2));
    }

    /**
    * Set start_time field
    *
    * @param startTime
    */
    public void setStartTime(DateTime startTime) {
        setFieldValue("start_time", startTime.getTimestamp());
    }

    /**
    * Get start_position_lat field
    * Units: semicircles
    *
    * @return start_position_lat
    */
    public Integer getStartPositionLat() {
        return getFieldIntegerValue(3);
    }

    /**
    * Set start_position_lat field
    * Units: semicircles
    *
    * @param startPositionLat
    */
    public void setStartPositionLat(Integer startPositionLat) {
        setFieldValue("start_position_lat", startPositionLat);
    }

    /**
    * Get start_position_long field
    * Units: semicircles
    *
    * @return start_position_long
    */
    public Integer getStartPositionLong() {
        return getFieldIntegerValue(4);
    }

    /**
    * Set start_position_long field
    * Units: semicircles
    *
    * @param startPositionLong
    */
    public void setStartPositionLong(Integer startPositionLong) {
        setFieldValue("start_position_long", startPositionLong);
    }

    /**
    * Get sport field
    *
    * @return sport
    */
    public Sport getSport() {
        Short value = getFieldShortValue(5);
        if (value == null) return null;
        return Sport.getByValue(value);
    }

    /**
    * Set sport field
    *
    * @param sport
    */
    public void setSport(Sport sport) {
        setFieldValue("sport", sport.value);
    }

    /**
    * Get sub_sport field
    *
    * @return sub_sport
    */
    public SubSport getSubSport() {
        Short value = getFieldShortValue(6);
        if (value == null) return null;
        return SubSport.getByValue(value);
    }

    /**
    * Set sub_sport field
    *
    * @param subSport
    */
    public void setSubSport(SubSport subSport) {
        setFieldValue("sub_sport", subSport.value);
    }

    /**
    * Get total_elapsed_time field
    * Units: s
    * Comment: Time (includes pauses)
    *
    * @return total_elapsed_time
    */
    public Float getTotalElapsedTime() {
        return getFieldFloatValue(7);
    }

    /**
    * Set total_elapsed_time field
    * Units: s
    * Comment: Time (includes pauses)
    *
    * @param totalElapsedTime
    */
    public void setTotalElapsedTime(Float totalElapsedTime) {
        setFieldValue("total_elapsed_time", totalElapsedTime);
    }

    /**
    * Get total_timer_time field
    * Units: s
    * Comment: Timer Time (excludes pauses)
    *
    * @return total_timer_time
    */
    public Float getTotalTimerTime() {
        return getFieldFloatValue(8);
    }

    /**
    * Set total_timer_time field
    * Units: s
    * Comment: Timer Time (excludes pauses)
    *
    * @param totalTimerTime
    */
    public void setTotalTimerTime(Float totalTimerTime) {
        setFieldValue("total_timer_time", totalTimerTime);
    }

    /**
    * Get total_distance field
    * Units: m
    *
    * @return total_distance
    */
    public Float getTotalDistance() {
        return getFieldFloatValue(9);
    }

    /**
    * Set total_distance field
    * Units: m
    *
    * @param totalDistance
    */
    public void setTotalDistance(Float totalDistance) {
        setFieldValue("total_distance", totalDistance);
    }

    /**
    * Get total_cycles field
    * Units: cycles
    *
    * @return total_cycles
    */
    public Long getTotalCycles() {
        return getFieldLongValue(10);
    }

    /**
    * Set total_cycles field
    * Units: cycles
    *
    * @param totalCycles
    */
    public void setTotalCycles(Long totalCycles) {
        setFieldValue("total_cycles", totalCycles);
    }

    /**
    * Get total_strides field
    * Units: strides
    *
    * @return total_strides
    */
    public Long getTotalStrides() {
        return getFieldLongValue("total_strides");
    }

    /**
    * Set total_strides field
    * Units: strides
    *
    * @param totalStrides
    */
    public void setTotalStrides(Long totalStrides) {
        setFieldValue("total_strides", totalStrides);
    }

    /**
    * Get total_calories field
    * Units: kcal
    *
    * @return total_calories
    */
    public Integer getTotalCalories() {
        return getFieldIntegerValue(11);
    }

    /**
    * Set total_calories field
    * Units: kcal
    *
    * @param totalCalories
    */
    public void setTotalCalories(Integer totalCalories) {
        setFieldValue("total_calories", totalCalories);
    }

    /**
    * Get total_fat_calories field
    * Units: kcal
    *
    * @return total_fat_calories
    */
    public Integer getTotalFatCalories() {
        return getFieldIntegerValue(13);
    }

    /**
    * Set total_fat_calories field
    * Units: kcal
    *
    * @param totalFatCalories
    */
    public void setTotalFatCalories(Integer totalFatCalories) {
        setFieldValue("total_fat_calories", totalFatCalories);
    }

    /**
    * Get avg_speed field
    * Units: m/s
    * Comment: total_distance / total_timer_time
    *
    * @return avg_speed
    */
    public Float getAvgSpeed() {
        return getFieldFloatValue(14);
    }

    /**
    * Set avg_speed field
    * Units: m/s
    * Comment: total_distance / total_timer_time
    *
    * @param avgSpeed
    */
    public void setAvgSpeed(Float avgSpeed) {
        setFieldValue("avg_speed", avgSpeed);
    }

    /**
    * Get max_speed field
    * Units: m/s
    *
    * @return max_speed
    */
    public Float getMaxSpeed() {
        return getFieldFloatValue(15);
    }

    /**
    * Set max_speed field
    * Units: m/s
    *
    * @param maxSpeed
    */
    public void setMaxSpeed(Float maxSpeed) {
        setFieldValue("max_speed", maxSpeed);
    }

    /**
    * Get avg_heart_rate field
    * Units: bpm
    * Comment: average heart rate (excludes pause time)
    *
    * @return avg_heart_rate
    */
    public Short getAvgHeartRate() {
        return getFieldShortValue(16);
    }

    /**
    * Set avg_heart_rate field
    * Units: bpm
    * Comment: average heart rate (excludes pause time)
    *
    * @param avgHeartRate
    */
    public void setAvgHeartRate(Short avgHeartRate) {
        setFieldValue("avg_heart_rate", avgHeartRate);
    }

    /**
    * Get max_heart_rate field
    * Units: bpm
    *
    * @return max_heart_rate
    */
    public Short getMaxHeartRate() {
        return getFieldShortValue(17);
    }

    /**
    * Set max_heart_rate field
    * Units: bpm
    *
    * @param maxHeartRate
    */
    public void setMaxHeartRate(Short maxHeartRate) {
        setFieldValue("max_heart_rate", maxHeartRate);
    }

    /**
    * Get avg_cadence field
    * Units: rpm
    * Comment: total_cycles / total_timer_time if non_zero_avg_cadence otherwise total_cycles / total_elapsed_time
    *
    * @return avg_cadence
    */
    public Short getAvgCadence() {
        return getFieldShortValue(18);
    }

    /**
    * Set avg_cadence field
    * Units: rpm
    * Comment: total_cycles / total_timer_time if non_zero_avg_cadence otherwise total_cycles / total_elapsed_time
    *
    * @param avgCadence
    */
    public void setAvgCadence(Short avgCadence) {
        setFieldValue("avg_cadence", avgCadence);
    }

    /**
    * Get avg_running_cadence field
    * Units: strides/min
    *
    * @return avg_running_cadence
    */
    public Short getAvgRunningCadence() {
        return getFieldShortValue("avg_running_cadence");
    }

    /**
    * Set avg_running_cadence field
    * Units: strides/min
    *
    * @param avgRunningCadence
    */
    public void setAvgRunningCadence(Short avgRunningCadence) {
        setFieldValue("avg_running_cadence", avgRunningCadence);
    }

    /**
    * Get max_cadence field
    * Units: rpm
    *
    * @return max_cadence
    */
    public Short getMaxCadence() {
        return getFieldShortValue(19);
    }

    /**
    * Set max_cadence field
    * Units: rpm
    *
    * @param maxCadence
    */
    public void setMaxCadence(Short maxCadence) {
        setFieldValue("max_cadence", maxCadence);
    }

    /**
    * Get max_running_cadence field
    * Units: strides/min
    *
    * @return max_running_cadence
    */
    public Short getMaxRunningCadence() {
        return getFieldShortValue("max_running_cadence");
    }

    /**
    * Set max_running_cadence field
    * Units: strides/min
    *
    * @param maxRunningCadence
    */
    public void setMaxRunningCadence(Short maxRunningCadence) {
        setFieldValue("max_running_cadence", maxRunningCadence);
    }

    /**
    * Get avg_power field
    * Units: watts
    * Comment: total_power / total_timer_time if non_zero_avg_power otherwise total_power / total_elapsed_time
    *
    * @return avg_power
    */
    public Integer getAvgPower() {
        return getFieldIntegerValue(20);
    }

    /**
    * Set avg_power field
    * Units: watts
    * Comment: total_power / total_timer_time if non_zero_avg_power otherwise total_power / total_elapsed_time
    *
    * @param avgPower
    */
    public void setAvgPower(Integer avgPower) {
        setFieldValue("avg_power", avgPower);
    }

    /**
    * Get max_power field
    * Units: watts
    *
    * @return max_power
    */
    public Integer getMaxPower() {
        return getFieldIntegerValue(21);
    }

    /**
    * Set max_power field
    * Units: watts
    *
    * @param maxPower
    */
    public void setMaxPower(Integer maxPower) {
        setFieldValue("max_power", maxPower);
    }

    /**
    * Get total_ascent field
    * Units: m
    *
    * @return total_ascent
    */
    public Integer getTotalAscent() {
        return getFieldIntegerValue(22);
    }

    /**
    * Set total_ascent field
    * Units: m
    *
    * @param totalAscent
    */
    public void setTotalAscent(Integer totalAscent) {
        setFieldValue("total_ascent", totalAscent);
    }

    /**
    * Get total_descent field
    * Units: m
    *
    * @return total_descent
    */
    public Integer getTotalDescent() {
        return getFieldIntegerValue(23);
    }

    /**
    * Set total_descent field
    * Units: m
    *
    * @param totalDescent
    */
    public void setTotalDescent(Integer totalDescent) {
        setFieldValue("total_descent", totalDescent);
    }

    /**
    * Get total_training_effect field
    *
    * @return total_training_effect
    */
    public Float getTotalTrainingEffect() {
        return getFieldFloatValue(24);
    }

    /**
    * Set total_training_effect field
    *
    * @param totalTrainingEffect
    */
    public void setTotalTrainingEffect(Float totalTrainingEffect) {
        setFieldValue("total_training_effect", totalTrainingEffect);
    }

    /**
    * Get first_lap_index field
    *
    * @return first_lap_index
    */
    public Integer getFirstLapIndex() {
        return getFieldIntegerValue(25);
    }

    /**
    * Set first_lap_index field
    *
    * @param firstLapIndex
    */
    public void setFirstLapIndex(Integer firstLapIndex) {
        setFieldValue("first_lap_index", firstLapIndex);
    }

    /**
    * Get num_laps field
    *
    * @return num_laps
    */
    public Integer getNumLaps() {
        return getFieldIntegerValue(26);
    }

    /**
    * Set num_laps field
    *
    * @param numLaps
    */
    public void setNumLaps(Integer numLaps) {
        setFieldValue("num_laps", numLaps);
    }

    /**
    * Get event_group field
    *
    * @return event_group
    */
    public Short getEventGroup() {
        return getFieldShortValue(27);
    }

    /**
    * Set event_group field
    *
    * @param eventGroup
    */
    public void setEventGroup(Short eventGroup) {
        setFieldValue("event_group", eventGroup);
    }

    /**
    * Get trigger field
    *
    * @return trigger
    */
    public SessionTrigger getTrigger() {
        Short value = getFieldShortValue(28);
        if (value == null) return null;
        return SessionTrigger.getByValue(value);
    }

    /**
    * Set trigger field
    *
    * @param trigger
    */
    public void setTrigger(SessionTrigger trigger) {
        setFieldValue("trigger", trigger.value);
    }

    /**
    * Get nec_lat field
    * Units: semicircles
    *
    * @return nec_lat
    */
    public Integer getNecLat() {
        return getFieldIntegerValue(29);
    }

    /**
    * Set nec_lat field
    * Units: semicircles
    *
    * @param necLat
    */
    public void setNecLat(Integer necLat) {
        setFieldValue("nec_lat", necLat);
    }

    /**
    * Get nec_long field
    * Units: semicircles
    *
    * @return nec_long
    */
    public Integer getNecLong() {
        return getFieldIntegerValue(30);
    }

    /**
    * Set nec_long field
    * Units: semicircles
    *
    * @param necLong
    */
    public void setNecLong(Integer necLong) {
        setFieldValue("nec_long", necLong);
    }

    /**
    * Get swc_lat field
    * Units: semicircles
    *
    * @return swc_lat
    */
    public Integer getSwcLat() {
        return getFieldIntegerValue(31);
    }

    /**
    * Set swc_lat field
    * Units: semicircles
    *
    * @param swcLat
    */
    public void setSwcLat(Integer swcLat) {
        setFieldValue("swc_lat", swcLat);
    }

    /**
    * Get swc_long field
    * Units: semicircles
    *
    * @return swc_long
    */
    public Integer getSwcLong() {
        return getFieldIntegerValue(32);
    }

    /**
    * Set swc_long field
    * Units: semicircles
    *
    * @param swcLong
    */
    public void setSwcLong(Integer swcLong) {
        setFieldValue("swc_long", swcLong);
    }
}
