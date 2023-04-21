package takatuka.classreader.logic.factory;

/**
 * <p>Title: </p>
 * <p>Description:
 * Based on Placeholder design pattern.
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 */
public final class FactoryPlaceholder {

    private FactoryFacade factory = FactoryFacade.getInstanceOf();

    private static final FactoryPlaceholder FactoryPlaceholder = new FactoryPlaceholder();

    private FactoryPlaceholder() {
        super();
    }

    public static FactoryPlaceholder getInstanceOf() {
        return FactoryPlaceholder;
    }

    public void setFactory(FactoryFacade factory) {
        this.factory = factory;
    }

    public FactoryFacade getFactory() {
        return factory;
    }
}
