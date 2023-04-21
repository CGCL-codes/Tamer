package org.bluprint.app.util.uml2;

import java.util.NoSuchElementException;
import org.eclipse.uml2.Package;

public interface PackageIterator {

    public void init(Package aPackage);

    public boolean hasNext();

    public Package next() throws NoSuchElementException;
}
