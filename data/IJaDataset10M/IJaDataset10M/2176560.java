package com.iparelan.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.testng.annotations.Test;
import com.iparelan.util.annotations.Copyright;

@Copyright("Copyright &copy; 2008, Iparelan Solutions, LLC. All rights reserved.")
public class Tests {

    @Test(groups = { "functest", "checkintest" })
    public void hashCodeConstantsSingletonPropertyViolation() {
        try {
            final Constructor<HashCodeConstants> constructor = HashCodeConstants.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        } catch (final InstantiationException shouldNotFailThisWayButDontIgnoreJustInCase) {
            throw new RuntimeException(shouldNotFailThisWayButDontIgnoreJustInCase);
        } catch (final NoSuchMethodException shouldNotFailThisWayButDontIgnoreJustInCase) {
            throw new RuntimeException(shouldNotFailThisWayButDontIgnoreJustInCase);
        } catch (final IllegalAccessException shouldNotFailThisWayButDontIgnoreJustInCase) {
            throw new RuntimeException(shouldNotFailThisWayButDontIgnoreJustInCase);
        } catch (final InvocationTargetException ite) {
            final Throwable throwable = ite.getTargetException();
            final AssertionError ae = AssertionError.class.cast(throwable);
            final String msg = ae.getMessage();
            assert "not instantiable".equals(msg);
        }
    }
}
