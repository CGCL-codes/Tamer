package edu.ucla.mbi.curator.forms.curator;

import org.apache.struts.action.ActionForm;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: Feb 21, 2006
 * Time: 1:28:16 PM
 */
public class ParticipantDetailsForm extends ActionForm {

    private String commit;

    private String participantId;

    private String systemOrganismTaxId;

    private String nonsystemOrganismTaxId;

    private String nonsystemOrganismName;

    public String getNonsystemOrganismName() {
        return nonsystemOrganismName;
    }

    public void setNonsystemOrganismName(String nonsystemOrganismName) {
        this.nonsystemOrganismName = nonsystemOrganismName;
    }

    public String getNonsystemOrganismTaxId() {
        return nonsystemOrganismTaxId;
    }

    public void setNonsystemOrganismTaxId(String nonsystemOrganismTaxId) {
        this.nonsystemOrganismTaxId = nonsystemOrganismTaxId;
    }

    public String getSystemOrganismTaxId() {
        return systemOrganismTaxId;
    }

    public void setSystemOrganismTaxId(String systemOrganismTaxId) {
        this.systemOrganismTaxId = systemOrganismTaxId;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }
}
