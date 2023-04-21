package fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.mopp;

/**
 * A basic implementation of the
 * fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.IOcl4tstElementMapping
 * interface.
 * 
 * @param <ReferenceType> the type of the reference that can be mapped to
 */
public class Ocl4tstElementMapping<ReferenceType> implements fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.IOcl4tstElementMapping<ReferenceType> {

    private final ReferenceType target;

    private String identifier;

    private String warning;

    public Ocl4tstElementMapping(String identifier, ReferenceType target, String warning) {
        super();
        this.target = target;
        this.identifier = identifier;
        this.warning = warning;
    }

    public ReferenceType getTargetElement() {
        return target;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getWarning() {
        return warning;
    }
}
