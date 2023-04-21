package serene.internal.datatype;

import org.relaxng.datatype.DatatypeLibrary;
import org.relaxng.datatype.DatatypeLibraryFactory;
import serene.Constants;

public class InternalLibraryFactory implements DatatypeLibraryFactory {

    public InternalLibraryFactory() {
    }

    public InternalLibrary createDatatypeLibrary(String namespace) {
        if (namespace.equals(Constants.INTERNAL_DATATYPE_LIBRARY)) {
            return new InternalLibrary();
        }
        return null;
    }
}
