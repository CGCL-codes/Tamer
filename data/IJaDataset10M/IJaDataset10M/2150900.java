package jfun.yan;

/**
 * Zephyr Business Solutions Corp.
 *
 * @author Ben Yu
 *
 */
final class MbindComponent extends Component {

    private final Creator c1;

    private final ComponentBinder binder;

    MbindComponent(final Creator c1, final ComponentBinder binder) {
        this.c1 = c1;
        this.binder = binder;
    }

    public Class getType() {
        return binder.bindType(c1.getType());
    }

    public boolean isConcrete() {
        return false;
    }

    public Object create(Dependency dep) {
        final Object v1 = c1.create(dep);
        return run_binder(binder, v1).create(dep);
    }

    public Class verify(Dependency dep) {
        return binder.verify(c1.verify(dep)).verify(dep);
    }

    public boolean equals(Object obj) {
        if (obj instanceof MbindComponent) {
            final MbindComponent dc2 = (MbindComponent) obj;
            return c1.equals(dc2.c1) && binder.equals(dc2.binder);
        } else return false;
    }

    public int hashCode() {
        return c1.hashCode() * 31 + binder.hashCode();
    }

    public String toString() {
        return "(" + c1 + ") >>= (" + binder + ")";
    }

    private Creator run_binder(Binder binder, Object v) {
        try {
            return binder.bind(v);
        } catch (Error e) {
            throw e;
        } catch (YanException e) {
            throw e;
        } catch (Throwable e) {
            throw new jfun.yan.ComponentInstantiationException(e);
        }
    }

    public boolean isSingleton() {
        return false;
    }
}
