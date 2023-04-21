package engine.relativeNodeAddress;

import java.io.Serializable;

/**
 * Field Class implements a container for a directional bind,
 * relative offset, and String alias for this Field.
 * One or more Field objects are encapsulated in an Address.
 * @author catheyc
 * @see RelativeNodeAddress
 */
public class RelativeNodeField implements Serializable {

    private int bindArg;

    private int offsetArg;

    private String fieldAlias;

    /**
	 * Field Constructor
	 * @param bindArg	The int Bind Argument, an extensible sign bit on the offset
	 * @param offsetArg	The int Offset Argument, how many hops to make on this dimension
	 * @param fieldAlias	The String Alias for this field such as "Y" or "X"
	 */
    public RelativeNodeField(int bindArg, int offsetArg, String fieldAlias) {
        this.bindArg = bindArg;
        this.offsetArg = offsetArg;
        this.fieldAlias = fieldAlias;
    }

    /**
	 * getBindArg returns the bind argument of this Field.
	 * @return bindArg	The int bind argument of this Field.
	 */
    public int getBindArg() {
        return this.bindArg;
    }

    /**
	 * getOffsetArg returns the offset argument of this Field.
	 * @return offsetArg	The int offset argument of this Field.
	 */
    public int getOffsetArg() {
        return this.offsetArg;
    }

    /**
	 * getAlias returns the String Alias of this Field.
	 * @return fieldAlias	The String Alias of this Field.
	 */
    public String getAlias() {
        return this.fieldAlias;
    }

    /**
	 * decOffsetArg For minimal routing we need to only decrement offsets
	 * @return offsetArg	The resulting decremented offset argument
	 */
    public int decOffsetArg() {
        if (this.offsetArg > 0) this.offsetArg = this.offsetArg - 1;
        return this.offsetArg;
    }

    /**
	 * incOffsetArg For non-minimal routing we need to increment offsets
	 * @return offsetArg	The resulting incremented offset argument
	 */
    public int incOffsetArg() {
        this.offsetArg = this.offsetArg + 1;
        return this.offsetArg;
    }

    /**
	 * setBindArg allows the bindArg to be changed, good for U-turns
	 * @param bindArg	int that defines the new bindArg of the Field.
	 */
    public void setBindArg(int bindArg) {
        this.bindArg = bindArg;
    }

    /** 
	 * setOffsetArg allows the offsetArg to be changed.
	 * Used for Network Address Translation
	 * @param offsetArg	int that defines the new offsetArg of the Field.
	 */
    public void setOffsetArg(int offsetArg) {
        this.offsetArg = offsetArg;
    }

    /**
	 * setFieldAlias allows the alias to be changed.
	 * Used for Network Address Translation.
	 * @param fieldAlias	int that defines the new alias of the Field.
	 */
    public void setFieldAlias(String fieldAlias) {
        this.fieldAlias = fieldAlias;
    }

    /**
	 * copy creates a copy of the field's instance variables in a new object
	 * @return field copy of this Field Object
	 */
    public RelativeNodeField copy() {
        return new RelativeNodeField(bindArg, offsetArg, fieldAlias);
    }
}
