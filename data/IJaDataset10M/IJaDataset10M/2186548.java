package net.chrisrichardson.arid;

/**
 * Scans for abstract classes and interfaces and returns the most derived types
 */
public class InterfaceAndAbstractClassPackageScanner extends AbstractPackageScanner {

    @Override
    protected boolean isMatch(Class<?> type) {
        return !isConcreteClass(type);
    }
}
