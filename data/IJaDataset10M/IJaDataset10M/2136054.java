package com.redhipps.hips.client.model;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ScheduleSolutionBlock extends Model {

    @Persistent
    private ScheduleBlock scheduleBlock;

    @Persistent
    private Doctor doctor;

    @Deprecated
    @NotPersistent
    private PythonDatastoreKey blockRef;

    @Deprecated
    @NotPersistent
    private PythonDatastoreKey doctorRef;

    public ScheduleSolutionBlock() {
    }

    public ScheduleBlock getScheduleBlock() {
        return scheduleBlock;
    }

    public void setScheduleBlock(ScheduleBlock scheduleBlock) {
        this.scheduleBlock = scheduleBlock;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @Deprecated
    public ScheduleSolutionBlock(PythonDatastoreKey key) {
        super(key);
    }

    @Deprecated
    public PythonDatastoreKey getBlockRef() {
        return blockRef;
    }

    @Deprecated
    public void setBlockRef(PythonDatastoreKey blockRef) {
        this.blockRef = blockRef;
    }

    @Deprecated
    public PythonDatastoreKey getDoctorRef() {
        return doctorRef;
    }

    @Deprecated
    public void setDoctorRef(PythonDatastoreKey doctorRef) {
        this.doctorRef = doctorRef;
    }
}
