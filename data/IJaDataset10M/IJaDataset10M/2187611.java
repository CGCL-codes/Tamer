package nacaLib.varEx;

import nacaLib.base.*;

public abstract class InitializeManager extends CJMapObject {

    public abstract void initialize(VarBufferPos buffer, VarDefBuffer varDefBuffer, int nOffset, InitializeCache initializeCache);
}
