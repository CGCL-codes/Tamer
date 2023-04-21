package com.germinus.xpression.cms.model;

import java.io.Serializable;

/**
 * 
 * @author Miguel Arlandy
 *
 * Date: 26-sep-2005
 * Time: 13:53:24
 */
public class AttachSchool implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2419300484912508541L;

    private EntitySex[] entitySex = new EntitySex[1];

    private String delegateDirector;

    private String tratDelegateDirector;

    private String patronatoAgent;

    public AttachSchool() {
        for (int i = 0; i < 1; i++) {
            entitySex[i] = new EntitySex();
        }
    }

    /**
     * @return Returns the entitySex.
     */
    public EntitySex[] getEntitySex() {
        return entitySex;
    }

    /**
     * @param entitySex The entitySex to set.
     */
    public void setEntitySex(EntitySex[] entitySex) {
        this.entitySex = entitySex;
    }

    /**
     * @return Returns the delegateDirector.
     */
    public String getDelegateDirector() {
        return delegateDirector;
    }

    /**
     * @param delegateDirector The delegateDirector to set.
     */
    public void setDelegateDirector(String delegateDirector) {
        this.delegateDirector = delegateDirector;
    }

    /**
     * @return Returns the patronatoAgent.
     */
    public String getPatronatoAgent() {
        return patronatoAgent;
    }

    /**
     * @param patronatoAgent The patronatoAgent to set.
     */
    public void setPatronatoAgent(String patronatoAgent) {
        this.patronatoAgent = patronatoAgent;
    }

    /**
     * @return Returns the tratDelegateDirector.
     */
    public String getTratDelegateDirector() {
        return tratDelegateDirector;
    }

    /**
     * @param tratDelegateDirector The tratDelegateDirector to set.
     */
    public void setTratDelegateDirector(String tratDelegateDirector) {
        this.tratDelegateDirector = tratDelegateDirector;
    }
}
