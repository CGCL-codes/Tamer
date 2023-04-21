package org.xmi.infoset.ext;

import java.util.List;

/**
 * connecteable shape
 * @author e3a
 */
public interface ConnecteableShape extends Shape {

    /**
	 * add source relationship
	 * @param r
	 */
    public void addSourceRelationship(Connection r);

    /**
	 * add target relationship
	 * @param r
	 */
    public void addTargetRelationship(Connection r);

    /**
	 * get source relationship
	 * @return
	 */
    public List<Connection> getSourceRelationship();

    /**
	 * set source relationship
	 * @param sourceRelationship
	 */
    public void setSourceRelationship(List<Connection> sourceRelationship);

    /**
	 * get target relationship
	 * @return
	 */
    public List<Connection> getTargetRelationship();

    /**
	 * set target relationship
	 * @param targetRelationship
	 */
    public void setTargetRelationship(List<Connection> targetRelationship);
}
