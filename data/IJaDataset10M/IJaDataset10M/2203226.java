package com.apusic.ebiz.framework.core.validation;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.apusic.ebiz.framework.core.DummyUser;
import com.apusic.ebiz.framework.core.FrameworkRuntimeException;

public class ValidationUtilsTest {

    @Test
    public void invokeValidator() {
        DummyUser user = new DummyUser();
        ValidationReport validationReport = ValidationUtils.invokeValidator(new MockUserNameValidator(), "name", user);
        assertEquals("SmartOrg-Err-001", validationReport.getCode());
    }

    @Test(expected = FrameworkRuntimeException.class)
    public void invokeValidatorFailed() {
        DummyUser user = new DummyUser();
        ValidationReport validationReport = ValidationUtils.invokeValidator(new MockUserNameValidator(), "noExistName", user);
        assertEquals("SmartOrg-Err-001", validationReport.getCode());
    }

    @Test(expected = FrameworkRuntimeException.class)
    public void invokeValidatorNullPointerException() {
        DummyUser user = new DummyUser();
        ValidationReport validationReport = ValidationUtils.invokeValidator(new MockUserNameValidator(), "role.roleName", user);
    }
}
