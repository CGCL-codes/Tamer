package net.stickycode.mockwire.binder;

import net.stickycode.mockwire.Mocker;
import net.stickycode.mockwire.binder.MockerFactory;

public class MockerFactoryLoader {

    private static MockerFactory SINGLETON;

    public static Mocker load() {
        if (SINGLETON != null) return SINGLETON.create();
        try {
            Class<?> klass = Class.forName(MockerFactory.class.getName() + "Binder");
            if (MockerFactory.class.isAssignableFrom(klass)) {
                SINGLETON = (MockerFactory) klass.newInstance();
                return SINGLETON.create();
            }
            throw new IllegalStateException("Class " + klass.getName() + " should implement " + MockerFactory.class.getName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("You must have an Mocker implmentation to run Mockwire", e);
        } catch (InstantiationException e) {
            throw new IllegalStateException("You must have an Mocker implmentation to run Mockwire", e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("You must have an Mocker implmentation to run Mockwire", e);
        }
    }

    /**
   * Used for highjacking the actual mocker implementation used;
   */
    static void preset(MockerFactory mocker) {
        SINGLETON = mocker;
    }
}
