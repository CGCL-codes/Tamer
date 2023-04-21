package net.java.slee.resource.diameter.gx.events;

import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;

/**
 * Base interface for RoReAuthMessage
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 */
public interface GxReAuthMessage extends DiameterMessage {

    /**
   * Returns the value of the Auth-Application-Id AVP, of type Unsigned32.
   *
   * @return
   */
    long getAuthApplicationId();

    /**
   * Sets the value of the Auth-Application-Id AVP, of type Unsigned32.
   *
   * @param authApplicationId
   * @throws IllegalStateException
   */
    void setAuthApplicationId(long authApplicationId) throws IllegalStateException;

    /**
   * Returns true if the Auth-Application-Id AVP is present in the message.
   *
   * @return
   */
    boolean hasAuthApplicationId();

    /**
   * Returns the set of Route-Record AVPs.
   *
   * @return
   */
    DiameterIdentity[] getRouteRecords();

    /**
   * Sets a single Route-Record AVP in the message, of type DiameterIdentity.
   *
   * @param routeRecord
   * @throws IllegalStateException
   */
    void setRouteRecord(DiameterIdentity routeRecord) throws IllegalStateException;

    /**
   * Sets the set of Route-Record AVPs, with all the values in the given
   * array.
   *
   * @param routeRecords
   * @throws IllegalStateException
   */
    void setRouteRecords(DiameterIdentity[] routeRecords) throws IllegalStateException;
}
