package com.turnengine.client.local.mobile.bean;

import com.javabi.codebuilder.generated.IGeneratedBean;

/**
 * The I Mobile.
 */
public interface IMobile extends IGeneratedBean, Comparable<IMobile> {

    /**
	 * Returns the id.
	 * @return the id.
	 */
    int getId();

    /**
	 * Returns the name.
	 * @return the name.
	 */
    String getName();

    /**
	 * Returns the player id.
	 * @return the player id.
	 */
    int getPlayerId();

    /**
	 * Returns the origin id.
	 * @return the origin id.
	 */
    int getOriginId();

    /**
	 * Returns the destination id.
	 * @return the destination id.
	 */
    int getDestinationId();

    /**
	 * Sets the id.
	 * @param id the id to set.
	 */
    void setId(int id);

    /**
	 * Sets the name.
	 * @param name the name to set.
	 */
    void setName(String name);

    /**
	 * Sets the player id.
	 * @param playerId the player id to set.
	 */
    void setPlayerId(int playerId);

    /**
	 * Sets the origin id.
	 * @param originId the origin id to set.
	 */
    void setOriginId(int originId);

    /**
	 * Sets the destination id.
	 * @param destinationId the destination id to set.
	 */
    void setDestinationId(int destinationId);
}
