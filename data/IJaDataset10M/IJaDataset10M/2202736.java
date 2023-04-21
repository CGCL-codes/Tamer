package net.sf.mmm.util.value.impl.pojo1;

/**
 * This is some code only used for
 * {@link net.sf.mmm.util.value.impl.ComposedValueConverterTest}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.1.0
 */
@SuppressWarnings("all")
public class SubPojoImpl implements SubPojo {

    private FooEnum foo;

    private int integer;

    private String string;

    /**
   * {@inheritDoc}
   */
    public FooEnum getFoo() {
        return this.foo;
    }

    public void setFoo(FooEnum foo) {
        this.foo = foo;
    }

    /**
   * {@inheritDoc}
   */
    public int getInteger() {
        return this.integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }

    /**
   * {@inheritDoc}
   */
    public String getString() {
        return this.string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
