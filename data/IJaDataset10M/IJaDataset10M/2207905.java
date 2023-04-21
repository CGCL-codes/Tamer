package se.biobanksregistersyd.vienti;

import se.biobanksregistersyd.vienti.cleaners.Cleaner;

/**
 * A container class for creating, storing and handle <code>Cleaner</code>
 * objects.
 * 
 * @author jtorin
 */
public class CleanerContainer extends Container {

    /** Current classname. "se.biobanksregistersyd.vienti.CleanerContainer" */
    private static final String CLASSNAME = CurrentContext.getCurrentClassName();

    /**
     * Instantiates all <code>Cleaner</code> objects and asks each to load its
     * settings. <p/>
     * 
     * The following properties may be loaded when this class is instantiated.
     * <p/>
     * <dl>
     * <dt> &lt;classname&gt;.cleaners
     * <dd> A list of tokens, each representing a set of settings for creating
     * one <code>Cleaner</code> instance.
     * <dt> &lt;prefix&gt;.&lt;classname&gt;.cleanerclass
     * <dd> The name of the <code>Cleaner</code> class which can delete one or
     * several files in a directory. This must be an actual implementation
     * class, not one of the helper superclasses. <code>prefix</code> is a
     * token from the <code>cleaners</code> property (see above).
     * </dl>
     * 
     * @throws ContainerException
     *             if the <code>Container</code> could not be instantiated.
     * @see Cleaner
     * @see Container
     */
    public CleanerContainer() throws ContainerException {
        super(CLASSNAME + ".cleaners", CLASSNAME + ".cleanerclass");
    }

    /**
     * Return the <code>Cleaner</code> object from index position
     * <code>index</code>.
     * 
     * @param index
     *            an <code>int</code> with the index position of the
     *            <code>Cleaner</code> to return.
     * @return a <code>Cleaner</code> object from the internal list.
     * @throws ContainerException
     *             if the <code>Container</code>er does not contain any
     *             objects, or of the object is of the wrong type.
     */
    public Cleaner getCleaner(final int index) throws ContainerException {
        Object tmp = getObject(index);
        if (tmp instanceof Cleaner) {
            return (Cleaner) tmp;
        }
        throw new ContainerException(ContainerExceptionEnum.OBJECT_OF_WRONG_TYPE);
    }

    /**
     * Examine and handle an exception that was caught during the instantiation
     * of a class, more specifically, those exceptions stemming from the actual
     * class and conveyed through an <code>InvocationTargetException</code>.
     * <p/> This method should handle all those exceptions that are possible to
     * come from the instantiation of the class that this <code>Container</code>
     * subclass is meant to contain. <p/> If the exception is identified in this
     * method, it should throw an <code>ContainerException</code> describing
     * the cause. It is probably a good idea to chain the exception in
     * <code>targetException</code> together with the new thrown.
     * 
     * @param dynamicClassname
     *            a <code>String</code> with the name of the class that failed
     *            to instantiate.
     * @param targetException
     *            a <code>Throwable</code> to examine and handle. Notice that
     *            this is not (neccesary) an
     *            <code>InvocationTargetException</code> but rather the
     *            exception it contained.
     * @throws ContainerException
     *             contains an explanation and/or encapsulation of the examined
     *             exception.
     */
    protected void handleInvocationTargetException(final String dynamicClassname, final Throwable targetException) throws ContainerException {
        if (targetException.getClass() == ParameterException.class) {
            throw new ContainerException(ContainerExceptionEnum.COULD_NOT_INSTANTIATE, targetException, "Class name: " + dynamicClassname + ". ");
        }
    }
}
