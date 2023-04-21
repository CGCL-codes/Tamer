package org.mantikhor.llapi;

/**
 * 
 * NOT EXPECTED TO BE IMPLEMENTED -- FOR EXTENSION ONLY
 * 
 * @author Bill Blondeau
 *
 */
public interface Reifiable {

    /**
     * If there is a need to assign properties to an item
     * that has no independent existence (best example: the
     * instance of a PropertyDefinition), the Properties are assigned to an
     * anonymous <em>reification node</em>, or "reefnode". 
     * This method provides an accessor for the reefnode; because
     * a reefnode is always anonymous, we only care about its
     * poperty-containing characteristics, so represent it as a
     * <code>Burst</code>.
     * 
     * Largely in the interest of performance, a <code>null</code>
     * value is allowed to represent the state of the reefnode being 
     * an empty <code>Burst</code>.
     * 
     * @return <code>null</code> if no reefnode has been created;
     *      otherwise the <code>ResourceNode</code> (whether empty or not)
     *      that contains the instance's Properties.
     */
    public abstract Burst getReefNode();

    /**
     * Convenience method.
     * @return <code>false</code> if a call to <code>this.getReefNode()
     *      would return either:
     *      <ul>
     *          <li>
     *              <code>null</code>, or
     *          </li>
     *          <li>
     *              a <code>Burst</code> for which <code>isEmpty() 
     *              == true</code>;
     *          </li>
     *      </ul>
     *      otherwise, this method returns <code>true</code>.
     */
    public abstract boolean hasReefProperties();
}
