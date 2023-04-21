package org.torweg.pulse.component.shop.checkout;

/**
 * An <code>IExtraChargeable</code> represents a type in the checkout that might
 * add some <code>ExtraCharge</code>.
 * 
 * @author Christian Schatt
 * @version $Revision: 1.2 $
 */
public interface IExtraChargeable {

    /**
	 * Returns the <code>ExtraCharge</code>.
	 * 
	 * @return the <code>ExtraCharge</code>
	 */
    ExtraCharge getExtraCharge();
}
