package ru.arriah.common.ejb;

import ru.arriah.common.ejb.help.*;

public interface SequenceLocalObject extends GenericEntityLocalObject {

    public String getName();

    public int getValue();

    public KeyBlock getNextBlock(int size);
}
