package org.gvsig.tools.exception;

import java.util.Map;

public class BaseRuntimeExceptionTest extends BaseExceptionTest {

    public void testRuntimeExceptionNature() {
        Object test = new Object() {

            public String toString() {
                throw new BaseRuntimeException("ERROR", "ERROR", 1) {

                    protected Map values() {
                        return null;
                    }
                };
            }
        };
        try {
            test.toString();
            fail("RuntimeException not generated");
        } catch (RuntimeException rex) {
        }
    }

    protected IBaseException createBadDateException(String driver, Throwable throwable) {
        return new BadDateException(driver, throwable);
    }

    protected IBaseException createDriverException(String driver, Throwable throwable) {
        return new DriverRuntimeException(driver, throwable);
    }

    class BadDateException extends DriverRuntimeException {

        private static final long serialVersionUID = -8985920349210629998L;

        private static final String KEY = "Driver_%(driverName)s_Formato_de_fecha_incorrecto";

        private static final String MESSAGE = "Driver %(driverName)s: Formato de fecha incorrecto";

        public BadDateException(String driverName, Throwable cause) {
            super(driverName, MESSAGE, cause, KEY, serialVersionUID);
        }
    }
}
