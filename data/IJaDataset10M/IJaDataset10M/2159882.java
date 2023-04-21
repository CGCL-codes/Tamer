package org.jagent.service.transport.http;

import java.io.*;
import javax.agent.*;
import javax.agent.service.*;
import javax.agent.service.transport.*;
import org.jagent.acr.AcrLocator;

/**
 * The <CODE>HttpLocator</CODE> is an implementation of the
 * <CODE>Locator</CODE> interface.
 * @see Locator
 */
public class HttpLocator extends AcrLocator {

    /**
     * Gets the address bound to this locator
     * @return the address bound to this locator
     */
    public String getAddress() {
        return (String) get(HttpMessageTransportService.ADDRESS);
    }

    /**
     * Sets an address to this locator
     * @param addr an address bound to this locator
     */
    public void setAddress(String addr) {
        set(HttpMessageTransportService.ADDRESS, addr);
    }

    /**
     * An HttpLocator must have a serialized form.
     * @param serializer serializes this Locator.
     */
    public void acceptSerializer(JasSerializer serializer) {
    }

    /**
     * Determine the hash code associated with the Locator
     * @return hash code of this locator
     */
    public int hashCode() {
        return this.hashCode();
    }

    /**
     * Determine if the given Object is equal to this Locator
     * @param obj the reference object with which to compare
     * @return <CODE>true</CODE> if the given object and this
     * object are equal, otherwise, <CODE>false</CODE>
     */
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
