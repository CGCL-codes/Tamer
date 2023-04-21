package org.kwantu.app.model;

import java.io.Serializable;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import org.kwantu.persistence.AbstractPersistentObject;

/**
 * MyKwantuApplication maps together KwantuApplication and the user in which the 
 * KwantuAppplication belongs to.
 * 
 * @author siviwe
 */
@Entity
public class MyKwantuApplication extends AbstractPersistentObject implements Serializable {

    private KwantuApplication owningKwantuApplication;

    private KwantuUser owningUser;

    @ManyToOne
    public KwantuUser getOwningUser() {
        return owningUser;
    }

    public void setOwningUser(final KwantuUser owningUser) {
        this.owningUser = owningUser;
    }

    @ManyToOne
    public KwantuApplication getOwningKwantuApplication() {
        return owningKwantuApplication;
    }

    public void setOwningKwantuApplication(KwantuApplication owningKwantuApplication) {
        this.owningKwantuApplication = owningKwantuApplication;
    }
}
