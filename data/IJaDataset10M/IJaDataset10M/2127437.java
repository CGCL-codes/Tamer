package com.simpledata.bc.datamodel;

/**
 * Like clone (but simple adaptation)
 */
public interface Copiable {

    /** return a copy of itslef (like clone) **/
    public Copiable copy();

    /**
	 * Interface for options that are copiable into another one<BR>
	 * BCoptions implementing this interface wil not return a new instance
	 * but fill an option with their value<BR>
	 * <B>This interface is used as TAGGING interface for merging 
	 * an option into another Tarif</B>
	 */
    public interface TransferableOption {

        /**
		 * @return true if this Copiable Option can be copied to this class
		 */
        public boolean canCopyValuesInto(Class destination);

        /** 
		 * copy all the data of this option into destination<BR>
		 * You can use canCopyValuesInto() to check if this is possible
		 **/
        public void copyValuesInto(BCOption destination);
    }
}
