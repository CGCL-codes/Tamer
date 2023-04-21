package net.sourceforge.babyecma;

/**
 *

 */
public final class ESBoolean extends ESPrimitive {

    private final boolean value;

    private ESBoolean(boolean value) {
        this.value = value;
    }

    /**
     *
     */
    public static final ESBoolean TRUE = new ESBoolean(true);

    /**
     *
     */
    public static final ESBoolean FALSE = new ESBoolean(false);

    /**
     *
     * @param b
     * @return
     */
    public static ESBoolean valueOf(boolean b) {
        return b ? TRUE : FALSE;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean booleanValue() {
        return value;
    }

    /**
     *
     * @return
     */
    @Override
    public double doubleValue() {
        return value ? 1 : 0;
    }

    @Override
    public String toString() {
        return value ? ESNames.true_ : ESNames.false_;
    }

    /**
     *
     * @return
     */
    @Override
    public Wrapper toESObject() {
        return Wrapper.create(this);
    }

    /**
     *
     * @return
     */
    @Override
    public String getTypeof() {
        return ESNames.boolean_;
    }

    @Override
    public boolean equals(Object x) {
        return (x instanceof ESBoolean) && value == ((ESBoolean) x).value;
    }

    @Override
    public int hashCode() {
        return Boolean.valueOf(value).hashCode();
    }

    /**
     *
     */
    public static final class Wrapper extends SimpleESObject {

        private final ESBoolean value;

        private Wrapper() {
            super(null, ESNames.Boolean);
            this.value = ESBoolean.FALSE;
        }

        private Wrapper(ESValue value) {
            super(ESGlobals.getBooleanObjectPrototype(), null);
            this.value = value.toESBoolean();
        }

        /**
         *
         * @return
         */
        public static Wrapper createBooleanObjectPrototype() {
            return new Wrapper();
        }

        /**
         *
         * @param value
         * @return
         */
        public static Wrapper create(ESValue value) {
            return new Wrapper(value);
        }

        /**
         *
         * @param b
         * @return
         */
        public static Wrapper create(boolean b) {
            return create(ESBoolean.valueOf(b));
        }

        /**
         *
         * @return
         */
        @Override
        public ESBoolean getPrimitiveValue() {
            return value;
        }
    }

    /**
     *
     */
    public static final class Function extends ESFunction {

        private Function() {
            super(1, ESGlobals.getBooleanObjectPrototype());
        }

        /**
         *
         * @return
         */
        public static Function createBooleanObjectConstructor() {
            return new Function();
        }

        /**
         *
         * @param target
         * @param args
         * @return
         */
        @Override
        public ESBoolean apply(ESValue target, ESValue... args) {
            return ESArgs.at(args, 0).toESBoolean();
        }

        /**
         *
         * @param args
         * @return
         */
        @Override
        public Wrapper construct(ESValue... args) {
            return Wrapper.create(apply(null, args));
        }
    }
}
