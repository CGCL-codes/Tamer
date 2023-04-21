package org.cmsuite2.business.validator;

import it.ec.commons.security.spring.jpa.model.role.Role;
import it.ec.commons.web.ValidateException;
import org.cmsuite2.business.validator.RoleValidator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/context/spring-context.xml" })
public class RoleValidatorTest {

    @Autowired
    private RoleValidator roleValidator;

    private Role role = new Role();

    @Before
    public void setUp() {
        role.setId(1);
        role.setName("aaa");
    }

    @Test
    public void validateOk() {
        ValidateException ex = null;
        try {
            roleValidator.validateStep1(role);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoNull() {
        ValidateException ex = null;
        try {
            roleValidator.validateStep1(null);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoNameNull() {
        ValidateException ex = null;
        role.setName(null);
        try {
            roleValidator.validateStep1(role);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoNameTooLong() {
        ValidateException ex = null;
        char[] chars = new char[256];
        role.setName(new String(chars));
        try {
            roleValidator.validateStep1(role);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @After
    public void tearDown() {
    }

    public RoleValidator getRoleValidator() {
        return roleValidator;
    }

    public void setRoleValidator(RoleValidator roleValidator) {
        this.roleValidator = roleValidator;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
