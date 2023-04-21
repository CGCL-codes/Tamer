package org.codecover.eclipse.views;

/**
 * This {@link RedundancyGraphNode} is the type of each node in Redundancy Graph Project supervisor: Vahid
 * Garousi (http://www.ucalgary.ca/~vgarousi/) Software Quality Engineering Research Group (SoftQual)
 * Department of Electrical and Computer Engineering Schulich School of Engineering University of Calgary,
 * Alberta, Canada http://www.softqual.ucalgary.ca
 *
 * @author Negar Koochakzadeh
 * @version 1.0
 */
public class RedundancyGraphNode {

    public Double Redundancy;

    public String type;

    public String TestName;

    public RedundancyGraphNode(String name, Double Red) {
        this.Redundancy = Red;
        this.TestName = name;
    }

    public String getLable() {
        if (this.Redundancy != 1000.0) {
            return this.TestName + "  " + this.Redundancy;
        } else {
            return this.TestName;
        }
    }
}
