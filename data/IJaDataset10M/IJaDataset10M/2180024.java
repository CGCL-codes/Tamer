package com.turnengine.client.global.game.command;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * The Get Game Definitions By Game Name.
 */
public class GetGameDefinitionsByGameName implements IGetGameDefinitionsByGameName {

    /** The command id. */
    public static final short COMMAND_ID = 324;

    /** The game name. */
    private String gameName;

    /**
	 * Creates a new Get Game Definitions By Game Name.
	 */
    public GetGameDefinitionsByGameName() {
    }

    /**
	 * Creates a new Get Game Definitions By Game Name.
	 * @param gameName the game name
	 */
    public GetGameDefinitionsByGameName(String gameName) {
        setGameName(gameName);
    }

    /**
	 * Creates a new Get Game Definitions By Game Name.
	 * @param iGetGameDefinitionsByGameName the i get game definitions by game name
	 */
    public GetGameDefinitionsByGameName(IGetGameDefinitionsByGameName iGetGameDefinitionsByGameName) {
        setGameName(iGetGameDefinitionsByGameName.getGameName());
    }

    /**
	 * Returns the game name.
	 * @return the game name.
	 */
    public String getGameName() {
        return gameName;
    }

    /**
	 * Sets the game name.
	 * @param gameName the game name to set.
	 */
    public void setGameName(String gameName) {
        if (gameName == null) {
            throw new NullPointerException("gameName");
        }
        this.gameName = gameName;
    }

    /**
	 * Returns the hash code.
	 * @return the hash code.
	 */
    @Override
    public int hashCode() {
        HashCodeBuilder hash = new HashCodeBuilder(163, 37);
        hash.append(gameName);
        return hash.toHashCode();
    }

    /**
	 * Returns true if this is equal to the given object.
	 * @param object the object to compare.
	 * @return true if this is equal to the given object.
	 */
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof GetGameDefinitionsByGameName) {
            GetGameDefinitionsByGameName compare = (GetGameDefinitionsByGameName) object;
            EqualsBuilder equals = new EqualsBuilder();
            equals.append(this.gameName, compare.gameName);
            return equals.isEquals();
        }
        return false;
    }

    /**
	 * Compare this to the given object.
	 * @param compare the object to compare to.
	 * @return the result of the comparison.
	 */
    @Override
    public int compareTo(IGetGameDefinitionsByGameName compare) {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(this.gameName, compare.getGameName());
        return builder.toComparison();
    }

    /**
	 * Returns this as a string.
	 * @return this as a string.
	 */
    @Override
    public String toString() {
        ToStringBuilder string = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        if (gameName != null) {
            string.append("gameName", gameName);
        }
        return string.toString();
    }

    public short getCommandId() {
        return COMMAND_ID;
    }
}
