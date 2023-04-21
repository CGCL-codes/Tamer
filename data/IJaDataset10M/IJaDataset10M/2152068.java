package de.schlund.pfixxml.targets;

/**
 * Abstract base implementation supplying methods common to all
 * types of dependencies
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 */
abstract class AbstractAuxDependency implements AuxDependency {

    protected DependencyType type;

    public DependencyType getType() {
        return this.type;
    }

    public int compareTo(AuxDependency o) {
        return getType().getTag().compareTo(o.getType().getTag());
    }
}
