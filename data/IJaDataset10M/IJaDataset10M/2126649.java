package org.norecess.citkit.lir;

import org.norecess.citkit.visitors.LIRRegisterVisitor;

public interface LIRRegister {

    public <T> T accept(LIRRegisterVisitor<T> visitor);
}
