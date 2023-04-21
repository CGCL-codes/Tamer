package net.sf.brightside.travelsystem.service.facade.hibernate.integration;

import net.sf.brightside.travelsystem.core.spring.AbstractSpringTest;
import net.sf.brightside.travelsystem.metamodel.Passenger;
import net.sf.brightside.travelsystem.service.facade.RegisterPassenger;
import net.sf.brightside.travelsystem.service.facade.hibernate.exception.PassengerAlreadyExistsException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RegisterPassengerImplTest extends AbstractSpringTest {

    private RegisterPassenger underTest;

    @BeforeMethod
    public void setUp() throws Exception {
        underTest = (RegisterPassenger) this.context.getBean(RegisterPassenger.class.getName());
    }

    @Test
    public void registerPassenger() {
        Passenger p = (Passenger) context.getBean(Passenger.class.getName());
        p.setUsername("Paja");
        p.setPassword("Patak");
        p.setFirstName("Nikola");
        p.setLastName("Nikolic");
        try {
            underTest.registerPassenger(p);
        } catch (PassengerAlreadyExistsException e) {
            e.printStackTrace();
        }
    }
}
