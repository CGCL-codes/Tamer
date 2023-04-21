package net.frontlinesms.email.pop;

import javax.mail.Address;

/**
 * Test object to simulate the {@link Address} class.
 *
 * @author Carlos Eduardo Endler Genz
 * @date 21/02/2009
 */
@SuppressWarnings("serial")
public class MockMailAddress extends Address {

    private String address;

    public MockMailAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object arg0) {
        return false;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public String toString() {
        return address;
    }
}
