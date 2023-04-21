package org.lds.wilmington.christiana.preparedness.data.listener;

import java.util.EventListener;

/**
 * 
 * @author Jay Askren
 *
 */
public interface FamilyAlteredListener extends EventListener {

    public void familyAltered(FamilyAlteredEvent event);
}
