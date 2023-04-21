package net.wimpi.pim.factory;

import net.wimpi.pim.contact.io.ContactMarshaller;
import net.wimpi.pim.contact.io.ContactUnmarshaller;

/**
 * Interface for a <tt>ContactIOFactory</tt>, which
 * can be used to instantiate marshaller and unmarshaller
 * instances.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public interface ContactIOFactory {

    /**
   * Returns a new <tt>ContactMarshaller</tt> instance.
   *
   * @return the newly created <tt>ContactMarshaller</tt> instance.
   */
    public ContactMarshaller createContactMarshaller();

    /**
   * Returns a new <tt>ContactUnmarshaller</tt> instance.
   *
   * @return the newly created <tt>ContactUnmarshaller</tt> instance.
   */
    public ContactUnmarshaller createContactUnmarshaller();
}
