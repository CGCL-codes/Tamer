package eu.planets_project.pp.plato.model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import org.hibernate.validator.Length;

/**
 * Entity bean storing additional information about an Evaluation-Step.
 *
 * @author Mark Guttenbrunner
 */
@Entity
public class ExecutablePlanDefinition implements Serializable, ITouchable {

    private static final long serialVersionUID = 8385664635418556928L;

    @Id
    @GeneratedValue
    private int id;

    private String objectPath;

    @Length(max = 32672)
    @Column(length = 32672)
    private String toolParameters;

    @Length(max = 32672)
    @Column(length = 32672)
    private String triggersConditions;

    @Length(max = 32672)
    @Column(length = 32672)
    private String validateQA;

    @Length(max = 2000000)
    @Column(length = 2000000)
    protected String executablePlan;

    @Length(max = 2000000)
    @Column(length = 2000000)
    protected String eprintsExecutablePlan;

    public String getExecutablePlan() {
        return executablePlan;
    }

    public void setExecutablePlan(String executablePlan) {
        this.executablePlan = executablePlan;
    }

    public String getEprintsExecutablePlan() {
        return eprintsExecutablePlan;
    }

    public void setEprintsExecutablePlan(String eprintsExecutablePlan) {
        this.eprintsExecutablePlan = eprintsExecutablePlan;
    }

    @OneToOne(cascade = CascadeType.ALL)
    private ChangeLog changeLog = new ChangeLog();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ChangeLog getChangeLog() {
        return this.changeLog;
    }

    public void setChangeLog(ChangeLog value) {
        changeLog = value;
    }

    public boolean isChanged() {
        return changeLog.isAltered();
    }

    public void touch() {
        getChangeLog().touch();
    }

    /**
     * @see ITouchable#handleChanges(IChangesHandler)
     */
    public void handleChanges(IChangesHandler h) {
        h.visit(this);
    }

    public String getObjectPath() {
        return objectPath;
    }

    public void setObjectPath(String objectPath) {
        this.objectPath = objectPath;
    }

    public String getTriggersConditions() {
        return triggersConditions;
    }

    public void setTriggersConditions(String triggersConditions) {
        this.triggersConditions = triggersConditions;
    }

    public String getValidateQA() {
        return validateQA;
    }

    public void setValidateQA(String validateQA) {
        this.validateQA = validateQA;
    }

    public String getToolParameters() {
        return toolParameters;
    }

    public void setToolParameters(String toolParameters) {
        this.toolParameters = toolParameters;
    }
}
