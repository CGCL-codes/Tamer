package org.osmius.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Register of Web Services activity
 */
public class OsmWsAudit extends BaseObject implements Serializable {

    /**
    * Audit identifier
    */
    private Long idnAudit;

    /**
    * User
    */
    private OsmUser osmUser;

    /**
    * Audit date
    */
    private Date dtiAudit;

    /**
    * Action
    */
    private OsmWsAction osmWsAction;

    /**
    * Parameters
    */
    private String txtParameters;

    /**
    * Result
    */
    private Integer indResult;

    /**
    * Default constructor
    */
    public OsmWsAudit() {
    }

    /**
    * Constructor with parameters
    * @param idnAudit Audit identifier
    * @param osmUser User
    * @param osmWsAction Audit date
    * @param dtiAudit Action
    * @param txtParameters Parameters
    * @param indResult Result
    */
    public OsmWsAudit(Long idnAudit, OsmUser osmUser, OsmWsAction osmWsAction, Date dtiAudit, String txtParameters, Integer indResult) {
        this.idnAudit = idnAudit;
        this.osmUser = osmUser;
        this.osmWsAction = osmWsAction;
        this.dtiAudit = dtiAudit;
        this.txtParameters = txtParameters;
        this.indResult = indResult;
    }

    /**
    * Gets the audit identifier
    * @return Audit identifier
    */
    public Long getIdnAudit() {
        return idnAudit;
    }

    /**
    * Sets the audit identifier
    * @param idnAudit Audit identifier
    */
    public void setIdnAudit(Long idnAudit) {
        this.idnAudit = idnAudit;
    }

    /**
    * Gets the user
    * @return User
    */
    public OsmUser getOsmUser() {
        return osmUser;
    }

    /**
    * Sets the user
    * @param osmUser User
    */
    public void setOsmUser(OsmUser osmUser) {
        this.osmUser = osmUser;
    }

    /**
    * Gets the date
    * @return Date
    */
    public Date getDtiAudit() {
        return dtiAudit;
    }

    /**
    * Sets the date
    * @param dtiAudit Date
    */
    public void setDtiAudit(Date dtiAudit) {
        this.dtiAudit = dtiAudit;
    }

    /**
    * Gets the action
    * @return Action
    */
    public OsmWsAction getOsmWsAction() {
        return osmWsAction;
    }

    /**
    * Sets the action
    * @param osmWsAction Action
    */
    public void setOsmWsAction(OsmWsAction osmWsAction) {
        this.osmWsAction = osmWsAction;
    }

    /**
    * Gets the parameters
    * @return Parameters
    */
    public String getTxtParameters() {
        return txtParameters;
    }

    /**
    * Sets the parameters
    * @param txtParameters Action
    */
    public void setTxtParameters(String txtParameters) {
        this.txtParameters = txtParameters;
    }

    /**
    * Gets the result
    * @return Result
    */
    public Integer getIndResult() {
        return indResult;
    }

    /**
    * Sets the result
    * @param indResult Action
    */
    public void setIndResult(Integer indResult) {
        this.indResult = indResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OsmWsAudit that = (OsmWsAudit) o;
        if (!dtiAudit.equals(that.dtiAudit)) return false;
        if (!idnAudit.equals(that.idnAudit)) return false;
        if (!indResult.equals(that.indResult)) return false;
        if (!osmUser.equals(that.osmUser)) return false;
        if (!osmWsAction.equals(that.osmWsAction)) return false;
        if (!txtParameters.equals(that.txtParameters)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = idnAudit.hashCode();
        result = 31 * result + osmUser.hashCode();
        result = 31 * result + dtiAudit.hashCode();
        result = 31 * result + osmWsAction.hashCode();
        result = 31 * result + txtParameters.hashCode();
        result = 31 * result + indResult.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "OsmWsAudit{" + "idnAudit=" + idnAudit + ", osmUser=" + osmUser + ", dtiAudit=" + dtiAudit + ", osmWsAction=" + osmWsAction + ", txtParameters='" + txtParameters + '\'' + ", indResult=" + indResult + '}';
    }
}
