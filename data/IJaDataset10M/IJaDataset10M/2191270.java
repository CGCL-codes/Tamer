package net.sourceforge.freejava.io.resource.preparation;

import java.io.IOException;
import java.util.Collection;

public interface IFormatDumpPreparation extends Cloneable {

    IFormatDumpPreparation clone();

    void dumpObject(Object o) throws IOException;

    void dumpObjects(Collection<?> objects) throws IOException;

    void dumpXML(Object o) throws IOException;
}
