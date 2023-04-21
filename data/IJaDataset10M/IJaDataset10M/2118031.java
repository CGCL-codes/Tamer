package org.openworks.domain.hr;

import junit.framework.TestCase;
import org.openworks.domain.common.Role;

/**
 * Test case for Employee.java
 */
public class EmployeeTest extends TestCase {

    private Employee employee = null;

    public void setUp() {
        this.employee = new Employee();
    }

    public void testGetName() {
        assertNull(this.employee.getName());
    }

    public void testGetRole() {
        assertNull(this.employee.getRole());
        Role role = new Role();
        this.employee.setRole(role);
        assertNotNull(this.employee.getRole());
    }

    public void testGetContacts() {
        assertNotNull(this.employee.getContacts());
    }

    public void testSetContact() {
        fail();
    }
}
