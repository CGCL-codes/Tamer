package com.ideo.sweetdevria.taglib.grid.common;

/**
 * This class represents the method to implement for every tag evaluating the content of a column 
 * These method must be copied to the implementing class, including the comments.
 *
 * @author Julien Maupoux
 *
 */
public interface IColumnContentTag {

    public Comparable getObject();

    /**
	 * Set a java object as content
	 * @param object	a java object as content
     * @jsp.attribute required="false"
     * rtexprvalue="true"
     * type="java.lang.Comparable"
     * description="Set a java object as content. Permit to use all its advantages like compare(), toString()."
	 */
    public void setObject(Comparable object);

    public String getKey();

    /**
	 * Set a string as content. This attribut permits to internationalize easily
	 * @param key	a string as content
     * @jsp.attribute required="false"
     * rtexprvalue="true"
     * type="java.lang.String"
     * description="Set a string as content. This attribute permits to internationalize easily."
	 */
    public void setKey(String key);
}
