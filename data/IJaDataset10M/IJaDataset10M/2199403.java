package org.alcatel.jsce.object;

/**
 *  Description:
 * <p>
 * Represents an index of an osp object.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class ObjectIndex {

    /** The name*/
    private String name = null;

    /** Defines wheter the index is unique or not*/
    private boolean unicity = false;

    /** The list of string which constitutes the index*/
    private String[] attributes = new String[0];

    /** Defines wheter the index has to be build in the SMF side*/
    private boolean smf = true;

    /** Defines whether the index has to be build in te SLEE side*/
    private boolean slee = true;

    /** Defines wheter the index is the key*/
    private boolean key = false;

    /**
	 * 
	 */
    public ObjectIndex() {
    }

    /**
	 * @param attributes the list of fields	
	 * @param name the index name
	 * @param unicity the unicity of the index
	 */
    public ObjectIndex(String[] attributes, String name, boolean unicity) {
        this.attributes = attributes;
        this.name = name;
        this.unicity = unicity;
    }

    /**
	 * @return Returns the attributes.
	 */
    public String[] getAttributes() {
        return attributes;
    }

    /**
	 * @param attributes The attributes to set.
	 */
    public void setAttributes(String[] attributes) {
        this.attributes = attributes;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return Returns the unicity.
	 */
    public boolean isUnicity() {
        return unicity;
    }

    /**
	 * @param unicity The unicity to set.
	 */
    public void setUnicity(boolean unicity) {
        this.unicity = unicity;
    }

    /**
	 * @return Returns the slee.
	 */
    public boolean isSlee() {
        return slee;
    }

    /**
	 * @param slee The slee to set.
	 */
    public void setSlee(boolean slee) {
        this.slee = slee;
    }

    /**
	 * @return Returns the smf.
	 */
    public boolean isSmf() {
        return smf;
    }

    /**
	 * @param smf The smf to set.
	 */
    public void setSmf(boolean smf) {
        this.smf = smf;
    }

    public boolean isKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }
}
