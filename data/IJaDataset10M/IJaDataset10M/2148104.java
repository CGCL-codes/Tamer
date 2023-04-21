package net.sourceforge.mords.docs.common;

import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author david
 */
public interface Index extends Serializable {

    public String getIndex();

    public Collection<String> getCategories();
}
