package org.torweg.pulse.component.shop.payment;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Christian Schatt
 * @version $Revision$
 */
public class IPaymentResponseXmlAdapter extends XmlAdapter<Object, IPaymentResponse> {

    /**
	 * Marshals the given <code>IPaymentResponse</code> to an
	 * <code>Object</code>.
	 * 
	 * @param pay
	 *            the <code>IPaymentResponse</code> to marshal
	 * @return the marshaled <code>Object</code>
	 */
    @Override
    public final Object marshal(final IPaymentResponse pay) {
        return pay;
    }

    /**
	 * Unmarshals an <code>IPaymentResponse</code> from the given
	 * <code>Object</code>.
	 * 
	 * @param obj
	 *            the <code>Object</code> to unmarshal
	 * @return the unmarshaled <code>IPaymentResponse</code>
	 */
    @Override
    public final IPaymentResponse unmarshal(final Object obj) {
        return (IPaymentResponse) obj;
    }
}
