package de.bea.domingo.map;

import de.bea.domingo.DViewEntry;

/**
 * Abstract base class for mapping between instances and documents.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public abstract class BaseInstanceDigestMapper extends BaseInstanceMapper implements InstanceDigestMapper {

    /** Class of instances of digests of the business objects. */
    private Class fDigestClass;

    /**
     * Constructor. The class of instances of digest of the business objects is
     * assumed to be the same as the instance class.
     *
     * @param instanceClass the class of instance of the business objects
     */
    public BaseInstanceDigestMapper(final Class instanceClass) {
        this(instanceClass, instanceClass);
        add(new DirectMapper("$Conflict", "Conflict", Boolean.TYPE));
        add(new DirectMapper("$Ref", "ParentUniversalId", String.class));
    }

    /**
     * Constructor.
     *
     * @param instanceClass the class of instance of the business objects
     * @param digestClass the class of instance of digests of the business
     *            objects
     */
    public BaseInstanceDigestMapper(final Class instanceClass, final Class digestClass) {
        super(instanceClass);
        fDigestClass = digestClass;
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.map.InstanceMapper#getDigestClass()
     */
    public final Class getDigestClass() {
        return fDigestClass;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.bea.domingo.map.InstanceMapper#newDigest()
     */
    public final Object newDigest() {
        try {
            return fDigestClass.newInstance();
        } catch (InstantiationException e) {
            throw new MappingRuntimeException("Cannot create digest instance", e);
        } catch (IllegalAccessException e) {
            throw new MappingRuntimeException("Cannot create digest instance", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.map.InstanceMapper#map(de.bea.domingo.DViewEntry, java.lang.Object)
     */
    public final void map(final DViewEntry viewEntry, final Object digest) throws MappingException {
        ((BaseDigest) digest).setUniversalId(viewEntry.getUniversalID());
        mapDigest(viewEntry, digest);
    }

    /**
     * Delegates concrete view entry mappings to sub classes.
     *
     * @param viewEntry the domingo ViewEntry
     * @param digest the digest instance
     * @throws MappingException if an error occurred during mapping
     */
    protected abstract void mapDigest(DViewEntry viewEntry, Object digest) throws MappingException;
}
