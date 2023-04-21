package net.sf.chex4j;

import java.io.IOException;
import javassist.CannotCompileException;
import javassist.NotFoundException;

public interface ClassTransformer {

    public byte[] transformClassfile(byte[] classfileBuffer, String canonicalName) throws NotFoundException, ClassNotFoundException, CannotCompileException, IOException;
}
