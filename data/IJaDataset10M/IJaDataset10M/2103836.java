package net.sf.annaf.core.data;

import java.io.Serializable;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-11-12
 *
 */
public interface DataBean extends Serializable {

    public Serializable getCustom(String ident);

    public void addCustom(String ident, Serializable custom);
}
