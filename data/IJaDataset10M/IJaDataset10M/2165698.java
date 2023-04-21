package org.gems.designer;

import java.io.Serializable;

public interface Subtype extends Serializable {

    public Object newInstance();

    public String getName();

    public Object getTemplate();

    public Class getType();
}
