package org.jsecurity.authz;

import org.jsecurity.ExceptionTest;

/**
 * @author Les Hazlewood
 * @since Jun 10, 2008 4:05:26 PM
 */
public class MissingAccountExceptionTest extends ExceptionTest {

    protected Class getExceptionClass() {
        return MissingAccountException.class;
    }
}
