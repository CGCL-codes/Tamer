package com.redhipps.hips.client.io;

import com.redhipps.hips.client.model.Doctor;
import com.redhipps.hips.client.model.DoctorConstraint;
import com.redhipps.hips.client.model.Institution;
import com.redhipps.hips.client.model.Schedule;
import com.redhipps.hips.client.model.ScheduleBlock;
import com.redhipps.hips.client.model.ScheduleSolution;
import com.redhipps.hips.client.model.ScheduleSolutionBlock;

public abstract class ReaderVisitor {

    public void visit(Institution model) {
    }

    public void visit(Doctor model) {
    }

    public void visit(DoctorConstraint model) {
    }

    public void visit(Schedule model) {
    }

    public void visit(ScheduleBlock model) {
    }

    public void visit(ScheduleSolution model) {
    }

    public void visit(ScheduleSolutionBlock model) {
    }
}
