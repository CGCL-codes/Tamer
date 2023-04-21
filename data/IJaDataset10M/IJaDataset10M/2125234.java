package de.icehorsetools.dataAccess.objects;

import java.io.Serializable;
import org.ugat.interfaces.IUnItemDescriptor;
import org.ugat.wiser.interfaces.IUnComponentHandler;
import de.icehorsetools.dataAccess.hibernate.objects.SortcriteriaHBM;

/**
 * @author tkr
 * @version $Id: Sortcriteria.java 346 2009-07-17 12:36:54Z kruegertom $
 */
public class Sortcriteria extends SortcriteriaHBM implements java.io.Serializable, IUnItemDescriptor {

    public String getDisplayString() {
        return this.getArgument();
    }

    public String toDisplayString() {
        return null;
    }

    public Serializable toIdentifier() {
        return this.getIdentifier();
    }

    public String toText(IUnComponentHandler xContext) {
        return null;
    }
}
