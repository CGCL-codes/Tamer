package org.hyperimage.service.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Jens-Martin Loebel
 */
@Entity(name = "HIObjectContent")
@Inheritance(strategy = InheritanceType.JOINED)
@XmlSeeAlso({ HIView.class, HIInscription.class })
public class HIObjectContent extends HIBase {

    @ManyToOne(cascade = CascadeType.PERSIST, targetEntity = HIObject.class)
    HIObject object;

    @XmlTransient
    public HIObject getObject() {
        return object;
    }

    public void setObject(HIObject object) {
        this.object = object;
    }
}
