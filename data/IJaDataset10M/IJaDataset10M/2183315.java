package org.ztemplates.actions.urlhandler.tree.match;

import java.io.Serializable;

public abstract class ZSegment implements Serializable {

    public abstract void toXml(StringBuilder sb, int depth);

    public abstract boolean isMatchingTheSame(ZSegment other);

    public abstract String toDefinition();
}
