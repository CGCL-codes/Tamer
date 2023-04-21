package net.sourceforge.babyecma;

/**
 *

 */
public abstract class ESFunction extends SimpleESObject {

    /**
     *
     * @param prototype
     * @param className
     */
    protected ESFunction(ESObject prototype, String className) {
        super(prototype, className);
        putValue(ESNames.length, ESNumber.ZERO);
    }

    /**
     *
     * @param length
     * @param fProto
     */
    protected ESFunction(int length, ESObject fProto) {
        this(ESGlobals.getFunctionPrototype(), null);
        if (fProto == null) {
            fProto = SourceESObject.create();
        }
        putValue(ESNames.length, ESNumber.valueOf(Math.max(length, 0)));
        putValue(ESNames.prototype, fProto);
        fProto.putValue(ESNames.constructor, this);
    }

    /**
     *
     * @return
     */
    @Override
    public boolean supportsApply() {
        return true;
    }

    /**
     *
     * @param target
     * @param args
     * @return
     */
    @Override
    public abstract ESValue apply(ESValue target, ESValue... args);
}
