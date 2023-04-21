package org.smallmind.scribe.pen.nutsnbolts.util;

public interface UniquelyIdentified {

    public abstract UniqueId.TYPE getType();

    public abstract Object getUniqueIdentifier();
}
