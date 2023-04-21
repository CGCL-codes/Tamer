package net.sourceforge.iwii.db.dev.bo.project.artifact.phase1;

import net.sourceforge.iwii.db.dev.bo.AbstractConvertableBO;
import net.sourceforge.iwii.db.dev.bo.IBusinessObject;
import net.sourceforge.iwii.db.dev.common.TempIdGenerator;

/**
 * Class represents project worker business object.
 * 
 * @author Grzegorz 'Gregor736' Wolszczak
 * @version 1.00
 */
public class ProjectWorkerBO extends AbstractConvertableBO<Long> {

    private String name;

    private Integer number;

    private Integer workUnits;

    private ProjectResourcesBO resource;

    public ProjectWorkerBO() {
        this.setDatabaseId(TempIdGenerator.getInstance().generateId());
    }

    public ProjectWorkerBO(ProjectResourcesBO resource) {
        this.setDatabaseId(TempIdGenerator.getInstance().generateId());
        this.resource = resource;
    }

    public ProjectWorkerBO(String name, Integer number) {
        this.setDatabaseId(TempIdGenerator.getInstance().generateId());
        this.name = name;
        this.number = number;
    }

    public ProjectResourcesBO getResource() {
        return resource;
    }

    public void setResource(ProjectResourcesBO resource) {
        this.resource = resource;
    }

    public Integer getWorkUnits() {
        return workUnits;
    }

    public void setWorkUnits(Integer workUnits) {
        this.workUnits = workUnits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "business-object://convertable/" + this.getClass().getName() + "[databaseId=" + this.getDatabaseId() + "]";
    }

    @Override
    public void initWithOtherBO(IBusinessObject otherBO) {
        super.initWithOtherBO(otherBO);
        if (otherBO instanceof ProjectWorkerBO) {
            ProjectWorkerBO bo = (ProjectWorkerBO) otherBO;
            this.setName(bo.getName());
            this.setNumber(bo.getNumber());
            this.setResource(bo.getResource());
            this.setWorkUnits(bo.getWorkUnits());
        }
    }
}
