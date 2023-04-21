package org.objectwiz.model;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * An object that is linked to a specific application.
 *
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
@MappedSuperclass
public class ApplicationSpecificObject extends EntityBase {

    private ApplicationSettings applicationSettings;

    /**
     * Link to the application that contains this object.
     */
    @ManyToOne(optional = false)
    public ApplicationSettings getApplicationSettings() {
        return applicationSettings;
    }

    public void setApplicationSettings(ApplicationSettings applicationSettings) {
        this.applicationSettings = applicationSettings;
    }
}
