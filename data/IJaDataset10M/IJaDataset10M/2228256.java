package org.appspy.server.bo;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Olivier HEDIN / olivier@appspy.org
 */
@Entity
@Table(name = "URL")
public class URL extends Feature {

    @ManyToOne
    @JoinColumn(name = "webappId")
    protected Webapp mWebapp;

    /**
	 * @return the webapp
	 */
    public Webapp getWebapp() {
        return mWebapp;
    }

    /**
	 * @param webapp the webapp to set
	 */
    public void setWebapp(Webapp webapp) {
        this.mWebapp = webapp;
    }
}
