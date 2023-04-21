package org.xteam.sled.tree;

import java.util.List;
import org.xteam.sled.model.AbsoluteField;

public abstract class FieldStrategy {

    public abstract List<AbsoluteField> limitFields(List<AbsoluteField> afields);
}
