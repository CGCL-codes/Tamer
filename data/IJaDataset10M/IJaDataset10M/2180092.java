package com.handcoded.meta;

/**
 * The <CODE>DTD</CODE> interface provides access to information that describes
 * an XML DTD based grammar.
 * 
 * @author 	BitWise
 * @version	$Id: DTD.java 70 2006-10-06 10:33:46Z andrew_jacobs $
 * @since	TFP 1.0
 */
public interface DTD extends Grammar {

    /**
	 * Returns the public identifier associated with this <CODE>DTD</CODE>
	 * instance.
	 * 
	 * @return 	The public identifier string.
	 * @since	TFP 1.0
	 */
    public String getPublicId();

    /**
	 * Returns the system identifier associated with this <CODE>DTD</CODE>
	 * instance.
	 * 
	 * @return 	The system identifier string.
	 * @since	TFP 1.0
	 */
    public String getSystemId();
}
