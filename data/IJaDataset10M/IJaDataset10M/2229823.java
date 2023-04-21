package ru.nsu.ccfit.pm.econ.common.engine.data;

import javax.annotation.Nonnegative;
import ru.nsu.ccfit.pm.econ.common.engine.roles.IUPlayer;

/**
 * Unmodifiable interface for share holding data objects.
 * @author dragonfly
 */
public interface IUShareHolding {

    /**
	 * Identifier of a company which issued the share.
	 * @return Company identifier.
	 * @see IUCompany
	 */
    long getCompanyId();

    /**
	 * Number of shares in the holding.
	 * @return Number of shares.
	 */
    @Nonnegative
    int getAmount();

    /**
	 * Shares owner identifier.
	 * <p>Client requires this value to be valid only for share holdings 
	 * that belong to the player behind the client (i.e. the value should 
	 * be equal to the player id). In any other case any value other then 
	 * the player id should suffice for the client.</p>
	 * @return Identifier of the owner of this share holding.
	 * @see IUPlayer
	 */
    long getOwnerId();
}
