package org.imogene.web.gwt.common.entity;

/**
 * This interface describes the methods 
 * that an Imogene modeled business entity
 * shall have when it is georeferenced
 * @author Medes-IMPS
 */
public interface IsGeoreferenced {

    public Double getLatitude();

    public Double getLongitude();
}
