package net.larsbehnke.petclinicplus.hibernate;

import net.larsbehnke.petclinicplus.AbstractClinicTests;

/**
 * Live unit tests for JpaTemplateClinic implementation.
 * "app-ctx-jpa.xml" determines the actual beans to test.
 *
 * @author Juergen Hoeller
 */
public class HibernateJpaClinicTests extends AbstractClinicTests {

    protected String getConfigPath() {
        return "app-ctx-jpa.xml";
    }
}
