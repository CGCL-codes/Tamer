package es.caib.zonaper.persistence.delegate;

import es.caib.zonaper.persistence.delegate.DelegateFactory;
import es.caib.zonaper.persistence.delegate.PadDelegate;

/**
 * Define m�todos est�ticos para obtener delegates del interfaz de la PAD.
 */
public class DelegatePADUtil {

    private DelegatePADUtil() {
    }

    public static PadDelegate getPadDelegate() {
        return (PadDelegate) DelegateFactory.getDelegate(PadDelegate.class);
    }
}
