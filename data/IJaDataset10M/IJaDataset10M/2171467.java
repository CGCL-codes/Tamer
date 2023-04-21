package com.technoetic.xplanner.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;
import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.repository.IterationRepository;

public class Project2 extends DomainObject implements Nameable, NoteAttachable, Describable {

    private String name;

    private Collection iterations = new HashSet();

    private Collection notificationReceivers = new TreeSet();

    private String description;

    private boolean hidden;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection getIterations() {
        return iterations;
    }

    public void setIterations(Collection iterations) {
        this.iterations = iterations;
    }

    public Iteration getCurrentIteration() {
        return IterationRepository.getCurrentIteration(getId());
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean flag) {
        hidden = flag;
    }

    public Collection getNotificationReceivers() {
        return notificationReceivers;
    }

    public void setNotificationReceivers(Collection notificationReceivers) {
        this.notificationReceivers = notificationReceivers;
    }

    @Override
    public String toString() {
        return "Project{" + "id='" + getId() + ", " + "name='" + name + "'" + "}";
    }
}
