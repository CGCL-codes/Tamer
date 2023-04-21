package org.hmaciel.descop.ejb.act;

import javax.persistence.Entity;
import javax.persistence.Transient;
import org.hmaciel.descop.ejb.datatypes.IIBean;

/**
 * @author lgrundel
 */
@SuppressWarnings("serial")
@Entity
public class ActGrafica extends ActBean {

    public ActGrafica() {
        super();
    }

    @Transient
    public IIBean getIi() {
        return id.get(0);
    }
}
