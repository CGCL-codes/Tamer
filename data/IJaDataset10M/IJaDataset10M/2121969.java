package de.cue4net.eventservice.model.interfaces;

import de.cue4net.eventservice.model.shared.MonetaryAmount;

/**
 * @author Keino Uelze - cue4net
 * @version $Id: Buyable.java,v 1.2 2008-06-05 12:19:10 keino Exp $
 */
public interface Buyable {

    MonetaryAmount getMonetaryAmount();

    void setMonetaryAmount(MonetaryAmount monetaryAmount);
}
