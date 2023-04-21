package uk.ac.cam.caret.minibix.metadata.impl.type;

import uk.ac.cam.caret.minibix.metadata.api.*;
import uk.ac.cam.caret.minibix.metadata.impl.*;

public interface Type {

    public boolean isIndexedString();

    public boolean isString();

    public String unparse(Object in, boolean top, Conversion conv) throws IncompatibleException;

    public boolean canUnbracket(Object in, Conversion conv) throws IncompatibleException;
}
